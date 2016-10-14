package com.jchart.io.factory;

import java.util.List;

import com.jchart.io.factory.impl.IoFutures;

public class IoFactory {
    /**
     * This method instantiates a particular subclass implementing
     * the abstract methods based on the information obtained from the
     * properties file
     */
	private static IoFactoryIntr _ioFactory = null;
	private static IoFutures _ioFutures = null;
	
	public static void initInstance(String cgiDir, String tickerDir, boolean useCgi,
			String dataDir) {
		_ioFactory.init(cgiDir, tickerDir, useCgi, dataDir);
		//_ioFutures.init(cgiDir, tickerDir, useCgi, isApplet, dataDir);
	}
    public static IoFactoryIntr getInstance(String ticker) {
    	IoFactoryIntr retval = null;
    	if (ticker.toUpperCase().startsWith("F_")) {
    		retval = _ioFutures;
    	} else {
    		retval = _ioFactory;
    	}
    	return retval;
    }
    
    /**
	 * @param string
	 */
	public static void init(String className)
		throws Exception {
		try {
			_ioFactory = (IoFactoryIntr) Class.forName(className).newInstance();
			//_ioFutures = new IoFutures();
		} catch (Exception e) {
			throw new Exception("Exception while getting IoFactoryIntr: " + e.getMessage());
		}
	}

	public static List<String> getTickerList(String tickerFile) throws Exception {
		return _ioFactory.getTickerList(tickerFile);
	}

}
