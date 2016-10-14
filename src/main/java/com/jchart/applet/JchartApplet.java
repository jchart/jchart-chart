package com.jchart.applet;
import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import com.jchart.JchartFrameDestroyer;
import com.jchart.JchartInitializer;
import com.jchart.model.InitParmParser;
import com.jchart.model.JchartComposite;
import com.jchart.model.Kjchart;
import com.jchart.view.client.FrmJchart;
import com.jchart.view.client.LoadingPanel;
import com.jchart.view.client.PnlJchart;

//logging
//import org.apache.log4j.Category;

/**
 * Jchart - Financial Technical Analysis Software
 * Author: Paul Russo
 * Creation Date: Jan 8, 2000
 */
public class JchartApplet extends Applet 
    implements  Kjchart, JchartFrameDestroyer, ActionListener, Runnable, AppletParamAccessor {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//private final static Category log = Category.getInstance(JchartBase.class.getName());
    private boolean _loadingJchart = false;
    private boolean _isApplet  = true; 
    private boolean _isFloat = false; 
    private String _printUrl;
    //models
    private JchartComposite _jchartComposite = new JchartComposite();
	private JchartInitializer _jchartInitializer;
	private JchartComponent _jchartComponent;
	//panels, frame
	private PnlJchart _pnlJchartFloat;
	private PnlJchart _pnlJchart;
	private FrmJchart _frmJchart;
    //top level buttons
    private Button _btnFrameFloat = new Button("Float");
    private Panel _pnlButton;
    private Panel _centerPanel = new Panel();
    private Panel _topPanel = new Panel();
	

    public void init() {  
        getAppletContext().showStatus("Loading Jchart...");
        _pnlButton = new Panel();
        _pnlButton.setLayout(new FlowLayout(FlowLayout.LEFT));
        _pnlButton.add(_btnFrameFloat);	    
        _btnFrameFloat.addActionListener(this);
        _centerPanel.setLayout(new BorderLayout());
        _topPanel.setBackground(Color.yellow);
        _centerPanel.setBackground(Color.blue);
        setLayout(new BorderLayout());
        add(BorderLayout.NORTH, _pnlButton);
        _centerPanel.add(BorderLayout.CENTER,new LoadingPanel());
        add(BorderLayout.CENTER,_centerPanel);
        
    	_jchartInitializer = new JchartInitializer();
    	_jchartInitializer.initApplet(this);
    	_jchartComposite = _jchartInitializer.getJchartComposite();
        _jchartComponent = new JchartComponent(this);
        _jchartComponent.init(_jchartInitializer);
    }
    
	public void run() {
        jchartRun();
        _loadingJchart = false;
    }

	public void start() {
        initJchart();
    }
	
    public void stop() {
    	super.stop();
        _loadingJchart = false;
    }        

    public void destroyFrame() {
        dock();
    }
    
    public void destroy() {
    	System.out.println("Jchart exit");
    	_jchartComposite.getColorScheme().destroy();
    	super.destroy();
    }
    
    public void dock() {
        _pnlJchart.setVisible(true);
        _frmJchart.dispose();
        _pnlJchart.plotMain();
        _isFloat = false;
    }

    public void floatFrame() {
        if (_isFloat) return;
        InitParmParser initParmParser = _jchartInitializer.getInitParmParser();
        _isFloat = true;
        _pnlJchart.setVisible(false);
        _jchartComponent.floatFrame();
        _pnlJchartFloat = _jchartComponent.getPnlJchartFloat();
		_frmJchart = new FrmJchart(_pnlJchartFloat, _jchartComposite, this);
		_frmJchart.setTickerDir(initParmParser.getTickerDir());
		_frmJchart.addNotify();
		_frmJchart.repaint();
  		_frmJchart.setSize(700,400);
		_frmJchart.repaint();
        _frmJchart.invalidate(); //make it plot
		_frmJchart.setVisible(true);
		_pnlJchartFloat.plotMain();
  }

    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource(); 
        if (obj == _btnFrameFloat) {
            floatFrame();
        }
    }
    
    
    public void visitPrintUrl() {
    	if (!_isApplet)
    		return;
		String timeFrame = _jchartComposite.getTimeFrameModel().getPrintTimeFrame();
		String priceChart = _jchartComposite.getChartTypeModel().getPrintChartTypeText();
    	String location = _printUrl + "&symbol=" + _jchartComposite.getQuoteDataModel().getTicker() 
    		+ "&scale=" + timeFrame 
			+ "&type=" + priceChart; 
    		
		try {
			URL url = new URL(this.getDocumentBase(), location);
			//OpenBrowserWin.open(url,this);
			this.getAppletContext().showDocument(url,"_blank");
			} catch (MalformedURLException e) {
				System.out.println(_printUrl + _jchartComposite.getQuoteDataModel().getTicker() + "\n" + e);
			}	
    }

     public String getAppletInfo() {
        return "Name: Jchart\r\n" +
            "Web Site: http://www.jchart.com\r\n" +
            "Email: paul.russo@jchart.com";
    }

    /**
     * load jchart
     */
    public void loadJchart() {
	    _loadingJchart = false;
	    _centerPanel.removeAll(); 
        if (_isFloat) {
            _pnlJchartFloat = _jchartComponent.getPnlJchartFloat();
    	   	_centerPanel.add(BorderLayout.CENTER,_pnlJchartFloat);
        } else {
            _pnlJchart = _jchartComponent.getPnlJchart();
            _centerPanel.add(BorderLayout.CENTER,_pnlJchart);
        }
        _centerPanel.validate();
    }

    private void jchartLoadContent(int prevVersion,int currVersion) {
        if (_isFloat) {
            _centerPanel.remove(_pnlJchartFloat);
        } else {
        	_centerPanel.remove(_pnlJchart);
        }
        //prevent jchart from doing anything
		//_jchart.setAlive(false);
            
    }

    private void jchartRun() {
    	String ticker = _jchartComposite.getJchartRequest().getTicker();
    	_jchartComponent.tickerRequest(ticker, _isFloat);
/*        if (_loadingJchart) {
            long sleep = 1000; //1 second;
            while (_loadingJchart) {
                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException e) {}
            }
            this.setSize(_centerPanel.getSize());
*/            loadJchart();
            _jchartComponent.plotMain();
            repaint();               
//        }
    }
    
    private void jchartDock() {
        _jchartComponent.dock();
    }

    
    /**
     * initialize jchart in a separate thread
     */
    public void initJchart() {
    	_centerPanel.removeAll();
    	_centerPanel.add(BorderLayout.CENTER, new LoadingPanel());
    	_centerPanel.validate();
        _loadingJchart = true;
        Thread t = new Thread(this);
        t.start();
    }

	@Override
	public String getParam(String strName) {
		return getParameter(strName);
	}


}
