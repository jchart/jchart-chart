package com.jchart.view.common;

public interface PnlJchartIntr {
   public boolean isBusy();

   public void chartTypeChanged();

   public void sblChanged(String sbl);

   public int getListno(int xval);

   public void plotZoom(int beginPlotRec, int endPlotRec);

   public void plotMain();
}
