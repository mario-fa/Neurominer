<%@page import="com.sun.org.apache.xpath.internal.functions.Function"%>
<%@page import="java.util.Vector"%>
<%@page import="br.com.neurominer.util.funtion"%>
<%
	session.setAttribute("viewMode", "N");
	Vector<String> index = new Vector<String>();
	if (session.getAttribute("historic") != null)
		index = (Vector<String>)session.getAttribute("historic");
	index.addElement("home.jsp");
	session.setAttribute("historic", index);
%>
<html>
<head>
<title>..:: NEUROMINER.COM ::..</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="css/menu.css" rel="stylesheet" type="text/css"> 
<script src="js/menu.js" type="text/javascript"></script>
<script src="js/ajax.js" type="text/javascript"></script>
<script type="text/javascript" src="js/validations.js"></script>
<!--[if IE]>
<style>
ul.MenuBarHorizontal ul
{
	margin: 0;
	padding: 0;
	list-style-type: none;
	font-size: 100%;
	z-index: 1020;
	cursor: default;
	width: 21.2em;
	position: absolute;
	left: -1000em;
	top: 174px;	
}
.login{
	background:#FFFFCC;
	font-size:12px; 
	color:#666666; 
	margin-bottom:5px; 
	padding:1px;
	}
</style>
<![endif]-->
<script>
function LimparLogin(ob,op,vl)
{
	if (op == 'f')
		if (ob.value == vl)
			ob.value = '';
	if (op == 'e')
		if (ob.value == '')
			ob.value = vl;
}
/*window.onbeforeunload = function(e)
{
	var message = 'Use the arrow lowers to return!';
	if (typeof evt == 'undefined')
		evt = window.event;
  	if (evt)
    	evt.returnValue = message;
  	return message;
}*/

</script>
</head>
<body background="" style="background-image: url('images/background.jpg');background-attachment:fixed; background-repeat: no-repeat;" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onload="CarregarContainer('home.jsp')">
<br/>

