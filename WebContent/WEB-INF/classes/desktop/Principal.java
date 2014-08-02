package desktop;
import javax.swing.*;


import java.awt.*;
import java.awt.event.*;
import negocio.*;

/*
 * Título:       Principal.java
 * Descrição:    Classe Principal da Ferramenta MinerJur
 * Assunto:      Dissertacao do Mestrado em Sistemas e Computação
 * Orientador:   Manoel Mendonça
 * @author       Cristiane Costa Magalhães
 * Data       :  Jul/2007
 * @version 1.0
 */

public class Principal extends JFrame {
  JMenuBar jMenuPrincipal = new JMenuBar();
  
  //Itens do Menu Principal
  JMenu jMenuCadastro = new JMenu();
  JMenu jMenuAtualizacao = new JMenu();
  JMenu jMenuConsulta = new JMenu();
  JMenu jMenuConfiguracoes = new JMenu();
  
  //Itens de Menu - Cadastro
  JMenu jMenuTesauro = new JMenu();
  JMenuItem jMenuItemCadastroCategoria = new JMenuItem();
  JMenuItem jMenuItemCadastroSubCategoria = new JMenuItem();
  JMenuItem jMenuItemCadastroEquivalencia = new JMenuItem();
  JMenuItem jMenuItemCadastroDescritor = new JMenuItem();
  
  //Itens de Menu - Atualização
  JMenuItem jMenuItemAtualizaDocumentosBase = new JMenuItem();
  JMenuItem jMenuItemAtualizaClusters = new JMenuItem();

  //Itens de Menu - Consulta
  JMenuItem jMenuItemConsultaJulgadosSimilares = new JMenuItem();
  

  //Itens de Menu - Configuracoes
  JMenuItem jMenuItemParametro = new JMenuItem();

  //Itens de Menu - Sair
  JMenuItem jMenuItemSair = new JMenuItem();
  
  private JInternalFrame frameViewClass;
  private JDesktopPane theDesktop;


