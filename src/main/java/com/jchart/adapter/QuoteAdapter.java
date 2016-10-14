/**
 * 
 */
package com.jchart.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jchart.model.Quote;
import com.jchart.util.FormatUtils;

/**
 * @author <a href="mailto:paul.russo@jchart.com">Paul S. Russo</a>
 *
 * @created Aug 19, 2007
 */
public class QuoteAdapter {
    private static SimpleDateFormat MMDDYY = new SimpleDateFormat( "MM/dd/yy" ); 
	
	public static Map<Date, Quote> getQuoteMap(List<Quote> quotes) {
		Map<Date, Quote> retval = new HashMap<Date, Quote>();
		for (Quote quoteDataBean : quotes) {
			retval.put(quoteDataBean.getDate(), quoteDataBean);
		}
		return retval;
	}

	public static Date getJchartDate(Date jchartDate) {
        Date date = null;
        String dateStr = FormatUtils.formatDate(jchartDate);
        try {
            date = MMDDYY.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }
	
	public static Date[] getJchartDates(Date[] dates) {
		Date[] retval = new Date[dates.length];
		for (int i=0;i<dates.length;i++) {
			retval[i] = getJchartDate(dates[i]);
		}
		return retval;
	}

}
