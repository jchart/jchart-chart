package com.jchart.model;

import com.jchart.model.color.ColorScheme;
import com.jchart.model.indicator.IndicatorModel;

public class JchartComposite {
   private ChartSizeModel _chartSizeModel = new ChartSizeModel();
   private TimeFrameModel _timeFrameModel = new TimeFrameModel();
   private ChartTypeModel _chartTypeModel = new ChartTypeModel();
   private ColorScheme _colorScheme;
   private PlotDataModel _plotDataModel = new PlotDataModel();
   private IndicatorModel _indicatorModel = new IndicatorModel();
   private JchartRequest _jchartRequest = new JchartRequest();
   private QuoteDataModel _quoteDataModel;

   public void setQuoteDataModel(QuoteDataModel quoteDataModel) {
      _quoteDataModel = quoteDataModel;
      _quoteDataModel.init();
      _plotDataModel.setQuoteDataModel(quoteDataModel);
   }

   public ChartSizeModel getChartSizeModel() {
      return _chartSizeModel;
   }

   public TimeFrameModel getTimeFrameModel() {
      return _timeFrameModel;
   }

   public ChartTypeModel getChartTypeModel() {
      return _chartTypeModel;
   }

   public IndicatorModel getIndicatorModel() {
      return _indicatorModel;
   }

   public ColorScheme getColorScheme() {
      return _colorScheme;
   }

   public PlotDataModel getPlotDataModel() {
      return _plotDataModel;
   }

   public QuoteDataModel getQuoteDataModel() {
      return _quoteDataModel;
   }

   public PlotRange getPlotRange() {
      return _plotDataModel.getPlotRange();
   }

   public ChartSizeModel getNewChartSizeModel() {
      _chartSizeModel = new ChartSizeModel();
      return _chartSizeModel;
   }

   public JchartRequest getJchartRequest() {
      return _jchartRequest;
   }

   public void setColorScheme(ColorScheme colorScheme) {
      _colorScheme = colorScheme;
   }

}
