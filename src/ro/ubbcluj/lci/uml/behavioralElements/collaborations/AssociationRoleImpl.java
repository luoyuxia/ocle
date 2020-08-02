package ro.ubbcluj.lci.uml.behavioralElements.collaborations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Link;
import ro.ubbcluj.lci.uml.foundation.core.Association;
import ro.ubbcluj.lci.uml.foundation.core.AssociationImpl;
import ro.ubbcluj.lci.uml.foundation.dataTypes.Multiplicity;

public class AssociationRoleImpl extends AssociationImpl implements AssociationRole {
   protected Multiplicity theMultiplicity;
   protected Set theConformingLinkList;
   protected Association theBase;
   protected Set theMessageList;

   public AssociationRoleImpl() {
   }

   public Multiplicity getMultiplicity() {
      return this.theMultiplicity;
   }

   public void setMultiplicity(Multiplicity multiplicity) {
      this.theMultiplicity = multiplicity;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "multiplicity", 0));
      }

   }

   public Set getCollectionConformingLinkList() {
      return this.theConformingLinkList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theConformingLinkList);
   }

   public Enumeration getConformingLinkList() {
      return Collections.enumeration(this.getCollectionConformingLinkList());
   }

   public void addConformingLink(Link arg) {
      if (arg != null) {
         if (this.theConformingLinkList == null) {
            this.theConformingLinkList = new LinkedHashSet();
         }

         if (this.theConformingLinkList.add(arg)) {
            arg.addPlayedRole(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "conformingLink", 1));
            }
         }
      }

   }

   public void removeConformingLink(Link arg) {
      if (this.theConformingLinkList != null && arg != null && this.theConformingLinkList.remove(arg)) {
         arg.removePlayedRole(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "conformingLink", 2));
         }
      }

   }

   public Association getBase() {
      return this.theBase;
   }

   public void setBase(Association arg) {
      if (this.theBase != arg) {
         Association temp = this.theBase;
         this.theBase = null;
         if (temp != null) {
            temp.removeAssociationRole(this);
         }

         if (arg != null) {
            this.theBase = arg;
            arg.addAssociationRole(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "base", 0));
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
            arg.setCommunicationConnection(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "message", 1));
            }
         }
      }

   }

   public void removeMessage(Message arg) {
      if (this.theMessageList != null && arg != null && this.theMessageList.remove(arg)) {
         arg.setCommunicationConnection((AssociationRole)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "message", 2));
         }
      }

   }

   protected void internalRemove() {
      Enumeration tmpConformingLinkEnum = this.getConformingLinkList();
      ArrayList tmpConformingLinkList = new ArrayList();

      while(tmpConformingLinkEnum.hasMoreElements()) {
         tmpConformingLinkList.add(tmpConformingLinkEnum.nextElement());
      }

      Iterator it = tmpConformingLinkList.iterator();

      while(it.hasNext()) {
         Link tmpConformingLink = (Link)it.next();
         tmpConformingLink.removePlayedRole(this);
      }

      Association tmpBase = this.getBase();
      if (tmpBase != null) {
         tmpBase.removeAssociationRole(this);
      }

      Enumeration tmpMessageEnum = this.getMessageList();
      ArrayList tmpMessageList = new ArrayList();

      while(tmpMessageEnum.hasMoreElements()) {
         tmpMessageList.add(tmpMessageEnum.nextElement());
      }

       it = tmpMessageList.iterator();

      while(it.hasNext()) {
         ((Message)it.next()).setCommunicationConnection((AssociationRole)null);
      }

      super.internalRemove();
   }
}
