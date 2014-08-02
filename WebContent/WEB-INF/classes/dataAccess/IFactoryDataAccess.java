/*
 * Título:       IFactoryDataAccess.java
 * Descrição:    Interface Factory para criação de objetos de acesso aos dados
 * Assunto:      Dissertacao do Mestrado em Sistemas e Computação
 * Orientador:   Manoel Mendonça
 * @author       Cristiane Costa Magalhães
 * Data       :  Jun/2007
 * @version 1.0
 */
package dataAccess;

public interface IFactoryDataAccess {
	
	   public IAccessData createAccessFile();

}