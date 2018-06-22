package com.zc.bookstore.book.service;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.zc.bookstore.book.dao.BookDao;
import com.zc.bookstore.book.domain.Book;
import com.zc.bookstore.category.domain.Category;
import com.zc.pager.PageBean;

public class BookService {
	
	private BookDao bookDao;
	
	public void setBookDao(BookDao bookDao) {
		this.bookDao = bookDao;
	}
	
	public BookDao getBookDao() {
		return bookDao;
	}
	
	/**
	 * 按分类查询图书（分页）
	 * @param cid
	 * @param pc
	 * @return
	 */
	public PageBean<Book> findByCategory(String cid, int pc) {
		return bookDao.findByCategory(cid, pc);
	}
	
	/**
	 * 按作者查询
	 * @param author
	 * @param pc
	 * @return
	 */
	public PageBean<Book> findByAuthor(String author, int pc) {
		return bookDao.findByAuthor(author, pc);
	}
	
	/**
	 * 按出版社查询
	 * @param press
	 * @param pc
	 * @return
	 */
	public PageBean<Book> findByPress(String press, int pc) {
		return bookDao.findByPress(press, pc);
	}
	
	/**
	 * 按图名查询
	 * @param bname
	 * @param pc
	 * @return
	 */
	public PageBean<Book> findByBname(String bookName, int pc) {
		return bookDao.findByBookName(bookName, pc);
	}
	
	/**
	 * 多条件组合查询
	 * @param book
	 * @param pc
	 * @return
	 */
	public PageBean<Book> findByCombination(Book book, int pc) {
		return bookDao.findByCombination(book, pc);
	}
	
	/**
	 * 加载图书
	 * @param bid
	 * @return
	 */
	public Book load(String bid) {
		return bookDao.load(bid);
	}

	/**
	 * 根据商品二级目录找到对应的一级目录
	 * @param cid
	 * @return
	 */
	public String findPidByBookCid(String cid){
		return bookDao.findPidByBookCid(cid);
	}
	
	/**
	 * 编辑图书
	 * @param book
	 */
	public void editBookInfo(Book book) {
		bookDao.editBookInfo(book);
	}
	
	/**
	 * 删除图书
	 * @param bid
	 */
	public void deleteBookInfo(String bid) {
		bookDao.deleteBookInfo(bid);
	}

	/**
	 * 添加图书
	 * @param book
	 */
	public void addBookInfo(Book book) {
		bookDao.addBookInfo(book);
	}
}
