/**
 * 
 */
package com.jchart.model.indicator.pnf;

import java.util.List;

import org.junit.Assert;

import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href ="paul.russo@jchart.com">Paul Russo</a>
 * @created Jan 25, 2011
 */
public class TestBuySell {
	
	private List<PnfItem> _rawPnfItems;

	@Before 
	public void init() {
		try {
			_rawPnfItems = PnfItemReader.getInstance().getPnfItems();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testRawItems() {
		Assert.assertNotNull(_rawPnfItems);
		Assert.assertEquals(2516, _rawPnfItems.size());
	}

	//@Test
	public void testBuySellItems() {
		List<PnfItem> calcBuySellPnfItems = PnFCalc.calcBuySellPnfItems(_rawPnfItems);
		Assert.assertNotNull(calcBuySellPnfItems);
		Assert.assertTrue(calcBuySellPnfItems.size() == 13); // was 14 TODO
		PnfItem item = calcBuySellPnfItems.get(6);
		Assert.assertEquals(12.0f, item.getPrice(),0);
	}

}