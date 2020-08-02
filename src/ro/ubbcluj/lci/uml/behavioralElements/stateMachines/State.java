package ro.ubbcluj.lci.uml.behavioralElements.stateMachines;

import java.util.Enumeration;
import java.util.Set;
import ro.ubbcluj.lci.uml.behavioralElements.activityGraphs.ClassifierInState;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Procedure;

public interface State extends StateVertex {
   Procedure getExit();

   void setExit(Procedure var1);

   Procedure getDoActivity();

   void setDoActivity(Procedure var1);

   StateMachine getStateMachine();

   void setStateMachine(StateMachine var1);

   Enumeration getInternalTransitionList();

   Set getCollectionInternalTransitionList();

   void addInternalTransition(Transition var1);

   void removeInternalTransition(Transition var1);

   Procedure getEntry();

   void setEntry(Procedure var1);

   Enumeration getDeferrableEventList();

   Set getCollectionDeferrableEventList();

   void addDeferrableEvent(Event var1);

   void removeDeferrableEvent(Event var1);

   Enumeration getClassifierInStateList();

   Set getCollectionClassifierInStateList();

   void addClassifierInState(ClassifierInState var1);

   void removeClassifierInState(ClassifierInState var1);
}
