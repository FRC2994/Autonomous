package ca.team2994.frc.autonomous;

/**
 * A type of waypoint action
 * 
 * @author <a href="https://github.com/eandr127">eandr127</a>
 * @author <a href="https://github.com/JackMc">JackMc</a>
 * 
 */
public interface Waypoint extends Runnable {
	
	/** @deprecated by {@link #run() run()}
	 * Performs the action associated with this waypoint. 
	 * @param drive The RobotDrive to drive with
	 */
	public void execute(DriveManager drive);
	
	/**
	 * 
	 * @return
	 */
	public long getTime();
}
