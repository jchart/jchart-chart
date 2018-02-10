package com.jchart;

import java.util.List;
import java.util.Properties;

import org.junit.Test;

import com.jchart.io.quoteretriever.IQuoteRetriever;
import com.jchart.io.quoteretriever.alphavantage.AlphaQuoteRetriever;
import com.jchart.model.JchartModelFacade;
import com.jchart.model.Quote;

import org.junit.Assert;

/**
 * @author <a href="mailto:paul.russo@jchart.com>Paul Russo</a>
 * @since Feb 10, 2018
 */
public class TestAphaQuoteRetriever {
   
   @Test
   public void test() throws Exception {
      Properties props = new Properties();
      props.setProperty("apikey", "demo");
      JchartModelFacade.setJchartProps(props);
      IQuoteRetriever quoteRetiever = new AlphaQuoteRetriever();
      List<Quote> quotes = quoteRetiever.getRemoteQuotes("MSFT", 500);
      Assert.assertNotNull(quotes);
      Assert.assertEquals(500, quotes.size());
      quotes.forEach(System.out::println);
   }

}
