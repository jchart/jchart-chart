package com.jchart.model;
/**
 * singleton class model of end-of-day and intraday periods 
 * the model part of a MVC pattern
 */
public class TimeFrameModel implements java.io.Serializable {
    private static final String[] mixedText = {"Intraday","  1 Week  "," 1 Month  ",
                                             "  3 Month "," 6 Month  ",
                                             "  1 Year  ","  2 Year 1/2  ",
                                             "  5 Year  ", "  10 Year  "};

    private static final String[] mixedTextL2 = {"Intraday","1 Week","1 Month",
                                                 "","",
                                                 "1 Year","","",""};

    private static final int[] dataPointNum = {480,5,21,63,126,252,550,1300,2600};
//	private static final int ONEDAYMINUTES  = dataPointNum[0];
	public static final int ONEWEEK        = dataPointNum[1];
	public static final int ONEMONTH       = dataPointNum[2];
	public static final int THREEMONTHS    = dataPointNum[3];
	public static final int SIXMONTHS      = dataPointNum[4];
	public static final int ONEYEAR        = dataPointNum[5];
	public static final int TWOHALFYEAR    = dataPointNum[6];

    public static final int PERIOD_INTRADAY = 0;
    private static final int[] intradayPeriod = {1,5,10,15,30,60,120};
    private static int numMixedPeriod = mixedText.length;

    private int _curPeriod;
    private int _initialPeriod = -1;
    private boolean _intraday;

	TimeFrameModel() {
	}
	
    public int getDefNumIntradayRecs() {
        return dataPointNum[PERIOD_INTRADAY];
    }    
    /**
     * @return boolean
     */
    public boolean isIntraday() {	
        return (_intraday == true);
    }	
    /**
     * set the current mode
     */
    public void setIntraday(boolean b) {
        _intraday = b;
        if (_intraday)
            _curPeriod = PERIOD_INTRADAY;
    }
    /**
     * @return gets current period
     */
    public int getCurPeriod() {
        return _curPeriod;
    }	
    /**
     * @return sets current period
     */
    public void setCurPeriod(int i) {
        _curPeriod = i;
        if (_initialPeriod == -1)
            _initialPeriod = i;
        if (_curPeriod == PERIOD_INTRADAY)
            setIntraday(true);
//         else 
//             setIntraday(false);
    }
    public String getIntradayLabel() {
        return mixedText[PERIOD_INTRADAY];
    }
    /**
     * @param integer
     * @return text in element i
     */
    public static String getMixedText(int i) {
        return mixedText[i];
    }	
    public String getMixedTextL2(int i) {
        return mixedTextL2[i];
    }	
    /**
     * @param integer
     * @return number of periods (data points)
     */
    public int getNumDataPoints(int i) {
        return dataPointNum[i];
    }	
    /**
     * @param integer
     * @return number of periods (data points) for the current period
     */
    public int getNumDataPoints() {
        return dataPointNum[_curPeriod];
    }	
    /**
     * @param integer
     * @return time period in element i
     */
    public int getIntradayPeriod(int i) {
        return intradayPeriod[i];
    }
    /**
     * @return number of periods
     */
    public static int getNumMixedPeriods() {
        return numMixedPeriod;
    }
    /**
     * reset
     */
    public void reset() {
        _curPeriod = _initialPeriod;
    }	
    /**
     * @return number of period
     * added for compatability with TimeFrameTag
     */
    public static int getNumPeriods() {
        return getNumMixedPeriods();
    }
    /**
     * @param integer
     * @return text in element i
     * added for compatability with TimeFrameTag
     */
    public String getEodText(int i) {
        return getMixedText(i);
    }	    
    public int setInitialPlotRecs() {
        if (isIntraday()) {
            return getDefNumIntradayRecs();
        } else {
            return getNumDataPoints();
        }
    }

    public String toString() {
        return
            "_curPeriod: "+ _curPeriod 
            + "\n_initialPeriod: "+ _initialPeriod
            + "\n_intraday: "+ _intraday
            + "\ndataPointNum[_curPeriod]: "+ dataPointNum[_curPeriod]
            + "\nmixedText[_curPeriod]: "+mixedText[_curPeriod] 
            + "\nmixedTextL2[_curPeriod]: "+ mixedTextL2[_curPeriod];
    }
	/**
	 * @return String
	 */
	public String getPrintTimeFrame() {
		switch (_curPeriod) {
		case 0:
			return "1d";
		case 1:
			return "5d";
		case 2:
			return "1m";
		case 3:
			return "3m";
		case 4:
			return "6m";
		case 5:
			return "1y";
		case 6:
			return "2y";
		case 7:
			return "5y";
		default:
			return "5y";		
		}
	}
}
