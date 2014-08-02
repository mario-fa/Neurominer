package cargaBanco;
import java.sql.ResultSet;
import java.sql.SQLException;

import bancoDados.*;





public class Carga {

	public static final int DESCRITOR = 1;
	public static final int TG = 2;
	public static final int TE = 3;
	public static final int TR = 4;
	public static final int UP = 5;
	public static final int CAT = 6;
	public static final int USE = 7;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		   
		    
		    AcessoBanco acessoBanco = new AcessoBanco();
		   
		   String lstrSQL;
		   
	   
		   
	       int estado=0;
	       //*****************************************************************************
           // ************************* COLOCADO PARA EVITAR INICIO INDEVIDO DA ROTINA
	       if (estado==0) return;
	       // COMENTE A LINHA ACIMA SE QUISER LIBERAR O CODIGO
	       // *****************************************************************************
	       
	       String campo;
	       String descritor, codDescritor=null, codTermoGenerico=null, codTermoEspecifico=null, codTermoRelacionado=null, codTermoEquivalente=null;
	       String codSubCategoria=null, cod_categoria= null;
	       int extremo1, extremo2;
	       try
	          {	       
	    	   
		  System.out.print("Apagando dados das tabelas \n");
		  lstrSQL = "delete from descritorTermosGenericos";
		  acessoBanco.exec_command(lstrSQL);	
		  lstrSQL = "delete from descritorTermosRelacionados";
		  acessoBanco.exec_command(lstrSQL);
		  lstrSQL = "delete from descritorTermosEspecificos";
		  acessoBanco.exec_command(lstrSQL);	
		  lstrSQL = "delete from descritorEquivalencias";
		  acessoBanco.exec_command(lstrSQL);	  
		       
		   lstrSQL = "select * FROM TEJUR_23_11_NOVO WHERE TEJUR_23_11_NOVO.Campo1<>'' ";     
		   ResultSet rs = acessoBanco.getRS(lstrSQL);
	       while (rs.next()){
               campo = rs.getString("Campo1");

               if (campo.contains("("))
	    		 estado = DESCRITOR;
		       if (campo.contains("TG"))
			     estado = TG;
			   if (campo.contains("TE"))
				 estado = TE;
			   if (campo.contains("TR"))
				 estado = TR;
			   if (campo.contains("UP"))
			     estado = UP;
			   if (campo.contains("CAT"))
				 estado = CAT;
			   if (campo.contains("USE"))
				 estado = USE;			    		 
	        
			   switch (estado) {
			      case DESCRITOR:
			    	  descritor = rs.getString("Campo3").trim();
			    	  lstrSQL = "select * FROM descritor WHERE descritor.desDescritor = '" + descritor + "'"; 
			    	  ResultSet rs2 = acessoBanco.getRS(lstrSQL);
				      if (rs2.next()){
			    	    codDescritor = rs2.getString("codDescritor");
			    	    System.out.print ("---------------------------------------- \n");
                        System.out.print ("DESCRITOR - " + codDescritor + " " + rs2.getString("desDescritor") + "\n");
				      }
			    	  break;
			      case TG:
			    	  descritor = rs.getString("Campo3").trim();
			    	  lstrSQL = "select * FROM descritor WHERE descritor.desDescritor = '" + descritor + "'"; 
			    	  ResultSet rs3 = acessoBanco.getRS(lstrSQL);
				      if (rs3.next()){
			    	    codTermoGenerico = rs3.getString("codDescritor");
			    	    System.out.print ("TERMO GENÉRICO - " + codTermoGenerico + " " + rs3.getString("desDescritor") + "\n");
			  		  lstrSQL = "insert into descritorTermosGenericos values (" + codDescritor + "," + codTermoGenerico + ")";
					  acessoBanco.exec_command(lstrSQL);				
				      }
			    	  break;
			    	  
				  case TE:
				      descritor = rs.getString("Campo3").trim();
				      lstrSQL = "select * FROM descritor WHERE descritor.desDescritor = '" + descritor + "'"; 
				      ResultSet rs4 = acessoBanco.getRS(lstrSQL);
					 if (rs4.next()){
				       codTermoEspecifico = rs4.getString("codDescritor");
				       System.out.print ("TERMO ESPECIFICO - " + codTermoEspecifico + " " + rs4.getString("desDescritor") + "\n");
				  	   lstrSQL = "insert into descritorTermosEspecificos values (" + codDescritor + "," + codTermoEspecifico + ")";
					   acessoBanco.exec_command(lstrSQL);	
					 }
				     break;	

			      case TR:
					 descritor = rs.getString("Campo3").trim();
					 lstrSQL = "select * FROM descritor WHERE descritor.desDescritor = '" + descritor + "'"; 
					 ResultSet rs5 = acessoBanco.getRS(lstrSQL);
					 if (rs5.next()){
					    codTermoRelacionado = rs5.getString("codDescritor");
					    System.out.print ("TERMO RELACIONADO - " + codTermoRelacionado + " " + rs5.getString("desDescritor") + "\n");
					  	lstrSQL = "insert into descritorTermosRelacionados values (" + codDescritor + "," + codTermoRelacionado + ")";
						acessoBanco.exec_command(lstrSQL);	
					 }
					 break;	
				    	  
			      case UP:
						 descritor = rs.getString("Campo3").trim();
						 lstrSQL = "select * FROM descritor WHERE descritor.desDescritor = '" + descritor + "'"; 
						 ResultSet rs6 = acessoBanco.getRS(lstrSQL);
						 if (rs6.next()){
						    codTermoEquivalente = rs6.getString("codDescritor");
						    System.out.print ("TERMO EQUIVALENTE - " + codTermoEquivalente + " " + rs6.getString("desDescritor") + "\n");
						  	lstrSQL = "insert into descritorEquivalencias values (" + codDescritor + "," + codTermoEquivalente + ")";
							acessoBanco.exec_command(lstrSQL);	
						 }
						 break;					    	  

			      case CAT:
						 descritor = rs.getString("Campo3").trim();
						 
						 extremo1 = 0;
						 do {
							 extremo2 = descritor.indexOf("/", extremo1);
							 if (extremo2 == -1){
								 extremo2 = descritor.length();
							 }
							 cod_categoria = descritor.substring(extremo1, extremo2);
							 	 
							 lstrSQL = "select * FROM subcategoria WHERE subcategoria.SigSubCategoria = '" + cod_categoria + "'"; 
							 ResultSet rs7 = acessoBanco.getRS(lstrSQL);
							 if (rs7.next()){
							    codSubCategoria = rs7.getString("codSubCategoria");
							    System.out.print ("SUBCATEGORIA - " + codSubCategoria + " " + rs7.getString("desSubcategoria") + "\n");
							  	lstrSQL = "insert into descritorSubCategorias values (" + codDescritor + "," + codSubCategoria + ")";
								acessoBanco.exec_command(lstrSQL);
							 }
							 extremo1 = extremo2 + 1;
						 }
						 while (extremo2 != descritor.length());
						 break;	
			      case USE:
						rs.next();
						break;	
			   }
			   
			          }
	          }
	          catch (SQLException e) { System.out.print(e.getMessage());}
	    }

	}


