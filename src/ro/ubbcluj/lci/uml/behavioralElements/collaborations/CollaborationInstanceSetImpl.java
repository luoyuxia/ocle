package ro.ubbcluj.lci.uml.behavioralElements.collaborations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Instance;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Link;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.uml.foundation.core.ModelElementImpl;

public class CollaborationInstanceSetImpl extends ModelElementImpl implements CollaborationInstanceSet {
   protected Set theInteractionInstanceList;
   protected Set theParticipatingLinkList;
   protected Set theConstrainingElementList;
   protected Collaboration theCollaboration;
   protected Set theParticipatingInstanceList;

   public CollaborationInstanceSetImpl() {
   }

   public Set getCollectionInteractionInstanceList() {
      return this.theInteractionInstanceList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theInteractionInstanceList);
   }

   public Enumeration getInteractionInstanceList() {
      return Collections.enumeration(this.getCollectionInteractionInstanceList());
   }

   public void addInteractionInstance(InteractionInstanceSet arg) {
      if (arg != null) {
         if (this.theInteractionInstanceList == null) {
            this.theInteractionInstanceList = new LinkedHashSet();
         }

         if (this.theInteractionInstanceList.add(arg)) {
            arg.setContext(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "interactionInstance", 1));
            }
         }
      }

   }

   public void removeInteractionInstance(InteractionInstanceSet arg) {
      if (this.theInteractionInstanceList != null && arg != null && this.theInteractionInstanceList.remove(arg)) {
         arg.setContext((CollaborationInstanceSet)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "interactionInstance", 2));
         }
      }

   }

   public Set getCollectionParticipatingLinkList() {
      return this.theParticipatingLinkList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theParticipatingLinkList);
   }

   public Enumeration getParticipatingLinkList() {
      return Collections.enumeration(this.getCollectionParticipatingLinkList());
   }

   public void addParticipatingLink(Link arg) {
      if (arg != null) {
         if (this.theParticipatingLinkList == null) {
            this.theParticipatingLinkList = new LinkedHashSet();
         }

         if (this.theParticipatingLinkList.add(arg)) {
            arg.addCollaborationInstanceSet(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "participatingLink", 1));
            }
         }
      }

   }

   public void removeParticipatingLink(Link arg) {
      if (this.theParticipatingLinkList != null && arg != null && this.theParticipatingLinkList.remove(arg)) {
         arg.removeCollaborationInstanceSet(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "participatingLink", 2));
         }
      }

   }

   public Set getCollectionConstrainingElementList() {
      return this.theConstrainingElementList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theConstrainingElementList);
   }

   public Enumeration getConstrainingElementList() {
      return Collections.enumeration(this.getCollectionConstrainingElementList());
   }

   public void addConstrainingElement(ModelElement arg) {
      if (arg != null) {
         if (this.theConstrainingElementList == null) {
            this.theConstrainingElementList = new LinkedHashSet();
         }

         if (this.theConstrainingElementList.add(arg)) {
            arg.addCollaborationInstanceSet(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "constrainingElement", 1));
            }
         }
      }

   }

   public void removeConstrainingElement(ModelElement arg) {
      if (this.theConstrainingElementList != null && arg != null && this.theConstrainingElementList.remove(arg)) {
         arg.removeCollaborationInstanceSet(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "constrainingElement", 2));
         }
      }

   }

   public Collaboration getCollaboration() {
      return this.theCollaboration;
   }

   public void setCollaboration(Collaboration arg) {
      if (this.theCollaboration != arg) {
         Collaboration temp = this.theCollaboration;
         this.theCollaboration = null;
         if (temp != null) {
            temp.removeCollaborationInstanceSet(this);
         }

         if (arg != null) {
            this.theCollaboration = arg;
            arg.addCollaborationInstanceSet(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "collaboration", 0));
         }
      }

   }

   public Set getCollectionParticipatingInstanceList() {
      return this.theParticipatingInstanceList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theParticipatingInstanceList);
   }

   public Enumeration getParticipatingInstanceList() {
      return Collections.enumeration(this.getCollectionParticipatingInstanceList());
   }

   public void addParticipatingInstance(Instance arg) {
      if (arg != null) {
         if (this.theParticipatingInstanceList == null) {
            this.theParticipatingInstanceList = new LinkedHashSet();
         }

         if (this.theParticipatingInstanceList.add(arg)) {
            arg.addCollaborationInstanceSet(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "participatingInstance", 1));
            }
         }
      }

   }

   public void removeParticipatingInstance(Instance arg) {
      if (this.theParticipatingInstanceList != null && arg != null && this.theParticipatingInstanceList.remove(arg)) {
         arg.removeCollaborationInstanceSet(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "participatingInstance", 2));
         }
      }

   }

   protected void internalRemove() {
      Enumeration tmpInteractionInstanceEnum = this.getInteractionInstanceList();
      ArrayList tmpInteractionInstanceList = new ArrayList();

      while(tmpInteractionInstanceEnum.hasMoreElements()) {
         tmpInteractionInstanceList.add(tmpInteractionInstanceEnum.nextElement());
      }

      Iterator it = tmpInteractionInstanceList.iterator();

      while(it.hasNext()) {
         ((InteractionInstanceSet)it.next()).remove();
      }

      Enumeration tmpParticipatingLinkEnum = this.getParticipatingLinkList();
      ArrayList tmpParticipatingLinkList = new ArrayList();

      while(tmpParticipatingLinkEnum.hasMoreElements()) {
         tmpParticipatingLinkList.add(tmpParticipatingLinkEnum.nextElement());
      }

       it = tmpParticipatingLinkList.iterator();

      while(it.hasNext()) {
         Link tmpParticipatingLink = (Link)it.next();
         tmpParticipatingLink.removeCollaborationInstanceSet(this);
      }

      Enumeration tmpConstrainingElementEnum = this.getConstrainingElementList();
      ArrayList tmpConstrainingElementList = new ArrayList();

      while(tmpConstrainingElementEnum.hasMoreElements()) {
         tmpConstrainingElementList.add(tmpConstrainingElementEnum.nextElement());
      }

       it = tmpConstrainingElementList.iterator();

      while(it.hasNext()) {
         ModelElement tmpConstrainingElement = (ModelElement)it.next();
         tmpConstrainingElement.removeCollaborationInstanceSet(this);
      }

      Collaboration tmpCollaboration = this.getCollaboration();
      if (tmpCollaboration != null) {
         tmpCollaboration.removeCollaborationInstanceSet(this);
      }

      Enumeration tmpParticipatingInstanceEnum = this.getParticipatingInstanceList();
      ArrayList tmpParticipatingInstanceList = new ArrayList();

      while(tmpParticipatingInstanceEnum.hasMoreElements()) {
         tmpParticipatingInstanceList.add(tmpParticipatingInstanceEnum.nextElement());
      }

       it = tmpParticipatingInstanceList.iterator();

      while(it.hasNext()) {
         Instance tmpParticipatingInstance = (Instance)it.next();
         tmpParticipatingInstance.removeCollaborationInstanceSet(this);
      }

      super.internalRemove();
   }
}
