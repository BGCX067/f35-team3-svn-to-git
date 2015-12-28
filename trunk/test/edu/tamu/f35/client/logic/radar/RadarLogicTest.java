package edu.tamu.f35.client.logic.radar;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;

import edu.tamu.f35.client.logic.world.PlaneObject;
import edu.tamu.f35.client.logic.world.RandomMovingEnemy;
import edu.tamu.f35.client.logic.world.WorldGroundStaticObject;
import edu.tamu.f35.client.logic.world.WorldObject;
import f35.rmi.RadarObject;

public class RadarLogicTest extends TestCase {

	RadarLogic rLogic;

	@Before
	public void setUp() throws Exception {
		rLogic = RadarLogic.getInstance();
	}

	public void testRadarMapping() {

		WorldObject object = new WorldGroundStaticObject(30, 30);
		PlaneObject plane = new PlaneObject(20, 20);
		plane.setAbsoluteThetha(0);

		double transformX = rLogic.mapToRadarObject(object,
				plane.getAbsoluteX(), plane.getAbsoluteY(),
				plane.getAbsoluteThetha()).getRelativeX();

		double transformY = rLogic.mapToRadarObject(object,
				plane.getAbsoluteX(), plane.getAbsoluteY(),
				plane.getAbsoluteThetha()).getRelativeY();

		assertEquals("The coordinates on X are not the expected", 10.0,
				roundTwoDecimals(transformX));
		assertEquals(
				"The coordinates on X are not the expected",
				10.0,
				rLogic.mapToRadarObject(object, plane.getAbsoluteX(),
						plane.getAbsoluteY(), plane.getAbsoluteThetha())
						.getRelativeY());
		plane.setAbsoluteY(30.0);
		assertEquals(
				"The coordinates on Y are not the expected",
				0.0,
				rLogic.mapToRadarObject(object, plane.getAbsoluteX(),
						plane.getAbsoluteY(), plane.getAbsoluteThetha())
						.getRelativeY());
		plane.setAbsoluteThetha(90.0);
		transformX = rLogic.mapToRadarObject(object, plane.getAbsoluteX(),
				plane.getAbsoluteY(), plane.getAbsoluteThetha()).getRelativeX();
		transformY = rLogic.mapToRadarObject(object, plane.getAbsoluteX(),
				plane.getAbsoluteY(), plane.getAbsoluteThetha()).getRelativeY();

		assertEquals("The coordinates on X are not the expected", 0.0,
				roundTwoDecimals(transformX));

		assertEquals("The coordinates on Y are not the expected", -10.0,
				transformY);

	}

	/**
	 * This will be a unit test where the test will be mocking the radar
	 * controller, Avionics Object, Plane Object and we check that every time
	 * the radar calls its plane object’s step function the absolute coordinates
	 * of the next position of the plane object will be calculated.
	 * 
	 * Initially the plane is at (10,10)m facing north with speed 5 m/step()
	 */
	public void testPlaneStep() {

		PlaneObject plane = new PlaneObject(10, 10);

		plane.setSpeed(5);
		plane.setDirection(0);

		assertEquals("The initial coordinates on X are not the expected", 10.0,
				roundTwoDecimals(plane.getAbsoluteX()));

		assertEquals("The initial coordinates on Y are not the expected", 10.0,
				roundTwoDecimals(plane.getAbsoluteY()));

		plane.step();

		assertEquals("The coordinates on X are not the expected", 10.0,
				roundTwoDecimals(plane.getAbsoluteX()));

		assertEquals("The coordinates on Y are not the expected", 15.0,
				roundTwoDecimals(plane.getAbsoluteY()));

		plane.step();

		assertEquals("The coordinates on X are not the expected", 10.0,
				roundTwoDecimals(plane.getAbsoluteX()));

		assertEquals("The coordinates on Y are not the expected", 20.0,
				roundTwoDecimals(plane.getAbsoluteY()));

		plane.setDirection(45);
		plane.setSpeed(14.1421);
		plane.step();

		assertEquals("The coordinates on X are not the expected", 20.0,
				roundTwoDecimals(plane.getAbsoluteX()));

		assertEquals("The coordinates on Y are not the expected", 30.0,
				roundTwoDecimals(plane.getAbsoluteY()));

	}

	public void testCoordinateTransform() {

		List<WorldObject> objectsInWorld;
		PlaneObject plane;

		double range = 50;

		objectsInWorld = new ArrayList<WorldObject>();

		WorldObject object = new WorldGroundStaticObject(0, 20);
		objectsInWorld.add(object);

		object = new WorldGroundStaticObject(20, 30);
		objectsInWorld.add(object);

		object = new RandomMovingEnemy(30, 30);

		plane = new PlaneObject(10, 10);
		plane.setSpeed(5);
		plane.setDirection(0);

		List<RadarObject> objectsDetected = rLogic.transformObjects(
				objectsInWorld, plane, range);

		assertNotNull("The list of detected objects is null", objectsDetected);

		assertFalse("The list of detected objects is empty",
				objectsDetected.isEmpty());

		assertEquals("The X coordinates of O1 are not the expected", -10.0,
				roundTwoDecimals(objectsDetected.get(0).getRelativeX()));
		assertEquals("The Y coordinates of O1 are not the expected", 10.0,
				roundTwoDecimals(objectsDetected.get(0).getRelativeY()));

	}
	
	public void testObjectDistance() {
		WorldObject object1 = new WorldGroundStaticObject(0,0);	
		WorldObject object2 = new WorldGroundStaticObject(0, 10);
		assertEquals("Unexpected distance", 10.0, object1.distanceToObject(object2));
		assertEquals("Unexpected distance", 10.0, object2.distanceToObject(object1));
		
	}
	
	public void testObjectDirection() {
		WorldObject object1 = new WorldGroundStaticObject(0,0);	
		WorldObject object2 = new WorldGroundStaticObject(10, 0);
		assertEquals("Unexpected direction", 0.0, object1.directionOfObject(object2));
		assertEquals("Unexpected direction", 180.0, object2.directionOfObject(object1));
		
	}

	private double roundTwoDecimals(double d) {
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		return Double.valueOf(twoDForm.format(d));
	}

	@After
	public void tearDown() throws Exception {
		rLogic = new RadarLogic();
	}

}
