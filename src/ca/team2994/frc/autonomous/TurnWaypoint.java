package ca.team2994.frc.autonomous;

public class TurnWaypoint implements Waypoint {
	private int angle;
	private long time;
	private DriveManager manager;
	
	public TurnWaypoint(int angle, long time, DriveManager manager) {
		this.angle = angle;
		this.time = time;
		this.manager = manager;
	}
	
	/** @deprecated by {@link #run() run()}
	 * {@inheritDoc}
	 * Turn robot
	 */
	@Override
	public void execute(DriveManager drive) {
		// Turn angle degrees
		drive.driveTurn(angle);
	}
	
	@Override
	public long getTime() {
		return time;
	}

	@Override
	public void run() {

		
		manager.driveTurn(angle);
		
	}
}
