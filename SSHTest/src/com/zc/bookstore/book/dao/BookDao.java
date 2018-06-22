package com.zc.bookstore.book.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.zc.bookstore.basedao.BaseDao;
import com.zc.bookstore.book.domain.Book;
import com.zc.bookstore.category.domain.Category;
import com.zc.bookstore.constants.PageConstants;
import com.zc.bookstore.utils.OtherUtil;
import com.zc.bookstore.utils.SQLStringUtil;
import com.zc.pager.PageBean;

public class BookDao extends BaseDao{
	
	/**
	 * 分类查询(cid)
	 * @param cid
	 * @param pc
	 * @return 分装的PageBean(当前分页显示的信息)
	 */
	public PageBean<Book> findByCategory(String cid, int pc){
		Map<String, Object> criteria = new HashMap<>();
		criteria.put("cid", cid);
		return findByCriteria(criteria, pc);
	}
	
	/**
	 * 按作者查询
	 * @param author
	 * @param pc
	 * @return
	 */
	public PageBean<Book> findByAuthor(String author, int pc){
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("author", author);
		return findByCriteria(criteria, pc);
	}
	
	/**
	 * 出版社查询
	 * @param press
	 * @param pc
	 * @return
	 */
	public PageBean<Book> findByPress(String press, int pc){
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("press", press);
		return findByCriteria(criteria, pc);
	}
	
	/**
	 * 按书名分类
	 * @param bookName
	 * @param pc
	 * @return
	 */
	public PageBean<Book> findByBookName(String bookName, int pc){
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("bname", bookName);
		return findByCriteria(criteria, pc);
	}
	
	/**
	 * 多条件组合查询
	 * @param book
	 * @param pc
	 * @return
	 */
	public PageBean<Book> findByCombination(Book book, int pc){
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("bname", book.getBname());
		criteria.put("author", book.getAuthor());
		criteria.put("press", book.getPress());
		return findByCriteria(criteria, pc);
	}
	
	/**
	 * 加载图书信息
	 * @param bid
	 * @return
	 */
	public Book load(String bid){
		String SQL = "SELECT b.*, c.* FROM (SELECT * FROM t_book WHERE bid = :bid) as b , (SELECT * FROM t_category) as c WHERE b.cid = c.cid";
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		List bookAndCategoryComb = session.createSQLQuery(SQL)
				.addEntity("b", Book.class)
				.addJoin("c", "b.category")
				.setString("bid", bid)
				.list();
		tx.commit();
		session.close();
		Book book = null;
		for(Object obj : bookAndCategoryComb){
			Object[] objs = (Object[])obj;
			book = (Book)objs[0];
			Category category = (Category)objs[1];
			book.setCategory(category);
		}
		return book;
	}
	
	/**
	 * 根据商品二级目录找到对应的一级目录
	 * @param cid
	 * @return
	 */
	public String findPidByBookCid(String cid){
		String SQL = "SELECT * FROM t_category WHERE cid = :cid";
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		List categories = session.createSQLQuery(SQL)
		.addEntity(Category.class)
		.setString("cid", cid)
		.list();
		tx.commit();
		if(categories.size() != 0){
			return ((Category)categories.get(0)).getPid();
		}
		return null;
	}
	
	/**
	 * 条件查询商品信息(分页查询)
	 * @param criteria 条件信息的封装
	 * @param pc 当前约束
	 * @return
	 */
	public PageBean<Book> findByCriteria(Map<String, Object> criteria, int pc){
		String appendLanguageByFlag = null;
		Set<String> keySet = criteria.keySet();
		if(keySet.contains("cid")){
			appendLanguageByFlag = " AND b.category.";
		}else{
			appendLanguageByFlag = " AND b.";
		}
		//拼接SQL查询条件
		List<Object> params = new ArrayList<Object>();
		StringBuilder whereSQL = new StringBuilder(" WHERE 1=1");
		for(String name : criteria.keySet()){
			Object value = criteria.get(name);
			if(value == null){
				continue;
			}
			whereSQL.append(appendLanguageByFlag).append(name).append(" LIKE ?");
			params.add("'%" + value + "%'");
		}
		
		//排序
		String orderBySQL = " order by b.orderBy";
		String finalWhereSQL = SQLStringUtil.appendSQL(params, whereSQL.toString());
		//总记录数
		String cntSQL = "SELECT count(*) FROM Book b" + finalWhereSQL;
		System.out.println(cntSQL);
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		Number cnt = (Number)session.createQuery(cntSQL).uniqueResult();
		int tr = cnt == null ? 0 : cnt.intValue();
		//最终SQL语句
		String SQL = "SELECT b FROM Book b" + finalWhereSQL + orderBySQL;
		System.out.println(SQL);
		//limit参数
		int ps = PageConstants.BOOK_PAGE_SIZE;
		int firstPosition = ps * (pc - 1);
		
		//条件查询图书信息
		List books = session.createQuery(SQL)
				.setFirstResult(firstPosition)
				.setMaxResults(ps)
				.list();
		
		tx.commit();
		session.close();
		
		//封装PageBean 
		PageBean<Book> pb = new PageBean<Book>();
		pb.setPc(pc);
		pb.setPs(ps);
		pb.setTr(tr);
		pb.setDataList(books);
		
		return pb;
	}
	
	/**
	 * 查询指定分类下的图书个数
	 * @param cid
	 * @return
	 */
	public int countByCategory(String cid){
		String SQL = "SELECT count(*) FROM t_book WHERE cid = :cid";
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		Number count = (Number)session.createSQLQuery(SQL)
				.setString("cid", cid)
				.uniqueResult();
		tx.commit();
		session.close();
		return count == null ? 0 : count.intValue();
	}
	
	/**
	 * 更新数据表内容
	 * @param book
	 */
	public void editBookInfo(Book book){
//		String SQL = "UPDATE t_book SET bname=?,author=?,price=?,currPrice=?," +
//				"discount=?,press=?,publishtime=?,edition=?,pageNum=?,wordNum=?," +
//				"printtime=?,booksize=?,paper=?,cid=? WHERE bid=?";
//		String finalSQL = SQLStringUtil.appendUpdateSQL(SQL, book);
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		Book bookByDB = (Book)session.load(Book.class, book.getBid());
		bookByDB = OtherUtil.replaceAll(bookByDB, book);
		session.save(bookByDB);
		tx.commit();
		session.close();
	}
	
	/**
	 * 删除图书
	 * @param bid
	 */
	public void deleteBookInfo(String bid){
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		Book bookByDB = (Book)session.load(Book.class, bid);
		session.delete(bookByDB);
		tx.commit();
		session.close();
	}
	
	/**
	 * 添加图书
	 * @param book
	 */
	public void addBookInfo(Book book){
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		session.save(book);
		tx.commit();
		session.close();
	}
}
