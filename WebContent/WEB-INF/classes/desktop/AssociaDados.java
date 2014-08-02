/*
 * Título:       AssociaDados.java
 * Descrição:    Interface genérica para associação de dados
 * Assunto:      Dissertacao do Mestrado em Sistemas e Computação
 * Orientador:   Manoel Mendonça
 * @author       Cristiane Costa Magalhães
 * Data       :  Ago/2007
 * @version 1.0
 */


package desktop;


import java.awt.*;
import javax.swing.*;
import java.awt.event.*;


 public class AssociaDados extends JDialog {

    MensagemErro me = new MensagemErro();
    JList jlist1 = new JList();
    JList jlist2 = new JList();
    DefaultListModel model_ind1 = new DefaultListModel();
    DefaultListModel model_data1 = new DefaultListModel();
    DefaultListModel model_ind2 = new DefaultListModel();
    DefaultListModel model_data2 = new DefaultListModel();
    String label1, label2;
    JButton btAssocia = new JButton(">>");
    JButton btDesassocia = new JButton("<<");
    JButton btFechar = new JButton("Salvar Dados");
    private IObserverFechar observador;


    public AssociaDados(Frame owner, String titulo, String [][] list1, String[][] list2, String lb1, String lb2) {
      super(owner, titulo, true);
      if (list1 != null){
        for (int i = 0; i < list1.length; ++i) {
          model_ind1.add(i, list1[i][0]);
          model_data1.add(i, list1[i][1]);
        }
      }
      if (list2 != null){
        for (int i = 0; i < list2.length; ++i) {
          model_ind2.add(i, list2[i][0]);
          model_data2.add(i, list2[i][1]);
        }
      }

      label1= lb1;
      label2=lb2;
      try {
        jbInit();

      }
      catch(Exception e) {
        e.printStackTrace();
      }
   }

  private void jbInit() throws Exception {

    GridBagLayout gridbag = new GridBagLayout();
    GridBagConstraints c = new GridBagConstraints();
    getContentPane().setLayout(gridbag);
    c.fill = GridBagConstraints.BOTH;

    JPanel panel_label1 = new JPanel(new FlowLayout (FlowLayout.LEFT, 0,1 ));
    JLabel label_1 = new JLabel(label1);
    panel_label1.add (label_1);
    c.gridx = 0;
    c.gridy = 0;
    gridbag.setConstraints(panel_label1, c);
    getContentPane().add(panel_label1);

    jlist1 = new JList (model_data1);

    JScrollPane scrollPane1 = new JScrollPane (jlist1);
    scrollPane1.setPreferredSize(new Dimension (200,150));
    c.gridx = 0;
    c.gridy = 1;
    gridbag.setConstraints(scrollPane1, c);
    getContentPane().add(scrollPane1);

    JPanel panel_1 = new JPanel(new GridLayout(2,0));
    panel_1.add(btAssocia);
    panel_1.add(btDesassocia);
    c.gridx = 1;
    c.gridy = 1;
    gridbag.setConstraints(panel_1, c);
    getContentPane().add(panel_1);


    JPanel panel_label2 = new JPanel(new FlowLayout (FlowLayout.LEFT,0,1 ));
    JLabel label_2 = new JLabel(label2);
    panel_label2.add (label_2);
    c.gridx = 2;
    c.gridy = 0;
    gridbag.setConstraints(panel_label2, c);
    getContentPane().add(panel_label2);

    jlist2 = new JList (model_data2);

    JScrollPane scrollPane2 = new JScrollPane (jlist2);
    scrollPane2.setPreferredSize(new Dimension (200,150));
    c.gridx = 2;
    c.gridy = 1;
    gridbag.setConstraints(scrollPane2, c);
    getContentPane().add(scrollPane2);

    JPanel panel_2 = new JPanel(new FlowLayout (FlowLayout.CENTER));
    panel_2.add(btFechar);
    c.gridx = 1;
    c.gridy = 2;
    gridbag.setConstraints(panel_2, c);
    getContentPane().add(panel_2);
    pack();
    setResizable(false);

    ActionListener lst = new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        associaDado();
      }
    };
    btAssocia.addActionListener(lst);

    ActionListener lst2 = new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        desassociaDado();
      }
    };
    btDesassocia.addActionListener(lst2);

    btFechar.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButtonFechar_actionPerformed(e);
      }
    });
  }

  private void associaDado(){
     int selecionados [] = jlist1.getSelectedIndices();
     for (int i=0; i<selecionados.length;++i){
       model_data2.add(model_data2.size(),
                       model_data1.getElementAt(selecionados[i]));
                       model_ind2.add(model_ind2.size(), model_ind1.getElementAt(selecionados[i]));
     }
     for (int i=0; i<selecionados.length;++i){
         model_data1.removeElementAt(selecionados[i]-i);
         model_ind1.removeElementAt(selecionados[i]-i);
     }
  }

  private void desassociaDado(){
     int selecionados [] = jlist2.getSelectedIndices();
     for (int i=0; i<selecionados.length;++i){
         model_data1.add(model_data1.size(), model_data2.getElementAt(selecionados[i]));
         model_ind1.add(model_ind1.size(), model_ind2.getElementAt(selecionados[i]));
     }
     for (int i=0; i<selecionados.length;++i){
         model_data2.removeElementAt(selecionados[i]-i);
         model_ind2.removeElementAt(selecionados[i]-i);

     }
  }

  void jButtonFechar_actionPerformed(ActionEvent e) {
     observador.fechar();
  }

  public void addListener(IObserverFechar observer){
    observador = observer;
  }

  public String [][] getLista1(){
    int numColumns = model_data1.size();
    String [][] resultado= new String [numColumns][2];
    for (int i=0; i <model_data1.getSize();++i){
      resultado[i][0] = String.valueOf(model_ind1.getElementAt(i));
      resultado[i][1] = String.valueOf(model_data1.getElementAt(i));
    }
    return (resultado);
  }

  public String [][] getLista2(){
    int numColumns = model_data2.size();
    String [][] resultado= new String [numColumns][2];
    for (int i=0; i <model_data2.getSize();++i){
      resultado[i][0] = String.valueOf(model_ind2.getElementAt(i));
      resultado[i][1] = String.valueOf(model_data2.getElementAt(i));
    }
    return (resultado);
  }


}
