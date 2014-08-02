/* Título:      AcessoBanco.java
* Descrição:    Acesso a Banco de Dados em Access
* Assunto:      Dissertacao do Mestrado em Sistemas e Computação
* Orientador:   Manoel Mendonça
* @author       Cristiane Costa Magalhães
* Data       :  Ago/2007
* @version 1.0
*/

package bancoDados;

import java.sql.*;

public class GerenciadorAcessoBanco {

	private static Connection con = null;

	public GerenciadorAcessoBanco() {
		try {
			Class.forName ("sun.jdbc.odbc.JdbcOdbcDriver");
		} catch( ClassNotFoundException ee) {
			ee.printStackTrace();
		}
	}

	static void open() throws SQLException {
		if (con == null) {
			// ODBC data source name
			String dsn = "jdbc:odbc:bdMinerJur";
			String user = "admin";
			String password = "";

			// Connect to the database
			con = DriverManager.getConnection(dsn, user, password);

			// Shut on autocommit
			con.setAutoCommit(true);
		}
	}

	/**
	 * Metodo que fecha a conexao com o banco de dados.
	 * @throws SQLException Tratamento de Erro.
	 */
	public static void close() throws SQLException {
		if (con != null) {
			con.close();
			con = null;
		}
	}


	/**
	 * Metodo que retorna um ResultSet de um dado comando SQL.
	 * @param astrSQL String que representa o comando SQL (select).
	 * @throws SQLException Tratamento de Erro.
	 * @return um <code> ResultSet </code> de dados do comando SQL.
	 */
	public ResultSet getRS(String astrSQL) throws SQLException
	{
		Statement stmt; // SQL statement object
		ResultSet rs = null;   // SQL query results

		try {
			open();
			stmt = con.createStatement();
			rs = stmt.executeQuery(astrSQL);
		} catch (SQLException e) {
			throw new SQLException("Database error: ao executar a query:" + astrSQL + " - " + e.getMessage());
		}
		return rs;
	}

	/**
	 * Metodo que executa um comando SQL no banco de dados.
	 * @param astrSQL String que representa o comando SQL (insert/update/delete).
	 * @return um <code> Integer </code> que determina o sucesso ou nao de um executeUpdate
	 * @throws SQLException Tratamento de Erro.
	 * do statement de uma conexao com o banco de dados.
	 */
	public int exec_command(String astrSQL) throws SQLException
	{
		int resultado=0;
		try{
			open();
			Statement statement = con.createStatement();
			resultado = statement.executeUpdate(astrSQL);
			statement.close();
		} catch (SQLException e) {
			throw new SQLException("Database error: erro ao executar a query:" + astrSQL + " - " + e.getMessage());
		}
		return (resultado);
	}

}
