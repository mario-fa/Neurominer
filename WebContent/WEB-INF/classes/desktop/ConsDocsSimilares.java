package desktop;


import hypertree.demo.DemoMinerJur;

import javax.swing.*;

import bancoDados.AcessoBanco;

import acessoDados.Arquivo.AccessFile;
import java.sql.ResultSet;

import negocio.*;

import uk.ac.shef.wit.simmetrics.similaritymetrics.AbstractStringMetric;
import uk.ac.shef.wit.simmetrics.similaritymetrics.CosineSimilarity;
import uk.ac.shef.wit.simmetrics.similaritymetrics.EuclideanDistance;
import uk.ac.shef.wit.simmetrics.similaritymetrics.JaroWinkler;
import uk.ac.shef.wit.simmetrics.similaritymetrics.Levenshtein;

import conversor.conversorDOC;
import conversor.conversorPDF;
import dados.Descritor;
import textTreatment.*;
import termsTreatment.*;


import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

public class ConsDocsSimilares extends JPanel implements PropertyChangeListener {

	private IObserverFechar observador;
	
	JPanel panel;

	JPanel jPanelDocsSimilares = new JPanel(new FlowLayout());

	JLabel jLabelCodLabelArquivo = new JLabel();
	JTextField jTextFieldArquivo = new JTextField(50);

	JLabel jLabelTextoBusca = new JLabel();
	JLabel jLabelTextoExplicacaoArquivo = new JLabel();
	JLabel jLabelTextoExplicacao = new JLabel();
	JLabel jLabelTextoExplicacao2 = new JLabel();

	JTextArea jTextFieldTextoBusca = new JTextArea(15,50);
	JButton jButtonArquivo = new JButton();
	JButton jButtonConsulta = new JButton();
	JButton jButtonFechar = new JButton();
	JLabel espaco = new JLabel("                ");
	JTextField jTextFieldPercentualAproximacao = new JTextField(03);
	JLabel jLabelTextoPercentualAproximacao = new JLabel();

    private int counterPassos, totalPassos;
 
	private JCheckBox opAdministrativo, opAmbiental, opComercial, opConsumidor, opConstitucional,
	opCivel, opEconomicoFinanceiro, opEleitoral, opInternacionalPrivado, opMaritimo, opInternacionalPublico,
	opProcessualCivil, opPenal, opProcessualPenal, opPrevidenciario, opTributario, opPropriedadeIntelectual, opTodos;

	private JRadioButton opArvore, opLista;
	
	private ProgressMonitor progressMonitor;

	private Task task;
	final static int ONE_SECOND = 60;
	
	class Task extends SwingWorker<Void, Void> 
	{
		/*
		 * Main task. Executed in background thread.
		 */
		@Override
		public Void doInBackground() {
				this.consultaDocSimilares();
			return null;
		}

		/*
		 * Executed in event dispatching thread
		 */
		@Override
		public void done() {
			Toolkit.getDefaultToolkit().beep();
			//JOptionPane.showMessageDialog(panel,
			//		"Rotina concluída com sucesso",
			//		"Atualização de Grupos de Documentos", JOptionPane.OK_OPTION);
			jButtonConsulta.setEnabled(true);
			jButtonFechar.setEnabled(true);
			setCursor(null); //turn off the wait cursor

		}
		
		void consultaDocSimilares()
		
