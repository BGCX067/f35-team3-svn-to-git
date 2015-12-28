package edu.tamu.f35.client.logic.radar;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

import javazoom.jl.player.Player;
import nav.rmi.common.DisplayData;
import nav.rmi.common.RemoteNav;
import edu.tamu.f35.client.gui.radar.RadarListener;
import edu.tamu.f35.client.logic.AvionicsMock;
import edu.tamu.f35.client.logic.world.AllyPlane;
import edu.tamu.f35.client.logic.world.PlaneObject;
import edu.tamu.f35.client.logic.world.WorldObject;
import edu.tamu.f35.client.logic.world.WorldRepresentation;
import edu.tamu.f35.remote.RemoteRadarImpl;
import f35.rmi.RadarObject;

public class RadarLogic {

	public static final double DEFAULT_RANGE = 50.0;

	private WorldRepresentation myWorld;

	private RemoteNav myAvionicsMock = null;

	/**
	 * The current range of the radar
	 */
	private double range;

	private List<RadarObject> myListOfRadarObjects;

	private PlaneObject myPlane;

	private List<RadarListener> radarListeners;

	private static RadarLogic instance = null;

	protected boolean isWorldEmulatorRunning = false;

	private RadarUpdateThread radarThread = null;

	public static final double INITIAL_PLANE_X = 10.0;

	public static final double INITIAL_PLANE_Y = 10.0;

	public static final String SOUND_FILENAME = "audio/blip2.mp3";

	public static final String AVIONICS_SERVER = "128.194.131.37";

	private Player soundPlayer;

