/**
 * 
 */
package com.jchart.io.factory.impl;

import java.util.Properties;

import com.jchart.model.JchartModelFacade;

/**
 * @author <a href ="paul.russo@jchart.com">Paul Russo</a>
 * @created Dec 10, 2010
 */
public class IoJchartScreenWeb extends IoJchartPsr {

	private static final long serialVersionUID = 1L;
	protected String getEodUrlStr(String ticker, int maxQuotes) {
		String retval = null;
		Properties props = JchartModelFacade.getJchartProps();
		retval = props.getProperty("jchart.service.eod.url") + "/" + ticker;
		return retval;
	}
}
