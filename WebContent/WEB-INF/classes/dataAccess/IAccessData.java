/*
 * Título:       IAccessData.java
 * Descrição:    Interface de Acesso aos Dados
 * Assunto:      Dissertacao do Mestrado em Sistemas e Computação
 * Orientador:   Manoel Mendonça
 * @author       Cristiane Costa Magalhães
 * Data       :  Jun/2007
 * @version 1.0
 */
package dataAccess;
import java.util.ArrayList;

public interface IAccessData {
  
	public ArrayList readBase ();
	public void setParameters(String nameFile);
}
