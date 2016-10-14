package com.jchart;

import com.jchart.model.JchartComposite;

public interface JchartIntr {
   public boolean getIsApplet();

   public void destroy();

   public void killFrame();

   public String getLastTrade();

   public void visitPrintUrl();

   public boolean isShowing();

   public JchartComposite getJchartComposite();
}
