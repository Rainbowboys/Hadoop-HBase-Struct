package com.oracle.action;

import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.web.jsf.FacesContextUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.alibaba.fastjson.JSON;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.oracle.Entity.User;
import com.oracle.base.BaseAction;

import com.oracle.util.Constants;
import com.oracle.util.DateUtil;
import com.oracle.util.Json;
import com.oracle.util.ManyUtil;
import com.oracle.vo.FileSystemvo;

public class FileAction extends BaseAction {

	/**
	 * ��ѯ�û���ĳ��Ŀ¼�µ������ļ�
	 * 
	 * @return
	 * @throws Exception
	 * 
	 */
	private String mkdir;
	private File file;
	private String fileFileName;
	private String dir;
	private String ids;
	private String dirName;

	public String list() throws Exception {

		String dir = request.getParameter("dir");

		if (dir == null) {
			// ��ѯ�û���Ŀ¼�µ����� / username
			// user.getUsername()
			dir = Constants.ROOT
					+ session.getAttribute(Constants.SESSION_USER_NAME);
		}
		List<FileSystemvo> list = fd.queryAllbyDir(dir);
		ActionContext.getContext().put("dir", dir);
		ActionContext.getContext().put("files", list);
		return "list";
	}

	/**
	 * �ϴ��ļ�
	 * 
	 * @return
	 * @throws Exception
	 */
	public String upload() throws Exception {
		// String dir=request.getParameter("dir");
		fd.upload(file, dir, fileFileName);
		return "upload";
	}

	/**
	 * �½��ļ���
	 */
	public Json mkdir() {
		Json json = new Json();

		try {
			if (!ManyUtil.isEmpty(mkdir)) {
				// ��ֵ��Ч
				json.setMsg("Ŀ¼Ϊ����Ч");

			} else {
				if (session.getAttribute(Constants.SESSION_USER_NAME) == null) {
					// �û��Ѿ�ע�� �����Դ���
					json.setMsg("�û��Ѿ�ע�������ɴ���Ŀ¼");

				}
				// String dir = request.getParameter("dir");
				// String dirName = request.getParameter("dirName");
				if (ManyUtil.isEmpty(dirName)) {
					dir = dirName;
				} else {
					dir = Constants.ROOT
							+ session.getAttribute(Constants.SESSION_USER_NAME);
				}
				String newmkdir=dir+"/"+mkdir;
				if (!fd.dirIsexists(newmkdir)) {
					fd.mkdir(newmkdir);
					json.setSuccess(true);
					json.setMsg("�����ɹ�");
					FileSystemvo fSystemvo = new FileSystemvo();
					fSystemvo.setDate(DateUtil.DateToString("yyyy-MM-dd HH:mm",
							new Date()));
					fSystemvo.setName(mkdir);
					fSystemvo.setType(Constants.FILE_TYPE_DIR);
				} else {
					json.setMsg("Ŀ¼�Ѿ�����");
				}

			}

			
			response.setCharacterEncoding("UTF-8");
			PrintWriter out=response.getWriter();
			out.write(JSON.toJSONString(json));
			//out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			json.setMsg("����ʧ��");
		}
		return null;

	}

	/**
	 * ɾ���ļ���Ŀ¼
	 * 
	 * @return
	 */
	public Json delete() throws Exception {

		// String idS=request.getParameter("ids");
		Json json = new Json();
		String ds[] = ids.split(",");
		try {
			for (String dsn : ds) {
				fd.delete(dir + "/" + dsn);
			}
			json.setSuccess(true);
			json.setMsg("ɾ���ɹ�");

		} catch (Exception e) {
			e.printStackTrace();
			json.setMsg("ɾ��ʧ��");
		}
		return json;

	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getMkdir() {
		return mkdir;
	}

	public String getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public void setMkdir(String mkdir) {
		this.mkdir = mkdir;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getDirName() {
		return dirName;
	}

	public void setDirName(String dirName) {
		this.dirName = dirName;
	}

}
