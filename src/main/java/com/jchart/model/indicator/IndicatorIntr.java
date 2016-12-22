package com.jchart.model.indicator;

import java.util.Date;

public interface IndicatorIntr {
   public DateValue getHistogram();

   public DateValue getSignalLine();

   public DateValue getVal1();

   public DateValue getVal2();

   public Date[] getDt();

   public float[] getVal();

   public Date getDt(int i);

   public float getVal(int i);
}
