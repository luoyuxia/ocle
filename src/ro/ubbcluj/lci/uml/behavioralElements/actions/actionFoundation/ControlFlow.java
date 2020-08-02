package ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation;

import ro.ubbcluj.lci.uml.foundation.core.ModelElement;

public interface ControlFlow extends ModelElement {
   Action getSuccessor();

   void setSuccessor(Action var1);

   Action getPredecessor();

   void setPredecessor(Action var1);
}
