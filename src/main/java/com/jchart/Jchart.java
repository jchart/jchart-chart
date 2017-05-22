package com.jchart;

import java.util.List;

import com.jchart.io.TickerListener;
import com.jchart.io.TickerRequest;
import com.jchart.model.InitParmParser;
import com.jchart.model.JchartComposite;
import com.jchart.model.JchartModelFacade;
import com.jchart.view.client.FrmJchart;
import com.jchart.view.client.PnlJchart;

/**
 * Jchart - Financial Technical Analysis Software Author: Paul Russo Creation
 * Date: Jan 8, 2000 Copyright &copy 2000,2016
 */
public class Jchart implements TickerListener, JchartFrameDestroyer {

   private static PnlJchart _pnlJchartFloat;
   private FrmJchart _frmJchart;
   private JchartInitializer _jchartInitializer;
   protected JchartComposite _jchartComposite;

   // top level buttons
   // constructor
   public Jchart() {
      super();
   }

   // called first; called only if run as an application
   public static void main(String args[]) {
      Jchart jchart = new Jchart();
      jchart.init(args);
   }

   /**
    * if application then this is called by main() if applet then this runs
    * first
    */
   public void init(String args[]) {
      _jchartInitializer = new JchartInitializer();
      _jchartInitializer.init(args);
      _jchartComposite = _jchartInitializer.getJchartComposite();
      TickerRequest tickerRequest = new TickerRequest(_jchartComposite, this);
      String ticker = _jchartComposite.getJchartRequest().getTicker();
      _jchartComposite.getQuoteDataModel().setLiveQuote();
      tickerRequest.requestBlock(JchartModelFacade.getJchartProperty("startup.ticker"));
      initPanel();
   }

   private void initPanel() {
      InitParmParser initParmParser = _jchartInitializer.getInitParmParser();
      _pnlJchartFloat = new PnlJchart();
      _pnlJchartFloat.setIsFloat(true);
      _pnlJchartFloat.setJchartComposite(_jchartComposite);
      _pnlJchartFloat.init(initParmParser.getTickerList());
      _frmJchart = new FrmJchart(_pnlJchartFloat, _jchartComposite, this);
      _frmJchart.setTickerDir(initParmParser.getTickerDir());
      _frmJchart.addNotify();
      _frmJchart.setVisible(true);
   }

   public void destroyFrame() {
      // _frmJchart.dispose();
      _frmJchart = null;
      _pnlJchartFloat = null;
      _jchartInitializer.destroy();
      System.exit(0);
   }

   public void setTickerList(List<String> tickerList) {
      _pnlJchartFloat.setTickerList(tickerList);
   }

   public void setIsGetTicker(boolean b) {
      _frmJchart.setIsGetTicker(b);
   }

   public boolean isShowing() {
      return _pnlJchartFloat.isShowing();
   }

   public void updateYlabel(float f) {
      _pnlJchartFloat.updateYlabel(f);
   }

   public void requestComplete(boolean found) {
      if (_pnlJchartFloat == null)
         return;
      _pnlJchartFloat.requestComplete(found);
      _pnlJchartFloat.setFocus();
   }
}
