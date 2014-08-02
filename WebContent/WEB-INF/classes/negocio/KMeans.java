package negocio;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import dados.BaseTextos;
import dados.Cluster;

public class KMeans {

	private Connection conn;
	private Vector<Cluster> clusters;
	private int cod_cluster;
	BaseTextos baseTextos = new BaseTextos(); 

	
	public KMeans() throws ClassNotFoundException, SQLException{
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		this.conn = DriverManager.getConnection("jdbc:odbc:bdMinerJur", "", "");
		this.clusters = new Vector<Cluster>();
		
	}

	public void carregarClusters() throws SQLException {
		
		PreparedStatement stmt = this.conn.prepareStatement("SELECT DISTINCT CODCLUSTER FROM CLUSTER");
		ResultSet rs = stmt.executeQuery();
		
		Cluster cls;
		while(rs.next()){
			cls = new Cluster(this.conn, rs.getInt("codCluster"), rs.getInt("codCluster"), 1);
			this.clusters.add(cls);
		}		
		rs.close();
		stmt.close();
	}// fim do método carregarClusters()

	public void carregarSubClusters(int codCluster) throws SQLException {
		
		PreparedStatement stmt = this.conn.prepareStatement("SELECT DISTINCT CODSUBCLUSTER FROM CLUSTER WHERE CODCLUSTER = " + codCluster);
		ResultSet rs = stmt.executeQuery();
		
		Cluster cls;
		while(rs.next()){
			cls = new Cluster(this.conn, codCluster, rs.getInt("codSubCluster"), 3);
			this.clusters.add(cls);
		//	this.subClusters.add(cls);

		}	
		rs.close();
		stmt.close();
	}// fim do método carregarSubClusters()

	public void carregarArquivoSubClusters(int codCluster, int codSubCluster) throws SQLException {
		
		PreparedStatement stmt = this.conn.prepareStatement("SELECT DISTINCT CODARQUIVO FROM CLUSTER WHERE CODCLUSTER = " + codCluster +
		" AND CODSUBCLUSTER = " + codSubCluster);
		ResultSet rs = stmt.executeQuery();
		
		Cluster cls;
		while(rs.next()){
			int codTemp = rs.getInt("codArquivo");
			cls = new Cluster(this.conn, codTemp, codTemp, 2);

			this.clusters.add(cls);
		}	
		rs.close();
		stmt.close();
	}// fim do método carregarSArquivoSubClusters()

	public void carregarClustersEspecificos(int codCluster) throws SQLException {
				
		Cluster cls;
			cls = new Cluster(this.conn, codCluster, codCluster, 1);
			this.clusters.add(cls);
	}// fim do método carregarClustersEspecificos()

	public void gerarClusters(int qtd_cluster, int num_cluster) throws SQLException{
		//PreparedStatement stmt = conn.prepareStatement("SELECT DISTINCT CODCLUSTER FROM CLUSTER ");
		
	   this.cod_cluster = num_cluster;	
       PreparedStatement stmt = conn.prepareStatement("SELECT TOP " + qtd_cluster + " CODARQUIVO FROM CLUSTER " +
       " WHERE CODCLUSTER = " + num_cluster + 
       " ORDER BY RND(INT(NOW*CODARQUIVO)-NOW*CODARQUIVO)");

		ResultSet rs = stmt.executeQuery();
		
		Cluster cls;
		int i = 0;
		while(rs.next()){
			cls = new Cluster(this.conn, i,rs.getInt("CODARQUIVO"), 2);
			this.clusters.add(cls);
			++i;
		}	
		rs.close();
		stmt.close();
	}// fim do método gerarClusters()
	
	public void kmean(int qtdSubCluster) throws SQLException
	{
		int i=0;
		
		do
		{
			i++;
			this.atualizarCentros();
			this.distanciaAssociacao();
		}while(this.precisaAtualizar() && i<=qtdSubCluster);

		System.out.println("Clusters Gerados");
		
	}
	
	public void distanciaAssociacao() throws SQLException{
		
		int indiceCluster = -1;

		Double menorDistancia = Double.MAX_VALUE;
		
		PreparedStatement stmt = conn.prepareStatement("SELECT CODARQUIVO FROM CLUSTER " +
		"WHERE CODCLUSTER = " + this.cod_cluster + " ORDER BY CODARQUIVO");
		ResultSet rs = stmt.executeQuery();
		
		Iterator<Cluster> it = this.clusters.iterator();
		
		Cluster cls;
		int codArquivo, indice = 0;
		
		while(rs.next()){
			codArquivo = rs.getInt("CODARQUIVO");
		
			while(it.hasNext()){
				cls = (Cluster) it.next();
				if(cls.getDistancia(codArquivo) < menorDistancia){
					menorDistancia = cls.getDistancia(codArquivo);
					indiceCluster = indice;
				}
				indice++;
			}
			
			it = this.clusters.iterator();
			
			indice = 0;
			System.out.println("Cluster " + indiceCluster + " acomodou documento " + codArquivo);
			
			if (this.clusters.elementAt(indiceCluster).buscaDoc(codArquivo) == -1){
				this.removerDocClusters(codArquivo);
				this.clusters.elementAt(indiceCluster).addDoc(codArquivo);
			}
			
				
			
            ++indiceCluster;
			stmt = conn.prepareStatement("UPDATE CLUSTER SET CODSUBCLUSTER = " + indiceCluster +
					" WHERE CODCLUSTER = " + this.cod_cluster + " AND CODARQUIVO = " + codArquivo);
			stmt.execute(); 			
			
			
			indiceCluster = -1;

			menorDistancia = Double.MAX_VALUE;
		}
		rs.close();
		stmt.close();
		
	}
	
	public void removerDocClusters(int codigoDoc){
		Iterator<Cluster> it = this.clusters.iterator();
		while(it.hasNext()){
			((Cluster)it.next()).removeDoc(codigoDoc);			
		}		
	}
	
	public void esvaziarClusters(){
		Iterator<Cluster> it = this.clusters.iterator();
		while(it.hasNext()){
			((Cluster)it.next()).apagarDocumentos();			
		}		
	}
	
	public void atualizarCentros() throws SQLException{
		Iterator<Cluster> it = this.clusters.iterator();
		while(it.hasNext()){
			Cluster cl = (Cluster)it.next(); 
			cl.atualizarCentro();	
			cl.atualizado = true;
		}		
	}
	
	public boolean precisaAtualizar(){		
		Iterator<Cluster> it = this.clusters.iterator();
		
		while(it.hasNext())	
			if(((Cluster)it.next()).atualizado == false) return true;		
		return false;
	}
	
	public Hashtable getDistancia(int codArquivo)
	{
		double distancia=0; 
		
		Iterator<Cluster> it = this.clusters.iterator();

		Cluster cls;
		Hashtable <Integer, Double> hstDistancias = new Hashtable<Integer, Double>();		
		
		while(it.hasNext()){
				cls = (Cluster) it.next();
				distancia = cls.getDistancia(codArquivo);
				hstDistancias.put (cls.getCluster(), distancia);
		}
			
		return (hstDistancias);
	}

	public void gravaDistancias (int codArquivoOrigem, int codCluster, int codSubCluster, int codArquivoBase, double valDistCluster, double valDistSubCluster,double valDistArquivo)
	{
		try{
			baseTextos.insereDistancias(codArquivoOrigem, codCluster, codSubCluster, codArquivoBase, valDistCluster, valDistSubCluster, valDistArquivo);
		}
		catch (SQLException e) {
			e.printStackTrace();
			
		}
	}
	
}
