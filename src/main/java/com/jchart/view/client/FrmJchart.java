package com.jchart.view.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.jchart.JchartFrameDestroyer;
import com.jchart.io.factory.IoFactory;
import com.jchart.model.JchartComposite;
import com.jchart.model.color.ColorScheme;

public class FrmJchart extends Frame implements ActionListener {
   private static final long serialVersionUID = 1L;
   // private InitParmParser _initParmParser;
   private static final int INITIAL_WIDTH = 650;
   private static final int INITIAL_HEIGHT = 400;
   private static String TITLE = "Jchart Interactive";
   private PnlJchart _pnlJchart;
   private MenuItem _mnuExit;
   private MenuItem _mnuOpen;
   private MenuItem _mnuMA;
   private MenuItem[] _mnuColor;
   private ColorScheme _colorScheme;
   private String _tickerFile;
   private String _tickerDir;
   private JchartComposite _jchartComposite;
   private JchartFrameDestroyer _jchartDestroyer;

   public FrmJchart(PnlJchart pnlJchart, JchartComposite jchartComposite,
         JchartFrameDestroyer jchartDestroyer) {
      super();
      _jchartDestroyer = jchartDestroyer;
      _jchartComposite = jchartComposite;
      String title = TITLE;
      /*
       * if (initParmParser.getTickerListName() != null) { title += " - " +
       * initParmParser.getTickerListName(); }
       */ setTitle(title);
      // _initParmParser = initParmParser;
      _pnlJchart = pnlJchart;
      _colorScheme = _jchartComposite.getColorScheme();
      // position frame
      setSize(INITIAL_WIDTH, INITIAL_HEIGHT);

      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

      setLocation((screenSize.width / 2) - (INITIAL_WIDTH / 2),
            (screenSize.height / 2) - (INITIAL_HEIGHT / 2));
      setLayout(new BorderLayout());

      this.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent e) {
            destroy();
         }
      });

      _mnuColor = new MenuItem[_colorScheme.getNumColors()];

      // create the menu bar
      MenuBar menuBar = new MenuBar();

      // add the File menu with its items
      Menu menu = new Menu("File");
      _mnuOpen = new MenuItem("Open");
      _mnuExit = new MenuItem("Exit");
      _mnuExit.addActionListener(this);
      _mnuOpen.addActionListener(this);
      menu.add(_mnuOpen);
      menu.add(_mnuExit);
      menuBar.add(menu);

      // edit Menu
      menu = new Menu("Edit");
      _mnuMA = new MenuItem("Moving Averages");
      _mnuMA.addActionListener(this);
      menu.add(_mnuMA);

      // color selection
      Menu mnuEditColor = new Menu("Color");

      for (int i = 0; i < _colorScheme.getNumColors(); i++) {
         _mnuColor[i] = new MenuItem(_colorScheme.getColorName(i));
         _mnuColor[i].addActionListener(this);
         mnuEditColor.add(_mnuColor[i]);
      }

      menu.add(mnuEditColor);
      menuBar.add(menu);

      // Add the panel
      setMenuBar(menuBar);
      add(BorderLayout.CENTER, _pnlJchart); // add after init()
   }

   public void setTickerDir(String tickerDir) {
      _tickerDir = tickerDir;

   }

   private void destroy() {
      _jchartDestroyer.destroyFrame();
      // if (_jchartApplet != null) {
      // _jchartApplet.dock();

      // return;
      // }
      /*
       * if (_jchart.getIsApplet()) { _jchart.killFrame(); } else {
       * _jchart.killFrame(); _jchart.destroy(); }
       */ // write _tickerFile location
   }

   // events section
   public void actionPerformed(ActionEvent e) {
      Object obj = e.getSource();

      if (obj == _mnuExit) {
         destroy();
      } else if (obj == _mnuOpen) {
         FileDialog fd = new FileDialog(this, "Load a Ticker File",
               FileDialog.LOAD);

         fd.setDirectory(_tickerDir);
         fd.setVisible(true);

         if (fd.getFile() != null) {
            _tickerFile = "file:///" + fd.getDirectory() + fd.getFile();
            java.util.List tickerList = loadTickerList(_tickerFile);
            _pnlJchart.setTickerList(tickerList);
            setTitle(TITLE + " - " + fd.getFile());
         }
      } else if (obj == _mnuMA) {
         maDialog();
      } else {
         for (int i = 0; i < _colorScheme.getNumColors(); i++) {
            if (obj == _mnuColor[i]) {
               _colorScheme.setColor(i, i);
               _pnlJchart.plotMain();

               break;
            }
         }
      }
   }

   private java.util.List<String> loadTickerList(String tickerUrl) {
      java.util.List<String> tickerList = null;
      try {
         tickerList = IoFactory.getTickerList(tickerUrl);
      } catch (Exception e) {
         System.out.println(
               "error in loadTickers() " + tickerUrl + "\n" + e.toString());
      }

      return tickerList;
   }

   private void maDialog() {
      DlgMA dlgMA = new DlgMA(this, _jchartComposite.getChartTypeModel());
      dlgMA.setVisible(true);
   }

   public void setIsGetTicker(boolean b) {
      _pnlJchart.setIsGetTicker(b);
   }

   public PnlJchart getPnlJchart() {
      return _pnlJchart;
   }
}
