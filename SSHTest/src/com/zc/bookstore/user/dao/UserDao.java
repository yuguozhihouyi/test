package com.zc.bookstore.user.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.zc.bookstore.basedao.BaseDao;
import com.zc.bookstore.user.domain.User;

public class UserDao extends BaseDao{

	/**
	 * 校验指定登录名是否存在
	 * @param loginName
	 * @return
	 */
	public boolean validateLoginName(String loginName){
		String SQL = "SELECT COUNT(*) FROM t_user WHERE loginname = :loginName";
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		Number cnt = (Number)session.createSQLQuery(SQL)
				.setString("loginName", loginName)
				.uniqueResult();
		tx.commit();
		session.close();
		return cnt.intValue() == 0 ? false : true;
	}
	
	/**
	 * 校验指定邮箱的用户是否存在
	 * @param email
	 * @return
	 */
	public boolean validateEmail(String email){
		String SQL = "SELECT COUNT(*) FROM t_user WHERE email = :email";
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		Number cnt = (Number)session.createSQLQuery(SQL)
				.setString("email", email)
				.uniqueResult();
		tx.commit();
		session.close();
		return cnt.intValue() == 0 ? false : true;
	}
	
	/**
	 * 添加用户
	 * @param user
	 */
	public void addUser(User user){
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		session.save(user);
		tx.commit();
		session.close();
	}
	
	/**
	 * 通过激活码查询用户
	 * @param activationCode
	 * @return
	 */
	public User findByActivationCode(String activationCode){
		String SQL = "SELECT * FROM t_user WHERE activationCode = :activationCode";
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		List users = session.createSQLQuery(SQL)
				.addEntity(User.class)
				.setString("activationCode", activationCode)
				.list();
		tx.commit();
		session.close();
		return (User)(users.get(0));
	}
	
	/**
	 * 通过uid修改用户状态
	 * @param uid
	 * @param status
	 */
	public void updateUserStatus(String uid, boolean status){
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		User user = (User)session.load(User.class, uid);
		user.setStatus(status);
		session.update(user);
		tx.commit();
		session.close();
	}
	
	/**
	 * 用户名和密码查询用户
	 * @param loginName
	 * @param loginPass
	 * @return
	 */
	public User findByUserNameAndUserPassword(String loginName, String loginPass){
		String SQL = "SELECT * FROM t_user WHERE loginname = :loginName AND loginpass = :loginPass";
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		List users = session.createSQLQuery(SQL)
				.addEntity(User.class)
				.setString("loginName", loginName)
				.setString("loginPass", loginPass)
				.list();
		if(users.size() == 0){
			return null;
		}
		return (User)(users.get(0));
	}
	
	/**
	 * 修改用户密码
	 * @param uid
	 * @param newPassword
	 */
	public void updatePassword(String uid, String newPassword){
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		User user = (User)session.load(User.class, uid);
		user.setLoginpass(newPassword);
		session.update(user);
		tx.commit();
		session.close();
	}
}
