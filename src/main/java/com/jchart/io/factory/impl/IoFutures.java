/**
 * 
 */
package com.jchart.io.factory.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.jchart.io.IoJchartBase;
import com.jchart.io.factory.IoFactoryIntr;
import com.jchart.model.JchartComposite;
import com.jchart.model.Quote;
import com.jchart.model.QuoteDataModel;

/**
 * @author <a href="mailto:paul.russo@jchart.com">Paul S. Russo</a>
 * @since Jul 20, 2012
 * 
 * http://www.tradingblox.com/tradingblox/free-historical-data.htm
 */

public class IoFutures extends IoJchartBase implements IoFactoryIntr {

	private DateFormat _downloadDateFmt = new SimpleDateFormat("yyyyMMdd");
    private Map<String,FuturesDescr> _futuresTickerMap = new HashMap<String, FuturesDescr>();
    private static Properties _conameProps = new Properties();
    

    public void init(String cgiDir, String tickerDir, boolean useCgi,
           String dataDir) {
    	super.init(cgiDir, tickerDir, useCgi, dataDir);
    	try {
			readFuturesDescr();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    }
    
    public static Properties getConameProps() {
    	return _conameProps;
    }

	@Override
	protected boolean getEodQuotes(String sbl, JchartComposite jchartComposite) throws Exception {
		boolean retval = false;
		FuturesDescr futuresDescr = _futuresTickerMap.get(sbl.toUpperCase());
		if (futuresDescr == null) {
			return false;
		}
		
		BufferedReader br = null;
		ZipFile zipFile = null;
		
		String sblPath = ("Futures/" + futuresDescr.id + ".TXT");
		try {
			zipFile = new ZipFile(_dataDir + "/DataOnly.zip");
			ZipEntry zipEntry = zipFile.getEntry(sblPath);
			if (zipEntry != null) {
				QuoteDataModel.setMaxQuotes(5000);
				QuoteDataModel quoteDataModel = jchartComposite.getQuoteDataModel();
				quoteDataModel.init();
				br = new BufferedReader(new InputStreamReader(
						zipFile.getInputStream(zipEntry)));
				readQuotes(br, quoteDataModel);
				retval = true;
			}
		} catch (IOException ioe) {
			System.out.println(ioe);
			System.out.println(_dataDir + sblPath);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (zipFile != null) {
					try {
						zipFile.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return retval;
	}

	private void readQuotes(BufferedReader br, QuoteDataModel quoteDataModel)
			throws IOException {
		int numQuotes = 0;

		String inputLine = null;
		int i = 0;
		quoteDataModel.setNumQuotesIn(0);

		long priorQuoteDt = 0l;
		while ((inputLine = br.readLine()) != null) {
			if (i == 21) {
				System.out.print(".");
				i = 0;
			}
			i++;

			Quote quote = null;
			try {
				quote = parseQuoteIn(inputLine);
				if (quote.getDate().getTime() != priorQuoteDt) { // no dups
					quoteDataModel.setQuoteIn(quote);
					numQuotes++;
					priorQuoteDt = quote.getDate().getTime();
				}
			} catch (Exception e) {
				e.printStackTrace();
				// just skip it
			}

			if (numQuotes == QuoteDataModel.getMaxQuotes()) {
				break;
			}
		}
	}

	protected Quote parseQuoteIn(String s) throws Exception {
		Quote retval = new Quote();
		StringTokenizer st = new StringTokenizer(s, ",");
		Date date = _downloadDateFmt.parse(st.nextToken().trim());
		float open = new Float(st.nextToken().trim()).floatValue();
		float hi = new Float(st.nextToken().trim()).floatValue();
		float low = new Float(st.nextToken().trim()).floatValue();
		float close = new Float(st.nextToken().trim()).floatValue();
		long volume = new Float(st.nextToken().trim()).longValue();
		retval.setDate(date);
		retval.setOpen(open);
		retval.setHi(hi);
		retval.setLow(low);
		retval.setClose(close);
		retval.setVolume(volume);
		// Close price adjusted for dividends and splits
		float adjClose = new Float(st.nextToken()).floatValue();
		float ratio = adjClose / close;
		if (ratio != 1f) {
			retval.setOpen(open * ratio);
			retval.setHi(hi * ratio);
			retval.setLow(low * ratio);
			retval.setClose(adjClose);
		}
		return retval;
	}
	
    private void readFuturesDescr() throws Exception {
        InputStreamReader isrTicFH = null;
        BufferedReader brTicFH = null;
        String inputLine = null;

        isrTicFH = new InputStreamReader(getClass().getResourceAsStream("/futures.csv"));
        brTicFH = new BufferedReader(isrTicFH);
        inputLine = brTicFH.readLine();
        while ((inputLine = brTicFH.readLine()) != null) {
        	String[] elements = inputLine.split(",");
        	FuturesDescr futuresDescr = new FuturesDescr();
        	String ticker = elements[0];
        	futuresDescr.id = elements[1];
        	futuresDescr.descr = elements[2];
        	_futuresTickerMap.put(ticker, futuresDescr);
        	_conameProps.put(ticker,futuresDescr.descr);
        }
        if (isrTicFH != null) {
            isrTicFH.close();
        }

        if (brTicFH != null) {
            brTicFH.close();
        }
    }


	class FuturesDescr {
		String id;
		String descr;
	}
}
