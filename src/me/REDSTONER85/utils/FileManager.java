package me.REDSTONER85.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
	
	public static File getFile(String s) throws IOException {
		
		File file = new File(s);
		
        if (!file.exists()) throw new IOException("File not found: \"" + s + "\"");
        
        return file;
		
	}
	
	public static void write(String filename, String s) throws IOException {
		
		PrintWriter w = new PrintWriter(filename);
		
		w.print(s);
		
		w.close();
		
	}
	
	public static String readLine(String s) throws IOException {
		
		BufferedReader br = new BufferedReader(new FileReader(FileManager.getFile(s)));
		
		String st = br.readLine();
		
		br.close();
		
		return st;
		
	}
	
	public static String read(String s) throws IOException {
		
		StringBuilder out = new StringBuilder();
		
		BufferedReader br = new BufferedReader(new FileReader(FileManager.getFile(s)));
		
		String line = br.readLine();
		
		while (line != null) {
			out.append(out.length() == 0 ? line : ("\n" + line));
			line = br.readLine();
		}
		
		br.close();
		
		return out.toString();
		
	}
	
	public static List<String> readAllLines(String fileLocation) throws IOException {
		
		List<String> out = new ArrayList<>();
		
		BufferedReader br = new BufferedReader(new FileReader(FileManager.getFile(fileLocation)));
		
		String line = br.readLine();
		
		while (line != null) {
			
			if (!line.isEmpty()) out.add(line);
			line = br.readLine();
			
		}
		
		br.close();
		
		return out;
		
	}
	
	public static void writeFile(String folderPath, String fileName, Object value) {
		
		try {
		
			if (folderPath == null || fileName == null) return;
			
			if (!folderPath.equals("")) folderPath += "/";
			String absolutePath = "cache/" + folderPath;
			
			Path folderpath = Paths.get(absolutePath);
			File folder = new File(absolutePath);
			
			if (!folder.exists()) Files.createDirectories(folderpath);
			
			FileManager.write(absolutePath + fileName + ".shark", value + "");
		
		} catch (Exception E) {}
		
	}
	
}
