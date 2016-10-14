package com.jchart.model;

public class ChartSizeModel  implements java.io.Serializable {
	private final static int SMALL_XWIDTH = 500;
	private final static int SMALL_HEIGHT = 100;
	private final static int SMALL_FONTSIZE = 9;
	private final static int LARGE_FONTSIZE = 10;
	private final static int SMALL_CONAMESIZE = 100;
	private final static int SMALL_CHART = 100;

	private boolean _isSmallXWidth;
	private boolean _isSmallHeight;
	private boolean _printConame;
	private boolean _printDesc;
	private int _labelFontSize;
	private StringBuffer _sbLabelStrSize = new StringBuffer();
	
	ChartSizeModel() {		
	}
	
	public void init (int width, int height, String hi) {
		_isSmallXWidth = (width <=  SMALL_XWIDTH);
        _isSmallHeight = (height <=  SMALL_HEIGHT);
		if (_isSmallXWidth)
			_labelFontSize = SMALL_FONTSIZE;
		else
			_labelFontSize = LARGE_FONTSIZE;
	
		//add space right 
		int len = hi.length() + 1;
		
		for (int i=0;i<len;i++)
			_sbLabelStrSize.append("X");

		_printConame = (width >  SMALL_CONAMESIZE);
		_printDesc = (width >   SMALL_CHART);
			
    }
    public boolean isSmallXWidth() {return _isSmallXWidth;}
    public boolean isSmallHeight() {return _isSmallHeight;}
    public int getLabelFontSize() {return _labelFontSize;}
	public boolean printConame() {
		return _printConame;
	}
	
	public boolean printDesc() {
		return _printDesc;
	}
	public String getLabelStrSize() {return _sbLabelStrSize.toString();}
}
