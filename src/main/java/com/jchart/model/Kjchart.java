package com.jchart.model;

public interface Kjchart {
   // public final static int MAXQUOTES = 1400;
   public final static int MAXTRLINE = 100;
   public final static float IFILLER = -55555f;
   // indicator constants
   public static final int PRICEMA1 = 11;
   public static final int PRICEMA2 = 12;
   public static final int PRICEMA3 = 13;

   // clipping region percentages
   public static final int XEXPAND = 2;

   // chart type constants
   public final static int CT_PRICE = -1;
   public final static int CT_VOLUME = -2;
   public final static int CT_INDICATOR = -3;
   public final static int CT_LABEL = -4;

   public static final int POS_PRICE = 1;
   public static final int POS_INDICATOR = 2;
   public static final int POS_BOTTOM_LABEL = 3;
   public static final int POS_PNF = 4;
   public static final int POS_SINGLE_PLOT = 5;

   public static final String BULLISH_PCT_PREFIX = "_bp_";

}
