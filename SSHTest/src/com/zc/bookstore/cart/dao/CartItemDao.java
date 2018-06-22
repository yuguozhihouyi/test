package com.zc.bookstore.cart.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.zc.bookstore.basedao.BaseDao;
import com.zc.bookstore.book.domain.Book;
import com.zc.bookstore.cart.domain.CartItem;
import com.zc.bookstore.user.domain.User;
/**
 * t_cartItem表的Dao实现
 * @author Administrator
 *
 */
public class CartItemDao extends BaseDao{

	/**
	 * 用户id得到购物车条目集合
	 * @param uid
	 * @return
	 */
	public List<CartItem> findByUser(String uid){
		String SQL = "SELECT ci.*, b.* FROM t_cartitem ci, t_book b WHERE ci.bid = b.bid AND ci.uid = :uid ORDER BY ci.orderBy";
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		List list = session.createSQLQuery(SQL)
				.addEntity("ci", CartItem.class)
				.addJoin("b", "ci.book")
				.setString("uid", uid)
				.list();
		List<CartItem> cartItems = new ArrayList<CartItem>();
		for(Object obj : list){
			Object[] temp = (Object[])obj;
			CartItem cartItem = (CartItem)temp[0];
			Book book = (Book)temp[1];
			cartItem.setBook(book);
			cartItems.add(cartItem);
		}
		return cartItems;
	}
	
	/**
	 * 添加购物车条目
	 * @param cartItem
	 */
	public void addCartItem(CartItem cartItem){
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		session.save(cartItem);
		tx.commit();
		session.close();
	}
	
	/**
	 * 在已有条目上添加图书数量时使用，获取已有条目，更新条目中的图书数量（update）
	 * @param uid
	 * @param bid
	 */
	public void addNumberInSameCartItem(String uid, String bid, int quantity){
		String SQL ="UPDATE t_cartItem SET quantity = :quantity WHERE uid = :uid AND bid = :bid";
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		session.createSQLQuery(SQL)
			.setInteger("quantity", quantity + 1)
			.setString("uid", uid)
			.setString("bid", bid)
			.executeUpdate();
		tx.commit();
		session.close();
	}
	
	/**
	 * 批量删除t_cartItem中的条目
	 * @param cartItemId
	 */
	public void deleteBatch(List<String> cartItemIds){
		//拼接批量删除的SQL语句
		StringBuilder SQL = new StringBuilder("DELETE FROM CartItem ci WHERE ci.cartItemId IN (");
		for(int i = 0; i < cartItemIds.size(); i++){
			String cartItemId = cartItemIds.get(i);
			if(i == cartItemIds.size() - 1){
				SQL.append("'" + cartItemId + "'");
			}else{
				SQL.append("'" + cartItemId + "',");
			}
		}
		SQL.append(")");
		
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		session.createQuery(SQL.toString()).executeUpdate();
		tx.commit();
		session.close();
	}
	
	/**
	 * 加载cartItem条目
	 * @param cartItemId
	 * @return
	 */
	public CartItem load(String cartItemId){
		String SQL = "SELECT ci.*, b.* FROM t_cartitem ci, t_book b WHERE ci.bid = b.bid AND ci.cartItemId = :cartItemId";
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		List list = session.createSQLQuery(SQL)
				.addEntity("ci", CartItem.class)
				.addJoin("b", "ci.book")
				.setString("cartItemId", cartItemId)
				.list();
		tx.commit();
		session.close();
		if(list.size() == 0){
			return null;
		}
		Object[]temp = (Object[])list.get(0);
		CartItem cartItem = (CartItem)temp[0];
		Book book = (Book)temp[1];
		cartItem.setBook(book);
		return cartItem;
	}
	
	/**
	 * 加载多条购物车记录
	 * @param cartItemIds
	 * @return
	 */
	public List<CartItem> loadCartItems(List<String> cartItemIds){
		StringBuilder SQL = new StringBuilder("SELECT * From t_cartitem ci WHERE ci.cartItemId in(");
		for(int i = 0; i < cartItemIds.size(); i++){
			String cartItemId = cartItemIds.get(i);
			if(i == cartItemIds.size() - 1){
				SQL.append("'" + cartItemId + "'");
			}else{
				SQL.append("'" + cartItemId + "',");
			}
		}
		SQL.append(")");
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		List list = session.createSQLQuery(SQL.toString())
				.addEntity(CartItem.class)
				.list();
		tx.commit();
		session.close();
		/*//拼接SQL语句
		StringBuilder SQL = new StringBuilder("SELECT ci.*, b.* FROM t_cartitem ci, t_book b WHERE ci.bid = b.bid IN (");
		for(int i = 0; i < cart.size(); i++){
			CartItem cart1 = cart.get(i);
			if(i == cart.size() - 1){
				SQL.append("'" + cart1.getBook().getBid() + "'");
			}else{
				SQL.append("'" + cart1.getBook().getBid() + "',");
			}
		}
		SQL.append(")");
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		List list = session.createSQLQuery(SQL.toString())
				.addEntity("ci", CartItem.class)
				.addJoin("b", "ci.book")
				.list();
		List<CartItem> cartItems = new ArrayList<CartItem>();
		for(Object obj : list){
			Object[] temp = (Object[])obj;
			CartItem cartItem = (CartItem)temp[0];
			Book book = (Book)temp[1];
			cartItem.setBook(book);
			cartItems.add(cartItem);
		}
		for(CartItem cartItem : cartItems){
			System.out.println(cartItem.getCartItemId() + cartItem.getBook().getBname());
		}*/
		return list;
	}
	
	/**
	 * 得到条目数量
	 * @param cartItem
	 * @return
	 */
	public int cartItemNumberByCartItemId(CartItem cartItem){
		String SQL = "SELECT COUNT(*) FROM t_cartitem WHERE cartItemId = :cartItemId";
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		Number cnt = (Number)session.createSQLQuery(SQL)
				.setString("cartItemId", cartItem.getCartItemId())
				.uniqueResult();
		
		return cnt.intValue();
	}
	
	/**
	 * 是否存在相同bid的条目
	 * @param bid
	 * @return
	 */
	public boolean hasCartItemBy(String bid){
		String SQL = "SELECT COUNT(*) FROM t_cartitem WHERE bid = :bid";
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		Number cnt = (Number)(session.createSQLQuery(SQL)
				.setString("bid", bid)
				.uniqueResult());
		
		return cnt.intValue() == 0 ? false :true;
	}
}
