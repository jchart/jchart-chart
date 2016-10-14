package com.jchart.model;

public class ChartTypeModel implements ChangeRequest, java.io.Serializable {
    private boolean _isRs;
	private boolean _isForward;
	private boolean _isBack;
	private boolean _chartTypeChanged;
    //indicator MA constants
    public static final int MACD_MA1 = 12;
    public static final int MACD_MA2 = 26;
    public static final int MACD_SIGNAL = 3;
    public static final int MACDHIST_MA1 = 36;
    public static final int MACDHIST_MA2 = 78;
    public static final int MACDHIST_SIGNAL = 11;
    public static final int STOCH_MA = 7;
    public static final int RSI_MA = 21;
    public static final int PRICE_MA1 = 21;
    public static final int PRICE_MA2 = 50;
    public static final int PRICE_MA3 = 200;
    //private
	private ChartOption _priceOption;
	private ChartOption _indicatorOption;
	private ChartOption _initialPriceOption;
	private ChartOption _initialIndicatorOption;
	
    private boolean _priceMaOn;
    private boolean _bandsOn;
    private boolean _regressTrendlineOn;
    private boolean _isPriceMaDirty;
    private boolean _isVolMountain;
    private boolean _isSinglePlot;
    private boolean _isComparison;
    private boolean _isColor;
    private boolean _isGif;
    private boolean _isPrint;

    //moving averages
    private int[] _ma = new int[3];
    private int[] _priceMa = { PRICE_MA1, PRICE_MA2, PRICE_MA3 };
    private int _adxMa = 18;
    private int _rsiMa = 21;
	private boolean _horizontalLineOn;

	ChartTypeModel() {	
	}
	
    public void reset() {
        _priceOption = _initialPriceOption;
        _indicatorOption = _initialIndicatorOption;
    }

	public void setInitialPriceOption(String name) {
		_initialPriceOption = ChartOption.getPriceOption(name);
		_priceOption = _initialPriceOption;
	}
	public void setInitialIndicatorOption(String name) {
		_initialIndicatorOption = ChartOption.getIndicatorOption(name);
		_indicatorOption = _initialIndicatorOption;
	}
	
    public void setPriceOption(String priceName) {
		_priceOption = ChartOption.getPriceOption(priceName);
        _indicatorOption = _initialIndicatorOption;
    }

	public void setIndicatorOption(String indicatorName) {
		_indicatorOption = ChartOption.getIndicatorOption(indicatorName);
		setindicatorOptionAttr();
	}


    public void setChartOption(ChartOption chartOption)  {
		_isRs = false;
        _isComparison = false;
        _isColor = false;
        _isPrint = false;
		_isBack = false;
		_isForward = false;
		_isPriceMaDirty = true;


		if (chartOption == ChartOption.VOLUME) {
            _bandsOn = false;
            _priceMaOn = false;
		}
		if (chartOption == ChartOption.BACK) {
			_isBack = true;
			
			return;
		}
			
		if (chartOption == ChartOption.FORWARD) {
			_isForward = true;
			
			return;
		}

        if (chartOption == ChartOption.COLORS) {
            _isColor = true;

            return;
        }

		if (chartOption == ChartOption.SINGLEPLOT) {
			if (_isSinglePlot) {
				_isSinglePlot = false;
			} else {
				_isSinglePlot = true;
			}
			
			return;
		}

        if (chartOption == ChartOption.PRINT) {
            _isPrint = true;

            return;
        }

        if (chartOption == ChartOption.PRICEMA) {
            if (_priceOption == ChartOption.PNF) {
                _priceOption = _initialPriceOption;
            }

            _bandsOn = false;
            _priceMaOn = true;
            _isPriceMaDirty = true;
			_indicatorOption = ChartOption.VOLUME;

            return;
        }

        if (chartOption == ChartOption.BBANDS) {
            _priceMaOn = false;
            _bandsOn = true;
            _isPriceMaDirty = true;
			_indicatorOption = ChartOption.VOLUME;
            return;
        }
        
        if (chartOption == ChartOption.COMPARE) {
            _priceMaOn = false;
            _isSinglePlot = true;
            _isComparison = true;
            _priceOption = ChartOption.COMPARE;
			_indicatorOption = ChartOption.VOLUME;

            return;
        }

		if (chartOption == ChartOption.RSSYMBOL) {
			_isRs = true;
			return;
		}
		
        if (chartOption == ChartOption.REGRESS) {
            toggleRegressTrendLineOn();
            _horizontalLineOn = false;

            return;
        }

        if (chartOption == ChartOption.HORIZONTAL_LINE) {
            toggleHorizontalLineOn();
            _regressTrendlineOn = false;

            return;
        }

        // is chart option a price chart?
		String optionName = chartOption.getName();
        boolean isPriceChart = ChartOption.isPriceOption(optionName);

        if (isPriceChart) {
            _indicatorOption = _initialIndicatorOption;
            setPriceOption(optionName);
        } else {
            if (_priceOption == ChartOption.PNF) {
				setPriceOption(_initialPriceOption.getName());
            }

            _isSinglePlot = false;
            setIndicatorOption(optionName);
        }
    }

    public ChartOption getPriceOption() {
        return _priceOption;
    }

    public ChartOption getIndicatorOption() {
        return _indicatorOption;
    }

    public boolean isPriceOptionSet() {
        return (_priceOption  !=  null);
    }

