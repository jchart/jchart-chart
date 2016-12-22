package com.jchart.model;

import java.util.Properties;

public class JchartModelFacade implements java.io.Serializable {
   private static final long serialVersionUID = 1L;

   private static Properties _jchartProps;

   public static void setVolMountain(JchartComposite jchartComposite) {
      if (jchartComposite.getPlotRange()
            .getNumPlotRecs() > TimeFrameModel.ONEYEAR) {
         jchartComposite.getChartTypeModel().isVolMountain(true);
      } else {
         jchartComposite.getChartTypeModel().isVolMountain(false);
      }
   }

   public static void calcPlotRange(JchartComposite jchartComposite) {
      int numDataPoints = jchartComposite.getTimeFrameModel()
            .getNumDataPoints();
      ChartTypeModel chartTypeModel = jchartComposite.getChartTypeModel();
      int numQuotes = 0;

      if (jchartComposite.getChartTypeModel().isComparison()) {
         numQuotes = jchartComposite.getQuoteDataModel().getNumCompareQuotes();
      } else {
         numQuotes = jchartComposite.getQuoteDataModel().getNumQuotes();
      }

      int numPlotRecs = 0;

      if (numQuotes > numDataPoints) {
         numPlotRecs = numDataPoints;
      } else {
         numPlotRecs = numQuotes;
      }

      if (chartTypeModel.isBack()) {
         jchartComposite.getPlotRange().calcBack();
      } else if (chartTypeModel.isBack()) {
         jchartComposite.getPlotRange().calcForward();
      } else {
         jchartComposite.getPlotRange().calcPlotRange(numPlotRecs, numQuotes);
      }

   }

   public static void calcPlotRangeZoom(JchartComposite jchartComposite,
         int beginPlotRec, int endPlotRec) {
      jchartComposite.getPlotRange().calcPlotRangeZoom(beginPlotRec,
            endPlotRec);
   }

   public static void indicateMain(JchartComposite jchartComposite) {
      jchartComposite.getIndicatorModel().calcIndicator(jchartComposite);
   }

   public static String getChartText() {
      return _jchartProps.getProperty("copyright");
   }

   public static String getEodDomain() {
      return _jchartProps.getProperty("eodDomain");
   }

   public static void setJchartProps(Properties jchartProps) {
      _jchartProps = jchartProps;
   }

   public static Properties getJchartProps() {
      return _jchartProps;
   }

   /**
    * update models with the request data
    * 
    * @param _jchartComposite
    * @param _jchartRequest
    */
   public static void init(JchartComposite jchartComposite) {
      JchartRequest jchartRequest = jchartComposite.getJchartRequest();
      TimeFrameModel timeFrameModel = jchartComposite.getTimeFrameModel();
      jchartComposite.getColorScheme().init();
      jchartComposite.getColorScheme().processRequest(jchartRequest);
      jchartComposite.getChartTypeModel().processRequest(jchartRequest);

      // model of timeframe and set period
      if (!timeFrameModel.isIntraday()) {
         timeFrameModel.setCurPeriod(jchartRequest.getTimeFrame());
      }
      jchartComposite.getIndicatorModel().calcIndicator(jchartComposite);
   }

}
