package com.zc.bookstore.admin.admin.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ModelDriven;
import com.zc.bookstore.admin.admin.domain.Admin;
import com.zc.bookstore.admin.admin.service.AdminService;
import com.zc.bookstore.baseaction.BaseAction;

public class AdminAction extends BaseAction implements ServletRequestAware, ServletResponseAware, ModelDriven<Admin>{
	
	private AdminService adminService;
	private Admin admin;
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}
	
	public AdminService getAdminService() {
		return adminService;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}
	
	public Admin getAdmin() {
		return admin;
	}

	/**
	 * 管理员登录
	 * @return
	 */
	public String login(){
		Admin admin = adminService.login(this.admin.getAdminname(), this.admin.getAdminpwd());
		if(admin == null){
			request.setAttribute("msg", "用户名或密码错误！");
			request.setAttribute("adminname", this.admin.getAdminname());
			return "error";
		}else{
			request.getSession().setAttribute("admin", this.admin);
			return "success";
		}
	}
	
	/**
	 * 退出
	 * @return
	 */
	public String quit(){
		request.getSession().removeAttribute("admin");
		return "success";
		
	}
	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	@Override
	public Admin getModel() {
		if(admin == null){
			admin = new Admin();
		}
		return admin;
	}

}
