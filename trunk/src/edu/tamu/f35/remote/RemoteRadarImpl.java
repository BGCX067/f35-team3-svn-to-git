package edu.tamu.f35.remote;

import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import edu.tamu.f35.client.logic.radar.RadarLogic;
import f35.rmi.RadarObject;
import f35.rmi.RemoteRadar;

public class RemoteRadarImpl extends UnicastRemoteObject implements RemoteRadar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8544881677650149976L;

	public RemoteRadarImpl() throws RemoteException {
		super();		
	}

	@Override
	public List<RadarObject> getRadarObjects() throws RemoteException {		
		return RadarLogic.getInstance().getRadarObjects();
	}
	
	public static void main (String[] args)
	{
		try {
			Registry reg = LocateRegistry.getRegistry();
			reg.bind("radar", new RemoteRadarImpl());
			System.out.println("Radar server running");
		} catch (AccessException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (AlreadyBoundException e) {			
			e.printStackTrace();
		} 
	}

}
