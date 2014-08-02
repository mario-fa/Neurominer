package Carga;

import Carga.ProcessCarga;

public class Process {

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		ProcessCarga pc = new ProcessCarga();
			
		try 
		{
			pc.Process();
		} 
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

}
