package com.jchart.model;

import java.util.List;

import com.jchart.model.indicator.IndicatorIntr;
import com.jchart.model.indicator.pnf.PnFCalc;
import com.jchart.model.indicator.pnf.Pnf;
import com.jchart.model.indicator.pnf.PnfItem;
import com.jchart.view.common.JchartRenderer;

public class PlotDataModel {
   private float _ihi;
   private float _ilo;
   private float _vhi;
   private float _phi;
   private float _plo;
   private int _intradayMinInc;
   private YlabelCalc _ylabelCalc = new YlabelCalc();
   private PlotRange _plotRange = new PlotRange();
   private QuoteDataModel _quoteDataModel;
   private Pnf _pnf;
   private boolean _isComparison;
   private IndicatorIntr _rangeValHi;
   private IndicatorIntr _rangeValLo;
   private ChartOption _chartOption;

   public void init() {
      if (_isComparison) {
         String rsTicker = _quoteDataModel.getCompareTicker();
         if (rsTicker == null || rsTicker.trim().equals(""))
            return;
         hiloRangeRs();
      } else {
         hiloRangePrice();
      }
      calcYlabels();
   }

   public float getVhi() {
      return _vhi;
   }

   public YlabelCalc getYlabelCalc() {
      return _ylabelCalc;
   }

   public void setVhi(float f) {
      _vhi = f;
   }

   public void setYlabelCalc(YlabelCalc calc) {
      _ylabelCalc = calc;
   }

   public PlotRange getPlotRange() {
      return _plotRange;
   }

   private void calcYlabels() {
      _ylabelCalc.calcYlabels((float) _phi, (float) _plo);
   }

   /**
    * @return the number of boxes from low box to current box
    */
   public float calcFirstY1PnF() {
      List<PnfItem> pnfItems = _pnf.getDisplayPnfItems();
      float ival = _pnf.getPnfLo() - PnFCalc.getBoxSize(_pnf.getPnfLo());
      float boxsize;
      int boxcount = 0;
      PnfItem pnfItem = pnfItems.get(0);
      if (pnfItem.isXBox()) {
         while (ival < pnfItem.getPrice()) {
            boxsize = PnFCalc.getBoxSize(ival);
            ival += boxsize;
            boxcount++;
         }

      } else {
         int numBoxes = Math.abs(pnfItem.getNumBoxes());
         float price = PnFCalc.getXBoxVal(numBoxes, pnfItem.getPrice());
         while (ival < price) {
            boxsize = PnFCalc.getBoxSize(ival);
            ival += boxsize;
            boxcount++;
         }
      }
      return boxcount;
   }

   private void hiloRangeRs() {
      _phi = Float.MIN_VALUE;
      _plo = Float.MAX_VALUE;
      hiloRangeRs(2);
      hiloRangeRs(3);
   }

   private void hiloRangeRs(int rsNum) {
      float hi = 0f;
      float lo = 0f;

      for (int i = _plotRange.getBeginPlotRec(); i < _plotRange
            .getEndPlotRec(); i++) {
         Quote rsQuote = _quoteDataModel.getCompareQuote(rsNum, i);
         hi = rsQuote.getClose();
         lo = rsQuote.getClose();

         if (hi > _phi) {
            _phi = hi;
         }

         if (lo < _plo) {
            _plo = lo;
         }
      }
   }

   private void hiloRangePrice() {
      boolean useClose = false;

      if ((_chartOption == ChartOption.LINECHART)
            || (_chartOption == ChartOption.MOUNTAINCHART)) {
         useClose = true;
      }

      float mahi = Float.MIN_VALUE;
      float malo = Float.MAX_VALUE;

      _phi = Float.MIN_VALUE;
      _plo = Float.MAX_VALUE;
      _vhi = Float.MIN_VALUE;

      float hi = 0f;
      float lo = 0f;

      for (int i = _plotRange.getBeginPlotRec(); i < _plotRange
            .getEndPlotRec(); i++) {
         Quote quote = _quoteDataModel.getQuote(i);
         if (useClose) {
            hi = quote.getClose();
            lo = quote.getClose();
         } else {
            hi = quote.getHi();
            lo = quote.getLow();
         }

         if (hi > _phi) {
            _phi = hi;
         }

         if (lo < _plo) {
            _plo = lo;
         }

         if (quote.getVolume() > _vhi) {
            _vhi = quote.getVolume();
         }

         if (_rangeValHi != null) {
            if (_rangeValHi.getVal(i) > mahi) {
               mahi = _rangeValHi.getVal(i);
            }
         }

         if (_rangeValLo != null) {
            if ((_rangeValLo.getVal(i) < malo)
                  && (_rangeValLo.getVal(i) != Kjchart.IFILLER)) {
               malo = _rangeValLo.getVal(i);
            }
         }
      }

      if (mahi > _phi) {
         _phi = mahi;
      }

      if (malo < _plo) {
         _plo = malo;
      }
   }

   public void hiloRangeIndicator(float[] val) {
      hiloRangeIndicator(val, null);
   }

   public void hiloRangeIndicator(float[] val, float[] val2) {
      hiloRangeIndicator(val, val2, null);

   }

