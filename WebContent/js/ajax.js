// Ajax

// 1º passo : Realizando uma requisição HTTP via javascript
// será necessário uma instância da classe XMLHTTP

var tipoUser = ""; 

// realiza uma requisição HTTP
function realizarRequisicaoHTTP()
{
    //requisição http
    http_request = false;

	//instanciando o objecto XMLHTTP responsável pela requisição http a depender do browser do cliente
	if (window.XMLHttpRequest) // se o browser for o Mozilla, Safari,...
	{
		http_request = new XMLHttpRequest();
	}
	else if (window.ActiveXObject) // se o browser for o Internet Explorer
	{
		try
		{
			http_request = new ActiveXObject("Msxml2.XMLHTTP");
		}
		catch (e)
		{
			try
			{
				http_request = new ActiveXObject("Microsoft.XMLHTTP");
			}
			catch (e) {}
		}
	}

	//retornando o objeto responsável pela conexão
	return http_request;
}

// Setando o cabeçalho para não buscar as informações no cache do browser
function atualizaCache(http_request)
{
   http_request.setRequestHeader("Cache-Control","no-store, no-cache, must-revalidate");
   http_request.setRequestHeader("Cache-Control","post-check=0, pre-check=0");
   http_request.setRequestHeader("Pragma", "no-cache");
}

// Setando o tipo MIME (Multipurpose Internet Mail Extension)
function setandoTipoMIME(http_request)
{
   http_request.setRequestHeader('Content-Type',"text/plain; charset=iso-8859-1");
}

//Resolvendo problema de cache
function antiCacheBrowser(url)
{
	var dt = new Date();
    if(url.indexOf("?" ) >= 0)// já tem parametros
	{
		return url + "&" + encodeURI(Math.random() + "_" + dt.getTime());
    }
	else
	{
		return url + "?" + encodeURI(Math.random() + "_" + dt.getTime());
	}
}

// Function que irá carregar as paginas para div
function CarregarContainer(link,pageAlt)
{
	//localizando div na pagina
  	pagina = document.getElementById('container');
  	
  	//Exibindo mensagem carregando
  	pagina.innerHTML =	"<div id='carregando' style='display:block' align='center'>"
            		   +"<img src='images/aguarde.gif'/>"+
            			"</div>";

	var extLink = "";
	if (pageAlt != undefined && pageAlt != "" && pageAlt != "sim")
	{
		extLink = "sim" + pageAlt;
	}
	
   	//realizando uma solicitação HTTP
   	var pedido_http = realizarRequisicaoHTTP();
   	
   	var retorno = false;
   
   	//página de destino que será processada de maneira assíncrona : true
   	pedido_http.open("GET",antiCacheBrowser(link+"?mdView="+extLink),true);

   	//atualizando o cache
   	atualizaCache(pedido_http);

   	//definindo a funsão que irá processar a resposta HTTP
   	pedido_http.onreadystatechange=function()
	{
		if (pedido_http.readyState==4)
	   	{
			if (pedido_http.status==200)
			{
				var head = "";
			 	//obtendo o resultado do processamento
			 	var resposta = pedido_http.responseText;
			 	
			 	//Adicionando Limk opcional de Visualização
			 	if (pageAlt != undefined && pageAlt == "sim")
			 	{
					head = "<div id=\"pageAlt\" align=\"right\">"
            			+"<a href=\"viewPage.jsp?link="+link+"&mdView="+extLink+"\" target=\"_blank\">"
            			+"<img src=\"images/newView.png\" width=\"32px\" height=\"32px\" border=\"0\" title=\"Environment View\"></a>"
						+"</div>";
				}
				
				//Adicionando resposta a div dinamicamente
				pagina.innerHTML = head + resposta;
				
				//CarregarFooter()
				if (pageAlt != undefined && pageAlt == "nao")
				{ 
					CarregarFooter('?mdView=sim');
				}
				else
					CarregarFooter('?mdView='+extLink);
		  	}
		  	if (pedido_http.status==500 || pedido_http.status==404 )
		  	{
			  	CarregarContainer("notFind.jsp");
		  	}
	   	}	
		else
		{
		 	retorno = true;
	  	}
  	}
	
	if (retorno)
		alert('Ocorreu um erro na comunicacao Assincrona!');
	
	//Realizando pedido
  	pedido_http.send("");
}

// Function que irá carregar as paginas para div
function CarregarContainerLimpo(link)
{
	//localizando div na pagina
  	pagina = document.getElementById('container');
  	
  	//Exibindo mensagem carregando
  	pagina.innerHTML =	"<div id='carregando' style='display:block' align='center'>"
            		   +"<img src='images/aguarde.gif'/>"+
            			"</div>";
	
   	//realizando uma solicitação HTTP
   	var pedido_http = realizarRequisicaoHTTP();
   	
   	var retorno = false;
   
   	//página de destino que será processada de maneira assíncrona : true
   	pedido_http.open("GET",antiCacheBrowser(link),true);

   	//atualizando o cache
   	atualizaCache(pedido_http);

   	//definindo a funsão que irá processar a resposta HTTP
   	pedido_http.onreadystatechange=function()
	{
		if (pedido_http.readyState==4)
	   	{
			if (pedido_http.status==200)
			{
				var head = "";
			 	//obtendo o resultado do processamento
			 	var resposta = pedido_http.responseText;
			 	
				
				//Adicionando resposta a div dinamicamente
				pagina.innerHTML = resposta;
				
				CarregarFooter('');
		  	}
		  	if (pedido_http.status==500 || pedido_http.status==404 )
		  	{
			  	CarregarContainer("notFind.jsp");
		  	}
	   	}	
		else
		{
		 	retorno = true;
	  	}
  	}
	
	if (retorno)
		alert('Ocorreu um erro na comunicacao Assincrona!');
	
	//Realizando pedido
  	pedido_http.send("");
}

