package com.jchart.model;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import com.jchart.io.factory.impl.TickerConame;
import com.jchart.livequote.LiveQuote;
import com.jchart.livequote.LiveQuoteRequest;
import com.jchart.livequote.LiveQuoteRequestFactory;
import com.jchart.livequote.yhoo.YhooLiveQuoteRequest;

public class QuoteDataModel {
   private static boolean _isLocal;
   private static boolean _saveQuotes;
   private int _numRsQuotes;
   private String _rsSymbol;
   private static int _maxQuotes;
   private boolean _isIntraday;
   private int _numQuotes;
   private int _numQuotesIn;
   private int _numCompareQuotes;
   private String _ticker;
   private String _compareTicker;
   private Quote[][] _compareQuotes; // 0,1 = raw base,compare 2,3 = relative
                                     // change
   private Quote[] _rsQuotes;
   private Quote[] _quotes;
   private Quote[] _quotesIn;
   private String _tickerConame;
   private boolean _isLiveQuote;

   public void init() {
      _quotes = new Quote[_maxQuotes];
      _quotesIn = new Quote[_maxQuotes];
      _compareQuotes = (new Quote[4][_maxQuotes]);
      _rsQuotes = new Quote[_maxQuotes];
      for (int i = 0; i < _maxQuotes; i++) {
         _quotes[i] = new Quote();
         _compareQuotes[0][i] = new Quote();
         _compareQuotes[1][i] = new Quote();
         _compareQuotes[2][i] = new Quote();
         _compareQuotes[3][i] = new Quote();
         _quotesIn[i] = new Quote();
         _rsQuotes[i] = new Quote();
      }
   }

   private void resetQuotesIn() {
      _numQuotesIn = 0;
      for (int i = 0; i < _maxQuotes; i++) {
         _quotesIn[i].setClose(0);
         _quotesIn[i].setDate(null);
         _quotesIn[i].setHi(0);
         _quotesIn[i].setLow(0);
         _quotesIn[i].setOpen(0);
         _quotesIn[i].setVolume(0);
      }
   }

   public Quote[] getQuotes() {
      return _quotes;
   }

   public Quote[] getRsQuotes() {
      return _rsQuotes;
   }

   public void setQuotes(Quote[] quotes) {
      _quotes = quotes;
   }

   public Quote[][] getCompareQuotes() {
      return _compareQuotes;
   }

   public void calcRs(PlotRange plotRange) {
      calcRs(plotRange.getBeginPlotRec(), plotRange.getEndPlotRec());
   }

   private void calcRs(int beginPlotRec, int endPlotRec) {
      float prevCl0;
      float prevCl1;
      float curCl0 = _compareQuotes[0][beginPlotRec].getClose();
      float curCl1 = _compareQuotes[1][beginPlotRec].getClose();
      float rsVal0 = 100;
      float rsVal1 = 100;

      _compareQuotes[2][beginPlotRec].setClose(rsVal0);
      _compareQuotes[3][beginPlotRec].setClose(rsVal1);
      for (int i = beginPlotRec + 1; i < endPlotRec; i++) {
         prevCl0 = curCl0;
         prevCl1 = curCl1;
         curCl0 = _compareQuotes[0][i].getClose();
         curCl1 = _compareQuotes[1][i].getClose();
         rsVal0 += ((curCl0 / prevCl0) - 1);
         rsVal1 += ((curCl1 / prevCl1) - 1);
         _compareQuotes[2][i].setClose(rsVal0);
         _compareQuotes[3][i].setClose(rsVal1);
         _compareQuotes[2][i].setDate(_compareQuotes[0][i].getDate());
         _compareQuotes[3][i].setDate(_compareQuotes[0][i].getDate());
      }
   }

   public String getCompareTicker() {
      return _compareTicker;
   }

   public static void setMaxQuotes(int maxQuotes) {
      _maxQuotes = maxQuotes;
   }

   public Quote getCompareQuote(int rsNum, int i) {
      return _compareQuotes[rsNum][i];
   }

   public String getTicker() {
      return _ticker;
   }

