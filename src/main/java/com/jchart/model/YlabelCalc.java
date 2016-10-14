package com.jchart.model;

public class YlabelCalc {
   private static final int NUMLABELS = 8;
   private static final float LOWFRACT = 0.0625f;
   private float _fInc;
   private float _fLabelLo;
   private int _decimalPos;

   public YlabelCalc(float hi, float lo) {
      calcYlabels(hi, lo);
   }

   public YlabelCalc() {
   }

   public float getLabelLo() {
      return _fLabelLo;
   }

   public float getInc() {
      return _fInc;
   }

   public int getNumLabels() {
      return NUMLABELS;
   }

   public int getDecimalPos() {
      return _decimalPos;
   }

   public void calcYlabels(float hi, float lo) {
      _fInc = 0;
      _decimalPos = 0;

      // get range
      float range = hi - lo;
      float roundVal = 0;
      if (range > 15) {
         roundVal = 5f;
      } else if (range > 10) {
         roundVal = 2f;
      } else if (range > 4) {
         roundVal = 1f;
      } else if (range > 2) {
         roundVal = 0.5f;
         _decimalPos = 1;
      } else if (range > 0.75f) {
         roundVal = 0.25f;
         _decimalPos = 2;
      } else if (range > 0.20f) {
         roundVal = 0.125f;
         _decimalPos = 3;
      } else {
         roundVal = LOWFRACT;
         _decimalPos = 4;
      }
      _fInc = (int) (range / NUMLABELS);
      _fInc = roundUp(_fInc, roundVal);
      _fLabelLo = roundUp((int) lo, roundVal);
      while (lo > _fLabelLo) {
         _fLabelLo += _fInc;
      }
      if (_fLabelLo > hi) {
         _fLabelLo = hi;
      }
      if (getActualNumLabels(hi, lo) < 4) {
         _fInc = LOWFRACT;
      }

      // printLabels(_fInc, _fLabelLo);
   }

   /**
    * @param num
    *           - integer to be adjusted
    * @param roundVal
    *           - divisor
    * @return int - a number that is evenly divisible by roundVal
    **/
   private float roundUp(float num, float roundVal) {
      float i = LOWFRACT;
      float inc = LOWFRACT;
      float newNum = 0;
      do {
         newNum = num + i;
         i += inc;
      } while ((newNum % roundVal) != 0);

      return newNum;
   }

   private int getActualNumLabels(float hi, float lo) {
      int i = 0;
      for (float f = lo; f < hi; f += _fInc) {
         i++;
      }

      return i;
   }

   public static void main(String[] args) {
      new YlabelCalc(new Float(args[0]).floatValue(),
            new Float(args[1]).floatValue());
   }
}
