package com.zc.bookstore.order.service;

import java.util.List;


import com.zc.bookstore.order.dao.OrderDao;
import com.zc.bookstore.order.domain.Order;
import com.zc.bookstore.order.domain.OrderItem;
import com.zc.pager.PageBean;

public class OrderService {
	
	private OrderDao orderDao;
	
	public void setOrderDao(OrderDao orderDao) {
		this.orderDao = orderDao;
	}
	
	public OrderDao getOrderDao() {
		return orderDao;
	}

	/**
	 * 生成订单
	 * @param order
	 */
	public void create(Order order, List<OrderItem> orderItems) {
		orderDao.add(order, orderItems);
	}

	/**
	 * 我的订单
	 * @param uid
	 * @return
	 */
	public PageBean<Order> myOrders(String uid, int pc) {
		return orderDao.findByUser(uid, pc);
	}

	/**
	 * 加载订单
	 * @param oid
	 * @return
	 */
	public Order load(String oid) {
		return orderDao.load(oid);
	}
	
	/**
	 * 修改订单状态
	 * @param oid
	 * @param status
	 */
	public void updateStatus(String oid, int status) {
		orderDao.updateOrderStatus(oid, status);
	}
	
	/**
	 * 通过oid查询状态
	 * @param oid
	 * @return
	 */
	public int findStatusByOid(String oid) {
		return orderDao.findStatusByOrder(oid);
	}

	/**
	 * 查看所有订单
	 * @param pc
	 * @return
	 */
	public PageBean<Order> findAll(int pc) {
		return orderDao.findAllOrder(pc);
	}
	
	/**
	 * 按状态查询订单
	 * @param status
	 * @param pc
	 * @return
	 */
	public PageBean<Order> findByStatus(int status, int pc) {
		return orderDao.findAllOrderByStatus(status, pc);
	}
}
