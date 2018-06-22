package com.zc.bookstore.cart.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.GenericGenerator;

import com.zc.bookstore.book.domain.Book;
import com.zc.bookstore.user.domain.User;

@Entity @Table(name="t_cartitem")
public class CartItem implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id @GenericGenerator(name="pk_uuid", strategy="uuid")
	@GeneratedValue(generator="pk_uuid")
	private String cartItemId;
	private int quantity;
	@OneToOne(targetEntity=Book.class)
	@JoinColumn(name="bid", referencedColumnName="bid", unique=true)
	private Book book;
	@ManyToOne(targetEntity=User.class)
	@JoinColumn(name="uid", nullable=false)
	private User owner;
	@Generated(GenerationTime.INSERT)
	@Column(nullable=false, name="orderBy")
	private int orderBy;
	
	//单个订单条目的总金额
	public double getSubtotal(){
		BigDecimal v1 = new BigDecimal(Double.toString(book.getCurrPrice()));
		BigDecimal v2 = new BigDecimal(Double.toString(quantity));
		return v1.multiply(v2).doubleValue();
	}

	public int getOrderBy() {
		return orderBy;
	}
	
	public void setOrderBy(int orderBy) {
		this.orderBy = orderBy;
	}
	
	public String getCartItemId() {
		return cartItemId;
	}

	public void setCartItemId(String cartItemId) {
		this.cartItemId = cartItemId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}
	
}
