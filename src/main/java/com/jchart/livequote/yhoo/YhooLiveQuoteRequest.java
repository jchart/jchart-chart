package com.jchart.livequote.yhoo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

import com.jchart.livequote.LiveQuote;
import com.jchart.livequote.LiveQuoteRequest;
import com.jchart.model.Quote;

public class YhooLiveQuoteRequest implements LiveQuoteRequest {

   private static final String BASE_URL = "http://download.finance.yahoo.com/d/quotes.csv?f=ohgl1vnd1&s=";
   private DateFormat _downloadDateFmt = new SimpleDateFormat("MM/dd/yyyy");

   @Override
   public LiveQuote getLiveQuote(String ticker) {
      LiveQuote retval = null;
      try {
         StringBuffer urlString = new StringBuffer();
         urlString.append(BASE_URL).append(ticker);
         URL url = new URL(urlString.toString());
         URLConnection connection = url.openConnection();
         try (BufferedReader br = new BufferedReader(
               new InputStreamReader(connection.getInputStream()))) {
            String quoteLine = br.readLine();
            if (!quoteLine.contains("N/A")) {
               retval = parseQuote(quoteLine);
            }
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
      return retval;
   }

   private LiveQuote parseQuote(String quoteLine) throws ParseException {
      LiveQuote retval = new LiveQuote();
      StringTokenizer st = new StringTokenizer(quoteLine, ",");
      int numTokens = st.countTokens();
      Quote quote = new Quote();
      quote.setOpen(Float.parseFloat(st.nextToken()));
      quote.setHi(Float.parseFloat(st.nextToken()));
      quote.setLow(Float.parseFloat(st.nextToken()));
      quote.setClose(Float.parseFloat(st.nextToken()));
      quote.setVolume(Long.parseLong(st.nextToken()));
      String coname = st.nextToken().replaceAll("\"", "");
      if (numTokens == 8) {
         st.nextToken();
      }
      Date date = _downloadDateFmt
            .parse(st.nextToken().trim().replaceAll("\"", ""));
      quote.setDate(date);
      retval.setConame(coname);
      retval.setQuote(quote);
      return retval;
   }

}
