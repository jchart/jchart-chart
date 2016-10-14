package com.jchart.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.jchart.applet.AppletParamAccessor;
import com.jchart.io.factory.IoFactory;


public class InitParmParser {
    //initial parms
	public static final String P_TICKERDIR = "tickerDir";
	public static final String P_TICKERFILE = "tickerFile";
    public static final String P_MAXQUOTES = "maxQuotes";
    public static final String P_TICKERS = "tickers";
    protected static final String P_CGIDIR = "cgiDir";
    protected Map<String,String> _cmdLineArgs = new HashMap<String,String>();
    protected List<String> _tickerList = new ArrayList<String>();
    protected boolean _useCgi;
    protected String _cgiDir;
    protected String _tickerFile;
    protected String _tickerDir;
    protected JchartRequest _jchartRequest;
    protected String _tickerListName;
	private AppletParamAccessor _appletParamAccessor;
    
    public static final String P_TICKER = "ticker";
    public static final String P_DATADIR = "dataDir";
    public static final String P_SINGLEPLOT = "singlePlot";
	public static final String P_ISLOCAL = "isLocal";
	public static final String P_ISSWT = "isSwt";
	public static final String P_TICKER_LISTNAME = "tickerListName";

    //colors full version
    public static final String P_PRICE_COLOR = "priceColor";
    public static final String P_VOL_COLOR = "volColor";
    public static final String P_TOP_BACK_COLOR = "topBackColor";
    public static final String P_BOT_BACK_COLOR = "botBackColor";
    public static final String P_TOP_CAPTION_COLOR = "topCaptionColor";
    public static final String P_BOT_CAPTION_COLOR = "botCaptionColor";
    public static final String P_BORDER_COLOR = "borderColor";
    public static final String P_GRID_COLOR = "gridColor";
    public static final String P_XY_LABEL_COLOR = "xyLabelColor";
    public static final String P_MOUNT_BORDER_COLOR = "mountBorderColor";
    public static final String P_GIF_BORDER_COLOR = "gifBorderColor";
    public static final String P_PRICECHART = "priceChart";
    public static final String P_INDICATORCHART = "indicatorChart";
    public static final String P_MOVINGAVERAGE = "movingAverage";
    public static final String P_EODPERIOD = "eodPeriod";
    private static final String P_USECGI = "useCgi";
    private static final int MAX_QUOTES = 2600;

    
    public void setAppletParamAccessor (AppletParamAccessor appletParamAccessor) {
    	_appletParamAccessor = appletParamAccessor;
    }

    public void init() {
        String param = getParam(P_TICKERDIR);
        if (param != null) {
            if (!param.endsWith("/")) {
            	param += "/";
            }
            _tickerDir = param;
        } else {
            _tickerDir = null;
        }

        param = getParam(P_CGIDIR);
        if (param != null) {
            _cgiDir = param;
        } else {
            _cgiDir = "http://www.jchart.com/cgi-bin/";
        }
        param = getParam(P_USECGI);
        if (param != null) {
            _useCgi = Boolean.parseBoolean(param);
        }

        param = getParam(P_MAXQUOTES);
        if (param == null) {
            QuoteDataModel.setMaxQuotes(MAX_QUOTES);
        } else {
        	int maxQuotes = Integer.parseInt(param);
        	if (maxQuotes > MAX_QUOTES) {
        		maxQuotes = MAX_QUOTES;
        	}
            QuoteDataModel.setMaxQuotes(maxQuotes);
        }
        String dataDir = getParam(P_DATADIR);
        if (dataDir != null && !dataDir.endsWith("/")) {
        	dataDir += "/";
        }
        
        if (_appletParamAccessor != null) {
        	_useCgi = true;
        }
        
        IoFactory.initInstance(_cgiDir, _tickerDir, _useCgi,  dataDir);
    }

