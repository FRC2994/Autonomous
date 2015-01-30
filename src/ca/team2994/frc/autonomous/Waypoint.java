package ca.team2994.frc.autonomous;

public interface Waypoint {
	/**
	 * Performs the action associated with this waypoint. 
	 * @param drive
	 */
	public void execute(DriveManager drive);
}
