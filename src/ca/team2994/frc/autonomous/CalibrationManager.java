package ca.team2994.frc.autonomous;

import static java.util.logging.Level.INFO;

import java.io.File;

import ca.team2994.frc.utils.ButtonEntry;
import ca.team2994.frc.utils.EJoystick;
import ca.team2994.frc.utils.Utils;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.RobotDrive;

/**
 * Manages the calibration of the left and right encoders
 * 
 * @author <a href="https://github.com/eandr127">eandr127</a>
 * @author <a href="https://github.com/JackMc">JackMc</a>
 *
 */
public class CalibrationManager {
	
	/**
	 * The joystick to use
	 */
	EJoystick stick;
	
	/**
	 * The RobotBase for the robot
	 */
	RobotBase base;
	
	/**
	 * Robot - used to bail out if disable
	 */
	RobotDrive drive;
	
	/**
	 * The button used on the joystick when calibrating.
	 */
	private static final int CALIBRATION_BUTTON = 2;
	
	/**
	 * Initialize values for object
	 * @param stick The joystick to use
	 * @param drive The RobotDrive to drive with
	 * @param base Robot - used to bail out if disable
	 */
	public CalibrationManager(EJoystick stick, RobotDrive drive, RobotBase base) {
		this.stick = stick;
		this.base = base;
		this.drive = drive;
	}
	
	
	/**
	 * Calibrates the encoders for using Encoder.getDistance()
	 * @param left The left encoder to use
	 * @param right The right encoder to use
	 * @param stick The joystick to use
	 */
	public void calibrateEncoders(Encoder left, Encoder right, EJoystick stick) {
		Utils.ROBOT_LOGGER.log(INFO, "Calibration");
    	left.reset();
    	right.reset();
    	
    	stick.enableButton(CALIBRATION_BUTTON);
    	
    	int i = 0;
    	while(i != ButtonEntry.EVENT_CLOSED) {
    		stick.update();
    		if(!base.isTest()) {
    			return;
    		}
    		drive.arcadeDrive(-stick.getY(), -stick.getX()); // drive with arcade style (use right stick) (inverted) // drive with arcade style (use right stick)
    		i = stick.getEvent(2);
    	}
    	
    	drive.drive(0, 0);
    	
    	int encoderAValue = left.get();
    	int encoderBValue = right.get();
    	
    	double encoderAConstant = 0;
    	double encoderBConstant = 0;
    	
    	if(encoderAValue != 0) {
    		encoderAConstant = 5.0 / encoderAValue;
    	}
    	
    	if(encoderBValue != 0) {
    		encoderBConstant = 5.0 / encoderBValue;
    	}
    	
		Utils.writeLineToFile("//Encoder A (Left), Distance Travelled: 5ft, Number of encoder ticks: " + encoderAValue
    			+ ", Calibration constant: " + encoderAConstant, 
    			new File(Utils.CALIBRATION_OUTPUT_FILE_LOC));
    	
    	Utils.writeLineToFile("//Encoder B (Right), Distance Travelled: 5ft, Number of encoder ticks: " + encoderBValue
    			+ ", Calibration constant: " + encoderBConstant, 
    			new File(Utils.CALIBRATION_OUTPUT_FILE_LOC));
    	
    	Utils.writeLineToFile(encoderAConstant + ", " + encoderBConstant, new File(Utils.CALIBRATION_OUTPUT_FILE_LOC));
	}

}
