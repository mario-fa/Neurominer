/*
 * T�tulo:       AcessBD.java
 * Descri��o:    Classe acesso a dados em arquivo
 * Assunto:      Dissertacao do Mestrado em Sistemas e Computa��o
 * Orientador:   Manoel Mendon�a
 * @author       Cristiane Costa Magalh�es
 * Data       :  Jun/2007
 * @version 1.0
 */

package dataAccess.File;

import java.io.*;

public abstract class FileHandler {

 private PrintWriter outputData;
 private BufferedReader inputData;

 // Cria arquivo de sa�da
 public void Create(String outputfile) throws Exception
 {
        try
        {
            outputData= new PrintWriter(new BufferedWriter(new FileWriter(outputfile)));
        }
        catch(Exception e)
        {
          throw new Exception();
        }

    }

   // Salva arquivo de dados
  public  void Save(Object i)
  {
        outputData.println(i);

  }

  // Fecha arquivo de dados
  public  void CloseFile() throws Exception
  {
    try
    {
      if (outputData != null)
        outputData.close();

      if (inputData != null)
        inputData.close();
    }
    catch (Exception e)
    {
      throw new Exception();
    }
  }

  // Abre arquivo para leitura
  public  void OpenFile(String filename) throws Exception
  {
    try
    {
        inputData= new BufferedReader (new FileReader (new String (filename)));
    }
    catch(Exception e)
    {
      throw new Exception();
    }
  }

  // Leitura do arquivo de dados
  public String read() throws Exception
  {
    try
    {
        return (inputData.readLine());
    }
    catch(Exception e)
    {
      throw new Exception();
    }
  }

  //Obt�m lista de arquivos do diret�rio
  public String[] getList(String nameDirectory)
  {
    File dir = new File (nameDirectory);
    return (dir.list());
  }
}





