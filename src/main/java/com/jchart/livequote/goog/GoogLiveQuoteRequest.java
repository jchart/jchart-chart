/**
 * 
 */
package com.jchart.livequote.goog;

import java.io.InputStream;
import java.net.URL;

import com.jchart.livequote.LiveQuote;
import com.jchart.livequote.LiveQuoteRequest;

/**
 * @author <a href="mailto:paul.russo@jchart.com">Paul S. Russo</a>
 * @since Aug 27, 2012
 */
public class GoogLiveQuoteRequest implements LiveQuoteRequest {
	
	private static final String BASE_URL = "http://www.google.com/ig/api?stock=";
	
	/**
	 * @see com.jchart.livequote.goog.LiveQuoteRequest#getLiveQuote(java.lang.String)
	 */
	@Override
	public LiveQuote getLiveQuote(String ticker) {
		LiveQuote retval = null;
		try { 
			URL quoteData = new URL(BASE_URL + ticker);
			InputStream is = quoteData.openStream();
			try {
				GoogLiveQuoteSaxHandler handler = new GoogLiveQuoteSaxHandler();
				retval = handler.execute(handler, is);
		    } finally {
		        is.close();
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retval;
	}

}
