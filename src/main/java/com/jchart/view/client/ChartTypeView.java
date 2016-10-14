package com.jchart.view.client;

import java.awt.Button;
import java.awt.Color;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.jchart.model.ChartOption;
import com.jchart.model.ChartTypeModel;
import com.jchart.view.common.PnlJchartIntr;

public class ChartTypeView implements ActionListener {
   private PnlJchartIntr _pnlJchart;
   private ChartOption _chartOption;
   private ChartTypeModel _chartTypeModel;
   private Button _chartOptionButton = new Button("Options");
   private Button _chartOptionButtonL2 = new Button("Options");
   private PopupMenu _chartOptionPopupMenuL2 = new PopupMenu("Options");
   private PopupMenu _chartOptionPopupMenu = new PopupMenu("Options");
   private boolean _processingKey;

   public ChartTypeView(PnlJchartIntr pnlJchart,
         ChartTypeModel chartTypeModel) {
      _pnlJchart = pnlJchart;
      _chartTypeModel = chartTypeModel;
      _chartOptionButton.addActionListener(this);
      _chartOptionPopupMenuL2.addActionListener(this);
      _chartOptionButtonL2.addActionListener(this);
      _chartOptionButton.setForeground(Color.black);
      _chartOptionButton.setBackground(Color.white);
      _chartOptionButtonL2.setForeground(Color.black);
      _chartOptionButtonL2.setBackground(Color.white);

      Menu priceMenu = new Menu("Price Charts ");
      Menu indicatorMenu = new Menu("Indicators");
      Menu priceMenuL2 = new Menu("Price Charts");
      Menu indicatorMenuL2 = new Menu("Indicators");
      _chartOptionPopupMenu.add(priceMenu);
      _chartOptionPopupMenu.add(indicatorMenu);
      _chartOptionPopupMenuL2.add(priceMenuL2);
      _chartOptionPopupMenuL2.add(indicatorMenuL2);

      for (int i = 0; i < ChartOption.PRICE_OPTIONS_LIST.size(); i++) {
         MenuItem chartOptionMenuItem = getChartOptionMenuItem(
               ChartOption.getPriceName(i));
         MenuItem chartOptionMenuItemL2 = getChartOptionMenuItem(
               ChartOption.getPriceName(i));
         priceMenu.add(chartOptionMenuItem);
         priceMenuL2.add(chartOptionMenuItemL2);
      }
      for (int i = 0; i < ChartOption.INDICATOR_OPTIONS_LIST.size(); i++) {
         MenuItem chartOptionMenuItem = getChartOptionMenuItem(
               ChartOption.getIndicatorName(i));
         MenuItem chartOptionMenuItemL2 = getChartOptionMenuItem(
               ChartOption.getIndicatorName(i));
         indicatorMenu.add(chartOptionMenuItem);
         indicatorMenuL2.add(chartOptionMenuItemL2);
      }
      for (int i = 0; i < ChartOption.OTHER_OPTIONS_LIST.size(); i++) {
         MenuItem chartOptionMenuItem = getChartOptionMenuItem(
               ChartOption.getOtherName(i));
         MenuItem chartOptionMenuItemL2 = getChartOptionMenuItem(
               ChartOption.getOtherName(i));
         _chartOptionPopupMenu.add(chartOptionMenuItem);
         _chartOptionPopupMenuL2.add(chartOptionMenuItemL2);
      }
      _chartOptionButton.add(_chartOptionPopupMenu);
      _chartOptionButtonL2.add(_chartOptionPopupMenuL2);
   }

   private MenuItem getChartOptionMenuItem(String optionName) {
      MenuItem mi = new MenuItem(optionName);
      mi.setActionCommand(optionName);
      mi.addActionListener(this);
      return mi;
   }

   public void setChartOption(ChartOption chartOption) {
      _chartOption = chartOption;
   }

   public ChartOption getChartOption() {
      return _chartOption;
   }

   public Button getChartTypeButton() {
      return _chartOptionButton;
   }

   public Button getChartTypeButtonL2() {
      return _chartOptionButtonL2;
   }

   public void actionPerformed(ActionEvent e) {
      if (_pnlJchart.isBusy()) {
         return;
      }
      Object obj = e.getSource();
      if (obj instanceof MenuItem) {
         String chartOptionName = e.getActionCommand();
         _chartTypeModel
               .setChartOption(ChartOption.getChartOption(chartOptionName));
         _pnlJchart.chartTypeChanged();
      } else if (obj.equals(_chartOptionButton)) {
         _chartOptionPopupMenu.show(_chartOptionButton, 1, 1);
      } else if (obj.equals(_chartOptionButtonL2)) {
         _chartOptionPopupMenuL2.show(_chartOptionButtonL2, 1, 1);
      }
   }

   public void processKeyEventIndicator(int selected) {
      String chartOptionName = ChartOption.getIndicatorName(selected);
      processKeyEvent(selected, chartOptionName);
   }

   public void processKeyEventPrice(int selected) {
      String chartOptionName = ChartOption.getPriceName(selected);
      processKeyEvent(selected, chartOptionName);
   }

   /**
    * @param selected
    */
   private void processKeyEvent(int selected, String chartOptionName) {
      if (_processingKey) {
         return;
      }
      _processingKey = true;
      _chartTypeModel
            .setChartOption(ChartOption.getChartOption(chartOptionName));
      _pnlJchart.chartTypeChanged();
      _processingKey = false;
   }
}
