package ro.ubbcluj.lci.uml.behavioralElements.activityGraphs;

import java.util.Enumeration;
import java.util.Set;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Procedure;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.SimpleState;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.core.Parameter;

public interface ObjectFlowState extends SimpleState {
   boolean isSynch();

   void setSynch(boolean var1);

   Enumeration getParameterList();

   Set getCollectionParameterList();

   void addParameter(Parameter var1);

   void removeParameter(Parameter var1);

   Classifier getType();

   void setType(Classifier var1);

   Set allNextLeafStates();

   Set allPreviousLeafStates();

   boolean isInputAction(Procedure var1);

   boolean isOutputAction(Procedure var1);
}
