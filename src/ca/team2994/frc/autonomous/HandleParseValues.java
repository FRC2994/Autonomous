package ca.team2994.frc.autonomous;

public interface HandleParseValues {
	
	/**
	 * Takes the values found in auto.log
	 * @param vals The values founf in auto.log
	 */
	public abstract void HandleAutonValues(double[] vals);
}
