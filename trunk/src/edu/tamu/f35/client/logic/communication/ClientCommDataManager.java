//A singleton class, one place to access all the data resources managed at client side
package edu.tamu.f35.client.logic.communication;

import java.net.Socket;
import java.util.ArrayList;

import edu.tamu.f35.dataCotract.Mission;
import edu.tamu.f35.dataCotract.User;

//Maintains the list of sockets and stream
//Maintains the list of all online users
//Maintains the list of Messages
public class ClientCommDataManager 
{
	private static User user;
	private static ClientCommDataManager instance = null;
	//private static Socket loginSocket;
	private static Socket genCommSocket;
	//private static ObjectInputStream genCommInputStream;
	//private static ObjectOutputStream genCommOutputStream;
	
	
	private static Socket onlineNameListSocket;
	//private static ObjectInputStream nameListInputStream; //Online user list maintenance
	//private static ObjectOutputStream nameListOutputStream;//Online user list maintenance
	
	//private static Socket logOutSocket;
	//private static ObjectOutputStream logOutOutputStream;
	//private static ObjectInputStream logOutOutputStream;
	
	private static ArrayList<User> onlineUsers;
	private static ArrayList<Mission> activeUserMission; 
	
	
	protected ClientCommDataManager()
	{
		genCommSocket = null;
		onlineNameListSocket = null;
		//logOutSocket = null;
		onlineUsers = new ArrayList<User>();
		activeUserMission = new ArrayList<Mission>();
	}
	
	
	public int SetSockets(User us, Socket genCom, Socket online/*, Socket logOut*/) throws ClientException
	{
		System.out.println("SetSockets() : Entered");
		try
		{
			user = us;
			genCommSocket = genCom;
			onlineNameListSocket = online;
			//logOutSocket = logOut;
			System.out.println("Client: returning from Set Sockets");
			System.out.println("SetSockets() : Exited");
			return 1;
		}
		catch(Exception e)
		{
			throw new ClientException("Stream is not established : "+ e.getMessage());
		}
	}
	
	//Static singleton instance 
	public static ClientCommDataManager getInstance() 
	{
		if(instance == null) 
		{
			instance = new ClientCommDataManager();
			System.out.println("ClientCommDataManager Singletone Instance Initialized");
		}
		return instance;
	}
	
	public User getUser()
	{
		return user;
	}
	
	public Socket getGenCommSocket()
	{
		return genCommSocket;
	}
	
	public Socket getOnlineListSocket()
	{
		return onlineNameListSocket;
	}
	/*
	public Socket getLogOutSocket()
	{
		return logOutSocket;
	}
	*/
	public void modifyOnlineUserList(ArrayList<User> ulist)
	{
		onlineUsers.clear();
		onlineUsers = ulist;
	}
	
	
	public void modifyActiveMissionForUserList(ArrayList<Mission> mlist)
	{
		activeUserMission.clear();
		activeUserMission = mlist;
	}
	
	public ArrayList<User> getOnlineUserList()
	{
		return onlineUsers;
	}
	
	public ArrayList<Mission> getActiveMissionList()
	{
		return activeUserMission;
	}
}
