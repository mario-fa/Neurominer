/* Título:       AtuDocumentoBase.java
 * Descrição:    Interface Visual para atualização de documentos na base de dados
 * Assunto:      Dissertacao do Mestrado em Sistemas e Computação
 * Orientador:   Manoel Mendonça
 * @author       Cristiane Costa Magalhães
 * Data       :  Ago/2007
 * @version 1.0
 */

package desktop;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.io.File;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;


import javax.swing.BorderFactory;
import javax.swing.DefaultButtonModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;


import termsTreatment.Terms;
import textTreatment.textTreatment;
import uk.ac.shef.wit.simmetrics.similaritymetrics.AbstractStringMetric;
import uk.ac.shef.wit.simmetrics.similaritymetrics.CosineSimilarity;
import uk.ac.shef.wit.simmetrics.similaritymetrics.EuclideanDistance;
import uk.ac.shef.wit.simmetrics.similaritymetrics.JaroWinkler;
import uk.ac.shef.wit.simmetrics.similaritymetrics.Levenshtein;
import java.awt.event.ActionEvent;
import java.beans.*;
import java.awt.*;
import javax.swing.*;


import acessoDados.Arquivo.*;
import negocio.*;


//Conversor de Arquivo
import conversor.conversorDOC;
import conversor.conversorPDF;
import dados.Descritor;




public class AtuDocumentoBase extends JPanel implements PropertyChangeListener 
{
	JPanel panelAtuDocumentoBase= new JPanel();
	JButton jButtonSelecionarArquivos = null; 
	JButton jButtonAtualizar = null; 
	JButton jButtonFechar = null; 

	private ArrayList <String> listFileName = new ArrayList<String>();
	private ArrayList <String> listPathName = new ArrayList<String>();

	private ProgressMonitor progressMonitor;

	private Task task;
	final static int ONE_SECOND = 60;

	private JLabel jLabelTextoExplicacao = new JLabel();
	private JLabel jLabelTextoExplicacaoArquivo = new JLabel();
	private JLabel jLabelCodLabelArquivo = new JLabel();
	private JTextField jTextFieldArquivo = new JTextField(50);  

	private JRadioButton opAdministrativo, opAmbiental, opComercial, opConsumidor, opConstitucional,
	opCivel, opEconomicoFinanceiro, opEleitoral, opInternacionalPrivado, opMaritimo, opInternacionalPublico,
	opProcessualCivil, opPenal, opProcessualPenal, opPrevidenciario, opTributario, opPropriedadeIntelectual;
 
	
	private IObserverFechar observador;

	private int counterArquivo, totalArquivos;
	File [] fileName; 
	
	class Task extends SwingWorker<Void, Void> {
		/*
		 * Main task. Executed in background thread.
		 */
		@Override
		public Void doInBackground() {
			if (fileName.length > 0)
			{
				this.atualizaDocumentosBase(fileName);
			}
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
					"Atualização de Documentos", JOptionPane.OK_OPTION);
			jButtonSelecionarArquivos.setEnabled(true);
			jButtonAtualizar.setEnabled(true);
			jButtonFechar.setEnabled(true);
			setCursor(null); //turn off the wait cursor
			
		}

