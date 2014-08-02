/*
 * Título:       Cadastro.java
 * Descrição:    Classe de Negócio responsável pelo cadastro dos dados 
 * Assunto:      Dissertacao do Mestrado em Sistemas e Computação
 * Orientador:   Manoel Mendonça
 * @author       Cristiane Costa Magalhães
 * Data       :  Ago/2007
 * @version 1.0
 */

package negocio;

import dados.*;

import java.sql.*;
import java.util.*;



public class Cadastro {
  
  private Categoria categoria;
  private SubCategoria subCategoria;
  private Equivalencia equivalencia;
  private Descritor descritor;
  
  public Cadastro() {
  }

 // Métodos que tratam de Categoria 
 public int inserirCategoria(String astrDescricao, String astrSigla) throws SQLException {
  categoria = new Categoria();
  categoria.setDescricao(astrDescricao);
  categoria.setSigla(astrSigla);
  categoria.persistir(astrDescricao, astrSigla);
  return categoria.getCodigo();
}
 
 public Categoria buscarCategoria(int aintCodigo) throws SQLException {
 	categoria = new Categoria(aintCodigo);
    if (categoria.getDescricao() != null) {
        return categoria;
    }
    else
    {
      return null;
    }
 }
 public void alterarCategoria(int aintCodigo, String astrDescricao,String astrSigla) throws SQLException
   {
 	categoria.setCodigo(aintCodigo);
 	categoria.setDescricao(astrDescricao);
 	categoria.setSigla(astrSigla);
 	categoria.alterar(aintCodigo, astrDescricao, astrSigla);
 }

 public void excluirCategoria(int aintCodigo) throws SQLException {
 	categoria.excluir(aintCodigo);
 }

 // Métodos que tratam de Sub Categoria
  public int inserirSubCategoria(String astrDescricao, String astrSigla,
                        int aintCodCategoria) throws SQLException
  {
   subCategoria= new SubCategoria();
   subCategoria.setDescricao(astrDescricao);
   subCategoria.setSigla(astrSigla);
   subCategoria.setCategoria(aintCodCategoria);
   subCategoria.persistir(astrDescricao, astrSigla,aintCodCategoria);
   return subCategoria.getCodigo();
}

 public SubCategoria buscarSubCategoria(int aintCodigo) throws SQLException {
 	subCategoria = new SubCategoria(aintCodigo);
    if (subCategoria.getDescricao() != null) {
        return subCategoria;
    }
    else
    {
      return null;
    }
 }

 public void alterarSubCategoria(int aintCodigo, String astrDescricao, String astrSigla,
                            int aintCodCategoria) throws SQLException
   {
 	 subCategoria.setDescricao(astrDescricao);
 	 subCategoria.setSigla(astrSigla);
 	 subCategoria.setCategoria(aintCodCategoria);
 	 subCategoria.alterar(aintCodigo, astrDescricao, astrSigla, aintCodCategoria);
 }

 public void excluirSubCategoria(int aintCodigo) throws SQLException {
 	subCategoria.excluir(aintCodigo);
 }

 public ArrayList obterCategoria() throws SQLException {
 	
    ArrayList arlCategoria = SubCategoria.obterCategoria();
    return arlCategoria;
  }

