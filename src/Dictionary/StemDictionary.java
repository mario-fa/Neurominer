/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Dictionary;

import Dictionary.ProcessCarga;
import Compare.PorterStemAnalyzer;
import java.io.StringReader;
import java.sql.ResultSet;
import java.util.StringTokenizer;
import org.apache.lucene.analysis.TokenStream;
import java.sql.ResultSet;

import Dados.AcessoBanco;
import java.sql.SQLException;

/**
 *
 * @author marioandre
 */
public class StemDictionary
{
      private static String path_stopwords = "D:/[DATAMINING]/stopwords.txt";
      private String grama = null;
      ResultSet rsStem = null;
      AcessoBanco con = null;
      //private AcessoBanco con = null;


      public StemDictionary(String grama)
      {
         this.setGrama(grama);
         this.con = AcessoBanco.getInstancia();
      }

      public void loadStems() throws Exception
      {
           ProcessCarga pc = new ProcessCarga();
             pc.GetDictionary(getGrama());
             ResultSet rs = pc.getRs();
             String stem;
             String descriptor;
             StringReader sr = null;

             TokenStream tokenstream = null;
             StringTokenizer token = null;
             String type_equ = null;
             int tamanhoStem = 0;
             int numStem;
             PorterStemAnalyzer ps = new PorterStemAnalyzer();
	  	     ps.ReadFile(path_stopwords);
                          
             while (rs.next())
             {
               type_equ = "U";
               descriptor = rs.getString("descriptor");
               descriptor = descriptor.replace("'", "\\'");
               System.out.print("Descriptor Ori: " + descriptor + "\n" );
               sr = new StringReader(descriptor);
	  	       tokenstream = ps.tokenStream(null, sr);
               stem = PorterStemAnalyzer.displayTokenStream(tokenstream);
               token = new StringTokenizer(stem);
               tamanhoStem = token.countTokens();
               //Buxcando o termo pelo Stem
               rsStem = GetTerm(stem);
               rsStem.next();
               //Verificando se já existe o stem do termo no banco
               numStem = rsStem.getInt("num");
               if (numStem > 0)
               {
                   stem = null;
                   type_equ = "U";
                   tamanhoStem = 0;
               }
               else
               {
                 stem = stem.trim();
                 if (getGrama().equalsIgnoreCase("N"))
                 {
                   //Verificando se o termo continua N-Grama
                   if (tamanhoStem > 1) type_equ = "N";
                 }
               }
                 System.out.println("Descriptor: " + descriptor + " - Stem:  " + stem + "- Count: " + tamanhoStem + " - Type Des:  " + rs.getString("type_des") +  " - Type Equi:  " + type_equ + "numStem: " + numStem + "\n");
           //      System.out.print("desc: " + descriptor + "\n");
                 updateTerm(descriptor, stem, type_equ, tamanhoStem);
               //}
             }             
      }


      public static void updateTerm (String descriptor, String equivalent_stem, String type_equ, int final_size) throws Exception
	{
			try
			{
				String sql = "UPDATE neurominer.dictionary_dim set equivalent_stem = '" + equivalent_stem +
                        "', type_equ = '" + type_equ + "', final_size =" + final_size + " where descriptor = E'" + descriptor + "'";
                System.out.print("Update: " + sql + '\n');
				int lines = 0;
                AcessoBanco con2 = new AcessoBanco().getInstancia();
				lines = con2.exec_command(sql);
				if (lines <= 0)
					System.out.println("Problema na atualizacao de stem: " + equivalent_stem);
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
	}

      public ResultSet GetTerm(String stem) throws Exception
	  {
		try
		{			
			String sql = "";

			sql = "SELECT count(1) as num FROM neurominer.dictionary_dim "+
				 "where language like 'English' "+
				 " and equivalent_stem = '"+stem.trim()+"'";
			//this.rs =  getCon().getRS(sql);
            //System.out.print("SQL: " + sql);
            return getCon().getRS(sql);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
            return null;
		}


	}
         
         public static void main(String[] args) throws Exception
         {
             StemDictionary sd = new StemDictionary("u");
             sd.loadStems();
         }

    /**
     * @return the grama
     */
    public String getGrama() {
        return grama;
    }

    /**
     * @param grama the grama to set
     */
    public void setGrama(String grama) {
        this.grama = grama;
    }

    /**
     * @return the con
     */
    public AcessoBanco getCon() {
        return con;
    }

    /**
     * @param con the con to set
     */
    public void setCon(AcessoBanco con) {
        this.con = con;
    }
}
