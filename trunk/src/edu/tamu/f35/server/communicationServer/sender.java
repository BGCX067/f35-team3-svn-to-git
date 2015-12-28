package edu.tamu.f35.server.communicationServer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

class StreamData
{
	public static ArrayList<BufferedInputStream> dataInputs = new ArrayList<BufferedInputStream>();
	public static ArrayList<BufferedOutputStream> dataOutputs = new ArrayList<BufferedOutputStream>();
	
	
	private static StreamData instance = null;
	protected StreamData()
	{
	}
	
	synchronized
	public void addBufI(BufferedInputStream bs)
	{
		dataInputs.add(bs);
	}
	
	synchronized
	public void addBufO(BufferedOutputStream bs)
	{
		dataOutputs.add(bs);
	}
	
	public static StreamData getInstance()
	{
		if(instance == null)
			instance = new StreamData();
		return instance;
	}
}

class VoiceThreadCommunication implements Runnable
{
	private Socket clientSocket;
	BufferedOutputStream out;
	BufferedInputStream input;
	static SourceDataLine sdLine;
	Thread t;
	byte tempBuffer[] = new byte[10000];
	public VoiceThreadCommunication(Socket cs, SourceDataLine sd)
	{
		clientSocket = cs;
		try
		{
			StreamData sdi = StreamData.getInstance();
			input = new BufferedInputStream(clientSocket.getInputStream());	
			out=new BufferedOutputStream(clientSocket.getOutputStream());
			sdi.addBufI(input);
			sdi.addBufO(out);
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
			//System.out.println("Exception 2");
		}
		sdLine = sd; 
		t = new Thread(this);
		t.start();
	}

	public void run() 
	{
		// TODO Auto-generated method stub
		try
		{	
			while(input.read(tempBuffer)!=-1)
			{			
				sdLine.write(tempBuffer,0,10000);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			//System.out.println("Exception 1");
		}
		
	}

	
}


public class sender {
	ServerSocket MyService;
	Socket clientSocket = null;
	BufferedInputStream input;
	TargetDataLine targetDataLine;
    static int i = 0;
	BufferedOutputStream out;
	  ByteArrayOutputStream byteArrayOutputStream;
	  AudioFormat audioFormat;	
	
	  SourceDataLine sourceDataLine;	  
	 byte tempBuffer[] = new byte[10000];
	
	 sender() throws LineUnavailableException{   
    	try {
    		audioFormat = getAudioFormat();
    		DataLine.Info dataLineInfo =  new DataLine.Info( SourceDataLine.class,audioFormat);
    		sourceDataLine = (SourceDataLine)
    	    AudioSystem.getLine(dataLineInfo);
    	    sourceDataLine.open(audioFormat);
    	    sourceDataLine.start();
    	    
			MyService = new ServerSocket(500);
			
			while(true)
			{
				ServerDisplay.getInstance().DisplayOutput("Waiting for Client");
				//System.out.println("Waiting for Client");
				clientSocket = MyService.accept();
				ServerDisplay.getInstance().DisplayOutput("Client Connected");
				
				if(i==0)
					{captureAudio(); i++;}
				new VoiceThreadCommunication(clientSocket, sourceDataLine);
			}

		} catch (IOException e) {
			
			e.printStackTrace();
		}
       
	}
	 private AudioFormat getAudioFormat(){
		    float sampleRate = 8000.0F;		  
		    int sampleSizeInBits = 16;		   
		    int channels = 1;		    
		    boolean signed = true;		    
		    boolean bigEndian = false;		 
		    return new AudioFormat(
		                      sampleRate,
		                      sampleSizeInBits,
		                      channels,
		                      signed,
		                      bigEndian);
		  }
	public static void main(String s[]) throws LineUnavailableException{
		 sender s2=new sender();
	}
	
	
	private void captureAudio() {
		try {
			
			Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
			System.out.println("Available mixers:");
			for (int cnt = 0; cnt < mixerInfo.length; cnt++) {
				System.out.println(mixerInfo[cnt].getName());
			}
			audioFormat = getAudioFormat();

			DataLine.Info dataLineInfo = new DataLine.Info(
					TargetDataLine.class, audioFormat);

			Mixer mixer = AudioSystem.getMixer(mixerInfo[3]);

			targetDataLine = (TargetDataLine) mixer.getLine(dataLineInfo);

			targetDataLine.open(audioFormat);
			targetDataLine.start();

			Thread captureThread = new CaptureThread();
			captureThread.start();		
		} catch (Exception e) {
			e.printStackTrace();
			//System.exit(0);
		}
	}
	
	class CaptureThread extends Thread {

		byte tempBuffer[] = new byte[10000];
		ArrayList<BufferedOutputStream> outThreads;
		public void run() {			
			try {
				while (true) {
					int cnt = targetDataLine.read(tempBuffer, 0,
							tempBuffer.length);
					outThreads = StreamData.getInstance().dataOutputs;
					for(BufferedOutputStream bo : outThreads)
						bo.write(tempBuffer);	
					//outThreads.get(0).write(tempBuffer);
				}
				
			} catch (Exception e) {
				
				e.printStackTrace();
				//System.exit(0);
			}
		}
	}

}
