package ca.team2994.frc.autonomous;

public class DriveWaypoint implements Waypoint {
	private double distance;
	
	public DriveWaypoint(double distance) {
		this.distance = distance;
	}

	/**
	 * {@inheritDoc}
	 * Drive forward
	 */
	@Override
	public void execute(DriveManager drive) {
		// Turn angle degrees
		drive.driveStraight(distance);
	}
}
