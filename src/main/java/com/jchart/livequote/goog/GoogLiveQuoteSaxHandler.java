package com.jchart.livequote.goog;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import com.jchart.livequote.LiveQuote;
import com.jchart.model.Quote;

public class GoogLiveQuoteSaxHandler extends DefaultHandler {

	private static DateFormat DATE_FMT = new SimpleDateFormat("yyyyMMdd");
	private Quote _quote = new Quote();
	private LiveQuote _liveQuote = new LiveQuote();

	public LiveQuote execute(DefaultHandler handler, InputStream is) throws Exception {
		// set system property org.xml.sax.driver
		XMLReader xr = XMLReaderFactory.createXMLReader();
		xr.setContentHandler(handler);
		xr.setErrorHandler(handler);
		InputStreamReader ir = new InputStreamReader(is);
	    xr.parse(new InputSource(ir));
	    _liveQuote.setQuote(_quote);
	    return _liveQuote;
	}
	

	@Override
	public void startElement(String uri, String name, String qName,
			Attributes atts) {
		String val = null;
		if (atts != null && atts.getLength() > 0) {
			val = atts.getValue(0);
		}
		if  (val != null && val.trim().length() > 0) {
			if ("high".equals(qName)) {
				_quote.setHi(new Float(val));
			} else if ("open".equals(qName)) {
				_quote.setOpen(new Float(val));
			} else if ("low".equals(qName)) {
				_quote.setLow(new Float(val));
			} else if ("last".equals(qName)) {
				_quote.setClose(new Float(val));
			} else if ("volume".equals(qName)) {
				_quote.setVolume(new Long(val));
			} else if ("company".equals(qName)) {
				_liveQuote.setConame(new String(val));
			} else if ("trade_date_utc".equals(qName)) {
			    try {
					_quote.setDate(DATE_FMT.parse(new String(val)));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}

		}
	}
	
}
