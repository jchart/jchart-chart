package com.jchart.view.client;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.jchart.model.color.ColorScheme;
import com.jchart.view.common.PnlJchartIntr;

public class DlgColor extends Dialog implements ActionListener {
   private static final Insets insets = new Insets(10, 10, 10, 10);
   private ColorScheme _colorScheme;
   private PnlJchartIntr _pnlJchartIntr;
   private boolean _isComplete;
   private Frame _frame;

   DlgColor(Frame frame, ColorScheme colorScheme, PnlJchartIntr pnlJchartIntr) {
      super(frame, "Set Color", true);
      _frame = frame;
      _colorScheme = colorScheme;
      _pnlJchartIntr = pnlJchartIntr;

      this.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent e) {
            setVisible(false);
         }
      });

      int numColors = _colorScheme.getNumColors();
      Panel p1 = new Panel();
      p1.setLayout(new GridLayout(numColors + 4, 2));// Math.round(numColors /2)
      p1.add(new Label(" "));

      Button b;
      for (int i = 0; i < numColors; i++) {
         b = new Button(_colorScheme.getColorName(i));
         b.addActionListener(this);
         p1.add(b);
      }
      b = new Button("OK");
      b.addActionListener(this);
      p1.add(b);
      p1.add(new Label("          "));
      p1.add(new Label("          "));
      add(BorderLayout.CENTER, p1);

      // Initialize this dialog to its preferred size.
      pack();
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      Dimension d = getSize();
      setLocation(screenSize.width / 2 - (int) d.width / 2,
            screenSize.height / 2 - (int) d.height / 2);
   }

   public Insets getInsets() {
      return insets;
   }

   public void actionPerformed(ActionEvent event) {
      Object source = event.getSource();
      if (source instanceof Button) {
         Button b = (Button) source;
         for (int i = 0; i < _colorScheme.getNumColors(); i++) {
            if (b.getLabel().equals(_colorScheme.getColorName(i))) {
               _colorScheme.setColor(i, i);
               _pnlJchartIntr.plotMain();
            }
         }
         if (b.getLabel().equals("OK")) {
            setVisible(false);
            dispose();
            _frame.dispose();
         }

      }
   }

   public boolean isComplete() {
      return _isComplete;
   }
}