    public void parse(JchartComposite jchartComposite) {
        _jchartRequest = jchartComposite.getJchartRequest();
        String param = getParam(P_TICKER_LISTNAME);
        if (param != null) {
            _tickerListName = param;
        }

        param = getParam(P_TICKERFILE);
        if (param != null) {
            _tickerFile = param;
        } else {
            _tickerFile = null;
        }

        param = getParam(P_TICKER);
        if (param != null) {
            _jchartRequest.setTicker(param);
        }

        param = getParam(P_TICKERS);
        if ((param != null) && (param.trim().length() > 0)) {
			StringTokenizer stTickers = new StringTokenizer(param, ",");
			while (stTickers.hasMoreElements()) {
				String ticker = stTickers.nextToken();
				_tickerList.add(ticker);
 	       }
        }

        //color parms full ver
        param = getParam(P_PRICE_COLOR);
        if (param != null) {
            _jchartRequest.setPriceColor(param);
        }

        param = getParam(P_VOL_COLOR);
        if (param != null) {
            _jchartRequest.setVolColor(param);
        }

        param = getParam(P_TOP_BACK_COLOR);
        if (param != null) {
            _jchartRequest.setTopBackColor(param);
        }

        param = getParam(P_BOT_BACK_COLOR);
        if (param != null) {
            _jchartRequest.setBotBackColor(param);
        }

        param = getParam(P_TOP_CAPTION_COLOR);
        if (param != null) {
            _jchartRequest.setTopCaptionColor(param);
        }

        param = getParam(P_BOT_CAPTION_COLOR);
        if (param != null) {
            _jchartRequest.setBotCaptionColor(param);
        }

        param = getParam(P_BORDER_COLOR);
        if (param != null) {
            _jchartRequest.setBorderColor(param);
        }

        param = getParam(P_GRID_COLOR);
        if (param != null) {
            _jchartRequest.setGridColor(param);
        }

        param = getParam(P_XY_LABEL_COLOR);
        if (param != null) {
            _jchartRequest.setXyLabelColor(param);
        }

        param = getParam(P_MOUNT_BORDER_COLOR);
        if (param != null) {
            _jchartRequest.setMountBorderColor(param);
        }

        param = getParam(P_GIF_BORDER_COLOR);
        if (param != null) {
            _jchartRequest.setGifBorderColor(param);
        }

        //end colors
        param = getParam(P_PRICECHART);
        if (param != null) {
            _jchartRequest.setPriceOption(ChartOption.getChartOption(param));
        }

        param = getParam(P_INDICATORCHART);
        if (param != null) {
            _jchartRequest.setIndicatorOption(ChartOption.getChartOption(param));
        }

        param = getParam(P_EODPERIOD);
        if (param != null) {
            _jchartRequest.setTimeFrame(Integer.parseInt(param));
        }

        param = getParam(P_MOVINGAVERAGE);
        if ((param != null) && param.equalsIgnoreCase("true")) {
            _jchartRequest.setMovingAverage("true");
        }

        param = getParam(P_SINGLEPLOT);
        if (param != null) {
            _jchartRequest.setSinglePlot("true");
        }

		param = getParam(P_ISLOCAL);
		if (param != null && Boolean.valueOf(param)) {
			QuoteDataModel.setLocal();
		}

    }

    public void loadCmdLineArgs(String[] args) {
        for (int i = 0; i < args.length; i++) { //first one is class name
            String arg = args[i];
            if (arg.indexOf("=") == -1) {
                continue;
            }
            String name = arg.substring(0, arg.indexOf("="));
            String value = arg.substring(arg.indexOf("=") + 1);
            if (!"null".equals(value)) {
                _cmdLineArgs.put(name, value);
            }
        }
    }

    public String getValue(String name) {
    	return (String)_cmdLineArgs.get(name);
    }

    public String getPathValue(String name) {
    	String retval = null;
		retval = (String)_cmdLineArgs.get(name);
		if (!retval.endsWith("/")) {
			retval += "/";
		}
    	return retval;
    }


    protected String getParam(String strName) {
        String parm = null;

        //command line overrides 
        Object obj = _cmdLineArgs.get(strName);
        if (obj != null) {
            parm = (String) obj;
        } else {
        	if (_appletParamAccessor != null) {
        		parm = _appletParamAccessor.getParam(strName);
        	}
        }

        return parm;
    }

    public List<String> getTickerList() {
        return _tickerList;
    }

    public String getTickerDir() {
        return _tickerDir;
    }

    public String getCgiDir() {
        return _cgiDir;
    }

    public String getTickerListName() {
        return _tickerListName;
    }
    
    public List<String> loadTickers() {
        try {
        	if (_tickerDir != null && _tickerFile != null) {
	            _tickerList = IoFactory.getTickerList("file:///" +
	                    _tickerDir + _tickerFile);
        	}
        } catch (Exception e) {
                e.printStackTrace();
        }

        return _tickerList;
    }
}
