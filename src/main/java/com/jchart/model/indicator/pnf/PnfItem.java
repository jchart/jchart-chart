/**
 * 
 */
package com.jchart.model.indicator.pnf;

/**
 * @author <a href ="paul.russo@jchart.com">Paul Russo</a>
 * @created Nov 1, 2010
 */
public class PnfItem {

   /**
    * numBoxes is how many boxes to get to current price level
    */
   private float _price;
   private String _date;
   private int _numBoxes;

   public float getPrice() {
      return _price;
   }

   public void setPrice(float price) {
      _price = price;
   }

   public String getDate() {
      return _date;
   }

   public void setDate(String date) {
      _date = date;
   }

   public int getNumBoxes() {
      return _numBoxes;
   }

   public void setNumBoxes(int numBoxes) {
      _numBoxes = numBoxes;
   }

   public boolean isXBox() {
      return _numBoxes >= 0;
   }

   public String toString() {
      StringBuffer retval = new StringBuffer();
      retval.append(_date).append(",").append(_numBoxes).append(",")
            .append(_price);
      return retval.toString();
   }
}
