package com.zc.bookstore.admin.admin.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.zc.bookstore.admin.admin.domain.Admin;
import com.zc.bookstore.basedao.BaseDao;

public class AdminDao extends BaseDao{

	/**
	 * 根据用户名和密码获取用户实体信息
	 * @param adminname
	 * @param adminpwd
	 * @return Admin对象
	 */
	public Admin findAdminByAdminnameAndAdminpwd(String adminname, String adminpwd){
		Session session = getSession();
		String sql = "SELECT * FROM t_admin WHERE adminname = :adminname AND adminpwd = :adminpwd";
		Transaction tx = session.beginTransaction();
		List admins = session.createSQLQuery(sql)
				.addEntity(Admin.class)
				.setString("adminname", adminname)
				.setString("adminpwd", adminpwd)
				.list();
		tx.commit();
		session.close();
		if(admins.size() == 0){
			return null;
		}
		Admin adm = (Admin)admins.get(0);
		return adm;
	}
}
