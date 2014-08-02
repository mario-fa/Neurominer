package Carga;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import Carga.Load_Developer;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.spell.PlainTextDictionary;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.apache.lucene.search.spell.LevensteinDistance;

import java.util.StringTokenizer;

import javax.swing.text.StyledEditorKit.BoldAction;

//import com.sun.mail.imap.protocol.Item;

import desktop.MensagemErro;

//import util.ConverteDatas;

import Dados.AcessoBanco;
import Processing.*;

import org.apache.lucene.search.spell.JaroWinklerDistance;
import org.apache.lucene.search.spell.PlainTextDictionary;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

/**
 * 
 * @author Paulo Henrique
 *
 */
public class ProcessCarga
{
	int project_id = 1;
	public ResultSet rs_dic_n = null;
	public ResultSet rs_dic_u = null;
	public ResultSet rs_dev = null;
	public ResultSet rs_tempo = null;
	public ResultSet rs_totais = null;
	public HashMap<String, String> hp_dic_n = new HashMap<String, String>();
	public HashMap<String, String> hp_dic_u = new HashMap<String, String>();
	public int qtde_message_day_month_year = 0;
	public ArrayList<String> dic_u = new ArrayList<String>();
	public ArrayList<String> dic_n = new ArrayList<String>();
	private ArrayList<Load_Developer> developer = null;
	private ArrayList<String> random = new ArrayList<String>();
	//private ArrayList<String> 
	HashMap<String,String> hp_term_day = new HashMap<String, String>();
	HashMap<String,String> hp_term_count = new HashMap<String, String>();
	
	ArrayList<Integer> id_sentence = new ArrayList<Integer>();

	String day = "";
	int have_terms = 0;	
	
	ArrayList<String> content_day = new ArrayList<String>();
	ArrayList<String> content_day_2 = new ArrayList<String>();
	
	//Vars CheckDictionary (busca)
	int nu_dias = 0;
	int nu_key_termos_u = 0;
	int nu_key_termos_n = 0;
	int key_termo_id = 0;
		
	/**
	 * @return the rs_dic
	 */
	public ResultSet getRs_dicn() {
		return rs_dic_n;
	}

	/**
	 * @param rs the rs_dic to set
	 */
	public void setRs_dic_n(ResultSet rs_dic_n) {
		this.rs_dic_n = rs_dic_n;
	}
	
	/**
	 * @return the rs_dev
	 */
	public ResultSet getRs_dev() {
		return rs_dev;
	}

	/**
	 * @param rs_dev the rs_dev to set
	 */
	public void setRs_dev(ResultSet rs_dev) {
		this.rs_dev = rs_dev;
	}
	
	/**
	 * @return the qtde_message_day_month_year
	 */
	public int getQtde_message_day_month_year() {
		return qtde_message_day_month_year;
	}

	/**
	 * @param qtde_message_day_month_year the qtde_message_day_month_year to set
	 */
	public void setQtde_message_day_month_year(int qtde_message_day_month_year) {
		this.qtde_message_day_month_year = qtde_message_day_month_year;
	}
	
