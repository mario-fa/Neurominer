package Dictionary;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import Processing.*;
import Dados.AcessoBanco;

/**
 * 
 * @author Paulo Henrique
 *
 */
public class ProcessCarga
{
	public ResultSet rs = null;
	public ArrayList<String> dim = null;
	
	/**
	 * @return the rs
	 */
	public ResultSet getRs() {
		return rs;
	}

	/**
	 * @param rs the rs to set
	 */
	public void setRs(ResultSet rs) {
		this.rs = rs;
	}
	
	/**
	 * Method that Get Dictionary 
	 * @param type_word - > U = U-Gramas or N = N-Grama
	 * @return Result Set with data
	 * @throws Exception
	 */
	public void GetDictionary(String type_word) throws Exception
	{
		try
		{
			AcessoBanco con = new AcessoBanco().getInstancia();
			
			String sql = "";
			
			sql = "SELECT dictionary_id, profile_id, descriptor, equivalent_stem, 'language',"+
				 "'level', weight, type_des, type_equ, final_size "+
				 "FROM neurominer.dictionary_dim "+
				 "where language like 'English' "+
				 " and type_des like '"+type_word.toUpperCase()+"' order by descriptor";
		
			//System.out.println(sql);
			this.rs =  con.getRS(sql);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}	
	}
	
	/**
	 * Method that Get Message 
	 * @param email - > Email developer
	 * @return Result Set with data
	 * @throws Exception
	 */
	public void GetMessageByDeveloper(String email_developer) throws Exception
	{
		try
		{
			AcessoBanco con = new AcessoBanco().getInstancia();
			
			String sql = "";
			
			sql = "SELECT loaded_email_id, load_email_project_id, sent_date, message_from,"+ 
			      " message_to, subject, 'content'"+
			      " FROM neurominer.loaded_email ";		
		
			//System.out.println(sql);
			this.rs =  con.getRS(sql);
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
    public static ArrayList<String> MessageInLines(String message)
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
	
	public static String RemoveBKB(String text)
	{
		//[BKB] refers to line break
    	return text.replaceAll("[BKB]", "");
	}
	/**
     * Method will check if there is value in then text
     * @param text
     * @return text where there is value or ""
     */
    public static ArrayList<String> CheckDictionary(ArrayList<String> dictionary,ArrayList<String> sentence)
    {
    	ArrayList<String> rs = new ArrayList<String>(); 
    	rs = dictionary;
    	boolean result = false;
    	try
    	{
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
				words = dictionary.get(i).split(" "); 
				// Here you put the dictionary
		        for (int j = 0; j < words.length; j++)
		        {
		        	if (words[j] != "")
		        	{
		        		pq.add(new Term("title",words[i].toString().toLowerCase()));
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
	
		        // 4. display results
		        System.out.println("Found " + hits.length + " hits.");
		        for(int k = 0; k < hits.length;++k)
		        {
		        	int docId = hits[i].doc;
		        	Document d = searcher.doc(docId);
		        	System.out.println((i + 1) + ". " + d.get("title"));
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
    	
    	return sentence;
    }
	
	public void Process() throws Exception
	{
		GetDictionary("N");
		
		// Getting descriptor of the dintionary
		if (this.rs != null)
		{
			while (rs.next())
			{
				dim.add(rs.getString("descriptor"));
			}
		}
		
		
		
	}
	
	/**
	 * Add value to index Write
	 * @param w = Index Writer
	 * @param value index
	 * @throws IOException
	 */
	private static void addDoc(IndexWriter w, String value) throws IOException
	{
		Document doc = new Document();
	    doc.add(new Field("title", value, Field.Store.YES, Field.Index.ANALYZED));
	    w.addDocument(doc);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		
		
		// TODO Auto-generated method stub
	}
}
