package ca.team2994.frc.autonomous;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import ca.team2994.frc.utils.Utils;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;

/**
 * This class is used for parsing a text file for use during autonomous mode
 * 
 * @author <a href="https://github.com/eandr127">eandr127</a>
 * @author <a href="https://github.com/JackMc">JackMc</a>
 *
 */
public class ParseFile {
	
	/**
	 * The talon motors on the robot
	 */
	private final Talon[] motors;
	
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
	public ParseFile(File file, Talon[] t, Encoder[] e) {
		encoders = e;
		motors = t;
		start(file);
	}

	/**
	 * Opens file and parses line by line periodically, (Every second), and passing the line on to {@link #parseString(String) parseString(java.lang.String s)}
	 * @param file The to be opened and parsed
	 */
	public void start(File file) {
		if(motors == null)
			return;
		
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

		Utils.ROBOT_LOGGER.info("\n================================");

		int i = 0;
		for(String s : args) {
			double val = Double.parseDouble(s);
			motors[i].set(val);
			Utils.ROBOT_LOGGER.info("Motor " + i + ": " + val);
			i++;
		}
		Utils.ROBOT_LOGGER.info("================================\n");
	}


}
