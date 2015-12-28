package edu.tamu.f35.client.gui.communication;

/****************************************************************
*	Version		:	1.0
*	Date		:	02/03/2007
*	
*	Description
*	This is a client side of chat application.
*	This application is used to sending and receiving the messages
*	and in this we can maintain the list of all online users
*	
*	Remarks
*	Before running the client application make sure the server is 
*	running.If server is running then only you can execute your
*	application.
******************************************************************/
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import edu.tamu.f35.client.logic.communication.Tx;

//create the GUI of the client side
public class MyClient extends /*WindowAdapter*/ JPanel implements ActionListener
{
	
	//JFrame clientFrame;
	JList genCommTextAreaList;
	JList clientNameAreaList;
	JLabel usrOnlnLab;
	JLabel msgLogLab;
	JTextField toTypeTf;
	DefaultListModel textAreaModel;
	DefaultListModel clientNameListmodel;
	JButton sendBtn;
	JButton logOutBtn;
	JButton voiceBtn;
	JButton disconnectBtn;
	JScrollPane genCommTAScrollPane;
	JScrollPane clientNameAreaScrollPane;
	JLabel label;
	Socket generalCommSocket,logOutCommSocket,clientNameSocket;
	DataInputStream genCommIpStream;
	DataOutputStream genCommOpStream;
	DataOutputStream logOutCommOpStream;
	DataOutputStream clientNameOpStream;
	DataInputStream clientNameIpStream;
	String name;
	Tx tx = new Tx();
	static boolean flag = false;
	MyClient(String name, String hostIp, String port)throws IOException{
		//clientFrame = new JFrame("Client Side");
		toTypeTf=new JTextField();
		textAreaModel=new DefaultListModel();
		clientNameListmodel=new DefaultListModel();
		usrOnlnLab = new JLabel("Online Users");
		usrOnlnLab.setForeground(Color.LIGHT_GRAY);
		msgLogLab = new JLabel("Message Log");
		msgLogLab.setForeground(Color.LIGHT_GRAY);
		label=new JLabel("Message");
		label.setForeground(Color.LIGHT_GRAY);
		genCommTextAreaList=new JList(textAreaModel);
		
		clientNameAreaList=new JList(clientNameListmodel);
		sendBtn=new JButton("Send");
		logOutBtn=new JButton("Logout");
		voiceBtn = new JButton("Voice Connect");
		disconnectBtn = new JButton(" Voice Disconnect");
		genCommTAScrollPane=new JScrollPane(genCommTextAreaList);
		clientNameAreaScrollPane=new JScrollPane(clientNameAreaList);
		JPanel panel = new JPanel();
		sendBtn.addActionListener(this);
		sendBtn.setForeground(Color.GREEN);
		logOutBtn.addActionListener(this);
		logOutBtn.setForeground(Color.RED);
		voiceBtn.addActionListener(this);
		voiceBtn.setForeground(Color.GREEN);
		disconnectBtn.setForeground(Color.GREEN);
		disconnectBtn.addActionListener(this);
		add(msgLogLab); add(usrOnlnLab);
		add(toTypeTf);add(sendBtn);add(genCommTAScrollPane);
		add(label);add(logOutBtn);add(voiceBtn);//add(disconnectBtn);
		add(clientNameAreaScrollPane);
		/*msgLogLab.setBounds(10,20,150, 15);
		genCommTAScrollPane.setBounds(10,40,230,200);
		usrOnlnLab.setBounds(260,20,150, 15);
		clientNameAreaScrollPane.setBounds(260,40,100,200);
		label.setBounds(20,250,80,30);
		toTypeTf.setBounds(100,250,140,30);
		sendBtn.setBounds(260,250,90,30);
		logOutBtn.setBounds(260,300,90,30);*/
		msgLogLab.setBounds(5,5,120,15);
		genCommTAScrollPane.setBounds(5,20,230,250);
		usrOnlnLab.setBounds(240,5,120,15);
		clientNameAreaScrollPane.setBounds(240,20,95,150);
		//gsOnlnLab.setBounds(290,144,150,15);
		//gsNameAreaScrollPane.setBounds(290,158,145,120);
		//missionOnlnLab.setBounds(290,279,150,15);
		//missionNameAreaScrollPane.setBounds(290,293,145,80);
		label.setBounds(5,272,60,15);
		toTypeTf.setBounds(5,292,230,45);
		sendBtn.setBounds(20,343,150,50);
		logOutBtn.setBounds(20,390,150,50);
		voiceBtn.setBounds(200,343,150,50);
		disconnectBtn.setBounds(200,390,150,50);
		//clientFrame.add(panel);
		setLayout(null);
		//clientFrame.setSize(400, 400);
		//clientFrame.setVisible(true);
		setVisible(true);
		setBorder(getBorder());
		setBackground(Color.DARK_GRAY);
		
		//clientFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.name=name;
		//clientFrame.addWindowListener(this);
		generalCommSocket=new Socket(hostIp,2001);	//creates a socket object
		logOutCommSocket=new Socket(hostIp,2001);
		clientNameSocket=new Socket(hostIp,2001);
	    //create inputstream for a particular socket
		genCommIpStream=new DataInputStream(generalCommSocket.getInputStream());
		//create outputstream
		genCommOpStream=new DataOutputStream(generalCommSocket.getOutputStream());
		
		//sending a message for login
		genCommOpStream.writeUTF(name+" has Logged in");	
		
		logOutCommOpStream=new DataOutputStream(logOutCommSocket.getOutputStream());
		
		clientNameOpStream=new DataOutputStream(clientNameSocket.getOutputStream());
		clientNameIpStream=new DataInputStream(clientNameSocket.getInputStream());

// creating a thread for maintaning the list of user name
		ClientNameMaintananceThread m1=new ClientNameMaintananceThread(clientNameOpStream,clientNameListmodel,name,clientNameIpStream);
		Thread t1=new Thread(m1);
		t1.start();			
	//creating a thread for receiving a messages
		GenCommManagerThread m=new GenCommManagerThread(genCommIpStream,textAreaModel);
		Thread t=new Thread(m);
		t.start();
  	}
	public void actionPerformed(ActionEvent e){
		// sending the messages
		if(e.getSource()== sendBtn){	
			String str="";
			str=toTypeTf.getText();
			toTypeTf.setText("");
			str=name+": > "+str;
			try{
				genCommOpStream.writeUTF(str);
				System.out.println(str);
				genCommOpStream.flush();
			}catch(IOException ae){System.out.println(ae);}
		}
		// client logout
		if (e.getSource()==logOutBtn){
			//clientFrame.dispose();
			try{
					//sending the message for logout
					genCommOpStream.writeUTF(name+" has Logged out");
					logOutCommOpStream.writeUTF(name);
					logOutCommOpStream.flush();
					Thread.currentThread().sleep(1000);
					TempTopLevelChatPanel.getInstance().setLayoutPane(TempTopLevelChatPanel.LOGINPANEL);
					tx.stopThreads();
					//System.exit(1);
				}
			catch(Exception oe){}
		}
		
		if (e.getSource()==voiceBtn){
			//clientFrame.dispose();
			try
			{
				//Tx tx = new Tx();
				if(!flag)
				{
					tx.captureAudio();
					voiceBtn.setText("Voice Disconnect");
					voiceBtn.setForeground(Color.RED);
				}
				else
				{
					tx.stopThreads();
					voiceBtn.setText("Voice Connect");
					voiceBtn.setForeground(Color.GREEN);
				}
				flag = !flag;
			}
			catch(Exception oe){}
		}
		if (e.getSource()==disconnectBtn){
			try
			{
				tx.stopThreads();
			}
			catch(Exception oe){}
		}
	}
	/*public void windowClosing(WindowEvent w)
	{
		try
		{
			logOutCommOpStream.writeUTF(name);
			logOutCommOpStream.flush();	
			Thread.currentThread().sleep(1000);
			System.exit(1);
		}
		catch(Exception oe){}
	}*/
}

