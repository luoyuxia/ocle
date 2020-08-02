package ro.ubbcluj.lci.uml.foundation.core;

import java.util.Set;
import ro.ubbcluj.lci.uml.behavioralElements.activityGraphs.ObjectFlowState;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.Event;
import ro.ubbcluj.lci.uml.foundation.dataTypes.Expression;

public interface Parameter extends ModelElement {
   int getKind();

   void setKind(int var1);

   Expression getDefaultValue();

   void setDefaultValue(Expression var1);

   Event getEvent();

   void setEvent(Event var1);

   java.util.Enumeration getStateList();

   Set getCollectionStateList();

   void addState(ObjectFlowState var1);

   void removeState(ObjectFlowState var1);

   Classifier getType();

   void setType(Classifier var1);

   BehavioralFeature getBehavioralFeature();

   void setBehavioralFeature(BehavioralFeature var1);
}
