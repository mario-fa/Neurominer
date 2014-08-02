package Compare;

import java.io.File;
import java.io.IOException;
import org.apache.lucene.search.spell.JaroWinklerDistance;
import org.apache.lucene.search.spell.PlainTextDictionary;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;


/**
 * @author marioandre
 * Utilizando SpellChecker para comparar termos
 * UNIGRAMAS
**/
public class Main
{
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException
    {

     Directory diretorioDicionario = new RAMDirectory();
    //criação do diretório
     SpellChecker sp = new SpellChecker(diretorioDicionario);
    //instância do objeto SpellChecker

    //Aqui seria a msg do email
     sp.indexDictionary(new PlainTextDictionary(new File("dicionario.txt")));
    //indexação do Dictionary (há duas implementações).

     //Temos q ter atenÃ§Ã£o para as buscas dos termos N-grams (ou seja frases)
    /*
     * procura primeiro os termos dos N-gramas
     * verifica se algum dos termos do n-grmaa existe no texto
     * se existir, verifica o tamanho do n-grama
     * podemos verificar a distancia de JaroWinkler entre os n-gramas e o termo do n-grama + ( tamanho do n-grama -1) para a direita e para a esquerda
     *
     *
     * *procuro primeiro so n-gramas no meu texto
     * encontrando a frase com similaridade com o n-grama, retiro cada termo do n-grama da frase, mesmo q em ordem diferente
     * depois uso o algoritmo de SpellChecker para pesquisar os u-gramas
     * Usando import org.apache.lucene.analysis.standard.StandardAnalyzer;
     */
     //VER ISTO ACIMA

     String pesquisa = "colo view tesre em life"; //Aqui seria o dicionario. Iremos procurar do dicionario para o texto
     //seu termo pesquiado
     int numeroDeSugestoes = 1;
     //nÃºmero de sugestÃµes similares
     String[] similares = sp.suggestSimilar(pesquisa, numeroDeSugestoes);
     //as sugestÃµes em si.

     System.out.println("Termo da Pesquisa: " + pesquisa);
     for (String palavra : similares) 
     {
    	 System.out.println("Termo(s) Equivalente(s): " + palavra);
     }

     pesquisa = "appeared";
     similares = sp.suggestSimilar(pesquisa, numeroDeSugestoes);
     System.out.println("Termo da Pesquisa 2: " + pesquisa);
     for (String palavra : similares)
     {
    	 System.out.println("Termo(s) Equivalente(s): " + palavra);
     }

	/*
	* Usando JaroWinklerDistance
	*/
	JaroWinklerDistance jw = new JaroWinklerDistance();
	System.out.println(" JaroWinklerDistance: ");
	System.out.println(" JaroWinklerDistance: " + jw.getDistance("sight eye  sore", "sight sore eye"));
    
    }
}
