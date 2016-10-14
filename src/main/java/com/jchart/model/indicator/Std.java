/*
 * Created on Jan 15, 2005
 *
 */
package com.jchart.model.indicator;

import java.util.Date;

import com.jchart.model.Kjchart;

/**
 * @author Paul Russo
 * 
 *         Standard Deviation
 *
 */
public class Std extends DateValue {

   Std(Date[] inDt, float[] inVal, int numQuotes, int ma) {
      super(numQuotes);
      calcStd(inDt, inVal, numQuotes, ma);
   }

   /**
    * @param inDt
    * @param inVal
    * @param numQuotes
    * @param ma
    */
   private void calcStd(Date[] inDt, float[] inVal, int numQuotes, int ma) {

      // pad leading records with Ifiller
      int ipad = 1;
      int i;
      for (i = 0; i < ((ma + ipad) - 2); i++) {
         dt[i] = null;
         val[i] = Kjchart.IFILLER;
      }
      // calc std
      while (i != numQuotes) {
         float sum = 0;
         double sumSqr = 0;
         int j;
         for (j = i; j > (i - ma); j--) {
            sum += inVal[j];
            sumSqr += inVal[j] * inVal[j];
         }
         int listPos = j + ma;
         double numer = sumSqr - ((sum * sum) / ma);
         double quotient = numer / ma;
         double std = Math.sqrt(quotient);

         // set values
         dt[listPos] = inDt[listPos];
         val[listPos] = (float) std;
         i++;
      }
   }

}
