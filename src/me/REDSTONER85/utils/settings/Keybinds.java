package me.REDSTONER85.utils.settings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Keybinds {

	public static List<Object[]> keybinds = new ArrayList<>();
	private static HashMap<String, Integer> keybindNameMap = new HashMap<>();
	
	public static int getKeybind(int id) {
		return (int)keybinds.get(id)[1];
	}
	
	public static int getKeybind(String name) {
		return (int)keybinds.get(keybindNameMap.get(name))[1];
	}
	
	public static String getKeybindName(int id) {
		return (String)keybinds.get(id)[0];
	}
	
	public static void setKeybind(int id, int key) {
		keybinds.set(id, new Object[] {getKeybindName(id), key});
	}
	
	private static void initKeybind(String name, int defaultKey) {
		int id = keybinds.size();
		keybinds.add(new Object[] {name, defaultKey});
		keybindNameMap.put(name, id);
	}
	
	static {
		
		initKeybind("up", 87);
		initKeybind("down", 83);
		initKeybind("left", 65);
		initKeybind("right", 68);
		initKeybind("dash", 16);
		initKeybind("pass", 88);
		initKeybind("see solution", 69);
		initKeybind("mute", 77);
		
	}
	
}
