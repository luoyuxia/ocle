package ro.ubbcluj.lci.uml.behavioralElements.actions.computationActions;

import ro.ubbcluj.lci.uml.foundation.core.DataType;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.uml.foundation.dataTypes.Multiplicity;

public interface ArgumentSpecification extends ModelElement {
   int getOrdering();

   void setOrdering(int var1);

   Multiplicity getMultiplicity();

   void setMultiplicity(Multiplicity var1);

   DataType getType();

   void setType(DataType var1);
}
