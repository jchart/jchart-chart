package com.jchart.io.factory.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.jchart.io.IoJchartBase;
import com.jchart.io.factory.IoFactoryIntr;
import com.jchart.io.quoteretriever.IQuoteRetriever;
import com.jchart.io.quoteretriever.yahoo.YahooQuoteRetriever;
import com.jchart.model.JchartComposite;
import com.jchart.model.Quote;
import com.jchart.model.QuoteDataModel;

/**
 * concrete implementation of IoFactory
 */
public class IoJchartPsr extends IoJchartBase implements IoFactoryIntr {

   protected boolean getEodQuotes(String sbl, JchartComposite jchartComposite)
         throws Exception {
      boolean retval = false;
      QuoteDataModel quoteDataModel = jchartComposite.getQuoteDataModel();
      quoteDataModel.setNumQuotesIn(0);
      int maxQuotes = QuoteDataModel.getMaxQuotes();
      if (sbl.equalsIgnoreCase("_SPX")) {
         sbl = "_GSPC";
      }

      List<Quote> quotes = null;
      // read local quotes 
      if (QuoteDataModel.isLocal()) {
         quotes = getLocalQuotes(sbl, maxQuotes);
      } else {
         String savePath = null;
         IQuoteRetriever quoteRetriever = new YahooQuoteRetriever();
         // save remote quotes
         if (QuoteDataModel.saveQutoes() && (_dataDir != null)) {
            savePath = _dataDir + sbl.substring(0, 1).toLowerCase()
                  + File.separator + sbl.toLowerCase() + ".csv";
            quoteRetriever.saveRemoteQuotes(sbl, maxQuotes, savePath);
            retval = true;
         } else {
            // read remote quotes
            quotes = getRemoteQuotes(sbl, maxQuotes);
         }
         if (quotes != null) {
            int numQutoes = quotes.size();
            System.out.println(numQutoes);
            if (numQutoes > 0) {
               quotes.forEach(quoteDataModel::setQuoteIn);
               //quoteDataModel.reverseQuotes();
               retval = true;
            }      
         }         
      }
      return retval;
   }

   private List<Quote> getRemoteQuotes(String sbl, int maxQuotes)
         throws Exception {
      List<Quote> retval = null;
      IQuoteRetriever quoteRetriever = new YahooQuoteRetriever();
      int tries = 1;
      for (int i = 0; i < 3; i++) {
         retval = quoteRetriever.getRemoteQuotes(sbl, maxQuotes);
         if (retval.size() >= 0) {
            break;
         } else {
            System.out.println("tries: " + tries);
            tries++;
         }
      }
      return retval;
   }

   private List<Quote> getLocalQuotes(String sbl, int maxQuotes) throws Exception {
      List<Quote> retval = null;
      String sblPath = (sbl.substring(0, 1) + "/" + sbl + ".csv").toLowerCase();
      IQuoteRetriever quoteRetriever = new YahooQuoteRetriever();
      try (ZipFile zipFile = new ZipFile(_dataDir + "jchart-data.zip")) {
         ZipEntry zipEntry = zipFile.getEntry(sblPath);
         if (zipEntry != null) {
            try (BufferedReader br =
                  new BufferedReader(new InputStreamReader(zipFile.getInputStream(zipEntry)))) {
               retval = quoteRetriever.getLocalQuotes(br, maxQuotes);
            }
         }
      }            
      return retval;
   }

}
