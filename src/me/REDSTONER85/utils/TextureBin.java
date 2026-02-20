package me.REDSTONER85.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class TextureBin {
	
	public static HashMap<String, BufferedImage> CHARACTER_MAP = new HashMap<>();
	
	public static BufferedImage SHARK;
	public static BufferedImage FLAG;
	public static BufferedImage ARROW_UP;
	public static BufferedImage ARROW_DOWN;
	public static BufferedImage ARROW_LEFT;
	public static BufferedImage ARROW_RIGHT;
	
	public static void init() {
		
		try {
			
			initCharacterTextures();
			
			SHARK = ImageIO.read(new File("assets/characters/shark.png"));
			FLAG = ImageIO.read(new File("assets/flag.png"));
			ARROW_UP = ImageIO.read(new File("assets/up.png"));
			ARROW_DOWN = ImageIO.read(new File("assets/down.png"));
			ARROW_LEFT = ImageIO.read(new File("assets/left.png"));
			ARROW_RIGHT = ImageIO.read(new File("assets/right.png"));
			
		} catch (IOException E) {
			
			System.out.println("Failed to retrieve textures!");
			
		}
		
	}
	
	private static void initCharacterTextures() throws IOException {
		
		File folder = new File("assets/characters");
		
		if (!folder.exists()) {
			System.out.println("Failed to retrieve character textures!");
			return;
		}
		
		File[] files = folder.listFiles();
		
		for (File f : files) {
			
			if (f.isDirectory()) continue;
			String name = f.getName();
			if (name.equals("shark.png")) continue;
			if (!name.endsWith(".png")) continue;
			if (name.length() < 5) continue;
			CHARACTER_MAP.put(name.substring(0, name.length() - 4), ImageIO.read(f));
			
		}
		
	}
	
}
