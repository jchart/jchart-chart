package com.jchart.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * typesafe enum pattern 
 */
public class ChartOption implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private String _name;
	private String _desc;
	
	private ChartOption(String name, String descr) {
		_name = name;
		_desc = descr;
	}
	
	public String toString () {
		return _name + ": " 
			+ _desc + ": isPriceChart: \n"; 
	}

	//price chart type
	public static final ChartOption LINECHART = new ChartOption("Line Chart", 
		"");
	public static final ChartOption BARCHART = new ChartOption("Bar Chart", 
		"Chart of Open Hi Low Close");
	public static final ChartOption CANDLESTICK = new ChartOption("CandleStick",
		"CandleStick");
	public static final ChartOption MOUNTAINCHART = new ChartOption("Mountain Chart",
		"Mountain Chart of Close");
	public static final ChartOption PNF = new ChartOption("Point and Figure",
		"");
	static final ChartOption PRINT = new ChartOption("Print",
		"");
	
	//indicator types
	public static final ChartOption VOLUME = new ChartOption("Volume",
		"in Millions");
	public static final ChartOption PRICEMA = new ChartOption("Moving Averages",
		"");
	public static final ChartOption MACD = new ChartOption("MACD",
		"");
	public static final ChartOption MACDHIST = new ChartOption("MACD Histogram",
		"");
	public static final ChartOption MONEYFLOW = new ChartOption("Money Flow (PSR)",
		"");
	public static final ChartOption STOCHASTIC = new ChartOption("Stochastic",
		"80|20");
	public static final ChartOption RSI = new ChartOption("Relative Strength Index",
		"80|20");
	public static final ChartOption BBANDS = new ChartOption("Bollinger Bands",
		"");
	public static final ChartOption ADX = new ChartOption("ADX",
			"");
	public static final ChartOption RELATIVE_STRENGTH = new ChartOption("Relative Strength",
		"RS Line, 10 Day MA, 30 Day MA");
	
	//other chart types
	static final ChartOption COMPARE = new ChartOption("Comparison","");
	static final ChartOption RSSYMBOL = new ChartOption("RS Symbol","");
	static final ChartOption COLORS = new ChartOption("Set Color","");
	static final ChartOption REGRESS = new ChartOption("Regress Trendline","");
	static final ChartOption PARALLEL_LIE = new ChartOption("Parallel line","");
	static final ChartOption HORIZONTAL_LINE = new ChartOption("Horizontal line","");
	static final ChartOption SINGLEPLOT = new ChartOption("SinglePlot","");
	static final ChartOption BACK = new ChartOption("<< Back","");
	static final ChartOption FORWARD = new ChartOption("Forward >>","");

	private static final ChartOption[] PRICE_OPTIONS = 
		{ LINECHART, BARCHART, CANDLESTICK, MOUNTAINCHART, PNF, PRINT };

	private static final ChartOption[] INDICATOR_OPTIONS = 
		{ VOLUME, PRICEMA, BBANDS, MACD, MACDHIST, MONEYFLOW, 
		  STOCHASTIC, RSI,  RELATIVE_STRENGTH, ADX};

	private static final ChartOption[] OTHER_OPTIONS = 
		{ COMPARE, RSSYMBOL, COLORS, REGRESS, HORIZONTAL_LINE, SINGLEPLOT, BACK, FORWARD };

	public static final List PRICE_OPTIONS_LIST = 
		Collections.unmodifiableList(Arrays.asList(PRICE_OPTIONS));

	public static final List INDICATOR_OPTIONS_LIST = 
		Collections.unmodifiableList(Arrays.asList(INDICATOR_OPTIONS));

	public static final List OTHER_OPTIONS_LIST = 
		Collections.unmodifiableList(Arrays.asList(OTHER_OPTIONS));
		
	public static boolean priceOptionEquals(String name, int i) {
		if ( getPriceName(i).equals(name) )
			return true;
		else
			return false;		
	}
	public static boolean indicatorOptionEquals(String name, int i) {
		if ( getIndicatorName(i).equals(name) )
			return true;
		else
			return false;		
	}

	public static boolean otherOptionEquals(String name, int i) {
		if ( getOtherName(i).equals(name) )
			return true;
		else
			return false;		
	}

	public String getName() {
		return _name;		
	}
	public String getDesc() {
		return _desc;		
	}

	static ChartOption getPriceOption(String name) {
		ChartOption option = null;
		for (int i=0;i<ChartOption.PRICE_OPTIONS_LIST.size();i++) {
			//need to use .equals because parm could be command line parm
			if (((ChartOption) ChartOption.PRICE_OPTIONS_LIST.get(i))._name.equals(name))  {
				option = (ChartOption) ChartOption.PRICE_OPTIONS_LIST.get(i);
				break;
			}
		}
		if (option == null)
			option = LINECHART; 
		return option;
	}

	static ChartOption getOtherOption(String name) {
		ChartOption option = null;
		for (int i=0;i<ChartOption.OTHER_OPTIONS_LIST.size();i++) {
			if (((ChartOption) ChartOption.OTHER_OPTIONS_LIST.get(i))._name.equals(name))  {
				option = (ChartOption) ChartOption.OTHER_OPTIONS_LIST.get(i);
				break;
			}
		}
		if (option == null)
			option = LINECHART; 
		return option;
	}

	static ChartOption getIndicatorOption(String name) {
		ChartOption option = null;
		for (int i=0;i<ChartOption.INDICATOR_OPTIONS_LIST.size();i++) {
			if (((ChartOption) ChartOption.INDICATOR_OPTIONS_LIST.get(i))._name.equals(name)) {
				option = (ChartOption) ChartOption.INDICATOR_OPTIONS_LIST.get(i);
				break;
			}
		}
		if (option == null)
			option = VOLUME;
		return option;
	}
	
	public static String getPriceName(int i) {
		return ((ChartOption) ChartOption.PRICE_OPTIONS_LIST.get(i))._name;
	}
	public static String getIndicatorName(int i) {
		return ((ChartOption) ChartOption.INDICATOR_OPTIONS_LIST.get(i))._name;		
	}
	public static String getOtherName(int i) {
		return ((ChartOption) ChartOption.OTHER_OPTIONS_LIST.get(i))._name;
	}
	public static String getPriceDesc(int i) {
		return ((ChartOption) ChartOption.PRICE_OPTIONS_LIST.get(i))._desc;
	}
	public static String getIndicatorDesc(int i) {
		return ((ChartOption) ChartOption.INDICATOR_OPTIONS_LIST.get(i))._desc;		
	}
	public static String getOtherDesc(int i) {
		return ((ChartOption) ChartOption.OTHER_OPTIONS_LIST.get(i))._desc;
	}

	public static int getNumOptions() {
		return PRICE_OPTIONS_LIST.size() + INDICATOR_OPTIONS_LIST.size() + OTHER_OPTIONS_LIST.size();
	}

	public static boolean isOtherOption(String name)  {
		boolean retval = false;
		for (int i = 0;i<OTHER_OPTIONS_LIST.size();i++)  {
			if (getOtherName(i) == name)  {
				retval = true;
				break;
			}
		}
		return retval;
	}
	public static boolean isPriceOption(String name)  {
		boolean retval = false;
		for (int i = 0;i<PRICE_OPTIONS_LIST.size();i++)  {
			if (getPriceName(i).equals(name))  {
				retval = true;
				break;
			}
		}
		return retval;
	}
	public static boolean isIndicatorOption(String name)  {
		boolean retval = false;
		for (int i = 0;i<INDICATOR_OPTIONS_LIST.size();i++)  {
			if (getIndicatorName(i).equals(name))  {
				retval = true;
				break;
			}
		}
		return retval;
	}
	public static String getOptionName(int i) {
		String name = null;
		String retval = null;
		
		if (i < PRICE_OPTIONS_LIST.size()) {
			name =  ( (ChartOption)PRICE_OPTIONS_LIST.get(i)).getName();
			if (isPriceOption(name))
				retval = name;
		}
		if (retval == null) {
			if (i < INDICATOR_OPTIONS_LIST.size()) {
				name =  ( (ChartOption)INDICATOR_OPTIONS_LIST.get(i)).getName();
				if (isIndicatorOption(name))
					retval = name;			
			}		
		}
		if (retval == null) {
			if (i < OTHER_OPTIONS_LIST.size()) 
				retval =  ( (ChartOption)OTHER_OPTIONS_LIST.get(i)).getName();		
		}		
		if (retval == null)
			return "";
		else 
			return retval;	
	}

	public static ChartOption getChartOption(String optionName) {
		ChartOption chartOption = LINECHART;
		if (isPriceOption(optionName))
			chartOption = getPriceOption(optionName);
		else if (isIndicatorOption(optionName))
			chartOption = getIndicatorOption(optionName);
		else if (isOtherOption(optionName))
			chartOption = getOtherOption(optionName);
		return chartOption;	
	}
	
	public boolean equals ( Object obj ) {
		if (!(obj instanceof ChartOption))
		   return false;
		else
			return _name.equals ((( ChartOption )obj )._name) 
				&&  _desc.equals ((( ChartOption )obj )._desc);
	}   
	
	public int hashcode ( ) {
		int result = 17;
		result = 37 * result + _name.hashCode();
		result = 37 * result + _desc.hashCode();
		return result;
	}

}
