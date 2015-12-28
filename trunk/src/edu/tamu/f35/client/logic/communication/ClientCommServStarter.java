//This class is the starting point of communication manager
//Responsibility of this class it to initialize UIs
//Start the communication server chat module
//Start the RMI services exposed by Communication Server
package edu.tamu.f35.client.logic.communication;

import javax.swing.JFrame;

import edu.tamu.f35.client.gui.communication.ClientCommTopLevelUI;

public class ClientCommServStarter 
{
	ClientCommTopLevelUI chatTopLevelUiInstance;
	static ClientCommManager commManagerInstance;
	public ClientCommServStarter()
	{
		//Start UI, inherently swing component will start on a separate thread
		JFrame communicationFrame = new JFrame();

		communicationFrame.setVisible(true);
		chatTopLevelUiInstance = ClientCommTopLevelUI.getInstance();
		communicationFrame.setContentPane(chatTopLevelUiInstance);
		communicationFrame.setSize(455,600);
		//communicationFrame.pack();
		communicationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Communication Manager is a separate thread 
		commManagerInstance = ClientCommManager.getInstance();
		
		//Tx tx = new Tx();
		//tx.captureAudio();
		//System.out.println("I am Here");
	}
	public static void main(String args[])
	{
		
		 new ClientCommServStarter();
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
