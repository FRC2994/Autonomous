
package ca.team2994.frc.autonomous;


import static java.util.logging.Level.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import ca.team2994.frc.utils.ButtonEntry;
import ca.team2994.frc.utils.EJoystick;
import ca.team2994.frc.utils.SimPID;
import ca.team2994.frc.utils.Utils;

import com.google.common.base.Charsets;
import com.google.common.collect.Iterables;
import com.google.common.io.Files;

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
	
	/**
	 * The RobotDrive to use to drive
	 */
    private RobotDrive myRobot;
    
    /**
     * The joystick to use
     */
    private EJoystick stick;
    
    /**
     * The first talon motor
     */
    private Talon motorA;
    
    /**
     * The second talon motor
     */
    private Talon motorB;
    
    /**
     * The encoder for the first motor
     */
    private Encoder encoderA;
    
    /**
     * The encoder for the second motor
     */
    private Encoder encoderB;
    
    /**
     * The SimPID to be used
     */
	private SimPID sim;
	
    /**
     * This is the code first run when the robot code is started
     */
	public void robotInit() {
    	sim = new SimPID(2.16, 0.0, 0.0, 0.1);
    	
    	motorA = new Talon(0);
    	motorB = new Talon(1);
    	
    	encoderA = new Encoder(0, 1, true);
    	encoderB = new Encoder(2, 3, true);
    	
        myRobot = new RobotDrive(motorA, motorB);
        myRobot.setExpiration(0.1);
        stick = new EJoystick(0);
        
		Utils.configureRobotLogger();
		Utils.ROBOT_LOGGER.log(INFO, "Constructer");
		
		try {
			
			List<String> guavaResult = Files.readLines(new File(Utils.CALIBRATION_OUTPUT_FILE_LOC), Charsets.UTF_8);
			// Filter to only get those with one digit  *** Still No Copying Done! ***
			Iterable<String> guavaResultFiltered = Iterables.filter(guavaResult, Utils.skipComments);
			
			String[] s = Iterables.toArray(Utils.SPLITTER.split(guavaResultFiltered.iterator().next()), String.class);
			
			
			double encoderAConst = Double.parseDouble(s[0]);
			double encoderBConst = Double.parseDouble(s[1]);
				
			encoderA.setDistancePerPulse(encoderAConst);
			encoderB.setDistancePerPulse(encoderBConst);
			
		} catch (IOException e) {
			Utils.logException(Utils.ROBOT_LOGGER, e);
			encoderA.setDistancePerPulse(1);
			encoderB.setDistancePerPulse(1);
		}
	}

    /**
     * Either run preprogrammed file or call {@link #PIDTest() PIDTest()}
     */
    public void autonomous() {
    	Utils.ROBOT_LOGGER.log(INFO, "Autonomous");
    	myRobot.setSafetyEnabled(false);
    	
    	encoderA.reset();
    	encoderB.reset();
    	
    	
    	
    	if(USE_PID) {
    		Utils.driveStraight(0.25, 1.0, encoderA, encoderB, myRobot, sim);
    	}
    	else {
    		new ParseFile(new File(Utils.AUTONOMOUS_OUTPUT_FILE_LOC),
    			new Encoder[] {
    				encoderA,
    				encoderB
    			}, 
    			myRobot);
    	}
    	
        myRobot.drive(0.0, 0.0);	// stop robot
    }

    private void testPID() {
    	if(autoLoopCounter == 0) {
    		sim.setDesiredValue(1.0);
    		encoderA.reset();
        	encoderB.reset();
    	}
    	
    	double driveVal = sim.calcPID((encoderA.getDistance() + encoderB.getDistance()) / 2.0);
    	double limitVal = SimPID.limitValue(driveVal, 0.25);
    	
    	myRobot.setLeftRightMotorOutputs(limitVal + 0.0038, limitVal);
    	autoLoopCounter++;
    	
    	System.out.println("isDone: " + sim.isDone() + " driveVal: " + driveVal + " limitVal: " + limitVal + " leftEncoder: " + encoderA.getDistance() + " rightEncoder: " + encoderB.getDistance());
    	if(autoLoopCounter % 100 == 0) {
    		Utils.ROBOT_LOGGER.log(INFO, ("isDone: " + sim.isDone() + " driveVal: " + driveVal + " limitVal: " + limitVal + " leftEncoder: " + encoderA.getDistance() + " rightEncoder: " + encoderB.getDistance()));
    	}
    }
    
    /**
     * Runs the motors with arcade steering or log waypoints (TODO).
     */
    public void operatorControl() {
    	if(!SAVE_WAYPOINTS) {
    		Utils.ROBOT_LOGGER.log(INFO, "Tele-Op");
    		myRobot.setSafetyEnabled(true);
    		while (isOperatorControl() && isEnabled()) {
    			myRobot.arcadeDrive(-stick.getY(), -stick.getX()); // drive with arcade style (use right stick) (inverted)
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
    	Utils.ROBOT_LOGGER.log(INFO, "Calibration");
    	encoderA.reset();
    	encoderB.reset();
    	
    	stick.enableButton(2);
    	
    	int i = 0;
    	while(i != ButtonEntry.EVENT_CLOSED) {
    		stick.update();
    		if(!isTest()) {
    			return;
    		}
    		myRobot.arcadeDrive(-stick.getY(), -stick.getX()); // drive with arcade style (use right stick) (inverted) // drive with arcade style (use right stick)
    		i = stick.getEvent(2);
    	}
    	
    	myRobot.drive(0, 0);
    	
    	int encoderAValue = encoderA.get();
    	int encoderBValue = encoderB.get();
    	
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
