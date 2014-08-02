/*
 * Título:       Descritor.java
 * Descrição:    Cadastro de Descritores
 * Assunto:      Dissertacao do Mestrado em Sistemas e Computação
 * Orientador:   Manoel Mendonça
 * @author       Cristiane Costa Magalhães
 * Data       :  Ago/2007
 * @version 1.0
 */

package dados;

import java.sql.*;
import java.util.*;


import negocio.Configuracoes;
import bancoDados.AcessoBanco;
import textTreatment.textTreatment;

public class Descritor {

	AcessoBanco acessoBanco = new AcessoBanco();
	int codigo;
	String descricao;

	public Descritor() {
	}

	public int getCodigo(){
		return codigo;
	}

	public String getDescricao(){
		return descricao;
	}

	public void setCodigo(int aintCodigo){
		codigo = aintCodigo;
	}

	public void setDescricao(String astrDescricao){
		descricao = astrDescricao;
	}

	public Descritor (int aintDescritor) throws SQLException
	{
		definirDescritor(aintDescritor);
	}

	private void definirDescritor (int aintDescritor) throws SQLException
	{
		String lstrSQL = "Select codDescritor , desDescritor ";
		lstrSQL = lstrSQL + " from descritor where codDescritor =" + aintDescritor;
		ResultSet rs = acessoBanco.getRS(lstrSQL);
		if (rs.next()){
			setCodigo(aintDescritor);
			setDescricao(rs.getString("desDescritor"));
		}
		rs.close ();
	}

	public ArrayList obterConjuntoDescritor() throws SQLException
	{
		ArrayList<Object> arlDescritor = new ArrayList<Object>();
		AcessoBanco ab = new AcessoBanco();

		try{
			String lstrSQL = "Select codDescritor from descritor";
			ResultSet lrsRS = ab.getRS(lstrSQL);
			while (lrsRS.next()){
				Descritor descritor = new Descritor(Integer.parseInt(lrsRS.getString("codDescritor")));
				arlDescritor.add(descritor);
			}
		}
		catch (SQLException e) {
			throw new SQLException ("Erro em Descritor: " + e.getMessage());
		}
		finally{
			AcessoBanco.close();
		}
		return arlDescritor;
	}

	public Hashtable obterRelacaoDescritor() throws SQLException
	{
		Hashtable <Integer, String> relDescritor  = new Hashtable<Integer, String>();
		AcessoBanco ab = new AcessoBanco();

		try{
			String lstrSQL = "Select codDescritor, desDescritor from descritor";
			ResultSet lrsRS = ab.getRS(lstrSQL);
			while (lrsRS.next()){
				Integer codDescritor = Integer.parseInt(lrsRS.getString("codDescritor"));
				String desDescritor = lrsRS.getString("desDescritor");
				relDescritor.put(codDescritor, desDescritor);  
			}
		}
		catch (SQLException e) {
			throw new SQLException ("Erro em Descritor: " + e.getMessage());
		}
		finally{
			AcessoBanco.close();
		}
		return relDescritor;
	}

	/**
	 * Persiste o objeto descritor
	 * @throws SQLException em caso de um erro na insercao.
	 */
	public void persistir(String astrNome) throws SQLException
	{
		try{
			String lstrSQL;
			lstrSQL = "select max (codDescritor) as max from descritor";
			ResultSet rsTemp = acessoBanco.getRS(lstrSQL);
			rsTemp.next();
			int codNovo = rsTemp.getInt("max");
			codNovo = codNovo +1;

			lstrSQL = "insert into descritor (codDescritor, desDescritor) values ";
			lstrSQL = lstrSQL + " (" + codNovo + ",'" + astrNome + "') ";
			acessoBanco.exec_command(lstrSQL);

			lstrSQL = "select codDescritor from descritor where ";
			lstrSQL = lstrSQL + " codDescritor = " + codNovo + " and desDescritor = '" + astrNome + "'";
			ResultSet rs = acessoBanco.getRS(lstrSQL);
			rs.next();
			setCodigo(rs.getInt("codDescritor"));
			rs.close ();

		}catch(SQLException ex){
			throw new SQLException ("Erro em Descritor: " + ex.getMessage());
		}
	}

	public void alterar(int aintCodigo, String astrNome) throws SQLException
	{
		try{
			String lstrSQL;
			lstrSQL = "update descritor set desDescritor = '" + astrNome + "'";
			lstrSQL = lstrSQL + " where codDescritor = " + aintCodigo;
			acessoBanco.exec_command(lstrSQL);
		}
		catch(SQLException ex){
			throw new SQLException ("Erro em Descritor: " + ex.getMessage());
		}
	}

	public void excluir(int aintCodigo) throws SQLException
	{

		String lstrSQL = "delete from descritor where codDescritor = " + aintCodigo;
		acessoBanco.exec_command(lstrSQL);
	}

