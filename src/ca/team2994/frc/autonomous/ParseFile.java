package ca.team2994.frc.autonomous;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.google.common.base.Charsets;
import com.google.common.collect.Iterables;
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
	@SuppressWarnings("unused")
	private final RobotDrive drive;
	
	/**
	 * The shaft encoders on the robot
	 */
	@SuppressWarnings("unused")
	private final Encoder[] encoders;
	

	/**
	 * Assigns {@code e} to {@code encoders} then passes on to {@link #start(File) start(java.io.File file)} Note: Currently does not work
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
			Iterable<String> guavaResultFiltered = Iterables.filter(guavaResult, Utils.skipComments);
			
			for (String line: guavaResultFiltered) {
				handleStateArray(Iterables.toArray(Utils.SPLITTER.split(line), String.class));
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
		
		double[] doubleVals = new double[args.length]; //Initialize to size of args
		
		int i = 0; //counter to convert array
		for(String s : args) {
			double val = Double.parseDouble(s); //Convert s to double
			doubleVals[i] = val; 				//Add it to array
			i++;
		}
		HandleParseValues handleParseValues = new HandleParseValuesImpl(); 	//Make new object to call method from 
		handleParseValues.HandleAutonValues(doubleVals);					//Pass on values
	}


}
