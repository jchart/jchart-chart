package com.jchart.model;

import com.jchart.model.color.ColorValue;

/**
 * @author <a href="mailto:paul.russo@jchart.com">Paul S. Russo</a>
 */
public class JchartRequest implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private int _lastDate;
	private String _ticker = "_DJI";
    private int _timeFrame = 3;
    private ChartOption _priceOption = ChartOption.BARCHART;
    private ChartOption _indicatorOption = ChartOption.VOLUME;
    private String _priceColor = "#FFFF00";
    private String _volColor = "#FFFF99";
    private String _topBackColor = "#000033";
    private String _botBackColor = "#000066";
    private String _topCaptionColor = "#FFFFFF";
    private String _botCaptionColor = "#CCCCCC";
    private String _xyLabelColor = "#FFFFFF";
    private String _gridColor = "#FFFFFF";
    private String _borderColor = "#102850";
    private String _mountBorderColor = "#000000";
    private String _gifBorderColor = "#000000";
    private String _chartText = "Jchart";
    private String _movingAverage = "false";
    private String _singlePlot = "false";
    private int _width = 700;
    private int _height = 350;

    JchartRequest() {
    }

    //getters
    public String getTicker() {
        return _ticker;
    }

    public int getTimeFrame() {
        return _timeFrame;
    }

    public ColorValue getPriceColor() {
        return decodeColor(_priceColor);
    }

    public ColorValue getVolColor() {
        return decodeColor(_volColor);
    }

    public ColorValue getTopBackColor() {
        return decodeColor(_topBackColor);
    }

    public ColorValue getBotBackColor() {
        return decodeColor(_botBackColor);
    }

    public ColorValue getTopCaptionColor() {
        return decodeColor(_topCaptionColor);
    }

    public ColorValue getBotCaptionColor() {
        return decodeColor(_botCaptionColor);
    }

    public ColorValue getBorderColor() {
        return decodeColor(_borderColor);
    }

    public ColorValue getGridColor() {
        return decodeColor(_gridColor);
    }

    public ColorValue getXyLabelColor() {
        return decodeColor(_xyLabelColor);
    }

    //setters
    public void setTicker(String s) {
        _ticker = s.toUpperCase();
    }

    public void setTimeFrame(int i) {
        _timeFrame = i;
    }

    public void setPriceColor(String s) {
        _priceColor = s;
    }

    public void setPriceOption(ChartOption option) {
        _priceOption = option;
    }

    public ChartOption getPriceOption() {
        return _priceOption;
    }

    public void setIndicatorOption(ChartOption option) {
        _indicatorOption = option;
    }
    public ChartOption getIndicatorOption() {
        return _indicatorOption;
    }

    public void setMovingAverage(String s) {
        _movingAverage = s;
    }

    public void setVolColor(String s) {
        _volColor = s;
    }

    public void setTopBackColor(String s) {
        _topBackColor = s;
    }

    public void setBotBackColor(String s) {
        _botBackColor = s;
    }

    public void setTopCaptionColor(String s) {
        _topCaptionColor = s;
    }

    public void setBotCaptionColor(String s) {
        _botCaptionColor = s;
    }

    public void setBorderColor(String s) {
        _borderColor = s;
    }

    public void setMountBorderColor(String s) {
        _mountBorderColor = s;
    }

    public ColorValue getMountBorderColor() {
        return decodeColor(_mountBorderColor);
    }

    public void setGifBorderColor(String s) {
        _gifBorderColor = s;
    }

    public ColorValue getGifBorderColor() {
        return decodeColor(_gifBorderColor);
    }

    public void setGridColor(String s) {
        _gridColor = s;
    }

    public void setXyLabelColor(String s) {
        _xyLabelColor = s;
    }

    public void setChartText(String s) {
        _chartText = s;
    }

    public void setSinglePlot(String s) {
        _singlePlot = s;
    }

    public void setWidth(int w) {
        _width = w;
    }

    public void setHeight(int h) {
        _height = h;
    }

    private ColorValue decodeColor(String hexColor) {
    	int r = Integer.valueOf(hexColor.substring(1, 3), 16).intValue();
    	int g = Integer.valueOf(hexColor.substring(3, 5), 16).intValue();
    	int b = Integer.valueOf(hexColor.substring(5, 7), 16).intValue();
    	return new ColorValue(r, g, b);
    }

    public boolean getPriceMaOn() {
        return "true".equalsIgnoreCase(_movingAverage);
    }

    public boolean isSinglePlot() {
        return "true".equalsIgnoreCase(_singlePlot);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof JchartRequest)) {
            return false;
        } else {
            return _ticker.equals(((JchartRequest) obj)._ticker) &&
            (_timeFrame == (((JchartRequest) obj)._timeFrame)) &&
            _priceOption.equals(((JchartRequest) obj)._priceOption) &&
            _indicatorOption.equals(((JchartRequest) obj)._indicatorOption) &&
            _priceColor.equals(((JchartRequest) obj)._priceColor) &&
            _volColor.equals(((JchartRequest) obj)._volColor) &&
            _topBackColor.equals(((JchartRequest) obj)._topBackColor) &&
            _botCaptionColor.equals(((JchartRequest) obj)._botCaptionColor) &&
            _xyLabelColor.equals(((JchartRequest) obj)._xyLabelColor) &&
            _gridColor.equals(((JchartRequest) obj)._gridColor) &&
            _borderColor.equals(((JchartRequest) obj)._borderColor) &&
            _mountBorderColor.equals(((JchartRequest) obj)._mountBorderColor) &&
            _gifBorderColor.equals(((JchartRequest) obj)._gifBorderColor) &&
            _chartText.equals(((JchartRequest) obj)._chartText) &&
            _movingAverage.equals(((JchartRequest) obj)._movingAverage) &&
            _singlePlot.equals(((JchartRequest) obj)._singlePlot);
        }
    }

    public int hashCode() {
        int result = 17;
		result = (37 * result)  + _lastDate;
		result = (37 * result)  + _timeFrame;
		result = (37 * result)  + _width;
		result = (37 * result)  + _height;

        return result;
    }

	/**
	 * @param i
	 */
	public void setLastDate(int i) {
		_lastDate = i;
		
	}
}
