package com.jchart.view.client;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;

public class LoadingPanel extends Panel implements Runnable {
   private Image image = null;
   private Graphics imageG = null;
   private static final int PROGRESS_HEIGHT = 18;
   private static final int PROGRESS_WIDTH = 200;
   private int pctDone = 0;
   private Font font = new Font("SansSerif", Font.PLAIN, 10);
   private FontMetrics fontmetrics;
   private final static int BORDER_WIDTH = 2;
   private boolean loaded = false;

   public void init() {
      setBackground(Color.black);
      Thread t = new Thread(this);
      t.start();
   }

   public LoadingPanel() {
      super();
      init();
   }

   public void update(Graphics g) {
      image = createImage(getSize().width, getSize().height);
      imageG = image.getGraphics();
      imageG.setColor(Color.decode("#CCCCCC"));
      imageG.fillRect(0, 0, getSize().width, getSize().height);
      imageG.setColor(Color.black);
      imageG.fillRect(0, 0, getSize().width, getSize().height);
      fontmetrics = imageG.getFontMetrics(font);
      imageG.setFont(font);
      imageG.setColor(Color.white);

      int yPos = (int) (getSize().height / 2) - PROGRESS_HEIGHT;
      imageG.drawString("Loading Components... ",
            (int) (getSize().width
                  - fontmetrics.stringWidth("Loading Components... ")) / 2,
            yPos - 20);
      int xPos = ((int) (getSize().width - PROGRESS_WIDTH) / 2);

      imageG.setColor(Color.decode("#CCCCCC"));
      imageG.drawRect(xPos, yPos, PROGRESS_WIDTH + BORDER_WIDTH,
            PROGRESS_HEIGHT + BORDER_WIDTH);
      imageG.setColor(Color.gray);
      imageG.fillRect(xPos + BORDER_WIDTH, yPos + BORDER_WIDTH, PROGRESS_WIDTH,
            PROGRESS_HEIGHT);

      pctDone += 10;
      imageG.setColor(Color.blue);
      imageG.fillRect(xPos + BORDER_WIDTH, yPos + BORDER_WIDTH, pctDone,
            PROGRESS_HEIGHT);
      if (pctDone < 100)
         imageG.setColor(Color.black);
      else
         imageG.setColor(Color.white);
      imageG.drawString(pctDone / 2 + "%", xPos + 90,
            yPos + fontmetrics.getHeight() + BORDER_WIDTH);
      g.drawImage(image, 0, 0, this);
   }

   /**
    * stops the thread
    */
   public void setLoaded() {
      this.loaded = true;
   }

   /**
    * this thread blocks on io call
    */
   public void run() {
      long sleep = 300; // 0.5 seconds;
      do {
         repaint();
         try {
            Thread.sleep(sleep);
         } catch (InterruptedException i) {
         }
         // if (pctDone == PROGRESS_WIDTH)
         // pctDone = 0;
      } while (pctDone != PROGRESS_WIDTH);
   }

}
