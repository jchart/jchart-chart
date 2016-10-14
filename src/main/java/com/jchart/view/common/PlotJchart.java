package com.jchart.view.common;

import com.jchart.adapter.android.Orientation;
import com.jchart.model.ChartOption;
import com.jchart.model.ChartSizeModel;
import com.jchart.model.ChartTypeModel;
import com.jchart.model.Frect;
import com.jchart.model.JchartComposite;
import com.jchart.model.JchartModelFacade;
import com.jchart.model.Kjchart;
import com.jchart.model.PlotDataModel;
import com.jchart.model.PlotRange;
import com.jchart.model.Quote;
import com.jchart.model.QuoteDataModel;
import com.jchart.model.QuoteString;
import com.jchart.model.TimeFrameModel;
import com.jchart.model.YlabelCalc;
import com.jchart.model.color.ColorScheme;
import com.jchart.model.color.ColorValue;
import com.jchart.model.indicator.IndicatorIntr;
import com.jchart.model.indicator.IndicatorModel;
import com.jchart.model.indicator.pnf.Pnf;
import com.jchart.model.indicator.pnf.PnfItem;
import com.jchart.util.FormatUtils;

public class PlotJchart {

   private JchartRenderer _jchartRender;
   private Orientation _orientation = Orientation.VERTICAL;
   private boolean _isAndroid;

   public void setRender(JchartRenderer jchartRender) {
      _jchartRender = jchartRender;
   }

   public void setOrientation(Orientation orientation) {
      _orientation = orientation;
   }

   public void setAndroid() {
      _isAndroid = true;
   }

   /**
    * use by drag zoom
    */
   public void plotZoom(JchartComposite jchartComposite, int beginPlotRec,
         int endPlotRec) {
      JchartModelFacade.calcPlotRangeZoom(jchartComposite, beginPlotRec,
            endPlotRec);
      plotMain(jchartComposite);
   }

