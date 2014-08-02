package dados;


import java.sql.*;
import java.util.ArrayList;
import java.util.Hashtable;


import bancoDados.AcessoBanco;



public class BaseTextos{

	AcessoBanco acessoBanco = new AcessoBanco();
	int codArquivo;
	String nomArquivo;
	String nomTermo;
	int codCluster;
	int frqTermo;
	int frqtermo;
	

	public BaseTextos() {
	}

	public int getCodigoArquivo () throws SQLException
	{
		AcessoBanco acessoBanco = new AcessoBanco();
		int codArquivo=0; 
		try{
			String lstrSQL = "select max (codArquivo) as max from bagOfWords";
			ResultSet rsTemp = acessoBanco.getRS(lstrSQL);
			rsTemp.next();
				
			codArquivo = rsTemp.getInt("max");
			
		}
		catch (SQLException e) {
	         throw new SQLException ("Erro ao buscar maior número de arquivo: " + e.getMessage());
	     }
	    finally{
	    	AcessoBanco.close();
	    }
		return (codArquivo);    
	
	}
		
	public String getNomeArquivo(){
		return nomArquivo;
	}

	public String getNomeTermo(){
		return nomTermo;
	}

	public int getFrequenciaTermo(){
		return frqtermo;
	}

	public void setCodigoArquivo(int aintCodigo){
		codArquivo = aintCodigo;
	}

	public void setNomeArquivo(String astrDescricao){
		nomArquivo = astrDescricao;
	}

	public void setNomeTermo(String astrNomeTermo){
		nomTermo = astrNomeTermo;
	}

	public void setFrequenciaTermo(int aintFreqTermo){
		frqtermo = aintFreqTermo;
	}


	/**
	 * Persiste o objeto 
	 * @throws SQLException em caso de um erro na insercao.
	 */
	public void salvarBagOfWords(String [][] bagOfWords) throws SQLException
	{
		String nomArquivo, nomTermo, nomCaminhoArquivo;
		
		try{
			if (bagOfWords.length > 0 && Integer.parseInt(bagOfWords[0][4])== 0)
    			  excluiArquivoFonte();
			
			AcessoBanco acessoBanco = new AcessoBanco();
			for (int i = 0; i < bagOfWords.length; i++) 
			{
				if ((i%1000)== 0){
					   acessoBanco.close();
					   acessoBanco = new AcessoBanco();					
				}

				nomArquivo = bagOfWords[i][0];
				nomTermo = bagOfWords[i][1];
				frqTermo = Integer.parseInt(bagOfWords[i][3]);
				codArquivo = Integer.parseInt(bagOfWords[i][4]);
				codCluster = Integer.parseInt(bagOfWords[i][5]);
				nomCaminhoArquivo = bagOfWords[i][6];
				
					
				String lstrSQL;
				lstrSQL = "select max (codBagOfWords) as max from bagOfWords";
				ResultSet rsTemp = acessoBanco.getRS(lstrSQL);
				rsTemp.next();
				
				
				int codNovo = rsTemp.getInt("max");
				codNovo = codNovo +1;
                
				lstrSQL = "insert into bagOfWords (codBagOfWords, codArquivo, nomArquivo, nomTermo, frqTermo, frqRelativaTermo,codCluster,nomCaminhoArquivo) values ";
				lstrSQL = lstrSQL + " (" + codNovo + "," + codArquivo + ",'" + nomArquivo + "','"+ nomTermo+ "', " + frqTermo + ", "+0+", " + codCluster + ", '" + nomCaminhoArquivo + "') ";
				acessoBanco.exec_command(lstrSQL);

			}
			
			//Atualiza a frequencia relativa de cada termo da BagOfWords
			this.calculaFrequenciaRelativa(codArquivo);

		}catch(SQLException ex){
			throw new SQLException ("Erro em Bag Of Words: " + ex.getMessage());
		}


	}
	
