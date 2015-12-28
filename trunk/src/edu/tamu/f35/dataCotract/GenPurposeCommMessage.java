package edu.tamu.f35.dataCotract;

import java.io.Serializable;
import java.util.ArrayList;


//This message defines a general purpose message... Which is communicated over general communication socket
//This could be of three types (not creating any super class or sub class, simply adding redundant information in the class)
// 1. BRODCAST Message - Broadcasted to all the online users
// 2. PRIVATE Message -  Send to only one user
// 3. MISSION BROADCAST Message - Broadcasted to a list of missions
public class GenPurposeCommMessage implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3986176786375958971L;
	
	public String senderId;
	public String senderName;
	public ArrayList<String> recvIds; //Use in case of private communication
	//public String recvName;
	public MessageType msgType;
	public String message;
	public ArrayList<String> missionIds; //Should be null in case of private or broad cast message
	public ArrayList<RestoreMessage> resMessages;

	
	public GenPurposeCommMessage(String sid, String sName, ArrayList<String> rids, 
								MessageType mType, ArrayList<String>  missions, String msg)
	{
		senderId = sid;
		senderName = sName;
		recvIds = rids;
		//recvName = rName;
		msgType = mType;
		missionIds = missions;
		message = msg;
	}
	
	public GenPurposeCommMessage(String sId, MessageType mt, ArrayList<RestoreMessage> resMsgs)
	{
		senderId = sId;
		msgType = mt;
		resMessages = resMsgs;
	}
	
	
}
