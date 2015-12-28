package edu.tamu.f35.client.logic.communication;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import edu.tamu.f35.client.gui.communication.ClientChatWidowUI;
import edu.tamu.f35.dataCotract.LoginAckMessage;
import edu.tamu.f35.dataCotract.LoginLogoutMessage;
import edu.tamu.f35.dataCotract.MessageType;
import edu.tamu.f35.dataCotract.User;

public class ClientLoginManager 
{
	private String hostIpAddress;
	
	private Socket loginSocket;
	//private ObjectInputStream loginObjectInputStream;
	//private ObjectOutputStream loginObjectoutputStream;
	
	
	private Socket genCommSocket;
	private Socket listMaintanaceSocket;
	//private Socket logOutSocket;
	

	
	private void CloseConnection() throws Exception
	{
		//loginObjectInputStream.close();
		//loginObjectoutputStream.close();
		loginSocket.close();
	}
	
	private void OpenCommunicationPorts(User user) throws Exception
	{	
		System.out.println("ClientLoginManager::OpenCommunicationPorts(): Opening other ports");
		try
		{
			//System.out.println("Client: Inside OpenCommunicationPorts()");
			int port = Integer.parseInt(ClientConfiguration.getProperty("chatport"));
			//System.out.println("Clinet: Get the other port:"+port);
			//System.out.println("Client: Waiting for genSocket");
			genCommSocket = new Socket(hostIpAddress, port);
			//System.out.println("Client: Waiting for listMaintanaceSocket");
			listMaintanaceSocket = new Socket(hostIpAddress, port);
			//System.out.println("Client: Waiting for logOutSocket");
			//logOutSocket = new Socket(hostIpAddress, port);
			ClientCommDataManager ccdm = ClientCommDataManager.getInstance();
			//System.out.println("ClientLoginManager::OpenCommunicationPorts(): Ports Opened Successfully");
			ccdm.SetSockets(user, genCommSocket, listMaintanaceSocket/*, logOutSocket*/);
			System.out.println("ClientLoginManager::OpenCommunicationPorts(): Opening other ports Successfull");
			
		}
		catch(Exception e)
		{
			throw e;
		}
		
	}
	public ClientLoginManager(String userName, String hostIp) throws ClientException, Exception
	{
		System.out.println("In Client...");
		int port;
		try
		{		
			ClientCommManager.getInstance().setStatus(ClientCommManager.BEGIN_CONNECT);
			hostIpAddress = hostIp;
			port = Integer.parseInt(ClientConfiguration.getProperty("loginport"));	
			//System.out.println(port);
			//System.out.println("Waiting for server connection");
			//1:Open the server connection and authenticate itself
			loginSocket = new Socket(hostIpAddress, port);
			System.out.println("Connection Accepted");
			ObjectOutputStream loginObjectoutputStream = new ObjectOutputStream(loginSocket.getOutputStream());
			//Send the user id to server in a Login Message to perform authentication
			//1.1: Create a new login message object
			LoginLogoutMessage loginRequest = new LoginLogoutMessage(userName, MessageType.LGI,"A Login Request");
			//System.out.println("Sending Login Object"+ loginRequest.senderId);
			//1.2: Write(send) to the server
			loginObjectoutputStream.writeObject(loginRequest);
			loginObjectoutputStream.flush();
			//2: Receive the acknowledgment and check whether login is successful or not
			ObjectInputStream loginObjectInputStream = new ObjectInputStream(loginSocket.getInputStream());
			//2.1: Get the Acknowledgment object
			Object ackObj = loginObjectInputStream.readObject();
			//2.2 if it is an acknowledgment then continue with login else throw exception
			if(ackObj instanceof LoginAckMessage)
			{
				LoginAckMessage ackMessage = (LoginAckMessage)ackObj;
				//System.out.println("***1");
				if(ackMessage.reqStatus == LoginAckMessage.SUCCEED)
				{
					ClientCommManager.getInstance().setStatus(ClientCommManager.CONNECTING);
					//System.out.println("Client: Opening other ports");
					OpenCommunicationPorts(ackMessage.user);
					//System.out.println("***2");
					loginObjectoutputStream.close();
					loginObjectInputStream.close();
					CloseConnection();
					//System.out.println("***3");
					new ClientOnlineListManager();
					new ClientGenCommReceiver();
					ClientCommManager.getInstance().setStatus(ClientCommManager.CONNECTED);
					ClientChatWidowUI.getInstance().refreshNotification("WELCOME: "+ackMessage.user.getName()+"("+ackMessage.user.getId()+")");
				}
				else
				{
					//System.out.println("***4");
					loginObjectInputStream.close();
					loginObjectoutputStream.close();
					CloseConnection();
					System.out.println("***5");
					ClientCommManager.getInstance().setStatus(ClientCommManager.DISCONNECTED);
					throw new ClientException(ackMessage.message);
				}
				//Perform creation of dedicated sockets
			}
			else
			{
				try
				{
					CloseConnection();
				}
				catch(Exception ex)
				{System.out.println("Check this exception" + ex.getMessage());}
				
				throw new ClientException("Some error while connection establishment ");
			}
		}
		catch(NumberFormatException ex) 
		{
			throw new ClientException("Configuration File Port Value is not correctly Set Up "+ex.getMessage());
		}
		catch(UnknownHostException ex)
		{
			throw new ClientException("Unable to connect, Unknown host" + ex.getMessage());
		}
		catch(IOException ex)
		{
			throw new ClientException("Unable to connect, connection establisment failed" + ex.getMessage());
		}
	}
}
