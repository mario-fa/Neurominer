<%@page language="java" import="java.io.*,java.sql.*,javax.sql.*,javax.naming.*" %>
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
<br/>
<br/>
<%
ResultSet rs;
String sql;

try {
Class.forName("org.postgresql.Driver");
Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/methan_dbneurominer", "methan", "alemarme");
Statement stmt = connection.createStatement();

sql = "select d.name, t.date, load_id, p.neurolinguistic_profile, te.name as tech "+
	" from neurominer.profile_fact f, neurominer.developer_dim d, neurominer.time_dim t, neurominer.profile_dim p, neurominer.technique_dim te "+ 
	" where d.developer_id = f.developer_id and t.time_id = f.time_id and p.profile_id = f.profile_id "+
	" and te.technique_id = f.technique_id order by d.name, load_id";

rs = stmt.executeQuery(sql);



%>

<div style="width:705px; height:100% overflow-y:scroll; padding-left: 5px; padding-right: 2px">
<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0">
        <tr>
          <td height="92" valign="top"><p align="center"><b><font color="#21316F" size="2" face="Verdana, Arial, Helvetica, sans-serif">CLASSIFICATIONS</font></b><br>
                    <a href="#"><img src="prototype/images/ad_busca.gif" alt="Fazer nova Busca?" border="0" /></a>
          
            <table width="98%" border="0" align="center" cellspacing="1">
              <tr bgcolor="#CBD2EF"></tr>
              <td width="9%" bgcolor="#CBD2EF"><div align="center"><font size="1" face="Verdana, Arial, Helvetica, sans-serif">Developer</font></div>
      </td>
  <td width="9%" height="14" bgcolor="#CBD2EF"><div align="center"><font size="1" face="Verdana, Arial, Helvetica, sans-serif">Date</font></div>
    </td>
      <td width="9%" bgcolor="#CBD2EF"><div align="center"><font size="1" face="Verdana, Arial, Helvetica, sans-serif">Load_ID</font></div>
      </td>
      <td width="9%" bgcolor="#CBD2EF"><div align="center"><font size="1" face="Verdana, Arial, Helvetica, sans-serif">Profile</font></div>
      </td>
      <td width="9%" bgcolor="#CBD2EF"><div align="center"><font size="1" face="Verdana, Arial, Helvetica, sans-serif">Technique</font></div>
      </td>
      <td width="5%" bgcolor="#CBD2EF"><div align="center"><font size="1" face="Verdana, Arial, Helvetica, sans-serif">Graphic</font></div>
      </td>
  <%
  while (rs.next())
		 {
  %>
            

  <tr bgcolor="#EFEFEF">
    <td width="13%"><font size="1" face="Verdana, Arial, Helvetica, sans-serif"><%=rs.getString("name")%></font></td>
    <td width="13%"><font size="1" face="Verdana, Arial, Helvetica, sans-serif">&nbsp;<%=rs.getDate("date")%></font></td>
    <td width="13%"><div align="center"><font size="1" face="Verdana, Arial, Helvetica, sans-serif"><%=rs.getInt("load_id")%></font></div>
    </td>
    <td width="13%"><div align="center"><font size="1" face="Verdana, Arial, Helvetica, sans-serif"><%=rs.getString("neurolinguistic_profile")%></font></div>
    </td>
    <td width="13%"><div align="center"><font size="1" face="Verdana, Arial, Helvetica, sans-serif"><a href="#" onclick="CarregarContainer('prototype/createGraphic.jsp?acao=<%=rs.getString("tech")%>')"><%=rs.getString("tech")%></a></font></div>
    </td>
    <td width="15%" align="center"><a href="#" onclick="CarregarContainer('prototype/createGraphic.jsp?acao=class&dev=<%=rs.getString("name")%>')"><img src="prototype/images/grafico.jpg" alt="Alterar dados" width="34" height="31" border="0"></a></td>
  </tr>
  <%}%>
  
            </table>
            <br></td>
        </tr></table>
        </div>
 <%
        connection.close();
} catch (SQLException sqle) {

out.write("OCORREU UMA PROBLEMA - FAVOR INFORMAR ADMINISTRADOR POR email@email.com.br!<br><br>Exception::<br>" + sqle);
sqle.printStackTrace();

} finally {

}
        %>