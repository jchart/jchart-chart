package com.jchart.io.factory;

import java.util.List;

import com.jchart.model.JchartComposite;

/**
 * interface of an Abstract factory pattern of io datasources
 */
public interface IoFactoryIntr {
   public void init(String cgiDir, String tickerDir, boolean useCgi,
         String dataSource);

   public boolean tickerRequestEod(String sbl, JchartComposite jchartComposite)
         throws Exception;

   public boolean tickerRequestIntraday(String ticker,
         JchartComposite jchartComposite) throws Exception;

   public List<String> getTickerList(String tickerFile) throws Exception;

   public boolean checkIn(String prodIdF, String prodId) throws Exception;

}
