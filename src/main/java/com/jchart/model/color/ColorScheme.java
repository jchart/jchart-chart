package com.jchart.model.color;

import java.util.HashMap;
import java.util.Map;

import com.jchart.model.ChangeRequest;
import com.jchart.model.JchartRequest;

public abstract class ColorScheme
      implements ChangeRequest, java.io.Serializable {

   // colorizable attributes

   // color scheme constants
   protected static final int DEFAULT = 0;
   protected static final int BLACK = 1;
   protected static final int WHITE = 2;
   protected static final int MAROON = 4;
   protected static final int PRINTER = 5;

   // color name contants

   protected Map _colorMap;

   protected abstract void addColor(ColorValue colorValue);

   protected abstract void setColor(ColorValue colorValue1,
         ColorValue colorVal2);

   private int _background = -1;
   private int _border = -1;
   private JchartRequest _jchartRequest;

   private static final String[] _colorLabel = { "Default", "Black", "White",
         "DarkBlue", "Maroon", "Printer" };
   private static final int _numColors = _colorLabel.length;

   public abstract void destroy();

   public Object getColor(ColorValue colorValue) {
      return _colorMap.get(colorValue);
   }

   public String getColorName(int i) {
      return _colorLabel[i];
   }

   public int getNumColors() {
      return _numColors;
   }

   public void init() {
      _colorMap = new HashMap();
      for (int i = 0; i < ColorValue.COLOR_VALUES.length; i++) {
         addColor(ColorValue.COLOR_VALUES[i]);
      }

   }

   public void setColor(int background, int border) {
      if (background == _background) {

         return;
      }
      _background = background;
      _border = border;

      switch (_background) {
      case WHITE:
         setColor(ColorValue.PRICE, ColorValue.COLOR_BLUE);
         setColor(ColorValue.VOL, ColorValue.COLOR_BLUE);
         setColor(ColorValue.TOPBACK, ColorValue.COLOR_WHITE);
         setColor(ColorValue.BOTBACK, ColorValue.COLOR_WHITE);
         setColor(ColorValue.TOPCAPTION, ColorValue.COLOR_DARKGRAY);
         setColor(ColorValue.BOTCAPTION, ColorValue.COLOR_DARKGRAY);

         break;

      case BLACK:
         setColor(ColorValue.PRICE, ColorValue.COLOR_YELLOW);
         setColor(ColorValue.VOL, ColorValue.COLOR_YELLOW);
         setColor(ColorValue.TOPBACK, ColorValue.COLOR_BLACK);
         setColor(ColorValue.BOTBACK, ColorValue.COLOR_BLACK);
         setColor(ColorValue.TOPCAPTION, ColorValue.COLOR_LIGHTGRAY);
         setColor(ColorValue.BOTCAPTION, ColorValue.COLOR_LIGHTGRAY);

         break;

      case MAROON:
         setColor(ColorValue.PRICE, ColorValue.COLOR_YELLOW);
         setColor(ColorValue.VOL, ColorValue.COLOR_YELLOW);
         setColor(ColorValue.TOPBACK, ColorValue.COLOR_MAROON);
         setColor(ColorValue.BOTBACK, ColorValue.COLOR_DARKBLUE1);
         setColor(ColorValue.TOPCAPTION, ColorValue.COLOR_LIGHTGRAY);
         setColor(ColorValue.BOTCAPTION, ColorValue.COLOR_LIGHTGRAY);

         break;

      default:
         setColor(ColorValue.PRICE, ColorValue.COLOR_YELLOW);
         setColor(ColorValue.VOL, ColorValue.COLOR_YELLOW);
         setColor(ColorValue.TOPBACK, ColorValue.COLOR_DARKBLUE1);
         setColor(ColorValue.BOTBACK, ColorValue.COLOR_DARKBLUE2);
         setColor(ColorValue.TOPCAPTION, ColorValue.COLOR_WHITE);
         setColor(ColorValue.BOTCAPTION, ColorValue.COLOR_WHITE);

         break;

      case PRINTER:
         setColor(ColorValue.PRICE, ColorValue.COLOR_BLACK);
         setColor(ColorValue.VOL, ColorValue.COLOR_BLACK);
         setColor(ColorValue.TOPBACK, ColorValue.COLOR_WHITE);
         setColor(ColorValue.BOTBACK, ColorValue.COLOR_WHITE);
         setColor(ColorValue.TOPCAPTION, ColorValue.COLOR_DARKGRAY);
         setColor(ColorValue.BOTCAPTION, ColorValue.COLOR_DARKGRAY);

         break;

      case DEFAULT:
         processRequest(_jchartRequest);
         return;
      }

      switch (_border) {
      case WHITE:
         setColor(ColorValue.BORDER, ColorValue.COLOR_WHITE);
         setColor(ColorValue.GRID, ColorValue.COLOR_BLACK);
         setColor(ColorValue.XYLABEL, ColorValue.COLOR_BLACK);

         break;

      case BLACK:
         setColor(ColorValue.BORDER, ColorValue.COLOR_BLACK);
         setColor(ColorValue.GRID, ColorValue.COLOR_WHITE);
         setColor(ColorValue.XYLABEL, ColorValue.COLOR_WHITE);

         break;

      case PRINTER:
         setColor(ColorValue.BORDER, ColorValue.COLOR_WHITE);
         setColor(ColorValue.GRID, ColorValue.COLOR_BLACK);
         setColor(ColorValue.XYLABEL, ColorValue.COLOR_BLACK);

         break;

      default:
         setColor(ColorValue.BORDER, ColorValue.COLOR_BLACK);
         setColor(ColorValue.GRID, ColorValue.COLOR_LIGHTGRAY);
         setColor(ColorValue.XYLABEL, ColorValue.COLOR_LIGHTGRAY);

         break;
      }
   }

   public void processRequest(JchartRequest jchartRequest) {
      _jchartRequest = jchartRequest;
      setColor(ColorValue.PRICE, _jchartRequest.getPriceColor());
      setColor(ColorValue.VOL, _jchartRequest.getVolColor());
      setColor(ColorValue.TOPBACK, _jchartRequest.getTopBackColor());
      setColor(ColorValue.BOTBACK, _jchartRequest.getBotBackColor());
      setColor(ColorValue.TOPCAPTION, _jchartRequest.getTopCaptionColor());
      setColor(ColorValue.BOTCAPTION, _jchartRequest.getBotCaptionColor());
      setColor(ColorValue.BORDER, _jchartRequest.getBorderColor());
      setColor(ColorValue.GRID, _jchartRequest.getGridColor());
      setColor(ColorValue.XYLABEL, _jchartRequest.getXyLabelColor());
      setColor(ColorValue.MOUNTBORDER, _jchartRequest.getMountBorderColor());
      setColor(ColorValue.GIFBORDER, _jchartRequest.getGifBorderColor());
   }

}