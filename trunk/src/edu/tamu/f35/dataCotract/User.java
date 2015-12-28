package edu.tamu.f35.dataCotract;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5890784271680983407L;
	
	private String name;
	private String id;
	private TerminalType type;
	private ArrayList<String> missions;
	
	public String getName(){return name;}
	public void setName(String n){name = n;}
	public String getId() {return id;}
	public void SetId(String i) {id = i;}
	public TerminalType getType(){return type;}
	public void SetType(TerminalType tt){type = tt;}
	public ArrayList<String> getMissions(){return missions;}
	public void setMissions(ArrayList<String> m) {missions = m;}
	
	public User(String n, String i, String tt)
	{
		name = n;
		id = i;
		type = TerminalType.valueOf(tt);
	}
	
	public User()
	{
		
	}
	
}
