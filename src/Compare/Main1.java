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



/**
 * @author marioandre
 * Utilizando SpellChecker para comparar termos
 */
public class Main1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ParseException 
    {
      // 0. Specify the analyzer for tokenizing text.
    //    The same analyzer should be used for indexing and searching
    StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);

    // 1. create the index
    Directory index = new RAMDirectory();

    // the boolean arg in the IndexWriter ctor means to
    // create a new index, overwriting any existing index
    IndexWriter w = new IndexWriter(index, analyzer, true, IndexWriter.MaxFieldLength.UNLIMITED);

    addDoc(w, "Lucene in Action for about ten days here in brazil Lucene for Dummies kkkkkkk eeeeeeee nnnn Lucene for Dummies xxxx tttt dddd jjjjjjj k");
    addDoc(w, "Lucene for Dummies");
    addDoc(w, "Lucene for Dummies");
    addDoc(w, "Lucene for Dummies ten");
    addDoc(w, "Lucene for Dummies");
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
    String querystr = args.length > 0 ? args[0] : "Action brazil";

    // the "title" arg specifies the default field to use
    // when no field is explicitly specified in the query.
    Query q = new QueryParser(Version.LUCENE_CURRENT, "title", analyzer).parse(querystr);

    // 3. search
    int hitsPerPage = 100; // aqui tem q ser o tamanho do meu texto, pois temos q selecionar todas as frases com termos do n-grma
    IndexSearcher searcher = new IndexSearcher(index, true);
    TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
    searcher.search(q, collector);
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

  private static void addDoc(IndexWriter w, String value) throws IOException {
    Document doc = new Document();
    doc.add(new Field("title", value, Field.Store.YES, Field.Index.ANALYZED));
    w.addDocument(doc);
  }
     
    }
