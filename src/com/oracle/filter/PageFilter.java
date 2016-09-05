package com.oracle.filter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.hadoop.fs.Path;


public class PageFilter implements Filter{
    
	 private Set<String> paths=new HashSet<String>();
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
	    HttpServletRequest request=(HttpServletRequest) req;
	    HttpServletResponse response=(HttpServletResponse) resp;
	    String path=request.getServletPath();
	    String username=(String) request.getSession().getAttribute("username");
	    if(username!=null){
	    	  chain.doFilter(request, response);
	    }else {
			if(path.startsWith("/static")||paths.contains(path)){
				  chain.doFilter(request,response);
			}
			else {
				response.sendRedirect("login.jsp");
			}
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		paths.add("/login.jsp");
		paths.add("/loginUserAction");
		paths.add("/registUserAction");
	}

}
