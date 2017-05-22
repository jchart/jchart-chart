package com.jchart.io.quoteretriever;

import java.io.BufferedReader;
import java.util.List;

import com.jchart.model.Quote;

/**
 * @author <a href="mailto:paul.russo@jchart.com>Paul Russo</a>
 * @since May 21, 2017
 */
public interface IQuoteRetriever {

   List<Quote> getRemoteQuotes(String ticker, int maxQuotes) throws Exception;

   List<Quote> getLocalQuotes(BufferedReader br, int maxQuotes)
         throws Exception;

   void saveRemoteQuotes(String ticker, int maxQuotes, String savePath)
         throws Exception;


}