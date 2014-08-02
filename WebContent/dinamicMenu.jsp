<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page language="java" import="java.io.*,java.sql.*,javax.sql.*,javax.naming.*" %>
<%
	String user = (String)session.getAttribute("user_log");
	
	if (user != null && user == "1")
	{
%>
	<ul id="MenuBar1" class="MenuBarHorizontal"> 
       <li style="width:60px;" ><a href="#" onClick="CarregarContainer('home.jsp')">Home</a> </li>
       <li  style="width:60px;" ><a href="#" class="MenuBarItemSubmenu" title="Load Emails/Messages">Load</a>
           <ul  style="width:120px;" >
               <li style="width:120px;"><a href="#">Load TXTs</a></li>
               <li style="width:120px;"><a href="#">Load from Server</a></li>
               <li style="width:120px;"><a href="#">Load DW</a></li>
               <li style="width:120px;"><a href="#">Cancel Loads</a></li>                              
    		</ul>
       </li>
       <li  style="width:140px;"><a href="#"  class="MenuBarItemSubmenu" >Classify Develorpers</a>
           <ul style="width:180px;">
               <li style="width:180px;"><a href="#">Using NLM</a></li>
               <li style="width:180px;" ><a href="#">Using Cluster</a>
               <li style="width:180px;"><a href="#">Using Genetic Algorithms</a></li>
               <li style="width:180px;" ><a href="#">Using Neural Network</a></li>
           </ul>
       </li>
       <li  style="width:100px;"><a href="#"  class="MenuBarItemSubmenu">Insert</a>
           <ul style="width:100px;">
               <li style="width:100px;"><a href="#">Parameters</a></li>
               <li style="width:100px;" ><a href="#">Dictionary</a>
               <li style="width:100px;" ><a href="#">Developers...</a>
               <li style="width:100px;"><a href="#">Techniques</a></li>
               <li style="width:100px;" ><a href="#">Projects</a>
           </ul>
       </li>
       <li  style="width:90px;"><a href="#"  class="MenuBarItemSubmenu" >OLAP</a>
           <ul style="width:130px;">
               <li style="width:130px;"><a href="#" onclick="CarregarContainer('prototype/profiles.jsp')">Classifications</a></li>
               <li style="width:130px;"><a href="#" onclick="CarregarContainer('prototype/means.jsp','sim')">Statistics</a></li>
               <li style="width:130px;" ><a href="#">Social Network</a></li>
               <li style="width:130px;" ><a href="#">Loads Summary</a></li>
           </ul>
       </li>
       <li style="width:110px;" ><a href="#" class="MenuBarItemSubmenu" >Administration</a>
        <ul>
            <li style="width:113px;"><a href="#"  onclick="CarregarContainer('registerUser.jsp')" >Register User</a></li>
            <li style="width:113px;"><a href="#"  onclick="CarregarContainer('registerDeveloper.jsp')" >Register Developerr</a></li>
            <li style="width:113px;"><a href="#" >User Query</a></li>
            <li style="width:113px;"><a href="#"  onclick="Logar('n')">Logout</a></li>
        </ul>
 		</li>                      
 	</ul>
<% 	}else{ %>
	<ul id="MenuBar1" name="#user#" class="MenuBarHorizontal"> 
		<li style="width:60px;" ><a href="#" onClick="CarregarContainer('home.jsp')">Home</a> </li> 
  	</ul>
<%	} %>