	public String[][] obtemListaTermosGenericosAssociados(int aintCodigo) throws SQLException{

		AcessoBanco ab = new AcessoBanco();

		String lstrSQL = "Select count(*) FROM (SELECT DISTINCT descritor.codDescritor, descritor.desDescritor";
		lstrSQL = lstrSQL + " FROM descritor LEFT JOIN descritorTermosGenericos ON  ";
		lstrSQL = lstrSQL + " descritor.codDescritor=descritorTermosGenericos.codDescritor";
		lstrSQL = lstrSQL + " WHERE ((descritor.codDescritor)  In (select ";
		lstrSQL = lstrSQL + " descritorTermosGenericos.codTermoGenerico from ";
		lstrSQL = lstrSQL + " descritorTermosGenericos, descritor ";
		lstrSQL = lstrSQL + " WHERE descritor.codDescritor = descritorTermosGenericos.codDescritor ";
		lstrSQL = lstrSQL + " and descritor.codDescritor= "+aintCodigo+"))) as max" ;


		ResultSet rstCount = ab.getRS(lstrSQL);
		rstCount.next();
		int numRows = rstCount.getInt(1);


		lstrSQL = " SELECT distinct descritor.codDescritor, descritor.desDescritor";
		lstrSQL = lstrSQL + " FROM descritor LEFT JOIN descritorTermosGenericos ON  ";
		lstrSQL = lstrSQL + " descritor.codDescritor=descritorTermosGenericos.codDescritor";
		lstrSQL = lstrSQL + " WHERE ((descritor.codDescritor)  In (select ";
		lstrSQL = lstrSQL + " descritorTermosGenericos.codTermoGenerico from ";
		lstrSQL = lstrSQL + " descritorTermosGenericos, descritor ";
		lstrSQL = lstrSQL + " WHERE descritor.codDescritor = descritorTermosGenericos.codDescritor ";
		lstrSQL = lstrSQL + " and descritor.codDescritor= "+aintCodigo+"))" ;


		String [] [] strResultado = null;
		int i=0;
		int numColumns = 2;
		strResultado = new String [numRows] [numColumns];

		try{
			ResultSet lrsRS = ab.getRS(lstrSQL);
			while (lrsRS.next()){
				for (int j=1; j < numColumns+1; j++)
				{
					strResultado[i][j-1] = lrsRS.getString(j);
				}
				++i;
			}
		}
		catch (SQLException e) {
			throw new SQLException ("Erro na Consulta de Termos Genericos associados a Descritores: " + e.getMessage());
		}
		finally{
			AcessoBanco.close();
		}
		return strResultado;
	}

	public String[][] obtemListaTermosGenericosAAssociar(int aintCodigo) throws SQLException{

		AcessoBanco ab = new AcessoBanco();
		String lstrSQL = "Select count(*) FROM (SELECT DISTINCT descritor.codDescritor, descritor.desDescritor";
		lstrSQL = lstrSQL + " FROM descritor LEFT JOIN descritorTermosGenericos ON  ";
		lstrSQL = lstrSQL + " descritor.codDescritor=descritorTermosGenericos.codDescritor";
		lstrSQL = lstrSQL + " WHERE ((descritor.codDescritor) not in (select ";
		lstrSQL = lstrSQL + " descritorTermosGenericos.codTermoGenerico from ";
		lstrSQL = lstrSQL + " descritorTermosGenericos, descritor ";
		lstrSQL = lstrSQL + " WHERE descritor.codDescritor = descritorTermosGenericos.codDescritor ";
		lstrSQL = lstrSQL + " and descritor.codDescritor= "+aintCodigo+"))) as max" ;


		ResultSet rstCount = ab.getRS(lstrSQL);
		rstCount.next();
		int numRows = rstCount.getInt(1);

		lstrSQL = " SELECT distinct descritor.codDescritor, descritor.desDescritor";
		lstrSQL = lstrSQL + " FROM descritor LEFT JOIN descritorTermosGenericos ON  ";
		lstrSQL = lstrSQL + " descritor.codDescritor=descritorTermosGenericos.codDescritor";
		lstrSQL = lstrSQL + " WHERE ((descritor.codDescritor) not in (select ";
		lstrSQL = lstrSQL + " descritorTermosGenericos.codTermoGenerico from ";
		lstrSQL = lstrSQL + " descritorTermosGenericos, descritor ";
		lstrSQL = lstrSQL + " WHERE descritor.codDescritor = descritorTermosGenericos.codDescritor ";
		lstrSQL = lstrSQL + " and descritor.codDescritor= "+aintCodigo+"))" ;


		String [] [] strResultado = null;
		int i=0;
		int numColumns = 2;
		strResultado = new String [numRows] [numColumns];

		try{
			ResultSet lrsRS = ab.getRS(lstrSQL);
			while (lrsRS.next()){
				for (int j=1; j < numColumns+1; j++)
				{
					strResultado[i][j-1] = lrsRS.getString(j);
				}
				++i;
			}
		}
		catch (SQLException e) {
			throw new SQLException ("Erro na Consulta de Termos Genericos a associar com Descritores : " + e.getMessage());
		}
		finally{
			AcessoBanco.close();
		}
		return strResultado;
	}

