/**
 * 
 */
package com.jchart.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * @author <a href ="paul.russo@jchart.com">Paul Russo</a>
 * @since Jul 31, 2011
 */

public class ConameFactory {
	
	private static Properties _conaneProps;

	public void init() {
		try {
			_conaneProps = loadConameProps(this.getClass());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getConame(String ticker) {
		String retval = null;
		retval = _conaneProps.getProperty(ticker);
		return retval;
	}
	public static Properties loadConameProps(Class<?> clazz) throws FileNotFoundException, IOException {
		Properties retval = new Properties();
		return retval;
	}

}
