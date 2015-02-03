package ca.team2994.frc.autonomous;

/**
 * A type of waypoint action
 * 
 * @author <a href="https://github.com/eandr127">eandr127</a>
 * @author <a href="https://github.com/JackMc">JackMc</a>
 * 
 */
public interface Waypoint extends Runnable {
		
	/**
	 * 
	 * @return
	 */
	public long getTime();
}
