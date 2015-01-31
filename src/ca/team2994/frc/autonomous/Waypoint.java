package ca.team2994.frc.autonomous;

public interface Waypoint extends Runnable {
	/** @deprecated by {@link #run() run()}
	 * Performs the action associated with this waypoint. 
	 * @param drive The RobotDrive to drive with
	 */
	public void execute(DriveManager drive);
	
	public long getTime();
}
