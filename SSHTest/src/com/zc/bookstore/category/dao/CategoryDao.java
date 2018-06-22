package com.zc.bookstore.category.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.zc.bookstore.basedao.BaseDao;
import com.zc.bookstore.category.domain.Category;
import com.zc.bookstore.utils.OtherUtil;

public class CategoryDao extends BaseDao{
	
	/**
	 * 返回所有的Category
	 * @return
	 */
	public List<Category> findAll(){
		String SQL = "SELECT * FROM t_category WHERE pid = :pid";
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		//得到一级分类(pid为null)
		List<Category> firstCategory = findFirstCategory();
		for(Category category : firstCategory){
			//得到二级分类(根据pid)
			List secondCategorys = session.createSQLQuery(SQL)
					.addEntity(Category.class)
					.setString("pid", category.getCid())
					.list();
			//将所有的二级目录中设置对应的一级目录
			for(int i = 0; i < secondCategorys.size(); i++){
				((Category)(secondCategorys.get(i))).setParent(category);
			}
			//将设置好一级目录的二级目录重新赋给一级目录
			category.setChildren(secondCategorys);
		}
		
		return firstCategory;
	}

	/**
	 * 添加一级分类
	 * @param category
	 */
	public void addFirstCategory(Category category){
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		session.save(category);
		tx.commit();
		session.close();
	}
	
	/**
	 * 获取所有的一级分类
	 * @return
	 */
	public List<Category> findFirstCategory(){
		String SQL = "SELECT * FROM t_category WHERE pid is null order by orderBy";
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		List firstCategory = session.createSQLQuery(SQL)
				.addEntity(Category.class)
				.list();
		tx.commit();
		session.close();
		return firstCategory;
	}
	
	/**
	 * 加载分类
	 * @param cid
	 * @return
	 */
	public Category load(String cid){
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		Category category = (Category)session.get(Category.class, cid);
		tx.commit();
		session.close();
		return category;
	}
	
	/**
	 * 修改分类
	 * @param category
	 */
	public void editCategory(Category category){
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		System.out.println(category.getCid());
		Category loadCategory = (Category)session.get(Category.class, category.getCid());
		loadCategory = OtherUtil.replaceAll(loadCategory, category);
		session.update(loadCategory);
		tx.commit();
		session.close();
	}
	
	/**
	 * 一级分类下二级分类的个数
	 * @param pid
	 * @return
	 */
	public int findSecondCategoryCountByPid(String pid){
		String SQL = "SELECT COUNT(*) FROM t_category WHERE pid = :pid";
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		Number cnt = (Number)session.createSQLQuery(SQL)
				.setString("pid", pid)
				.uniqueResult();
		return cnt == null ? 0 : cnt.intValue();
	}
	
	/**
	 * 删除指定的分类(删除的是一级分类，二级分类也要一起删除)
	 * @param cid
	 */
	public void deleteCategory(String cid){
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		//先加载指定cid的category
		Category category = (Category)session.load(Category.class, cid);
		if(category.getPid() == null){
			//参数cid是一个一级分类的分类id(也就是二级分类的pid), 得到所有pid对应的二级分类
			//直接调用findSecondCategory()方法
			List secondCategorys = findSecondCategory(cid);
			List<String> secondCategoryIds = new ArrayList<String>();
			for(Category secondCategory : (List<Category>)secondCategorys){
				secondCategoryIds.add(secondCategory.getCid());
			}
			for(String secondCategoryId : secondCategoryIds){
				Category secondCategory = (Category)session.load(Category.class, secondCategoryId);
				session.delete(secondCategory);
			}
		}
		
		session.delete(category);
		tx.commit();
		session.close();
	}
	
	/**
	 * 加载指定一级分类下的所有二级分类
	 * @param pid
	 * @return
	 */
	public List<Category> findSecondCategory(String pid){
		String SQL = "SELECT * FROM t_category WHERE pid = :pid order by orderBy";
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		List secondCategorys = session.createSQLQuery(SQL)
				.addEntity(Category.class)
				.setString("pid", pid)
				.list();
		return secondCategorys;
	}
}
