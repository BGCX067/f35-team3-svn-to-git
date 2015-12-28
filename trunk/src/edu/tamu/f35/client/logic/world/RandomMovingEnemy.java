package edu.tamu.f35.client.logic.world;

public class RandomMovingEnemy extends WorldObject {

	private double speed;

	public static final double FLYING_HEIGHT = 100.0;

	public static final double DEFAULT_SPEED = 10.0;

	public static final double ENEMY_SIZE = 15.5;

	public RandomMovingEnemy() {
		super();
		this.speed = 0.0;
	}

	public RandomMovingEnemy(double absoluteX, double absoluteY) {
		this(absoluteX, absoluteY, ENEMY_SIZE, DEFAULT_SPEED, 180.0);
	}

	public RandomMovingEnemy(double absoluteX, double absoluteY, double size,
			double speed, double direction) {
		super(absoluteX, absoluteY, FLYING_HEIGHT, 0.0, size);
		setDirection(direction);
		this.speed = speed;
	}

	/**
	 * This method is supposed to move the enemy in the current direction with
	 * the current speed
	 */
	public void step() {
		absoluteX += speed * Math.cos(absoluteThetha * Math.PI / 180);
		absoluteY += speed * Math.sin(absoluteThetha * Math.PI / 180);

	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
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

}
