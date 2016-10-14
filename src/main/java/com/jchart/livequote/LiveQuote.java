/**
 * 
 */
package com.jchart.livequote;

import com.jchart.model.Quote;

/**
 * @author <a href="mailto:paul.russo@jchart.com">Paul S. Russo</a>
 * @since Sep 17, 2012
 */

public class LiveQuote {
	private Quote quote;
	private String coname;
	public Quote getQuote() {
		return quote;
	}
	public String getConame() {
		return coname;
	}
	public void setQuote(Quote quote) {
		this.quote = quote;
	}
	public void setConame(String coname) {
		this.coname = coname;
	}
	@Override
	public String toString() {
		return "LiveQuote [quote=" + quote + ", coname=" + coname + "]";
	}


}