   public void hiloRangeIndicator(float[] vals1, float[] vals2, float[] vals3) {
      float val2Hi = Float.MIN_VALUE;
      float val2Lo = Float.MAX_VALUE;
      float val3Hi = Float.MIN_VALUE;
      float val3Lo = Float.MAX_VALUE;
      _ihi = Float.MIN_VALUE;
      _ilo = Float.MAX_VALUE;

      int j = _plotRange.getBeginPlotRec();

      while (vals1[j] == Kjchart.IFILLER) {
         j++;
      }

      for (int i = j; i < _plotRange.getEndPlotRec(); i++) {
         if (vals1[i] > _ihi) {
            _ihi = vals1[i];
         }

         if (vals1[i] < _ilo) {
            _ilo = vals1[i];
         }
         if (vals2 != null && vals2[i] != Kjchart.IFILLER) {
            if (vals2[i] > val2Hi) {
               val2Hi = vals2[i];
            }

            if (vals2[i] < val2Lo) {
               val2Lo = vals2[i];
            }
         }
         if (vals3 != null && vals3[i] != Kjchart.IFILLER) {
            if (vals3[i] > val3Hi) {
               val3Hi = vals3[i];
            }

            if (vals3[i] < val3Lo) {
               val3Lo = vals3[i];
            }
         }

      }
      if (vals2 != null) {

         if (val2Hi > _ihi) {
            _ihi = val2Hi;
         }

         if (val2Lo < _ilo) {
            _ilo = val2Lo;
         }
      }

      if (vals3 != null) {

         if (val3Hi > _ihi) {
            _ihi = val3Hi;
         }

         if (val3Lo < _ilo) {
            _ilo = val3Lo;
         }
      }
   }

   public Frect setCoordinatesPnf(JchartRenderer jchartRender) {
      Frect retval = new Frect();
      retval.right = _pnf.getNumXBoxes();
      retval.top = 0;
      retval.bottom = (int) _pnf.getBottom();
      jchartRender.setCoordinates(Kjchart.POS_PNF, retval);
      return retval;
   }

   public void setIntradayCount(boolean isSmallXWidth) {
      setIntradayCountFull(isSmallXWidth);
   }

   public void setIntradayCountFull(boolean isSmallXWidth) {
      _intradayMinInc = 30;
      if (isSmallXWidth) {
         _intradayMinInc = 60; // hour
      } else if (_plotRange.getNumPlotRecs() < (TimeFrameModel.ONEMONTH + 5)) {
         _intradayMinInc = 1;
      } else if (_plotRange.getNumPlotRecs() < (TimeFrameModel.ONEMONTH * 3)) {
         _intradayMinInc = 10;
      } else if (_plotRange.getNumPlotRecs() < TimeFrameModel.SIXMONTHS) {
         _intradayMinInc = 15;
      } else if (_plotRange.getNumPlotRecs() < TimeFrameModel.ONEYEAR) {
         _intradayMinInc = 10;
      }
   }

   public void setCoordinates(int chartType, int chartPos,
         JchartRenderer jchartRender) {
      // set _graphRect
      Frect graphRect = new Frect();

      switch (chartType) {
      case Kjchart.CT_PRICE:
         graphRect.top = (float) _phi;
         graphRect.bottom = (float) _plo;

         break;

      case Kjchart.CT_VOLUME:
         graphRect.top = (float) _vhi;
         graphRect.bottom = 0;

         break;

      case Kjchart.CT_INDICATOR:
         graphRect.top = (float) _ihi;
         graphRect.bottom = (float) _ilo;

         break;

      case Kjchart.CT_LABEL:
         graphRect.top = 0;
         graphRect.bottom = 100;

         break;
      }

      graphRect.left = 0;

      int rightPad = 0; // padding between last price and border

      if (_plotRange.getNumPlotRecs() > TimeFrameModel.ONEMONTH) {
         rightPad++;
      }

      graphRect.right = (_plotRange.getNumPlotRecs() + rightPad)
            * Kjchart.XEXPAND;

      jchartRender.setCoordinates(chartPos, graphRect);
   }

   public String padDate(String dt) {
      StringBuffer sb = new StringBuffer(dt);

      while (sb.length() < 8) {
         sb.insert(0, '0');
      }

      return sb.toString();
   }

   public float getPhi() {
      return _phi;
   }

   public void setPnf(Pnf pnf) {
      _pnf = pnf;
   }

   public void setIhi(float ihi) {
      _ihi = ihi;
   }

   public void setIlo(float ilo) {
      _ilo = ilo;
   }

   public float getIhi() {
      return _ihi;
   }

   public float getIlo() {
      return _ilo;
   }

   public Pnf getPnf() {
      return _pnf;
   }

   public float getPlo() {
      return _plo;
   }

   public int getIntradayMinInc() {
      return _intradayMinInc;
   }

   public void setQuoteDataModel(QuoteDataModel quoteDataModel) {
      _quoteDataModel = quoteDataModel;
   }

   public void setComparison(boolean isComparison) {
      _isComparison = isComparison;

   }

   public void setChartOption(ChartOption chartOption) {
      _chartOption = chartOption;

   }

   public void setRange(IndicatorIntr rangeValHi, IndicatorIntr rangeValLo) {
      _rangeValHi = rangeValHi;
      _rangeValLo = rangeValLo;

   }

}
