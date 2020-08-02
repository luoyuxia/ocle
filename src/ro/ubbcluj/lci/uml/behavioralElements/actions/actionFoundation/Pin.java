package ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation;

import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.uml.foundation.dataTypes.Multiplicity;

public interface Pin extends ModelElement {
   int getOrdering();

   void setOrdering(int var1);

   Multiplicity getMultiplicity();

   void setMultiplicity(Multiplicity var1);

   Classifier getType();

   void setType(Classifier var1);
}
