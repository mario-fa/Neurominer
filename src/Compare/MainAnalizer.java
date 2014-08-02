package Compare;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;

import org.apache.lucene.index.Term;

/**
 * @author marioandre
 */
public class MainAnalizer {

	public static String ltrim(String str)
	{
	  if(str==null) return "";
	  if (str.trim().equals("")) return "";

	  while (str.charAt(0) == ' ')
	  {
	       str = str.substring(1);
	  }
	  return str;
	}
	
	
	public static void recusivo()  throws IOException, ParseException 
	{
		
		
		
		  // 0. Specify the analyzer for tokenizing text.
	    //    The same analyzer should be used for indexing and searching
	    StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);

	    // 1. create the index
	    Directory index = new RAMDirectory();

	    // the boolean arg in the IndexWriter ctor means to
	    // create a new index, overwriting any existing index
	    IndexWriter w = new IndexWriter(index, analyzer, true, IndexWriter.MaxFieldLength.UNLIMITED);

	    addDoc(w, "Lucene A Action for about ten days here in brazil kkkkkkk Action for about ten eeeeeeee nnnn xxxx tttt dddd jjjjjjj k");
	    addDoc(w, "Lucene in for days ten here in brazil kkkkkkk eeeeeeee nnnn xxxx tttt dddd jjjjjjj k");
	    addDoc(w, "quiet hear call alpha  [2]");
	    addDoc(w, "Lucene for Dummies");
	    addDoc(w, "Lucene for Dummies");
	    addDoc(w, " Action for about ten");
	    addDoc(w, "Lucene for Dummies");
	    addDoc(w, "Lucene for Dummies");
	    addDoc(w, "Lucene for Dummies");
	    addDoc(w, "Lucene for Dummies");
	    addDoc(w, "Lucene for Dummies");
	    addDoc(w, "Managing Gigabytes");
	    addDoc(w, "The Art of Computer Science");
	    addDoc(w, "The Art of Computer Science");
	    addDoc(w, "The Art of Computer Science");
	    addDoc(w, "The Art of Computer Science");
	    addDoc(w, "The Art of Computer Science");
	    addDoc(w, "The Art of Computer Science");
	    addDoc(w, "The Art of Computer Science");
	    addDoc(w, "The Art of Computer Science");
	    addDoc(w, "The Art of Computer Science");

	    w.close();

	    // 2. query
	    //String querystr = args.length > 0 ? args[0] : "lucene for ten"; // Usando Levenstein Distance

	    PhraseQuery q1 = new PhraseQuery();
	    q1.setSlop(2);
	    q1.add(new Term("title","quiet"));
	    q1.add(new Term("title","hear"));
	    q1.add(new Term("title","call"));
	    //SystManagingrint("Query: " + q1.toString());

	   Query q = new QueryParser(Version.LUCENE_CURRENT, "title", analyzer).parse(q1.toString());
	 //   Query q = new QueryParser(Version.LUCENE_CURRENT, "title", analyzer).parse(querystr);
	   // Query q = new FuzzyQuery(new Term("title", "Lucene for Dummies"),new Float(0.2));

	 

	    
	    // 3. search
	    int hitsPerPage = 20; // aqui tem q ser o tamanho do meu texto, pois temos q selecionar todas as frases com termos do n-grma
	    IndexSearcher searcher = new IndexSearcher(index, true);
	    TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
	    searcher.search(q, collector);
	    //searcher.search(q, 10);
	    ScoreDoc[] hits = collector.topDocs().scoreDocs;

	    // 4. display results
	    System.out.println("Found " + hits.length + " hits.");
	    for(int i=0;i<hits.length;++i) {
	      int docId = hits[i].doc;
	      Document d = searcher.doc(docId);
	      System.out.println((i + 1) + ". " + d.get("title"));
	    }

	    // searcher can only be closed when there
	    // is no need to access the documents any more.
	    searcher.close();
	}
	
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ParseException 
    {
    	BigDecimal a = new BigDecimal(0); 

    	BigDecimal t = a.divide(new BigDecimal(1000000));
    	
    	System.out.println(t+"");
    	

    	
      // 0. Specify the analyzer for tokenizing text.
    //    The same analyzer should be used for indexing and searching
    StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);

    // 1. create the index
    Directory index = new RAMDirectory();

    // the boolean arg in the IndexWriter ctor means to
    // create a new index, overwriting any existing index
    IndexWriter w = new IndexWriter(index, analyzer, true, IndexWriter.MaxFieldLength.UNLIMITED);

    addDoc(w, "quiet call look see throw");
    addDoc(w, "juliana call look see throw");
    addDoc(w, "maria call look see throw");

   

    // 2. query
    //String querystr = args.length > 0 ? args[0] : "lucene for ten"; // Usando Levenstein Distance
    
    String[] test = new String[4];
    test[0] = "quiet";
    test[1] = "call";
    test[2] = "look";
    test[3] = "see";
    
    for (int i = 0; i< 4;i++)
    {
	    PhraseQuery q1 = new PhraseQuery();
	    q1.setSlop(2);
	
	    q1.add(new Term("title",test[i]));
	    //SystManagingrint("Query: " + q1.toString());
	
	     Query q = new QueryParser(Version.LUCENE_CURRENT, "title", analyzer).parse(q1.toString());
	     //Query q = new QueryParser(Version.LUCENE_CURRENT, "title", analyzer).parse(querystr);
	     //Query q = new FuzzyQuery(new Term("title", "Lucene for Dummies"),new Float(0.2));
	    
	    // 3. search
	    int hitsPerPage = 20; // aqui tem q ser o tamanho do meu texto, pois temos q selecionar todas as frases com termos do n-grma
	    IndexSearcher searcher = new IndexSearcher(index, true);
	    TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
	    searcher.search(q, collector);
	    //searcher.search(q, 10);
	    ScoreDoc[] hits = collector.topDocs().scoreDocs;
	
	    // 4. display results
	    System.out.println("Found " + hits.length + " hits.");
	    for(int j=0;j<hits.length;++j)
	    {
	    	
	    	
	    	 w.close();
			  int docId = hits[j].doc;
			  Document d = searcher.doc(docId);
			  System.out.println((j + 1) + ". " + d.get("title"));
			  
			  System.out.println(test[i]);
			  

			    addDoc(w, "paulo call look see throw");

			    w.close();
	    }
	
	    // searcher can only be closed when there
	    // is no need to access the documents any more.
	    searcher.close();
    }
  }

  private static void addDoc(IndexWriter w, String value) throws IOException
  {
    Document doc = new Document();
    doc.add(new Field("title", value, Field.Store.YES, Field.Index.ANALYZED));
    w.addDocument(doc);
  }
     
    }
