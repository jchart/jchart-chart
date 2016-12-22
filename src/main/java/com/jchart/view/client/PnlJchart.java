package com.jchart.view.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Panel;

import com.jchart.io.TickerListener;
import com.jchart.io.TickerRequest;
import com.jchart.model.ChartTypeModel;
import com.jchart.model.JchartComposite;
import com.jchart.model.JchartModelFacade;
import com.jchart.model.QuoteString;
import com.jchart.model.TimeFrameModel;
import com.jchart.model.color.ColorScheme;
import com.jchart.model.color.ColorValue;
import com.jchart.view.client.factory.GuiFactory;
import com.jchart.view.client.factory.PnlTopIntr;
import com.jchart.view.common.BorderPanel;
import com.jchart.view.common.JchartCanvas;
import com.jchart.view.common.PlotJchart;

public class PnlJchart extends Panel
      implements com.jchart.view.common.PnlJchartIntr, TickerListener {
   private static final long serialVersionUID = 1L;
   private boolean _isInitialized;
   // cursors
   private static final Cursor CURSOR_WAIT = new Cursor(Cursor.WAIT_CURSOR);
   private static final Cursor CURSOR_DEFAULT = new Cursor(
         Cursor.DEFAULT_CURSOR);
   private static final Insets insets = new Insets(0, 0, 0, 0);
   private Font fontApplication = new Font("SansSerif", Font.PLAIN, 10);
   private Font fontApplet = new Font("SansSerif", Font.PLAIN, 10);
   private PnlTopIntr _pnlTop;
   private Panel _topPanel;
   private Panel _botPanel;
   private TimeFrameButtonView _timeFrameButtonView;
   private ChartTypeView _chartTypeView;
   private JchartCanvas _jchartCanvas;

   // flags
   protected boolean _isBusy = false;
   private boolean _isGetTicker = false;
   // protected JchartIntr _jchart;
   private JchartComposite _jchartComposite;
   private ChartTypeModel _chartTypeModel;
   private TimeFrameModel _timeFrameModel;
   private ColorScheme _colorScheme;
   protected PlotJchart _plotJchart = new PlotJchart();
   private boolean _isFloat;
   private boolean _isApplet;

   public void setIsAppelt(boolean isApplet) {
      _isApplet = isApplet;
   }

   public void setIsFloat(boolean isFloat) {
      _isFloat = isFloat;
   }

   public void setJchartComposite(JchartComposite jchartComposite) {
      _jchartComposite = jchartComposite;
      _chartTypeModel = _jchartComposite.getChartTypeModel();
      _timeFrameModel = _jchartComposite.getTimeFrameModel();
      _colorScheme = _jchartComposite.getColorScheme();
   }

   public void init(java.util.List<String> tickerList) {
      _jchartCanvas = new JchartCanvas(_plotJchart, _jchartComposite);
      _plotJchart.setRender(_jchartCanvas);
      MouseAction mouseAction = new MouseAction(_jchartCanvas, _jchartComposite,
            this);
      _jchartCanvas.setMouseAction(mouseAction);

      setLayout(new BorderLayout(0, 0));

      if (_isApplet) {
         setFont(fontApplet);
      } else {
         setFont(fontApplication);
      }
      _chartTypeView = new ChartTypeView(this, _chartTypeModel);

      // top panel
      _pnlTop = GuiFactory.getInstance().getPnlTop(this, _chartTypeView,
            new ExchangeChoice(),
            (Color) _colorScheme.getColor(ColorValue.BORDER),
            (Color) _colorScheme.getColor(ColorValue.XYLABEL));
      setTickerList(tickerList);
      _topPanel = _pnlTop.getPanel();

      // bottom panel
      _botPanel = new Panel();
      _timeFrameButtonView = new TimeFrameButtonView(this,
            _chartTypeView.getChartTypeButtonL2(), _timeFrameModel);

      _botPanel.setLayout(new GridLayout(1, 0));

      // add panels
      add(BorderLayout.CENTER, getPlotPanel(_isFloat));
      setFullVersion();
   }

   public Insets getInsets() {
      return insets;
   }

   private void setFullVersion() {
      add(BorderLayout.NORTH, _topPanel);
      _botPanel.removeAll();
      _botPanel.add(_timeFrameButtonView.getPanel());
      add(BorderLayout.SOUTH, _botPanel);
      validate();
   }

   private Panel getPlotPanel(boolean isFloat) {
      Color backColor = (Color) _colorScheme.getColor(ColorValue.BORDER);
      Panel p = new BorderPanel(this, isFloat, backColor);
      p.add(_jchartCanvas);

      return p;
   }

   public void setFocus() {
      _pnlTop.setFocus();
   }

   /**
    * sets panel parm features with this panels features
    */
   public void setPanelFeatures(PnlJchart p) {
      p._chartTypeModel
            .setPriceOption(_chartTypeModel.getPriceOption().getName());
      p._chartTypeModel
            .setIndicatorOption(_chartTypeModel.getIndicatorOption().getName());

      p._chartTypeView.setChartOption(_chartTypeView.getChartOption());
   }

   /**
    * set the price label to the value of the mouse position call by
    * _plotJchart.mouseMoved
    */
   public void setCurPosPriceLabel(QuoteString quote, String cursorPos) {
      _pnlTop.setLabelStatus(quote, cursorPos);
   }

   // called by Jchart
   public void requestComplete(boolean found) {
      if (found) {
         _chartTypeModel.chartTypeChanged(true);
         _plotJchart.reset();
         System.gc();

         if (_chartTypeModel.isPriceMaOn()) {
            _chartTypeModel.setIsPriceMaDirty(true);
         }
         if (_chartTypeModel.isBandsOn()) {
            _chartTypeModel.setIsPriceMaDirty(true);
         }
      }
      readyState();
      calcPlotRange();
      plotMain();
      setIsGetTicker(false);
   }

   public void plotZoom(int beginPlotRec, int endPlotRec) {
      _plotJchart.plotZoom(_jchartComposite, beginPlotRec, endPlotRec);
   }

   public synchronized void plotMain() {
      if (_isBusy) {
         return;
      }
      System.gc();
      if (!_isInitialized) {
         _isInitialized = true;
         JchartModelFacade.calcPlotRange(_jchartComposite);
      }
      _plotJchart.plotMain(_jchartComposite);
   }

   public void nullTrendLines() {
      _jchartCanvas.nullTrendLines();
   }

   public void waitState() {
      if (_isGetTicker) {
         return;
      }
      _isBusy = true;
      setCursor(CURSOR_WAIT);
   }

   public void readyState() {
      _isBusy = false;
      setCursor(CURSOR_DEFAULT);
      _pnlTop.setTicker("");
      _pnlTop.setFocus();
   }

   public void updateYlabel(float f) {
      _plotJchart.updateYlabel(_jchartComposite.getPlotDataModel(), f);
   }

   // events section
   public void chartTypeChanged() {
      if (_isBusy) {
         return;
      }
      if (_chartTypeModel.isPrint()) {
         // _jchart.visitPrintUrl();
      } else if (_chartTypeModel.isColor()) {
         DlgColor dlgColors = new DlgColor(new Frame(),
               _jchartComposite.getColorScheme(), this);
         dlgColors.setVisible(true);

      } else if (_chartTypeModel.isComparison()) {
         DlgCompare dlgCompare = new DlgCompare(new Frame(), _jchartComposite);
         _isBusy = true;
         dlgCompare.setVisible(true);
         _isBusy = false;
      } else if (_chartTypeModel.isRs()) {
         DlgRs dlgRs = new DlgRs(new Frame(), _jchartComposite);
         _isBusy = true;
         dlgRs.setVisible(true);
         _isBusy = false;

      }

      if ((_chartTypeModel.isBack()) || (_chartTypeModel.isForward())) {
         JchartModelFacade.calcPlotRange(_jchartComposite);
         _chartTypeModel.isBack(false);
         _chartTypeModel.isForward(false);
      }
      _chartTypeModel.chartTypeChanged(true);
      plotMain();
   }

   public void sblChanged(String sbl) {
      if (_isBusy) {
         return;
      }
      waitState();
      tickerRequest(sbl);
   }

   private void tickerRequest() {
      tickerRequest(_jchartComposite.getQuoteDataModel().getTicker());
   }

   public void tickerRequest(String ticker) {
      waitState();

      if (_isGetTicker) {
         return;
      }
      _isGetTicker = true;

      if (ticker == null) {
         ticker = _jchartComposite.getQuoteDataModel().getTicker();
      }

      // if (ticker.startsWith("^")) {
      // ticker = "%5E" + ticker.substring(1);
      // }

      _jchartComposite.getQuoteDataModel().setLiveQuote();
      TickerRequest tickerRequest = new TickerRequest(_jchartComposite, this);
      tickerRequest.request(ticker.toLowerCase());
   }

   public boolean isBusy() {
      return _isBusy;
   }

   public void setTickerList(java.util.List<String> tickerList) {
      _pnlTop.setTickerList(tickerList);
   }

   public boolean getIsGetTicker() {
      return _isGetTicker;
   }

   public void setIsGetTicker(boolean isGetTicker) {
      if (!isGetTicker) {
         readyState();
      }
      _isGetTicker = isGetTicker;
   }

   // called by TimeFrameButtonView
   public void plotEod(boolean wasIntraday) {
      nullTrendLines();

      if (wasIntraday) {
         _chartTypeModel.reset();
         tickerRequest();
      } else {
         plotMain();
         readyState();
      }
   }

   // called by PnlTimeFrame
   public void plotIntraday() {
      nullTrendLines();
      tickerRequest();
   }

   /**
    * outer panel calls this method to call innermost panels paint which will
    * redisplay a previously created Image
    */
   public void paintIt(Graphics g) {
      _jchartCanvas.paint(g);
      invalidate();
      repaint();
      plotMain();
   }

   public boolean isApplet() {
      return _isApplet;
   }

   public int getListno(int xval) {
      return _plotJchart.getListno(_jchartComposite.getPlotDataModel(), xval);
   }

   public void calcPlotRange() {
      JchartModelFacade.calcPlotRange(_jchartComposite);
   }

   /**
    * @return
    */
   public TimeFrameButtonView getTimeFrameButtonView() {
      return _timeFrameButtonView;
   }
}
