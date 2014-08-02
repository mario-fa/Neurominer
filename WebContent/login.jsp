<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page language="java" import="java.io.*,java.sql.*,javax.sql.*,javax.naming.*" %>
<%
	response.setHeader("Cache-Control","no-cache");   
	response.setHeader("Pragma","no-cache");   
	response.setDateHeader("Expires", 3);
	
	String login = request.getParameter("lg");
	String pass = request.getParameter("ps");
	String op = request.getParameter("op");

	if (op.equals("s"))
	{
		//out.println("dd");
		Class.forName("org.postgresql.Driver");
		Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/methan_dbneurominer", "methan", "alemarme");
		ResultSet rs = null;
		String sql = "";
		
		Statement stmt = con.createStatement();
		
		sql = "select * from neurominer.user where username_ds='"+login+"' and password_ds=md5('"+pass+"')";
	
		rs = stmt.executeQuery(sql);
		if (rs.next())
		{
			session.setAttribute("user_log","1");
		}
		else
		{
			session.setAttribute("user_log","0");
		}
	}
	else
	{
		session.setAttribute("user_log","0");
	}
	
	out.println(session.getAttribute("user_log"));
%>