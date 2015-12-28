package edu.tamu.f35.client.gui.radar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import edu.tamu.f35.client.gui.Cockpit;
import edu.tamu.f35.client.logic.radar.RadarLogic;
import edu.tamu.f35.client.logic.world.PlaneObject;

public class RadarInfoPanel extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JLabel currentCoordinates;

	private JButton activateRadar;

	private JPanel buttonPanel;

	private ImageIcon radarOffIcon;

	private ImageIcon radarOnIcon;

	private boolean isRadarOn = false;

	public RadarInfoPanel() {

		setPreferredSize(new Dimension(400, 200));
		setBackground(Color.BLACK);
		setForeground(Color.WHITE);

		Border infoPanelBorder = BorderFactory.createMatteBorder(2, 4, 4, 2,
				Cockpit.BORDER_COLOR);

		setBorder(infoPanelBorder);
		setLayout(new BorderLayout());

		Font font = new Font("Courier", Font.PLAIN, 18);

		Color transparent = new Color(0, 0, 0, 0);
		// coordinates
		JLabel coordinateLbl = new JLabel("Coord: ");
		coordinateLbl.setForeground(Color.white);
		coordinateLbl.setFont(font);
		currentCoordinates = new JLabel("(0,0)");
		currentCoordinates.setForeground(Color.white);
		currentCoordinates.setFont(font);

		JPanel coordMiniPanel = new JPanel();
		coordMiniPanel.setBackground(transparent);

		coordMiniPanel.setLayout(new BorderLayout());
		coordMiniPanel.add(coordinateLbl, BorderLayout.LINE_START);
		coordMiniPanel.add(currentCoordinates, BorderLayout.CENTER);

		buttonPanel = new JPanel();

		radarOffIcon = new ImageIcon("images/radarOff.png", "Radar Off");
		radarOnIcon = new ImageIcon("images/radarOn.png", "Radar On");

		// TODO resize image
		activateRadar = new JButton("RADAR OFF", radarOffIcon);
		activateRadar.setVerticalTextPosition(AbstractButton.CENTER);
		activateRadar.setHorizontalTextPosition(AbstractButton.LEADING);
		activateRadar.addActionListener(this);
		activateRadar.setFont(font);
		activateRadar.setForeground(Color.WHITE);
		activateRadar.setBackground(Color.DARK_GRAY);
		activateRadar.setMargin(new Insets(1, 1, 1, 1));

		buttonPanel.add(activateRadar);
		buttonPanel.setBackground(Color.BLACK);
		Border buttonDivider = BorderFactory.createMatteBorder(2, 0, 0, 0,
				Cockpit.BORDER_COLOR);
		buttonPanel.setBorder(buttonDivider);

		add(coordMiniPanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.PAGE_END);

	}

	public void updatePlaneInfo(PlaneObject plane) {
		if (plane != null) {
			NumberFormat formatter = new DecimalFormat("#0.00");

			String coord = "(" + formatter.format(plane.getAbsoluteX()) + ","
					+ formatter.format(plane.getAbsoluteY()) + ")";
			currentCoordinates.setText(coord);
		}
		repaint();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource().equals(activateRadar)) {
			RadarLogic radarLogic = RadarLogic.getInstance();
			if (!isRadarOn) {
				System.out.println("Radar Activated");
				activateRadar.setText("RADAR ON");
				activateRadar.setIcon(radarOnIcon);
				radarLogic.activateRadar();
				isRadarOn = true;
			} else {
				System.out.println("Radar Deactivated");
				activateRadar.setText("RADAR OFF");
				activateRadar.setIcon(radarOffIcon);
				radarLogic.deactivateRadar();
				isRadarOn = false;
			}
		}

	}

}
