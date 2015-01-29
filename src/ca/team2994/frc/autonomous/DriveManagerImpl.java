package ca.team2994.frc.autonomous;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.google.common.base.Charsets;
import com.google.common.collect.Iterables;
import com.google.common.io.Files;

import ca.team2994.frc.utils.EJoystick;
import ca.team2994.frc.utils.SimGyro;
import ca.team2994.frc.utils.SimLib;
import ca.team2994.frc.utils.SimPID;
import ca.team2994.frc.utils.Utils;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.RobotDrive;

/**
 * Manages the driving part of autonomous.
 */
public class DriveManagerImpl implements DriveManager {
	// Robot - used to bail out if disable
	private RobotBase robot;

	// PID
	private SimPID encoderPID;
	private SimPID gyroPID;
	
	// Sensors
	private SimGyro gyro;
	private Encoder rightEncoder;
	private Encoder leftEncoder;
	
	// User input
	private EJoystick stick;

	// Output
	private RobotDrive drive;
	
	private CalibrationManager calibration;
	
	/**
	 * Initializes a DriveManagerImpl. This class is an implementation of the interface DriveManager and 
	 * can be used to drive the robot via turning or driving straight. If a certain piece of hardware is
	 * unavailable, the correct action is to pass in its associated object as null, and an exception will
	 * be thrown if the associated functionality is requested. 
	 * 
	 * In the case of encoders, passing in one as null will cause the other to be used as the sole encoder.
	 * Passing in both as null will indicate that there is no encoder functionality on the robot (i.e. the 
	 * robot cannot drive straight reliably).
	 * 
	 * @param drive The RobotDrive object associated with the drive train.
	 * @param base An object representing the running robot code and its state. Used to check if movement loops should bail out.
	 * @param gyro The gryoscope on the robot used for turning.
	 * @param leftEncoder The encoder on the "left" side of the robot. Passing this in as null will cause the other 
	 * 						(right) encoder to be assumed as the only one.
	 * @param rightEncoder The encoder on the "right" side of the robot. Passing this in as null will cause the other 
	 * 						(left) encoder to be assumed as the only one.
	 */
	public DriveManagerImpl(RobotDrive drive, RobotBase base, 
							SimGyro gyro, Encoder leftEncoder, 
							Encoder rightEncoder, EJoystick stick) {
		this.drive = drive;
		this.robot = base;
		this.gyro = gyro;
		this.leftEncoder = leftEncoder;
		this.rightEncoder = rightEncoder;
		this.stick = stick;
		
		// Initialize the gyro (takes 1.0 seconds cause of a wait in the code,
		// can we fix this?)
		gyro.initGyro();
		
		// Read encoder values from a file.
		readEncoderValues();
		
		// TODO: Read these in from the Constants file or the SmartDashboard
		// Rationale: The D stops it from thrasing, P is taken from Simbotics
		this.gyroPID = new SimPID(2.16, 0.0, 0.1, 1.0);
		// Rationale: P is taken from Simbotics.
		this.encoderPID = new SimPID(2.16, 0.0, 0.0, 0.1);
		
		// Initialize the Calibration instance
		this.calibration = new CalibrationManager(stick, drive, base);
	}
	
	private void readEncoderValues() {
		try {

			List<String> guavaResult = Files.readLines(new File(Utils.CALIBRATION_OUTPUT_FILE_LOC), Charsets.UTF_8);
			// Filter to only get those with one digit  *** Still No Copying Done! ***
			Iterable<String> guavaResultFiltered = Iterables.filter(guavaResult, Utils.skipComments);

			String[] s = Iterables.toArray(Utils.SPLITTER.split(guavaResultFiltered.iterator().next()), String.class);


			double encoderAConst = Double.parseDouble(s[0]);
			double encoderBConst = Double.parseDouble(s[1]);

			leftEncoder.setDistancePerPulse(encoderAConst);
			rightEncoder.setDistancePerPulse(encoderBConst);

		} catch (IOException e) {
			Utils.logException(Utils.ROBOT_LOGGER, e);
			leftEncoder.setDistancePerPulse(1);
			rightEncoder.setDistancePerPulse(1);
		}
	}
	
	/* (non-Javadoc)
	 * @see ca.team2994.frc.autonomous.DriveManager#driveStraight(int)
	 */
	@Override
	public void driveStraight(int units) {
		// Reset the encoders (encoder.get(Distance|)() == 0)
		leftEncoder.reset();
		rightEncoder.reset();
		// Set up the desired number of units.
		encoderPID.setDesiredValue(units);
		// Reset the encoder PID to a reasonable state.
		encoderPID.resetErrorSum();
    	encoderPID.resetPreviousVal();
    	// Used to make sure that the PID doesn't bail out as done
    	// right away (we know both the distances are zero from the 
    	// above reset).
    	encoderPID.calcPID(0);

    	// The first conditional here checks if the PID is done, pretty simple.
    	// The second conditional is there to make sure that we bail if the robot isn't enabled.
  		while(!encoderPID.isDone() && robot.isEnabled() && robot.isAutonomous()) {
  	    	double driveVal = encoderPID.calcPID((leftEncoder.getDistance() + rightEncoder.getDistance()) / 2.0);
  	    	// TODO: Read this from the constants file as "encoderPIDMax"
  	    	double limitVal = SimLib.limitValue(driveVal, 0.25);
  	    	
  	    	drive.setLeftRightMotorOutputs(limitVal + 0.0038, limitVal);
  		}

  		// Reset the motors (safety and sanity for if we bail out on a none-isDone() 
  		// condition).
    	drive.setLeftRightMotorOutputs(0, 0);
	}
	
	/* (non-Javadoc)
	 * @see ca.team2994.frc.autonomous.DriveManager#driveTurn(int)
	 */
	@Override
	public void driveTurn(int degrees) {
		gyroPID.setDesiredValue(degrees);
  		gyro.reset(0);
		// Reset the gyro PID to a reasonable state.
		encoderPID.resetErrorSum();
    	encoderPID.resetPreviousVal();
    	// Used to make sure that the PID doesn't bail out as done
    	// right away (we know the gyro angle is zero from the above 
    	// reset).
		gyroPID.calcPID(0);

		while(!gyroPID.isDone() && robot.isEnabled() && robot.isAutonomous()) {
			double driveVal = gyroPID.calcPID(gyro.getAngle());
			// TODO: Read this from the constants file as "gyroPIDMax"
  	    	double limitVal = SimLib.limitValue(driveVal, 0.5);
  	    	
  	    	if(degrees < 0) {
  	    		drive.setLeftRightMotorOutputs(limitVal, -limitVal);
  	    	}
  	    	else {
  	    		drive.setLeftRightMotorOutputs(-limitVal, limitVal);
  	    	}
		}
		
  		// Reset the motors (safety and sanity for if we bail out on a none-isDone() 
  		// condition).
		drive.drive(0.0, 0.0);
	}
	
	public void arcadeDrive() {
		//TODO: Change this for competition robot??
		drive.arcadeDrive(-stick.getY(), -stick.getX()); // drive with arcade style (use right stick) (inverted)
	}
	
	public void runCalibration() {
		calibration.calibrateEncoders(leftEncoder, rightEncoder, stick);
	}
}
