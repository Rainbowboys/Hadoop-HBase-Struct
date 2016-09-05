package com.oracle.db;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

import com.oracle.base.BaseAction;
import com.oracle.util.Constants;
import com.sun.research.ws.wadl.Request;

/**
 * 
 * @author zhangchengrui 2016/07/04
 * 
 */
public class HbaseDB extends BaseAction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7137236230164276653L;
	static Connection connection = null;

	private static class HbaseDBInstance {
		private static final HbaseDB instance = new HbaseDB();
	}

	public static HbaseDB getInstance() {
		return HbaseDBInstance.instance;
	}

	private Object readResolve() {
		return getInstance();
	}

	/**
	 * ���캯��
	 */
	private HbaseDB() {
		Configuration conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", Constants.SERVICE_HOST);
		conf.set("hbase.rootdir", Constants.SERVICE_HBASE_DIR);
		try {
			// ��������
			connection = ConnectionFactory.createConnection(conf);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * ����username����user_id�� ����id
	 */
	public static long getIdByUsername(String name) {
		long id = 0;
		try {
			Table table = connection.getTable(TableName.valueOf("user_id"));
			System.out.println(name);
			Get get = new Get(Bytes.toBytes(name));

			get.addColumn(Bytes.toBytes("id"), Bytes.toBytes("id"));
			Result result = table.get(get);
			System.out.println(result);

			
			List<Cell> cell = result.getColumnCells(Bytes.toBytes("id"),
					Bytes.toBytes("id"));
			if(cell!=null&&cell.size()>0&&cell.get(0)!=null){
				byte[] bytes = CellUtil.cloneValue(cell.get(0));
				id = Bytes.toLong(bytes);
			}
			table.close();

		} catch (Exception e) {
			e.printStackTrace();
			return id;
		}

		return id;
	}

	/**
	 * ����id����id_user�� ����pwd
	 */
	public static String getPwdById(long id) {
		String pwd = null;
		try {
			Table table = connection.getTable(TableName.valueOf("id_user"));
			Get get = new Get(Bytes.toBytes(id));
			get.addColumn(Bytes.toBytes("user"), Bytes.toBytes("pwd"));
			Result result = table.get(get);
			List<Cell> cell = result.getColumnCells(Bytes.toBytes("user"),
					Bytes.toBytes("pwd"));
			byte[] bytes = CellUtil.cloneValue(cell.get(0));
			pwd = Bytes.toString(bytes);

			table.close();

		} catch (Exception e) {
			e.printStackTrace();
			return pwd;
		}
		return pwd;
	}

	/**
	 * ����ID�����û�����
	 */
	public static String getUsernameById(Long id) {
		String name = null;
		try {
			Table table = connection.getTable(TableName.valueOf("id_user"));
			Get get = new Get(Bytes.toBytes(id));
			Result result = table.get(get);
			List<Cell> cell = result.getColumnCells(Bytes.toBytes("user"),
					Bytes.toBytes("name"));
			byte[] bytes = CellUtil.cloneValue(cell.get(0));
			name = Bytes.toString(bytes);
			table.close();
		

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return name;
		}
		return name;
	}

	/**
	 * ��� �û����Ƿ����
	 */
	public static boolean CheckUsername(String name) {

		try {
			Table table = connection.getTable(TableName.valueOf("user_id"));
			Get get = new Get(Bytes.toBytes(name));

			if (table.exists(get)) {
				table.close();
				return true;
			} else {
				table.close();
				return false;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * �����û����������ȷ��
	 */
	public long chkuser(String username, String password) throws Exception {
		System.out.println("begin Check");
		long id=0;
	     id = getIdByUsername(username);
		System.out.println(username);
		System.out.println(id);
		if (id == 0) {
			//���û�������
			session.setAttribute("result", "0");
			return id;
		}
		else if (password.equals(getPwdById(id))) {
			return id;
		} else {
			//�������
			session.setAttribute("result", "1");
			id=0;
			return id;
		}

	}

	/**
	 * ��ȡ�û����id
	 */
	public static long getGid(String row) {
		long gid = 0;
		try {
			Table table = connection.getTable(TableName.valueOf("gid"));

			gid = table.incrementColumnValue(Bytes.toBytes(row),
					Bytes.toBytes("gid"), Bytes.toBytes(row), 1);
			table.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return gid;
		}
		System.out.println(gid);
		return gid;
	}

	/**
	 * ���add �� user_id�����ݣ�
	 * 
	 * @param tabelName
	 * @param rowkey
	 * @param family
	 * @param quali
	 * @param value
	 * 
	 */
	public void add(String tabelName, String rowkey, String family,
			String quali, long value) {
		try {
			Table table = connection.getTable(TableName.valueOf(tabelName));
			Put put = new Put(Bytes.toBytes(rowkey));
			put.addColumn(Bytes.toBytes(family), Bytes.toBytes(quali),
					Bytes.toBytes(value));
			table.put(put);
			table.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * ����id_user �������
	 * 
	 * @param tabelName
	 * @param rowkey
	 * @param family
	 * @param quali
	 * @param value
	 * @throws Exception
	 */
	public void addUser(String tabelName, long rowkey, String family,
			String quali, String value) throws Exception {
		Table table = connection.getTable(TableName.valueOf(tabelName));
		Put put = new Put(Bytes.toBytes(rowkey));
		put.addColumn(Bytes.toBytes(family), Bytes.toBytes(quali),
				Bytes.toBytes(value));
		table.put(put);
		table.close();
	}
	/**
	 * ��ӱ� 
	 * @param tableName  
	 * @param family  ��������
	 * @param version  ���汾
	 * @throws Exception
	 */
	public static void createTable(String tableName,String family[],int version) throws Exception{
		try {
			Admin admin=connection.getAdmin();
			TableName tablename=TableName.valueOf(tableName);
			//���������ɾ����
			if(admin.tableExists(tablename)){
				admin.disableTable(tablename);
				admin.deleteTable(tablename);
			}else{
				HTableDescriptor decp=new HTableDescriptor(tablename);
				HColumnDescriptor hd=null;
				for (int i = 0; i < family.length; i++) {
					hd=new HColumnDescriptor(Bytes.toBytes(family[i]));
					hd.setMaxVersions(version);
				    decp.addFamily(hd);
				    admin.createTable(decp);
				}   
			}
			admin.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	/**
	 * ����Rowkeyɾ������
	 * @param tableName
	 * @param rowkey[]����
	 * @throws Exception
	 */
	public static void deleteRow(String tableName,String []rowkey) throws Exception{
		Table table=connection.getTable(TableName.valueOf(tableName));
		List<Delete> deletes=new ArrayList<Delete>();
		for (int i = 0; i < rowkey.length; i++) {
			Delete delete=new Delete(Bytes.toBytes(rowkey[i]));
			deletes.add(delete);
		}
		table.delete(deletes);
		table.close();
	}
	/**
	 * ɾ������ĳһ��
	 * @param tableName
	 * @param family
	 * @param rowkey1
	 * @param qulifier
	 * @throws Exception
	 */
	
	public static void deleteColums(String tableName,String family,Long rowkey1,String qulifier)throws Exception{
		Table table=connection.getTable(TableName.valueOf(tableName));
		Delete delete=new Delete(Bytes.toBytes(rowkey1));
		delete.addColumn(Bytes.toBytes(family), Bytes.toBytes(qulifier));
		table.delete(delete);
		table.close();
		
	}
	public void CheckEmail(){
		
	}

}
