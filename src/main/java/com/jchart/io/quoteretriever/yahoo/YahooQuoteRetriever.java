package com.jchart.io.quoteretriever.yahoo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import com.jchart.io.quoteretriever.IQuoteRetriever;
import com.jchart.model.Quote;
import com.jchart.model.QuoteDataModel;

/**
 * @author <a href="mailto:paul.russo@jchart.com>Paul Russo</a>
 * @since May 20, 2017
 */
public class YahooQuoteRetriever implements IQuoteRetriever {

  // https://finance.yahoo.com/quote/AA/history?p=AA
   
   private DateFormat _downloadDateFmt = new SimpleDateFormat("yyyy-MM-dd");
   
   private static final String SESSION_URL = "https://finance.yahoo.com";
   private static final String CRUMB_URL = "https://query1.finance.yahoo.com/v1/test/getcrumb";
   private static final String DOWNLOAD_URL = "https://query1.finance.yahoo.com/v7/finance/download/";
   
   @Override
   public List<Quote> getLocalQuotes(BufferedReader br, int maxQuotes) throws Exception {
      return null;
   }

   @Override
   public List<Quote> getRemoteQuotes(String ticker, int maxQuotes) throws Exception {
      List<Quote> retval = null;
      QuotesIn quotesIn = remoteQuoteRequest(ticker, maxQuotes);
      retval = quotesIn.getQuotes();
      return retval;
   }
   
   @Override
   public void saveRemoteQuotes(String ticker, int maxQuotes, String savePath) throws Exception {
      QuotesIn quotesIn = remoteQuoteRequest(ticker, maxQuotes, true); // save remote quotes
      List<String> quotes = quotesIn.getQuoteLines();
      try (BufferedWriter bw =
         new BufferedWriter(new FileWriter(savePath))) {
         quotes.forEach(q -> {
            try {
               bw.write(q + "\n");
            } catch (IOException e) {
               e.printStackTrace();
            }
         });
      }
   }

   private SessionInfo getSessionInfo() throws Exception {
      SessionInfo retval = new SessionInfo();
      String cookie = getCookie();
      if (cookie == null) {
         for (int i=0;i<5;i++) {
            TimeUnit.SECONDS.sleep(2);
            cookie = getCookie();
            if (cookie != null) {
               break;
            }
         }
      }
      String crumb = null;
      if (cookie != null) {
         crumb = getCrumb(cookie);
      } else {
         throw new IllegalStateException("cookie = null");
      }
      if (crumb == null) {
         throw new IllegalStateException("crumb = null");
      }
      retval.setCookie(cookie);
      retval.setCrumb(crumb);
      return retval;
   }
   
   
   private String getEodUrlString(String ticker, int maxQuotes, String crumb) throws Exception {
      String retval = null;
      StringBuilder sb = new StringBuilder();
      int numRequestQuotes = maxQuotes * 2;
      QuoteDataModel.setMaxQuotes(maxQuotes);
      LocalDate period2Date = LocalDate.now();
      LocalDate period1Date = period2Date.minusDays(numRequestQuotes);
      long period1 = getUnixTimeStamp(period1Date);
      long period2 = getUnixTimeStamp(period2Date);
      sb.append(DOWNLOAD_URL)
         .append(ticker)
         .append("?period1=")
         .append(period1)
         .append("&period2=")
         .append(period2)
         .append("&interval=1d&events=history")
         .append("&crumb=")
         .append(crumb);
       retval = sb.toString();
      return retval;
   }

   private String getCrumb(String cookie) throws Exception {
      String retval = null;
      URLConnection connection = new URL(CRUMB_URL)
            .openConnection();
      connection.setRequestProperty("Cookie", cookie);
      try (BufferedReader br =
            new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            retval = br.readLine();
      }
      return retval;
   }
   
   private QuotesIn remoteQuoteRequest(String ticker, int maxQuotes) throws Exception {
      return remoteQuoteRequest(ticker, maxQuotes, false); //do not save quotes
   }
   
