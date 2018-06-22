package com.zc.bookstore.order.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;

import com.zc.bookstore.user.domain.User;

@Entity @Table(name="t_order")
public class Order implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id @GenericGenerator(name="pk_uuid", strategy="uuid")
	@GeneratedValue(generator="pk_uuid")
	private String oid;
	private String ordertime;
	private double total;
	private int status;
	private String address;
	@ManyToOne(targetEntity=User.class)
	@JoinColumn(name="uid", nullable=false)
	@Cascade(CascadeType.ALL)
	private User owner;
	@OneToMany(targetEntity=OrderItem.class, mappedBy="order", fetch=FetchType.EAGER)
	private List<OrderItem> orderItemList;
	
	public Order() {}
	
	public Order(String oid) {
		super();
		this.oid = oid;
	}

	public Order(String ordertime, double total, int status, String address, User owner,
			List<OrderItem> orderItemList) {
		super();
		this.ordertime = ordertime;
		this.total = total;
		this.status = status;
		this.address = address;
		this.owner = owner;
		this.orderItemList = orderItemList;
	}
	
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public String getOrdertime() {
		return ordertime;
	}
	public void setOrdertime(String ordertime) {
		this.ordertime = ordertime;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public User getOwner() {
		return owner;
	}
	public void setOwner(User owner) {
		this.owner = owner;
	}
	public List<OrderItem> getOrderItemList() {
		return orderItemList;
	}
	public void setOrderItemList(List<OrderItem> orderItemList) {
		this.orderItemList = orderItemList;
	}
}
