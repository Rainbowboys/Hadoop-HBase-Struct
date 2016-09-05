package com.oracle.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.hadoop.fs.Hdfs;
import org.apache.hadoop.fs.permission.FsAction;
import org.apache.http.HttpRequest;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.oracle.Entity.User;
import com.oracle.base.BaseAction;
import com.oracle.db.HbaseDB;
import com.oracle.db.HdfsDB;
import com.oracle.util.Constants;
import com.oracle.util.ManyUtil;

public class UserAction extends BaseAction implements ModelDriven<User> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private User user = new User();
	/**
	 * 用户登录
	 * @return
	 * @throws Exception
	 */
	public String login() throws Exception {
	
		System.out.println(user.getUsername());
		System.out.println(user.getPassword());
		long id = db.chkuser(user.getUsername(), user.getPassword());
		System.out.println(id);
		if (id > 0) {
			 session.setAttribute(Constants.SESSION_USER_NAME, HbaseDB.getUsernameById(id));
			 System.out.println(session.getAttribute(Constants.SESSION_USER_NAME));
			return "loginSuccess";
		}else {
			ActionContext.getContext().put("loginerror", "1");
			System.out.println(ActionContext.getContext().get("loginerror")+"结果");
			
			//session.setAttribute("loginerror", "1");
			return "loginError";
		}
		
	}
	/**
	 * 用户注册
	 * @return
	 * @throws Exception
	 */

	public String  regist() throws Exception {

		System.out.println("begin regist" + user.getUsername()
				+ user.getPassword() + user.getEmail());
		if (!HbaseDB.CheckUsername(user.getUsername())) {
			long id = HbaseDB.getGid("gid");
			if (ManyUtil.isEmpty(user.getUsername())
					&& ManyUtil.isEmpty(user.getPassword())
					&& ManyUtil.isEmpty(user.getEmail())) {
				db.add(Constants.TABLE_USER_ID, user.getUsername(), "id", "id",
						id);
				db.addUser(Constants.TABEL_ID_USER, id, "user", "pwd",
						user.getPassword());
				db.addUser(Constants.TABEL_ID_USER, id, "user", "name",
						user.getUsername());
				db.addUser(Constants.TABEL_ID_USER, id, "user", "email",
						user.getEmail());
				fd.mkdir("/"+user.getUsername());
				return "registSuccess";
			}
		} else {
			ActionContext.getContext().put("result", "3");
		}
		return "registError";

	}

	public String  logout() throws Exception{
		//清空会话内容
		session.removeAttribute("username");
	
		return "logout";

	}

	@Override
	public User getModel() {
		// TODO Auto-generated method stub
		return user;
	}
}
