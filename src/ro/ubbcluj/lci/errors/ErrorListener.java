package ro.ubbcluj.lci.errors;

import java.util.EventListener;

public interface ErrorListener extends EventListener {
   void errorOccured(ErrorMessage var1);
}
