
package conversor;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.pdfbox.pdfparser.PDFParser;
import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.util.PDFTextStripper;

public class conversorPDF{

	private String enderecoRecurso;

	public void setEnderecoRecurso(String enderecoRecurso) {
		this.enderecoRecurso = enderecoRecurso; // caminho do arquivo
	}

	public String getConteudo(File f) {

		FileInputStream is = null;
		try {
			is = new FileInputStream(f);
		} catch (IOException e) {
			System.out.println("ERRO: " + e.getMessage());
			return null;
		}

		PDDocument pdfDocument = null;
		try {
			PDFParser parser = new PDFParser(is);
			parser.parse();
			pdfDocument = parser.getPDDocument();
			PDFTextStripper stripper = new PDFTextStripper();
			return stripper.getText(pdfDocument);

		} catch (IOException e) {
			return "ERRO: Can't open stream" + e;
		} catch (Throwable e) {
			// catch this, since we need to close the resources
			return "ERRO: An error occurred while getting contents from PDF "
					+ e;
		} finally {
			if (pdfDocument != null) {
				try {
					pdfDocument.close();
				} catch (IOException e) {
					return "ERRO: Can't close pdf." + e;
				}
			}
		}
	}
}
