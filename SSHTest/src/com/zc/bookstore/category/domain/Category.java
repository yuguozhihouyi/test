package com.zc.bookstore.category.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

@Entity @Table(name="t_category")
public class Category implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	private String cid;
	@Column(name="cname", unique=true)
	private String cname;
	private String pid;
	@Transient
	private List<Category> children;//所有子分类
	@Transient
	private Category parent;//父分类
	private String detail;
	@Generated(GenerationTime.INSERT)
	@Column(nullable=false, name="orderBy")
	private int orderBy;
	
	public Category() {}

	public Category(String cname, String pid, String detail) {
		super();
		this.cname = cname;
		this.pid = pid;
		this.detail = detail;
	}
	
	public Category(String cid, String cname, String pid, String detail) {
		super();
		this.cid = cid;
		this.cname = cname;
		this.pid = pid;
		this.detail = detail;
	}

	public void setOrderBy(int orderBy) {
		this.orderBy = orderBy;
	}
	
	public int getOrderBy() {
		return orderBy;
	}
	
	public String getPid() {
		return pid;
	}
	
	public void setPid(String pid) {
		this.pid = pid;
	}
	
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public List<Category> getChildren() {
		return children;
	}
	public void setChildren(List<Category> children) {
		this.children = children;
	}
	public Category getParent() {
		return parent;
	}
	public void setParent(Category parent) {
		this.parent = parent;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
}
