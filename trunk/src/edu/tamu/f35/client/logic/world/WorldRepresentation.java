package edu.tamu.f35.client.logic.world;

import java.util.ArrayList;
import java.util.List;

public class WorldRepresentation {

	private List<WorldObject> objectsInWorld;

	private PlaneObject principalPlane;

	private int currentStep = 0;

	public static final String DEFAULT_WORLD_NAME = "defaultWorld";

	public WorldRepresentation() {
		objectsInWorld = new ArrayList<WorldObject>();
		principalPlane = null;
	}

	public WorldRepresentation(String scenarioName, PlaneObject principalPlane) {
		this();
		this.principalPlane = principalPlane;
		if (scenarioName.equals(DEFAULT_WORLD_NAME)) {
			populateDefaultWorld();
		}
	}

	private void populateDefaultWorld() {
		WorldObject x1 = new WorldGroundStaticObject(20.0, 30.0);
		WorldObject x2 = new WorldGroundStaticObject(0.0, 20.0);
		WorldObject p1 = new RandomMovingEnemy(30.0, 30.0, 3.0, -10.0, 0.0);
		FollowingEnemy p2 = new FollowingEnemy(60.0, 80.0);
		p2.setPlane(principalPlane);
		AllyPlane a1 = new AllyPlane(-30.0, 10.0);
		a1.setPlane(principalPlane);

		objectsInWorld.add(x1);
		objectsInWorld.add(x2);
		objectsInWorld.add(p1);
		objectsInWorld.add(p2);
		objectsInWorld.add(a1);

	}

	public void step() {
		for (WorldObject wObject : objectsInWorld) {
			wObject.step();
		}

		currentStep++;
	}

	public List<WorldObject> getObjectsInWorld() {
		return objectsInWorld;
	}

	public PlaneObject getPrincipalPlane() {
		return principalPlane;
	}

	public void setPrincipalPlane(PlaneObject principalPlane) {
		this.principalPlane = principalPlane;
	}

}
