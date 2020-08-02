package ro.ubbcluj.lci.uml.behavioralElements.stateMachines;

import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.uml.foundation.dataTypes.BooleanExpression;

public interface Guard extends ModelElement {
   BooleanExpression getExpression();

   void setExpression(BooleanExpression var1);

   Transition getTransition();

   void setTransition(Transition var1);
}
