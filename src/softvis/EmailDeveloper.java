package softvis;

import java.util.Comparator;
import java.util.Date;

public class EmailDeveloper implements Comparable<EmailDeveloper>
{
	private java.util.Date date;
	private String line;
	
	public java.util.Date getDate() {
		return date;
	}

	public void setDate(java.util.Date date) {
		this.date = date;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}
	
	public EmailDeveloper(Date date, String line)
	{
		setDate(date);
		setLine(line);		
	}
	
	 @Override
	  public int compareTo(EmailDeveloper o) {
	    return getDate().compareTo(o.getDate());
	  }
}
