package me.REDSTONER85.utils.settings;

import java.io.File;
import java.util.HashMap;

import me.REDSTONER85.utils.FileManager;

public class Settings {

	public static final int NP_ALLOW_DASHES = 0;
	public static final int NP_ALLOW_IMPOSSIBLE = 1;
	public static HashMap<Integer, Boolean> npMap = new HashMap<>();
	private static HashMap<Integer, String> npDescriptionMap = new HashMap<>();
	
	public static int highScore = 1;
	public static int maxFrameRate = 240;
	
	public static void updateAll() {
		
		if (new File("cache/pb.shark").exists()) {
			try {
				highScore = Integer.parseInt(FileManager.readLine("cache/pb.shark"));
			} catch (Exception E) {}
		}
		
		if (new File("cache/fps.shark").exists()) {
			try {
				maxFrameRate = Integer.parseInt(FileManager.readLine("cache/fps.shark"));
			} catch (Exception E) {}
		}
		
		for (int i = 0; i < Keybinds.keybinds.size(); i++) {
			
			String name = Keybinds.getKeybindName(i);
			if (!(new File("cache/keybinds/" + name + ".shark").exists())) continue;
			
			try {
				Keybinds.setKeybind(i, Integer.parseInt(FileManager.readLine("cache/keybinds/" + name + ".shark")));
			} catch (Exception E) {}
			
		}
		
		if (new File("cache/character.shark").exists()) {
			try {
				String name = FileManager.readLine("cache/character.shark");
				for (SCharacter sc : SCharacter.characters) {
					if (!sc.getName().equals(name)) continue;
					SCharacter.currentCharacter = sc;
					break;
				}
			} catch (Exception E) {}
		}
		
	}
	
	public static boolean getNPSetting(int setting) {
		if (!npMap.containsKey(setting)) return false;
		return npMap.get(setting);
	}
	
	public static void setNPSetting(int setting, boolean value) {
		npMap.put(setting, value);
	}
	
	public static String getNPSettingDescription(int setting) {
		if (!npMap.containsKey(setting)) return "???";
		return npDescriptionMap.get(setting);
	}
	
	private static void initNPSetting(int setting, boolean defaultValue, String description) {
		setNPSetting(setting, defaultValue);
		npDescriptionMap.put(setting, description);
	}
	
	static {
		initNPSetting(NP_ALLOW_DASHES, true, "Enable Dashes");
		initNPSetting(NP_ALLOW_IMPOSSIBLE, true, "Allow Impossible Levels");
	}
	
}
