package ro.ubbcluj.lci.uml.behavioralElements.collaborations;

import java.util.Enumeration;
import java.util.Set;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Link;
import ro.ubbcluj.lci.uml.foundation.core.Association;
import ro.ubbcluj.lci.uml.foundation.dataTypes.Multiplicity;

public interface AssociationRole extends Association {
   Multiplicity getMultiplicity();

   void setMultiplicity(Multiplicity var1);

   Enumeration getConformingLinkList();

   Set getCollectionConformingLinkList();

   void addConformingLink(Link var1);

   void removeConformingLink(Link var1);

   Association getBase();

   void setBase(Association var1);

   Enumeration getMessageList();

   Set getCollectionMessageList();

   void addMessage(Message var1);

   void removeMessage(Message var1);
}
