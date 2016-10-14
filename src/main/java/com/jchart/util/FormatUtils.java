package com.jchart.util;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A class of data formatting routines<br>
 *
 */
public class FormatUtils {
   // private static DateFormat _dateFmt = new SimpleDateFormat("dd/MM/yyyy");
   private static DateFormat _dateFmt = new SimpleDateFormat("MM/dd/yyyy");

   public final static String[] monthName = { "January", "Febuary", "March",
         "April", "May", "June", "July", "August", "September", "October",
         "November", "December" };

   // constructor
   public FormatUtils() {
   }

   public static String formatDate(Date dt) {
      String retval = null;
      retval = _dateFmt.format(dt);
      return retval;
   }

   public static Date formatDate(String date) {
      Date retval = null;
      try {
         retval = _dateFmt.parse(date);
      } catch (ParseException e) {
         e.printStackTrace();
      }
      return retval;
   }

   public static String autoFormatDecimal(String s) {
      return formatDecimal(s, 0);
   }

   public static String formatDecimal(String s, int pos) {
      Double d = new Double(0.0);

      try {
         d = new Double(s);
      } catch (NumberFormatException e) {
      }

      DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
      df.setMinimumFractionDigits(pos);
      df.setMaximumFractionDigits(pos);
      df.setGroupingUsed(true);

      return df.format(d);
   }

   public String removeChar(String s, char c) {
      StringBuffer sb = new StringBuffer();

      for (int i = 0; i < s.length(); i++)
         if (s.charAt(i) != c) {
            sb.append(s.charAt(i));
         }

      return sb.toString();
   }

   public String replaceChar(String s, char oldChar, char newChar) {
      StringBuffer sb = new StringBuffer();

      for (int i = 0; i < s.length(); i++)
         if (s.charAt(i) == oldChar) {
            sb.append(newChar);
         } else {
            sb.append(s.charAt(i));
         }

      return sb.toString();
   }

   public static String formatVol(long vol) {
      Long f = new Long(vol / 1000000);
      String strVol = f.toString();
      int posDecimal = strVol.indexOf(".");

      return formatDecimal(f.toString(), 3 - posDecimal) + "M";
   }

   public static double round(double d, int precision) {
      return (double) Math.round(d * precision) / precision;
   }

}
