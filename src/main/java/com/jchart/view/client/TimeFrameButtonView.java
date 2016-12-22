package com.jchart.view.client;

import java.awt.Button;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.jchart.model.TimeFrameModel;

public class TimeFrameButtonView implements ActionListener {
   private Button _zoomButton = new Button("Zoom");
   // time frame buttons
   private Button[] _periodButton = new Button[7];

   private TimeFrameModel _timeFrameModel;
   private Panel _buttonBorderPanel = new ButtonBorderPanel();
   private PnlJchart _pnlJchart;
   private boolean _isIntraday;

   /**
    * This class implements a panel with buttons of varying timeframes both
    * end-of-day and intraday
    */
   public TimeFrameButtonView(PnlJchart pnlJchart, Button chartTypeButton,
         TimeFrameModel timeFrameModel) {
      _pnlJchart = pnlJchart;
      _timeFrameModel = timeFrameModel;
      _isIntraday = _timeFrameModel.isIntraday();
      int numPeriodButtons = TimeFrameModel.getNumMixedPeriods();
      setPeriodButtons(numPeriodButtons);
      setButtonPanel(numPeriodButtons);
   }

   private void setPeriodButtons(int numPeriodButtons) {
      _periodButton = new Button[numPeriodButtons];
      for (int i = 0; i < numPeriodButtons; i++) {
         _periodButton[i] = new Button();
         _periodButton[i].setLabel(TimeFrameModel.getMixedText(i));
         _periodButton[i].addActionListener(this);
         _periodButton[i].setBackground(Color.lightGray);
      }
      _periodButton[0].setEnabled(false);

   }

   private void setButtonPanel(int numPeriodButtons) {
      GridBagConstraints gbc = new GridBagConstraints();
      GridBagLayout gridbag = new GridBagLayout();
      _buttonBorderPanel.setLayout(gridbag);
      gbc.fill = GridBagConstraints.HORIZONTAL;
      gbc.gridx = GridBagConstraints.RELATIVE;
      int i = 0;
      for (; i < numPeriodButtons - 1; i++) {
         gridbag.setConstraints(_periodButton[i], gbc);
         _buttonBorderPanel.add(_periodButton[i]);
      }
      gridbag.setConstraints(_periodButton[i], gbc);
      _buttonBorderPanel.add(_periodButton[i]);
      _periodButton[_timeFrameModel.getCurPeriod()]
            .setBackground(Color.decode("#f1f1f1"));
      _zoomButton.setBackground(Color.decode("#f1f1f1"));
      _zoomButton.addActionListener(this);
      gbc.weightx = 1.0;
      gridbag.setConstraints(_zoomButton, gbc);
      _buttonBorderPanel.add(_zoomButton);
   }

   public Panel getPanel() {
      return _buttonBorderPanel;
   }

   private void setButtonTextFull(int curPeriod, boolean zoomIn) {
      _periodButton[curPeriod].setBackground(Color.decode("#f1f1f1"));
      int resetColor = 0;
      if (zoomIn) {
         resetColor = curPeriod + 1;
      } else {
         resetColor = curPeriod - 1;
      }
      if (resetColor == _periodButton.length)
         resetColor = 1;
      if (resetColor == 0)
         resetColor = _periodButton.length - 1;
      _periodButton[resetColor].setBackground(Color.lightGray);
   }

   public void actionPerformed(ActionEvent e) {
      if (_pnlJchart.isBusy())
         return;
      _pnlJchart.waitState();
      Object obj = e.getSource();
      boolean zoomIn = false;

      int curPeriod = _timeFrameModel.getCurPeriod();
      // zoom
      if (obj == _zoomButton) {
         zoomIn = true;
         zoom(zoomIn);
         return;

      } else {
         _timeFrameModel.setIntraday(false);
         for (int i = 0; i < _periodButton.length; i++) {
            if (obj == _periodButton[i]) {
               curPeriod = i;
               setButtonTextFull(curPeriod, zoomIn);
            } else {
               _periodButton[i].setBackground(Color.lightGray);
            }
         }
      }
      if (curPeriod == TimeFrameModel.PERIOD_INTRADAY) {
         setIntraday(curPeriod);
      } else {
         setEod(curPeriod);
      }
      _pnlJchart.calcPlotRange();
      _pnlJchart.plotMain();
      _pnlJchart.readyState();
   }

   public void setEod(int curPeriod) {
      _timeFrameModel.setCurPeriod(curPeriod);
      _pnlJchart.plotEod(_isIntraday);
      _isIntraday = false;

   }

   public void setIntraday(int curPeriod) {
      _isIntraday = true;
      _timeFrameModel.setCurPeriod(curPeriod);
      _pnlJchart.plotIntraday();
   }

   /**
    * @param b
    */
   public void zoom(boolean zoomIn) {
      int curPeriod = _timeFrameModel.getCurPeriod();
      if (curPeriod < 1)
         curPeriod = _periodButton.length - 1;

      if (_timeFrameModel.isIntraday()) {
         if (curPeriod > _periodButton.length - 2)
            curPeriod = _periodButton.length - 2;

      } else {
         if (zoomIn) {
            if (curPeriod == 1) {
               curPeriod = _periodButton.length;
            }
         }
      }

      if (zoomIn) {
         curPeriod--;
      } else {
         curPeriod++;

      }
      if (curPeriod == _periodButton.length) {
         curPeriod = 1;
      }
      _timeFrameModel.setCurPeriod(curPeriod);
      if (!_timeFrameModel.isIntraday()) {
         setButtonTextFull(curPeriod, zoomIn);
      }
      if (curPeriod == TimeFrameModel.PERIOD_INTRADAY) {
         setIntraday(curPeriod);
      } else {
         setEod(curPeriod);
      }
      _pnlJchart.calcPlotRange();
      _pnlJchart.plotMain();
      _pnlJchart.readyState();
   }
}
