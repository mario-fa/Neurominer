/*
 * Título:       AcessBDBaseFile.java
 * Descrição:    Acesso a base de dados em arquivo
 * Assunto:      Dissertacao do Mestrado em Sistemas e Computação
 * Orientador:   Manoel Mendonça
 * @author       Cristiane Costa Magalhães
 * Data       :  Jun/2007
 * @version 1.0
 */

package dataAccess.File ;

import dataAccess.*;
import java.util.ArrayList;


public class AccessBaseFile implements IAccessData
{
	private String dataFile;	
	private ArrayList <String> list = null;


	// Retorna base de palavras do arquivo
	public ArrayList readBase()
	{
		// Define a localização e nome do arquivo 
		String strFile = this.dataFile;

		//Declare arquivo
		AccessFile inputFile = new AccessFile();
		try
		{
			list = new ArrayList<String>();
			inputFile.OpenFile(strFile);
			String strValue = inputFile.read();
			while (strValue != null)
			{
				list.add(strValue);
				strValue = inputFile.read();
			}      
		}
		catch (Exception exception)
		{
			System.out.println("Erro de Arquivo") ;

		}
		return (list);
	}

	public void setParameters(String nameFile)
	{
		this.dataFile = nameFile;
	}


}
