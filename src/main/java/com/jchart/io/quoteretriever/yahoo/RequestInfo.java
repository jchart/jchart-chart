package com.jchart.io.quoteretriever.yahoo;

/**
 * @author <a href="mailto:paul.russo@jchart.com>Paul Russo</a>
 * @since May 21, 2017
 */
class RequestInfo {
   private String savePath;
   private String ticker;
   private int maxQuotes;

   RequestInfo(String ticker, int maxQuotes) {
      this.ticker = ticker;
      this.maxQuotes = maxQuotes;
   }
   
   public String getSavePath() {
      return savePath;
   }
   public void setSavePath(String savePath) {
      this.savePath = savePath;
   }
   public String getTicker() {
      return ticker;
   }
   public int getMaxQuotes() {
      return maxQuotes;
   }
   @Override
   public String toString() {
      return "RequestInfo [savePath=" + savePath + ", ticker=" + ticker
            + ", maxQuotes=" + maxQuotes + "]";
   }

}
