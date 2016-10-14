/**
 * 
 */
package com.jchart.model.indicator;

import com.jchart.model.Kjchart;
import com.jchart.model.Quote;
import com.jchart.util.FormatUtils;

/**
 * @author <a href="mailto:paul.russo.64@gmail.com">Paul S. Russo</a>
 * @since Oct 24, 2012
 */

public class Adx extends DateValue {

   private DateValue _plusDi;
   private DateValue _minusDi;

   private int startPos = 0;

   Adx(int numQuotes, Quote[] quotes, int ma) {
      super(numQuotes);
      init(numQuotes, ma);
      calc(numQuotes, quotes, ma);
   }

   public DateValue getVal1() {
      return _plusDi;
   }

   public DateValue getVal2() {
      return _minusDi;
   }

   private void init(int numQuotes, int ma) {
      _plusDi = new DataValImpl(numQuotes);
      _minusDi = new DataValImpl(numQuotes);
      // pad leading records with Ifiller
      startPos = (ma * 2) - 1;
      int i = 0;
      for (i = 0; i < startPos; i++) {
         dt[i] = null;
         val[i] = Kjchart.IFILLER;
         _plusDi.dt[i] = null;
         _plusDi.val[i] = Kjchart.IFILLER;
         _minusDi.dt[i] = null;
         _minusDi.val[i] = Kjchart.IFILLER;
      }
   }

   private void calc(int numQuotes, Quote[] quotes, int ma) {
      // calc starting averages
      double trueRangeAccum = 0d;
      double plusDmAccum = 0d;
      double minusDmAccum = 0d;
      Quote currQuote;
      Quote priorQuote;
      for (int i = 1; i < ma + 1; i++) {
         priorQuote = quotes[i - 1];
         currQuote = quotes[i];
         trueRangeAccum += calcTrueRange(currQuote, priorQuote);
         plusDmAccum += calcPlusDm(currQuote, priorQuote);
         minusDmAccum += calcMinusDm(currQuote, priorQuote);
      }
      // averages start with the sums
      double avgPlusDi = 100d * (plusDmAccum / trueRangeAccum);
      double avgMinusDi = 100d * (minusDmAccum / trueRangeAccum);
      double diffDi = Math.abs(avgPlusDi - avgMinusDi);
      double sumDi = avgPlusDi + avgMinusDi;
      double dx = 100d * (diffDi / sumDi);

      // averages
      double priorAvgTrueRange = trueRangeAccum;
      double priorAvgPlusDm = plusDmAccum;
      double priorAvgMinusDm = minusDmAccum;

      // calc ADX using same MA moving forward by MA
      double dxAccum = dx;
      int endPos = (ma * 2);
      for (int i = ma + 1; i < endPos; i++) {
         priorQuote = quotes[i - 1];
         currQuote = quotes[i];
         double trueRange = calcTrueRange(currQuote, priorQuote);
         double plusDm = calcPlusDm(currQuote, priorQuote);
         double minusDm = calcMinusDm(currQuote, priorQuote);
         double avgTrueRange = priorAvgTrueRange - (priorAvgTrueRange / ma)
               + trueRange;
         double avgPlusDm = priorAvgPlusDm - (priorAvgPlusDm / ma) + plusDm;
         double avgMinusDm = priorAvgMinusDm - (priorAvgMinusDm / ma) + minusDm;
         avgPlusDi = 100d * (avgPlusDm / avgTrueRange);
         avgMinusDi = 100d * (avgMinusDm / avgTrueRange);
         diffDi = Math.abs(avgPlusDi - avgMinusDi);
         sumDi = avgPlusDi + avgMinusDi;
         dx = (100d * (diffDi / sumDi));
         dxAccum += dx;
         priorAvgTrueRange = avgTrueRange;
         priorAvgPlusDm = avgPlusDm;
         priorAvgMinusDm = avgMinusDm;
      }
      double adx = dxAccum / ma;
      // save values in map starting at position (_ma * 2) -1
      int startPos = (ma * 2) - 1;
      dt[startPos] = quotes[startPos].getDate();
      val[startPos] = (float) adx;
      _plusDi.val[startPos] = (float) avgPlusDi;
      _minusDi.val[startPos] = (float) avgMinusDi;

      // continue to save values in map for the rest of the data
      double priorAdx = adx;
      startPos++;
      for (int i = startPos; i < numQuotes; i++) {
         priorQuote = quotes[i - 1];
         currQuote = quotes[i];
         double trueRange = calcTrueRange(currQuote, priorQuote);
         double plusDm = calcPlusDm(currQuote, priorQuote);
         double minusDm = calcMinusDm(currQuote, priorQuote);
         double avgTrueRange = priorAvgTrueRange - (priorAvgTrueRange / ma)
               + trueRange;
         double avgPlusDm = priorAvgPlusDm - (priorAvgPlusDm / ma) + plusDm;
         double avgMinusDm = priorAvgMinusDm - (priorAvgMinusDm / ma) + minusDm;
         avgPlusDi = 100d * (avgPlusDm / avgTrueRange);
         avgMinusDi = 100d * (avgMinusDm / avgTrueRange);
         diffDi = Math.abs(avgPlusDi - avgMinusDi);
         sumDi = avgPlusDi + avgMinusDi;
         dx = (100d * (diffDi / sumDi));
         adx = ((priorAdx * (ma - 1)) + dx) / ma;

         dt[i] = quotes[i].getDate();
         val[i] = (float) adx;
         _plusDi.val[i] = (float) avgPlusDi;
         _plusDi.dt[i] = quotes[i].getDate();
         _minusDi.val[i] = (float) avgMinusDi;
         _minusDi.dt[i] = quotes[i].getDate();

         priorAvgTrueRange = avgTrueRange;
         priorAvgPlusDm = avgPlusDm;
         priorAvgMinusDm = avgMinusDm;
         priorAdx = adx;
      }
   }

   private double calcPlusDm(Quote currQuote, Quote priorQuote) {
      double retval = 0d;
      double hiDiff = currQuote.getHi() - priorQuote.getHi();
      double lowDiff = priorQuote.getLow() - currQuote.getLow();
      if (hiDiff > lowDiff) {
         if (hiDiff > 0) {
            retval = hiDiff;
         }
      }
      return retval;
   }

   private double calcMinusDm(Quote currQuote, Quote priorQuote) {
      double retval = 0d;
      double hiDiff = currQuote.getHi() - priorQuote.getHi();
      double lowDiff = priorQuote.getLow() - currQuote.getLow();
      if (lowDiff > hiDiff) {
         if (lowDiff > 0) {
            retval = lowDiff;
         }
      }
      return retval;
   }

   private double calcTrueRange(Quote currentQuote, Quote priorQuote) {
      double retval = 0.0d;
      double h_l = FormatUtils
            .round(currentQuote.getHi() - currentQuote.getLow(), 100000);
      double h_pdc = Math.abs(FormatUtils
            .round(currentQuote.getHi() - priorQuote.getClose(), 100000));
      double l_pdc = Math.abs(FormatUtils
            .round(currentQuote.getLow() - priorQuote.getClose(), 100000));
      retval = h_l;
      if (h_pdc > retval) {
         retval = h_pdc;
      }
      if (l_pdc > retval) {
         retval = l_pdc;
      }
      return retval;
   }

}
