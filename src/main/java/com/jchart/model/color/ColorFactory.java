package com.jchart.model.color;

public class ColorFactory {
	/**
     * This method instantiates a particular subclass implementing
     * the abstract methods based on the information obtained from the
     * properties file
     */

	
	/**
	 * @param string
	 */
	public static ColorScheme getColorScheme(String className) {
		ColorScheme retval = null;
		try {
			retval = (ColorScheme) Class.forName(className).newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Exception while getting ColorScheme: " + e.getMessage());
		}
		return retval;
	}

}
