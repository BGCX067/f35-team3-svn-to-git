package edu.tamu.f35.client.logic.communication;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.net.Socket;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class Tx {
	boolean stopCapture = false;

	ByteArrayOutputStream byteArrayOutputStream;

	AudioFormat audioFormat;

	TargetDataLine targetDataLine;

	AudioInputStream audioInputStream;

	BufferedOutputStream out = null;

	BufferedInputStream in = null;

	Socket sock = null;

	SourceDataLine sourceDataLine;

	public static void main(String[] args) {
		Tx tx = new Tx();
		tx.captureAudio();

	}

	public void captureAudio() {
		try {
		
			stopCapture = false;

			//sock = new Socket("192.168.1.111", 500);
			//sock = new Socket("128.194.131.37", 500);
			//sock = new Socket("128.194.131.40", 500);
			System.out.println("Trying to establish server connection...");
			String serverIp = ClientConfiguration.getProperty("voiceip");
			sock = new Socket(serverIp, 500);
			System.out.println("Server Connection failed");
			out = new BufferedOutputStream(sock.getOutputStream());
			in = new BufferedInputStream(sock.getInputStream());

			Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
			System.out.println("Available mixers:");
			for (int cnt = 0; cnt < mixerInfo.length; cnt++) {
				System.out.println(mixerInfo[cnt].getName());
			}
			audioFormat = getAudioFormat();
			System.out.println("Audio Format");
			System.out.println(audioFormat.toString());
			DataLine.Info dataLineInfo = new DataLine.Info(
					TargetDataLine.class, audioFormat);

			Mixer mixer = AudioSystem.getMixer(mixerInfo[3]);

			targetDataLine = (TargetDataLine) mixer.getLine(dataLineInfo);
			targetDataLine.open(audioFormat);
			targetDataLine.start();

			Thread captureThread = new CaptureThread();
			captureThread.start();

			DataLine.Info dataLineInfo1 = new DataLine.Info(
					SourceDataLine.class, audioFormat);
			sourceDataLine = (SourceDataLine) AudioSystem
					.getLine(dataLineInfo1);
			sourceDataLine.open(audioFormat);
			sourceDataLine.start();
			Thread playThread = new PlayThread();
			playThread.start();

		} catch (Exception e) {
			System.out.println(e);
			//System.exit(0);
			e.printStackTrace();
		}
	}
	public void stopThreads(){
	//	sourceDataLine.close();
	//	targetDataLine.close();
	//	sourceDataLine.stop();
	//	targetDataLine.stop();
		stopCapture = true;
	}
	
	class CaptureThread extends Thread {

		byte tempBuffer[] = new byte[10000];

		public void run() {
			byteArrayOutputStream = new ByteArrayOutputStream();
			stopCapture = false;
			try {
				while (!stopCapture) {

					int cnt = targetDataLine.read(tempBuffer, 0,
							tempBuffer.length);

					out.write(tempBuffer);

					if (cnt > 0) {

						byteArrayOutputStream.write(tempBuffer, 0, cnt);

					}
				}
				byteArrayOutputStream.close();
			} catch (Exception e) {
				System.out.println(e);
				e.printStackTrace();
				//System.exit(0);
			}
		}
	}

	private AudioFormat getAudioFormat() {
		float sampleRate = 8000.0F;

		int sampleSizeInBits = 16;

		int channels = 1;

		boolean signed = true;

		boolean bigEndian = false;

		return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed,
				bigEndian);
	}

	class PlayThread extends Thread {
		byte tempBuffer[] = new byte[10000];
		public void run() {
			try {
				while (in.read(tempBuffer) != -1) {
					if(!stopCapture)
						sourceDataLine.write(tempBuffer, 0, 10000);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
