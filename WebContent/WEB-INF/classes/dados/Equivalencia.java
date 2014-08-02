/*
 * Título:       Equivalencia.java
 * Descrição:    Cadastro de Equivalencias
 * Assunto:      Dissertacao do Mestrado em Sistemas e Computação
 * Orientador:   Manoel Mendonça
 * @author       Cristiane Costa Magalhães
 * Data       :  Ago/2007
 * @version 1.0
 */

package dados;
import java.sql.*;
import java.util.*;

import bancoDados.AcessoBanco;


public class Equivalencia{

  AcessoBanco acessoBanco = new AcessoBanco();
  int codigo;
  String descricao;
  
  public Equivalencia() {
  }

  public int getCodigo(){
    return codigo;
  }

  public String getDescricao(){
    return descricao;
  }

  public void setCodigo(int aintCodigo){
    codigo = aintCodigo;
  }

  public void setDescricao(String astrDescricao){
    descricao = astrDescricao;
  }


  public Equivalencia (int aintEquivalencia) throws SQLException
    {
      definirEquivalencia(aintEquivalencia);
    }

    private void definirEquivalencia(int aintEquivalencia) throws SQLException
    {
       String lstrSQL = "Select codEquivalencia, desEquivalencia ";
       lstrSQL = lstrSQL + " from equivalencia where codEquivalencia =" + aintEquivalencia;
       ResultSet rs = acessoBanco.getRS(lstrSQL);
       if (rs.next()){
           setCodigo(aintEquivalencia);
           setDescricao(rs.getString("desEquivalencia"));
       }
       rs.close ();
    }

  public ArrayList obterConjuntoEquivalencia() throws SQLException
  {
    ArrayList <Object> arlEquivalencia = new ArrayList<Object>();
    AcessoBanco ab = new AcessoBanco();

    try{
      String lstrSQL = "Select codEquivalencia from equivalencia";
      ResultSet lrsRS = ab.getRS(lstrSQL);
      while (lrsRS.next()){
         Equivalencia equivalencia = new Equivalencia(Integer.parseInt(lrsRS.getString("codEquivalencia")));
         arlEquivalencia.add(equivalencia);
       }
    }
     catch (SQLException e) {
         throw new SQLException ("Erro em Equivalencia: " + e.getMessage());
     }
    finally{
    	AcessoBanco.close();
    }
    return arlEquivalencia;
  }


  /**
  * Persiste o objeto Equivalencia
  * @throws SQLException em caso de um erro na insercao.
  */
   public void persistir(String astrNome) throws SQLException
   {
     try{
       String lstrSQL;
       lstrSQL = "select max (codEquivalencia) as max from equivalencia";
       ResultSet rsTemp = acessoBanco.getRS(lstrSQL);
       rsTemp.next();
       int codNovo = rsTemp.getInt("max");
       codNovo = codNovo +1;

         lstrSQL = "insert into equivalencia (codEquivalencia, desEquivalencia) values ";
         lstrSQL = lstrSQL + " (" + codNovo + ",'" + astrNome + "') ";
         acessoBanco.exec_command(lstrSQL);

         lstrSQL = "select codEquivalencia from equivalencia where ";
         lstrSQL = lstrSQL + " codEquivalencia = " + codNovo + " and desEquivalencia = '" + astrNome + "'";
         ResultSet rs = acessoBanco.getRS(lstrSQL);
         rs.next();
         setCodigo(rs.getInt("codEquivalencia"));
         rs.close ();
         rsTemp.close();

     }catch(SQLException ex){
       throw new SQLException ("Erro em Equivalencia: " + ex.getMessage());
     }
    }

 public void alterar(int aintCodigo, String astrNome) throws SQLException
    {
      try{
        String lstrSQL;
        lstrSQL = "update equivalencia set desEquivalencia = '" + astrNome + "'";
        lstrSQL = lstrSQL + " where codEquivalencia = " + aintCodigo;
        acessoBanco.exec_command(lstrSQL);
        }
      catch(SQLException ex){
        throw new SQLException ("Erro em Equivalencia: " + ex.getMessage());
      }
     }

    public void excluir(int aintCodigo) throws SQLException
    {
      String lstrSQL = "delete from equivalencia where codEquivalencia = " + aintCodigo;
      acessoBanco.exec_command(lstrSQL);
    }
}
