package ca.team2994.frc.autonomous;

public interface DriveManager {

	/**
	 * Uses a PID controller to drive straight units of whatever units we were
	 * calibrated to.
	 * @param units Number of units to drive forward. Determined by calibration.
	 */
	public abstract void driveStraight(double units);

	/**
	 * Turns degrees degrees to the left when degrees is negative and to the right
	 * when degrees is positive.
	 * TODO: Check if left and right turns are actually always negative and positive
	 * 		 respectively.
	 * @param degrees The number of degrees to turn
	 */
	public abstract void driveTurn(int degrees);
	
	/**
	 * Drive using arcade drive. This means that the joystick will be used so that 
	 * the y axis (up and down on the joystick) will move the robot forward and 
	 * backwards and the x axis (left and right on the joystick) will spin the 
	 * robot left and right.
	 */
	public abstract void arcadeDrive();
	
	/**
	 * Runs a calibration routine to set the encoder distance per pulse. 
	 */
	public void runCalibration();
	
	
	/**
	 * Reads PID values from file and sets them
	 */
	public void readPIDValues();
	
	/**
	 * Called at the beginning of autonomous
	 */
	public void runAutonomous();
	
	/**
	 * 
	 */
	public void runTeleOPLogging();
	
	public void resetMeasurements();
}