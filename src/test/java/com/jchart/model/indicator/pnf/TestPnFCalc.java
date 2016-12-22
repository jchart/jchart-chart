/**
 * 
 */
package com.jchart.model.indicator.pnf;

import org.junit.Assert;

import org.junit.Test;

/**
 * @author <a href ="paul.russo@jchart.com">Paul Russo</a>
 * @created Oct 25, 2010
 */
public class TestPnFCalc {

	@Test 
	public void testRevOBox() {
		float revOBox = PnFCalc.getRevOBox(22f);
		Assert.assertEquals(19.5f, revOBox,0);
		revOBox = PnFCalc.getRevOBox(21f);
		Assert.assertEquals(19.0f, revOBox,0);
		revOBox = PnFCalc.getRevOBox(20f);
		Assert.assertEquals(18.5f, revOBox,0);
	}

	@Test 
	public void testRevXBox() {
		float revXBox = PnFCalc.getRevXBox(18.5f);
		Assert.assertEquals(20.0f, revXBox,0);
		revXBox = PnFCalc.getRevXBox(19f);
		Assert.assertEquals(21.0f, revXBox,0);
		revXBox = PnFCalc.getRevXBox(19.5f);
		Assert.assertEquals(22.0f, revXBox,0);
	}
	
	@Test 
	public void testNumRevXBoxes() {
		float hi = 25f;
		float curOBox = 20f;
		int xBoxes = PnFCalc.getNumRevXBoxes(hi, curOBox);
		Assert.assertEquals(5, xBoxes);
		hi = 22f;
		curOBox = 18.5f;
		xBoxes = PnFCalc.getNumRevXBoxes(hi, curOBox);
		Assert.assertEquals(5, xBoxes);
		hi = 22f;
		curOBox = 19.5f;
		xBoxes = PnFCalc.getNumRevXBoxes(hi, curOBox);
		Assert.assertEquals(3, xBoxes);
		hi = 21f;
		curOBox = 19.5f;
		xBoxes = PnFCalc.getNumRevXBoxes(hi, curOBox);
		Assert.assertEquals(0, xBoxes);
	}

	@Test 
	public void testNumRevOBoxes() {
		float lo = 20f;
		float curOBox = 25f;
		int oBoxes = PnFCalc.getNumRevOBoxes(lo, curOBox);
		Assert.assertEquals(5, oBoxes);
		lo = 18.5f;
		curOBox = 22f;
		oBoxes = PnFCalc.getNumRevOBoxes(lo, curOBox);
		Assert.assertEquals(5, oBoxes);
		lo = 19.5f;
		curOBox = 21f;
		oBoxes = PnFCalc.getNumRevOBoxes(lo, curOBox);
		Assert.assertEquals(0, oBoxes);
	}
	
	@Test 
	public void testNewXBoxes() {
		float hi = 27f;
		float curXBox = 25f;
		int xBoxes = PnFCalc.getNewXBoxes(hi, curXBox);
		Assert.assertEquals(2, xBoxes);
		hi = 27f;
		curXBox = 28f;
		xBoxes = PnFCalc.getNewXBoxes(hi, curXBox);
		Assert.assertEquals(0, xBoxes);
		hi = 22f;
		curXBox = 18.0f;
		xBoxes = PnFCalc.getNewXBoxes(hi, curXBox);
		Assert.assertEquals(6, xBoxes);
	}

	@Test 
	public void testNewOBoxes() {
		float lo = 25f;
		float curOBox = 27f;
		int oBoxes = PnFCalc.getNewOBoxes(lo, curOBox);
		Assert.assertEquals(2, oBoxes);
		lo = 28f;
		curOBox = 27f;
		oBoxes = PnFCalc.getNewOBoxes(lo, curOBox);
		Assert.assertEquals(0, oBoxes);
		lo = 18f;
		curOBox = 22.0f;
		oBoxes = PnFCalc.getNewOBoxes(lo, curOBox);
		Assert.assertEquals(6, oBoxes);
	}

	@Test 
	public void testXBoxVal() {
		float curBox = 27f;
		int numBoxes = 3;
		float boxVal = PnFCalc.getXBoxVal(numBoxes, curBox);
		Assert.assertEquals(30f, boxVal,0);
		curBox = 18f;
		numBoxes = 6;
		boxVal = PnFCalc.getXBoxVal(numBoxes, curBox);
		Assert.assertEquals(22f, boxVal,0);
	}

	@Test 
	public void testOBoxVal() {
		float curBox = 27f;
		int numBoxes = 3;
		float boxVal = PnFCalc.getOBoxVal(numBoxes, curBox);
		Assert.assertEquals(24f, boxVal,0);
		numBoxes = 6;
		boxVal = PnFCalc.getOBoxVal(numBoxes, curBox);
		Assert.assertEquals(21f, boxVal,0);
	}
	
	@Test
	public void testCalcNumYBoxes() {
		float curPrice = 25f;
		float lowPrice = 3f;
		int numYBoxes = PnFCalc.calcNumYBoxes(curPrice, lowPrice);
		Assert.assertEquals(44, numYBoxes);
		curPrice = 3;
		numYBoxes = PnFCalc.calcNumYBoxes(curPrice, lowPrice);
		Assert.assertEquals(1, numYBoxes);
	}
	
	@Test
	public void testCalcPlotVal() {
		float curPrice = 25;
		float lowPrice = 3f;
		int plotVal = PnFCalc.calcPlotVal(curPrice, lowPrice);
		Assert.assertEquals(49, plotVal);
		curPrice = 3;
		plotVal = PnFCalc.calcPlotVal(curPrice, lowPrice);
		Assert.assertEquals(6, plotVal);
		curPrice = 4;
		plotVal = PnFCalc.calcPlotVal(curPrice, lowPrice);
		Assert.assertEquals(10, plotVal);
	}
	
	@Test
	public void testCalcMaxYVal() {
		float hiPrice = 25;
		float lowPrice = 3f;
		int maxYVal = PnFCalc.calcMaxYVal(hiPrice, lowPrice);
		Assert.assertEquals(54, maxYVal);
	}
	
}
