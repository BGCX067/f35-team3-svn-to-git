package edu.tamu.f35.server.communicationServer;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import edu.tamu.f35.client.persistance.UserDBAccess;
import edu.tamu.f35.dataCotract.LoginAckMessage;
import edu.tamu.f35.dataCotract.LoginLogoutMessage;
import edu.tamu.f35.dataCotract.MessageType;
import edu.tamu.f35.dataCotract.User;

//Process a login request on a separate thread

public class ServerLoginRequestManager implements Runnable
{
	Thread loginRequestThrd;
	Socket clientLoginRequestSocket;
	Socket genCommSocket;
	Socket onlineListSocket;
	Socket logoutSocket;
	User user;
	ServerDisplay sd; 
	int isFaultedLogin;
	
	private void OpenCommunicationPorts()
	{
		System.out.println("OpenCommunicationPorts() Entered");
		try
		{		
			ServChatCommManager sccm = ServChatCommManager.getInstance();
			ServerSocket ss = sccm.getChatServSocket();
			genCommSocket = ss.accept();
			onlineListSocket = ss.accept();
			//System.out.println("Online accepted");
			//logoutSocket = ss.accept();
			//System.out.println("Gen Communication Socket accepted");			
			ServCommDataManager scdm = ServCommDataManager.getInstance();
			//System.out.println("got the data mamnager instance");
			scdm.AddUser(user, genCommSocket, onlineListSocket/*, logoutSocket*/);
			System.out.println("OpenCommunicationPorts(): Exited");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	public ServerLoginRequestManager(Socket clientRequestSkt)
	{
		sd = ServerDisplay.getInstance();
		clientLoginRequestSocket = clientRequestSkt;
		loginRequestThrd = new Thread(this, "Login Request Manager");
		loginRequestThrd.start();
	}
	
	public void run() 
	{
		// TODO Auto-generated method stub
		try
		{
			System.out.println("Waiting for a accept object");
			ObjectInputStream ois = new ObjectInputStream(clientLoginRequestSocket.getInputStream());
			Object inputObj = ois.readObject();
			if(inputObj instanceof LoginLogoutMessage)
			{
				LoginLogoutMessage lm = (LoginLogoutMessage)inputObj;
				//System.out.println("Login Object Received "+ lm.senderId);
				ServCommDataManager scdm = ServCommDataManager.getInstance();
				boolean userIsOnline = scdm.userExist(lm.senderId);
				//If user is already there then remove it from data base manager list		
				
				
				
				if(!userIsOnline)
				{//We can skip this if the user is already online
					user = UserDBAccess.GetUser(lm.senderId);
					if(user != null)
						sd.DisplayOutput("New User Login attempt: "+user.getId()+" - "+user.getName());
				}
				else
				{	
					UserInformation dupUser = scdm.getUserInformation(lm.senderId);
					user = dupUser.user;
					isFaultedLogin = dupUser.status;
					System.out.println("Faulted Login Attempt for 3 ? "+ isFaultedLogin+"  "+user.getId());
					sd.DisplayOutput("Existing User login attempt, remove from UserInformation: "+user.getId()+" - "+user.getName());
					scdm.removeAUserInformation(lm.senderId);
					//ServChatCommManager.getInstance().CleanUpUserInfo(dupUser);
				}
				
			    
				if(user != null) 
				{
					System.out.println("User Found");
					LoginAckMessage lam = new LoginAckMessage(user, LoginAckMessage.SUCCEED, "Login Successful");
					ObjectOutputStream oos = new ObjectOutputStream(clientLoginRequestSocket.getOutputStream());
					oos.writeObject(lam);
					oos.flush();
					oos.close();
					ois.close();
					clientLoginRequestSocket.close();
					//System.out.println("Sent Success Ack");
					//System.out.println("Opening Other ports");
					OpenCommunicationPorts();
					//System.out.println("Open Other port successfull");

					if(isFaultedLogin == UserInformation.FAULTED)
						ServChatCommManager.getInstance().sendRestoreMessage(user);
					
					//Start the new thread for general purpose messaging for this specific user
					new ServGenCommManager(user);
					new ServOnlineListManager(user);
					
					ServChatCommManager.getInstance().DistributeUpdateOnlineUserList();	
					System.out.println("Inform other about login of: "+user.getId());
					ServChatCommManager.getInstance().informLogInOut(user, MessageType.LGI);
					sd.DisplayOutput("Login Successfull for: "+user.getId()+" - "+user.getName());
				}
				else
				{
					String str;
					/*if (userIsOnline)
						str  = "Login Denied - you are already login from another terminal";
					else*/
				    str = "INVALID Credentials - Please try again";
					//System.out.println("Login Failed");
					LoginAckMessage lam = new LoginAckMessage(null, LoginAckMessage.FAILED, str);
					ObjectOutputStream oos = new ObjectOutputStream(clientLoginRequestSocket.getOutputStream());
					oos.writeObject(lam);
					oos.flush();
					oos.close();
					clientLoginRequestSocket.close();
					//System.out.println("Sent Failed Ack");
					sd.DisplayOutput("Login Attempt failed: User not found in Data Base ");
				}
			}
			else
			{
				//System.out.println("Login Faulted");
				LoginAckMessage lam = new LoginAckMessage(null, LoginAckMessage.FAULTED, "Login is in faulted state");
				ObjectOutputStream oos = new ObjectOutputStream(clientLoginRequestSocket.getOutputStream());
				oos.writeObject(lam);
				oos.flush();
				//System.out.println("Sent Ack Faulted");
				sd.DisplayOutput("Login Attempt failed: Error Occurred in login ");
			}
		}
		catch(Exception e)
		{
			System.out.println("Server Crashed");
			e.printStackTrace();
		}
	}
	
}
