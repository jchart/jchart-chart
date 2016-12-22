package com.jchart.view.common;

import com.jchart.model.Frect;
import com.jchart.model.Irect;

public class Graphport {
   private Irect irect;
   private Frect frect;
   private float fXZero;
   private float fYZero;
   private float fXUnit;
   private float fYUnit;
   private float total;

   public Graphport() {
      frect = new Frect();
      irect = new Irect();
   }

   public void setPorts(Irect viewRect, Frect graphRect) {
      irect = viewRect;
      frect = graphRect;
      calcTransform();
   }

   public void reset() {
      calcTransform();
   }

   private void calcTransform() {
      // Calculate horizontal ratios
      total = frect.right - frect.left;
      if (total != 0) {
         fXUnit = (irect.right - irect.left) / total;
      }
      // fXZero = (-frect.right * fXUnit) + irect.right;
      fXZero = (-frect.right * fXUnit) + irect.right;

      // Calculate vertical ratios
      total = frect.top - frect.bottom;
      if (total != 0) {
         fYUnit = (irect.bottom - irect.top) / total;
      }
      fYZero = (frect.top * fYUnit) + irect.top;
   }

   public int transformX(float coordinate) {
      float x = ((coordinate * fXUnit) + fXZero);
      int xInt = Math.round(x);
      return xInt;
   }

   public int transformY(float coordinate) {
      float y = (-(coordinate * fYUnit) + fYZero);
      int yInt = Math.round(y);
      return yInt;
   }

   public float inverseX(int coordinate) {
      return (coordinate - fXZero) / fXUnit;
   }

   public float inverseY(int coordinate) {
      return (fYZero - coordinate) / fYUnit;
   }

   public boolean ptInViewPort(int x, int y) {
      return ((x >= irect.left) & (x <= irect.right) & (y >= irect.top)
            & (x <= irect.bottom));
   }
}
