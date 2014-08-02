package testesDeFiltro;

import java.io.*;
import java.util.Arrays;
import classes.EMailFilter;

public class Main {

    public static String readTextFile(String fileName) {
        StringBuffer buffer = new StringBuffer();
        try {
            FileInputStream fis = new FileInputStream(fileName);
            InputStreamReader isr = new InputStreamReader(fis, "ISO-8859-1");
            Reader in = new BufferedReader(isr);
            int ch;
            while ((ch = in.read()) > -1) {
                buffer.append((char) ch);
            }
            in.close();
            return buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        String [] filter = {
                                "----",
                                "escrito por",
                                "Wroted By",
                                "de:",
                                "de :",
                                "from:",
                                "from :",
                                "para:",
                                "para :",
                                "to:",
                                "to :",
                                "subject:",
                                "subject :",
                                "assunto:",
                                "assunto :",
                                "enviada em:",
                                "enviada em :",
                                "--",
                                ">>",
                                "__",
                                "citado",
                                "mentioned"
                           };

        EMailFilter.setFilter(filter);

        // Lembrar de alterar o diretório dos arquivos sempre que necessário!
        String directory = "D:/[DATAMINING]/EmailsTeter/";
        String prefix = "email";
        String contentFile = "";

        // create a file that is really a directory
        File aDirectory = new File(directory);

        // get a listing of all files in the directory
        String[] filesInDir = aDirectory.list();

         // sort the list of files (optional)
         Arrays.sort(filesInDir);

         System.out.println("\n\n\nTRATANDO TODOS OS ARQUIVOS DO DIRETÓRIO " +
          directory + " CUJO NOME COMEÃ‡A COM O TEXTO " + prefix + "\n\n\n\n");

        for (int i = 0; i < filesInDir.length; i++) {
            if ( filesInDir[i].startsWith(prefix) ) {
                contentFile = readTextFile(directory + filesInDir[i]);
                System.out.println("\n\n\nARQUIVO : " + directory + filesInDir[i]);
                System.out.println("\n\n#########################  ANTES DA FILTRAGEM  ##########################");
                System.out.println(contentFile);
                System.out.println("\n###########################################################################");

                System.out.println("\n\n#########################  DEPOIS DA FILTRAGEM  #########################");
                contentFile = EMailFilter.Filter(contentFile); // Realizando a filtragem
                System.out.println(contentFile);
                System.out.println("\n###########################################################################");
                
            }
        }
    }
}
