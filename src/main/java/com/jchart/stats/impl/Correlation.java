/**
 * 
 */
package com.jchart.stats.impl;

import java.util.List;

/**
 * @author <a href ="paul.russo@jchart.com">Paul Russo</a>
 * @created Jul 2, 2011
 */
public class Correlation {
	
	// r numerator = sum(X*Y) - [ (sum(x) * sum(y)) / n  ]
	// r denominator = sqr[ (sum(x)^2) - (sum(x^2) / n) * (sum(y)^2) - (sum(y^2) / n) ]
	//
	// denomX = (sum(x)^2) - (sum(x^2) / n) * 
	// denomY = (sum(y)^2) - (sum(y^2) / n)

	public StatsBean calc(List<Double> xVals, List<Double> yVals) {
		StatsBean retval = new StatsBean();

		// missing data Exception
		if (xVals.size() != yVals.size()) {
			throw new IllegalStateException("missing data: xVals and Yvals are different sizes");
		}
		double xVal = 0;
		double xSum = 0d;
		double xSumSqr = 0d;
		double yVal = 0;
		double ySum = 0d;
		double ySumSqr = 0d;
		double xyCrossProduct = 0d;
		int numVals = xVals.size();
		for (int i=0;i<xVals.size();i++) {
			xVal = xVals.get(i);
			yVal = yVals.get(i);
			xSum += xVal;
			ySum += yVal;
			xSumSqr += Math.pow(xVal,2);  
			ySumSqr += Math.pow(yVal,2);  
			xyCrossProduct += xVal * yVal;
		}
		// average
		double xAvg = xSum / numVals;
		double yAvg = ySum / numVals;
		
		// standard deviation
		double xStdNumer = xSumSqr - (Math.pow(xSum, 2) / numVals);
		double xStd = Math.sqrt(xStdNumer/(numVals-1));
		double yStdNumer = ySumSqr - (Math.pow(ySum, 2) / numVals); 
		double yStd = Math.sqrt(yStdNumer/(numVals-1));

		// correlation
		double numer =  xyCrossProduct - ( (xSum * ySum)/numVals);
		double denomX = xSumSqr - (Math.pow(xSum,2) / numVals);
		double denomY = ySumSqr - (Math.pow(ySum,2) / numVals);
		// correlation coefficient
		double r = numer / Math.sqrt(denomX * denomY); 
		double r2 = Math.pow(r,2);
		retval.setAvgX(xAvg);
		retval.setAvgY(yAvg);
		retval.setStdX(xStd);
		retval.setStdY(yStd);
		retval.setR(r);
		retval.setR2(r2);
		retval.setLength(numVals);
		return retval;
	}

}