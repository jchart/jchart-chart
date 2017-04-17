package com.jchart;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;

import com.jchart.io.IoJchartBase;
import com.jchart.io.factory.impl.IoJchartPsr;
import com.jchart.model.JchartModelFacade;

/**
 * @author <a href="mailto:paul.russo@jchart.com>Paul Russo</a>
 * @since Apr 17, 2017
 */

public class TestQuoteRetrieval {

   @Test
   public void testUrl() throws Exception {
      String urlString = getUrlString();
      Assert.assertNotNull(urlString);
      System.out.println(urlString);
      URL url = new URL(urlString);
      InputStream is = url.openStream();
      InputStreamReader isr = new InputStreamReader(is,"UTF-8");
      BufferedReader br = new BufferedReader(isr);
      Assert.assertNotNull(br);
      String line = br.readLine();
      Assert.assertNotNull(line);
      Assert.assertEquals("Date,Open,High,Low,Close,Volume,Adj Close", line);
   }
      
   
   private String getUrlString() throws Exception {
      Properties jchartProps = IoJchartBase.loadProperties(getClass(), "jchart.properties");
      JchartModelFacade.setJchartProps(jchartProps);
      IoJchartTest io = new IoJchartTest();
      String retval = io.getEodUrlStr("QQQ");
      return retval;
   }


   public class IoJchartTest extends IoJchartPsr {

      public String getEodUrlStr(String sbl) {
         return getEodUrlStr("QQQ", 15);
      }

      
   }
   
}
