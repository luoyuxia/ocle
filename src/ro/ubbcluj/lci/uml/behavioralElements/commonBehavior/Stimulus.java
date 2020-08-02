package ro.ubbcluj.lci.uml.behavioralElements.commonBehavior;

import java.util.Enumeration;
import java.util.Set;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.InteractionInstanceSet;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.Message;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;

public interface Stimulus extends ModelElement {
   Procedure getDispatchAction();

   void setDispatchAction(Procedure var1);

   Enumeration getArgumentList();

   Set getCollectionArgumentList();

   void addArgument(Instance var1);

   void removeArgument(Instance var1);

   Link getCommunicationLink();

   void setCommunicationLink(Link var1);

   Instance getSender();

   void setSender(Instance var1);

   Enumeration getInteractionInstanceSetList();

   Set getCollectionInteractionInstanceSetList();

   void addInteractionInstanceSet(InteractionInstanceSet var1);

   void removeInteractionInstanceSet(InteractionInstanceSet var1);

   Enumeration getPlayedRoleList();

   Set getCollectionPlayedRoleList();

   void addPlayedRole(Message var1);

   void removePlayedRole(Message var1);

   Instance getReceiver();

   void setReceiver(Instance var1);
}
