
/*
 * Título:       CadCategoria.java
 * Descrição:    Interface para o cadastro de categorias
 * Assunto:      Dissertacao do Mestrado em Sistemas e Computação
 * Orientador:   Manoel Mendonça
 * @author       Cristiane Costa Magalhães
 * Data       :  Ago/2007
 * @version 1.0
 *
*/

package desktop;
import dados.*;
import util.*;

import java.util.*;
import javax.swing.*;

import negocio.*;


import java.awt.*;
import java.awt.event.*;

public class CadSubCategoria extends JPanel {
  private Cadastro cadastro = new Cadastro();
  private SubCategoria subCategoria;
  private IObserverFechar observador;
  private final int ESPACAMENTO = 30;
  ArrayList arlCategoria;

  JPanel jPanelSubCategoria = new JPanel(new FlowLayout());

  JLabel jLabelBuscaCodSubCategoria = new JLabel();
  JTextField jTextFieldBuscaCodSubCategoria = new JTextField(20);

  JLabel jLabelCodLabelSubCategoria = new JLabel();
  JLabel jLabelCodSubCategoria = new JLabel();
  JLabel jLabelDescricaoSubCategoria = new JLabel();
  JLabel jLabelSiglaSubCategoria = new JLabel();
  JLabel jLabelCategoria = new JLabel();

  JTextField jTextFieldDescricaoSubCategoria = new JTextField(35);
  JTextField jTextFieldSiglaSubCategoria = new JTextField(10);
  JComboBox jComboBoxCategoria = new JComboBox();

