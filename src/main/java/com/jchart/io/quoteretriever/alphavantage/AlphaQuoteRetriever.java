package com.jchart.io.quoteretriever.alphavantage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import com.jchart.io.quoteretriever.IQuoteRetriever;
import com.jchart.model.JchartModelFacade;
import com.jchart.model.Quote;

/**
 * @author <a href="mailto:paul.russo@jchart.com>Paul Russo</a>
 * @since Feb 10, 2018
 */
public class AlphaQuoteRetriever implements IQuoteRetriever {

   private final static String EOD_URL = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&outputsize=full&datatype=csv&symbol=";

   private DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
   
   @Override
   public List<Quote> getRemoteQuotes(String ticker, int maxQuotes)
         throws Exception {
      List<Quote> retval = null;
      String apiKey = JchartModelFacade.getJchartProperty("alphavantage.apikey");
      String eodUrlStr = EOD_URL + ticker + "&apikey=" + apiKey;
      URLConnection connection = new URL(eodUrlStr).openConnection();
      try (BufferedReader br = new BufferedReader(
            new InputStreamReader(connection.getInputStream()))) {
         retval = getQuotes(br, maxQuotes);
      }
      return retval;
   }

   private List<Quote> getQuotes(BufferedReader br, int maxQuotes) throws Exception {
      List<Quote> retval = new ArrayList<>();
      String inputLine;
      int i = 0;
      // skip header
      br.readLine();
      while ((inputLine = br.readLine()) != null) {
         if (i == maxQuotes) {
            break;
         }
         if (i % 21 == 0) {
            System.out.print(".");
         }
         i++;
         Quote quote = parseQuoteIn(inputLine);
         retval.add(quote);
      }
      Collections.reverse(retval);
      return retval;
   }
   

   private Quote parseQuoteIn(String s) throws Exception {
      Quote retval = new Quote();
      //Date,Open,High,Low,Close,Volume
      StringTokenizer st = new StringTokenizer(s, ",");
      Date date = DATE_FORMAT.parse(st.nextToken());
      float open = new Float(st.nextToken()).floatValue();
      float hi = new Float(st.nextToken()).floatValue();
      float low = new Float(st.nextToken()).floatValue();
      float close = new Float(st.nextToken()).floatValue();
      long volume = new Float(st.nextToken()).longValue();
      retval.setDate(date);
      retval.setOpen(open);
      retval.setHi(hi);
      retval.setLow(low);
      retval.setClose(close);
      retval.setVolume(volume);
      return retval;
   }

@Override
public List<Quote> getLocalQuotes(BufferedReader br, int maxQuotes) throws Exception {
	// TODO Auto-generated method stub
	return null;
}

@Override
public void saveRemoteQuotes(String ticker, int maxQuotes, String savePath) throws Exception {
	// TODO Auto-generated method stub
	
}

}