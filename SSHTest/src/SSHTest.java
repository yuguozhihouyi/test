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
	}

}
