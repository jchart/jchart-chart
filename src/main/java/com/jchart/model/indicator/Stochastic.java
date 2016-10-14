package com.jchart.model.indicator;

import java.util.Date;

import com.jchart.model.Quote;

public class Stochastic extends DateValue {

   private Ema _signalLine;
   private Sma _slowStoch;

   Stochastic(int numQutoes, Quote[] quotes, int kN) {
      super(numQutoes);
      calcStoch(numQutoes, quotes, kN);
   }

   private boolean calcStoch(int numQutoes, Quote[] quotes, int kN) {
      int curListPos;
      float pctKfastPrior = 0;
      int kMA = 3;
      int kSmooth = 2;
      float hi;
      float lo;
      float pctKraw = 0;
      float pctKfast;

      float[] wkVal = new float[numQutoes];
      Date[] wkDt = new Date[numQutoes];

      // first calc fast K
      curListPos = kN - 1;

      // KfastP Fill in leading dates
      for (int i = 0; i < curListPos; i++) {
         wkDt[i] = quotes[i].getDate();
      }

      while (curListPos < numQutoes) {
         hi = -999999;
         lo = 999999;

         for (int i = curListPos; i > (curListPos - kN + 1); i--) {
            if (quotes[i].getHi() > hi) {
               hi = quotes[i].getHi();
            }
            if (quotes[i].getLow() < lo) {
               lo = quotes[i].getLow();
            }
         }

         if ((hi - lo) > 0) {
            pctKraw = ((quotes[curListPos].getClose() - lo) / (hi - lo)) * 100;
         }

         pctKfast = ((pctKfastPrior * kSmooth) + pctKraw) / kMA;
         pctKfastPrior = pctKfast;

         wkDt[curListPos] = quotes[curListPos].getDate();
         wkVal[curListPos] = pctKfast;
         curListPos++;
      }

      // now calc 3 period ma of fastK - this is slowK
      _slowStoch = new Sma(wkDt, wkVal, numQutoes, kMA);

      // now calc 3 period ma of slowK - this is the trigger
      _signalLine = new Ema(_slowStoch.dt, _slowStoch.val, numQutoes, 3, kMA);

      return true;
   }

   public float[] getVal() {
      return _slowStoch.getVal();
   }

   public DateValue getSignalLine() {
      return _signalLine;
   }

   public Date getDt(int i) {
      return _slowStoch.dt[i];
   }

   public float getVal(int i) {
      return _slowStoch.val[i];
   }

}
