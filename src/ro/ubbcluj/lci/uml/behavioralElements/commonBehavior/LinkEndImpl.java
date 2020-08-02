package ro.ubbcluj.lci.uml.behavioralElements.commonBehavior;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import ro.ubbcluj.lci.codegen.framework.dt.OrderedSet;
import ro.ubbcluj.lci.codegen.framework.ocl.CollectionUtilities;
import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.foundation.core.AssociationEnd;
import ro.ubbcluj.lci.uml.foundation.core.ModelElementImpl;

public class LinkEndImpl extends ModelElementImpl implements LinkEnd {
   protected Instance theInstance;
   protected AssociationEnd theAssociationEnd;
   protected OrderedSet theQualifierValueList;
   protected Link theLink;

   public LinkEndImpl() {
   }

   public Instance getInstance() {
      return this.theInstance;
   }

   public void setInstance(Instance arg) {
      if (this.theInstance != arg) {
         Instance temp = this.theInstance;
         this.theInstance = null;
         if (temp != null) {
            temp.removeLinkEnd(this);
         }

         if (arg != null) {
            this.theInstance = arg;
            arg.addLinkEnd(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "instance", 0));
         }
      }

   }

   public AssociationEnd getAssociationEnd() {
      return this.theAssociationEnd;
   }

   public void setAssociationEnd(AssociationEnd arg) {
      if (this.theAssociationEnd != arg) {
         AssociationEnd temp = this.theAssociationEnd;
         this.theAssociationEnd = null;
         if (temp != null) {
            temp.removeLinkEnd(this);
         }

         if (arg != null) {
            this.theAssociationEnd = arg;
            arg.addLinkEnd(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "associationEnd", 0));
         }
      }

   }

   public OrderedSet getCollectionQualifierValueList() {
      return this.theQualifierValueList == null ? CollectionUtilities.newOrderedSet() : CollectionUtilities.newOrderedSet(this.theQualifierValueList);
   }

   public Enumeration getQualifierValueList() {
      return Collections.enumeration(this.getCollectionQualifierValueList());
   }

   public void addQualifierValue(AttributeLink arg) {
      if (arg != null) {
         if (this.theQualifierValueList == null) {
            this.theQualifierValueList = new OrderedSet();
         }

         if (this.theQualifierValueList.add(arg)) {
            arg.setLinkEnd(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "qualifierValue", 1));
            }
         }
      }

   }

   public void removeQualifierValue(AttributeLink arg) {
      if (this.theQualifierValueList != null && arg != null && this.theQualifierValueList.remove(arg)) {
         arg.setLinkEnd((LinkEnd)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "qualifierValue", 2));
         }
      }

   }

   public Link getLink() {
      return this.theLink;
   }

   public void setLink(Link arg) {
      if (this.theLink != arg) {
         Link temp = this.theLink;
         this.theLink = null;
         if (temp != null) {
            temp.removeConnection(this);
         }

         if (arg != null) {
            this.theLink = arg;
            arg.addConnection(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "link", 0));
         }
      }

   }

   protected void internalRemove() {
      Instance tmpInstance = this.getInstance();
      if (tmpInstance != null) {
         tmpInstance.removeLinkEnd(this);
      }

      AssociationEnd tmpAssociationEnd = this.getAssociationEnd();
      if (tmpAssociationEnd != null) {
         tmpAssociationEnd.removeLinkEnd(this);
      }

      Enumeration tmpQualifierValueEnum = this.getQualifierValueList();
      ArrayList tmpQualifierValueList = new ArrayList();

      while(tmpQualifierValueEnum.hasMoreElements()) {
         tmpQualifierValueList.add(tmpQualifierValueEnum.nextElement());
      }

      Iterator it = tmpQualifierValueList.iterator();

      while(it.hasNext()) {
         ((AttributeLink)it.next()).remove();
      }

      Link tmpLink = this.getLink();
      if (tmpLink != null) {
         tmpLink.removeConnection(this);
      }

      super.internalRemove();
   }
}
