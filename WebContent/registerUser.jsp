<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page language="java" import="java.io.*,java.sql.*,javax.sql.*,javax.naming.*" %>
<%
	if (session.getAttribute("user_log") == null || !session.getAttribute("user_log").equals("1"))
	{	
		response.sendRedirect("index.jsp"); 
	}

	Class.forName("org.postgresql.Driver");
	Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/methan_dbneurominer", "methan", "alemarme");

	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	String date = sdf.format(new java.util.Date());
	
	String fullName = "";
	String userName = "";
	String password = "";
	String profile = "";
	String status  = "";
	String edit = "";
	
	ResultSet rs = null;
	String sql = "";
	
	if (request.getParameter("edit") != null && request.getParameter("edit") == "sim")
	{
		edit = "sim";
		try
		{			
			fullName = rs.getString("user_nm");
			userName = rs.getString("username_ds");
			password = rs.getString("password_ds");
			profile  = rs.getString("profile_ds");
			status	 = rs.getString("status_fl");
		}
		catch(SQLException e)  
		{  
		    // se houve algum erro, uma exceção é gerada para informar o erro   
		    e.printStackTrace(); //vejamos que erro foi gerado e quem o gerou  
		}  
		finally  
		{  
			try   
			{  
				con.close();  
			}  
			catch(SQLException onConClose)  
			{  
			    System.out.println("Houve erro no fechamento da conexão");  
			    onConClose.printStackTrace();  
			}  
		}
	}
	else
	{
		Statement stmt = con.createStatement();
		
		sql = "select profile_id, profile_ds from  neurominer.sistem_profile";

		rs = stmt.executeQuery(sql);
		
		if (request.getParameter("user_nm") 	!= null && request.getParameter("username_ds") != null &&
			request.getParameter("password_ds") != null && request.getParameter("profile_ds")  != null &&
			request.getParameter("status_fl") 	!= null)
		{
			//Dados Usuario
			fullName = request.getParameter("user_nm");
			userName = request.getParameter("username_ds");
			password = request.getParameter("password_ds");
			profile  = request.getParameter("profile_ds");
			status	 = request.getParameter("status_fl");
	
			try
			{			
				sql = "INSERT INTO neurominer.user"+
					  "(user_nm, username_ds, password_ds, registration_dt,status_fl, profile_id)"+
					  " VALUES ('"+fullName+"','"+userName+"',md5('"+password+"'),now(),'"+status+"',"+profile+")";
				
				rs = stmt.executeQuery(sql);
			}  
			catch(SQLException e)  
			{  
			    // se houve algum erro, uma exceção é gerada para informar o erro   
			    e.printStackTrace(); //vejamos que erro foi gerado e quem o gerou  
			}  
			finally  
			{  
				try   
				{  
					con.close();  
				}  
				catch(SQLException onConClose)  
				{  
				    System.out.println("Houve erro no fechamento da conexão");  
				    onConClose.printStackTrace();  
				}  
			} // fim do bloco try-catch-finally  
		}
	}
%>
<form>
<table width="400px" border="1" align="center" cellpadding="0" cellspacing="0" bordercolor="#B6D3E8" >
  <tr>
    <td>
    <table width="100%" border="0" cellspacing="0" cellpadding="0" style="padding-left:3px" class="txtBlue" >
          <th align="center" valign="middle" colspan="2">
			<br/><span class="titleBlue">JOIN THE USER</span><br/>
    		<br/> 
          </th>
          <tr>
            <td width="108" class="txtBlue">Full Name</td>
            <td width="192"><label>
              <input name="user_nm" type="text" id="user_nm" maxlength="100" value="<%=fullName %>" />
            </label></td>
          </tr>
          <tr>
            <td class="txtBlue">Username</td>
            <td><label>
              <input name="username_ds" type="text" id="username_ds" maxlength="50"  value="<%=userName %>" />
            </label></td>
          </tr>
          <tr>
            <td class="txtBlue">Password</td>
            <td><label>
              <input name="password_ds" type="password" id="password_ds" maxlength="50" value="<%=password %>" />
            </label></td>
          </tr>
          <tr>
            <td class="txtBlue">Registration date</td>
            <td><label>
              <input type="text" name="registratio_dt" id="registratio_dt" readonly="readonly" value="<%=date %>" />
            </label></td>
          </tr>
          <tr>
            <td class="txtBlue">Status</td>
          <td><label>
              <select name="status_fl" id="status_fl">
                <%
                if (status == "1")
                {%>
                	<option value="1" selected="selected">Active</option>
                	<option value="0">Inactive</option>
                <%}else if (status == "0"){ %>
                	<option value="1">Active</option>
                	<option value="0" selected="selected">Inactive</option>
                <%}else { %>
                	<option value="1">Active</option>
                	<option value="0">Inactive</option>
                <%} %>
              </select>
            </label></td>
          </tr>
          <tr>
            <td class="txtBlue">Profile</td>
            <td><label>
            <select name="profile_ds" id="profile_ds">
            	<%
            	if (rs != null)
            	{
	                while (rs.next())
	                {
	                	out.println("<option value=\""+rs.getInt("profile_id")+"\">"+rs.getString("profile_ds")+"</option> ");
	                }
                }
                %>            	
            </select>
            </label></td>
          </tr>
          <tr>
            <td colspan="2">&nbsp;
            </td>
          </tr>
          <tr>
            <td colspan="2" align="center">
            	<input class="b_enter" name="btClear" type="reset" value="Clear" />
                &nbsp;
                <input class="b_enter" name="btSave" type="button" value="Save" onclick="return ValidateUser(<%=edit %>);"/>
            </td>
          </tr>
          <tr>
            <td colspan="2">&nbsp;
            </td>	
          </tr>
        </table>
    </td>
  </tr>
</table>
</form>
