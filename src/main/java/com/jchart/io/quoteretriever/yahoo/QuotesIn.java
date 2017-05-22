package com.jchart.io.quoteretriever.yahoo;

import java.util.ArrayList;
import java.util.List;

import com.jchart.model.Quote;

/**
 * @author <a href="mailto:paul.russo@jchart.com>Paul Russo</a>
 * @since May 21, 2017
 */
public class QuotesIn {
   
   private List<Quote> quotes = new ArrayList<>();
   private List<String> quoteLines = new ArrayList<>();
   public void addQuote(Quote quote) {
      this.quotes.add(quote);
   }
   public void addQuoteLine(String quoteLine) {
      this.quoteLines.add(quoteLine);
   }
   public List<Quote> getQuotes() {
      return quotes;
   }
   public List<String> getQuoteLines() {
      return quoteLines;
   }
   @Override
   public String toString() {
      return "QuotesIn [quotes=" + quotes + ", quoteLines=" + quoteLines + "]";
   }
   
   
}
