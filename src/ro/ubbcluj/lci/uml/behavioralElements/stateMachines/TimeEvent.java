package ro.ubbcluj.lci.uml.behavioralElements.stateMachines;

import ro.ubbcluj.lci.uml.foundation.dataTypes.TimeExpression;

public interface TimeEvent extends Event {
   TimeExpression getWhen();

   void setWhen(TimeExpression var1);
}
