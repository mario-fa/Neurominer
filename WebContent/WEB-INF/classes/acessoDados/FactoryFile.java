/*
 * T�tulo:       FactoryFile.java
 * Descri��o:    Factory para acesso a dados em arquivo
 * Assunto:      Dissertacao do Mestrado em Sistemas e Computa��o
 * Orientador:   Manoel Mendon�a
 * @author       Cristiane Costa Magalh�es
 * Data       :  Jun/2007
 * @version 1.0
 */

package acessoDados;

import acessoDados.Arquivo.*;
//import acessoDados.AccessBDBaseFile;
//import acessoDados.IAccessData;
//import acessoDados.IFactoryDataAccess;

public class FactoryFile implements IFactoryDataAccess
{
   public IAccessData createAccessFile()
   {
     return new AccessBDBaseFile();
   }

}
