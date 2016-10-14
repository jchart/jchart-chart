package com.jchart.view.client;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.jchart.model.ChartTypeModel;

public class DlgMA extends Dialog implements ActionListener {
    private ChartTypeModel _chartTypeModel;
    private TextField _tfMa1,_tfMa2,_tfMa3, _tfRsi;
    private FrmJchart _frmJchart;
    private Button _setButton;

    DlgMA (Frame frame, ChartTypeModel chartTypeModel) {
        super(frame, "Edit Moving Averages", true);
        _frmJchart = (FrmJchart)frame;
        _chartTypeModel = chartTypeModel;

        this.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    setVisible(false);
                }
            });
		
        //Create middle section.
        Panel p1 = new Panel();
        p1.setLayout(new GridLayout(4,1));
        Label label = new Label("MA: 1:",Label.RIGHT);
        p1.add(label);
        _tfMa1 = new TextField(_chartTypeModel.getPriceMa(0)+"",5);
        _tfMa1.addActionListener(this);
        p1.add(_tfMa1);
        label = new Label("MA: 2:",Label.RIGHT);
        p1.add(label);
        _tfMa2 = new TextField(_chartTypeModel.getPriceMa(1)+"",5);
        _tfMa2.addActionListener(this);
        p1.add(_tfMa2);
        label = new Label("MA: 3:",Label.RIGHT);
        p1.add(label);
        _tfMa3 = new TextField(_chartTypeModel.getPriceMa(2)+"",5);
        _tfMa3.addActionListener(this);
        p1.add(_tfMa3);
        label = new Label("RSI:",Label.RIGHT);
        p1.add(label);


        _tfRsi = new TextField(_chartTypeModel.getRsiMa()+"",5);
        _tfRsi.addActionListener(this);
        p1.add(_tfRsi);

        add("Center", p1);
        
        //Create bottom row.
        Panel p2 = new Panel();
        p2.setLayout(new FlowLayout(FlowLayout.RIGHT));
        Button b = new Button("Cancel");
        b.addActionListener(this);
        _setButton = new Button("Set");
        _setButton.addActionListener(this);
        p2.add(b);
        p2.add(_setButton);
        add("South", p2);
        
        //Initialize this dialog to its preferred size.
        pack();
    }
    
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        if (source == _setButton) {
            try {
            	int ma1 = Integer.parseInt(_tfMa1.getText());
            	int ma2 = Integer.parseInt(_tfMa2.getText());
            	int ma3 = Integer.parseInt(_tfMa3.getText());
            	int rsiMa = Integer.parseInt(_tfRsi.getText());
                _chartTypeModel.setPriceMa(0, ma1);
                _chartTypeModel.setPriceMa(1, ma2);
                _chartTypeModel.setPriceMa(2, ma3);
                _chartTypeModel.setRsiMa(rsiMa);
    			_chartTypeModel.chartTypeChanged(true);
    			_chartTypeModel.togglePriceMaOn();
    			_chartTypeModel.togglePriceMaOn();
                _frmJchart.getPnlJchart().plotMain();
            } catch (NumberFormatException e) {
            	//ignore;
            }
        }
		setVisible(false);
		dispose();
    }
}





