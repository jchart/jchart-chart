package com.jchart.io.factory.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class TickerConame {
   private static String BASE_URL = "http://finance.yahoo.com/d/quotes.csv?s=";// XOM&f=n

   public String get(String ticker) {
      String retval = null;
      try {
         StringBuffer urlString = new StringBuffer();
         urlString.append(BASE_URL).append(ticker).append("&f=n");
         URL url = new URL(urlString.toString());
         URLConnection connection = url.openConnection();
         try (BufferedReader br = new BufferedReader(
               new InputStreamReader(connection.getInputStream()))) {
            String coname = br.readLine() + " " + ticker;
            retval = coname.replaceAll("\"", "");
            if (retval.contains(ticker)) {
               retval = retval.replaceFirst(ticker, "");
            }
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
      return retval;
   }
}
