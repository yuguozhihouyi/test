package com.zc.bookstore.order.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;

import com.zc.bookstore.book.domain.Book;

@Entity @Table(name="t_orderitem")
public class OrderItem implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id @GenericGenerator(name="pk_uuid", strategy="uuid")
	@GeneratedValue(generator="pk_uuid")
	private String orderItemId;
	private int quantity;
	private double subtotal;
	@OneToOne(targetEntity=Book.class)
	@JoinColumn(name="bid", referencedColumnName="bid", unique=true)
	private Book book;
	@ManyToOne(targetEntity=Order.class)
	@JoinColumn(name="oid", referencedColumnName="oid", nullable=false)
	@Cascade(CascadeType.ALL)
	private Order order;
	
	public OrderItem() {}
	
	public OrderItem(int quantity, double subtotal, Book book) {
		super();
		this.quantity = quantity;
		this.subtotal = subtotal;
		this.book = book;
	}

	public String getOrderItemId() {
		return orderItemId;
	}
	public void setOrderItemId(String orderItemId) {
		this.orderItemId = orderItemId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public double getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
}
