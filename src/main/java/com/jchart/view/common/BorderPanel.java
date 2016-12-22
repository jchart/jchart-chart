package com.jchart.view.common;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Panel;

import com.jchart.view.client.PnlJchart;

public class BorderPanel extends Panel {
   private static final long serialVersionUID = 1L;
   private PnlJchart _pnlJchart;
   private Dimension _size;
   private static final Insets insets = new Insets(5, 7, 5, 2);

   public Insets getInsets() {
      return insets;
   }

   private boolean _firstTime = true;

   public BorderPanel(PnlJchart pnlJchart, boolean isFloat, Color backColor) {
      super();
      _pnlJchart = pnlJchart;
      if (isFloat)
         _firstTime = false;
      setBackground(backColor);
      setLayout(new GridLayout(1, 0));
      _size = getSize();
   }

   public void update(Graphics g) {
      paint(g);
   }

   /**
    * called by container - display image from off screen buffer instead of
    * re-plotting the chart
    */
   public void paint(Graphics g) {
      if (_firstTime) {
         _firstTime = false;
         return;
      }
      Dimension size = getSize();
      // replot if resized
      if ((_size.height != size.height) || (_size.width != size.width)) {
         _size = size;
         _pnlJchart.plotMain();
      } else {
         _pnlJchart.paintIt(g);
      }
   }
}
