package com.jchart.io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import com.jchart.model.ChartTypeModel;
import com.jchart.model.JchartComposite;
import com.jchart.model.QuoteDataModel;


//logging
//import org.apache.log4j.Category;
public abstract class IoJchartBase
    implements com.jchart.io.factory.IoFactoryIntr {
	//    private final static Category log = Category.getInstance(IoJchartBase.class.getName());

	protected String _dataDir;
	protected String _cgiDir;
	protected String _tickerDir;
	protected boolean _useCgi;


    //abstract 
    protected abstract boolean getEodQuotes(String sbl, JchartComposite jchartComposite)
        throws Exception;

    public void init(String cgiDir, String tickerDir, boolean useCgi,
            String dataDir) {
            _dataDir = dataDir;
            _cgiDir = cgiDir;
            _tickerDir = tickerDir;
            _useCgi = useCgi;
        }


    
 	/**
	 * retrieve quotes for previously saved _sbl and _rsSbl
	 * @return boolean
	 * @throws Exception
	 */
	private boolean rsRefresh(JchartComposite jchartComposite) throws Exception {
		boolean requestOk = false;
		QuoteDataModel quoteDataModel = jchartComposite.getQuoteDataModel();
		String rsTicker = quoteDataModel.getCompareTicker(); //save rsSbl as it gets reset in getQuote
		boolean isIntraDay = quoteDataModel.isIntraday();
		requestOk = getQuotes(quoteDataModel.getTicker(),jchartComposite,isIntraDay);
		if (requestOk)
			requestOk = getQuotes(rsTicker,jchartComposite,isIntraDay);
		return requestOk;
	}

    public boolean tickerRequestEod(String sbl, JchartComposite jchartComposite) throws Exception {
    	boolean retval = false;
		boolean isIntraDay = false;
		QuoteDataModel quoteDataModel = jchartComposite.getQuoteDataModel();
		boolean wasIntraday = quoteDataModel.isIntraday();
		ChartTypeModel chartTypeModel = jchartComposite.getChartTypeModel();
		quoteDataModel.isIntraday(isIntraDay);
		if ( (chartTypeModel.isComparison()) 
			&& (wasIntraday) ) {
			retval = rsRefresh(jchartComposite);
		} else { 
			retval =  getQuotes(sbl, jchartComposite, isIntraDay);
		}
		return retval;
    }

    public boolean tickerRequestIntraday(String ticker, JchartComposite jchartComposite) throws Exception {
		boolean requestOk = false;
		QuoteDataModel quoteDataModel = jchartComposite.getQuoteDataModel();
		ChartTypeModel chartTypeModel = jchartComposite.getChartTypeModel();
		quoteDataModel.isIntraday(true);
		if ( (chartTypeModel.isComparison())
			&& (ticker.equalsIgnoreCase(quoteDataModel.getTicker())) ) 
			requestOk = rsRefresh(jchartComposite);
		else
			requestOk = getQuotes(ticker, jchartComposite, true);
			
		return requestOk;
    }

    /**
     * @param String sbl
     * @param boolean isIntraday
     */
    private boolean getQuotes(String sbl, JchartComposite jchartComposite, boolean isIntraDay)
        throws Exception {
		QuoteDataModel quoteDataModel = jchartComposite.getQuoteDataModel();
		ChartTypeModel chartTypeModel = jchartComposite.getChartTypeModel();
        //      log.debug("requesting "+sbl);
        System.out.println("requesting " + sbl);
		boolean requestOk = false;        

        if (isIntraDay) {
            requestOk = getIntraDayQuotes(sbl, jchartComposite);
        } else {
            requestOk = getEodQuotes(sbl, jchartComposite);
        }
		quoteDataModel.isIntraday(isIntraDay);

        if (requestOk) {
            System.out.println(sbl + ": found");

//            if (!chartTypeModel.isComparison() && !chartTypeModel.isRs()) {/
//				quoteDataModel.setComane(JchartModelFacade.getConame(sbl.toUpperCase());
//            }
 			quoteDataModel.requestComplete(sbl, chartTypeModel);

        } else {
			if (isIntraDay) { 
				quoteDataModel.requestComplete(sbl, chartTypeModel);
			}
            System.out.println(sbl + ": no data found");
        }

        return requestOk;
    }

    public boolean checkIn(String prodIdF, String prodId)
        throws Exception {
        return true;
    }


    /**
     * Loads property file keys and values into a properties object.
     * This method is useful for a class that is trying to read a properties
     * file within a jar file.
     */
    public static synchronized Properties loadProperties(Class<?> callingClass,
        String filename) throws FileNotFoundException, IOException {
        InputStream is = callingClass.getResourceAsStream("/" + filename);
        Properties props = new Properties();
        props.load(is);
        is.close();

        return props;
    }

    protected String getCurDate() {
        String yyyy;
        String mm;
        String dd;
        Integer yyyyI;
        Integer mmI;
        Integer ddI;
        Calendar rightNow = Calendar.getInstance();
        yyyyI = new Integer(rightNow.get(Calendar.YEAR));
        mmI = new Integer(rightNow.get(Calendar.MONTH) + 1);
        ddI = new Integer(rightNow.get(Calendar.DAY_OF_MONTH));
        yyyy = yyyyI.toString();
        mm = mmI.toString();
        dd = ddI.toString();

        if (dd.length() < 2) {
            dd = '0' + dd;
        }

        if (mm.length() < 2) {
            mm = '0' + mm;
        }

        return dd + mm + yyyy;
    }

 
    private boolean getIntraDayQuotes(String sbl, JchartComposite jchartComposite) throws Exception {
        return false;
    }
	

	/**
	 * Loads ticker=coname
	 * This method is useful for a class that is trying to read a properties
	 * file within a jar file.
	 */
	public static Properties loadProps(Class<?> callingClass, String fileName) 
		throws FileNotFoundException, IOException {
			InputStream is = callingClass.getResourceAsStream("/" + fileName);
			Properties props = new Properties();
			props.load(is);
			is.close();
			return props;
		}
	
    /**
     * @param tickerFile
     * @return String a list of tickers
     */
    public synchronized List<String> getTickerList(String tickerFile)
        throws Exception {
        List<String> retval = readTickers(tickerFile);

        if (retval.isEmpty()) {
        	retval = readTickers("default.tic");
        }

        return retval;
    }

    
    private List<String> readTickers(String tickerFile) throws Exception {
        List<String> retval = new ArrayList<String>();
        InputStreamReader isrTicFH = null;
        BufferedReader brTicFH = null;
        String inputLine = null;

        URL url = new URL(tickerFile);
        isrTicFH = new InputStreamReader(url.openStream());
        brTicFH = new BufferedReader(isrTicFH);
        inputLine = brTicFH.readLine();
        if (inputLine.indexOf(")") == -1) {
        	retval.add(inputLine);
        }

        while ((inputLine = brTicFH.readLine()) != null) {
        	retval.add(inputLine);
        }
        if (isrTicFH != null) {
            isrTicFH.close();
        }

        if (brTicFH != null) {
            brTicFH.close();
        }

        return retval;
    }

    

}
