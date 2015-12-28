package edu.tamu.f35.client.logic.communication;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class ClientConfiguration 
{
	final static String fileName = "client.config";
	static Properties prop;
	
	static 
	{
		prop = new Properties();
		String fileName = "client.config";
	    InputStream is = null;
		try {
			is = new FileInputStream(fileName);
		    prop.load(is);
		} catch 
		(Exception e) 
		{
			System.out.println("Configuration File is missing");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String getProperty(String propName)
	{
		return prop.getProperty(propName).toString();
	}
	
}


