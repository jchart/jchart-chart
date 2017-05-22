package com.jchart;

import java.util.List;
import java.util.Properties;

import com.jchart.applet.AppletParamAccessor;
import com.jchart.io.IoJchartBase;
import com.jchart.io.factory.IoFactory;
import com.jchart.model.ConameFactory;
import com.jchart.model.InitParmParser;
import com.jchart.model.JchartComposite;
import com.jchart.model.JchartModelFacade;
import com.jchart.model.QuoteDataModel;
import com.jchart.model.color.ColorFactory;
import com.jchart.model.color.ColorScheme;
import com.jchart.view.client.factory.GuiFactory;

/**
 * Jchart - Financial Technical Analysis Software Author: Paul Russo
 * CreationvDate: Jan 8, 2000
 */
public class JchartInitializer {
   private JchartComposite _jchartComposite = new JchartComposite();
   private InitParmParser _initParmParser = new InitParmParser();
   private Properties _jchartProps = null;
   private static final String JCHART_PROPS = "jchart.properties";
   private boolean _isApplet = false;

   public void initApplet(AppletParamAccessor appletParamAccessor) {
      _isApplet = true;
      _initParmParser.setAppletParamAccessor(appletParamAccessor);
      init();
   }

   public void init(String args[]) {
      _initParmParser.loadCmdLineArgs(args);
      init();
   }

   private void init() {
      try {
         if (_jchartProps == null) {
            _jchartProps = IoJchartBase.loadProperties(getClass(),
                  JCHART_PROPS);
         }
      } catch (Exception e) {
         System.out.println(e + "\nMissing " + getClass() + "." + JCHART_PROPS);
         destroy();
         return;
      }
      JchartModelFacade.setJchartProps(_jchartProps);

      try {
         GuiFactory.init(_jchartProps.getProperty("GuiFactory"));
         if (_isApplet) {
            IoFactory.init(_jchartProps.getProperty("IoFactoryWeb"));
         } else {
            IoFactory.init(_jchartProps.getProperty("IoFactory.class"));
         }
      } catch (Exception e) {
         throw new RuntimeException(e);
      }

      System.out.println("** Jchart Startup - Version "
            + _jchartProps.getProperty("Version") + " **");
      _initParmParser.init();
      new ConameFactory().init();
      _initParmParser.parse(_jchartComposite);
      List<String> tickerList = _initParmParser.getTickerList();
      if (tickerList.isEmpty()) {
         _initParmParser.loadTickers();
      }
      _jchartComposite.setQuoteDataModel(new QuoteDataModel());

      String colorSchemeName = _jchartProps.getProperty("ColorScheme");
      ColorScheme colorScheme = ColorFactory.getColorScheme(colorSchemeName);
      _jchartComposite.setColorScheme(colorScheme);
      JchartModelFacade.init(_jchartComposite);
   }

   public JchartComposite getJchartComposite() {
      return _jchartComposite;
   }

   public InitParmParser getInitParmParser() {
      return _initParmParser;
   }

   public void destroy() {
      _jchartComposite.getColorScheme().destroy();
      System.out.println("Jchart exit");
   }

}
