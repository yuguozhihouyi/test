package com.zc.bookstore.admin.admin.service;

import com.zc.bookstore.admin.admin.dao.AdminDao;
import com.zc.bookstore.admin.admin.domain.Admin;

public class AdminService {
	private AdminDao adminDao;
	
	public void setAdminDao(AdminDao adminDao) {
		this.adminDao = adminDao;
	}
	
	public AdminDao getAdminDao() {
		return adminDao;
	}
	
	/**
	 *  管理员登录
	 * @param adminname
	 * @param adminpwd
	 * @return
	 */
	public Admin login(String adminname, String adminpwd){
		return adminDao.findAdminByAdminnameAndAdminpwd(adminname, adminpwd);
	}
}
