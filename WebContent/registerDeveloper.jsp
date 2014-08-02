<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="br.neurominer.Business.Developer"%>
<%@page import="br.neurominer.Entity.EntDeveloper"%>
<%@page language="java" import="java.io.*,java.sql.*,javax.sql.*,javax.naming.*" %>
<%
	if (session.getAttribute("user_log") == null || !session.getAttribute("user_log").equals("1"))
	{	
		response.sendRedirect("index.jsp"); 
	}
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	String date = sdf.format(new java.util.Date());
	
	Developer developer = new Developer();
	EntDeveloper entDeveloper = new EntDeveloper();
	
	String name = "";
	String email= "";
	String login= "";
	String expertise = "";
	String flag = "";
	String position  = "";
	String edit = null;
	
	ResultSet rs = null;
	String sql = "";
	
	if (request.getParameter("edit") != null && request.getParameter("edit") == "sim")
	{
		edit = "sim";
		try
		{			
			name = rs.getString("name");
			email = rs.getString("email");
			expertise = rs.getString("expertise");
			position  = rs.getString("position");
			flag	 = rs.getString("flag");
		}
		catch(SQLException e)  
		{  
		    // se houve algum erro, uma exceção é gerada para informar o erro   
		    e.printStackTrace(); //vejamos que erro foi gerado e quem o gerou  
		}  
	}
	else if (request.getParameter("name") 	!= null && request.getParameter("email") != null &&
			request.getParameter("login") != null && request.getParameter("flag")  != null)
	{
		//Dados Usuario
		entDeveloper.setName(request.getParameter("name"));
		entDeveloper.setEmail(request.getParameter("email"));
		entDeveloper.setLogin(request.getParameter("login"));
		entDeveloper.setExpertise(request.getParameter("expertise"));
		entDeveloper.setPosition(request.getParameter("position"));
		entDeveloper.setFlag(request.getParameter("flag"));
	
		try
		{	
			Developer.Insert(entDeveloper);
			System.out.println("Successfully saved!");
		}
		catch(SQLException e)  
		{  
		    // se houve algum erro, uma exceção é gerada para informar o erro   
		    e.printStackTrace(); //vejamos que erro foi gerado e quem o gerou 
		    System.out.println(e);
		} 
		catch(Exception e)  
		{  
			System.out.println("Houve erro no fechamento da conexão");
		}
	}
%>
<form>
<table width="400px" border="1" align="center" cellpadding="0" cellspacing="0" bordercolor="#B6D3E8" >
  <tr>
     <td>
        <table width="100%" border="0" align="center">
          <tr>
            <td colspan="3" class="txtBlue" align="center">
            	<br/><span class="titleBlue">Register Developer</span> 
			</td>
          </tr>
          <tr>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
          </tr>
          <tr>
            <td class="txtBlue">Name</td>
            <td><label>
              <input type="text" name="name" id="name" size="40" maxlength="100"/>
            </label></td>
          </tr>
          <tr>
            <td class="txtBlue">Login</td>
            <td><input type="text" name="login" id="login"  size="40" maxlength="100"/></td>
          </tr>
          <tr>
            <td class="txtBlue">Email</td>
            <td><input type="text" name="email" id="email"  size="40" maxlength="100"/></td>
          </tr>
          <tr>
            <td class="txtBlue">Expertise</td>
            <td><input type="text" name="expertise" id="expertise"  size="40" maxlength="100"/></td>
          </tr>
          <tr>
            <td class="txtBlue">Status</td>
            <td><p>
              <label>
                <input type="radio" name="flag" value="t" id="flag_0" checked="checked" />
                Ativo</label>
              <label>
                <input type="radio" name="flag" value="f" id="flag_1" />
                Inativo</label>
              <br />
            </p></td>
          </tr>
          <tr>
            <td class="txtBlue">Position</td>
            <td><input type="text" name="position" id="position"  size="40" maxlength="100"/></td>
          </tr>
          <tr>
            <td class="txtBlue">Start date </td>
            	<td><input type="text" name="start_date" id="name2s"  size="14" maxlength="100"  readonly="readonly" value="<%=date %>" />
            </td>
          </tr>
          <tr>
            <td class="txtBlue">Finish date</td>
            <td><input type="text" name="name2" id="name2"  size="14" maxlength="100"/></td>
          </tr>
          <tr>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
          </tr>
          <tr>
            <td colspan="2" align="center">
              <input class="b_enter"  name="btClear" type="button" value="Clear" />
              &nbsp;
              &nbsp;
              <input class="b_enter" name="btSave" type="button" value="Save" onclick="return ValidateDeveloper(<%=edit %>);"/>
          </tr>
          <tr>
            <td colspan="2">&nbsp;</td>
          </tr>
        </table>
     </td>
   </tr>
</table>
</form>