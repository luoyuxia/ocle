package ro.ubbcluj.lci.gui.editor.event;

import java.util.EventObject;
import ro.ubbcluj.lci.gui.editor.AbstractPad;

public class ViewActivationEvent extends EventObject {
   private AbstractPad activatedView;
   private AbstractPad deactivatedView;

   public ViewActivationEvent(Object source, AbstractPad activated, AbstractPad deactivated) {
      super(source);
      this.activatedView = activated;
      this.deactivatedView = deactivated;
   }

   public AbstractPad getActivePad() {
      return this.activatedView;
   }

   public AbstractPad getOldActivePad() {
      return this.deactivatedView;
   }
}
