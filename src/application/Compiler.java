package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Scanner;

public class Compiler{

	static LinkedHashMap<Character, String> rules = new LinkedHashMap<Character, String>();
	static final char[] grammarNonTerminal = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
	static final char[] grammarTerminal = "abcdefghijklmnopqrstuvwxyz->%:!|,#$()*+/ε.".toCharArray();
	static boolean deleteEpsilon = false;
	public static String language = "";
	static String version = "";
	static String revDate = "";
	static boolean bootFound = true;
	
	private static void bootConfig () {
		InputStream streamIn = Compiler.class.getResourceAsStream("/data/boot.config");
        Scanner scanner = new Scanner(streamIn);
        scanner.useDelimiter("\\r?\\n");
		while (scanner.hasNextLine()) {
			String inputLine = scanner.next();
			if(inputLine.contains("#LANGUAGE")) {
				String bootLangFile = "./lang.info";
				try {
					FileInputStream file = new FileInputStream(bootLangFile);
					InputStreamReader inputReader = new InputStreamReader(file, StandardCharsets.UTF_8);
	                Scanner langScanner = new Scanner(inputReader);
	                scanner.useDelimiter("\\r?\\n");
	                inputLine = langScanner.next();
	                langScanner.close();
				} catch (FileNotFoundException e) {
					bootFound = false;
				}
				inputLine = inputLine.substring(inputLine.indexOf("=") + 1);
	    		language = inputLine;
	    	}
			else if (inputLine.contains("#VERSION")) {
				inputLine = inputLine.substring(inputLine.indexOf("=") + 1);
				version = inputLine;
			}
			else if (inputLine.contains("#REVDATE")) {
				inputLine = inputLine.substring(inputLine.indexOf("=") + 1);
				revDate = inputLine;
			}
		}
		scanner.close();
	}
	  
	public static void main(String[] args) {
		bootConfig();
		//Graphics.startGraphics(args);
		GraphicsFXML.startGraphics(args);
        //System.out.println(javafx.scene.text.Font.getFamilies());
		//System.out.println(com.sun.javafx.runtime.VersionInfo.getRuntimeVersion());
	}
}
