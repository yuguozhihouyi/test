package com.zc.bookstore.admin.book;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.zc.bookstore.baseaction.BaseAction;
import com.zc.bookstore.book.domain.Book;
import com.zc.bookstore.book.service.BookService;
import com.zc.bookstore.category.domain.Category;
import com.zc.bookstore.category.service.CategoryService;
import com.zc.bookstore.utils.CommonUtil;

public class AdminAddBookAction extends BaseAction implements ServletRequestAware, ServletResponseAware{

	private static final long serialVersionUID = 1L;
	
	private HttpServletRequest request;
	private HttpServletResponse response;
	private CategoryService categoryService;
	private BookService bookService;
	
	public void setCategoryService(CategoryService categoryService) {
		this.categoryService = categoryService;
	}
	
	public CategoryService getCategoryService() {
		return categoryService;
	}
	
	public void setBookService(BookService bookService) {
		this.bookService = bookService;
	}
	
	public BookService getBookService() {
		return bookService;
	}
	
	public String addBook(){
		try {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("text/html;charset=utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload  sfu = new ServletFileUpload(factory);
		try {
			List<FileItem> itemList = sfu.parseRequest(request);
			Book book = new Book();
			book.setBname(itemList.get(0).getString("UTF-8"));
//			book.setBname(request.getParameter("bname"));
			FileItem image_w = itemList.get(1);
//			String image_w_name = request.getParameter("image_w");
//			FileInputStream fis = new FileInputStream(new File(image_w_name));
			String image_w_name = image_w.getName();
			File file = new File(image_w_name);
			int index = image_w_name.lastIndexOf("\\");
			if(index > 0){
				image_w_name = image_w_name.substring(index + 1);
			}
			if(!image_w_name.toLowerCase().endsWith("jpg") && !image_w_name.toLowerCase().endsWith("png") &&
					!image_w_name.toLowerCase().endsWith("bmp")){
				request.setAttribute("parents", categoryService.findFirstCategory());
				request.setAttribute("msg", "图片的扩展名只能是：jpg、png、bmp格式");
				return "error";
			}
			FileInputStream fis = new FileInputStream(file);
			String bookSavePath = request.getServletContext().getRealPath("/book_img");
			image_w_name = CommonUtil.uuid() + "_" +image_w_name;
			File fileTarget = new File(bookSavePath, image_w_name);
//			image_w.write(file);
			FileOutputStream fos = new FileOutputStream(fileTarget);
			int len = 0;
			byte[] buf = new byte[1024];
			while((len = fis.read(buf, 0, buf.length)) != -1){
				fos.write(buf, 0, len);
			}
			if(fos != null){
				fos.close();
			}
			if (fis != null) {
				fis.close();
			}
			book.setImage_w("book_img/" + image_w_name);
			
			FileItem image_b = itemList.get(2);
//			String image_b_name = request.getParameter("image_b");
//			fis = new FileInputStream(new File(image_b_name));
			
			String image_b_name = image_b.getName();
			File file2 = new File(image_b_name);
			index = image_b_name.lastIndexOf("\\");
			if(index > 0){
				image_b_name = image_b_name.substring(index + 1);
			}
			if(!image_b_name.toLowerCase().endsWith("jpg") && !image_b_name.toLowerCase().endsWith("png") &&
					!image_b_name.toLowerCase().endsWith("bmp")){
				request.setAttribute("parents", categoryService.findFirstCategory());
				request.setAttribute("msg", "图片的扩展名只能是：jpg、png、bmp格式");
				return "error";
			}
			fis = new FileInputStream(file2);
			image_b_name = CommonUtil.uuid() + image_b_name;
			File fileTarget2 = new File(bookSavePath, image_b_name);
//			image_b.write(file);
			fos = new FileOutputStream(fileTarget2);
			len = 0;
			buf = new byte[1024];
			while((len = fis.read(buf, 0, buf.length)) != -1){
				fos.write(buf, 0, len);
			}
			if(fos != null){
				fos.close();
			}
			if (fis != null) {
				fis.close();
			}
			book.setImage_b("book_img/" + image_b_name);
			
			book.setCurrPrice(Double.parseDouble(itemList.get(3).getString()));//设置当前价
//			book.setCurrPrice(Double.parseDouble(request.getParameter("price")));//设置当前价
			book.setPrice(Double.parseDouble(itemList.get(4).getString()));//设置定价
//			book.setCurrPrice(Double.parseDouble(request.getParameter("disprice")));//设置定价
			book.setDiscount(Double.parseDouble(itemList.get(5).getString()));//设置折扣
//			book.setCurrPrice(Double.parseDouble(request.getParameter("discount")));//设置折扣
			book.setAuthor(itemList.get(6).getString("utf-8"));//设置作者
//			book.setAuthor(request.getParameter("author"));//设置作者
			book.setPress(itemList.get(7).getString("utf-8"));//设置出版社
//			book.setPress(request.getParameter("press"));//设置出版社
			book.setPublishtime(itemList.get(8).getString("utf-8"));//设置出版时间
//			book.setPublishtime(request.getParameter("publishtime"));//设置出版时间
			book.setEdition(itemList.get(9).getString("utf-8"));//设置版次
//			book.setEdition(request.getParameter("edition"));//设置版次
			book.setPageNum(Integer.parseInt(itemList.get(10).getString("utf-8")));//设置页数
//			book.setPageNum(Integer.parseInt(request.getParameter("pageNum")));//设置页数
			book.setWordNum(Integer.parseInt(itemList.get(11).getString("utf-8")));//设置字数
//			book.setWordNum(Integer.parseInt(request.getParameter("wordNum")));//设置字数
			book.setPrinttime(itemList.get(12).getString("utf-8"));//设置印刷时间
//			book.setPrinttime(request.getParameter("printtime"));//设置印刷时间
			book.setBooksize(Integer.parseInt(itemList.get(13).getString("utf-8")));//设置开本
//			book.setBooksize(Integer.parseInt(request.getParameter("booksize")));//设置开本
			book.setPaper(itemList.get(14).getString("utf-8"));//设置纸质
//			book.setPaper(request.getParameter("paper"));//设置纸质
			
//			Category category = new Category();
			Category category = categoryService.load(itemList.get(16).getString());
//			category.setCid(itemList.get(16).getString());
//			category.setCid(request.getParameter("cid"));
			book.setCategory(category);
			
			bookService.addBookInfo(book);
			request.setAttribute("msg", "添加图书新信息成功");
			return "success";
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			if(e instanceof FileUploadBase.FileSizeLimitExceededException){
				request.setAttribute("msg", "图片大小超出限制");
				return "error";
			}else if(e instanceof FileUploadBase.SizeLimitExceededException){
				request.setAttribute("msg", "请求大小超出限制");
				return "error";
			}else{
				e.printStackTrace();
				return "error";
			}
		}
		return "success";
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

}
