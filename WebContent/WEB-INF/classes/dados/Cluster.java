package dados;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Vector;


public class Cluster {

	private Vector<Integer> documentos;
	private Vector<String> termos;
	private Vector<Double> vetorCluster;
	private int codcluster;
	private int codSubcluster;
	public boolean atualizado;
	private Connection conn;
	private Vector<Double> vetorDoc;

	public Cluster(Connection conn, int cluster, int codigo, int modo)
	{

//		Modo
//		1- Carrega cluster pelo banco de dados
//		2- Criando clusters
//		3- Carrega subclusters pelo banco de dados 

		this.conn = conn;
		this.documentos = new Vector<Integer>();
		this.vetorCluster = new Vector<Double>();
		this.termos = new Vector<String>();
		this.vetorDoc = new Vector<Double>();		
		
		if(modo == 1)
		{
			this.codcluster = cluster;
			try{
				this.carregaCluster();
	//			System.out.println("Cluster "+ cluster +" carregado");

			}catch (SQLException e) {
				e.printStackTrace();
			}	

		}else
		{
			if (modo == 2){
				this.codcluster = cluster;			
				try {
					this.documentos.add(codigo);
					atualizarCentro();
//					System.out.println("Cluster " + cluster + " criado com arquivo de codigo: "+ codigo + "\n");

				} catch (Exception e) {
					e.printStackTrace();
				}			
			}
			else{
				if (modo == 3){
					this.codcluster = codigo;
					this.codSubcluster = cluster;
					try {
						this.carregaSubCluster();
	//					System.out.println("Subcluster "+ codigo +" do cluster " + cluster + " carregado");

					} catch (Exception e) {
						e.printStackTrace();
					}			

				}
			}	
		}	
		this.atualizado = true;

	}// fim do construtor da classe cluster

	public int getCluster ()
	{
		return (this.codcluster);
	}
	
	public void atualizarCentro() throws SQLException{

		this.termos.clear();
		this.vetorCluster.clear();
		this.vetorDoc.clear();

		if(this.documentos.size() > 0){
			Iterator<Integer> it = this.documentos.iterator();

			String sentenca;
			sentenca = "SELECT NOMTERMO, (SUM(FRQRELATIVATERMO) / " + this.documentos.size() + ") AS FREQ " +
			"FROM BAGOFWORDS WHERE CODARQUIVO IN ( " + (Integer)it.next();

			while (it.hasNext()){
				sentenca += ", ";
				sentenca += + (Integer)it.next();
			}

			sentenca += ") GROUP BY NOMTERMO";

			PreparedStatement stmt = this.conn.prepareStatement(sentenca);
			ResultSet rs = stmt.executeQuery();
		
			while(rs.next()){
				this.termos.add(rs.getString("nomTermo"));
				this.vetorCluster.add(rs.getDouble("FREQ"));
				this.vetorDoc.add(0.0);
			}
			//this.atualizado = true;
			rs.close ();
			stmt.close();
		}
		

	}// fim do método atualizarCentro()

	public Double getDistancia(int codigoDoc){

		try{
			if (this.buscaDoc(codigoDoc) != -1) 
				return this.distanciaDocInterno(codigoDoc);
			else
				return this.distanciaDocInterno(codigoDoc);

		}catch(SQLException e){
			e.printStackTrace();
		}

		return 0.0;		

	}// fim do método getDistancia(int)

