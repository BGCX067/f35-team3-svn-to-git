package edu.tamu.f35.client.logic.world;

public class WorldGroundStaticObject extends WorldObject {

	public static final double GROUND_OBJ_SIZE = 20;

	public WorldGroundStaticObject() {
		super();
	}

	public WorldGroundStaticObject(double absoluteX, double absoluteY, double theta,
			double size) {
		super(absoluteX, absoluteY, 0.0, theta, size);
	}

	public WorldGroundStaticObject(double absoluteX, double absoluteY) {
		super(absoluteX, absoluteY, 0.0, 90, GROUND_OBJ_SIZE);
	}

	@Override
	public void step() {
		// do nothing, the object is static

	}

}
