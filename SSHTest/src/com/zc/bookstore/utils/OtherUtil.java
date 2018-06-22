package com.zc.bookstore.utils;

import com.zc.bookstore.book.domain.Book;
import com.zc.bookstore.category.domain.Category;

public class OtherUtil {
	public static Book replaceAll(Book bookByDB, Book book){
		bookByDB.setBname(book.getBname());
		bookByDB.setAuthor(book.getAuthor());
		bookByDB.setPrice(book.getPrice());
		bookByDB.setCurrPrice(book.getCurrPrice());
		bookByDB.setDiscount(book.getDiscount());
		bookByDB.setPress(book.getPress());
		bookByDB.setPublishtime(book.getPublishtime());
		bookByDB.setEdition(book.getEdition());
		bookByDB.setPageNum(book.getPageNum());
		bookByDB.setWordNum(book.getWordNum());
		bookByDB.setPrinttime(book.getPrinttime());
		bookByDB.setBooksize(book.getBooksize());
		bookByDB.setPaper(book.getPaper());
		bookByDB.getCategory().setCid(book.getCategory().getCid());
		return bookByDB;
	}
	
	public static Category replaceAll(Category categoryByDB, Category category){
		if (category.getPid() == null) {
			categoryByDB.setCname(category.getCname());
			categoryByDB.setDetail(category.getDetail());
		}else{
			categoryByDB.setCname(category.getCname());
			categoryByDB.setDetail(category.getDetail());
			categoryByDB.setPid(category.getPid());
		}
		return categoryByDB;
	}
}
