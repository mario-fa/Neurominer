package Compare;

//import org.apache.lucene.analysis.standard.StandardAnalyzer;
//import org.apache.lucene.document.Document;
//import org.apache.lucene.document.Field;
//import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.ParseException;
//import org.apache.lucene.queryParser.QueryParser;
//import org.apache.lucene.search.*;
//import org.apache.lucene.store.Directory;
//import org.apache.lucene.store.RAMDirectory;
//import org.apache.lucene.util.Version;

import java.io.IOException;
//import java.io.Reader;
import java.io.StringReader;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;



/**
 * @author marioandre
 * Utilizando SpellChecker para comparar termos
 */
public class Main2
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ParseException 
    {
      StringReader sr = new StringReader("Hi, I know this is an old topic, but I just ran into this again. Any chance we could see something like this make it in the 2.3.X branch? I did not see a reply on this.");
      System.out.print("ANTES: " + "Hi, I know this is an old topic, but I just ran into this again. Any chance we could see something like this make it in the 2.3.X branch? I did not see a reply on this. \nDEPOIS: ");
      PorterStemAnalyzer ps = new PorterStemAnalyzer();
      ps.ReadFile("dicionario.txt");
      TokenStream tokenstream = ps.tokenStream(null, sr);
      PorterStemAnalyzer.displayTokenStream(tokenstream);

      //Tokenizer token = (Tokenizer) ps.tokenStream(null, sr);
      //while(tokenstream.incrementToken())
      //{
      //	System.out.print("valor: " + tokenstream.toString() + "\n");
      //}
      //System.out.print(token.toString());
    }
}
