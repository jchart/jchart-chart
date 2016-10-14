package com.jchart.model;

public class PlotRange implements java.io.Serializable {
   private int _beginPlotRec;
   private int _endPlotRec;
   private int _numPlotRecs;

   public int getBeginPlotRec() {
      return _beginPlotRec;
   }

   public int getEndPlotRec() {
      return _endPlotRec;
   }

   public int getNumPlotRecs() {
      return _numPlotRecs;
   }

   public void calcPlotRange(int numPlotRecs, int numQuotes) {
      _numPlotRecs = numPlotRecs;
      _endPlotRec = numQuotes;
      _beginPlotRec = _endPlotRec - _numPlotRecs;
   }

   public void calcPlotRangeZoom(int beginPlotRec, int endPlotRec) {
      _beginPlotRec = beginPlotRec;
      _endPlotRec = endPlotRec;
      _numPlotRecs = _endPlotRec - _beginPlotRec;
   }

   public void calcBack() {
      _beginPlotRec -= _numPlotRecs;
      _endPlotRec -= _numPlotRecs;
      if (_beginPlotRec < 0) {
         _beginPlotRec = 0;
         _endPlotRec = _numPlotRecs;
      }
   }

   public void calcForward() {
      _beginPlotRec += _numPlotRecs;
      _endPlotRec += _numPlotRecs;
   }

   public String toString() {
      return "\n_numPlotRecs: " + _numPlotRecs + "\n_beginPlotRec: "
            + _beginPlotRec + "\n_endPlotRec: " + _endPlotRec;
   }
}