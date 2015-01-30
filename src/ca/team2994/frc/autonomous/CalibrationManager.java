package ca.team2994.frc.autonomous;

import static java.util.logging.Level.INFO;

import java.io.File;

import ca.team2994.frc.utils.ButtonEntry;
import ca.team2994.frc.utils.EJoystick;
import ca.team2994.frc.utils.Utils;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.RobotDrive;

public class CalibrationManager{
	EJoystick stick;
	RobotBase base;
	RobotDrive drive;
	
	/**
	 * The button used on the joystick when calibrating.
	 */
	private static final int CALIBRATION_BUTTON = 2;
	
	public CalibrationManager(EJoystick stick, RobotDrive drive, RobotBase base) {
		this.stick = stick;
		this.base = base;
		this.drive = drive;
	}
	
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
