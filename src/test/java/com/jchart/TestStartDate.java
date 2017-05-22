package com.jchart;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author <a href="mailto:paul.russo@jchart.com>Paul Russo</a>
 * @since May 20, 2017
 */
public class TestStartDate {
   
   @Test
   public void test() {
      int n = getNumQuotes(7);
      Assert.assertEquals(9, n);
      n = getNumQuotes(15);
      Assert.assertEquals(19, n);
      n = getNumQuotes(17);
      Assert.assertEquals(21, n);
      n = getNumQuotes(22);
      Assert.assertEquals(28, n);

   
   }

   private int getNumQuotes(int maxQuotes) {
      int retval = 0;
      int f = (int)(Math.floor(maxQuotes / 7));
      retval = maxQuotes + (f * 2);
      return retval;
   }


}
