package edu.tamu.f35.client.gui.communication;


import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import edu.tamu.f35.client.logic.communication.ClientConfiguration;
import edu.tamu.f35.client.logic.communication.ClientException;
import edu.tamu.f35.client.logic.communication.ClientLoginManager;


public class ClientCommLoginUI extends JPanel implements ActionListener, Runnable, KeyListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5979851244355931609L;

	private static String hostIP;
	
	JLabel heading;
	JLabel userIdLab;
	JTextField userIdTf;
	JLabel hostIpLab;
	JTextField hostIpTf;
	JButton connectBtn;
	JLabel notLab;
	JTextArea loginNotificationTA;
	String message;
	
	
	private static ClientCommLoginUI instance = null;
	
	protected ClientCommLoginUI()
	{
		super();
		JPanel pane;
		setLayout(new GridLayout(6, 1));
		
		heading=new JLabel("F35 MESSENGER", (int) CENTER_ALIGNMENT);
		heading.setFont(new Font("Arial", Font.BOLD,30));
		heading.setBackground(Color.black);
		heading.setForeground(Color.green);
		add(heading);
		
		pane = new JPanel(new FlowLayout(FlowLayout.CENTER));
		userIdLab = new JLabel("User Id: ");
		userIdLab.setForeground(Color.white);
		userIdLab.setBackground(Color.black);
		
		userIdTf = new JTextField(15);
		userIdTf.setEditable(true);
		userIdTf.setForeground(Color.green);
		userIdTf.setBackground(Color.black);
		userIdTf.addKeyListener(this);
		pane.add(userIdLab);
		pane.add(userIdTf);
		pane.setBackground(Color.black);
		add(pane);
		
		pane = new JPanel(new FlowLayout(FlowLayout.CENTER));
		hostIpLab = new JLabel("Host IP: ");
		hostIpLab.setForeground(Color.white);
		hostIpLab.setBackground(Color.black);
		
		hostIP = (String)ClientConfiguration.getProperty("hostip");
		hostIpTf = new JTextField(hostIP, 15);
		hostIpTf.addKeyListener(this);
		hostIpTf.setEditable(true);
		hostIpTf.setForeground(Color.green);
		hostIpTf.setBackground(Color.black);
		pane.add(hostIpLab);
		pane.add(hostIpTf);
		pane.setBackground(Color.black);
		add(pane);
		
		
		pane = new JPanel(new FlowLayout(FlowLayout.CENTER));
		connectBtn = new JButton("Connect");
		connectBtn.addActionListener(this);
		connectBtn.setSize(15, 10);
		connectBtn.setBackground(Color.black);
		connectBtn.setForeground(Color.green);
		//connectBtn.setBounds(0, 0, 20, 10);
		pane.add(connectBtn);
		pane.setBackground(Color.black);
		add(pane);
		
		pane = new JPanel(new FlowLayout(FlowLayout.LEFT));
		notLab = new JLabel("Notification: ");
		notLab.setFont(new Font("Arial", Font.BOLD,15));
		notLab.setForeground(Color.white);
		notLab.setBackground(Color.black);
		notLab.setBounds(2, 40, 100, 40);
		pane.add(notLab);
		pane.setBackground(Color.black);
		add(pane);
		
		loginNotificationTA = new JTextArea(4, 40);
		loginNotificationTA.setAlignmentX(LEFT_ALIGNMENT);
		loginNotificationTA.setAutoscrolls(true);
		loginNotificationTA.setEditable(false);
		loginNotificationTA.setLineWrap(true);
		loginNotificationTA.setForeground(Color.red);
		loginNotificationTA.setBackground(Color.black);
		add(loginNotificationTA);
		//setSize(450,850);
		setBackground(Color.black);
		
	}
	
	public static ClientCommLoginUI getInstance()
	{
		if(instance == null)
		{
			instance = new ClientCommLoginUI();
		}
		return instance;
	}

	private void performAction()
	{
		try
		{
			//ClientLoginManager logManager = 
		    new ClientLoginManager(userIdTf.getText(), hostIpTf.getText());
			loginNotificationTA.setText("Connecting Please Wait....");
			//ClientCommManager.getInstance().setStatus(ClientCommManager.CONNECTING);				
		}
		catch(ClientException ce)
		{
			loginNotificationTA.setForeground(Color.RED);
			loginNotificationTA.setText(ce.getMessage());
		}
		catch(Exception e1)
		{
			loginNotificationTA.setForeground(Color.RED);
			loginNotificationTA.setText("Unhandled Exception" + e1.getMessage());
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		performAction();
	}
	
	public void refreshNotification(String msg)
	{
		message = msg;
		SwingUtilities.invokeLater(this);
	}
	
	public void run()
	{
		loginNotificationTA.setText(message);
	}

	@Override
	public void keyPressed(KeyEvent ae) {
		 int key = ae.getKeyCode();
	     if (key == KeyEvent.VK_ENTER) 
	     {
	    	 performAction();
	     }
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
