package ro.ubbcluj.lci.uml.behavioralElements.commonBehavior;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.InteractionInstanceSet;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.Message;
import ro.ubbcluj.lci.uml.foundation.core.ModelElementImpl;

public class StimulusImpl extends ModelElementImpl implements Stimulus {
   protected Procedure theDispatchAction;
   protected Set theArgumentList;
   protected Link theCommunicationLink;
   protected Instance theSender;
   protected Set theInteractionInstanceSetList;
   protected Set thePlayedRoleList;
   protected Instance theReceiver;

   public StimulusImpl() {
   }

   public Procedure getDispatchAction() {
      return this.theDispatchAction;
   }

   public void setDispatchAction(Procedure arg) {
      if (this.theDispatchAction != arg) {
         Procedure temp = this.theDispatchAction;
         this.theDispatchAction = null;
         if (temp != null) {
            temp.removeStimulus(this);
         }

         if (arg != null) {
            this.theDispatchAction = arg;
            arg.addStimulus(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "dispatchAction", 0));
         }
      }

   }

   public Set getCollectionArgumentList() {
      return this.theArgumentList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theArgumentList);
   }

   public Enumeration getArgumentList() {
      return Collections.enumeration(this.getCollectionArgumentList());
   }

   public void addArgument(Instance arg) {
      if (arg != null) {
         if (this.theArgumentList == null) {
            this.theArgumentList = new LinkedHashSet();
         }

         if (this.theArgumentList.add(arg)) {
            arg.addStimulus1(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "argument", 1));
            }
         }
      }

   }

   public void removeArgument(Instance arg) {
      if (this.theArgumentList != null && arg != null && this.theArgumentList.remove(arg)) {
         arg.removeStimulus1(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "argument", 2));
         }
      }

   }

   public Link getCommunicationLink() {
      return this.theCommunicationLink;
   }

   public void setCommunicationLink(Link arg) {
      if (this.theCommunicationLink != arg) {
         Link temp = this.theCommunicationLink;
         this.theCommunicationLink = null;
         if (temp != null) {
            temp.removeStimulus(this);
         }

         if (arg != null) {
            this.theCommunicationLink = arg;
            arg.addStimulus(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "communicationLink", 0));
         }
      }

   }

   public Instance getSender() {
      return this.theSender;
   }

   public void setSender(Instance arg) {
      if (this.theSender != arg) {
         Instance temp = this.theSender;
         this.theSender = null;
         if (temp != null) {
            temp.removeStimulus2(this);
         }

         if (arg != null) {
            this.theSender = arg;
            arg.addStimulus2(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "sender", 0));
         }
      }

   }

   public Set getCollectionInteractionInstanceSetList() {
      return this.theInteractionInstanceSetList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theInteractionInstanceSetList);
   }

   public Enumeration getInteractionInstanceSetList() {
      return Collections.enumeration(this.getCollectionInteractionInstanceSetList());
   }

   public void addInteractionInstanceSet(InteractionInstanceSet arg) {
      if (arg != null) {
         if (this.theInteractionInstanceSetList == null) {
            this.theInteractionInstanceSetList = new LinkedHashSet();
         }

         if (this.theInteractionInstanceSetList.add(arg)) {
            arg.addParticipatingStimulus(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "interactionInstanceSet", 1));
            }
         }
      }

   }

   public void removeInteractionInstanceSet(InteractionInstanceSet arg) {
      if (this.theInteractionInstanceSetList != null && arg != null && this.theInteractionInstanceSetList.remove(arg)) {
         arg.removeParticipatingStimulus(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "interactionInstanceSet", 2));
         }
      }

   }

   public Set getCollectionPlayedRoleList() {
      return this.thePlayedRoleList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.thePlayedRoleList);
   }

   public Enumeration getPlayedRoleList() {
      return Collections.enumeration(this.getCollectionPlayedRoleList());
   }

   public void addPlayedRole(Message arg) {
      if (arg != null) {
         if (this.thePlayedRoleList == null) {
            this.thePlayedRoleList = new LinkedHashSet();
         }

         if (this.thePlayedRoleList.add(arg)) {
            arg.addConformingStimulus(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "playedRole", 1));
            }
         }
      }

   }

   public void removePlayedRole(Message arg) {
      if (this.thePlayedRoleList != null && arg != null && this.thePlayedRoleList.remove(arg)) {
         arg.removeConformingStimulus(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "playedRole", 2));
         }
      }

   }

   public Instance getReceiver() {
      return this.theReceiver;
   }

   public void setReceiver(Instance arg) {
      if (this.theReceiver != arg) {
         Instance temp = this.theReceiver;
         this.theReceiver = null;
         if (temp != null) {
            temp.removeStimulus3(this);
         }

         if (arg != null) {
            this.theReceiver = arg;
            arg.addStimulus3(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "receiver", 0));
         }
      }

   }

   protected void internalRemove() {
      Procedure tmpDispatchAction = this.getDispatchAction();
      if (tmpDispatchAction != null) {
         tmpDispatchAction.removeStimulus(this);
      }

      Enumeration tmpArgumentEnum = this.getArgumentList();
      ArrayList tmpArgumentList = new ArrayList();

      while(tmpArgumentEnum.hasMoreElements()) {
         tmpArgumentList.add(tmpArgumentEnum.nextElement());
      }

      Iterator it = tmpArgumentList.iterator();

      Instance tmpSender;
      while(it.hasNext()) {
         tmpSender = (Instance)it.next();
         tmpSender.removeStimulus1(this);
      }

      Link tmpCommunicationLink = this.getCommunicationLink();
      if (tmpCommunicationLink != null) {
         tmpCommunicationLink.removeStimulus(this);
      }

      tmpSender = this.getSender();
      if (tmpSender != null) {
         tmpSender.removeStimulus2(this);
      }

      Enumeration tmpInteractionInstanceSetEnum = this.getInteractionInstanceSetList();
      ArrayList tmpInteractionInstanceSetList = new ArrayList();

      while(tmpInteractionInstanceSetEnum.hasMoreElements()) {
         tmpInteractionInstanceSetList.add(tmpInteractionInstanceSetEnum.nextElement());
      }

       it = tmpInteractionInstanceSetList.iterator();

      while(it.hasNext()) {
         InteractionInstanceSet tmpInteractionInstanceSet = (InteractionInstanceSet)it.next();
         tmpInteractionInstanceSet.removeParticipatingStimulus(this);
         if (tmpInteractionInstanceSet.getCollectionParticipatingStimulusList().size() < 1) {
            tmpInteractionInstanceSet.remove();
         }
      }

      Enumeration tmpPlayedRoleEnum = this.getPlayedRoleList();
      ArrayList tmpPlayedRoleList = new ArrayList();

      while(tmpPlayedRoleEnum.hasMoreElements()) {
         tmpPlayedRoleList.add(tmpPlayedRoleEnum.nextElement());
      }

       it = tmpPlayedRoleList.iterator();

      while(it.hasNext()) {
         Message tmpPlayedRole = (Message)it.next();
         tmpPlayedRole.removeConformingStimulus(this);
      }

      Instance tmpReceiver = this.getReceiver();
      if (tmpReceiver != null) {
         tmpReceiver.removeStimulus3(this);
      }

      super.internalRemove();
   }
}
