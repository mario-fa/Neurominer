/*
 * Título:       CadEquivalencia.java
 * Descrição:    Interface para o cadastro de equivalencias
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

public class CadEquivalencia extends JPanel {
  private Cadastro cadastro = new Cadastro();
  private Equivalencia equivalencia;
  private IObserverFechar observador;
  private final int ESPACAMENTO = 30;
  
  JPanel jPanelEquivalencia = new JPanel(new FlowLayout());

  JLabel jLabelBuscaCodEquivalencia = new JLabel();
  JTextField jTextFieldBuscaCodEquivalencia = new JTextField(20);

  JLabel jLabelCodLabelEquivalencia = new JLabel();
  JLabel jLabelCodEquivalencia = new JLabel();

  JLabel jLabelNomeEquivalencia = new JLabel();
  JTextField jTextFieldNomeEquivalencia = new JTextField(30);


  JButton jButtonNovoEquivalencia = new JButton();
  JButton jButtonInserirEquivalencia = new JButton();
  JButton jButtonAlterarEquivalencia = new JButton();
  JButton jButtonExcluir = new JButton();
  JButton jButtonBuscarEquivalencia = new JButton();
  JButton jButtonFechar = new JButton();

  public CadEquivalencia() {
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
    jLabelBuscaCodEquivalencia.setText("Busca por Código:");
    jTextFieldBuscaCodEquivalencia.setText("");
    jButtonBuscarEquivalencia.setText("Buscar");
    panel_1.add(jLabelBuscaCodEquivalencia);
    panel_1.add(jTextFieldBuscaCodEquivalencia);
    panel_1.add(jButtonBuscarEquivalencia);
    panel_1.setBorder(BorderFactory.createTitledBorder(""));

    c.gridx = 0;
    c.gridy = 0;
    gridbag.setConstraints(panel_1, c);
    panel.add (panel_1);

    JPanel panel_2 = new JPanel(new FlowLayout(FlowLayout.LEFT,5,0));
    panel_2.setPreferredSize(new Dimension(51, ESPACAMENTO));
    JLabel espaco = new JLabel("         ");
    jLabelCodLabelEquivalencia.setText("Código:");
    jLabelCodEquivalencia.setForeground(Color.blue);
    panel_2.add(jLabelCodLabelEquivalencia);
    panel_2.add(espaco);
    panel_2.add(jLabelCodEquivalencia);

    c.gridx = 0;
    c.gridy = 4;
    gridbag.setConstraints(panel_2, c);
    panel.add ( panel_2);

    JPanel panel_3 = new JPanel(new FlowLayout(FlowLayout.LEFT,5,0));
    panel_3.setPreferredSize(new Dimension(51, ESPACAMENTO));
    jLabelNomeEquivalencia.setText("Descrição :");
    jTextFieldNomeEquivalencia.setText("");
    panel_3.add(jLabelNomeEquivalencia);
    panel_3.add(jTextFieldNomeEquivalencia);
    c.gridx = 0;
    c.gridy = 6;
    gridbag.setConstraints(panel_3, c);
    panel.add ( panel_3);


    JPanel panel_5 = new JPanel(new FlowLayout(FlowLayout.CENTER));

    jButtonInserirEquivalencia.setText("Inserir");
    jButtonAlterarEquivalencia.setText("Alterar");
    jButtonAlterarEquivalencia.setEnabled(false);
    jButtonExcluir.setText("Excluir");
    jButtonExcluir.setEnabled(false);
    jButtonNovoEquivalencia.setText("Novo");
    jButtonNovoEquivalencia.setEnabled(false);
    jButtonFechar.setText("Fechar");
    jButtonFechar.setEnabled(true);
    panel_5.add(jButtonInserirEquivalencia);
    panel_5.add(jButtonAlterarEquivalencia);
    panel_5.add(jButtonExcluir);
    panel_5.add(jButtonNovoEquivalencia);
    panel_5.add(jButtonFechar);
    c.gridx = 0;
    c.gridy = 10;
    gridbag.setConstraints(panel_5, c);
    panel.add ( panel_5);

    add(panel);

    jButtonInserirEquivalencia.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButtonInserir_actionPerformed(e);
      }
    });

    jButtonAlterarEquivalencia.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButtonAlterar_actionPerformed(e);
      }
    });

    jButtonExcluir.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButtonExcluir_actionPerformed(e);
      }
    });

    jButtonBuscarEquivalencia.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButtonBuscar_actionPerformed(e);
      }
    });

    jButtonNovoEquivalencia.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButtonNovo_actionPerformed(e);
      }
    });

    jButtonFechar.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButtonFechar_actionPerformed(e);
      }
    });

    jTextFieldBuscaCodEquivalencia.addKeyListener(
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
          int lintCodEquivalencia;
          lintCodEquivalencia = cadastro.inserirEquivalencia(jTextFieldNomeEquivalencia.getText());
          jLabelCodEquivalencia.setText("" + lintCodEquivalencia);
          habilitarBotoes(false);
          JOptionPane.showMessageDialog(jPanelEquivalencia, "A Equivalência foi inserida com sucesso.", "Atenção", JOptionPane.WARNING_MESSAGE);
      } catch(Exception ex){
          MensagemErro ed = new MensagemErro();
          ed.setLabel(ex.getMessage());
          ed.setVisible(false);
      }
    }
  }

  void jButtonBuscar_actionPerformed(ActionEvent e) {
    if (jTextFieldBuscaCodEquivalencia.getText().toString().equalsIgnoreCase("")) {
      JOptionPane.showMessageDialog(jPanelEquivalencia,
                                    "Campo de Busca não informado.",
                                    "Atenção", JOptionPane.WARNING_MESSAGE);

    }
    else
    {
      try {
        limparComponentes();
        equivalencia = cadastro.buscarEquivalencia(Integer.parseInt(
            jTextFieldBuscaCodEquivalencia.getText()));

        if (equivalencia != null) {
          jTextFieldNomeEquivalencia.setText(equivalencia.getDescricao());
          jLabelCodEquivalencia.setText(jTextFieldBuscaCodEquivalencia.getText());
          jTextFieldBuscaCodEquivalencia.setText("");
          habilitarBotoes(false);
        }
        else {
          JOptionPane.showMessageDialog(jPanelEquivalencia,
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
         cadastro.alterarEquivalencia(Integer.parseInt(jLabelCodEquivalencia.getText()), jTextFieldNomeEquivalencia.getText());
         JOptionPane.showMessageDialog(jPanelEquivalencia,
             "A Equivalência foi alterada com sucesso.", "Atenção",
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
     jLabelCodEquivalencia.setText("");
     jTextFieldNomeEquivalencia.setText("");
    }

  private int validarCampos(){
    int retorno = 0;

    if (jTextFieldNomeEquivalencia.getText().toString().equals(""))
    {
      JOptionPane.showMessageDialog(jPanelEquivalencia, "Campos de preenchimento obrigatório não podem ser nulos", "Atenção", JOptionPane.WARNING_MESSAGE);
      retorno = 1;
    }
    return retorno;
  }

  private void habilitarBotoes(boolean aboEstado){
      jButtonNovoEquivalencia.setEnabled(!aboEstado);
      jButtonInserirEquivalencia.setEnabled(aboEstado) ;
      jButtonAlterarEquivalencia.setEnabled(!aboEstado) ;
      jButtonExcluir.setEnabled(!aboEstado);
  }

  void jButtonExcluir_actionPerformed(ActionEvent e) {
    try{
    if (JOptionPane.showConfirmDialog(jPanelEquivalencia,"Deseja realmente excluir? "," Excluir Equivalencia",JOptionPane.YES_NO_OPTION) == 0) {
      cadastro.excluirEquivalencia(Integer.parseInt(jLabelCodEquivalencia.getText()));
      habilitarBotoes(true);
      limparComponentes();
      JOptionPane.showMessageDialog(jPanelEquivalencia, "A Equivalência foi excluída com sucesso.", "Atenção", JOptionPane.WARNING_MESSAGE);
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
