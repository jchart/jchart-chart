package com.jchart.view.common;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import com.jchart.model.ChartTypeModel;
import com.jchart.model.Fpoint;
import com.jchart.model.Frect;
import com.jchart.model.Irect;
import com.jchart.model.JchartComposite;
import com.jchart.model.Kjchart;
import com.jchart.model.PlotDataModel;
import com.jchart.model.Trendline;
import com.jchart.model.color.ColorScheme;
import com.jchart.model.color.ColorValue;
import com.jchart.view.client.MouseAction;

public class JchartCanvas extends Component
      implements MouseListener, MouseMotionListener, JchartRenderer {

   private static final long serialVersionUID = 6774757586859430757L;
   private static Trendline[] _trendlines = new Trendline[Kjchart.MAXTRLINE];
   private Graphport _gp;

   /** off screen Graphic context used in double buffering */
   private Graphics _imageBufG;
   private Image _imageBuf = null;

   // canvas Graphic context
   private Graphics _canvasG;

   // trendline support
   private static int _curTrLine;

   private Dimension _canvasSize;

   private int _yLabelRightX;
   private Font _labelFont;
   private FontMetrics _labelFontmetrics;

   private int _rightLabelWidth;
   private int _xLabelPos;
   private int _leftLabelWidth = 0;
   private int _labelHeight;

   // plot sections
   private int _topRectTop;
   private int _topRectHeight;
   private int _botRectTop;
   private int _botRectHeight;
   private int _rectWidth;

   private Graphics _ylabelBufG;
   private Image _ylabelBuf;

   private MouseAction _mouseAction;
   private Point _vertPointA = new Point(0, 0);
   private Point _vertPointB = new Point(0, 0);
   private JchartComposite _jchartComposite;
   private ChartTypeModel _chartTypeModel;
   private ColorScheme _colorScheme;

   // constructor
   public JchartCanvas(PlotJchart plotJchart, JchartComposite jchartComposite) {
      _jchartComposite = jchartComposite;
      _chartTypeModel = _jchartComposite.getChartTypeModel();
      _colorScheme = _jchartComposite.getColorScheme();
      _gp = new Graphport();
      addMouseMotionListener(this);
      addMouseListener(this);
   }

   /**
    * set the color scheme
    */
   public void setColor(ColorValue colorValue) {
      Color color = (Color) _colorScheme.getColor(colorValue);
      // background
      _imageBufG.setColor(color);
      _imageBufG.fillRect(0, 0, _canvasSize.width, _canvasSize.height);
   }

   /**
    * creates on and off screen Graphics contexts
    * 
    * @param dimension
    *           d - initial size of plot area
    */
   public void doCreateImage(int width, int height, int labelFontSize,
         String labelStrSize) {
      doCreateImage(null, width, height, labelFontSize, labelStrSize);
   }

   /**
    * creates on and off screen Graphics contexts
    * 
    * @param Image
    *           from another Component
    * @param Dimension
    *           initial size of plot area
    */
   public void doCreateImage(Image image, int width, int height,
         int labelFontSize, String labelStrSize) {
      finalizeGraphics();
      setCanvas(width, height);
      if (image != null) {
         // get Graphics context from image from another Component
         _imageBuf = image;
         _imageBufG = image.getGraphics();
      } else {
         // initialize this Components off screen Graphics contexts
         if (width == 0 || height == 0) {
            return;
         }
         _imageBuf = createImage(width, height);
         if (_imageBuf != null)
            _imageBufG = _imageBuf.getGraphics();
      }
      // set label dimensions
      _labelFont = new Font("Arial", Font.PLAIN, labelFontSize);// SansSerif
      _labelFontmetrics = _imageBufG.getFontMetrics(_labelFont);
      _labelHeight = _labelFontmetrics.getHeight() + 4;

      _rightLabelWidth = _labelFontmetrics.stringWidth(labelStrSize) + 2;
      _ylabelBuf = createImage(_rightLabelWidth, _labelHeight);
      // null if run by jchartServer - gif charts
      if (_ylabelBuf != null) {
         _ylabelBufG = _ylabelBuf.getGraphics();
         _ylabelBufG.setFont(_labelFont);
      }
   }

   private void setCanvas(int width, int height) {
      Dimension d = new Dimension(width, height);
      if (d != null) {
         setSize(d);
      }
      _canvasSize = getSize();
      _canvasG = getGraphics();
   }

   /**
    * release resources
    */
   private void finalizeGraphics() {
      if (_canvasG != null) {
         _canvasG.finalize();
      }
      if (_imageBuf != null) {
         _imageBuf.flush();
      }
      if (_imageBufG != null) {
         _imageBufG.finalize();
      }
      if (_ylabelBuf != null) {
         _ylabelBuf.flush();
      }
      if (_ylabelBufG != null) {
         _ylabelBufG.finalize();
      }
   }

   /**
    * @return Image used in creation of gif image
    */
   public Image getImage() {
      return _imageBuf;
   }

   public void setCoordinates(int chartPos, Frect graphRect) {
      Dimension d = getSize();
      int h = d.height;
      int w = d.width;

      Irect viewRect = new Irect();
      viewRect.left = _leftLabelWidth;
      viewRect.right = w - _rightLabelWidth;
      // price
      int top1 = _labelHeight - 3;
      int bot1 = Math.round(h * 0.60f);
      if (_chartTypeModel.isPriceMountian())
         bot1 -= Math.round(_labelHeight / 2);
      else
         bot1 -= (_labelHeight);

      // indicator
      int top2;
      if (_chartTypeModel.isGif())
         top2 = bot1 + _labelHeight * 2;// + Math.round(_labelHeight/2);
      else
         top2 = bot1 + (_labelHeight * 2) + 2;

      if (_chartTypeModel.isPriceMountian())
         top2 -= Math.round(_labelHeight / 2);

      int bot2 = h - _labelHeight;

      // bottom labels
      int bot3 = h - Math.round(_labelHeight / 4);

      int top5 = top1 + 2;// + 20;// + Math.round(_labelHeight/2) -2;
      int t = (bot2 - Math.round(_labelHeight / 2)) + 2;
      int bot5 = t;
      _xLabelPos = bot5 + Math.round(_labelHeight / 2) + 2;
      switch (chartPos) {
      case Kjchart.POS_PRICE:
         viewRect.top = top1;
         viewRect.bottom = bot1;
         break;
      case Kjchart.POS_INDICATOR:
         viewRect.top = top2;
         viewRect.bottom = bot2;
         break;
      case Kjchart.POS_BOTTOM_LABEL:
         viewRect.top = h;
         viewRect.bottom = bot3;
         break;
      case Kjchart.POS_PNF:
         viewRect.top = h;
         viewRect.left = 0;
         viewRect.right = w;
         viewRect.bottom = 0;
         break;
      case Kjchart.POS_SINGLE_PLOT:
         viewRect.top = top5;
         viewRect.bottom = bot5;
         break;
      }
      _gp.setPorts(viewRect, graphRect);
   }

   public void fillPlotSection(boolean singlePlot) {
      Dimension d = getSize();
      int h = d.height;
      int w = d.width;

      setColor(ColorValue.TOPBACK);
      // border
      _imageBufG.setColor((Color) _colorScheme.getColor(ColorValue.BORDER));
      _imageBufG.fillRect(0, 0, w, h);

      // define rectangle sections
      _rectWidth = w - (_rightLabelWidth + _leftLabelWidth);

      // top section
      if (singlePlot) {
         _topRectHeight = h - (_labelHeight + Math.round(_labelHeight / 2));
         _topRectTop = Math.round(_labelHeight / 2) - 5;
      } else {
         _topRectHeight = Math.round(h * 0.60f) - Math.round(_labelHeight / 2);
      }
      _imageBufG.setColor((Color) _colorScheme.getColor(ColorValue.TOPBACK));
      _imageBufG.fillRect(_leftLabelWidth, _topRectTop, _rectWidth,
            _topRectHeight);

      if (!singlePlot) {
         // bottom indicator section
         _botRectTop = Math.round(h * 0.60f) + Math.round(_labelHeight / 2);
         _botRectHeight = Math.round(h * 0.40f) - (_labelHeight * 2)
               + Math.round(_labelHeight / 2) + 1;
         _imageBufG.setColor((Color) _colorScheme.getColor(ColorValue.BOTBACK));
         _imageBufG.fillRect(_leftLabelWidth, _botRectTop, _rectWidth,
               _botRectHeight);
      }
      _yLabelRightX = _rectWidth + _leftLabelWidth + 4;
   }

   /**
    * fillPlotSection must be called first
    * 
    * @param singlePlot
    */
   public void drawPlotSectionBorder(boolean singlePlot, boolean isGif) {
      Color c = (Color) _colorScheme.getColor(ColorValue.GIFBORDER);
      if ((isGif) && (c == null))
         return;
      if (c == null)
         c = (Color) _colorScheme.getColor(ColorValue.XYLABEL);
      _imageBufG.setColor(c);
      _imageBufG.drawRect(_leftLabelWidth, _topRectTop, _rectWidth,
            _topRectHeight);
      if (!singlePlot) {
         _imageBufG.drawRect(_leftLabelWidth, _botRectTop, _rectWidth,
               _botRectHeight);

      }
   }

   public void setImageColor(ColorValue colorValue) {
      _imageBufG.setColor((Color) _colorScheme.getColor(colorValue));
   }

   // ************ Double Buffer Drawing Output Section
   // ********************************
   public void drawYlabelRight(float y, String label) {
      _imageBufG.setColor((Color) _colorScheme.getColor(ColorValue.XYLABEL));
      _imageBufG.setFont(_labelFont);
      int labelSize = 3;
      int yTransform = _gp.transformY(y);
      // y -= _labelHeight/2;
      _imageBufG.drawString(label, _yLabelRightX, yTransform + labelSize);
   }

   public void drawaLabel(ColorValue foreground, ColorValue background,
         String label, float y) {
      drawaLabel(foreground, background, label, 0, y);
   }

   public void drawaLabel(ColorValue foreground, ColorValue background,
         String label, int x, float y) {
      _imageBufG.setColor((Color) _colorScheme.getColor(foreground));
      _imageBufG.drawString(" " + label, x + +_leftLabelWidth,
            _gp.transformY(y));
   }

   public void drawYlabelRightTop(String s, int plotSection) {
      _imageBufG.setColor((Color) _colorScheme.getColor(ColorValue.XYLABEL));
      _imageBufG.setFont(_labelFont);
      Dimension d = getSize();
      int h = d.height;
      int top = 0;
      if (plotSection == 2) {
         top = Math.round(h * 0.60f) + Math.round(_labelHeight / 2) - 3;
      } else {
         _imageBufG.setFont(_labelFont);
         top = Math.round(_labelHeight / 2);
      }

      int xStartPos = _rightLabelWidth - 3;// +
                                           // _labelFontmetrics.stringWidth(s);
      int w = d.width - xStartPos;
      _imageBufG.drawString(s, w, top);
   }

   public void drawMidRightLabel(String label, boolean singlePlot) {
      int labelWidth;
      if (singlePlot) {
         labelWidth = _labelFontmetrics.stringWidth(label) + _rightLabelWidth
               - 5;
      } else {
         labelWidth = _labelFontmetrics.stringWidth(label) + _rightLabelWidth;
      }
      int xStartPos = getSize().width - labelWidth;

      drawMidLabel(label, singlePlot, xStartPos);
   }

   public void drawMidLeftLabel(String label, boolean singlePlot) {
      drawMidLabel(label, singlePlot, 0);
   }

   private void drawMidLabel(String label, boolean singlePlot, int xStartPos) {
      Dimension d = getSize();
      int h = d.height;
      _imageBufG.setColor((Color) _colorScheme.getColor(ColorValue.BOTCAPTION));
      int botRectTop;
      if (singlePlot) {
         botRectTop = h - 3;
      } else {
         botRectTop = Math.round(h * 0.60f) + Math.round(_labelHeight / 2) - 3;
      }
      _imageBufG.drawString(label, xStartPos, botRectTop);
   }

   public void drawNextHorizLabel(ColorValue colorValue, String priorLabel,
         String label, float y) {
      int priorLabelWidth = _labelFontmetrics.stringWidth(priorLabel);
      _imageBufG.setColor((Color) _colorScheme.getColor(colorValue));
      _imageBufG.drawString("  " + label, priorLabelWidth, _gp.transformY(y));
   }

   /**
    * draw a line to the off screen Graphics buffer
    */
   public void drawaLine(ColorValue colorValue, float x1, float y1, float x2,
         float y2) {
      _imageBufG.setColor((Color) _colorScheme.getColor(colorValue));
      _imageBufG.drawLine(_gp.transformX(x1), _gp.transformY(y1),
            _gp.transformX(x2), _gp.transformY(y2));
   }

   /**
    * draw a rect to the off screen Graphics buffer
    */
   public void drawaRect(ColorValue colorValue, float x, float y1, float w,
         float y2) {
      _imageBufG.setColor((Color) _colorScheme.getColor(colorValue));
      int y1Int = _gp.transformY(y1);
      int y2Int = _gp.transformY(y2);
      int h = y2Int - y1Int;
      _imageBufG.drawRect(_gp.transformX(x), y1Int, _gp.transformX(w), h);
   }

   /**
    * fill a rect to the off screen Graphics buffer
    */
   public void fillaRect(ColorValue colorValue, float x, float y1, float w,
         float y2) {
      _imageBufG.setColor((Color) _colorScheme.getColor(colorValue));
      int y1Int = _gp.transformY(y1);
      int y2Int = _gp.transformY(y2);
      int h = y2Int - y1Int;
      _imageBufG.fillRect(_gp.transformX(x), y1Int, _gp.transformX(w), h);
   }

   /**
    * fill a poly to the off screen Graphics buffer
    */
   public void fillaPolygon(ColorValue colorValue, float[] xf, float[] yf) {
      _imageBufG.setColor((Color) _colorScheme.getColor(colorValue));
      Polygon p = new Polygon();
      for (int i = 0; i < xf.length; i++) {
         p.addPoint(_gp.transformX(xf[i]), _gp.transformY(yf[i]));
      }
      _imageBufG.fillPolygon(p);
   }

   /**
    * draw a dotted line to the Graphics buffer
    */
   public void drawaDotLineY(float x) {
      _imageBufG.setColor(
            ((Color) _colorScheme.getColor(ColorValue.GRID)).darker());
      for (float i = 0; i < 100; i += 2) {
         _imageBufG.drawLine(_gp.transformX(x), _gp.transformY(i),
               _gp.transformX(x), _gp.transformY(i));
      }
   }

   /**
    * draw a dotted line to the Graphics buffer
    */
   public void drawaDotLineX(int numPlotRecs, float x1, float y1, float x2,
         float y2) {
      _imageBufG.setColor(
            ((Color) _colorScheme.getColor(ColorValue.GRID)).darker());

      float dotinc = 0f;
      float xaxis = (numPlotRecs * 2); // xexpand

      if (numPlotRecs < 51)
         dotinc = 1f;
      else if (numPlotRecs < 126)
         dotinc = 2.5f;
      else if (numPlotRecs < 251)
         dotinc = 5f;
      else if (numPlotRecs < 551)
         dotinc = 10f;
      else
         dotinc = 20f;

      for (float i = 0; i < (xaxis); i += dotinc) {
         _imageBufG.drawLine(_gp.transformX(i), _gp.transformY(y1),
               _gp.transformX(i), _gp.transformY(y2));
      }
   }

   /**
    * draw a String to the Graphics buffer
    */
   public void drawaString(ColorValue colorValue, String stringOut, float x,
         float y) {
      _imageBufG.setColor((Color) _colorScheme.getColor(colorValue));
      _imageBufG.setFont(_labelFont);
      _imageBufG.drawString(stringOut, _gp.transformX(x), _gp.transformY(y));
   }

   /**
    * draw a String to the Graphics buffer
    */
   public void drawXLabel(ColorValue colorValue, String stringOut, float x) {
      _imageBufG.setColor((Color) _colorScheme.getColor(colorValue));
      _imageBufG.setFont(_labelFont);
      _imageBufG.drawString(stringOut, _gp.transformX(x), _xLabelPos);
   }

   public void drawXLabelTick(ColorValue colorValue, float x) {
      int tickPos = _xLabelPos;// - _labelHeight;
      _imageBufG.setColor((Color) _colorScheme.getColor(colorValue));
      _imageBufG.drawLine(_gp.transformX(x), tickPos - 10, _gp.transformX(x),
            tickPos - 15);
   }

   /**
    * draw an image to the off screen Graphics buffer and to the _canvasG
    * display image from off screen buffer instead of re-plotting the chart
    */
   public void displayImage() {
      try {
         if (_imageBufG != null) {
            _imageBufG.drawImage(_imageBuf, 0, 0, this);
         }
         // no double buffering
         if (_canvasG != null) {
            _canvasG.drawImage(_imageBuf, 0, 0, this);
         }
      } catch (Exception e) {
      }
   }

   // ************ End Drawing Output Section **************/

   // ************ Trend Line Section **********************
   public void setTrendline(Point pointA, Point pointB) {
      _curTrLine = getCurTrendline();
      PlotDataModel plotDataModel = _jchartComposite.getPlotDataModel();
      _trendlines[_curTrLine] = new Trendline(pointA, pointB, _gp);
      if (_chartTypeModel.isRegressTrendlineOn()) {
         // erase prior trendline
         drawTrendLine(_trendlines[_curTrLine].getPointAi(),
               _trendlines[_curTrLine].getPointBi());

         _trendlines[_curTrLine].regress(
               _jchartComposite.getQuoteDataModel().getQuotes(),
               plotDataModel.getPlotRange());

         // draw new trendline
         drawTrendLine(_trendlines[_curTrLine].getPointAi(),
               _trendlines[_curTrLine].getPointBi());

      } else if (_chartTypeModel.isHorizontalLineOn()) {
         drawTrendLine(_trendlines[_curTrLine].getPointAi(),
               _trendlines[_curTrLine].getPointBi());

         _trendlines[_curTrLine].horizontal(plotDataModel.getPlotRange());

         // draw new trendline
         drawTrendLine(_trendlines[_curTrLine].getPointAi(),
               _trendlines[_curTrLine].getPointBi());

      }
   }

   public void updateTrendline(Point pointA, Point pointB) {
      _trendlines[_curTrLine].update(pointA, pointB);
   }

   public void drawTrendLines() {
      Trendline[] trendline = null;
      try {
         trendline = JchartCanvas.getTrendLines();
      } catch (NoClassDefFoundError e) {
         // NO-OP
      }
      if (trendline == null) {
         return;
      }

      Fpoint pointA;
      Fpoint pointB;

      for (int i = 0; i < Kjchart.MAXTRLINE; i++)
         if (trendline[i] != null) {
            pointA = trendline[i].getPointAf();
            pointB = trendline[i].getPointBf();
            drawaLine(ColorValue.COLOR_GREEN, pointA.x, pointA.y, pointB.x,
                  pointB.y);
         }
   }

   private int getCurTrendline() {
      int curTrLine = -1;
      // find a null trendline
      for (int i = 0; i < Kjchart.MAXTRLINE; i++) {
         if (_trendlines[i] == null) {
            curTrLine = i;
            break;
         }
      }
      if (curTrLine == -1) {
         curTrLine++;
         if (curTrLine == Kjchart.MAXTRLINE)
            curTrLine = 0;
      }
      return curTrLine;
   }

   public boolean onTrendLine() {
      // check all trendlines
      for (int i = 0; i < Kjchart.MAXTRLINE; i++)
         if (_trendlines[i] != null) {
            if (_trendlines[i].onTrendLine(_mouseAction.getPtClicked())) {
               _curTrLine = i;
               return true;
            }
         }
      return false;
   }

   public void delTrLine() {
      drawTrendLine(_trendlines[_curTrLine].getPointAi(),
            _trendlines[_curTrLine].getPointBi());
      _trendlines[_curTrLine] = null;
   }

   public void nullTrendLines() {
      for (int i = 0; i < Kjchart.MAXTRLINE; i++) {
         _trendlines[i] = null;
      }
      _curTrLine = 0;
   }

   public void drawTrendLine(Point pointAi, Point pointBi) {
      if (!initialized())
         return;

      Color c = Color.green;
      c.brighter();
      // on canvas
      _canvasG.setXORMode(c);
      _canvasG.drawLine(pointAi.x, pointAi.y, pointBi.x, pointBi.y);
      _canvasG.setPaintMode();
      // on buffer
      _imageBufG.setColor((Color) _colorScheme.getColor(ColorValue.TOPBACK));
      _imageBufG.setXORMode(c);
      _imageBufG.drawLine(pointAi.x, pointAi.y, pointBi.x, pointBi.y);
      _imageBufG.setPaintMode();
   }

   public void drawVert() {
      drawVert(ColorValue.COLOR_GREEN);
   }

   public void eraseVert() {
      if (!initialized())
         return;
      drawVert();
   }

   private void drawVert(ColorValue colorValue) {
      if (!initialized())
         return;
      Color c = (Color) _colorScheme.getColor(colorValue);

      c.brighter();
      // on canvas
      _canvasG.setXORMode(c);
      _canvasG.drawLine(_vertPointA.x, _vertPointA.y, _vertPointB.x,
            _vertPointB.y);
      _canvasG.setPaintMode();
      // on buffer
      _imageBufG.setColor((Color) _colorScheme.getColor(ColorValue.TOPBACK));
      _imageBufG.setXORMode(c);
      _imageBufG.drawLine(_vertPointA.x, _vertPointA.y, _vertPointB.x,
            _vertPointB.y);
      _imageBufG.setPaintMode();
   }

   public void setVert(int x) {
      _vertPointA.x = x;
      _vertPointA.y = 0;
      _vertPointB.x = x;
      _vertPointB.y = _canvasSize.height;
   }

   public void drawVertLine(int x) {
      drawVertLine(x, ColorValue.COLOR_GREEN);
   }

   public void drawVertLine(int x, ColorValue colorValue) {
      if (!initialized())
         return;
      setVert(x);
      drawVert(colorValue);
   }

   public void drawHorizLine(int xMove, int yMove) {
      Point ptMove = new Point(xMove, yMove);
      Point pointAi = new Point(0, ptMove.y);
      Point pointBi = new Point(_canvasSize.width, ptMove.y);
      drawTrendLine(pointAi, pointBi);
   }

   public void fillVertRect(ColorValue colorValue, int x, int w) {
      Color c = (Color) _colorScheme.getColor(colorValue);
      c.brighter();
      // on canvas
      _canvasG.setXORMode(c);
      _canvasG.fillRect(x, 0, w, _canvasSize.height);
      _canvasG.setPaintMode();
      // on buffer
      _imageBufG.setColor((Color) _colorScheme.getColor(ColorValue.TOPBACK));
      _imageBufG.setXORMode(c);
      _imageBufG.fillRect(x, 0, w, _canvasSize.height);
      _imageBufG.setPaintMode();
      _mouseAction.setDrawVertRect(true);
   }

   /**
    * @return Trendline[]
    */
   public static Trendline[] getTrendLines() {
      return _trendlines;
   }

   public static Trendline getTrendline() {
      return _trendlines[_curTrLine];
   }

   public float inverseX(int x) {
      return _gp.inverseX(x);
   }

   public void reset() {
      _gp.reset();
   }

   private boolean initialized() {
      return (_canvasG != null) && (_imageBufG != null);
   }

   public void drawYlabelRightInverse(float y, String label) {
      _ylabelBufG.setColor(Color.decode("#FF6600"));
      _ylabelBufG.fillRect(0, 0, _rightLabelWidth, _labelHeight);
      _ylabelBufG.setColor(Color.white);
      _ylabelBufG.fillRect(1, 1, _rightLabelWidth - 3, _labelHeight - 2);
      _ylabelBufG.setColor(Color.black);
      _ylabelBufG.drawString(label, 2, _labelHeight - (int) (_labelHeight / 4));
      _canvasG.drawImage(_ylabelBuf, _yLabelRightX - 3,
            _gp.transformY(y) - _labelHeight + (int) (_labelHeight / 3), this);
   }

   // delegate mouse actions to MouseAction
   public void mousePressed(MouseEvent e) {
      _mouseAction.mousePressed(e);
   }

   public void mouseReleased(MouseEvent e) {
      _mouseAction.mouseReleased(e);
   }

   public void mouseDragged(MouseEvent e) {
      if (e.getModifiers() == MouseEvent.BUTTON1_MASK)
         _mouseAction.mouseDragged(e);
   }

   public void mouseMoved(MouseEvent e) {
      _mouseAction.mouseMoved(e);
   }

   // override implementations of mouse events
   public void mouseClicked(MouseEvent e) {
      _mouseAction.mouseClicked(e);
   }

   public void mouseEntered(MouseEvent e) {
      _mouseAction.mouseEntered(e);
   }

   public void mouseExited(MouseEvent e) {
      _mouseAction.mouseExited(e);
   }

   // public void setCurTrendLine() {
   // _trendline.setPointAi(new Point(_trendlines[_curTrLine].getPointAi()));
   // _trendline.setPointBi(new Point(_trendlines[_curTrLine].getPointBi()));
   // }
   public Float getInverseY(int i) {
      return new Float(_gp.inverseY(i));
   }

   // method override
   public void update(Graphics g) {
      paint(g);
   }

   /**
    * called by container - display image from off screen buffer instead of
    * re-plotting the chart
    */
   public void paint(Graphics g) {
      if (_imageBufG != null)
         displayImage();
   }

   public void setMouseAction(MouseAction mouseAction) {
      _mouseAction = mouseAction;
   }

   public boolean isSwt() {
      return false;
   }
}
