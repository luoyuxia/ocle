package ro.ubbcluj.lci.gui.browser;

import javax.swing.JMenu;

class GMenu extends JMenu implements Validator {
   public GMenu(String name) {
      super(name);
   }

   public void refreshValidation() {
      for(int i = 0; i < this.getMenuComponentCount(); ++i) {
         Object comp = this.getMenuComponent(i);
         if (comp instanceof Validator) {
            Validator v = (Validator)comp;
            v.refreshValidation();
         }
      }

   }
}
