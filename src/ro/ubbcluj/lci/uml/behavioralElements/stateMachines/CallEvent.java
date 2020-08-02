package ro.ubbcluj.lci.uml.behavioralElements.stateMachines;

import ro.ubbcluj.lci.uml.foundation.core.Operation;

public interface CallEvent extends Event {
   Operation getOperation();

   void setOperation(Operation var1);
}
