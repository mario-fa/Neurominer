package softvis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.lucene.search.spell.LevensteinDistance;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

import util.ConverteDatas;
import util.readTextFile;

import Dados.AcessoBanco;

public class FindingDeveloper
{
	
	private int countSun = 0;
	private int countMon = 0;
	private int countTue = 0;
	private int countWen = 0;
	private int countThu = 0;
	private int countFri = 0;
	private int countSat = 0;
	
	private int h00 = 0, h01 = 0,h02 = 0,h03 = 0,h04 = 0,h05 = 0,h06 = 0,h07 = 0, h08 = 0, h09 = 0,h10 = 0,h11 = 0,h12 = 0;
	private int h13 = 0, h14 = 0,h15 = 0,h16 = 0,h17 = 0,h18 = 0,h19 = 0,h20 = 0, h21 = 0, h22 = 0,h23 = 0,h24 = 0;
	
	private HashMap<String, String> hp_utc = new HashMap<String, String>();	

	public void countWeekDay(int day)
	{
		switch( day )
		{
		  case 1: 
			  this.setCountSun(this.getCountSun() + 1);
			  break;
		  case 2: 
			  this.setCountMon(this.getCountMon() + 1);
			  break;
		  case 3: 
			  this.setCountTue(this.getCountTue() + 1);
			  break;
		  case 4: 
			  this.setCountWen(this.getCountWen() + 1);
			  break;
		  case 5: 
			  this.setCountThu(this.getCountThu() + 1);
			  break;
		  case 6: 
			  this.setCountFri(this.getCountFri() + 1);
			  break;
		  case 7: 
			  this.setCountSat(this.getCountSat() + 1);
			  break;
		
		}
	}
	
