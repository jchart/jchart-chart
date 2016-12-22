/**
 * 
 */
package com.jchart.stats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.jchart.stats.impl.Correlation;
import com.jchart.stats.impl.StatsBean;

/**
 * @author <a href ="paul.russo@jchart.com">Paul Russo</a>
 * @created Jul 2, 2011
 */
public abstract class BaseStatsTest {
	
	protected StatsBean getStatsBean(Double[] x, Double[] y) {
		StatsBean retval = null;
		Correlation correlation = new Correlation();
		List<Double> xVals = new ArrayList<>(Arrays.asList(x));
		List<Double> yVals = new ArrayList<>(Arrays.asList(y));
		retval  = correlation.calc(xVals, yVals);
		return retval;
	}

}
