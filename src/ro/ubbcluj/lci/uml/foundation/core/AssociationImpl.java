package ro.ubbcluj.lci.uml.foundation.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import ro.ubbcluj.lci.codegen.framework.dt.OrderedSet;
import ro.ubbcluj.lci.codegen.framework.ocl.CollectionUtilities;
import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.AssociationRole;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Link;

public class AssociationImpl extends GeneralizableElementImpl implements Association {
   protected Set theAssociationRoleList;
   protected Set theLinkList;
   protected OrderedSet theConnectionList;

   public AssociationImpl() {
   }

   public Set getCollectionAssociationRoleList() {
      return this.theAssociationRoleList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theAssociationRoleList);
   }

   public java.util.Enumeration getAssociationRoleList() {
      return Collections.enumeration(this.getCollectionAssociationRoleList());
   }

   public void addAssociationRole(AssociationRole arg) {
      if (arg != null) {
         if (this.theAssociationRoleList == null) {
            this.theAssociationRoleList = new LinkedHashSet();
         }

         if (this.theAssociationRoleList.add(arg)) {
            arg.setBase(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "associationRole", 1));
            }
         }
      }

   }

   public void removeAssociationRole(AssociationRole arg) {
      if (this.theAssociationRoleList != null && arg != null && this.theAssociationRoleList.remove(arg)) {
         arg.setBase((Association)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "associationRole", 2));
         }
      }

   }

   public Set getCollectionLinkList() {
      return this.theLinkList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theLinkList);
   }

   public java.util.Enumeration getLinkList() {
      return Collections.enumeration(this.getCollectionLinkList());
   }

   public void addLink(Link arg) {
      if (arg != null) {
         if (this.theLinkList == null) {
            this.theLinkList = new LinkedHashSet();
         }

         if (this.theLinkList.add(arg)) {
            arg.setAssociation(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "link", 1));
            }
         }
      }

   }

   public void removeLink(Link arg) {
      if (this.theLinkList != null && arg != null && this.theLinkList.remove(arg)) {
         arg.setAssociation((Association)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "link", 2));
         }
      }

   }

   public OrderedSet getCollectionConnectionList() {
      return this.theConnectionList == null ? CollectionUtilities.newOrderedSet() : CollectionUtilities.newOrderedSet(this.theConnectionList);
   }

   public java.util.Enumeration getConnectionList() {
      return Collections.enumeration(this.getCollectionConnectionList());
   }

   public void addConnection(AssociationEnd arg) {
      if (arg != null) {
         if (this.theConnectionList == null) {
            this.theConnectionList = new OrderedSet();
         }

         if (this.theConnectionList.add(arg)) {
            arg.setAssociation(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "connection", 1));
            }
         }
      }

   }

   public void removeConnection(AssociationEnd arg) {
      if (this.theConnectionList != null && arg != null && this.theConnectionList.remove(arg)) {
         arg.setAssociation((Association)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "connection", 2));
         }
      }

   }

   public Set allConnections() {
      List seqConnection = this.getCollectionConnectionList();
      Set setAsSet = CollectionUtilities.asSet(seqConnection);
      return setAsSet;
   }

   protected void internalRemove() {
      java.util.Enumeration tmpAssociationRoleEnum = this.getAssociationRoleList();
      ArrayList tmpAssociationRoleList = new ArrayList();

      while(tmpAssociationRoleEnum.hasMoreElements()) {
         tmpAssociationRoleList.add(tmpAssociationRoleEnum.nextElement());
      }

      Iterator it = tmpAssociationRoleList.iterator();

      while(it.hasNext()) {
         ((AssociationRole)it.next()).setBase((Association)null);
      }

      java.util.Enumeration tmpLinkEnum = this.getLinkList();
      ArrayList tmpLinkList = new ArrayList();

      while(tmpLinkEnum.hasMoreElements()) {
         tmpLinkList.add(tmpLinkEnum.nextElement());
      }

       it = tmpLinkList.iterator();

      while(it.hasNext()) {
         ((Link)it.next()).remove();
      }

      java.util.Enumeration tmpConnectionEnum = this.getConnectionList();
      ArrayList tmpConnectionList = new ArrayList();

      while(tmpConnectionEnum.hasMoreElements()) {
         tmpConnectionList.add(tmpConnectionEnum.nextElement());
      }

       it = tmpConnectionList.iterator();

      while(it.hasNext()) {
         ((AssociationEnd)it.next()).remove();
      }

      super.internalRemove();
   }
}