	public void inserirDescritorTermosGenericos(String[][] strListaAAssociar) throws SQLException
	{
		try{
			String lstrSQL;
			int i=0;

			lstrSQL = "delete from descritorTermosGenericos where codDescritor = " + this.getCodigo();
			lstrSQL = lstrSQL + " and codTermoGenerico <> 0 ";
			acessoBanco.exec_command(lstrSQL);

			while (i < strListaAAssociar.length){
				lstrSQL = "insert into descritorTermosGenericos (codDescritor, codTermoGenerico) values ";
				lstrSQL = lstrSQL + " (" + this.getCodigo() + ", " + strListaAAssociar[i][0] + ")";
				acessoBanco.exec_command(lstrSQL);
				i++;
			}
		}
		catch(SQLException ex){
			throw new SQLException ("Erro na associação de Descritores a Termos Genéricos: " + ex.getMessage());
		}
	}


	public String[][] obtemListaTermosEspecificosAssociados(int aintCodigo) throws SQLException{

		AcessoBanco ab = new AcessoBanco();

		String lstrSQL = "Select count(*) FROM (SELECT DISTINCT descritor.codDescritor, descritor.desDescritor";
		lstrSQL = lstrSQL + " FROM descritor LEFT JOIN descritorTermosEspecificos ON  ";
		lstrSQL = lstrSQL + " descritor.codDescritor=descritorTermosEspecificos.codDescritor";
		lstrSQL = lstrSQL + " WHERE ((descritor.codDescritor)  In (select ";
		lstrSQL = lstrSQL + " descritorTermosEspecificos.codTermoEspecifico from ";
		lstrSQL = lstrSQL + " descritorTermosEspecificos, descritor ";
		lstrSQL = lstrSQL + " WHERE descritor.codDescritor = descritorTermosEspecificos.codDescritor ";
		lstrSQL = lstrSQL + " and descritor.codDescritor= "+aintCodigo+"))) as max" ;


		ResultSet rstCount = ab.getRS(lstrSQL);
		rstCount.next();
		int numRows = rstCount.getInt(1);


		lstrSQL = " SELECT distinct descritor.codDescritor, descritor.desDescritor";
		lstrSQL = lstrSQL + " FROM descritor LEFT JOIN descritorTermosEspecificos ON  ";
		lstrSQL = lstrSQL + " descritor.codDescritor=descritorTermosEspecificos.codDescritor";
		lstrSQL = lstrSQL + " WHERE ((descritor.codDescritor)  In (select ";
		lstrSQL = lstrSQL + " descritorTermosEspecificos.codTermoEspecifico from ";
		lstrSQL = lstrSQL + " descritorTermosEspecificos, descritor ";
		lstrSQL = lstrSQL + " WHERE descritor.codDescritor = descritorTermosEspecificos.codDescritor ";
		lstrSQL = lstrSQL + " and descritor.codDescritor= "+aintCodigo+"))" ;


		String [] [] strResultado = null;
		int i=0;
		int numColumns = 2;
		strResultado = new String [numRows] [numColumns];

		try{
			ResultSet lrsRS = ab.getRS(lstrSQL);
			while (lrsRS.next()){
				for (int j=1; j < numColumns+1; j++)
				{
					strResultado[i][j-1] = lrsRS.getString(j);
				}
				++i;
			}
		}
		catch (SQLException e) {
			throw new SQLException ("Erro na Consulta de Termos Especificos associados a Descritores: " + e.getMessage());
		}
		finally{
			AcessoBanco.close();
		}
		return strResultado;
	}

	public String[][] obtemListaTermosEspecificosAAssociar(int aintCodigo) throws SQLException{

		AcessoBanco ab = new AcessoBanco();
		String lstrSQL = "Select count(*) FROM (SELECT DISTINCT descritor.codDescritor, descritor.desDescritor";
		lstrSQL = lstrSQL + " FROM descritor LEFT JOIN descritorTermosEspecificos ON  ";
		lstrSQL = lstrSQL + " descritor.codDescritor=descritorTermosEspecificos.codDescritor";
		lstrSQL = lstrSQL + " WHERE ((descritor.codDescritor) not in (select ";
		lstrSQL = lstrSQL + " descritorTermosEspecificos.codTermoEspecifico from ";
		lstrSQL = lstrSQL + " descritorTermosEspecificos, descritor ";
		lstrSQL = lstrSQL + " WHERE descritor.codDescritor = descritorTermosEspecificos.codDescritor ";
		lstrSQL = lstrSQL + " and descritor.codDescritor = "+aintCodigo+"))) as max" ;


		ResultSet rstCount = ab.getRS(lstrSQL);
		rstCount.next();
		int numRows = rstCount.getInt(1);

		lstrSQL = " SELECT distinct descritor.codDescritor, descritor.desDescritor";
		lstrSQL = lstrSQL + " FROM descritor LEFT JOIN descritorTermosEspecificos ON  ";
		lstrSQL = lstrSQL + " descritor.codDescritor=descritorTermosEspecificos.codDescritor";
		lstrSQL = lstrSQL + " WHERE ((descritor.codDescritor) not in (select ";
		lstrSQL = lstrSQL + " descritorTermosEspecificos.codTermoEspecifico from ";
		lstrSQL = lstrSQL + " descritorTermosEspecificos, descritor ";
		lstrSQL = lstrSQL + " WHERE descritor.codDescritor = descritorTermosEspecificos.codDescritor ";
		lstrSQL = lstrSQL + " and descritor.codDescritor= "+aintCodigo+"))" ;


		String [] [] strResultado = null;
		int i=0;
		int numColumns = 2;
		strResultado = new String [numRows] [numColumns];

		try{
			ResultSet lrsRS = ab.getRS(lstrSQL);
			while (lrsRS.next()){
				for (int j=1; j < numColumns+1; j++)
				{
					strResultado[i][j-1] = lrsRS.getString(j);
				}
				++i;
			}
		}
		catch (SQLException e) {
			throw new SQLException ("Erro na Consulta de Termos Específicos a associar com Descritores : " + e.getMessage());
		}
		finally{
			AcessoBanco.close();
		}
		return strResultado;
	}

