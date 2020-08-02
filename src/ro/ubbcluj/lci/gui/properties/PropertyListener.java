package ro.ubbcluj.lci.gui.properties;

import java.util.EventListener;

public interface PropertyListener extends EventListener {
   void valueChanged(PropertyEvent var1);

   void subjectChanged(PropertyEvent var1);
}
