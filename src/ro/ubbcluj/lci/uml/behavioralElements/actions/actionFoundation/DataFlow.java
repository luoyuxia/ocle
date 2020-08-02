package ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation;

import ro.ubbcluj.lci.uml.foundation.core.ModelElement;

public interface DataFlow extends ModelElement {
   InputPin getDestination();

   void setDestination(InputPin var1);

   OutputPin getSource();

   void setSource(OutputPin var1);
}
