package com.jchart.model.indicator;

import java.util.Date;

import com.jchart.model.ChartOption;
import com.jchart.model.ChartTypeModel;
import com.jchart.model.JchartComposite;
import com.jchart.model.Kjchart;
import com.jchart.model.Quote;
import com.jchart.model.QuoteDataModel;
import com.jchart.model.indicator.pnf.PnFCalc;
import com.jchart.model.indicator.pnf.Pnf;


public class IndicatorModel implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    //priceMa classes
    private Sma _priceMa1;
    private Sma _priceMa2;
    private Sma _priceMa3;

    // indicator classes
    private Pnf _pnf;
    private PsrMoneyFlow _moneyFlow;
    private Macd _macd;
    private Stochastic _stochastic;
    private RelativeStrength _relativeStrength;
    private Rsi _rsi;
    private BollingerBands _bollingerBands;
    private Adx _adx;

    public IndicatorIntr getIndicator(ChartOption chartOption) {
        Object obj = null;
        if (chartOption == ChartOption.MONEYFLOW) {
            obj = _moneyFlow;
        } else if (chartOption == ChartOption.MACD) {
            obj = _macd;
        } else if (chartOption == ChartOption.MACDHIST) {
            obj = _macd;
        } else if (chartOption == ChartOption.STOCHASTIC) {
            obj = _stochastic;
        } else if (chartOption == ChartOption.RSI) {
            obj = _rsi;
        } else if (chartOption == ChartOption.BBANDS) {
            obj = _bollingerBands;
        } else if (chartOption == ChartOption.ADX) {
            obj = _adx;
        } else if (chartOption == ChartOption.RELATIVE_STRENGTH) {
            obj = _relativeStrength;
        }

        return (IndicatorIntr) obj;
    }

    public Pnf getPnf() {
        return _pnf;
    }

    public IndicatorIntr getPriceMa(int priceMa) {
        Sma sma = null;
        switch (priceMa) {
        case Kjchart.PRICEMA1:
            sma = _priceMa1;

            break;

        case Kjchart.PRICEMA2:
            sma = _priceMa2;

            break;

        case Kjchart.PRICEMA3:
            sma = _priceMa3;

            break;
        }

        return sma;
    }

    public void calcIndicator(JchartComposite jchartComposite) {
        QuoteDataModel quoteDataModel = jchartComposite.getQuoteDataModel();
        ChartTypeModel chartTypeModel = jchartComposite.getChartTypeModel();
        int numQuotes = quoteDataModel.getNumQuotes();
        Quote[] quotes = quoteDataModel.getQuotes();
        reset();
        if (chartTypeModel.getPriceOption() == ChartOption.PNF) {
        	String ticker = quoteDataModel.getTicker();
        	if (ticker.startsWith(Kjchart.BULLISH_PCT_PREFIX)) {
        		PnFCalc.setBullishPct(true);
        	} else {
        		PnFCalc.setBullishPct(false);
        	}
            doPnf(numQuotes, quotes);
            return;
        }

        ChartOption indicatorOption = chartTypeModel.getIndicatorOption();

        if (indicatorOption == ChartOption.MONEYFLOW) {
            doMoneyFlow(jchartComposite);
        } else if (indicatorOption == ChartOption.MACD) {
            doMACD(numQuotes, quotes, chartTypeModel.getMa(0),
                chartTypeModel.getMa(1), chartTypeModel.getMa(2));
        } else if (indicatorOption == ChartOption.MACDHIST) {
            doMACD(numQuotes, quotes, chartTypeModel.getMa(0),
                chartTypeModel.getMa(1), chartTypeModel.getMa(2));
        } else if (indicatorOption == ChartOption.STOCHASTIC) {
            doStoch(numQuotes, quotes, chartTypeModel.getMa(0));
        } else if (indicatorOption == ChartOption.RSI) {
            doRsi(numQuotes, quotes, chartTypeModel.getRsiMa());
        } else if (indicatorOption == ChartOption.RELATIVE_STRENGTH) {
            doRelativeStrength(jchartComposite);
        } else if (indicatorOption == ChartOption.ADX) {
            doAdx(numQuotes, quotes, chartTypeModel.getAdxMa());
        }

        if (chartTypeModel.getIsPriceMaDirty()) {
		    if (chartTypeModel.isPriceMaOn()) {
		        doPriceMa(quoteDataModel, chartTypeModel);
		    } else if (chartTypeModel.isBandsOn()) {
		    	int bandMA = chartTypeModel.getPriceMa(1);
		    	if (numQuotes > bandMA) {
		    		doBollingerBands(numQuotes, quotes, chartTypeModel.getPriceMa(1));
		    	}
		    }
	        chartTypeModel.setIsPriceMaDirty(false);
        }
        chartTypeModel.chartTypeChanged(false);
    }

    //calculate indicators
    private void doPnf(int qListSize, Quote[] quotes) {
        if (_pnf == null) {
            _pnf = new Pnf();
            _pnf.execute(qListSize, quotes);
        }
    }

    private void doMoneyFlow(JchartComposite jchartComposite) {
        if (_moneyFlow == null) {
            _moneyFlow = new PsrMoneyFlow(jchartComposite);
        }
    }

    private void doMACD(int qListSize, Quote[] quotes, int ma1, int ma2, int ma3) {
        if (_macd == null) {
            _macd = new Macd(qListSize, quotes, ma1, ma2, ma3);
        }
    }

    private void doStoch(int qListSize, Quote[] quotes, int ma) {
        if (_stochastic == null) {
            _stochastic = new Stochastic(qListSize, quotes, ma);
        }
    }

    private void doRsi(int qListSize, Quote[] quotes, int ma) {
        if (_rsi == null) {
            _rsi = new Rsi(qListSize, quotes, ma);
        }
    }

    private void doBollingerBands(int qListSize, Quote[] quotes, int ma) {
        if (_bollingerBands == null) {
        	_bollingerBands = new BollingerBands(qListSize, quotes, ma);
        }
    }

    private void doRelativeStrength(JchartComposite jchartComposite) {
        if (_relativeStrength == null) {
            _relativeStrength = new RelativeStrength(jchartComposite);
			_relativeStrength.init();
        }
    }

    private void doAdx(int qListSize, Quote[] quotes, int ma) {
        if (_adx == null) {
        	_adx = new Adx(qListSize, quotes, ma);
        }
    }

    private void doPriceMa(QuoteDataModel quoteDataModel,
        ChartTypeModel chartTypeModel) {
        int numQuotes = quoteDataModel.getNumQuotes();
        int maxQuotes = QuoteDataModel.getMaxQuotes();
        Quote[] quotes = quoteDataModel.getQuotes();
        int ma1 = chartTypeModel.getPriceMa(0);
        int ma2 = chartTypeModel.getPriceMa(1);
        int ma3 = chartTypeModel.getPriceMa(2);

        float[] clVal = new float[maxQuotes];
        Date[] clDt = new Date[maxQuotes];

        //fill with closing prices
        for (int i = 0; i < numQuotes; i++) {
            clDt[i] = quotes[i].getDate();
            clVal[i] = quotes[i].getClose();
        }
        if ((ma1 == 0) || (ma1 > numQuotes)) {
            _priceMa1 = null;
        } else {
            _priceMa1 = new Sma(clDt, clVal, numQuotes, ma1);
        }

        if ((ma2 == 0) || (ma2 > numQuotes)) {
            _priceMa2 = null;
        } else {
            _priceMa2 = new Sma(clDt, clVal, numQuotes, ma2);
        }

        if ((ma3 == 0) || (ma3 > numQuotes)) {
            _priceMa3 = null;
        } else {
            _priceMa3 = new Sma(clDt, clVal, numQuotes, ma3);
        }
    }

    private void reset() {
        _priceMa1 = null;
        _priceMa2 = null;
        _priceMa3 = null;
        _pnf = null;
        _moneyFlow = null;
        _macd = null;
        _stochastic = null;
        _rsi = null;
        _relativeStrength = null;
        _bollingerBands = null;
        _adx = null;
    }
}
