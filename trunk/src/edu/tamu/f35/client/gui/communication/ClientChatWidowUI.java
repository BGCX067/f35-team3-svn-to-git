package edu.tamu.f35.client.gui.communication;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import edu.tamu.f35.client.logic.communication.ClientCommDataManager;
import edu.tamu.f35.client.logic.communication.ClientCommMessageSender;
import edu.tamu.f35.client.logic.communication.ClientException;
import edu.tamu.f35.client.logic.communication.Tx;
import edu.tamu.f35.dataCotract.Mission;
import edu.tamu.f35.dataCotract.TerminalType;
import edu.tamu.f35.dataCotract.User;

public class ClientChatWidowUI extends JPanel implements ActionListener, KeyListener, Runnable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1752341341860200420L;
	boolean firsttime = true;
	private static ClientChatWidowUI instance = null;
	private static final int REFRESHLIST = 1;  //Refresh the list of online users, groudstations and Missions
	private static final int REFRESHTEXT = 2;  //Refresh the list of chat text display area
	private static final int REFRESHALL  = 3; //Refresh all
	private static final int REFRESHNOT  = 4;

	JCheckBox enableEKStrokeCB;
	ButtonGroup chatOptionGrpBG;
	JRadioButton pvtRB;
	JRadioButton brdRB;
	JButton pvtSendBtn;
	JButton voiceBtn;
	JButton clearSelectionBtn;
	
	
	
	JList genCommTextAreaList;
	JList clientNameAreaList;
	JList gsNameAreaList;
	JList missionNameAreaList;
	JLabel usrOnlnLab;
	JLabel gsOnlnLab;
	JLabel missionOnlnLab;
	JLabel msgLogLab;
	//JButton disconnectBtn;
	JTextField toTypeTf;
	JTextArea notificationTA;
	DefaultListModel textAreaModel;
	DefaultListModel clientNameListmodel;
	DefaultListModel gsNameListmodel;
	DefaultListModel missionNameListmodel;
	JButton sendBtn;
	JButton logOutBtn;
	JScrollPane genCommTAScrollPane;
	JScrollPane clientNameAreaScrollPane;
	JScrollPane gsNameAreaScrollPane;
	JScrollPane missionNameAreaScrollPane;
	JLabel label;
	Socket generalCommSocket,logOutCommSocket,clientNameSocket;
	DataInputStream genCommIpStream;
	DataOutputStream genCommOpStream;
	DataOutputStream logOutCommOpStream;
	DataOutputStream clientNameOpStream;
	DataInputStream clientNameIpStream;
	String name;
	int refreshOption;
	ArrayList<String> newModelText;
	String newNotText;
	ArrayList<String> onlineListGenUser;
	ArrayList<String> onlineListGS;
	ArrayList<String> activeMission;
	Font labelFont;
	Tx tx;
	static boolean flag = false;
	protected ClientChatWidowUI()
	{
		super();
		//pvtBtn.setLabel("P");
		try
		{
			tx = new Tx();
		}
		catch(Exception e) {System.out.println("Err!!! Voice Communication");}
		
		labelFont = new Font("Arial", Font.PLAIN,10);
		
		onlineListGenUser = new ArrayList<String>();
		onlineListGS = new ArrayList<String>();
		activeMission = new ArrayList<String>();
		
		notificationTA = new JTextArea(4, 40);
		notificationTA.setAlignmentX(LEFT_ALIGNMENT);
		notificationTA.setAutoscrolls(true);
		notificationTA.setEditable(false);
		notificationTA.setLineWrap(true);
		add(notificationTA);
		
		enableEKStrokeCB = new JCheckBox("Enable Enter Key", true);
		enableEKStrokeCB.setFont(labelFont);
		enableEKStrokeCB.setForeground(Color.white);
		enableEKStrokeCB.setBackground(Color.black);
		pvtRB = new JRadioButton("Private");
		brdRB = new JRadioButton("Broadcast");

		chatOptionGrpBG = new ButtonGroup();
		chatOptionGrpBG.add(pvtRB);
		//pvtRB.addActionListener(this);
		//brdRB.addActionListener(this);
		chatOptionGrpBG.add(brdRB);
		brdRB.setSelected(true);
		pvtSendBtn = new JButton("Private");
		pvtSendBtn.addActionListener(this);
		voiceBtn = new JButton("Voice Connect");
		//disconnectBtn = new JButton("Voice Disconnect");
		voiceBtn.addActionListener(this);
		voiceBtn.setForeground(Color.GREEN);
		//disconnectBtn.setForeground(Color.GREEN);
		//disconnectBtn.addActionListener(this);
		clearSelectionBtn = new JButton("C");
		clearSelectionBtn.addActionListener(this);
		clearSelectionBtn.setBackground(Color.red);
		clearSelectionBtn.setForeground(Color.white);
		
		toTypeTf=new JTextField();
		toTypeTf.addKeyListener(this);
		toTypeTf.setBackground(Color.black);
		toTypeTf.setForeground(Color.green);
		textAreaModel=new DefaultListModel();
		clientNameListmodel=new DefaultListModel();
		gsNameListmodel=new DefaultListModel();
		missionNameListmodel=new DefaultListModel();
		usrOnlnLab = new JLabel("OnlineUsers");
		usrOnlnLab.setForeground(Color.white);
		gsOnlnLab = new JLabel("GS Users");
		gsOnlnLab.setForeground(Color.white);
		missionOnlnLab = new JLabel("Mission");
		missionOnlnLab.setForeground(Color.white);
		msgLogLab = new JLabel("Messages");
		msgLogLab.setForeground(Color.white);
		label=new JLabel("Text:");
		label.setForeground(Color.white);
		genCommTextAreaList=new JList(textAreaModel);
		genCommTextAreaList.setBackground(Color.black);
		genCommTextAreaList.setForeground(Color.green);

		//genCommTextAreaList.setFont(chatTextFont);
		//---------
		
		//genCommTextAreaList.
		//--------
		
		clientNameAreaList=new JList(clientNameListmodel);
		clientNameAreaList.setBackground(Color.black);
		clientNameAreaList.setForeground(Color.green);
		gsNameAreaList=new JList(gsNameListmodel);
		gsNameAreaList.setBackground(Color.black);
		gsNameAreaList.setForeground(Color.green);
		missionNameAreaList=new JList(missionNameListmodel);
		missionNameAreaList.setBackground(Color.black);
		missionNameAreaList.setForeground(Color.green);
		sendBtn=new JButton("Broadcast");
		sendBtn.addActionListener(this);
		logOutBtn=new JButton("Logout");
		logOutBtn.addActionListener(this);
		//genCommTextAreaList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		//genCommTextAreaList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		genCommTAScrollPane=new JScrollPane(genCommTextAreaList);
		//genCommTAScrollPane.set
		genCommTAScrollPane.getViewport().setView(genCommTextAreaList);
		clientNameAreaScrollPane=new JScrollPane(clientNameAreaList);
		clientNameAreaList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		gsNameAreaScrollPane=new JScrollPane(gsNameAreaList);
		gsNameAreaList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		missionNameAreaScrollPane=new JScrollPane(missionNameAreaList);
		missionNameAreaList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		sendBtn.setForeground(Color.green);
		sendBtn.setBackground(Color.black);
		pvtSendBtn.setBounds(120,380,110,25);
		pvtSendBtn.setForeground(Color.green);
		pvtSendBtn.setBackground(Color.black);
		brdRB.setForeground(Color.white);
		brdRB.setBackground(Color.black);
		pvtRB.setForeground(Color.white);
		pvtRB.setBackground(Color.black);
		logOutBtn.setForeground(Color.red);
		logOutBtn.setBackground(Color.black);
		voiceBtn.setForeground(Color.green);
		voiceBtn.setBackground(Color.black);
		//sendBtn.addActionListener(this);
		//logOutBtn.addActionListener(this);
		add(msgLogLab);
		msgLogLab.setFont(labelFont);
		add(usrOnlnLab);
		usrOnlnLab.setFont(labelFont);
		add(gsOnlnLab);
		gsOnlnLab.setFont(labelFont);
		add(missionOnlnLab);
		missionOnlnLab.setFont(labelFont);
		add(toTypeTf);
		//toTypeTf.setBorder(null);
		add(sendBtn);
		add(genCommTAScrollPane);
		add(label);
		label.setFont(labelFont);
		add(logOutBtn);
		add(clientNameAreaScrollPane);
		add(gsNameAreaScrollPane);
		add(missionNameAreaScrollPane);
		add(notificationTA);
		add(pvtRB);
		add(brdRB);
		add(pvtSendBtn);
		add(enableEKStrokeCB);
		add(voiceBtn);
		add(clearSelectionBtn);
		
		setBackground(Color.black);
		
		msgLogLab.setBounds(5,5,100,15);
		genCommTAScrollPane.setBounds(5,20,230,300);
		usrOnlnLab.setBounds(240,5,80,15);
		
		clientNameAreaScrollPane.setBounds(240,20,95,150);
		gsOnlnLab.setBounds(240,174,80,15);
		gsNameAreaScrollPane.setBounds(240,188,95,150);
		missionOnlnLab.setBounds(240,339,150,15);
		missionNameAreaScrollPane.setBounds(240,353,95,150);
		label.setBounds(5,322,40,15);
		enableEKStrokeCB.setBounds(40,322,120,15);
		clearSelectionBtn.setBounds(180, 322, 50, 15);
		toTypeTf.setBounds(5,342,230,40);
		sendBtn.setBounds(5,387,110,25);
		pvtSendBtn.setBounds(120,387,110,25);
		brdRB.setBounds(8, 427, 100, 15);
		pvtRB.setBounds(125,427, 100, 15);
		
		logOutBtn.setBounds(5,457,110,25);
		voiceBtn.setBounds(120,457,110,25);
		notificationTA.setBounds(5,517,330,30);
		clientNameListmodel.addElement("abc");
		setLayout(null);
		System.out.println("*****" + getHeight());
		//setSize(450,850);
		
	}
	
	public static ClientChatWidowUI getInstance()
	{
		if(instance == null)
		{
			instance = new ClientChatWidowUI();
		}
		return instance;
	}
	
	private ArrayList<String> getIntendedUsers()
	{
		ArrayList<String> userList = new ArrayList<String>();
		
		Object[] pilotObList = clientNameAreaList.getSelectedValues();
		for(Object o : pilotObList)
		{
			String s = o.toString();
			s = s.substring(s.indexOf('(')+1, s.indexOf(')'));
			userList.add(s);
		}
		
		Object[] gsObList = gsNameAreaList.getSelectedValues();
		for(Object o : gsObList)
		{
			String s = o.toString();
			s = s.substring(s.indexOf('(')+1, s.indexOf(')'));
			userList.add(s);
		}
		
		return userList;
	}
	
	private ArrayList<String> getIntendedMissions()
	{
		ArrayList<String> missionList = new ArrayList<String>();
		Object[] missionObList = missionNameAreaList.getSelectedValues();
		for(Object o : missionObList)
		{
			String s = o.toString();
			s = s.substring(s.indexOf('(')+1, s.indexOf(')'));
			missionList.add(s);
		}
		return missionList;
	}

	private void SendMessage(boolean isPrivate)
	{
		System.out.println("SendMessage() private: "+ isPrivate);
		String str = toTypeTf.getText();
		toTypeTf.setText("");
		ClientCommMessageSender ccms = new ClientCommMessageSender();
		if(!isPrivate)
		{
			try
			{
				ccms.SendBroadCastMessage(str);
			}
			catch(ClientException e)
			{
				notificationTA.setForeground(Color.red);
				Toolkit.getDefaultToolkit().beep(); 
				notificationTA.setText("Message send failed, try again !!!");      
			}
		}
		else
		{
			ArrayList<String> intendedUsers = getIntendedUsers();
			ArrayList<String> intendedMissions = getIntendedMissions();
			if(intendedUsers.isEmpty() && intendedMissions.isEmpty())
			{
				Toolkit.getDefaultToolkit().beep(); 
				notificationTA.setText("No user or mission is selected to send !!!");
			}
			else
			{
				try 
				{
					ccms.SendPrivateMessage(str, intendedUsers, intendedMissions);
				}
				catch (ClientException e) 
				{
					notificationTA.setForeground(Color.red);
					Toolkit.getDefaultToolkit().beep(); 
					notificationTA.setText("Message send failed, try again !!!");
					//e.printStackTrace();
				}
				 
			}
		}
	}
	public void actionPerformed(ActionEvent ae) 
	{
		//notificationTA.setText("");
		if(ae.getSource() == sendBtn)
		{
			System.out.println("Broacast Clicked");
			SendMessage(false);	
		}
		if(ae.getSource() == pvtSendBtn)
		{
			System.out.println("private clicked");
			
			SendMessage(true);	
		}
		if(ae.getSource() == logOutBtn)
		{
			try
			{
				System.out.println("actionPerformed():Logout Clicked Entered");
				ClientCommMessageSender ccms = new ClientCommMessageSender();
				ccms.SendLogOutMessage();
				System.out.println("actionPerformed():Logout Clicked Returned"); 
				ClientCommLoginUI.getInstance().loginNotificationTA.setForeground(Color.black);
				ClientCommLoginUI.getInstance().loginNotificationTA.setText("You are logged out");
				voiceBtn.setText("Voice Connect");
				voiceBtn.setForeground(Color.GREEN);
				try{tx.stopThreads();} catch(Exception e){System.out.println("Err!!! tx can not be stopped");}
			}
			catch(ClientException e)
			{
				notificationTA.setForeground(Color.red);
				Toolkit.getDefaultToolkit().beep(); 
				notificationTA.setText("Logout failed, try again !!!");      
			}
		}
		if(ae.getSource() == clearSelectionBtn)
		{
			clientNameAreaList.clearSelection();
			gsNameAreaList.clearSelection();
			missionNameAreaList.clearSelection();
		}
		if (ae.getSource()==voiceBtn)
		{
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
	}

	@Override
	public void keyPressed(KeyEvent ae) 
	{
		 int key = ae.getKeyCode();
	     if (key == KeyEvent.VK_ENTER) 
	     {
	    	 if(enableEKStrokeCB.isSelected())
	    	 {
	    		 if(brdRB.isSelected())
	    			 SendMessage(false);
	    		 else
	    			 SendMessage(true);
	    	 }
	     }	     
	}
	

	public void keyReleased(KeyEvent arg0) 
	{} //not implemented

	public void keyTyped(KeyEvent arg0) 
	{} //not implemented

	public void refreshText(ArrayList<String> msg)
	{
		newModelText = msg;
		this.refreshOption = REFRESHTEXT;
		SwingUtilities.invokeLater(this);
	}
	
	public void refreshNotification(String msg)
	{
		newNotText = msg;
		this.refreshOption = REFRESHNOT;
		SwingUtilities.invokeLater(this);
	}
	
	public void refreshList() 
	{
		this.refreshOption = REFRESHLIST;
		ClientCommDataManager ccdm = ClientCommDataManager.getInstance();
		ArrayList<User> onlineUsers = ccdm.getOnlineUserList();
		ArrayList<Mission> activeMissionObj = ccdm.getActiveMissionList();
		onlineListGS =  new ArrayList<String>();
		onlineListGenUser = new ArrayList<String>();
		activeMission = new ArrayList<String>();
		
		for(User u : onlineUsers)
		{
			String elem = u.getName()+"("+u.getId()+")";
			//System.out.println("Adding :"+ elem + u.getType());
			if(u.getType() == TerminalType.PILOT)
			{
				System.out.println("Addin elem***:" + elem );
				onlineListGenUser.add(elem);
			}
			if(u.getType() == TerminalType.GROUNDSTATION)
			{
				onlineListGS.add(elem);
			}
		}
		
		for(Mission m : activeMissionObj)
		{
			String elem = m.getName()+"("+m.getId()+")";
			activeMission.add(elem);
		}
		
		SwingUtilities.invokeLater(this);
	}
	private void refreshList1(ArrayList<String> genComm, ArrayList<String> gsComm, ArrayList<String> misList)
	{
		if(clientNameListmodel != null)
			clientNameListmodel.clear();
		for(String s : genComm)
			clientNameListmodel.addElement(s);
		
		if(gsNameListmodel != null)
			gsNameListmodel.clear();
		for(String s : gsComm)
			gsNameListmodel.addElement(s);
		
		if(missionNameListmodel != null)
			missionNameListmodel.clear();
		for(String s : misList)
			missionNameListmodel.addElement(s);
	}
	
	private void textAreaRefresh(ArrayList<String> str)
	{
		for(String s : str)
		{
			textAreaModel.addElement(s);
		}
	}
	public void run() 
	{
		//notificationTA.setText("");
		switch(refreshOption)
		{
			case REFRESHLIST:
				refreshList1(onlineListGenUser, onlineListGS, activeMission);
				break;
			case REFRESHTEXT:
				textAreaRefresh(newModelText);
				break;
			case REFRESHNOT:
				//Toolkit.getDefaultToolkit().beep();
				notificationTA.setText(newNotText);
				break;
			case REFRESHALL:
				textAreaRefresh(newModelText);
				refreshList1(onlineListGenUser, onlineListGS, activeMission);
				break;
				
		}
		
	}

	
	
	/*protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		int x = getHeight();
		int y = getWidth();
		int x3 = x/3;
		int y3 = y/3;
		genCommTAScrollPane.setBounds(5,40,2*x3,2*y3 - 10);
		clientNameAreaScrollPane.setBounds(2*x3+5,40,100,200);
		
		
	}*/
}
