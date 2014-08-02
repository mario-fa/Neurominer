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
<% 
	String mdView = null;
	String View = "";
	mdView = request.getParameter("mdView");
	
	View = "nao";%>

<br/>
<br/>
<%
ResultSet rs;
String sql;

try {
Class.forName("org.postgresql.Driver");
Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/methan_dbneurominer", "methan", "alemarme");
Statement stmt = connection.createStatement();

sql = "select d.name, t.date, Auditive_Total,Visual_Total, Kinesthetic_Total, Auditive_Mean, Auditive_SD,Visual_Mean, Visual_SD, Kinesthetic_Mean,Kinesthetic_SD"+
	" from neurominer.statistic_fact f, neurominer.developer_dim d, neurominer.time_dim t"+
	" where d.developer_id = f.developer_id and t.time_id = f.time_id order by d.name";

rs = stmt.executeQuery(sql);

	if (mdView.equals("sim"))
	{	
%>		<div style="width:100%; height:100% overflow-y:scroll; padding-left: 5px; padding-right: 2px">
<%  }else{ %>
		<div style="overflow-x:scroll; width:705px; height:100% overflow-y:scroll; padding-left: 5px; padding-right: 2px">
<%  } %>
        <table width="90%" border="0" align="center" cellpadding="0" cellspacing="0">
            <tr>
            	<td height="92" valign="top"><p align="center"><b><font color="#21316F" size="2" face="Verdana, Arial, Helvetica, sans-serif">Developers
                Totals</font></b><br>
                <a href="#"><img src="prototype/images/ad_busca.gif" alt="Fazer nova Busca?" border="0" /></a>
                
                <table width="98%" border="0" align="center" cellspacing="1">
                    <tr bgcolor="#CBD2EF"></tr>
                    <tr>
                        <td width="9%" bgcolor="#CBD2EF"><div align="center"><font size="1" face="Verdana, Arial, Helvetica, sans-serif">Developer</font></div>
                        </td>
                        <td width="9%" height="14" bgcolor="#CBD2EF"><div align="center"><font size="1" face="Verdana, Arial, Helvetica, sans-serif">Date</font></div>
                        </td>
                        <td width="9%" bgcolor="#CBD2EF"><div align="center"><font size="1" face="Verdana, Arial, Helvetica, sans-serif">Auditive_Total</font></div>
                        </td>
                        <td width="9%" bgcolor="#CBD2EF"><div align="center"><font size="1" face="Verdana, Arial, Helvetica, sans-serif">Visual_Total</font></div>
                        </td>
                        <td width="9%" bgcolor="#CBD2EF"><div align="center"><font size="1" face="Verdana, Arial, Helvetica, sans-serif">Kinesthetic_Total</font></div>
                        </td>
                        <td width="9%" bgcolor="#CBD2EF"><div align="center"><font size="1" face="Verdana, Arial, Helvetica, sans-serif">Auditive_Mean</font></div>
                        </td>
                        <td width="9%" bgcolor="#CBD2EF"><div align="center"><font size="1" face="Verdana, Arial, Helvetica, sans-serif">Auditive_SD</font></div>
                        </td>
                        <td width="9%" bgcolor="#CBD2EF"><div align="center"><font size="1" face="Verdana, Arial, Helvetica, sans-serif">Visual_Mean</font></div>
                        </td>
                        <td width="9%" bgcolor="#CBD2EF"><div align="center"><font size="1" face="Verdana, Arial, Helvetica, sans-serif">Visual_SD</font></div>
                        </td>
                        <td width="9%" bgcolor="#CBD2EF"><div align="center"><font size="1" face="Verdana, Arial, Helvetica, sans-serif">Kinesthetic_Mean</font></div>
                        </td>
                        <td width="10%" bgcolor="#CBD2EF"><div align="center"><font size="1" face="Verdana, Arial, Helvetica, sans-serif">
                        <div align="center"><font size="1" face="Verdana, Arial, Helvetica, sans-serif">Kinesthetic_SD</font></div>
                        </font></div>
                        </td>
                        <td width="5%" bgcolor="#CBD2EF"><div align="center"><font size="1" face="Verdana, Arial, Helvetica, sans-serif">Graphic</font></div>
                        </td>
                     </tr>
                        <%
                        while (rs.next())
                        {
                        %>
                        
                        
                      <tr bgcolor="#EFEFEF">
                        <td width="13%"><font size="1" face="Verdana, Arial, Helvetica, sans-serif"><%=rs.getString("name")%></font></td>
                        <td width="13%"><font size="1" face="Verdana, Arial, Helvetica, sans-serif">&nbsp;<%=rs.getDate("date")%></font></td>
                        <td width="13%"><div align="center"><font size="1" face="Verdana, Arial, Helvetica, sans-serif"><%=rs.getInt("Auditive_Total")%></font></div>
                        </td>
                        <td width="13%"><div align="center"><font size="1" face="Verdana, Arial, Helvetica, sans-serif"><%=rs.getInt("Visual_Total")%></font></div>
                        </td>
                        <td width="13%"><div align="center"><font size="1" face="Verdana, Arial, Helvetica, sans-serif"><%=rs.getInt("Kinesthetic_Total")%></font></div>
                        </td>
                        <td width="13%"><div align="center"><font size="1" face="Verdana, Arial, Helvetica, sans-serif"><%=rs.getDouble("Auditive_Mean")%></font></div>
                        </td>
                        <td width="13%"><div align="center"><font size="1" face="Verdana, Arial, Helvetica, sans-serif"><%=rs.getDouble("Auditive_SD")%></font></div>
                        </td>
                        <td width="13%"><div align="center"><font size="1" face="Verdana, Arial, Helvetica, sans-serif"><%=rs.getDouble("Visual_Mean")%></font></div>
                        </td>
                        <td width="13%"><div align="center"><font size="1" face="Verdana, Arial, Helvetica, sans-serif"><%=rs.getDouble("Visual_SD")%></font></div>
                        </td>
                        <td width="13%"><div align="center"><font size="1" face="Verdana, Arial, Helvetica, sans-serif"><%=rs.getDouble("Kinesthetic_Mean")%></font></div>
                        </td>
                        <td width="13%"><div align="center"><font size="1" face="Verdana, Arial, Helvetica, sans-serif"><%=rs.getDouble("Kinesthetic_SD")%></font></div>
                        </td>
                        <td width="15%" align="center"><a href="#" onclick="CarregarContainer('prototype/createGraphic.jsp?acao=means&dev=<%=rs.getString("name")%>','<%=View %>')"><img src="prototype/images/grafico.jpg" alt="Alterar dados" width="34" height="31" border="0"></a>	</td>
                    </tr>
                <%}%>
                
                </table>
            <br></td>
            </tr>
         </table>
        
        </div>
 <%
        connection.close();
} catch (SQLException sqle) {

out.write("OCORREU UMA PROBLEMA - FAVOR INFORMAR ADMINISTRADOR POR email@email.com.br!<br><br>Exception::<br>" + sqle);
sqle.printStackTrace();

} finally {

}
        %>