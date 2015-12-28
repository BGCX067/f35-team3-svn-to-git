package edu.tamu.f35.dataCotract;

import java.io.Serializable;

public class RestoreMessage implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1196321865821249751L;
	public String senderId;
	public String senderName;
	public String time;
	public String message;
	public MessageType msgType;
	
	public RestoreMessage(String sid, String sn, String t, String msg, MessageType mt)
	{
		senderId = sid;
		senderName = sn;
		time = t;
		message = msg;
		msgType = mt;
	}
}
