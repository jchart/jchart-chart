package com.jchart.view.client;

import java.awt.Button;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.jchart.io.TickerListener;
import com.jchart.io.TickerRequest;
import com.jchart.model.JchartComposite;
import com.jchart.model.QuoteDataModel;

public class DlgRs extends Dialog implements ActionListener, TickerListener {
   private JchartComposite _jchartComposite;
   private TextField _tfRs;
   private Button _setButton;
   private QuoteDataModel _quoteTypeModel;
   private Frame _frame;

   DlgRs(Frame frame, JchartComposite jchartComposite) {
      super(frame, "Relative Strength Symbol", true);
      _frame = frame;
      _jchartComposite = jchartComposite;
      _quoteTypeModel = _jchartComposite.getQuoteDataModel();

      this.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent e) {
            setVisible(false);
         }
      });

      // Create middle section.
      Panel p1 = new Panel();
      p1.setLayout(new GridLayout(1, 1));
      Label label = new Label("RS Symbol:", Label.RIGHT);
      p1.add(label);
      _tfRs = new TextField(_quoteTypeModel.getRsSymbol() + "", 5);
      _tfRs.addActionListener(this);
      p1.add(_tfRs);

      add("Center", p1);

      // Create bottom row.
      Panel p2 = new Panel();
      p2.setLayout(new FlowLayout(FlowLayout.RIGHT));
      Button b = new Button("Cancel");
      b.addActionListener(this);
      _setButton = new Button("Set");
      _setButton.addActionListener(this);
      p2.add(b);
      p2.add(_setButton);
      add("South", p2);

      // Initialize this dialog to its preferred size.
      pack();

      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      Dimension d = getSize();
      setLocation((screenSize.width / 2) - ((int) d.width / 2),
            (screenSize.height / 2) - ((int) d.height / 2));

   }

   public void actionPerformed(ActionEvent event) {
      Object source = event.getSource();
      if (source == _setButton) {
         String rsSymbol = _tfRs.getText();
         if (rsSymbol.trim().length() > 0) {
            TickerRequest tickerRequest = new TickerRequest(_jchartComposite,
                  this);
            tickerRequest.requestBlock(rsSymbol);
         }
         setVisible(false);
         dispose();
         _frame.dispose();
      }
   }

   public void requestComplete(boolean found) {
   }

}
