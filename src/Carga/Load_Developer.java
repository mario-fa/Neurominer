/**
 * 
 */
package Carga;

import java.util.HashMap;

import javax.swing.text.TabableView;

/**
 * @author PH
 *
 */
public class Load_Developer
{
	private String email = "";
	private String year = "";
	private String month = "";
	private String day = "";
	private String content = "";
	private String date = "";
	
	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the year
	 */
	public String getYear() {
		return year;
	}
	/**
	 * @param year the year to set
	 */
	public void setYear(String year) {
		this.year = year;
	}
	/**
	 * @return the month
	 */
	public String getMonth() {
		return month;
	}
	/**
	 * @param month the month to set
	 */
	public void setMonth(String month) {
		this.month = month;
	}
	/**
	 * @return the day
	 */
	public String getDay() {
		return day;
	}
	/**
	 * @param day the day to set
	 */
	public void setDay(String day) {
		this.day = day;
	}
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	
	
	public Load_Developer(String email,String date, String year, String month, String day,String content)
	{
		this.email = email;
		this.date = date;
		this.year = year;
		this.month = month;
		this.day = day;
		this.content = content;
	}	
	
}
