package neurominer;

import java.io.IOException;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.LowerCaseTokenizer;
import org.apache.lucene.analysis.PorterStemFilter;

import java.io.Reader;
import java.util.HashSet;
//import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;

/**
 * PorterStemAnalyzer processes input
 * text by stemming English words to their roots.
 * This Analyzer also converts the input to lower case
 * and removes stop words.  A small set of default stop
 * words is defined in the STOP_WORDS
 * array, but a caller can specify an alternative set
 * of stop words by calling non-default constructor.
 */
public class PorterStemAnalyzer extends Analyzer
{
    //private static Hashtable _stopTable;

    Set stopWords = new HashSet();

    public static final String[] STOP_WORDS =
    {
        "$", "about", "after", "all", "also", "an", "and",
        "another", "any", "are", "arent", "as", "at","able",
   "above",
   "according",
   "accordingly",
   "across",
   "actually",
   "afterwards",
   "again",
   "against",
   "allow",
   "allows",
   "almost",
   "alone",
   "along",
   "already",
   "also",
   "although",
   "always",
   "am",
   "among",
   "amongst",
   "anybody",
   "anyhow",
   "anyone",
   "anything",
   "anyway",
   "anyways",
   "anywhere",
   "apart",
   "aside",
   "associated",
   "available",
   "away",
   "awfully",
 "be",


        "because", "been", "before", "being", "between",
        "both", "but", "by", "came", "can","cannot","cant",
        "come",


        "could", "couldnt",
"did", "didnt",
"do", "dont",
"does","doesnt",
 "each", "else",
        "for", "from", "has", "had",
"hasnt",
"hadnt",
        "he", "hed","have","havent","hes",
 "her", "here", "him", "himself",


        "his", "how","happens",
   "hardly",
   "having",
   "hello",
   "help",
   "hence",
   "her",
   "hereafter",
   "hereby",
   "herein",
   "hereupon",
   "hers",
   "herself",
   "hi",
   "him",
   "himself",
   "his",
   "hither",
   "hopefully",
   "how",
   "howbeit",
   "however",
"if", "ill", "in", "into", "is", "isnt",
"it","id",
        "its", "just", "many", "me",
        "might", "more", "most", "much", "must", "my",


        "never","no", "not", "now","name",
   "namely",
   "nd",
   "near",
   "nearly",
   "necessary",
   "need",
   "needs",
   "neither",
   "nevertheless",
   "new",
   "next",
   "nine",
   "nobody",
   "non",
   "none",
   "noone",
   "nor",
   "normally",
   "nothing",
   "novel",
   "nowhere",
 "of", "on", "only", "or",
        "other", "our", "out", "over", "re", "same", "should", "shouldnt",
"since", "so", "some","she","shes","shed",


        "still", "such", "than", "that", "the",
        "their", "them", "then", "there", "these",
        "they", "theyd",
 "th",
   "thank",
   "thanks",
   "thanx",
   "thats",
   "theirs",
   "them",
   "themselves",
   "thence",
   "thereafter",
   "thereby",
   "therefore",
   "therein",
   "theres",
   "thereupon",
 "theyre", "this", "those", "through", "to", "too",


        "under", "up", "use", "very", "want", "was",
"wasnt",
        "way", "we", "well", "were","werent",
 "what", "when","whats",


        "where","wheres",
 "which","whichs",
 "while", "who", "whos","whose",
 "will","wont",
        "with", "would", "wouldnt",
"you", "your","youre","youll","youd",
"yes",
   "yet",
   "yours",
   "yourself",
   "yourselves",
                "a", "b", "c", "d", "e", "f", "g", "h", "i",
        "j", "k", "l", "m", "n", "o", "p", "q", "r",
        "s", "t", "u", "v", "w", "x", "y", "z", "zero",

    };

   

    /**
     * Construtor .
     */
    public PorterStemAnalyzer()
    {
       // stopWords.addAll((Collection)STOP_WORDS.getClass());
        for (int i=0; i < STOP_WORDS.length; i++)
        {          
          stopWords.add(STOP_WORDS[i]);
        }
    }

    /**
     * Builds an analyzer with the given stop words.
     *
     * @param stopWords a String array of stop words
     */
//    public PorterStemAnalyzer(Set stop)
//   {
       // stopTable = StopFilter.makeStopTable(stopWords);
 //        stopWords = StopFilter.makeStopTable(stop);
//    }

    /**
     * Processes the input by first converting it to
     * lower case, then by eliminating stop words, and
     * finally by performing Porter stemming on it.
     *
     * @param reader the Reader that
     *               provides access to the input text
     * @return an instance of TokenStream
     */
 //   public final TokenStream tokenStream(Reader reader)
 //   {
 //       return new PorterStemFilter(new StopFilter(new LowerCaseTokenizer(reader), stopWords));
 //   }
    
    public final TokenStream tokenStream(String fieldName, Reader reader)
    {
        TokenStream result = new LowerCaseTokenizer(reader);
        result = new StopFilter(true, result, stopWords, true);
        result = new PorterStemFilter(result);
        return result;        
    }

    public static void displayTokenStream(TokenStream tokenStream) throws IOException {

        TermAttribute termAtt = (TermAttribute) tokenStream.getAttribute(TermAttribute.class);
        //TypeAttribute typeAtt = tokenStream.getAttribute(TypeAttribute.class); Nao versao 3.0


        while (tokenStream.incrementToken())
        {
            System.out.print(termAtt.term());
            System.out.print(' ');
            //System.out.println(typeAtt.type());
        }
    }



//    public TokenStream tokenStream(String fieldName, Reader reader) {
//         TokenStream result = new LowerCaseTokenizer(reader);
//         if (!stopWords.isEmpty()) {
//             result = new StopFilter(result, stopWords, true);
//         }
//         if (usePorterStemming) {
//             result = new PorterStemFilter(result);
//         }
//         return result;
//     }



}
