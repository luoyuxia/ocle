package ro.ubbcluj.lci.uml.behavioralElements.collaborations;

import java.util.Enumeration;
import java.util.Set;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.Action;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Procedure;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Stimulus;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;

public interface Message extends ModelElement {
   Enumeration getConformingStimulusList();

   Set getCollectionConformingStimulusList();

   void addConformingStimulus(Stimulus var1);

   void removeConformingStimulus(Stimulus var1);

   Action getAction();

   void setAction(Action var1);

   Enumeration getPredecessorList();

   Set getCollectionPredecessorList();

   void addPredecessor(Message var1);

   void removePredecessor(Message var1);

   Procedure getProcedure();

   void setProcedure(Procedure var1);

   Interaction getInteraction();

   void setInteraction(Interaction var1);

   Enumeration getSuccessorList();

   Set getCollectionSuccessorList();

   void addSuccessor(Message var1);

   void removeSuccessor(Message var1);

   Message getActivator();

   void setActivator(Message var1);

   ClassifierRole getReceiver();

   void setReceiver(ClassifierRole var1);

   ClassifierRole getSender();

   void setSender(ClassifierRole var1);

   AssociationRole getCommunicationConnection();

   void setCommunicationConnection(AssociationRole var1);

   Enumeration getMessageList();

   Set getCollectionMessageList();

   void addMessage(Message var1);

   void removeMessage(Message var1);

   Set allPredecessors();
}
