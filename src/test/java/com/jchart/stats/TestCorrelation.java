/**
 * 
 */
package com.jchart.stats;

import org.junit.Assert;

import org.junit.Test;

import com.jchart.stats.impl.StatsBean;

/**
 * @author <a href ="paul.russo@jchart.com">Paul Russo</a>
 * @created Jul 2, 2011
 */
public class TestCorrelation extends BaseStatsTest {
	
	@Test
	public void test1() {
		Double[] XVALS = {120d,110d,100d,90d,80d}; 
		Double[] YVALS = {120d,90d,100d,110d,80d}; 
		StatsBean bean = getStatsBean(XVALS, YVALS);
		Assert.assertEquals(0.6, bean.getR(),0);
		Assert.assertEquals(0.36, bean.getR2(),0);
		Assert.assertEquals(100.0d, bean.getAvgX(),0);
		double stdX = (double) (Math.round(bean.getStdX() * 100d)/100d);
		Assert.assertEquals(15.81d, stdX,0);
		
	}

	@Test
	public void test2() {
		Double[] XVALS = {120d,110d,100d,90d,80d}; 
		Double[] YVALS = {80d,90d,100d,110d,120d}; 
		StatsBean bean = getStatsBean(XVALS, YVALS);
		Assert.assertEquals(-1.0d, bean.getR(),0);
	}

	@Test
	public void test3() {
		Double[] XVALS = {120d,110d,100d,90d,80d}; 
		Double[] YVALS = {120d,110d,100d,90d,80d}; 
		StatsBean bean = getStatsBean(XVALS, YVALS);
		Assert.assertEquals(1.0d, bean.getR(),0);
		System.out.println(bean);
	}
	
}
