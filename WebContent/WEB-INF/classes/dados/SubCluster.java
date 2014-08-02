package dados;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import bancoDados.AcessoBanco;



public class SubCluster {

	private Hashtable <Integer, Integer> hstSubCluster;
	private Vector<Integer> documentos;
	private Vector<Double> distancias;
	private Vector<String> termos;
	private Vector<Double> vetorFrequencia;
	
	public int codcluster;
	public boolean atualizado;

	public int codSubcluster;

	public SubCluster (int codCluster,int codSubCluster) {

		this.hstSubCluster = new Hashtable<Integer, Integer>();
		this.termos = new Vector<String>();
		this.documentos = new Vector<Integer>();
		this.vetorFrequencia = new Vector<Double>();
		
		
		this.codSubcluster = codSubCluster;
		try {
//			this.documentos.add(codigo);
//			this.distancias.add(0.0);
			//this.gravarCentro();
			System.out.println("SubCluster "+ codSubCluster + "do" + codCluster + "criado");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public SubCluster () {
		this.hstSubCluster = new Hashtable<Integer, Integer>();
		this.termos = new Vector<String>();
		this.documentos = new Vector<Integer>();
		this.vetorFrequencia = new Vector<Double>();
	}

	public void carregaDocumentos() throws SQLException 
	{
		AcessoBanco ab = new AcessoBanco();

		String lsStr = "SELECT CODARQUIVO FROM CLUSTER ";
		lsStr =	lsStr + " WHERE CODCLUSTER = " + this.codcluster;
		ResultSet rs = ab.getRS(lsStr);

		while (rs.next()) {
			this.documentos.add(rs.getInt("CODARQUIVO"));
			//this.distancias.add(rs.getDouble("DISTCENTRO"));
		}
		rs.close();
		//this.carregarCentro();

	}
	
	public void atualizarCentroSubCluster(int codCluster) throws SQLException 
	{
		AcessoBanco ab = new AcessoBanco();
		
		this.termos.clear();
		this.hstSubCluster.clear();
		//Acrescentei esse código para carregar o vetor de documentos
		this.carregaDocumentos();

		if (this.documentos.size() > 0) {
			Iterator<Integer> it = this.documentos.iterator();

			String sentenca;
			sentenca = "SELECT NOMTERMO, (SUM(FRQRELATIVATERMO) / "
					+ this.documentos.size() + ") AS FREQ "
					+ "FROM BAGOFWORDS, CLUSTER WHERE CLUSTER.CODARQUIVO = BAGOFWORDS.CODARQUIVO AND " 
					+ "CLUSTER.CODCLUSTER = "+ codCluster + " AND "
					+ "BAGOFWORDS.CODARQUIVO = "+ (Integer) it.next();

			while (it.hasNext()) {
				sentenca += " OR ";
				sentenca += "BAGOFWORDS.CODARQUIVO = " + (Integer) it.next();
			}

			sentenca += " GROUP BY NOMTERMO";

			ResultSet rs = ab.getRS(sentenca);
			
			while (rs.next()) {
				this.termos.add(rs.getString("nomTermo"));
				this.vetorFrequencia.add(rs.getDouble("FREQ"));
			}
			this.atualizado = true;
			rs.close();
//			this.gravarAssociacoes();
		}

	}
}

