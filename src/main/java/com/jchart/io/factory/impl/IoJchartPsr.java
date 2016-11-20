package com.jchart.io.factory.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.jchart.io.IoJchartBase;
import com.jchart.model.JchartComposite;
import com.jchart.model.JchartModelFacade;
import com.jchart.model.Quote;
import com.jchart.model.QuoteDataModel;
import com.jchart.util.FormatUtils;

/**
 * concrete implementation of IoFactory
 */
public class IoJchartPsr extends IoJchartBase
      implements com.jchart.io.factory.IoFactoryIntr {

   private final static String CGI_EOD = "jc_getHist.cgi";
   private DateFormat _downloadDateFmt = new SimpleDateFormat("yyyy-MM-dd");

   protected boolean getEodQuotes(String sbl, JchartComposite jchartComposite)
         throws Exception {
      QuoteDataModel quoteDataModel = jchartComposite.getQuoteDataModel();

      if (sbl.equalsIgnoreCase("_SPX")) {
         sbl = "_GSPC";
      }

      int numQuotes = 0;

      if (QuoteDataModel.isLocal()) {
         if (sbl.charAt(0) == '%') {
            numQuotes = getEodQuotesFile(sbl.substring(1), quoteDataModel);
         } else {
            numQuotes = getEodQuotesZipFile(sbl, quoteDataModel);
            if (numQuotes == 0) {
               numQuotes = getEodQuotesUrl(sbl, quoteDataModel);
            }
         }

      } else {
         int tries = 1;
         for (int i = 0; i < 3; i++) {
            numQuotes = getEodQuotesUrl(sbl, quoteDataModel);
            if (numQuotes > 0) {
               break;
            } else {
               System.out.println("tries: " + tries);
               tries++;
            }
         }
      }
      System.out.println(numQuotes);
      if (numQuotes != 0) {
         quoteDataModel.reverseQuotes();
         return true;
      } else {
         return false;
      }
   }

   /**
    * @param sbl
    * @param quoteDataModel
    */
   private int getEodQuotesZipFile(String sbl, QuoteDataModel quoteDataModel) {
      int numQuotes = 0;
      BufferedReader br = null;
      ZipFile zipFile = null;
      String sblPath = (sbl.substring(0, 1) + "/" + sbl + ".csv").toLowerCase();
      try {
         zipFile = new ZipFile(_dataDir + "jchart-data.zip");
         ZipEntry zipEntry = zipFile.getEntry(sblPath);
         if (zipEntry != null) {
            br = new BufferedReader(
                  new InputStreamReader(zipFile.getInputStream(zipEntry)));
            numQuotes = readQuotes(br, quoteDataModel);
         }
      } catch (IOException ioe) {
         System.out.println(ioe);
         System.out.println(_dataDir + sblPath);
      } finally {
         if (br != null) {
            try {
               br.close();
            } catch (IOException e) {
               e.printStackTrace();
            }
            if (zipFile != null) {
               try {
                  zipFile.close();
               } catch (IOException e) {
                  e.printStackTrace();
               }
            }
         }
      }

      return numQuotes;
   }

   /**
    * @param sbl
    * @param quoteDataModel
    */
   private int getEodQuotesFile(String sbl, QuoteDataModel quoteDataModel) {
      int numQuotes = 0;
      BufferedReader br = null;
      String sblPath = _dataDir + File.separatorChar + sbl + ".csv";
      try {
         br = new BufferedReader(
               new InputStreamReader(new FileInputStream(sblPath)));
         numQuotes = readQuotes(br, quoteDataModel);
      } catch (IOException ioe) {
         System.out.println(ioe);
         System.out.println(_dataDir + sblPath);
      } finally {
         if (br != null) {
            try {
               br.close();
            } catch (IOException e) {
               e.printStackTrace();
            }
         }
      }
      return numQuotes;
   }

   private int getEodQuotesUrl(String sbl, QuoteDataModel quoteDataModel)
         throws Exception {

      System.out.print(sbl.toUpperCase() + ' ');

      int numQuotes = 0;
      int maxQuotes = QuoteDataModel.getMaxQuotes();

      if (sbl != null) {
         sbl.trim();
      }

      if (sbl.indexOf(" ") > -1) {
         return 0;
      }

      String eodUrlStr = getEodUrlStr(sbl, maxQuotes);

      URL url = new URL(eodUrlStr);
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

      return numQuotes;
   }

   /**
    * @return
    */
   protected String getEodUrlStr(String sbl, int maxQuotes) {
      String retval = null;

      // current date
      String ddmmyyyy = getCurDate();
      String dd = ddmmyyyy.substring(0, 2);
      int dd2 = Integer.parseInt(dd) - 1;
      if (dd2 == 0) {
         dd2 = 1;
      }
      Integer mmI = new Integer(ddmmyyyy.substring(2, 4));
      mmI = new Integer(mmI.intValue() - 1);

      int mm = mmI.intValue();
      String yyyyEnd = ddmmyyyy.substring(4, 8);
      int yyyyStart = new Integer(yyyyEnd).intValue() - 3;

      switch (maxQuotes) {
      case 250:
         yyyyStart = new Integer(yyyyEnd).intValue() - 2;

         break;

      case 550:
         yyyyStart = new Integer(yyyyEnd).intValue() - 3;

         break;

      case 1300:
         yyyyStart = new Integer(yyyyEnd).intValue() - 5;

         break;

      case 2600:
         yyyyStart = new Integer(yyyyEnd).intValue() - 10;

         break;

      default:
         yyyyStart = new Integer(yyyyEnd).intValue() - 3;

         break;
      }

      // http://chart.yahoo.com/table.csv?s=cnet&a=01&b=01&c=98&d=01&e=01&f=99&g=d&q=q
      // http://www.ny.frb.org/markets/fxrates/historical/FXtoxml1.cfm?CTCD=JPY&Type=SPOT&time=12:00&dt1=10/29/2003&dt2=10/29/2004

      String fmtStr = null;
      fmtStr = "s=" + sbl + "&d=" + mm + "&e=" + dd + "&f=" + yyyyEnd + "&g=d"
            + "&a=" + mm + "&b=" + dd2 + "&c=" + yyyyStart + "&ignore=.csv";

      if (_useCgi) {
         retval = _cgiDir + CGI_EOD + "?" + fmtStr; // yyStartStr+mm+dd+"&"+yyEnd+mm+dd+"&"+sbl;
      } else {
         String eodDomain = JchartModelFacade.getEodDomain();
         retval = eodDomain + "/table.csv?" + fmtStr;
      }

      return retval;
   }

   private int readQuotes(BufferedReader br, QuoteDataModel quoteDataModel)
         throws IOException {
      return readQuotes(br, null, quoteDataModel);
   }

   private int readQuotes(BufferedReader br, BufferedWriter bw,
         QuoteDataModel quoteDataModel) throws IOException {
      int numQuotes = 0;

      String inputLine;
      while ((inputLine = br.readLine()) != null) {
         if (inputLine.startsWith("Date,Open,High,Low,Close,Volume")) {
            break;
         }
      }

      if (inputLine == null) {
         return 0;
      }

      if (bw != null) {
         bw.write(inputLine + "\n");
      }
      int i = 0;
      quoteDataModel.setNumQuotesIn(0);

      long priorQuoteDt = 0l;
      while ((inputLine = br.readLine()) != null) {
         if (i == 21) {
            System.out.print(".");
            i = 0;
         }
         if (inputLine.startsWith("<!--")) {
            continue;
         }
         i++;

         Quote quote = null;

         try {
            quote = parseQuoteIn(inputLine);
            if (quote.getDate().getTime() != priorQuoteDt) { // no dups
               quoteDataModel.setQuoteIn(quote);
               numQuotes++;
               if (bw != null) {
                  bw.write(inputLine + "\n");
               }
               priorQuoteDt = quote.getDate().getTime();
            }
         } catch (Exception e) {
            // just skip it
         }

         if (numQuotes == QuoteDataModel.getMaxQuotes()) {
            break;
         }
      }

      return numQuotes;
   }

   protected Quote parseQuoteIn(String s) throws Exception {
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
      // Close price adjusted for dividends and splits
      float adjClose = new Float(st.nextToken()).floatValue();
      float ratio = (float) FormatUtils.round(adjClose / close, 1000);
      if (ratio != 1f) {
         retval.setOpen(open * ratio);
         retval.setHi(hi * ratio);
         retval.setLow(low * ratio);
         retval.setClose(adjClose);
      }
      return retval;
   }

}
