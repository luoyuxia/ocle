package ro.ubbcluj.lci.uml.behavioralElements.commonBehavior;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import ro.ubbcluj.lci.codegen.framework.dt.OrderedSet;
import ro.ubbcluj.lci.codegen.framework.ocl.CollectionUtilities;
import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.AssociationRole;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.CollaborationInstanceSet;
import ro.ubbcluj.lci.uml.foundation.core.Association;
import ro.ubbcluj.lci.uml.foundation.core.ModelElementImpl;

public class LinkImpl extends ModelElementImpl implements Link {
   protected Instance theOwner;
   protected Set theCollaborationInstanceSetList;
   protected OrderedSet theConnectionList;
   protected Set theStimulusList;
   protected Association theAssociation;
   protected Set thePlayedRoleList;

   public LinkImpl() {
   }

   public Instance getOwner() {
      return this.theOwner;
   }

   public void setOwner(Instance arg) {
      if (this.theOwner != arg) {
         Instance temp = this.theOwner;
         this.theOwner = null;
         if (temp != null) {
            temp.removeOwnedLink(this);
         }

         if (arg != null) {
            this.theOwner = arg;
            arg.addOwnedLink(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "owner", 0));
         }
      }

   }

   public Set getCollectionCollaborationInstanceSetList() {
      return this.theCollaborationInstanceSetList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theCollaborationInstanceSetList);
   }

   public Enumeration getCollaborationInstanceSetList() {
      return Collections.enumeration(this.getCollectionCollaborationInstanceSetList());
   }

   public void addCollaborationInstanceSet(CollaborationInstanceSet arg) {
      if (arg != null) {
         if (this.theCollaborationInstanceSetList == null) {
            this.theCollaborationInstanceSetList = new LinkedHashSet();
         }

         if (this.theCollaborationInstanceSetList.add(arg)) {
            arg.addParticipatingLink(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "collaborationInstanceSet", 1));
            }
         }
      }

   }

   public void removeCollaborationInstanceSet(CollaborationInstanceSet arg) {
      if (this.theCollaborationInstanceSetList != null && arg != null && this.theCollaborationInstanceSetList.remove(arg)) {
         arg.removeParticipatingLink(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "collaborationInstanceSet", 2));
         }
      }

   }

   public OrderedSet getCollectionConnectionList() {
      return this.theConnectionList == null ? CollectionUtilities.newOrderedSet() : CollectionUtilities.newOrderedSet(this.theConnectionList);
   }

   public Enumeration getConnectionList() {
      return Collections.enumeration(this.getCollectionConnectionList());
   }

   public void addConnection(LinkEnd arg) {
      if (arg != null) {
         if (this.theConnectionList == null) {
            this.theConnectionList = new OrderedSet();
         }

         if (this.theConnectionList.add(arg)) {
            arg.setLink(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "connection", 1));
            }
         }
      }

   }

   public void removeConnection(LinkEnd arg) {
      if (this.theConnectionList != null && arg != null && this.theConnectionList.remove(arg)) {
         arg.setLink((Link)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "connection", 2));
         }
      }

   }

   public Set getCollectionStimulusList() {
      return this.theStimulusList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theStimulusList);
   }

   public Enumeration getStimulusList() {
      return Collections.enumeration(this.getCollectionStimulusList());
   }

   public void addStimulus(Stimulus arg) {
      if (arg != null) {
         if (this.theStimulusList == null) {
            this.theStimulusList = new LinkedHashSet();
         }

         if (this.theStimulusList.add(arg)) {
            arg.setCommunicationLink(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "stimulus", 1));
            }
         }
      }

   }

   public void removeStimulus(Stimulus arg) {
      if (this.theStimulusList != null && arg != null && this.theStimulusList.remove(arg)) {
         arg.setCommunicationLink((Link)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "stimulus", 2));
         }
      }

   }

   public Association getAssociation() {
      return this.theAssociation;
   }

   public void setAssociation(Association arg) {
      if (this.theAssociation != arg) {
         Association temp = this.theAssociation;
         this.theAssociation = null;
         if (temp != null) {
            temp.removeLink(this);
         }

         if (arg != null) {
            this.theAssociation = arg;
            arg.addLink(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "association", 0));
         }
      }

   }

   public Set getCollectionPlayedRoleList() {
      return this.thePlayedRoleList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.thePlayedRoleList);
   }

   public Enumeration getPlayedRoleList() {
      return Collections.enumeration(this.getCollectionPlayedRoleList());
   }

   public void addPlayedRole(AssociationRole arg) {
      if (arg != null) {
         if (this.thePlayedRoleList == null) {
            this.thePlayedRoleList = new LinkedHashSet();
         }

         if (this.thePlayedRoleList.add(arg)) {
            arg.addConformingLink(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "playedRole", 1));
            }
         }
      }

   }

   public void removePlayedRole(AssociationRole arg) {
      if (this.thePlayedRoleList != null && arg != null && this.thePlayedRoleList.remove(arg)) {
         arg.removeConformingLink(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "playedRole", 2));
         }
      }

   }

   protected void internalRemove() {
      Instance tmpOwner = this.getOwner();
      if (tmpOwner != null) {
         tmpOwner.removeOwnedLink(this);
      }

      Enumeration tmpCollaborationInstanceSetEnum = this.getCollaborationInstanceSetList();
      ArrayList tmpCollaborationInstanceSetList = new ArrayList();

      while(tmpCollaborationInstanceSetEnum.hasMoreElements()) {
         tmpCollaborationInstanceSetList.add(tmpCollaborationInstanceSetEnum.nextElement());
      }

      Iterator it = tmpCollaborationInstanceSetList.iterator();

      while(it.hasNext()) {
         CollaborationInstanceSet tmpCollaborationInstanceSet = (CollaborationInstanceSet)it.next();
         tmpCollaborationInstanceSet.removeParticipatingLink(this);
      }

      Enumeration tmpConnectionEnum = this.getConnectionList();
      ArrayList tmpConnectionList = new ArrayList();

      while(tmpConnectionEnum.hasMoreElements()) {
         tmpConnectionList.add(tmpConnectionEnum.nextElement());
      }

       it = tmpConnectionList.iterator();

      while(it.hasNext()) {
         ((LinkEnd)it.next()).remove();
      }

      Enumeration tmpStimulusEnum = this.getStimulusList();
      ArrayList tmpStimulusList = new ArrayList();

      while(tmpStimulusEnum.hasMoreElements()) {
         tmpStimulusList.add(tmpStimulusEnum.nextElement());
      }

       it = tmpStimulusList.iterator();

      while(it.hasNext()) {
         ((Stimulus)it.next()).setCommunicationLink((Link)null);
      }

      Association tmpAssociation = this.getAssociation();
      if (tmpAssociation != null) {
         tmpAssociation.removeLink(this);
      }

      Enumeration tmpPlayedRoleEnum = this.getPlayedRoleList();
      ArrayList tmpPlayedRoleList = new ArrayList();

      while(tmpPlayedRoleEnum.hasMoreElements()) {
         tmpPlayedRoleList.add(tmpPlayedRoleEnum.nextElement());
      }

       it = tmpPlayedRoleList.iterator();

      while(it.hasNext()) {
         AssociationRole tmpPlayedRole = (AssociationRole)it.next();
         tmpPlayedRole.removeConformingLink(this);
      }

      super.internalRemove();
   }
}
