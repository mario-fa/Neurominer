package Processing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Vector;
import java.io.*;

import sun.security.pkcs.ContentInfo;

import Compare.*;
import Dados.AcessoBanco;
import Entity.EntDeveloper;

//PoterStemAnalyzer

import org.apache.lucene.queryParser.ParseException;
import java.io.StringReader;

import javax.swing.text.StyledEditorKit.BoldAction;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;

//Using Porte + Dictionary
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

public class Preprocessing
{
	//private static String path_msg = "D:/[DATAMINING]/[Neurominer2]/2000-2009/";
	//private static String path_msg = "C:/file_apache/";
	private static String path_msg = "C:/Arquivos Mário/EXPERIMENTO NEUROMINER/Arquivos Apache/processing/";
	//private static String path_stopwords = "D:/[DATAMINING]/stopwords.txt";	
	private static String path_stopwords = "C:/Users/mario.Think_Mario/workspace/Neurominer/stopwords.txt";
	/**
	 * Methodo will read the txt file
	 * @param file name
	 * @return text string
	 */
	public static String readTextFile(String fileName)
	{
        StringBuffer buffer = new StringBuffer();
        try 
        {
            FileInputStream fis = new FileInputStream(fileName);
            InputStreamReader isr = new InputStreamReader(fis, "ISO-8859-1");
            Reader in = new BufferedReader(isr);
            int ch;
            while ((ch = in.read()) > -1)
            {
                buffer.append((char) ch);
            }
            in.close();
            return buffer.toString();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
	}	
	
	/**
	 * Methodo will read the txt file
	 * @param file name
	 * @return Stringbuffer text
	 */
	public static StringBuffer readFile(String fileName)
	{
        StringBuffer buffer = new StringBuffer();
        try 
        {
            FileInputStream fis = new FileInputStream(fileName);
            InputStreamReader isr = new InputStreamReader(fis, "ISO-8859-1");
            Reader in = new BufferedReader(isr);
            int ch;
            while ((ch = in.read()) > -1)
            {
                buffer.append((char) ch);
            }
            in.close();
            return buffer;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
	}
	
	/**
	 * Save data in database
	 * @throws SQLException if there is exception.
	 */
	public static void InsertLoadEmail(String sent_date,String message_from,String message_to, String subject,String content, String email_file, String timeZone) throws Exception
	{		
		if (!sent_date.equals("") && !message_from.equals("") && !message_to.equals("") && !subject.equals("") && !content.equals(""))
		{
			try
			{
				AcessoBanco con = new AcessoBanco().getInstancia();
				
//				String sql = "INSERT INTO neurominer.loaded_email("+
//							 "load_email_project_id, sent_date, message_from,"+ 
//							 "message_to, subject, content)"+
//							 "VALUES (1, '"+sent_date+"', '"+message_from+"',"+
//							 "'"+message_to+"', '"+subject+"', '"+content+"');";
				// The code over was update to:
				
				//System.out.println(sql);
				String sql = "INSERT INTO neurominer.loaded_email("+
						 "load_email_project_id, sent_date, message_from,"+ 
						 "message_to, subject, content, email_file, time_zone)"+
						 "VALUES (1, '"+sent_date+"', '"+message_from+"',"+
						 "'"+message_to+"', '"+subject+"', '"+content+"', '" +email_file + "', '" + timeZone +"')";				
				
				//load_email_project_id = 1 - Apache
				int lines = 0;			
				lines = con.exec_command(sql);
				
				
				if (lines <= 0)
					System.out.println("problem! email: "+message_from+"\n date: "+sent_date);
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * 
	 */
	public static void FilterView (ArrayList<String> texto, String file) throws IOException, ParseException 
	{		
		texto = BrokingMsg(texto);
		/*//Verificação
		System.out.println("\n\n#########################  ANTES DA FILTRAGEM  ##########################");
		System.out.println(texto);
		System.out.println("\n###########################################################################");
		try 
		{
			GetDictionary("U");
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("\n\n#########################  DEPOIS DA FILTRAGEM  #########################");
		*/
		//seting message
		EMailFilter.setMessage(texto);		               
		EMailFilter.Filter();// Realizando a filtragem

		//Using PorterSteamAnalizer	
	  	//COMENTANDO PARA RETIRAR A CARGA DO CORPO DA MSG
//		StringReader sr = new StringReader(EMailFilter.getMessage_content());
//	  	PorterStemAnalyzer ps = new PorterStemAnalyzer();
//	  	ps.ReadFile(path_stopwords);
//	  	TokenStream tokenstream = ps.tokenStream(null, sr);
	  	
	  	//COMENTANDO PARA RETIRAR A CARGA DO CORPO DA MSG
	  	//String content = PorterStemAnalyzer.displayTokenStream(tokenstream);

		//PARA INSERIR O CORPO DA MSG SEM RETIRAR CARACTERES ESPECIAIS: EMailFilter.getMessage_content()
		//System.out.println("MSG: " + EMailFilter.getMessage_content());

	  	
	  	String date = RemoveWordsDate(EMailFilter.getSent_date());
	  	String timeZone = EMailFilter.getTime_zone();
	  	String from = EMailFilter.getMessage_from().replaceAll("'", "\"");
	  	String to = EMailFilter.getMessage_to().replaceAll("'", "\"");
	  	String subject = EMailFilter.getSubject().replaceAll("'", "\"");
	  	
	  	//Removendo "BKB" e Redimensionando os textos quando necessario
	  	from = RemoveBKB(from);
	  	//from = ResizeText(from,300);
	  	to = RemoveBKB(to);
	  	to = ResizeText(from,3000);
	  	subject = RemoveBKB(subject);
	  	subject = ResizeText(subject,500);
	  	
	  	//content = ResizeText(subject,10000);
	    //COMENTANDO PARA RETIRAR A CARGA DO CORPO DA MSG
	  	String content = "-";
	  	
	  	try
	  	{
	  		System.out.println("E-MAIL: " + from + " - " + date + " - " + subject + " - " + to + " - " + file);
	  		InsertLoadEmail(date,from,to,subject,content, file, timeZone);
	  	}
	  	catch (Exception e)
	  	{
			// TODO: handle exception
	  		//e.printStackTrace();
	  		e.toString();
		}
		//System.out.println(filtro);
		//System.out.println("\n###########################################################################");
	}	
	
	/**
	 * Method will to remove words after the seconds
	 * @param msg
	 * @return new array message
	 */
	public static String RemoveWordsDate(String text)
	{
		int pos = -1;
    	
    	pos = text.lastIndexOf(":");
    	
    	if (pos > -1)
    	{
    		text = text.substring(0, pos + 3);
    	}
    	
		return text;
	}
	
	/**
	 * Method will to resize the text
	 * @param msg
	 * @return new array message
	 */
	public static String ResizeText(String text,int size)
	{
		if (text.length() > size)
		{
			text.substring(0, size);
		}
		
		return text;
	}
	
	public static String RemoveBKB(String text)
	{
		//[BKB] refers to line break
    	return text.replaceAll("[BKB]", "");
	}
	
	public static boolean CheckSizeWord(String text)
	{
		boolean result = false;
		
		String[] words;
		words = text.split(" ");  
        for (int i=0; i < words.length; i++)
        {
        	if (words[i].length() > 20)
        	{
        		result = true;
        	}
        }
        
        return result;
	}
	
	/**
	 * Method will to put 'bkb' in the end line
	 * @param msg
	 * @return new array message
	 */
	public static ArrayList<String> BrokingMsg(ArrayList<String> msg)
	{
		int pos = -1;
    	
    	for (int i = 0; i < msg.size();i++)
    	{
    		if (msg.get(i) != null)
    		{	    		
    			//get content  
    			if (EMailFilter.CheckFilter(msg.get(i),"x-spam-rating:"))
	    		{
	    			int index = msg.get(i).indexOf("x-spam-rating:");
	    	    	if (index == 0)
	    	    	{
	    	    		pos = i;
	    	    	}
	    		}	
    			else if (EMailFilter.CheckFilter(msg.get(i),"x-virus-checked:"))
	    		{
	    			int index = msg.get(i).indexOf("x-virus-checked:");
	    	    	if (index == 0)
	    	    	{
	    	    		pos = i;
	    	    	}
	    		}
	    		//message without reply    		
	    		else if (EMailFilter.CheckFilter(msg.get(i),"x-status:"))
	    		{
	    			int index = msg.get(i).indexOf("x-status:");
	    	    	if (index == 0)
	    	    	{
	    	    		pos = i;
	    	    	}
	    		}		
	    		//message which reply
	    		else if (EMailFilter.CheckFilter(msg.get(i),"reply-to:"))
	    		{
	    			int index = msg.get(i).indexOf("reply-to:");
	    	    	if (index == 0)
	    	    	{
	    	    		pos = i;
	    	    	}
	    		}
    		}
    	}
    	
    	//[BKB] refers to line break
    	for (int i = pos+1; i < msg.size();i++)
    	{
	    	msg.set(i, msg.get(i)+"[BKB]");
    	}
    	
		return msg;
	}

    public static void main(String[] args)
    {
    	String [] filter =
    	{
                "escrito por",
                "wroted By",
                "de:",
                "de :",
                "from:",
                "from :",
                "para:",
                "para :",
                "to:",
                "to :",
                "subject:",
                "subject :",
                "assunto:",
                "assunto :",
                "enviada em:",
                "enviada em :",
                "citado",
                "mentioned",
                "wrote:",
                "wrote :",
                "escreveu:",
                "escreveu :",
                "mencionou:",
                "mencionou :",
                "date:",
                "date :"
        };
    	
    	String[] FilterSpecial =
    	{                
                "-",
                ">",
                "_",
                "}",
                "{",
                "=",
                "*",
                "@",
                "#",
                "$",
                "%",
                "¨",
                "&",
                "§",
                "+",
                "!",
    	};
    	
    	String tipo = "U"; //N - N-files, U - Unic File
    	
        EMailFilter.setFilter(filter);
        EMailFilter.setFilterSpecial(FilterSpecial);

        // Lembrar de alterar o diretório dos arquivos sempre que necessário!
        String directory = path_msg;
        //String prefix = "199503";
        String contentFile = "";

        // create a file that is really a directory
        File aDirectory = new File(directory);

        // get a listing of all files in the directory
        String[] filesInDir = aDirectory.list();
        
        // sort the list of files (optional)
        Arrays.sort(filesInDir);
         
		if (tipo.equals("N"))
		{
			/*
			System.out.println("\nTRATANDO TODOS OS ARQUIVOS DO DIRETÓRIO " +
			directory + " CUJO NOME COMEÇA COM O TEXTO " + prefix + "\n");
			
			for (int i = 0; i < filesInDir.length; i++)
			{
				if (filesInDir[i].startsWith(prefix))
				{
					contentFile = readTextFile(directory + filesInDir[i]);
					System.out.println("\nARQUIVO : " + directory + filesInDir[i]);
					System.out.println("\n\n#########################  ANTES DA FILTRAGEM  ##########################");
					//System.out.println(contentFile);
					System.out.println("\n###########################################################################");
					
					System.out.println("\n\n#########################  DEPOIS DA FILTRAGEM  #########################");
					//EMailFilter.setMessage(contentFile);
					               
					contentFile = EMailFilter.Filter();// Realizando a filtragem
					System.out.println(contentFile);
					System.out.println("###########################################################################");            
				}
			}
			*/
		}
		else
		{
			StringBuffer strBuffer = new StringBuffer();
			strBuffer = readFile(directory + filesInDir[0]);		
			ArrayList<String> texto = new ArrayList<String>();
			
			for (int i = 0; i < filesInDir.length; i++)
			{
	        	try
	        	{
					FileInputStream fis = new FileInputStream(path_msg+filesInDir[i]);
			        InputStreamReader isr = new InputStreamReader(fis, "ISO-8859-1");
			        Reader in = new BufferedReader(isr);					
				
		            ArrayList<String> temp = new ArrayList<String>();
					StringBuffer sim = new StringBuffer();					
					boolean find = false;
					int count = 0;
					int ch;
					while((ch = in.read()) > -1)
					{					
						sim.append((char)ch);					
					}  
					
					in.close();
					
					String ddd = sim.toString();
					
					Scanner sc = new Scanner(ddd);
					String content = "";
					int lines = 0;
				  
				    while (sc.hasNextLine())  
				    {
				    	String a = "";
				    	a = sc.nextLine().toLowerCase();
				    	if (true)// if (!CheckSizeWord(a) //Testar
				    	{				    	
					    	if (EMailFilter.CheckFilter(a,"From owner-new-httpd") || EMailFilter.CheckFilter(a,"From dev-return") 
					    	 || EMailFilter.CheckFilter(a,"From new-httpd"))
							{
								if (find)
								{
									find = false;
								}
								else
								{
									find = true;
								}
							}
							if (find)
							{	
								temp.add(a);
							}
							else if (!find)
							{
								lines++;
								//FilterView(temp);
								//The code above was update to:
								FilterView(temp,filesInDir[i]);
								temp = new ArrayList<String>();
								temp.add(a);
								find = true;
							}
							if (!sc.hasNextLine())
							{
								lines++;
								//FilterView(temp);
								//The code above was update to:
								FilterView(temp,filesInDir[i]);
							}	
				    	}				    	
				    } 
				    System.out.println("Messages inserted: "+lines);
				    System.out.println("Arquivo: "+filesInDir[i].toString());
					//System.out.println("QUANTIDADE DE MENSAGENS: "+texto.size());	
	        	}
	        	catch (Exception e)
				{
					e.printStackTrace(); 
					// TODO: handle exception
				}
			}
		}			
    }    
}









