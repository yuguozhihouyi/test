package com.zc.pager;

import java.util.List;

public class PageBean<T> {
	
	private int pc;
	private int tr;
	private int ps;
	private List<T> dataList;
	private String url;
	
	public int getTp(){
		int tp = tr/ps;
		return tr%ps == 0 ? tp: tp+1;
	}
	
	public int getPc() {
		return pc;
	}
	public void setPc(int pc) {
		this.pc = pc;
	}
	public int getTr() {
		return tr;
	}
	public void setTr(int tr) {
		this.tr = tr;
	}
	public int getPs() {
		return ps;
	}
	public void setPs(int ps) {
		this.ps = ps;
	}
	public List<T> getDataList() {
		return dataList;
	}
	public void setDataList(List<T> dataList) {
		this.dataList = dataList;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	

}
