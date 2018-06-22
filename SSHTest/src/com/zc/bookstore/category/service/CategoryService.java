package com.zc.bookstore.category.service;
import java.util.List;

import com.zc.bookstore.category.dao.CategoryDao;
import com.zc.bookstore.category.domain.Category;

public class CategoryService {
	private CategoryDao categoryDao;
	
	public void setCategoryDao(CategoryDao categoryDao) {
		this.categoryDao = categoryDao;
	}
	
	public CategoryDao getCategoryDao() {
		return categoryDao;
	}
	
	/**
	 * 返回所有分类
	 * @return
	 */
	public List<Category> findAll() {
		return categoryDao.findAll();
	}

	/**
	 * 添加分类
	 * @param category
	 */
	public void add(Category category) {
		categoryDao.addFirstCategory(category);
	}
	
	/**
	 * 查询所有一级分类
	 * @return
	 */
	public List<Category> findFirstCategory() {
		return categoryDao.findFirstCategory();
	}

	/**
	 * 加载分类
	 * @param cid
	 * @return
	 */
	public Category load(String cid) {
		return categoryDao.load(cid);
	}

	/**
	 * 修改分类
	 * @param category
	 */
	public void edit(Category category) {
		categoryDao.editCategory(category);
	}
	
	/**
	 * 删除指定分类
	 * @param cid
	 * @throws CategoryException 
	 */
	public void deleteOneLevel(String cid){
		categoryDao.deleteCategory(cid);
	}

	/**
	 * 删除2级分类
	 * @param cid
	 * @throws CategoryException
	 */
	public void deleteSecondCategory(String cid) {
		categoryDao.deleteCategory(cid);	
	}

	/**
	 * 加载指定父分类下所有子分类
	 * @param pid
	 * @return
	 */
	public List<Category> findChildren(String pid) {
		return categoryDao.findSecondCategory(pid);
	}

}
