package ca.team2994.frc.autonomous;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class ParseTest {


	public static void main(String[] args) {
		new ParseTest(new File("C:\\Users\\Ryan\\Desktop\\waypointEx.txt"));
	}
	
	public ParseTest(File file) {
		try {
			//new Timer(1);
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
			//t.purge();
 
		}
		catch(Exception ex) {
			System.err.println("Error!");
			ex.printStackTrace();
		}
	}
	
	
	
	private void parseString(String s) {	
		
		String[] strArray = s.split(",");
		handleStateArray(strArray);
	}
	
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