   public void plotMain(JchartComposite jchartComposite) {
      JchartModelFacade.setVolMountain(jchartComposite);

      PlotDataModel plotDataModel = jchartComposite.getPlotDataModel();
      PlotRange plotRange = plotDataModel.getPlotRange();
      ChartSizeModel chartSizeModel = jchartComposite.getNewChartSizeModel();
      ColorScheme colorScheme = jchartComposite.getColorScheme();
      IndicatorModel indicatorModel = jchartComposite.getIndicatorModel();
      QuoteDataModel quoteDataModel = jchartComposite.getQuoteDataModel();
      TimeFrameModel timeFrameModel = jchartComposite.getTimeFrameModel();
      ChartTypeModel chartTypeModel = jchartComposite.getChartTypeModel();

      if (chartTypeModel.chartTypeChanged()) {
         indicatorModel.calcIndicator(jchartComposite);
      }

      IndicatorIntr rangeValHi;
      IndicatorIntr rangeValLo;
      IndicatorIntr priceMa3 = indicatorModel.getPriceMa(Kjchart.PRICEMA3);
      IndicatorIntr bands = indicatorModel.getIndicator(ChartOption.BBANDS);
      if (bands != null) {
         rangeValHi = bands.getVal1();
         rangeValLo = bands.getVal2();
      } else {
         rangeValHi = priceMa3;
         rangeValLo = priceMa3;
      }
      boolean isComparison = chartTypeModel.isComparison();
      if (isComparison) {
         if (quoteDataModel.getCompareTicker() == null) {
            return;
         } else {
            quoteDataModel.calcRs(plotRange);
         }
      }
      ChartOption chartOption = chartTypeModel.getPriceOption();
      plotDataModel.setComparison(isComparison);
      plotDataModel.setChartOption(chartOption);
      plotDataModel.setRange(rangeValHi, rangeValLo);
      plotDataModel.setQuoteDataModel(quoteDataModel);
      plotDataModel.init();

      int width = _jchartRender.getWidth();
      int height = _jchartRender.getHeight();

      // bug

      /*
       * if (d.height <= 0) { Dimension d2 = _jchart.getSize(); d.height =
       * d2.height + d.height; d.width = d2.width; }
       */
      if (jchartComposite.getChartTypeModel().isSinglePlot()) {
         Float f = new Float(plotDataModel.getPhi());
         chartSizeModel.init(width, height, FormatUtils.formatDecimal(
               f.toString(), plotDataModel.getYlabelCalc().getDecimalPos()));
      } else {
         chartSizeModel.init(width, height, "123.4");
      }

      int labelFontSize = chartSizeModel.getLabelFontSize();
      String labelStrSize = chartSizeModel.getLabelStrSize();

      _jchartRender.doCreateImage(width, height, labelFontSize, labelStrSize);

      if (jchartComposite.getChartTypeModel()
            .getPriceOption() == ChartOption.PNF) {
         _jchartRender.setColor(ColorValue.TOPBACK);
         plotDataModel.setPnf(indicatorModel.getPnf());
         Frect frect = plotDataModel.setCoordinatesPnf(_jchartRender);
         plotPnf(plotDataModel, colorScheme, frect);
         plotDataModel.setCoordinates(Kjchart.CT_LABEL, Kjchart.POS_PRICE,
               _jchartRender);
         _jchartRender.drawaLabel(ColorValue.TOPCAPTION, ColorValue.TOPBACK,
               quoteDataModel.getTickerConame(), 0);
         displayImage();
         plotDataModel.setCoordinates(Kjchart.CT_PRICE, Kjchart.POS_PRICE,
               _jchartRender);

         return;
      } else {
         _jchartRender.fillPlotSection(
               jchartComposite.getChartTypeModel().isSinglePlot());
      }

      if (timeFrameModel.isIntraday()) {
         plotDataModel.setIntradayCount(chartSizeModel.isSmallXWidth());
      }

      if (jchartComposite.getChartTypeModel().isSinglePlot()) {
         // grid lines

         if (timeFrameModel.isIntraday()) {
            xLabelsPriceIntraday(jchartComposite, true);
         } else {
            plotDataModel.setCoordinates(Kjchart.CT_LABEL, Kjchart.POS_PRICE,
                  _jchartRender);
            plotDataModel.setCoordinates(Kjchart.CT_LABEL,
                  Kjchart.POS_SINGLE_PLOT, _jchartRender);
            xLabelsPrice(jchartComposite, true);
         }

         plotDataModel.setCoordinates(Kjchart.CT_PRICE, Kjchart.POS_SINGLE_PLOT,
               _jchartRender);

      } else {
         // grid lines
         plotDataModel.setCoordinates(Kjchart.CT_LABEL, Kjchart.POS_PRICE,
               _jchartRender);

         if (timeFrameModel.isIntraday()) {
            xLabelsPriceIntraday(jchartComposite, true);
            plotDataModel.setCoordinates(Kjchart.CT_LABEL,
                  Kjchart.POS_INDICATOR, _jchartRender);
            xLabelsPriceIntraday(jchartComposite, true);
         } else {
            xLabelsPrice(jchartComposite, true);
            plotDataModel.setCoordinates(Kjchart.CT_LABEL,
                  Kjchart.POS_INDICATOR, _jchartRender);
            xLabelsPrice(jchartComposite, true);
         }

         plotDataModel.setCoordinates(Kjchart.CT_PRICE, Kjchart.POS_PRICE,
               _jchartRender);
      }

      // *psr check here 5 year
      yLabels(jchartComposite);

      // end grid lines
      if (chartTypeModel.isComparison()) {
         plotComparison(jchartComposite);
      } else if (chartTypeModel.isPriceLine()) {
         plotQuoteLine(jchartComposite, ColorValue.PRICE);
      } else if (chartTypeModel.isPriceCandle()) {
         plotCandle(jchartComposite);
      } else if (chartTypeModel.isPriceMountian()) {
         plotMountainPrice(jchartComposite);
         plotQuoteLine(jchartComposite, ColorValue.MOUNTBORDER);
      } else {
         if (_jchartRender.isSwt()) {
            plotOHLCSwt(jchartComposite);
         } else {
            plotOHLC(jchartComposite);
         }
      }

      if (chartTypeModel.isPriceMaOn()) {
         plotMovingAverages(indicatorModel, plotRange);
      }

      if (chartTypeModel.isBandsOn()) {
         plotBands(indicatorModel, plotRange);
      }

      plotDataModel.setCoordinates(Kjchart.CT_LABEL, Kjchart.POS_BOTTOM_LABEL,
            _jchartRender);

      if (timeFrameModel.isIntraday()) {
         xLabelsPriceIntraday(jchartComposite, false);
      } else {
         xLabelsPrice(jchartComposite, false);
      }

      String sblTxt = "";

      if (!chartTypeModel.isComparison()) {
         if (chartSizeModel.printConame()) {
            sblTxt = jchartComposite.getQuoteDataModel().getTickerConame();
         } else {
            sblTxt = jchartComposite.getQuoteDataModel().getTicker();
         }
      }
      if (_isAndroid) {
         Quote lastQuote = getLastQuote(jchartComposite);
         switch (_orientation) {
         case VERTICAL:
            sblTxt = jchartComposite.getQuoteDataModel().getTicker();
            break;
         case HORIZONTAL:
            sblTxt = jchartComposite.getQuoteDataModel().getTickerConame();
            break;
         }

         sblTxt += (" " + FormatUtils.formatDate(lastQuote.getDate())
               + " Close: "
               + FormatUtils.formatDecimal(lastQuote.getClose() + "", 3));

      } else {
         if (chartSizeModel.printDesc()) {
            ChartOption priceOption = chartTypeModel.getPriceOption();
            sblTxt += ("              " + priceOption.getName());
         }
      }

      plotDataModel.setCoordinates(Kjchart.CT_LABEL, Kjchart.POS_PRICE,
            _jchartRender);

      if (jchartComposite.getChartTypeModel().isComparison()) {
         sblTxt = quoteDataModel.getTicker();
      }

      if (chartTypeModel.isComparison()) {
         _jchartRender.drawaLabel(ColorValue.COLOR_YELLOW, ColorValue.TOPBACK,
               sblTxt, 0);
         _jchartRender.drawNextHorizLabel(ColorValue.COLOR_WHITE, sblTxt,
               " vs ", 0);
         sblTxt += " vs ";
         _jchartRender.drawNextHorizLabel(ColorValue.COLOR_GREEN, sblTxt,
               quoteDataModel.getCompareTicker(), 0);
      } else {
         _jchartRender.drawaLabel(ColorValue.TOPCAPTION, ColorValue.TOPBACK,
               sblTxt, 0);
      }

      if (chartTypeModel.isPriceMaOn()) {
         if (chartSizeModel.printDesc()) {
            drawMALabels(jchartComposite, sblTxt + " ");
         }
      }

      // mid left label text
      if (chartSizeModel.printDesc()) {
         String midLeftLabel = JchartModelFacade.getChartText();
         _jchartRender.drawMidLeftLabel(midLeftLabel,
               chartTypeModel.isSinglePlot());
      }

      // mid right label text
      int endPlotRec = plotRange.getEndPlotRec();
      Quote quote1 = jchartComposite.getQuoteDataModel()
            .getQuote(endPlotRec - 1);
      Quote quote2 = jchartComposite.getQuoteDataModel()
            .getQuote(endPlotRec - 2);
      String midRightLabel = new QuoteString(false, quote1, quote2.getClose())
            .toString();
      _jchartRender.drawMidRightLabel(midRightLabel,
            chartTypeModel.isSinglePlot());

      _jchartRender.drawPlotSectionBorder(chartTypeModel.isSinglePlot(),
            chartTypeModel.isGif());

      if (chartTypeModel.isSinglePlot()) {
         displayImage();

         return;
      }

      boolean vol = false;
      ChartOption indicatorOption = chartTypeModel.getIndicatorOption();

      IndicatorIntr indicator = indicatorModel.getIndicator(indicatorOption);

      if (indicatorOption == ChartOption.MONEYFLOW) {
         plotDataModel.hiloRangeIndicator(indicator.getHistogram().getVal());
         plotDataModel.setCoordinates(Kjchart.CT_INDICATOR,
               Kjchart.POS_INDICATOR, _jchartRender);

         float xaxis = plotHist(ColorValue.COLOR_WHITE, ColorValue.COLOR_RED,
               indicator.getHistogram().getVal(), plotDataModel);
         threshHoldLines(plotDataModel, colorScheme, indicatorOption, xaxis);

      } else if (indicatorOption == ChartOption.MACD) {
         plotDataModel.hiloRangeIndicator(indicator.getVal());
         plotDataModel.setCoordinates(Kjchart.CT_INDICATOR,
               Kjchart.POS_INDICATOR, _jchartRender);
         _jchartRender.setImageColor(ColorValue.COLOR_GREEN);
         plotLine(plotRange, ColorValue.COLOR_GREEN, indicator.getVal());

         float xaxis = plotLine(plotRange, ColorValue.COLOR_RED,
               indicator.getSignalLine().getVal());
         threshHoldLines(plotDataModel, colorScheme, indicatorOption, xaxis);

      } else if (indicatorOption == ChartOption.MACDHIST) {
         plotDataModel.hiloRangeIndicator(indicator.getHistogram().getVal());
         plotDataModel.setCoordinates(Kjchart.CT_INDICATOR,
               Kjchart.POS_INDICATOR, _jchartRender);
         _jchartRender.setImageColor(ColorValue.COLOR_GREEN);

         float xaxis = plotHist(ColorValue.COLOR_GREEN, ColorValue.COLOR_RED,
               indicator.getHistogram().getVal(), plotDataModel);
         threshHoldLines(plotDataModel, colorScheme, indicatorOption, xaxis);

      } else if (indicatorOption == ChartOption.STOCHASTIC) {
         plotDataModel.setIhi(100);
         plotDataModel.setIlo(0);
         plotDataModel.setCoordinates(Kjchart.CT_INDICATOR,
               Kjchart.POS_INDICATOR, _jchartRender);
         _jchartRender.setImageColor(ColorValue.COLOR_YELLOW);
         plotLine(plotRange, ColorValue.COLOR_YELLOW, indicator.getVal());

         float xaxis = plotLine(plotRange, ColorValue.COLOR_WHITE,
               indicator.getSignalLine().getVal());
         threshHoldLines(plotDataModel, colorScheme, indicatorOption, xaxis);

      } else if (indicatorOption == ChartOption.RSI) {
         plotDataModel.setIhi(100);
         plotDataModel.setIlo(0);
         plotDataModel.setCoordinates(Kjchart.CT_INDICATOR,
               Kjchart.POS_INDICATOR, _jchartRender);
         _jchartRender.setImageColor(ColorValue.COLOR_GREEN);

         float xaxis = plotLine(plotRange, ColorValue.COLOR_GREEN,
               indicator.getVal());
         threshHoldLines(plotDataModel, colorScheme, indicatorOption, xaxis);

      } else if (indicatorOption == ChartOption.RELATIVE_STRENGTH) {
         plotDataModel.hiloRangeIndicator(indicator.getVal(),
               indicator.getVal2().getVal());
         plotDataModel.setCoordinates(Kjchart.CT_INDICATOR,
               Kjchart.POS_INDICATOR, _jchartRender);
         _jchartRender.setImageColor(ColorValue.COLOR_WHITE);

         plotLine(plotRange, ColorValue.COLOR_WHITE, indicator.getVal());
         plotLine(plotRange, ColorValue.COLOR_GREEN,
               indicator.getVal1().getVal());
         float xaxis = plotLine(plotRange, ColorValue.COLOR_RED,
               indicator.getVal2().getVal());
         threshHoldLines(plotDataModel, colorScheme, indicatorOption, xaxis);

      } else if (indicatorOption == ChartOption.ADX) {
         plotDataModel.hiloRangeIndicator(indicator.getVal(),
               indicator.getVal1().getVal(), indicator.getVal2().getVal());
         plotDataModel.setCoordinates(Kjchart.CT_INDICATOR,
               Kjchart.POS_INDICATOR, _jchartRender);
         _jchartRender.setImageColor(ColorValue.COLOR_WHITE);

         plotLine(plotRange, ColorValue.COLOR_WHITE, indicator.getVal());
         plotLine(plotRange, ColorValue.COLOR_GREEN,
               indicator.getVal1().getVal());
         float xaxis = plotLine(plotRange, ColorValue.COLOR_RED,
               indicator.getVal2().getVal());
         threshHoldLines(plotDataModel, colorScheme, indicatorOption, xaxis);

      } else if (indicatorOption == ChartOption.VOLUME) {
         vol = true;
      }

      String chartTypeText = indicatorOption.getName();
      String chartTypeDesc = indicatorOption.getDesc();

      if (vol) {
         chartTypeText = "Volume";
         plotDataModel.setCoordinates(Kjchart.CT_VOLUME, Kjchart.POS_INDICATOR,
               _jchartRender);

         yIndicatorLabels(plotDataModel, ChartOption.VOLUME);

         if (jchartComposite.getChartTypeModel().isVolMountain()) {
            plotMountainVol(jchartComposite);
         } else {
            if (_jchartRender.isSwt()) {
               plotVolSwt(jchartComposite);
            } else {
               plotVol(jchartComposite);
            }
         }
      } else {
         yIndicatorLabels(plotDataModel, indicatorOption);
      }

      plotDataModel.setCoordinates(Kjchart.CT_PRICE, Kjchart.POS_PRICE,
            _jchartRender);
      _jchartRender.drawTrendLines();

      plotDataModel.setCoordinates(Kjchart.CT_LABEL, Kjchart.POS_INDICATOR,
            _jchartRender);
      _jchartRender.drawaLabel(ColorValue.BOTCAPTION, ColorValue.BOTBACK,
            chartTypeText + " " + chartTypeDesc, 0);
      displayImage();
      plotDataModel.setCoordinates(Kjchart.CT_PRICE, Kjchart.POS_PRICE,
            _jchartRender);
      _jchartRender.drawPlotSectionBorder(
            jchartComposite.getChartTypeModel().isSinglePlot(),
            jchartComposite.getChartTypeModel().isGif());
   }

