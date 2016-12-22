/**
 * 
 */
package com.jchart.stats;

import org.junit.Assert;

import org.junit.Test;

import com.jchart.stats.impl.Regression;
import com.jchart.stats.impl.StatsBean;

/**
 * @author <a href ="paul.russo@jchart.com">Paul Russo</a>
 * @created Jul 2, 2011
 */
public class TestRegression extends BaseStatsTest {
	
	@Test
	public void test1() {
		//http://www.stat.wmich.edu/s216/book/node126.html
		Double[] XVALS = {9300d,10565d,15000d,15000d,17764d,57000d,65940d,73676d,77006d,93739d,146088d,153260d}; 
		Double[] YVALS = {7100d,15500d,4400d,4400d,5900d,4600d,8800d,2000d,2750d,2550d,960d,1025d};
		StatsBean bean = getStatsBean(XVALS, YVALS);
		double avgX = (double) Math.round(bean.getAvgX());
		double stdX = (double) Math.round(bean.getStdX());
		double avgY = (double) Math.round(bean.getAvgY());
		double stdY = (double) Math.round(bean.getStdY());
		double r = (double) Math.round(bean.getR() *1000d)/1000d;
		
		Assert.assertEquals(61195.0d, avgX,0);
		Assert.assertEquals(50989.0d, stdX,0);
		Assert.assertEquals(4999.0d, avgY,0);
		Assert.assertEquals(4079.0d, stdY,0);
		Assert.assertEquals(-0.641d, r,0);
		
		Regression regression = new Regression();
		regression.execute(bean);
		double predictedY = regression.getPredictedY(1d);
		Assert.assertEquals(8136d, predictedY,.5);
		
	}	

	@Test
	public void test2() {
		//http://onlinestatbook.com/chapter12/intro.html
		Double[] XVALS = {1d,2d,3d,4d,5d}; 
		Double[] YVALS = {1d,2d,1.3d,3.75d,2.25d};
		StatsBean bean = getStatsBean(XVALS, YVALS);
		Regression regression = new Regression();
		regression.execute(bean);
		double slope = Math.round(regression.getSlope() *1000d)/1000d;
		double yIntercept = Math.round(regression.getYIntercept() *1000d)/1000d;
		Assert.assertEquals(0.425d, slope,0);
		Assert.assertEquals(0.785d, yIntercept,0);
	}	
	
	@Test
	public void test3() {
		//http://people.hofstra.edu/Stefan_Waner/calctopic1/regression.html
		Double[] XVALS = {1d,2d,3d,4d}; 
		Double[] YVALS = {1.5d,1.6d,2.1d,3.0d};
		StatsBean bean = getStatsBean(XVALS, YVALS);
		Regression regression = new Regression();
		regression.execute(bean);
		double slope = Math.round(regression.getSlope() *1000d)/1000d;
		double yIntercept = Math.round(regression.getYIntercept() *1000d)/1000d;
		Assert.assertEquals(0.5d, slope,0);
		Assert.assertEquals(0.8d, yIntercept,0);
	}

	@Test
	public void test4() {
		//http://people.hofstra.edu/Stefan_Waner/calctopic1/regression.html
		//Example 2 Demand for Homes 
		Double[] XVALS = {160d,180d,200d,220d,240d,260d,280d}; 
		Double[] YVALS = {126d,103d,82d,75d,82d,40d,20d};
		StatsBean bean = getStatsBean(XVALS, YVALS);
		Regression regression = new Regression();
		regression.execute(bean);
		double slope = Math.round(regression.getSlope() *10000d)/10000d;
		double yIntercept = Math.round(regression.getYIntercept() *10d)/10d;
		Assert.assertEquals(-0.7929d, slope,0);
		Assert.assertEquals(249.9d, yIntercept,0);
		double predictedY = Math.round(regression.getPredictedY(140d));
		Assert.assertEquals(139d, predictedY,0);
	}
	
	@Test
	public void test5() {
		Double[] XVALS = {700d,600d,500d,400d,300d}; 
		Double[] YVALS = {0d,3d,2d,1d,4d};
		StatsBean bean = getStatsBean(XVALS, YVALS);
		Regression regression = new Regression();
		regression.execute(bean);
		double r = Math.round(bean.getR() *10000d)/10000d;
		Assert.assertEquals(-0.6d, r,0);
		Double[] YVALS2 = {4d,1d,2d,3d,0d};
		bean = getStatsBean(XVALS, YVALS2);
		regression.execute(bean);
		double slope = Math.round(regression.getSlope() *10000d)/10000d;
		Assert.assertEquals(0.006d, slope,0);
		double yIntercept = Math.round(regression.getYIntercept() *10d)/10d;
		Assert.assertEquals(-1d, yIntercept,0);
	}
	
	@Test
	public void test6() {
		Double[] XVALS = {1d,2d,3d,4d,5d,6d,7d,8d,9d,10d}; 
		Double[] YVALS = {1480.94d,1485.98d,1492.56d,1494.81d,1494.82d,1502.96d,1500.18d,1507.84d,1501.96d,1498.11d};
		StatsBean bean = getStatsBean(XVALS, YVALS);
		Regression regression = new Regression();
		regression.execute(bean);
		double slope = Math.round(regression.getSlope() *10000d)/10000d;
		Assert.assertEquals(2.2245d, slope,0);
		double yIntercept = Math.round(regression.getYIntercept() *10d)/10d;
		Assert.assertEquals(1483.8d, yIntercept,0);
		System.out.println(bean);
	}


}
