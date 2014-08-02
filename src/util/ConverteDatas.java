package util;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ConverteDatas
{  
    
    public static long obterMinutos(String hora1) {  
        String[] time = hora1.split(":");  
        try {  
            return Integer.parseInt(time[1]) + (Integer.parseInt(time[0])*60);  
        } catch (NumberFormatException e) {  
            return 0;  
        }  
    } 
    
    public static String obterTempo(long minutos){  
        return String.format ("%02d:%02d", (minutos / 60), (minutos % 60));  
    } 
    
    public static String changeTime(String hours1, String hours2)
    {
		String time = obterTempo(obterMinutos(hours1) + obterMinutos(hours2));
		//int timeI = Integer.parseInt(time);
		return time;
		//System.out.println(timeI);

    }
    
    public static String changeTime(String hours, String minutes, String seg, String hoursChange, String minChange)
    {

		int hoursI, minutesI, segI, hoursChangeI, minChangeI;
		hoursI    = Integer.parseInt(hours);
		minutesI  = Integer.parseInt(minutes);
		segI      = Integer.parseInt(seg);
		hoursChangeI = Integer.parseInt(hoursChange);
		minChangeI   = Integer.parseInt(minChange);
		
	    GregorianCalendar gc = new GregorianCalendar();  
	    
	    SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");  
	    Time time2 = new Time(hoursI, minutesI, segI);  
	    gc.setTimeInMillis(time2.getTime());  
	  
	    gc.add(Calendar.HOUR, hoursChangeI);  
	    gc.add(Calendar.MINUTE, minChangeI);  
	  
	    return sdf2.format(gc.getTime());
    }
    
    public static String changeTime3(int year, int month, int day, String hours, String minutes, String seg, int hoursChange, String minChange)
    {

		int hoursI, minutesI, segI, hoursChangeI, minChangeI, yearI, monthI, dayI;
		hoursI    = Integer.parseInt(hours);
		minutesI  = Integer.parseInt(minutes);
		//System.out.println("Hour: " + hours + " Min: " + minutes +" SEG: " + seg);
		segI      = Integer.parseInt(seg);

				
		//hoursChangeI = Integer.parseInt(hoursChange);
		minChangeI   = Integer.parseInt(minChange);
		
	    GregorianCalendar gc = new GregorianCalendar();  
	    
	    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd | HH:mm:ss");  
	    //Time time2 = new Time(hoursI, minutesI, segI);  
	    //gc.setTimeInMillis(time2.getTime());  
	    
	    //gc.set(year, month, date, hourOfDay, minute, second);
	    gc.set(year, month-1, day, hoursI, minutesI, segI);
	  
	    gc.add(Calendar.HOUR, hoursChange);  
	    gc.add(Calendar.MINUTE, minChangeI);  
	  
	    return sdf2.format(gc.getTime());
    }
    
    public static int checkTime(String time1, String time2)
    {   	
    	int hour1 = Integer.parseInt(time1.substring(0, 2));
    	int hour2 = Integer.parseInt(time2.substring(0, 2));
    	int status = 0;
    	if (hour1 > hour2)
    	{
    		status = 1;
    	}
    	else if (hour2 > hour1) status = 2;
    	
    	return status;
    	
    }
	
  /************************************************************************** 
   * Converte String para DATE 
   **************************************************************************/  
  public Date strToDate(String data, String formato){  
      Date resultado = null;  
      SimpleDateFormat sdf =  new SimpleDateFormat(formato);  
      try {  
          resultado = sdf.parse(data);  
      } catch (ParseException ex) {  
          ex.printStackTrace ();  
      }  
      return resultado;  
  }  
   
 public Date strToDate(String data){        
      return strToDate(data,"dd-MM-yyyy");  
  }  
    
    
  /************************************************************************** 
   * Converte DATE para String 
   **************************************************************************/  

  // Você chama este método e ele chama o debaixo passando o formato da data.  
  public static String formatDate(Date data) {  
      return formatDate(data, "dd-MM-yyyy");  
  }  

  public static String formatDate(Date data, String formato) {  
      SimpleDateFormat sdf = new SimpleDateFormat(formato);  
      return sdf.format(data);  
  }  
}
