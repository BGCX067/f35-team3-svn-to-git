package edu.tamu.f35.client.logic.world;

import java.text.DecimalFormat;
import java.util.Random;

import edu.tamu.f35.client.logic.radar.RadarLogic;

public class FollowingEnemy extends WorldObject {

	public static final double FLYING_HEIGHT = 100.0;

	public static final double DEFAULT_SPEED = 10.0;

	public static final double ENEMY_SIZE = 15.5;

	private double speed;

	private PlaneObject objectivePlane;

	private Random randomGenerator;

	public FollowingEnemy() {
		super();
		this.speed = 0.0;
	}

	public FollowingEnemy(double absoluteX, double absoluteY) {
		this(absoluteX, absoluteY, ENEMY_SIZE, DEFAULT_SPEED, 180.0);
	}

	protected FollowingEnemy(double absoluteX, double absoluteY, double size,
			double speed, double direction) {
		super(absoluteX, absoluteY, FLYING_HEIGHT, 0.0, size);
		setDirection(direction);
		this.speed = speed;
		randomGenerator = new Random();
	}

	/**
	 * This method is supposed to move the enemy in the current direction with
	 * the current speed
	 */
	public void step() {
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		System.out.println("Enemy: (" + twoDForm.format(absoluteX) + ","
				+ twoDForm.format(absoluteY) + ")");
		if (objectivePlane == null) {
			setAbsoluteDirection(randomGenerator.nextDouble() * 360);
		} else {
			double objectiveDir = calculateDirection(objectivePlane);
			setAbsoluteDirection(objectiveDir);
			double adjustedSpeed = calculateSpeed(objectivePlane);
			setSpeed(adjustedSpeed);

		}

		absoluteX += speed * Math.cos(absoluteThetha * Math.PI / 180);
		absoluteY += speed * Math.sin(absoluteThetha * Math.PI / 180);

	}

	private double calculateSpeed(PlaneObject objectivePlane) {
		double distToPlane = distanceToObject(objectivePlane);
		if (distToPlane > RadarLogic.DEFAULT_RANGE) {
			return objectivePlane.getSpeed() * 2;
		} else if (distToPlane > RadarLogic.DEFAULT_RANGE / 2) {
			return objectivePlane.getSpeed() / 2;
		} else {
			return 0;
		}
	}

	private double calculateDirection(PlaneObject objectivePlane) {
		return directionOfObject(objectivePlane);
	}

	/**
	 * Sets the plane object so the enemy can know where to go next
	 */
	public void setPlane(PlaneObject plane) {
		this.objectivePlane = plane;
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

	/**
	 * Sets the direction in degrees assuming counterclockwise reference with 0°
	 * at the east.
	 * 
	 * @param direction
	 */
	public void setAbsoluteDirection(double direction) {
		setAbsoluteThetha(direction);
	}

}
