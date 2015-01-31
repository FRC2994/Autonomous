package ca.team2994.frc.autonomous;

public class DriveWaypoint implements Waypoint {
	private double distance;
	private long time;
	private DriveManager manager;
	
	public DriveWaypoint(double distance, long time, DriveManager manager) {
		this.distance = distance;
		this.time = time;
		this.manager = manager;
	}

	/** @deprecated by {@link #run() run()}
	 * {@inheritDoc}
	 * Drive forward
	 */
	@Override
	public void execute(DriveManager drive) {
		// Turn angle degrees
		drive.driveStraight(distance);
	}

	@Override
	public long getTime() {
		return time;
	}

	@Override
	public void run() {
		manager.driveStraight(distance);
	}
}