   /**
    * @param indicatorModel
    * @param plotRange
    */
   private void plotBands(IndicatorModel indicatorModel, PlotRange plotRange) {
      IndicatorIntr bands = indicatorModel.getIndicator(ChartOption.BBANDS);
      if (bands != null) {
         plotLine(plotRange, ColorValue.COLOR_MAGENTA, bands.getVal());
         plotLine(plotRange, ColorValue.COLOR_GRAY, bands.getVal1().getVal());
         plotLine(plotRange, ColorValue.COLOR_GRAY, bands.getVal2().getVal());
      }
   }

   private void plotMovingAverages(IndicatorModel indicatorModel,
         PlotRange plotRange) {
      IndicatorIntr priceMa1 = indicatorModel.getPriceMa(Kjchart.PRICEMA1);
      IndicatorIntr priceMa2 = indicatorModel.getPriceMa(Kjchart.PRICEMA2);
      IndicatorIntr priceMa3 = indicatorModel.getPriceMa(Kjchart.PRICEMA3);

      if (priceMa1 != null) {
         plotLine(plotRange, ColorValue.COLOR_MAGENTA, priceMa1.getVal());
      }

      if (priceMa2 != null) {
         plotLine(plotRange, ColorValue.COLOR_CYAN, priceMa2.getVal());
      }

      if (priceMa3 != null) {
         plotLine(plotRange, ColorValue.COLOR_RED, priceMa3.getVal());
      }
   }

   private void plotComparison(JchartComposite jchartComposite) {
      plotComparison(jchartComposite, 2, ColorValue.COLOR_YELLOW);
      plotComparison(jchartComposite, 3, ColorValue.COLOR_GREEN);
   }

   private void plotComparison(JchartComposite jchartComposite, int rsNum,
         ColorValue colorValue) {
      int startRec;
      int xaxis = 1;
      float moveFrom;
      float moveTo;
      int i = jchartComposite.getPlotDataModel().getPlotRange()
            .getBeginPlotRec();
      Quote[][] rsQuotes = jchartComposite.getQuoteDataModel()
            .getCompareQuotes();

      while (rsQuotes[rsNum][i].getOpen() == Kjchart.IFILLER) {
         xaxis += Kjchart.XEXPAND;
         i++;
      }

      xaxis -= 1; // center on close

      if (xaxis < 1) {
         xaxis = 1;
      }

      i++;
      startRec = i - 1;
      moveFrom = rsQuotes[rsNum][startRec].getClose();

      for (i = startRec; i < jchartComposite.getPlotDataModel().getPlotRange()
            .getEndPlotRec(); i++) {
         moveTo = rsQuotes[rsNum][i].getClose();
         _jchartRender.drawaLine(colorValue, xaxis - 1f, moveFrom, xaxis + 1f,
               moveTo);
         moveFrom = moveTo;
         xaxis += Kjchart.XEXPAND;
      }
   }

