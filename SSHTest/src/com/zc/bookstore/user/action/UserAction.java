package com.zc.bookstore.user.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.zc.bookstore.baseaction.BaseAction;
import com.zc.bookstore.user.domain.User;
import com.zc.bookstore.user.service.UserException;
import com.zc.bookstore.user.service.UserService;

public class UserAction extends BaseAction implements ServletRequestAware, ServletResponseAware{

	private static final long serialVersionUID = 1L;
	
	private UserService userService;
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	private String result;
	
	public void setResult(String result) {
		this.result = result;
	}
	
	public String getResult() {
		return result;
	}
	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	public UserService getUserService() {
		return userService;
	}
	
	/**
	 * 异步校验登录名
	 * @return
	 */
	public String validateLoginname() {
		String loginname = request.getParameter("loginname");
		System.out.println(loginname);
		//如果登录名已被注册返回true
		int flag = 0;
		if(userService.validateLoginname(loginname)){
			flag = 1;
		}
		try {
			response.setCharacterEncoding("UTF-8");
			response.getWriter().print(flag + "");
			response.flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 校验老密码
	 * @return
	 */
	public String validateLoginpass(){
		/*
		 * 1. 从session中获取当前用户，再获取当前用户的用户名
		 */
		User user = (User) request.getSession().getAttribute("user");
		String loginname = user.getLoginname();
		
		/*
		 * 2. 获取旧密码
		 */
		String loginpass = request.getParameter("loginpass");
		
		/*
		 * 3. 创建formBean，调用userService.login()方法进行校验
		 */
		User formBean = new User();
		formBean.setLoginname(loginname);
		formBean.setLoginpass(loginpass);
		User _user = userService.login(formBean);
		
		/*
		 * 4. 如果用户名密码正确，返回true，否则返回false
		 */
		boolean flag = _user != null;
		try {
			response.getWriter().print(flag + "");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 异步校验Email
	 * @return
	 */
	public String validateEmail() {
		String email = request.getParameter("email");
		int flag = 0;
		//如果Email已被注册返回true
		if(userService.validateEmail(email)){
			flag = 1;
		}
		try {
			response.setCharacterEncoding("UTF-8");
			response.getWriter().print(flag + "");
			response.flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 异步校验验证码
	 * @return
	 */
	public String validateVerifyCode() {
		String vCode = (String) request.getSession().getAttribute("vCode");
		String verifyCode = request.getParameter("verifyCode");
		boolean flag = vCode.equalsIgnoreCase(verifyCode);//如果验证码正确返回true
		try {
			response.getWriter().print(flag + "");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 注册功能
	 * @return
	 */
	public String regist() {
		/*
		 * 1. 封装表单数据到User对象中
		 */
		Map<String, String[]> map = request.getParameterMap();
		User user = new User();
		user.setLoginname(map.get("loginname")[0]);
		user.setLoginpass(map.get("loginpass")[0]);
		user.setReloginpass(map.get("reloginpass")[0]);
		user.setEmail(map.get("email")[0]);
//		user.setActivationCode(map.get("activationCode")[0]);
//		user.setStatus(Boolean.parseBoolean(map.get("status")[0]));
		user.setVerifyCode(map.get("verifyCode")[0]);
		/*
		 * 2. 对表单数据进行服务器端校验
		 */
		Map<String,String> errors = validateRegist(user);
		if(errors != null && errors.size() > 0) {//是否存在校验错误信息
			request.setAttribute("errors", errors);
			request.setAttribute("user", user);
			return "error";
		}
			/*
			 * 3. 调用userService完成注册
			 */
			userService.regist(user);
			
			/*
			 * 4. 保存注册成功信息，转发到msg.jsp显示
			 */
			request.setAttribute("code", "success");//是成功信息还是错误信息
			request.setAttribute("msg", "恭喜，注册成功！请马上到邮箱完成激活！");
			return "success";
	}
	
	/**
	 * 激活功能
	 * @return
	 */
	public String activation() {
		/*
		 * 1. 获取激活码
		 */
		String activationCode = request.getParameter("activationCode");
		try {
			/*
			 * 2. 调用userService的activation方法完成激活操作
			 */
			userService.activation(activationCode);
			/*
			 * 3. 如果没有抛出异常，那么保存成功信息
			 */
			request.setAttribute("code", "success");
			request.setAttribute("msg", "恭喜，激活成功，请马上登录！");
		} catch (UserException e) {
			/*
			 * 4. 如果抛出了异步，保存错误信息
			 */
			request.setAttribute("code", "error");
			request.setAttribute("msg", e.getMessage());
			return "successorerror";
		}
		// 无论成功还是失败，都会转发到msg.jsp显示信息。
		return "successorerror";
		
	}
	
	/**
	 * 登录功能
	 * @return
	 */
	public String login() {
		/*
		 * 1. 封装表单数据到User对象中
		 */
		Map<String, String[]> map = request.getParameterMap();
		User formBean = new User();
		formBean.setLoginname(map.get("loginname")[0]);
		formBean.setLoginpass(map.get("loginpass")[0]);
		formBean.setVerifyCode(map.get("verifyCode")[0]);
		
		/*
		 * 2. 对formBean进行服务器端表单校验
		 */
		Map<String,String> errors = validateLogin(formBean);
		if(errors != null && errors.size() > 0) {//是否存在校验错误信息
			request.setAttribute("errors", errors);
			request.setAttribute("user", formBean);
			return "error";
		}
		
		/*
		 * 3. 调用userService#login(User user)完成登录
		 * 判断数据库中是否存在该用户
		 */
		User user = userService.login(formBean);
		/*
		 * 4. 判断是否查询到User
		 */
		if(user == null) {//如果用户登录失败
			request.setAttribute("user", formBean);
			request.setAttribute("msg", "用户名或密码错误！");
			return "error";
		} else if(!user.isStatus()) {//如果用户还没有激活
			request.setAttribute("user", formBean);
			request.setAttribute("msg", "您还没有激活！");
			return "error";	
		} else {//如果登录成功
			request.getSession().setAttribute("sessionUser", user);
			// 保存cookie，为了login.jsp页面可以记住当前用户名
			String name = "loginname";
			String value;
			try {
				value = URLEncoder.encode(user.getLoginname(), "UTF-8");
				Cookie cookie = new Cookie(name, value);
				cookie.setMaxAge(1000 * 60 * 60 * 24);
				response.addCookie(cookie);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return "error";
			}
			return "success";//重定向到主页
		}
	}
	
	/**
	 * 退出功能
	 * @return
	 */
	public String quit() {
		request.getSession().removeAttribute("sessionUser");
		return "success";
	}

	/**
	 * 修改密码
	 * @return
	 */
	public String updatePassword() {
		/*
		 * 1. 封装表单数据到User
		 */
		Map<String, String[]> map = request.getParameterMap();
		User formBean = new User();
		formBean.setNewpass(map.get("newpass")[0]);
		/*
		 * 2. 调用userService的updatePassword(String uid, String newpass)方法修改密码
		 */
		User user = (User) request.getSession().getAttribute("sessionUser");
		userService.updatePassword(user.getUid(), formBean.getNewpass());
		
		/*
		 * 3. 转发到msg.jsp
		 */
		request.setAttribute("code", "success");
		request.setAttribute("msg", "恭喜，密码修改成功！");
		return "success";
	}
	
	/*
	 * 登录校验方法
	 */
	private Map<String, String> validateLogin(User user) {
		Map<String,String> errors = new HashMap<String,String>();
		//对loginname进行校验
		String loginname = user.getLoginname();
		if(loginname == null || loginname.isEmpty()) {
			errors.put("loginname", "用户名不能为空！");
		} else if(loginname.length() < 3 || loginname.length() > 20) {
			errors.put("loginname", "用户名长度必须在3~20之间！");
		}
		
		// 对loginpass进行校验
		String loginpass = user.getLoginpass();
		if(loginpass == null || loginpass.isEmpty()) {
			errors.put("loginpass", "密码不能为空！");
		} else if(loginpass.length() < 3 || loginpass.length() > 20) {
			errors.put("loginpass", "密码长度必须在3~20之间！");
		}
		
		// 对验证码进行校验
		String verifyCode = user.getVerifyCode();
		String vCode = (String) request.getSession().getAttribute("vCode");
		if(verifyCode == null || verifyCode.isEmpty()) {
			errors.put("verifyCode", "验证码不能为空！");
		} else if(verifyCode.length() != 4) {
			errors.put("verifyCode", "错误的验证码！");
		} else if(!verifyCode.equalsIgnoreCase(vCode)) {
			errors.put("verifyCode", "错误的验证码！");
		}
		return errors;
	}

	/*
	 * 服务器端表单校验
	 * 如果校验通过，返回长度为0的Map，如果校验失败，那么返回的Map中保存的是错误信息。
	 *   Map中key为字段名称，值为错误信息。
	 */
	private Map<String, String> validateRegist(User user) {
		Map<String,String> errors = new HashMap<String,String>();
		//对loginname进行校验
		String loginname = user.getLoginname();
		if(loginname == null || loginname.isEmpty()) {
			errors.put("loginname", "用户名不能为空！");
		} else if(loginname.length() < 3 || loginname.length() > 20) {
			errors.put("loginname", "用户名长度必须在3~20之间！");
		} else if(userService.validateLoginname(loginname)) {
			errors.put("loginname", "用户名已被注册过！");
		}
		
		// 对loginpass进行校验
		String loginpass = user.getLoginpass();
		if(loginpass == null || loginpass.isEmpty()) {
			errors.put("loginpass", "密码不能为空！");
		} else if(loginpass.length() < 3 || loginpass.length() > 20) {
			errors.put("loginpass", "密码长度必须在3~20之间！");
		}
		
		// 对确认密码进行校验
		String reloginpass = user.getReloginpass();
		if(reloginpass == null || reloginpass.isEmpty()) {
			errors.put("reloginpass", "确认密码不能为空！");
		} else if(!reloginpass.equalsIgnoreCase(loginpass)) {
			errors.put("reloginpass", "两次输入密码不一致！");
		}
		
		// 对Email进行校验
		String email = user.getEmail();
		if(email == null || email.isEmpty()) {
			errors.put("email", "Email不能为空！");
		} else if(!email.matches("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$")) {
			errors.put("email", "错误的Email格式！");
		} else if(userService.validateEmail(email)) {
			errors.put("email", "Email已被注册过！");
		}
		
		// 对验证码进行校验
		String verifyCode = user.getVerifyCode();
		String vCode = (String) request.getSession().getAttribute("vCode");
		if(verifyCode == null || verifyCode.isEmpty()) {
			errors.put("verifyCode", "验证码不能为空！");
		} else if(verifyCode.length() != 4) {
			errors.put("verifyCode", "错误的验证码！");
		} else if(!verifyCode.equalsIgnoreCase(vCode)) {
			errors.put("verifyCode", "错误的验证码！");
		}
		return errors;
	}

	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

}
