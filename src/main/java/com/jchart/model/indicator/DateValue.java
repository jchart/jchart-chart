package com.jchart.model.indicator;

import java.util.Date;

public abstract class DateValue implements IndicatorIntr {

   protected Date[] dt;
   protected float[] val;

   public DateValue(int qListSize) {
      dt = new Date[qListSize];
      val = new float[qListSize];
   }

   public Date[] getDt() {
      return dt;
   }

   public float[] getVal() {
      return val;
   }

   public Date getDt(int i) {
      return dt[i];
   }

   public float getVal(int i) {
      return val[i];
   }

   public DateValue getHistogram() {
      return null;
   }

   public DateValue getSignalLine() {
      return null;
   }

   public DateValue getVal1() {
      return null;
   }

   public DateValue getVal2() {
      return null;
   }
}
