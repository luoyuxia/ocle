package ro.ubbcluj.lci.uml.behavioralElements.collaborations;

import java.util.Enumeration;
import java.util.Set;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;

public interface Interaction extends ModelElement {
   Enumeration getMessageList();

   Set getCollectionMessageList();

   void addMessage(Message var1);

   void removeMessage(Message var1);

   Collaboration getContext();

   void setContext(Collaboration var1);

   Enumeration getInteractionInstanceSetList();

   Set getCollectionInteractionInstanceSetList();

   void addInteractionInstanceSet(InteractionInstanceSet var1);

   void removeInteractionInstanceSet(InteractionInstanceSet var1);
}
