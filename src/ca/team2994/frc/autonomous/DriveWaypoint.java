package ca.team2994.frc.autonomous;

public class DriveWaypoint implements Waypoint {
	private int distance;
	
	public DriveWaypoint(int distance) {
		this.distance = distance;
	}

	@Override
	public void execute(DriveManager drive) {
		// Turn angle degrees
		drive.driveStraight(distance);
	}
}
