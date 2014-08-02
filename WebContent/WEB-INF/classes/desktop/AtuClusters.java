/* Título:       AtuClusters.java
 * Descrição:    Interface Visual para atualização dos grupos de documentos
 * Assunto:      Dissertacao do Mestrado em Sistemas e Computação
 * Orientador:   Manoel Mendonça
 * @author       Cristiane Costa Magalhães
 * Data       :  Jan/2008
 * @version 1.0
 */

package desktop;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JCheckBox;


import java.awt.event.ActionEvent;
import java.beans.*;
import java.awt.*;
import javax.swing.*;

import bancoDados.AcessoBanco;


import negocio.*;

public class AtuClusters extends JPanel implements PropertyChangeListener 
{
	JPanel panelAtuDocumentoBase= new JPanel();
	JLabel jLabelTextoExplicacao,jLabelQtdGrupos = new JLabel();
	JTextField jTextFieldQtdGrupos = new JTextField(3);

	JButton jButtonAtualizar = null; 
	JButton jButtonFechar = null; 

	private ProgressMonitor progressMonitor;

	private Task task;
	final static int ONE_SECOND = 60;

	private JCheckBox opAdministrativo, opAmbiental, opComercial, opConsumidor, opConstitucional,
	opCivel, opEconomicoFinanceiro, opEleitoral, opInternacionalPrivado, opMaritimo, opInternacionalPublico,
	opProcessualCivil, opPenal, opProcessualPenal, opPrevidenciario, opTributario, opPropriedadeIntelectual, opTodos;

	private IObserverFechar observador;

	private int counterCategoria, totalArquivos;

	class Task extends SwingWorker<Void, Void> 
	{
		/*
		 * Main task. Executed in background thread.
		 */
		@Override
		public Void doInBackground() {
				this.atualizaClusters();
			return null;
		}

		/*
		 * Executed in event dispatching thread
		 */
		@Override
		public void done() {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(panelAtuDocumentoBase,
					"Rotina concluída com sucesso",
					"Atualização de Grupos de Documentos", JOptionPane.OK_OPTION);
			jButtonAtualizar.setEnabled(true);
			jButtonFechar.setEnabled(true);
			setCursor(null); //turn off the wait cursor

		}

		public void atualizaClusters ()
		{
			ArrayList <Integer>arlCategorias = new ArrayList<Integer> ();
			RepositorioTextos repositorioTextos = new RepositorioTextos();
	
			float counter =0;
			progressMonitor.setProgress(0);
			setProgress(0);	

			try{
				if (this.validarCampos())
				{
					arlCategorias = this.getCategoriasJuridicas();
					counterCategoria = 0;
					totalArquivos = arlCategorias.size();
					for (int i = 0; i < arlCategorias.size(); i++)
					{
						counterCategoria++;
						repositorioTextos.atualizaClusters(Integer.parseInt(jTextFieldQtdGrupos.getText()), arlCategorias.get(i));
						counter = ((i/arlCategorias.size())*100); 
						setProgress((int) counter);	
					}
				}
			}
			catch(Exception ex)
			{
				MensagemErro ed = new MensagemErro();
				ed.setLabel(ex.getMessage());
				ed.setVisible(true);
			}
		}	

		private boolean validarCampos ()
		{
			boolean retorno = true;
			if (!((opTodos.isSelected())||(opAdministrativo.isSelected())|| (opAmbiental.isSelected()) ||
			(opComercial.isSelected()) || (opConsumidor.isSelected()) || (opConstitucional.isSelected())||
			(opCivel.isSelected())|| (opEconomicoFinanceiro.isSelected()) || (opEleitoral.isSelected()) ||
			(opInternacionalPrivado.isSelected()) || (opMaritimo.isSelected()) || (opInternacionalPublico.isSelected())||
			(opProcessualCivil.isSelected()) || (opPenal.isSelected()) || (opProcessualPenal.isSelected()) ||
			(opPrevidenciario.isSelected()) || (opTributario.isSelected()) || (opPropriedadeIntelectual.isSelected())))
			{
				JOptionPane.showMessageDialog(panelAtuDocumentoBase,
						"Selecione pelo menos uma categoria jurídica.",
						"Atenção", JOptionPane.WARNING_MESSAGE);
				retorno = false;

			}
			if ((jTextFieldQtdGrupos.getText().toString().equals("")))
			{
				JOptionPane.showMessageDialog(panelAtuDocumentoBase, "Indique a quantidade de subgrupos que deseja criar dentro dos grupos selecionados", "Atenção", JOptionPane.WARNING_MESSAGE);
				jTextFieldQtdGrupos.setFocusable(true);	
				retorno = false;
			}
			return retorno;
		}

