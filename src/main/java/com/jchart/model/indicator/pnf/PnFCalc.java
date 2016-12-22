/**
 * 
 */
package com.jchart.model.indicator.pnf;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jchart.model.Quote;

/**
 * given the current box, return the X or O reversal box factors in box size
 * changes
 * 
 * @author <a href ="paul.russo@jchart.com">Paul Russo</a>
 * @created Oct 25, 2010
 */
public class PnFCalc {
   private static int REV_SIZE = 3;
   private static int XSPAN = 21;
   private static SimpleDateFormat _dateF = new SimpleDateFormat("MM/dd/yyyy");
   private static boolean IS_BULLISH_PCT;

   private float _revBox;

   public float getRevBox() {
      return _revBox;
   }

   public void init(float curBox, boolean isUpReversal) {
      float revPoints = calcRevPoints(curBox, isUpReversal);
      if (isUpReversal) {
         _revBox = curBox + revPoints;
      } else {
         _revBox = curBox - revPoints;
      }

   }

   public static void setBullishPct(boolean b) {
      IS_BULLISH_PCT = b;
   }

   public static boolean isBullishPct() {
      return IS_BULLISH_PCT;
   }

   public List<PnfItem> execute(int qListSize, Quote[] quotes) {
      List<PnfItem> retval = new ArrayList<PnfItem>();
      boolean isXbox = true;
      float curBox = new Double(quotes[0].getLow()).intValue()
            + PnFCalc.getBoxSize(quotes[0].getLow());
      if (curBox > quotes[0].getHi()) {
         curBox = new Double(quotes[0].getLow()).intValue();
      }

      int numBoxes = 0;
      for (Quote quote : quotes) {
         if (quote.getDate() == null)
            continue;
         int numNewBoxes = 0;
         int numRevBoxes = 0;

         PnfItem pnfItem = new PnfItem();
         Date date = quote.getDate();
         pnfItem.setDate(_dateF.format(date));
         pnfItem.setNumBoxes(numBoxes);

         // continuation
         if (isXbox) {
            numNewBoxes = PnFCalc.getNewXBoxes(quote.getHi(), curBox);
         } else {
            numNewBoxes = PnFCalc.getNewOBoxes(quote.getLow(), curBox);
         }
         if (numNewBoxes > 0) {
            if (isXbox) {
               numBoxes += numNewBoxes;
               curBox = PnFCalc.getXBoxVal(numNewBoxes, curBox);
            } else {
               numBoxes -= numNewBoxes;
               curBox = PnFCalc.getOBoxVal(numNewBoxes, curBox);
            }
            pnfItem.setNumBoxes(numBoxes);
            pnfItem.setPrice(curBox);
            retval.add(pnfItem);
            continue;
         }

         // reversal
         if (isXbox) {
            numRevBoxes = PnFCalc.getNumRevOBoxes(quote.getLow(), curBox);
         } else {
            numRevBoxes = PnFCalc.getNumRevXBoxes(quote.getHi(), curBox);
         }
         if (numRevBoxes > 0) {
            if (isXbox) {
               numBoxes = numRevBoxes * -1;
               curBox = PnFCalc.getOBoxVal(numRevBoxes, curBox);
            } else {
               numBoxes = numRevBoxes;
               curBox = PnFCalc.getXBoxVal(numRevBoxes, curBox);
            }
            isXbox = !isXbox;
            pnfItem.setNumBoxes(numBoxes);
            pnfItem.setPrice(curBox);
            retval.add(pnfItem);
            continue;
         }
         pnfItem.setPrice(curBox);
         retval.add(pnfItem);
      }

      return retval;
   }

   public static float getRevXBox(float curBox) {
      float retval = 0;
      PnFCalc calc = new PnFCalc();
      calc.init(curBox, true);
      retval = calc.getRevBox();
      return retval;
   }

   public static int getNewXBoxes(float hi, float curBox) {
      int retval = 0;
      if (hi >= curBox) {
         float boxsize = PnFCalc.getBoxSize(curBox);
         float xBox = curBox + boxsize;
         while (xBox <= hi) {
            boxsize = PnFCalc.getBoxSize(xBox);
            xBox += boxsize;
            retval++;
         }
      }
      return retval;
   }

   public static float getXBoxVal(int numBoxes, float curBox) {
      float retval = curBox;
      for (int i = 0; i < numBoxes; i++) {
         float boxsize = PnFCalc.getBoxSize(retval);
         retval += boxsize;
      }
      return retval;
   }

