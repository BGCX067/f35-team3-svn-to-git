package edu.tamu.f35.dataCotract;

import java.io.Serializable;

public class Mission implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1664977244149852081L;
	private String missionId;
	private String missionName;
	
	public String getId(){return missionId;}
	public void setId(String id) {missionId = id;}
	public String getName(){return missionName;}
	public void setName(String name){missionName = name;}
}
