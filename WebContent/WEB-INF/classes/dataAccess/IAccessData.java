/*
 * T�tulo:       IAccessData.java
 * Descri��o:    Interface de Acesso aos Dados
 * Assunto:      Dissertacao do Mestrado em Sistemas e Computa��o
 * Orientador:   Manoel Mendon�a
 * @author       Cristiane Costa Magalh�es
 * Data       :  Jun/2007
 * @version 1.0
 */
package dataAccess;
import java.util.ArrayList;

public interface IAccessData {
  
	public ArrayList readBase ();
	public void setParameters(String nameFile);
}
