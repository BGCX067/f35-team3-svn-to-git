package edu.tamu.f35.server.communicationServer;

import java.io.ObjectInputStream;
import java.net.Socket;

import edu.tamu.f35.dataCotract.LoginLogoutMessage;
import edu.tamu.f35.dataCotract.MessageType;
import edu.tamu.f35.dataCotract.User;


//For each user there is a thread, listen to only logout request
public class ServOnlineListManager implements Runnable
{
	Thread thisThread;
	Socket onlineListSocket;
	User user;
	ServCommDataManager scdm;
	ServChatCommManager sccm;
	ServerDisplay sd;
	
	public ServOnlineListManager(User user)
	{
		scdm = ServCommDataManager.getInstance();
		sccm = ServChatCommManager.getInstance();
		sd = ServerDisplay.getInstance();
		this.user = user;
		onlineListSocket = scdm.getOnlineListUserSocket(this.user.getId());
		thisThread = new Thread(this);
		thisThread.start();
	}
	
	public void run()
	{
		try
		{
			System.out.println("ServOnlineListManager:Waiting for log out request from user " + user.getId());
			ObjectInputStream ois = new ObjectInputStream(onlineListSocket.getInputStream());
			Object obj = ois.readObject();
			if(obj instanceof LoginLogoutMessage)
			{
				
				LoginLogoutMessage llm = (LoginLogoutMessage)obj;
				sd.DisplayOutput("ServOnlineListManager:Login out user "+ llm.senderId);
				UserInformation  ui = scdm.getOnlineUserInformations().get(user.getId());
				
				//Crucial setting
				ui.status = UserInformation.LOGGEDOUT;
				
				scdm.setUserStatus(user.getId(), UserInformation.LOGGEDOUT);
				ServerDisplay.getInstance().DisplayOutput("User is Logged out: "+user.getName()+"("+user.getId()+")");   
				scdm.removeAUserInformation(user.getId());
				//System.out.println("ServOnlineListManager:Distrubuting List of Users");
				sccm.DistributeUpdateOnlineUserList();
				sccm.informLogInOut(user, MessageType.LGO);
				sccm.CleanUpUserInfo(ui);
			}
		}
		catch(Exception e)
		{
			System.out.println("EXCEPTION:ServOnlineListManager(User user) : Log out read");
			ServChatCommManager.getInstance().ManageException(e, user.getId());
		}
	}
		
}
