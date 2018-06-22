import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.zc.bookstore.book.dao.BookDao;
import com.zc.bookstore.book.domain.Book;
import com.zc.bookstore.order.dao.OrderDao;
import com.zc.bookstore.user.dao.UserDao;
import com.zc.pager.PageBean;

public class SSHTest {

	@Test
	public void test() {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext-beans.xml");
//		AdminDao ad = ctx.getBean(AdminDao.class);
//		Admin adm = ad.findAdminByAdminnameAndAdminpwd("zhengchen", "123456");
//		System.out.println(adm.getAdminname());
		BookDao bd = ctx.getBean(BookDao.class);
//		Map<String, Object> criteria = new HashMap<>();
//		criteria.put("author", "周");
//		criteria.put("price", 79.00);
		PageBean<Book> pb = bd.findByCategory("5F79D0D246AD4216AC04E9C5FAB3199E", 1);
		for(Book book : pb.getDataList()){
			System.out.println();
		}
//		PageBean<Book> pb = bd.findByCombination(new Book("Java核心技术：卷Ⅰ基础知识（原书第8版）", "昊斯特曼", "机械工业出版社"), 1);
		
//		for(Book book : pb.getDataList()){
//			System.out.println(book.getAuthor() + book.getBname() + book.getCategory().getCid());
//		}
		
//		Book book = bd.load("260F8A3594F741C1B0EB69616F65045B");
//		System.out.println(book.getBname() + " " + book.getAuthor() + " " + book.getCategory().getChildren());
		
		
//		Book book = new Book("aaa", "bbb", new Category("7", "dsds", "1", "sasasas"));
//		Session session = ((SessionFactory)ctx.getBean(SessionFactory.class)).openSession();
//		Transaction tx = session.beginTransaction();
//		session.save(book);
//		tx.commit();
//		session.close();
		
//		int count = bd.countByCategory("5F79D0D246AD4216AC04E9C5FAB3199E");
//		System.out.println(count);
		
//		CartItemDao cd = (CartItemDao)ctx.getBean("cartItemDao");
//		List<CartItem> list = cd.findByUser("32DB3700D2564254982BC58B0E4D95BC");
//		for(CartItem cartItem : list){
//			System.out.println(cartItem.getBook().getBname() + cartItem.getSubtotal() + cartItem.getOwner().getUid());
//		}
		
//		CategoryDao cd = (CategoryDao)ctx.getBean("categoryDao");
//		List<Category> list = cd.findAll();
//		for(Category category : list){
//			System.out.println(category.getChildren().get(0).getCname());
//		}
//		System.out.println(list.get(0).getChildren().get(0).getCname() + list.get(0).getChildren().get(0).getParent().getCname());
	
//		cd.addFirstCategory(new Category("8", "haha", null, "test"));
		
//		List<Category> categories  = cd.findFirstCategory();
//		for(Category category : categories){
//			System.out.println(category.getCname());
//		}
		
//		Category category = cd.load("8");
//		System.out.println(category.getCid());
		
//		cd.editCategory(new Category("8", "zz", null, "testhaha"));
		
//		int cnt = cd.findSecondCategoryCountByPid("8");
//		System.out.println(cnt);
		
//		cd.deleteCategory("8");
		
//		List<Category> categories = cd.findSecondCategory("1");
//		System.out.println(categories.get(0));
		
		UserDao ud = (UserDao)ctx.getBean("userDao");
//		System.out.println(ud.validateLoginName("sasa"));
		
//		System.out.println(ud.validateEmail("1571134208@qq.com"));
		
//		ud.addUser(new User("郑晨","zc19920308", null, null, "qe312", null, true, "dsfsdfdfsdf"));
		
//		User user = ud.findByActivationCode("dsfsdfdfsdf");
//		System.out.println(user.getLoginname());
		
//		ud.updateUserStatus("402881e65dfe8dfb015dfe8dfd820000", false);
		
//		User user = ud.findByUserNameAndUserPassword("郑晨", "zc19920308");
//		System.out.println(user.getLoginname());
		
//		ud.updatePassword("402881e65dfe8dfb015dfe8dfd820000", "zzz32132134");
		
		OrderDao od = (OrderDao)ctx.getBean("orderDao");
//		User ower = ud.findByUserNameAndUserPassword("郑晨", "zzz32132134");
//		List<OrderItem> orderItems = new ArrayList<OrderItem>();
//		Book book = bd.load("000A18FDB38F470DBE9CD0972BADB23F");
//		orderItems.add(new OrderItem(3, 23.0, book));
//		Date date = new Date();
//		DateFormat df = new SimpleDateFormat("YY-M-D HH:mm:ss");
//		System.out.println(df.format(date));
//		Order order = new Order(df.format(date), 23.0, 1, "american", ower, null);
//		od.add(order, orderItems);
		
//		PageBean<Order> pb = od.findByUser("32DB3700D2564254982BC58B0E4D95BC", 1);
//		for(Order order : pb.getDataList()){
//			System.out.println(order.getOrderItemList().size() + " " + order.getOrderItemList().get(0).getBook().getBname());
////			System.out.println(order.getOrderItemList().size());
//		}
		
//		Order order = od.load("0B657B624D8D4B919B7E5F175AA90530");
//		for(OrderItem orderItem : order.getOrderItemList()){
//			System.out.println(orderItem.getBook().getAuthor());
//		}
		
//		od.updateOrderStatus("402881e65dffdd1a015dffdd1c320000", 2);
		
//		int status = od.findStatusByOrder("402881e65dffdd1a015dffdd1c320000");
//		System.out.println(status);
		
//		PageBean<Order> pb = od.findAllOrder(1);
//		System.out.println(pb.getDataList().size());
		
//		PageBean<Order> pb = od.findAllOrderByStatus(1, 1);
//		System.out.println(pb.getDataList().size());
		
//		User user = ud.findByUserNameAndUserPassword("sasa", "123456");
		
//		CommonUtil.sendMail(user);
	}

}
