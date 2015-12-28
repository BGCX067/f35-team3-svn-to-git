package edu.tamu.f35.client.persistance;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import edu.tamu.f35.dataCotract.MessageType;
import edu.tamu.f35.dataCotract.RestoreMessage;




public class MessageLogDBAccess 
{
	private static final String selectMaxId = "select max(MessageId) as mid from Message";
	String insBroMes = "Insert into BroadMessageLog";
	
	private static MessageLogDBAccess instance = null;
	protected MessageLogDBAccess()
	{
		
	}
	
	public static MessageLogDBAccess getInstance()
	{
		if(instance==null)
			instance = new MessageLogDBAccess();
		return instance;
	}
	
	private int getMaxMessageId()throws Exception
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
		
	}
	synchronized
	public int createBroadCastMessage(String senderId, String message)
	{
		
		try
		{
			int uniqueid = getMaxMessageId()+1;
			String query = "insert into Message(MessageId, MessageText) values("+uniqueid+",'"+message+"')";
			System.out.println(query);
			String query2 = "insert into BroadCastMessagelog(MessageId, SenderId) values("+uniqueid+",'"+senderId+"')";
			System.out.println(query2);
		
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			Connection conn = DriverManager.getConnection("jdbc:odbc:MSAccessDatabase");
			Statement s = conn.createStatement();
			s.execute(query);
			s.execute(query2);
			s.close();
			conn.close();
			return uniqueid;
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return -1;
		}
	}
	
	
	public ArrayList<RestoreMessage> retreiveMessages(String userId)
	{
		try
		{
			ArrayList<RestoreMessage> restoreMsg = new ArrayList<RestoreMessage>();
			String query2 = "select top 2 UserId as SenderId, UserName as SenderName, MessageText, TimeStamp"+
					    " from User u, PrivateMessageLog pml, Message m "+
					    "where u.UserId = pml.SenderId " +
					    "and pml.MessageId = m.MessageId and pml.ReceiverId = '"+userId+"' order by TimeStamp desc";
				      
			System.out.println(query2);
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			Connection conn = DriverManager.getConnection("jdbc:odbc:MSAccessDatabase");
			Statement s = conn.createStatement();
			s.execute(query2);
			ResultSet rset = s.getResultSet();
			while(rset.next())
			{
				//System.out.println(rset.getString("SenderId") +" "+ rset.getString("SenderName")+" "+ rset.getString("MessageText")+" "+ rset.getString("TimeStamp"));//+" "+rset.getString("TimeStamp"));
				//long d = Date.parse(rset.getString("TimeStamp"));//(rset.getString("TimeStamp"))
				String SenderId = rset.getString("SenderId");
				String SenderName = rset.getString("SenderName");
				String message = rset.getString("MessageText");
				String timeStamp = rset.getString("TimeStamp");
				restoreMsg.add(new RestoreMessage(SenderId, SenderName, timeStamp, message, MessageType.PVT));
				
			}
			
			String qury = "select top 2 MessageText, TimeStamp, UserId as SenderId, UserName as SenderName" +
					" from BroadCastMessageLog bml, Message m, User u " +
					"where m.MessageId = bml.MessageId and u.UserId = bml.SenderId order by TimeStamp desc";
			System.out.println(qury);
			s.execute(qury);
			rset = s.getResultSet();
			while(rset.next())
			{
				//System.out.println(rset.getString("SenderId")+" "+rset.getString("SenderName")+" "+rset.getString("MessageText") +" "+ rset.getString("TimeStamp"));//+" "+ rset.getString("MessageText")+" "+ rset.getString("TimeStamp"));//+" "+rset.getString("TimeStamp"));
				String SenderId = rset.getString("SenderId");
				String SenderName = rset.getString("SenderName");
				String message = rset.getString("MessageText");
				String timeStamp = rset.getString("TimeStamp");
				restoreMsg.add(new RestoreMessage(SenderId, SenderName, timeStamp, message, MessageType.BCT));
			}
			s.close();
			conn.close();
			//if(missionList.isEmpty()) return null;
			Collections.sort(restoreMsg, new Comparator(){
				 
	            public int compare(Object o1, Object o2) {
	            	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	            	RestoreMessage p1 = (RestoreMessage) o1;
	            	RestoreMessage p2 = (RestoreMessage) o2;
	            	Date d1 = null;
	            	Date d2 = null;
	            	int returnValue;
	            	try
	            	{
	            		d1 = dateFormat.parse(p1.time);
	            		d2 = dateFormat.parse(p2.time);
	            		returnValue = d1.compareTo(d2);
	            	}
	            	catch(Exception e)
	            	{
	            		returnValue = 0;
	            	}      	
	            	return returnValue;
	            }
	        });
			return restoreMsg;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		//return null;
	}
	
	

	
	synchronized
	public int createPrivateMessage(String senderId, ArrayList<String> recvIds, String message) 
	{
		try
		{
			int uniqueid = getMaxMessageId()+1;
			String query = "insert into Message(MessageId, MessageText) values("+uniqueid+",'"+message+"')";
			System.out.println(query);

			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			Connection conn = DriverManager.getConnection("jdbc:odbc:MSAccessDatabase");
			Statement s = conn.createStatement();
			s.execute(query);

			for(String recvId : recvIds)
			{
				String query2 = "insert into PrivateMessagelog(SenderId, ReceiverId, MessageId) values('"+senderId+"','"+recvId+"',"+uniqueid+")";
				System.out.println(query2);
				s.execute(query2);
			}
			
			s.close();
			conn.close();
			return uniqueid;
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return -1;
		}
	}
	
	
	public static void main(String args[])
	{
		//String senderId = "A0001";
		ArrayList<String> ars = new ArrayList<String>();
		ars.add("A0001");
		ars.add("A0002");
		ars.add("G0001");
		//String msg = "This is for two three";
		//MessageLogDBAccess.getInstance().createPrivateMessage(senderId, ars, msg);
		//MessageLogDBAccess.getInstance().createBroadCastMessage("G0001", "Broad cast from G0001");
		
		ArrayList<RestoreMessage> rs = MessageLogDBAccess.getInstance().retreiveMessages("A0001");
		
		
		//dateFormat.parse("jjj").compareTo(arg0);
		
		

		
		for(RestoreMessage r : rs)
			System.out.println(r.senderId+" * "+r.senderName+" * "+r.message+" * "+r.time+" * "+r.msgType);
	}
}
