/*
 * Título:       CadDescritor.java
 * Descrição:    Interface para o cadastro de descritores
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

public class CadDescritor extends JPanel {
  private Cadastro cadastro = new Cadastro();
  private Descritor descritor;
  private IObserverFechar observador;
  private final int ESPACAMENTO = 30;

  JPanel jPanelDescritor = new JPanel(new FlowLayout());

  JLabel jLabelBuscaCodDescritor = new JLabel();
  JTextField jTextFieldBuscaCodDescritor = new JTextField(20);

  JLabel jLabelCodLabelDescritor = new JLabel();
  JLabel jLabelCodDescritor = new JLabel();

  JLabel jLabelNomeDescritor = new JLabel();
  JTextField jTextFieldNomeDescritor = new JTextField(30);

  JButton jButtonNovoDescritor = new JButton();
  JButton jButtonInserirDescritor = new JButton();
  JButton jButtonAlterarDescritor = new JButton();
  JButton jButtonExcluir = new JButton();
  JButton jButtonBuscarDescritor = new JButton();
  JButton jButtonFechar = new JButton();
  JButton jButtonTermosGenericos = new JButton();
  JButton jButtonTermosEspecificos = new JButton();
  JButton jButtonEquivalencias = new JButton();
  JButton jButtonTermosRelacionados = new JButton();
  JButton jButtonSubCategorias = new JButton();
  JButton jButtonAplicarRadicalizacao = new JButton();

  String[][] listaTermosGenericosAssociados;
  String[][] listaTermosGenericosAAssociar;

  String[][] listaTermosEspecificosAssociados;
  String[][] listaTermosEspecificosAAssociar;

  String[][] listaEquivalenciasAssociadas;
  String[][] listaEquivalenciasAAssociar;

  String[][] listaTermosRelacionadosAssociados;
  String[][] listaTermosRelacionadosAAssociar;

  String[][] listaSubCategoriasAssociadas;
  String[][] listaSubCategoriasAAssociar;

  public CadDescritor() {
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
    jLabelBuscaCodDescritor.setText("Busca por Código:");
    jTextFieldBuscaCodDescritor.setText("");
    jButtonBuscarDescritor.setText("Buscar");
    panel_1.add(jLabelBuscaCodDescritor);
    panel_1.add(jTextFieldBuscaCodDescritor);
    panel_1.add(jButtonBuscarDescritor);
    panel_1.setBorder(BorderFactory.createTitledBorder(""));

    c.gridx = 0;
    c.gridy = 0;
    gridbag.setConstraints(panel_1, c);
    panel.add (panel_1);

    JPanel panel_2 = new JPanel(new FlowLayout(FlowLayout.LEFT,5,0));
    panel_2.setPreferredSize(new Dimension(51, ESPACAMENTO));
    JLabel espaco = new JLabel("         ");
    jLabelCodLabelDescritor.setText("Código:");
    jLabelCodDescritor.setForeground(Color.blue);
    panel_2.add(jLabelCodLabelDescritor);
    panel_2.add(espaco);
    panel_2.add(jLabelCodDescritor);

    c.gridx = 0;
    c.gridy = 4;
    gridbag.setConstraints(panel_2, c);
    panel.add ( panel_2);

    JPanel panel_3 = new JPanel(new FlowLayout(FlowLayout.LEFT,5,0));
    panel_3.setPreferredSize(new Dimension(51, ESPACAMENTO));
    jLabelNomeDescritor.setText("Título * :");
    JLabel espaco2 = new JLabel("          ");
    jTextFieldNomeDescritor.setText("");
    panel_3.add(jLabelNomeDescritor);
    panel_3.add(espaco2);
    panel_3.add(jTextFieldNomeDescritor);
    c.gridx = 0;
    c.gridy = 6;
    gridbag.setConstraints(panel_3, c);
    panel.add ( panel_3);

    JPanel panel_5 = new JPanel(new FlowLayout(FlowLayout.CENTER));

    jButtonInserirDescritor.setText("Inserir");
    jButtonAlterarDescritor.setText("Alterar");
    jButtonAlterarDescritor.setEnabled(false);
    jButtonExcluir.setText("Excluir");
    jButtonExcluir.setEnabled(false);
    jButtonNovoDescritor.setText("Novo");
    jButtonNovoDescritor.setEnabled(false);
    jButtonAplicarRadicalizacao.setText("Aplicar Radicalização");
    jButtonAplicarRadicalizacao.setEnabled(false);
    jButtonFechar.setText("Fechar");
    jButtonFechar.setEnabled(true);
    panel_5.add(jButtonInserirDescritor);
    panel_5.add(jButtonAlterarDescritor);
    panel_5.add(jButtonExcluir);
    panel_5.add(jButtonNovoDescritor);
    panel_5.add(jButtonFechar);
    c.gridx = 0;
    c.gridy = 10;
    gridbag.setConstraints(panel_5, c);
    panel.add ( panel_5);

    JPanel panel_9 = new JPanel(new FlowLayout(FlowLayout.CENTER));
    jButtonTermosGenericos.setText("Termos Genéricos");
    jButtonTermosGenericos.setEnabled(false);
    jButtonTermosEspecificos.setText("Termos Específicos");
    jButtonTermosEspecificos.setEnabled(false);
    jButtonEquivalencias.setText("Equivalencias");
    jButtonEquivalencias.setEnabled(false);
    jButtonTermosRelacionados.setText("Termos Relacionados");
    jButtonTermosRelacionados.setEnabled(false);
    jButtonSubCategorias.setText("Sub Categorias");
    jButtonSubCategorias.setEnabled(false);
    panel_9.add(jButtonTermosGenericos);
    panel_9.add(jButtonTermosEspecificos);
    panel_9.add(jButtonEquivalencias);
    panel_9.add(jButtonTermosRelacionados);
    panel_9.add(jButtonSubCategorias);
    panel_9.add(jButtonAplicarRadicalizacao);
    c.gridx = 0;
    c.gridy = 20;
    gridbag.setConstraints(panel_9, c);
    panel.add ( panel_9);

    add(panel);

    jButtonInserirDescritor.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButtonInserirDescritor_actionPerformed(e);
      }
    });

    jButtonAlterarDescritor.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButtonAlterarDescritor_actionPerformed(e);
      }
    });

    jButtonExcluir.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButtonExcluir_actionPerformed(e);
      }
    });

    jButtonBuscarDescritor.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButtonBuscarDescritor_actionPerformed(e);
      }
    });

    jButtonNovoDescritor.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButtonNovoDescritor_actionPerformed(e);
      }
    });

    jButtonAplicarRadicalizacao.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(ActionEvent e) {
        	jButtonAplicarRadicalizacao_actionPerformed(e);
        }
      });
    
    jButtonFechar.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButtonFechar_actionPerformed(e);
      }
    });

  jButtonTermosGenericos.addActionListener(new java.awt.event.ActionListener() {
  public void actionPerformed(ActionEvent e) {
    jButtonTermosGenericos_actionPerformed(e);
  }
   });

 jButtonTermosEspecificos.addActionListener(new java.awt.event.ActionListener() {
   public void actionPerformed(ActionEvent e) {
	   jButtonTermosEspecificos_actionPerformed(e);
   }
 });
 jButtonEquivalencias.addActionListener(new java.awt.event.ActionListener() {
   public void actionPerformed(ActionEvent e) {
	   jButtonEquivalencias_actionPerformed(e);
   }
 });
 jButtonTermosRelacionados.addActionListener(new java.awt.event.ActionListener() {
   public void actionPerformed(ActionEvent e) {
	   jButtonTermosRelacionados_actionPerformed(e);
   }
 });
jButtonSubCategorias.addActionListener(new java.awt.event.ActionListener() {
	   public void actionPerformed(ActionEvent e) {
		   jButtonSubCategorias_actionPerformed(e);
	   }
});
 
 jTextFieldBuscaCodDescritor.addKeyListener(
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

  void carregaListasTermosGenericos () throws Exception{
      try{
      listaTermosGenericosAAssociar = cadastro.populaListaTermosGenericosAAssociar(Integer.parseInt(jLabelCodDescritor.getText()));
    } catch(Exception ex){
      MensagemErro ed = new MensagemErro();
      ed.setLabel(ex.getMessage());
      ed.setVisible(true);
    }
    try{
     listaTermosGenericosAssociados = cadastro.populaListaTermosGenericosAssociados(Integer.parseInt(jLabelCodDescritor.getText()));
   } catch(Exception ex){
       MensagemErro ed = new MensagemErro();
       ed.setLabel(ex.getMessage());
       ed.setVisible(true);
   }
  }

  void carregaListasTermosEspecificos () throws Exception{
      try{
      listaTermosEspecificosAAssociar = cadastro.populaListaTermosEspecificosAAssociar(Integer.parseInt(jLabelCodDescritor.getText()));
    } catch(Exception ex){
      MensagemErro ed = new MensagemErro();
      ed.setLabel(ex.getMessage());
      ed.setVisible(true);
    }
    try{
     listaTermosEspecificosAssociados = cadastro.populaListaTermosEspecificosAssociados(Integer.parseInt(jLabelCodDescritor.getText()));
   } catch(Exception ex){
       MensagemErro ed = new MensagemErro();
       ed.setLabel(ex.getMessage());
       ed.setVisible(true);
   }
  }

  void carregaListasEquivalencias () throws Exception{
      try{
      listaEquivalenciasAAssociar = cadastro.populaListaEquivalenciasAAssociar(Integer.parseInt(jLabelCodDescritor.getText()));
    } catch(Exception ex){
      MensagemErro ed = new MensagemErro();
      ed.setLabel(ex.getMessage());
      ed.setVisible(true);
    }
    try{
     listaEquivalenciasAssociadas = cadastro.populaListaEquivalenciasAssociadas(Integer.parseInt(jLabelCodDescritor.getText()));
   } catch(Exception ex){
       MensagemErro ed = new MensagemErro();
       ed.setLabel(ex.getMessage());
       ed.setVisible(true);
   }
  }

void carregaListasTermosRelacionados () throws Exception{
	try{
      listaTermosRelacionadosAAssociar = cadastro.populaListaTermosRelacionadosAAssociar(Integer.parseInt(jLabelCodDescritor.getText()));
    } catch(Exception ex){
      MensagemErro ed = new MensagemErro();
      ed.setLabel(ex.getMessage());
      ed.setVisible(true);
    }
    try{
     listaTermosRelacionadosAssociados = cadastro.populaListaTermosRelacionadosAssociados(Integer.parseInt(jLabelCodDescritor.getText()));
   } catch(Exception ex){
       MensagemErro ed = new MensagemErro();
       ed.setLabel(ex.getMessage());
       ed.setVisible(true);
   }
}

void carregaListasSubCategorias () throws Exception{
	try{
      listaSubCategoriasAAssociar = cadastro.populaListaSubCategoriasAAssociar(Integer.parseInt(jLabelCodDescritor.getText()));
    } catch(Exception ex){
      MensagemErro ed = new MensagemErro();
      ed.setLabel(ex.getMessage());
      ed.setVisible(true);
    }
    try{
     listaSubCategoriasAssociadas = cadastro.populaListaSubCategoriasAssociadas(Integer.parseInt(jLabelCodDescritor.getText()));
   } catch(Exception ex){
       MensagemErro ed = new MensagemErro();
       ed.setLabel(ex.getMessage());
       ed.setVisible(true);
   }
}
  void jButtonInserirDescritor_actionPerformed(ActionEvent e) {
    if (validarCampos() == 0){
      try {
        int lintCodDescritor;
        lintCodDescritor = cadastro.inserirDescritor(jTextFieldNomeDescritor.
            getText());
        jLabelCodDescritor.setText("" + lintCodDescritor);
        habilitarBotoes(false);
        JOptionPane.showMessageDialog(jPanelDescritor,
                                      "O Descritor foi inserido com sucesso.",
                                      "Atenção", JOptionPane.WARNING_MESSAGE);
      }
      catch (Exception ex) {
        MensagemErro ed = new MensagemErro();
        ed.setLabel(ex.getMessage());
        ed.setVisible(true);
      }
    }
  }

  void jButtonBuscarDescritor_actionPerformed(ActionEvent e) {
    if (jTextFieldBuscaCodDescritor.getText().toString().equalsIgnoreCase("")) {
      JOptionPane.showMessageDialog(jPanelDescritor,
                                    "Campo de Busca não informado.",
                                    "Atenção", JOptionPane.WARNING_MESSAGE);

    }
    else {
      try {
        limparComponentes();
        descritor = cadastro.buscarDescritor(Integer.parseInt(
            jTextFieldBuscaCodDescritor.getText()));

        if (descritor != null) {
          jTextFieldNomeDescritor.setText(descritor.getDescricao());
          jLabelCodDescritor.setText(jTextFieldBuscaCodDescritor.getText());
          jTextFieldBuscaCodDescritor.setText("");
          habilitarBotoes(false);
        }
        else {
          JOptionPane.showMessageDialog(jPanelDescritor,
                                        "Nenhum registro encontrado.",
                                        "Atenção", JOptionPane.WARNING_MESSAGE);
          limparComponentes();
        }

      }
      catch (Exception ex) {
        MensagemErro ed = new MensagemErro();
        ed.setLabel(ex.getMessage());
        ed.setVisible(true);
      }
    }
  }

  void jButtonAlterarDescritor_actionPerformed(ActionEvent e) {
    if (validarCampos() == 0){
      try{
       cadastro.alterarDescritor(Integer.parseInt(jLabelCodDescritor.getText()), jTextFieldNomeDescritor.getText());
       JOptionPane.showMessageDialog(jPanelDescritor, "O Descritor foi alterado com sucesso.", "Atenção", JOptionPane.WARNING_MESSAGE);
      } catch(Exception ex){
          MensagemErro ed = new MensagemErro();
          ed.setLabel(ex.getMessage());
          ed.setVisible(true);
      }
    }
  }

  void jButtonNovoDescritor_actionPerformed(ActionEvent e) {
    habilitarBotoes(true);
    limparComponentes();
  }

  void jButtonAplicarRadicalizacao_actionPerformed(ActionEvent e) {
      try{
    	  cadastro.aplicarRadicalizacao();
          JOptionPane.showMessageDialog(jPanelDescritor, "Rotina de radicalização efetuada com sucesso.", "Atenção", JOptionPane.WARNING_MESSAGE);
         } catch(Exception ex){
             MensagemErro ed = new MensagemErro();
             ed.setLabel(ex.getMessage());
             ed.setVisible(true);
         }
  }
  
  void jButtonFechar_actionPerformed(ActionEvent e) {
     observador.fechar();
  }


  private void limparComponentes(){
     jLabelCodDescritor.setText("");
     jTextFieldNomeDescritor.setText("");
  }

  private int validarCampos(){
    int retorno = 0;

    if (jTextFieldNomeDescritor.getText().toString().equals(""))
    {
      JOptionPane.showMessageDialog(jPanelDescritor, "Campos de preenchimento obrigatório não podem ser nulos", "Atenção", JOptionPane.WARNING_MESSAGE);
      retorno = 1;
    }
    return retorno;
  }

  private void habilitarBotoes(boolean aboEstado){
      jButtonNovoDescritor.setEnabled(!aboEstado);
      jButtonInserirDescritor.setEnabled(aboEstado) ;
      jButtonAlterarDescritor.setEnabled(!aboEstado) ;
      jButtonTermosGenericos.setEnabled(!aboEstado) ;
      jButtonTermosEspecificos.setEnabled(!aboEstado) ;
      jButtonEquivalencias.setEnabled(!aboEstado) ;
      jButtonTermosRelacionados.setEnabled(!aboEstado) ;
      jButtonSubCategorias.setEnabled(!aboEstado) ;
      jButtonAplicarRadicalizacao.setEnabled(!aboEstado) ;
      jButtonExcluir.setEnabled(!aboEstado);
  }

  void jButtonExcluir_actionPerformed(ActionEvent e) {
    try{
    if (JOptionPane.showConfirmDialog(jPanelDescritor ,"Deseja realmente excluir? "," Excluir Descritor ",JOptionPane.YES_NO_OPTION) == 0) {
      cadastro.excluirDescritor(Integer.parseInt(jLabelCodDescritor.getText()));
      habilitarBotoes(true);
      limparComponentes();
      JOptionPane.showMessageDialog(jPanelDescritor, "O Descritor foi excluído com sucesso.", "Atenção", JOptionPane.WARNING_MESSAGE);
    }
   } catch(Exception ex){
        MensagemErro ed = new MensagemErro();
        ed.setLabel(ex.getMessage());
        ed.setVisible(true);
   }

  }

  void jButtonTermosGenericos_actionPerformed(ActionEvent e) {
    final Frame frame = new Frame();
    try{
      carregaListasTermosGenericos();
    }
    catch (Exception ex){
      MensagemErro ed = new MensagemErro();
      ed.setLabel(ex.getMessage());
      ed.setVisible(true);
    }
     final AssociaDados sobre = new AssociaDados(frame,"Associação de Dados", listaTermosGenericosAAssociar, listaTermosGenericosAssociados, "Termos Genericos a Associar", "Termos Genericos Associados");
     sobre.addListener(
     new ObserverAdapterFechar()
      {
        public void fechar()
        {
          listaTermosGenericosAAssociar = sobre.getLista1();
          listaTermosGenericosAssociados = sobre.getLista2();
          frame.dispose();
          try{
            cadastro.inserirDescritorTermosGenericos(listaTermosGenericosAssociados);
          }
          catch (Exception ex){
            MensagemErro ed = new MensagemErro();
            ed.setLabel(ex.getMessage());
            ed.setVisible(true);
          }
        }
      }
    );
    sobre.setVisible(true);
}

  void jButtonTermosEspecificos_actionPerformed(ActionEvent e) {
	    final Frame frame = new Frame();
	    try{
	      carregaListasTermosEspecificos();
	    }
	    catch (Exception ex){
	      MensagemErro ed = new MensagemErro();
	      ed.setLabel(ex.getMessage());
	      ed.setVisible(true);
	    }
	     final AssociaDados sobre = new AssociaDados(frame,"Associação de Dados", listaTermosEspecificosAAssociar, listaTermosEspecificosAssociados, "Termos Específicos a Associar", "Termos Específicos Associados");
	     sobre.addListener(
	     new ObserverAdapterFechar()
	      {
	        public void fechar()
	        {
	          listaTermosEspecificosAAssociar = sobre.getLista1();
	          listaTermosEspecificosAssociados = sobre.getLista2();
	          frame.dispose();
	          try{
	            cadastro.inserirDescritorTermosEspecificos(listaTermosEspecificosAssociados);
	          }
	          catch (Exception ex){
	            MensagemErro ed = new MensagemErro();
	            ed.setLabel(ex.getMessage());
	            ed.setVisible(true);
	          }
	        }
	      }
	    );
	    sobre.setVisible(true);
}
  
  void jButtonEquivalencias_actionPerformed(ActionEvent e) {
	    final Frame frame = new Frame();
	    try{
	      carregaListasEquivalencias();
	    }
	    catch (Exception ex){
	      MensagemErro ed = new MensagemErro();
	      ed.setLabel(ex.getMessage());
	      ed.setVisible(true);
	    }
	     final AssociaDados sobre = new AssociaDados(frame,"Associação de Dados", listaEquivalenciasAAssociar, listaEquivalenciasAssociadas, "Equivalencias a Associar", "Equivalencias Associadas");
	     sobre.addListener(
	     new ObserverAdapterFechar()
	      {
	        public void fechar()
	        {
	          listaEquivalenciasAAssociar = sobre.getLista1();
	          listaEquivalenciasAssociadas = sobre.getLista2();
	          frame.dispose();
	          try{
	            cadastro.inserirDescritorEquivalencias(listaEquivalenciasAssociadas);
	          }
	          catch (Exception ex){
	            MensagemErro ed = new MensagemErro();
	            ed.setLabel(ex.getMessage());
	            ed.setVisible(true);
	          }
	        }
	      }
	    );
	    sobre.setVisible(true);
}
  
 void jButtonTermosRelacionados_actionPerformed(ActionEvent e) {
	    final Frame frame = new Frame();
	    try{
	      carregaListasTermosRelacionados();
	    }
	    catch (Exception ex){
	      MensagemErro ed = new MensagemErro();
	      ed.setLabel(ex.getMessage());
	      ed.setVisible(true);
	    }
	     final AssociaDados sobre = new AssociaDados(frame,"Associação de Dados", listaTermosRelacionadosAAssociar, listaTermosRelacionadosAssociados, "Termos Relacionados a Associar", "Termos Relacionados Associados");
	     sobre.addListener(
	     new ObserverAdapterFechar()
	      {
	        public void fechar()
	        {
	          listaTermosRelacionadosAAssociar = sobre.getLista1();
	          listaTermosRelacionadosAssociados= sobre.getLista2();
	          frame.dispose();
	          try{
	            cadastro.inserirDescritorTermosRelacionados(listaTermosRelacionadosAssociados);
	          }
	          catch (Exception ex){
	            MensagemErro ed = new MensagemErro();
	            ed.setLabel(ex.getMessage());
	            ed.setVisible(true);
	          }
	        }
	      }
	    );
	    sobre.setVisible(true);
}
 
 void jButtonSubCategorias_actionPerformed(ActionEvent e) {
	 final Frame frame = new Frame();
	 try{
		 carregaListasSubCategorias();
	 }
	 catch (Exception ex){
		 MensagemErro ed = new MensagemErro();
	     ed.setLabel(ex.getMessage());
	     ed.setVisible(true);
	 }
	 final AssociaDados sobre = new AssociaDados(frame,"Associação de Dados", listaSubCategoriasAAssociar, listaSubCategoriasAssociadas, "Sub Categorias a Associar", "Sub Categorias Associadas");
	 sobre.addListener(
	 new ObserverAdapterFechar()
	 {
		 public void fechar()
	     {
			 listaSubCategoriasAAssociar = sobre.getLista1();
	         listaSubCategoriasAssociadas = sobre.getLista2();
	          frame.dispose();
	          try{
	            cadastro.inserirDescritorSubCategorias(listaSubCategoriasAssociadas);
	          }
	          catch (Exception ex){
	            MensagemErro ed = new MensagemErro();
	            ed.setLabel(ex.getMessage());
	            ed.setVisible(true);
	          }
	        }
	      }
	    );
	    sobre.setVisible(true);
	}
 public void addListener(IObserverFechar observer){
    observador = observer;
  }

}