/**
 * 
 */
package com.jchart.stats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;

import org.junit.Test;

import com.jchart.stats.impl.StandardDeviation;

/**
 * @author <a href ="paul.russo@jchart.com">Paul Russo</a>
 * @created Jul 4, 2011
 */
public class TestStd {
	
	@Test
	public void test1() {
		Double[] XVALS = {9300d,10565d,15000d,15000d,17764d,57000d,65940d,73676d,77006d,93739d,146088d,153260d}; 
		Double[] YVALS = {7100d,15500d,4400d,4400d,5900d,4600d,8800d,2000d,2750d,2550d,960d,1025d};
		@SuppressWarnings({ "rawtypes", "unchecked" })
		List<Double> xVals = new ArrayList(Arrays.asList(XVALS));
		@SuppressWarnings({ "rawtypes", "unchecked" })
		List<Double> yVals = new ArrayList(Arrays.asList(YVALS));
		StandardDeviation standardDeviaton = new StandardDeviation();
		double stdX = standardDeviaton.calc(xVals);
		stdX = (double) Math.round(stdX);
		double stdY = standardDeviaton.calc(yVals);
		stdY = (double) Math.round(stdY);
		Assert.assertEquals(50989.0d, stdX,0);
		Assert.assertEquals(4079.0d, stdY,0);
	}

	@Test
	public void test2() {
		Double[] XVALS = {-30d,-20d,0d,20d,30d}; 
		@SuppressWarnings({ "rawtypes", "unchecked" })
		List<Double> xVals = new ArrayList(Arrays.asList(XVALS));
		StandardDeviation standardDeviaton = new StandardDeviation();
		double stdX = standardDeviaton.calc(xVals);
		System.out.println(stdX);
		System.out.println(standardDeviaton.getAvg());
		
		Double[] XVALS2 = {0d,10d,30d,50d,60d}; 
		@SuppressWarnings({ "rawtypes", "unchecked" })
		List<Double> xVals2 = new ArrayList(Arrays.asList(XVALS2));
		StandardDeviation standardDeviaton2 = new StandardDeviation();
		double stdX2 = standardDeviaton2.calc(xVals2);
		System.out.println(stdX2);
		System.out.println(standardDeviaton2.getAvg());
	}
}
