package ca.team2994.frc.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * A Utilities class
 * 
 * @author <a href="http://github.com/eandr127">eandr127</a>
 * @author <a href="http://github.com/JackMc">JackMc</a>
 *
 */
public class Utils {
	
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
			// TODO Auto-generated catch block
			e.printStackTrace();
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
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;	
			}
		}
		
		stream.println(line);
		
		return true;
	}
	
	/**
	 * Passes line on to {link #writeLineToFile(String, File) writeLineToFile(java.lang.String line, java.io.File file)} using AUTONOMOUS_OUTPUT_FILE_LOC for the location parameter
	 * @param line The String to write to the file
	 * @return Whether the operation was successful or not
	 */
	public static boolean writeLineToFile(String line) {
		return writeLineToFile(line, new File(AUTONOMOUS_OUTPUT_FILE_LOC));
	}
}
