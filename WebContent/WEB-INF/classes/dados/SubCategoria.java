/*
 * Título:       Categoria.java
 * Descrição:    Cadastro de Sub Categorias
 * Assunto:      Dissertacao do Mestrado em Sistemas e Computação
 * Orientador:   Manoel Mendonça
 * @author       Cristiane Costa Magalhães
 * Data       :  Ago/2007
 * @version 1.0
 */

package dados;
import util.*;
import java.sql.*;
import java.util.*;

import bancoDados.AcessoBanco;



public class SubCategoria{

	AcessoBanco acessoBanco = new AcessoBanco();
	int codigo;
	String descricao;
	String sigla;
	int categoria;

	public SubCategoria() {
	}

	public int getCodigo(){
		return codigo;
	}

	public String getDescricao(){
		return descricao;
	}

	public String getSigla(){
		return sigla;
	}

	public int getCategoria(){
		return categoria;
	}

	public void setCodigo(int aintCodigo){
		codigo = aintCodigo;
	}

	public void setDescricao(String astrDescricao){
		descricao = astrDescricao;
	}

	public void setSigla(String astrSigla){
		sigla = astrSigla;
	}

	public void setCategoria(int astrCategoria){
		categoria = astrCategoria;
	}

	public SubCategoria (int aintSubCategoria) throws SQLException
	{
		definirSubCategoria (aintSubCategoria);
	}

	private void definirSubCategoria (int aintSubCategoria) throws SQLException
	{
		String lstrSQL = "Select codSubCategoria, desSubCategoria, sigSubCategoria, codCategoria ";
		lstrSQL = lstrSQL + " from subCategoria where codSubCategoria =" + aintSubCategoria;
		ResultSet rs = acessoBanco.getRS(lstrSQL);
		if (rs.next()){
			setCodigo(aintSubCategoria);
			setDescricao(rs.getString("desSubCategoria"));
			setSigla(rs.getString("sigSubCategoria"));
			setCategoria(rs.getInt("codCategoria"));
		}
		rs.close();
	}

	public ArrayList obterConjuntoSubCategoria() throws SQLException
	{
		ArrayList <Object> arlSubCategoria = new ArrayList<Object>();
		AcessoBanco ab = new AcessoBanco();

		try{
			String lstrSQL = "Select codSubCategoria from subCategoria";
			ResultSet lrsRS = ab.getRS(lstrSQL);
			while (lrsRS.next()){
				SubCategoria subCategoria = new SubCategoria(Integer.parseInt(lrsRS.getString("CodSubCategoria")));
				arlSubCategoria.add(subCategoria);
			}
		}
		catch (SQLException e) {
			throw new SQLException ("Erro em Sub Categoria: " + e.getMessage());
		}
		finally{
			AcessoBanco.close();
		}
		return arlSubCategoria;

	}


	/**
	 * Persiste o objeto SubCategoria
	 * @throws SQLException em caso de um erro na insercao.
	 */
	public void persistir(String astrNome, String astrSigla, int aintCategoria) throws SQLException
	{
		try{
			String lstrSQL;
			lstrSQL = "select max (codSubCategoria) as max from subCategoria";
			ResultSet rsTemp = acessoBanco.getRS(lstrSQL);
			rsTemp.next();
			int codNovo = rsTemp.getInt("max");
			codNovo = codNovo +1;

			lstrSQL = "insert into subCategoria (codSubCategoria, codCategoria, desSubCategoria, sigSubCategoria) values ";
			lstrSQL = lstrSQL + " (" + codNovo + "," + aintCategoria + ", '" + astrNome + "','"+ astrSigla+ "') ";
			acessoBanco.exec_command(lstrSQL);

			lstrSQL = "select codSubCategoria from subCategoria where ";
			lstrSQL = lstrSQL + " codSubCategoria = " + codNovo + " and desSubCategoria = '" + astrNome + "' and ";
			lstrSQL = lstrSQL + " codCategoria = " + aintCategoria + " and ";         
			lstrSQL = lstrSQL + " sigSubCategoria = '" + astrSigla + "'";
			ResultSet rs = acessoBanco.getRS(lstrSQL);
			rs.next();
			setCodigo(rs.getInt("codSubCategoria"));
			rs.close();
			rsTemp.close();

		}catch(SQLException ex){
			throw new SQLException ("Erro em Sub Categoria: " + ex.getMessage());
		}
	}

	public void alterar(int aintCodigo, String astrNome, String astrSigla, int aintCategoria) throws SQLException
	{
		try{
			String lstrSQL;
			lstrSQL = "update subCategoria set desSubCategoria = '" + astrNome + "'";
			lstrSQL = lstrSQL + ",sigSubCategoria = '" + astrSigla + "'";
			lstrSQL = lstrSQL + ",codCategoria = " + aintCategoria + "";
			lstrSQL = lstrSQL + " where codSubCategoria = " + aintCodigo;
			acessoBanco.exec_command(lstrSQL);
		}
		catch(SQLException ex){
			throw new SQLException ("Erro em Sub Categoria: " + ex.getMessage());
		}
	}

	public void excluir(int aintCodigo) throws SQLException
	{
		String lstrSQL = "delete from subCategoria where codSubCategoria = " + aintCodigo;
		acessoBanco.exec_command(lstrSQL);
	}


	public static ArrayList obterCategoria() throws SQLException {

		ArrayList<Object> ar = new ArrayList<Object>();
		String lstrSQL;
		ResultSet lrsRS;
		AcessoBanco db2 = new AcessoBanco();
		try
		{
			lstrSQL = "select codCategoria as id, desCategoria as descricao from categoria order by codCategoria";
			lrsRS = db2.getRS(lstrSQL);
			DadosCombo col;
			while (lrsRS.next()){
				col = new DadosCombo(lrsRS.getInt("id"), lrsRS.getString("descricao"));
				ar.add(col);
			}
		}
		catch (SQLException e) {
			throw new SQLException ("Erro em Sub Categoria: " + e.getMessage());
		}
		finally{
			AcessoBanco.close();
		}

		return ar;
	}
}
