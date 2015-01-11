
package ca.team2994.frc.autonomous;


import static java.util.logging.Level.INFO;

import java.io.File;

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
    	
    	encoderA = new Encoder(0, 1, false);
    	encoderB = new Encoder(2, 3, true);
    	
        myRobot = new RobotDrive(motorA, motorB);
        myRobot.setExpiration(0.1);
        stick = new EJoystick(0);
        
		Utils.configureRobotLogger();
		Utils.ROBOT_LOGGER.log(INFO, "Constructer");
    }

    /**
     * Drive left and right motors for 2 seconds then stop
     * TODO: Run program from log
     */
    public void autonomous() {
    	Utils.ROBOT_LOGGER.log(INFO, "Autonomous");
    	
    	new ParseFile(new File(Utils.AUTONOMOUS_OUTPUT_FILE_LOC), new Talon[] {
    		motorA,
    		motorB
    	}, new Encoder[] {
    		encoderA,
    		encoderB
    	});
    	
        myRobot.setSafetyEnabled(false);
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
    	
    	int i = 0;
    	while(i != 2) {
    		i = stick.getEvent(1);
    	}
    	
    	Utils.writeLineToFile("//Encoder A, Distance Travelled: 5ft, Number of encoder ticks: " + encoderA.getDistance() + ", Calibration constant: " + 5 / encoderA.getDistance(), new File(Utils.AUTONOMOUS_OUTPUT_FILE_LOC));
    	Utils.writeLineToFile("//Encoder B, Distance Travelled: 5ft, Number of encoder ticks: " + encoderB.getDistance() + ", Calibration constant: " + 5 / encoderB.getDistance(), new File(Utils.AUTONOMOUS_OUTPUT_FILE_LOC));
    	Utils.writeLineToFile(5 / encoderA.getDistance() + ", " + 5 / encoderB.getDistance(), new File(Utils.AUTONOMOUS_OUTPUT_FILE_LOC));
    }
    
    
}
