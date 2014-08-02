<%@page import="br.com.neurominer.util.funtion"%>
<%@page import="java.util.Vector"%>
<%
	if (session.getAttribute("user_log") == null || !session.getAttribute("user_log").equals("1"))
	{	
		response.sendRedirect("index.jsp"); 
	}

	if (request.getParameter("redi") == null)
	{
		session.setAttribute("historic", funtion.ManipulaSession((Vector<String>)session.getAttribute("historic"),funtion.GetLastLink((Vector<String>)session.getAttribute("historic"))));
	}
%>
<br/>
<br/>
<div style="width:705px; height:100% overflow-y:scroll; padding-left: 5px; padding-right: 2px">

	<%
	String acao = request.getParameter("acao");
	String dev = request.getParameter("dev");
	if (acao.equals("means"))
       {
       %>
         <table width="90%" border="0" align="center" cellpadding="0" cellspacing="0" class="sge">
       <tr>
         <td height="92" valign="top"><p align="center"><br>
                   <a href="#"><img src="prototype/images/graphicProfile.JPG" alt="Fazer nova Busca?" width="501" height="291" border="0" /></a>
         
           <br></td>
       </tr></table>
       <%
       }
       else
       {
	  if (acao.equals("class"))
	    if (dev.equals("Methanias"))
           {
           %>
                <table width="90%" border="0" align="center" cellpadding="0" cellspacing="0" class="sge">
       <tr>
         <td height="92" valign="top"><p align="center"><br>
                   <a href="#"><img src="prototype/images/graphicClassMetha.JPG" alt="Fazer nova Busca?" width="501" height="291" border="0" /></a>
         
           <br></td>
       </tr></table>
          <%
	   }
	   else
	   {
	   %>
	    	                <table width="90%" border="0" align="center" cellpadding="0" cellspacing="0" class="sge">
       <tr>
         <td height="92" valign="top"><p align="center"><br>
                   <a href="#"><img src="prototype/images/graphicClassAlex.JPG" alt="Fazer nova Busca?" width="501" height="291" border="0" /></a>
         
           <br></td>
       </tr></table>
	  <%
	   }
	  else
	  {
	    if (acao.equals("NLM"))
		{
	  %>
	  <table width="90%" border="0" align="center" cellpadding="0" cellspacing="0" class="sge">
       <tr>
         <td height="92" valign="top"><p align="center"><br>
                   <a href="#"><img src="prototype/images/graphicTechNLM.JPG" alt="Fazer nova Busca?" width="501" height="291" border="0" /></a>
         
           <br></td>
       </tr></table>
	  <%
		}
		else
		{
		%>
		<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0" class="sge">
       <tr>
         <td height="92" valign="top"><p align="center"><br>
                   <a href="#"><img src="prototype/images/graphicTechCluster.JPG" alt="Fazer nova Busca?" width="501" height="291" border="0" /></a>
         
           <br></td>
       </tr></table>
		<%
	   }
	   }
         }
       %>
</div>