package ro.ubbcluj.lci.uml.behavioralElements.stateMachines;

import java.util.Enumeration;
import java.util.Set;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;

public interface StateMachine extends ModelElement {
   Enumeration getSubmachineStateList();

   Set getCollectionSubmachineStateList();

   void addSubmachineState(SubmachineState var1);

   void removeSubmachineState(SubmachineState var1);

   State getTop();

   void setTop(State var1);

   ModelElement getContext();

   void setContext(ModelElement var1);

   Enumeration getTransitionsList();

   Set getCollectionTransitionsList();

   void addTransitions(Transition var1);

   void removeTransitions(Transition var1);

   CompositeState LCA(State var1, State var2);

   boolean ancestor(State var1, State var2);
}
