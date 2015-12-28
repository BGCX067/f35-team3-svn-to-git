package edu.tamu.f35.client.logic.communication;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import edu.tamu.f35.dataCotract.GenPurposeCommMessage;
import edu.tamu.f35.dataCotract.LoginLogoutMessage;
import edu.tamu.f35.dataCotract.MessageType;
import edu.tamu.f35.dataCotract.User;

public class ClientCommMessageSender
{
	private ClientCommDataManager ccdm;
	
	public ClientCommMessageSender()
	{
		ccdm = ClientCommDataManager.getInstance();
	}

	public void SendBroadCastMessage(String message) throws ClientException
	{
		User user = ccdm.getUser();
		Socket genComSocket = ccdm.getGenCommSocket();
		try
		{
			System.out.println("SendBroadCastMessage():Sending Gen Purpose Message....");
			System.out.println("SendBroadCastMessage():Message Sent From Client:" + message);
			ObjectOutputStream oos = new ObjectOutputStream(genComSocket.getOutputStream());
			GenPurposeCommMessage gpcm = new GenPurposeCommMessage(user.getId(), user.getName(), null,
											MessageType.BCT, null, message);

			oos.writeObject(gpcm);			
			System.out.println("SendBroadCastMessage():Message Sent.....");
			oos.flush();
		}
		catch(Exception e)
		{
			ClientCommManager.getInstance().ManageException(e);
			throw new ClientException("Unable to send Message, Pleae try again");
		}
	}
	
	public void SendLogOutMessage() throws ClientException
	{
			User user = ccdm.getUser();
			
			//Socket logOutSocket = ccdm.getLogOutSocket();
			ClientCommManager.getInstance().setStatus(ClientCommManager.LOGGEDOUT);
			Socket onLineListSkt = ccdm.getOnlineListSocket();
			try
			{
				System.out.println("SendLogOutMessage():Sending Logout Object....");
				ObjectOutputStream oos = new ObjectOutputStream(onLineListSkt.getOutputStream());
				LoginLogoutMessage llm = new LoginLogoutMessage(user.getId(),MessageType.LGO,"Logged Out");
				oos.writeObject(llm);
				oos.flush();
				Thread.sleep(500); //Give server a bit of time to get logged off
				ClientCommManager.CleanUp();
				System.out.println("SendLogOutMessage():Logout Sent.....");
			}
			catch(Exception e)
			{
				ClientCommManager.getInstance().ManageException(e);
				throw new ClientException("Problem in logout");
			}
	}

	public void SendPrivateMessage(String message, ArrayList<String> intendedUsers,
			ArrayList<String> intendedMissions)  throws ClientException
	{
		User user = ccdm.getUser();
		Socket genComSocket = ccdm.getGenCommSocket();
		try
		{
			System.out.println("SendBroadCastMessage():Sending Private Gen Purpose Message....");
			System.out.println("SendBroadCastMessage():Message Sent From Client:" + message);
			ObjectOutputStream oos = new ObjectOutputStream(genComSocket.getOutputStream());
			GenPurposeCommMessage gpcm = new GenPurposeCommMessage(user.getId(), user.getName(), intendedUsers,
											MessageType.PVT, intendedMissions, message);

			oos.writeObject(gpcm);			
			System.out.println("SendBroadCastMessage():Message Sent.....");
			oos.flush();
		}
		catch(Exception e)
		{
			ClientCommManager.getInstance().ManageException(e);
			throw new ClientException("Unable to send Message, Pleae try again");
		}
		
	}
}
