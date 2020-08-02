package ro.ubbcluj.lci.gui.editor.event;

import java.util.EventListener;
import ro.ubbcluj.lci.gui.editor.AbstractPad;

public interface ViewActivationListener extends EventListener {
   void viewActivated(ViewActivationEvent var1);

   void viewClosed(AbstractPad var1);
}
