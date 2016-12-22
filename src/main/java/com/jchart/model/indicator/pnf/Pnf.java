package com.jchart.model.indicator.pnf;

import java.util.ArrayList;
import java.util.List;

import com.jchart.model.Quote;

public class Pnf {

   /** one item per for each day **/
   private List<PnfItem> _rawPnfItems;
   /** one item per for each column - alternate X's and O's **/
   private List<PnfItem> _summayPnfItems;
   /** list size <= MAX_DISPLAY_COLS **/
   private List<PnfItem> _displayPnfItems;
   private List<PnfItem> _buySellPnfItems;
   private int _numYBoxes;
   private int _numXBoxes;
   private float _pnfHi;
   private float _pnfLo;
   private int _boxPad;
   private int _xLabelRightPos;
   private static int MAX_DISPLAY_COLS = 120;

   public List<PnfItem> execute(int qListSize, Quote[] quotes) {
      _rawPnfItems = new PnFCalc().execute(qListSize, quotes);
      _summayPnfItems = createSummaryPnfItems(_rawPnfItems);
      if (_summayPnfItems.size() > 0) {
         _displayPnfItems = createDisplayPnfItems(_summayPnfItems);
         _buySellPnfItems = PnFCalc.calcBuySellPnfItems(_rawPnfItems);
         int numPnfDisplayItems = _displayPnfItems.size();
         hiLoRangePnf();
         _numYBoxes = PnFCalc.calcNumYBoxes(_pnfHi, _pnfLo);
         _numXBoxes = PnFCalc.calcNumXBoxes(numPnfDisplayItems);
         _boxPad = PnFCalc.getBoxPad(_numYBoxes);
         _xLabelRightPos = PnFCalc.calcXLabelRightPos(numPnfDisplayItems);
         // printItems(_summayPnfItems);
      }
      return _rawPnfItems;
   }

   private void printItems(List<PnfItem> pnfItems) {
      for (PnfItem pnfItem : pnfItems) {
         System.out.println(pnfItem);
      }
   }

   private List<PnfItem> createDisplayPnfItems(List<PnfItem> summayPnfItems) {
      List<PnfItem> retval = new ArrayList<PnfItem>();
      int pnfDisplayItemsSize = summayPnfItems.size();
      if (pnfDisplayItemsSize > MAX_DISPLAY_COLS) {
         int pnfDisplayStartPos = pnfDisplayItemsSize - MAX_DISPLAY_COLS;
         PnfItem pnfItem = null;
         for (int i = pnfDisplayStartPos; i < pnfDisplayItemsSize; i++) {
            pnfItem = summayPnfItems.get(i);
            retval.add(pnfItem);
         }
      } else {
         retval = summayPnfItems;
      }
      return retval;
   }

   private List<PnfItem> createSummaryPnfItems(List<PnfItem> rawPnfItems) {
      List<PnfItem> retval = new ArrayList<PnfItem>();
      PnfItem priorPnfItem = rawPnfItems.get(0);
      for (PnfItem pnfItem : rawPnfItems) {
         if (priorPnfItem.getNumBoxes() == 0) {
            priorPnfItem = pnfItem;
            continue;
         }
         if (pnfItem.isXBox() != priorPnfItem.isXBox()) {
            retval.add(priorPnfItem);
         }
         priorPnfItem = pnfItem;
      }
      // check last one
      if (retval.size() > 0) {
         PnfItem lastPnfItem = retval.get(retval.size() - 1);
         if (lastPnfItem.getNumBoxes() != priorPnfItem.getNumBoxes()) {
            if (priorPnfItem.isXBox() != lastPnfItem.isXBox()) {
               retval.add(priorPnfItem);
            } else {
               retval.set(retval.size() - 1, priorPnfItem);
            }
         }
      }
      return retval;
   }

   private void hiLoRangePnf() {
      if (PnFCalc.isBullishPct()) {
         _pnfHi = 100;
         _pnfLo = 0;
         return;
      }

      float curbox;
      _pnfHi = Float.MIN_VALUE;
      _pnfLo = Float.MAX_VALUE;
      for (PnfItem pnfItem : _displayPnfItems) {
         curbox = pnfItem.getPrice();
         if (pnfItem.isXBox()) {
            curbox = pnfItem.getPrice();
            if (curbox > _pnfHi) {
               _pnfHi = curbox;
            }
         } else {
            if (curbox < _pnfLo) {
               _pnfLo = curbox;
            }
         }
      }
   }

   public List<PnfItem> getRawPnfItems() {
      return _rawPnfItems;
   }

   public List<PnfItem> getSummaryItems() {
      return _summayPnfItems;
   }

   public List<PnfItem> getDisplayPnfItems() {
      return _displayPnfItems;
   }

   public List<PnfItem> getBuySellPnfItems() {
      return _buySellPnfItems;
   }

   public boolean isBuy() {
      boolean retval = false;
      if (_buySellPnfItems != null && _buySellPnfItems.size() > 0) {
         PnfItem pnfItem = _buySellPnfItems.get(_buySellPnfItems.size() - 1);
         retval = pnfItem.isXBox();
      }
      return retval;
   }

   public int getNumYBoxes() {
      return _numYBoxes;
   }

   public float getPnfHi() {
      return _pnfHi;
   }

   public float getPnfLo() {
      return _pnfLo;
   }

   public int getNumXBoxes() {
      return _numXBoxes;
   }

   public int getBoxPad() {
      return _boxPad;
   }

   public int getRowInc(float price) {
      return PnFCalc.calcRowInc(price);
   }

   public int getXLabelRightPos() {
      return _xLabelRightPos;
   }

   public float getBottom() {
      float retval = 0f;
      retval = PnFCalc.calcMaxYVal(_pnfHi, _pnfLo);
      return retval;
   }

   public float getPlotVal(float curPrice) {
      return PnFCalc.calcPlotVal(curPrice, _pnfLo);
   }

}