   public static int getMaxQuotes() {
      return _maxQuotes;
   }

   private void copyQuotes() {
      for (int i = 0; i < _numQuotesIn; i++) {
         _quotes[i].setDate(_quotesIn[i].getDate());
         _quotes[i].setOpen(_quotesIn[i].getOpen());
         _quotes[i].setHi(_quotesIn[i].getHi());
         _quotes[i].setLow(_quotesIn[i].getLow());
         _quotes[i].setClose(_quotesIn[i].getClose());
         _quotes[i].setVolume(_quotesIn[i].getVolume());
      }
      _numQuotes = _numQuotesIn;
      for (int i = _numQuotesIn; i < _maxQuotes; i++) {
         _quotes[i].setDate(null);
         _quotes[i].setOpen(0);
         _quotes[i].setHi(0);
         _quotes[i].setLow(0);
         _quotes[i].setClose(0);
         _quotes[i].setVolume(0);
      }
   }

   private void copyQuotesCompare() {
      // copy quotes
      // percent change
      // match _quotesIn dates to quotes dates using quotes[] as the base
      Date dt1;
      Date dt2;
      int i = 0;
      int j = 0;
      int compareQuoteCount = 0;

      while (i < _numQuotes) {
         dt1 = _quotesIn[j].getDate();
         dt2 = _quotes[i].getDate();

         while (dt1.before(dt2)) {
            j++;

            if (j == _numQuotesIn) {
               break;
            }
            dt1 = _quotesIn[j].getDate();
            dt2 = _quotes[i].getDate();
         }

         if (j == _numQuotesIn) {
            break;
         }

         dt1 = _quotesIn[j].getDate();
         dt2 = _quotes[i].getDate();

         while (dt1.after(dt2)) {
            i++;

            if (i == _numQuotes) {
               break;
            }
            dt1 = _quotesIn[j].getDate();
            dt2 = _quotes[i].getDate();
         }

         if (i == _numQuotes) {
            break;
         }

         // at this point the dates should be equal
         _compareQuotes[0][compareQuoteCount].setDate(_quotes[i].getDate());
         _compareQuotes[0][compareQuoteCount].setOpen(_quotes[i].getOpen());
         _compareQuotes[0][compareQuoteCount].setHi(_quotes[i].getHi());
         _compareQuotes[0][compareQuoteCount].setLow(_quotes[i].getLow());
         _compareQuotes[0][compareQuoteCount].setClose(_quotes[i].getClose());
         _compareQuotes[0][compareQuoteCount].setVolume(_quotes[i].getVolume());
         _compareQuotes[1][compareQuoteCount].setDate(_quotesIn[j].getDate());
         _compareQuotes[1][compareQuoteCount].setOpen(_quotesIn[j].getOpen());
         _compareQuotes[1][compareQuoteCount].setHi(_quotesIn[j].getHi());
         _compareQuotes[1][compareQuoteCount].setLow(_quotesIn[j].getLow());
         _compareQuotes[1][compareQuoteCount].setClose(_quotesIn[j].getClose());
         _compareQuotes[1][compareQuoteCount]
               .setVolume(_quotesIn[j].getVolume());
         i++;
         j++;
         compareQuoteCount++;
      }
      _numCompareQuotes = compareQuoteCount;
   }

   public void isIntraday(boolean b) {
      _isIntraday = b;
      ;
   }

   public boolean isIntraday() {
      return _isIntraday;
   }

   public void requestComplete(String ticker, ChartTypeModel chartTypeModel) {
      ticker = ticker.toUpperCase();
      if (chartTypeModel.isComparison()) {
         _compareTicker = ticker;
         copyQuotesCompare();
      } else if (chartTypeModel.isRs()) {
         _rsSymbol = ticker;
         copyQuotesRs();
      } else {
         _ticker = ticker;
         copyQuotes();
      }
      resetQuotesIn();
      if (!isLiveQuote()) {
         _tickerConame = new TickerConame().get(_ticker);
      }
   }

