package ca.team2994.frc.utils;

import static java.util.logging.Level.INFO;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.google.common.base.Predicate;
import com.google.common.base.Splitter;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotDrive;

/**
 * A Utilities class
 * 
 * @author <a href="https://github.com/eandr127">eandr127</a>
 * @author <a href="https://github.com/JackMc">JackMc</a>
 *
 */
public class Utils {
	
	/**
	 * A splitter to use when splitting strings by commas
	 */
	public static final Splitter SPLITTER = Splitter.on(',').trimResults().omitEmptyStrings();

	/**
	 * A filter to skip comments
	 */
	public static Predicate<String> skipComments = new Predicate<String>() {
		public boolean apply(String str) {
			return !str.startsWith("//");
		}
	};	
	
	/**
	 * The location of the calibration file
	 */
	public static final String CALIBRATION_OUTPUT_FILE_LOC = "/home/lvuser/calibration.txt";
	
	/**
	 * Location of Autonomous output file
	 */
	public static final String AUTONOMOUS_OUTPUT_FILE_LOC = "/home/lvuser/auto.log";
	
	/**
	 * The print stream to write to a file using {@link #writeLineToFile(String, File) writeLineToFile(java.lang.String line java.io.File file)}
	 */
	public static PrintStream stream = null;
	
	/**
	 * Name of the Logger
	 */
	public static final String ROBOT_LOGGER_NAME = "RobotLogger";
	
	/**
	 * The logger to be used
	 */
	public static final Logger ROBOT_LOGGER = Logger.getLogger(ROBOT_LOGGER_NAME);
	
	/**
	 * The path for the log
	 */
	public static final String ROBOT_LOG_FILENAME = "/home/lvuser/robot.log";
	
	/**
	 * Configures the logger
	 */
	public static void configureRobotLogger() {
		try {
			final FileHandler fh = new FileHandler(ROBOT_LOG_FILENAME, true);
			fh.setFormatter(new SimpleFormatter());
			ROBOT_LOGGER.addHandler(fh);
			
			Runtime.getRuntime().addShutdownHook(new Thread()
			{
			    @Override
			    public void run()
			    {
			    	Utils.ROBOT_LOGGER.info("Closing logger");
			    	fh.flush();
			    	fh.close();
			    }
			});
			
		} catch (SecurityException | IOException e) {
			Utils.logException(Utils.ROBOT_LOGGER, e);
		}
	}
	
	/**
	 * Writes a String to the file specified
	 * @param line The String to write to the file
	 * @param file The file to write to
	 * @return Whether the operation was successful or not
	 */
	public static boolean writeLineToFile(String line, File file) {
		
		if(stream == null) {
			try {
				stream = new PrintStream(new FileOutputStream(file, false));
			} catch (FileNotFoundException e) {
				Utils.logException(Utils.ROBOT_LOGGER, e);
				return false;	
			}
		}
		
		stream.println(line);
		
		return true;
	}
	
	/**
	 * Passes line on to {@link #writeLineToFile(String, File) writeLineToFile(java.lang.String line, java.io.File file)} using AUTONOMOUS_OUTPUT_FILE_LOC for the location parameter
	 * @param line The String to write to the file
	 * @return Whether the operation was successful or not
	 */
	public static boolean writeLineToFile(String line) {
		return writeLineToFile(line, new File(AUTONOMOUS_OUTPUT_FILE_LOC));
	}
	
	/**
	 * Drive until a distance is reached
	 * @param speed The speed to drive at
	 * @param turnMagnitude How much to turn
	 * @param aDistance How far to drive on the first motor
	 * @param bDistance How much to drive on the second motor
	 * @param encoderA The first Encoder
	 * @param encoderB the second Encoder
	 * @param drive The RobotDrive to use
	 */
	public static void driveDistance(double speed, double turnMagnitude, int aDistance, int bDistance, Encoder encoderA, Encoder encoderB, RobotDrive drive) {
		encoderA.reset();
		encoderB.reset();
		
    	while((encoderA.getDistance() < aDistance || encoderB.getDistance() < bDistance) && !drive.isSafetyEnabled()) {
    		drive.drive(speed * -1, turnMagnitude);
    	}
    	
    	drive.drive(0, 0);
	}	
	
	public static void logException(Logger log, Exception e) {
		StringWriter errors = new StringWriter();
		e.printStackTrace(new PrintWriter(errors));
		String exception = errors.toString();
		
		log.severe(exception);
	}
	
	/**
	 * Drive until a distance is reached
	 * @param speedA The speed to drive at
	 * @param speedB How much to turn
	 * @param aDistance How far to drive on the first motor
	 * @param bDistance How much to drive on the second motor
	 * @param encoderA The first Encoder
	 * @param encoderB the second Encoder
	 * @param drive The RobotDrive to use
	 */
	public static void driveStraight(double speed, double distance, Encoder encoderA, Encoder encoderB, RobotDrive drive, SimPID sim) {
		encoderA.reset();
		encoderB.reset();

		
		if(distance < 0) {
			IllegalArgumentException e = new IllegalArgumentException("Distances must be positive"); 
			logException(ROBOT_LOGGER, e);
			throw e;
		}
		
		int autoLoopCounter = 0;
		sim.calcPID((encoderA.getDistance() + encoderB.getDistance()) / 2.0);
  		while(!sim.isDone() && !drive.isSafetyEnabled()) {
  	    	if(autoLoopCounter == 0) {
  	    		sim.setDesiredValue(1.0);
  	    		encoderA.reset();
  	        	encoderB.reset();
  	    	}
  	    	
  	    	double driveVal = sim.calcPID((encoderA.getDistance() + encoderB.getDistance()) / 2.0);
  	    	double limitVal = SimPID.limitValue(driveVal, 0.25);
  	    	
  	    	drive.setLeftRightMotorOutputs(limitVal + 0.0038, limitVal);
  	    	autoLoopCounter++;
  	    	
  	    	System.out.println("isDone: " + sim.isDone() + " driveVal: " + driveVal + " limitVal: " + limitVal + " leftEncoder: " + encoderA.getDistance() + " rightEncoder: " + encoderB.getDistance());
  	    	if(autoLoopCounter % 100 == 0) {
  	    		Utils.ROBOT_LOGGER.log(INFO, ("isDone: " + sim.isDone() + " driveVal: " + driveVal + " limitVal: " + limitVal + " leftEncoder: " + encoderA.getDistance() + " rightEncoder: " + encoderB.getDistance()));
  	    	}
  		}
    	sim.resetErrorSum();
    	sim.resetPreviousVal();
    	
    	
    	encoderA.reset();
    	encoderB.reset();
    	
    	drive.setLeftRightMotorOutputs(0, 0);
	}
}