   private QuotesIn remoteQuoteRequest(String ticker, int maxQuotes, boolean saveQuotes) throws Exception {
      QuotesIn retval = null;
      SessionInfo sessionInfo = getSessionInfo();
      RequestInfo requestInfo = new RequestInfo(ticker, maxQuotes);
      String eodUrlStr = getEodUrlString(ticker, maxQuotes, sessionInfo.getCrumb());
      URLConnection connection = new URL(eodUrlStr)
            .openConnection();
      connection.setRequestProperty("Cookie", sessionInfo.getCookie());
      try (BufferedReader br =
            new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
         retval = getQuotes(br, requestInfo, saveQuotes);
      }
      return retval;
   }


   /**
    * reads local or remote based on BufferedReader
    */
   private QuotesIn getQuotes(BufferedReader br, RequestInfo requestInfo, boolean saveQuotes) throws Exception {
      QuotesIn retval = new QuotesIn();
      String inputLine;
      while ((inputLine = br.readLine()) != null) {
         if (inputLine.trim().equals("Date,Open,High,Low,Close,Adj Close,Volume")) {
            if (saveQuotes) {
               retval.addQuoteLine(inputLine);
            }
            break;
         }
      }
      int i = 0;
      long priorQuoteDt = 0l;
      System.out.print(requestInfo.getTicker() + " ");
      while ((inputLine = br.readLine()) != null) {
         if (i == 21) {
            System.out.print(".");
            i = 0;
         }
         if (inputLine.startsWith("<!--")) {
            continue;
         }
         i++;
         try {   
            Quote quote = parseQuoteIn(inputLine);
            if (quote.getDate().getTime() != priorQuoteDt) { // no dups
               priorQuoteDt = quote.getDate().getTime();
               if (saveQuotes) {
                  retval.addQuoteLine(inputLine);
               } else {
                  retval.addQuote(quote);
               }
            }
         } catch (Exception e) {
            // skip it
         }
      }
      // resize the list to match maxQuotes
      if (saveQuotes) {
         int listSize = retval.getQuoteLines().size();
         int delSize = listSize - requestInfo.getMaxQuotes();
         if (delSize > 0) {
            retval.getQuoteLines().subList(0, delSize +1).clear(); // header
         }
      } else {
         int listSize = retval.getQuotes().size();
         int delSize = listSize - requestInfo.getMaxQuotes();
         if (delSize > 0) {
            retval.getQuotes().subList(0, delSize).clear();
         }
      }
      System.out.println();
      return retval;
   }


   private String getCookie() throws Exception {
      String retval = null;
      URLConnection connection = new URL(SESSION_URL)
            .openConnection();
      Map<String,List<String>> map = connection.getHeaderFields();
      List<String> cookies = map.get("Set-Cookie");
      if (cookies != null) {
         retval = cookies.get(0);
      }
      return retval;
   }
   
   private long getUnixTimeStamp(LocalDate localDate) throws Exception {
      long retval = 0l;
      Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
      retval = date.getTime() / 1000; 
      return retval;
   }

   private Quote parseQuoteIn(String s) throws Exception {
      Quote retval = new Quote();
      StringTokenizer st = new StringTokenizer(s, ",");
      Date date = _downloadDateFmt.parse(st.nextToken());
      float open = new Float(st.nextToken()).floatValue();
      float hi = new Float(st.nextToken()).floatValue();
      float low = new Float(st.nextToken()).floatValue();
      float adjClose = new Float(st.nextToken()).floatValue();
      float close = new Float(st.nextToken()).floatValue();
      long volume = new Float(st.nextToken()).longValue();
      retval.setDate(date);
      retval.setOpen(open);
      retval.setHi(hi);
      retval.setLow(low);
      retval.setClose(adjClose);
      retval.setVolume(volume);

/*      // close price adjusted for dividends and splits
      float ratio = (float) FormatUtils.round(adjClose / close, 1000);
      if (ratio != 1f) {
         retval.setOpen(open * ratio);
         retval.setHi(hi * ratio);
         retval.setLow(low * ratio);
         retval.setClose(adjClose);
      }
*/      return retval;
   }

}
