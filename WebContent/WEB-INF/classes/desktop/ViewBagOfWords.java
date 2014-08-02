package desktop;

import desktop.color.*;
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

public class ViewBagOfWords extends JPanel  
{

	private IObserverFechar observador;
	
   private AcessoBanco acessoBanco = new AcessoBanco();	
   private String [][] data;
   private JTable table;
   private JLabel lblTotal;
   private TableColumn tc0, tc1, tc2,tc3;
   
   public ViewBagOfWords()
   {
     super(new GridLayout(1,0));
     data = this.getDataBagOfWords();

     this.setLayout(new BorderLayout(2,1));

     table = new JTable(new MyTableModel());

     table.setPreferredScrollableViewportSize(new Dimension(500, 200));

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

     JPanel summaryPanel = new JPanel();
     summaryPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
     summaryPanel.setBorder(BorderFactory.createTitledBorder("Bag Of Words"));
     summaryPanel.setVisible(true);

     try
      {
        UIManager.setLookAndFeel("Windows");
        SwingUtilities.updateComponentTreeUI(this);
      }
      catch (Exception e){ }
   }

   private String [][] getDataBagOfWords() {

	   try {
       String lstrSQL = "SELECT count (*) ";
		lstrSQL = lstrSQL + " FROM bagofwords"; 

	   ResultSet rs = acessoBanco.getRS(lstrSQL);
       rs.next();
       int numRows = rs.getInt(1);
       int numColumns = 7;
       String [] [] dado = null;
	   dado = new String [numRows] [numColumns];

        lstrSQL = "SELECT bagOfWords.codBagOfWords, bagOfWords.codArquivo, bagOfWords.nomArquivo, bagOfWords.nomTermo, bagOfWords.frqTermo, bagOfWords.codcluster";
		lstrSQL = lstrSQL + " FROM bagOfWords "; 
	
		rs = acessoBanco.getRS(lstrSQL);
        int i = 0;
		while (rs.next()){
   	
        dado [i][0] = rs.getString(1);
        dado [i][1] = rs.getString(2);
        dado [i][2] = rs.getString(3);
        dado [i][3] = rs.getString(4);
        dado [i][4] = rs.getString(5);
        dado [i][5] = rs.getString(6);
        
        ++i;
		}
		
		return (dado);
       
       
	   }
		catch(SQLException ex){
			try {
				throw new SQLException (ex.getMessage());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    return null;
   }
   
   public void eraseClass()
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
         data = this.getDataBagOfWords();
         table.setModel(new MyTableModel());
         table.setRowSelectionInterval(0, 0);

       }
     }
   }

   class MyTableModel extends AbstractTableModel {

       private String[] columnNames = {"codbagofwords",
                                       "codarquivo",
                                       "nomarquivo",
                                       "nomtermo",
                                       "frqtermo",
                                       "codcluster"};

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

         setBackground( Color.BLUE);
         setForeground (Color.black);

           return super.getTableCellRendererComponent(table,
                                                           value, isSelected, hasFocus,
                                                           row, column);

     }
   }



public void addListener(IObserverFechar observer){
	observador = observer;
}

}
