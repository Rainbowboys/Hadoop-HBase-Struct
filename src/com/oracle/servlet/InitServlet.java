package com.oracle.servlet;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.oracle.util.Constants;

public class InitServlet extends HttpServlet {
	@Override
	public void init() throws ServletException {
		// 读取properties配置文件
		String path = null;
//		try {
//			Properties properties = new Properties();
//			properties.load(InitServlet.class.getResourceAsStream("conf.properties"));
		  
//			
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
		try {
			FileInputStream fis;
			path = InitServlet.class.getResource("/").getPath().toString()
					.trim();

			fis = new FileInputStream(path + "conf.properties");
			Properties properties = new Properties();
			properties.load(fis);
			fis.close();

			// 覆盖Constant里的相关属性值
			String zk_host = properties.getProperty("zk_host");
			// System.out.println("zk_host:"+zk_host);
			if (zk_host != null) {
				Constants.SERVICE_HOST = zk_host;
			}
			String hbase_dir = properties.getProperty("hbase_dir");
			// System.out.println("hbase_dir"+hbase_dir);
			if (hbase_dir != null) {
				Constants.SERVICE_HBASE_DIR = hbase_dir;
			}
			String hdfs_dir = properties.getProperty("hdfs");
			// System.out.println("hbase_dir"+hbase_dir);
			if (hdfs_dir != null) {
				Constants.SERVICE_HDFS_DIR = hdfs_dir;
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