	/**
	 * Method get developer_id
	 * @param email -> Email developer added
	 * @throws SQLException if there is exception.
	 */
	public  int GetDeveloper_id(String email)
	{
		int id = 0;
		
		if (!email.equals("") )
		{
			try
			{
				AcessoBanco con = new AcessoBanco().getInstancia();
				
				String sql = "SELECT developer_id"+
							 " FROM neurominer.fact_emails"+
							 " where email ilike '"+email+"'";
				
				ResultSet rs = con.getRS(sql);
				while (rs.next())
				{
					id = Integer.parseInt(rs.getString("developer_id"));
				}				
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		}
		return id;
	}
	
	public  int GetTime_dim(String month,String year)
	{
		int id = 0;
		
		if (!month.equals("") && !year.equals(""))
		{
			try
			{
				AcessoBanco con = new AcessoBanco().getInstancia();
				
				String sql = " SELECT time_id"+
							 " FROM neurominer.time_dim"+
							 " WHERE level=2 and month = "+Integer.parseInt(month)+" and year='"+year+"'";
				System.out.println(sql);
				
				ResultSet rs = con.getRS(sql);
				while (rs.next())
				{
					id = Integer.parseInt(rs.getString("time_id"));
				}				
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		}
		return id;
	}
	
	public  int[] GetNroTotalMesesEmailDaysTerms(int developer_id,int load_id) throws Exception
	{
		int[] id = new int[4];
		id[0] = 0;//emails
		id[1] = 0;//meses
		id[2] = 0;//dias
		id[3] = 0;//termos
		
		if (developer_id != 0 && load_id != 0)
		{
			try
			{
				AcessoBanco con = new AcessoBanco().getInstancia();
				
				String sql = " SELECT \"Nro_total_meses\",\"Nro_total_emails\",\"Nro_total_dias\",terms_total"+
							 " FROM neurominer.cargas_dev"+
							 " where developer_id ="+developer_id+" and load_id = "+
							 " (select max(load_id) from neurominer.cargas_dev where developer_id ="+developer_id+")";
				
				ResultSet rs = con.getRS(sql);
				while (rs.next())
				{
					id[0] = Integer.parseInt(rs.getString("\"Nro_total_emails\""));
					id[1] = Integer.parseInt(rs.getString("\"Nro_total_meses\""));
					id[2] = Integer.parseInt(rs.getString("\"Nro_total_dias\""));
					id[3] = Integer.parseInt(rs.getString("terms_total"));
				}			
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		}
		return id;
	}
	
	public void InsertTerm_fact(int time_id, int project_id, int developer_id, int load_id, int term_number,int qtd_nu_dias, 
			 int term_total, int qtd_nu_termos, int days_number, int term_id)
	{
		try
		{
			AcessoBanco con = new AcessoBanco().getInstancia();
			
			String sql = " INSERT INTO neurominer.term_fact("+
		            	 " time_id, project_id, developer_id, load_id, term_number, daily_frequency,"+ 
			             " weight_total, term_total, term_frequency_total, days_number,term_id,total_term_id)"+
			             " VALUES ("+time_id+","+project_id+","+developer_id+","+load_id+","+term_number+","+
			             " (select neurominer.divide("+days_number+".0,"+ qtd_nu_dias+"))," +
			             " (select neurominer.divide("+days_number+".0,"+ qtd_nu_dias+")+neurominer.divide("+term_total+".0,"+ qtd_nu_termos+")),"+term_total+","+
			             " (select neurominer.divide("+term_total+".0,"+ qtd_nu_termos+")),"+days_number+","+term_id+","+qtd_nu_termos+")";

			int lines = con.exec_command(sql);		
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public  void UpdadeTerm_fact(int time_id, int project_id, int developer_id, int load_id, int term_total, int days_number, int term_id,int nro_total_dias, int terms_total)
	{
		try
		{
			AcessoBanco con = new AcessoBanco().getInstancia();
			
			String sql = " UPDATE neurominer.term_fact"+
		            	 " set daily_frequency="+days_number/nro_total_dias+", weight_total="+(days_number/nro_total_dias)+(term_total/terms_total)+
		            	 ", term_frequency_total="+term_total/terms_total+ 
			             " Where time_id = "+time_id+" and project_id="+project_id+" and  developer_id="+
			             +developer_id+" and term_id="+term_id;	             
			             
			int lines = con.exec_command(sql);		
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public  void GetRest(int time_id,int project_id, int developer_id, int load_id, int nro_total_dias, int terms_total)
	{
		try
		{
			AcessoBanco con = new AcessoBanco().getInstancia();
			
			String sql = " SELECT time_id, project_id, developer_id, load_id, term_number, daily_frequency, "+
						 " weight_total, term_total, term_frequency_total, days_number,total_term_id,"+ 
						 " term_id"+
						 " FROM neurominer.term_fact"+
						 " where time_id in "+
						 " ("+
						 " SELECT max(time_id)"+
						 " FROM neurominer.term_fact"+
						 " where time_id < "+
						 " ("+
						 " SELECT max(time_id)"+
						 " FROM neurominer.term_fact"+
						" where project_id = "+project_id+" and developer_id = "+developer_id+" and load_id = "+load_id+
						" )"+
						" and project_id = "+project_id+" and developer_id = "+developer_id+" and load_id = "+load_id+	
						" )"+
						" and "+
						" term_id not in "+
						" ("+
						" SELECT term_id"+
						" FROM neurominer.term_fact"+
						" where time_id = "+
						" ("+
						" SELECT max(time_id)"+
						" FROM neurominer.term_fact"+
						" where project_id = "+project_id+" and developer_id = "+developer_id+" and load_id = "+load_id+
						" )"+
						" and project_id = "+project_id+" and developer_id = "+developer_id+" and load_id = "+load_id+
						" )"+
						" and project_id = "+project_id+" and developer_id = "+developer_id+" and load_id = "+load_id;
			
			ResultSet rs = null;
			
			rs = con.getRS(sql);
			
			while (rs.next())
			{
				int term_total = Integer.parseInt(rs.getString("term_total"));
				int days_number = Integer.parseInt(rs.getString("days_number"));
				int term_id = Integer.parseInt(rs.getString("term_id"));
				int total_term = Integer.parseInt(rs.getString("total_term_id"));
				
				try
				{					
					String sql2 = " INSERT INTO neurominer.term_fact("+
				            	 " time_id, project_id, developer_id, load_id, term_number, daily_frequency,"+ 
					             " weight_total, term_total, term_frequency_total, days_number,term_id,total_term_id)"+
					             " VALUES ("+time_id+","+project_id+","+developer_id+","+load_id+",0,"+
					             " (select neurominer.divide("+days_number+".0,"+nro_total_dias+"))," +
					             "(select neurominer.divide("+days_number+".0,"+nro_total_dias+")+neurominer.divide("+term_total+".0,"+terms_total+"))"+
					             ","+term_total+",(select neurominer.divide("+term_total+".0,"+terms_total+")),"+days_number+","+term_id+","+total_term+")";
					
					int lines = con.exec_command(sql2);		
				}
				catch(SQLException e)
				{
					e.printStackTrace();
				}
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public  int[] GetNroTermByTime(int project_id, int developer_id,int term_id)
	{
		int[] id = new int[2];
		id[0] = 0;
		id[1] = 0;
		
		try
		{
			AcessoBanco con = new AcessoBanco().getInstancia();
			
			String sql = " SELECT term_total,days_number FROM neurominer.term_fact"+
			             " Where time_id=(" +
			             " select max(time_id) from neurominer.term_fact where" +
			             " project_id="+project_id+" and developer_id="+developer_id+
			             " and term_id="+term_id+
			             ") and project_id="+project_id+" and developer_id="+developer_id+
			             " and term_id="+term_id;
			
			ResultSet rs = con.getRS(sql);
			if (rs != null)
			{
				while (rs.next())
				{
					id[0] = Integer.parseInt(rs.getString("term_total"));
					id[1] = Integer.parseInt(rs.getString("days_number"));
				}
			}
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return id;
	}
	
	/**
	 * Method will insert new load and return id of this load
	 * @param Load_id -> id Load
	 * @throws SQLException if there is exception.
	 */
	public  int InsertCarga(String date_ini, String date_fim)
	{
		int id = 0;	
		
		try
		{
			AcessoBanco con = new AcessoBanco().getInstancia();
			
			String sql = "INSERT INTO neurominer.cargas(date, reference_date_ini, reference_date_fin, cancelada)"+
						// " VALUES (now()::date,'"+date_ini+"','"+date_fim+"', false);"+	
						 " VALUES (now()::date,'2000/08/29','2005/12/31', false)";
			String sql2 = " select currval('neurominer.cargas_load_id_seq') as developer_id;";
			int line = con.exec_command(sql);
			
     		ResultSet rs = con.getRS(sql2);
			
     		if (rs != null)
     		{     		
				while (rs.next())
				{
					id = Integer.parseInt(rs.getString("developer_id"));
				}	
     		}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return id;
	}
	
	public  void InsertCarga_dev(int load_id,int developer_id,int terms_total,int Nro_dias,int Nro_total_dias, 
			int Nro_emails,int Nro_total_emails,int terms,int Nro_meses,int Nro_total_meses)
	{
		try
		{
			AcessoBanco con = new AcessoBanco().getInstancia();
			
			String sql = "INSERT INTO neurominer.cargas_dev(load_id,"+
			             " developer_id, terms_total, \"Nro_dias\", \"Nro_total_dias\", \"Nro_emails\", "+
			             " \"Nro_total_emails\", terms, \"Nro_meses\", \"Nro_total_meses\")"+
						 " VALUES ("+load_id+","+developer_id+","+terms_total+","+Nro_dias+","+Nro_total_dias+
						 ","+Nro_emails+","+Nro_total_emails+","+terms+","+Nro_meses+","+Nro_total_meses+")";
			
			int lines = con.exec_command(sql);					
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}	
	
	public  int UpdateCarga_dev(int load_id,int developer_id,int terms_total,int Nro_dias,
									   int Nro_total_dias,int terms,int Nro_emails,int Nro_total_emails,
									   int Nro_meses, int Nro_total_meses) throws Exception
	{
		int lines = 0;
		try
		{
			AcessoBanco con = new AcessoBanco().getInstancia();
			
			String sql = " UPDATE neurominer.cargas_dev"+
						 " SET terms_total="+terms_total+"," +
						 " \"Nro_dias\"="+Nro_dias+", \"Nro_total_dias\"="+Nro_total_dias+", "+
						 " \"Nro_emails\"="+Nro_emails+", \"Nro_total_emails\"="+Nro_total_emails+
						 ", terms="+terms+", \"Nro_meses\"="+Nro_meses+", "+
						 " \"Nro_total_meses\"="+Nro_total_meses+
						 " WHERE load_id="+load_id+" and developer_id="+developer_id;
			
			lines = con.exec_command(sql);					
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return lines;
	}
	
	/**
	 * Method that Get Dictionary 
	 * @param type_word - > U = U-Gramas or N = N-Grama
	 * @return Result Set with data
	 * @throws Exception
	 */
	public void GetDictionary(String type_word)
	{
		try
		{
			AcessoBanco con = new AcessoBanco().getInstancia();
			
			String sql = "";
			
			sql = "SELECT dictionary_id, profile_id, descriptor, equivalent_stem, 'language',"+
				 "'level', weight, type_des, type_equ, final_size "+
				 "FROM neurominer.dictionary_dim "+
				 "where language like 'English' "+
				 " and type_equ like '"+type_word.toUpperCase()+"'" +
				 " and equivalent_stem not like 'null' and equivalent_stem is not null order by dictionary_id desc";			
		
			//System.out.println(sql);
			if (type_word == "N")
			{
				this.rs_dic_n =  con.getRS(sql);
			}
			else if (type_word == "U")
			{
				this.rs_dic_u =  con.getRS(sql);
			}			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}	
	}
	
	public void GetTempo(int mes,int ano,int developer_id) throws Exception
	{
		try
		{
			AcessoBanco con = new AcessoBanco().getInstancia();
			
			String sql = "";
			
			sql = " SELECT id_termo,mes,ano,sum(flag) as dias, sum(qtde) as termo"+
				  " FROM neurominer.temp_dias"+
				  " where mes = "+mes+" and ano = "+ano+ " and id_developer="+developer_id+
				  " group by mes,id_termo,ano order by mes,ano,id_termo";
		
			this.rs_tempo =  con.getRS(sql);
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}	
	}
	
	public void GetTotaisMes(int mes,int ano,int developer_id) throws Exception
	{
		try
		{
			AcessoBanco con = new AcessoBanco().getInstancia();
			
			String sql = "";
			
			sql = " SELECT sum(flag) as dias, sum(qtde) as termo"+
				  " FROM neurominer.temp_dias"+
				  " where mes = "+mes+" and id_developer="+developer_id+ " and ano="+ano+
				  " group by mes,ano,id_developer order by mes,ano,id_developer";
		
			this.rs_totais =  con.getRS(sql);
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}	
	}
	
	/**
	 * Method that Get Message by Developer 
	 * @param type - > TRUE = Get message by one developer, FALSE = Get all message - email_developer
	 * @param email_developer - > Email developer = Unique OR followed  by "|" 
	 * @return Result Set with data
	 * @throws Exception
	 */
	public void GetMessageByDeveloper(boolean type,String email_developer)
	{
		try
		{
			AcessoBanco con = new AcessoBanco().getInstancia();
			
			String sql = "";
			
			if (type && email_developer != "")
			{
				sql = "SELECT loaded_email_id, load_email_project_id, to_char(sent_date,'yyyy/MM/dd') as sent_date, message_from, "+
				      " message_to, subject, content,DATE_PART('MONTH', sent_date) AS month,DATE_PART('DAY', sent_date) AS day,"+
				      " DATE_PART('YEAR', sent_date) AS year"+
				      " FROM neurominer.loaded_email";
				String[] list_email = email_developer.split(";");
				for (int i = 0; i < list_email.length; i++)
				{
					if (sql.indexOf("where") > -1)
					{
						sql += " or message_from  ilike '%"+list_email[i]+"%' ";
					}
					else
					{
						sql += " where message_from  ilike '%"+list_email[i]+"%' ";
					}
				}
				
				sql += " order by sent_date asc";
				
				ResultSet rs_qtde = null;
				
				String sql2 = "";
				
				sql2 = " select count(*) as qtde from "+
					   " (SELECT  count(*) as t "+
					   " FROM neurominer.loaded_email";
				for (int i = 0; i < list_email.length; i++)
				{
					if (sql2.indexOf("where") > -1)
					{
						sql2 += " or message_from  ilike '%"+list_email[i]+"%' ";
					}
					else
					{
						sql2 += " where message_from  ilike '%"+list_email[i]+"%' ";
					}
				}
					   
				sql2 += " group by sent_date"+
					   " order by sent_date) as tabela";
				
				rs_qtde = con.getRS(sql2);
				if (rs_qtde != null)
				{
					while (rs_qtde.next())
					{
						setQtde_message_day_month_year(Integer.parseInt(rs_qtde.getString("qtde")));
					}					
				}
				
			}
			else if (!type && email_developer != "")
			{
				sql = "SELECT loaded_email_id, load_email_project_id, to_char(sent_date,'yyyy/MM/dd') as sent_date, message_from, "+
				      " message_to, subject, content,DATE_PART('MONTH', sent_date) AS month,DATE_PART('DAY', sent_date) AS day,"+
				      " DATE_PART('YEAR', sent_date) AS year"+
				      " FROM neurominer.loaded_email";
				String[] list_email = email_developer.split(";");
				for (int i = 0; i < list_email.length; i++)
				{
					if (sql.indexOf("where") > -1)
					{
						sql += " and message_from not ilike '%"+list_email[i]+"%' ";
					}
					else
					{
						sql += " where message_from not ilike '%"+list_email[i]+"%' ";
					}
				}
				sql += " and to_char(sent_date,'yyyy') in ('2002','2004') "+
					   " order by sent_date";
				
				ResultSet rs_qtde = null;
				
				String sql2 = "";
				
				sql2 = " select count(*) as qtde from "+
					   " (SELECT  count(*) as t "+
					   " FROM neurominer.loaded_email";
				for (int i = 0; i < list_email.length; i++)
				{
					if (sql2.indexOf("where") > -1)
					{
						sql2 += " and message_from not ilike '%"+list_email[i]+"%' ";
					}
					else
					{
						sql2 += " where message_from not ilike '%"+list_email[i]+"%' ";
					}
				}
					   
				sql2 += " and to_char(sent_date,'yyyy') in ('2002','2004') " +
						" group by sent_date"+
					   " order by sent_date) as tabela";
				
				rs_qtde = con.getRS(sql2);
				if (rs_qtde != null)
				{
					while (rs_qtde.next())
					{
						setQtde_message_day_month_year(Integer.parseInt(rs_qtde.getString("qtde")));
					}					
				}
			}
			
			this.rs_dev = con.getRS(sql);		
					
			//System.out.println(sql);
 
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}	
	}
	
	/**
     * Method will break the message in lines
     * @param message
     * @return Array message ""
     */
    public  ArrayList<String> MessageInLines(String message)
    {
    	StringBuffer str = new StringBuffer();
    	ArrayList<String> msg = new ArrayList<String>();
        String[] resultados = null;
        if (message != null)
        {
        	resultados = message.split("bkb");  
	        for (int i=0; i<resultados.length; i++)
	        {
	        	if (resultados[i] != "")
	        	{
	        		//Seting and turn in lower case
	        		str.append(msg.add(resultados[i].toLowerCase()));
	        	}
	        }
        }
    	
    	return msg;
    }
	
    //Get number total terms
    public  int GetNro_terms_total(ArrayList<String> content)
    {
    	if (content != null)
    	{	    	
	    	int count = 0;
	    	for (int i = 0; i < content.size();i++)
	    	{
	    		int c = 0;
	    		if (content.get(i) != null)
	    		{
	    			if (content.get(i) != "")
	    			{
	    				String[] terms = ltrim(content.get(i)).split(" ");
	    				c = terms.length;
	    			}
	    			else
	    			{
	    				c = 0;
	    			}
	    			
	    		}
	    		count = c + count;
	    	}
	    	
	    	return count;
    	}
    	return 0;
    }
    
	public  String RemoveBKB(String text)
	{
		//[BKB] refers to line break
    	return text.replaceAll("[BKB]", "");
	}
	
	
	public void insertTermoTemp(int dia, int mes, int ano, int idTermo,int idDeveloper) throws Exception
    {
		try
		{
			AcessoBanco con = new AcessoBanco().getInstancia();

			String sql = "";

            sql = 	" SELECT count(1) as num "+
                	" FROM neurominer.temp_dias "+
                	" where dia = "+ dia +
                	" and mes = " + mes +
                	" and ano = " + ano +
                	" and id_termo = " + idTermo + 
                	" and id_developer = "+ idDeveloper;
	
             ResultSet rs1 = con.getRS(sql);
			 rs1.next();
			 int num = rs1.getInt("num");            
			 int lines = 0;
			 if (num > 0)
			 {
				 String sqlU = "UPDATE neurominer.temp_dias set qtde = qtde  + 1 where dia = " + dia +
			                   " and mes = " + mes + " and ano = " + ano + " and id_termo="+idTermo + " and id_developer="+idDeveloper;                                                                  
            	lines = con.exec_command(sqlU);
           		if (lines <= 0)
             	System.out.println("Problema na atualizacao de stem: " + sqlU);
			 }
			 else
			 {
				 String sqlI = "INSERT INTO neurominer.temp_dias(dia, mes, ano, id_termo, flag, qtde,id_developer) "+
			                    "VALUES(" + dia + ", " + mes + ", " + ano + ", " + idTermo + ", 1 , 1,"+idDeveloper+")";
			     lines = con.exec_command(sqlI);
			     if (lines <= 0)
			    	 System.out.println("Problema na atualizacao de stem: " + sqlI);
			 }
       }
       catch(SQLException e)
       {
    	   e.printStackTrace();
       }
    }
	
	
	/**
     * Method will check if there is value in then text
     * @param text
     * @param type_search - > N = n-grama e U = u-grama
     * @return text where there is value or ""
     */
    public void CheckDictionary(ArrayList<String> dictionary,ArrayList<String> sentence,int Load_id,int Developer_id,String type_search,String month,String year,String day)
    {
    	nu_dias = 0;
    	nu_key_termos_u = 0;
    	nu_key_termos_n = 0;	
    	
    	ArrayList<String> rs = new ArrayList<String>(); 
    	rs = dictionary;
    	boolean result = false;
    	try
    	{
    		
    		HashMap<String, String> hp_day = new HashMap<String, String>();
    		
    		// 0. Specify the analyzer for tokenizing text.
    	    //The same analyzer should be used for indexing and searching
    	    StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);

    	    // 1. create the index
    	    Directory index = new RAMDirectory();

    	    // the boolean arg in the IndexWriter ctor means to
    	    // create a new index, overwriting any existing index
    	    IndexWriter w = new IndexWriter(index, analyzer, true, IndexWriter.MaxFieldLength.UNLIMITED);
    		
    	    
	    	for (int i = 0;i < sentence.size(); i++)
	    	{	    		
	    		addDoc(w, sentence.get(i) + " ["+i+"]");	    	    
	    	}
	    	
	    	w.close();
	    	
	    	for (int i = 0;i < dictionary.size(); i++)
	    	{
		        // 2. query
		        //String querystr = args.length > 0 ? args[0] : "lucene for ten"; // Usando Levenstein Distance	    	
				
				PhraseQuery pq = new PhraseQuery();
				pq.setSlop(2);
				
				String[] words = null;
				String words2 = "";
				
				if (type_search.equals("N"))
	        	{					
					words = dictionary.get(i).split(" "); 
					// Here you put the dictionary
			        for (int j = 0; j < words.length; j++)
			        {
			        	if (!words[j].equals(""))
			        	{
			        		pq.add(new Term("title",words[j].toString().toLowerCase()));
			        	}
			        }
	        	}
	
				Query q = new QueryParser(Version.LUCENE_CURRENT, "title", analyzer).parse(pq.toString());
		       
		        // 3. search
		        int hitsPerPage = 20; // aqui tem q ser o tamanho do meu texto, pois temos q selecionar todas as frases com termos do n-grma
		        IndexSearcher searcher = new IndexSearcher(index, true);
		        TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
		        searcher.search(q, collector);
		        //searcher.search(q, 10);
		        ScoreDoc[] hits = collector.topDocs().scoreDocs;		       
		        
		        ArrayList<String> sentence_finded = new ArrayList<String>();
		        ArrayList<String> dics = new ArrayList<String>();
		        boolean find = false;
		        
		        // 4. display results
		        //Cáculo da carga
		        //System.out.println("Found " + hits.length + " hits.");
		        for(int k = 0; k < hits.length;++k)
		        {
		        	have_terms = 1;
		        	
		        	int docId = hits[k].doc;
		        	Document d = searcher.doc(docId);
		        	System.out.println((k + 1) + ". " + d.get("title"));
		        	//System.out.println("dim: " +dictionary.get(i));
		        	
		        	int id = GetSentenceById(d.get("title"));
		        	String newPhrase = sentence.get(id);
		        	//System.out.println("sentence original :" + sentence.get(id));
		        	
		        	//Adicianando index de sentença existente
		        	id_sentence.add(id);
		        	
		        	if (type_search.equals("N"))
		        	{	
			        	for (int j = 0; j < words.length; j++)
				        {			        	
				        	newPhrase = newPhrase.replaceFirst(words[j], "");
				        }
		        	}

		        	//System.out.println("sentence nova :" + newPhrase);
        		        	
		        	if (type_search.equals("N"))
		        	{
		        		int id_termo = Integer.parseInt(hp_dic_n.get(dictionary.get(i)));
		        		insertTermoTemp(Integer.parseInt(day), Integer.parseInt(month), Integer.parseInt(year), id_termo,Developer_id);
		        	}
		        	       	
		        	CheckSteam(dic_u, newPhrase,month,year,Integer.parseInt(day),Developer_id); 	
		        }		        
		        
		        // searcher can only be closed when there
		        // is no need to access the documents any more.
		        searcher.close();
	    	}
    	}
    	catch (Exception e)
    	{
			// TODO: handle exception
    		e.printStackTrace();
		}

    	//return sentence;
    }    
    
    /*
     * Directory diretorioDicionario = new RAMDirectory();
       SpellChecker sp = new SpellChecker(diretorioDicionario);
       sp.indexDictionary(new PlainTextDictionary(new File("dicionario.txt")));
     * Return O id to termo do dicionário. Caso nao exista return Zero
     */
    public int findStem(String st, SpellChecker sp) throws IOException
    {
        int id = 0;
        String pesquisa = st +"~";
        int numeroDeSugestoes = 1;
        //número de sugestões similares
        String[] similares = sp.suggestSimilar(pesquisa, numeroDeSugestoes); //Usando Levenstein Distance
        for (String palavra : similares)
        {
            id = Integer.parseInt(hp_dic_u.get(palavra));
        }
        return id;
    }
    
    public void CheckSteam(ArrayList<String> dic, String frase,String month,String year,int day,int idDeveloper) throws NumberFormatException, Exception
    {
    	StringTokenizer st = new StringTokenizer(frase);
   	
        while (st.hasMoreTokens())
        {		
        	String tok = st.nextToken();
        	//System.out.println("Token: "+tok);

        	LevensteinDistance ls = new LevensteinDistance();
            
        	float distancia = 0;
        	String steam = "";
        	
        	for (int i = 0; i < dic.size(); i++)
        	{
	        	distancia = ls.getDistance(tok, dic.get(i));
	        	if (distancia >= 0.9)// termos iguais
				{
	        		steam = dic.get(i);
	        		break;
				}
        	}
        	if (!steam.equals(""))// termos iguais
			{
        		have_terms = 1;
				System.out.println("Achou Ugrama : "+tok);
				insertTermoTemp(day, Integer.parseInt(month), Integer.parseInt(year), Integer.parseInt(hp_dic_u.get(steam)),idDeveloper);
			}
        }
    }
    
    public int GetSentenceById(String id)
    {
    	int index = Integer.parseInt(id.substring(id.indexOf("[")+1, id.indexOf("]")));
    	    	
    	return index;
    }
    
    public  int GetDicionaryById(String id)
    {
    	int index = Integer.parseInt(id.substring(id.indexOf("[")+1, id.indexOf("]")-1));
    	    	
    	return index;
    }
    	    
    public  String[] GetDays_extreme(String year,String month, Load_Developer[] ld)
    {
    	String[] days = new String[2];
    	
    	int day_least = 32;
    	int day_biger = 1;
    	
    	for (int j = 0; j < ld.length; j++)
		{
    		if (ld[j].getYear().equals(year) && ld[j].getMonth().equals(month))
    		{
    			if (Integer.parseInt(ld[j].getDay()) < day_least)
    			{
    				day_least = Integer.parseInt(ld[j].getDay());
    				days[0] = ld[j].getDate();
    			}
    			if (Integer.parseInt(ld[j].getDay()) > day_biger)
    			{
    				day_biger = Integer.parseInt(ld[j].getDay());
    				days[1] = ld[j].getDate();
    			}
    		}
		}
    	
    	return days;
    }
    
	
   
    public void Process() throws NumberFormatException, Exception
	{
		
		GetDictionary("N");
		// Getting descriptor of the dintionary
		if (this.rs_dic_n != null)
		{
			while (this.rs_dic_n.next())
			{
				hp_dic_n.put(rs_dic_n.getString("equivalent_stem"), rs_dic_n.getString("dictionary_id"));				
				dic_n.add(rs_dic_n.getString("equivalent_stem"));
			}
		}
	
		GetDictionary("U");
		// Getting descriptor of the dintionary
		if (this.rs_dic_u != null)
		{
			while (rs_dic_u.next())
			{
				hp_dic_u.put(rs_dic_u.getString("equivalent_stem"), rs_dic_u.getString("dictionary_id"));				
				dic_u.add(rs_dic_u.getString("equivalent_stem"));
			}
		}	
		
		//cluster
		String list_email = "nd@apache.org;nd@perlig.de;rbbloom@us.ibm.com;rbb@raleigh.ibm.com;rbb@hyperreal.org;rbb@locus.apache.org;rbb@apache.org;"+
							"dgaudet@arctic.org;dean@arctic.org;dgaudet@hyperreal.org;wrowe@rowe-clan.net;wrowe@apache.org;admin@rowe-clan.net;wrowe@covalent.net;wrowe@lnd.com";
		
		
		//String list_email = "nd@apache.org;nd@perlig.de";
		//String list_email = "rbbloom@us.ibm.com;rbb@raleigh.ibm.com;rbb@hyperreal.org;rbb@locus.apache.org;rbb@apache.org";				
		//String list_email = "dgaudet@arctic.org;dean@arctic.org;dgaudet@hyperreal.org";
		//String list_email = "wrowe@rowe-clan.net;wrowe@apache.org;admin@rowe-clan.net;wrowe@covalent.net;wrowe@lnd.com"; //Alterar para lista WROWE
		//String list_email = "wrowe@lnd.com";
		String email_developer = "nd@apache.org";
		int qtde_month = 0;
		HashMap<String,String> list_month_year = new HashMap<String, String>();
		HashMap<String,String> list_qtde_email_month_year = new HashMap<String, String>(); 
		String  prior_month = "";
		String  prior_year = "";
		//quantidade de emails da carga geral
		int count_emails = 0;
		
		GetMessageByDeveloper(false,list_email);
		
		//System.out.println("tamnanho do vetor LD : "+getQtde_message_day_month_year());
		Load_Developer[] ld = new Load_Developer[getQtde_message_day_month_year()];
		
		//Getting content by developer
		if (this.rs_dev != null)
		{
			int count = 0;			
			
			while (rs_dev.next())
			{
				//Observação para contents vazios
				if (ld[0] ==  null && count == 0)
				{
					//loaded_email_id, load_email_project_id, sent_date, message_from,  message_to, subject, 'content',month,day,year
					//String email, String year, String month, String day,String content
					ld[count] = new Load_Developer(list_email,rs_dev.getString("sent_date"),rs_dev.getString("year"),
													rs_dev.getString("month"),rs_dev.getString("day"),rs_dev.getString("content"));			
				}
				else
				{
					if (ld[count].getDate().equals(rs_dev.getString("sent_date")))
					{
						ld[count].setContent(ld[count].getContent() + rs_dev.getString("content"));
					}
					
					else
					{
						count++;
						ld[count] = new Load_Developer(list_email,rs_dev.getString("sent_date"),rs_dev.getString("year"),
								rs_dev.getString("month"),rs_dev.getString("day"),rs_dev.getString("content"));		
					}
				}				
				
				if (!prior_month.equals(rs_dev.getString("month")) || (prior_month.equals(rs_dev.getString("month")) 
						&& !prior_year.equals(rs_dev.getString("year"))))
				{				
					list_month_year.put(qtde_month+"",rs_dev.getString("month")+";"+rs_dev.getString("year")+";");
					qtde_month++;
					//count_emails = 1;
				}				
				
				prior_month = rs_dev.getString("month");
				prior_year = rs_dev.getString("year");
				//list_qtde_email_month_year.put(prior_year+prior_month, count_emails+"");
				count_emails++;
			}		
		}
			

		//num total de termos da carga
		int	terms = 0;
		
		//ConverteDatas cd = new ConverteDatas();			
		/*
		Date date_min = cd.strToDate("2000/08/29","yyyy/MM/dd");
		Date date_max = cd.strToDate("2005/12/31","yyyy/MM/dd");
		*/
		
		//System.out.println("count ld ="+ld.length);
		
		for (int i = 0;i < ld.length;i++)
		{
			/*
				if (cd.strToDate(ld[i].getDate(),"yyyy/MM/dd").before(date_min))
				{
					date_min = cd.strToDate(ld[i].getDate(),"yyyy/MM/dd");
				}
				if (cd.strToDate(ld[i].getDate(),"yyyy/MM/dd").after(date_max))
				{
					date_max = cd.strToDate(ld[i].getDate());
				}
				*/
			
			//if (ld[i] != null)
			//{
			
				if (ld[i].getContent() != "")
				{
					//lembrar de pega a linha anterior
					terms = terms + GetNro_terms_total(MessageInLines(ld[i].getContent()));
				}
			//}			
		}
		
		//Getting developer_id
		//int Developer_id = GetDeveloper_id(email_developer);
		//int Developer_id = 3;//Cluster 3;
		int Developer_id = 4;//Cluster 2;
		
		//String[] days = GetDays_extreme(year, month, ld);
		
		//Adding new carga and getting your load_id
		int Load_id = InsertCarga("2000/01/31","2005/12/31");
		
		//int id[] = GetNroTotalMesesEmailDaysTerms(Developer_id,Load_id);
		int Nro_total_emails = count_emails;//Mudar quanda tiver tempo
		int	Nro_total_meses = qtde_month;
		int	Nro_total_dias = ld.length;
		int Nro_emails = count_emails;
		int Nro_dias = ld.length;
		int Nro_meses = qtde_month;
		int Nr_term = terms;
		int Nr_total_terms = terms;
		//InsertCarga_dev(load_id, developer_id, terms_total, Nro_dias, Nro_total_dias, Nro_emails, Nro_total_emails, terms, Nro_meses, Nro_total_meses)
		InsertCarga_dev(Load_id,Developer_id,Nr_total_terms,Nro_dias,Nro_total_dias,Nro_emails,Nro_total_emails,Nr_term,Nro_meses,Nro_total_meses);
							
		HashMap<Integer, Integer> hp_qtde_days_number = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> hp_qtde_terms_total = new HashMap<Integer, Integer>();
		
		int hp_qtde_days_number_total = 0;
		int hp_qtde_terms_total_total = 0;
				
		//Getting months
		for (int i = 0; i < qtde_month;i++)
		{
			have_terms = 0;
			
			String month_year = list_month_year.get(i+"");
			String month = month_year.split(";")[0].toString();
			String year = month_year.split(";")[1].toString();

			boolean find = false;
			
			int	key_terms_total = 0;
		
			random = null;
			
        	ArrayList<String> random_x = new ArrayList<String>();
			
			for (int x = 0;x<100000;x++)
			{
				random_x.add(month_year+"a"+x);
			}
			
			random = random_x;
			
			int term_day = 0;				
			int nro_term_month = 0;		
			
			int time_id = GetTime_dim(month, year);
			
			for (int j = 0; j < ld.length; j++)
			{					
				if (ld[j].getMonth().equals(month) && ld[j].getYear().equals(year))
				{
					day = ld[j].getDay();
					
					//Levando-se em conta que as mensagens vem ordenadas por data
					nro_term_month = nro_term_month + GetNro_terms_total(MessageInLines(ld[j].getContent()));
					
					//Cáculo da carga diário
					//cargas_dev
					//int	developer_id; id developer					
					
					int key_term = 0;
					
					//int	load_id; id carga						
					//term_fact
					
					content_day = MessageInLines(ld[j].getContent());
					//System.out.println("sentence_day:"+content_day.size());
					
					boolean achou = false;
					/*
					boolean achou = CheckDictionary(dic_n, content_day,Load_id,Developer_id,"N",month,year,day);
					
					while (achou)
					{
						achou = CheckDictionary(dic_n, content_day,Load_id,Developer_id,"N",month,year,day);
					}
					*/										
					CheckDictionary(dic_n, content_day,Load_id,Developer_id,"N",month,year,day);
					
					//System.out.println("Contents encontrados: "+id_sentence.size());
					
					for (int a = 0; a < content_day.size(); a++)
					{
						boolean fd = false;
						
						for (int b = 0; b < id_sentence.size(); b++)
						{
							if (a == id_sentence.get(b))
							{
								fd = true;
							}
						}	
						
						if (!fd)
						{
				        	CheckSteam(dic_u, content_day.get(a) ,month,year,Integer.parseInt(day),Developer_id);
						}
					}
				}
			}
			
			
			if (have_terms > 0)
			{
				GetTempo(Integer.parseInt(month),Integer.parseInt(year),Developer_id);
				
				//pega total mes
            	GetTotaisMes(Integer.parseInt(month),Integer.parseInt(year), Developer_id);
            	
            	if (rs_totais != null)
				{
	            	while (rs_totais.next())
		    		{
		            	hp_qtde_days_number_total =  hp_qtde_days_number_total + rs_totais.getInt("dias");
		            	hp_qtde_terms_total_total =  hp_qtde_terms_total_total + rs_totais.getInt("termo");
		    		}
				}
				
				//gravando term_fact
				if (rs_tempo != null)
				{
					
		    		while (rs_tempo.next())
		    		{    			
		            	int id_term = Integer.parseInt(rs_tempo.getString("id_termo"));
		            	/*
		            	int[] totals = GetNroTermByTime(project_id, Developer_id,id_term);
		            	int term_total = totals[0];
		            	int days_number = totals[1];
		            	*/
		            	
		            	int count_by_term = rs_tempo.getInt("termo");
		            	int count_by_day  =  rs_tempo.getInt("dias");
		            	
		            	int days = 0;
		            	if (hp_qtde_days_number.get(id_term) != null)
		            	{
		            		days = hp_qtde_days_number.get(id_term);
		            	}
		            	int term = 0;
		            	if (hp_qtde_terms_total.get(id_term) != null)
		            	{
		            		term = hp_qtde_terms_total.get(id_term);
		            	}
		            	
		            	hp_qtde_days_number.put(id_term, days + count_by_day);
		            	hp_qtde_terms_total.put(id_term, term + count_by_term);
		            	
		            	
		            	//Pegar o projeto
		            	int project_id = 1;
		            	
		            	InsertTerm_fact(time_id, project_id, Developer_id, Load_id, count_by_term, hp_qtde_days_number_total, hp_qtde_terms_total.get(id_term), hp_qtde_terms_total_total, hp_qtde_days_number.get(id_term),id_term);
		            	
		            	/*
		            	//time_id, project_id, developer_id, load_id, term_number, daily_frequency,weight_total, term_total, term_frequency_total, days_number,term_id)
		            	InsertTerm_fact(time_id, 1, Developer_id, Load_id, count_by_term, count_by_day,hp_qtde_days_number_total,hp_qtde_days_number.get(id_term),
		            			hp_qtde_terms_total.get(id_term),count_by_term,hp_qtde_terms_total.get(id_term),
		            			hp_qtde_days_number.get(id_term), id_term);
		            	*/
		    		}
				}
				
				GetRest(time_id, 1, Developer_id, Load_id, hp_qtde_days_number_total, hp_qtde_terms_total_total);
			}
		}
	}
	
	public  String ltrim(String str)
	{
	  if(str==null) return "";
	  if (str.trim().equals("")) return "";

	  while (str.charAt(0) == ' ')
	  {
	       str = str.substring(1);
	  }
	  return str;
	}
	
	/**
	 * Add value to index Write
	 * @param w = Index Writer
	 * @param value index
	 * @throws IOException
	 */
	private  void addDoc(IndexWriter w, String value) throws IOException
	{
		Document doc = new Document();
	    doc.add(new Field("title", value, Field.Store.YES, Field.Index.ANALYZED));
	    w.addDocument(doc);
	}

	public ProcessCarga()
	{
		this.project_id = 0;
		this.rs_dic_n = null;
		this.rs_dic_u = null;
		this.rs_dev = null;
		this.rs_tempo = null;
		this.hp_dic_n = new HashMap<String, String>();
		this.hp_dic_u = new HashMap<String, String>();
		this.qtde_message_day_month_year = 0;
		this.dic_u = new ArrayList<String>();
		this.dic_n = new ArrayList<String>();
		this.developer = new ArrayList<Load_Developer>();
		this.random = new ArrayList<String>();
		this.hp_term_day = new HashMap<String, String>();
		this.hp_term_count = new HashMap<String, String>();
		this.day = "";
		this.have_terms = 0;
		this.content_day = new ArrayList<String>();
		this.nu_dias = 0;
		this.nu_key_termos_u = 0;
		this.nu_key_termos_n = 0;
		this.key_termo_id = 0;
	}
}
