package com.jchart.model.indicator;

import java.util.Date;

import com.jchart.model.Kjchart;

public class Sma extends DateValue {

   Sma(Date[] inDt, float[] inVal, int numQuotes, int ma) {
      super(numQuotes);
      calcSMA(inDt, inVal, numQuotes, ma);
   }

   private void calcSMA(Date[] inDt, float[] inVal, int qListSize, int ma) {
      float isum = 0;
      int j;
      int i;
      int listPos;
      int ipad = 1;

      // pad leading records with Ifiller
      for (i = 0; i < ((ma + ipad) - 2); i++) {
         dt[i] = null;
         val[i] = Kjchart.IFILLER;
      }

      while (i != qListSize) {
         isum = 0;
         for (j = i; j > (i - ma); j--) {
            isum += inVal[j];
         }
         listPos = j + ma;

         // copy date
         dt[listPos] = inDt[listPos];
         val[listPos] = isum / ma;
         i++;
      }
   }
}
