package com.zc.bookstore.cart.service;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.jdbc.support.JdbcUtils;

import com.zc.bookstore.cart.dao.CartItemDao;
import com.zc.bookstore.cart.domain.CartItem;
import com.zc.bookstore.user.domain.User;

public class CartItemService {

	private CartItemDao cartItemDao;
	
	public void setCartItemDao(CartItemDao cartItemDao) {
		this.cartItemDao = cartItemDao;
	}
	
	public CartItemDao getCartItemDao() {
		return cartItemDao;
	}
	
	/**
	 * 我的购物车
	 * @param uid
	 * @return
	 */
	public List<CartItem> myCart(String uid) {
		return cartItemDao.findByUser(uid);
	}

	/**
	 * 添加购物车条目
	 * @param cartItem
	 */
	public void add(CartItem cartItem) {
		/*
		 * 查询这个条目是否已经存在，如果存在，那么就合并条目，而不是添加条目
		 */
		int cnt = cartItemDao.cartItemNumberByCartItemId(cartItem);
		if(cnt == 0){
			cartItemDao.addCartItem(cartItem);
		}else {
			updateQuantity(cartItem);
		}
	}
	
	/**
	 * 批量删除条目
	 * @param cartItemIds
	 */
	public void deleteBatch(List<String> cartItemIds) {
		cartItemDao.deleteBatch(cartItemIds);
	}
	
	/**
	 * 修改条目数量
	 * @param cartItemId
	 * @param quantity
	 */
	public void updateQuantity(CartItem cartItem) {
		System.out.println(cartItem);
		cartItemDao.addNumberInSameCartItem(cartItem.getOwner().getUid(), cartItem.getBook().getBid(), cartItem.getQuantity());
	}
	
	/**
	 * 加载CartItem
	 * @param cartItemId
	 * @return
	 */
	public CartItem load(String cartItemId) {
		return cartItemDao.load(cartItemId);
	}

	/**
	 * 加载多个条目
	 * @param cartItemIds
	 * @return
	 */
	public List<CartItem> loadCartItems(List<String> cartItemIds) {
		return cartItemDao.loadCartItems(cartItemIds);
	}
	
	/**
	 * 是否存在相同bid的条目
	 * @param bid
	 * @return
	 */
	public boolean hasCartItemBy(String bid){
		return cartItemDao.hasCartItemBy(bid);
	}
}
