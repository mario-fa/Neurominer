
package conversor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;

/**
 *
 * @author JotaV
 */

public class conversorDOC {
    
    /** Creates a new instance of conversorDOC */
    public conversorDOC(){
    }
    
    public String getConteudo(File f) throws FileNotFoundException, IOException{
        FileInputStream is = new FileInputStream(f);
        
        HWPFDocument wdoc = new HWPFDocument(is);
        WordExtractor extractor = new WordExtractor(wdoc);
        String conteudo = extractor.getText();
        conteudo = conteudo.replace("\n", " ");

        return conteudo;
    }
    
}
