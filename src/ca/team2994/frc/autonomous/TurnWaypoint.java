package ca.team2994.frc.autonomous;

public class TurnWaypoint implements Waypoint {
	private int angle;
	
	public TurnWaypoint(int angle) {
		this.angle = angle;
	}
	
	/**
	 * {@inheritDoc}
	 * Turn robot
	 */
	@Override
	public void execute(DriveManager drive) {
		// Turn angle degrees
		drive.driveTurn(angle);
	}
}
