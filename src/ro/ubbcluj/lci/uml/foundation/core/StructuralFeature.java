package ro.ubbcluj.lci.uml.foundation.core;

import ro.ubbcluj.lci.uml.foundation.dataTypes.Multiplicity;

public interface StructuralFeature extends Feature {
   int getChangeability();

   void setChangeability(int var1);

   int getTargetScope();

   void setTargetScope(int var1);

   int getOrdering();

   void setOrdering(int var1);

   Multiplicity getMultiplicity();

   void setMultiplicity(Multiplicity var1);

   Classifier getType();

   void setType(Classifier var1);
}