	public void inserirDescritorTermosEspecificos(String[][] strListaAAssociar) throws SQLException
	{
		try{
			String lstrSQL;
			int i=0;

			lstrSQL = "delete from descritorTermosEspecificos where codDescritor = " + this.getCodigo();
			lstrSQL = lstrSQL + " and codTermoEspecifico <> 0 ";
			acessoBanco.exec_command(lstrSQL);

			while (i < strListaAAssociar.length){
				lstrSQL = "insert into descritorTermosEspecificos (codDescritor, codTermoEspecifico) values ";
				lstrSQL = lstrSQL + " (" + this.getCodigo() + ", " + strListaAAssociar[i][0] + ")";
				acessoBanco.exec_command(lstrSQL);
				i++;
			}
		}
		catch(SQLException ex){
			throw new SQLException ("Erro na associação de Descritores a Termos Específicos: " + ex.getMessage());
		}
	}
	public String[][] obtemListaTermosRelacionadosAssociados(int aintCodigo) throws SQLException
	{

		AcessoBanco ab = new AcessoBanco();

		String lstrSQL = "Select count(*) FROM (SELECT DISTINCT descritor.codDescritor, descritor.desDescritor";
		lstrSQL = lstrSQL + " FROM descritor LEFT JOIN descritorTermosRelacionados ON  ";
		lstrSQL = lstrSQL + " descritor.codDescritor=descritorTermosRelacionados.codDescritor";
		lstrSQL = lstrSQL + " WHERE ((descritor.codDescritor)  In (select ";
		lstrSQL = lstrSQL + " descritorTermosRelacionados.codTermoRelacionado from ";
		lstrSQL = lstrSQL + " descritorTermosRelacionados, descritor ";
		lstrSQL = lstrSQL + " WHERE descritor.codDescritor = descritorTermosRelacionados.codDescritor ";
		lstrSQL = lstrSQL + " and descritor.codDescritor= "+aintCodigo+"))) as max" ;


		ResultSet rstCount = ab.getRS(lstrSQL);
		rstCount.next();
		int numRows = rstCount.getInt(1);


		lstrSQL = " SELECT distinct descritor.codDescritor, descritor.desDescritor";
		lstrSQL = lstrSQL + " FROM descritor LEFT JOIN descritorTermosRelacionados ON  ";
		lstrSQL = lstrSQL + " descritor.codDescritor=descritorTermosRelacionados.codDescritor";
		lstrSQL = lstrSQL + " WHERE ((descritor.codDescritor)  In (select ";
		lstrSQL = lstrSQL + " descritorTermosRelacionados.codTermoRelacionado from ";
		lstrSQL = lstrSQL + " descritorTermosRelacionados, descritor ";
		lstrSQL = lstrSQL + " WHERE descritor.codDescritor = descritorTermosRelacionados.codDescritor ";
		lstrSQL = lstrSQL + " and descritor.codDescritor= "+aintCodigo+"))" ;


		String [] [] strResultado = null;
		int i=0;
		int numColumns = 2;
		strResultado = new String [numRows] [numColumns];

		try{
			ResultSet lrsRS = ab.getRS(lstrSQL);
			while (lrsRS.next()){
				for (int j=1; j < numColumns+1; j++)
				{
					strResultado[i][j-1] = lrsRS.getString(j);
				}
				++i;
			}
		}
		catch (SQLException e) {
			throw new SQLException ("Erro na Consulta de Termos Relacionados associados a Descritores: " + e.getMessage());
		}
		finally{
			AcessoBanco.close();
		}
		return strResultado;
	}

