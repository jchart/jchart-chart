package com.jchart.io.quoteretriever.yahoo;

/**
 * @author <a href="mailto:paul.russo@jchart.com>Paul Russo</a>
 * @since May 21, 2017
 */
class SessionInfo {
   private String cookie;
   private String crumb;
   public String getCookie() {
      return cookie;
   }
   public void setCookie(String cookie) {
      this.cookie = cookie;
   }
   public String getCrumb() {
      return crumb;
   }
   public void setCrumb(String crumb) {
      this.crumb = crumb;
   }
   @Override
   public String toString() {
      return "SessionInfo [cookie=" + cookie + ", crumb=" + crumb + "]";
   }
   
   
}
