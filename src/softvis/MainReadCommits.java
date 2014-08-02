package softvis;


import java.io.*;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;
import org.apache.lucene.search.spell.LevensteinDistance;

import util.ConverteDatas;
import util.readTextFile;
import softvis.FindingDeveloper;

public class MainReadCommits
{

	/**
	 * @param args
	 */
	
  
//    public static long obterMinutos(String hora1) {  
//        String[] time = hora1.split(":");  
//        try {  
//            return Integer.parseInt(time[1]) + (Integer.parseInt(time[0])*60);  
//        } catch (NumberFormatException e) {  
//            return 0;  
//        }  
//    }  
  
//    public static String obterTempo(long minutos){  
//        return String.format ("%02d:%02d", (minutos / 60), (minutos % 60));  
//    } 
	
	public static void main(String[] args) {
		
       //System.out.print("Cahnge Hours: " + ConverteDatas.changeTime(19, 30, 0, 24, 0));

			FindingDeveloper developers = new FindingDeveloper();
			developers.lookForDeveloper();
			//developers.lookForCommitsPerDate();
			
			
			
			
			
			
			
			
			
			
			
			
			

		//READING COUNT_COMMITS AND PRINT THE DATA IN SYSTEM 
//		String nameFile = "C:/Users/mario.Think_Mario/workspace/Neurominer/count_commits.txt";
//        FileReader reader;
//		try {
//			reader = new FileReader(nameFile);
//
//        //leitor do arquivo
//        BufferedReader leitor = new BufferedReader(reader);	
//        String linha="";
//        String[] tagsDevFile  = new String[4];
//        String tagNickName = "" , tagNameDev = "", tagLat = "", tagLng = "";
//        
//        while(true)
//        {
//        	
//          try {
//			linha=leitor.readLine();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//          if(linha==null)
//            break;
//          //System.out.println("\n Linha: " + linha);
//          //sIterator = readTextFile.getStringSplit(linha);
//          tagsDevFile = readTextFile.getStringSplitArray(linha);
//    	  tagNickName = tagsDevFile[0]; 
//          tagNameDev = tagsDevFile[1]; 
//          tagLat = tagsDevFile[2];
//          tagLng = tagsDevFile[3];
//          
////          System.out.println("Linha: " + linha);
////          System.out.println("tagNickName : " + tagNickName);
////          System.out.println("tagNameDev : " + tagNameDev);
////          System.out.println("tagLat : " + tagLat);
////          System.out.println("tagLng : " + tagLng);
//          
//        }
//        
//		} catch (FileNotFoundException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
			
				
			
// String s1 = "colm maccarthaigh";
// String s2 = "colm maccarthaigh";
// if (s1.equals(s2)) System.out.print("Equals");
		
	//	StringTokenizer st = new StringTokenizer(frase);
	   	
     //   while (st.hasMoreTokens())
     //   {		
        	
		//FindingDeveloper.lookForDevLucene("William A. Rowe Jr.", "William A. Rowe Jr.");
		
				 
//		 try {
//			 File file = new File("C:/Users/mario.Think_Mario/workspace/Neurominer/texto.txt");  
//	         FileOutputStream fos = new FileOutputStream(file);  
//	         String text = "quero gravar este texto no arquivo";  
//	         fos.write(text.getBytes()); 
//	         String newLine = System.getProperty("line.separator");
//	         fos.write(newLine.getBytes());
//	         text = "Quero gravar este texto AQUI TAMBEM"; 
//	         fos.write(text.getBytes());      
//	         fos.close();
//	         System.out.println("Arquivo gravado com sucesso: " + file.getName());		
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		File file = new File("C:/Users/mario.Think_Mario/workspace/Neurominer/texto.txt");
//		FileWriter fw;
//		try {
//		fw = new FileWriter(file);
//
//		  BufferedWriter bw = new BufferedWriter(fw);
//		  String text = "Teste 1";
//		  bw.write(text);
//		  bw.newLine();
//		  text = "Teste 2";
//		  bw.write(text);
//		  bw.flush();  
//		  bw.close();
//		  System.out.println("Arquivo gravado com sucesso: " + file.getName());
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		
	}

}
