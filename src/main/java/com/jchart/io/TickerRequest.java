package com.jchart.io;

import com.jchart.io.factory.IoFactory;
import com.jchart.model.ChartTypeModel;
import com.jchart.model.JchartComposite;
import com.jchart.model.QuoteDataModel;

public class TickerRequest implements Runnable {

   private TickerListener _tickerListener;
   private String _ticker;
   private boolean _tickerFound;
   private JchartComposite _jchartComposite;
   private ChartTypeModel _chartTypeModel;
   private QuoteDataModel _quoteDataModel;

   public TickerRequest(JchartComposite jchartComposite,
         TickerListener tickerListener) {
      _tickerListener = tickerListener;
      _jchartComposite = jchartComposite;
      _chartTypeModel = _jchartComposite.getChartTypeModel();
      _quoteDataModel = _jchartComposite.getQuoteDataModel();
   }

   public void requestCompare(String baseTicker, String compareTicker) {
      // for RS, only call this if RS quotes are null
      boolean isIntraday = _jchartComposite.getTimeFrameModel().isIntraday();
      _chartTypeModel.isComparison(false);
      if (isIntraday)
         request(baseTicker, false);
      else if (!baseTicker.equalsIgnoreCase(_quoteDataModel.getTicker()))
         request(baseTicker, false);
      else
         _tickerFound = true;
      _chartTypeModel.isComparison(true);
      if (_tickerFound) {
         if (isIntraday)
            request(compareTicker, true);
         else if (!compareTicker
               .equalsIgnoreCase(_quoteDataModel.getCompareTicker()))
            request(compareTicker, true);
      }
   }

   public void requestBlock(String ticker) {
      request(ticker, true);
   }

   public void request(String ticker) {
      request(ticker, false);
   }

   private void request(String ticker, boolean blocking) {
      if (ticker == null) {
         return;
      }
      _ticker = ticker;
      if (blocking) {
         tickerRequest();
      } else {
         Thread t = new Thread(this);
         t.start();
      }
   }

   public void run() {
      tickerRequest();
   }

   private void tickerRequest() {
      try {
         boolean isIntraday = _jchartComposite.getTimeFrameModel().isIntraday();
         if (isIntraday)
            _tickerFound = IoFactory.getInstance(_ticker)
                  .tickerRequestIntraday(_ticker, _jchartComposite);
         else {
            _tickerFound = IoFactory.getInstance(_ticker)
                  .tickerRequestEod(_ticker, _jchartComposite);
         }
         if (_quoteDataModel.isLiveQuote()) {
            _quoteDataModel.liveQuote();
         }
         _tickerListener.requestComplete(_tickerFound);
      } catch (Exception e) {
         e.printStackTrace();
      }

   }

   /**
    * 
    */
   public void requestRs() {
      // quoteDataModel.getTicker(),
      // quoteDataModel.getRequestRsSymbol()

   }

}
