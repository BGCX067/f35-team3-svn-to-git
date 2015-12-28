package edu.tamu.f35.client.logic.world;

public class PlaneObject extends WorldObject {

	public static final double F35_PLANE_SIZE = 5.0;

	public static final double FLYING_HEIGHT = 110.0;

	private double speed;

	public PlaneObject() {
		super();

	}

	public PlaneObject(double x, double y) {
		super(x, y, FLYING_HEIGHT, F35_PLANE_SIZE);

	}

	/**
	 * Sets the direction in degrees assuming clockwise reference with 0° at the
	 * north.
	 * 
	 * @param direction
	 */
	public double getDirection() {
		return 90 - getAbsoluteThetha();
	}

	/**
	 * Sets the direction in degrees assuming clockwise reference with 0° at the
	 * north.
	 * 
	 * @param direction
	 */
	public void setDirection(double direction) {
		setAbsoluteThetha(90 - direction);
	}

	public static double getF35PlaneSize() {
		return F35_PLANE_SIZE;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	@Override
	public void step() {
		absoluteX += speed * Math.cos(absoluteThetha * Math.PI / 180);
		absoluteY += speed * Math.sin(absoluteThetha * Math.PI / 180);
	}

}
