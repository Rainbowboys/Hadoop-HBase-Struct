package com.oracle.db;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Progressable;

import com.oracle.util.Constants;
import com.oracle.util.DateUtil;
import com.oracle.vo.FileSystemvo;

/**
 * 
 * @author Rainbow 2016/07/05 ��Ҫ����ʵ��HDFS�ļ�ϵͳ�ϵ���ɾ��� ���µȲ���
 * 
 */
public class HdfsDB {
	private static Set<String> suf = new HashSet<String>();
	static {
		suf.add("csv");
		suf.add("txt");
		suf.add("doc");
		suf.add("xls");
		suf.add("xlsx");
		suf.add("ppt");
		suf.add("pptx");
	}
	Configuration conf = null;
	public static FileSystem fs = null;

	private static class HdfsDBInstance {
		private static final HdfsDB instance = new HdfsDB();
	}

	public static HdfsDB getInstance() {
		return HdfsDBInstance.instance;
	}

	private HdfsDB() {

		conf = new Configuration();
		try {

			// URI uri = new URI(Constants.SERVICE_HDFS_DIR);
			URI uri = new URI("hdfs://hadoop:9000");
			fs = FileSystem.get(uri, conf);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
 /**
  * �ж�Ŀ¼�Ƿ����
  */
	public boolean dirIsexists(String dir) {
		// TODO Auto-generated method stub
		boolean flag=false;
		try {
			flag=fs.exists(new Path(dir));
			return flag;
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * ��ָ����·���´����ļ��� ��ģ���Ѳ���
	 * 
	 * @param dir
	 */
	public void mkdir(String dir) throws Exception {
		if (!fs.exists(new Path(dir))) {
			boolean flag = fs.mkdirs(new Path(dir));
			System.out.println(flag ? "�����ɹ�" : "����ʧ��");
		}
	}

	/**
	 * ɾ��ָ��·���ļ����ļ��� ��ģ���Ѳ���
	 * 
	 * @param dir
	 * @throws Exception
	 */
	public void delete(String dir) throws Exception {
		fs.delete(new Path(dir), false);
	}

	/**
	 * �ϴ��ļ�
	 * 
	 * @param src
	 * @param dst
	 * @throws Exception
	 */
	public void upload(File file, String dir, String filename) throws Exception {
		String filePath = dir + "/" + filename;
		fs.create(new Path(filePath));
		fs.copyFromLocalFile(true, true, new Path(file.getPath()), new Path(
				filePath));
		System.out.println("UP����OK");
	}

	/**
	 * ������
	 * 
	 * @param src
	 * @param dst
	 * @throws Exception
	 */
	public void rename(String src, String dst) throws Exception {
		fs.rename(new Path(src), new Path(dst));
	}

	/**
	 * �����ļ�
	 * 
	 * @param dst
	 * @param src
	 * @throws Exception
	 */
	public static void download(String dst, String src) throws Exception {

	}

	/**
	 * ���ҵ�ǰ�û���Ŀ¼�µ������ļ��Լ�Ŀ¼�µ��ļ�
	 * 
	 * @param dir
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("null")
	public List<FileSystemvo> queryAllbyDir(String dir) throws Exception {
		List<FileSystemvo> fileSystemvos = new ArrayList<FileSystemvo>();
		FileStatus[] fileStatus = fs.listStatus(new Path(dir));
		FileSystemvo filevo = null;
		for (FileStatus fileStatus2 : fileStatus) {
			if (fileStatus2.isDirectory()) {
				// Ŀ¼
				// Ŀ¼�޸�ʱ��
				filevo = new FileSystemvo();
				filevo.setDate(DateUtil.longToString("yyyy-MM-dd HH:mm:ss",
						fileStatus2.getModificationTime()));
				filevo.setDir(fileStatus2.getPath().toString());
				filevo.setName(fileStatus2.getPath().getName());
				// ��Ŀ¼
				filevo.setPdir(fileStatus2.getPath().getParent().toString());
				filevo.setType(Constants.FILE_TYPE_DIR);
			} else if (fileStatus2.isFile()) {
				filevo = new FileSystemvo();
				filevo.setDate(DateUtil.longToString("yyyy-MM-dd HH:mm:ss",
						fileStatus2.getModificationTime()));
				filevo.setDir(fileStatus2.getPath().toString());
				filevo.setName(fileStatus2.getPath().getName());
				// ��Ŀ¼
				filevo.setPdir(fileStatus2.getPath().getParent().toString());
				filevo.setType(Constants.FILE_TYPE_FILE);
				filevo.setSize(fileStatus2.getLen() + "");
				String ext = filevo.getName().substring(
						filevo.getName().lastIndexOf(".") + 1,
						filevo.getName().length());
				filevo.setViewflag(suf.contains(ext) ? "true" : "false");
			}
			fileSystemvos.add(filevo);
		}

		return fileSystemvos;
	}

	/**
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		HdfsDB fd = new HdfsDB();
		fd.delete("/xiaozhang/ssssss55");
		// fd.queryAllbyDir("/data");
	}


}
