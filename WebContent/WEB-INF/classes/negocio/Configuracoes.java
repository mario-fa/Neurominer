/*
 * Título:       Configuracoes.java
 * Descrição:    Classe de Negócio responsável pela parametrização da ferramenta 
 * Assunto:      Dissertacao do Mestrado em Sistemas e Computação
 * Orientador:   Manoel Mendonça
 * @author       Cristiane Costa Magalhães
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
