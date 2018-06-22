package com.zc.bookstore.cart.action;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.zc.bookstore.baseaction.BaseAction;
import com.zc.bookstore.book.domain.Book;
import com.zc.bookstore.book.service.BookService;
import com.zc.bookstore.cart.domain.CartItem;
import com.zc.bookstore.cart.service.CartItemService;
import com.zc.bookstore.user.domain.User;

public class CartItemAction extends BaseAction implements ServletRequestAware, ServletResponseAware{

	private static final long serialVersionUID = 1L;

	private CartItemService cartItemService;
	private BookService bookService;
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	public void setCartItemService(CartItemService cartItemService) {
		this.cartItemService = cartItemService;
	}
	
	public CartItemService getCartItemService() {
		return cartItemService;
	}
	
	public void setBookService(BookService bookService) {
		this.bookService = bookService;
	}
	
	public BookService getBookService() {
		return bookService;
	}
	
	/**
	 * 我的购物车
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String myCart() {
		/*
		 * 1. 获取session中的user，并购物user的uid
		 */
		User user = (User)request.getSession().getAttribute("sessionUser");
//		System.out.println(user.getEmail());
		String uid = user.getUid();
		/*
		 * 2. 调用service的myCart(String uid)方法获取当前用户的所有购物车条目 
		 */
		List<CartItem> cartItemList = cartItemService.myCart(uid);
		/*
		 * 3. 保存到request中，转发到/jsps/cart/list.jsp
		 */
		request.setAttribute("cartItemList", cartItemList);
		return "success";
	}
	
	/**
	 * 添加购物车条目
	 * @return
	 */
	public String add() {
		/*
		 * 1. 封装表单中的bid到Book对象中 
		 */
		Book book = new Book();
		book.setBid(request.getParameter("bid"));
		/*
		 * 2. 从session中获取当前用户
		 */
		User owner = (User)request.getSession().getAttribute("sessionUser");
		/*
		 * 3. 把表单中的quantity封装到CartItem中
		 */
		CartItem cartItem = new CartItem();
		cartItem.setQuantity(Integer.parseInt(request.getParameter("quantity")));
		/*
		 * 4. 设置book
		 *    设置owner
		 */
		cartItem.setBook(book);
		cartItem.setOwner(owner);
		
		/*
		 * 5. 把cartItem添加到数据库
		 */
		cartItemService.add(cartItem);
		/*
		 * 6. 调用myCart()，即查询所有条目，保存到request中，转发到/jsps/cart/list.jsp
		 */
		return myCart();
	}
	
	/**
	 * 批量删除购物车条目
	 * 也能删除一个条目
	 * @return
	 */
	public String delete(){
		String cartItemIds = (String)(request.getParameter("cartItemIds"));
		String[] cartItemIdArray = cartItemIds.split(",");
		List<String> list = (List<String>)Arrays.asList(cartItemIdArray);
		cartItemService.deleteBatch(list);
		return myCart();
	}
	
	/**
	 * 修改条目数量
	 * @return
	 */
	public String updateQuantity(){
		/*
		 * 1. 获取cartItemId和quantity
		 */
		String cartItemId = request.getParameter("cartItemId");
		int quantity = Integer.parseInt(request.getParameter("quantity"));
		/*
		 * 2. 调用cartItemService#updateQuantity()方法修改数量
		 */
		CartItem cartItem = new CartItem();
		cartItem.setCartItemId(cartItemId);
		cartItem.setQuantity(quantity);
		cartItem.setOwner((User)request.getSession().getAttribute("sessionUser"));
		cartItem.setBook(bookService.load(request.getParameter("bid")));
		cartItemService.updateQuantity(cartItem);
		/*
		 * 3. 通过cartItemId加载CartItem对象
		 
		CartItem cartItem2 = cartItemService.load(cartItemId);
		
		 * 4. 把cartItem对象的quantity和subtotal封装成json对象返回
		 
//		String result = "{\"quantity\":" + quantity + ", \"subtotal\":" + cartItem2.getSubtotal() + "}";
		Map<String,Object> jsonResult = new HashMap<String, Object>();
		jsonResult.put("quantity", quantity + "");
		jsonResult.put("subtotal", cartItem2.getSubtotal() + "");*/
		return myCart();
//		try {
//			response.getWriter().print(result);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return null;
	}
	
	/**
	 * 判断到底是添加条目还是改变条目中商品数量
	 * @return
	 */
	public String judgeAddOrUpdateQuantity(){
		String bid = request.getParameter("bid");
		if(hasCartItemBy(bid)){
			updateQuantity();
		}else{
			add();
		}
		return "success";
	}
	
	/**
	 * 是否存在相同bid的条目
	 * @param bid
	 * @return
	 */
	public boolean hasCartItemBy(String bid){
		return cartItemService.hasCartItemBy(bid);
	}
	
	/**
	 * 加载多个条目
	 * @return
	 */
	public String loadCartItems() {
		String cartItemIds = request.getParameter("cartItemIds");
		System.out.println(cartItemIds);
		String[] cartItemIdArray = cartItemIds.split(",");
		List<String> list = (List<String>)Arrays.asList(cartItemIdArray);
		request.setAttribute("cartItemList", cartItemService.loadCartItems(list));
		request.setAttribute("cartItemIds", cartItemIds);
		return "success";
	}

	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}
}