	public String[][] obtemListaTermosRelacionadosAAssociar(int aintCodigo) throws SQLException
	{

		AcessoBanco ab = new AcessoBanco();
		String lstrSQL = "Select count(*) FROM (SELECT DISTINCT descritor.codDescritor, descritor.desDescritor";
		lstrSQL = lstrSQL + " FROM descritor LEFT JOIN descritorTermosRelacionados ON  ";
		lstrSQL = lstrSQL + " descritor.codDescritor=descritorTermosRelacionados.codDescritor";
		lstrSQL = lstrSQL + " WHERE ((descritor.codDescritor) not in (select ";
		lstrSQL = lstrSQL + " descritorTermosRelacionados.codTermoRelacionado from ";
		lstrSQL = lstrSQL + " descritorTermosRelacionados, descritor ";
		lstrSQL = lstrSQL + " WHERE descritor.codDescritor = descritorTermosRelacionados.codDescritor ";
		lstrSQL = lstrSQL + " and descritor.codDescritor = "+aintCodigo+"))) as max" ;


		ResultSet rstCount = ab.getRS(lstrSQL);
		rstCount.next();
		int numRows = rstCount.getInt(1);

		lstrSQL = " SELECT distinct descritor.codDescritor, descritor.desDescritor";
		lstrSQL = lstrSQL + " FROM descritor LEFT JOIN descritorTermosRelacionados ON  ";
		lstrSQL = lstrSQL + " descritor.codDescritor=descritorTermosRelacionados.codDescritor";
		lstrSQL = lstrSQL + " WHERE ((descritor.codDescritor) not in (select ";
		lstrSQL = lstrSQL + " descritorTermosRelacionados.codTermoRelacionado from ";
		lstrSQL = lstrSQL + " descritorTermosRelacionados, descritor ";
		lstrSQL = lstrSQL + " WHERE descritor.codDescritor = descritorTermosRelacionados.codDescritor ";
		lstrSQL = lstrSQL + " and descritor.codDescritor= "+aintCodigo+"))" ;


		String [] [] strResultado = null;
		int i=0;
		int numColumns = 2;
		strResultado = new String [numRows] [numColumns];

		try{
			ResultSet lrsRS = ab.getRS(lstrSQL);
			while (lrsRS.next()){
				for (int j=1; j < numColumns+1; j++)
				{
					strResultado[i][j-1] = lrsRS.getString(j);
				}
				++i;
			}
		}
		catch (SQLException e) {
			throw new SQLException ("Erro na Consulta de Termos Relacionados a associar com Descritores : " + e.getMessage());
		}
		finally{
			AcessoBanco.close();
		}
		return strResultado;
	}
	public void inserirDescritorTermosRelacionados(String[][] strListaAAssociar) throws SQLException
	{
		try{
			String lstrSQL;
			int i=0;

			lstrSQL = "delete from descritorTermosRelacionados where codDescritor = " + this.getCodigo();
			lstrSQL = lstrSQL + " and codTermoRelacionado <> 0 ";
			acessoBanco.exec_command(lstrSQL);

			while (i < strListaAAssociar.length){
				lstrSQL = "insert into descritorTermosRelacionados(codDescritor, codTermoRelacionado) values ";
				lstrSQL = lstrSQL + " (" + this.getCodigo() + ", " + strListaAAssociar[i][0] + ")";
				acessoBanco.exec_command(lstrSQL);
				i++;
			}
		}
		catch(SQLException ex){
			throw new SQLException ("Erro na associação de Descritores a Termos Relacionados: " + ex.getMessage());
		}
	}

	public String[][] obtemListaEquivalenciasAssociadas(int aintCodigo) throws SQLException
	{
		AcessoBanco ab = new AcessoBanco();

		String lstrSQL = "Select count(*) FROM (SELECT DISTINCT equivalencia.codEquivalencia, equivalencia.desEquivalencia";
		lstrSQL = lstrSQL + " FROM equivalencia LEFT JOIN descritorEquivalencias ON  ";
		lstrSQL = lstrSQL + " equivalencia.codEquivalencia = descritorEquivalencias.codEquivalencia ";
		lstrSQL = lstrSQL + " WHERE ((equivalencia.codEquivalencia)  In (select ";
		lstrSQL = lstrSQL + " descritorEquivalencias.codEquivalencia from ";
		lstrSQL = lstrSQL + " descritorEquivalencias, equivalencia ";
		lstrSQL = lstrSQL + " WHERE equivalencia.codEquivalencia = descritorEquivalencias.codEquivalencia ";
		lstrSQL = lstrSQL + " and descritorEquivalencias.codDescritor= "+aintCodigo+"))) as max" ;

		ResultSet rstCount = ab.getRS(lstrSQL);
		rstCount.next();
		int numRows = rstCount.getInt(1);

		lstrSQL = " SELECT distinct equivalencia.codEquivalencia, equivalencia.desEquivalencia";
		lstrSQL = lstrSQL + " FROM equivalencia LEFT JOIN descritorEquivalencias ON  ";
		lstrSQL = lstrSQL + " equivalencia.codEquivalencia = descritorEquivalencias.codEquivalencia ";
		lstrSQL = lstrSQL + " WHERE ((equivalencia.codEquivalencia) In (select ";
		lstrSQL = lstrSQL + " descritorEquivalencias.codEquivalencia from ";
		lstrSQL = lstrSQL + " descritorEquivalencias, equivalencia ";
		lstrSQL = lstrSQL + " WHERE equivalencia.codEquivalencia = descritorEquivalencias.codEquivalencia";
		lstrSQL = lstrSQL + " and descritorEquivalencias.codDescritor= "+aintCodigo+"))" ;

		String [] [] strResultado = null;
		int i=0;
		int numColumns = 2;
		strResultado = new String [numRows] [numColumns];

		try{
			ResultSet lrsRS = ab.getRS(lstrSQL);
			while (lrsRS.next()){
				for (int j=1; j < numColumns+1; j++)
				{
					strResultado[i][j-1] = lrsRS.getString(j);
				}
				++i;
			}
		}
		catch (SQLException e) {
			throw new SQLException ("Erro na Consulta de Termos Relacionados associados a Equivalencias: " + e.getMessage());
		}
		finally{
			AcessoBanco.close();
		}
		return strResultado;
	}

