package com.jchart.model.indicator;

import java.util.Date;

import com.jchart.io.TickerListener;
import com.jchart.io.TickerRequest;
import com.jchart.model.JchartComposite;
import com.jchart.model.Quote;
import com.jchart.model.QuoteDataModel;


public class RelativeStrength extends DateValue implements TickerListener {
	
    private JchartComposite _jchartComposite;
    private Sma _ma1;
    private Sma _ma2;

	private int _ma1Num = 10;
	private int _ma2Num = 60;

    public RelativeStrength(JchartComposite jchartComposite) {
        super(jchartComposite.getQuoteDataModel().getNumQuotes());
        _jchartComposite = jchartComposite;
    }

    public void setMa1Num(int ma1Num) {
    	_ma1Num = ma1Num;
    	
    }

    public void setMa2Num(int ma2Num) {
    	_ma2Num = ma2Num;
    }
    
    public void init() {
        QuoteDataModel quoteDataModel = _jchartComposite.getQuoteDataModel();
        if (quoteDataModel.getNumRsQuotes() == 0) {
			_jchartComposite.getChartTypeModel().isRs(true);
			TickerRequest tickerRequest = new TickerRequest(_jchartComposite,this);
			tickerRequest.requestBlock(quoteDataModel.getRsSymbol() );
		}
		_jchartComposite.getChartTypeModel().isRs(false);
		Quote[] quote = quoteDataModel.getQuotes();
		Quote[] rsQuote = quoteDataModel.getRsQuotes();
		
		int quoteNum = 0;
		int rsQuoteNum = 0;
		Date rsDt = rsQuote[rsQuoteNum].getDate();
		Date quoteDt = quote[quoteNum].getDate();
		do {
			if (rsDt == null || quoteDt == null) {
				if (rsDt == null) {
					val[quoteNum] = val[quoteNum -1];
				}
				break;
			}
			if (rsDt.before(quoteDt)) {
				//missing quote, skip rsQuotes
				do { 		 
					rsQuoteNum++;
					rsDt = rsQuote[rsQuoteNum].getDate();
				} while ( ((rsDt.before(quoteDt) 
					&& (rsQuoteNum < quoteDataModel.getNumRsQuotes()))
					|| rsDt == null));

			} else if (rsDt.after(quoteDt)) {
				//missing rsQuotes , add rsQuotes
				do {
					quoteDt = quote[quoteNum].getDate();
					val[quoteNum] = quote[quoteNum].getClose() / rsQuote[rsQuoteNum].getClose();
					dt[quoteNum] = quote[quoteNum].getDate();
					quoteNum++;		
				} while ( (rsDt.before(quoteDt)) 
					&& (rsQuoteNum < quoteDataModel.getNumRsQuotes()) );
			} else {
				//quote dates match
				dt[quoteNum] = quote[quoteNum].getDate();
				val[quoteNum] = quote[quoteNum].getClose() / rsQuote[rsQuoteNum].getClose();
				quoteNum++;
				rsQuoteNum++;
				if (quoteNum == quoteDataModel.getNumQuotes() ) {
					break;
				}
					
				rsDt = rsQuote[rsQuoteNum].getDate();
				quoteDt = quote[quoteNum].getDate();
			}
		} while (quoteNum < quoteDataModel.getNumQuotes() );
		_ma1 = new Sma (dt, val, quoteDataModel.getNumQuotes(), _ma1Num);
		_ma2 = new Sma (dt, val, quoteDataModel.getNumQuotes(), _ma2Num);
    }

	/**
	 * Y2K hack
	 */
	private double getDate(float f) {
		if (f < 600000 && f > 0)
			f += 1000000;
		return f;
	}

	public DateValue getVal1() {
		return _ma1;
	}
	public DateValue getVal2() {
		return _ma2;
	}

    public void requestComplete(boolean found) {
    }
    
    
}