		{
			totalPassos = 3;
			counterPassos = 0;
			
			String path, caminhoOriginal = "", nomeOriginal = "";
			ArrayList listFile = new ArrayList();
			Hashtable bagOfWordsArquivo = new Hashtable();
			Hashtable <String, String> bagOfWordsTesaurus = new Hashtable<String, String>();
			Hashtable <String, Hashtable> bagOfWordsGeralTexto = new Hashtable<String, Hashtable>();
			Hashtable <String, Hashtable> bagOfWordsGeralTesaurus = new Hashtable<String, Hashtable>();
			Hashtable <String, String> bagOfWordsNomeArquivo = new Hashtable<String, String>();

			Hashtable sinonimo =  new Hashtable();
			

			String [][] bagOfWordsTotal = null;
			int codArquivo=0;			
			int numRowsBagOfWordsTotal = 0;
			int codAlgoritmoClassificacao = 0;
			int numColumns = 7;
			int numLinha = 0;
			ArrayList <Integer>arlCategorias = new ArrayList<Integer>();

			arlCategorias = getCategoriasJuridicas();
			
			//progress
			counterPassos = 1;
			progressMonitor.setProgress(0);
			setProgress(0);
			
			if (arlCategorias.size()==0){
				JOptionPane.showMessageDialog(null,"Escolha ao menos uma área jurídica" , "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
				
			
			Configuracoes conf = new Configuracoes();
			RepositorioTextos repositorioTextos = new RepositorioTextos();

			try 
			{
				if (validarCampos())
				{	
					//Desabilita os botões
					habilitarBotoes(false);
					
					//Busca o algoritmo de Classificação indicado na tabela de Parâmetros
					codAlgoritmoClassificacao = conf.consultaAlgoritmoClassificacao();
					sinonimo= repositorioTextos.carregarSinominos();
					
					//Classe do componente bagOfWords para tratamento do texto.
					textTreatment tt = new textTreatment();

					if ((codAlgoritmoClassificacao!= 1) && (codAlgoritmoClassificacao != 2)) 
					{
						JOptionPane.showMessageDialog(jPanelDocsSimilares,
								"Erro ao consultar dados de Parametro.",
								"Erro", JOptionPane.ERROR_MESSAGE);
					}
					else
					{
						//Se o usuário tiver fornecido o nome do arquivo
						if ((!jTextFieldArquivo.getText().toString().equals("")))
						{
							String conteudo = " ";
							path = jTextFieldArquivo.getText().toString();

							//Chama o conversor de formato de arquivo
							File f = new File(path);						
							String caminho = f.getPath();
							caminhoOriginal =f.getPath();
							nomeOriginal = f.getName();
							caminho = caminho.substring(0, caminho.lastIndexOf("."));
							String extensao = path.substring(path.lastIndexOf("."), path.length());

							if (extensao.toUpperCase().compareTo(".PDF")==0){
								conversorPDF convert = new conversorPDF();
								// convert.setEnderecoRecurso(nameFile);
								convert.setEnderecoRecurso(path);
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
							float i=0;
							int total = sinonimo.size();
							float counter;
							for (Enumeration e2 = sinonimo.keys(); e2.hasMoreElements();) 
							{
								++i;
								counter = ((i/total)*100); 
								setProgress((int) counter);	
		
								String key = (String) e2.nextElement();
								String descritor = (String) sinonimo.get(key);
								key = "\\b" + key + "\\b";							
								conteudo = conteudo.replaceAll(key, descritor);
							}


							PrintWriter fw = new PrintWriter(path);
							fw.print(conteudo);
							fw.close();

							listFile = tt.readFile(path);
						} 
					else{
						//Se o usuário fornecer descrição no campo Texto para busca
						path = "Texto de busca";
						nomeOriginal = "Texto de Busca";
						//Rotina para substituir por sinonimos
						String textoBusca = jTextFieldTextoBusca.getText().toString().toUpperCase();
						String textoBuscaSinonimo = "";
						float i=0;
						int total = sinonimo.size();
						//System.out.println ("total = " + total);
						float counter;
						for (Enumeration e2 = sinonimo.keys(); e2.hasMoreElements();) 
						{
							++i;
							counter = ((i/total)*100); 
							setProgress((int) counter);	
							
							String key = (String) e2.nextElement();
							String descritor = (String) sinonimo.get(key);
							key = "\\b" + key + "\\b";							
							textoBuscaSinonimo = textoBusca.replaceAll(key, descritor);
						}
						listFile.add(textoBuscaSinonimo);
						
					}
					
						//Extração da bag of words
						if (codAlgoritmoClassificacao ==1)
						{
							bagOfWordsArquivo = tt.extractBagOfWordsByRlsp(listFile);
						}
						if (codAlgoritmoClassificacao ==2)
						{
							bagOfWordsArquivo = tt.extractBagOfWordsByPorter(listFile);
						}	

						//Aplica o Tesaurus
						bagOfWordsTesaurus = this.applyTesaurus(bagOfWordsArquivo);
						//progress
						
												
						numRowsBagOfWordsTotal = numRowsBagOfWordsTotal + bagOfWordsTesaurus.size();
						bagOfWordsGeralTexto.put(path, bagOfWordsArquivo);
						bagOfWordsGeralTesaurus.put(path, bagOfWordsTesaurus);
						bagOfWordsNomeArquivo.put(path, caminhoOriginal);


						//Cria o String [][] com o tamanho de linhas calculado no loop acima
						bagOfWordsTotal = new String [numRowsBagOfWordsTotal] [numColumns];

						Hashtable bagTexto = new Hashtable<String, Terms>();
						Hashtable bagTesaurus = new Hashtable<String, String>();

						float i=0;
						int total = bagOfWordsGeralTexto.size();
						float counter;
						
						for (Enumeration ex = bagOfWordsGeralTexto.keys(); ex.hasMoreElements();) 
						{
							++i;
							counter = ((i/total)*100); 
							setProgress((int) counter);	
							//System.out.println (counter);
							
							String nomeArq = (String) ex.nextElement();
							bagTexto = (Hashtable) bagOfWordsGeralTexto.get(nomeArq);
							bagTesaurus = (Hashtable) bagOfWordsGeralTesaurus.get(nomeArq);
							String caminhoCompleto = bagOfWordsNomeArquivo.get(nomeArq).toString(); 

							for (Enumeration e2 = bagTesaurus.keys(); e2.hasMoreElements();) 

							{
								String key = (String) e2.nextElement();

								Terms termo = (Terms) bagTexto.get(key);
								String words = bagTesaurus.get(key).toString();

								bagOfWordsTotal[numLinha][0] = nomeOriginal;
								bagOfWordsTotal[numLinha][1] = key;
								bagOfWordsTotal[numLinha][2] = words;
								bagOfWordsTotal[numLinha][3]=(Integer.toString(termo.getFrequency()));
								bagOfWordsTotal[numLinha][4]= (Integer.toString(codArquivo));
								bagOfWordsTotal[numLinha][5]= "0";
								bagOfWordsTotal[numLinha][6]= caminhoCompleto;
								
								numLinha++;
							}
						}
						counterPassos = 3;
						setProgress(30);		
					repositorioTextos.salvarBagOfWords(bagOfWordsTotal);
					setProgress(60);
					
					//Chama a rotina de consulta a documentos similares
					int codFormatoExibição = 2;
				    if (isSelected(opArvore))
			        {
				    	codFormatoExibição = 1;                	
			        }
				    if (isSelected(opLista))
			        {
				    	codFormatoExibição = 2;                	
			        }
					repositorioTextos.consultaDocsSimilares(codArquivo, arlCategorias, Integer.parseInt(jTextFieldPercentualAproximacao.getText()));

					try{
						counterPassos = 3;
						setProgress(90);
						DemoMinerJur.plotTree (Integer.parseInt(jTextFieldPercentualAproximacao.getText()),codFormatoExibição);
						setProgress(100);
					}
					catch (Exception exception)
					{
						JOptionPane.showMessageDialog(jPanelDocsSimilares,
								"Não existem casos julgados que atendam o percentual de similaridade indicado.",
								"Erro", JOptionPane.ERROR_MESSAGE);
					}			            	
					
					//Habilita os botões
					habilitarBotoes(true);

				}
			}	
		}	
		catch (Exception ex) 
		{
			MensagemErro ed = new MensagemErro();
			ed.setLabel(ex.getMessage());
			ed.setVisible(false);
		}
	}  

		
	public Hashtable<String, String> applyTesaurus (Hashtable bagOfWords) throws SQLException 
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
				JOptionPane.showMessageDialog(jPanelDocsSimilares,
						"Erro ao consultar dados de Parametro.",
						"Erro", JOptionPane.ERROR_MESSAGE);

			}
			else
			{

				//Carrega descritores do Tesaurus
				Descritor descritor = new Descritor();
				
				codAlgoritmoClassificacao = conf.consultaAlgoritmoClassificacao();
				
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

				//float i =0;
				//int qtdRadicais = hstDescRadicalizados.size();
				counterPassos = 2;
				float counter;
				float i=0;
				int total = bagOfWords.size();
				for (Enumeration e = bagOfWords.keys(); e.hasMoreElements();) 
				{
					++i;
					counter = ((i/total)*100); 
					setProgress((int) counter);	
					//System.out.println (counter);
					
					//Captura palavra da bagOfWords

					String key = (String) e.nextElement();
					String termoBagOfWords = key;
					//System.out.println (termoBagOfWords);

					// Comparando com a bag de Termos Radicalizados
					//i = 0;
					for (Enumeration e2 = hstDescRadicalizados.keys(); e2.hasMoreElements();) 
					{
//						if (((((i/qtdRadicais))*100)%10) == 0)
//						   System.out.println (((i/qtdRadicais))*100);
//						++i;
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
			//System.out.println (ex.getMessage());
			ed.setVisible(true);
		}
		return (bagOfWordswithTesaurus);
	} 
	}
	
	public ConsDocsSimilares() {
		super(new GridLayout(1,0));
		try {
			jbInit();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static boolean isSelected(JRadioButton btn) {
		DefaultButtonModel model = (DefaultButtonModel)btn.getModel();
		return model.getGroup().isSelected(model);
	}
	
	private void jbInit() throws Exception {

		this.setLayout(new BorderLayout(1,1));
		this.setBorder(BorderFactory.createTitledBorder(""));

		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		panel = new JPanel();
		panel.setLayout(gridbag);
		c.fill = GridBagConstraints.BOTH;

		JPanel explicacaoArquivo = new JPanel(new FlowLayout(FlowLayout.LEFT));
		jLabelTextoExplicacaoArquivo.setText("Selecione um arquivo que descreva o caso jurídico em análise.");
		explicacaoArquivo.add(jLabelTextoExplicacaoArquivo);

		c.gridx = 0;
		c.gridy = 0;
		gridbag.setConstraints(explicacaoArquivo, c);
		panel.add (explicacaoArquivo);

		JPanel explicacaoTexto = new JPanel(new FlowLayout(FlowLayout.LEFT));
		jLabelTextoExplicacao.setText("Caso não exista um documento que retrate o caso jurídico em análise, descreva-o no campo 'Texto para Busca'.");
		explicacaoTexto.add(jLabelTextoExplicacao);

		c.gridx = 0;
		c.gridy = 1;
		gridbag.setConstraints(explicacaoTexto, c);
		panel.add (explicacaoTexto);

		JPanel explicacaoTexto2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		jLabelTextoExplicacao2.setText("Quanto maior a quantidade de palavras fornecidas, maior a precisão no processo de busca.");
		explicacaoTexto2 .add(jLabelTextoExplicacao2);

		c.gridx = 0;
		c.gridy = 2;
		gridbag.setConstraints(explicacaoTexto2 , c);
		panel.add (explicacaoTexto2 );


		JPanel panel_1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		jLabelCodLabelArquivo.setText("Arquivo:");
		jTextFieldArquivo.setText("");
		jButtonArquivo.setText("Selecionar");
		panel_1.add(jLabelCodLabelArquivo);
		panel_1.add(espaco);
		panel_1.add(jTextFieldArquivo);
		panel_1.add(jButtonArquivo);
		panel_1.setBorder(BorderFactory.createTitledBorder(""));

		c.gridx = 0;
		c.gridy = 8;
		gridbag.setConstraints(panel_1, c);
		panel.add (panel_1);

		JPanel panel_2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		jLabelTextoBusca.setText("Texto para Busca:");
		jTextFieldTextoBusca.setText("");
		jTextFieldTextoBusca.setBorder(BorderFactory.createLineBorder(Color.black));
		jTextFieldTextoBusca.setLineWrap(true);
		panel_2.add(jLabelTextoBusca);
		panel_2.add(jTextFieldTextoBusca);

		c.gridx = 0;
		c.gridy = 12;
		gridbag.setConstraints(panel_2, c);
		panel.add (panel_2);

		JPanel panel_4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		jLabelTextoPercentualAproximacao.setText("Percentual de Similaridade (%):");
		jTextFieldPercentualAproximacao.setText("");
		jTextFieldPercentualAproximacao.setBorder(BorderFactory.createLineBorder(Color.black));
		panel_4.add(jLabelTextoPercentualAproximacao);
		panel_4.add(jTextFieldPercentualAproximacao);

		JPanel panelFormaExibicao = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelFormaExibicao.setBorder(BorderFactory.createTitledBorder("Formato de Exibição"));
		opArvore = new JRadioButton("Árvore",false);
		opLista = new JRadioButton("Lista",true);
		panelFormaExibicao.add(opArvore);
		panelFormaExibicao.add(opLista);

		//Group the radio buttons.
	    ButtonGroup groupX = new ButtonGroup();
	    groupX.add(opArvore);
	    groupX.add(opLista);
	    panel_4.add (panelFormaExibicao);
	    
		c.gridx = 0;
		c.gridy = 16;
		
		gridbag.setConstraints(panel_4, c);
		panel.add (panel_4);

		JPanel panelCategoria = new JPanel(new GridLayout(0,4));
		panelCategoria.setBorder(BorderFactory.createTitledBorder("Selecione a(s) área(s) jurídica onde deseja que seja efetuada a busca"));

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
		opTodos = new JCheckBox("Todas as Áreas ",false);
		Font fonteTodos = new Font ("Arial", Font.BOLD, 12);
		opTodos.setFont(fonteTodos);
		
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
		
		panelCategoria.add(opTodos);
		opTodos.setSelected(false);
		panel.add (panelCategoria);
		c.gridx = 0;
		c.gridy = 20;
		gridbag.setConstraints(panelCategoria, c);
		habilitarCheckbox ();

		JPanel panel_3 = new JPanel(new FlowLayout(FlowLayout.CENTER));

		jButtonConsulta.setText("Consultar Base de Jurisprudência");
		jButtonConsulta.setEnabled(true);
		jButtonFechar.setText("Fechar");
		jButtonFechar.setEnabled(true);
		panel_3.add(jButtonConsulta);
		panel_3.add(jButtonFechar);
		c.gridx = 0;
		c.gridy = 24;
		gridbag.setConstraints(panel_3, c);
		panel.add ( panel_3);

		add(panel);

		jButtonArquivo.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonArquivo_actionPerformed(e);
			}
		});

		jButtonConsulta.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonConsulta_actionPerformed(e);
			}
		});

