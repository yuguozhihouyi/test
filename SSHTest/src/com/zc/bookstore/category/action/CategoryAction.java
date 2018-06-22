package com.zc.bookstore.category.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.zc.bookstore.baseaction.BaseAction;
import com.zc.bookstore.category.domain.Category;
import com.zc.bookstore.category.service.CategoryService;

public class CategoryAction extends BaseAction implements ServletRequestAware{

	private static final long serialVersionUID = 1L;
	
	private CategoryService categoryService;
	private HttpServletRequest request;
	
	public void setCategoryService(CategoryService categoryService) {
		this.categoryService = categoryService;
	}
	
	public CategoryService getCategoryService() {
		return categoryService;
	}

	/**
	 * 返回所有分类
	 * @return
	 */
	public String findAll() {
		/*
		 * 1. 获取所有分类
		 */
		List<Category> parents = categoryService.findAll();
		System.out.println(parents.get(0).getChildren().get(0).getCname());
		/*
		 * 2. 保存到request中
		 */
//		request.setAttribute("parents", parents);
		request.getSession().setAttribute("parents", parents);
		/*
		 * 3. 转发到left.jsp
		 */
		return "success";
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}
}