    public boolean isIndicatorOptionSet() {
        return (_indicatorOption != null);
    }

    public boolean isVolMountain() {
        return _isVolMountain;
    }

    public void isVolMountain(boolean b) {
        _isVolMountain = b;
    }

    public boolean isGif() {
        return _isGif;
    }

    public void isGif(boolean b) {
        _isGif = b;
    }

    public boolean isSinglePlot() {
        return _isSinglePlot;
    }

    public void isSinglePlot(boolean b) {
        _isSinglePlot = b;
    }

    public boolean isComparison() {
        return _isComparison;
    }

    public void isComparison(boolean b) {
        _isComparison = b;
    }

	public boolean isRs() {
		return _isRs;
	}
    public boolean isColor() {
        return _isColor;
    }

    public boolean isPrint() {
        return _isPrint;
    }

    public String getPrintChartTypeText() {
    	
        String chartName = _priceOption.getName();

        if (chartName.endsWith("Chart")) {
			chartName = chartName.substring(0, chartName.length() - 6);
        }

        if (chartName.equals("Mountain")) {
			chartName = "Line";
        }

        return "Financial" + chartName;
    }

    public int getMa(int i) {
        return _ma[i];
    }

    public String getPriceMaStr(int i) {
        return _priceMa[i] + "";
    }

    public int getPriceMa(int i) {
        return _priceMa[i];
    }

    public int getAdxMa() {
        return _adxMa;
    }

    public void setAdxMa(int ma) {
        _adxMa = ma;
    }

    public boolean isPriceMaOn() {
        return _priceMaOn;
    }

    public boolean isBandsOn() {
        return _bandsOn;
    }

    public boolean isRegressTrendlineOn() {
        return _regressTrendlineOn;
    }

    public boolean isHorizontalLineOn() {
        return _horizontalLineOn;
    }

    public void togglePriceMaOn() {
        if (_priceMaOn) {
            _priceMaOn = false;
        } else {
            _priceMaOn = true;
            _isPriceMaDirty = true;
        }
    }

    public void toggleSinglePlotOn() {
        if (_isSinglePlot) {
            _isSinglePlot = false;
        } else {
            _isSinglePlot = true;
        }
    }

    public void toggleRegressTrendLineOn() {
        if (_regressTrendlineOn) {
            _regressTrendlineOn = false;
        } else {
            _regressTrendlineOn = true;
        }
    }

    public void toggleHorizontalLineOn() {
        if (_horizontalLineOn) {
        	_horizontalLineOn = false;
        } else {
        	_horizontalLineOn = true;
        }
    }

    public void setPriceMa(int i, int maVal) {
        _priceMa[i] = maVal;
    }

    public void setIsPriceMaDirty(boolean b) {
        _isPriceMaDirty = b;
    }

    public boolean getIsPriceMaDirty() {
        return _isPriceMaDirty;
    }

    private void setindicatorOptionAttr() {
        if (_initialIndicatorOption == null) {
			_initialIndicatorOption = _indicatorOption;
        }

		if (_indicatorOption == ChartOption.MACD) {
            _ma[0] = MACD_MA1;
            _ma[1] = MACD_MA2;
            _ma[2] = MACD_SIGNAL;

		} else if (_indicatorOption == ChartOption.MACDHIST) {
            _ma[0] = MACDHIST_MA1;
            _ma[1] = MACDHIST_MA2;
            _ma[2] = MACDHIST_SIGNAL;

		} else if (_indicatorOption == ChartOption.STOCHASTIC) {
            _ma[0] = STOCH_MA;

		} else if (_indicatorOption == ChartOption.RSI) {
            _ma[0] = RSI_MA;

		} else if (_indicatorOption == ChartOption.VOLUME) {
            _priceOption = _initialPriceOption;

        }
    }

    public boolean isPriceLine() {
        return _priceOption == ChartOption.LINECHART;
    }

    public boolean isPriceCandle() {
        return _priceOption == ChartOption.CANDLESTICK;
    }

    public boolean isPriceMountian() {
        return _priceOption == ChartOption.MOUNTAINCHART;
    }

	public void chartTypeChanged(boolean b) {
		_chartTypeChanged = b;
	}

	public boolean chartTypeChanged() {
		return _chartTypeChanged;
	}

	public void processRequest(JchartRequest jchartRequest) {
		_indicatorOption = jchartRequest.getIndicatorOption();
		_priceOption = jchartRequest.getPriceOption();
		_initialPriceOption = _priceOption;
		_initialIndicatorOption = _indicatorOption;
		_priceMaOn = jchartRequest.getPriceMaOn();
		if (_priceMaOn) {
			_isPriceMaDirty = true;
		}
		if (_bandsOn) {
			_isPriceMaDirty = true;
		}
		_isSinglePlot = jchartRequest.isSinglePlot();
		setindicatorOptionAttr();
		
	}

	public boolean isBack() {
		return _isBack;
	}
	public boolean isForward() {
		return _isForward;
	}
	public void isBack(boolean b) {
		_isBack = b;		
	}
	public void isForward(boolean b) {
		_isForward = b;
	}

	public void isRs(boolean b) {
		_isRs = b;
	}

	public int getRsiMa() {
		return _rsiMa;
	}

	public void setRsiMa(int ma) {
		_rsiMa = ma;
	}

}
