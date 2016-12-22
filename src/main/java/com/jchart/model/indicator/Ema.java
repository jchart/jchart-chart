package com.jchart.model.indicator;

import java.util.Date;

import com.jchart.model.Kjchart;

public class Ema extends DateValue {

   Ema(Date[] inDt, float[] inVal, int qListSize, int ma, int ipad) {
      super(qListSize);
      calcEMA(inDt, inVal, qListSize, ma, ipad);
   }

   private void calcEMA(Date[] inDt, float[] inVal, int qListSize, int ma,
         int ipad) {
      int recnum;
      int j;
      float sum;
      float curEMA;
      float priorEMA;
      float sma;
      float expo;

      // pad leading records with Ifiller
      for (int i = 0; i < ((ma + ipad) - 1); i++) {
         dt[i] = null;
         val[i] = Kjchart.IFILLER;
      }
      if (ipad > 0) {
         j = ipad + ma;
      } else {
         j = ma - 2;
      }
      sum = 0;

      // the starting point for ema is sma
      for (int i = j; i > (j - ma + 1); i--) {
         sum += inVal[i];
      }
      j++;
      sma = sum / ma;
      recnum = j; // + MA - 1;
      dt[recnum] = inDt[recnum];
      val[recnum] = sma;

      expo = 2.0f / (ma + 1);
      priorEMA = sma;
      recnum++;
      while (recnum < qListSize) {
         dt[recnum] = inDt[recnum];
         curEMA = priorEMA + (expo * (inVal[recnum] - priorEMA));
         val[recnum] = curEMA;
         priorEMA = curEMA;
         recnum++;
      }
   }
}