	public Double distanciaDocInterno(int codigoDoc) throws SQLException{

		int resultComp;
		String nomTermo;
		Double freqTermo;
		int posicao;
		Iterator<Integer> it = this.documentos.iterator();
		
		
		String sentenca1 = "SELECT BAGOFWORDS.NOMTERMO, BAGOFWORDS.FRQRELATIVATERMO FROM "; 
        sentenca1 += " BAGOFWORDS WHERE "; 
        sentenca1 += " BAGOFWORDS.CODARQUIVO = " + codigoDoc + " ";
        
        PreparedStatement stmt = this.conn.prepareStatement (sentenca1);
        ResultSet rs = stmt.executeQuery();
        
        
        
		while (rs.next())
		{	
			nomTermo = rs.getString("nomTermo");
			freqTermo = rs.getDouble("frqRelativaTermo");
			posicao = this.termos.indexOf(nomTermo);
			
			if (posicao != -1)
			{	
				this.vetorDoc.set(posicao, freqTermo);
			}	
		}
		rs.close ();
		stmt.close();
        
		double dw= 0.0, d2= 0.0, w2= 0.0, d= 0.0, w = 0.0;
//		if (vetorCluster.size() != vetorDoc.size()){
//		System.out.println("Tamanho do vetor: " + vetorCluster.size());
//		System.out.println("Tamanho do documento: " + vetorDoc.size());
//		System.out.println("Sentença: " + sentenca);
///		}
		
		for (int i=0; i < this.vetorCluster.size(); i++) 
		{
			d=(Double)this.vetorCluster.elementAt(i);
			w=(Double)vetorDoc.elementAt(i);
			dw+= d*w;
			d2+=d*d;
			w2+=w*w;
		}
		Double um = 1.00;		

		return (um-(Double)(dw/Math.sqrt(d2*w2)));

	}// fim do método distanciaDocInterno(int)


	public void addDoc(int codigoDoc){		
		if(this.buscaDoc(codigoDoc) == -1){ 
			this.documentos.add(codigoDoc);
			this.atualizado = false;
			try {
			this.atualizarCentro();
			}
			catch (SQLException e){}
		}		
	}// fim do método addDoc(int)

	public void removeDoc(int codigoDoc){		
		int indice = this.buscaDoc(codigoDoc);
		if(indice != -1){
			this.documentos.removeElementAt(indice);
			this.atualizado = false;
			try {
				this.atualizarCentro();
				}
				catch (SQLException e){}			
		}

	}// fim do método removeDoc(int)

	public int buscaDoc(int codigoDoc){		
		Iterator<Integer> it = this.documentos.iterator();
		Integer cod;

		while(it.hasNext()){			
			cod = (Integer) it.next();			
			if( cod == codigoDoc) return this.documentos.indexOf(cod);			
		}		
		return -1;

	}// fim do método buscaDoc(int)

	public void carregaCluster() throws SQLException{

		PreparedStatement stmt = this.conn.prepareStatement("SELECT CODARQUIVO FROM CLUSTER WHERE CODCLUSTER = " + this.codcluster);
		ResultSet rs = stmt.executeQuery();

		while(rs.next()) this.documentos.add(rs.getInt("CODARQUIVO"));
		this.atualizarCentro();
		rs.close ();
		stmt.close();

	}// fim do método carregarCluster()

	public void carregaSubCluster() throws SQLException{

		
//		PreparedStatement stmt = this.conn.prepareStatement("SELECT CODARQUIVO FROM CLUSTER WHERE CODCLUSTER = " + this.codcluster +
//		" AND CODSUBCLUSTER = " + this.codigo);
		PreparedStatement stmt = this.conn.prepareStatement("SELECT CODARQUIVO FROM CLUSTER WHERE CODCLUSTER = " + this.codSubcluster +
				" AND CODSUBCLUSTER = " + this.codcluster);

		ResultSet rs = stmt.executeQuery();
		
		while(rs.next()) this.documentos.add(rs.getInt("CODARQUIVO"));
		this.atualizarCentro();
		rs.close ();
		stmt.close();

	}// fim do método carregarSubCluster()

	public void imprimirDocs(){		

		Iterator<Integer> it = this.documentos.iterator();		
		while(it.hasNext()) System.out.println((Integer) it.next());

	}// fim do método iprimitdocs()

	public void apagarDocumentos(){
		this.documentos.clear();
	}
}

