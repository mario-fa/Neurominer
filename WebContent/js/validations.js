//REGISTER THE USER
function ValidateUser()
{
	var message = "";
	var link = "";
	if (document.getElementById("user_nm").value == "")
		message += " [FullName] ";
	if (document.getElementById("username_ds").value == "")
		message += " [UserName] ";
	if (document.getElementById("password_ds").value == "")
		message += " [Password] ";
	if (document.getElementById("profile_ds").value == "")
		message += " [Profile] ";
	if (message != "")
	{ 
		alert("O campos "+message+" são obrigatórios!");
		return false;
	}
	else
	{
		link = "?user_nm="+document.getElementById("user_nm").value;
		link += "&username_ds="+document.getElementById("username_ds").value;
		link += "&password_ds="+document.getElementById("password_ds").value;
		link += "&profile_ds="+document.getElementById("profile_ds").value;
		link += "&status_fl="+document.getElementById("status_fl").value
		alert(link);
		CarregarContainerLimpo('registerUser.jsp'+link);
		return true;
	}
}
//REGISTER DEVELOPER
function ValidateDeveloper()
{
	var message = "";
	var link = "";
	if (document.getElementById("name").value == "")
		message += " [Name] ";
	if (document.getElementById("email").value == "")
		message += " [email] ";
	if (document.getElementById("login").value == "")
		message += " [Login] ";
	if (message != "")
	{ 
		alert("O campos "+message+" são obrigatórios!");
		return false;
	}
	else
	{
		link = "?name="+document.getElementById("name").value;
		link += "&email="+document.getElementById("email").value;
		link += "&login="+document.getElementById("login").value;
		link += "&position="+document.getElementById("position").value;
		link += "&expertise="+document.getElementById("expertise").value;
		link += "&flag="+document.getElementById("flag").value;
		alert(link);
		CarregarContainerLimpo('registerDeveloper.jsp'+link);
		return true;
	}
}

function Logar(opcao)
{
	if (opcao == 's')
	{
		var message = "";
		var link = "";
		var login = document.getElementById("txtLogin").value;
		var pass = document.getElementById("txtPassword").value;
		if (login == "" || login== "Login" )
			message += " [Login] ";
		if (pass == "" || pass == "Password")
			message += " [Password] ";
		if (message != "")
		{ 
			alert("O campos "+message+" são obrigatórios!");
			return false;
		}
		else
		{
			Login(login,pass,opcao,'s');
			document.getElementById("txtLogin").value = "Login";
			document.getElementById("txtPassword").value = "Password";
		}
	}
	else
		Login(login,pass,'n','n');
}