package ca.team2994.frc.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigFile {

	public static final File DEFAULT_CONFIGURATION_FILE = new File("/home/lvuser/drive.properties");
	
	private final Properties properties = new Properties();

	public ConfigFile(String fileLoc) {
		this(new File(fileLoc));
	}

	public ConfigFile(File file) {
		try {
			InputStream in = new FileInputStream(file);
			properties.load(in);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getProperty(String key) {
		return properties.getProperty(key);
	}

	public String getProperty(String key, String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}

	/**
	 * Gets a property as a boolean value. Anything other than "TRUE" (case
	 * insensitive) is considered false.
	 * 
	 * @param key
	 *            The key to get and convert to a Boolean.
	 * @return The value of the key, converted to a Boolean Object as specified
	 *         above, or null if the property does not exist.
	 */
	public Boolean getPropertyAsBoolean(String key) {
		String prop = this.getProperty(key);

		if (prop == null) {
			return null;
		}

		return prop.equalsIgnoreCase("TRUE") ? true : false;
	}

	/**
	 * Gets a property as a boolean value. Anything other than "TRUE" (case
	 * insensitive) is considered false.
	 * 
	 * @param key
	 *            The key to get and convert to a Boolean.
	 * @param defaultValue
	 *            The value to return if the value does not exist.
	 * @return The value of the key, converted to a Boolean Object as specified
	 *         above, or defaultValue if the property does not exist.
	 */
	public Boolean getPropertyAsBoolean(String key, boolean defaultValue) {
		String prop = this.getProperty(key);

		if (prop == null) {
			return defaultValue;
		}

		return prop.equalsIgnoreCase("TRUE") ? true : false;
	}

	/**
	 * Gets a property as a Integer value. Anything not a integer returns null.
	 * 
	 * @param key
	 *            The key to get and convert to a Integer.
	 * @return The value of the key, converted to a Integer Object as specified
	 *         above, or null if the property does not exist or if a error
	 *         occurs while converting the property.
	 */
	public Integer getPropertyAsInteger(String key) {
		try {
			String s = this.getProperty(key);

			if (s == null) {
				return null;
			}

			return Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	/**
	 * Gets a property as a integer value. Anything that is not a integer is
	 * null.
	 * 
	 * @param key
	 *            The key to get and convert to a Integer.
	 * @param defaultValue
	 *            The value to return if the value does not exist.
	 * @return The value of the key, converted to a Integer Object as specified
	 *         above, null if a error occurred while converting the integer, or
	 *         defaultValue if the property did not exist.
	 */
	public Integer getPropertyAsInteger(String key, int defaultValue) {
		try {
			String s = this.getProperty(key);

			if (s == null) {
				return defaultValue;
			}

			return Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	/**
	 * Gets the property specified by the key argument and splits it with the
	 * given regular expression. If the value does not exist in the Properties
	 * list, the defaultValue is returned. Trimming can be turned on or off to
	 * remove trailing spaces.
	 * 
	 * @param key
	 *            The key who's value to split.
	 * @param defaultValue
	 *            The value to use if the key does not exist in the
	 *            configuration file.
	 * @param regexp
	 *            The regular expression to split the value by.
	 * @param trim
	 *            If the result should be trimmed (remove leading and trailing
	 *            spaces).
	 * @return The value of the key split by the specified regexp if it exists,
	 *         and defaultValue otherwise.
	 */
	public String[] getPropertyAsStringArray(String key, String[] defaultValue,
			String regexp, boolean trim) {
		String value = this.getProperty(key);

		if (value == null) {
			return defaultValue;
		} else {
			String[] split = value.split(regexp);

			if (trim) {
				for (int i = 0; i < split.length; i++) {
					split[i] = split[i].trim();
				}
			}
			return split;
		}
	}

	/**
	 * Gets the property specified by the key argument and splits it with the
	 * given regular expression. If the value does not exist in the Properties
	 * list, the defaultValue is returned. This method trims spaces by default.
	 * If you don't want to trim spaces, see
	 * {@link #getPropertyAsStringArray(String, String[], String, boolean)}.
	 * 
	 * @param key
	 *            The key who's value to split.
	 * @param defaultValue
	 *            The value to use if the key does not exist in the
	 *            configuration file.
	 * @param regexp
	 *            The regular expression to split the value by.
	 * @return The value of the key split by the specified regexp if it exists,
	 *         and defaultValue otherwise.
	 */
	public String[] getPropertyAsStringArray(String key, String[] defaultValue,
			String regexp) {
		return this.getPropertyAsStringArray(key, defaultValue, regexp, true);
	}

	/**
	 * Gets the property specified by the key argument and splits it with the
	 * regular expression "," (creating a comma-separated list). If the value
	 * does not exist in the Properties list, the defaultValue is returned. This
	 * method trims spaces by default. If you don't want to trim spaces, see
	 * {@link #getPropertyAsStringArray(String, String[], String, boolean)}.
	 * 
	 * @param key
	 *            The key who's value to split.
	 * @param defaultValue
	 *            The value to use if the key does not exist in the
	 *            configuration file.
	 * @return The value of the key split by the regular expression "," if it
	 *         exists, and defaultValue otherwise.
	 */
	public String[] getPropertyAsStringArray(String key, String[] defaultValue) {
		return this.getPropertyAsStringArray(key, defaultValue, ",", true);
	}

	/**
	 * Gets the property specified by the key argument and splits it with the
	 * regular expression "," (creating a comma-separated list). If the value
	 * does not exist in the Properties list, null is returned. This method
	 * trims spaces by default. If you don't want to trim spaces, see
	 * {@link #getPropertyAsStringArray(String, String[], String, boolean)}.
	 * 
	 * @param key
	 *            The key who's value to split.
	 * @return The value of the key split by the regular expression "," if it
	 *         exists, and null otherwise.
	 */
	public String[] getPropertyAsStringArray(String key) {
		return this.getPropertyAsStringArray(key, new String[] {}, ",", true);
	}
	
	public double[] getPropertyAsDoubleArray(String key) {
		String[] sArray = this.getPropertyAsStringArray(key);
		double[] dArray = new double[sArray.length];

		for (int i = 0; i < sArray.length; i++) {
			dArray[i] = Double.parseDouble(sArray[i]);
		}
		
		return dArray;
	}
}