	@SuppressWarnings("deprecation")
	public void lookForDeveloper()
	{
		String date = "", dateUTC = "", time = "", timeUTC = "", dateValue = "", newDate = "", newTime = "", hours = "", min = "", seg = "", hoursAdd = "", minAdd="";
		Date dateEmail;		
		String dateValueDB        = "";
		String from = "", fromValue = "", lineEmailJS = "", timeZone = "";;
		String[] tags  = new String[2];
		String[] tagsDevFile  = new String[4];
		String line = "", email = "";;
		String emailsFilesMoreThan07   = "C:/Users/mario.Think_Mario/workspace/Neurominer/sources/emailsFilesTESTE.txt";
		String pathSimilar0_ = "C:/Users/mario.Think_Mario/workspace/Neurominer/sources/emailsSimilar09.txt";
		String pathSimilar0_8 = "C:/Users/mario.Think_Mario/workspace/Neurominer/sources/emailsSimilar08.txt";
		String pathSimilar0_7 = "C:/Users/mario.Think_Mario/workspace/Neurominer/sources/emailsSimilar07.txt";
		String emailJS = "C:/Users/mario.Think_Mario/workspace/Neurominer/sources/emailJSTESTE.txt";		
		int count09    = 0;
		int count08    = 0;
		int countValue = 0, countDate = 0, countDateFinal = 1, countDay = 0, countMonth = 0, countRegister = 0, countHoursBefore = 0, countHoursAfter = 0;
		int countNOT = 0, utcCommitI = 0, hoursAddI = 0;
		int i        = 0, weekday = 0, day = 0, month = 0, year=0, dayCurrent = 1, monthCurrent = 1, yearCurrent = 1995, monthEmail=0, weekDayMod7 = 0;		
		float distancia = 0;
     	LevensteinDistance ls = new LevensteinDistance();	
		String tagNickName = "" , tagNameDev = "", tagLat = "", tagLng = "", s1 = "", s2 = "", subject = "", utcCommit = "";
		
		ArrayList emailsList = new ArrayList();
		
		int idEmail;		
		Calendar dataCalendar;
        AcessoBanco con = new AcessoBanco().getInstancia();
				
			   //String sql = "SELECT * FROM neurominer.loaded_email order by sent_date limit 100";
                 String sql = "SELECT * FROM neurominer.loaded_email order by sent_date";
				
				ResultSet rs;

				try {
					rs = con.getRS(sql);

				
                // readin Developers's file - count_commit.txt
				Iterator<String> sIterator;
				String tag;
				String nameFile = "C:/Users/mario.Think_Mario/workspace/Neurominer/sources/count_commits.txt";
				String nameFileUTC = "C:/Users/mario.Think_Mario/workspace/Neurominer/sources/utc.txt";
			    String linha="";
			    String linhaUTC = "";
			    File arq = new File(nameFile);
			    File arqUTC = new File(nameFileUTC);
				//Create text file
			    //----------- File with register above  0.9 distance
				File file = new File(emailsFilesMoreThan07); // Writer file
				FileWriter fw;
				  //----------- File with email register
				File fileEmailJS = new File(emailJS); // Writer file
				FileWriter fwEmailJS;
				//-----------
				//----------- File with register between  0.8 and 0.9
				File file08 = new File(pathSimilar0_8); // Writer file
				FileWriter fw08;
				//-----------
				//----------- File with register between  0.6 and 0.8
				File file07 = new File(pathSimilar0_7); // Writer file
				FileWriter fw07;
				//-----------
				
				try
				{
					//----------- File with register above the distance 0.9 
					fw = new FileWriter(file);
					BufferedWriter bw = new BufferedWriter(fw);	
					//----------- File with emailregister
					fwEmailJS = new FileWriter(fileEmailJS);
					BufferedWriter bwEmailJS = new BufferedWriter(fwEmailJS);	
					//----------- File with register between  0.8 and 0.9
					fw08 = new FileWriter(file08);
					BufferedWriter bw08 = new BufferedWriter(fw08);
					//----------- File with register between  0.8 and 0.9
					fw07 = new FileWriter(file07);
					BufferedWriter bw07 = new BufferedWriter(fw07);
					
			    if (arqUTC.exists())
			    {
			    	//hp_utc
			    	
			    	FileReader readerUTC = new FileReader(nameFileUTC);
			        //leitor do arquivo
			        BufferedReader leitorUTC = new BufferedReader(readerUTC);			    	
			    	while(true)
			        {			        	
			          i = 0;
			          linhaUTC=leitorUTC.readLine();
			          if(linhaUTC==null)			          
			            break;
			          hp_utc.put(linhaUTC.substring(0, linhaUTC.indexOf(";")), linhaUTC.substring(linhaUTC.indexOf(";")+1,linhaUTC.length()));
			        }

			    	//System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");	
			    	//System.out.println("HP: " + hp_utc.toString());			    				    
			    }			    
			    		    
			    if (arq.exists())
			    {

			      //tentando ler arquivo
			        //abrindo arquivo para leitura			    				    			   			    	

				    System.out.println("------------------------------------- DEVELOPERS -----------------------------------\n");			    
				   
				    while (rs.next())
				    {

				      from     = rs.getString("message_from");
				      from     = from.trim();
				      date     = rs.getString("sent_date").trim();
				      time     = date.substring(10, date.length()-3).trim();				      
				      date     = date.substring(0, 10);
				      timeZone = rs.getString("time_zone");		      
				      				      
				      //Look for timeZone
				      if (!((timeZone.indexOf("-") == 0) || (timeZone.indexOf("+") == 0)))
				      {
				    	  timeZone = hp_utc.get(timeZone.toUpperCase());				    	  
				      }
				      				      
//				      // recover day, month and year in string
//				      hours = time.substring(0, 2);
//				      min = time.substring(3, 5);
//				      seg = time.substring(6, time.length()-1);
				      
				     // System.out.print("TIME: " + time +  " --> Hours: " + hours + " - Min: " + min + " - Seg: " + seg + "'\n");
				      
				      //calculate the new Time and new Date	UTC
				      if (timeZone != null)
				      {
				       if (timeZone.indexOf(":") > 0)
				       {
				    	  hoursAdd =  timeZone.substring(0, 2);
				    	  minAdd   =  timeZone.substring(3, 5);
				       }
				       else
				       {
				    	  hoursAdd = timeZone; 
				    	  minAdd = "0";
				       }
				      } else timeZone = "0";
				      hoursAddI = Integer.parseInt(hoursAdd.trim());

				    		  
				      day      = Integer.parseInt(date.substring(8, 10)); //2005-12-31				      
				      month    = Integer.parseInt(date.substring(5, 7));
				      year     = Integer.parseInt(date.substring(0, 4));
				      
//				      newTime  = ConverteDatas.changeTime3(year, month, day, hours.trim(), min.trim(), seg.trim(), hoursAddI, minAdd.trim());				      
//				      System.out.println(" ----------------- UTC ---------------");
//				      System.out.println(" hoursAdd: " + hoursAdd + " minAdd: " + minAdd);				      
//				      System.out.println("Date: " + year + "/" + month + "/" + day + " - " + time+ " - NEWTIME: " + newTime);
				    	  				      
				      if (countRegister == 0) {
				    	  dayCurrent     = day;
				    	  monthCurrent   = month;
				    	  yearCurrent    = year;
				    	  countRegister  = 1;
				      }
				      
				     // aqui
				      
				      // Retorna o dia da semana: 1=domingo, 2=segunda-feira, ..., 7=sábado
				     // dataCalendar = new GregorianCalendar(year, month-1, day);
				     // weekday = dataCalendar.get(Calendar.DAY_OF_WEEK);	
				     //ERA AQUI
				      
				      //System.out.println("From: " + from + " DATE: " + date);
				      		      
					  	
				      // COMENTADO A APRTIR DAQUI - ABAIXO
					  subject  = rs.getString("subject");
					  idEmail  = rs.getInt("loaded_email_id");
					  tags     = splitEmail(from);
					  
				        FileReader reader = new FileReader(nameFile);
				        //leitor do arquivo
				        BufferedReader leitor = new BufferedReader(reader);
					
					// --------------------------------------- Varrendo count_commit.txt ----------------------
				    //Arquivo existe
				    
				        while(true)
				        {
				        	
				          i = 0;
				          linha=leitor.readLine();

				          if(linha==null)			          
				            break;
				          //System.out.println("\n Linha: " + linha);
	                      //sIterator = readTextFile.getStringSplit(linha);
				          tagsDevFile = readTextFile.getStringSplitArray(linha);
			        	  tagNickName = tagsDevFile[0]; 
				          tagNameDev = tagsDevFile[1]; 
				          tagLat = tagsDevFile[2];
				          tagLng = tagsDevFile[3];
				          utcCommit = tagsDevFile[4];
				          if (utcCommit == null)
				          {
				        	  utcCommit = "0";
				          }				          
				          utcCommitI = Integer.parseInt(utcCommit.trim());				          			          				                 	       
				          
					      //tags BD				
					      if (tags[0].equals("1")) // tags[1] = name; tags[2] = e-mail
					      { //Indica que o vetor possue uma coleção de campos do email válido. 0 indica o contrário					    	  
					    	s1 = tags[1].toLowerCase()	;	
					    	email = tags[2].toLowerCase();
					    	s2 = tagNameDev.toLowerCase();
					  		s1 = s1.replace(".", "").trim();
							s2 = s2.replace(".", "").trim();
							
							//System.out.println("STRINGS - S1: " + s1 + " - S2: " + s2);
				        	distancia = ls.getDistance(s1,s2); //Checking if name from DB is similar to name from commit's file;
				        	//System.out.println("Distancia: " + distancia + " --> tags[1]: " + s1 + " - " + s2);

					    	if ((distancia >= 0.9) || (distancia >= 0.8) || (distancia >= 0.7) || email.equals("nd@apache.org") || email.equals("nd@perlig.de")) {
				        	//if(null==null){					    								     							      
					    		if (!(s1.equals("hiroyuki hanai") || s1.equals("mark sutton") || s1.equals("david ziegler") || s1.equals("grisha trubetskoy")))
					    		{		
					    			if (email.equals("nd@apache.org") || email.equals("nd@perlig.de"))
					    			{
					    				tagNickName = "malo";
					    			}
					        	 
//					    			  hours = time.substring(0, 2);
//								      System.out.println("TIME: " + time+ " HOURS Before: " + Integer.parseInt(hours.trim()));
//							    	  if (Integer.parseInt(hours.trim()) == 12)
//							    	  {
//							    		  countHoursBefore++;
//							    	  }
							    	  
					    			//if (!hoursAdd.equals(utcCommit))
					    			if(!(hoursAddI == utcCommitI))
					    			{
					    				utcCommitI = utcCommitI * -1;	
					    			  //Change date to UTC date
					    				// recover day, month and year in string
									      hours = time.substring(0, 2);
									      min = time.substring(3, 5);
									      seg = time.substring(6, 8);
									      //System.out.println("Time: " + time);
								    	  
								      newTime  = ConverteDatas.changeTime3(year, month, day, hours.trim(), min.trim(), seg.trim(), hoursAddI, minAdd.trim());				      
//								      System.out.println(" ----------------- UTC ---------------");
//								      System.out.println(" hoursAdd: " + hoursAdd + " minAdd: " + minAdd);				      
//								      System.out.println("Date: " + year + "-" + month + "-" + day + " : " + time+ " - NEWTIME: " + newTime);								      
								      
								      //change date to local date of developer
							    		dateUTC = newTime.substring(0,newTime.indexOf("|")).trim();
							    		newTime = newTime.substring(newTime.indexOf("|")+1, newTime.length()).trim();							    		
							    		
									      day      = Integer.parseInt(dateUTC.substring(8, 10)); //2005-12-31				      
									      month    = Integer.parseInt(dateUTC.substring(5, 7));
									      year     = Integer.parseInt(dateUTC.substring(0, 4));
									      
									      hours = newTime.substring(0, 2);
									      min   = newTime.substring(3, 5);
									      seg   = newTime.substring(6, 8);
								      
								      //Change UTC date to local developer date
//								      System.out.println(" -------------------- LOCAL --------------------" );
//								      System.out.println(" hoursAdd-LOCAL: " + utcCommitI + " minAdd: 0" );
								      timeUTC = newTime;
								      newTime  = ConverteDatas.changeTime3(year, month, day, hours.trim(), min.trim(), seg.trim(), utcCommitI, "0".trim());
//								      System.out.println("Date2: " + year + "-" + month + "-" + day + " : " + timeUTC+ " - NEWTIME: " + newTime);
//								      System.out.println(" -----------------------------------------------" );
								      
							          date = newTime.substring(0,newTime.indexOf("|"));
							    	  newTime = newTime.substring(newTime.indexOf("|")+1, newTime.length());
							    	  time    = newTime;
							    	  
//							    	  hours = newTime.substring(0, 3);
//							    	  System.out.println("TIME: " + newTime+ " HOURS After: " + Integer.parseInt(hours.trim()));
//							    	  if (Integer.parseInt(hours.trim()) == 12)
//							    	  {
//							    		  countHoursAfter++;
//							    	  }
							    	  countDate++;

								      day      = Integer.parseInt(date.substring(8, 10)); //2005-12-31				      
								      month    = Integer.parseInt(date.substring(5, 7));
								      year     = Integer.parseInt(date.substring(0, 4));
					    			}
					    			
								      // Retorna o dia da semana: 1=domingo, 2=segunda-feira, ..., 7=sábado
								      dataCalendar = new GregorianCalendar(year, month-1, day);
								      weekday = dataCalendar.get(Calendar.DAY_OF_WEEK);	
					    			
					    			// Domingo: 6, Segunda: 0 - terca: 1
					    			weekDayMod7 = (weekday+5) % 7;
					    			countWeekDay(weekday);
					    			//line = "{'author': '"  + tagNickName.trim() + "', 'value': " +countDateFinal +", 'lat': "+tagLat +", 'lng': " +tagLng+ ", 'date': '" +date+ "', 'time': '" +time+ "', 'weekday': "+ weekday + "-" + weekDayMod7 +"},";
					    			line = "{'author': '"  + tagNickName.trim() + "', 'value': " +countDateFinal +", 'lat': "+tagLat +", 'lng': " +tagLng+ ", 'date': '" +date+ "', 'time': '" +time+ "', 'weekday': "+ weekDayMod7 +"},";
					    			// {"author": "JeremySkinner", "value": 1, "lat": -1, "lng": -1, "date": "2009-12-20", "time": "21:44:41", "weekday": 6},					    			          
					        	  
					    			//Order information of e-mails by date
					    			
					    			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");					    			
					    			dateEmail = sdf.parse(date);
					    			//System.out.print("Date -------> " + date + " - NEW: " + dateEmail.toString());
					    			emailsList.add(new EmailDeveloper(dateEmail, line));
					    			
					    			countHours(time);
					    			
					    		  System.out.println(line + "--> COUNT: " + countMonth);
					              count09++;
//							      bw.write(line);
//							      bw.newLine();	
							      							      
							      //Checking tha day of week and count how many message has per day
							      //System.out.println("DAY: " + day + " - DAY CURRENT: " + dayCurrent + " - Month Current: " + monthCurrent + " - YEAR CURR: " + yearCurrent);				      
							      if (dayCurrent == day){
							    	  countDay++;
							    	  countMonth = countMonth+1;
							      }
							      else
							      {		    	  
							    	  if (monthCurrent == month){
							    		  dayCurrent = day;
							    		  countMonth = countMonth+1;

							    		  //countMonth = countMonth+countDay;
							    	  }
							    	  else {	
							    		  monthEmail = monthCurrent-1; //Diminuir menos 1;
								    	  lineEmailJS = "['E-mail', new Date ("+year+","+monthEmail+",1)," +countMonth+"],";								    	
									      bwEmailJS.write(lineEmailJS);
									      bwEmailJS.newLine();	
							    		  if (yearCurrent == year)
							    		  {
//							    			  monthEmail = monthCurrent-1;
//									    	  lineEmailJS = "['E-mail', new Date ("+year+","+monthEmail+",1)," +countMonth+"],";
								    			//  ['E-mail',  new Date (1988,0,0), 1000],		
								    	      //System.out.println("lineEmailJS: " + lineEmailJS);
									    	  
							    			  monthCurrent = month;
							    			  dayCurrent = 1;
							    			  countMonth = 1;
							    		  }
							    		  else{
							    			  monthCurrent = 1;
							    			  dayCurrent   = 1;
							    			  yearCurrent = year;
							    		  }			    			  				    		  				    		  
							    	  }
							    	  countDay = 1;
							    	  //System.out.println("Dia Corrente: " + dayCurrent + " QTD: " + countDay);

							      }

					              break;
					    		}
					    	}
					      }
					      else {
						    //line = "{'name': '-----', 'message': '-----'" + ", 'value': ----- , 'lat': -----, ', number': ----- , 'date': '-----', 'time': '-----', 'lng': -----}";
						    line = "{'author': '-----', 'value': '-----', 'lat': '-----', 'lng': '-----', 'date': '-----', 'time': '-----', 'weekday': '-----' },";
						    System.out.println(line);
						    countNOT++;
					      }
				        }		
				      
					   // FIM DO COMENTÁRIO AQUI
	  					  					  
				   }			   
				      monthEmail = month-1; //Diminuir menos 1;
			    	  lineEmailJS = "['E-mail', new Date ("+year+","+monthEmail+",1)," +countMonth+"],";			    	  
				      bwEmailJS.write(lineEmailJS);
				      bwEmailJS.newLine();

				      //Order information of e-mails by date
				      Collections.sort(emailsList);
				      System.out.println("--------------------------- EmailDeveloper ---------------------------");
				      for (int s = 0; s < emailsList.size(); s++)
				      {
				    	  EmailDeveloper emailO = (EmailDeveloper) emailsList.get(s);
					      bw.write(emailO.getLine());
					      bw.newLine();	
				    	  //System.out.println("EmailDeveloper: " + emailO.getLine());
				      }
				      System.out.println("-------------------- END | EmailDeveloper | END ---------------------- ");

				      
				bw.flush();  
				bwEmailJS.flush();
				bw.close();
				//-----------
				bw08.flush();  
				bw08.close();
				//-----------
				bw07.flush();  
				bw07.close();
								
				System.out.println("\nArquivo gravado com sucesso: " + file.getName());
				System.out.println("Arquivo gravado com sucesso: " + file08.getName());
				System.out.println("Arquivo gravado com sucesso: " + file07.getName());
				System.out.println("\nCOUNT NOT: " + countNOT);
				System.out.println("emailsFiles : " + count09);
				System.out.println("\n---------------- E-mails per Weekday ------------------");
				System.out.println("['Sun', 'email',1,0," + getCountSun() + "]");
				System.out.println("['Mon', 'email',2,0," + getCountMon() + "]");
				System.out.println("['Tue', 'email',3,0," + getCountTue() + "]");
				System.out.println("['Wen', 'email',4,0," + getCountWen() + "]");
				System.out.println("['Thu', 'email',5,0," + getCountThu() + "]");
				System.out.println("['Fri', 'email',6,0," + getCountFri() + "]");
				System.out.println("['Sat', 'email',7,0," + getCountSat() + "]");
				
				
				System.out.println("COUNT Date: " + countDate);
				System.out.println("H 00: " + getH00());
				System.out.println("H 01: " + getH01());
				System.out.println("H 02: " + getH02());
				System.out.println("H 03: " + getH03());
				System.out.println("H 04: " + getH04());
				System.out.println("H 05: " + getH05());
				System.out.println("H 06: " + getH06());
				System.out.println("H 07: " + getH07());
				System.out.println("H 08: " + getH08());
				System.out.println("H 09: " + getH09());
				System.out.println("H 10: " + getH10());
				System.out.println("H 11: " + getH11());
				System.out.println("H 12: " + getH12());
				System.out.println("H 13: " + getH13());
				System.out.println("H 14: " + getH14());
				System.out.println("H 15: " + getH15());
				System.out.println("H 16: " + getH16());
				System.out.println("H 17: " + getH17());
				System.out.println("H 18: " + getH18());
				System.out.println("H 19: " + getH19());
				System.out.println("H 20: " + getH20());
				System.out.println("H 21: " + getH21());
				System.out.println("H 22: " + getH22());
				System.out.println("H 23: " + getH23());
				int total = getH00()+getH01()+getH02()+getH03()+getH04()+getH05()+getH06()+getH07()+getH08()+getH09()+getH10()+getH11()+getH12()+getH13()+getH14()+getH15()+getH16()+getH17()+getH18()+getH19()+getH20()+getH21()+getH22()+getH23();
				System.out.println("TOTAL: " + total);		
				
				
			    }
			    } catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (java.text.ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

	   //}
	}
	
	public void countHours(String time)
	{
		int t = Integer.parseInt(time.trim().substring(0,2).trim());
		
		switch (t) {
		case 0: setH00(getH00()+1);			
			break;
		case 1: setH01(getH01()+1);			
		    break;
		case 2: setH02(getH02()+1);			
		  break;
	    case 3: setH03(getH03()+1);			
	      break;
		case 4: setH04(getH04()+1);			
		  break;
	    case 5: setH05(getH05()+1);			
	      break;
	    case 6: setH06(getH06()+1);			
	      break;
        case 7: setH07(getH07()+1);			
          break;
		case 8: setH08(getH08()+1);			
		break;
	    case 9: setH09(getH09()+1);			
	      break;
	    case 10: setH10(getH10()+1);			
	      break;
        case 11: setH11(getH11()+1);			
          break;
	    case 12: setH12(getH12()+1);			
	      break;
        case 13: setH13(getH13()+1);			
          break;
        case 14: setH14(getH14()+1);			
          break;
        case 15: setH15(getH15()+1);			
          break;
		case 16: setH16(getH16()+1);			
		  break;
	    case 17: setH17(getH17()+1);			
	      break;
	    case 18: setH18(getH18()+1);			
	      break;
        case 19: setH19(getH19()+1);			
          break;
	    case 20: setH20(getH20()+1);			
	      break;
        case 21: setH21(getH21()+1);			
          break;
        case 22: setH22(getH22()+1);			
          break;
        case 23: setH23(getH23()+1);			
          break;            
		}
	}
	
	public void resetCounts()
	{
		setCountFri(0);
		setCountMon(0);
		setCountSat(0);
		setCountSun(0);
		setCountThu(0);
		setCountTue(0);
		setCountWen(0);
	}
	
	//FALTA TRANSFORMAR AS HORAS E DATAS PARA UTC
	public void lookForCommitsPerDate()
	{


		String commitsJS = "C:/Users/mario.Think_Mario/workspace/Neurominer/sources/commitsJSTESTE.txt";
		resetCounts();
				try {	
			    //Variaveis que irao armazenar os dados do arquivo JSON 
			    String date = "";
			    int day = 0, month = 0, year = 0,  dayCurrent = 1, monthCurrent = 1, yearCurrent = 1996;			    
			    int index = 0, countDay = 0, countMonth = 0, countRegister = 0, cont = 0;
			    int monthEmail=0, weekday;
			    //{"author": "undefined", "value": 1, "lat": -1, "lng": -1, "date": "1996-01-14", "time": "18:49:52", "weekday": 6},			    
				String nameFile = "C:/Users/mario.Think_Mario/workspace/Neurominer/sources/commits.js";			
			    String linha="", lineEmailJS = "";
			    File arq = new File(nameFile);
			    GregorianCalendar dataCalendar;
			    
				  //----------- File with email register
				File fileCammitsJS = new File(commitsJS); // Writer file
				FileWriter fwCommitsJS;			

				

					fwCommitsJS = new FileWriter(fileCammitsJS);
					BufferedWriter bwCommitsJS = new BufferedWriter(fwCommitsJS);	
												    		    
			    if (arq.exists())
			    {

			      //tentando ler arquivo
			        //abrindo arquivo para leitura			    				    			   			    	

				    System.out.println("------------------------------------- COMMITS -----------------------------------\n");			    
				    FileReader reader = new FileReader(nameFile);
			        //leitor do arquivo
			        BufferedReader leitor = new BufferedReader(reader);				          
			        //Reading with Json
			       // JSONObject jsonObject;
				    while(true)
				    {				        	
				    	cont++;
				          linha=leitor.readLine();
				          if(linha==null)			          
				            break;
				          System.out.println(cont+ "=" +linha);
				          
				          index = linha.indexOf("date");
				          date = linha.substring(index+8, index+18);
				          day      = Integer.parseInt(date.substring(8, 10)); //2005-12-31				      
					      month    = Integer.parseInt(date.substring(5, 7));
					      year     = Integer.parseInt(date.substring(0, 4));
//				          bwCommitsJS.write(linha);
//				          bwCommitsJS.newLine();	
					      
					   // Retorna o dia da semana: 1=domingo, 2=segunda-feira, ..., 7=sábado
					      dataCalendar = new GregorianCalendar(year, month-1, day);
					      weekday = dataCalendar.get(Calendar.DAY_OF_WEEK);	
					      countWeekDay(weekday);

//				          //Cria o parse de tratamento 
//				          JSONParser parser = new JSONParser(); 
//				        	  //Salva no objeto JSONObject o que o parse tratou do arquivo 
//				        	  jsonObject = (JSONObject) parser.parse(new FileReader( "C:/Users/mario.Think_Mario/workspace/Neurominer/sources/commits.js")); 
//				        	  //Salva nas variaveis os dados retirados do arquivo 
//				        	  date = (String) jsonObject.get("date"); 
//				        	  System.out.printf( "Data: ", date); 
				        	  			        	  
				          
							      
				          if (dayCurrent == day)
				          {
							    	  countDay++;
							    	  countMonth = countMonth+1;
							      }
							      else
							      {		    	  
							    	  if (monthCurrent == month){
							    		  dayCurrent = day;
							    		  countMonth = countMonth+1;

							    		  //countMonth = countMonth+countDay;
							    	  }
							    	  else {	
							    		  monthEmail = monthCurrent-1; //Diminuir menos 1;
								    	  lineEmailJS = "['Commit', new Date ("+year+","+monthEmail+",1)," +countMonth+"],";								    	
//									      bwEmailJS.write(lineEmailJS);
//									      bwEmailJS.newLine();	
								          bwCommitsJS.write(lineEmailJS);
								          bwCommitsJS.newLine();
							    		  if (yearCurrent == year)
							    		  {
//							    			  monthEmail = monthCurrent-1;
//									    	  lineEmailJS = "['E-mail', new Date ("+year+","+monthEmail+",1)," +countMonth+"],";
								    			//  ['E-mail',  new Date (1988,0,0), 1000],		
								    	      //System.out.println("lineEmailJS: " + lineEmailJS);
									    	  
							    			  monthCurrent = month;
							    			  dayCurrent = 1;
							    			  countMonth = 1;
							    		  }
							    		  else{
							    			  monthCurrent = 1;
							    			  dayCurrent   = 1;
							    			  yearCurrent = year;
							    		  }			    			  				    		  				    		  
							    	  }
							    	  countDay = 1;
							    	  //System.out.println("Dia Corrente: " + dayCurrent + " QTD: " + countDay);

							      }

					    		}
				                monthEmail = month-1; //Diminuir menos 1;
			    	            lineEmailJS = "['Commit', new Date ("+year+","+monthEmail+",1)," +countMonth+"],";			    	  
			    	            bwCommitsJS.write(lineEmailJS);
			    	            bwCommitsJS.newLine();
				      
				            bwCommitsJS.flush();
				            System.out.println("\n---------------- Commits per Weekday ------------------");
							System.out.println("['Sun', 'commit',1," + getCountSun() +  ",0]");
							System.out.println("['Mon', 'commit',2," + getCountMon() + ",0]");
							System.out.println("['Tue', 'commit',3," + getCountTue() + ",0]");
							System.out.println("['Wen', 'commit',4," + getCountWen() +  ",0]");
							System.out.println("['Thu', 'commit',5," + getCountThu() + ",0]");
							System.out.println("['Fri', 'commit',6," + getCountFri() + ",0]");
							System.out.println("['Sat', 'commit',7," + getCountSat() + ",0]");
					    	}		   
				
		          }	
		          catch (FileNotFoundException e) {
		        	  e.printStackTrace();
	              }catch (IOException e) 
		          { 
	            	  e.printStackTrace();
	              } 



	   //}
	}
	
	public String[] splitEmail(String email)
	{
		String name = "";
		String[] tags = new String[3];
		if (email.indexOf("<") > 0)
		{		  
		  tags[0] = "1"; //Indica que o vetor possue uma coleção de campos do email válido. 0 indica o contrário
		  name = email.substring(0, email.indexOf("<")).trim(); //Name
		  tags[1] = name.replace("\"", ""); //Name
		  tags[2] = email.substring(email.indexOf("<")+1, email.length()-1).trim(); //Email
		}
		else
		{
			if (email.indexOf("(") > 0)
			{
				  tags[0] = "1"; //Indica que o vetor possue uma coleção de campos do email válido. 0 indica o contrário
				  name = email.substring(email.indexOf("(")+1, email.length()-1).trim();
				  tags[1] = name.replace("\"", ""); //Name
				  tags[2] = email.substring(0, email.indexOf("(")).trim(); //Email
			}
			else
			{
				if (email.indexOf("@") > 0){
					  tags[0] = "1"; //Indica que o vetor possue uma coleção de campos doi email válido. 0 indica o contrário
					  tags[1] = "NO NAME"; //Name
					  tags[2] = email.trim(); //Email
				} else tags[0] = "0";
			}
		}
		
		return tags;
		
	}

	public static void lookForDevLucene(String s1, String s2)
	{
    	LevensteinDistance ls = new LevensteinDistance();
        
    	float distancia = 0;
    	//String steam = "";
    	
    //	for (int i = 0; i < dic.size(); i++)
    //	{
        	distancia = ls.getDistance(s1, s2);
        	System.out.println("Distância: " + distancia);
        	if (distancia >= 0.8)// termos iguais
			{
        		System.out.println("Achou Ugrama -> S1: "+ s1 + " - S2: " + s2);
			}
    //	}


			
			
		//}
 //   }
	}

	public int getCountMon() {
		return countMon;
	}

	public void setCountMon(int countMon) {
		this.countMon = countMon;
	}

	public int getCountSun() {
		return countSun;
	}

	public void setCountSun(int countSun) {
		this.countSun = countSun;
	}

	public int getCountTue() {
		return countTue;
	}

	public void setCountTue(int countTue) {
		this.countTue = countTue;
	}

	public int getCountWen() {
		return countWen;
	}

	public void setCountWen(int countWen) {
		this.countWen = countWen;
	}

	public int getCountThu() {
		return countThu;
	}

	public void setCountThu(int countThu) {
		this.countThu = countThu;
	}

	public int getCountFri() {
		return countFri;
	}

	public void setCountFri(int countFri) {
		this.countFri = countFri;
	}

	public int getCountSat() {
		return countSat;
	}

	public void setCountSat(int countSat) {
		this.countSat = countSat;
	}

	public int getH00() {
		return h00;
	}

	public void setH00(int h00) {
		this.h00 = h00;
	}

	public int getH01() {
		return h01;
	}

	public void setH01(int h01) {
		this.h01 = h01;
	}

	public int getH02() {
		return h02;
	}

	public void setH02(int h02) {
		this.h02 = h02;
	}

	public int getH03() {
		return h03;
	}

	public void setH03(int h03) {
		this.h03 = h03;
	}

	public int getH04() {
		return h04;
	}

	public void setH04(int h04) {
		this.h04 = h04;
	}

	public int getH05() {
		return h05;
	}

	public void setH05(int h05) {
		this.h05 = h05;
	}

	public int getH06() {
		return h06;
	}

	public void setH06(int h06) {
		this.h06 = h06;
	}

	public int getH07() {
		return h07;
	}

	public void setH07(int h07) {
		this.h07 = h07;
	}

	public int getH08() {
		return h08;
	}

	public void setH08(int h08) {
		this.h08 = h08;
	}

	public int getH09() {
		return h09;
	}

	public void setH09(int h09) {
		this.h09 = h09;
	}

	public int getH10() {
		return h10;
	}

	public void setH10(int h10) {
		this.h10 = h10;
	}

	public int getH11() {
		return h11;
	}

	public void setH11(int h11) {
		this.h11 = h11;
	}

	public int getH12() {
		return h12;
	}

	public void setH12(int h12) {
		this.h12 = h12;
	}

	public int getH13() {
		return h13;
	}

	public void setH13(int h13) {
		this.h13 = h13;
	}

	public int getH14() {
		return h14;
	}

	public void setH14(int h14) {
		this.h14 = h14;
	}

	public int getH15() {
		return h15;
	}

	public void setH15(int h15) {
		this.h15 = h15;
	}

	public int getH16() {
		return h16;
	}

	public void setH16(int h16) {
		this.h16 = h16;
	}

	public int getH17() {
		return h17;
	}

	public void setH17(int h17) {
		this.h17 = h17;
	}

	public int getH18() {
		return h18;
	}

	public void setH18(int h18) {
		this.h18 = h18;
	}

	public int getH19() {
		return h19;
	}

	public void setH19(int h19) {
		this.h19 = h19;
	}

	public int getH20() {
		return h20;
	}

	public void setH20(int h20) {
		this.h20 = h20;
	}

	public int getH21() {
		return h21;
	}

	public void setH21(int h21) {
		this.h21 = h21;
	}

	public int getH22() {
		return h22;
	}

	public void setH22(int h22) {
		this.h22 = h22;
	}

	public int getH23() {
		return h23;
	}

	public void setH23(int h23) {
		this.h23 = h23;
	}

	public int getH24() {
		return h24;
	}

	public void setH24(int h24) {
		this.h24 = h24;
	}
	

	
}