
package edu.tamu.f35.client.gui.communication;

import java.awt.CardLayout;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class TempTopLevelChatPanel extends JPanel implements Runnable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7525589154844756935L;
	
	
	public static final String LOGINPANEL = "LoginPanel";
	public static final String CHATPANEL = "ChatPanel";
	//static MyClient chatUiInstance;
	static Login loginUiInstance;
	static CardLayout layout;
	private static TempTopLevelChatPanel instance = null;
	static String currentPane;
	protected TempTopLevelChatPanel()
	{
		loginUiInstance = new Login();
		//chatUiInstance = new MyClient();
		layout = new CardLayout();
		setLayout(layout);
		add(loginUiInstance, LOGINPANEL);
		//add(chatUiInstance, CHATPANEL);
		layout.show(this, LOGINPANEL);
		//setSize(450,850);
		setVisible(true);
	}
	
	public static TempTopLevelChatPanel getInstance()
	{
		if(instance == null)
			instance = new TempTopLevelChatPanel();
		return instance;
	}
	
	public void setLayoutPane(String name)
	{
		currentPane = name;
		SwingUtilities.invokeLater(this);
	}

	@Override
	public void run() 
	{
		// TODO Auto-generated method stub
		layout.show(this, currentPane);
	}
}
