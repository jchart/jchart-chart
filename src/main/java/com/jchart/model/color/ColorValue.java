/*
 * Created on Dec 27, 2005
 *
 */
package com.jchart.model.color;

public class ColorValue {

   public static final ColorValue COLOR_WHITE = new ColorValue(255, 255, 255);
   public static final ColorValue COLOR_BLACK = new ColorValue(0, 0, 0);
   public static final ColorValue COLOR_BLUE = new ColorValue(0, 0, 255);
   public static final ColorValue COLOR_DARKGRAY = new ColorValue(64, 64, 64);
   public static final ColorValue COLOR_YELLOW = new ColorValue(255, 255, 0);
   public static final ColorValue COLOR_LIGHTGRAY = new ColorValue(192, 192,
         192);
   public static final ColorValue COLOR_DARKBLUE1 = new ColorValue(0, 50, 100);
   public static final ColorValue COLOR_DARKBLUE2 = new ColorValue(0, 50, 150);
   public static final ColorValue COLOR_MAROON = new ColorValue(102, 0, 51);
   public static final ColorValue COLOR_CYAN = new ColorValue(0, 255, 255);
   public static final ColorValue COLOR_MAGENTA = new ColorValue(255, 0, 255);
   public static final ColorValue COLOR_RED = new ColorValue(255, 0, 0);
   public static final ColorValue COLOR_GREEN = new ColorValue(0, 255, 0);
   public static final ColorValue COLOR_GRAY = new ColorValue(128, 128, 128);

   public static final ColorValue PRICE = new ColorValue(0, 0, 0);
   public static final ColorValue VOL = new ColorValue(0, 0, 0);
   public static final ColorValue TOPBACK = new ColorValue(0, 0, 0);
   public static final ColorValue BOTBACK = new ColorValue(0, 0, 0);
   public static final ColorValue TOPCAPTION = new ColorValue(0, 0, 0);
   public static final ColorValue BOTCAPTION = new ColorValue(0, 0, 0);
   public static final ColorValue BORDER = new ColorValue(0, 0, 0);
   public static final ColorValue GRID = new ColorValue(0, 0, 0);
   public static final ColorValue XYLABEL = new ColorValue(0, 0, 0);
   public static final ColorValue PNLSTATSBACK = new ColorValue(0, 0, 0);
   public static final ColorValue PNLSTATSFORE = new ColorValue(0, 0, 0);
   public static final ColorValue MOUNTBORDER = new ColorValue(0, 0, 0);
   public static final ColorValue GIFBORDER = new ColorValue(0, 0, 0);

   public static final ColorValue[] COLOR_VALUES = { COLOR_WHITE, COLOR_BLACK,
         COLOR_BLUE, COLOR_DARKGRAY, COLOR_YELLOW, COLOR_LIGHTGRAY,
         COLOR_DARKBLUE1, COLOR_DARKBLUE1, COLOR_MAROON, COLOR_CYAN,
         COLOR_MAGENTA, COLOR_RED, COLOR_GREEN, COLOR_GRAY, PRICE, VOL, TOPBACK,
         BOTBACK, TOPCAPTION, BOTCAPTION, BORDER, GRID, XYLABEL, PNLSTATSBACK,
         PNLSTATSFORE, MOUNTBORDER, GIFBORDER };

   private int _red;
   private int _green;
   private int _blue;

   public ColorValue(int red, int green, int blue) {
      _red = red;
      _green = green;
      _blue = blue;
   }

   public int getRed() {
      return _red;
   }

   public int getGreen() {
      return _green;
   }

   public int getBlue() {
      return _blue;
   }

}
