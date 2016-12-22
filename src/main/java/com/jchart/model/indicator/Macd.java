package com.jchart.model.indicator;

import java.util.Date;

import com.jchart.model.Kjchart;
import com.jchart.model.Quote;

public class Macd extends DateValue {
   private static final long serialVersionUID = 1L;

   private Ema _signalLine;
   private Histogram _histogram;

   // constructor
   Macd(int qListSize, Quote[] quotes, int ma1, int ma2, int ma3) {
      super(qListSize);
      calcMACD(qListSize, quotes, ma1, ma2, ma3);
   }

   private boolean calcMACD(int qListSize, Quote[] quotes, int ma1, int ma2,
         int ma3) {
      int recnum;
      float[] clVal = new float[qListSize];
      Date[] clDt = new Date[qListSize];

      // pad leading records with Ifiller
      for (int i = 0; i < (ma2 - 1); i++) {
         dt[i] = null;
         val[i] = Kjchart.IFILLER;
      }

      // fill with closing prices
      for (int i = 0; i < qListSize; i++) {
         clVal[i] = quotes[i].getClose();
         clDt[i] = quotes[i].getDate();
      }

      Ema ema1 = new Ema(clDt, clVal, qListSize, ma1, 0);
      Ema ema2 = new Ema(clDt, clVal, qListSize, ma2, 0);

      recnum = ma2 - 1;
      while (recnum < qListSize) {
         dt[recnum] = quotes[recnum].getDate();
         val[recnum] = ema1.val[recnum] - ema2.val[recnum];
         recnum++;
      }
      ema1 = null;
      ema2 = null;
      // MACD trigger line
      _signalLine = new Ema(dt, val, qListSize, ma3, ma2);
      _histogram = new Histogram(dt, val, _signalLine.dt, _signalLine.val,
            qListSize);
      return true;
   }

   public DateValue getHistogram() {
      return _histogram;
   }

   public DateValue getSignalLine() {
      return _signalLine;
   }

}
