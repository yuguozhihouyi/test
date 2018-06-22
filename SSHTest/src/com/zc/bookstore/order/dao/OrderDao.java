package com.zc.bookstore.order.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.zc.bookstore.basedao.BaseDao;
import com.zc.bookstore.constants.PageConstants;
import com.zc.bookstore.order.domain.Order;
import com.zc.bookstore.order.domain.OrderItem;
import com.zc.bookstore.utils.SQLStringUtil;
import com.zc.pager.PageBean;

public class OrderDao extends BaseDao{
	
	/**
	 * 添加订单
	 * @param order
	 */
	public void add(Order order,List<OrderItem> orderItems){
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		session.save(order);
		for(OrderItem orderItem : orderItems){
			orderItem.setOrder(order);
			session.persist(orderItem);
		}
		tx.commit();
		session.close();
	}
	
	/**
	 * 通过用户查询订单
	 * @param uid
	 * @param pc
	 * @return
	 */
	public PageBean<Order> findByUser(String uid, int pc){
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("uid", uid);
		return findByCriteria(criteria, pc);
	}
	
	/**
	 * 加载所有该订单的订单条目
	 * @param order
	 * @return
	 */
	private List<OrderItem> loadAllOrderItemList(Order order){
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		Order orderLoad = (Order)session.load(Order.class, order.getOid());
		List<OrderItem> orderItems = orderLoad.getOrderItemList();
		return orderItems;
	}
	
	/**
	 * 加载订单
	 * @param oid
	 * @return
	 */
	public Order load(String oid){
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		Order orderLoad = (Order)session.get(Order.class, oid);
		return orderLoad;
	}
	
	/**
	 * 修改订单状态
	 * @param oid
	 * @param status
	 */
	public void updateOrderStatus(String oid, int status){
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		Order order = (Order)session.load(Order.class, oid);
		order.setStatus(status);
		session.update(order);
		tx.commit();
		session.close();
	}
	
	/**
	 * 通过订单号查询订单状态
	 * @param oid
	 * @return
	 */
	public int findStatusByOrder(String oid){
		String SQL = "SELECT * FROM t_order WHERE oid = :oid";
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		List order = session.createSQLQuery(SQL)
				.addEntity(Order.class)
				.setString("oid", oid)
				.list();
		return ((Order)(order.get(0))).getStatus();
	}
	
	/**
	 * 查询所有订单(分页)
	 * @param pc
	 * @return
	 */
	public PageBean<Order> findAllOrder(int pc){
		Map<String, Object> criteria = null;
		return findByCriteria(criteria, pc);
	}
	
	/**
	 * 按订单状态查询所有订单(分页)
	 * @param status
	 * @param pc
	 * @return
	 */
	public PageBean<Order> findAllOrderByStatus(int status, int pc){
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("status", status);
		return findByCriteria(criteria, pc);
	}
	
	/**
	 * 条件查询订单(分页查询)
	 * @param criteria 条件信息的封装
	 * @param pc 当前约束
	 * @return
	 */
	public PageBean<Order> findByCriteria(Map<String, Object> criteria, int pc){
		StringBuilder whereSQL = new StringBuilder(" WHERE 1=1");
		String finalWhereSQL = null;
		if(criteria == null){
			finalWhereSQL = whereSQL.toString();
		}else{
			//拼接SQL查询条件
			List<Object> params = new ArrayList<Object>();
			
			for(String name : criteria.keySet()){
				Object value = criteria.get(name);
				if(value == null){
					continue;
				}
				whereSQL.append(" AND ").append(name).append(" = ?");
				params.add("'" + value + "'");
			}
			finalWhereSQL = SQLStringUtil.appendSQL(params, whereSQL.toString());
		}
		
		
		//排序
		String orderBySQL = " order by o.ordertime";
		
		//总记录数
		String cntSQL = "SELECT count(*) FROM Order o" + finalWhereSQL;
		System.out.println(cntSQL);
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		Number cnt = (Number)session.createQuery(cntSQL).uniqueResult();
		int tr = cnt == null ? 0 : cnt.intValue();
		//最终SQL语句
		String SQL = "SELECT o FROM Order o" + finalWhereSQL + orderBySQL;
		System.out.println(SQL);
		//limit参数
		int ps = PageConstants.BOOK_PAGE_SIZE;
		int firstPosition = ps * (pc - 1);
		
		//条件查询订单信息
		List orders = session.createQuery(SQL)
				.setFirstResult(firstPosition)
				.setMaxResults(ps)
				.list();
		
		tx.commit();
		session.close();
		
		//封装PageBean 
		PageBean<Order> pb = new PageBean<Order>();
		pb.setPc(pc);
		pb.setPs(ps);
		pb.setTr(tr);
		pb.setDataList(orders);
		
		return pb;
	}
}
