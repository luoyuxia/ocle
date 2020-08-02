package ro.ubbcluj.lci.utils;

import java.awt.Font;
import javax.swing.JLabel;

public class BlinkerThread extends InterruptibleTask {
   private JLabel component;
   private int delay;
   private Font basic;
   private Font bold;

   public BlinkerThread(JLabel c, int delay) {
      this.component = c;
      this.delay = delay;
      this.basic = c.getFont();
      if (this.basic.isBold()) {
         this.basic = this.basic.deriveFont(0, this.basic.getSize2D());
         c.setFont(this.basic);
      }

      this.bold = this.basic.deriveFont(1);
   }

   public void realRun() {
      while(!this.isCancelled) {
         try {
            Thread.sleep((long)this.delay);
         } catch (InterruptedException var5) {
         }

         synchronized(this.component) {
            if (this.basic.equals(this.component.getFont())) {
               this.component.setFont(this.bold);
            } else {
               this.component.setFont(this.basic);
            }
         }
      }

      synchronized(this.component) {
         this.component.setFont(this.basic);
      }
   }
}
