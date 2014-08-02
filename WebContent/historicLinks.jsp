<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="java.util.Vector"%>
<%@page import="br.com.neurominer.util.funtion"%>
<%
	Vector<String> vetor = (Vector<String>)session.getAttribute("historic");
	String link = request.getParameter("link");
	if (vetor.size() > 0)
		vetor = funtion.DelLastLink(vetor);
	session.setAttribute("historic", vetor);
	link = link.indexOf("?") != -1? link +  "&redi=sim" : link +  "?redi=sim";

	response.sendRedirect(link);
%>