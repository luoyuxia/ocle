package ro.ubbcluj.lci.gui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Toolkit;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;

public class SplashScreen extends JWindow {
   private JLabel label;
   private static Runnable waitRunner;

   public SplashScreen(String image, Frame f) {
      super(f);
    //  System.out.println(this.getClass().getResource("/").getPath());
      this.label = new JLabel("");
      this.getContentPane().add(this.label, "Center");
      this.pack();
      this.getRootPane().setBorder(BorderFactory.createRaisedBevelBorder());
      Dimension label_size = this.label.getPreferredSize();
      Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
      Point location = new Point(screen.width / 2 - label_size.width / 2, screen.height / 2 - label_size.height / 2);
      this.setLocation(location);
      this.setCursor(new Cursor(3));
      final Runnable closeRunner = new Runnable() {
         public void run() {
            SplashScreen.this.setVisible(false);
            SplashScreen.this.dispose();
         }
      };
      waitRunner = new Runnable() {
         public synchronized void run() {
            try {
               this.wait();
               SwingUtilities.invokeAndWait(closeRunner);
            } catch (Exception var2) {
               var2.printStackTrace();
            }

         }
      };
      this.setVisible(true);
      Thread splashThread = new Thread(waitRunner, "Splash Thread");
      splashThread.start();
   }

   public static void signal() {
      synchronized(waitRunner) {
         waitRunner.notify();
      }
   }
}
