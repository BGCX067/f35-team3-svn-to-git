package edu.tamu.f35.server.communicationServer;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;

import edu.tamu.f35.client.persistance.MissionUserDBAccess;
import edu.tamu.f35.dataCotract.Mission;
import edu.tamu.f35.dataCotract.User;


class UserInformation
{
	public final static int INITIALIZED = 0;
	public final static int ONLINE = 1;
	public final static int LOGGEDOUT = 2;
	public final static int FAULTED = 3;
	
	User user;
	Socket genCommSocket;
	Socket onLineListSocket;
	int status = INITIALIZED;
	//Socket logOutSocket;
	//ObjectInputStream genOis;
	//ObjectOutputStream genOos;
	ArrayList<Mission> activeUserMissions = new ArrayList<Mission>();  //List of the mission in which this user is involved
}

public class ServCommDataManager 
{
	private static Hashtable<String, UserInformation> onlineUsers;
	
	private static ServCommDataManager instance = null;
	
	protected ServCommDataManager()
	{
		onlineUsers = new Hashtable<String, UserInformation>();
	}
	
	public static ServCommDataManager getInstance()
	{
		if(instance == null)
			instance = new ServCommDataManager();
		return instance;
	}
	
	public void AddUser(User user, Socket genSocket, Socket onLineListSocket/*, Socket logOutSocket*/)
	{
		System.out.println("AddUser()Entered:" );
		try
		{
			UserInformation ui = new UserInformation();
			ui.user = user;
			//System.out.println("Inside AddUser(): user added");
			ui.genCommSocket = genSocket;
			//System.out.println("Inside AddUser(): gen socket ");
			ui.onLineListSocket = onLineListSocket;
			//ui.logOutSocket = logOutSocket;
			ui.status = UserInformation.ONLINE;
			System.out.println("AddUser() to user informations "+ui.user.getId());
			onlineUsers.put(user.getId(), ui);
			
			ui.activeUserMissions = MissionUserDBAccess.GetActiveMissionForUser(ui.user.getId());
			//System.out.println("Returning from add user");
			//System.out.println(onlineUsers.toString());
			System.out.println("AddUser()Exiting : User Added"+user.getId());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public UserInformation getUserInformation(String userId)
	{
		return onlineUsers.get(userId);
	}
	public void removeAUserInformation(String userId)
	{
		if(onlineUsers != null)
		{
			onlineUsers.remove(userId);
			System.out.println("removeAUserInformation():"+userId);
		}
	}
	//Returns the socket used by a user for general purpose communication
	public Socket getGenUserSocket(String userId)
	{
		return onlineUsers.get(userId).genCommSocket;
	}
	
	public Socket getOnlineListUserSocket(String userId)
	{
		return onlineUsers.get(userId).onLineListSocket;
	}
	
	public ArrayList<Mission> getOnlineUserMissionList(String userId)
	{
		return onlineUsers.get(userId).activeUserMissions;
	}
	/*public Socket getLogoutUserSocket(String userId)
	{
		return onlineUsers.get(userId).logOutSocket;
	}*/
	
	public Hashtable<String, UserInformation> getOnlineUserInformations()
	{
		return onlineUsers;
	}
	
	public ArrayList<User> getOnlineUsers()
	{
		ArrayList<User> ulist = new ArrayList<User>();
		
		for (UserInformation ui : onlineUsers.values())
		{
			if(ui.status != UserInformation.FAULTED)
				ulist.add(ui.user);
		}
		return ulist;
	}
	
	public boolean userExist(String userId)
	{
		return onlineUsers.containsKey(userId);
	}
	
	synchronized
	public int getUserStatus(String userId)
	{
		try
		{
			return onlineUsers.get(userId).status;
		}
		catch(Exception e)
		{
			return -1;
		}
	}
	
	synchronized
	public void setUserStatus(String userId, int s)
	{
		if(onlineUsers.get(userId)!= null)
		{
			onlineUsers.get(userId).status = s;
			System.out.println("setUserStatus():Status set to: "+onlineUsers.get(userId).status);
		}
		else
		{
			System.out.println("No user to set");
		}
	}
}
