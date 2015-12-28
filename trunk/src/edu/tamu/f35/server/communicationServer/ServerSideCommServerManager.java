//To start all the processes

package edu.tamu.f35.server.communicationServer;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerSideCommServerManager 
{
	//private final static int LOGINSERVSOCKET = 2001; //Chat communication server socket
	private final static int CHATSERVSOCKET = 2002; //Chat communication server socket
	private ServerSocket loginServSocket;
	private static int loginServPort;

	public static ServerDisplay sd;
	static private ServChatRequestAcceptor chatReqAcceptor;
	
	public ServerSideCommServerManager() throws IOException
	{
		
		loginServPort = Integer.parseInt(ServerConfiguration.getProperty("loginport"));
		loginServSocket = new ServerSocket(loginServPort);
		
		
		sd = ServerDisplay.getInstance();
		chatReqAcceptor = ServChatRequestAcceptor.getInstance(loginServSocket);
		chatReqAcceptor.start_up();
		/*try
		{
			Sender.StartSender();
		}
		catch(Exception e)
		{
			System.out.println("Unable to start voice communication");
		}*/
	}
	
	public static void main(String args[])
	{
	
		try
		{
			new ServerSideCommServerManager();
			sd.DisplayOutput("Chat Server Started at Port: "+CHATSERVSOCKET);
			System.out.println("Server Started");
			Thread.sleep(500);
			chatReqAcceptor.waitToFinish();
		}
		catch(Exception e)
		{
			System.out.println("Unable to start server--> ");
			e.printStackTrace();
		}
	}
}
