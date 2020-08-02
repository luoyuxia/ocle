package ro.ubbcluj.lci.uml.behavioralElements.collaborations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import ro.ubbcluj.lci.codegen.framework.ocl.CollectionUtilities;
import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.Action;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Procedure;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Stimulus;
import ro.ubbcluj.lci.uml.foundation.core.ModelElementImpl;

public class MessageImpl extends ModelElementImpl implements Message {
   protected Set theConformingStimulusList;
   protected Action theAction;
   protected Set thePredecessorList;
   protected Procedure theProcedure;
   protected Interaction theInteraction;
   protected Set theSuccessorList;
   protected Message theActivator;
   protected ClassifierRole theReceiver;
   protected ClassifierRole theSender;
   protected AssociationRole theCommunicationConnection;
   protected Set theMessageList;

   public MessageImpl() {
   }

   public Set getCollectionConformingStimulusList() {
      return this.theConformingStimulusList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theConformingStimulusList);
   }

   public Enumeration getConformingStimulusList() {
      return Collections.enumeration(this.getCollectionConformingStimulusList());
   }

   public void addConformingStimulus(Stimulus arg) {
      if (arg != null) {
         if (this.theConformingStimulusList == null) {
            this.theConformingStimulusList = new LinkedHashSet();
         }

         if (this.theConformingStimulusList.add(arg)) {
            arg.addPlayedRole(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "conformingStimulus", 1));
            }
         }
      }

   }

   public void removeConformingStimulus(Stimulus arg) {
      if (this.theConformingStimulusList != null && arg != null && this.theConformingStimulusList.remove(arg)) {
         arg.removePlayedRole(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "conformingStimulus", 2));
         }
      }

   }

   public Action getAction() {
      return this.theAction;
   }

   public void setAction(Action arg) {
      if (this.theAction != arg) {
         Action temp = this.theAction;
         this.theAction = null;
         if (temp != null) {
            temp.removeMessage(this);
         }

         if (arg != null) {
            this.theAction = arg;
            arg.addMessage(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "action", 0));
         }
      }

   }

   public Set getCollectionPredecessorList() {
      return this.thePredecessorList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.thePredecessorList);
   }

   public Enumeration getPredecessorList() {
      return Collections.enumeration(this.getCollectionPredecessorList());
   }

   public void addPredecessor(Message arg) {
      if (arg != null) {
         if (this.thePredecessorList == null) {
            this.thePredecessorList = new LinkedHashSet();
         }

         if (this.thePredecessorList.add(arg)) {
            arg.addSuccessor(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "predecessor", 1));
            }
         }
      }

   }

   public void removePredecessor(Message arg) {
      if (this.thePredecessorList != null && arg != null && this.thePredecessorList.remove(arg)) {
         arg.removeSuccessor(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "predecessor", 2));
         }
      }

   }

   public Procedure getProcedure() {
      return this.theProcedure;
   }

   public void setProcedure(Procedure arg) {
      if (this.theProcedure != arg) {
         Procedure temp = this.theProcedure;
         this.theProcedure = null;
         if (temp != null) {
            temp.removeMessage(this);
         }

         if (arg != null) {
            this.theProcedure = arg;
            arg.addMessage(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "procedure", 0));
         }
      }

   }

   public Interaction getInteraction() {
      return this.theInteraction;
   }

   public void setInteraction(Interaction arg) {
      if (this.theInteraction != arg) {
         Interaction temp = this.theInteraction;
         this.theInteraction = null;
         if (temp != null) {
            temp.removeMessage(this);
         }

         if (arg != null) {
            this.theInteraction = arg;
            arg.addMessage(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "interaction", 0));
         }
      }

   }

   public Set getCollectionSuccessorList() {
      return this.theSuccessorList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theSuccessorList);
   }

   public Enumeration getSuccessorList() {
      return Collections.enumeration(this.getCollectionSuccessorList());
   }

   public void addSuccessor(Message arg) {
      if (arg != null) {
         if (this.theSuccessorList == null) {
            this.theSuccessorList = new LinkedHashSet();
         }

         if (this.theSuccessorList.add(arg)) {
            arg.addPredecessor(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "successor", 1));
            }
         }
      }

   }

   public void removeSuccessor(Message arg) {
      if (this.theSuccessorList != null && arg != null && this.theSuccessorList.remove(arg)) {
         arg.removePredecessor(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "successor", 2));
         }
      }

   }

   public Message getActivator() {
      return this.theActivator;
   }

   public void setActivator(Message arg) {
      if (this.theActivator != arg) {
         Message temp = this.theActivator;
         this.theActivator = null;
         if (temp != null) {
            temp.removeMessage(this);
         }

         if (arg != null) {
            this.theActivator = arg;
            arg.addMessage(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "activator", 0));
         }
      }

   }

   public ClassifierRole getReceiver() {
      return this.theReceiver;
   }

   public void setReceiver(ClassifierRole arg) {
      if (this.theReceiver != arg) {
         ClassifierRole temp = this.theReceiver;
         this.theReceiver = null;
         if (temp != null) {
            temp.removeMessage(this);
         }

         if (arg != null) {
            this.theReceiver = arg;
            arg.addMessage(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "receiver", 0));
         }
      }

   }

   public ClassifierRole getSender() {
      return this.theSender;
   }

   public void setSender(ClassifierRole arg) {
      if (this.theSender != arg) {
         ClassifierRole temp = this.theSender;
         this.theSender = null;
         if (temp != null) {
            temp.removeMessage1(this);
         }

         if (arg != null) {
            this.theSender = arg;
            arg.addMessage1(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "sender", 0));
         }
      }

   }

   public AssociationRole getCommunicationConnection() {
      return this.theCommunicationConnection;
   }

   public void setCommunicationConnection(AssociationRole arg) {
      if (this.theCommunicationConnection != arg) {
         AssociationRole temp = this.theCommunicationConnection;
         this.theCommunicationConnection = null;
         if (temp != null) {
            temp.removeMessage(this);
         }

         if (arg != null) {
            this.theCommunicationConnection = arg;
            arg.addMessage(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "communicationConnection", 0));
         }
      }

   }

   public Set getCollectionMessageList() {
      return this.theMessageList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theMessageList);
   }

   public Enumeration getMessageList() {
      return Collections.enumeration(this.getCollectionMessageList());
   }

   public void addMessage(Message arg) {
      if (arg != null) {
         if (this.theMessageList == null) {
            this.theMessageList = new LinkedHashSet();
         }

         if (this.theMessageList.add(arg)) {
            arg.setActivator(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "message", 1));
            }
         }
      }

   }

   public void removeMessage(Message arg) {
      if (this.theMessageList != null && arg != null && this.theMessageList.remove(arg)) {
         arg.setActivator((Message)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "message", 2));
         }
      }

   }

   public Set allPredecessors() {
      Set setPredecessor = this.getCollectionPredecessorList();
      Set setPredecessor0 = this.getCollectionPredecessorList();
      List bagCollect = CollectionUtilities.newBag();
      Iterator iter = setPredecessor0.iterator();

      Set setAsSet;
      while(iter.hasNext()) {
         Message decl = (Message)iter.next();
         setAsSet = decl.allPredecessors();
         bagCollect.add(setAsSet);
      }

      bagCollect = CollectionUtilities.flatten(bagCollect);
      List bagUnion = CollectionUtilities.union(setPredecessor, bagCollect);
      setAsSet = CollectionUtilities.asSet(bagUnion);
      return setAsSet;
   }

   protected void internalRemove() {
      Enumeration tmpConformingStimulusEnum = this.getConformingStimulusList();
      ArrayList tmpConformingStimulusList = new ArrayList();

      while(tmpConformingStimulusEnum.hasMoreElements()) {
         tmpConformingStimulusList.add(tmpConformingStimulusEnum.nextElement());
      }

      Iterator it = tmpConformingStimulusList.iterator();

      while(it.hasNext()) {
         Stimulus tmpConformingStimulus = (Stimulus)it.next();
         tmpConformingStimulus.removePlayedRole(this);
      }

      Action tmpAction = this.getAction();
      if (tmpAction != null) {
         tmpAction.removeMessage(this);
      }

      Enumeration tmpPredecessorEnum = this.getPredecessorList();
      ArrayList tmpPredecessorList = new ArrayList();

      while(tmpPredecessorEnum.hasMoreElements()) {
         tmpPredecessorList.add(tmpPredecessorEnum.nextElement());
      }

       it = tmpPredecessorList.iterator();

      while(it.hasNext()) {
         Message tmpPredecessor = (Message)it.next();
         tmpPredecessor.removeSuccessor(this);
      }

      Procedure tmpProcedure = this.getProcedure();
      if (tmpProcedure != null) {
         tmpProcedure.removeMessage(this);
      }

      Interaction tmpInteraction = this.getInteraction();
      if (tmpInteraction != null) {
         tmpInteraction.removeMessage(this);
         if (tmpInteraction.getCollectionMessageList().size() < 1) {
            tmpInteraction.remove();
         }
      }

      Enumeration tmpSuccessorEnum = this.getSuccessorList();
      ArrayList tmpSuccessorList = new ArrayList();

      while(tmpSuccessorEnum.hasMoreElements()) {
         tmpSuccessorList.add(tmpSuccessorEnum.nextElement());
      }

       it = tmpSuccessorList.iterator();

      while(it.hasNext()) {
         Message tmpSuccessor = (Message)it.next();
         tmpSuccessor.removePredecessor(this);
      }

      Message tmpActivator = this.getActivator();
      if (tmpActivator != null) {
         tmpActivator.removeMessage(this);
      }

      ClassifierRole tmpReceiver = this.getReceiver();
      if (tmpReceiver != null) {
         tmpReceiver.removeMessage(this);
      }

      ClassifierRole tmpSender = this.getSender();
      if (tmpSender != null) {
         tmpSender.removeMessage1(this);
      }

      AssociationRole tmpCommunicationConnection = this.getCommunicationConnection();
      if (tmpCommunicationConnection != null) {
         tmpCommunicationConnection.removeMessage(this);
      }

      Enumeration tmpMessageEnum = this.getMessageList();
      ArrayList tmpMessageList = new ArrayList();

      while(tmpMessageEnum.hasMoreElements()) {
         tmpMessageList.add(tmpMessageEnum.nextElement());
      }

       it = tmpMessageList.iterator();

      while(it.hasNext()) {
         ((Message)it.next()).setActivator((Message)null);
      }

      super.internalRemove();
   }
}
