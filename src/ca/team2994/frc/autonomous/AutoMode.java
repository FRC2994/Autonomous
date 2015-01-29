package ca.team2994.frc.autonomous;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import ca.team2994.frc.utils.Utils;

import com.google.common.base.Charsets;
import com.google.common.collect.Iterables;
import com.google.common.io.Files;

public class AutoMode {
	private DriveManager drive;
	private String filename;
	private String name;
	private List<Waypoint> waypoints;
	
	/**
	 * Initialize a AutoMode object and read from the autonomous waypoints file
	 * specified by filename. After this call if an exception is not thrown the
	 * autonomous mode is ready to be executed.
	 * 
	 * @param name The human-readable name of this autonomous mode.
	 * @param filename The filename containing waypoints.
	 * @param drive The drive manager for the robot.
	 * @throws IOException If the waypoints file doesn't exist or cannot be read. 
	 */
	public AutoMode(String name, String filename, DriveManager drive) throws IOException {
		this.name = name;
		this.filename = filename;
		this.drive = drive;
		this.waypoints = new ArrayList<>();
		this.loadWaypoints();
	}
	
	/**
	 * Load the waypoints specified in the waypoints file into the ArrayList.
	 * @throws IOException If the file cannot be read or doesn't exist. 
	 */
	private void loadWaypoints() throws IOException {
		// TODO: Read from the autonomous waypoints file (filename)
		// Magic Guava splitting/reading voodoo
		List<String> guavaResult = Files.readLines(new File(filename), Charsets.UTF_8);
		// Filter to only get those with one digit  *** Still No Copying Done! ***
		Iterable<String> guavaResultFiltered = Iterables.filter(guavaResult, 
				Utils.skipComments);
		guavaResultFiltered.forEach(new Consumer<String>() {
			@Override
			public void accept(String line) {
				String[] s = Iterables.toArray(Utils.SPLITTER.split(line), String.class);
				/*
				 * s should now contain:
				 * 1) The type of action (turn or drive)
				 * 2) The associated values
				 */
				String type = s[0];
				
				if (type.equalsIgnoreCase("turn")) {
					try {
						Integer angle = Integer.parseInt(s[1]);
						waypoints.add(new TurnWaypoint(angle));
					} catch (NumberFormatException nef) {
						nef.printStackTrace();
						Utils.logException(Utils.ROBOT_LOGGER, nef);
					}
				}
				else if (type.equalsIgnoreCase("drive")) {
					try {
						Integer distance = Integer.parseInt(s[1]);
						//TODO: Add a class for a normal waypoint (drive straight)
						waypoints.add(new DriveWaypoint(distance));
					} catch (NumberFormatException nef) {
						nef.printStackTrace();
						Utils.logException(Utils.ROBOT_LOGGER, nef);
					}
					
				}
			}
			
		});
		// Contains the split up ones for this line 
		
		
	}
}
