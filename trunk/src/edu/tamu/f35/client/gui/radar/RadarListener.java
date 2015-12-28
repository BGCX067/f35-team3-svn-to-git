package edu.tamu.f35.client.gui.radar;

import java.util.List;

import edu.tamu.f35.client.logic.world.PlaneObject;
import f35.rmi.RadarObject;

public interface RadarListener {
	
	public void radarUpdate(List<RadarObject> objects, PlaneObject planeInfo);
	
	public void rangeChanged(double range);

}
