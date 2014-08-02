/*
 * Título:       CadCategoria.java
 * Descrição:    Interface para o cadastro de categorias
 * Assunto:      Dissertacao do Mestrado em Sistemas e Computação
 * Orientador:   Manoel Mendonça
 * @author       Cristiane Costa Magalhães
 * Data       :  Ago/2007
 * @version 1.0
 */

package desktop;
import dados.*;

import javax.swing.*;

import negocio.*;


import java.awt.*;
import java.awt.event.*;

public class CadCategoria extends JPanel {
  private Cadastro cadastro = new Cadastro();
  private Categoria categoria;
  private IObserverFechar observador;
  private final int ESPACAMENTO = 30;
  
  JPanel jPanelCategoria = new JPanel(new FlowLayout());

  JLabel jLabelBuscaCodCategoria = new JLabel();
  JTextField jTextFieldBuscaCodCategoria = new JTextField(20);

  JLabel jLabelCodLabelCategoria = new JLabel();
  JLabel jLabelCodCategoria = new JLabel();

  JLabel jLabelNomeCategoria = new JLabel();
  JTextField jTextFieldNomeCategoria = new JTextField(30);

  JLabel jLabelSiglaCategoria = new JLabel();
  JTextField jTextFieldSiglaCategoria = new JTextField(10);


  JButton jButtonNovoCategoria = new JButton();
  JButton jButtonInserirCategoria = new JButton();
  JButton jButtonAlterarCategoria = new JButton();
  JButton jButtonExcluir = new JButton();
  JButton jButtonBuscarCategoria = new JButton();
  JButton jButtonFechar = new JButton();