   public static float getOBoxVal(int numBoxes, float curBox) {
      float retval = curBox;
      float boxsize = PnFCalc.getBoxSize(retval);
      float priorBoxsize = boxsize;
      for (int i = 0; i < numBoxes; i++) {
         boxsize = PnFCalc.getBoxSize(retval);
         retval -= boxsize;
         if (boxsize != priorBoxsize) {
            retval += boxsize;
         }
         priorBoxsize = boxsize;
      }
      return retval;
   }

   public static int getNewOBoxes(float lo, float curBox) {
      int retval = 0;
      if (lo <= curBox) {
         float boxsize = PnFCalc.getBoxSize(curBox);
         float priorBoxsize = boxsize;
         float oBox = curBox - boxsize;
         while (oBox >= lo) {
            boxsize = PnFCalc.getBoxSize(oBox);
            if (boxsize != priorBoxsize) {
               retval++;
            }
            priorBoxsize = boxsize;
            oBox -= boxsize;
            retval++;

         }
      }
      return retval;
   }

   public static int getNumRevXBoxes(float hi, float curBox) {
      int retval = 0;
      PnFCalc calc = new PnFCalc();
      calc.init(curBox, true);
      float xRevBox = calc.getRevBox();
      if (hi >= xRevBox) {
         retval = REV_SIZE;
         float boxsize = PnFCalc.getBoxSize(xRevBox);
         float xBox = xRevBox + boxsize;
         while (xBox <= hi) {
            boxsize = PnFCalc.getBoxSize(xRevBox);
            xBox += boxsize;
            retval++;
         }
      }
      return retval;
   }

   public static int getNumRevOBoxes(float lo, float curBox) {
      int retval = 0;
      PnFCalc calc = new PnFCalc();
      calc.init(curBox, false);
      float oRevBox = calc.getRevBox();
      if (lo <= oRevBox) {
         retval = REV_SIZE;
         float boxsize = PnFCalc.getBoxSize(oRevBox);
         float oBox = oRevBox - boxsize;
         while (oBox >= lo) {
            boxsize = PnFCalc.getBoxSize(oRevBox);
            oBox -= boxsize;
            retval++;
         }
      }
      return retval;
   }

   public static float getRevOBox(float curBox) {
      float retval = 0;
      PnFCalc calc = new PnFCalc();
      calc.init(curBox, false);
      retval = calc.getRevBox();
      return retval;
   }

   public static double calcStd(List<PnfItem> pnfItems) {
      double retval = 0;
      double sum = 0;
      double sumSqr = 0;
      double stdRange = pnfItems.size();
      for (PnfItem pnfItem : pnfItems) {
         double numBoxes = pnfItem.getNumBoxes();
         sum += numBoxes;
         sumSqr += numBoxes * numBoxes;
      }
      double numer = sumSqr - ((sum * sum) / stdRange);
      double quotient = numer / stdRange;
      retval = Math.sqrt(quotient);
      return retval;
   }

   public static double calcAvg(List<PnfItem> pnfItems) {
      double retval = 0d;
      double sum = 0;
      for (PnfItem pnfItem : pnfItems) {
         sum += pnfItem.getNumBoxes();
      }
      retval = sum / pnfItems.size();
      return retval;
   }

   public static List<PnfItem> calcBuySellPnfItems(List<PnfItem> pnfItems) {
      List<PnfItem> retval = new ArrayList<PnfItem>();
      PnfItem firstPnfItem = pnfItems.get(0);
      PnfItem priorColumnHiPnfItem = firstPnfItem;
      priorColumnHiPnfItem.setPrice(-1);
      PnfItem priorColumnLoPnfItem = firstPnfItem;
      priorColumnLoPnfItem.setPrice(1);
      PnfItem priorColumnPnfItem = firstPnfItem;
      boolean isXBox = firstPnfItem.isXBox();
      boolean isBuy = false;
      boolean isSell = false;
      boolean columnItemAdded = false;
      for (PnfItem pnfItem : pnfItems) {

         // one one signal per column; skip items until next column
         if (columnItemAdded) {
            if (pnfItem.isXBox() == isXBox) {
               if (isXBox) {
                  priorColumnHiPnfItem = pnfItem;
               } else {
                  priorColumnLoPnfItem = pnfItem;
               }
               priorColumnPnfItem = pnfItem;
               continue;
            }
         }
         columnItemAdded = false;

         // buy/sell check occurs here
         if (pnfItem.isXBox()) {
            if (!isBuy) {
               if (pnfItem.getPrice() > priorColumnHiPnfItem.getPrice()) {
                  // buy signal
                  isBuy = true;
                  isSell = false;
                  retval.add(pnfItem);
                  columnItemAdded = true;
               }
            }

         } else {
            if (!isSell) {
               if (pnfItem.getPrice() < priorColumnLoPnfItem.getPrice()) {
                  // sell signal
                  isSell = true;
                  isBuy = false;
                  retval.add(pnfItem);
                  columnItemAdded = true;
               }
            }
         }

         if (pnfItem.isXBox() != isXBox) {
            // column changed set the hi or lo item
            isXBox = pnfItem.isXBox();
            if (isXBox) {
               priorColumnLoPnfItem = priorColumnPnfItem;

            } else {
               priorColumnHiPnfItem = priorColumnPnfItem;
            }
         }
         priorColumnPnfItem = pnfItem;
      }
      return retval;
   }