		public void atualizaDocumentosBase (File [] pathName)
		{
			String nameFile, path, caminhoOriginal="";
			ArrayList listFile = new ArrayList();
			Hashtable bagOfWordsTexto = new Hashtable();
			
			Hashtable sinonimo =  new Hashtable();

			String [][] bagOfWordsTotal = null;
			int codArquivo=0;			
			int numRowsBagOfWordsTotal = 0;
			int numColumns = 7;
			int numLinha = 0;
			int codAlgoritmoClassificacao = 0;
			int codCluster = 0;

			Configuracoes conf = new Configuracoes();
			RepositorioTextos repositorioTextos = new RepositorioTextos();
			
			//Pega o número do cluster 
		    if (isSelected(opAdministrativo))
	        {
		    	codCluster = 1;                	
	        }
		    if (isSelected(opAmbiental))
	        {
		    	codCluster = 2;                	
	        }
		    if (isSelected(opComercial))
	        {
		    	codCluster = 3;                	
	        }
		    if (isSelected(opConsumidor))
	        {
		    	codCluster = 4;                	
	        }
		    if (isSelected(opConstitucional))
	        {
		    	codCluster = 5;                	
	        }
		    if (isSelected(opCivel))
	        {
		    	codCluster = 6;                	
	        }
		    if (isSelected(opEconomicoFinanceiro))
	        {
		    	codCluster = 7;                	
	        }
		    if (isSelected(opEleitoral))
	        {
		    	codCluster = 8;                	
	        }
		    if (isSelected(opInternacionalPrivado))
	        {
		    	codCluster = 9;                	
	        }
		    if (isSelected(opMaritimo))
	        {
		    	codCluster = 10;                	
	        }
		    if (isSelected(opInternacionalPublico))
	        {
		    	codCluster = 11;                	
	        }
		    if (isSelected(opProcessualCivil))
	        {
		    	codCluster = 12;                	
	        }
		    if (isSelected(opPenal))
	        {
		    	codCluster = 13;                	
	        }
		    if (isSelected(opProcessualPenal))
	        {
		    	codCluster = 14;                	
	        }
		    if (isSelected(opPrevidenciario))
	        {
		    	codCluster = 15;                	
	        }
		    if (isSelected(opTributario))
	        {
		    	codCluster = 16;                	
	        }
		    if (isSelected(opPropriedadeIntelectual))
	        {
		    	codCluster = 17;                	
	        }

		    try{
				codArquivo = repositorioTextos.getCodigoArquivo();
				sinonimo= repositorioTextos.carregarSinominos();
				
				codAlgoritmoClassificacao = conf.consultaAlgoritmoClassificacao();
				if ((codAlgoritmoClassificacao!= 1) && (codAlgoritmoClassificacao != 2)) 
				{
					JOptionPane.showMessageDialog(panelAtuDocumentoBase,
							"Erro ao consultar dados de Parametro.",
							"Erro", JOptionPane.ERROR_MESSAGE);
				}
				else
				{
					buildListFile(pathName);
					textTreatment tt = new textTreatment();
					int countFile = listFileName.size();
					String conteudo = " ";
					
					counterArquivo =0; 
					totalArquivos = countFile;
					for (int i=0;i < countFile;++i)
					{
						++counterArquivo; 
						nameFile = listFileName.get(i).toString();
						path = listPathName.get(i).toString();
						
						//Chama o conversor de formato de arquivo
						File f = new File(path);						
						String caminho = f.getPath();
						caminhoOriginal = f.getPath();
						caminho = caminho.substring(0, caminho.lastIndexOf("."));
			            String extensao = path.substring(path.lastIndexOf("."), path.length());
			            
			            if (extensao.toUpperCase().compareTo(".PDF")==0){
			                conversorPDF convert = new conversorPDF();
			                convert.setEnderecoRecurso(nameFile);
			                conteudo = convert.getConteudo(f);
			                path = caminho + "_sinonimo.txt";

			                
			            }else if (extensao.toUpperCase().compareTo(".DOC")==0){
			                conversorDOC convert = new conversorDOC();
			                convert = new conversorDOC();
			                conteudo = convert.getConteudo(f);
			                path = caminho + "_sinonimo.txt";
			            }else {
			            	
			        		AccessFile inputFile = new AccessFile();
			        		try
			        		{
			        			path = caminho + ".txt";
			        			inputFile.OpenFile(path);
			        			String strValue = inputFile.read();
			        			while (strValue != null)
			        			{
				        			conteudo = conteudo +  strValue + " ";
			        				strValue = inputFile.read();
			        			}  
			        			path = caminho + "_sinonimo.txt";

			        		}
			        		catch (Exception exception)
			        		{
			        			System.out.println("Erro de Arquivo") ;
			        		}			            	
			            }
			            
			            //Rotina para substituir por sinonimos
			            conteudo = conteudo.toUpperCase();
						for (Enumeration e2 = sinonimo.keys(); e2.hasMoreElements();) 
							
						{
							String key = (String) e2.nextElement();
							String descritor = (String) sinonimo.get(key);
							key = "\\b" + key + "\\b";							
							conteudo = conteudo.replaceAll(key, descritor);
						}
					
			            
			            PrintWriter fw = new PrintWriter(path);
			            fw.print(conteudo);
			            fw.close();
			            
						listFile = tt.readFile(path);
						if (codAlgoritmoClassificacao ==1)
						{
							bagOfWordsTexto = tt.extractBagOfWordsByRlsp(listFile);
						}
						if (codAlgoritmoClassificacao ==2)
						{
							bagOfWordsTexto = tt.extractBagOfWordsByPorter(listFile);
						}	


						//Aplica o Tesaurus
						Hashtable <Integer, String> bagOfWordsTesaurus = new Hashtable<Integer, String>();
						Hashtable <String, Hashtable> bagOfWordsGeralTexto = new Hashtable<String, Hashtable>();
						Hashtable <String, Hashtable> bagOfWordsGeralTesaurus = new Hashtable<String, Hashtable>();
						Hashtable <String, String> bagOfWordsArquivo = new Hashtable<String, String>();
						
						bagOfWordsTesaurus = this.applyTesaurus(bagOfWordsTexto);
						bagOfWordsGeralTexto.put(nameFile, bagOfWordsTexto);
						bagOfWordsGeralTesaurus.put(nameFile, bagOfWordsTesaurus);
						bagOfWordsArquivo.put(nameFile, caminhoOriginal);

						numRowsBagOfWordsTotal = numRowsBagOfWordsTotal + bagOfWordsTesaurus.size();

						// INICIO
						//Cria o String [][] com o tamanho de linhas calculado no loop acima
						bagOfWordsTotal = new String [numRowsBagOfWordsTotal] [numColumns];

							Hashtable bagTexto = new Hashtable<String, Terms>();
							Hashtable bagTesaurus = new Hashtable<String, String>();
							
							for (Enumeration e = bagOfWordsGeralTexto.keys(); e.hasMoreElements();) 
							{
								String nomeArq = (String) e.nextElement();
								bagTexto = (Hashtable) bagOfWordsGeralTexto.get(nomeArq);
								bagTesaurus = (Hashtable) bagOfWordsGeralTesaurus.get(nomeArq);
								String caminhoCompleto = bagOfWordsArquivo.get(nomeArq).toString(); 
			        			
								// Atualiza o código do arquivo (cada nome de arquivo tem um código
								codArquivo = codArquivo + 1;
								
									for (Enumeration e2 = bagTesaurus.keys(); e2.hasMoreElements();) 
											
									{
										
										String key = (String) e2.nextElement();
										Terms termo = (Terms) bagTexto.get(key);
										String words = bagTesaurus.get(key).toString();
		
										bagOfWordsTotal[numLinha][0] = nomeArq;
										bagOfWordsTotal[numLinha][1] = key;
										bagOfWordsTotal[numLinha][2] = words;
										bagOfWordsTotal[numLinha][3]=(Integer.toString(termo.getFrequency()));
										bagOfWordsTotal[numLinha][4]= (Integer.toString(codArquivo));
										bagOfWordsTotal[numLinha][5]= (Integer.toString(codCluster));
										bagOfWordsTotal[numLinha][6]= caminhoCompleto;
										numLinha++;
										
									}

							}
							
							repositorioTextos.salvarBagOfWords(bagOfWordsTotal);
							setProgress(100);	
							//Insere os arquivos na tabela de clusters
							repositorioTextos.inserirCluster(bagOfWordsTotal);
							numLinha = 0;
							numRowsBagOfWordsTotal = 0;
							// FIM	
						} 
					
					}    


			}
			catch(Exception ex){
				System.out.println (ex.getMessage());
				MensagemErro ed = new MensagemErro();
				ed.setLabel(ex.getMessage());
				ed.setVisible(true);
			}
		}	

