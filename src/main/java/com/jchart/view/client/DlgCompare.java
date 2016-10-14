package com.jchart.view.client;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.jchart.JchartIntr;
import com.jchart.io.TickerListener;
import com.jchart.io.TickerRequest;
import com.jchart.model.JchartComposite;


public class DlgCompare extends Dialog implements ActionListener, ItemListener, TickerListener {
    private static final Insets insets = new Insets(20, 5, 20, 10);
    private TextField _tfBaseSbl;
    private TextField _tfRsSbl;
    private Button _okButton;
    private ExchangeChoice _exchangeBaseChoice = new ExchangeChoice();
    private ExchangeChoice _exchangeRsChoice = new ExchangeChoice();
    private IndexChoice _indexChoice = new IndexChoice();
    private Choice _usIndexChoice;
    private Choice _canadiaIndexChoice;
	private Frame _frame;
	private JchartComposite _jchartComposite;

    DlgCompare(Frame frame, JchartComposite jchartComposite) {
        super(frame, "Compare", true);
        _frame = frame;
        _jchartComposite = jchartComposite;

        this.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    setVisible(false);
                }
            });

		/*********** panel components ********************/
		//incidie drop downs
		_usIndexChoice = _indexChoice.getUsIndexChoice();
		_canadiaIndexChoice = _indexChoice.getCanadiaIndexChoice();
		_usIndexChoice.addItemListener(this);
		_canadiaIndexChoice.addItemListener(this);

		//base ticker
		_tfBaseSbl = new TextField(_jchartComposite.getQuoteDataModel().getTicker(), 7);
		_tfBaseSbl.addActionListener(this);
		_tfBaseSbl.setForeground(Color.black);
		_tfBaseSbl.setBackground(Color.white);
		//rs ticker
		_tfRsSbl = new TextField(_jchartComposite.getQuoteDataModel().getCompareTicker(), 7);
		_tfRsSbl.addActionListener(this);
		_tfRsSbl.setForeground(Color.black);
		_tfRsSbl.setBackground(Color.white);
		//buttons
		Button cancelButton = new Button("Cancel");
		cancelButton.addActionListener(this);
		_okButton = new Button("OK");
		_okButton.addActionListener(this);
		//labels
		Label l1 = new Label("Compare:", Label.RIGHT);
		Label l2 = new Label("To:", Label.RIGHT);
		Label l3 = new Label("To:", Label.RIGHT);
		Label l5 = new Label("");
		Label l6 = new Label("   Enter a symbol or select an index");
		Label l7 = new Label("");
		Label l8 = new Label("");

		Panel p = new Panel();
		GridBagConstraints gbc = new GridBagConstraints();
		GridBagLayout gridbag = new GridBagLayout();
		gbc.insets = new Insets(0,0,0,0);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = GridBagConstraints.RELATIVE;
		gbc.gridy = 1;
		p.setLayout(gridbag);
		gridbag.setConstraints(l5,gbc);
		p.add(l5);

		gbc.gridy++;
		gridbag.setConstraints(l1,gbc);
		p.add(l1);
		gridbag.setConstraints(_tfBaseSbl,gbc);
		p.add(_tfBaseSbl);
		gridbag.setConstraints(_exchangeBaseChoice,gbc);
		p.add(_exchangeBaseChoice);

		gbc.gridy ++;
		gbc.gridwidth = 3;
		gridbag.setConstraints(l6,gbc);
		p.add(l6);
		gbc.gridwidth = 1;

		gbc.gridy ++;
		gridbag.setConstraints(l2,gbc);
		p.add(l2);
		gridbag.setConstraints(_tfRsSbl,gbc);
      	p.add(_tfRsSbl);
		gridbag.setConstraints(_exchangeRsChoice,gbc);
		p.add(_exchangeRsChoice);
		
		gbc.gridy++;
		gridbag.setConstraints(l3,gbc);
		p.add(l3);
		gbc.gridwidth = 2;
		gridbag.setConstraints(_usIndexChoice,gbc);
		p.add(_usIndexChoice);
		gbc.gridwidth = 1;

		gbc.gridy++;
		gbc.gridwidth = 1;

		Panel p1 = new Panel();
		p1.setLayout(new GridLayout(1, 2));
		p1.add(cancelButton);
		p1.add(_okButton);
		
		gridbag.setConstraints(l7,gbc);
		p.add(l7);
		gbc.gridwidth = 2;
		gridbag.setConstraints(p1,gbc);
		p.add(p1);

		gbc.gridwidth = 1;			
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.gridy ++;
		gridbag.setConstraints(l8,gbc);
		p.add(l8);
		
        add("Center", p);
        pack();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension d = getSize();
        setLocation((screenSize.width / 2) - ((int) d.width / 2),
            (screenSize.height / 2) - ((int) d.height / 2));
    }

    public Insets getInsets() {
        return insets;
    }

    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();

        if ((source == _okButton) || source instanceof TextField) {
        	String baseTicker = "";
        	String rsSbl = "";
        	if (_indexChoice.isCanadian(_tfBaseSbl.getText()))
        		baseTicker = _tfBaseSbl.getText();
        	else 	
				baseTicker = _exchangeBaseChoice.getPrefix() + _tfBaseSbl.getText();
			if (_indexChoice.isCanadian(_tfRsSbl.getText()))
				rsSbl = _tfRsSbl.getText();
			else 
				rsSbl = _exchangeRsChoice.getPrefix() + _tfRsSbl.getText();
			if (baseTicker.trim().length() > 0 && rsSbl.trim().length() > 0) {	
				TickerRequest tickerRequest = new TickerRequest(_jchartComposite,this);
				tickerRequest.requestCompare(baseTicker,rsSbl);
	
			}
        }
		setVisible(false);
		dispose();
		_frame.dispose();
    }

    public void itemStateChanged(ItemEvent event) {
        Object source = event.getSource();
		String ind = "";
		if (source == _usIndexChoice) {
			ind = _indexChoice.getUsSbl();
		} else if (source == _canadiaIndexChoice) {
			ind = _indexChoice.getCanadianSbl();
		}
		if (_tfBaseSbl.getText().trim().length() == 0) {
			if (source == _canadiaIndexChoice)
				_exchangeBaseChoice.select(1);
			else
				_exchangeBaseChoice.select(0);
			_tfBaseSbl.setText(ind);
		} else {
			if (source == _canadiaIndexChoice)
				_exchangeRsChoice.select(1);
			else
				_exchangeRsChoice.select(0);
			_tfRsSbl.setText(ind);
		}
    }

	public void requestComplete(boolean found) {
	}
    
}
