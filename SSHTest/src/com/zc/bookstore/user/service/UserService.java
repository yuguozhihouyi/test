package com.zc.bookstore.user.service;

import java.io.IOException;
import java.util.Properties;

import com.zc.bookstore.user.dao.UserDao;
import com.zc.bookstore.user.domain.User;
import com.zc.bookstore.utils.CommonUtil;

public class UserService {

	private UserDao userDao;
	
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	public UserDao getUserDao() {
		return userDao;
	}
	
	/**
	 * 校验指定登录名的会员是否存在
	 * @param loginname
	 * @return
	 */
	public boolean validateLoginname(String loginName) {
		return userDao.validateLoginName(loginName);
	}
	
	/**
	 * 校验指定Email的会员是否存在
	 * @param loginname
	 * @return
	 */
	public boolean validateEmail(String email) {
		return userDao.validateEmail(email);
	}
	
	/**
	 * 注册功能
	 * @param user
	 */
	public void regist(final User user) {
		//1. 对user进行数据补全
		user.setActivationCode(CommonUtil.uuid() + CommonUtil.uuid());
		user.setStatus(false);
		//2. 向数据库添加记录
		userDao.addUser(user);
		//3. 向用户注册邮箱地址发送“激活”邮件
		new Thread(new Runnable() {
			public void run() {
				CommonUtil.sendMail(user);
			}
		}).start();
		
	}
	
	/**
	 * 激活功能
	 * @param activationCode
	 * @throws UserException
	 */
	public void activation(String activationCode) throws UserException {
			//1. 通过激活码查询用户
			User user = userDao.findByActivationCode(activationCode);
			//2. 如果没有查询到用户，说明激活码无效
			if(user == null) throw new UserException("无效的激活码！");
			//3. 如果用户的状态已经是激活状态，那么说明用户二次激活
			if(user.isStatus()) throw new UserException("您已经激活了，不要二次激活！");
			//4. 修改用户状态为true，即激活状态。
			userDao.updateUserStatus(user.getUid(), true);
	}

	/**
	 * 登录功能
	 * @param formBean
	 * @return
	 */
	public User login(User formBean) {
			return userDao.findByUserNameAndUserPassword(
					formBean.getLoginname(), formBean.getLoginpass());
	}

	/**
	 * 修改密码
	 * @param uid
	 * @param newpass
	 */
	public void updatePassword(String uid, String newpass) {
			userDao.updatePassword(uid, newpass);
	}
}