		public ArrayList getCategoriasJuridicas ()
		{
			ArrayList<Integer> arlCategorias = new ArrayList<Integer>();
			if (opTodos.isSelected())
			{
				for (int i=1; i<18; i++){
					arlCategorias.add(i);
				}
			}
			//Pega o número do cluster 
			if (opAdministrativo.isSelected())
			{
				arlCategorias.add(1);                	
			}
			if (opAmbiental.isSelected())
			{
				arlCategorias.add(2);                	
			}
			if (opComercial.isSelected())
			{
				arlCategorias.add(3);                	
			}
			if (opConsumidor.isSelected())
			{
				arlCategorias.add(4);                	
			}
			if (opConstitucional.isSelected())
			{
				arlCategorias.add(5);                	
			}
			if (opCivel.isSelected())
			{
				arlCategorias.add(6);                	
			}
			if (opEconomicoFinanceiro.isSelected())
			{
				arlCategorias.add(7);                	
			}
			if (opEleitoral.isSelected())
			{
				arlCategorias.add(8);                	
			}
			if (opInternacionalPrivado.isSelected())
			{
				arlCategorias.add(9);                	
			}
			if (opMaritimo.isSelected())
			{
				arlCategorias.add(10);                	
			}
			if (opInternacionalPublico.isSelected())
			{
				arlCategorias.add(11);                	
			}
			if (opProcessualCivil.isSelected())
			{
				arlCategorias.add(12);                	
			}
			if (opPenal.isSelected())
			{
				arlCategorias.add(13);                	
			}
			if (opProcessualPenal.isSelected())
			{
				arlCategorias.add(14);                	
			}
			if (opPrevidenciario.isSelected())
			{
				arlCategorias.add(15);                	
			}
			if (opTributario.isSelected())
			{
				arlCategorias.add(16);                	
			}
			if (opPropriedadeIntelectual.isSelected())
			{
				arlCategorias.add(17);                	
			}
			return (arlCategorias);

		}
	}	
		/**
		 * Invoked when task's progress property changes.
		 */
		public void propertyChange(PropertyChangeEvent evt) 
		{
			if ("progress" == evt.getPropertyName()) 
			{
				int progress = (Integer) evt.getNewValue();
				progressMonitor.setProgress(progress);
				String message =
					String.format("Completando Rotina %d de %d - %d%%.\n",counterCategoria, totalArquivos, progress);
				progressMonitor.setNote(message);
				if (progressMonitor.isCanceled()) {
					task.cancel(true);
				} 

			}
		}	
		
	
	public AtuClusters ()
	{
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
		panelAtuDocumentoBase.setLayout(gridbag);
		c.fill = GridBagConstraints.BOTH;

		JPanel explicacaoArquivo = new JPanel(new FlowLayout(FlowLayout.LEFT));
		jLabelTextoExplicacao = new JLabel ();
		jLabelTextoExplicacao.setText("Selecione os grupos de documentos - representados pelas áreas jurídicas - que deseja reagrupar.");
		explicacaoArquivo.add(jLabelTextoExplicacao);
		c.gridx = 0;
		c.gridy = 0;
		gridbag.setConstraints(explicacaoArquivo, c);
		panelAtuDocumentoBase.add (explicacaoArquivo);

		JPanel panelCategoria = new JPanel(new GridLayout(0,4));
		panelCategoria.setBorder(BorderFactory.createTitledBorder("Áreas Jurídicas"));

		opAdministrativo = new JCheckBox("Administrativo",false);
		panelCategoria.add(opAdministrativo);
		opAmbiental = new JCheckBox("Ambiental",false);
		panelCategoria.add(opAmbiental);
		opComercial = new JCheckBox("Comercial",false);
		panelCategoria.add(opComercial);
		opConsumidor = new JCheckBox("Consumidor",false);
		panelCategoria.add(opConsumidor);
		opConstitucional = new JCheckBox("Constitucional",false);
		panelCategoria.add(opConstitucional);

		opCivel = new JCheckBox("Cível",false);
		panelCategoria.add(opCivel);
		opEconomicoFinanceiro = new JCheckBox("Econômico Financeiro",false);
		panelCategoria.add(opEconomicoFinanceiro);
		opEleitoral = new JCheckBox("Eleitoral",false);
		panelCategoria.add(opEleitoral);
		opInternacionalPrivado = new JCheckBox("Internacional Privado",false);
		panelCategoria.add(opInternacionalPrivado);
		opMaritimo = new JCheckBox("Marítimo",false);
		panelCategoria.add(opMaritimo);
		opInternacionalPublico = new JCheckBox("Internacional Público",false);
		panelCategoria.add(opInternacionalPublico);
		opProcessualCivil = new JCheckBox("Processual Civil",false);
		panelCategoria.add(opProcessualCivil);
		opPenal = new JCheckBox("Penal",false);
		panelCategoria.add(opPenal);
		opProcessualPenal = new JCheckBox("Processual Penal",false);
		panelCategoria.add(opProcessualPenal);
		opPrevidenciario = new JCheckBox("Previdenciário",false);
		panelCategoria.add(opPrevidenciario);
		opTributario = new JCheckBox("Tributario",false);
		panelCategoria.add(opTributario);
		opPropriedadeIntelectual = new JCheckBox("Propriedade Intelectual",false);
		panelCategoria.add(opPropriedadeIntelectual);
		opTodos = new JCheckBox("Todas as Áreas",false);
		Font fonteTodos = new Font ("Arial", Font.BOLD, 12);
		opTodos.setFont(fonteTodos);
		panelCategoria.add(opTodos);
		
		opTodos.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (opTodos.isSelected()){
					if (opAdministrativo.isEnabled())
					{
						opAdministrativo.setSelected(true);
						}
					if (opAmbiental.isEnabled())
					{
						opAmbiental.setSelected(true);
						}
					if (opComercial.isEnabled())
					{
						opComercial.setSelected(true);
						}
					if (opConsumidor.isEnabled())
					{
						opConsumidor.setSelected(true);
						}
					if (opConstitucional.isEnabled())
					{
						opConstitucional.setSelected(true);
						}
					if (opCivel.isEnabled())
					{
						opCivel.setSelected(true);
						}
					if (opEconomicoFinanceiro.isEnabled())
					{
						opEconomicoFinanceiro.setSelected(true);
						}
					if (opEleitoral.isEnabled())
					{
						opEleitoral.setSelected(true);
						}
					if (opInternacionalPrivado.isEnabled())
					{
						opInternacionalPrivado.setSelected(true);
						}
					if (opMaritimo.isEnabled())
					{
						opMaritimo.setSelected(true);
						}
					if (opInternacionalPublico.isEnabled())
					{
						opInternacionalPublico.setSelected(true);
						}
					if (opProcessualCivil.isEnabled())
					{
						opProcessualCivil.setSelected(true);
						}
					if (opPenal.isEnabled())
					{
						opPenal.setSelected(true);
						}
					if (opProcessualPenal.isEnabled())
					{
						opProcessualPenal.setSelected(true);
						}
					if (opPrevidenciario.isEnabled())
					{
						opPrevidenciario.setSelected(true);
						}
					if (opTributario.isEnabled())
					{
						opTributario.setSelected(true);
						}
					if (opPropriedadeIntelectual.isEnabled())
					{
						opPropriedadeIntelectual.setSelected(true);
						}
				}
				else{
					if (opAdministrativo.isEnabled())
					{
						opAdministrativo.setSelected(false);
						}
					if (opAmbiental.isEnabled())
					{
						opAmbiental.setSelected(false);
						}
					if (opComercial.isEnabled())
					{
						opComercial.setSelected(false);
						}
					if (opConsumidor.isEnabled())
					{
						opConsumidor.setSelected(false);
						}
					if (opConstitucional.isEnabled())
					{
						opConstitucional.setSelected(false);
						}
					if (opCivel.isEnabled())
					{
						opCivel.setSelected(false);
						}
					if (opEconomicoFinanceiro.isEnabled())
					{
						opEconomicoFinanceiro.setSelected(false);
						}
					if (opEleitoral.isEnabled())
					{
						opEleitoral.setSelected(false);
						}
					if (opInternacionalPrivado.isEnabled())
					{
						opInternacionalPrivado.setSelected(false);
						}
					if (opMaritimo.isEnabled())
					{
						opMaritimo.setSelected(false);
						}
					if (opInternacionalPublico.isEnabled())
					{
						opInternacionalPublico.setSelected(false);
						}
					if (opProcessualCivil.isEnabled())
					{
						opProcessualCivil.setSelected(false);
						}
					if (opPenal.isEnabled())
					{
						opPenal.setSelected(false);
						}
					if (opProcessualPenal.isEnabled())
					{
						opProcessualPenal.setSelected(false);
						}
					if (opPrevidenciario.isEnabled())
					{
						opPrevidenciario.setSelected(false);
						}
					if (opTributario.isEnabled())
					{
						opTributario.setSelected(false);
						}
					if (opPropriedadeIntelectual.isEnabled())
					{
						opPropriedadeIntelectual.setSelected(false);
						}
				}
				   
			
			}});

		
		panelAtuDocumentoBase.add (panelCategoria);
		c.gridx = 0;
		c.gridy = 20;
		gridbag.setConstraints(panelCategoria, c);

		JPanel panel_1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		jLabelQtdGrupos.setText("Quantidade de subgrupos de documentos:");
		jTextFieldQtdGrupos.setText("");
		panel_1.add(jLabelQtdGrupos);
		panel_1.add(jTextFieldQtdGrupos);
		panel_1.setBorder(BorderFactory.createTitledBorder(""));

		c.gridx = 0;
		c.gridy = 40;
		gridbag.setConstraints(panel_1, c);
		panelAtuDocumentoBase.add ( panel_1);


		JPanel panel_3 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		jButtonAtualizar = new JButton ("Atualizar grupos e subgrupos de documentos");
		jButtonAtualizar.setEnabled(true);
		jButtonFechar = new JButton ("Fechar");
		jButtonFechar.setEnabled(true);
		panel_3.add(jButtonAtualizar);
		panel_3.add(jButtonFechar);
		c.gridx = 0;
		c.gridy = 60;
		gridbag.setConstraints(panel_3, c);
		panelAtuDocumentoBase.add ( panel_3);

		add(panelAtuDocumentoBase);


		jButtonAtualizar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonAtualizar_actionPerformed(e);
			}
		});

		jButtonFechar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonFechar_actionPerformed(e);
			}
		});

		//Adiciona tudo ao Panel geral	
		add (panelAtuDocumentoBase);
		
		
		habilitarCheckbox();
		
	}


	void jButtonAtualizar_actionPerformed(ActionEvent e) 
	{
		jButtonAtualizar.setEnabled (false);
		jButtonFechar.setEnabled(false);
		progressMonitor = new ProgressMonitor (AtuClusters.this,
				"Aguarde - Rotina em Processamento",
				"", 0, 100);
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		task = new Task();
		task.addPropertyChangeListener(this);
		task.execute();
	}


	void jButtonFechar_actionPerformed(ActionEvent e) {
		observador.fechar();
	}	  

	public void addListener(IObserverFechar observer){
		observador = observer;
	}

	private void habilitarCheckbox (){

		if (! existsCluster (1))	
			opAdministrativo.setEnabled(false);

		if (! existsCluster (2))	
		   opAmbiental.setEnabled(false);

		if (! existsCluster (3))	
			opComercial.setEnabled(false);

		if (! existsCluster (4))	
	        opConsumidor.setEnabled(false);

	              	
		if (! existsCluster (5))	
		  opConstitucional.setEnabled(false);
	 
		if (! existsCluster (6))	
		    opCivel.setEnabled(false);
		
		if (! existsCluster (7))	
		   opEconomicoFinanceiro.setEnabled(false);

		if (! existsCluster (8))	
	        opEleitoral.setEnabled(false);

		if (! existsCluster (9))	
	       opInternacionalPrivado.setEnabled(false);

		if (! existsCluster (10))	
	        opMaritimo.setEnabled(false);

		if (! existsCluster (11))	
		  opInternacionalPublico.setEnabled(false);	
		
		if (! existsCluster (12))	
	       opProcessualCivil.setEnabled(false);

		if (! existsCluster (13))	   
			 opPenal.setEnabled(false);

		if (! existsCluster (14))	
		  opProcessualPenal.setEnabled(false);

		if (! existsCluster (15))	
	       opPrevidenciario.setEnabled(false);
		
		if (! existsCluster (16))	
		   opTributario.setEnabled(false);
		
		if 		(! existsCluster (17))
		   opPropriedadeIntelectual.setEnabled(false);
		


		
	}
	
	private boolean existsCluster (int codCluster){
		
		try {
		AcessoBanco acessoBanco = new AcessoBanco();	

	    String lstrSQL = "SELECT count (*) from bagofwords where codcluster = " + codCluster;

		ResultSet rs = acessoBanco.getRS(lstrSQL);

		rs.next();
		boolean flag = (rs.getInt(1)>0);
		rs.close();
		return (flag);
		}
		catch(SQLException ex){
			try {
				throw new SQLException (ex.getMessage());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return (false); 
		
	}

}
