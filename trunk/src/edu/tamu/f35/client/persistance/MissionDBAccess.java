package edu.tamu.f35.client.persistance;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class MissionDBAccess 
{
	static ArrayList<String> GetActiveMissions()
	{
		try
		{
			ArrayList<String> missionList = new ArrayList<String>();
			String query = "select MissionId from Mission where Active = true ";//where Active is 'true'
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
			e.printStackTrace();
			return null;
		}
	}
}
