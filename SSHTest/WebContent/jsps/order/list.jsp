<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>订单列表</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<meta http-equiv="content-type" content="text/html;charset=utf-8">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<link rel="stylesheet" type="text/css" href="<c:url value='/jsps/css/order/list.css'/>" />
	<link rel="stylesheet" type="text/css" href="<c:url value='/jsps/pager/pager.css'/>" />
    <script type="text/javascript" src="<c:url value='/jsps/pager/pager.js'/>"></script>
  </head>
  
  <body>
<div class="divMain">
	<div class="divTitle">
		<span style="margin-left: 150px;margin-right: 280px;">商品信息</span>
		<span style="margin-left: 40px;margin-right: 38px;">金额</span>
		<span style="margin-left: 50px;margin-right: 40px;">订单状态</span>
		<span style="margin-left: 50px;margin-right: 50px;">操作</span>
	</div>
	<br/>
	<table align="center" border="0" width="100%" cellpadding="0" cellspacing="0">


<c:forEach items="${pb.dataList }" var="order">
		<tr class="tt">
			<td width="320px">订单号：<a  href="<c:url value='/OrderServlet?method=load&oid=${order.oid }&oper=desc'/>">${order.oid }</a></td>
			<td width="200px">下单时间：${order.ordertime }</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
		</tr>
		<tr style="padding-top: 10px; padding-bottom: 10px;">
			<td colspan="2">


<c:forEach items="${order.orderItemList }" var="item">
	<a class="link2" href="<c:url value='/book/bookAction_load?method=load&bid=${item.book.bid }'/>">
	    <img border="0" width="70" src="<c:url value='/${item.book.image_b }'/>"/>
	</a>
</c:forEach>


			</td>
			<td width="115px">
				<span class="price_t">&yen;${order.total }</span>
			</td>
			<td width="142px">
				<c:choose>
					<c:when test="${order.status eq 1 }">未付款</c:when>
					<c:when test="${order.status eq 2 }">未发货</c:when>
					<c:when test="${order.status eq 3 }">未确认收货</c:when>
					<c:when test="${order.status eq 4 }">交易完成</c:when>
					<c:when test="${order.status eq 5 }">已取消</c:when>
				</c:choose>
			</td>
			<td>
				<a href="<c:url value='/order/orderAction_load?method=load&oid=${order.oid }&oper=desc'/>">查看</a><br/>
				<c:choose>
					<c:when test="${order.status eq 1 }">
						<a href="<c:url value='/order/orderAction_prepareForPayment?method=prepareForPayment&oid=${order.oid }'/>">支付</a><br/>
						<a href="<c:url value='/order/orderAction_load?method=load&oid=${order.oid }&oper=cancel'/>">取消</a><br/>						
					</c:when>
					<c:when test="${order.status eq 3 }">
						<a href="<c:url value='/order/orderAction_load?method=load&oid=${order.oid }&oper=confirm'/>">确认收货</a><br/>
					</c:when>
				</c:choose>
			</td>
		</tr>
</c:forEach>

	</table>
	<br/>
	<%@include file="/jsps/pager/pager.jsp" %>
</div>
  </body>
</html>