	public String[][] obtemListaEquivalenciasAAssociar(int aintCodigo) throws SQLException{

		AcessoBanco ab = new AcessoBanco();

		String lstrSQL = "Select count(*) FROM (SELECT DISTINCT equivalencia.codEquivalencia, equivalencia.desEquivalencia";
		lstrSQL = lstrSQL + " FROM equivalencia LEFT JOIN descritorEquivalencias ON  ";
		lstrSQL = lstrSQL + " equivalencia.codEquivalencia=descritorEquivalencias.codEquivalencia";
		lstrSQL = lstrSQL + " WHERE ((equivalencia.codEquivalencia)  not In (select ";
		lstrSQL = lstrSQL + " descritorEquivalencias.codEquivalencia from ";
		lstrSQL = lstrSQL + " descritorEquivalencias, equivalencia ";
		lstrSQL = lstrSQL + " WHERE equivalencia.codEquivalencia = descritorEquivalencias.codEquivalencia ";
		lstrSQL = lstrSQL + " and descritorEquivalencias.codDescritor= "+aintCodigo+"))) as max" ;

		ResultSet rstCount = ab.getRS(lstrSQL);
		rstCount.next();
		int numRows = rstCount.getInt(1);

		lstrSQL = " SELECT distinct equivalencia.codEquivalencia, equivalencia.desEquivalencia";
		lstrSQL = lstrSQL + " FROM equivalencia LEFT JOIN descritorEquivalencias ON  ";
		lstrSQL = lstrSQL + " equivalencia.codEquivalencia = descritorEquivalencias.codEquivalencia ";
		lstrSQL = lstrSQL + " WHERE ((equivalencia.codEquivalencia) not In (select ";
		lstrSQL = lstrSQL + " descritorEquivalencias.codEquivalencia from ";
		lstrSQL = lstrSQL + " descritorEquivalencias, equivalencia ";
		lstrSQL = lstrSQL + " WHERE equivalencia.codEquivalencia = descritorEquivalencias.codEquivalencia";
		lstrSQL = lstrSQL + " and descritorEquivalencias.codDescritor= "+aintCodigo+"))" ;

		String [] [] strResultado = null;
		int i=0;
		int numColumns = 2;
		strResultado = new String [numRows] [numColumns];

		try{
			ResultSet lrsRS = ab.getRS(lstrSQL);
			while (lrsRS.next()){
				for (int j=1; j < numColumns+1; j++)
				{
					strResultado[i][j-1] = lrsRS.getString(j);
				}
				++i;
			}
		}
		catch (SQLException e) {
			throw new SQLException ("Erro na Consulta de Termos Relacionados a associar com Equivalencias: " + e.getMessage());
		}
		finally{
			AcessoBanco.close();
		}
		return strResultado;
	}

	public void inserirDescritorEquivalencias(String[][] strListaAAssociar) throws SQLException
	{
		try{
			String lstrSQL;
			int i=0;

			lstrSQL = "delete from descritorEquivalencias where codDescritor = " + this.getCodigo();

			lstrSQL = lstrSQL + " and codEquivalencia <> 0 ";
			acessoBanco.exec_command(lstrSQL);

			while (i < strListaAAssociar.length){
				lstrSQL = "insert into descritorEquivalencias (codDescritor, codEquivalencia) values ";
				lstrSQL = lstrSQL + " (" + this.getCodigo() + ", " + strListaAAssociar[i][0] + ")";
				acessoBanco.exec_command(lstrSQL);
				i++;
			}
		}
		catch(SQLException ex){
			throw new SQLException ("Erro na associação de Descritores a Equivalências: " + ex.getMessage());
		}
	}