	private void calculaFrequenciaRelativa(int codArquivo) throws SQLException
	{
		try{
			String lstrSQL = "drop table TESTE2";
			acessoBanco.exec_command(lstrSQL);
		}
		catch (SQLException e){
		}
		
		String lstrSQL = "SELECT * INTO TESTE2 ";
		lstrSQL = lstrSQL + " FROM [SELECT  bagOfWords.codBagOfWords as codBagOfWords, bagOfWords.codArquivo as codarquivo, bagOfWords.nomArquivo, bagOfWords.nomTermo, bagOfWords.frqTermo, (frqTermo/MaxDefrqTermo) AS RELATIVO ";
		lstrSQL = lstrSQL + " FROM bagOfWords,(SELECT bagOfWords.codArquivo as codigoarquivo, Max(bagOfWords.frqTermo) AS MaxDefrqTermo ";
		lstrSQL = lstrSQL + " FROM bagOfWords ";
		lstrSQL = lstrSQL + " WHERE codArquivo = " + codArquivo;
		lstrSQL = lstrSQL + " GROUP BY bagOfWords.codArquivo)";
		lstrSQL = lstrSQL + "WHERE bagOfWords.codArquivo=codigoarquivo]. AS TESTE";
		acessoBanco.exec_command(lstrSQL);
		
		lstrSQL = "update bagOfWords INNER JOIN TESTE2 ";
		lstrSQL = lstrSQL + "ON bagOfWords.codBagOfWords = TESTE2.codBagOfWords ";
		lstrSQL = lstrSQL + "SET  bagOfWords.frqRelativaTermo = TESTE2.RELATIVO";
		acessoBanco.exec_command(lstrSQL);
		AcessoBanco.close();

	}

	public Hashtable carregarSinonimos () throws SQLException
	{
		Hashtable <String, String> sinonimo =  new Hashtable<String, String>();
		AcessoBanco ab = new AcessoBanco();
		 
		try{
			String lstrSQL = "SELECT descritor.desDescritor, descritor_1.desDescritor AS equivalencia ";
			lstrSQL = lstrSQL + "FROM (descritor INNER JOIN descritorEquivalencias ON ";
			lstrSQL = lstrSQL + "descritor.codDescritor = descritorEquivalencias.codDescritor) INNER JOIN descritor AS descritor_1 ";
			lstrSQL = lstrSQL + "ON descritorEquivalencias.codEquivalencia = descritor_1.codDescritor";
			ResultSet lrsRS = ab.getRS(lstrSQL);
		    while (lrsRS.next()){
		    	sinonimo.put(lrsRS.getString("equivalencia"), lrsRS.getString("desDescritor"));
		       }
		    }
		     catch (SQLException e) {
		         throw new SQLException ("Erro ao carregar sinônimos: " + e.getMessage());
		     }
		    finally{
		    	AcessoBanco.close();
		    }
		return (sinonimo);    
	
	}
	
	public void insereCluster(String [][] bagOfWords) throws SQLException
	{
		ArrayList <Object> arlCodArquivos  = new ArrayList <Object>();
		
		try{
			for (int i = 0; i < bagOfWords.length; i++) 
			{
				codArquivo = Integer.parseInt(bagOfWords[i][4]);
				codCluster = Integer.parseInt(bagOfWords[i][5]);

				if (! arlCodArquivos.contains(codArquivo))
				{
					arlCodArquivos.add(codArquivo);
					
					String lstrSQL = "insert into cluster(codArquivo, codCluster, codSubCluster) values ";
					lstrSQL = lstrSQL + " (" + codArquivo + ", "+codCluster+", "+null+")";
					acessoBanco.exec_command(lstrSQL);
				}	
			}
		}catch(SQLException ex){
			throw new SQLException ("Erro na inserção na tabela de clusters : " + ex.getMessage());
		}
		finally{
	    	AcessoBanco.close();
	    }
	}

	public void insereDistancias(int codArquivoOrigem, int codCluster, int codSubCluster, int codArquivoBase, double valDistCluster, double valDistSubCluster,double valDistArquivo) throws SQLException
	{
		try{
			String lstrSQL = "insert into documentosSimilares(codArquivoOrigem, codCluster, codSubCluster, codArquivoBase, valDistanciaCluster, valDistanciaSubCluster, valDistanciaArquivo) values ";
			lstrSQL = lstrSQL + " (" + codArquivoOrigem + ", "+codCluster+", "+codSubCluster+", "+codArquivoBase+", "+valDistCluster+", "+valDistSubCluster+", "+valDistArquivo+")";
			acessoBanco.exec_command(lstrSQL);
					
		}catch(SQLException ex){
			throw new SQLException ("Erro na inserção na tabela de documentos similares : " + ex.getMessage());
		}
		finally{
	    	AcessoBanco.close();
	    }
	}
	public void excluiArquivoFonte() throws SQLException
	{
		try{
			String lstrSQL = "delete from bagOfWords where codArquivo = " + 0;
//			System.out.println(lstrSQL);
			acessoBanco.exec_command(lstrSQL);
					
		}catch(SQLException ex){
			throw new SQLException ("Erro na limpeza da tabela bagOfWords : " + ex.getMessage());
		}
		finally{
	    	AcessoBanco.close();
	    }
	}

}
