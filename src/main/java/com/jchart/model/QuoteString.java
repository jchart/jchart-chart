package com.jchart.model;
import com.jchart.util.FormatUtils;

public class QuoteString {
    public String dt;
    public String op;
    public String hi;
    public String lo;
    public String cl;
    public String vo;
    public String chg;
    public String curPos;
    private Quote quote;

    public QuoteString(boolean isIntraday, Quote quote, float priorCl) {
    	this.quote = quote;
        Double f;
        dt = "Dt: " + FormatUtils.formatDate(quote.getDate()) + " ";
        f = new Double(quote.getOpen()); 
        op = "Op: " + FormatUtils.formatDecimal(f.toString(),3) + " ";
        f = new Double(quote.getHi()); 
        hi = "Hi: " + FormatUtils.formatDecimal(f.toString(),3) + " ";
        f = new Double(quote.getLow()); 
        lo = "Low: " + FormatUtils.formatDecimal(f.toString(),3) + " ";
        f = new Double(quote.getClose());
        cl = "Lst: " + FormatUtils.formatDecimal(f.toString(),3) + " ";
        f = new Double(quote.getVolume()); 
//        vo = "Vol: " + FormatUtils.formatDecimal(f.toString(),0) + " ";
		vo = "Vol: " + FormatUtils.formatVol(quote.getVolume()) + " ";
		//FormatUtils.formatVol(f)
        f = new Double(quote.getClose() - priorCl); 
        chg = "Chg: " + FormatUtils.formatDecimal(f.toString(),3) + " ";
    }

    public long getVolume() {
        return quote.getVolume();
    }

    public String toString() {
		return dt + cl + chg + vo ;
	}

}
