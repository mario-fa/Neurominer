/*
 * Título:       Categoria.java
 * Descrição:    Cadastro de Categorias
 * Assunto:      Dissertacao do Mestrado em Sistemas e Computação
 * Orientador:   Manoel Mendonça
 * @author       Cristiane Costa Magalhães
 * Data       :  Ago/2007
 * @version 1.0
 */

package dados;

import java.sql.*;
import java.util.*;
import bancoDados.AcessoBanco;


public class Categoria{

	AcessoBanco acessoBanco = new AcessoBanco();
	int codigo;
	String descricao;
	String sigla;

	public Categoria() {
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

	public void setCodigo(int aintCodigo){
		codigo = aintCodigo;
	}

	public void setDescricao(String astrDescricao){
		descricao = astrDescricao;
	}

	public void setSigla(String astrSigla){
		sigla = astrSigla;
	}

	public Categoria (int aintCategoria) throws SQLException
	{
		definirCategoria(aintCategoria);
	}

	private void definirCategoria (int aintCategoria) throws SQLException
	{
		String lstrSQL = "Select codCategoria, desCategoria, sigCategoria ";
		lstrSQL = lstrSQL + " from categoria where codCategoria =" + aintCategoria;
		ResultSet rs = acessoBanco.getRS(lstrSQL);
		if (rs.next()){
			setCodigo(aintCategoria);
			setDescricao(rs.getString("desCategoria"));
			setSigla(rs.getString("sigCategoria"));
		}
		rs.close();
	}

	public ArrayList obterConjuntoCategoria() throws SQLException
	{
		ArrayList <Object> arlCategoria = new ArrayList <Object>();
		AcessoBanco ab = new AcessoBanco();

		try{
			String lstrSQL = "Select codCategoria from categoria";
			ResultSet lrsRS = ab.getRS(lstrSQL);
			while (lrsRS.next()){
				Categoria categoria = new Categoria(Integer.parseInt(lrsRS.getString("codCategoria")));
				arlCategoria.add(categoria);
			}
		}
		catch (SQLException e) {
			throw new SQLException ("Erro em Categoria: " + e.getMessage());
		}
		finally{
			AcessoBanco.close();
		}
		return arlCategoria;
	}


	/**
	 * Persiste o objeto Categoria
	 * @throws SQLException em caso de um erro na insercao.
	 */
	public void persistir(String astrNome, String astrSigla) throws SQLException
	{
		try{
			String lstrSQL;
			lstrSQL = "select max (codCategoria) as max from categoria";
			ResultSet rsTemp = acessoBanco.getRS(lstrSQL);
			rsTemp.next();
			int codNovo = rsTemp.getInt("max");
			codNovo = codNovo +1;

			lstrSQL = "insert into categoria (codCategoria, desCategoria, sigCategoria) values ";
			lstrSQL = lstrSQL + " (" + codNovo + ",'" + astrNome + "','"+ astrSigla+ "') ";
			acessoBanco.exec_command(lstrSQL);

			lstrSQL = "select codCategoria from categoria where ";
			lstrSQL = lstrSQL + " codCategoria = " + codNovo + " and desCategoria = '" + astrNome + "' and ";
			lstrSQL = lstrSQL + " sigCategoria = '" + astrSigla + "'";
			ResultSet rs = acessoBanco.getRS(lstrSQL);
			rs.next();
			setCodigo(rs.getInt("codCategoria"));
			rs.close ();

		}catch(SQLException ex){
			throw new SQLException ("Erro em Categoria: " + ex.getMessage());
		}
	}

	public void alterar(int aintCodigo, String astrNome, String astrSigla) throws SQLException
	{
		try{
			String lstrSQL;
			lstrSQL = "update categoria set desCategoria = '" + astrNome + "'";
			lstrSQL = lstrSQL + ",sigCategoria = '" + astrSigla + "'";
			lstrSQL = lstrSQL + " where codCategoria = " + aintCodigo;
			acessoBanco.exec_command(lstrSQL);
		}
		catch(SQLException ex){
			throw new SQLException ("Erro em Categoria: " + ex.getMessage());
		}
	}

	public void excluir(int aintCodigo) throws SQLException
	{
		String lstrSQL = "delete from categoria where codCategoria = " + aintCodigo;
		acessoBanco.exec_command(lstrSQL);
	}
	
	public Hashtable obterRelacaoCategorias() throws SQLException
	{
		Hashtable <Integer, String> relCategoria  = new Hashtable<Integer, String>();
		AcessoBanco ab = new AcessoBanco();

		try{
			String lstrSQL = "Select codCategoria, desCategoria from categoria";
			ResultSet lrsRS = ab.getRS(lstrSQL);
			while (lrsRS.next()){
				Integer codCategoria = Integer.parseInt(lrsRS.getString("codCategoria"));
				String desCategoria = lrsRS.getString("desCategoria");
				relCategoria.put(codCategoria, desCategoria);  
			}
		}
		catch (SQLException e) {
			throw new SQLException ("Erro ao carregar todas as categorias: " + e.getMessage());
		}
		finally{
			AcessoBanco.close();
		}
		return relCategoria;	
	}

}
