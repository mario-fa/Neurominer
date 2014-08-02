/*
 * T�tulo:       IFactoryDataAccess.java
 * Descri��o:    Interface Factory para cria��o de objetos de acesso aos dados
 * Assunto:      Dissertacao do Mestrado em Sistemas e Computa��o
 * Orientador:   Manoel Mendon�a
 * @author       Cristiane Costa Magalh�es
 * Data       :  Jun/2007
 * @version 1.0
 */
package acessoDados;

//import dataAccess.IAccessData;

public interface IFactoryDataAccess {
	
	   public IAccessData createAccessFile();

}