package edu.tamu.f35.server.communicationServer;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import edu.tamu.f35.client.persistance.MessageLogDBAccess;
import edu.tamu.f35.client.persistance.MissionUserDBAccess;
import edu.tamu.f35.dataCotract.GenPurposeCommMessage;
import edu.tamu.f35.dataCotract.MessageType;
import edu.tamu.f35.dataCotract.User;

class ThreadedDBWriter implements Runnable
{
	String message;
	Thread t;
	int runOption = 1;
	String senderId;
	ArrayList<String> receiverIds;
	ThreadedDBWriter(String senderId, String msg)
	{
		runOption  = 1;
		this.senderId = senderId;
		message = msg;
		t = new Thread(this);
		t.start();
	}
	
	ThreadedDBWriter(String senderId, ArrayList<String> receiverIds, String msg)
	{
		runOption = 2;
		this.senderId = senderId;
		this.receiverIds = receiverIds;
		message = msg;
		t = new Thread(this);
		t.start();
	}
	
	public void run() 
	{		
		switch(runOption)
		{
		case 1:
			MessageLogDBAccess.getInstance().createBroadCastMessage(senderId, message);
			break;
		case 2:
			MessageLogDBAccess.getInstance().createPrivateMessage(senderId, receiverIds, message);
		}
		
	}
	
}

//Manages Messages on general communication socket, for each user there is a thread
public class ServGenCommManager implements Runnable
{
	String userId;
	UserInformation userInformation;
	Socket genCommSocket;
	Thread userSpecificThread;
	ServCommDataManager scdm;

	public ServGenCommManager(User user)
	{
		this.userId = user.getId();
		System.out.println("ServGenCommManager: Starting step for listener for User Id "+this.userId);
		genCommSocket = ServCommDataManager.getInstance().getGenUserSocket(this.userId);
		System.out.println("Socket: "+ genCommSocket.toString());
		scdm = ServCommDataManager.getInstance();
		userSpecificThread = new Thread(this);	
		userSpecificThread.start();
	}
	@Override
	public void run() 
	{
		try
		{
			while(true)
			{
				System.out.println("ServGenCommManager: "+this.userId+" Waiting for object read in ");
				ObjectInputStream ois = new ObjectInputStream(genCommSocket.getInputStream());
				Object messageObject = ois.readObject();
				if(messageObject instanceof GenPurposeCommMessage)
				{
					GenPurposeCommMessage gpcm = (GenPurposeCommMessage) messageObject;
					System.out.println("ServGenCommManager: Message Received at server for: "+userId+" message:"+ gpcm.message);
					System.out.println("This message is from: "+gpcm.senderId +"     " +gpcm.senderName);
					if (gpcm.msgType == MessageType.PVT)
					{
						distributePrivate(gpcm);
					}
					else if  (gpcm.msgType == MessageType.BCT)
					{
						distributeToAll(gpcm);
						
					}
					else
					{
						System.out.println("Server received a general purpose message which is "+gpcm.msgType+" "+gpcm.message);
						System.out.println("****IT SHOULD NOT BE HERE****");
					}
				}
			}
			
		}
		catch(Exception e)
		{
			System.out.println("EXCEPTION:ServGenCommManager(User user) : Input Read for user "+ userId);
			ServChatCommManager.getInstance().ManageException(e, userId);
		}
	}
	
	private void distributeToAll(GenPurposeCommMessage gpcm)
	{
		System.out.println("In distribute all");
		new ThreadedDBWriter(gpcm.senderId, gpcm.message);
		for(UserInformation ui : ServCommDataManager.getInstance().getOnlineUserInformations().values())
		{
			try
			{
				System.out.println("distributeAll(): Private Message is sending to "+ ui.user.getId());
				ObjectOutputStream oos = new ObjectOutputStream(ui.genCommSocket.getOutputStream());
				oos.writeObject(gpcm);
				oos.flush();
			}
			catch(Exception e)
			{
				System.out.println("distributeToAll() FAILED: to"+ ui.user.getId()+" "+ui.status+" for message "+gpcm.message);
				e.printStackTrace();
			}
			
		}

	}
	
	private void distributePrivate(GenPurposeCommMessage gpcm)
	{
		HashSet<String> userIdsToSend = new HashSet<String>();
		//A: For a user perform
		//1. Get the list of all online users
		ArrayList<String> onlineUsers = Collections.list(scdm.getOnlineUserInformations().keys());
		//2. Get the list of intended receiver of this message
		ArrayList<String> intendedReceivers = new ArrayList<String>();
		if(gpcm.recvIds != null)
			intendedReceivers = gpcm.recvIds;
		//3. Get the list of all online users for all the missions for the user
		ArrayList<String> userMissionUsers = new ArrayList<String>();
		if(gpcm.missionIds != null)
			userMissionUsers = MissionUserDBAccess.GetCommonMissionsUsers(gpcm.missionIds);
		
		userIdsToSend.addAll(intendedReceivers);
		if(userMissionUsers != null)
			userIdsToSend.addAll(userMissionUsers);
		//Oops!!! send to the sender too
		userIdsToSend.add(gpcm.senderId);
		
		userIdsToSend.retainAll(onlineUsers);
		
		System.out.println("distributePrivate(): Private Message is sending to "+ userIdsToSend);
		new ThreadedDBWriter(gpcm.senderId, new ArrayList<String>(userIdsToSend), gpcm.message);
		for(String uid : userIdsToSend)
		{
			try
			{
				System.out.println("distributePrivate(): Private Message is sending to "+ uid);
				ObjectOutputStream oos = new ObjectOutputStream(scdm.getGenUserSocket(uid).getOutputStream());
				oos.writeObject(gpcm);
				oos.flush();
			}
			catch(Exception e)
			{
				System.out.println("distributeToAll() FAILED: to"+ uid+" "+scdm.getUserStatus(uid)+" for message "+gpcm.message);
				e.printStackTrace();
			}
		}
	}

}
