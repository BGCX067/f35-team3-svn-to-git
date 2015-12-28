package edu.tamu.f35.client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import edu.tamu.f35.client.gui.communication.ClientCommTopLevelUI;
import edu.tamu.f35.client.gui.radar.RadarDisplay;
import edu.tamu.f35.client.logic.communication.ClientCommManager;
import edu.tamu.f35.client.logic.radar.RadarLogic;

/**
 * The cockpit is a frame that will contain all interface panels.
 * 
 * @author dalogsdon
 *
 */
public class Cockpit extends JFrame implements ComponentListener
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -767092861275597308L;

	public static final double ASPECT_RATIO = 2.4;
	public static final Color BORDER_COLOR = new Color(192,243,255);
	
	private JPanel cockpitPanel;
	private JPanel stripPanel;
	private JPanel selectablePanel;
	private RadarDisplay myRadarDisplay;
	//private TempTopLevelChatPanel chatPaneInstance;
	ClientCommTopLevelUI chatTopLevelUiInstance;
	static ClientCommManager commManagerInstance;
	
	public Cockpit()
	{
		setTitle("F-35 Cockpit");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double height = screenSize.getWidth() / ASPECT_RATIO;
		setSize(new Dimension(screenSize.width, (int)height));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addComponentListener(this);
		
		//--Radar Group--
		RadarLogic radarLogic;
		try {
			radarLogic = RadarLogic.getInstance();
			myRadarDisplay = new RadarDisplay(radarLogic);
		} catch (Exception e) {
			System.out.println("Failed creating the radar logic or display: ");
			e.printStackTrace();
		}
		
		//chatPaneInstance = TempTopLevelChatPanel.getInstance();
		//-----Communication System-----
		chatTopLevelUiInstance = ClientCommTopLevelUI.getInstance();
		commManagerInstance = ClientCommManager.getInstance();
		
		cockpitPanel = new JPanel();
		cockpitPanel.setLayout(new BorderLayout());
		
		selectablePanel = new JPanel();
		selectablePanel.setLayout(new GridLayout(1, 4));
		
		stripPanel = new JPanel();
		stripPanel.setLayout(new GridLayout(1, 2));
		
		selectablePanel.add(myRadarDisplay);
		selectablePanel.add(chatTopLevelUiInstance);
		//selectablePanel.add(new AircraftStatusPanel());
		selectablePanel.add(new AircraftStatusPanel());
		selectablePanel.add(new AircraftStatusPanel());
		
		stripPanel.add(new WarningsPanel());
		stripPanel.add(new WarningsPanel());
		
		cockpitPanel.add(stripPanel, BorderLayout.PAGE_START);
		cockpitPanel.add(selectablePanel, BorderLayout.CENTER);
		
		add(cockpitPanel);
		resizePanels();
	}
	
	private void resizePanels()
	{
		stripPanel.setPreferredSize(new Dimension((int)(stripPanel.getWidth()), (int)(getHeight()/10.0)));
	}
	
	public static void main (String[] args)
	{
		//System.setSecurityManager(new RMISecurityManager());
		Cockpit c = new Cockpit();
		c.setVisible(true);
	}

	@Override
	public void componentHidden(ComponentEvent e) {}

	@Override
	public void componentMoved(ComponentEvent e) {}

	@Override
	public void componentResized(ComponentEvent e) 
	{
		resizePanels();
	}

	@Override
	public void componentShown(ComponentEvent e) {}
}
