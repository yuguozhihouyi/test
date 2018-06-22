package com.zc.bookstore.user.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity @Table(name="t_user")
public class User implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id @GenericGenerator(name="pk_uuid", strategy="uuid")
	@GeneratedValue(generator="pk_uuid")
	private String uid;
	private String loginname;
	private String loginpass;
	private String newpass;
	private String reloginpass;
	private String email;
	private String verifyCode;
	private boolean status;
	private String activationCode;
	
	public User() {}
	
	public User(String loginname, String loginpass, String newpass, String reloginpass, String email, String verifyCode,
			boolean status, String activationCode) {
		super();
		this.loginname = loginname;
		this.loginpass = loginpass;
		this.newpass = newpass;
		this.reloginpass = reloginpass;
		this.email = email;
		this.verifyCode = verifyCode;
		this.status = status;
		this.activationCode = activationCode;
	}

	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public String getLoginpass() {
		return loginpass;
	}
	public void setLoginpass(String loginpass) {
		this.loginpass = loginpass;
	}
	public String getNewpass() {
		return newpass;
	}
	public void setNewpass(String newpass) {
		this.newpass = newpass;
	}
	public String getReloginpass() {
		return reloginpass;
	}
	public void setReloginpass(String reloginpass) {
		this.reloginpass = reloginpass;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getVerifyCode() {
		return verifyCode;
	}
	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getActivationCode() {
		return activationCode;
	}
	public void setActivationCode(String activationCode) {
		this.activationCode = activationCode;
	}
	
}
