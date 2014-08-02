/*
 * Título:       Parametro.java
 * Descrição:    Parametros da Ferramenta
 * Assunto:      Dissertacao do Mestrado em Sistemas e Computação
 * Orientador:   Manoel Mendonça
 * @author       Cristiane Costa Magalhães
 * Data       :  Ago/2007
 * @version 1.0
 */

package dados;

import java.sql.*;
import bancoDados.AcessoBanco;


public class Parametro{

	AcessoBanco acessoBanco = new AcessoBanco();
	int codigoAlgoritmoRadicalizacao;
	int codigoAlgoritmoCompString;
	int valPercentualCompString;

	public Parametro() {
	}

	public int getCodigoAlgoritmoRadicalizacao() throws SQLException{
		AcessoBanco ab = new AcessoBanco();
		try{
			String lstrSQL;
			lstrSQL = "select codAlgoritmoRadicalizacao from parametro";
			ResultSet lrsRS = ab.getRS(lstrSQL);
			while (lrsRS.next()){
				codigoAlgoritmoRadicalizacao = Integer.parseInt(lrsRS.getString("codAlgoritmoRadicalizacao"));
			}
		}
		catch(SQLException ex){
			throw new SQLException ("Erro em Parametro: " + ex.getMessage());
		}
		finally
		{
			AcessoBanco.close();
		}
		return codigoAlgoritmoRadicalizacao;
	}

	public void setCodigoAlgoritmoRadicalizacao(int aintCodigoAlgoritmoRadicalizacao){
		codigoAlgoritmoRadicalizacao = aintCodigoAlgoritmoRadicalizacao;
	}

	public int getCodigoAlgoritmoCompString() throws SQLException{
		AcessoBanco ab = new AcessoBanco();
		try{
			String lstrSQL;
			lstrSQL = "select codAlgoritmoCompString from parametro";
			ResultSet lrsRS = ab.getRS(lstrSQL);
			while (lrsRS.next()){
				codigoAlgoritmoCompString = Integer.parseInt(lrsRS.getString("codAlgoritmoCompString"));
			}
		}
		catch(SQLException ex){
			throw new SQLException ("Erro em Parametro: " + ex.getMessage());
		}
		finally
		{
			AcessoBanco.close();
		}		
		return codigoAlgoritmoCompString;
	}

	public void setCodigoAlgoritmoCompString(int aintCodigoAlgoritmoCompString){
		codigoAlgoritmoCompString = aintCodigoAlgoritmoCompString;
	}


	public int getValPercentualCompString() throws SQLException{
		AcessoBanco ab = new AcessoBanco();
		try{
			String lstrSQL;
			lstrSQL = "select valPercCompString from parametro";
			ResultSet lrsRS = ab.getRS(lstrSQL);
			while (lrsRS.next()){
				valPercentualCompString = Integer.parseInt(lrsRS.getString("valPercCompString"));
			}
		}
		catch(SQLException ex){
			throw new SQLException ("Erro em Parametro: " + ex.getMessage());
		}
		finally
		{
			AcessoBanco.close();
		}		
		return valPercentualCompString;
	}

	public void setValPercentualCompString(int aintValPercCompString){
		valPercentualCompString = aintValPercCompString;
	}

	public void salvar(int aintcodAlgoritmoRadicalizacao, int aintcodAlgoritmoCompString, int aintValPercCompString) throws SQLException
	{
		try{
			String lstrSQL;
			lstrSQL = "update parametro set codAlgoritmoRadicalizacao = " + aintcodAlgoritmoRadicalizacao + "";
			lstrSQL = lstrSQL + " , codAlgoritmoCompString = " + aintcodAlgoritmoCompString + "";
			lstrSQL = lstrSQL + " , valPercCompString = " + aintValPercCompString + "";

			acessoBanco.exec_command(lstrSQL);
			setCodigoAlgoritmoRadicalizacao(aintcodAlgoritmoRadicalizacao);
			setCodigoAlgoritmoCompString(aintcodAlgoritmoCompString);
			setValPercentualCompString(aintValPercCompString);
		}
		catch(SQLException ex){
			throw new SQLException ("Erro em Parametro: " + ex.getMessage());
		}
	}
	
	public void excluir(int aintCodigo) throws SQLException
	{
		String lstrSQL = "delete * from parametro";
		acessoBanco.exec_command(lstrSQL);
	}
}
