
package ca.team2994.frc.autonomous;


import static java.util.logging.Level.INFO;

import java.io.File;
import java.io.IOException;
import java.util.List;

import ca.team2994.frc.utils.ButtonEntry;
import ca.team2994.frc.utils.EJoystick;
import ca.team2994.frc.utils.SimGyro;
import ca.team2994.frc.utils.SimLib;
import ca.team2994.frc.utils.SimPID;
import ca.team2994.frc.utils.Utils;

import com.google.common.base.Charsets;
import com.google.common.collect.Iterables;
import com.google.common.io.Files;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;

/**
 * The entry class for the robot, handling autonomous, tele-op, and test mode.
 * This class is built off the default Robot class
 * 
 * @author <a href="https://github.com/eandr127">eandr127</a>
 * @author <a href="https://github.com/JackMc">JackMc</a>
 */
public class Robot extends SampleRobot {
	int autoLoopCounter;
	
	/**
	 * Whether to use regular tele-op or to log waypoints
	 */
	private static final boolean SAVE_WAYPOINTS = true;
	
	/**
	 * Whether to run expirimental PID code or the working autonomous
	 */
	private static final boolean USE_PID = true;
	
	private EJoystick stick;
	
	DriveManager driveManager;
	// TODO: Implement mode-switching functionality
	AutoMode currentMode;
	
	
	
    /**
     * This is the code first run when the robot code is started
     */
	public void robotInit() {
		Utils.configureRobotLogger();
		Utils.ROBOT_LOGGER.log(INFO, "Robo-Init");
		
		stick = new EJoystick(0);
		
		driveManager = new DriveManagerImpl(new RobotDrive(0, 1), this, new SimGyro(new AnalogInput(1), 0),
				new Encoder(0, 1, true), new Encoder(2, 3, true), stick);
		
		try {
			currentMode = new AutoMode("Drive straight and turn", "straightturn.waypoints", driveManager);
		} catch (IOException e) {
			Utils.logException(Utils.ROBOT_LOGGER, e);
			e.printStackTrace();
		}
	}

	public Robot() {
	}
	
    /**
     * Either run preprogrammed file or call {@link #PIDTest() PIDTest()}
     */
    public void autonomous() {
        driveManager.driveTurn(90);
    }
    
    /**
     * Runs the motors with arcade steering or log waypoints (TODO).
     */
    public void operatorControl() {
    	if(!SAVE_WAYPOINTS) {
    		Utils.ROBOT_LOGGER.log(INFO, "Tele-Op");
    		while (isOperatorControl() && isEnabled()) {
    			driveManager.arcadeDrive();
    			Timer.delay(0.005);		// wait for a motor update time
    		}
    	}
    	else {
    		
    	}
    }

    
    /**
     * Calibration for encoders
     * TODO: Tell user to drive 5 feet
     */
    public void test() {
    	driveManager.runCalibration();
    }
    
    
}
