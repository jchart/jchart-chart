/**
 * 
 */
package com.jchart.stats.impl;

import java.util.List;

/**
 * @author <a href ="paul.russo@jchart.com">Paul Russo</a>
 * @created Jul 2, 2011
 */
public class StandardDeviation {

   private double avg = 0d;

   public double calc(List<Double> vals) {
      double retval = 0d;
      double sum = 0d;
      double sumSqr = 0d;
      double numer = 0d;
      int numVals = vals.size();
      for (Double val : vals) {
         sum += val;
         sumSqr += Math.pow(val, 2);
      }
      numer = sumSqr - (Math.pow(sum, 2) / numVals);
      avg = sum / numVals;
      retval = Math.sqrt(numer / (numVals - 1));
      return retval;
   }

   public double getAvg() {
      return avg;
   }

}
