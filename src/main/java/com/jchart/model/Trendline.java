package com.jchart.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import com.jchart.stats.impl.Correlation;
import com.jchart.stats.impl.Regression;
import com.jchart.stats.impl.StatsBean;
import com.jchart.view.common.Graphport;

public class Trendline {
   // integer points
   private Point _pointAi;
   private Point _pointBi;
   // float points
   private Fpoint _pointAf;
   private Fpoint _pointBf;
   // line description
   private float _yIntercept = 0;
   private float _slope = 0;
   private Graphport _graphport;

   // constructors
   public Trendline(Point pointAi, Point pointBi, Graphport graphport) {
      _graphport = graphport;
      update(pointAi, pointBi);
   }

   public void update(Point pointAi, Point pointBi) {
      _pointAi = new Point(pointAi);
      _pointBi = new Point(pointBi);
      _pointAf = inversePoint(_pointAi);
      _pointBf = inversePoint(_pointBi);
      calcSlope();
   }

   public Point getPointAi() {
      return _pointAi;
   }

   public Point getPointBi() {
      return _pointBi;
   }

   public Fpoint getPointAf() {
      return _pointAf;
   }

   public Fpoint getPointBf() {
      return _pointBf;
   }

   public boolean onTrendLine(Point p) {
      // check bounds
      if ((p.x < _pointAi.x) || (p.x > _pointBi.x))
         return false;
      // check _slope
      float y = ((_slope * p.x) + _yIntercept);
      if ((p.y <= y + 4) && (p.y >= y - 4))
         return true;
      else
         return false;
   }

   private void calcSlope() {
      if ((_pointBi.x - _pointAi.x) == 0)
         return;
      try {
         float numer = _pointBi.y - _pointAi.y;
         float denom = _pointBi.x - _pointAi.x;
         _slope = (numer / denom);
      } catch (Exception e) {
         return;
      }
      _yIntercept = ((_pointAi.y + _pointBi.y) / 2)
            - (_slope * ((_pointAi.x + _pointBi.x) / 2));
   }

   /**
    * regress trendline Y = a + bX regression line
    * 
    * b = slope a = yIntercept
    */
   public void regress(Quote[] quotes, PlotRange plotRange) {
      float xStartf = _pointAf.x;
      float xEndf = _pointBf.x;
      if (xStartf > xEndf) {
         float temp = xStartf;
         xStartf = xEndf;
         xEndf = temp;
      }

      double d = Math.ceil(xStartf / 2);
      double d2 = Math.ceil(xEndf / 2);
      int xStart = (int) Math.round(d) - 1;
      int xEnd = (int) Math.round(d2) - 1;
      xStart += plotRange.getBeginPlotRec();
      xEnd += plotRange.getBeginPlotRec();

      if (xEnd > plotRange.getEndPlotRec() - 1) {
         xEnd = plotRange.getEndPlotRec() - 1;
      }
      int numRecs = xEnd - xStart + 1;
      List<Double> xVals = new ArrayList<Double>();
      List<Double> yVals = new ArrayList<Double>();
      int x = 0;
      for (int i = xStart; i < xEnd + 1; i++) {
         yVals.add(new Double(quotes[i].getClose()));
         xVals.add(new Double(x + 1));
         x++;
      }
      Correlation correlation = new Correlation();
      StatsBean statsBean = correlation.calc(xVals, yVals);
      Regression regression = new Regression();
      regression.execute(statsBean);
      double slope = statsBean.getSlope();
      double yIntercept = statsBean.getYintercept();
      System.out.println(statsBean);

      _pointAf.y = (float) (yIntercept + (slope * 1));
      _pointBf.y = (float) (yIntercept + (slope * numRecs));
      _pointAi.y = _graphport.transformY(_pointAf.y);
      _pointBi.y = _graphport.transformY(_pointBf.y);
      calcSlope();
   }

   /**
    * @param quotes
    * @param plotRange
    */
   public void horizontal(PlotRange plotRange) {
      int endX = plotRange.getEndPlotRec() - 1;
      Point p = new Point();
      p.x = endX;
      p.y = _pointAi.y;
      _pointBf = inversePoint(p);
      _pointBi = p;
   }

   public Fpoint inversePoint(Point p) {
      Fpoint fPoint = new Fpoint();
      fPoint.x = _graphport.inverseX(p.x);
      fPoint.y = _graphport.inverseY(p.y);
      return fPoint;
   }

}