// class is used to maintaning the list of user name
class ClientNameMaintananceThread implements Runnable{
	DataOutputStream clientNameOpStream;
	DefaultListModel clientNameListmodel;	
	DataInputStream clientNameIpStream;
	String name,lname;
	ArrayList alname=new ArrayList(); //stores the list of user names
	ObjectInputStream obj; // read the list of user names
	int i=0;
	ClientNameMaintananceThread(DataOutputStream clientNameOpStream,DefaultListModel clientNameListmodel,String name,DataInputStream clientNameIpStream){
		this.clientNameOpStream=clientNameOpStream;
		this.clientNameListmodel=clientNameListmodel;
		this.name=name;
		this.clientNameIpStream=clientNameIpStream;
	}
	public void run(){
		try{
			clientNameOpStream.writeUTF(name);  // write the user name in output stream
			while(true){
			obj=new ObjectInputStream(clientNameIpStream);
			//read the list of user names
			alname=(ArrayList)obj.readObject(); 
			if(i>0)
				clientNameListmodel.clear(); 
			Iterator i1=alname.iterator();
			System.out.println(alname);
			while(i1.hasNext()){
				lname=(String)i1.next();
				i++;
				 //add the user names in list box
				clientNameListmodel.addElement(lname);
				}
			}
		}catch(Exception oe){}
	}
}
//class is used to received the messages
class GenCommManagerThread implements Runnable{
	DataInputStream genCommIpStream;
	DefaultListModel textAreaModel;
	GenCommManagerThread(DataInputStream genCommIpStream, DefaultListModel textAreaModel){
		this.genCommIpStream=genCommIpStream;
		this.textAreaModel=textAreaModel;
	}
	public void run(){
		String str1="";
		while(true){
			try{
				str1=genCommIpStream.readUTF(); // receive the message
				// add the message in list box
				textAreaModel.addElement(str1);
			}catch(Exception e){}
		}
	}
}