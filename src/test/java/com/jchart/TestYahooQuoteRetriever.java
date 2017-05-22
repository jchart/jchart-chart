package com.jchart;

import java.util.List;

import org.junit.Test;

import com.jchart.io.quoteretriever.IQuoteRetriever;
import com.jchart.io.quoteretriever.yahoo.YahooQuoteRetriever;
import com.jchart.model.Quote;

import org.junit.Assert;

/**
 * @author <a href="mailto:paul.russo@jchart.com>Paul Russo</a>
 * @since May 21, 2017
 */
public class TestYahooQuoteRetriever {
   
   @Test
   public void test() throws Exception {
      IQuoteRetriever retriever = new YahooQuoteRetriever();
      List<Quote> quotes = retriever.getRemoteQuotes("QQQ", 100);
      Assert.assertEquals(100, quotes.size());
      quotes = retriever.getRemoteQuotes("AA", 25);
      Assert.assertEquals(25, quotes.size());
      quotes.forEach(System.out::println);
      retriever.saveRemoteQuotes("AA", 2600, "/home/paul/_/aa.csv");
   }

}