   private float plotHist(ColorValue clPlusZero, ColorValue clMinusZero,
         float[] val, PlotDataModel plotDataModel) {
      int startRec;
      int xaxis = 1;
      float moveTo;
      int i = plotDataModel.getPlotRange().getBeginPlotRec();

      boolean filler = false;
      if (val[i] == Kjchart.IFILLER)
         filler = true;
      while (val[i] == Kjchart.IFILLER) {
         xaxis += Kjchart.XEXPAND;
         i++;
      }

      if (filler)
         startRec = i + 2;
      else
         startRec = plotDataModel.getPlotRange().getBeginPlotRec();

      for (i = startRec; i < plotDataModel.getPlotRange()
            .getEndPlotRec(); i++) {
         moveTo = val[i];

         if (moveTo < 0) {
            if (plotDataModel.getIhi() < 0) {
               _jchartRender.drawaLine(clMinusZero, xaxis,
                     plotDataModel.getIhi(), xaxis, moveTo);
            } else {
               _jchartRender.drawaLine(clMinusZero, xaxis, 0, xaxis, moveTo);
            }
         } else if (plotDataModel.getIlo() > 0) {
            _jchartRender.drawaLine(clPlusZero, xaxis, plotDataModel.getIlo(),
                  xaxis, moveTo);
         } else {
            _jchartRender.drawaLine(clPlusZero, xaxis, 0, xaxis, moveTo);
         }

         xaxis += Kjchart.XEXPAND;
      }

      xaxis -= (Kjchart.XEXPAND / 2);

      return xaxis;
   }

   private void plotPnf(PlotDataModel plotDataModel, ColorScheme colorScheme,
         Frect frect) {
      float x1 = 1;
      float x2 = 1;
      float y1 = plotDataModel.calcFirstY1PnF();
      float y2 = y1;
      float xAddO;
      float xInc = 20f;
      float ylabpos;
      String yy;
      String prevyy;

      Pnf pnf = plotDataModel.getPnf();

      yy = "  ";
      prevyy = " _";

      ylabpos = 1;

      for (PnfItem pnfItem : pnf.getDisplayPnfItems()) {
         boolean isXBox = pnfItem.isXBox();
         int numBoxes = Math.abs(pnfItem.getNumBoxes());

         String dtStr = pnfItem.getDate();
         yy = dtStr.substring(8, 10);

         if (!yy.equals(prevyy)) {
            _jchartRender.drawaString(ColorValue.XYLABEL, yy, x1, ylabpos);
            prevyy = yy;
         }

         // x-axis increments
         x1 = x2;
         x2 = x1 + xInc;

         // y-axis
         float price = pnfItem.getPrice();
         y1 = pnf.getPlotVal(price) + 1;
         y2 = y1 - 1;

         for (int j = 0; j < numBoxes; j++) {
            if (isXBox) {
               _jchartRender.drawaLine(ColorValue.COLOR_GREEN, x1, y1, x2, y2);
               _jchartRender.drawaLine(ColorValue.COLOR_GREEN, x2, y1, x1, y2);

               y1--;
               y2 = y1 - 1;

            } else {
               xAddO = (xInc / 3.0f);
               _jchartRender.drawaLine(ColorValue.COLOR_RED, x1 + xAddO, y1,
                     x1 + (xAddO * 2f), y1);
               _jchartRender.drawaLine(ColorValue.COLOR_RED, x1 + (xAddO * 2f),
                     y1, x2, y1 - 0.33f);
               _jchartRender.drawaLine(ColorValue.COLOR_RED, x2, y1 - 0.33f, x2,
                     y1 - 0.66f);
               _jchartRender.drawaLine(ColorValue.COLOR_RED, x2, y1 - 0.66f,
                     x1 + (xAddO * 2f), y2);
               _jchartRender.drawaLine(ColorValue.COLOR_RED, x1 + (xAddO * 2f),
                     y2, x1 + xAddO, y2);
               _jchartRender.drawaLine(ColorValue.COLOR_RED, x1 + xAddO, y2, x1,
                     y1 - 0.66f);
               _jchartRender.drawaLine(ColorValue.COLOR_RED, x1, y1 - 0.66f, x1,
                     y1 - 0.33f);
               _jchartRender.drawaLine(ColorValue.COLOR_RED, x1, y1 - 0.33f,
                     x1 + xAddO, y1);

               y1++;
               y2 = y1 - 1;
            }
         }
      }

      pnFGrid(frect, xInc, plotDataModel, colorScheme);
   }

   private void pnFGrid(Frect frect, float xInc, PlotDataModel plotDataModel,
         ColorScheme colorScheme) {
      Pnf pnf = plotDataModel.getPnf();
      int xLableRightPos = pnf.getXLabelRightPos();
      int startRow;
      float endRow;
      startRow = (int) pnf.getPnfLo() - pnf.getBoxPad();
      endRow = pnf.getPnfHi() + pnf.getBoxPad();
      int plotLineCount = 0;

      for (int y = startRow; y < endRow; y++) {
         plotLineCount++;
         float plotVal = pnf.getPlotVal(y);
         int rowInc = pnf.getRowInc(y);
         if (rowInc == plotLineCount) {
            _jchartRender.drawaString(ColorValue.GRID, y + "", 1, plotVal);
            _jchartRender.drawaString(ColorValue.GRID, y + "",
                  frect.right - xLableRightPos, plotVal);

            _jchartRender.drawaDotLineX(pnf.getDisplayPnfItems().size() * 21, 0,
                  plotVal, frect.right, plotVal);

            // _jchartRender.drawaLine(ColorValue.GRID,
            /// 0, plotVal, frect.right, plotVal);
            plotLineCount = 0;
         }
      }
   }

   private void plotOHLC(JchartComposite jchartComposite) {
      PlotDataModel plotDataModel = jchartComposite.getPlotDataModel();
      PlotRange plotRange = plotDataModel.getPlotRange();
      Quote[] quotes = jchartComposite.getQuoteDataModel().getQuotes();
      int xaxis = 1;
      int numplotRecs = plotRange.getNumPlotRecs();

      for (int i = plotRange.getBeginPlotRec(); i < plotRange
            .getEndPlotRec(); i++) {
         if (numplotRecs < TimeFrameModel.THREEMONTHS) {
            // hi low rect
            _jchartRender.fillaRect(ColorValue.PRICE, xaxis - 0.25f,
                  quotes[i].getHi(), +0.5f, quotes[i].getLow());

            // adjust - if open is the low, rect not filled compeletly
            if (quotes[i].getOpen() > 0) { // bad data
               _jchartRender.drawaLine(ColorValue.PRICE, xaxis,
                     quotes[i].getOpen(), xaxis + 0.15f, quotes[i].getOpen());
            }

            // adjust if close is low,, rect not filled compeletly
            if (quotes[i].getClose() == quotes[i].getLow()) {
               _jchartRender.drawaLine(ColorValue.PRICE, xaxis,
                     quotes[i].getClose(), xaxis - 0.15f, quotes[i].getClose());
            }

         } else {
            // hi low line
            _jchartRender.drawaLine(ColorValue.PRICE, xaxis, quotes[i].getHi(),
                  xaxis, quotes[i].getLow());
         }

         // close
         _jchartRender.drawaLine(ColorValue.PRICE, xaxis, quotes[i].getClose(),
               xaxis + 0.5f, quotes[i].getClose());

         // open
         if (quotes[i].getOpen() > 0) {
            _jchartRender.drawaLine(ColorValue.PRICE, xaxis - 0.5f,
                  quotes[i].getOpen(), xaxis, quotes[i].getOpen());
         }

         xaxis += Kjchart.XEXPAND;
      }
   }