<table id="Table_01" width="901" height="95%" border="0" cellpadding="0" cellspacing="0" align="center" background="">
	<tr>
		<td>
			<img src="images/index_01.gif" width="10" height="9" alt=""></td>
		<td bgcolor="#eceded" background="images/index_02.gif" width="85" height="9"></td>
		<td rowspan="3" bgcolor="#eceded" width="229" height="51"></td>
		<td rowspan="5" bgcolor="#eceded" width="100%" height="126"></td>
		<td colspan="5" rowspan="5" bgcolor="#eceded"><img src="images/index_05.png" width="558" height="126" alt=""></td>
		<td rowspan="2">
			<img src="images/index_06.png" width="10" height="12" alt=""></td>
		<td>
			<img src="images/spacer.gif" width="1" height="9" alt=""></td>
	</tr>
	<tr>
		<td colspan="2" rowspan="4" bgcolor="#eceded">
		<a href="index.jsp">
			<img src="images/index_07.gif" width="95" height="117" border="0" title="neurominer.com"></a></td>
		<td>
			<img src="images/spacer.gif" width="1" height="3" alt=""></td>
	</tr>
	<tr>
		<td rowspan="3">
			<img src="images/index_08.png" width="10" height="114" alt=""></td>
		<td>
			<img src="images/spacer.gif" width="1" height="39" alt=""></td>
	</tr>
	<tr>
		<td bgcolor="#eceded">
		<a href="index.jsp">
			<img src="images/index_09.gif" width="229" height="26" border="0" title="neurominer.com"></a></td>
		<td>
			<img src="images/spacer.gif" width="1" height="26" alt=""></td>
	</tr>
	<tr>
		<td bgcolor="#eceded" width="229" height="49" alt=""></td>
		<td>
			<img src="images/spacer.gif" width="1" height="49" alt=""></td>
	</tr>
	<tr>
		<td colspan="10" background="images/index_11.png" width="900" height="3" alt=""></td>
		<td>
			<img src="images/spacer.gif" width="1" height="3" alt=""></td>
	</tr>
	<tr>
		<td colspan="10" bgcolor="#5e8ce9">
            <div id="menu">
                <ul id="MenuBar1" class="MenuBarHorizontal"> 
                    <li style="width:60px;" ><a href="#" onClick="CarregarContainer('home.jsp')">Home</a></li> 
              	</ul>
        	</div>
             <div align="right" style="padding-top:3;">
                    <span style="text-align:right;">
                    <input class="i_enter" name="txtLogin" id="txtLogin" type="text" value="Login" onFocus="LimparLogin(this,'f','Login')" onBlur="LimparLogin(this,'e','Login')">&nbsp;
                    <input class="i_enter" id="txtPassword" name="txtPassword" type="password" value="Password"  onFocus="LimparLogin(this,'f','Password')" onBlur="LimparLogin(this,'e','Password')">
                    &nbsp;<input class="b_enter" name="tbEnter" type="button" value="Enter" onClick="Logar('s')">&nbsp;</span>
             </div>
             </td>
             <td>
             	<img src="images/spacer.gif" width="1" height="24" alt=""></td>
             </tr>
	<tr>
		<td colspan="10" background="images/index_13.png" width="900" height="9"></td>
		<td>
			<img src="images/spacer.gif" width="1" height="9" alt=""></td>
	</tr>
	<tr>
		<td colspan="6" background="images/index_14.png" width="712" height="7" alt=""></td>
		<td>
			<img src="images/index_15.png" width="16" height="7" alt=""></td>
		<td colspan="3" background="images/index_16.png" width="172" height="7" alt=""></td>
		<td>
			<img src="images/spacer.gif" width="1" height="7" alt=""></td>
	</tr>
	<tr>
		<td colspan="6" style="width:712px;" bgcolor="#FFFFFF" valign="top">
		<br/>
            <div id="container" style="width:711px;">
            	
            </div>          
            <div id="footer" name="footer" align="right" style="vertical-align: bottom">

            </div>
        </td>
		<td background="images/index_18.png" width="16" height="479" alt=""></td>
		<td colspan="3" width="172" height="100%" bgcolor="#FFFFFF" valign="top">
            <div>
                <table border="0" cellpadding="0" cellspacing="0">
                    <tr align="center">
                        <th bgcolor="#FFFFCC">Universities / Companies</th>
                    </tr>
                    <br/>
                    <tr>
                        <td><img alt="" src="images/seta1.png">&nbsp;&nbsp;<a href="http://www.ufs.br" target="_blank">U F S</a></td>                      
                    </tr>
                    <tr>
                        <td><img alt="" src="images/seta1.png">&nbsp;&nbsp;<a href="http://www.ufba.br" target="_blank">U F B A</a></td>                      
                    </tr>
                    <tr>
                        <td><img alt="" src="images/seta1.png">&nbsp;&nbsp;<a href="http://cefetse.edu.br" target="_blank"> I F S</a></td>                        
                     </tr>
                    <tr>
                        <td><img alt="" src="images/seta1.png">&nbsp;&nbsp;<a href="http://java.sun.com" target="_blank">S U N</a></td>                        
                     </tr>
            	</table>
            	<br/>
            	<table border="0" cellpadding="0" cellspacing="0">
                    <tr align="center">
                        <th bgcolor="#FFFFCC">Government </th>
                    </tr>
                    <br/>
                    <tr>
                        <td><img alt="" src="images/seta1.png">&nbsp;&nbsp;<a href="http://www.planalto.gov.br/" target="_blank">Brazilian Government </a></td>                      
                    </tr>
                    <tr>
                        <td><img alt="" src="images/seta1.png">&nbsp;&nbsp;<a href="http://www.cnpq.br/" target="_blank">CNPq </a></td>                        
                     </tr>
            	</table>
            	<br/>
            	<table border="0" cellpadding="0" cellspacing="0">
                    <tr align="center">
                        <th bgcolor="#FFFFCC">Useful Technology</th>
                    </tr>
                    <br/>
                    <tr>
                        <td><img alt="" src="images/seta1.png">&nbsp;&nbsp;<a href="http://en.wikipedia.org/wiki/Text_mining" target="_blank">Text Mining</a></td>                      
                    </tr>
                    <tr>
                        <td><img alt="" src="images/seta1.png">&nbsp;&nbsp;<a href="http://en.wikipedia.org/wiki/Artificial_intelligence" target="_blank">Artificial Intelligence</a></td>                      
                    </tr>
                    <tr>
                        <td><img alt="" src="images/seta1.png">&nbsp;&nbsp;<a href="http://en.wikipedia.org/wiki/Analysis_of_variance" target="_blank">Analysis of Variance</a></td>                        
                     </tr>
                    <tr>
                        <td><img alt="" src="images/seta1.png">&nbsp;&nbsp;<a href="http://en.wikipedia.org/wiki/Statistics" target="_blank">Statistics</a></td>                        
                     </tr>
            	</table>
            </div>
        </td>
		<td>
			<img src="images/spacer.gif" width="1" height="479" alt=""></td>
	</tr>
	<tr>
		<td colspan="6" background="images/index_20.gif" width="712" height="8" alt=""></td>
		<td>
			<img src="images/index_21.gif" width="16" height="8" alt=""></td>
		<td colspan="3" background="images/index_22.gif" width="172" height="8" alt=""></td>
		<td>
			<img src="images/spacer.gif" width="1" height="8" alt=""></td>
	</tr>
	<tr>
		<td colspan="10" bgcolor="#FFFFFF" width="900" height="6" alt=""></td>
		<td>
			<img src="images/spacer.gif" width="1" height="6" alt=""></td>
	</tr>
	<tr>
		<td colspan="5" bgcolor="#FFFFFF" width="346" height="1" alt=""></td>
		<td colspan="5" bgcolor="#FFFFFF" width="554" height="1" alt=""></td>
		<td>
			<img src="images/spacer.gif" width="1" height="1" alt=""></td>
	</tr>
	<tr>
		<td colspan="10" background="images/index_26.jpg" width="900" height="25" alt="" align="center">© copyright 2009 - neurominer</td>
		<td>
			<img src="images/spacer.gif" width="1" height="25" alt=""></td>
	</tr>
	<tr>
		<td>
			<img src="images/index_27.gif" width="10" height="12" alt=""></td>
		<td colspan="7" bgcolor="#eceded" width="879" height="12" alt=""></td>
		<td colspan="2">
			<img src="images/index_29.gif" width="11" height="12" alt=""></td>
		<td>
			<img src="images/spacer.gif" width="1" height="12" alt=""></td>
	</tr>
	<tr>
		<td>
			<img src="images/spacer.gif" width="10" height="1" alt=""></td>
		<td>
			<img src="images/spacer.gif" width="85" height="1" alt=""></td>
		<td>
			<img src="images/spacer.gif" width="229" height="1" alt=""></td>
		<td>
			<img src="images/spacer.gif" width="8" height="1" alt=""></td>
		<td>
			<img src="images/spacer.gif" width="14" height="1" alt=""></td>
		<td>
			<img src="images/spacer.gif" width="366" height="1" alt=""></td>
		<td>
			<img src="images/spacer.gif" width="16" height="1" alt=""></td>
		<td>
			<img src="images/spacer.gif" width="161" height="1" alt=""></td>
		<td>
			<img src="images/spacer.gif" width="1" height="1" alt=""></td>
		<td>
			<img src="images/spacer.gif" width="10" height="1" alt=""></td>
		<td></td>
	</tr>
</table>
<br/>
</body>
</html>