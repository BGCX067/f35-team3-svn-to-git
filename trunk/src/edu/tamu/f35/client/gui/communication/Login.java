package edu.tamu.f35.client.gui.communication;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

// Login class which takes a user name and passed it to client class
public class Login extends JPanel implements ActionListener{
	//JFrame frame1;
	JTextField hostIpTf;
	JTextField portTf;
	JTextField userIdTf;
	JButton loginBtn;
	JLabel heading;
	JLabel hostIPLab;
	JLabel portLab;
	JLabel userIdLab;
	
	
	/*public static void main(String[] args){
		System.out.println("In main1");
		new Login();
		System.out.println("In main after call");
	}*/
	public Login(){
		System.out.println("In Login start");
		//frame1 = new JFrame("Login Page");
		//frame1.setBackground(Color.orange);
		userIdTf=new JTextField();
		loginBtn=new JButton("Login");
		loginBtn.setForeground(Color.green);
		heading=new JLabel("F35 Messenger");
		heading.setFont(new Font("Arial", Font.BOLD,40));	
		
		
		portLab=new JLabel("Port:");
		hostIPLab = new JLabel("Host IP:");
		userIdLab=new JLabel("Userid:");
		
		portTf = new JTextField(10);
		hostIpTf = new JTextField(15);
		userIdTf = new JTextField(20);
		
		portTf.setEditable(false);
		hostIpTf.setEditable(true);
		
		portLab.setFont(new Font("Serif", Font.PLAIN, 20));
		hostIPLab.setFont(new Font("Serif", Font.PLAIN, 20));
		userIdLab.setFont(new Font("Serif", Font.PLAIN, 20));
		
		//-portField = new JTextField(10); 
		//-portField.setEditable(true);
		//ipField.setText(hostIP);
		//-label1.setFont(new Font("Serif", Font.PLAIN, 10));
		//-label2.setFont(new Font("Serif", Font.PLAIN, 10));
		
		add(heading);
		heading.setBounds(40,20,300,60);
		heading.setForeground(Color.LIGHT_GRAY);
		add(portLab);
		//Port
		portLab.setBounds(40,100,150, 40);
		portLab.setForeground(Color.LIGHT_GRAY);
		add(portTf);
		portTf.setBounds(130,112,150,20);
		portTf.setText("2001");
		//Host
		add(hostIPLab);
		hostIPLab.setBounds(40,130,150, 40);
		hostIPLab.setForeground(Color.LIGHT_GRAY);
		add(hostIpTf);
		hostIpTf.setBounds(130,142,150,20);
		hostIpTf.setText("localhost");
		//User
		add(userIdLab);
		userIdLab.setBounds(40,160,150, 40);
		userIdLab.setForeground(Color.LIGHT_GRAY);
		add(userIdTf);
		userIdTf.setBounds(130,172,150,20);
		
		add(loginBtn);
		loginBtn.addActionListener(this);
		loginBtn.setBounds(130,250,90,30);
		
		//panel.setVisible(true);
		//panel.setSize(300, 300);
	
		setLayout(null);
		
		//setSize(400, 400);
	    setVisible(true);
	    setForeground(Color.LIGHT_GRAY);
	    setBackground(Color.DARK_GRAY);
	    //B b = new Border();
	    setBorder(getBorder());
		//frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		System.out.println("In login end");
	}
	// pass the user name to MyClient class
	public void actionPerformed(ActionEvent e){
		System.out.println("In action performed");
		String userId="";
		try{
			//frame1.dispose();
			userId= userIdTf.getText();
			//TempTopLevelChatPanel.getInstance().chatUiInstance =
			    // new MyClient(userId, hostIpTf.getText(), portTf.getText());
			MyClient mc = new MyClient(userId, hostIpTf.getText(), portTf.getText());
			TempTopLevelChatPanel.getInstance().add(mc, TempTopLevelChatPanel.CHATPANEL);
			TempTopLevelChatPanel.getInstance().setLayoutPane(TempTopLevelChatPanel.CHATPANEL);
			//test t = new test();
			//TempTopLevelChatPanel.getInstance().add(t, TempTopLevelChatPanel.CHATPANEL);
			//TempTopLevelChatPanel.getInstance().setLayoutPane(TempTopLevelChatPanel.CHATPANEL);
		}catch (Exception te){
			te.printStackTrace();
			System.out.println("Exception");
		}
		System.out.println("In action performed end");
	}
}