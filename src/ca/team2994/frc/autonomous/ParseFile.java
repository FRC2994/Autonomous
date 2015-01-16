package ca.team2994.frc.autonomous;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotDrive;

/**
 * This class is used for parsing a text file for use during autonomous mode
 * 
 * @author <a href="https://github.com/eandr127">eandr127</a>
 * @author <a href="https://github.com/JackMc">JackMc</a>
 *
 */
public class ParseFile {
	
	/**
	 * The robot drive to move the robot with
	 */
	@SuppressWarnings("unused")
	private final RobotDrive drive;
	
	/**
	 * The shaft encoders on the robot
	 */
	@SuppressWarnings("unused")
	private final Encoder[] encoders;
	

	/**
	 * Assigns {@code motors} to {@code t} then passes on to {link #start(File) start(java.io.File file)}
	 * @param file The file to be parsed
	 * @param t The Talons to be passed to {@code motors}
	 */
	public ParseFile(File file, Encoder[] e, RobotDrive drive) {
		this.drive = drive;
		encoders = e;
		start(file);
	}

	/**
	 * Opens file and parses line by line periodically, (Every second), and passing the line on to {@link #parseString(String) parseString(java.lang.String s)}
	 * @param file The to be opened and parsed
	 */
	public void start(File file) {
		
		/*try {
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
							for(Talon t : motors) {
								t.set(0);
							}	
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
			Utils.ROBOT_LOGGER.severe("Error!");
			ex.printStackTrace();
		}*/
		try {
			String line = null;
			@SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(new FileReader(file));
			while((line = br.readLine()) != null) {
				line = line.replaceAll("\\s", "");
				if(!line.startsWith("//") && !line.isEmpty()) {
					parseString(line);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Splits string by comma and passes it on to {link #handleStateArray(String[]) handleStateArray(java.lang.String[])}
	 * @param s The string to be processed 
	 */
	private void parseString(String s) {	

		String[] strArray = s.split(",");
		handleStateArray(strArray);
	}

	/**
	 * Does something with the array (designed for motor setting speeds)
	 * @param args The array of Strings to be used
	 */
	private void handleStateArray(String[] args) {
		@SuppressWarnings("unused")
		int i = 0;
		for(String s : args) {
			@SuppressWarnings("unused")
			double val = Double.parseDouble(s);
			
			
			
			i++;
		}
	}


}
