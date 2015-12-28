package edu.tamu.f35.dataCotract;

import java.io.Serializable;
import java.util.ArrayList;

public class OnlineUserListMessage implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1746147387906407991L;
	
	public String senderId;
	public ArrayList<User> userList;
	public ArrayList<Mission> missionList;
	
	public OnlineUserListMessage(String sid, ArrayList<User> ulist, ArrayList<Mission> mlist)
	{
		senderId = sid;
		userList = ulist;
		missionList = mlist;
	}
}
