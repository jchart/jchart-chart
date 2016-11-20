package com.jchart.view.client;

import java.awt.Choice;
import java.awt.Color;

public class ExchangeChoice extends Choice {
   private final static String[] _exchangeName = { "USA", "TSX", "TSXV", "ME" };
   private final static String[] _exchangePrefix = { "", "T.", "V.", "M." };

   public ExchangeChoice() {
      setForeground(Color.black);
      setBackground(Color.white);
      add(_exchangeName[0]);
      add(_exchangeName[1]);
      add(_exchangeName[2]);
      add(_exchangeName[3]);
   }

   public String getPrefix() {
      return _exchangePrefix[getSelectedIndex()];
   }
}