    public Principal() {
    try {
      init();

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  
    public JDesktopPane getTheDesktop(){
    	return (this.theDesktop);
    }
    
  private void init() throws Exception {

  	this.setIconImage(null);
    this.setTitle("Ferramenta MinerJur");
        
    //Menu Cadastro
    jMenuCadastro.setText("Cadastro");
    jMenuTesauro.setText("Tesauro");
    jMenuItemCadastroCategoria.setText("Categoria");
    jMenuItemCadastroSubCategoria.setText("Subcategoria");
    jMenuItemCadastroEquivalencia.setText("Equivalência");
    jMenuItemCadastroDescritor.setText("Descritor");
      
    jMenuItemCadastroCategoria.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(ActionEvent e) {
        	jMenuItemCadastroCategoria_actionPerformed(e);
        }
      });
    jMenuItemCadastroSubCategoria.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(ActionEvent e) {
        	jMenuItemCadastroSubCategoria_actionPerformed(e);
        }
      });
    jMenuItemCadastroEquivalencia.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(ActionEvent e) {
        	jMenuItemCadastroEquivalencia_actionPerformed(e);
        }
      });
    jMenuItemCadastroDescritor.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(ActionEvent e) {
        	jMenuItemCadastroDescritor_actionPerformed(e);
        }
      });

    //Menu Atualização
    jMenuAtualizacao.setText("Atualização");
    jMenuItemAtualizaDocumentosBase.setText("Base de Jurisprudência");
    jMenuItemAtualizaDocumentosBase.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
    	  jMenuItemAtualizaDocumentosBase_actionPerformed(e);
      }
    });

    jMenuItemAtualizaClusters.setText("Grupos e Subgrupos de Documentos");
    jMenuItemAtualizaClusters.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
    	  jMenuItemAtualizaClusters_actionPerformed(e);
      }
    });

    //Menu Consulta
    jMenuConsulta.setText("Consulta");
    jMenuItemConsultaJulgadosSimilares.setText("Casos Julgados Similares");
    jMenuItemConsultaJulgadosSimilares.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
    	  jMenuItemConsultaJulgadosSimilares_actionPerformed(e);
      }
    });
    
    
    //Menu Configuracoes
    jMenuConfiguracoes.setText("Configurações");
    jMenuItemParametro.setText("Parâmetros");
    jMenuItemParametro.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
    	  jMenuItemParametro_actionPerformed(e);
      }
    });

    jMenuItemSair.setText("Sair");
    jMenuItemSair.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
      	jMenuItemSair_actionPerformed(e);
      }
    });

    //Adicionando itens ao Menu Principal
    jMenuPrincipal.add(jMenuCadastro);
    jMenuPrincipal.add(jMenuAtualizacao);
    jMenuPrincipal.add(jMenuConsulta);
    jMenuPrincipal.add(jMenuConfiguracoes);
    jMenuPrincipal.add(jMenuItemSair);
    
    //Adicionando itens ao Menu Cadastro
    jMenuTesauro.add(jMenuItemCadastroCategoria);
    jMenuTesauro.add(jMenuItemCadastroSubCategoria);    
    jMenuTesauro.add(jMenuItemCadastroDescritor);    
    jMenuTesauro.add(jMenuItemCadastroEquivalencia);    
    jMenuCadastro.add(jMenuTesauro);

    //Adicionando itens ao Menu Atualização
    jMenuAtualizacao.add(jMenuItemAtualizaDocumentosBase);
    jMenuAtualizacao.add(jMenuItemAtualizaClusters);
    //jMenuAtualizacao.add(jMenuTesauro);
    
    //Adicionando itens ao Menu Consultas    
    jMenuConsulta.add(jMenuItemConsultaJulgadosSimilares);
   
    //jMenuConsulta.add(jMenuTesauro);


    //Adicionando itens ao Menu Configurações    
    jMenuConfiguracoes.add(jMenuItemParametro);

    
    setBounds(0,0,1024,768);
    setJMenuBar(jMenuPrincipal);

    // Configura o Desktop
    theDesktop = new JDesktopPane();
    
    this.getContentPane().add(theDesktop);
    this.setVisible(true);
    
  } 

   public static void main(String[] args) {
    Principal p = new Principal();
    p.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   }

void jMenuItemAtualizaDocumentosBase_actionPerformed(ActionEvent e) 
   {
   	setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
   	if (frameViewClass == null)
   		this.initViewClassAtualizaRepositorio();
   	if (! frameViewClass.isVisible())
   	        this.initViewClassAtualizaRepositorio();
   	frameViewClass.setVisible(true);
   	setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

}    


void jMenuItemAtualizaClusters_actionPerformed(ActionEvent e) 
{
   	setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
   	if (frameViewClass == null)
   		this.initViewClassAtualizaClusters();
   	if (! frameViewClass.isVisible())
   	        this.initViewClassAtualizaClusters();
   	frameViewClass.setVisible(true);
   	setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

 }    

void jMenuItemConsultaJulgadosSimilares_actionPerformed(ActionEvent e) 
{
    frameViewClass = new JInternalFrame("Consulta casos julgados similares",false, true, false,false);
    Container container = frameViewClass.getContentPane();
    final ConsDocsSimilares ci = new ConsDocsSimilares();
    container.add(ci);
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout());

      ci.addListener(
      new ObserverAdapterFechar()
       {
         public void fechar()
         {
           frameViewClass.dispose();
         }
       }
       );

 	frameViewClass.setSize(1000,750);
    frameViewClass.pack();
    theDesktop.add(frameViewClass);
    frameViewClass.setVisible(true);
}    

private void initViewClassAtualizaRepositorio(){
	frameViewClass = new JInternalFrame("Base de Jurisprudência",false, false, true,false);
	Container container = frameViewClass.getContentPane();
	final AtuDocumentoBase repositorioDocumentos = new AtuDocumentoBase  (); 
	container.add(repositorioDocumentos);

    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout());

    repositorioDocumentos.addListener(
      new ObserverAdapterFechar()
       {
         public void fechar()
         {
           frameViewClass.dispose();
         }
       }
       );


    frameViewClass.pack();
    theDesktop.add(frameViewClass);
    frameViewClass.setVisible(true);

}

