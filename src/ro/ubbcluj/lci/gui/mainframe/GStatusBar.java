package ro.ubbcluj.lci.gui.mainframe;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

public class GStatusBar extends JLabel {
   private static GStatusBar statusBar = new GStatusBar();

   protected GStatusBar() {
      super("   ");
      this.setBorder(BorderFactory.createEtchedBorder());
   }

   public static GStatusBar getStatusBar() {
      return statusBar;
   }

   public void reset() {
      this.setText("");
   }

   public void print(String text) {
      this.setText(text);
   }
}
