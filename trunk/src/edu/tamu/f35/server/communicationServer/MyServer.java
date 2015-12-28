package edu.tamu.f35.server.communicationServer;

/****************************************************************
 *	Version		:	1.0
 *	Date		:	02/03/2007
 *	
 *	Description
 *	This is a Server Side application of Chat System.
 *	This application is used for receiving the messages from any client
 *	and send to each and every client and in this we can maintain the
 *	list of all online users.
 *
 *	Remarks
 *	This application is unable to provide the private chatting facility
 ******************************************************************/

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class DBWriter implements Runnable
{
	String msg;
	Thread t;
	DBWriter(String ms)
	{
		msg = ms;
		t = new Thread(this);
		t.start();
	}
	public void run() 
	{		
		//MessageLogDBAccess.getInstance().createBroadCastMessage(msg);
	}
	
}

public class MyServer {
	private ServerSocket serverSocket;
	private Socket genCommSocket;
	private List<Socket> genCommSocketList = new ArrayList<Socket>(); //List og general communication Socket
	private List<Socket> logOutCommSocketList = new ArrayList<Socket>(); 
	private List<Socket> clientNameSocketList = new ArrayList<Socket>(); //list of sockets for all the clients
	private List<String> alname = new ArrayList<String>();
	private Socket logOutCommSocket, clientNameSocket;

	public MyServer() throws IOException {
		ServerDisplay sd = ServerDisplay.getInstance();
		serverSocket = new ServerSocket(2001); // create server socket
		sd.DisplayOutput("Server Started at port 2001");
		while (true) {
			
			//1.Socket Creation Step
			genCommSocket = serverSocket.accept(); // accept the client socket
			sd.DisplayOutput("Client Request Accepted coming from client @:"+genCommSocket.getInetAddress().toString());
			logOutCommSocket = serverSocket.accept();
			clientNameSocket = serverSocket.accept();  //Client name at its input stream
			
			
			genCommSocketList.add(genCommSocket); // add the client socket in arraylist
			logOutCommSocketList.add(logOutCommSocket);
			clientNameSocketList.add(clientNameSocket);
			System.out.println("Client is Connected");
			OnLineClientListMaintananceThread m = new OnLineClientListMaintananceThread(clientNameSocket, clientNameSocketList, alname); // new thread for
															// maintaning the
															// list of user name
			Thread t2 = new Thread(m);
			t2.start();

			GeneralCommunicationThread r = new GeneralCommunicationThread(genCommSocket, genCommSocketList);// new thread for receive and
												// sending the messages
			Thread t = new Thread(r);
			t.start();

			LogOutUserMaintainaceThread my = new LogOutUserMaintainaceThread(logOutCommSocket, logOutCommSocketList, genCommSocket, clientNameSocket); // new thread for
															// update the list
															// of user name
			Thread t1 = new Thread(my);
			t1.start();
		}
	}

	public static void main(String[] args) {
		try {
			new MyServer();
		} catch (IOException e) {
		}
	}
}

// class is used to update the list of user name
class LogOutUserMaintainaceThread implements Runnable {
	Socket logOutCommSocket, genCommSocket, s2;
	static List logOutCommSocketList;
	DataInputStream ddin;
	String logOutUserName;
	ServerDisplay sd;

	LogOutUserMaintainaceThread(Socket logOutCommSocket, List logOutCommSocketList, Socket genCommSocket, Socket s2) {
		//new thread for
		// update the list
		// of user name
		sd = ServerDisplay.getInstance();
		//sd.DisplayOutput("Thread created updating the list of user names at client "+s1.getInetAddress().toString());
		this.logOutCommSocket = logOutCommSocket;
		this.logOutCommSocketList = logOutCommSocketList;
		this.genCommSocket = genCommSocket;
		this.s2 = s2;
	}

	public void run() {
		try {
			ddin = new DataInputStream(logOutCommSocket.getInputStream());
			while (true) {
				logOutUserName = ddin.readUTF(); //Read logout user name
				System.out.println("Exit  :" + logOutUserName);
				OnLineClientListMaintananceThread.alname.remove(logOutUserName);// remove the logout user name
												// from arraylist
				OnLineClientListMaintananceThread.every();
				logOutCommSocketList.remove(logOutCommSocket);
				GeneralCommunicationThread.genCommSocketList.remove(genCommSocket);
				OnLineClientListMaintananceThread.clientNameSocketList.remove(s2);
				sd.DisplayOutput("Disconnected from client "+logOutUserName + " @" + logOutCommSocket.getInetAddress().toString());
				//if (al1.isEmpty())
					//System.exit(0); // all client has been logout
			}
		} catch (Exception ie) {
		}
	}
}

// class is used to maintain the list of all online users
class OnLineClientListMaintananceThread implements Runnable {
	Socket clientNameSocket;
	static List clientNameSocketList;
	static List alname;
	static DataInputStream din1;
	static DataOutputStream dout1;
	ServerDisplay sd;

	OnLineClientListMaintananceThread(Socket clientNameSocket, List clientNameSocketList, List alname) {
		sd = ServerDisplay.getInstance();
		//
		this.clientNameSocket = clientNameSocket;
		this.clientNameSocketList = clientNameSocketList;
		this.alname = alname;
	}

	public void run() {
		try {
			din1 = new DataInputStream(clientNameSocket.getInputStream());
			String n = din1.readUTF();
			sd.DisplayOutput("Connected to client "+n+" @"+clientNameSocket.getInetAddress().toString());
			alname.add(n); // store the user name in arraylist
			every();
		} catch (Exception oe) {
			System.out.println("Main expression" + oe);
		}
	}

	// send the list of user name to all client
	static void every() throws Exception {
		Iterator i1 = clientNameSocketList.iterator();
		Socket st1;

		while (i1.hasNext()) {
			st1 = (Socket) i1.next();
			dout1 = new DataOutputStream(st1.getOutputStream());
			ObjectOutputStream obj = new ObjectOutputStream(dout1);
			obj.writeObject(alname); // write the list of users in stream of all
										// clients
			dout1.flush();
			obj.flush();
		}
	}
}

// class is used to receive the message and send it to all clients
class GeneralCommunicationThread implements Runnable {
	Socket genCommSocket;
	static List genCommSocketList;
	DataInputStream genCommSocketInputStream;
	DataOutputStream genCommSocketOutputStream;
	ServerDisplay sd;

	GeneralCommunicationThread(Socket genCommSocket, List genCommSocketList) {
		//sd = ServerDisplay.getInstance();
		//sd.DisplayOutput("Thread Created for receiving and sending msg at client "+s.getInetAddress().toString());
		this.genCommSocket = genCommSocket;
		this.genCommSocketList = genCommSocketList;
	}

	public void run() {
		String str;
		int i = 1;
		try {
			genCommSocketInputStream = new DataInputStream(genCommSocket.getInputStream());
		} catch (Exception e) {
		}

		while (i == 1) {
			try {
				str = genCommSocketInputStream.readUTF(); // read the message
				new DBWriter(str);
				distribute(str);
			} catch (IOException e) {
			}
		}
	}

	// send it to all clients
	public void distribute(String str) throws IOException {
		Iterator i = genCommSocketList.iterator();
		Socket st;
		while (i.hasNext()) {
			st = (Socket) i.next();
			genCommSocketOutputStream = new DataOutputStream(st.getOutputStream());
			genCommSocketOutputStream.writeUTF(str);
			genCommSocketOutputStream.flush();
		}
	}
}