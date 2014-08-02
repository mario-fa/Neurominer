package Processing;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.AbstractDocument.LeafElement;

public class EMailFilter
{

    /**
     * The filter
     */
    private static String[] filter;
    /**
     * The filterSpecial
     */
    private static String[] filterSpecial;
    /**
     * The message_from
     */
    public static String message_from;
    /**
     * The message_to
     */
    public static String message_to;
    /**
     * The subject
     */
    public static String subject;
    /**
     * The sent_date
     */
    public static String sent_date;
    
    private static String time_zone;
   
    /**
     * The content
     */
    public static ArrayList<String> content = new ArrayList<String>();
    /**
     * The message_content
     */
    public static String message_content;
    /**
     * The message_content
     */
    public static ArrayList<String>  message_content_list; 
    /**
     * The message
     */
    public static ArrayList<String> message;
    
    public static String email_file;
    
    /**
     * @return the filter
     */    
    public static String[] getFilter()
    {
        return filter;
    }
    
    public static String getEmail_file() {
		return email_file;
	}
    
    /**
     * @return the FilterSpecial
     */    
    public static String[] getFilterSpecial()
    {
        return filter;
    }
    
    /**
	 * @return the message_from
	 */
	public static String getMessage_from() {
		return message_from;
	}

	/**
	 * @param message_from the message_from to set
	 */
	public static void setMessage_from(String message_from) {
		EMailFilter.message_from = message_from;
	}

	/**
	 * @return the message_to
	 */
	public static String getMessage_to() {
		return message_to;
	}

	/**
	 * @param message_to the message_to to set
	 */
	public static void setMessage_to(String message_to) {
		EMailFilter.message_to = message_to;
	}

	/**
	 * @return the subject
	 */
	public static String getSubject() {
		return subject;
	}

	/**
	 * @param subject the subject to set
	 */
	public static void setSubject(String subject) {
		EMailFilter.subject = subject;
	}

	/**
	 * @return the sent_date
	 */
	public static String getSent_date() {
		return sent_date;
	}

	/**
	 * @param sent_date the sent_date to set
	 */
	public static void setSent_date(String sent_date) {
		EMailFilter.sent_date = sent_date;
	}

	/**
	 * @return the content
	 */
	public static ArrayList<String> getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public static void setContent(ArrayList<String> content) {
		EMailFilter.content = content;
	}
   
	/**
	 * @return the message_content
	 */
	public static String getMessage_content() 
	{
		return message_content;
	}
	/**
	 * @return the message_content_list
	 */
	public static ArrayList<String> getMessage_content_list() 
	{
		return message_content_list;
	}
	/**
	 * @param message_content the message_content to set
	 */
	public static void setMessage_content(String message_content)
	{
		EMailFilter.message_content = message_content;
	}
	/**
	 * @param message_content the message_content to set
	 */
	public static void setMessage_content_list(ArrayList<String> message_content_list)
	{
		EMailFilter.message_content_list = message_content_list;
	}
	/**
	 * @return the message
	 */
	public static ArrayList<String> getMessage() 
	{
		return message;
	}
	
	/**
	 * @param message the message to set
	 */
	public static void setMessage(ArrayList<String> message)
	{
		EMailFilter.message = message;
	}
	
	/**
     * @param aFilter the filter to set
     */
    public static void setFilter(String[] aFilter)
    {
        filter = aFilter;
    }
    
	/**
     * @param aFilter the filterSpecial to set
     */
    public static void setFilterSpecial(String[] aFilterSpecial)
    {
        filterSpecial = aFilterSpecial;
    }
    
    /**
     * Method will break the message in lines
     * @param message
     * @return Array message ""
     */
    public static ArrayList<String> MessageInLines(String message)
    {
    	StringBuffer str = new StringBuffer();    	
    	ArrayList<String> msg = new ArrayList<String>();
        String[] resultados = null;
        if (message != null)
        {
        	resultados = message.split("\\n");  
	        for (int x=0; x<resultados.length; x++)
	        {
	        	if (resultados[x] != "")
	        		//Seting and turn in lower case
	        		str.append(msg.add(resultados[x].toLowerCase()));
	        }
        }
    	
    	return msg;
    }
    