	public String[][] obtemListaSubCategoriasAssociadas(int aintCodigo) throws SQLException
	{
		AcessoBanco ab = new AcessoBanco();
		String lstrSQL = "Select count(*) FROM (SELECT distinct subCategoria.codSubCategoria, subCategoria.desSubCategoria";
		lstrSQL = lstrSQL + " FROM subCategoria LEFT JOIN descritorSubCategorias ON  ";
		lstrSQL = lstrSQL + " subCategoria.codSubCategoria = descritorSubCategorias.codSubCategoria";
		lstrSQL = lstrSQL + " WHERE ((subCategoria.codSubCategoria) in (select ";
		lstrSQL = lstrSQL + " descritorSubCategorias.codSubCategoria from ";
		lstrSQL = lstrSQL + " descritorSubCategorias, subCategoria ";
		lstrSQL = lstrSQL + " WHERE subCategoria.codSubCategoria = descritorSubCategorias.codSubCategoria ";
		lstrSQL = lstrSQL + " and descritorSubCategorias.codDescritor= "+aintCodigo+"))) as max" ;

		ResultSet rstCount = ab.getRS(lstrSQL);
		rstCount.next();
		int numRows = rstCount.getInt(1);

		lstrSQL = " SELECT distinct subCategoria.codSubCategoria, subCategoria.desSubCategoria";
		lstrSQL = lstrSQL + " FROM subCategoria LEFT JOIN descritorSubCategorias ON  ";
		lstrSQL = lstrSQL + " subCategoria.codSubCategoria = descritorSubCategorias.codSubCategoria";
		lstrSQL = lstrSQL + " WHERE ((subCategoria.codSubCategoria) in (select ";
		lstrSQL = lstrSQL + " descritorSubCategorias.codSubCategoria from ";
		lstrSQL = lstrSQL + " descritorSubCategorias, subCategoria ";
		lstrSQL = lstrSQL + " WHERE subCategoria.codSubCategoria = descritorSubCategorias.codSubCategoria ";
		lstrSQL = lstrSQL + " and descritorSubCategorias.codDescritor= "+aintCodigo+"))" ;


		String [] [] strResultado = null;
		int i=0;
		int numColumns = 2;
		strResultado = new String [numRows] [numColumns];

		try{
			ResultSet lrsRS = ab.getRS(lstrSQL);
			while (lrsRS.next()){
				for (int j=1; j < numColumns+1; j++)
				{
					strResultado[i][j-1] = lrsRS.getString(j);
				}
				++i;
			}
		}
		catch (SQLException e) {
			throw new SQLException ("Erro na Consulta de Termos Relacionados associados a SubCategorias: " + e.getMessage());
		}
		finally{
			AcessoBanco.close();

		}
		return strResultado;
	}

	public String[][] obtemListaSubCategoriasAAssociar(int aintCodigo) throws SQLException
	{
		AcessoBanco ab = new AcessoBanco();
		String lstrSQL = "Select count(*) FROM (SELECT distinct subCategoria.codSubCategoria, subCategoria.desSubCategoria";
		lstrSQL = lstrSQL + " FROM subCategoria LEFT JOIN descritorSubCategorias ON  ";
		lstrSQL = lstrSQL + " subCategoria.codSubCategoria = descritorSubCategorias.codSubCategoria";
		lstrSQL = lstrSQL + " WHERE ((subCategoria.codSubCategoria) not in (select ";
		lstrSQL = lstrSQL + " descritorSubCategorias.codSubCategoria from ";
		lstrSQL = lstrSQL + " descritorSubCategorias, subCategoria ";
		lstrSQL = lstrSQL + " WHERE subCategoria.codSubCategoria = descritorSubCategorias.codSubCategoria ";
		lstrSQL = lstrSQL + " and descritorSubCategorias.codDescritor= "+aintCodigo+"))) as max" ;


		ResultSet rstCount = ab.getRS(lstrSQL);
		rstCount.next();
		int numRows = rstCount.getInt(1);

		lstrSQL = " SELECT distinct subCategoria.codSubCategoria, subCategoria.desSubCategoria";
		lstrSQL = lstrSQL + " FROM subCategoria LEFT JOIN descritorSubCategorias ON  ";
		lstrSQL = lstrSQL + " subCategoria.codSubCategoria = descritorSubCategorias.codSubCategoria";
		lstrSQL = lstrSQL + " WHERE ((subCategoria.codSubCategoria) not in (select ";
		lstrSQL = lstrSQL + " descritorSubCategorias.codSubCategoria from ";
		lstrSQL = lstrSQL + " descritorSubCategorias, subCategoria ";
		lstrSQL = lstrSQL + " WHERE subCategoria.codSubCategoria = descritorSubCategorias.codSubCategoria ";
		lstrSQL = lstrSQL + " and descritorSubCategorias.codDescritor= "+aintCodigo+"))" ;


		String [] [] strResultado = null;
		int i=0;
		int numColumns = 2;
		strResultado = new String [numRows] [numColumns];

		try{
			ResultSet lrsRS = ab.getRS(lstrSQL);
			while (lrsRS.next()){
				for (int j=1; j < numColumns+1; j++)
				{
					strResultado[i][j-1] = lrsRS.getString(j);
				}
				++i;
			}
		}
		catch (SQLException e) {
			throw new SQLException ("Erro na Consulta de Termos Relacionados a associar com SubCategorias: " + e.getMessage());
		}
		finally{
			AcessoBanco.close();
		}
		return strResultado;
	}

