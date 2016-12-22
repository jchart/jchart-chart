package com.jchart.model;

import java.util.Date;

public class Quote {
   private Date date;
   private float open;
   private float hi;
   private float low;
   private float close;
   private float change;
   private long volume;

   public Date getDate() {
      return date;
   }

   public void setDate(Date date) {
      this.date = date;
   }

   public float getOpen() {
      return open;
   }

   public void setOpen(float open) {
      this.open = Quote.round(open, 1000);
   }

   public float getHi() {
      return hi;
   }

   public void setHi(float hi) {
      this.hi = Quote.round(hi, 1000);
   }

   public float getLow() {
      return low;
   }

   public void setLow(float low) {
      this.low = Quote.round(low, 1000);
   }

   public float getClose() {
      return close;
   }

   public void setClose(float close) {
      this.close = Quote.round(close, 1000);
   }

   public long getVolume() {
      return volume;
   }

   public void setVolume(long volume) {
      this.volume = volume;
   }

   public float getChange() {
      return change;
   }

   public void setChange(float change) {
      this.change = Quote.round(change, 1000);
   }

   private static float round(double d, int precision) {
      return (float) Math.round(d * precision) / precision;
   }

   @Override
   public String toString() {
      return "Quote [date=" + date + ", open=" + open + ", hi=" + hi + ", low="
            + low + ", close=" + close + ", change=" + change + ", volume="
            + volume + "]";
   }

}
