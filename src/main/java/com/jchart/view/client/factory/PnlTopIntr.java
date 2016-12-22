package com.jchart.view.client.factory;

import java.awt.Panel;
import java.util.List;

import com.jchart.model.QuoteString;

public interface PnlTopIntr {
   public void setLabelStatus(QuoteString quote, String cursorPos);

   public void setVisible();

   public void setTicker(String s);

   public void setTickerList(List tickerList);

   public int getSbl();

   public Panel getPanel();

   public void setFocus();
}
