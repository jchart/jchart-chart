package com.jchart.view.client;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import com.jchart.model.JchartComposite;
import com.jchart.model.Quote;
import com.jchart.model.QuoteString;
import com.jchart.model.Trendline;
import com.jchart.model.color.ColorValue;
import com.jchart.util.FormatUtils;
import com.jchart.view.common.JchartCanvas;

public class MouseAction implements MouseListener, MouseMotionListener {
   private JchartCanvas _jchartCanvas;
   private int _beginPlotRec;
   private boolean _isPopupMenu = false;
   private Point _ptClicked;
   private boolean _dragTrLine = false;
   private boolean _drawTrLine = false;
   private int _xVert;
   private boolean _drawVertRect = false;
   private PnlJchart _pnlJchart;
   private Point _anchorPointA = new Point();
   private Point _anchorPointB = new Point();
   private Point _dragPointA = new Point();
   private Point _dragPointB = new Point();
   private int _mouseButton;
   private JchartComposite _jchartComposite;
   // cursor types
   private static final Cursor _curDefault = new Cursor(Cursor.DEFAULT_CURSOR);
   private static final Cursor _curXhair = new Cursor(Cursor.CROSSHAIR_CURSOR);

   // constructor
   public MouseAction(JchartCanvas jchartCanvas,
         JchartComposite jchartComposite, PnlJchart pnlJchart) {
      _jchartComposite = jchartComposite;
      _jchartCanvas = jchartCanvas;
      _pnlJchart = pnlJchart;
   }

   /**
    * mousePressed
    */
   public void mousePressed(MouseEvent e) {
      if (_isPopupMenu) {
         _isPopupMenu = false;
      }
      _ptClicked = new Point(e.getX(), e.getY());
      int clicks = e.getClickCount();
      _mouseButton = e.getModifiers();

      // if button 2 or 3 was pressed
      if ((_mouseButton == MouseEvent.BUTTON2_MASK) && (clicks == 1)) {
         _pnlJchart.plotMain();
         return;
      }

      if (clicks == 1) {
         if (_jchartCanvas.getCursor() == _curXhair)
            return;
         if (_jchartCanvas.onTrendLine()) {
            Trendline trendline = JchartCanvas.getTrendline();
            _dragPointA = new Point(trendline.getPointAi());
            _dragPointB = new Point(trendline.getPointBi());
            _anchorPointA = new Point(trendline.getPointAi());
            _anchorPointB = new Point(trendline.getPointBi());
            _dragTrLine = true;
            _drawTrLine = false;
            return;

         } else {
            _dragTrLine = false;
            // set drawtrendline mode
            if (_drawTrLine) {
               _jchartCanvas.setTrendline(_anchorPointA, _anchorPointB);
               _drawTrLine = false;
               return;
            } else {
               _anchorPointA = _ptClicked;
               _anchorPointB = _ptClicked;
               _dragPointA = new Point(_anchorPointA);
               _drawTrLine = true;
            }
         }

      } else if ((_mouseButton == MouseEvent.BUTTON1_MASK) && (clicks == 2)) {
         _dragTrLine = false;
         _drawTrLine = false;
         if (_jchartCanvas.getCursor() == _curXhair) {//
            _jchartCanvas.setCursor(_curDefault);
            // horizontal & vertical lines
            _jchartCanvas.drawHorizLine(_ptClicked.x, _ptClicked.y);
            _jchartCanvas.drawVertLine(_ptClicked.x);
         } else if (_jchartCanvas.onTrendLine()) {
            _jchartCanvas.delTrLine();
         } else {
            _jchartCanvas.setCursor(_curXhair);
            _jchartCanvas.drawHorizLine(_ptClicked.x, _ptClicked.y);
            _jchartCanvas.drawVertLine(_ptClicked.x);
            // _jchartCanvas.eraseVert();
         }
      }
   }

   public void mouseReleased(MouseEvent e) {
      if (_jchartCanvas.getCursor() == _curXhair)
         return;

      if (_dragTrLine) {
         _dragTrLine = false;
         _jchartCanvas.updateTrendline(_dragPointA, _dragPointB);
      }
      if (_drawVertRect) {
         int endPlotRec = getListno(e.getX());
         // Quote quote1 =
         // _jchart.getJchartComposite().getQuoteDataModel().getQuote(_beginPlotRec);
         // Quote quote2 =
         // _jchart.getJchartComposite().getQuoteDataModel().getQuote(endPlotRec);
         _drawVertRect = false;
         if (_beginPlotRec > endPlotRec) {
            int tempPlotRec = endPlotRec;
            endPlotRec = _beginPlotRec;
            _beginPlotRec = tempPlotRec;
         }
         if (endPlotRec - _beginPlotRec < 5) {
            _pnlJchart.plotMain();
         } else {
            endPlotRec++;
            _pnlJchart.plotZoom(_beginPlotRec, endPlotRec);
         }
      }

   }

