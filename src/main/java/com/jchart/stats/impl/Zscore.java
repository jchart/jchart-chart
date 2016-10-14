/**
 * 
 */
package com.jchart.stats.impl;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href ="paul.russo@jchart.com">Paul Russo</a>
 * @since Jul 31, 2011
 */

public class Zscore {
	
	public List<Double> calc(List<Double> vals) {
		List<Double> retval = new ArrayList<Double>();
		StandardDeviation standardDeviation = new StandardDeviation();
		double std = standardDeviation.calc(vals);
		double avg = standardDeviation.getAvg();
		double z = 0d;
		for (Double val : vals) {
			z = (val - avg)/std;
			retval.add(z);
		}
		return retval;
	}

}
