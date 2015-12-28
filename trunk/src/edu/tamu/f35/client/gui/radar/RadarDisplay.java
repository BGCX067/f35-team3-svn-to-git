package edu.tamu.f35.client.gui.radar;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import edu.tamu.f35.client.logic.radar.RadarLogic;
import edu.tamu.f35.client.logic.world.PlaneObject;
import f35.rmi.RadarObject;

public class RadarDisplay extends JPanel implements RadarListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7325982925998440638L;

	private RadarInfoPanel infoDisplay;

	final private RadarVisualDisplay visualDisplay;

	private RadarLogic radarLogic;

	private List<RadarObject> objectsInRange;

	public RadarDisplay(RadarLogic radarLogic) {
		super();
		if (radarLogic == null) {
			System.out.println("The radar logic is null!");
		} else {
			this.radarLogic = radarLogic;
			this.radarLogic.addRadarListener(this);
		}

		objectsInRange = new ArrayList<RadarObject>();

		setLayout(new BorderLayout());
		visualDisplay = new RadarVisualDisplay();
		visualDisplay.setRange(RadarLogic.DEFAULT_RANGE);

		infoDisplay = new RadarInfoPanel();

		this.add(infoDisplay, BorderLayout.SOUTH);
		this.add(visualDisplay, BorderLayout.CENTER);
	}

	public double getRange() {
		if (radarLogic != null)
			return radarLogic.getRange();

		else
			return visualDisplay.getRange();
	}

	public void setRange(double range) {
		if (radarLogic != null)
			radarLogic.setRange(range);
		visualDisplay.setRange(range);

	}

	public List<RadarObject> getObjectsInRange() {
		return objectsInRange;
	}

	@Override
	public void radarUpdate(List<RadarObject> objects, PlaneObject planeInfo) {
		this.objectsInRange = objects;
		visualDisplay.setRadarObjects(objectsInRange);
		visualDisplay.repaint();
		infoDisplay.updatePlaneInfo(planeInfo);

	}

	@Override
	public void rangeChanged(double range) {
		visualDisplay.setRange(range);

	}
}
