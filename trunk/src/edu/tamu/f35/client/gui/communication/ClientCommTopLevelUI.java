package edu.tamu.f35.client.gui.communication;

import java.awt.CardLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

public class ClientCommTopLevelUI extends JPanel implements Runnable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7525589154844756935L;
	
	
	public static final String LOGINPANEL = "LoginPanel";
	public static final String CHATPANEL = "ChatPanel";
	ClientChatWidowUI chatUiInstance;
	ClientCommLoginUI loginUiInstance;
	CardLayout layout;
	private static ClientCommTopLevelUI instance = null;
	static String currentPane;
	Border border;
	
	protected ClientCommTopLevelUI()
	{
		super();
		//border = BorderFactory.createEmptyBorder(1,1,1,1);
		border = BorderFactory.createLineBorder(new Color(192,243,255));
		loginUiInstance = ClientCommLoginUI.getInstance();
		chatUiInstance = ClientChatWidowUI.getInstance();
		layout = new CardLayout();
		setLayout(layout);
		add(loginUiInstance, LOGINPANEL);
		add(chatUiInstance, CHATPANEL);
		layout.show(this, LOGINPANEL);
		setBorder(border);
		//setSize(450,850);
		setVisible(true);
	}
	
	public static ClientCommTopLevelUI getInstance()
	{
		if(instance == null)
			instance = new ClientCommTopLevelUI();
		return instance;
	}
	
	public void setLayoutPane(String name)
	{
		currentPane = name;
		SwingUtilities.invokeLater(this);
	}
	public void run() 
	{
		layout.show(this, currentPane);
	}
}
