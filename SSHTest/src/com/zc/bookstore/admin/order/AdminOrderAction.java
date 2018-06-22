package com.zc.bookstore.admin.order;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.zc.bookstore.baseaction.BaseAction;
import com.zc.bookstore.order.domain.Order;
import com.zc.bookstore.order.service.OrderService;
import com.zc.pager.PageBean;

public class AdminOrderAction extends BaseAction implements ServletRequestAware, ServletResponseAware{

	private HttpServletRequest request;
	private HttpServletResponse response;
	private OrderService orderService;
	
	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}
	
	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}
	
	public OrderService getOrderService() {
		return orderService;
	}
	
	/*
	 * 获取当前页码
	 * @return
	 */
	private int getPageCode() {
		String pageCode = request.getParameter("pc");
		if(pageCode == null) return 1;
		try {
			return Integer.parseInt(pageCode); 
		} catch(RuntimeException e) {
			return 1;
		}
	}
	
	/*
	 * 获取请求的url，但去除pc参数
	 * @return
	 */
	private String getUrl() {
		String url = request.getRequestURI() + "?" + request.getQueryString();
		int fromIndex = url.lastIndexOf("&pc=");
		if(fromIndex == -1) return url;
		int toIndex = url.indexOf("&", fromIndex + 1);
		if(toIndex == -1) return url.substring(0, fromIndex);
		return url.substring(0, fromIndex) + url.substring(toIndex);
	}
	
	/**
	 * 查看所有订单
	 * @return
	 */
	public String findAll(){
		/*
		 * 1. 获取当前页码
		 */
		int pc = getPageCode();
		/*
		 * 2. 查询PageBean
		 */
		PageBean<Order> pb = orderService.findAll(pc);
		/*
		 * 3. 获取url，设置给PageBean
		 */
		String url = getUrl();
		pb.setUrl(url);
		/*
		 * 4. 把PageBean保存到request，转发到/jsps/order/list.jsp
		 */
		request.setAttribute("pb", pb);
		return "success";
		//f:/adminjsps/admin/order/list.jsp
	}
	
	/**
	 * 按状态查询
	 * @return
	 */
	public String findByStatus(){
		/*
		 * 1. 获取当前页码
		 */
		int pc = getPageCode();
		/*
		 * 2. 查询PageBean
		 */
		int status = Integer.parseInt(request.getParameter("status"));
		PageBean<Order> pb = orderService.findByStatus(status, pc);
		/*
		 * 3. 获取url，设置给PageBean
		 */
		String url = getUrl();
		pb.setUrl(url);
		/*
		 * 4. 把PageBean保存到request，转发到/jsps/order/list.jsp
		 */
		request.setAttribute("pb", pb);
		return "success";
		//f:/adminjsps/admin/order/list.jsp
	}
	
	/**
	 * 加载订单
	 * @return
	 */
	public String load(){
		String oid = request.getParameter("oid");
		request.setAttribute("order", orderService.load(oid));
		String oper = request.getParameter("oper");
		request.setAttribute("oper", oper);
		return "success";
		///adminjsps/admin/order/desc.jsp
	}
	
	/**
	 * 取消订单
	 * @return
	 */
	public String cancel(){
		String oid = request.getParameter("oid");
		orderService.updateStatus(oid, 5);
		request.setAttribute("msg", "订单已取消！");
		return "success";
		//f:/adminjsps/msg.jsp
	}
	
	/**
	 * 发货
	 * @return
	 */
	public String deliver(){
		String oid = request.getParameter("oid");
		orderService.updateStatus(oid, 3);
		request.setAttribute("msg", "发货成功！");
		return "success";
		//f:/adminjsps/msg.jsp
	}
}
