/*
 * Título:       ConfParametro.java
 * Descrição:    Interface de Parametrização da Ferramenta
 * Assunto:      Dissertacao do Mestrado em Sistemas e Computação
 * Orientador:   Manoel Mendonça
 * @author       Cristiane Costa Magalhães
 * Data       :  Ago/2007
 * @version 1.0
 */

package desktop;

import javax.swing.*;

import negocio.Configuracoes;


import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.sql.SQLException;

public class ConfParametro extends JPanel
{
	
	private IObserverFechar observador;
	private JRadioButton opPorter, opOrengo, opLevenshtein, opCoseno, opDistanciaEuclideana, opJaroWinkler;
    private JTextField jTextFieldValorPercentualCompString = new JTextField(3);
 
	Configuracoes configuracoes = new Configuracoes();
	JPanel jPanelParametro = new JPanel(new FlowLayout());

	JButton jButtonFechar = new JButton();
	JButton jButtonSalvar = new JButton();

	public ConfParametro() {
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
		c.fill = GridBagConstraints.BOTH;


		JPanel panelAlgoritmoRadicalizacao = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelAlgoritmoRadicalizacao.setBorder(BorderFactory.createTitledBorder("Algoritomo de Radicalização"));
		opOrengo = new JRadioButton("Stemmer Portuguese (RSLP)",true);
		opPorter = new JRadioButton("Porter Stemming",false);
		panelAlgoritmoRadicalizacao.add(opOrengo);
		panelAlgoritmoRadicalizacao.add(opPorter);
		c.gridx = 0;
		c.gridy = 0;
		gridbag.setConstraints(panelAlgoritmoRadicalizacao, c);
		
		//Group the radio buttons.
	    ButtonGroup groupX = new ButtonGroup();
	    groupX.add(opPorter);
	    groupX.add(opOrengo);
	    panel.add (panelAlgoritmoRadicalizacao);


	    //Panel do Algoritmo de Comparação	
		JPanel panelAlgoritmoCompString = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelAlgoritmoCompString.setBorder(BorderFactory.createTitledBorder("Algoritmo de Comparação entre Strings"));
		opLevenshtein = new JRadioButton("Levenshtein",true);
		panelAlgoritmoCompString.add(opLevenshtein);
		opCoseno = new JRadioButton("Coseno",false);
		panelAlgoritmoCompString.add(opCoseno);
		opDistanciaEuclideana = new JRadioButton("Distância Euclideana",true);
		panelAlgoritmoCompString.add(opDistanciaEuclideana);
		opJaroWinkler = new JRadioButton("Jaro Winkler",false);
		panelAlgoritmoCompString.add(opJaroWinkler);

	    //Group the radio buttons.
	    ButtonGroup groupY = new ButtonGroup();
	    groupY.add(opLevenshtein);
	    groupY.add(opCoseno);
	    groupY.add(opDistanciaEuclideana);
	    groupY.add(opJaroWinkler);
	    panel.add (panelAlgoritmoCompString);
	    
		c.gridx = 0;
		c.gridy = 20;
		gridbag.setConstraints(panelAlgoritmoCompString, c);
	    
		JPanel panelCompString = new JPanel(new FlowLayout(FlowLayout.LEFT,5,0));
		panelCompString.setBorder(BorderFactory.createTitledBorder(""));
	    JLabel jLabelValorPercentualCompString = new JLabel();
	    jLabelValorPercentualCompString.setText("Percentual de Comparação entre Strings: ");
	    jTextFieldValorPercentualCompString.setText("");
	    
	    panelCompString.add (jLabelValorPercentualCompString);
	    panelCompString.add (jTextFieldValorPercentualCompString);
		c.gridx = 0;
		c.gridy = 40;
		gridbag.setConstraints(panelCompString, c);

	    panel.add (panelCompString);
	    
		JPanel panel_button = new JPanel(new FlowLayout(FlowLayout.CENTER));

		jButtonSalvar.setText("Salvar");
		jButtonSalvar.setEnabled(true);
		panel_button.add(jButtonSalvar);
		
		jButtonFechar.setText("Fechar");
		jButtonFechar.setEnabled(true);
		panel_button.add(jButtonFechar);
		
		c.gridx = 0;
		c.gridy = 60;
		gridbag.setConstraints(panel_button, c);
		panel.add (panel_button);

		add(panel);

		jButtonFechar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonFechar_actionPerformed(e);
			}
		});

		jButtonSalvar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonSalvar_actionPerformed(e);
			}
		});
		
		this.carregaDados();
		
	}	    

	public static boolean isSelected(JRadioButton btn) {
		DefaultButtonModel model = (DefaultButtonModel)btn.getModel();
		return model.getGroup().isSelected(model);
	}
	
	public void addListener(IObserverFechar observer){
		observador = observer;
	}

	void jButtonFechar_actionPerformed(ActionEvent e) {
		observador.fechar();
	}

	void jButtonSalvar_actionPerformed(ActionEvent e) 
	{
	int codAlgoritmoRadicalizacao = 0;	
	int codAlgoritmoCompString = 0;
	int valPercCompString = 0;
	
	    if (isSelected(opOrengo))
        {
	    	codAlgoritmoRadicalizacao = 1;                	
        }
	    if (isSelected(opPorter))
        {
	    	codAlgoritmoRadicalizacao = 2;                	
        }
	    if (isSelected(opLevenshtein))
        {
	    	codAlgoritmoCompString = 3;                	
        }
	    if (isSelected(opCoseno))
        {
	    	codAlgoritmoCompString = 4;                	
        }
	    if (isSelected(opDistanciaEuclideana))
        {
	    	codAlgoritmoCompString = 5;                	
        }
	    if (isSelected(opJaroWinkler))
        {
	    	codAlgoritmoCompString = 6;                	
        }
	    validarCampos();
	    valPercCompString = Integer.parseInt(jTextFieldValorPercentualCompString.getText().toString());
	    try {
			configuracoes.salvarParametro(codAlgoritmoRadicalizacao, codAlgoritmoCompString, valPercCompString);
	    	JOptionPane.showMessageDialog(jPanelParametro,
	                                      "Os parâmetros foram alterados com sucesso.",
	                                      "Atenção", JOptionPane.WARNING_MESSAGE);
	      }
	      catch (Exception ex) {
	        MensagemErro ed = new MensagemErro();
	        ed.setLabel(ex.getMessage());
	        ed.setVisible(true);
	      }

	}
	void carregaDados ()throws SQLException
	{
		int codAlgoritmoClassificacao = 0;
		int codAlgoritmoCompString = 0;
		int valPercCompString = 0;
		
		try{
			codAlgoritmoClassificacao = configuracoes.consultaAlgoritmoClassificacao();
			codAlgoritmoCompString = configuracoes.consultaAlgoritmoCompString();
			valPercCompString = configuracoes.consultaValorPercentualCompString();
			
		}

		catch (Exception e) {
			throw new SQLException ("Erro ao carregar Parametros: " + e.getMessage());		
		}

		if (codAlgoritmoClassificacao == 1) {
			opOrengo.setSelected(true);
		}
		if (codAlgoritmoClassificacao == 2) {
			opPorter.setSelected(true);
		}
		if (codAlgoritmoCompString == 3) {
			opLevenshtein.setSelected(true);
		}
		if (codAlgoritmoCompString == 4) {
			opCoseno.setSelected(true);
		}
		if (codAlgoritmoCompString == 5) {
			opDistanciaEuclideana.setSelected(true);
		}
		if (codAlgoritmoCompString == 6) {
			opJaroWinkler.setSelected(true);
		}
		jTextFieldValorPercentualCompString.setText(Integer.toString(valPercCompString));
	}
	
	  private int validarCampos(){
		    int retorno = 0;

		    if ((jTextFieldValorPercentualCompString.getText().toString().equals("")))
		    {
		      JOptionPane.showMessageDialog(jPanelParametro, "Campos de preenchimento obrigatório não podem ser nulos", "Atenção", JOptionPane.WARNING_MESSAGE);
		      retorno = 1;
		    }
		    return retorno;
		  }
}
