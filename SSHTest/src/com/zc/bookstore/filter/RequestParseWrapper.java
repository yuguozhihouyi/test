package com.zc.bookstore.filter;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.dispatcher.multipart.JakartaMultiPartRequest;

public class RequestParseWrapper extends JakartaMultiPartRequest{

	@Override
	public void parse(HttpServletRequest arg0, String arg1) throws IOException {
		super.parse(arg0, arg1);
	}
}