   private void plotOHLCSwt(JchartComposite jchartComposite) {
      PlotDataModel plotDataModel = jchartComposite.getPlotDataModel();
      PlotRange plotRange = plotDataModel.getPlotRange();
      Quote[] quotes = jchartComposite.getQuoteDataModel().getQuotes();
      int xaxis = 1;
      int numplotRecs = plotRange.getNumPlotRecs();

      for (int i = plotRange.getBeginPlotRec(); i < plotRange
            .getEndPlotRec(); i++) {
         if (numplotRecs < TimeFrameModel.THREEMONTHS) {

            // adjust - if open is the low, rect not filled compeletly
            if (quotes[i].getOpen() > 0) { // bad data
               _jchartRender.drawaLine(ColorValue.PRICE, xaxis,
                     quotes[i].getOpen(), xaxis + 0.15f, quotes[i].getOpen());
            }

            // adjust if close is low,, rect not filled compeletly
            if (quotes[i].getClose() == quotes[i].getLow()) {
               _jchartRender.drawaLine(ColorValue.PRICE, xaxis,
                     quotes[i].getClose(), xaxis - 0.15f, quotes[i].getClose());
            }

            // hi low rect
            if (numplotRecs > TimeFrameModel.ONEWEEK) {
               _jchartRender.fillaRect(ColorValue.PRICE, xaxis - 0.40f,
                     quotes[i].getHi(), +0.20f, quotes[i].getLow());
               // open
               if (quotes[i].getOpen() > 0) {
                  _jchartRender.drawaLine(ColorValue.PRICE, xaxis - 0.7f,
                        quotes[i].getOpen(), xaxis, quotes[i].getOpen());
               }
            } else {
               _jchartRender.fillaRect(ColorValue.PRICE, xaxis - 0.25f,
                     quotes[i].getHi(), +0.40f, quotes[i].getLow());
            }

         } else {
            // hi low line
            _jchartRender.drawaLine(ColorValue.PRICE, xaxis, quotes[i].getHi(),
                  xaxis, quotes[i].getLow());
         }

         // close
         if ((numplotRecs < TimeFrameModel.THREEMONTHS)
               && (numplotRecs > TimeFrameModel.ONEWEEK)) {
            _jchartRender.drawaLine(ColorValue.PRICE, xaxis,
                  quotes[i].getClose(), xaxis + 0.8f, quotes[i].getClose());

         } else {
            _jchartRender.drawaLine(ColorValue.PRICE, xaxis,
                  quotes[i].getClose(), xaxis + 0.5f, quotes[i].getClose());
         }

         // open
         if (quotes[i].getOpen() > 0) {
            _jchartRender.drawaLine(ColorValue.PRICE, xaxis - 0.5f,
                  quotes[i].getOpen(), xaxis, quotes[i].getOpen());
         }
         xaxis += Kjchart.XEXPAND;
      }
   }

   private void plotCandle(JchartComposite jchartComposite) {
      PlotDataModel plotDataModel = jchartComposite.getPlotDataModel();
      Quote[] quotes = jchartComposite.getQuoteDataModel().getQuotes();
      PlotRange plotRange = plotDataModel.getPlotRange();
      int xaxis = 1;
      boolean darkBody;

      for (int i = plotRange.getBeginPlotRec(); i < plotRange
            .getEndPlotRec(); i++) {
         if (quotes[i].getOpen() <= 0) {
            quotes[i].setOpen(quotes[i].getClose());
         }

         if (quotes[i].getOpen() < quotes[i].getClose()) {
            darkBody = false;
         } else {
            darkBody = true;
         }

         if (darkBody) {
            // draw a box
            _jchartRender.fillaRect(ColorValue.PRICE, xaxis - 0.5f,
                  quotes[i].getOpen(), 1f, quotes[i].getClose());

            // if (quotes[i].getOpen() == quotes[i].getClose()) {
            // draw again because of draw rect inacuracy if op - cl too close
            _jchartRender.drawaLine(ColorValue.PRICE, xaxis - 0.5f,
                  quotes[i].getOpen(), xaxis + 0.4f, quotes[i].getOpen());

            // adjustment - redraw
            for (float j = xaxis - 0.5f; j < (xaxis + 0.5f); j += 0.01f)
               _jchartRender.drawaLine(ColorValue.PRICE, j,
                     quotes[i].getClose(), j, quotes[i].getOpen());

            // }
            // draw tails
            _jchartRender.drawaLine(ColorValue.PRICE, xaxis, quotes[i].getHi(),
                  xaxis, quotes[i].getOpen());

            _jchartRender.drawaLine(ColorValue.PRICE, xaxis,
                  quotes[i].getClose(), xaxis, quotes[i].getLow());
         } else {
            _jchartRender.drawaRect(ColorValue.PRICE, xaxis - 0.5f,
                  quotes[i].getClose(), 1f, quotes[i].getOpen());

            // if ( (quotes[i].getOpen() - quotes[i].getClose()) < .1) {
            // draw again because of draw rect inacuracy if op - cl too close
            _jchartRender.drawaLine(ColorValue.PRICE, xaxis - 0.05f,
                  quotes[i].getOpen(), xaxis + 0.04f, quotes[i].getOpen());

            // }
            // draw tails
            _jchartRender.drawaLine(ColorValue.PRICE, xaxis, quotes[i].getHi(),
                  xaxis, quotes[i].getClose());

            _jchartRender.drawaLine(ColorValue.PRICE, xaxis,
                  quotes[i].getOpen(), xaxis, quotes[i].getLow());
         }

         xaxis += Kjchart.XEXPAND;
      }
   }

   private void plotVol(JchartComposite jchartComposite) {
      PlotDataModel plotDataModel = jchartComposite.getPlotDataModel();
      Quote[] quotes = jchartComposite.getQuoteDataModel().getQuotes();
      PlotRange plotRange = plotDataModel.getPlotRange();
      int xaxis = 1;
      int numplotRecs = plotRange.getNumPlotRecs();

      for (int i = plotRange.getBeginPlotRec(); i < plotRange
            .getEndPlotRec(); i++) {
         if (numplotRecs < TimeFrameModel.THREEMONTHS) {
            _jchartRender.fillaRect(ColorValue.VOL, xaxis - 0.25f,
                  quotes[i].getVolume(), +0.50f, 0);
         } else {
            _jchartRender.drawaLine(ColorValue.VOL, xaxis, 0, xaxis,
                  quotes[i].getVolume());
         }

         xaxis += Kjchart.XEXPAND;
      }
   }

