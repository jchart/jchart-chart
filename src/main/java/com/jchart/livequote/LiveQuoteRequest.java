package com.jchart.livequote;

public interface LiveQuoteRequest {

   public abstract LiveQuote getLiveQuote(String ticker);

}