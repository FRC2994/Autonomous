package ca.team2994.frc.autonomous;

/**
 * A type of waypoint action (drive straight)
 * 
 * @author <a href="https://github.com/eandr127">eandr127</a>
 * @author <a href="https://github.com/JackMc">JackMc</a>
 * 
 */
public class DriveWaypoint implements Waypoint {
	
	/**
	 * 
	 */
	private double distance;
	
	/**
	 * 
	 */
	private long time;
	
	/**
	 * 
	 */
	private DriveManager manager;
	
	/**
	 * 
	 * @param distance
	 * @param time
	 * @param manager
	 */
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
