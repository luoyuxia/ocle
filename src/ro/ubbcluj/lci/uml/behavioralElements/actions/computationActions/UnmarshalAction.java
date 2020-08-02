package ro.ubbcluj.lci.uml.behavioralElements.actions.computationActions;

import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.PrimitiveAction;
import ro.ubbcluj.lci.uml.foundation.core.Class;

public interface UnmarshalAction extends PrimitiveAction {
   Class getUnmarshalType();

   void setUnmarshalType(Class var1);
}