		public Hashtable applyTesaurus (Hashtable bagOfWords) throws SQLException 
		{
			Hashtable <String, String> bagOfWordswithTesaurus  = new Hashtable<String, String>();
			Hashtable <Integer, String> hstDescRadicalizados = new Hashtable<Integer, String>();

			   
			int codAlgoritmoCompString = 0;
			int codAlgoritmoClassificacao = 0;
			double valPercCompString = 0;
			AbstractStringMetric metric = null; 

			Configuracoes conf = new Configuracoes();

			try{
				codAlgoritmoCompString = conf.consultaAlgoritmoCompString();
				if ((codAlgoritmoCompString!= 3) && (codAlgoritmoCompString != 4) && 
						(codAlgoritmoCompString!= 5) && (codAlgoritmoCompString != 6)) 
				{
					JOptionPane.showMessageDialog(panelAtuDocumentoBase,
							"Erro ao consultar dados de Parametro.",
							"Erro", JOptionPane.ERROR_MESSAGE);

				}
				else
				{
					codAlgoritmoClassificacao = conf.consultaAlgoritmoClassificacao();
					//Carrega descritores do Tesaurus
					Descritor descritor = new Descritor();
					if (codAlgoritmoClassificacao ==1)
						hstDescRadicalizados = descritor.obterTermosRadicalizadosRlsp();
					if (codAlgoritmoClassificacao ==2)
						hstDescRadicalizados = descritor.obterTermosRadicalizadosPorter();
			

					if (codAlgoritmoCompString == 3)
					{
						metric = new Levenshtein();
					}

					if (codAlgoritmoCompString == 4)
					{
						metric = new CosineSimilarity();
					}
					if (codAlgoritmoCompString == 5)
					{
						metric = new EuclideanDistance();
					}

					if (codAlgoritmoCompString == 6)
					{
						metric = new JaroWinkler();
					}
					
					//Captura o valor de percentual de comparação de strings
					valPercCompString = (conf.consultaValorPercentualCompString());
					
					float i =0;
					float counter;
					progressMonitor.setProgress(0);
					setProgress(0);	
					for (Enumeration e = bagOfWords.keys(); e.hasMoreElements();) 
					{
						++i;
						counter = ((i/bagOfWords.size())*100); 

						if (counter != 100)
							setProgress((int) counter);	
						//Captura palavra da bagOfWords

						String key = (String) e.nextElement();
						String termoBagOfWords = key;
						
						// Comparando com a bag de Termos Radicalizados
						for (Enumeration e2 = hstDescRadicalizados.keys(); e2.hasMoreElements();) 
						{
								Integer key2 = (Integer) e2.nextElement();
								String termoTesaurus = hstDescRadicalizados.get(key2).toString().toLowerCase();


								double result = metric.getSimilarity(termoTesaurus, termoBagOfWords);
								if (result >= valPercCompString/100){
									bagOfWordswithTesaurus.put(termoBagOfWords,termoTesaurus);
								}

						}
					}
	
				}
			}	
			catch(SQLException ex){
				MensagemErro ed = new MensagemErro();
				ed.setLabel(ex.getMessage());
				ed.setVisible(true);

			}
			return (bagOfWordswithTesaurus);
		} 

	}



	/**
	 * Invoked when task's progress property changes.
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		if ("progress" == evt.getPropertyName()) {
			int progress = (Integer) evt.getNewValue();
			progressMonitor.setProgress(progress);
			String message =
				String.format("Completando Rotina %d de %d - %d%%.\n",counterArquivo, totalArquivos, progress);
			progressMonitor.setNote(message);
			if (progressMonitor.isCanceled()) {
				task.cancel(true);
			} 

		} 

	}

	public AtuDocumentoBase ()
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
		jLabelTextoExplicacaoArquivo.setText("Selecione os arquivos que deseja acrescentar à base.");
		explicacaoArquivo.add(jLabelTextoExplicacaoArquivo);
		c.gridx = 0;
		c.gridy = 0;
		gridbag.setConstraints(explicacaoArquivo, c);
		panelAtuDocumentoBase.add (explicacaoArquivo);

		JPanel explicacaoTexto = new JPanel(new FlowLayout(FlowLayout.LEFT));
		jLabelTextoExplicacao.setText("ATENÇÃO: Os documentos devem pertencer a uma mesma categoria jurídica.");
		explicacaoTexto.add(jLabelTextoExplicacao);
		c.gridx = 0;
		c.gridy = 1;
		gridbag.setConstraints(explicacaoTexto, c);
		panelAtuDocumentoBase.add (explicacaoTexto);
		
		JPanel panel_1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		jButtonSelecionarArquivos = new JButton ("Selecionar arquivos");
		jLabelCodLabelArquivo.setText("Arquivo:");
		jTextFieldArquivo.setText("");
		panel_1.add(jLabelCodLabelArquivo);
		panel_1.add(jTextFieldArquivo);
		panel_1.add(jButtonSelecionarArquivos );
		panel_1.setBorder(BorderFactory.createTitledBorder(""));

		c.gridx = 0;
		c.gridy = 2;
		gridbag.setConstraints(panel_1, c);
		panelAtuDocumentoBase.add (panel_1);

		JPanel panelCategoria = new JPanel(new GridLayout(0,4));
		panelCategoria.setBorder(BorderFactory.createTitledBorder("Área Jurídica"));
		
		opAdministrativo = new JRadioButton("Administrativo",true);
		panelCategoria.add(opAdministrativo);
		opAmbiental = new JRadioButton("Ambiental",false);
		panelCategoria.add(opAmbiental);
		opComercial = new JRadioButton("Comercial",false);
		panelCategoria.add(opComercial);
		opConsumidor = new JRadioButton("Consumidor",true);
		panelCategoria.add(opConsumidor);
		opConstitucional = new JRadioButton("Constitucional",false);
		panelCategoria.add(opConstitucional);

		opCivel = new JRadioButton("Cível",false);
		panelCategoria.add(opCivel);
		opEconomicoFinanceiro = new JRadioButton("Econômico Financeiro",true);
		panelCategoria.add(opEconomicoFinanceiro);
		opEleitoral = new JRadioButton("Eleitoral",false);
		panelCategoria.add(opEleitoral);
		opInternacionalPrivado = new JRadioButton("Internacional Privado",false);
		panelCategoria.add(opInternacionalPrivado);
		opMaritimo = new JRadioButton("Marítimo",true);
		panelCategoria.add(opMaritimo);
		opInternacionalPublico = new JRadioButton("Internacional Público",false);
		panelCategoria.add(opInternacionalPublico);
		opProcessualCivil = new JRadioButton("Processual Civil",false);
		panelCategoria.add(opProcessualCivil);
		opPenal = new JRadioButton("Penal",false);
		panelCategoria.add(opPenal);
		opProcessualPenal = new JRadioButton("Processual Penal",true);
		panelCategoria.add(opProcessualPenal);
		opPrevidenciario = new JRadioButton("Previdenciário",false);
		panelCategoria.add(opPrevidenciario);
		opTributario = new JRadioButton("Tributario",false);
		panelCategoria.add(opTributario);
		opPropriedadeIntelectual = new JRadioButton("Propriedade Intelectual",false);
		panelCategoria.add(opPropriedadeIntelectual);

		//Agrupa algumas opções de categorias
		ButtonGroup groupX = new ButtonGroup();
		groupX.add(opAdministrativo);
		groupX.add(opAmbiental);
		groupX.add(opComercial);
		groupX.add(opConsumidor);
		groupX.add(opConstitucional);
		groupX.add(opCivel);
		groupX.add(opEconomicoFinanceiro);
		groupX.add(opEleitoral);
		groupX.add(opInternacionalPrivado);
		groupX.add(opMaritimo);
		groupX.add(opInternacionalPublico);
		groupX.add(opProcessualCivil);
		groupX.add(opPenal);
		groupX.add(opProcessualPenal);
		groupX.add(opPrevidenciario);
		groupX.add(opTributario);
		groupX.add(opPropriedadeIntelectual);

		//panelCategoria.add (panelCategoria1);
		panelAtuDocumentoBase.add (panelCategoria);
		c.gridx = 0;
		c.gridy = 20;
		gridbag.setConstraints(panelCategoria, c);

		JPanel panel_3 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		jButtonAtualizar = new JButton ("Atualizar Base de Jurisprudência");
		jButtonAtualizar.setEnabled(true);
		jButtonFechar = new JButton ("Fechar");
		jButtonFechar.setEnabled(true);
		panel_3.add(jButtonAtualizar);
		panel_3.add(jButtonFechar);
		c.gridx = 0;
		c.gridy = 40;
		gridbag.setConstraints(panel_3, c);
		panelAtuDocumentoBase.add ( panel_3);

		add(panelAtuDocumentoBase);

		jButtonSelecionarArquivos.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonSelecionarArquivos_actionPerformed(e);
			}
		});

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
	}

	public static boolean isSelected(JRadioButton btn) {
		DefaultButtonModel model = (DefaultButtonModel)btn.getModel();
		return model.getGroup().isSelected(model);
	}

	void jButtonSelecionarArquivos_actionPerformed(ActionEvent e) 
	{
		jButtonSelecionarArquivos.setEnabled (false);
		
		// display dialog so user can choose file
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fileChooser.setMultiSelectionEnabled(true);
		fileChooser.setCurrentDirectory(fileChooser.getCurrentDirectory());

		int result = fileChooser.showOpenDialog( null );

		// if user clicked Cancel button on dialog, return
		if ( result == JFileChooser.CANCEL_OPTION ){
			jButtonSelecionarArquivos.setEnabled(true);
			return;
		}
			

		// obtain selected file
		fileName = fileChooser.getSelectedFiles();
		jTextFieldArquivo.setText("Arquivos selecionados");
		jButtonSelecionarArquivos.setEnabled (true);

	}

	void jButtonAtualizar_actionPerformed(ActionEvent e) 
	{
		jButtonSelecionarArquivos.setEnabled (false);
		jButtonAtualizar.setEnabled (false);
		jButtonFechar.setEnabled(false);
		progressMonitor = new ProgressMonitor (AtuDocumentoBase.this,
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
	
	public void showSelectFiles()
	{
		// display dialog so user can choose file
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fileChooser.setMultiSelectionEnabled(true);
		fileChooser.setCurrentDirectory(fileChooser.getCurrentDirectory());

		int result = fileChooser.showOpenDialog( null );

		// if user clicked Cancel button on dialog, return
		if ( result == JFileChooser.CANCEL_OPTION )
			return;

		// obtain selected file
		fileName = fileChooser.getSelectedFiles();
//		if (fileName.length > 0)
//		{
//			this.selectFile(fileName);
///		}

	}

	public void buildListFile (File [] pathName)
	{
		listFileName.clear();
		listPathName.clear();

		for (int i=0;i < pathName.length; ++i)
		{
			File file = pathName[i];
			if (file.isDirectory())
			{
				File[] fileList = file.listFiles();
				this.buildListFile(fileList);
			}
			if (!file.isDirectory() )
			{
				listFileName.add(file.getName());
				listPathName.add(file.getAbsolutePath());
			}
		}		
	}

}
