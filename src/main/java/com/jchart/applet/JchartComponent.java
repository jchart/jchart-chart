package com.jchart.applet;

import java.applet.Applet;
import java.awt.Dimension;
import java.util.List;

import com.jchart.JchartInitializer;
import com.jchart.io.TickerListener;
import com.jchart.model.InitParmParser;
import com.jchart.model.JchartComposite;
import com.jchart.view.client.PnlJchart;

public class JchartComponent implements TickerListener {

   private static PnlJchart _pnlJchartFloat;
   private static PnlJchart _pnlJchart;
   private Applet _applet;
   private boolean _isFloat;

   public JchartComponent(Applet applet) {
      _applet = applet;
   }

   public void init(JchartInitializer jchartInitializer) {
      JchartComposite jchartComposite = jchartInitializer.getJchartComposite();
      InitParmParser initParmParser = jchartInitializer.getInitParmParser();
      List<String> tickerList = initParmParser.getTickerList();
      _pnlJchartFloat = new PnlJchart();
      _pnlJchartFloat.setIsFloat(true);
      _pnlJchartFloat.setJchartComposite(jchartComposite);
      _pnlJchartFloat.init(tickerList);
      _pnlJchart = new PnlJchart();
      _pnlJchart.setIsFloat(false);
      _pnlJchart.setJchartComposite(jchartComposite);
      _pnlJchart.init(tickerList);

   }

   public PnlJchart getPnlJchart(boolean isFloat) {
      PnlJchart retval = null;
      boolean wasFloat = _isFloat;
      _isFloat = isFloat;
      if (_isFloat) {
         _pnlJchartFloat.setPanelFeatures(_pnlJchart);
         retval = _pnlJchartFloat;
      } else {
         if (wasFloat) {
            _pnlJchart.setPanelFeatures(_pnlJchartFloat);
         }
         retval = _pnlJchart;
      }
      return retval;
   }

   public boolean isShowing() {
      boolean retval = false;
      if (_isFloat) {
         retval = _pnlJchartFloat.isShowing();
      } else {
         retval = _pnlJchart.isShowing();
      }
      return retval;
   }

   public void setIsGetTicker(boolean b) {
      if (_isFloat) {
         _pnlJchartFloat.setIsGetTicker(b);
      } else {
         _pnlJchart.setIsGetTicker(b);
      }
   }

   public void plotMain() {
      if (!isShowing())
         return;
      if (_isFloat) {
         _pnlJchartFloat.plotMain();
      } else {
         _pnlJchart.plotMain();
      }
   }

   public void plotZoom(int beginPlotRec, int endPlotRec) {
      if (!isShowing())
         return;
      if (_isFloat) {
         _pnlJchartFloat.plotZoom(beginPlotRec, endPlotRec);
      } else {
         _pnlJchart.plotZoom(beginPlotRec, endPlotRec);
      }
   }

   public void tickerRequest(String ticker, boolean isFloat) {
      if (isFloat) {
         _pnlJchartFloat.tickerRequest(ticker);
      } else {
         _pnlJchart.tickerRequest(ticker);
      }
   }

   // public void tickerRecieved(boolean tickerFound) {
   // if (_isFloat) {
   // _pnlJchartFloat.tickerRecieved(tickerFound);
   // _pnlJchartFloat.setFocus();
   // } else {
   // _pnlJchart.tickerRecieved(tickerFound);
   // _pnlJchart.setFocus();
   // }
   // }

   /*
    * TickerRequest tickerRequest = new TickerRequest(jchartComposite,this);
    * String ticker = jchartComposite.getJchartRequest().getTicker();
    * tickerRequest.requestBlock(ticker);
    * 
    */
   public void dock() {
      _pnlJchart.setPanelFeatures(_pnlJchartFloat);
   }

   public void floatFrame() {
      _pnlJchartFloat.setPanelFeatures(_pnlJchart);
   }

   public void updateYlabel(float f) {
      if (_isFloat) {
         _pnlJchartFloat.updateYlabel(f);
      } else {
         _pnlJchart.updateYlabel(f);
      }
   }

   public void setSize(Dimension d) {
      _applet.setSize(d);
   }

   public void requestComplete(boolean found) {
      if (_pnlJchartFloat == null)
         return;
      if (_isFloat) {
         _pnlJchartFloat.requestComplete(found);
         _pnlJchartFloat.setFocus();
      } else {
         _pnlJchart.requestComplete(found);
         _pnlJchart.setFocus();
      }
   }

   public PnlJchart getPnlJchartFloat() {
      return _pnlJchartFloat;
   }

   public PnlJchart getPnlJchart() {
      return _pnlJchart;
   }

}
