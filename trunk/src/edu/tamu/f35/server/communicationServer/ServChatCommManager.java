package edu.tamu.f35.server.communicationServer;

import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import edu.tamu.f35.client.persistance.MessageLogDBAccess;
import edu.tamu.f35.dataCotract.GenPurposeCommMessage;
import edu.tamu.f35.dataCotract.MessageType;
import edu.tamu.f35.dataCotract.OnlineUserListMessage;
import edu.tamu.f35.dataCotract.RestoreMessage;
import edu.tamu.f35.dataCotract.User;


class CleanUpUser implements Runnable
{
	UserInformation uinfo;
	Thread thisThread;
	public CleanUpUser(UserInformation ui)
	{
		uinfo = ui;
		thisThread = new Thread(this);
		thisThread.start();
	}
	
	public void run()
	{
		try
		{
			uinfo.genCommSocket.close();
			uinfo.onLineListSocket.close();
		}
		catch(Exception e)
		{
			System.out.println("This will surely throw error - subsiding it for now");
		}
		
	}
}

class DistributeUpdateList implements Runnable
{
	Thread thisThread;
	ServCommDataManager scdm;

	DistributeUpdateList()
	{
		scdm = ServCommDataManager.getInstance();
		thisThread = new Thread(this);
		thisThread.start();
	}
	
	public void run() 
	{
		ArrayList<User> ulist = scdm.getOnlineUsers();

		System.out.println("Distibution has elements: "+ulist.size());
		System.out.println("DistributeUpdateList():Sending User List");
		
		for(UserInformation ui : ServCommDataManager.getInstance().getOnlineUserInformations().values())
		{
			if(ui.status == UserInformation.FAULTED)
				continue;
			OnlineUserListMessage oulm = new OnlineUserListMessage("SERVER", ulist, ui.activeUserMissions);
			try
			{
				System.out.println("Sending updated list to : "+ui.user.getId()+" "+ui.user.getName()+" Status:"+ui.status );
				ObjectOutputStream oos = new ObjectOutputStream(ui.onLineListSocket.getOutputStream());
				oos.writeObject(oulm);
				oos.flush();
				System.out.println("Sending updated list SUCCESS to : "+ui.user.getId()+" "+ui.user.getName());
			}
			catch(Exception e)
			{
				System.out.println("Sending updated list to : "+ui.user.getId()+" "+ui.user.getName());
				e.printStackTrace();
			}
		}
	}
	
}

class InformLogInOut implements Runnable
{
	Thread t;
	User user;
	ServCommDataManager scdm;
	MessageType msgType;
	public InformLogInOut(User u, MessageType mt)
	{
		user = u;
		msgType = mt;
		scdm = ServCommDataManager.getInstance();
		t = new Thread(this);
		t.start();
	}
	public void run()
	{
		
		GenPurposeCommMessage gpcm = new GenPurposeCommMessage(user.getId(), user.getName(), null, 
										msgType, null, "Login/Logout");
		System.out.println("InformLogInOut(): In login logout: "+msgType+ "  "+user.getId());
		for(UserInformation ui : ServCommDataManager.getInstance().getOnlineUserInformations().values())
		{
			System.out.println("InformLogInOut(): Sending logout.login list to "+ui.user.getId());
			if(ui.user.getId() == user.getId() || ui.status == UserInformation.FAULTED)
				continue;
			try
			{
				System.out.println("InformLogInOut(): Started login logout send to "+ui.user.getId());
				ObjectOutputStream oos = new ObjectOutputStream(ui.genCommSocket.getOutputStream());
				oos.writeObject(gpcm);
				oos.flush();
				System.out.println("InformLogInOut(): Started login logout send to "+ui.user.getId());
				//System.out.println("User List Sent");
			}
			catch(Exception e)
			{
				System.out.println("InformLogInOut(): FAILED SEND login logout to "+ui.user.getId());
				e.printStackTrace();
			}
		}
		
	}
}


public class ServChatCommManager 
{	
	//private static final int CHATSERVSOCKET = 2002;
	private ServerSocket chatServSocket;
	private static ServChatCommManager instance = null;
	int chatPort;
	protected ServChatCommManager()
	{
		try
		{
			chatPort = Integer.parseInt(ServerConfiguration.getProperty("chatport"));
			chatServSocket = new ServerSocket(chatPort);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static ServChatCommManager getInstance()
	{
		if(instance == null)
			instance = new ServChatCommManager();
		return instance;
	}
	
	public ServerSocket getChatServSocket()
	{
		return chatServSocket;
	}
	
	public void DistributeUpdateOnlineUserList()
	{
		new DistributeUpdateList();
	}
	
	public void CleanUpUserInfo(UserInformation ui)
	{
		new CleanUpUser(ui);
	}
	
	public void informLogInOut(User user, MessageType mt)
	{
		new InformLogInOut(user, mt);
	}
	
	synchronized
	public void ManageException(Exception e, String userId)
	{
		ServCommDataManager scdm = ServCommDataManager.getInstance();
		int st = scdm.getUserStatus(userId);
		if(st == -1 || st == UserInformation.LOGGEDOUT)
		{
			System.out.println("ManageException(), user is aleady logged out" + userId);
			e.printStackTrace();
		}
		else
		{
			System.out.println("ManageException(), user status is set to faulted" + userId);
			scdm.setUserStatus(userId, UserInformation.FAULTED);
			ServerDisplay.getInstance().DisplayOutput("Thread is making User in FAULTED STATE "+userId);
			DistributeUpdateOnlineUserList();
		}
	}

	public void sendRestoreMessage(User user) 
	{
		System.out.println("sendRestoreMessage");
		try
		{
			System.out.println("sendRestoreMessage() - restoring message for user: "+ user.getId());
			Socket genOut =  ServCommDataManager.getInstance().getGenUserSocket(user.getId());
			ArrayList<RestoreMessage> resMessages = MessageLogDBAccess.getInstance().retreiveMessages(user.getId());
			GenPurposeCommMessage gpcm = new GenPurposeCommMessage("SERVER", MessageType.RES, resMessages);
			ObjectOutputStream oos = new ObjectOutputStream(genOut.getOutputStream());
			oos.writeObject(gpcm);
			oos.flush();
			System.out.println("sendRestoreMessage() - restoring sent successfull to: "+ user.getId());
		}
		catch(Exception e)
		{
			System.out.println("Restore Message Failed for user "+user.getId());
			e.printStackTrace();
		}
	}
}
