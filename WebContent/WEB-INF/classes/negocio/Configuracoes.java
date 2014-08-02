/*
 * T�tulo:       Configuracoes.java
 * Descri��o:    Classe de Neg�cio respons�vel pela parametriza��o da ferramenta 
 * Assunto:      Dissertacao do Mestrado em Sistemas e Computa��o
 * Orientador:   Manoel Mendon�a
 * @author       Cristiane Costa Magalh�es
 * Data       :  Ago/2007
 * @version 1.0
 */

package negocio;

import java.sql.SQLException;

import dados.Parametro;

public class Configuracoes {

	private Parametro parametro = new Parametro();

	public  Configuracoes() {
		 
	}
	
	public void salvarParametro(int aintCodAlgoritmoRadicalizacao, int aintCodAlgoritmoCompString, int aintValPercCompString) throws SQLException
	{
		parametro.salvar(aintCodAlgoritmoRadicalizacao, aintCodAlgoritmoCompString,aintValPercCompString);
	}

	public int consultaAlgoritmoClassificacao() throws SQLException{
		int codAlgoritmoClassificacao = 0;
		codAlgoritmoClassificacao = parametro.getCodigoAlgoritmoRadicalizacao();
		return(codAlgoritmoClassificacao);
	}
	public int consultaAlgoritmoCompString() throws SQLException{
		int codAlgoritmoCompString = 0;
		codAlgoritmoCompString = parametro.getCodigoAlgoritmoCompString();
		return(codAlgoritmoCompString);
	}

	public int consultaValorPercentualCompString() throws SQLException{
		int valPercCompString = 0;
		valPercCompString = parametro.getValPercentualCompString();
		return(valPercCompString);
	}

	}