  JButton jButtonNovoSubCategoria= new JButton();
  JButton jButtonInserirSubCategoria = new JButton();
  JButton jButtonAlterarSubCategoria = new JButton();
  JButton jButtonExcluir = new JButton();
  JButton jButtonBuscarSubCategoria = new JButton();
  JButton jButtonFechar = new JButton();

   
  public CadSubCategoria() {
    super(new GridLayout(1,0));
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {

    this.setLayout(new BorderLayout(1,1));
    this.setBorder(BorderFactory.createTitledBorder(""));

    GridBagLayout gridbag = new GridBagLayout();
    GridBagConstraints c = new GridBagConstraints();
    JPanel panel = new JPanel();
    panel.setLayout(gridbag);
    c.fill = GridBagConstraints.HORIZONTAL;

    JPanel panel_1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    jLabelBuscaCodSubCategoria.setText("Busca por Código:");
    jTextFieldBuscaCodSubCategoria.setText("");
    jButtonBuscarSubCategoria.setText("Buscar");
    panel_1.add(jLabelBuscaCodSubCategoria);
    panel_1.add(jTextFieldBuscaCodSubCategoria);
    panel_1.add(jButtonBuscarSubCategoria);
    panel_1.setBorder(BorderFactory.createTitledBorder(""));

    c.gridx = 0;
    c.gridy = 0;
    gridbag.setConstraints(panel_1, c);
    panel.add (panel_1);

    JPanel panel_2 = new JPanel(new FlowLayout(FlowLayout.LEFT,5,0));
    panel_2.setPreferredSize(new Dimension(500, ESPACAMENTO));
    JLabel espaco = new JLabel("        ");
    jLabelCodLabelSubCategoria.setText("Código:");
    jLabelCodSubCategoria.setForeground(Color.blue);
    panel_2.add(jLabelCodLabelSubCategoria);
    panel_2.add(espaco);
    panel_2.add(jLabelCodSubCategoria);

    c.gridx = 0;
    c.gridy = 4;
    gridbag.setConstraints(panel_2, c);
    panel.add ( panel_2);

    JPanel panel_3 = new JPanel(new FlowLayout(FlowLayout.LEFT,5,0));
    panel_3.setPreferredSize(new Dimension(500, ESPACAMENTO));
    jLabelDescricaoSubCategoria.setText("Descrição :");
    jTextFieldDescricaoSubCategoria.setText("");
    panel_3.add(jLabelDescricaoSubCategoria);
    panel_3.add(jTextFieldDescricaoSubCategoria);
    c.gridx = 0;
    c.gridy = 8;
    gridbag.setConstraints(panel_3, c);
    panel.add ( panel_3);

    JPanel panel_4 = new JPanel(new FlowLayout(FlowLayout.LEFT,5,0));
    panel_4.setPreferredSize(new Dimension(500, ESPACAMENTO));
    jLabelSiglaSubCategoria.setText("Sigla :");
    JLabel espaco3 = new JLabel("  ");
    jTextFieldSiglaSubCategoria.setText("");
    panel_4.add(jLabelSiglaSubCategoria);
    panel_4.add(jTextFieldSiglaSubCategoria);
    panel_4.add(espaco3);
    c.gridx = 0;
    c.gridy = 12;
    gridbag.setConstraints(panel_4, c);
    panel.add ( panel_4);

    JPanel panel_7 = new JPanel(new FlowLayout(FlowLayout.LEFT,5,0));
    panel_7.setPreferredSize(new Dimension(200, ESPACAMENTO));
    jLabelCategoria.setText("Categoria :");
    JLabel espaco7 = new JLabel("          ");
    panel_7.add(jLabelCategoria);
    panel_7.add(espaco7);

    //Carrega o combo de Categoria
    arlCategoria = new ArrayList();
    arlCategoria = cadastro.obterCategoria();
    
    for (Iterator iterator = arlCategoria.iterator(); iterator.hasNext();) {
      DadosCombo col = (DadosCombo) iterator.next();
      jComboBoxCategoria.addItem(col.getDesDadosCombo());
    }

    panel_7.add(jComboBoxCategoria);
    c.gridx = 0;
    c.gridy = 24;
    gridbag.setConstraints(panel_7, c);
    panel.add ( panel_7);

    JPanel panel_9 = new JPanel(new FlowLayout(FlowLayout.CENTER));
    jButtonInserirSubCategoria.setText("Inserir");
    jButtonAlterarSubCategoria.setText("Alterar");
    jButtonAlterarSubCategoria.setEnabled(false);
    jButtonExcluir.setText("Excluir");
    jButtonExcluir.setEnabled(false);
    jButtonNovoSubCategoria.setText("Novo");
    jButtonNovoSubCategoria.setEnabled(false);
    jButtonFechar.setText("Fechar");
    jButtonFechar.setEnabled(true);
    panel_9.add(jButtonInserirSubCategoria);
    panel_9.add(jButtonAlterarSubCategoria);
    panel_9.add(jButtonExcluir);
    panel_9.add(jButtonNovoSubCategoria);
    panel_9.add(jButtonFechar);
    c.gridx = 0;
    c.gridy = 28;
    gridbag.setConstraints(panel_9, c);
    panel.add ( panel_9);

    add(panel);


    jButtonInserirSubCategoria.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButtonInserirSubCategoria_actionPerformed(e);
      }
    });

    jButtonAlterarSubCategoria.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButtonAlterarEvento_actionPerformed(e);
      }
    });

    jButtonExcluir.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButtonExcluir_actionPerformed(e);
      }
    });

    jButtonBuscarSubCategoria.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButtonBuscarSubCategoria_actionPerformed(e);
      }
    });

    jButtonNovoSubCategoria.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButtonNovoEvento_actionPerformed(e);
      }
    });

    jButtonFechar.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButtonFechar_actionPerformed(e);
      }
    });
    jTextFieldBuscaCodSubCategoria.addKeyListener(
        new KeyAdapter() {
      public void keyTyped(KeyEvent e) {
        if (!Character.isDigit(e.getKeyChar()) &&
            !Character.isISOControl(e.getKeyChar())) {
          e.consume();
        }
        }
    }
    );

   limparComponentes();
  }

  private int obterIndexCategoria(int aintCodCategoria){
    //Exibe a categoria da Sub Categoria buscada
    int indexSubCategoria = -1;
    for (Iterator iterator = arlCategoria.iterator(); iterator.hasNext();) {
      DadosCombo col = (DadosCombo) iterator.next();
      if (col.getCodDadosCombo() == aintCodCategoria){
          indexSubCategoria = arlCategoria.indexOf(col);
          break;
      }
    }
    return indexSubCategoria;
  }
  
  void jButtonInserirSubCategoria_actionPerformed(ActionEvent e) {
    if (validarCampos() == 0){
      try {
        int lintCodSubCategoria=0;
        lintCodSubCategoria = cadastro.inserirSubCategoria(jTextFieldDescricaoSubCategoria.getText(),
         jTextFieldSiglaSubCategoria.getText(),
         jComboBoxCategoria.getSelectedIndex()+1);


        jLabelCodSubCategoria.setText("" + lintCodSubCategoria);
        habilitarBotoes(false);
        JOptionPane.showMessageDialog(jPanelSubCategoria,
                                      "A Sub Categoria foi inserido com sucesso.",
                                      "Atenção", JOptionPane.WARNING_MESSAGE);
      }
      catch (Exception ex) {
        MensagemErro ed = new MensagemErro();
        ed.setLabel(ex.getMessage());
        ed.setVisible(false);
      }
    }
  }

  void jButtonBuscarSubCategoria_actionPerformed(ActionEvent e) {
    if (jTextFieldBuscaCodSubCategoria.getText().toString().equalsIgnoreCase("")){
      JOptionPane.showMessageDialog(jPanelSubCategoria,
                                          "Campo de Busca não informado.",
                                          "Atenção", JOptionPane.WARNING_MESSAGE);

    }
    else{
      try {
        limparComponentes();
        subCategoria = cadastro.buscarSubCategoria(Integer.parseInt(
            jTextFieldBuscaCodSubCategoria.getText()));
        if (subCategoria != null) {
          jTextFieldDescricaoSubCategoria.setText(subCategoria.getDescricao());
          jTextFieldSiglaSubCategoria.setText(subCategoria.getSigla());
          jComboBoxCategoria.setSelectedIndex(obterIndexCategoria(subCategoria.getCategoria()));

          jLabelCodSubCategoria.setText(jTextFieldBuscaCodSubCategoria.getText());
          jTextFieldBuscaCodSubCategoria.setText("");
          habilitarBotoes(false);
        }
        else {
          JOptionPane.showMessageDialog(jPanelSubCategoria,
                                        "Nenhum registro encontrado.",
                                        "Atenção",
                                        JOptionPane.WARNING_MESSAGE);
          limparComponentes();
        }

      }
      catch (Exception ex) {
        MensagemErro ed = new MensagemErro();
        ed.setLabel(ex.getMessage());
        ed.setVisible(false);
      }
    }
  }


  void jButtonAlterarEvento_actionPerformed(ActionEvent e) {
    if (validarCampos() == 0){
      try{
        cadastro.alterarSubCategoria(Integer.parseInt(jLabelCodSubCategoria.getText()), jTextFieldDescricaoSubCategoria.getText(),
        			jTextFieldSiglaSubCategoria.getText(), jComboBoxCategoria.getSelectedIndex());
       JOptionPane.showMessageDialog(jPanelSubCategoria, "A Sub Categoria foi alterado com sucesso.", "Atenção", JOptionPane.WARNING_MESSAGE);
      } catch(Exception ex){
          MensagemErro ed = new MensagemErro();
          ed.setLabel(ex.getMessage());
          ed.setVisible(false);
      }
    }
  }

  void jButtonNovoEvento_actionPerformed(ActionEvent e) {
    habilitarBotoes(true);
    limparComponentes();
  }

  void jButtonFechar_actionPerformed(ActionEvent e) {
     observador.fechar();
  }


  private void limparComponentes(){
     jLabelCodSubCategoria.setText("");
     jTextFieldDescricaoSubCategoria.setText("");
     jTextFieldSiglaSubCategoria.setText("");
  }

  private int validarCampos(){
    int retorno = 0;

    if ((jTextFieldDescricaoSubCategoria.getText().toString().equals("") ||
          (jTextFieldSiglaSubCategoria.getText().toString().equals(""))))

    {
      JOptionPane.showMessageDialog(jPanelSubCategoria, "Campos de preenchimento obrigatório não podem ser nulos", "Atenção", JOptionPane.WARNING_MESSAGE);
      retorno = 1;
    }
    return retorno;
  }

  private void habilitarBotoes(boolean aboSubCategoria){
      jButtonNovoSubCategoria.setEnabled(!aboSubCategoria);
      jButtonInserirSubCategoria.setEnabled(aboSubCategoria) ;
      jButtonAlterarSubCategoria.setEnabled(!aboSubCategoria) ;
      jButtonExcluir.setEnabled(!aboSubCategoria);
  }

  void jButtonExcluir_actionPerformed(ActionEvent e) {
    try{
    if (JOptionPane.showConfirmDialog(jPanelSubCategoria,"Deseja realmente excluir? "," Excluir Sub Categoria",JOptionPane.YES_NO_OPTION) == 0) {
      cadastro.excluirSubCategoria(Integer.parseInt(jLabelCodSubCategoria.getText()));
      habilitarBotoes(true);
      limparComponentes();
      JOptionPane.showMessageDialog(jPanelSubCategoria, "A Sub Categoria foi excluída com sucesso.", "Atenção", JOptionPane.WARNING_MESSAGE);
    }
   } catch(Exception ex){
        MensagemErro ed = new MensagemErro();
        ed.setLabel(ex.getMessage());
        ed.setVisible(false);
    }

  }

  public void addListener(IObserverFechar observer){
    observador = observer;
  }

}
