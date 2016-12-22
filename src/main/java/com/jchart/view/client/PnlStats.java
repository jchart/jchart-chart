package com.jchart.view.client;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;

import com.jchart.model.QuoteString;
import com.jchart.util.FormatUtils;

public class PnlStats extends Panel {
   private static final long serialVersionUID = 1L;
   private Label lblDate = new Label();
   private Label lblLast = new Label();
   private Label lblChg = new Label();
   private Label lblHigh = new Label();
   private Label lblLow = new Label();
   private Label lblVol = new Label();
   private Label lblCursorPos = new Label();

   // constructor
   public PnlStats(Color borderColor, Color xyLabelColor) {
      setLayout(new GridLayout(1, 5));
      setBackground(borderColor);
      setForeground(xyLabelColor);
      setFont(new Font("arial", Font.PLAIN, 12));
      add(lblDate);
      add(lblLast);
      add(lblVol);
      add(lblChg);
      add(lblCursorPos);
   }

   public void setLabel(QuoteString quote, String cursorPos) {
      lblDate.setText(quote.dt);
      lblLast.setText(quote.cl);
      lblChg.setText(quote.chg);
      lblHigh.setText(quote.hi);
      lblLow.setText(quote.lo);
      lblVol.setText("Vol: " + FormatUtils.formatVol(quote.getVolume()) + " ");
      lblCursorPos.setText("Y:" + cursorPos);
   }

}