 public int inserirEquivalencia(String astrDescricao) throws SQLException {
	equivalencia = new Equivalencia();
	equivalencia.setDescricao(astrDescricao);
	equivalencia.persistir(astrDescricao);
	return equivalencia.getCodigo();
 }
	 
public Equivalencia buscarEquivalencia(int aintCodigo) throws SQLException {
	equivalencia = new Equivalencia(aintCodigo);
	if (equivalencia.getDescricao() != null) {
		return equivalencia;
	}
	else
	{
		return null;
	}
 }
public void alterarEquivalencia(int aintCodigo, String astrDescricao) throws SQLException
{
	equivalencia.setCodigo(aintCodigo);
	equivalencia.setDescricao(astrDescricao);
	equivalencia.alterar(aintCodigo, astrDescricao);
}

public void excluirEquivalencia(int aintCodigo) throws SQLException {
	equivalencia.excluir(aintCodigo);
}

public String[][] populaListaTermosGenericosAAssociar(int aintCodigo) throws SQLException {
	   String [][] strResultado;
	   strResultado = descritor.obtemListaTermosGenericosAAssociar(aintCodigo);
	   return strResultado;
}

public String[][] populaListaTermosGenericosAssociados(int aintCodigo) throws SQLException {
	   String [][] strResultado;
	   strResultado = descritor.obtemListaTermosGenericosAssociados(aintCodigo);
	   return strResultado;
 }

public String[][] populaListaTermosEspecificosAAssociar(int aintCodigo) throws SQLException {
	   String [][] strResultado;
	   strResultado = descritor.obtemListaTermosEspecificosAAssociar(aintCodigo);
	   return strResultado;
}

public String[][] populaListaTermosEspecificosAssociados(int aintCodigo) throws SQLException {
	   String [][] strResultado;
	   strResultado = descritor.obtemListaTermosEspecificosAssociados(aintCodigo);
	   return strResultado;
 }

public String[][] populaListaTermosRelacionadosAAssociar(int aintCodigo) throws SQLException {
	   String [][] strResultado;
	   strResultado = descritor.obtemListaTermosRelacionadosAAssociar(aintCodigo);
	   return strResultado;
}

public String[][] populaListaTermosRelacionadosAssociados(int aintCodigo) throws SQLException {
	   String [][] strResultado;
	   strResultado = descritor.obtemListaTermosRelacionadosAssociados(aintCodigo);
	   return strResultado;
}

public String[][] populaListaEquivalenciasAAssociar(int aintCodigo) throws SQLException {
	   String [][] strResultado;
	   strResultado = descritor.obtemListaEquivalenciasAAssociar(aintCodigo);
	   return strResultado;
}

public String[][] populaListaEquivalenciasAssociadas(int aintCodigo) throws SQLException {
	   String [][] strResultado;
	   strResultado = descritor.obtemListaEquivalenciasAssociadas(aintCodigo);
	   return strResultado;
}

public String[][] populaListaSubCategoriasAAssociar(int aintCodigo) throws SQLException {
	   String [][] strResultado;
	   strResultado = descritor.obtemListaSubCategoriasAAssociar(aintCodigo);
	   return strResultado;
}

public String[][] populaListaSubCategoriasAssociadas(int aintCodigo) throws SQLException {
	   String [][] strResultado;
	   strResultado = descritor.obtemListaSubCategoriasAssociadas(aintCodigo);
	   return strResultado;
}

public int inserirDescritor(String astrDescricao) throws SQLException {
	descritor = new Descritor();
	descritor.setDescricao(astrDescricao);
	descritor.persistir(astrDescricao);
	return descritor.getCodigo();
 }
	 
public Descritor buscarDescritor(int aintCodigo) throws SQLException {
	descritor = new Descritor(aintCodigo);
	if (descritor.getDescricao() != null) {
		return descritor;
	}
	else
	{
		return null;
	}
 }
public void alterarDescritor(int aintCodigo, String astrDescricao) throws SQLException
{
	descritor.setCodigo(aintCodigo);
	descritor.setDescricao(astrDescricao);
	descritor.alterar(aintCodigo, astrDescricao);
}

public void excluirDescritor(int aintCodigo) throws SQLException {
	descritor.excluir(aintCodigo);
}

public void inserirDescritorTermosGenericos(String[][] strListaAAssociar) throws SQLException 
{
	descritor.inserirDescritorTermosGenericos(strListaAAssociar);
}

public void inserirDescritorTermosEspecificos(String[][] strListaAAssociar) throws SQLException 
{
	descritor.inserirDescritorTermosEspecificos(strListaAAssociar);
}

public void inserirDescritorTermosRelacionados(String[][] strListaAAssociar) throws SQLException 
{
	descritor.inserirDescritorTermosRelacionados(strListaAAssociar);
}

public void inserirDescritorEquivalencias(String[][] strListaAAssociar) throws SQLException 
{
	descritor.inserirDescritorEquivalencias(strListaAAssociar);
}

public void inserirDescritorSubCategorias(String[][] strListaAAssociar) throws SQLException 
{
	descritor.inserirDescritorSubCategorias(strListaAAssociar);
}

public void aplicarRadicalizacao() throws SQLException 
{
	descritor.aplicarRadicalizacao();
}

}
