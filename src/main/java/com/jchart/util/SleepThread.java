package com.jchart.util;

/**
 * this class is needed because thread.interrupt causes applet exception in
 * jdk1.4
 */
public abstract class SleepThread extends Thread {
   private boolean wake;

   protected void threadSleep(long sleepMillis) {
      // System.out.println(this + " " + getClass());
      this.wake = false;
      try {
         Thread.sleep(1000);
      } catch (InterruptedException e) {
      }
      snooze(sleepMillis);
   }

   protected void threadWake() {
      this.wake = true;
   }

   private void snooze(long sleep) {
      // System.out.println(this + " snooze " + getClass());
      long now = System.currentTimeMillis();
      long stop = now + sleep;
      while (now < stop) {
         now = System.currentTimeMillis();
         try {
            Thread.sleep(1000);
         } catch (InterruptedException e) {
         }
         if (wake)
            break;
      }
      this.wake = true;
      // System.out.println(this + " snoose done " + getClass());
   }
}
