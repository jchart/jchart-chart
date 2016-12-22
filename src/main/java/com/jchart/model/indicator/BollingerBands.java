/*
 * Created on Jan 15, 2005
 *
 */
package com.jchart.model.indicator;

import java.util.Date;

import com.jchart.model.Kjchart;
import com.jchart.model.Quote;

/**
 * @author Paul Russo
 *
 */
public class BollingerBands extends DateValue {
   private float[] _clVal;
   private Date[] _clDt;
   private DateValue _upperBand;
   private DateValue _lowerBand;

   BollingerBands(int numQuotes, Quote[] quotes, int ma) {
      super(numQuotes);
      init(numQuotes, quotes);
      calcBollingerBands(numQuotes, ma);
   }

   private void init(int numQuotes, Quote[] quotes) {
      _clVal = new float[numQuotes];
      _clDt = new Date[numQuotes];
      _upperBand = new DataValImpl(numQuotes);
      _lowerBand = new DataValImpl(numQuotes);

      // fill with closing prices
      for (int i = 0; i < numQuotes; i++) {
         _clDt[i] = quotes[i].getDate();
         _clVal[i] = quotes[i].getClose();
      }
   }

   /**
    * @param numQuotes
    * @param quotes
    * @param ma
    */
   private void calcBollingerBands(int numQuotes, int ma) {
      Sma sma = calcSma(numQuotes, ma);
      Std std = calcStd(numQuotes, ma);
      int numStd = 2;

      _upperBand.dt = sma.dt;
      _lowerBand.dt = sma.dt;

      int i = 0;
      while (sma.getVal(i) == Kjchart.IFILLER) {
         i++;
      }
      float initVal = sma.getVal(i);
      i = 0;
      while (sma.getVal(i) == Kjchart.IFILLER) {
         dt[i] = null;
         val[i] = initVal;
         _upperBand.val[i] = initVal;
         _lowerBand.val[i] = initVal;
         i++;
      }

      while (i < numQuotes) {
         dt[i] = sma.getDt(i);
         val[i] = sma.getVal(i);
         float stdVal = (std.getVal(i) * numStd);
         _upperBand.val[i] = val[i] + stdVal;
         _lowerBand.val[i] = val[i] - stdVal;
         i++;
      }

      _clDt = null;
      _clVal = null;

   }

   /**
    * @param numQuotes
    * @param quotes
    * @param ma
    * @return
    */
   private Std calcStd(int numQuotes, int ma) {
      Std retval = null;
      retval = new Std(_clDt, _clVal, numQuotes, ma);
      return retval;
   }

   /**
    * @param numQuotes
    * @param quotes
    * @param ma
    * @return
    */
   private Sma calcSma(int numQuotes, int ma) {
      Sma retval = null;
      retval = new Sma(_clDt, _clVal, numQuotes, ma);
      return retval;
   }

   public DateValue getVal1() {
      return _upperBand;
   }

   public DateValue getVal2() {
      return _lowerBand;
   }

}