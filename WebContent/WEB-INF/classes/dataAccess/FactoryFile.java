/*
 * Título:       FactoryFile.java
 * Descrição:    Factory para acesso a dados em arquivo
 * Assunto:      Dissertacao do Mestrado em Sistemas e Computação
 * Orientador:   Manoel Mendonça
 * @author       Cristiane Costa Magalhães
 * Data       :  Jun/2007
 * @version 1.0
 */

package dataAccess;
import dataAccess.File.*;

public class FactoryFile implements IFactoryDataAccess
{
   public IAccessData createAccessFile()
   {
     return new AccessBDBaseFile();
   }

}
