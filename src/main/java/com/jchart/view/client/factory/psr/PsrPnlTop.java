package com.jchart.view.client.factory.psr;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import com.jchart.model.QuoteString;
import com.jchart.view.client.ChartTypeView;
import com.jchart.view.client.PnlJchart;
import com.jchart.view.client.PnlStats;
import com.jchart.view.client.TopBorderPanel;
import com.jchart.view.client.factory.PnlTopIntr;

public class PsrPnlTop
      implements PnlTopIntr, ActionListener, ItemListener, KeyListener {
   private TextField _txtTicker = new TextField(5);
   private Button _btnGo = new Button("Go");
   private Choice _chSbl = new Choice();
   private PnlStats _pnlStats;
   private PnlJchart _pnlJchart;
   private Panel _topPanel = new Panel();
   private Panel _topBorderPanel = new TopBorderPanel();
   private boolean _processingKey;
   private Button _chartTypeButton;
   private boolean _firstTime = true;
   private ChartTypeView _chartTypeView;

   public PsrPnlTop(PnlJchart pnlJchart, ChartTypeView chartTypeView,
         Choice exchangeChoice, Color borderColor, Color xyLabelColor) {
      _pnlJchart = pnlJchart;
      _chartTypeView = chartTypeView;
      _chartTypeButton = chartTypeView.getChartTypeButton();
      // add event listeners
      _btnGo.addActionListener(this);
      _txtTicker.addActionListener(this);
      _chSbl.addItemListener(this);
      _txtTicker.addKeyListener(this);
      // set colors
      _topPanel.setBackground(borderColor);
      // _topPanel.setForeground(Color.lightGray);
      _topPanel.setForeground(Color.white);
      _txtTicker.setBackground(Color.white);
      _txtTicker.setForeground(Color.black);
      _btnGo.setBackground(Color.lightGray);
      _btnGo.setForeground(Color.black);
      _chSbl.setForeground(Color.black);
      _chSbl.setBackground(Color.white);
      _chartTypeButton.setForeground(Color.black);
      _chartTypeButton.setBackground(Color.white);
      GridBagConstraints gbc = new GridBagConstraints();
      GridBagLayout gridbag = new GridBagLayout();
      gbc.insets = new Insets(3, 2, 2, 2);
      _topPanel.setLayout(gridbag);
      gbc.fill = GridBagConstraints.HORIZONTAL;
      gbc.gridx = GridBagConstraints.RELATIVE;
      gridbag.setConstraints(_txtTicker, gbc);
      _topPanel.add(_txtTicker);
      gridbag.setConstraints(_btnGo, gbc);
      _topPanel.add(_btnGo);
      gridbag.setConstraints(_chSbl, gbc);
      _topPanel.add(_chSbl);
      gridbag.setConstraints(_chartTypeButton, gbc);
      _topPanel.add(_chartTypeButton);
      _pnlStats = new PnlStats(borderColor, xyLabelColor);
      gbc.weightx = 1.0;
      gbc.gridwidth = GridBagConstraints.REMAINDER;
      gridbag.setConstraints(_pnlStats, gbc);
      _topPanel.add(_pnlStats);
      _topBorderPanel.add(_topPanel);
   }

   public void setTickerList(List tickerList) {
      if (tickerList.isEmpty()) {
         _chSbl.add("_DJI   ");
         return;
      }
      _chSbl.removeAll();
      // parse tickers
      _chSbl.setVisible(false);
      for (int i = 0; i < tickerList.size(); i++) {
         _chSbl.addItem((String) tickerList.get(i));
      }
      _chSbl.setVisible(true);
      _txtTicker.setText("");
      if (_firstTime)
         _firstTime = false;
      else
         _pnlJchart.tickerRequest((String) tickerList.get(0));

   }

   public void setLabelStatus(QuoteString quote, String cursorPos) {
      _pnlStats.setLabel(quote, cursorPos);
   }

   public void setVisible() {
      _topBorderPanel.setVisible(true);
   }

   public void setTicker(String s) {
      _txtTicker.setText(s);
   }

   public int getSbl() {
      return _chSbl.getSelectedIndex();
   }

   public Panel getPanel() {
      return _topBorderPanel;
   }

   public void setFocus() {
      _txtTicker.requestFocus();
   }

   private boolean browseList(boolean down) {
      int icount = _chSbl.getItemCount();
      int pos = _chSbl.getSelectedIndex();
      if (down) {
         pos++;
         if (pos < icount) {
            _chSbl.select(pos);
            _pnlJchart.tickerRequest(_chSbl.getSelectedItem().trim());
         } else
            return false;
      } else {
         pos--;
         if (pos >= 0) {
            _chSbl.select(pos);
            _pnlJchart.tickerRequest(_chSbl.getSelectedItem().trim());
         } else
            return false;
      }
      return true;
   }

   public void actionPerformed(ActionEvent e) {
      if (_pnlJchart.isBusy())
         return;
      _pnlJchart.waitState();
      Object obj = e.getSource();
      if ((obj == _txtTicker) || (obj == _btnGo)) {
         if (!_txtTicker.getText().trim().equals(""))
            _pnlJchart.tickerRequest(_txtTicker.getText());
         else {
            int sel = _chSbl.getSelectedIndex() + 1;
            if (sel == _chSbl.getItemCount())
               sel = 0;
            try {
               _chSbl.select(sel);
               _pnlJchart.tickerRequest(_chSbl.getSelectedItem());
            } catch (IllegalArgumentException ignore) {
               // select box not on panel
            }
         }

      }
      _pnlJchart.readyState();
   }

   // actions
   public void itemStateChanged(ItemEvent e) {
      Object obj = e.getSource();
      if (obj == _chSbl) {
         _pnlJchart.tickerRequest(_chSbl.getSelectedItem());
      }
   }

   public void keyPressed(KeyEvent e) {
      if (_processingKey)
         return;
      _processingKey = true;
      boolean indicator = true;
      int selected = -1;
      if (e.getModifiers() == MouseEvent.SHIFT_MASK) {
         indicator = false;
      }
      switch (e.getKeyCode()) {
      case KeyEvent.VK_F1:
         selected = 0;
         break;
      case KeyEvent.VK_F2:
         selected = 1;
         break;
      case KeyEvent.VK_F3:
         selected = 2;
         break;
      case KeyEvent.VK_F4:
         selected = 3;
         break;
      case KeyEvent.VK_F5:
         selected = 4;
         break;
      case KeyEvent.VK_F6:
         selected = 5;
         break;
      case KeyEvent.VK_F7:
         selected = 6;
         break;
      case KeyEvent.VK_F8:
         selected = 7;
         break;
      case KeyEvent.VK_F9:
         selected = 8;
         break;
      case KeyEvent.VK_F11:
         selected = 11;
         break;
      case KeyEvent.VK_F12:
         selected = 12;
         break;
      case KeyEvent.VK_SPACE:
         selected = -1;
         if (!_pnlJchart.getIsGetTicker())
            browseList(true);
         _txtTicker.setText("");
         break;
      }
      if (selected > -1) {
         if (e.getModifiers() == MouseEvent.SHIFT_MASK
               && e.getKeyCode() == KeyEvent.VK_F12) {
            _chartTypeView.processKeyEventIndicator(9);// ADX
         }
         if (indicator)
            if (selected < 10) {
               _chartTypeView.processKeyEventIndicator(selected);
            } else {
               if (selected == 11) {
                  _pnlJchart.getTimeFrameButtonView().zoom(false);
               } else if (selected == 12) {
                  _pnlJchart.getTimeFrameButtonView().zoom(true);
               }

            }
         else {
            if (selected < 5) {
               _chartTypeView.processKeyEventPrice(selected);
            }
         }

      }
      _processingKey = false;
   }

   public void keyTyped(KeyEvent e) {
   }

   public void keyReleased(KeyEvent e) {
   }
}
