package com.jchart.view.client.factory;

import java.awt.Color;

import com.jchart.view.client.ChartTypeView;
import com.jchart.view.client.ExchangeChoice;
import com.jchart.view.client.PnlJchart;

public interface GuiFactoryIntr {
   public PnlTopIntr getPnlTop(PnlJchart pnlJchart, ChartTypeView chartTypeView,
         ExchangeChoice exchangeChoice, Color borderColor, Color xyLabelColor);
}
