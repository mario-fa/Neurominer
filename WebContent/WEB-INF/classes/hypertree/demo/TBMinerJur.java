package hypertree.demo;

import desktop.color.*;
import desktop.IObserverFechar;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import bancoDados.AcessoBanco;

public class TBMinerJur extends JPanel  
{

private IObserverFechar observador;
	
   private AcessoBanco acessoBanco = new AcessoBanco();	
   private String [][] data;
   private JTable table;
   private TableColumn tc0, tc1, tc2,tc3;
   
   public TBMinerJur(int [] [] dado)
   {
     super(new GridLayout(1,0));
     data = this.getData(dado);


     this.setLayout(new BorderLayout(2,1));

     table = new JTable(new MyTableModel());

     table.setPreferredScrollableViewportSize(new Dimension(1024, 750));

     //Create the scroll pane and add the table to it.
     JScrollPane scrollPane = new JScrollPane(table);
     
     tc0 = table.getColumn(table.getColumnName(0));
     tc0.setCellRenderer(new MyRenderer());
     tc1 = table.getColumn(table.getColumnName(1));
     tc1.setCellRenderer(new MyRenderer());
     tc2 = table.getColumn(table.getColumnName(2));
     tc2.setCellRenderer(new MyRenderer());

     //Set up renderer and editor for the Favorite Color column.
     table.setDefaultRenderer(Color.class, new ColorRenderer(true));
     //Add the scroll pane to this panel.
     add(scrollPane,BorderLayout.NORTH);

     table.setSelectionMode(0);
     table.setRowSelectionInterval(0, 0);
     
     table.addMouseListener(new MouseAdapter() {   
    	   public void mouseClicked(MouseEvent e) {   
    	      if (e.getClickCount()== 2) { //Aos dois cliques   
    	          int a = table.getSelectedRow();   
    	          try{
    	           Runtime.getRuntime().exec ("rundll32 SHELL32.DLL,ShellExec_RunDLL " + data [a][3]);
    	          }
    	          catch (Exception ex){}
    	      }   
    	   }   
    	});  


     JPanel summaryPanel = new JPanel();
     summaryPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
     summaryPanel.setBorder(BorderFactory.createTitledBorder("Processos Similares"));
     summaryPanel.setVisible(true);

     try
      {
        UIManager.setLookAndFeel("Windows");
        SwingUtilities.updateComponentTreeUI(this);
      }
      catch (Exception e){ }
   }

   private String [][] getData(int [] [] dado) {


       String [] [] dataTable = new String [dado.length][5];
       
      for (int i=0; i <dado.length;++i){
    	  try {
			dataTable [i][0] = this.getNomeCluster(dado [i][1]);
			dataTable [i][1] = this.getNomeDocumento(dado [i][3]);
			dataTable [i][2] = String.valueOf(dado [i][6])+ "%";			
			dataTable [i][3] = this.getPathDocumento(dado [i][3]);			
			dataTable [i][4] = String.valueOf(dado [i][2]);					
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	  
      }
       
   	   return (dataTable);
       
       

   }
   
   public void viewDocument()
   {
     String strClassErased = data [table.getSelectedRow()][0].toString();
     if (strClassErased.equals("UNKNOWN"))
       JOptionPane.showMessageDialog (null,"Você está certo", "Deseja apagar?", JOptionPane.WARNING_MESSAGE);
      else
     {
       int n = JOptionPane.showOptionDialog(this,
      "Você está certo?",
      "Vai apagar mesmo",
      JOptionPane.YES_NO_OPTION,
      JOptionPane.QUESTION_MESSAGE,
      null,     //don't use a custom Icon
      null,  //the titles of buttons
      null); //default button title
      if (n==0)
       {
         //acessBD.deleteClasses(strSampleBayes, strClassErased);
    	  System.out.println ("teste");
         //data = this.getDataBagOfWords();
         table.setModel(new MyTableModel());
         table.setRowSelectionInterval(0, 0);

       }
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
   
   class MyTableModel extends AbstractTableModel {

       private String[] columnNames = {"Grupo",
                                       "Nome do Arquivo",
                                       "Percentual de Similaridade"};

       public int getColumnCount() {
           return columnNames.length;
       }

       public int getRowCount() {
           return data.length;
       }

       public String getColumnName(int col) {
           return columnNames[col];
       }

       public Object getValueAt(int row, int col) {
               return data[row][col];

       }

       /*
        * JTable uses this method to determine the default renderer/
        * editor for each cell.  If we didn't implement this method,
        * then the last column would contain text ("true"/"false"),
        * rather than a check box.
        */
       public Class getColumnClass(int c) {
           return getValueAt(0, c).getClass();
       }

       public boolean isCellEditable(int row, int col) {
             return false;
       }

   }

   //CellRenderer
   class MyRenderer
       extends DefaultTableCellRenderer {
     public Component getTableCellRendererComponent(JTable table,
         Object value,
         boolean isSelected,
         boolean hasFocus,
         int row,
         int column) {
/*
        int numSubCluster = Integer.parseInt(data [row][4]);
     	switch (numSubCluster)
    	{
    		case 0: setBackground(Color.blue);
    		        break;
    		case 1: setBackground(Color.cyan);
    				break;
    		case 2: setBackground(Color.orange);
					break;    		
    		case 3: setBackground(Color.green);
					break;    		
    		case 4: setBackground(Color.lightGray);
					break;    		
    		case 5: setBackground(Color.magenta);
					break;
    		case 6: setBackground(Color.darkGray);
					break;
    		case 7: setBackground(Color.pink);
					break;
    		case 8: setBackground(Color.red);
					break;
    		default:  setBackground(Color.red);
    	}    	 
*/    	 
     	setBackground (Color.white);
         setForeground (Color.black);
         this.setHorizontalAlignment(JLabel.CENTER);

           return super.getTableCellRendererComponent(table,
                                                           value, isSelected, hasFocus,
                                                           row, column);

     }
   }



public void addListener(IObserverFechar observer){
	observador = observer;
}

}

