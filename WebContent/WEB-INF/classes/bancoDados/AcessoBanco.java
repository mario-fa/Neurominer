package bancoDados;

/* T�tulo:      AcessoBanco.java
* Descri��o:    Acesso a Banco de Dados em Access
* Assunto:      Dissertacao do Mestrado em Sistemas e Computa��o
* Orientador:   Manoel Mendon�a
* @author       Cristiane Costa Magalh�es
* Data       :  Ago/2007
* @version 1.0
*/

 // Classe Utilit�ria para tratamento de Banco de Dados em MDB
  public class AcessoBanco extends GerenciadorAcessoBanco
  {

  // Objeto de classe do tipo AcessFile (padr�o Singleton)
  private static AcessoBanco singleInstance = null;

  public AcessoBanco()
  {
    super();
  }


  //Metodo para recuperar a unica instancia (singleton)
  public static AcessoBanco getInstancia()
  {
    if (singleInstance == null)
        singleInstance = new AcessoBanco();
    return singleInstance;
  }
}

