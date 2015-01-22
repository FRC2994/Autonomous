package ca.team2994.frc.autonomous;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Files;

import ca.team2994.frc.utils.Utils;
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
	private final RobotDrive drive;
	
	/**
	 * The shaft encoders on the robot
	 */
	private final Encoder[] encoders;
	

	/**
	 * Assigns {@code e} to {@code encoders} then passes on to {@link #start(File) start(java.io.File file)}
	 * @param file The file to be parsed
	 * @param e The Encoders to use
	 * @param drive The RobotDrive to drive with
	 */
	public ParseFile(File file, Encoder[] e, RobotDrive drive) {
		this.drive = drive;
		encoders = e;
		start(file);
	}

	/**
	 * Opens file and parses it, and splits it by line, then passes it on to {@link #handleStateArray(String[]) handleStateArray(java.lang.String[] args)}
	 * @param file The to be opened and parsed
	 */
	public void start(File file) {
		try {
			List<String> guavaResult = Files.readLines(file, Charsets.UTF_8);
			for(int i = 0; i < guavaResult.size(); i++) {
				handleStateArray((String[]) Lists.newArrayList(Utils.SPLITTER.split(guavaResult.get(i))).toArray());
			}
		} catch (IOException e) {
			Utils.logException(Utils.ROBOT_LOGGER, e);
		}
	}

	/**
	 * Does something with the array (designed for motor setting speeds)
	 * @param args The array of Strings to be used
	 */
	private void handleStateArray(String[] args) {
		double encoderADistance = 0;
		double encoderBDistance = 0;
		double speedA = 0;
		double speedB = 0;
		
		int i = 0;
		for(String s : args) {
			double val = Double.parseDouble(s);
			
			switch(i) {
				case 0:
					encoderADistance = val;
					break;
				case 1:
					encoderBDistance = val;
					break;
				case 2:
					speedA = val;
					break;
				case 3:
					speedB = val;
					break;
				default:
					Utils.ROBOT_LOGGER.severe("Error!");
					break;
			}
			i++;
		}
		
		Utils.setLeftRightMotorOutputsDistance(speedA, speedB, encoderADistance, encoderBDistance,
				encoders[0], encoders[1], drive);
	}


}
