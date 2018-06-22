<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>My JSP 'bookdesc.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<meta http-equiv="content-type" content="text/html;charset=utf-8">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<link rel="stylesheet" type="text/css" href="<c:url value='/adminjsps/admin/css/book/add.css'/>">
<link rel="stylesheet" type="text/css" href="<c:url value='/jquery/jquery.datepick.css'/>">
<script type="text/javascript" src="<c:url value='/jquery/jquery-1.5.1.js'/>"></script>
<script type="text/javascript" src="<c:url value='/jquery/jquery.datepick.js'/>"></script>
<script type="text/javascript" src="<c:url value='/jquery/jquery.datepick-zh-CN.js'/>"></script>
<script type="text/javascript">
$(function () {
	$("#publishtime").datepick({dateFormat:"yy-mm-dd"});
	$("#printtime").datepick({dateFormat:"yy-mm-dd"});
	
	$("#btn").addClass("btn1");
	$("#btn").hover(
		function() {
			$("#btn").removeClass("btn1");
			$("#btn").addClass("btn2");
		},
		function() {
			$("#btn").removeClass("btn2");
			$("#btn").addClass("btn1");
		}
	);
	
	$("#btn").click(function() {
		var bname = $("#bname").val();
		var currPrice = $("#currPrice").val();
		var price = $("#price").val();
		var discount = $("#discount").val();
		var author = $("#author").val();
		var press = $("#press").val();
		var pid = $("#pid").val();
		var cid = $("#cid").val();
		var image_w = $("#image_w").val();
		var image_b = $("#image_b").val();
		
		if(!bname || !currPrice || !price || !discount || !author || !press || !pid || !cid || !image_w || !image_b) {
			alert("图名、当前价、定价、折扣、作者、出版社、1级分类、2级分类、大图、小图都不能为空！");
			return false;
		}
		
		if(isNaN(currPrice) || isNaN(price) || isNaN(discount)) {
			alert("当前价、定价、折扣必须是合法小数！");
			return false;
		}
		$("#form").submit();
	});
	
	/*
	给1级分类下拉列表添加改变事件
	在重新选择了1级分类后，加载该1级分类的所有2级分类并显示在2级分类下拉列表中
	*/
	$("#pid").change(function() {
		var pid = $(this).val();//获取新的1级分类id
		$("#cid").empty();//清空2级分类下拉列表
		// 添加头选项
		$("#cid").append($("<option value=''>==请选择2级分类==</option>"));
		if(pid) {//如果新的1级分类不为空！
			$.ajax({
				url:"<c:url value='/admin/adminCategoryAction_childrenForAjax'/>",
				data:{method:"childrenForAjax", pid:pid},
				dataType:"json",
				type:"POST",
				async:"false",
				cache:"false",
				success:function(children) {
					// 循环遍历所有2级分类
					for(var i = 0; i < children.length; i++) {
						// 创建选项同
						var option = $("<option>").attr("value", children[i].cid).text(children[i].cname);
						// 向2级分类下拉列表中添加选项同
						$("#cid").append(option);	
					}
				}
			});
		}
	});
});

</script>
  </head>
  
  <body>
  <div>
   <p style="font-weight: 900; color: red;">${msg }</p>
   <form action="<c:url value='adminAddBookAction_addBook?method=addBook'/>" enctype="multipart/form-data" method="post" id="form">
    <div>
	    <ul>
	    	<li>书名：　<input id="bname" type="text" name="bname" style="width:500px;"/></li>
	    	<li>大图：　<input id="image_w" type="file" name="image_w"/></li>
	    	<li>小图：　<input id="image_b" type="file" name="image_b"/></li>
	    	<li>当前价：<input id="price" type="text" name="price" style="width:50px;"/></li>
	    	<li>定价：　<input id="currPrice" type="text" name="disprice" style="width:50px;"/>
	    	折扣：<input id="discount" type="text" name="discount" style="width:30px;"/>折</li>
	    </ul>
		<hr style="margin-left: 50px; height: 1px; color: #dcdcdc"/>
		<table>
			<tr>
				<td colspan="3">
					作者：　　<input type="text" id="author" name="author" style="width:150px;"/>
				</td>
			</tr>
			<tr>
				<td colspan="3">
					出版社：　<input type="text" name="press" id="press" style="width:200px;"/>
				</td>
			</tr>
			<tr>
				<td colspan="3">出版时间：<input type="text" id="publishtime" name="publishtime" style="width:100px;"/></td>
			</tr>
			<tr>
				<td>版次：　　<input type="text" name="edition" id="edition" style="width:40px;"/></td>
				<td>页数：　　<input type="text" name="pageNum" id="pageNum" style="width:50px;"/></td>
				<td>字数：　　<input type="text" name="wordNum" id="wordNum" style="width:80px;"/></td>
			</tr>
			<tr>
				<td width="250">印刷时间：<input type="text" name="printtime" id="printtime" style="width:100px;"/></td>
				<td width="250">开本：　　<input type="text" name="booksize" id="booksize" style="width:30px;"/></td>
				<td>纸张：　　<input type="text" name="paper" id="paper" style="width:80px;"/></td>
			</tr>
			<tr>
				<td>
					一级分类：<select name="pid" id="pid" onchange="loadChildren()">
						<option value="">==请选择1级分类==</option>
						<c:forEach items="${parents }" var="parent">
							<option value="${parent.cid }">${parent.cname }</option>
						</c:forEach>
					</select>
				</td>
				<td>
					二级分类：<select name="cid" id="cid" style="width: 150px;">
						<option value="">==请选择2级分类==</option>
					</select>
				</td>
				<td></td>
			</tr>
			<tr>
				<td>
					<input type="button" id="btn" name="btn" value="新书上架">
				</td>
				<td></td>
				<td></td>
			</tr>
		</table>
	</div>
   </form>
  </div>

  </body>
</html>