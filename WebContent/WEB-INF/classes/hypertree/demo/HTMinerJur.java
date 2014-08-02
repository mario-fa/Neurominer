
package hypertree.demo;

import hypertree.HTNode;
import java.util.Enumeration;
import java.util.Hashtable;
import java.awt.Color;
import java.sql.*;

import bancoDados.AcessoBanco;


public class HTMinerJur
    implements HTNode {

    private Hashtable children = null; // the children of this node
    private String name;
    private String pathFile;
    private int tipoDado;
    private int numCluster;
    private int distancia;
    private AcessoBanco acessoBanco = new AcessoBanco();

  /* --- Constructor --- */

    /**
     * Constructor.
     *
     * @param tipoDado: 0 - Documento Central (inicial)
     *                  1 - Cluster
     *                  2 - Documento do Cluster 
     * @throws SQLException 
     */            
    public HTMinerJur (int dado[][],int indice, int codArquivoOrigem, int codCluster, int codSubCluster, int codArquivoBase, int tipoDado) throws SQLException {
        
    	try {
    	
    	HTMinerJur child;
    	this.tipoDado = tipoDado;
    	children = new Hashtable();
    	switch (tipoDado)
    	{
    	case 0:
    		int codDocumento = codArquivoOrigem;
    		this.name = getNomeDocumento(codDocumento);
    		this.pathFile = getPathDocumento (codDocumento);
    		this.distancia = 100;
    		while (indice < dado.length){
    		   child = new HTMinerJur (dado, indice, codArquivoOrigem, codCluster, codSubCluster, codArquivoBase, 1);
               addChild(child);  
               while (indice < dado.length && (dado [indice][1] == codCluster)  )
                 ++ indice;
               if (indice < dado.length) {
	               codArquivoOrigem = dado [indice][0];
	               codCluster = dado [indice][1];               
	               codSubCluster= dado [indice][2];              
	               codArquivoBase = dado [indice][3];
               }
                              
    		}
    		break;
    	case 1:
    		this.name = getNomeCluster(codCluster);
    		this.distancia = dado [indice] [4];
    		int newCodCluster = codCluster;
    		while ((indice < dado.length) && (codCluster == newCodCluster)){    		
              child = new HTMinerJur (dado, indice, codArquivoOrigem, newCodCluster, codSubCluster, codArquivoBase, 2);
              addChild(child);
              ++indice;
              if (indice < dado.length) {
	              codArquivoOrigem = dado [indice][0];
	              newCodCluster = dado [indice][1];
	              codSubCluster= dado [indice][2];              
	              codArquivoBase = dado [indice][3];
              }
    		}
    		break;
    	case 2:
    		this.numCluster = codSubCluster;
    		this.distancia = dado [indice] [6];    		
    		this.name = getNomeDocumento(codArquivoBase);
    		this.pathFile = getPathDocumento (codArquivoBase);    		
    		break;
    	}
    	
		}
		catch(SQLException ex){
			throw new SQLException (ex.getMessage());
		} 
      
    }


    String getNomeDocumento(int codArquivo) throws SQLException{
		try {
	    	String lstrSQL = "SELECT distinct nomArquivo from bagOfWords where codArquivo = " + codArquivo;
			ResultSet rs = acessoBanco.getRS(lstrSQL);
	    	rs.next();
	    	String nome = rs.getString("nomArquivo");
	    	rs.close();
	    	return (nome);
		}
		catch(SQLException ex){
			throw new SQLException (ex.getMessage());
		} 
    }

    String getPathDocumento(int codArquivo) throws SQLException{
		try {
	    	String lstrSQL = "SELECT distinct nomCaminhoArquivo from bagOfWords where codArquivo = " + codArquivo;
			ResultSet rs = acessoBanco.getRS(lstrSQL);
	    	rs.next();
	    	String nome = rs.getString("nomCaminhoArquivo");
	    	rs.close();
	    	return (nome);
		}
		catch(SQLException ex){
			throw new SQLException (ex.getMessage());
		} 
    }
    
    
    
    String getNomeCluster(int codCluster) throws SQLException{
		try {
	    	String lstrSQL = "SELECT desCategoria from categoria where codCategoria = " + codCluster;
			ResultSet rs = acessoBanco.getRS(lstrSQL);
	    	rs.next();
	    	String nome = rs.getString("desCategoria");
	    	rs.close();
	    	return (nome);
		}
		catch(SQLException ex){
			throw new SQLException (ex.getMessage());
		} 
    }    
    
  /* --- Tree management --- */

    /**
     * Add child to the node.
     * 
     * @param child    the HTFileNode to add as a child
     */
    protected void addChild(HTMinerJur child) {
        children.put(child.getName(), child);
    }


  /* --- HTNode --- */

    /**
     * Returns the children of this node in an Enumeration.
     * If this node is a file, return a empty Enumeration.
     * Else, return an Enumeration full with HTFileNode.
     *
     * @return    an Enumeration containing childs of this node
     */
    public Enumeration children() {
        return children.elements();
    }

    /**
     * Returns true if this node is not a directory.
     */
    public boolean isLeaf() {
    	
        return (this.tipoDado == 4);
    }
    
    /**
     * Returns the name of the file.
     *
     * @return    the name of the file
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the color of the file.
     *
     * @return    the color of the file
     */
    public Color getColor() {
    	
    	switch (this.tipoDado)
    	{
    	case 0: return Color.white;
    	case 1: return Color.yellow;   	       
    	case 2: 
    		switch (this.numCluster){
    		case 0: return Color.blue;
    		case 1: return Color.cyan;
    		case 2: return Color.orange;
    		case 3: return Color.green;
    		case 4: return Color.gray;
    		case 5: return Color.magenta;
    		case 6: return Color.darkGray;
    		case 7: return Color.pink;
    		case 8: return Color.lightGray;
    		}
		default:  return Color.lightGray;
    	}
    }
    
    public int getDistance()
    {
    	return this.distancia;
    }
    public int getTypeData()
    {
    	return this.tipoDado;
    }
    
    public String getPathFile()
    {
    	return this.pathFile;
    }
    
}


