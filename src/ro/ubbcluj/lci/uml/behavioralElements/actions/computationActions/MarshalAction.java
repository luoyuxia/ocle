package ro.ubbcluj.lci.uml.behavioralElements.actions.computationActions;

import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.PrimitiveAction;
import ro.ubbcluj.lci.uml.foundation.core.Class;

public interface MarshalAction extends PrimitiveAction {
   Class getMarshalType();

   void setMarshalType(Class var1);
}
