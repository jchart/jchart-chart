package com.jchart.io.factory.impl;

import java.io.File;
import java.util.List;

import com.jchart.io.IoJchartBase;
import com.jchart.io.factory.IoFactoryIntr;
import com.jchart.io.quoteretriever.IQuoteRetriever;
import com.jchart.io.quoteretriever.alphavantage.AlphaQuoteRetriever;
import com.jchart.model.JchartComposite;
import com.jchart.model.Quote;
import com.jchart.model.QuoteDataModel;

/**
 * concrete implementation of IoFactory
 */
public class IoJchartPsr extends IoJchartBase implements IoFactoryIntr {
   
   private IQuoteRetriever _quoteRetriever = new AlphaQuoteRetriever();

   protected boolean getEodQuotes(String sbl, JchartComposite jchartComposite)
         throws Exception {
      boolean retval = false;
      QuoteDataModel quoteDataModel = jchartComposite.getQuoteDataModel();
      quoteDataModel.setNumQuotesIn(0);
      int maxQuotes = QuoteDataModel.getMaxQuotes();

      List<Quote> quotes = null;
      // read local quotes 
      if (QuoteDataModel.isLocal()) {
         quotes = getLocalQuotes(sbl, maxQuotes);
      } else {
         String savePath = null;
         
         // save remote quotes
         if (QuoteDataModel.getSaveQuotes() && (_dataDir != null)) {
            savePath = _dataDir + sbl.substring(0, 1).toLowerCase()
                  + File.separator + sbl.toLowerCase() + ".csv";
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
      int tries = 1;
      for (int i = 0; i < 3; i++) {
         retval = _quoteRetriever.getRemoteQuotes(sbl, maxQuotes);
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
      return retval;
   }

}
