package edu.tamu.f35.client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The warnings panel will display high priority warnings to the 
 * pilot.
 * 
 * This panel is one of the fixed top strip panels.
 * 
 * @author dalogsdon
 *
 */
public class WarningsPanel extends JPanel
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5600040474028445054L;
	
	public WarningsPanel()
	{
		setBackground(Color.BLACK);
		setLayout(new BorderLayout());
		JLabel label = new JLabel("WARNINGS!");
		label.setFont(new Font("Arial", Font.PLAIN, 18));
		label.setForeground(Cockpit.BORDER_COLOR);
		add(label, BorderLayout.PAGE_START);
		
		setBorder(BorderFactory.createLineBorder(Cockpit.BORDER_COLOR));
	}
	
	
}