   private void plotVolSwt(JchartComposite jchartComposite) {
      PlotDataModel plotDataModel = jchartComposite.getPlotDataModel();
      Quote[] quotes = jchartComposite.getQuoteDataModel().getQuotes();
      PlotRange plotRange = plotDataModel.getPlotRange();
      int xaxis = 1;
      int numplotRecs = plotRange.getNumPlotRecs();

      for (int i = plotRange.getBeginPlotRec(); i < plotRange
            .getEndPlotRec(); i++) {
         if (numplotRecs < TimeFrameModel.THREEMONTHS) {
            if (numplotRecs <= TimeFrameModel.ONEWEEK) {
               _jchartRender.fillaRect(ColorValue.VOL, xaxis - 0.25f,
                     quotes[i].getVolume(), +0.40f, 0);
            } else {
               /*
                * _jchartRender.fillaRect(ColorValue.VOL, xaxis - 0.25f,
                * quotes[i].getVolume(), +0.50f, 0);
                */ }
         } else {
            _jchartRender.drawaLine(ColorValue.VOL, xaxis, 0, xaxis,
                  quotes[i].getVolume());
         }

         xaxis += Kjchart.XEXPAND;
      }
   }

   private float plotLine(PlotRange plotRange, ColorValue colorValue,
         float[] val) {
      return plotLine(plotRange, colorValue, val, false);
   }

   private float plotLine(PlotRange plotRange, ColorValue colorValue,
         float[] val, boolean priceMA) {
      int startRec;
      int xaxis = 0;
      float moveFrom;
      float moveTo;
      int i = plotRange.getBeginPlotRec();

      while (val[i] == Kjchart.IFILLER) {
         xaxis += Kjchart.XEXPAND;
         i++;
      }

      i++;
      startRec = i - 1;
      moveFrom = val[startRec];

      if (!priceMA) {
         if (moveFrom == 0) {
            startRec += 2;
            moveFrom = val[startRec];
            xaxis += (Kjchart.XEXPAND * 2);
         }

         moveTo = val[startRec];
         _jchartRender.drawaLine(colorValue, 0f, moveFrom, xaxis + 1f, moveTo);
      }

      for (i = startRec; i < plotRange.getEndPlotRec(); i++) {
         moveTo = val[i];
         _jchartRender.drawaLine(colorValue, xaxis - 1f, moveFrom, xaxis + 1f,
               moveTo);
         moveFrom = moveTo;
         xaxis += Kjchart.XEXPAND;
      }

      return xaxis;
   }

   private void threshHoldLines(PlotDataModel plotDataModel,
         ColorScheme colorScheme, ChartOption chartOption, float xaxis) {
      if ((chartOption == ChartOption.STOCHASTIC)
            || (chartOption == ChartOption.RSI)) {
         _jchartRender.drawaLine(ColorValue.GRID, 0, 20, xaxis, 20);
         _jchartRender.drawaLine(ColorValue.GRID, 0, 80, xaxis, 80);
         _jchartRender.drawaLine(ColorValue.COLOR_RED, 0, 50, xaxis, 50);
      } else if (((chartOption == ChartOption.MACD)
            || (chartOption == ChartOption.MACDHIST)
            || (chartOption == ChartOption.MONEYFLOW))
            && (plotDataModel.getIhi() > 0)) {
         _jchartRender.drawaLine(ColorValue.GRID, 1, 0, xaxis, 0);
      } else if (chartOption == ChartOption.ADX) {
         _jchartRender.drawaLine(ColorValue.GRID, 0, 30, xaxis, 30);
         _jchartRender.drawaLine(ColorValue.GRID, 0, 23, xaxis, 23);
         _jchartRender.drawaLine(ColorValue.GRID, 0, 22, xaxis, 22);
         _jchartRender.drawaLine(ColorValue.GRID, 0, 15, xaxis, 15);
      }
   }

   // ******* Start Labels ***************************************
   private void yLabels(JchartComposite jchartComposite) {
      PlotDataModel plotDataModel = jchartComposite.getPlotDataModel();
      YlabelCalc ylabelCalc = plotDataModel.getYlabelCalc();
      boolean isComparison = jchartComposite.getChartTypeModel().isComparison();
      boolean isSmallHeight = jchartComposite.getChartSizeModel()
            .isSmallHeight();
      float yLabelLo = ylabelCalc.getLabelLo();
      int numLabels = ylabelCalc.getNumLabels();
      float inc = ylabelCalc.getInc();

      float tx = (plotDataModel.getPlotRange().getNumPlotRecs()) * 2; // xexpand
      float yLabel = yLabelLo;
      boolean skip = false;

      for (int i = 0; i < numLabels; i++) {
         if (yLabel <= plotDataModel.getPhi()) {
            if (skip) {
               skip = false;
               yLabel += inc;

               continue;
            }

            if (isSmallHeight) {
               skip = true;
            }

            Float f;

            if (isComparison) {
               f = new Float(yLabel - 100);
            } else {
               f = new Float(yLabel);
            }

            _jchartRender.drawYlabelRight(yLabel, FormatUtils
                  .formatDecimal(f.toString(), ylabelCalc.getDecimalPos()));

            _jchartRender.drawaDotLineX(
                  plotDataModel.getPlotRange().getNumPlotRecs(), tx, yLabel, 1,
                  yLabel);
         }

         yLabel += inc;
      }

      if (isComparison) {
         _jchartRender.drawYlabelRightTop("% Chg", 1);
      } else {
         _jchartRender.drawYlabelRightTop("Price", 1);
      }
   }

   private void yIndicatorLabels(PlotDataModel plotDataModel,
         ChartOption chartOption) {
      float yLabelInc;
      float yLabel;
      float yRange;
      int numY;

      if (chartOption == ChartOption.VOLUME) {
         plotDataModel.setIhi(plotDataModel.getVhi());
         plotDataModel.setIlo(0);
      }

      yRange = plotDataModel.getIhi() - plotDataModel.getIlo();

      // Y-Axis Labels for _jchart.getIndicatorModel()
      if (yRange == 0) {
         return;
      }

      numY = 4;

      yLabelInc = yRange / numY;
      yLabel = plotDataModel.getIlo();

      if (chartOption == ChartOption.VOLUME) {
         yRange = plotDataModel.getIhi();
         yLabel = 0;
         yLabelInc = yRange / numY;
      } else if ((chartOption == ChartOption.RSI)
            || (chartOption == ChartOption.STOCHASTIC)) {
         yLabel = 0;
         yRange = 100;
         yLabelInc = 20;
         numY = 5;
      }

      numY++;

      // Yfmt := '%4.2f';
      String yLabelText;
      int posDecimal;

      Float f = new Float(yLabel);

      if (chartOption == ChartOption.VOLUME) {
         _jchartRender.drawYlabelRight(yLabel,
               FormatUtils.formatDecimal(f.toString(), 0));
      } else {
         yLabelText = f.toString();
         posDecimal = yLabelText.indexOf(".");
         _jchartRender.drawYlabelRight(yLabel,
               FormatUtils.formatDecimal(f.toString(), 4 - posDecimal));
      }

      float tx = (plotDataModel.getPlotRange().getNumPlotRecs()) * 2; // xexpand
      yLabel = yLabel + yLabelInc;

      for (int i = 2; i < (numY + 1); i++) {
         if (chartOption == ChartOption.VOLUME) {
            _jchartRender.drawYlabelRight(yLabel,
                  FormatUtils.formatVol((long) yLabel));
         } else {
            f = new Float(yLabel);
            yLabelText = f.toString();
            posDecimal = yLabelText.indexOf(".");
            _jchartRender.drawYlabelRight(yLabel,
                  FormatUtils.formatDecimal(f.toString(), 4 - posDecimal));
         }

         _jchartRender.drawaDotLineX(
               plotDataModel.getPlotRange().getNumPlotRecs(), tx, yLabel, 1,
               yLabel);
         yLabel += yLabelInc;
      }

      if (chartOption == ChartOption.VOLUME) {
         String shares = JchartModelFacade.getJchartProps()
               .getProperty("shares.txt");
         _jchartRender.drawYlabelRightTop(shares, 2);
      }
   }

