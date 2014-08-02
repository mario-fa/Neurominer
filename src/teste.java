import java.io.*;
import java.util.Arrays;

import Cargaclass.EMailFilter;


public class teste {
	
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
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
