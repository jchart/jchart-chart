/**
 * 
 */
package com.jchart;

/**
 * @author <a href="mailto:paul123@jchart.com">Paul S. Russo</a>
 * 
 * @created Oct 21, 2007
 */
public class JchartArgs {

   private boolean _isLocal;
   private int _maxQuotes;
   private String _tickerFile;
   private String _tickerDir;
   private String _dataDir;
   private String _screenDir;

   public boolean isLocal() {
      return _isLocal;
   }

   public void setIsLocal(boolean isLocal) {
      _isLocal = isLocal;
   }

   public int getMaxQuotes() {
      return _maxQuotes;
   }

   public void setMaxQuotes(int maxQuotes) {
      _maxQuotes = maxQuotes;
   }

   public String getTickerFile() {
      return _tickerFile;
   }

   public void setTickerFile(String tickerFile) {
      _tickerFile = tickerFile;
   }

   public void setScreenDir(String screenDir) {
      _screenDir = screenDir;
   }

   public String getScreenDir() {
      return _screenDir;
   }

   public String getTickerDir() {
      return _tickerDir;
   }

   public void setTickerDir(String tickerDir) {
      _tickerDir = tickerDir;
   }

   public String getDataDir() {
      return _dataDir;
   }

   public void setDataDir(String dataDir) {
      _dataDir = dataDir;
   }

   public String[] getArgs() {
      String[] retval = new String[6];
      retval[0] = "tickerDir=" + _tickerDir;
      retval[1] = "tickerFile=" + _tickerFile;
      retval[2] = "maxQuotes=" + _maxQuotes;
      retval[3] = "dataDir=" + _dataDir;
      retval[4] = "screenDir=" + _screenDir;
      retval[5] = "isLocal=" + _isLocal;
      return retval;
   }

}
