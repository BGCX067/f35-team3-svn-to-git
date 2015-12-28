package edu.tamu.f35.client.logic;

import java.rmi.RemoteException;

import nav.rmi.common.DisplayData;
import nav.rmi.common.RemoteNav;

public class AvionicsMock implements RemoteNav {

	private double direction;
	private double speed;
	private int step = 0;

	public AvionicsMock() {
		direction = 0.0;
		speed = 5.0;
	}

	/**
	 * After 25 steps the plane will turn around
	 */
	public void step() {
		step++;
		if (step % 25 == 0 && direction == 0.0) {
			direction = 180.0;
		} else if (step % 25 == 0 && direction == 180.0) {
			direction = 0.0;
		}
	}

	@Override
	public DisplayData getData() throws RemoteException {
		DisplayData dd = new DisplayData();
		dd.current_speed = speed;
		dd.direction = direction;
		return dd;
	}
}
