package com.oracle.base;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.oracle.db.HbaseDB;
import com.oracle.db.HdfsDB;

public class BaseAction extends ActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static HttpServletRequest request = ServletActionContext.getRequest();
	public static HttpServletResponse response = ServletActionContext.getResponse();
	public static HttpSession session=request.getSession();
	public HbaseDB db = HbaseDB.getInstance();
	public HdfsDB fd = HdfsDB.getInstance();
}
