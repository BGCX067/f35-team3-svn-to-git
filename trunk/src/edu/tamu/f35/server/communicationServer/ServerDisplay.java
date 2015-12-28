package edu.tamu.f35.server.communicationServer;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class ServerDisplay implements Runnable
{
	private int i;
	private static ServerDisplay instance = null;
	private JFrame frame;
	private JTextField tf;
	private JPanel panel;
	private DefaultListModel model;
	private JList list;
	private JScrollPane scrollpane;
	String txtMsg;

	protected ServerDisplay() 
	{
		i = 0;
		frame = new JFrame("Communication Server");
		tf = new JTextField();
		panel = new JPanel();
		model = new DefaultListModel();
		list = new JList(model);
		scrollpane = new JScrollPane(list);
		panel.add(tf);
		panel.add(scrollpane);
		tf.setBounds(10,10,400,50);
		scrollpane.setBounds(10, 80, 400, 300);
		frame.add(panel);
		panel.setLayout(null);
		frame.setSize(450, 450);
	    frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	// Exists only to defeat instantiation.
	}
	public static ServerDisplay getInstance() 
	{
		if(instance == null) 
		{
			instance = new ServerDisplay();
		}
		return instance;
	}
	
	synchronized
	public void DisplayOutput(String s)
	{
		//String message;
		txtMsg =  ":"+Thread.currentThread().toString()+" : "+s;

		SwingUtilities.invokeLater(this);
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		tf.setText(txtMsg);
		model.addElement(++i+txtMsg);
		
	}
}