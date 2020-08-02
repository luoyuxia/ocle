package ro.ubbcluj.lci.gui.browser;

import javax.swing.Action;
import javax.swing.JMenuItem;

class GMenuItem extends JMenuItem implements Validator {
   public GMenuItem(Action a) {
      super(a);
   }

   public void refreshValidation() {
      Action aa = this.getAction();
      if (aa instanceof Validator) {
         ((Validator)aa).refreshValidation();
      }

   }
}
