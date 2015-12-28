package edu.tamu.f35.client.gui;
import javax.swing.JFrame;

import edu.tamu.f35.client.gui.communication.ClientCommTopLevelUI;
import edu.tamu.f35.client.logic.communication.ClientCommManager;


public class MockCommCockPit extends JFrame
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1600524052050109037L;
	
	//private TempTopLevelChatPanel chatPaneInstance;
	static ClientCommManager commManagerInstance;
	//static JFrame frame;
	public MockCommCockPit() {
		super("This is a mock frame");
		//chatPaneInstance = TempTopLevelChatPanel.getInstance();
		//chatPaneInstance = ;
		//chatPaneInstance
		//setContentPane(chatPaneInstance);
		//frame = new JFrame("Thisis a new Frame");
		//test t = new test();
		//frame.
		setContentPane(ClientCommTopLevelUI.getInstance());
		commManagerInstance = ClientCommManager.getInstance();
		//frame.
		//setVisible(true);
		//frame.
		setSize(500,650);
		//frame.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MockCommCockPit mainC = new MockCommCockPit();
		//mainC.pack();
		//frame.
		mainC.setVisible(true);
		try 
		{
			System.out.println("Waiting for threads to finish.");
			commManagerInstance.self.join();
		} 
		catch (InterruptedException e) 
		{
			System.out.println("Main thread Interrupted");
		}

	}

}
