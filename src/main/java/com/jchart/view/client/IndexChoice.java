package com.jchart.view.client;

import java.awt.Choice;
import java.awt.Color;

public class IndexChoice {
    private Choice _usChoice = new Choice();
    private Choice _canadianChoice = new Choice();
	private final static String[] _usSbl   = {"",".DJI",".SPX","COMP","NYA"};
	private final static String[] _usName = {"   US INDICIES  ","DJ INDUSTRIALS IND","S&P 500","NASDAQ COMP","NYSE COMP"};
    private final static String[] _canadianSbl = {"","IT.TTIN","IT.TX40","IT.TSXCI","IT.TTTK"};
    private final static String[] _canadianName = {"CANADIA INDICIES","TSX CDA  INDU","TSX CDA MIDCAP","TSX COMP","TSX CDA INFO TECH"};

	public IndexChoice () {
		_usChoice.setForeground(Color.black);
		_usChoice.setBackground(Color.white);
		_canadianChoice.setForeground(Color.black);
		_canadianChoice.setBackground(Color.white);
        for (int i=0;i<_usName.length;i++) {
            _usChoice.add(_usName[i]);
        }
        for (int i=0;i<_canadianName.length;i++) {
            _canadianChoice.add(_canadianName[i]);
        }
    }
    public Choice getUsIndexChoice() {
    		return _usChoice;
    }
    public Choice getCanadiaIndexChoice() {
    	return _canadianChoice;
    }
	public String getUsSbl() {
			return _usSbl[_usChoice.getSelectedIndex()];
	}
	public String getCanadianSbl() {
			return _canadianSbl[_canadianChoice.getSelectedIndex()];
	}
	public boolean isCanadian(String sbl) {
			return sbl.startsWith("IT.");
	}

}
