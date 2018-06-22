package com.zc.bookstore.filter;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class EncodingUtf8Request extends HttpServletRequestWrapper{
	
	private HttpServletRequest request;

	public EncodingUtf8Request(HttpServletRequest request) {
		super(request);
		this.request = request;
	}
	
	@Override
	public String getParameter(String name) {
		String method = request.getMethod();
		String value = null;
		try {
			request.setCharacterEncoding("utf-8");
			value = request.getParameter(name);
			
			if("get".equalsIgnoreCase(method) && value != null){
				value = new String(value.getBytes("iso-8859-1"), "utf-8");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return value;
	}
	
	@Override
	public String[] getParameterValues(String name) {
		String method = request.getMethod();
		String[] values = null;
		try {
			request.setCharacterEncoding("utf-8");
			values = request.getParameterValues(name);
			if("get".equalsIgnoreCase(method) && values.length != 0){
				for(int i = 0; i < values.length; i++){
					values[i] = new String(values[i].getBytes("iso-8859-1"), "utf-8");
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return values;
	}

}
