package desktop;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/*
 * Título:       Mensagem.java
 * Descrição:    Classe que implementa Mensagem de Erro
 * Assunto:      Dissertacao do Mestrado em Sistemas e Computação
 * Orientador:   Manoel Mendonça
 * @author       Cristiane Costa Magalhães
 * Data       :  Ago/2007
 * @version 1.0
 */

public class MensagemErro extends JDialog{
  JPanel jPanelMensagemErro = new JPanel();
  JButton jButtonOK = new JButton();
  JTextPane jTextPanelErro = new JTextPane();

  public MensagemErro() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    jPanelMensagemErro.setLayout(null);
    this.getContentPane().setLayout(null);
    jPanelMensagemErro.setBounds(new Rectangle(-3, 6, 400, 300));
    this.setModal(true);
    this.setTitle("Ferramenta MinerJur");
    jButtonOK.setText("OK");
    jButtonOK.setBounds(new Rectangle(161, 192, 79, 27));
    jButtonOK.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButtonOK_actionPerformed(e);
      }
    });
    jTextPanelErro.setBackground(SystemColor.control);
    jTextPanelErro.setForeground(Color.red);
    jTextPanelErro.setBorder(null);
    jTextPanelErro.setCaretColor(Color.red);
    jTextPanelErro.setEditable(false);
    jTextPanelErro.setBounds(new Rectangle(11, 7, 382, 175));
    this.getContentPane().add(jPanelMensagemErro, null);
    jPanelMensagemErro.add(jTextPanelErro, null);
    jPanelMensagemErro.add(jButtonOK, null);
    setBounds(20,20,400,300);
  }

/**
 * Método:  setLabel - atualiza a mensagem de erro para exibição na tela
 * @param   astrLabel - string que contém a mensagem de erro.
 */
  public void setLabel(String astrLabel){
    jTextPanelErro.setText(astrLabel);
  }

  void jButtonOK_actionPerformed(ActionEvent e) {
    this.dispose();
  }

}
