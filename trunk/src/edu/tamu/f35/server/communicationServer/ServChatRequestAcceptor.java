//Accept client request for chat communication

package edu.tamu.f35.server.communicationServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServChatRequestAcceptor implements Runnable
{

	private static ServChatRequestAcceptor instance = null;
	public Thread chatReqAccThread;
	ServerSocket serverSocket;
	Socket clientReqSocket;
	ServerDisplay sd;
	protected ServChatRequestAcceptor(ServerSocket ss)
	{
		sd = ServerDisplay.getInstance();
		serverSocket = ss;
		chatReqAccThread = new Thread(this, "chatReqAccThread");
	}
	
	public static ServChatRequestAcceptor getInstance(ServerSocket servSocket)
	{
		if(instance == null)
			instance = new ServChatRequestAcceptor(servSocket);
		return instance;
	}
	
	public static ServChatRequestAcceptor getInstance()
	{
		return instance;
	}
	
	public void start_up()
	{
		chatReqAccThread.start();
	}
	
	public void run() 
	{
		while(true)
		{
			try 
			{
				//Process an incoming login request from the client. If the request succeeded the
				//Process the login request
				System.out.println("Waiting for client accept");
				clientReqSocket = serverSocket.accept();
				sd.DisplayOutput("New client attempted login from: "+clientReqSocket.getInetAddress().toString());
				new ServerLoginRequestManager(clientReqSocket);	
				//System.out.println("Got one client login request");
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				System.out.println("ChatRequestAcceptorThread:Server accept failed");
				e.printStackTrace();
			}
		}
		
	}
	
	public void waitToFinish() throws Exception
	{
		chatReqAccThread.join();
	}
	

}