	protected RadarLogic() {

		myPlane = new PlaneObject(INITIAL_PLANE_X, INITIAL_PLANE_Y);
		
		// mocking objects
		myWorld = new WorldRepresentation(
				WorldRepresentation.DEFAULT_WORLD_NAME, myPlane);

		try {
			myAvionicsMock = (RemoteNav) Naming.lookup("rmi://"
					+ AVIONICS_SERVER + "/Navigation");
		} catch (MalformedURLException e2) {
			e2.printStackTrace();
		} catch (RemoteException e2) {
			e2.printStackTrace();
		} catch (NotBoundException e2) {
			System.out.println("Remote avionics not found, using AvionicsMock");
		}

		if (myAvionicsMock == null)
			myAvionicsMock = new AvionicsMock();

		
		myListOfRadarObjects = new ArrayList<RadarObject>();
		radarListeners = new ArrayList<RadarListener>();

		try {
			Registry reg = LocateRegistry.getRegistry();
			reg.bind("radar", new RemoteRadarImpl());
			System.out.println("Radar server running");
		} catch (AccessException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (AlreadyBoundException e) {
			try {
				Registry reg = LocateRegistry.getRegistry();
				reg.rebind("radar", new RemoteRadarImpl());
				System.out.println("Radar server rebinded and running");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

		setRange(DEFAULT_RANGE);

	}

	public void addRadarListener(RadarListener rl) {
		radarListeners.add(rl);
	}

	public void activateRadar() {

		if (!isWorldEmulatorRunning) {

			radarThread = new RadarUpdateThread();
			Thread t1 = new Thread(radarThread);
			t1.start();
		}

		isWorldEmulatorRunning = true;

	}

	public void deactivateRadar() {
		if (isWorldEmulatorRunning) {
			radarThread.activated = false;
			isWorldEmulatorRunning = false;
		}
	}

	public static synchronized RadarLogic getInstance() {
		if (instance == null) {
			instance = new RadarLogic();
		}

		return instance;
	}

	// class is used to update the list of user name
	class RadarUpdateThread implements Runnable {

		boolean activated = true;

		public void run() {
			System.out.println("Entering thread...");
			int step = 0;
			while (activated) {
				try {
					System.out.println("Step " + step);
					updatePlane();
					updateObjects();
					playSound();
					step++;
					Thread.sleep(1000);

				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}

	}

	/**
	 * Update the variable myPlane with the current state of the plane given the
	 * speed and direction from the Avionics system
	 */
	private void updatePlane() {
		try {
			DisplayData dd = myAvionicsMock.getData();
			double plane_direction = dd.direction;
			double plane_speed = dd.current_speed;

			myPlane.setSpeed(plane_speed);
			myPlane.setDirection(plane_direction);

			myPlane.step();
			if (myAvionicsMock instanceof AvionicsMock)
				((AvionicsMock) myAvionicsMock).step();

		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method mocks the radar readings and a constant change of the world
	 * objects
	 */
	private void updateObjects() {
		List<WorldObject> objectsInWorld = myWorld.getObjectsInWorld();

		myListOfRadarObjects = transformObjects(objectsInWorld, myPlane, range);
		for (RadarListener listener : radarListeners) {
			listener.radarUpdate(myListOfRadarObjects, myPlane);
		}
		myWorld.step();

	}

	/**
	 * Plays a sound when an enemy is found
	 */
	private void playSound() {
		try {
			if (myListOfRadarObjects != null && !myListOfRadarObjects.isEmpty()) {

				FileInputStream fis = new FileInputStream(SOUND_FILENAME);
				BufferedInputStream bis = new BufferedInputStream(fis);
				soundPlayer = new Player(bis);

				// run in new thread to play in background
				new Thread() {
					public void run() {
						try {
							soundPlayer.play();
						} catch (Exception e) {
							System.out.println(e);
						}
					}
				}.start();
			}
		} catch (Exception e) {
			System.out.println("Unable to play sound " + SOUND_FILENAME + ":");
			e.printStackTrace();
		}

	}

	/**
	 * This function takes as an input a list of objects in the world and maps
	 * it to a list of radar objects if they are within range.
	 * 
	 * @param objectsInWorld
	 * @param planeObject
	 * @param range
	 * @return
	 */
	public List<RadarObject> transformObjects(List<WorldObject> objectsInWorld,
			PlaneObject planeObject, double range) {
		List<RadarObject> radarObjects = new ArrayList<RadarObject>();

		double myX = planeObject.getAbsoluteX();
		double myY = planeObject.getAbsoluteY();
		double myTheta = planeObject.getDirection();
		int i = 1;
		for (WorldObject worldObject : objectsInWorld) {
			double objectX = worldObject.getAbsoluteX();
			double objectY = worldObject.getAbsoluteY();
			double dist = Math.sqrt((Math.pow(objectX - myX, 2) + Math.pow(
					objectY - myY, 2)));

			if (dist < range) {
				RadarObject rObject = mapToRadarObject(worldObject, myX, myY,
						myTheta);
				rObject.setUid("o" + i);
				rObject.setScreenName("o" + i);
				radarObjects.add(rObject);
				i++;
			}
		}

		return radarObjects;
	}

	public RadarObject mapToRadarObject(WorldObject worldObject, double myX,
			double myY, double myTheta) {

		double relR = Math
				.sqrt((Math.pow(worldObject.getAbsoluteX() - myX, 2) + Math
						.pow(worldObject.getAbsoluteY() - myY, 2)));
		double thetaAligned = Math.atan2(worldObject.getAbsoluteY() - myY,
				worldObject.getAbsoluteX() - myX) * 180 / Math.PI;

		double relTheta = thetaAligned - myTheta;

		double relativeX = relR * Math.cos(relTheta * Math.PI / 180);
		double relativeY = relR * Math.sin(relTheta * Math.PI / 180);
		
		RadarObject rObject = new RadarObject(relativeX, relativeY,
				worldObject.getAbsoluteX(), worldObject.getAbsoluteY(), "", "");

		if (worldObject instanceof AllyPlane)
			rObject.setFriend(true);

		else
			rObject.setFriend(false);

		return rObject;
	}

	public List<RadarObject> getRadarObjects() {
		return myListOfRadarObjects;
	}

	public PlaneObject getPlaneInfo() {
		// return myAvionicsMock.getCurrentCoordinates();
		return myPlane;
	}

	public double getRange() {
		return range;
	}

	public void setRange(double range) {
		this.range = range;
		for (RadarListener listener : radarListeners) {
			listener.rangeChanged(range);
		}
	}

}
