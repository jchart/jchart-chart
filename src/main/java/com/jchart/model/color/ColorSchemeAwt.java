package com.jchart.model.color;

import java.awt.Color;
import java.util.Iterator;

public class ColorSchemeAwt extends ColorScheme {
   private static final long serialVersionUID = 1L;

   // #660033 decode
   private static final Color COLOR_YELLOW = Color.yellow.brighter();

   protected void addColor(ColorValue colorValue) {
      Color color = new Color(colorValue.getRed(), colorValue.getGreen(),
            colorValue.getBlue());
      _colorMap.put(colorValue, color);
   }

   protected void setColor(ColorValue colorValKey, ColorValue colorVal) {
      Color targetColor = new Color(colorVal.getRed(), colorVal.getGreen(),
            colorVal.getBlue());
      _colorMap.put(colorValKey, targetColor);
   }

   public void destroy() {
      // explicit nulls
      for (Iterator itor = _colorMap.entrySet().iterator(); itor.hasNext();) {
         Object obj = itor.next();
         obj = null;
      }
   }

}
