package edu.tamu.f35.client.logic.communication;

import java.io.ObjectInputStream;
import java.net.Socket;

import edu.tamu.f35.client.gui.communication.ClientChatWidowUI;
import edu.tamu.f35.dataCotract.OnlineUserListMessage;

public class ClientOnlineListManager implements Runnable 
{

	private Thread thisThread;
	ClientCommDataManager ccdm;
	String userId;
	Socket onLineListSkt;
	
	public ClientOnlineListManager()
	{
		ccdm = ClientCommDataManager.getInstance();
		userId = ccdm.getUser().getId();
		onLineListSkt = ccdm.getOnlineListSocket();
		thisThread = new Thread(this);
		thisThread.start();
	}
	public void run() 
	{
		System.out.println("ClientOnlineListManager():Online list manager thread is running Waiting for Login/Logout new list");
			try
			{
				while(true)
				{
					System.out.println("ClientOnlineListManager():Online list manager thread is running Waiting for Login/Logout new list");
					ObjectInputStream ois = new ObjectInputStream(onLineListSkt.getInputStream());
					System.out.println("ClientOnlineListManager():New List Received");
					Object obj = ois.readObject();
					if(obj instanceof OnlineUserListMessage)
					{
						OnlineUserListMessage olum = (OnlineUserListMessage) obj;
						if(olum.userList != null)
							ccdm.modifyOnlineUserList(olum.userList);
						
						if(olum.missionList != null)
							ccdm.modifyActiveMissionForUserList(olum.missionList);
						
						System.out.println("ClientOnlineListManager():Received:" + olum.userList.get(0).getId());
						ClientChatWidowUI.getInstance().refreshList();
					}
				}
			}
			catch(Exception e)
			{
				ClientCommManager.getInstance().ManageException(e);
				ClientChatWidowUI.getInstance().refreshNotification("Problem in refresing list");
			}
		
	}
	
}
