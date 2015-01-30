package ca.team2994.frc.autonomous;

import java.io.File;

import ca.team2994.frc.utils.Utils;

public class HandleParseValuesImpl implements HandleParseValues {

	@Override
	public void HandleAutonValues(double[] vals) {
		double encoderDistance = 0.0;		//Distance to drive robot forward
		int gyroAngle = 0;					//Angle to turn robot
		
		int i = 0; 							//Counter for switch statement to set values from array
		for(double s : vals) { 				//Set values from array	
			switch(i++) { 					//Choose whether value is for encoderDistance or for gyroAngle
				case 0:
					gyroAngle = (int) s;	//Array is at spot 0, so assign encoderDistance to s
					break;
				case 1:
					encoderDistance = s;	//Array is at spot 1, so assign gyroAngle to s
					break;
				default:
					Utils.logException(Utils.ROBOT_LOGGER, new IllegalArgumentException(new File(Utils.AUTONOMOUS_OUTPUT_FILE_LOC).getName() + " has strange contents!")); //Value shouldn't exist!
					break;
			}
		}
		
		runCommands(encoderDistance, gyroAngle); //Now take care of the values
	}

	private void runCommands(double encoderDistance, int gyroAngle) {
		new TurnWaypoint(gyroAngle).execute(Robot.driveManager);		//Turn...
		new DriveWaypoint(encoderDistance).execute(Robot.driveManager);	//...and drive!
	}
}