   private void xLabelsPriceIntraday(JchartComposite jchartComposite,
         boolean grid) {
      ChartSizeModel chartSizeModel = jchartComposite.getChartSizeModel();
      PlotDataModel plotDataModel = jchartComposite.getPlotDataModel();
      PlotRange plotRange = plotDataModel.getPlotRange();
      int intradayMinInc = plotDataModel.getIntradayMinInc();
      int x = 0;
      String labeldt = "";
      int textBottom = 75;
      int counter = 0;
      int offset = 0;

      // do first label
      if (!chartSizeModel.isSmallXWidth()) {

         if (!grid) {
            if (jchartComposite.getChartTypeModel().isSinglePlot()) {
               _jchartRender.drawXLabel(ColorValue.XYLABEL, labeldt, x);
               _jchartRender.drawXLabelTick(ColorValue.XYLABEL, x);
            } else {
               _jchartRender.drawaString(ColorValue.XYLABEL, labeldt, x,
                     textBottom);
            }
         }
      } else {
         x = intradayMinInc;
         offset = 30;
         counter = intradayMinInc;
      }

      for (int i = plotRange.getBeginPlotRec() + offset; i < plotRange
            .getEndPlotRec(); i++) {
         if (counter == intradayMinInc) {

            if (grid) {
               _jchartRender.drawaDotLineY(x);
            } else {
               if (jchartComposite.getChartTypeModel().isSinglePlot()) {
                  _jchartRender.drawXLabel(ColorValue.XYLABEL, labeldt, x);
                  _jchartRender.drawXLabelTick(ColorValue.XYLABEL, x);
               } else {
                  _jchartRender.drawaString(ColorValue.XYLABEL, labeldt, x,
                        textBottom);
               }
            }

            counter = 0;
         }

         counter++;
         x += 2; // xexpand
      }
   }

   private void xLabelsPrice(JchartComposite jchartComposite, boolean grid) {
      PlotDataModel plotDataModel = jchartComposite.getPlotDataModel();
      Quote[] quotes = jchartComposite.getQuoteDataModel().getQuotes();
      PlotRange plotRange = plotDataModel.getPlotRange();
      TimeFrameModel timeFrameModel = jchartComposite.getTimeFrameModel();
      int textBottom = 75;

      int x = 0;
      int numPlotRecs = plotRange.getNumPlotRecs();
      // System.out.println("numPlotRecs: "+ numPlotRecs);
      int pointCount = 0;
      int curMonthSave = 0;
      boolean printLabel = false;
      boolean firstTime = true;
      String labeltxt = null;
      int monthsSkipped = 0;
      int monthsToSkip = 0;
      int pointsBetweenLabels = 0;

      if (numPlotRecs >= (TimeFrameModel.TWOHALFYEAR * 2)) {
         monthsToSkip = 8;
      } else if (numPlotRecs >= (TimeFrameModel.TWOHALFYEAR
            + TimeFrameModel.SIXMONTHS)) {
         monthsToSkip = 4;
      } else if (numPlotRecs >= (TimeFrameModel.TWOHALFYEAR)) {
         monthsToSkip = 2;
      }

      if (monthsToSkip == 0) {
         if ((numPlotRecs >= (TimeFrameModel.THREEMONTHS))
               && (numPlotRecs <= (TimeFrameModel.ONEYEAR)))
            pointsBetweenLabels = 10;
         else if (numPlotRecs > (TimeFrameModel.ONEWEEK * 3))
            pointsBetweenLabels = 2;

      }

      // phone override
      if (_isAndroid) {
         if (_orientation == Orientation.VERTICAL) {
            if (numPlotRecs > TimeFrameModel.SIXMONTHS) {
               pointsBetweenLabels = 40;
               monthsToSkip = 1;
            } else if (numPlotRecs > TimeFrameModel.THREEMONTHS) {
               pointsBetweenLabels = 20;
            }
         }
      }

      for (int i = plotRange.getBeginPlotRec(); i < plotRange
            .getEndPlotRec(); i++) {
         String labeldt = FormatUtils.formatDate(quotes[i].getDate());
         String mmdd = labeldt.substring(0, 2) + "/" + labeldt.substring(3, 5);
         String yy = labeldt.substring(8, 10);
         int day = Integer.parseInt(labeldt.substring(3, 5));
         int curMonth = Integer.parseInt(labeldt.substring(0, 2));

         if ((monthsToSkip == 0) && (numPlotRecs <= (TimeFrameModel.SIXMONTHS
               + TimeFrameModel.ONEWEEK))) {
            if ((pointCount == pointsBetweenLabels) && (day < 28)) { // BUG
                                                                     // clashing
                                                                     // label
                                                                     // issue
               pointCount = 0;
               labeltxt = mmdd;
               printLabel = true;
            } else {
               pointCount++;
            }
         }

         if (numPlotRecs >= (TimeFrameModel.ONEYEAR)) {
            printLabel = false;
         }

         // month has changed
         if (curMonth != curMonthSave) {
            curMonthSave = curMonth;
            if (firstTime) {
               firstTime = false;
            } else {
               printLabel = true;
               labeltxt = xlabel(yy, curMonth, timeFrameModel.isIntraday());

               pointCount = 0;
               if (monthsSkipped == monthsToSkip) {
                  monthsSkipped = 0;
               } else {
                  printLabel = false;
                  monthsSkipped++;
               }
            }
         }

         if (printLabel && !grid) {
            printLabel = false;
            if (jchartComposite.getChartTypeModel().isSinglePlot()) {
               _jchartRender.drawXLabel(ColorValue.XYLABEL, labeltxt, x);
               _jchartRender.drawXLabelTick(ColorValue.XYLABEL, x);
            } else {
               _jchartRender.drawaString(ColorValue.XYLABEL, labeltxt, x,
                     textBottom);
            }
         }
         if (printLabel && grid) {
            printLabel = false;
            _jchartRender.drawaDotLineY(x);
         }
         x += 2;
      }
   }

   private String xlabel(String yy, int mm, boolean isIntraday) {
      if (isIntraday) {
         return new Integer(mm).toString();
      }

      String s = "";

      switch (mm) {
      case 1:
         s = "Jan";

         break;

      case 2:
         s = "Feb";

         break;

      case 3:
         s = "Mar";

         break;

      case 4:
         s = "Apr";

         break;

      case 5:
         s = "May";

         break;

      case 6:
         s = "Jun";

         break;

      case 7:
         s = "Jul";

         break;

      case 8:
         s = "Aug";

         break;

      case 9:
         s = "Sep";

         break;

      case 10:
         s = "Oct";

         break;

      case 11:
         s = "Nov";

         break;

      case 12:
         s = "Dec";

         break;

      default:
         s = "XXX";
      }

      return s + yy;
   }

