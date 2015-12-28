package edu.tamu.f35.client.logic.radar;

public class RadarServer {

	private RadarLogic myRadarLogic;

	private RadarServerThread serverThread = null;

	public RadarServer() {
		myRadarLogic = RadarLogic.getInstance();

	}

	public void startServer() {
		serverThread = new RadarServerThread();
		Thread t1 = new Thread(serverThread);
		t1.start();
	}

	public void stopServer() {
		serverThread.activated = false;
	}

	// class is used to update the list of user name
	class RadarServerThread implements Runnable {

		boolean activated = true;

		public void run() {
			System.out.println("Server started...");
			while (activated) {
				try {

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}

	}
}
