package edu.tamu.f35.client.logic.world;

/**
 * Representation of an object in the emulated world in 3D space
 * 
 * @author fvides
 * 
 */
public abstract class WorldObject {

	protected double absoluteX;

	protected double absoluteY;

	protected double absoluteZ;

	/**
	 * Defines the orientation of the object as an angle in degrees. The object
	 * is at 0º when its head is aligned to the north and it increments
	 * counterclockwise
	 */
	protected double absoluteThetha;

	/**
	 * diameter of the bounding circle of the object in meters
	 */
	protected double size;

	protected WorldObject() {
		this(0.0, 0.0, 0.0, 0.0, 1.0);
	}

	protected WorldObject(double absoluteX, double absoluteY, double absoluteZ,
			double size) {
		this(absoluteX, absoluteY, absoluteZ, 0.0, size);
	}

	protected WorldObject(double absoluteX, double absoluteY, double absoluteZ,
			double theta, double size) {
		super();
		this.absoluteX = absoluteX;
		this.absoluteY = absoluteY;
		this.absoluteZ = absoluteZ;
		this.absoluteThetha = theta;
		this.size = size;
	}

	public double getAbsoluteX() {
		return absoluteX;
	}

	public void setAbsoluteX(double absoluteX) {
		this.absoluteX = absoluteX;
	}

	public double getAbsoluteY() {
		return absoluteY;
	}

	public void setAbsoluteY(double absoluteY) {
		this.absoluteY = absoluteY;
	}

	public double getAbsoluteZ() {
		return absoluteZ;
	}

	public void setAbsoluteZ(double absoluteZ) {
		this.absoluteZ = absoluteZ;
	}

	public double getSize() {
		return size;
	}

	public void setSize(double size) {
		this.size = size;
	}

	public double getAbsoluteThetha() {
		return absoluteThetha;
	}

	public void setAbsoluteThetha(double absoluteThetha) {
		this.absoluteThetha = absoluteThetha;
	}

	/**
	 * Returns the absolute euclidean distance to the specified object in the XY
	 * plane (disregards z), if the object is null the distance is
	 * Double.MAX_VALUE (infinity)
	 * 
	 * @param o
	 * @return
	 */
	public double distanceToObject(WorldObject o) {
		if (o == null)
			return Double.MAX_VALUE;

		double dist = Math
				.sqrt((Math.pow(absoluteX - o.getAbsoluteX(), 2) + Math.pow(
						absoluteY - o.getAbsoluteY(), 2)));
		return dist;
	}

	/**
	 * returns the direction of the specified object with 0º aligned to the east
	 * and incrementing counterclockwise
	 * 
	 * @param o
	 * @return
	 */
	public double directionOfObject(WorldObject o) {
		double theta = Math.atan2(o.getAbsoluteY() - absoluteY,
				o.getAbsoluteX() - absoluteX)
				* 180 / Math.PI;
		return theta;
	}

	/**
	 * Function used to update the coordinates of the object in the next step.
	 */
	public abstract void step();

}
