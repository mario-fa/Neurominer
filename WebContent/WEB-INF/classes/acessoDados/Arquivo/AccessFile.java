/*
 * Título:       AcessBD.java
 * Descrição:    Classe única (Singleton) de acesso a arquivo
 * Assunto:      Dissertacao do Mestrado em Sistemas e Computação
 * Orientador:   Manoel Mendonça
 * @author       Cristiane Costa Magalhães
 * Data       :  Jun/2007
 * @version 1.0
 */

package acessoDados.Arquivo;

//import dataAccess.File.AccessFile;
//import dataAccess.File.FileHandler;

  // Classe Utilitária para tratamento de Banco de Dados em Arquivo
  public class AccessFile extends FileHandler
  {

  // Objeto de classe do tipo AcessFile (padrão Singleton)
  private static AccessFile singleInstance = null;

  //Metodo para recuperar a unica instancia (singleton)
  public static AccessFile getInstancia()
  {
    if (singleInstance == null)
        singleInstance = new AccessFile();
    return singleInstance;
  }
}
