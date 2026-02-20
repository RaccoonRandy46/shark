package me.REDSTONER85.utils;

import java.awt.Color;

public class Utils {

	  public static float[] RGBtoHSV(float r, float g, float b) {
		  
		  float[] hsvDecimal = Color.RGBtoHSB((int)r, (int)g, (int)b, null);
			
		  float[] hsv = {hsvDecimal[0]*360, hsvDecimal[1]*100, hsvDecimal[2]*100};
		  
		  return hsv;
		  
	  }
	  
	  public static float[] HSVtoRGB(float h, float s, float v) {
	      
		  h /= 360;
		  s /= 100;
		  v /= 100;
		  
		  Color rgbColor = new Color(Color.HSBtoRGB(h, s, v));
		  
		  float[] rgb = {(float)rgbColor.getRed(), (float)rgbColor.getGreen(), (float)rgbColor.getBlue()};
			
		  return rgb;

	  }
	
}
