package com.zc.bookstore.book.action;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.zc.bookstore.baseaction.BaseAction;
import com.zc.bookstore.book.domain.Book;
import com.zc.bookstore.book.service.BookService;
import com.zc.pager.PageBean;

public class BookAction extends BaseAction implements ServletRequestAware{

	private static final long serialVersionUID = 1L;

	private BookService bookService;
	private HttpServletRequest request;
	
	public void setBookService(BookService bookService) {
		this.bookService = bookService;
	}
	
	public BookService getBookService() {
		return bookService;
	}
	
	/*
	 * 获取当前页码
	 */
	private int getPageCode() {
		String pageCode = request.getParameter("pc");
		if(pageCode == null) return 1;
		try {
			return Integer.parseInt(pageCode); 
		} catch(RuntimeException e) {
			return 1;
		}
	}
	
	/*
	 * 获取请求的url，但去除pc参数
	 */
	private String getUrl() {
		String url = request.getRequestURI() + "?" + request.getQueryString();
		int fromIndex = url.lastIndexOf("&pc=");
		if(fromIndex == -1) return url;
		int toIndex = url.indexOf("&", fromIndex + 1);
		if(toIndex == -1) return url.substring(0, fromIndex);
		return url.substring(0, fromIndex) + url.substring(toIndex);
	}
	
	/**
	 * 按分类查询图书（分页）
	 * @return
	 */
	public String findByCategory() {
		/*
		 * 1. 获取当前页码
		 */
		int pc = getPageCode();
		/*
		 * 2. 使用BookService查询，得到PageBean
		 */
		String cid = request.getParameter("cid");
		PageBean<Book> pb = bookService.findByCategory(cid, pc);
		/*
		 * 3. 获取url，设置给PageBean
		 */
		String url = getUrl();
		pb.setUrl(url);
		/*
		 * 4. 把PageBean保存到request，转发到/jsps/book/list.jsp
		 */
		request.setAttribute("pb", pb);
		return "success";
	}
	
	/**
	 * 按作者查询
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findByAuthor() {
		/*
		 * 1. 获取当前页码
		 */
		int pc = getPageCode();
		/*
		 * 2. 使用BookService查询，得到PageBean
		 */
		String author = request.getParameter("author");
		PageBean<Book> pb = bookService.findByAuthor(author, pc);
		/*
		 * 3. 获取url，设置给PageBean
		 */
		String url = getUrl();
		pb.setUrl(url);
		/*
		 * 4. 把PageBean保存到request，转发到/jsps/book/list.jsp
		 */
		request.setAttribute("pb", pb);
		return "success";
	}
	
	/**
	 * 按出版社查询
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findByPress() {
		/*
		 * 1. 获取当前页码
		 */
		int pc = getPageCode();
		/*
		 * 2. 使用BookService查询，得到PageBean
		 */
		String press = request.getParameter("press");
		PageBean<Book> pb = bookService.findByPress(press, pc);
		/*
		 * 3. 获取url，设置给PageBean
		 */
		String url = getUrl();
		pb.setUrl(url);
		/*
		 * 4. 把PageBean保存到request，转发到/jsps/book/list.jsp
		 */
		request.setAttribute("pb", pb);
		return "success";
	}
	
	/**
	 * 按图名查询
	 * @return
	 */
	public String findByBname() {
		/*
		 * 1. 获取当前页码
		 */
		int pc = getPageCode();
		/*
		 * 2. 使用BookService查询，得到PageBean
		 */
		String bname = request.getParameter("bname");
		System.out.println(bname);
		PageBean<Book> pb = bookService.findByBname(bname, pc);
		/*
		 * 3. 获取url，设置给PageBean
		 */
		String url = getUrl();
		pb.setUrl(url);
		/*
		 * 4. 把PageBean保存到request，转发到/jsps/book/list.jsp
		 */
		request.setAttribute("pb", pb);
		return "success";
	}
	
	/**
	 * 多条件组合查询
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findByCombination() {
		/*
		 * 1. 获取当前页码
		 */
		int pc = getPageCode();
		/*
		 * 2. 使用BookService查询，得到PageBean
		 */
		Book book = new Book();
		book.setBname(request.getParameter("bname"));
		book.setAuthor(request.getParameter("author"));
		book.setPress(request.getParameter("press"));
		PageBean<Book> pb = bookService.findByCombination(book, pc);
		/*
		 * 3. 获取url，设置给PageBean
		 */
		String url = getUrl();
		pb.setUrl(url);
		/*
		 * 4. 把PageBean保存到request，转发到/jsps/book/list.jsp
		 */
		request.setAttribute("pb", pb);
		return "success";
	}
	
	/**
	 * 加载图书
	 * @return
	 */
	public String load() {
		String bid = request.getParameter("bid");
		request.setAttribute("book", bookService.load(bid));
		return "success";
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}
}
