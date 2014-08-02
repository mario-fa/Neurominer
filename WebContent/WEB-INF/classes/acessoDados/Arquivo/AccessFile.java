/*
 * T�tulo:       AcessBD.java
 * Descri��o:    Classe �nica (Singleton) de acesso a arquivo
 * Assunto:      Dissertacao do Mestrado em Sistemas e Computa��o
 * Orientador:   Manoel Mendon�a
 * @author       Cristiane Costa Magalh�es
 * Data       :  Jun/2007
 * @version 1.0
 */

package acessoDados.Arquivo;

//import dataAccess.File.AccessFile;
//import dataAccess.File.FileHandler;

  // Classe Utilit�ria para tratamento de Banco de Dados em Arquivo
  public class AccessFile extends FileHandler
  {

  // Objeto de classe do tipo AcessFile (padr�o Singleton)
  private static AccessFile singleInstance = null;

  //Metodo para recuperar a unica instancia (singleton)
  public static AccessFile getInstancia()
  {
    if (singleInstance == null)
        singleInstance = new AccessFile();
    return singleInstance;
  }
}
