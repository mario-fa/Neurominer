/*
 * Título:       DadosCombo.java
 * Descrição:    Classe encarregada do tratamento com Combos 
 * Assunto:      Dissertacao do Mestrado em Sistemas e Computação
 * Orientador:   Manoel Mendonça
 * @author       Cristiane Costa Magalhães
 * Data       :  Ago/2007
 * @version 1.0
 */

package util;


public class DadosCombo{
    int codDadosCombo;
    String desDadosCombo;

    public int getCodDadosCombo(){
      return codDadosCombo;
    }

    public String getDesDadosCombo(){
      return desDadosCombo;
    }

    /**
    * Construtor da classe DadosCombo
    * @param aintCodDadosCombo Inteiro que representa o codigo do DadosCombo.
    * @param aStringDesDadosCombo String que representa a descrição do DadosCombo.
    */
    public DadosCombo(int aintCodDadosCombo, String aStringDesDadosCombo){
      codDadosCombo = aintCodDadosCombo;
      desDadosCombo = aStringDesDadosCombo;
    }
}
