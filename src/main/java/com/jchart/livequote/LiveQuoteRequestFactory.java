package com.jchart.livequote;

public class LiveQuoteRequestFactory {

   public static LiveQuoteRequest getLiveQuoteRequest(Class<?> clazz)
         throws Exception {
      LiveQuoteRequest retval = null;
      retval = (LiveQuoteRequest) Class.forName(clazz.getName()).newInstance();
      return retval;
   }

}
