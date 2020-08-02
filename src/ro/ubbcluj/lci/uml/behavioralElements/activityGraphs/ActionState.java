package ro.ubbcluj.lci.uml.behavioralElements.activityGraphs;

import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.SimpleState;
import ro.ubbcluj.lci.uml.foundation.dataTypes.ArgListsExpression;
import ro.ubbcluj.lci.uml.foundation.dataTypes.Multiplicity;

public interface ActionState extends SimpleState {
   boolean isDynamic();

   void setDynamic(boolean var1);

   ArgListsExpression getDynamicArguments();

   void setDynamicArguments(ArgListsExpression var1);

   Multiplicity getDynamicMultiplicity();

   void setDynamicMultiplicity(Multiplicity var1);
}
