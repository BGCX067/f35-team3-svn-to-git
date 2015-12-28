package edu.tamu.f35.client.gui;

import java.rmi.RMISecurityManager;

import javax.swing.JFrame;

import edu.tamu.f35.client.gui.radar.RadarDisplay;
import edu.tamu.f35.client.logic.radar.RadarLogic;

public class MockCockpit extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7953008358115517266L;
	
	private RadarDisplay myRadarDisplay;

	public MockCockpit() {

		RadarLogic radarLogic;
		try {
			radarLogic = RadarLogic.getInstance();
			myRadarDisplay = new RadarDisplay(radarLogic);
		} catch (Exception e) {
			System.out.println("Failed creating the radar logic or display: ");
			e.printStackTrace();
		}

		setContentPane(myRadarDisplay);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.setSecurityManager(new RMISecurityManager());
		MockCockpit mainC = new MockCockpit();
		mainC.pack();
		mainC.setVisible(true);

	}

}
