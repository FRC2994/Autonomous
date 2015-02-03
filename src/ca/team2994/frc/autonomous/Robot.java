
package ca.team2994.frc.autonomous;


import static java.util.logging.Level.INFO;


import ca.team2994.frc.utils.EJoystick;
import ca.team2994.frc.utils.SimGyro;
import ca.team2994.frc.utils.Utils;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;

/**
 * The entry class for the robot, handling autonomous, tele-op, and test mode.
 * This class is built off the default Robot class
 * 
 * @author <a href="https://github.com/eandr127">eandr127</a>
 * @author <a href="https://github.com/JackMc">JackMc</a>
 * 
 */
public class Robot extends SampleRobot {
	
	/**
	 * 
	 */
	int autoLoopCounter;
	
	/**
	 * Whether to use regular tele-op or to log waypoints
	 */
	private static final boolean SAVE_WAYPOINTS = true;
	
	
	/**
	 * 
	 */
	private EJoystick stick;
	
	/**
	 * 
	 */
	public static DriveManager driveManager;
	// TODO: Implement mode-switching functionality
	
	/**
	 * 
	 */
	AutoMode currentMode;
	
	/**
	 * 
	 */
	private SimGyro gyro;
	
    /**
     * This is the code first run when the robot code is started
     */
	public void robotInit() {
		Utils.configureRobotLogger();
		Utils.ROBOT_LOGGER.log(INFO, "Robo-Init");
		
		stick = new EJoystick(0);
		
		driveManager = new DriveManagerImpl(new RobotDrive(0, 1), this, gyro,
				new Encoder(0, 1, true), new Encoder(2, 3, true), stick);
	}

	/**
	 * Initialize gyro before robot code says that it is ready
	 */
	public Robot() {
		gyro = new SimGyro(new AnalogInput(1), 0);
		gyro.initGyro();
	}
	
    /**
     * Initialize PID values for the gyro, then call {@link DriveManager#runAutonomous() runAutonomous()}
     */
    public void autonomous() {
    	driveManager.readPIDValues();
    	driveManager.runAutonomous();
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
    		driveManager.runTeleOPLogging();
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
