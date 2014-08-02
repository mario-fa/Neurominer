package util;

import java.util.Vector;

public class funtion
{	
	public static String GetLastLink(Vector<String> historic)
	{
		if (historic.size() > 1)
			return historic.get(historic.size()-2);
		else
			return historic.get(historic.size()-1);
	}
	
	public static Vector<String> DelLastLink(Vector<String> historic)
	{
		historic.remove(historic.size()-1);
		return historic;
	}
	
	public static int GetLength(Vector<String>  historic)
	{
		return historic.size();
	}
	
	public static String GetPage(Object page)
	{
		String pagina = page.getClass().getCanonicalName();
		pagina = pagina.substring(pagina.indexOf(".jsp.")+5);
		pagina = pagina.replace(".","/");
		pagina = pagina.replace("_",".");
		
		return pagina;
	}
	
	public static Vector<String> ManipulaSession(Vector<String>  historic, Object page)
	{
		historic.addElement(GetPage(page));
		return historic;
	}
}
