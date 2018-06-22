package com.zc.bookstore.admin.category;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.zc.bookstore.baseaction.BaseAction;
import com.zc.bookstore.category.domain.Category;
import com.zc.bookstore.category.service.CategoryService;
import com.zc.bookstore.utils.CommonUtil;

public class AdminCategoryAction extends BaseAction  implements ServletRequestAware, ServletResponseAware{

	private static final long serialVersionUID = 1L;
	
	private HttpServletRequest request;
	private HttpServletResponse response;
	private CategoryService  categoryService;
	
	public void setCategoryService(CategoryService categoryService) {
		this.categoryService = categoryService;
	}
	
	public CategoryService getCategoryService() {
		return categoryService;
	}
	
	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}
	
	private String toJson(Category category) {
		StringBuilder sb = new StringBuilder("{");
		sb.append("\"cid\":").append("\"").append(category.getCid()).append("\",");
		sb.append("\"cname\":").append("\"").append(category.getCname()).append("\"");
		sb.append("}");
		return sb.toString();
	}
	
	private String toJsonArray(List<Category> categoryList) {
		StringBuilder sb = new StringBuilder("[");
		int size = categoryList.size();
		for(int i = 0; i < size; i++) {
			sb.append(toJson(categoryList.get(i)));
			if(i < size - 1) {
				sb.append(",");
			}
		}
		sb.append("]");
		return sb.toString();
	}
	
	/**
	 * 异步请求，查询指定1级分类下的所有2级分类
	 * @return
	 */
	public String childrenForAjax() {
		String pid = request.getParameter("pid");
		List<Category> children = categoryService.findChildren(pid);
		String json = toJsonArray(children);
		try {
			response.setCharacterEncoding("UTF-8");
			response.getWriter().print(json);
			response.flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 查询所有分类（为图书管理模块）
	 * @return
	 */
	public String findAllForBook() {
		request.setAttribute("parents", categoryService.findAll());
		return "success";
	}
	
	/**
	 * 查看所有分类
	 * @return
	 */
	public String findAll() {
		request.setAttribute("cList", categoryService.findAll());
		return "success";
	}
	
	/**
	 * 添加一级分类
	 * @return
	 */
	public String addOneLevel() {
		Map<String, String[]> params = request.getParameterMap();
		Category parent = new Category();
		parent.setCid(CommonUtil.uuid());
		parent.setCname(params.get("cname")[0]);
		parent.setDetail(params.get("detail")[0]);
		parent.setPid(null);;
		categoryService.add(parent);
		return findAll();
	}
	
	/**
	 * 添加二级分类：第一步
	 * @return
	 */
	public String addTwoLevelPre() {
		String pid = request.getParameter("pid");
		request.setAttribute("parents", categoryService.findFirstCategory());
		request.setAttribute("pid", pid);
		return "success";
	}
	
	/**
	 * 添加二级分类：第二步
	 * @return
	 */
	public String addTwoLevel() {
		Map<String, String[]> params = request.getParameterMap();
		Category child = new Category();
		child.setCid(CommonUtil.uuid());
		child.setCname(params.get("cname")[0]);
		child.setDetail(params.get("detail")[0]);
		child.setPid(request.getParameter("pid"));
		categoryService.add(child);
		return findAll();
	}
	
	/**
	 * 修改一级分类：第一步
	 * @return
	 */
	public String editOneLevelPre() {
		String cid = request.getParameter("cid");
		request.setAttribute("category", categoryService.load(cid));
		return "success";
	}
	
	/**
	 * 修改一级分类：第二步
	 * @return
	 */
	public String editOneLevel() {
		Map<String, String[]> params = request.getParameterMap();
		Category parent = new Category();
		parent.setCid(params.get("cid")[0]);
		parent.setCname(params.get("cname")[0]);
		parent.setDetail(params.get("detail")[0]);
		categoryService.edit(parent);
		return findAll();
	}
	
	/**
	 * 修改二级分类：第一步
	 * @return
	 */
	public String editTwoLevelPre() {
		String cid = request.getParameter("cid");
		request.setAttribute("child", categoryService.load(cid));
		request.setAttribute("parents", categoryService.findFirstCategory());
		return "success";
	}
	
	/**
	 * 修改二级分类：第二步
	 * @return
	 */
	public String editTwoLevel() {
		Map<String, String[]> params = request.getParameterMap();
		Category child = new Category();
		child.setCid(params.get("cid")[0]);
		child.setCname(params.get("cname")[0]);
		child.setDetail(params.get("detail")[0]);
		child.setPid(params.get("pid")[0]);		
		categoryService.edit(child);
		return findAll();
	}

	/**
	 * 删除一级分类
	 * @return
	 */
	public String deleteOneLevel() {
		String cid = request.getParameter("cid");
		categoryService.deleteOneLevel(cid);
		return findAll();
	}
	
	/**
	 * 删除二级分类
	 * @return
	 */
	public String deleteTwoLevel() {
		String cid = request.getParameter("cid");
		categoryService.deleteSecondCategory(cid);
		return findAll();
	}

}
