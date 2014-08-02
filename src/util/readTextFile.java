package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

import com.sun.java_cup.internal.runtime.Scanner;

public class readTextFile
{
	private String pathRead = "";
	private String pathWrite = "";
	private File fileWrite;

	public readTextFile(String pathRead, String pathWrite)
	{
		setPath(pathRead);
		setPathWrite(pathWrite);
		File file = new File(getPathWrite()); //se já existir, será sobreescrito
		setFileWrite(file);
	}

	public String getPath() {
		return pathRead;
	}

	public void setPath(String path) {
		this.pathRead = path;
	}
	
	public static Iterator<String> getStringSplit(String s)
	{  
		   int i = 0;
		   List<String> strings   = new ArrayList<String>(); 
		   List<String> stringsOut = new ArrayList<String>(); 
		  // List<String> resultados = new ArrayList<String>();
		   StringTokenizer st = new StringTokenizer(s, "|");  
		   while(st.hasMoreTokens())
		   {  
			  if ((i != 1) && (i != 3))
			  {
				  strings.add(st.nextToken());
			  }
			  else stringsOut.add(st.nextToken());
			  i++;
		   }  
		   return strings.iterator();  
		} 
	
	public static String[] getStringSplitArray(String s)
	{  
		   int i = 0;
		   int j = 0;
		   //List<String> strings   = new ArrayList<String>();		   
		   List<String> stringsOut = new ArrayList<String>(); 
		  // List<String> resultados = new ArrayList<String>();
		   StringTokenizer st = new StringTokenizer(s, "|"); 
		   String[] strings = new String[st.countTokens()];
		   while(st.hasMoreTokens())
		   {  
			  if ((i != 1) && (i != 3))
			  {
				  strings[j] = st.nextToken();
				  j++;
			  }
			  else stringsOut.add(st.nextToken());
			  i++;
		   }  
		   return strings; 
		} 
	
	public void readFile()
	{
		String mostra="";
		String[] json;
		Iterator<String> sIterator;
		String tag;
		//int i = 0;
	    //String nomeArq="arquivo.txt"; //Nome do arquivo,
		String nameFile = getPath();
	//pode ser absoluto, ou pastas /dir/teste.txt
	    String linha="";
	    File arq = new File(nameFile);

	    //Arquivo existe
	    if (arq.exists()){
	      mostra="Arquivo - '"+nameFile+"', aberto"+
	             " com sucesso!\n";
	      mostra+="Tamanho do arquivo "+
	           Long.toString(arq.length())+"\n";
	      //tentando ler arquivo
	      try{
	        mostra+="Conteudo:\n";
	        //abrindo arquivo para leitura
	        FileReader reader = new FileReader(nameFile);
	        //leitor do arquivo
	        BufferedReader leitor = new BufferedReader(reader);
	        while(true)
	        {
	          //i = 0;
	          linha=leitor.readLine();
	          if(linha==null)
	            break;
	          //mostra+=linha+"\n";
	          System.out.println("\n Linha: " + linha);
	          //json = linha.split("|");
	          //json = getStringSplit(linha);
	          sIterator = getStringSplit(linha); 
	          
	          
//	          while(sIterator.hasNext())
//	          {
//	        	  tag = sIterator.next();
//	        	  System.out.println("Tag: " + tag);
//	          }
	        }
	      }
	      catch(Exception erro) {}
	     // JOptionPane.showMessageDialog(null,mostra,"Arquivo"+
	     //            "...",1);
	     // System.out.println("Content of File: \n");
	     // System.out.println("\n" + mostra);
	      
	    }
	    //Se nao existir
	    else
	      JOptionPane.showMessageDialog(null,"Arquivo nao"+
	                 " existe!","Erro",0);
	  }
	

	
	public void writeFileOld(String text) throws IOException
	{		  
		FileWriter fw = new FileWriter(getFileWrite());
		BufferedWriter bw = new BufferedWriter(fw);  						  
		bw.write(text);
		bw.newLine();
		bw.flush();  
		bw.close();
		System.out.println("Arquivo gravado com sucesso: " + getFileWrite().getName());
	}
	
	public void writeFile(String text) throws IOException
	{
		  
         File file = new File(getPathWrite());  
         FileOutputStream fos = new FileOutputStream(file);  
         fos.write(text.getBytes());      
         fos.close();
         System.out.println("Arquivo gravado com sucesso: " + getFileWrite().getName());
	}

	public String getPathWrite() {
		return pathWrite;
	}

	public void setPathWrite(String pathWrite) {
		this.pathWrite = pathWrite;
	}
	
	public File getFileWrite() {
		return fileWrite;
	}

	public void setFileWrite(File fileWrite) {
		this.fileWrite = fileWrite;
	}
	
	

	
}
