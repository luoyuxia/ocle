package ro.ubbcluj.lci.gui.editor.mdi;

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
