package com.jchart.model.indicator;

import com.jchart.model.JchartComposite;
import com.jchart.model.Quote;
import com.jchart.model.QuoteDataModel;

/**
 * @author <a href="mailto:paul.russo.@jchart.com">Paul S. Russo</a>
 * @since Oct 24, 2012
 * 
 * PSR - MoneyFlow 
 * I designed this indicator after reflecting on candlestick charts and volume - Paul Russo
 * 
 */
public class PsrMoneyFlow extends DateValue {
	
    private Histogram _histogram;

    public PsrMoneyFlow(JchartComposite jchartComposite) {
    	super(jchartComposite.getQuoteDataModel().getNumQuotes());
        QuoteDataModel quoteDataModel = jchartComposite.getQuoteDataModel();
        int numQuotes = quoteDataModel.getNumQuotes();
        Quote[] quotes = quoteDataModel.getQuotes();
        calcMoneyFlow(numQuotes, quotes);
    }

    private void calcMoneyFlow(int qListSize, Quote[] quotes) {
        int kSmooth = 1;
        int kMA = 1;
        float range;
        float r = 0; //resistance, body, support
        float b = 0; //resistance, body, support
        float s = 0; //resistance, body, support
        float mfAccum = 0;
        float mfAccumPrior = 0;
        float mfSmooth;
        for (int i = 0; i < qListSize; i++) {
            range = quotes[i].getHi() - quotes[i].getLow();
            if (range > 0) {
                if (quotes[i].getClose() > quotes[i].getOpen()) {
                    r = ((quotes[i].getHi() - quotes[i].getClose()) / range) * -(quotes[i].getVolume());
                    b = ((quotes[i].getClose() - quotes[i].getOpen()) / range) * (quotes[i].getVolume() * 2f);
                    s = ((quotes[i].getOpen() - quotes[i].getLow()) / range) * (quotes[i].getVolume());
                } else {
                    r = ((quotes[i].getHi() - quotes[i].getOpen()) / range) * -(quotes[i].getVolume());
                    b = ((quotes[i].getOpen() - quotes[i].getClose()) / range) * -(quotes[i].getVolume() * 2f);
                    s = ((quotes[i].getClose() - quotes[i].getLow()) / range) * (quotes[i].getVolume());
                }
            }
            mfAccum = mfAccum + r + b + s;
            mfSmooth = ((mfAccumPrior * kSmooth) + mfAccum) / kMA;
            mfAccumPrior = mfAccum;
            dt[i] = quotes[i].getDate();
            val[i] = mfSmooth / 1000000;
        }
        Ema ema = new Ema(dt, val, qListSize, 21, 0);
        _histogram = new Histogram(dt, val, ema.dt, ema.val, qListSize);
        for (int i = 0; i < qListSize; i++) {
            dt[i] = _histogram.dt[i];
            val[i] = _histogram.val[i];
        }
    }

    public DateValue getHistogram() {
        return _histogram;
    }
}
