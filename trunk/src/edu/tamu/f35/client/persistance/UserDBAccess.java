package edu.tamu.f35.client.persistance;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import edu.tamu.f35.dataCotract.User;

public class UserDBAccess 
{
	
	/*private static int getMaxMessageId()throws Exception
	{
		try
		{
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			Connection conn = DriverManager.getConnection("jdbc:odbc:MSAccessDatabase");
			Statement s = conn.createStatement();
			s.execute(selectMaxId);
			ResultSet rset = s.getResultSet();
			rset.next();
			//System.out.println(rset.getInt(1));
			int maxId = rset.getInt("mid");
			System.out.println(maxId);
			s.close();
			conn.close();
			return maxId;//maxId;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		
	}*/
	synchronized
	public static User GetUser(String userId)
	{	
		try
		{
			String query = "select UserId, UserName, TerminalType from User where UserId = '"+userId+"'";
			System.out.println(query);
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			Connection conn = DriverManager.getConnection("jdbc:odbc:MSAccessDatabase");
			Statement s = conn.createStatement();
			s.execute(query);
			ResultSet rset = s.getResultSet();
			rset.next();
			String name = rset.getString("UserName");
			String id = rset.getString("UserId");
			String tt =  rset.getString("TerminalType");
			s.close();
			conn.close();
			return new User(name, id, tt);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		
	}
	
	public static void main(String args[])
	{
		//getMaxMessageId();
		User u = GetUser("A001");
		System.out.println("***");
		System.out.println("User: "+u.getName()+"  "+u.getId());
	}
	
}


/*public class UserDBAcess 
{
	
public static void main(String args[])
{
System.out.println("This is the first line");
try
{



        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date date = new Date();
        System.out.println(dateFormat.format(date)+ "   "+ date.toString());
    
	Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
	Connection conn = DriverManager.getConnection("jdbc:odbc:MSAccessDatabase");
	Statement s = conn.createStatement();

	//s.executeQuery("SELECT * FROM Mission");
	//s.execute("create table student ( Name string, ID integer )");
	//s.execute("Select * from Student");
	
	//String str = "INSERT INTO Message( MessageText) values(14, 'This is the 40urth message','"+dateFormat.format(date)+"')";
	String str = "INSERT INTO Message( MessageText) values('This is the thirdeeee message')";
	System.out.println(str);
	//PreparedStatement pstmn = conn.prepareStatement(str);
	s.executeUpdate(str, Statement.RETURN_GENERATED_KEYS);
	
	//ResultSet rset = s.getGeneratedKeys();

	//ResultSetMetaData rsmd = rset.getMetaData();
	//rsmd.

	//int numColumns = rsmd.getColumnCount();
	//System.out.println(numColumns);
	//System.out.println(rsmd.getColumnName(2));
	//while(rset.next())
	//System.out.println(rset.getString("SampleDescription"));
	//String st = "UPDATE Sample1 SET SampleDescription = 'abc'";
	//s.executeQuery(st);
	s.close();
	conn.close();
}

	catch(Exception e)
	{
		e.printStackTrace();
	}

}

}*/