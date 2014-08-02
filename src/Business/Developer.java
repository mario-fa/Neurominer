/**
 * 
 */
package Business;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import Dados.AcessoBanco;
import Entity.EntDeveloper;

/**
 * @author PH
 *
 */
public class Developer
{	
	/**
	 * Save data in database
	 * @throws SQLException if there is exception.
	 */
	public static void Insert(EntDeveloper developer) throws Exception
	{
		AcessoBanco con = new AcessoBanco().getInstancia();
		try
		{
			String sql = "INSERT INTO neurominer.developer_dim("+
		            "email,"+
		            "\"login\","+
		            "start_date,"+ 
		            "expertise,"+
		            "flag,"+
		            "name,"+
		            "\"position\")"+
		    "VALUES ("+
		            "'"+developer.getEmail()+"',"+
		            "'"+developer.getLogin()+"',"+
		            "now(),"+
		            "'"+developer.getExpertise()+"',"+
		            "'"+developer.getFlag()+"',"+
		            "'"+developer.getName()+"',"+
		            "'"+developer.getPosition()+"'"+
		            ")";
		
			System.out.println(sql);
			int ok =  con.exec_command(sql);
			System.out.println("cone "+ok+" enoc");
			
		}
		catch(SQLException ex)
		{
			throw new SQLException ("Error writing data : " + ex.getMessage());
		}
	}
	
	/**
	 * Update data in database
	 * @throws SQLException if there is exception.
	 */
	public void Update(EntDeveloper developer) throws Exception
	{
		AcessoBanco con = new AcessoBanco();
		
		try
		{
			String sql = "UPDATE neurominer.developer_dim"+
		            "SET developer_id="+
		            " ,email='"+developer.getEmail()+"'"+
		            " ,\"login\"='"+developer.getLogin()+"'"+
		            " ,finish_date="+developer.getStart_date()+
		            " ,expertise='"+developer.getExpertise()+"'"+
		            " ,flag='"+developer.getFlag()+"'"+
		            " ,name='"+developer.getName()+"'"+
		            " ,\"position\"='"+developer.getPosition()+"'";
		
			con.exec_command(sql);
		}
		catch(SQLException ex)
		{
			throw new SQLException ("Error writing data : " + ex.getMessage());
		}
	}
}