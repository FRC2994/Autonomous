package ca.team2994.frc.utils;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Utils {
	public static final String ROBOT_LOGGER_NAME = "RobotLogger";
	public static final Logger ROBOT_LOGGER = Logger.getLogger(ROBOT_LOGGER_NAME);
	public static final String ROBOT_LOG_FILENAME = "/home/lvuser/robot.log";
	
	public static void configureRobotLogger() {
		try {
			FileHandler fh = new FileHandler(ROBOT_LOG_FILENAME, true);
			fh.setFormatter(new SimpleFormatter());
			ROBOT_LOGGER.addHandler(fh);
		} catch (SecurityException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
