package com.jchart.view.client.factory.psr;
import java.awt.Color;

import com.jchart.view.client.ChartTypeView;
import com.jchart.view.client.ExchangeChoice;
import com.jchart.view.client.PnlJchart;
import com.jchart.view.client.factory.PnlTopIntr;

public class PsrGuiFactory implements java.io.Serializable,
				      com.jchart.view.client.factory.GuiFactoryIntr {
    public PnlTopIntr getPnlTop(PnlJchart pnlJchart, ChartTypeView chartTypeView, ExchangeChoice exchangeChoice,
                                Color borderColor, Color xyLabelColor) {
        return new PsrPnlTop(pnlJchart,chartTypeView,exchangeChoice,borderColor,xyLabelColor);
    }
}
