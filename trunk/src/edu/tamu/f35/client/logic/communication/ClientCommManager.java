//Main Class to start Client Communication
package edu.tamu.f35.client.logic.communication;
import edu.tamu.f35.client.gui.communication.ClientCommLoginUI;
import edu.tamu.f35.client.gui.communication.ClientCommTopLevelUI;

class CleanUpUser implements Runnable
{
	Thread thisThread;
	
	CleanUpUser()
	{
		thisThread = new Thread(this);
		thisThread.start();
	}
	public void run()
	{
		try
		{
			ClientCommDataManager ccdm = ClientCommDataManager.getInstance();
			ccdm.getGenCommSocket().close();
			ccdm.getOnlineListSocket().close();
		}
		catch(Exception e)
		{
			System.out.println("Expected Cleanup Issues");
			e.printStackTrace();
		}
	}
}

public class ClientCommManager implements Runnable
{
	public Thread self;

	public final static int BEGIN_CONNECT = 1;
	public final static int CONNECTED = 2;
	public final static int CONNECTING = 3;
	public final static int DISCONNECTED = 4;
	public final static int FAULTED = 5;
	public final static int CONNECTED2 = 6;
	public final static int LOGGEDOUT = 7;
	public final static int FAULTED2 = 8;
	//public final static int 
	public static int connectionStatus = DISCONNECTED;
	
	ClientCommTopLevelUI topLevelUiInstance;
	
	private static ClientCommManager instance = null;
	protected ClientCommManager()
	{
		connectionStatus = DISCONNECTED;
		self = new Thread(this, "ClientCommManagerThread");
		System.out.println("Client Communication Manager - Starting Thread");
		self.start();
	}
	
	public static ClientCommManager getInstance()
	{
		if(instance == null)
		{
			instance = new ClientCommManager();
		}
		return instance;
	}
	
	synchronized
	public void setStatus(int status)
	{
		connectionStatus = status;
	}

	@Override
	public void run() 
	{
		// TODO Auto-generated method stub
		while(true)
		{
		    try 
		    { // Poll the state of communication server at every one second
		      Thread.sleep(1000);
		    }
		    catch (InterruptedException e) {}
			switch(connectionStatus)
			{
				case BEGIN_CONNECT:
					System.out.println("In Begin Connect State: Do Nothing");
					break;
				case CONNECTING:
					//connectionStatus = CONNECTED;
					System.out.println("Client in CONNECTING state - do nothing");
					break;
				case DISCONNECTED:
					System.out.println("Client in DISCONNECTED state - do nothing");
					break;
				case CONNECTED:
					//System.out.println("Client in CONNECTED state - Launch Chat UI");
					//System.out.println("In Connecting State: Show Chat UI");
					topLevelUiInstance = ClientCommTopLevelUI.getInstance();
					topLevelUiInstance.setLayoutPane(ClientCommTopLevelUI.CHATPANEL);
					connectionStatus = CONNECTED2;
					break;
				case CONNECTED2:
					System.out.println("Client in CONNECTED2 UI Launced do nothing");
					break;
				case FAULTED:
					topLevelUiInstance = ClientCommTopLevelUI.getInstance();
					topLevelUiInstance.setLayoutPane(ClientCommTopLevelUI.LOGINPANEL);
					System.out.println("In Faulted state: Restore Connection");
					connectionStatus = FAULTED2;
					handleFaultedState();
					//Try reconnect in a separate
					//System.out.println("In Faulted state: Restore Connection");
					break;
				case FAULTED2:
					System.out.println("In Faulted2 state: Restore Connection");
					handleFaultedState();
					break;
				case LOGGEDOUT:
					topLevelUiInstance = ClientCommTopLevelUI.getInstance();
					topLevelUiInstance.setLayoutPane(ClientCommTopLevelUI.LOGINPANEL);
					connectionStatus = DISCONNECTED;
				default:
					System.out.println("Default Print - Do Nothing");
					break;
			}
		}
	}
	
	public static void CleanUp()
	{
		new CleanUpUser();
	}
	
	private static void sleep()
	{
		try
		{
			Thread.sleep(500);
		}
		catch(Exception e){}
	}
	private static void handleFaultedState()
	{
		sleep();
		ClientCommLoginUI.getInstance().refreshNotification("Link is down, Reconnecting.....");
		sleep();
		ClientCommDataManager ccdm  = ClientCommDataManager.getInstance();
		try
		{		
			String hostIp = (String)ClientConfiguration.getProperty("hostip");
			String userId = ccdm.getUser().getId();
			new ClientLoginManager(userId, hostIp);
		}
		catch(Exception e)
		{			
			connectionStatus = FAULTED;
			ClientCommLoginUI.getInstance().refreshNotification("Reconnection failed.....");
		}
	}
	synchronized
	public void ManageException(Exception e)
	{
        //ClientCommDataManager ccdm = ClientCommDataManager.getInstance(); 
		if(connectionStatus == LOGGEDOUT)
		{
			//do nothing 
			System.out.println("Exception while reading message, but client is logged out");
			e.printStackTrace();
		}
		else
		{
			System.out.print("Exception while reading message, but client is online, faulted state");
			connectionStatus = FAULTED;
		}
	}
	
}
