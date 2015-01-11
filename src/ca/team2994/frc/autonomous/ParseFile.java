package ca.team2994.frc.autonomous;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This class is used for parsing a text file for use during autonomous mode
 * 
 * @author eandr127
 * @author JackMc
 *
 */
public class ParseFile {

	/**
	 * See {@link #start(File) ParseFile.start(java.io.File file)}
	 * @param file File to be passed to {@link #start(File) start(java.io.File file)}
	 */
	public ParseFile(File file) {
		start(file);
	}
	
	/**
	 * Opens file and parses line by line periodically, (Every second), and passing the line on to {@link #parseString(String) parseString(java.lang.String s)}
	 * @param file The to be opened and parsed
	 */
	public void start(File file) {
		try {
			@SuppressWarnings("resource")
			final BufferedReader br = new BufferedReader(new FileReader(file));
			final Timer t = new Timer();
			t.schedule(new TimerTask() {

				@Override
				public void run() {
					try {
						String line = br.readLine();
						
						if(line == null) {
							br.close();
							t.cancel();
							t.purge();
							return;
						}
						
						line = line.replaceAll("\\s", "");
						if(!line.startsWith("//") && !line.isEmpty()) {
							parseString(line);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}
				
			}, 0, 1000);
 
		}
		catch(Exception ex) {
			System.err.println("Error!");
			ex.printStackTrace();
		}
	}
	
	/**
	 * An empty constructor
	 */
	public ParseFile() {
		
	}

	/*
	 * Splits string by comma and passes it on to {link #handleStateArray(String[]) handleStateArray(java.lang.String[])}
	 * @param s The string to be processed 
	 */
	private void parseString(String s) {	
		
		String[] strArray = s.split(",");
		handleStateArray(strArray);
	}
	
	/*
	 * Does something with the array (designed for motor setting speeds)
	 * @param args The array of Strings to be used
	 */
	private void handleStateArray(String[] args) {
		
		System.err.println("\n================================");
		
		int i = 0;
		for(String s : args) {
		double val = 0.0;
			switch(i++) {
				case 0:
					val = Double.parseDouble(s);
					System.err.println("Motor " + i + ": " + val);
					break;
				case 1:
					val = Double.parseDouble(s);
					System.err.println("Motor " + i + ": " + val);
					break;
				case 2:
					val = Double.parseDouble(s);
					System.err.println("Motor " + i + ": " + val);
					break;
				case 3:
					val = Double.parseDouble(s);
					System.err.println("Motor " + i + ": " + val);
					break;
				default:
					val = Double.parseDouble(s);
					System.err.println("Error");
					break;
			}
		}
		System.err.println("================================\n");
	}
	
	
}
