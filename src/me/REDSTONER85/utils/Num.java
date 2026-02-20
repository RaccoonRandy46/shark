package me.REDSTONER85.utils;

public class Num {

	public static double ran(double min, double max) {
		return Math.floor(Math.random() * (max - min + 1) + min);
	}
	
}
