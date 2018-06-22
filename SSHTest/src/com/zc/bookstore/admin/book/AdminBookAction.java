package com.zc.bookstore.admin.book;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.zc.bookstore.baseaction.BaseAction;
import com.zc.bookstore.book.domain.Book;
import com.zc.bookstore.book.service.BookService;
import com.zc.bookstore.category.service.CategoryService;
import com.zc.pager.PageBean;

public class AdminBookAction extends BaseAction implements ServletRequestAware, ServletResponseAware{

	private static final long serialVersionUID = 1L;
	
	private BookService bookService;
	private CategoryService categoryService;
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	public void setBookService(BookService bookService) {
		this.bookService = bookService;
	}
	
	public BookService getBookService() {
		return bookService;
	}
	
	public void setCategoryService(CategoryService categoryService) {
		this.categoryService = categoryService;
	}
	
	public CategoryService getCategoryService() {
		return categoryService;
	}
	
	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	/*
	 * 获取当前页码
	 */
	private int getPageCode(HttpServletRequest req) {
		String pageCode = req.getParameter("pc");
		if (pageCode == null)
			return 1;
		try {
			return Integer.parseInt(pageCode);
		} catch (RuntimeException e) {
			return 1;
		}
	}

	/*
	 * 获取请求的url，但去除pc参数
	 */
	private String getUrl(HttpServletRequest req) {
		String url = req.getRequestURI() + "?" + req.getQueryString();
		int fromIndex = url.lastIndexOf("&pc=");
		if (fromIndex == -1)
			return url;
		int toIndex = url.indexOf("&", fromIndex + 1);
		if (toIndex == -1)
			return url.substring(0, fromIndex);
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
		int pc = getPageCode(request);
		/*
		 * 2. 使用BookService查询，得到PageBean
		 */
		String cid = request.getParameter("cid");
		PageBean<Book> pb = bookService.findByCategory(cid, pc);
		/*
		 * 3. 获取url，设置给PageBean
		 */
		String url = getUrl(request);
		pb.setUrl(url);
		/*
		 * 4. 把PageBean保存到request，转发到/jsps/book/list.jsp
		 */
		request.setAttribute("pb", pb);
		return "findByCategory_success";
	}
	
	/**
	 * 按作者查询
	 * @return
	 */
	public String findByAuthor() {
		/*
		 * 1. 获取当前页码
		 */
		int pc = getPageCode(request);
		/*
		 * 2. 使用BookService查询，得到PageBean
		 */
		String author = request.getParameter("author");
		PageBean<Book> pb = bookService.findByAuthor(author, pc);
		/*
		 * 3. 获取url，设置给PageBean
		 */
		String url = getUrl(request);
		pb.setUrl(url);
		/*
		 * 4. 把PageBean保存到request，转发到/jsps/book/list.jsp
		 */
		request.setAttribute("pb", pb);
		return "findByAuthor_success";
	}
	
	/**
	 * 按出版社查询
	 * @return
	 */
	public String findByPress() {
		/*
		 * 1. 获取当前页码
		 */
		int pc = getPageCode(request);
		/*
		 * 2. 使用BookService查询，得到PageBean
		 */
		String press = request.getParameter("press");
		PageBean<Book> pb = bookService.findByPress(press, pc);
		/*
		 * 3. 获取url，设置给PageBean
		 */
		String url = getUrl(request);
		pb.setUrl(url);
		/*
		 * 4. 把PageBean保存到request，转发到/jsps/book/list.jsp
		 */
		request.setAttribute("pb", pb);
		return "findByPress_success";
	}
	
	/**
	 * 多条件组合查询
	 * @return
	 */
	public String findByCombination() {
		/*
		 * 1. 获取当前页码
		 */
		int pc = getPageCode(request);
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
		String url = getUrl(request);
		pb.setUrl(url);
		/*
		 * 4. 把PageBean保存到request，转发到/jsps/book/list.jsp
		 */
		request.setAttribute("pb", pb);
		return "findByCombination_success";
	}
	
	/**
	 * 加载图书
	 * @return
	 */
	public String load() {
		/*
		 * 1. 获取bid,通过bid加载book，保存到request中
		 */
		String bid = request.getParameter("bid");
		Book book = bookService.load(bid);
		request.setAttribute("book", book);
		/*
		 * 2. 获取所有1级分类，保存到request中
		 */
		request.setAttribute("parents", categoryService.findFirstCategory());
		/*
		 * 3. 获取当前图书所属1级分类下的所有2级分类
		 */
		// 获取当前图书所属1级分类cid
		String cid = book.getCategory().getCid();
		String pid = bookService.findPidByBookCid(cid);
		request.setAttribute("children", categoryService.findChildren(pid));
		return "success";
	}
	
	/**
	 * 编辑图书
	 * @return
	 */
	public String edit() {
		/*Book book = CommonUtils.toBean(request.getParameterMap(), Book.class);
		Category category = CommonUtils.toBean(req.getParameterMap(), Category.class);
		Category parent = new Category();
		parent.setCid(request.getParameter("pid"));
		category.setParent(parent);
		book.setCategory(category);
		
		bookService.editBookInfo(book);
		
		request.setAttribute("msg", "编辑图书成功！");*/
		return "success";
	}
	
	/**
	 * 删除图书
	 * @return
	 */
	public String delete() {
		String bid = request.getParameter("bid");
		bookService.deleteBookInfo(bid);
		request.setAttribute("msg", "删除图书成功！");
		return "success";
	}
	
	/**
	 * 添加图书：第一步(将已有的一级分类添加到request对象中转发到Add.jsp界面)
	 * @return
	 */
	public String addPre() {
		request.setAttribute("parents", categoryService.findFirstCategory());
		return "success";
	}

}
