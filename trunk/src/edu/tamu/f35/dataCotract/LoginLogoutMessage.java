package edu.tamu.f35.dataCotract;

import java.io.Serializable;

public class LoginLogoutMessage implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 757142040731480630L;
	public String senderId;
	//public TerminalType senderType; 
	String message = null;
	MessageType msgType;
	public LoginLogoutMessage(String sendId, /*TerminalType termType,*/ MessageType msgT, String msg)
	{
		senderId = sendId;
		//senderType = termType;
		//msgType = msgT;
		message = msg;
		msgType = msgT;
	}
	// LoginMessage
}
