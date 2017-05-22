/**
 * 
 */
package com.jchart.io.factory.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.StringTokenizer;

import com.jchart.io.IoJchartBase;
import com.jchart.model.JchartComposite;
import com.jchart.model.JchartModelFacade;
import com.jchart.model.Quote;
import com.jchart.model.QuoteDataModel;

/**
 * @author <a href="mailto:paul.russo@jchart.com">Paul S. Russo</a>
 *
 * @created Oct 2, 2008
 */
public class IoGoogleImpl extends IoJchartBase {

   // "http://finance.google.com/finance/historical?";// q=AA&startdate=Jan+01+1990&enddate=";
   private DateFormat _downloadDateFmt = new SimpleDateFormat("dd-MMM-yy");

   @Override
   protected boolean getEodQuotes(String sbl, JchartComposite jchartComposite)
         throws Exception {
      boolean retval = false;
      QuoteDataModel quoteDataModel = jchartComposite.getQuoteDataModel();

      if (sbl.startsWith("_")) {
         sbl = "." + sbl.substring(1);
      }
      int numQuotes = 0;
      // ddmmyyyy;
      String currentDate = getCurDate();
      StringBuffer endDate = new StringBuffer();
      endDate.append(currentDate.substring(0, 2)).append("+");
      endDate.append(currentDate.substring(2, 4)).append("+");
      endDate.append(currentDate.substring(4, 8)).append("+");
      // &enddate=Oct+3%2C+2008";
      String urlString = getEodUrlStr(sbl, QuoteDataModel.getMaxQuotes());
      URL url = new URL(urlString.toString());
      BufferedWriter bw = null;
      if (QuoteDataModel.saveQutoes() && (_dataDir != null)) {
         String sblPath = _dataDir + sbl.substring(0, 1).toLowerCase()
               + File.separator + sbl.toLowerCase() + ".csv";
         bw = new BufferedWriter(new FileWriter(sblPath));
      }

      try (BufferedReader br =
            new BufferedReader(new InputStreamReader(url.openStream()))) {

         String inputLine = br.readLine();
         while ((inputLine = br.readLine()) != null) {
            Quote quote = null;
            try {
               quote = parseQuoteIn(inputLine);
               quoteDataModel.setQuoteIn(quote);
               numQuotes++;
               /*
                * if (bw != null) { bw.write(inputLine + "\n"); }
                */ } catch (Exception e) {
               e.printStackTrace();
               // just skip it
            }

            if (numQuotes == QuoteDataModel.getMaxQuotes()) {
               break;
            }
         }
      }
      if (numQuotes > 0) {
         quoteDataModel.reverseQuotes();
         retval = true;
      }
      return retval;
   }

   protected int readQuotes(BufferedReader br, QuoteDataModel quoteDataModel)
         throws IOException {
      int numQuotes = 0;
      String inputLine = br.readLine();
      while ((inputLine = br.readLine()) != null) {
         Quote quote = null;
         try {
            quote = parseQuoteIn(inputLine);
            quoteDataModel.setQuoteIn(quote);
            numQuotes++;
            /*
             * if (bw != null) { bw.write(inputLine + "\n"); }
             */ } catch (Exception e) {
            e.printStackTrace();
            // just skip it
         }

         if (numQuotes == QuoteDataModel.getMaxQuotes()) {
            break;
         }
      }
      // TODO Auto-generated method stub
      return numQuotes;
   }

   private int readQuotes(BufferedReader brUrlFH, BufferedWriter bw,
         QuoteDataModel quoteDataModel) {
      return 0;
   }

   protected Quote parseQuoteIn(String s) throws Exception {
      // Date,Open,High,Low,Close,Volume
      Quote retval = new Quote();
      StringTokenizer st = new StringTokenizer(s, ",");
      Date date = _downloadDateFmt.parse(st.nextToken());
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

   protected String getEodUrlStr(String sbl, int maxQuotes) {
      String retval = null;
      StringBuilder sb = new StringBuilder();
      String baseUri = JchartModelFacade.getEodBaseUri();
      String currentDate = getCurDate();
      StringBuffer endDate = new StringBuffer();
      endDate.append(currentDate.substring(0, 2)).append("+");
      endDate.append(currentDate.substring(2, 4)).append("+");
      endDate.append(currentDate.substring(4, 8)).append("+");
      // &enddate=Oct+3%2C+2008";
      String startDate = getStartDate(maxQuotes);
      sb.append(baseUri).append("?q=")
         .append(sbl)
         .append("&startdate=")
         .append(startDate)
         .append("&enddate=")
         .append(endDate.toString())
         .append("&output=csv");
      retval = sb.toString();
      return retval;
   }
   
   private String getStartDate(int maxQuotes) {
      String retval = null;
      LocalDateTime localDateTime = LocalDateTime.now();
      LocalDate today = localDateTime.toLocalDate();
      LocalDate startDate = today.minusDays(maxQuotes * 2);
      StringBuilder sb = new StringBuilder();
      sb.append(startDate.getMonthValue())
         .append("+")
         .append(startDate.getMonthValue())
         .append("+")
         .append(startDate.getYear());
      retval = sb.toString();
      
      return retval;
   }

}
