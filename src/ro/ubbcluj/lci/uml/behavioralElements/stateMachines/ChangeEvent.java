package ro.ubbcluj.lci.uml.behavioralElements.stateMachines;

import ro.ubbcluj.lci.uml.foundation.dataTypes.BooleanExpression;

public interface ChangeEvent extends Event {
   BooleanExpression getChangeExpression();

   void setChangeExpression(BooleanExpression var1);
}
