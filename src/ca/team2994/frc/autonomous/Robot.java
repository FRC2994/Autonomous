
package ca.team2994.frc.autonomous;


import static java.util.logging.Level.INFO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import ca.team2994.frc.utils.ButtonEntry;
import ca.team2994.frc.utils.EJoystick;
import ca.team2994.frc.utils.Utils;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;

/**
 * This is a demo program showing the use of the RobotDrive class.
 * The SampleRobot class is the base of a robot application that will automatically call your
 * Autonomous and OperatorControl methods at the right time as controlled by the switches on
 * the driver station or the field controls.
 *
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SampleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 *
 * WARNING: While it may look like a good choice to use for your code if you're inexperienced,
 * don't. Unless you know what you are doing, complex code will be much more difficult under
 * this system. Use IterativeRobot or Command-Based instead if you're new.
 */
public class Robot extends SampleRobot {
    RobotDrive myRobot;
    EJoystick stick;
    
    final Talon motorA;
    final Talon motorB;
    
    final Encoder encoderA;
    final Encoder encoderB;

    public Robot() {

    	motorA = new Talon(0);
    	motorB = new Talon(1);
    	
    	encoderA = new Encoder(0, 1, true);
    	encoderB = new Encoder(2, 3, true);
    	
        myRobot = new RobotDrive(motorB, motorA);
        myRobot.setExpiration(0.1);
        stick = new EJoystick(0);
        
		Utils.configureRobotLogger();
		Utils.ROBOT_LOGGER.log(INFO, "Constructer");
		
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(new File(Utils.CALIBRATION_OUTPUT_FILE_LOC)));
			String line = null;
			while((line = br.readLine()) != null) {
				line = line.replaceAll("\\s", "");
				if(!line.startsWith("//") && !line.isEmpty()) {
					String[] s = line.split(",");
					double encoderAConst = Double.parseDouble(s[0]);
					double encoderBConst = Double.parseDouble(s[1]);
					
					encoderA.setDistancePerPulse(encoderAConst);
					encoderB.setDistancePerPulse(encoderBConst);
					break;
				}
			}
		} catch (IOException e) {
			encoderA.setDistancePerPulse(1);
			encoderB.setDistancePerPulse(1);
		}
    }

    /**
     * Drive left and right motors for 2 seconds then stop
     */
    public void autonomous() {
    	Utils.ROBOT_LOGGER.log(INFO, "Autonomous");
    	myRobot.setSafetyEnabled(false);
    	
    	/*new ParseFile(new File(Utils.AUTONOMOUS_OUTPUT_FILE_LOC), new Talon[] {
    		motorA,
    		motorB
    	}, new Encoder[] {
    		encoderA,
    		encoderB
    	});*/
    	
    	encoderA.reset();
    	encoderB.reset();
    	
    	while((encoderA.getDistance() < 5 || encoderB.getDistance() < 5) && isAutonomous()) {
    		myRobot.drive(-0.5, 0);
    	}
    	
    	myRobot.drive(0, 0);
    	  
        //myRobot.drive(-0.5, 0.0);	// drive forwards half speed
        //Timer.delay(2.0);		//    for 2 seconds
        //myRobot.drive(0.0, 0.0);	// stop robot
    }

    /**
     * Runs the motors with arcade steering.
     * TODO: Log waypoints
     */
    public void operatorControl() {
    	Utils.ROBOT_LOGGER.log(INFO, "Tele-Op");
        myRobot.setSafetyEnabled(true);
        while (isOperatorControl() && isEnabled()) {
            myRobot.arcadeDrive(stick); // drive with arcade style (use right stick)
            Timer.delay(0.005);		// wait for a motor update time
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
    		myRobot.arcadeDrive(stick); // drive with arcade style (use right stick)
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
