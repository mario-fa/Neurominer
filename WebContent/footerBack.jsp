<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="br.com.neurominer.util.funtion"%>
<%@page import="java.util.Vector"%>
<%
	String mdView = null;
	mdView = request.getParameter("mdView");
	
	if (!mdView.equals("sim"))
	{%>
<div id="back" align="right" style="vertical-align: bottom">
	<a href="#" onclick="CarregarContainer('historicLinks.jsp?link=<%=funtion.GetLastLink((Vector<String>)session.getAttribute("historic")) %>')">
	<img src="images/back.png" width="32px" height="32px" border="0"></a>
</div>
<% }%>