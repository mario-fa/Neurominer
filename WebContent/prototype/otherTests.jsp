<%@page import="br.com.neurominer.util.funtion"%>
<%@page import="java.util.Vector"%>
<%
	if (session.getAttribute("user_log") == null || !session.getAttribute("user_log").equals("1"))
	{	
		response.sendRedirect("index.jsp"); 
	}

	if (request.getParameter("redi") == null)
	{
		session.setAttribute("historic", funtion.ManipulaSession((Vector<String>)session.getAttribute("historic"),this));
	}
%>
<%
	if (session.getAttribute("viewMode") == "N")
	{%>
		<%@ include file="index.jsp" %>
<%  }%>
<SPAN id=screen_other style="text-align: center">
    <p>
    <p>
    <b> Under Construction... </b>
</SPAN>