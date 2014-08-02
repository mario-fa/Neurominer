package bancoDados;

/* Título:      AcessoBanco.java
* Descrição:    Acesso a Banco de Dados em Access
* Assunto:      Dissertacao do Mestrado em Sistemas e Computação
* Orientador:   Manoel Mendonça
* @author       Cristiane Costa Magalhães
* Data       :  Ago/2007
* @version 1.0
*/

 // Classe Utilitária para tratamento de Banco de Dados em MDB
  public class AcessoBanco extends GerenciadorAcessoBanco
  {

  // Objeto de classe do tipo AcessFile (padrão Singleton)
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

