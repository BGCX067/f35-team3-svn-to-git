package edu.tamu.f35.dataCotract;

import java.io.Serializable;

public class LoginAckMessage implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7644701609515808394L;
	public final static int FAILED = 1;
	public final static int SUCCEED = 2;
	public final static int FAULTED = 3;
	public User user;
	public int reqStatus;
	public String message;
	public LoginAckMessage(User u, int reqSts, String msg)
	{
		user = u;
		reqStatus = reqSts;
		message = msg;
	}
}