   public void updateYlabel(PlotDataModel plotDataModel, float yLabel) {
      if (yLabel == -1) {
         displayImage();
      } else {
         plotDataModel.setCoordinates(Kjchart.CT_PRICE, Kjchart.POS_PRICE,
               _jchartRender);

         Float f = new Float(yLabel);
         _jchartRender.drawYlabelRightInverse(yLabel, f.toString());
      }
   }

   private void plotQuoteLine(JchartComposite jchartComposite,
         ColorValue colorValue) {
      PlotDataModel plotDataModel = jchartComposite.getPlotDataModel();
      PlotRange plotRange = plotDataModel.getPlotRange();
      Quote[] quotes = jchartComposite.getQuoteDataModel().getQuotes();
      int xaxis = 2;
      float moveFrom;
      float moveTo;
      int startRec = plotRange.getBeginPlotRec();
      moveFrom = quotes[startRec].getClose();
      startRec++;

      for (int i = startRec; i < plotRange.getEndPlotRec(); i++) {
         moveTo = quotes[i].getClose();
         _jchartRender.drawaLine(colorValue, xaxis - 1f, moveFrom, xaxis + 1f,
               moveTo);
         moveFrom = moveTo;
         xaxis += Kjchart.XEXPAND;
      }
   }

   private void plotMountainPrice(JchartComposite jchartComposite) {
      PlotDataModel plotDataModel = jchartComposite.getPlotDataModel();
      Quote[] quotes = jchartComposite.getQuoteDataModel().getQuotes();
      PlotRange plotRange = plotDataModel.getPlotRange();
      int startRec = plotRange.getBeginPlotRec() + 1;
      int xaxis = 2;

      // int numPlotRecs = plotDataModel.getPlotRange().getNumPlotRecs();
      int numPlotRecs = plotRange.getEndPlotRec() - startRec + 1;
      float[] x = new float[numPlotRecs + 3];
      float[] y = new float[numPlotRecs + 3];
      int j = 0;

      y[j] = plotDataModel.getPlo();
      x[j] = 0;
      j++;
      y[j] = quotes[startRec].getClose();
      x[j] = 0;
      xaxis++;
      j++;

      for (int i = startRec; i < plotRange.getEndPlotRec(); i++) {
         y[j] = quotes[i].getClose();
         x[j] = xaxis;
         xaxis += Kjchart.XEXPAND;
         j += 1;
      }

      xaxis -= Kjchart.XEXPAND;
      y[j] = y[j - 1];
      x[j] = xaxis;
      j++;
      y[j] = plotDataModel.getPlo();
      x[j] = xaxis;
      _jchartRender.fillaPolygon(ColorValue.PRICE, x, y);
   }

   private void plotMountainVol(JchartComposite jchartComposite) {
      PlotDataModel plotDataModel = jchartComposite.getPlotDataModel();
      Quote[] quotes = jchartComposite.getQuoteDataModel().getQuotes();
      PlotRange plotRange = plotDataModel.getPlotRange();
      int xaxis = 1;
      int j = 0;
      int numPlotRecs = plotRange.getEndPlotRec() - plotRange.getBeginPlotRec()
            + 1;
      float[] x = new float[numPlotRecs + 1];
      float[] y = new float[numPlotRecs + 1];
      y[j] = 0;
      x[j] = xaxis;
      j++;

      for (int i = plotRange.getBeginPlotRec(); i < plotRange
            .getEndPlotRec(); i++) {
         x[j] = xaxis;
         y[j] = quotes[i].getVolume();
         xaxis += Kjchart.XEXPAND;
         j += 1;
      }

      xaxis -= Kjchart.XEXPAND;
      y[j] = 0;
      x[j] = xaxis;
      _jchartRender.fillaPolygon(ColorValue.VOL, x, y);
   }

   private void drawMALabels(JchartComposite jchartComposite,
         String priorLabel) {
      IndicatorModel indicatorModel = jchartComposite.getIndicatorModel();
      TimeFrameModel timeFrameModel = jchartComposite.getTimeFrameModel();
      ChartTypeModel chartTypeModel = jchartComposite.getChartTypeModel();

      IndicatorIntr priceMa1 = indicatorModel.getPriceMa(Kjchart.PRICEMA1);
      IndicatorIntr priceMa2 = indicatorModel.getPriceMa(Kjchart.PRICEMA2);
      IndicatorIntr priceMa3 = indicatorModel.getPriceMa(Kjchart.PRICEMA3);

      if ((priceMa1 == null) && (priceMa2 == null) && (priceMa3 == null)) {
         return;
      }

      String period = null;

      if (timeFrameModel.isIntraday()) {
         period = " Minutes  ";
      } else {
         period = " Days  ";
      }

      if (!_isAndroid) {
         String label = "  Moving Averages: ";
         _jchartRender.drawNextHorizLabel(ColorValue.TOPCAPTION, priorLabel,
               label, 0);
         priorLabel += label;

         if (priceMa1 != null) {
            int ma1 = chartTypeModel.getPriceMa(0);
            label = "--- " + ma1 + period;
            _jchartRender.drawNextHorizLabel(ColorValue.COLOR_MAGENTA,
                  priorLabel, label, 0);
            priorLabel += label;
         }

         if (priceMa2 != null) {
            int ma2 = jchartComposite.getChartTypeModel().getPriceMa(1);
            label = "--- " + ma2 + period;
            _jchartRender.drawNextHorizLabel(ColorValue.COLOR_CYAN, priorLabel,
                  label, 0);
            priorLabel += label;
         }

         if (priceMa3 != null) {
            int ma3 = jchartComposite.getChartTypeModel().getPriceMa(2);
            label = "--- " + ma3 + period;
            _jchartRender.drawNextHorizLabel(ColorValue.COLOR_RED, priorLabel,
                  label, 0);
         }
      }
   }

   public void reset() {
      _jchartRender.reset();
      _jchartRender.nullTrendLines();
   }

   /**
    * display a previously created Image
    */
   public void displayImage() {
      _jchartRender.displayImage();
   }

   public int getListno(PlotDataModel plotDataModel, int xMouseVal) {
      int listno = plotDataModel.getPlotRange().getBeginPlotRec()
            + (int) Math.floor(_jchartRender.inverseX(xMouseVal / 2));

      if (listno < plotDataModel.getPlotRange().getBeginPlotRec()) {
         listno = plotDataModel.getPlotRange().getBeginPlotRec();
      }

      if (listno >= plotDataModel.getPlotRange().getEndPlotRec()) {
         listno = plotDataModel.getPlotRange().getEndPlotRec() - 1;
      }

      return listno;
   }

   private Quote getLastQuote(JchartComposite jchartComposite) {
      Quote retval = null;
      Quote[] quotes = jchartComposite.getQuoteDataModel().getQuotes();
      retval = quotes[quotes.length - 1];
      return retval;
   }

}