   public void liveQuote() {
      if (_ticker == null) {
         return;
      }
      LiveQuote liveQuote = null;
      try {
         LiveQuoteRequest liveQuoteRequest = LiveQuoteRequestFactory
               .getLiveQuoteRequest(YhooLiveQuoteRequest.class);
         liveQuote = liveQuoteRequest.getLiveQuote(_ticker);
         if (liveQuote == null) {
            _tickerConame = new TickerConame().get(_ticker);
            return;
         }
         _tickerConame = liveQuote.getConame();
      } catch (Exception e) {
         e.printStackTrace();
         return;
      }
      if (liveQuote.getConame() != null) {
         _tickerConame = _ticker + " " + liveQuote.getConame();
         Quote quote = liveQuote.getQuote();
         if (quote.getDate() == null) {
            return;
         }
         Quote lastQuote = _quotes[_numQuotes - 1];
         Calendar cal1 = Calendar.getInstance();
         Calendar cal2 = Calendar.getInstance();
         cal1.setTime(quote.getDate());
         cal2.setTime(lastQuote.getDate());
         boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
               && cal1.get(Calendar.DAY_OF_YEAR) == cal2
                     .get(Calendar.DAY_OF_YEAR);
         if (!sameDay) {
            if (_numQuotes == _maxQuotes) {
               _quotes = Arrays.copyOf(_quotes, _maxQuotes + 1);
               _quotes[_numQuotes] = new Quote();
            }
            _quotes[_numQuotes].setDate(quote.getDate());
            _quotes[_numQuotes].setOpen(quote.getOpen());
            _quotes[_numQuotes].setHi(quote.getHi());
            _quotes[_numQuotes].setLow(quote.getLow());
            _quotes[_numQuotes].setClose(quote.getClose());
            _quotes[_numQuotes].setVolume(quote.getVolume());

            if (_numQuotes == _maxQuotes) {
               _rsQuotes = Arrays.copyOf(_rsQuotes, _maxQuotes + 1);
               _rsQuotes[_numQuotes] = new Quote();
            }
            _rsQuotes[_numQuotes].setDate(quote.getDate());
            _rsQuotes[_numQuotes].setOpen(quote.getOpen());
            _rsQuotes[_numQuotes].setHi(quote.getHi());
            _rsQuotes[_numQuotes].setLow(quote.getLow());
            _quotes[_numQuotes].setClose(quote.getClose());
            _rsQuotes[_numQuotes].setVolume(quote.getVolume());
 //           _numQuotes += 1;
         }
      } else {
         _tickerConame = _ticker + " "
               + ConameFactory.getConame(_ticker.toUpperCase());
      }

   }

   private void copyQuotesRs() {
      for (int i = 0; i < _numQuotesIn; i++) {
         _rsQuotes[i].setDate(_quotesIn[i].getDate());
         _rsQuotes[i].setOpen(_quotesIn[i].getOpen());
         _rsQuotes[i].setHi(_quotesIn[i].getHi());
         _rsQuotes[i].setLow(_quotesIn[i].getLow());
         _rsQuotes[i].setClose(_quotesIn[i].getClose());
         _rsQuotes[i].setVolume(_quotesIn[i].getVolume());
      }
      _numRsQuotes = _numQuotesIn;
      for (int i = _numQuotesIn; i < _maxQuotes; i++) {
         _rsQuotes[i].setDate(null);
         _rsQuotes[i].setOpen(0);
         _rsQuotes[i].setHi(0);
         _rsQuotes[i].setLow(0);
         _rsQuotes[i].setClose(0);
         _rsQuotes[i].setVolume(0);
      }
   }

   public String getTickerConame() {
      return _tickerConame;
   }

   public Quote getQuote(int recnum) {
      if (recnum >= _numQuotes) {
         return _quotes[_numQuotes - 1];
      } else if (recnum <= 0) {
         return _quotes[0];
      } else {
         return _quotes[recnum];
      }
   }

