package me.REDSTONER85.utils;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioManager {
	
	private static Clip sound;
	
	private static float masterVolume = 1;
	
	public static long lastRepeat = 0;
	
	public static void playSound(String name) {
		
		stopMusic();
		
		try {
			
			sound = AudioSystem.getClip();
			sound.open(AudioSystem.getAudioInputStream(new File("assets/audio/" + name + ".wav")));
			((FloatControl)sound.getControl(FloatControl.Type.MASTER_GAIN)).setValue(masterVolume);
			sound.start();
			
		}
		catch (UnsupportedAudioFileException e1) {System.out.println(e1.getMessage());}
		catch (IOException e2) {System.out.println(e2.getMessage());} 
		catch (LineUnavailableException e3) {System.out.println(e3.getMessage());}
		
	}
	
	private static Timer t = null;
	public static void startSongLoop() {
		
		if (t != null) t.cancel();
		
		t = new Timer();
		
		t.schedule(new TimerTask() {
			public void run() {
				
				playSound("skeletrix");
				lastRepeat = System.currentTimeMillis();
				
			}
		}, 10, 135000);
		
	}
	
	public static void stopMusic() {
		if (sound != null) sound.stop();
	}
	
	public static void stopSongLoop() {
		stopMusic();
		if (t == null) return;
		t.cancel();
		t = null;
	}
	
	public static void toggleMusic() {
		if (t == null) startSongLoop();
		else stopSongLoop();
	}
	
	public static void setMasterVolume(float f) {
		((FloatControl)sound.getControl(FloatControl.Type.MASTER_GAIN)).setValue(f);
		masterVolume = f;
	}
	
}
