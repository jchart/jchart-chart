/**
 * 
 */
package com.jchart.stats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;

import org.junit.Test;

import com.jchart.stats.impl.Zscore;

/**
 * @author <a href ="paul.russo@jchart.com">Paul Russo</a>
 * @created Jul 4, 2011
 */
public class TestZscore {
	
	@Test
	public void test1() {
		Double[] VALS = {80d,90d,100d,110d,120d}; 
		@SuppressWarnings({ "rawtypes", "unchecked" })
		List<Double> vals = new ArrayList(Arrays.asList(VALS));
		Zscore zscore = new Zscore();
		List<Double> zVals = zscore.calc(vals);
		double d0 = (double)Math.round(zVals.get(0) * 10000) / 10000;
		Assert.assertEquals(-1.2649, d0,0);
		double d1 = (double)Math.round(zVals.get(1) * 10000) / 10000;
		Assert.assertEquals(-0.6325, d1,0);
		double d2 = (double)Math.round(zVals.get(2) * 10000) / 10000;
		Assert.assertEquals(0.0, d2,0);
		double d3 = (double)Math.round(zVals.get(3) * 10000) / 10000;
		Assert.assertEquals(0.6325, d3,0);
		double d4 = (double)Math.round(zVals.get(4) * 10000) / 10000;
		Assert.assertEquals(1.2649, d4,0);
	}

}
