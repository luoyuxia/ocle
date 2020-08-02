package ro.ubbcluj.lci.gui.mainframe;

import javax.swing.JComponent;

public abstract class GView {
   private Object userObject;

   public GView() {
   }

   public abstract JComponent getComponent();

   public void setUserObject(Object object) {
      this.userObject = object;
   }

   public Object getUserObject() {
      return this.userObject;
   }
}
