package com.jchart.view.common;

import com.jchart.model.Frect;
import com.jchart.model.color.ColorValue;

public interface JchartRenderer {
   public void setColor(ColorValue colorValue);

   public void doCreateImage(int width, int height, int labelFontSize,
         String labelStrSize);

   public void setCoordinates(int chartPos, Frect graphRect);

   public void fillPlotSection(boolean singlePlot);

   public void drawPlotSectionBorder(boolean singlePlot, boolean isGif);

   public void setImageColor(ColorValue colorValue);

   public void drawYlabelRight(float y, String label);

   public void drawaLabel(ColorValue foreground, ColorValue background,
         String label, float y);

   public void drawaLabel(ColorValue foreground, ColorValue background,
         String label, int x, float y);

   public void drawYlabelRightTop(String s, int plotSection);

   public void drawMidRightLabel(String label, boolean singlePlot);

   public void drawMidLeftLabel(String label, boolean singlePlot);

   public void drawNextHorizLabel(ColorValue colorValue, String priorLabel,
         String label, float y);

   public void drawaLine(ColorValue colorValue, float x1, float y1, float x2,
         float y2);

   public void drawaRect(ColorValue colorValue, float x, float y1, float w,
         float y2);

   public void fillaRect(ColorValue colorValue, float x, float y1, float w,
         float y2);

   public void fillaPolygon(ColorValue colorValue, float[] xf, float[] yf);

   public void drawaDotLineY(float x);

   public void drawaDotLineX(int numPlotRecs, float x1, float y1, float x2,
         float y2);

   public void drawaString(ColorValue colorValue, String stringOut, float x,
         float y);

   public void drawXLabel(ColorValue colorValue, String stringOut, float x);

   public void drawXLabelTick(ColorValue colorValue, float x);

   public void displayImage();

   public void nullTrendLines();

   public void drawVert();

   public void drawVertLine(int x);

   public void drawVertLine(int x, ColorValue colorValue);

   public void drawHorizLine(int xMove, int yMove);

   public void fillVertRect(ColorValue colorValue, int x, int w);

   public void drawYlabelRightInverse(float y, String label);

   public int getWidth();

   public int getHeight();

   public void reset();

   public float inverseX(int i);

   public boolean isSwt();

   public void drawTrendLines();
}