  public CadCategoria() {
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
    jLabelBuscaCodCategoria.setText("Busca por Código:");
    jTextFieldBuscaCodCategoria.setText("");
    jButtonBuscarCategoria.setText("Buscar");
    panel_1.add(jLabelBuscaCodCategoria);
    panel_1.add(jTextFieldBuscaCodCategoria);
    panel_1.add(jButtonBuscarCategoria);
    panel_1.setBorder(BorderFactory.createTitledBorder(""));

    c.gridx = 0;
    c.gridy = 0;
    gridbag.setConstraints(panel_1, c);
    panel.add (panel_1);

    JPanel panel_2 = new JPanel(new FlowLayout(FlowLayout.LEFT,5,0));
    panel_2.setPreferredSize(new Dimension(51, ESPACAMENTO));
    JLabel espaco = new JLabel("         ");
    jLabelCodLabelCategoria.setText("Código:");
    jLabelCodCategoria.setForeground(Color.blue);
    panel_2.add(jLabelCodLabelCategoria);
    panel_2.add(espaco);
    panel_2.add(jLabelCodCategoria);

    c.gridx = 0;
    c.gridy = 4;
    gridbag.setConstraints(panel_2, c);
    panel.add ( panel_2);

    JPanel panel_3 = new JPanel(new FlowLayout(FlowLayout.LEFT,5,0));
    panel_3.setPreferredSize(new Dimension(51, ESPACAMENTO));
    jLabelNomeCategoria.setText("Descrição :");
    jTextFieldNomeCategoria.setText("");
    panel_3.add(jLabelNomeCategoria);
    panel_3.add(jTextFieldNomeCategoria);
    c.gridx = 0;
    c.gridy = 6;
    gridbag.setConstraints(panel_3, c);
    panel.add ( panel_3);

    JPanel panel_4 = new JPanel(new FlowLayout(FlowLayout.LEFT,5,0));
    panel_4.setPreferredSize(new Dimension(51, ESPACAMENTO+20));
    jLabelSiglaCategoria.setText("Sigla :");
    jTextFieldSiglaCategoria.setText("");
    panel_4.add(jLabelSiglaCategoria);
    panel_4.add(jTextFieldSiglaCategoria);
    c.gridx = 0;
    c.gridy = 8;
    gridbag.setConstraints(panel_4, c);
    panel.add ( panel_4);

    JPanel panel_5 = new JPanel(new FlowLayout(FlowLayout.CENTER));

    jButtonInserirCategoria.setText("Inserir");
    jButtonAlterarCategoria.setText("Alterar");
    jButtonAlterarCategoria.setEnabled(false);
    jButtonExcluir.setText("Excluir");
    jButtonExcluir.setEnabled(false);
    jButtonNovoCategoria.setText("Novo");
    jButtonNovoCategoria.setEnabled(false);
    jButtonFechar.setText("Fechar");
    jButtonFechar.setEnabled(true);
    panel_5.add(jButtonInserirCategoria);
    panel_5.add(jButtonAlterarCategoria);
    panel_5.add(jButtonExcluir);
    panel_5.add(jButtonNovoCategoria);
    panel_5.add(jButtonFechar);
    c.gridx = 0;
    c.gridy = 10;
    gridbag.setConstraints(panel_5, c);
    panel.add ( panel_5);

    add(panel);

    jButtonInserirCategoria.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButtonInserir_actionPerformed(e);
      }
    });

    jButtonAlterarCategoria.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButtonAlterar_actionPerformed(e);
      }
    });

    jButtonExcluir.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButtonExcluir_actionPerformed(e);
      }
    });

    jButtonBuscarCategoria.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButtonBuscar_actionPerformed(e);
      }
    });

    jButtonNovoCategoria.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButtonNovo_actionPerformed(e);
      }
    });

    jButtonFechar.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButtonFechar_actionPerformed(e);
      }
    });

    jTextFieldBuscaCodCategoria.addKeyListener(
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


  void jButtonInserir_actionPerformed(ActionEvent e) {
    if (validarCampos() == 0){
      try{
          int lintCodCategoria;
          lintCodCategoria = cadastro.inserirCategoria(jTextFieldNomeCategoria.getText(), jTextFieldSiglaCategoria.getText());
          jLabelCodCategoria.setText("" + lintCodCategoria);
          habilitarBotoes(false);
          JOptionPane.showMessageDialog(jPanelCategoria, "A Categoria foi inserida com sucesso.", "Atenção", JOptionPane.WARNING_MESSAGE);
      } catch(Exception ex){
          MensagemErro ed = new MensagemErro();
          ed.setLabel(ex.getMessage());
          ed.setVisible(false);
      }
    }
  }

  void jButtonBuscar_actionPerformed(ActionEvent e) {
    if (jTextFieldBuscaCodCategoria.getText().toString().equalsIgnoreCase("")) {
      JOptionPane.showMessageDialog(jPanelCategoria,
                                    "Campo de Busca não informado.",
                                    "Atenção", JOptionPane.WARNING_MESSAGE);

    }
    else
    {
      try {
        limparComponentes();
        categoria = cadastro.buscarCategoria(Integer.parseInt(
            jTextFieldBuscaCodCategoria.getText()));

        if (categoria != null) {
          jTextFieldNomeCategoria.setText(categoria.getDescricao());
          jTextFieldSiglaCategoria.setText(categoria.getSigla());
          jLabelCodCategoria.setText(jTextFieldBuscaCodCategoria.getText());
          jTextFieldBuscaCodCategoria.setText("");
          habilitarBotoes(false);
        }
        else {
          JOptionPane.showMessageDialog(jPanelCategoria,
                                        "Nenhum registro encontrado.",
                                        "Atenção", JOptionPane.WARNING_MESSAGE);
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

  void jButtonAlterar_actionPerformed(ActionEvent e) {
     if (validarCampos() == 0){
       try {
         cadastro.alterarCategoria(Integer.parseInt(jLabelCodCategoria.getText()), jTextFieldNomeCategoria.getText(),jTextFieldSiglaCategoria.
                                       getText());
         JOptionPane.showMessageDialog(jPanelCategoria,
             "A Categoria foi alterada com sucesso.", "Atenção",
                                       JOptionPane.WARNING_MESSAGE);
       }
       catch (Exception ex) {
         MensagemErro ed = new MensagemErro();
         ed.setLabel(ex.getMessage());
         ed.setVisible(false);
       }
     }
  }

  void jButtonNovo_actionPerformed(ActionEvent e) {
    habilitarBotoes(true);
    limparComponentes();
  }

  void jButtonFechar_actionPerformed(ActionEvent e) {
     observador.fechar();
  }


  private void limparComponentes(){
     jLabelCodCategoria.setText("");
     jTextFieldNomeCategoria.setText("");
     jTextFieldSiglaCategoria.setText("");
  }

  private int validarCampos(){
    int retorno = 0;

    if ((jTextFieldNomeCategoria.getText().toString().equals("")) ||
        (jTextFieldSiglaCategoria.getText().toString().equals("")))
    {
      JOptionPane.showMessageDialog(jPanelCategoria, "Campos de preenchimento obrigatório não podem ser nulos", "Atenção", JOptionPane.WARNING_MESSAGE);
      retorno = 1;
    }
    return retorno;
  }

  private void habilitarBotoes(boolean aboEstado){
      jButtonNovoCategoria.setEnabled(!aboEstado);
      jButtonInserirCategoria.setEnabled(aboEstado) ;
      jButtonAlterarCategoria.setEnabled(!aboEstado) ;
      jButtonExcluir.setEnabled(!aboEstado);
  }

  void jButtonExcluir_actionPerformed(ActionEvent e) {
    try{
    if (JOptionPane.showConfirmDialog(jPanelCategoria,"Deseja realmente excluir? "," Excluir Categoria",JOptionPane.YES_NO_OPTION) == 0) {
      cadastro.excluirCategoria(Integer.parseInt(jLabelCodCategoria.getText()));
      habilitarBotoes(true);
      limparComponentes();
      JOptionPane.showMessageDialog(jPanelCategoria, "A Categoria foi excluída com sucesso.", "Atenção", JOptionPane.WARNING_MESSAGE);
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