   public void fillIntradayDummyData() {
      _numQuotesIn = 0;

      int min1 = 30;
      int min2 = 0;

      for (float dt = 930; dt < 1600; dt++) {
         if (min1 < 60) {
            _quotesIn[_numQuotesIn].setDate(null);
            _quotesIn[_numQuotesIn].setOpen(0);
            _quotesIn[_numQuotesIn].setHi(0);
            _quotesIn[_numQuotesIn].setLow(0);
            _quotesIn[_numQuotesIn].setClose(0);
            _quotesIn[_numQuotesIn].setVolume(0);
            _numQuotesIn++;
            min1++;
         } else {
            min2++;

            if (min2 == 40) {
               min1 = 0;
               min2 = 0;
            }
         }
      }
   }

   public void setNumQuotesIn(int i) {
      _numQuotesIn = i;
   }

   public void setNumQuotes(int i) {
      _numQuotes = i;
   }

   public void padIntradayQutoes(int padHour, int hour, int padMin, int min,
         float cl) {
      while (padHour < hour) {
         // fill in minutes to next hour
         for (int j = padMin; j < 60; j++) {
            if (j < 10) {
               _quotesIn[_numQuotesIn].setDate(null);// new float(padHour + "0"
                                                     // + j).floatValue();
            } else {
               _quotesIn[_numQuotesIn].setDate(null); // new float(padHour + ""
                                                      // + j).floatValue();
            }
            _quotesIn[_numQuotesIn].setOpen(cl);
            _quotesIn[_numQuotesIn].setHi(cl);
            _quotesIn[_numQuotesIn].setLow(cl);
            _quotesIn[_numQuotesIn].setClose(cl);
            _quotesIn[_numQuotesIn].setVolume(0);
            _numQuotesIn++;

            if (_numQuotesIn == (_maxQuotes)) {
               break;
            }
         }
         padMin = 0;
         padHour++;
      }

      // fill in quotes to match minutes
      while (padMin < min) {
         if (_numQuotesIn == (_maxQuotes)) {
            break;
         }

         _quotesIn[_numQuotesIn].setOpen(cl);
         _quotesIn[_numQuotesIn].setHi(cl);
         _quotesIn[_numQuotesIn].setLow(cl);
         _quotesIn[_numQuotesIn].setClose(cl);
         _quotesIn[_numQuotesIn].setVolume(0);
         _numQuotesIn++;
         padMin++;
      }
   }

   /**
    * @param quote
    */
   public void setQuoteIn(Quote quote) {
      if (quote.getHi() == Double.POSITIVE_INFINITY)
         return;
      _quotesIn[_numQuotesIn].setDate(quote.getDate());
      _quotesIn[_numQuotesIn].setOpen(quote.getOpen());
      _quotesIn[_numQuotesIn].setHi(quote.getHi());
      _quotesIn[_numQuotesIn].setLow(quote.getLow());
      _quotesIn[_numQuotesIn].setClose(quote.getClose());
      _quotesIn[_numQuotesIn].setVolume(quote.getVolume());
      _numQuotesIn++;
   }

   public void reverseQuotes() {
      int i = 0;
      int j = _numQuotesIn - 1;
      Quote quote = new Quote();

      // swapit
      while (i < j) {
         quote = _quotesIn[i];
         _quotesIn[i] = _quotesIn[j];
         _quotesIn[j] = quote;
         i++;
         j--;
      }
   }

   public int getNumCompareQuotes() {
      return _numCompareQuotes;
   }

   public Date getLastDate() {
      return _quotes[_numQuotes - 1].getDate();
   }

   public int getNumQuotes() {
      return _numQuotes;
   }

   public int getNumRsQuotes() {
      return _numRsQuotes;
   }

   public String getRsSymbol() {
      return _rsSymbol;
   }

   public void setRsSymbol(String rsSymbol) {
      _rsSymbol = rsSymbol;
   }

   public static boolean isLocal() {
      return _isLocal;
   }

   public static void setLocal() {
      _isLocal = true;

   }

   public static void setSaveQuotes() {
      _saveQuotes = true;

   }

   public static boolean saveQutoes() {
      return _saveQuotes;
   }

   public boolean isLiveQuote() {
      return _isLiveQuote;
   }

   public void setLiveQuote() {
      _isLiveQuote = true;
   }

}
