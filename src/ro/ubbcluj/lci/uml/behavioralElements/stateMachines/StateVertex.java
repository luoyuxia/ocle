package ro.ubbcluj.lci.uml.behavioralElements.stateMachines;

import java.util.Enumeration;
import java.util.Set;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;

public interface StateVertex extends ModelElement {
   Enumeration getOutgoingList();

   Set getCollectionOutgoingList();

   void addOutgoing(Transition var1);

   void removeOutgoing(Transition var1);

   CompositeState getContainer();

   void setContainer(CompositeState var1);

   Enumeration getIncomingList();

   Set getCollectionIncomingList();

   void addIncoming(Transition var1);

   void removeIncoming(Transition var1);
}
