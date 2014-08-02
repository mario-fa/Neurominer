package hypertree.demo;

import hypertree.HyperTree;
import hypertree.HTView;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;

import bancoDados.AcessoBanco;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JOptionPane;
import desktop.*;


/**
 * The Demo class implements a demo for HyperTree. It display an HyperTree
 * view of a demo tree.
 * The demo tree is build from a file tree, passed in parameter.
 * Demo could take an argument, the path from which
 * start the representing of files.
 * If no arguments is given, the treemap start from the root.
 *
 * @author Christophe Bouthier [bouthier@loria.fr]
 * @version 1.0
 */
public class DemoMinerJur {

    private static HTMinerJur root      = null; // the root of the demo tree
    private static HyperTree  hypertree = null; // the hypertree builded
	
    /**
     * Display a demo HyperTree.
     * @throws SQLException 
     */
    //public static void main(String[] args)  {
   // 	plotTree (50);
    //}
    
    public static void plotTree (int percentualProximidade, int codFormatoExibicao)  {
   
		try {
			
		calculoPercentualDistancia();
		
        AcessoBanco acessoBanco = new AcessoBanco();			

        String lstrSQL = "SELECT count (*) ";
		lstrSQL = lstrSQL + " FROM documentosSimilaresPercentual where "; 
		lstrSQL = lstrSQL + " valDistanciaSubCluster >= " + percentualProximidade; 
		lstrSQL = lstrSQL + " and valDistanciaArquivo >= " + percentualProximidade; 

		ResultSet rs = acessoBanco.getRS(lstrSQL);
        rs.next();
        int numRows = rs.getInt(1);
        int numColumns = 7;
        
		int [] [] dado = null;
		dado = new int [numRows] [numColumns];

	    lstrSQL = "SELECT codArquivoOrigem, codCluster, codSubCluster, codArquivoBase, valDistanciaCluster, valDistanciaSubCluster, valDistanciaArquivo";
		lstrSQL = lstrSQL + " FROM documentosSimilaresPercentual where "; 
		lstrSQL = lstrSQL + " valDistanciaSubCluster >= " + percentualProximidade;			
		lstrSQL = lstrSQL + " and valDistanciaArquivo >= " + percentualProximidade;	
		if (codFormatoExibicao == 1)
			lstrSQL = lstrSQL + " order by codCluster, codSubCluster, codArquivoBase ";
		else
			lstrSQL = lstrSQL + " order by valDistanciaArquivo DESC";
		rs = acessoBanco.getRS(lstrSQL);

		int i = 0;
		boolean flag;
		while (flag = rs.next()){
   	
        dado [i][0] = rs.getInt("codArquivoOrigem");
        dado [i][1] = rs.getInt("codCluster");
        dado [i][2] = rs.getInt("codSubCluster");
        dado [i][3] = rs.getInt("codArquivoBase");
        dado [i][4] = rs.getInt("valDistanciaCluster");
        dado [i][5] = rs.getInt("valDistanciaSubCluster");
        dado [i][6] = rs.getInt("valDistanciaArquivo");

        ++i;
		}

		if (i != 0) 
		{
			if (codFormatoExibicao == 1)
			{	

			int tipoDado = 0;
	        root = new HTMinerJur(dado, 0, 0, dado [0][1], dado [0][2],dado [0][3], tipoDado);
	
	        hypertree = new HyperTree(root);
	        HTView view = hypertree.getView();
	
	        
	        JFrame viewFrame = new JFrame(root.getName());
	        viewFrame.setContentPane(view);
	        viewFrame.pack();
	        viewFrame.setSize(1024, 768);
	        viewFrame.setVisible(true);
			}
			else
			{	
	        initViewTable (dado);
			}
		}
		else{		
			 JOptionPane.showMessageDialog(null,"Não existem casos julgados com o percentual de similaridade especificado" , "Error", JOptionPane.ERROR_MESSAGE);
			    return;
		}
		rs.close();		
		}
		catch(SQLException ex){
			try {
				throw new SQLException (ex.getMessage());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 

    }
   
 private static void initViewTable (int [] [] dado){

	 JFrame frameViewData = new JFrame("Documentos Similares");
	 Container container = frameViewData.getContentPane();
	 final TBMinerJur tbMinerJur = new TBMinerJur(dado);
	 container.add(tbMinerJur,BorderLayout.CENTER);
	 frameViewData.pack();
	 frameViewData.setSize(1024,768);
	 frameViewData.setVisible(true);
	 
	 
 }
    private static void calculoPercentualDistancia()
    {
    	try {
    		
        AcessoBanco acessoBanco = new AcessoBanco();			

        String lstrSQL = "SELECT count (*) ";
		lstrSQL = lstrSQL + " FROM documentosSimilares"; 

		ResultSet rs = acessoBanco.getRS(lstrSQL);
        rs.next();
        int numRows = rs.getInt(1);
        int numColumns = 7;
        
		int [] [] dado = null;
		dado = new int [numRows] [numColumns];

        lstrSQL = "SELECT codArquivoOrigem, codCluster, codSubCluster, codArquivoBase, (1.0 -valDistanciaCluster)*100 as nvalDistanciaCluster, ( 1.0 - valDistanciaSubCluster)*100 as nvalDistanciaSubCluster, (1.0 - valDistanciaArquivo)*100 as nvalDistanciaArquivo";
		lstrSQL = lstrSQL + " FROM documentosSimilares"; 
		lstrSQL = lstrSQL + " order by documentosSimilares.codCluster, documentosSimilares.codSubCluster, documentosSimilares.codArquivoBase ";
	//	System.out.println (lstrSQL);
		rs = acessoBanco.getRS(lstrSQL);

		int i = 0;
        lstrSQL = "delete from documentosSimilaresPercentual ";
        acessoBanco.exec_command(lstrSQL);
		while (rs.next()){
   	
        dado [i][0] = rs.getInt("codArquivoOrigem");
        dado [i][1] = rs.getInt("codCluster");
        dado [i][2] = rs.getInt("codSubCluster");
        dado [i][3] = rs.getInt("codArquivoBase");
        dado [i][4] = rs.getInt("nvalDistanciaCluster");
        dado [i][5] = rs.getInt("nvalDistanciaSubCluster");
        dado [i][6] = rs.getInt("nvalDistanciaArquivo");

        // Estas linhas foram inseridas para mudar a formula

        lstrSQL = "INSERT INTO documentosSimilaresPercentual ( codArquivoOrigem, codCluster, codSubCluster, codArquivoBase, valDistanciaCluster, valDistanciaSubCluster, valDistanciaArquivo )";
        lstrSQL = lstrSQL + " values ( " + dado [i] [0] + ", " + dado [i] [1] + ", " + dado [i] [2] + ", " + dado [i] [3] + ", " + dado [i] [4] + ", " + dado [i] [5] + ", " + dado [i] [6] + ")";
        acessoBanco.exec_command(lstrSQL);
        // -----------------
        
        ++i;
		}

    	// --------------------------

		/*
        lstrSQL = "delete from documentosSimilaresPercentual ";
        acessoBanco.exec_command(lstrSQL);
	
        lstrSQL = "select sum (dist) as distancia from ";
       	lstrSQL = lstrSQL + " (select distinct codCluster, valdistanciacluster*100 as dist";
       	lstrSQL = lstrSQL + " from documentossimilares)";
		rs = acessoBanco.getRS(lstrSQL);
		rs.next();
		double valTotalDistanciaCluster = (double) rs.getInt("distancia");
		
        lstrSQL = "select codCluster, sum (dist) as distancia from ";
       	lstrSQL = lstrSQL + " (select distinct codCluster, valdistanciasubcluster*100 as dist";
       	lstrSQL = lstrSQL + " from documentossimilares)";
       	lstrSQL = lstrSQL + " group by codCluster";
		ResultSet rs1 = acessoBanco.getRS(lstrSQL);
		
        lstrSQL = "select distinct codCluster, sum (valdistanciaArquivo*100) as distancia";
        lstrSQL = lstrSQL + " from documentossimilares";
        lstrSQL = lstrSQL + " group by codCluster";
		ResultSet rs2 = acessoBanco.getRS(lstrSQL);
        
		int indice = 0;
		double DistanciaCluster, DistanciaSubCluster, DistanciaArquivo ;
		while (rs1.next() && rs2.next()){
			int codCluster = rs1.getInt ("codCluster");
			int valTotalSubCluster = rs1.getInt("distancia");
			int valTotalArquivo = rs2.getInt("distancia");

		    while ((indice < dado.length) && (dado [indice][1] == codCluster)){
		    	double valDistanciaCluster = (double) dado [indice] [4];
		    	DistanciaCluster =  ((valTotalDistanciaCluster - valDistanciaCluster)/valTotalDistanciaCluster)* 100;
		    	if (DistanciaCluster ==  0)
		    		DistanciaCluster = 100.0;
		    	dado [indice] [4] = (int) DistanciaCluster;
		    	
		    	double valDistanciaSubCluster = (double) dado [indice] [5];
		    	DistanciaSubCluster = ((valTotalSubCluster - valDistanciaSubCluster)/valTotalSubCluster)* 100;
		    	if (DistanciaSubCluster ==  0)
		    		DistanciaSubCluster = 100.0;

		    	dado [indice] [5] = (int) DistanciaSubCluster;  

		    	double valDistanciaArquivo = (double) dado [indice] [6];
		    	DistanciaArquivo = ((valTotalArquivo - valDistanciaArquivo)/valTotalArquivo)* 100;
		    	if (DistanciaArquivo ==  0)
		    		DistanciaArquivo = 100.0;

		    	dado [indice] [6] = (int) DistanciaArquivo;  
		    	
		        lstrSQL = "INSERT INTO documentosSimilaresPercentual ( codArquivoOrigem, codCluster, codSubCluster, codArquivoBase, valDistanciaCluster, valDistanciaSubCluster, valDistanciaArquivo )";
		        lstrSQL = lstrSQL + " values ( " + dado [indice] [0] + ", " + dado [indice] [1] + ", " + dado [indice] [2] + ", " + dado [indice] [3] + ", " + dado [indice] [4] + ", " + dado [indice] [5] + ", " + dado [indice] [6] + ")";
		        acessoBanco.exec_command(lstrSQL);
		    	++indice;
		    }
		}*/
		rs.close();		
		}
    	
		catch(SQLException ex){
			try {
				throw new SQLException (ex.getMessage());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    	
    	
    }

}

