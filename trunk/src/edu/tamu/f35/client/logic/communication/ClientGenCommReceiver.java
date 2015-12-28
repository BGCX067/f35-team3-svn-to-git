package edu.tamu.f35.client.logic.communication;

import java.io.ObjectInputStream;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import edu.tamu.f35.client.gui.communication.ClientChatWidowUI;
import edu.tamu.f35.dataCotract.GenPurposeCommMessage;
import edu.tamu.f35.dataCotract.MessageType;
import edu.tamu.f35.dataCotract.RestoreMessage;


//This class is used to receive a general purpose communication message
public class ClientGenCommReceiver implements Runnable
{
	Thread genComThrd;
	Socket genCommSocket;
	
	public ClientGenCommReceiver()
	{
		ClientCommDataManager ccdm = ClientCommDataManager.getInstance();
		genCommSocket = ccdm.getGenCommSocket();
		genComThrd = new Thread(this);
		genComThrd.start();
	}
	
	public static String dateNow()
	{
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		return dateFormat.format(new Date());
	}
	
	@Override
	public void run() 
	{
		System.out.println("ClientGenCommReceiver():Waiting for message");
		try
		{
			while(true)
			{
				System.out.println("ClientGenCommReceiver():here in communication server receiver");
				ObjectInputStream ois = new ObjectInputStream(genCommSocket.getInputStream());
				Object object = ois.readObject();
				if(object instanceof GenPurposeCommMessage)
				{
					ArrayList<String> msg = new ArrayList<String>();
					GenPurposeCommMessage gpcm = (GenPurposeCommMessage)object;
					if(gpcm.msgType == MessageType.LGI)
					{
						msg.add("["+dateNow()+"] "+gpcm.senderName +"("+gpcm.senderId+")"+" is online");
					}
					else if(gpcm.msgType == MessageType.LGO)
						msg.add("["+dateNow()+"] "+gpcm.senderName +"("+gpcm.senderId+")"+" has logged out");
					else if(gpcm.msgType == MessageType.RES)
					{
						ArrayList<RestoreMessage> resmsgs = gpcm.resMessages;
						for(RestoreMessage rm : resmsgs)
						{
							msg.add("["+rm.time+"] "+rm.senderName +"("+rm.senderId+")"+"["+rm.msgType+"]");
							msg.add(rm.message);
						}
					}
					else 
					{
						msg.add("["+dateNow()+"] "+gpcm.senderName +"("+gpcm.senderId+")"+"["+gpcm.msgType+"]");
						msg.add(gpcm.message);
					}
					System.out.println("Message Received:"+msg);
					ClientChatWidowUI.getInstance().refreshText(msg);
				}
			}
		}
		catch(Exception e)
		{
			//e.printStackTrace();
			ClientCommManager.getInstance().ManageException(e);
			ClientChatWidowUI.getInstance().refreshNotification("Problem in receiving messages");
		}
		
	}
}
