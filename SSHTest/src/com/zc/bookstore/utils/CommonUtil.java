package com.zc.bookstore.utils;

import java.security.GeneralSecurityException;
import java.util.Properties;
import java.util.UUID;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import com.sun.mail.util.MailSSLSocketFactory;
import com.zc.bookstore.user.domain.User;

public class CommonUtil {
	
	public static String uuid(){
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	/**
	 * 注册用户邮件激活(发送邮件)
	 * @param user
	 */
	public static void sendMail(User user){
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.debug", "true");
		props.setProperty("mail.host", "smtp.qq.com");
		props.setProperty("mail.transport.protocol", "smtp");
		props.put("mail.store.protocol", "smtp");
		props.put("mail.smtp.port", "465");
		
		MailSSLSocketFactory sFactory = null;
		try {
			sFactory = new MailSSLSocketFactory();
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}
		
		sFactory.setTrustAllHosts(true);
		props.put("mail.smtp.ssl.enable", "true");
		props.put("mail.smtp.ssl.socketFactory", sFactory);
		
		// 发送邮件
		Session mailSession = Session.getDefaultInstance(props,
				new Authenticator() {
					@Override
					protected PasswordAuthentication getPasswordAuthentication() {
						PasswordAuthentication pa = new PasswordAuthentication("1571134208@qq.com", "giyhunwrafqpjdha");
						return pa;
					}
				});
		Message message = new MimeMessage(mailSession);
		
		try {
			message.setFrom(new InternetAddress("1571134208@qq.com"));
			message.setRecipient(RecipientType.TO, new InternetAddress(user.getEmail()));
			message.setSubject(user.getLoginname() + ",欢迎您注册MyShop账号,请点击连接激活账号");
			StringBuilder sb = new StringBuilder();
			sb.append(user.getLoginname() + ",<br/>" + "欢迎您注册MyShop账号,请点击连接激活账号<br/>");
		
			sb.append("<font color='red'><a href='http://localhost:8080/SSHTest/user/userAction_activation?activationCode="
			+ user.getActivationCode() + "' target='_blank'>账号激活链接</a></font>");
			
			message.setContent(sb.toString(), "text/html;charset=UTF-8");
			Transport.send(message);
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

}