    /**
     * Method will get the data of the message how: from,to,sent date,subject and content
     * @param message
     */
    public static void GetDataMessage(ArrayList<String> msg)
    {
    	message_from = "";
    	message_to = "";
    	subject = "";
    	sent_date = "";
    	content = new ArrayList<String>(); 
    	int indexTime = 0;
    	 
    	boolean from = false;
    	boolean to = false;
    	boolean date = false;
    	boolean sub = false;
    	boolean cont = false;
    	int pos = -1;
    	String timeZoneTemp;
    	
    	for (int i = 0; i < msg.size();i++)
    	{
    		if (msg.get(i) != null)
    		{
	    		//get message_from
	    		if (CheckFilter(msg.get(i),"from: ") && !from)
	    		{
	    			int index = msg.get(i).indexOf("from: ");
	    	    	if (index == 0)
	    	    	{
		    			message_from = msg.get(i).replaceAll("from: ", "");
		    			from = true;
	    	    	}
	    		}
	    		//get message_to
	    		if (CheckFilter(msg.get(i),"to: ") && !to)
	    		{
	    			int index = msg.get(i).indexOf("to: ");
	    	    	if (index == 0)
	    	    	{
		    			message_to = msg.get(i).replaceAll("to: ", "");
		    			to = true;
	    	    	}
	    		}
	    		//get subject
	    		if (CheckFilter(msg.get(i),"subject: ") && !sub)
	    		{
	    			int index = msg.get(i).indexOf("subject: ");
	    	    	if (index == 0)
	    	    	{
		    			subject = msg.get(i).replaceAll("subject: ", "");
		    			sub = true;
	    	    	}
	    		}
	    		//get sent_date
	    		if (CheckFilter(msg.get(i),"date: ") && !date)
	    		{
	    			int index = msg.get(i).indexOf("date: ");
	    	    	if (index == 0)
	    	    	{	    	    		
		    			sent_date = msg.get(i).replaceAll("date: ", "");
		    			//time_zone = msg.get(i);
		    			timeZoneTemp = sent_date.substring(sent_date.indexOf(":")+1, sent_date.length());
		    			if (timeZoneTemp.indexOf(":") > 0)
		    			{
		    			  indexTime = sent_date.indexOf(":", sent_date.indexOf(":")+1)+3;
	    	    		  setTime_zone(sent_date.substring(indexTime+1, indexTime+4).trim());
		    			}
		    			else{		    						    				
		    	    		setTime_zone(timeZoneTemp.substring(3, 6).trim());
		    			}
		    			
	    	    		//System.out.println("Time Zone: " + sent_date + " --> " + getTime_zone());
	    	    		
		    			date = true;
	    	    	}
	    		}
	    		
	    		
	    		//get content  
    			if (CheckFilter(msg.get(i),"x-spam-rating:") && !cont)
	    		{
	    			int index = msg.get(i).indexOf("x-spam-rating:");
	    	    	if (index == 0)
	    	    	{
	    	    		pos = i;
	    	    	}
	    		}	
	    		if (CheckFilter(msg.get(i),"x-virus-checked:") && !cont)
	    		{
	    			int index = msg.get(i).indexOf("x-virus-checked:");
	    	    	if (index == 0)
	    	    	{
	    	    		pos = i;
	    	    	}
	    		}
	    		//message without reply    		
	    		else if (CheckFilter(msg.get(i),"x-status:") && !cont)
	    		{
	    			int index = msg.get(i).indexOf("x-status:");
	    	    	if (index == 0)
	    	    	{
	    	    		pos = i;
	    	    	}
	    		}		
	    		//message which reply
	    		else if (CheckFilter(msg.get(i),"reply-to:") && !cont)
	    		{
	    			int index = msg.get(i).indexOf("reply-to:");
	    	    	if (index == 0)
	    	    	{
	    	    		pos = i;
	    	    	}
	    		}
    		}
    	}
    	
    	for (int i = pos+1; i < msg.size();i++)
    	{
	    	content.add(msg.get(i));
    	}
    }
    
    /**
     * Method will check if there is value in then text
     * @param text
     * @return text where there is value or ""
     */
    public static boolean CheckFilter(String text,String value)
    {
    	boolean result = false;
    	text = text.toUpperCase();
    	if (text.indexOf(value.toUpperCase()) != -1)
    	{
    		result = true;
    	}
	
    	return result;
    }
    
    /**
     * Method will check if there is filter in then text
     * @param text
     * @return filter or ""
     */
    public static String CheckFilter(String text)
    {
    	String result = "";
    	boolean find = false;
    	text = text.toUpperCase().trim();
    	for (int i = 0; i < filterSpecial.length; i++)
        {
    		int index = text.indexOf(filterSpecial[i].toUpperCase());
	    	if (index != -1 && index == 0)
	    	{
	    		result = filterSpecial[i];
	    		find = true;
	    		break;
	    	}
        }
    	
    	if (!find)
    	{
	    	for (int i = 0; i < filter.length; i++)
	        {    	
		    	if (text.indexOf(filter[i].toUpperCase()) != -1)
		    	{
		    		result = filter[i];
		    		break;
		    	}
	        }
    	}
    	
    	return result;
    }
    
    /**
     * Method will to remove LINKs
     * @param text
     * @return text without links
     */
    public static String RemoveLink(String text)
    {
    	String newText = text;
    	text = text.toLowerCase();
    	int index = -1; 
    	index = text.indexOf("http://");
    	if (index > -1)
    	{
    		for (int i = index;i < text.length();i++ )
    		{
    			if (text.indexOf(" ",i) > i)
    			{
    				newText = text.replaceAll("http://(.*?) ", "");
    			}
    			else if (text.indexOf("\r",i) > i)
    			{
    				newText = text.replaceAll("http://(.*?)\r", "");
    			}
    		}
    	}
    	
    	return newText;
    }
    
