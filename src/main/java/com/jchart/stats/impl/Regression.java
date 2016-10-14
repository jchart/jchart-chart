/**
 * 
 */
package com.jchart.stats.impl;

/**
 * @author <a href ="paul.russo@jchart.com">Paul Russo</a>
 * @created Jul 2, 2011
 */
public class Regression {
	
	private StatsBean _statsBean;

	public static double getPredictedY(StatsBean statsBean, double x) {
		return statsBean.getYintercept() + (statsBean.getSlope() * x);
	}
	
	public void execute(StatsBean statsBean) {
		_statsBean = statsBean;
		calcSlope();
	}
	
	/**
	 * @return predicted y given x 
	 * 
	 * @return Y = a + bX
	 * 
	 */
	public double getPredictedY(double x) {
		return _statsBean.getYintercept() + (_statsBean.getSlope() * x);
	}
	
	public double getSlope() {
		return _statsBean.getSlope();
	}

	public double getYIntercept() {
		return _statsBean.getYintercept();
	}

	private void calcSlope() {
		double slope = _statsBean.getR() * (_statsBean.getStdY() / _statsBean.getStdX());
		double yIntercept = _statsBean.getAvgY() - (slope * _statsBean.getAvgX() );
		_statsBean.setSlope(slope);
		_statsBean.setYintercept(yIntercept);
	}
	


}
