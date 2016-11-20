package com.jchart.model.indicator;

import com.jchart.model.Quote;

public class Rsi extends DateValue {

   Rsi(int numQuotes, Quote[] quotes, int ma) {
      super(numQuotes);
      calc(numQuotes, quotes, ma);
   }

   private void calc(int numQuotes, Quote[] quotes, int ma) {
      double upAccum = 0d;
      double downAccum = 0d;
      double upAvg = 0d;
      double downAvg = 0d;
      double rsi = 0d;
      Quote currQuote;
      Quote priorQuote;
      double priorUpAvg = 0d;
      double priorDownAvg = 0d;

      for (int i = 1; i < ma + 1; i++) {
         priorQuote = quotes[i - 1];
         currQuote = quotes[i];
         double change = currQuote.getClose() - priorQuote.getClose();
         if (change > 0) {
            upAccum += change;
         } else if (change < 0) {
            change = Math.abs(change);
            downAccum += change;
         }
      }
      // averages start with the sums
      upAvg = upAccum / ma;
      downAvg = downAccum / ma;
      rsi = (upAvg / (upAvg + downAvg)) * 100;
      int startPos = ma;
      dt[startPos] = quotes[startPos].getDate();
      val[startPos] = (float) rsi;

      priorUpAvg = upAvg;
      priorDownAvg = downAvg;
      // continue to save values in map for the rest of the data
      startPos++;
      for (int i = startPos; i < numQuotes; i++) {
         double currGain = 0d;
         double currLoss = 0d;
         priorQuote = quotes[i - 1];
         currQuote = quotes[i];
         double change = currQuote.getClose() - priorQuote.getClose();
         if (change > 0) {
            currGain = change;
         } else if (change < 0) {
            currLoss = Math.abs(change);
         }
         upAvg = ((priorUpAvg * (ma - 1)) + currGain) / ma;
         downAvg = ((priorDownAvg * (ma - 1)) + currLoss) / ma;
         rsi = (upAvg / (upAvg + downAvg)) * 100;
         dt[i] = quotes[i].getDate();
         val[i] = (float) rsi;
         priorUpAvg = upAvg;
         priorDownAvg = downAvg;
      }
   }

}
