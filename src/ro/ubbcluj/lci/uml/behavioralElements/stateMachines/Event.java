package ro.ubbcluj.lci.uml.behavioralElements.stateMachines;

import java.util.Enumeration;
import java.util.Set;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.uml.foundation.core.Parameter;

public interface Event extends ModelElement {
   Enumeration getParameterList();

   Set getCollectionParameterList();

   void addParameter(Parameter var1);

   void removeParameter(Parameter var1);

   Enumeration getStateList();

   Set getCollectionStateList();

   void addState(State var1);

   void removeState(State var1);

   Enumeration getTransitionList();

   Set getCollectionTransitionList();

   void addTransition(Transition var1);

   void removeTransition(Transition var1);
}
