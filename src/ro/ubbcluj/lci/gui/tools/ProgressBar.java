package ro.ubbcluj.lci.gui.tools;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import ro.ubbcluj.lci.gui.mainframe.GStatusBar;
import ro.ubbcluj.lci.utils.progress.ProgressListener;
import ro.ubbcluj.lci.xmi.XMISerializer;

public class ProgressBar implements ProgressListener {
   public static final Color OPEN_COLOR = new Color(26316);
   public static final Color SAVE_COLOR = new Color(13382400);
   private static JWindow window;
   private static JPanel p;
   private static int internal_inc;
   private static ProgressBar progress_bar = null;
   private static JProgressBar internal_bar;
   private static JLabel label;
   private ProgressBar.ProgressThread pt;
   private boolean flag;

   public static ProgressBar getProgressBar(Frame parent) {
      if (progress_bar == null) {
         progress_bar = new ProgressBar(parent);
      }

      progress_bar.init();
      return progress_bar;
   }

   protected ProgressBar(Frame parent) {
      internal_bar = new JProgressBar(0, 0, 100);
      internal_bar.setForeground(Color.blue);
      internal_bar.setBorderPainted(true);
      internal_bar.setStringPainted(true);
      Point location = parent.getLocation();
      Dimension psize = parent.getSize();
      Dimension winsize = new Dimension(170, 60);
      Point win_loc = new Point(Math.abs(psize.width / 2 - winsize.width / 2) + location.x, Math.abs(psize.height / 2 - winsize.height / 2) + location.y);
      window = new JWindow(parent);
      window.setSize(winsize);
      window.setLocation(win_loc);
      window.getRootPane().setBorder(new LineBorder(Color.black, 2));
      int vspace = (winsize.height - 2 * internal_bar.getPreferredSize().height) / 6;
      p = new JPanel();
      p.setLayout(new BoxLayout(p, 1));
      p.add(Box.createRigidArea(new Dimension(0, vspace)));
      JPanel lp = new JPanel();
      label = new JLabel();
      label.setAlignmentX(0.0F);
      label.setAlignmentY(0.5F);
      lp.setLayout(new BoxLayout(lp, 0));
      lp.add(label);
      lp.add(Box.createHorizontalGlue());
      p.add(lp);
      p.add(Box.createRigidArea(new Dimension(0, vspace)));
      JPanel ip = new JPanel();
      ip.setLayout(new BoxLayout(ip, 0));
      ip.add(internal_bar);
      ip.add(Box.createHorizontalGlue());
      p.add(ip);
      p.add(Box.createRigidArea(new Dimension(0, vspace)));
      XMISerializer.getInstance().getDecoder().setProgressListener(this);
      this.init();
   }

   public void disableProgressString() {
      internal_bar.setStringPainted(false);
   }

   public void enableProgressString() {
      internal_bar.setStringPainted(true);
   }

   private void init() {
      internal_bar.setIndeterminate(false);
      internal_bar.setValue(0);
      internal_inc = 5;
      JPanel aux = new JPanel();
      aux.add(p);
      window.getContentPane().removeAll();
      window.getContentPane().add(aux);
   }

   private void finish() {
      window.setVisible(false);
      window.dispose();
   }

   public void startOld(String title) {
      label.setText(title);
      window.setVisible(true);
      this.pt = new ProgressBar.ProgressThread(title);
      this.flag = true;
      this.pt.start();
   }

   public void stopOld() {
      this.flag = false;
      window.setVisible(false);
   }

   public void startProgress(String title) {
      label.setText(title);
      window.setVisible(true);
      this.flag = true;
   }

   public void stopProgress() {
      window.setVisible(false);
   }

   public void setIncrement(int value) {
      if (value > 0) {
         internal_inc = value;
         internal_bar.setIndeterminate(false);
         internal_bar.setValue(0);
      } else {
         internal_bar.setIndeterminate(true);
      }

   }

   public void setColor(Color color) {
      internal_bar.setForeground(color);
   }

   public void start(String filename) {
   }

   public void progressValueChanged(int percent, Object details) {
      internal_bar.setValue(percent);

      try {
         Thread.sleep(10L);
      } catch (Exception var4) {
      }

   }

   public void stop() {
   }

   class ProgressThread extends Thread {
      private String name;
      private int actual_value = 0;
      Runnable finish = new Runnable() {
         public void run() {
            ProgressBar.this.finish();
         }
      };
      Runnable done = new Runnable() {
         public void run() {
            ProgressThread.this.updateProgressBar(ProgressBar.internal_bar.getMaximum());
         }
      };
      Runnable update = new Runnable() {
         public void run() {
            if (!ProgressBar.internal_bar.isIndeterminate()) {
               ProgressThread.this.updateProgressBar(ProgressBar.internal_bar.getValue() + ProgressBar.internal_inc);
            }

         }
      };

      protected ProgressThread(String text) {
         this.name = text;
      }

      protected void updateProgressBar(int value) {
         this.actual_value = value;
         ProgressBar.internal_bar.setValue(value);
      }

      public void run() {
         boolean var1 = false;

         try {
            while(ProgressBar.this.flag) {
               Thread.currentThread();
               Thread.sleep(100L);
               if (this.actual_value + ProgressBar.internal_inc <= ProgressBar.internal_bar.getMaximum() - 1) {
                  SwingUtilities.invokeAndWait(this.update);
               }
            }

            SwingUtilities.invokeAndWait(this.done);
            Thread.currentThread();
            Thread.sleep(100L);
            SwingUtilities.invokeAndWait(this.finish);
         } catch (Exception var3) {
            var3.printStackTrace();
         }

         GStatusBar.getStatusBar().print("Finished");
      }
   }
}
