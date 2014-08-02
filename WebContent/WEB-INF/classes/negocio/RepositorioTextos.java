package negocio;

import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.ArrayList;

import bancoDados.AcessoBanco;
import dados.*;

public class RepositorioTextos {
	
	BaseTextos baseTextos = new BaseTextos(); 
	
	public  RepositorioTextos() {
		 
	}

	public void salvarBagOfWords(String [][] bagOfWords) throws SQLException
	{
		baseTextos.salvarBagOfWords(bagOfWords);
	}
	
	public void inserirCluster(String [][] bagOfWords) throws SQLException
	{
		baseTextos.insereCluster(bagOfWords);
	}
	
	public Hashtable carregarSinominos() throws SQLException
	{
		Hashtable sinonimo =  new Hashtable <String, String>();
		sinonimo = baseTextos.carregarSinonimos(); 
		return (sinonimo );
	}
	public int getCodigoArquivo () throws SQLException
	{
		return (baseTextos.getCodigoArquivo());
	}

	public void atualizaClusters (int qtd_cluster, int num_cluster)
	{
		try {
			KMeans km = new KMeans();
			km.gerarClusters(qtd_cluster,num_cluster);
			km.kmean(qtd_cluster);
			
		} catch (SQLException e) {
			System.out.println (e.getMessage());
			e.printStackTrace();
			
		} catch (ClassNotFoundException e) {
			System.out.println (e.getMessage());			
			e.printStackTrace();

		}
		catch (Exception e)
		{
			System.out.println (e.getMessage());
		}
	}

	public void consultaDocsSimilares(int codArquivoOrigem, ArrayList arlClusters, int valPercentualSimilaridade) throws SQLException
	{
		Hashtable <Integer, Double> hstClusters = new Hashtable<Integer, Double>();		
		Hashtable <Integer, Double> hstSubClusters = new Hashtable<Integer, Double>();		
		Hashtable <Integer, Double> hstArquivos = new Hashtable<Integer, Double>();
		
		try {
		
			AcessoBanco acessoBanco = new AcessoBanco();	
	        String lstrSQL = "delete from documentosSimilares";
	        acessoBanco.exec_command(lstrSQL);
	       			
			for (int i = 0; i < arlClusters.size(); i++)
			{
				KMeans km = new KMeans();

				//Carrega os clusters informados pelo usuário
				km.carregarClustersEspecificos(Integer.parseInt(arlClusters.get(i).toString()));
				
				//Carrega as distâncias do arquivo até os clusters
				hstClusters = km.getDistancia(codArquivoOrigem);
				
				for (Enumeration e = hstClusters.keys(); e.hasMoreElements();) 
				{	
					int codCluster =  (Integer)e.nextElement();
					double valDistCluster =  (double)hstClusters.get(codCluster);
					
						KMeans kmsub = new KMeans();
					
						//Carrega os subclusters de cada cluster 
						kmsub.carregarSubClusters(codCluster);
					
						//Carrega as distâncias do arquivo até os subclusters
						hstSubClusters = kmsub.getDistancia(codArquivoOrigem);

						for (Enumeration e2 = hstSubClusters.keys(); e2.hasMoreElements();) 
						{
							int codSubCluster =  (Integer)e2.nextElement();
							double valDistSubCluster =  (double)hstSubClusters.get(codSubCluster);
							double perDistSubCluster = 100.0-(valDistSubCluster*100);

							//Testa se o percentual do cluster é igual ou maior do que o informado pelo usuário
							if (perDistSubCluster >= valPercentualSimilaridade)
							{	

								KMeans kmarq = new KMeans();

								//Carrega os arquivos de cada subcluster  
								kmarq.carregarArquivoSubClusters(codCluster, codSubCluster);
	
								//Carrega as distâncias do arquivo fonte até os arquivos de cada subcluster
								hstArquivos = kmarq.getDistancia(codArquivoOrigem);
	
								for (Enumeration e3 = hstArquivos.keys(); e3.hasMoreElements();) 
								{
									int codArquivoBase =  (Integer)e3.nextElement();
									double valDistArquivo=  (double)hstArquivos.get(codArquivoBase);
									double perDistArquivo = 100.0-(valDistArquivo*100);
									if (perDistArquivo >= valPercentualSimilaridade)
										km.gravaDistancias(codArquivoOrigem, codCluster, codSubCluster, codArquivoBase, valDistCluster, valDistSubCluster, valDistArquivo);
								}	
							}//fim do for de arquivo
						} //fim do if do subcluster
					}	//fim do for de subcluster
				
			}//fim do for de cluster
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