	public void inserirDescritorSubCategorias(String[][] strListaAAssociar) throws SQLException
	{
		try{
			String lstrSQL;
			int i=0;
			lstrSQL = "delete from descritorSubCategorias where codDescritor = " + this.getCodigo();
			lstrSQL = lstrSQL + " and codSubCategoria <> 0 ";
			acessoBanco.exec_command(lstrSQL);

			while (i < strListaAAssociar.length){
				lstrSQL = "insert into descritorSubCategorias (codDescritor, codSubCategoria) values ";
				lstrSQL = lstrSQL + " (" + this.getCodigo() + ", " + strListaAAssociar[i][0] + ")";
				acessoBanco.exec_command(lstrSQL);
				i++;
			}
		}
		catch(SQLException ex){
			throw new SQLException ("Erro na associação de Descritores a SubCategorias: " + ex.getMessage());
		}
	}
	
	public void aplicarRadicalizacao () throws SQLException
	{
		Hashtable <String, String> bagOfWordsDescritor  = new Hashtable<String, String>();
		Hashtable <String, String> hstTesaurus = new Hashtable <String, String> ();
		ArrayList <String> arlDescritor = new ArrayList<String>();
		int codAlgoritmoClassificacao = 0;
		
		Configuracoes conf = new Configuracoes(); 
		codAlgoritmoClassificacao = conf.consultaAlgoritmoClassificacao();
		
		textTreatment tt = new textTreatment();
		hstTesaurus = this.obterRelacaoDescritor();
		
		
		for (Enumeration e = hstTesaurus.keys(); e.hasMoreElements();)
		{
			Integer codDescritor = (Integer) e.nextElement();
			String desDescritor  = hstTesaurus.get(codDescritor).toString().toLowerCase();

			StringTokenizer tokens = new StringTokenizer (desDescritor);
			arlDescritor.clear();
			
			//Percorre os tokens
			while (tokens.hasMoreTokens())
			{
				// Captura Token
				String strToken = tokens.nextToken().toLowerCase();
				arlDescritor.add (strToken);
			}

			if (codAlgoritmoClassificacao ==1)
			{
				bagOfWordsDescritor = tt.extractBagOfWordsByRlsp(arlDescritor);
			}
			if (codAlgoritmoClassificacao ==2)
			{
				bagOfWordsDescritor = tt.extractBagOfWordsByPorter(arlDescritor);				
			}	

			String descTotal = "";
			for (Enumeration e2 = bagOfWordsDescritor.keys(); e2.hasMoreElements();) 
			{
				String key = (String) e2.nextElement().toString().toUpperCase();
				if ((bagOfWordsDescritor.size())> 1) 
				{
					descTotal = key + " " + descTotal;
				}
				else{
					descTotal = key;					
				}
				try{
					String lstrSQL="";
					if (codAlgoritmoClassificacao ==1)
					{	
						lstrSQL = "update descritor set desDescritorRadRlsp = '" + descTotal + "'";
						lstrSQL = lstrSQL + " where codDescritor = " + codDescritor;
					}
					if (codAlgoritmoClassificacao ==2)
					{	
						lstrSQL = "update descritor set desDescritorRadPorter = '" + descTotal + "'";
						lstrSQL = lstrSQL + " where codDescritor = " + codDescritor;
					}
					acessoBanco.exec_command(lstrSQL);
				}
				catch(SQLException ex){
					throw new SQLException ("Erro em Descritor: " + ex.getMessage());
				}
			}	
		}		
			
	}
	
	public Hashtable obterTermosRadicalizadosRlsp() throws SQLException
	{
		Hashtable <Integer, String> relDescritor  = new Hashtable<Integer, String>();
		AcessoBanco ab = new AcessoBanco();

		try{
			String lstrSQL = "Select codDescritor, desDescritorRadRlsp from descritor";
			ResultSet lrsRS = ab.getRS(lstrSQL);
			while (lrsRS.next()){
				Integer codDescritor = Integer.parseInt(lrsRS.getString("codDescritor"));
				String desDescritor = lrsRS.getString("desDescritorRadRlsp");
				relDescritor.put(codDescritor, desDescritor);  
			}
		}
		catch (SQLException e) {
			throw new SQLException ("Erro em Descritor: " + e.getMessage());
		}
		finally{
			AcessoBanco.close();
		}
		return relDescritor;
	}

	public Hashtable obterTermosRadicalizadosPorter() throws SQLException
	{
		Hashtable <Integer, String> relDescritor  = new Hashtable<Integer, String>();
		AcessoBanco ab = new AcessoBanco();

		try{
			String lstrSQL = "Select codDescritor, desDescritorRadPorter from descritor";
			ResultSet lrsRS = ab.getRS(lstrSQL);
			while (lrsRS.next()){
				Integer codDescritor = Integer.parseInt(lrsRS.getString("codDescritor"));
				String desDescritor = lrsRS.getString("desDescritorRadPorter");
				relDescritor.put(codDescritor, desDescritor);  
			}
		}
		catch (SQLException e) {
			throw new SQLException ("Erro em Descritor: " + e.getMessage());
		}
		finally{
			AcessoBanco.close();
		}
		return relDescritor;
	}


}