		jButtonFechar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonFechar_actionPerformed(e);
			}
		});

		limparComponentes();
	}


	void jButtonArquivo_actionPerformed(ActionEvent e) 
	{
		jButtonArquivo.setEnabled (false);
		jButtonFechar.setEnabled(false);

		// display dialog so user can choose file
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setCurrentDirectory(fileChooser.getCurrentDirectory());

		int result = fileChooser.showOpenDialog( null );

		// if user clicked Cancel button on dialog, return
		if ( result == JFileChooser.CANCEL_OPTION )
			return;

		// obtain selected file
		File fileName = fileChooser.getSelectedFile();
		this.jTextFieldArquivo.setText(fileName.getAbsoluteFile().toString());

		jButtonArquivo.setEnabled (true);
		jButtonFechar.setEnabled(true);

	}




void jButtonFechar_actionPerformed(ActionEvent e) {
	observador.fechar();
}


private void limparComponentes(){
	jTextFieldArquivo.setText("");
	jTextFieldTextoBusca.setText("");
}

private boolean validarCampos(){
	boolean retorno = true;

	if ((!jTextFieldArquivo.getText().toString().equals("")) &&
			(!jTextFieldTextoBusca.getText().toString().equals("")))
	{
		JOptionPane.showMessageDialog(jPanelDocsSimilares,
				"Não é permitido selecionar um arquivo e preencher o campo 'Texto de Busca'. Escolha uma das opções.",
				"Atenção", JOptionPane.WARNING_MESSAGE);
		jTextFieldArquivo.setFocusable(true);
		retorno = false;

	}	
	if ((jTextFieldArquivo.getText().toString().equals("")) && (jTextFieldTextoBusca.getText().toString().equals("")))
	{
		JOptionPane.showMessageDialog(jPanelDocsSimilares, "Selecione um arquivo ou descreva o caso em análise no campo 'Texto de Busca'", "Atenção", JOptionPane.WARNING_MESSAGE);
		jTextFieldArquivo.setFocusable(true);	
		retorno = false;
	}
	
	if ((jTextFieldPercentualAproximacao.getText().toString().equals("")))
	{
		JOptionPane.showMessageDialog(jPanelDocsSimilares, "Indique o percentual de similaridade desejado", "Atenção", JOptionPane.WARNING_MESSAGE);
		jTextFieldPercentualAproximacao.setFocusable(true);	
		retorno = false;
	}
	
	if (!((opTodos.isSelected())||(opAdministrativo.isSelected())|| (opAmbiental.isSelected()) ||
			(opComercial.isSelected()) || (opConsumidor.isSelected()) || (opConstitucional.isSelected())||
			(opCivel.isSelected())|| (opEconomicoFinanceiro.isSelected()) || (opEleitoral.isSelected()) ||
			(opInternacionalPrivado.isSelected()) || (opMaritimo.isSelected()) || (opInternacionalPublico.isSelected())||
			(opProcessualCivil.isSelected()) || (opPenal.isSelected()) || (opProcessualPenal.isSelected()) ||
			(opPrevidenciario.isSelected()) || (opTributario.isSelected()) || (opPropriedadeIntelectual.isSelected())))
			{
				JOptionPane.showMessageDialog(jPanelDocsSimilares,
						"Selecione pelo menos uma área jurídica.",
						"Atenção", JOptionPane.WARNING_MESSAGE);
				retorno = false;

			}

	return retorno;
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

private ArrayList getCategoriasJuridicas ()
{
	ArrayList<Integer> arlCategorias = new ArrayList<Integer>();
	
	if (opTodos.isSelected())
	{
		for (int i=1; i<18; i++){
			if (existsCluster (i))	
			  arlCategorias.add(i);
		}
		return (arlCategorias);
	}
	//Pega o número do cluster 
	if (opAdministrativo.isSelected())
	{
	//	if (existsCluster (1))	
			arlCategorias.add(1);                	
	}
	if (opAmbiental.isSelected())
	{
		//if (existsCluster (2))	
		   arlCategorias.add(2);                	
	}
	if (opComercial.isSelected())
	{
		//if (existsCluster (3))	
		   arlCategorias.add(3);                	
	}
	if (opConsumidor.isSelected())
	{
		//if (existsCluster (4))	
		    arlCategorias.add(4);                	
	}
	if (opConstitucional.isSelected())
	{
		//if (existsCluster (5))	
		   arlCategorias.add(5);                	
	}
	if (opCivel.isSelected())
	{
		//if (existsCluster (6))	
		    arlCategorias.add(6);                	
	}
	if (opEconomicoFinanceiro.isSelected())
	{
		//if (existsCluster (7))	
		   arlCategorias.add(7);                	
	}
	if (opEleitoral.isSelected())
	{
		//if (existsCluster (8))	
		   arlCategorias.add(8);                	
	}
	if (opInternacionalPrivado.isSelected())
	{
		//if (existsCluster (9))	
		  arlCategorias.add(9);                	
	}
	if (opMaritimo.isSelected())
	{
		//if (existsCluster (10))	
		  arlCategorias.add(10);                	
	}
	if (opInternacionalPublico.isSelected())
	{
		//if (existsCluster (11))	
		   arlCategorias.add(11);                	
	}
	if (opProcessualCivil.isSelected())
	{
		//if (existsCluster (12))	
		  arlCategorias.add(12);                	
	}
	if (opPenal.isSelected())
	{
		//if (existsCluster (13))	
		arlCategorias.add(13);                	
	}
	if (opProcessualPenal.isSelected())
	{
		//if (existsCluster (14))	
		arlCategorias.add(14);                	
	}
	if (opPrevidenciario.isSelected())
	{
		//if (existsCluster (15))	
		arlCategorias.add(15);                	
	}
	if (opTributario.isSelected())
	{
		//if (existsCluster (16))	
		arlCategorias.add(16);                	
	}
	if (opPropriedadeIntelectual.isSelected())
	{
		//if (existsCluster (17))	
		arlCategorias.add(17);                	
	}
	
	return (arlCategorias);
	


}
	
private boolean existsCluster (int codCluster){
	
	try {
	AcessoBanco acessoBanco = new AcessoBanco();	

    String lstrSQL = "SELECT count (*) from cluster where not isnull (codsubcluster) and codcluster = " + codCluster;

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

private void habilitarBotoes(boolean aboEstado){
	jButtonArquivo.setEnabled(aboEstado);
	jButtonConsulta.setEnabled(aboEstado) ;
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
			String.format("Completando Rotina %d de %d - %d%%.\n",counterPassos, totalPassos, progress);
		progressMonitor.setNote(message);
		if (progressMonitor.isCanceled()) {
			task.cancel(true);
		} 

	}
}	

void jButtonConsulta_actionPerformed(ActionEvent e) 
{
	jButtonConsulta.setEnabled (false);
	jButtonFechar.setEnabled(false);
	progressMonitor = new ProgressMonitor (ConsDocsSimilares.this,
			"Aguarde - Rotina em Processamento",
			"", 0, 100);
	setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	task = new Task();
	task.addPropertyChangeListener(this);
	task.execute();
}

public void addListener(IObserverFechar observer){
	observador = observer;
}

}
