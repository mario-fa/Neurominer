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
<br/>
<br/>
<link rel="stylesheet" href="prototype/css/tabs.css" type="text/css">
<table align="center">
  <tr>
    <td>
<input type="hidden" name="tmp.aba_email_tests" value="loading" />


<%--  <%! Evitando a definição de variáveis na memória compartilhada --%>
<%--  PARA EVITAR QUE A CONCORRÊNCIA GERE ESTADOS INCONSISTENTES --%>
<%
    String aba_email_tests = null;
    String load_emails = null;
%>

<%
    aba_email_tests = request.getParameter("tmp.aba_email_tests");
    load_emails = request.getParameter("tmp.load_emails");

    if (aba_email_tests == null)
        aba_email_tests = "loading";

    if (load_emails == null)
        load_emails = "n";
%>

<SPAN id="screen_email">

    <div id="header">
        <ul id="primary">
            <li>

<%
    if (aba_email_tests.equals("loading")) {
%>
                <span> Loading </span>
<%
    } else {
%>
                <a href="#" onclick="CarregarContainer('prototype/emailTests.jsp?tmp.aba_email_tests=loading')"> Loading </a>
<%
    }
%>

            </li>
            <li>

<%
    if (aba_email_tests.equals("filtering")) {
%>
                <span> Filtering </span>
<%
    } else {
%>
                <a href="#" onclick="CarregarContainer('prototype/emailTests.jsp?tmp.aba_email_tests=filtering')"> Filtering </a>
<%
    }
%>
            </li>
        </ul>
    </div>



<%
    if (aba_email_tests.equals("loading")) {
%>
    <SPAN id=loading  style="display:block" />
<%
    } else {
%>
    <SPAN id=loading  style="display:none" />
<%
    }
%>
      <table class="textonormal editQuadro">
        <tr>
          <td>

             <span class="fontMessageError"><wi:out var="tmp.erro"/></span>

             <FORM name="cadastro" method="post" action="emailTests.jsp">

                 <table class="textonormal editQuadroMenor">

                     <p>
                     <tr class="editTitulo">
                         <td colspan="2" align="center"> Informations to load e-mails </td>
                     </tr>

                     <tr>
                         <td>
                              POP3 Server:
                         </td>
                         <td>
                              <input type="text" class="textonormal" id="tmp.server" name="tmp.server" size="25">
                         </td>
                     </tr>


                     <tr>
                         <td>
                              Protocol:
                         </td>
                         <td>
                              <input type="text" class="textonormal" id="tmp.protocol" name="tmp.protocol" size="25">
                         </td>
                     </tr>

                     <tr>
                         <td>
                              Port:
                         </td>
                         <td>
                              <input type="text" class="textonormal" id="tmp.port" name="tmp.port" size="25">
                         </td>
                     </tr>

                     <tr>
                         <td>
                              User Name:
                         </td>
                         <td>
                              <input type="text" class="textonormal" id="tmp.user" name="tmp.user" size="25">
                         </td>
                     </tr>

                     <tr>
                         <td>
                              Password:
                         </td>
                         <td>
                              <input type="text" class="textonormal" id="tmp.pass" name="tmp.pass" size="25">
                         </td>
                     </tr>
                 </table>
                 <p>
                 <p>


                 <table class="rodape">
                     <tr>
                         <td>
                             <a href="#" onclick="CarregarContainer('prototype/emailTests.jsp?tmp.load_emails=s')"> <img src="prototype/images/load_email.jpg" border="0"  /> </a>
                         </td>
                         <td>
                             <font size="1" color="FF0000"> PS: The default protocol is "pop3".<br> Especially, for GMail server, use "pop3s" </font>
                         </td>
                     </tr>
                     <tr>
                         <td>
                             <font size="1"> Load E-mails </font>
                         </td>
                         <td>
                             &nbsp;
                         </td>
                     </tr>
                 </table>

             </FORM>

          </td>
        </tr>
      </table>
    </SPAN>


<%
    if (aba_email_tests.equals("filtering")) {
%>
    <SPAN id=loading  style="display:block" />
<%
    } else {
%>
    <SPAN id=loading  style="display:none" />
<%
    }
%>            
      <table class="textonormal editQuadro">
        <tr>
          <td>
            <ul id="subitens">

              <p>
              <li><a href=""> Test1 </a></li>
              <li><a href=""> Test2 </a></li>
              <p>


            </ul>
          </td>
        </tr>
      </table>
    </SPAN>
</SPAN>

      
<%
    if (load_emails.equals("s")){
// Código para carga dos e-mails aqui!            
%>

<b> UNDER CONSTRUCTION... <b/>

<%        
    }
%>

	</td>
  </tr>
</table>