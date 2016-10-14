/**
 * 
 */
package com.jchart.model.indicator.pnf;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href ="paul.russo@jchart.com">Paul Russo</a>
 * @created Jan 27, 2011
 */
public class PnfItemReader {
	
	private List<PnfItem> _pnfItems;
	private static PnfItemReader _pnfItemReader;
	
	static synchronized PnfItemReader getInstance() {
		if (_pnfItemReader == null) {
			_pnfItemReader = new PnfItemReader();
			_pnfItemReader.init();
		}
		return _pnfItemReader;
	}

	public void init() {
		try {
			_pnfItems = readPnfItems();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<PnfItem> getPnfItems() {
		return _pnfItems;
	}
	
	private List<PnfItem> readPnfItems() throws Exception {
		List<PnfItem> retval = new ArrayList<PnfItem>();
		String[] csvItems = new String[4];
		InputStream is = this.getClass().getResourceAsStream("/pnf/F_raw.csv");
		BufferedReader br = 
			new BufferedReader(new InputStreamReader(is));
			String inLine = null;
		     while ((inLine = br.readLine()) != null) {
  				 PnfItem pnfItem = new PnfItem();
		    	 csvItems = inLine.split(",");
		    	 pnfItem.setDate(csvItems[1]);
		    	 pnfItem.setNumBoxes(new Integer(csvItems[2]));
		    	 pnfItem.setPrice(new Float(csvItems[3]));
		    	 retval.add(pnfItem);
		     }

		return retval;
	}

}
