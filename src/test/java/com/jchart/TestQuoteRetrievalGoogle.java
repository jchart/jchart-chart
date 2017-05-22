package com.jchart;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;

import com.jchart.io.IoJchartBase;
import com.jchart.io.factory.impl.IoGoogleImpl;
import com.jchart.model.JchartModelFacade;

/**
 * @author <a href="mailto:paul.russo@jchart.com>Paul Russo</a>
 * @since Apr 17, 2017
 */

public class TestQuoteRetrievalGoogle {

   @Test
   public void test() throws Exception {
      String urlString = getUrlString(15);
      Assert.assertNotNull(urlString);
      System.out.println(urlString);
      URL url = new URL(urlString);
      int i = 0;
      try (BufferedReader br =
            new BufferedReader(new InputStreamReader(url.openStream()))) {
         Assert.assertNotNull(br);
         String line = br.readLine();
         Assert.assertNotNull(line);
         Assert.assertEquals("﻿Date,Open,High,Low,Close,Volume", line);
         while ((line = br.readLine()) != null) {
            System.out.println(line);
            i++;
         }
         Assert.assertTrue(i >= 15);
      }
   }
   
   private String getUrlString(int numQuotes) throws Exception {
      Properties jchartProps = IoJchartBase.loadProperties(getClass(), "jchart.properties");
      jchartProps.setProperty("IoFactory.class", "com.jchart.io.factory.impl.IoGoogleImpl");
      jchartProps.setProperty("IoFactory.eodBaseUri", "http://finance.google.com/finance/historical");
      JchartModelFacade.setJchartProps(jchartProps);
      IoJchartTest io = new IoJchartTest();
      io.setNumQuotes(numQuotes);
      String retval = io.getEodUrlStr("QQQ");
      return retval;
   }

   public class IoJchartTest extends IoGoogleImpl {
      int numQuotes;
      public void setNumQuotes(int n) {
         numQuotes = n;
      }

      public String getEodUrlStr(String sbl) {
         return getEodUrlStr("QQQ", numQuotes);
      }
   }
   
}
