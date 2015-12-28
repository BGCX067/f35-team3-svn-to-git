package edu.tamu.f35.client.persistance;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import edu.tamu.f35.dataCotract.Mission;



public class MissionUserDBAccess
{
	private static String convertToCommaSeparated(ArrayList<String> array)
	{
		System.out.println(array);
		String cs = array.toString().replace(", ","','");
		//cs = array.toString().replace(", ","','");
		cs = cs.replace("[", "('");
		cs = cs.replace("]", "')");
		return cs;
	}
	
	
	//Given a list of mission Ids it returns the common User involved in that mission
	//Obviously these missions are supposed to be active and it is the responsibility
	//of Service layer to send mission ids for only active missions
	public static ArrayList<String> GetCommonMissionsUsers(ArrayList<String> missionIds)
	{
		try
		{
			if(missionIds == null)
				return null;
			if(missionIds.isEmpty())
				return null;
				
			//List l = new List();
			String concatMId = convertToCommaSeparated(missionIds);
			System.out.println(concatMId);
			
			ArrayList<String> userList = new ArrayList<String>();
			//String missionid = "'M001','M002'";
			
			String query = "select distinct UserId from MissionUser where MissionId IN "+concatMId;
			System.out.println(query);
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			Connection conn = DriverManager.getConnection("jdbc:odbc:MSAccessDatabase");
			Statement s = conn.createStatement();
			s.execute(query);
			ResultSet rset = s.getResultSet();
			while(rset.next())
				userList.add(rset.getString("UserId"));
			s.close();
			conn.close();
			if(userList.isEmpty()) return null;
			return userList;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	
	//Retrieve all the missions for a give list of users
	public static ArrayList<String> GetCommonUsersMissions(ArrayList<String> userIds)
	{
		try
		{
			//List l = new List();
			String concatUId = convertToCommaSeparated(userIds);
			System.out.println(concatUId);
			
			ArrayList<String> missionList = new ArrayList<String>();
			//String missionid = "'M001','M002'";
			String query = "select distinct MissionId from MissionUser where UserId IN "+concatUId;
			System.out.println(query);
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			Connection conn = DriverManager.getConnection("jdbc:odbc:MSAccessDatabase");
			Statement s = conn.createStatement();
			s.execute(query);
			ResultSet rset = s.getResultSet();
			while(rset.next())
				missionList.add(rset.getString("MissionId"));
			s.close();
			conn.close();
			if(missionList.isEmpty()) return null;
			return missionList;
		}
		catch(Exception e)
		{
			//e.printStackTrace();
			return null;
		}
	}
	
	
	//Get the active mission for a given user
	public static ArrayList<Mission> GetActiveMissionForUser(String userId)
	{
		try
		{
			ArrayList<Mission> missionList = new ArrayList<Mission>();
			String query = "select Mission.MissionId as MissionId, MissionName from Mission, MissionUser" +
						   " where Mission.MissionId = MissionUser.MissionId"+
						   " and Active = true and UserId = '"+userId+"'";//where Active is 'true'
			System.out.println(query);
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			Connection conn = DriverManager.getConnection("jdbc:odbc:MSAccessDatabase");
			Statement s = conn.createStatement();
			s.execute(query);
			ResultSet rset = s.getResultSet();
			while(rset.next())
			{
				Mission m = new Mission();
				m.setId(rset.getString("MissionId"));
				m.setName(rset.getString("MissionName"));
				missionList.add(m);
			}
			s.close();
			conn.close();
			if(missionList.isEmpty()) return null;
			return missionList;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	
	//Testing
	public static void main(String args[])
	{
		
		ArrayList<String> missionIds = new ArrayList<String>();
		missionIds.add("M001");
		missionIds.add("M002");
		missionIds.add("M003");
		System.out.println(GetCommonMissionsUsers(missionIds));
		
		ArrayList<String> userIds = new ArrayList<String>();
		userIds.add("A0001");
		userIds.add("A0002");
		userIds.add("G0002");
		System.out.println(GetCommonUsersMissions(userIds));
		System.out.println( GetActiveMissionForUser("A0001"));
	}

}
