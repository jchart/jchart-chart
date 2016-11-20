/*
 * Created on Feb 23, 2004
 *
 */
package com.jchart.adapter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author <a href="mailto:paul.russo@jchart.com">Paul S. Russo</a>
 *
 * @created Aug 20, 2007
 */
public class QuoteDataBean implements Serializable {
   private static final long serialVersionUID = 1l;
   private double _op;
   private double _hi;
   private double _low;
   private double _cl;
   private long _vol;
   private Date _date;

   /**
    * @param date
    */
   public void setDate(Date date) {
      _date = date;
   }

   /**
    * @param d
    */
   public void setOp(double d) {
      _op = d;
   }

   /**
    * @param d
    */
   public void setHi(double d) {
      _hi = d;
   }

   /**
    * @param d
    */
   public void setLow(double d) {
      _low = d;
   }

   /**
    * @param d
    */
   public void setCl(double d) {
      _cl = d;
   }

   /**
    * @param d
    */
   public void setVol(long l) {
      _vol = l;
   }

   public double getOp() {
      return _op;
   }

   public double getHi() {
      return _hi;
   }

   public double getLow() {
      return _low;
   }

   public double getCl() {
      return _cl;
   }

   public long getVol() {
      return _vol;
   }

   /**
    * @return
    */
   public Date getDate() {
      return _date;
   }

   public String toString() {
      StringBuffer sb = new StringBuffer();
      sb.append("dt: " + _date + "\n");
      sb.append("op: " + _op + "\n");
      sb.append("hi: " + _hi + "\n");
      sb.append("low: " + _low + "\n");
      sb.append("cl: " + _cl + "\n");
      sb.append("vol: " + _vol + "\n");
      return sb.toString();
   }
}
