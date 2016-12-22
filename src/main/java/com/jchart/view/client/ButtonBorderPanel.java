package com.jchart.view.client;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Panel;

public class ButtonBorderPanel extends Panel {
   private static final Insets insets = new Insets(2, 0, 0, 0);

   public Insets getInsets() {
      return insets;
   }

   public ButtonBorderPanel() {
      setBackground(Color.decode("#CCCCCC"));
      setLayout(new GridLayout(0, 1));
   }
}
