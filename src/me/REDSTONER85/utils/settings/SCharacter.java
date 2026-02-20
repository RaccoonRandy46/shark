package me.REDSTONER85.utils.settings;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import me.REDSTONER85.utils.TextureBin;

public class SCharacter {

	public static List<SCharacter> characters = new ArrayList<>();
	public static SCharacter currentCharacter;
	
	private static int global_id = 0;
	
	private String name;
	private BufferedImage ig;
	private int id;
	
	public SCharacter(String nameIn, BufferedImage igIn) {
		this.name = nameIn;
		this.ig = igIn;
		this.id = global_id++;
	}
	
	public String getName() {
		return this.name;
	}
	
	public BufferedImage getTexture() {
		return this.ig;
	}
	
	public int getID() {
		return this.id;
	}
	
	private static void initCharacter(String name, BufferedImage ig) {
		characters.add(new SCharacter(name, ig));
	}
	
	static {
		
		initCharacter("shark", TextureBin.SHARK);
		
		Set<String> keys = TextureBin.CHARACTER_MAP.keySet();
		for (String key : keys) initCharacter(key, TextureBin.CHARACTER_MAP.get(key));
		
		currentCharacter = characters.get(0);
		
	}
	
}
