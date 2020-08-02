package ro.ubbcluj.lci.uml.modelManagement;

import java.util.Set;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;

public interface Subsystem extends Package, Classifier {
   boolean isInstantiable();

   void setInstantiable(boolean var1);

   Set allSpecificationElements();

   Set contents();
}