   /**
    * mouseDragged
    */
   public void mouseDragged(MouseEvent e) {
      if (_jchartCanvas.getCursor() == _curXhair)
         return;

      if (_drawTrLine) {
         // start of drag-zoom
         _drawTrLine = false;
         _beginPlotRec = getListno(_ptClicked.x);
         _xVert = _ptClicked.x;
         _jchartCanvas.setVert(_xVert);
         _pnlJchart.setFocus();
      }

      if (_dragTrLine) {
         // erase prior line
         _jchartCanvas.drawTrendLine(_dragPointA, _dragPointB);

         Point ptDrag = new Point(e.getX(), e.getY());
         Point ptA = new Point();
         Point ptB = new Point();

         ptA.x = ptDrag.x - (_ptClicked.x - _anchorPointA.x);
         ptA.y = ptDrag.y - (_ptClicked.y - _anchorPointA.y);
         ptB.x = ptDrag.x + (_anchorPointB.x - _ptClicked.x);
         ptB.y = ptDrag.y + (_anchorPointB.y - _ptClicked.y);

         // draw new line
         _jchartCanvas.drawTrendLine(ptA, ptB);

         _dragPointA = ptA;
         _dragPointB = ptB;

      } else {
         int x = e.getX();
         int w = x - _xVert;
         if (w > 0) {
            _jchartCanvas.fillVertRect(ColorValue.COLOR_LIGHTGRAY, _xVert, w);
         } else {
            _jchartCanvas.fillVertRect(ColorValue.COLOR_LIGHTGRAY, x, w * -1);
         }
         _xVert = x;
      }
   }

   /**
    * mouseMoved
    */
   public void mouseMoved(MouseEvent e) {
      Float f = _jchartCanvas.getInverseY(e.getY());
      int listno = getListno(e.getX());
      Quote quote = _jchartComposite.getQuoteDataModel().getQuote(listno);
      Quote quote2 = _jchartComposite.getQuoteDataModel().getQuote(listno - 1);
      String cursorPos = FormatUtils.formatDecimal(f.toString(), 3);
      _pnlJchart.setCurPosPriceLabel(
            new QuoteString(_jchartComposite.getTimeFrameModel().isIntraday(),
                  quote, (int) quote2.getClose()),
            cursorPos);

      Point ptMove = new Point(e.getX(), e.getY());
      // crosshair
      if (_jchartCanvas.getCursor() == _curXhair) {
         // erase prior horizontal line
         if (_ptClicked != null) {
            _jchartCanvas.drawHorizLine(_ptClicked.x, _ptClicked.y);
            _ptClicked = null;
         } else {
            _jchartCanvas.drawHorizLine(_anchorPointB.x, _anchorPointB.y);
         }
         _jchartCanvas.drawHorizLine(ptMove.x, ptMove.y);
         _anchorPointB = ptMove;
         // erase prior vertical line
         _jchartCanvas.eraseVert();
         _jchartCanvas.drawVertLine(ptMove.x);
      }
      if (_mouseButton == MouseEvent.BUTTON1_MASK)
         _drawTrLine = false;
      if (_drawTrLine) {
         // erase prior line
         _jchartCanvas.drawTrendLine(_anchorPointA, _anchorPointB);
         _anchorPointB = ptMove;
         _dragPointB = new Point(_anchorPointB);
         // draw new line
         _jchartCanvas.drawTrendLine(_anchorPointA, _anchorPointB);
      }
   }

   /**
    * getListno
    */
   private int getListno(int xval) {
      return _pnlJchart.getListno(xval);
   }

   // override implementations of mouse events
   public void mouseClicked(MouseEvent e) {
   }

   public void mouseEntered(MouseEvent e) {
      // eraseVert(); //**
   }

   public void mouseExited(MouseEvent e) {
      if (_jchartCanvas.getCursor() == _curXhair) {
         _jchartCanvas.setCursor(_curDefault);
         _jchartCanvas.invalidate();
         _jchartCanvas.repaint();

      }
   }

   public Point getPtClicked() {
      return _ptClicked;
   }

   public void setDrawVertRect(boolean b) {
      _drawVertRect = b;
   }
}
