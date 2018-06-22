package com.zc.bookstore.utils;

import java.util.ArrayList;
import java.util.List;

import com.zc.bookstore.book.domain.Book;

public class SQLStringUtil {

	//拼接sql语句方法(适用于BookDao中的拼接)
	public static String appendSQL(List<Object> params, String sqlStr){
		//替换SQL中的？为指定的值
		String[] sqlSplitArray= sqlStr.split("\\?");
		StringBuilder sb = new StringBuilder();
		String temp = null;
		for(int i = 0; i < sqlSplitArray.length; i++){
			temp = sqlSplitArray[i];
			temp = temp + (String)(params.get(i));
			sb.append(temp);
		}
		return sb.toString();	
	}
	
	public static String appendUpdateSQL(String sql, Object entity){
		List<Object> list = new ArrayList<Object>();
		if(entity instanceof Book){
			Book book = (Book)entity;
			Object[] params = {book.getBname(),book.getAuthor(),
					book.getPrice(), book.getCurrPrice(),book.getDiscount(),
					book.getPress(),book.getPublishtime(),book.getEdition(),
					book.getPageNum(),book.getWordNum(),book.getPrinttime(),
					book.getBooksize(),book.getPaper(),book.getCategory().getCid(),
					book.getBid()};
			
			for(Object obj : params){
				list.add(obj);
			}
		}
		return appendSQL(list, sql); 
	}
}