private void initViewClassAtualizaClusters(){
	frameViewClass = new JInternalFrame("Grupos e subgrupos de documentos",false, true, false,false);
	Container container = frameViewClass.getContentPane();
	final AtuClusters ci = new AtuClusters ();
	container.add(ci);
	JPanel buttonPanel = new JPanel();
	buttonPanel.setLayout(new FlowLayout());

	  ci.addListener(
	  new ObserverAdapterFechar()
	   {
	     public void fechar()
	     {
	       frameViewClass.dispose();
	     }
	   }
	   );


	frameViewClass.pack();
	theDesktop.add(frameViewClass);
	frameViewClass.setVisible(true);

}



void jMenuItemCadastroCategoria_actionPerformed(ActionEvent e) {
    frameViewClass = new JInternalFrame("Cadastro de Categorias",false, true, false,false);
    Container container = frameViewClass.getContentPane();
    final CadCategoria ci = new CadCategoria();
    container.add(ci);
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout());

      ci.addListener(
      new ObserverAdapterFechar()
       {
         public void fechar()
         {
           frameViewClass.dispose();
         }
       }
       );


    frameViewClass.pack();
    theDesktop.add(frameViewClass);
    frameViewClass.setVisible(true);

  }

void jMenuItemCadastroSubCategoria_actionPerformed(ActionEvent e) {
	
    frameViewClass = new JInternalFrame("Cadastro de Sub Categorias",false, true, false,false);
    Container container = frameViewClass.getContentPane();
    final CadSubCategoria ci = new CadSubCategoria();
    container.add(ci);
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout());

      ci.addListener(
      new ObserverAdapterFechar()
       {
         public void fechar()
         {
           frameViewClass.dispose();
         }
       }
       );


    frameViewClass.pack();
    theDesktop.add(frameViewClass);
    frameViewClass.setVisible(true);
  }

void jMenuItemCadastroEquivalencia_actionPerformed(ActionEvent e) {
	
    frameViewClass = new JInternalFrame("Cadastro de Equivalencias",false, true, false,false);
    Container container = frameViewClass.getContentPane();
    final CadEquivalencia ci = new CadEquivalencia();
    container.add(ci);
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout());

      ci.addListener(
      new ObserverAdapterFechar()
       {
         public void fechar()
         {
           frameViewClass.dispose();
         }
       }
       );


    frameViewClass.pack();
    theDesktop.add(frameViewClass);
    frameViewClass.setVisible(true);
  }

void jMenuItemCadastroDescritor_actionPerformed(ActionEvent e) {
	
    frameViewClass = new JInternalFrame("Cadastro de Descritores",false, true, false,false);
    Container container = frameViewClass.getContentPane();
    final CadDescritor ci = new CadDescritor();
    container.add(ci);
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout());

      ci.addListener(
      new ObserverAdapterFechar()
       {
         public void fechar()
         {
           frameViewClass.dispose();
         }
       }
       );


    frameViewClass.pack();
    theDesktop.add(frameViewClass);
    frameViewClass.setVisible(true);
  }

void jMenuItemParametro_actionPerformed(ActionEvent e) {
	
    frameViewClass = new JInternalFrame("Parâmetros",false, true, false,false);
    Container container = frameViewClass.getContentPane();
   
	final ConfParametro ci = new ConfParametro ();
	container.add(ci);
	
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout());

     ci.addListener(
    	      new ObserverAdapterFechar()
    	       {
    	         public void fechar()
    	         {
    	           frameViewClass.dispose();
    	         }
    	       }
    	       );

    frameViewClass.pack();
    theDesktop.add(frameViewClass);
    frameViewClass.setVisible(true);
  }

void jMenuItemSair_actionPerformed(ActionEvent e) {
    try{
      this.dispose();
    } catch(Exception ex){
    }
  }

}
