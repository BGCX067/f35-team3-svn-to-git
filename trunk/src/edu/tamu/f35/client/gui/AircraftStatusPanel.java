package edu.tamu.f35.client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The aircraft status panel will graphically display the status of
 * the aircraft. It will display health of body components as well
 * as other status things...
 * 
 * This panel is one of the selectable 2:3 panels
 *  
 * @author dalogsdon
 *
 */
public class AircraftStatusPanel extends JPanel
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1244212007665399574L;

	public AircraftStatusPanel()
	{
		setBackground(Color.BLACK);
		setLayout(new BorderLayout());
		JLabel label = new JLabel("STATUS!");
		label.setFont(new Font("Arial", Font.PLAIN, 18));
		label.setForeground(Cockpit.BORDER_COLOR);
		add(label, BorderLayout.PAGE_START);
		
		setBorder(BorderFactory.createLineBorder(Cockpit.BORDER_COLOR));
	}
	
	private void drawPlane(Graphics g)
	{
		// left wing
		g.drawLine((int)(getWidth()/2.5), 0, getWidth()/3, getHeight()/7);
		g.drawLine(getWidth()/3, getHeight()/7, getWidth()/15, getHeight()/4);
		g.drawLine(getWidth()/15, getHeight()/4, getWidth()/15, (int)(getHeight()/2.5));
		g.drawLine(getWidth()/15, (int)(getHeight()/2.5), (int)(getWidth()/3.2), (int)(getHeight()/2.25));
		g.drawLine(getWidth()/15, (int)(getHeight()/3.6), (int)(getWidth()/3.6), (int)(getHeight()/5.15));
		g.drawLine((int)(getWidth()/3.6), (int)(getHeight()/5.15), (int)(getWidth()/3.6), (int)(getHeight()/6));
		g.drawLine((int)(getWidth()/11.0), (int)(getHeight()/2.475), (int)(getWidth()/11.0), (int)(getHeight()/2.7));
		g.drawLine((int)(getWidth()/11.0), (int)(getHeight()/2.7), (int)(getWidth()/3.5), (int)(getHeight()/2.47));
		g.drawLine((int)(getWidth()/3.4), (int)(getHeight()/2.35), (int)(getWidth()/3.4), (int)(getHeight()/2.275));
		
		// right wing
		g.drawLine((int)(getWidth() - getWidth()/2.5), 0, getWidth() - getWidth()/3, getHeight()/7);
		g.drawLine(getWidth() - getWidth()/3, getHeight()/7, getWidth() - getWidth()/15, getHeight()/4);
		g.drawLine(getWidth() - getWidth()/15, getHeight()/4, getWidth() - getWidth()/15, (int)(getHeight()/2.5));
		g.drawLine(getWidth() - getWidth()/15, (int)(getHeight()/2.5), (int)(getWidth() - getWidth()/3.2), (int)(getHeight()/2.25));
		g.drawLine(getWidth() - getWidth()/15, (int)(getHeight()/3.6), (int)(getWidth() - getWidth()/3.6), (int)(getHeight()/5.15));
		g.drawLine((int)(getWidth() - getWidth()/3.6), (int)(getHeight()/5.15), (int)(getWidth() - getWidth()/3.6), (int)(getHeight()/6));
		g.drawLine((int)(getWidth() - getWidth()/11.0), (int)(getHeight()/2.475), (int)(getWidth() - getWidth()/11.0), (int)(getHeight()/2.7));
		g.drawLine((int)(getWidth() - getWidth()/11.0), (int)(getHeight()/2.7), (int)(getWidth() - getWidth()/3.5), (int)(getHeight()/2.47));
		g.drawLine((int)(getWidth() - getWidth()/3.4), (int)(getHeight()/2.35), (int)(getWidth() - getWidth()/3.4), (int)(getHeight()/2.275));
		
		// left tail vertical
		g.drawLine((int)(getWidth()/3.05), (int)(getHeight()/2.35), (int)(getWidth()/4.2), (int)(getHeight()/1.9));
		g.drawLine((int)(getWidth()/4.2), (int)(getHeight()/1.9), (int)(getWidth()/3.9), (int)(getHeight()/1.7));
		g.drawLine((int)(getWidth()/3.9), (int)(getHeight()/1.7), (int)(getWidth()/3.0), (int)(getHeight()/1.75));
		g.drawLine((int)(getWidth()/3.9), (int)(getHeight()/1.75), (int)(getWidth()/3.0), (int)(getHeight()/1.8));
		g.drawLine((int)(getWidth()/2.75), (int)(getHeight()/2.35), (int)(getWidth()/2.75), (int)(getHeight()/2.1));
		
		// right tail vertical
		g.drawLine((int)(getWidth() - getWidth()/3.05), (int)(getHeight()/2.35), (int)(getWidth() - getWidth()/4.2), (int)(getHeight()/1.9));
		g.drawLine((int)(getWidth() - getWidth()/4.2), (int)(getHeight()/1.9), (int)(getWidth() - getWidth()/3.9), (int)(getHeight()/1.7));
		g.drawLine((int)(getWidth() - getWidth()/3.9), (int)(getHeight()/1.7), (int)(getWidth() - getWidth()/3.0), (int)(getHeight()/1.75));
		g.drawLine((int)(getWidth() - getWidth()/3.9), (int)(getHeight()/1.75), (int)(getWidth() - getWidth()/3.0), (int)(getHeight()/1.8));
		g.drawLine((int)(getWidth() - getWidth()/2.75), (int)(getHeight()/2.35), (int)(getWidth() - getWidth()/2.75), (int)(getHeight()/2.1));
		
		// left tail
		g.drawLine((int)(getWidth()/4.1), (int)(getHeight()/1.8), (int)(getWidth()/8.0), (int)(getHeight()/1.65));
		g.drawLine((int)(getWidth()/8.0), (int)(getHeight()/1.65), (int)(getWidth()/7.5), (int)(getHeight()/1.5));
		g.drawLine((int)(getWidth()/7.5), (int)(getHeight()/1.5), (int)(getWidth()/2.6), (int)(getHeight()/1.4));
		g.drawLine((int)(getWidth()/2.6), (int)(getHeight()/1.4), (int)(getWidth()/2.55), (int)(getHeight()/1.425));
		
		// left tail
		g.drawLine((int)(getWidth() - getWidth()/4.1), (int)(getHeight()/1.8), (int)(getWidth() - getWidth()/8.0), (int)(getHeight()/1.65));
		g.drawLine((int)(getWidth() - getWidth()/8.0), (int)(getHeight()/1.65), (int)(getWidth() - getWidth()/7.5), (int)(getHeight()/1.5));
		g.drawLine((int)(getWidth() - getWidth()/7.5), (int)(getHeight()/1.5), (int)(getWidth() - getWidth()/2.6), (int)(getHeight()/1.4));
		g.drawLine((int)(getWidth() - getWidth()/2.6), (int)(getHeight()/1.4), (int)(getWidth() - getWidth()/2.55), (int)(getHeight()/1.425));
		
		// boxes
		g.drawRect((int)(getWidth()/3.5), (int)(getHeight()/2.7), 2*(int)(getWidth()/2-getWidth()/3.5), (int)(getHeight()/2.35-getHeight()/2.7));
		g.drawRect((int)(getWidth()/3.0), (int)(getHeight()/2.1), 2*(int)(getWidth()/2-getWidth()/3.0), (int)(getHeight()/4.4));
	}
	
	@Override
	public void paint(Graphics g)
	{
		super.paint(g);
		g.setColor(Cockpit.BORDER_COLOR);
		drawPlane(g);
	}
}
