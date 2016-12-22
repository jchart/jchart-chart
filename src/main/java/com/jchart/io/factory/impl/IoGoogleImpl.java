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
import java.util.Date;
import java.util.StringTokenizer;

import com.jchart.io.IoJchartBase;
import com.jchart.model.JchartComposite;
import com.jchart.model.Quote;
import com.jchart.model.QuoteDataModel;

/**
 * @author <a href="mailto:paul.russo@jchart.com">Paul S. Russo</a>
 *
 * @created Oct 2, 2008
 */
public class IoGoogleImpl extends IoJchartBase {

   private static String START_DATE = "startdate=Jan+01+1990&";
   private static String BASE_URL = "http://finance.google.com/finance/historical?";// q=AA&startdate=Jan+01+1990&enddate=";
   private DateFormat _downloadDateFmt = new SimpleDateFormat("yyyy-MM-dd");

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
      StringBuffer urlString = new StringBuffer();
      urlString.append(BASE_URL).append("q=").append(sbl).append("&")
            .append(START_DATE).append(endDate.toString())
            .append("&output=csv");
      URL url = new URL(urlString.toString());
      InputStreamReader isrUrlFH = null;
      BufferedReader brUrlFH = null;
      BufferedWriter bw = null;
      InputStream is = null;
      try {
         is = url.openStream();
         isrUrlFH = new InputStreamReader(is);
         brUrlFH = new BufferedReader(isrUrlFH);
         if (QuoteDataModel.saveQutoes() && (_dataDir != null)) {
            String sblPath = _dataDir + sbl.substring(0, 1).toLowerCase()
                  + File.separator + sbl.toLowerCase() + ".csv";
            bw = new BufferedWriter(new FileWriter(sblPath));
            numQuotes = readQuotes(brUrlFH, bw, quoteDataModel);
         } else {
            numQuotes = readQuotes(brUrlFH, quoteDataModel);
         }
      } catch (IOException ioe) {
         System.out.println(ioe);
         numQuotes = 0;
      } finally {
         if (brUrlFH != null) {
            brUrlFH.close();
            brUrlFH = null;
         }
         if (isrUrlFH != null) {
            isrUrlFH.close();
            brUrlFH = null;
         }
         if (is != null) {
            is.close();
            is = null;
         }
         if (bw != null) {
            bw.close();
            bw = null;
         }
      }
      if (numQuotes > 0) {
         quoteDataModel.reverseQuotes();
         retval = true;
      }
      return retval;
   }

   private int readQuotes(BufferedReader br, QuoteDataModel quoteDataModel)
         throws IOException {
      int numQuotes = 0;
      String inputLine = br.readLine();
      while ((inputLine = br.readLine()) != null) {
         System.out.println(inputLine);
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
      // TODO Auto-generated method stub
      return 0;
   }

   public void init(String cgiDir, String tickerDir, boolean useCgi,
         boolean isApplet, String dataSource) {
      // TODO Auto-generated method stub

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

}