    /**
     * Method will to remove TAGs
     * @param text
     * @return text without TAGs
     */
    public static String RemoveTag(String text)
    {
    	String newText = text;
    	newText = newText.toLowerCase();
    	newText = newText.replaceAll("  "," ");
    	int index = -1;
    	
    	newText = newText.replace("if(", "if (");    	
    	index = newText.indexOf("if (");
    	if (index > -1)
    	{
    		newText = newText.replace("if (", "TAG");
    		for (int i = index;i < newText.length();i++ )
    		{
    			if (newText.indexOf("}",i) > i)
    			{
    				newText = newText.replaceAll("TAG(.*?)}", "");
    			}
    		}
    	}    	
    	
    	newText = newText.replaceAll("  "," ");
    	index = -1; 
		newText = newText.replace("else{", "else {");
    	index = newText.indexOf("else {");
    	if (index > -1)
    	{

    		newText = newText.replace("else {", "TAG2");
    		for (int i = index;i < newText.length();i++ )
    		{    			
    			if (newText.indexOf("}",i) > i)
    			{
    				newText = newText.replaceAll("TAG2(.*?)}", "");
    			}
    		}
    	}
    	
    	newText = newText.replaceAll("  "," ");
    	index = -1; 
    	newText = newText.replace("for(", "for (");
    	index = newText.indexOf("for (");
    	if (index > -1)
    	{
    		newText = newText.replace("for (", "TAG4");
    		for (int i = index;i < newText.length();i++ )
    		{    			
    			if (newText.indexOf("}",i) > i)
    			{
    				newText = newText.replaceAll("TAG4(.*?)}", "");
    			}
    		}
    	}
    	
    	newText = newText.replaceAll("  "," ");
    	index = -1; 
		newText = newText.replace("while(", "while (");
    	index = newText.indexOf("while (");
    	if (index > -1)
    	{
    		newText = newText.replace("while (", "TAG5");
    		for (int i = index;i < newText.length();i++ )
    		{
    			if (newText.indexOf("}",i) > i)
    			{
    				newText = newText.replaceAll("TAG5(.*?)}", "");
    			}
    		}
    	}
    	
    	newText = newText.replaceAll("  "," ");
    	index = -1; 
    	index = newText.indexOf("<!");
    	if (index > -1)
    	{
    		newText = newText.replace("<!", "TAG6");
    		for (int i = index;i < newText.length();i++ )
    		{
    			if (newText.indexOf("->",i) > i)
    			{
    				newText = newText.replaceAll("TAG6(.*?)->", "");
    			}
    		}
    	}
    	
    	
    	return newText;
    }
    
    /**
     * Method will filter the message
     * @param message
     * @return message filtered
     */
    public static String Filter()
    {    
        if (filter.length == 0) // No Filter
            return  "";//message;
        else if (message != null)
        {        	
        	//setMessage(RemoveTag(message));
        	//setMessage(RemoveLink(message));
        	
        	GetDataMessage(message);
        	
        	//breaking the message
        	ArrayList<String> ListMessage = getContent();
        	ArrayList<String> newMessage = ListMessage;
        	
        	//System.out.println("***********************");
        	//System.out.println(content);
        	//System.out.println("***********************");
            String currentFilter;
            int endIndex = -1;
            String special = "";
            int index_normal = -1;
            
            if (ListMessage != null)
            {
	        	for (int j = 0; j < ListMessage.size(); j++)
	        	{
	        		ListMessage.set(j,RemoveLink(ListMessage.get(j)));
	        			        		
	        		if (CheckFilter(ListMessage.get(j).trim()) != "")
	        		{
		                newMessage.set(j,"NULO");	                
	        		}	        		
	            }
	        	String Content = "";
	        	ArrayList<String> list = new ArrayList<String>();
	        	
	        	/*System.out.println("From: " + getMessage_from()+
	        					   "\nTO: " + getMessage_to()+
	        					   "\nDate: " + getSent_date()+
	        					   "\nSubject: " + getSubject()+
	        					   "\nContent: ");
	        					   */
	        	for (int j = 0; j < ListMessage.size(); j++)
	            {
	        		if (newMessage.get(j) != "NULO" && newMessage.get(j).trim().length() > 0 && !CheckFilter(getMessage_from(),newMessage.get(j).trim()))
	        		{
	        			list.add(RemoveLink(newMessage.get(j)));
	        			Content += newMessage.get(j) + " ";
	        		}	        			
	            }
	        	
	        	////COMENTANDO PARA RETIRAR A CARGA DO CORPO DA MSG
	        	Content = RemoveLink(Content);
	        	Content = RemoveTag(Content);
	        	
	        	//System.out.println(Content);
	        	setMessage_content(Content);
	        	setMessage_content_list(list);
	        	
            }
        	
            return  "";
        }
        else
        	return "";
    }

	public static String getTime_zone() {
		return time_zone;
	}

	public static void setTime_zone(String time_zone) {
		EMailFilter.time_zone = time_zone;
	}
}