//Carregar footer
// Function que irá o botao voltar
function CarregarFooter(mdView)
{
	//localizando div na pagina
  	var footer = document.getElementById('footer');	
	
   	//realizando uma solicitação HTTP
   	var pedido_http = realizarRequisicaoHTTP();
   	
   	var retorno = false;
   	
   	//página de destino que será processada de maneira assíncrona : true
   	pedido_http.open("GET",antiCacheBrowser('footerBack.jsp'+mdView),true);
	
   	//atualizando o cache
   	atualizaCache(pedido_http);

   	//definindo a funsãoo que irá processar a resposta HTTP
   	pedido_http.onreadystatechange=function()
	{
		if (pedido_http.readyState==4)
	   	{
			if (pedido_http.status==200)
			{
			 	//obtendo o resultado do processamento
			 	var resposta = pedido_http.responseText;
				
				//Adicionando resposta a div dinamicamente
				//footer.style.display = "block";
				footer.innerHTML = resposta;
		  	}
	   	}
		else
		{
		 	retorno = true;
	  	}
  	}
	
	if (retorno)
		alert('Ocorreu um erro na comunicacao Assincrona!');
	
	//Realizando pedido
  	pedido_http.send("");
}

// Function que irá carregar as paginas para div
function Login(lg,ps,op,fl)
{
	//localizando div na pagina
  	pagina = document.getElementById('container');
  	
  	//Exibindo mensagem carregando
  	pagina.innerHTML =	"<div id='carregando' style='display:block' align='center'>"
            		   +"<img src='images/aguarde.gif'/>"+
            			"</div>";
	
   	//realizando uma solicitação HTTP
   	var pedido_http = realizarRequisicaoHTTP();
   	
   	var retorno = false;
   
   	//página de destino que será processada de maneira assíncrona : true
   	pedido_http.open("GET",antiCacheBrowser('login.jsp?lg='+lg+'&ps='+ps+'&op='+op),true);

   	//atualizando o cache
   	atualizaCache(pedido_http);

   	//definindo a funsão que irá processar a resposta HTTP
   	pedido_http.onreadystatechange=function()
	{
		if (pedido_http.readyState==4)
	   	{
			if (pedido_http.status==200)
			{
			 	//obtendo o resultado do processamento
			 	var resposta = pedido_http.responseText;
			
			 	CarregarMenuDinamico();
			 	CarregarContainer("home.jsp");

				if (fl == 's' && resposta == 0)
					alert("Login or Password Invalid!");
		  	}
		  	if (pedido_http.status==500 || pedido_http.status==404 )
		  	{
			  	CarregarContainer("notFind.jsp");
		  	}
	   	}	
		else
		{
		 	retorno = true;
	  	}
  	}
	
	if (retorno)
		alert('Ocorreu um erro na comunicacao Assincrona!');
	
	//Realizando pedido
  	pedido_http.send("");
}

//Carrega menu dinâmico
function CarregarMenuDinamico()
{
	//localizando div na pagina
  	pagina = document.getElementById('container');
  	
  	//Exibindo mensagem carregando
  	pagina.innerHTML =	"<div id='carregando' style='display:block' align='center'>"
            		   +"<img src='images/aguarde.gif'/>"+
            			"</div>";
	
   	//realizando uma solicitação HTTP
   	var pedido_http = realizarRequisicaoHTTP();
   	
   	var retorno = false;
   
   	//página de destino que será processada de maneira assíncrona : true
   	pedido_http.open("GET",antiCacheBrowser('dinamicMenu.jsp'),true);

   	//atualizando o cache
   	atualizaCache(pedido_http);

   	//definindo a funsão que irá processar a resposta HTTP
   	pedido_http.onreadystatechange=function()
	{
		if (pedido_http.readyState==4)
	   	{
			if (pedido_http.status==200)
			{
			 	//obtendo o resultado do processamento
			 	var resposta = pedido_http.responseText;
			 	
			 	document.getElementById('menu').innerHTML = resposta;
			 	
			 	menuReload();
			 	tipoUser =  resposta;
		  	}
		  	if (pedido_http.status==500 || pedido_http.status==404 )
		  	{
			  	CarregarContainer("notFind.jsp");
		  	}
	   	}	
		else
		{
		 	retorno = true;
	  	}
  	}
	
	if (retorno)
		alert('Ocorreu um erro na comunicacao com o servidor!');
	
	//Realizando pedido
  	pedido_http.send("");
}