   public static int calcNumYBoxes(float curPrice, float lowPrice) {
      int retval = 1;
      float boxsize;
      float accum = lowPrice;
      while (accum < curPrice) {
         boxsize = getBoxSize(accum);
         accum += boxsize;
         retval++;
      }
      return retval;
   }

   public static int calcPlotVal(float curPrice, float lowPrice) {
      int retval = 0;
      int numYBoxes = PnFCalc.calcNumYBoxes(curPrice, lowPrice);
      int boxPad = PnFCalc.getBoxPad(numYBoxes);
      retval = numYBoxes + boxPad;
      return retval;
   }

   public static int calcMaxYVal(float hiPrice, float lowPrice) {
      int retval = 0;
      int plotVal = PnFCalc.calcPlotVal(hiPrice, lowPrice);
      int numYBoxes = PnFCalc.calcNumYBoxes(hiPrice, lowPrice);
      retval = plotVal + getBoxPad(numYBoxes);
      return retval;
   }

   private float calcRevPoints(float curBox, boolean isUpReversal) {
      float retval = 0;
      float boxSize = PnFCalc.getBoxSize(curBox);
      float box = 0;
      if (isUpReversal) {
         box = curBox;
      } else {
         box = curBox - boxSize;
      }
      for (int i = 0; i < REV_SIZE; i++) {
         boxSize = PnFCalc.getBoxSize(box);
         retval += boxSize;
         if (isUpReversal) {
            box += boxSize;
         } else {
            box -= boxSize;
         }
      }
      return retval;
   }

   public static int calcNumXBoxes(int numDisplayItems) {
      int retval = 0;
      if (numDisplayItems < 100) {
         retval = (numDisplayItems * XSPAN) - XSPAN / 2;
      } else {
         retval = (numDisplayItems * XSPAN) - (XSPAN * 2);
      }
      return retval;
   }

   public static int calcXLabelRightPos(int numDisplayItems) {
      int retval = 0;
      if (numDisplayItems > 100) {
         retval = XSPAN * 3;
      } else {
         retval = XSPAN * 2;
      }
      return retval;
   }

   public static int getBoxPad(int numYBoxes) {
      int retval = 0;
      if (IS_BULLISH_PCT) {
         retval = 3;
      } else {
         if (numYBoxes < 500) {
            retval = 5;
         }
      }
      return retval;
   }

   public static int calcRowInc(float price) {
      int retval = 0;
      if (IS_BULLISH_PCT) {
         retval = 4;
      } else {
         if (price < 21) {
            retval = 1;
         } else if (price < 103) {
            retval = 2;
         } else if (price < 203) {
            retval = 4;
         } else if (price < 400) {
            retval = 8;
         } else if (price < 501) {
            retval = 10;
         } else if (price < 1001) {
            retval = 15;
         } else if (price < 2001) {
            retval = 30;
         } else {
            retval = 50;
         }
      }
      return retval;
   }

   public static float getBoxSize(float hilo) {
      float retval = 0f;
      if (IS_BULLISH_PCT) {
         retval = 2.0f;
      } else {
         if (hilo < 5) {
            retval = 0.25f;
         } else if (hilo < 20) {
            retval = 0.50f;
         } else if (hilo < 100) {
            retval = 1.0f;
         } else if (hilo < 200) {
            retval = 2.0f;
         } else if (hilo < 500) {
            retval = 4.0f;
         } else if (hilo < 1000) {
            retval = 10.0f;
         } else {
            retval = 50.0f;
         }
      }
      return retval;
   }

}
