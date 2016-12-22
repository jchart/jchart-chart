/**
 * 
 */
package com.jchart.stats.impl;

/**
 * @author <a href ="paul.russo@jchart.com">Paul Russo</a>
 * @created Jul 2, 2011
 */
public class StatsBean {

   private double avgX;
   private double stdX;
   private double avgY;
   private double stdY;
   private double r; // correlation coefficient
   private double r2; // percent variation relation
   private double yIntercept;
   private double slope;
   private int length;

   public double getAvgX() {
      return avgX;
   }

   public double getStdX() {
      return stdX;
   }

   public double getAvgY() {
      return avgY;
   }

   public double getStdY() {
      return stdY;
   }

   public double getR() {
      return r;
   }

   public double getR2() {
      return r2;
   }

   public double getYintercept() {
      return yIntercept;
   }

   public double getSlope() {
      return slope;
   }

   public int getLength() {
      return length;
   }

   public void setAvgX(double avgX) {
      this.avgX = avgX;
   }

   public void setStdX(double stdX) {
      this.stdX = stdX;
   }

   public void setAvgY(double avgY) {
      this.avgY = avgY;
   }

   public void setStdY(double stdY) {
      this.stdY = stdY;
   }

   public void setR(double r) {
      this.r = r;
   }

   public void setR2(double r2) {
      this.r2 = r2;
   }

   public void setYintercept(double yIntercept) {
      this.yIntercept = yIntercept;
   }

   public void setSlope(double slope) {
      this.slope = slope;
   }

   public void setLength(int length) {
      this.length = length;
   }

   @Override
   public String toString() {
      return "StatsBean [avgX=" + avgX + ", stdX=" + stdX + ", avgY=" + avgY
            + ", stdY=" + stdY + ", r=" + r + ", r2=" + r2 + ", yIntercept="
            + yIntercept + ", slope=" + slope + ", length=" + length + "]";
   }

}
