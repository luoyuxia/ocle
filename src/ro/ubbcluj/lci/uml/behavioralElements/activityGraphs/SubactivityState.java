package ro.ubbcluj.lci.uml.behavioralElements.activityGraphs;

import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.SubmachineState;
import ro.ubbcluj.lci.uml.foundation.dataTypes.ArgListsExpression;
import ro.ubbcluj.lci.uml.foundation.dataTypes.Multiplicity;

public interface SubactivityState extends SubmachineState {
   boolean isDynamic();

   void setDynamic(boolean var1);

   Multiplicity getDynamicMultiplicity();

   void setDynamicMultiplicity(Multiplicity var1);

   ArgListsExpression getDynamicArguments();

   void setDynamicArguments(ArgListsExpression var1);
}
