package ro.ubbcluj.lci.uml.foundation.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.AssociationEndRole;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.AttributeLink;
import ro.ubbcluj.lci.uml.foundation.dataTypes.Expression;

public class AttributeImpl extends StructuralFeatureImpl implements Attribute {
   protected Expression theInitialValue;
   protected Set theAssociationEndRoleList;
   protected AssociationEnd theAssociationEnd;
   protected Set theAttributeLinkList;

   public AttributeImpl() {
   }

   public Expression getInitialValue() {
      return this.theInitialValue;
   }

   public void setInitialValue(Expression initialValue) {
      this.theInitialValue = initialValue;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "initialValue", 0));
      }

   }

   public Set getCollectionAssociationEndRoleList() {
      return this.theAssociationEndRoleList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theAssociationEndRoleList);
   }

   public java.util.Enumeration getAssociationEndRoleList() {
      return Collections.enumeration(this.getCollectionAssociationEndRoleList());
   }

   public void addAssociationEndRole(AssociationEndRole arg) {
      if (arg != null) {
         if (this.theAssociationEndRoleList == null) {
            this.theAssociationEndRoleList = new LinkedHashSet();
         }

         if (this.theAssociationEndRoleList.add(arg)) {
            arg.addAvailableQualifier(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "associationEndRole", 1));
            }
         }
      }

   }

   public void removeAssociationEndRole(AssociationEndRole arg) {
      if (this.theAssociationEndRoleList != null && arg != null && this.theAssociationEndRoleList.remove(arg)) {
         arg.removeAvailableQualifier(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "associationEndRole", 2));
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
            temp.removeQualifier(this);
         }

         if (arg != null) {
            this.theAssociationEnd = arg;
            arg.addQualifier(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "associationEnd", 0));
         }
      }

   }

   public Set getCollectionAttributeLinkList() {
      return this.theAttributeLinkList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theAttributeLinkList);
   }

   public java.util.Enumeration getAttributeLinkList() {
      return Collections.enumeration(this.getCollectionAttributeLinkList());
   }

   public void addAttributeLink(AttributeLink arg) {
      if (arg != null) {
         if (this.theAttributeLinkList == null) {
            this.theAttributeLinkList = new LinkedHashSet();
         }

         if (this.theAttributeLinkList.add(arg)) {
            arg.setAttribute(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "attributeLink", 1));
            }
         }
      }

   }

   public void removeAttributeLink(AttributeLink arg) {
      if (this.theAttributeLinkList != null && arg != null && this.theAttributeLinkList.remove(arg)) {
         arg.setAttribute((Attribute)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "attributeLink", 2));
         }
      }

   }

   protected void internalRemove() {
      java.util.Enumeration tmpAssociationEndRoleEnum = this.getAssociationEndRoleList();
      ArrayList tmpAssociationEndRoleList = new ArrayList();

      while(tmpAssociationEndRoleEnum.hasMoreElements()) {
         tmpAssociationEndRoleList.add(tmpAssociationEndRoleEnum.nextElement());
      }

      Iterator it = tmpAssociationEndRoleList.iterator();

      while(it.hasNext()) {
         AssociationEndRole tmpAssociationEndRole = (AssociationEndRole)it.next();
         tmpAssociationEndRole.removeAvailableQualifier(this);
      }

      AssociationEnd tmpAssociationEnd = this.getAssociationEnd();
      if (tmpAssociationEnd != null) {
         tmpAssociationEnd.removeQualifier(this);
      }

      java.util.Enumeration tmpAttributeLinkEnum = this.getAttributeLinkList();
      ArrayList tmpAttributeLinkList = new ArrayList();

      while(tmpAttributeLinkEnum.hasMoreElements()) {
         tmpAttributeLinkList.add(tmpAttributeLinkEnum.nextElement());
      }

       it = tmpAttributeLinkList.iterator();

      while(it.hasNext()) {
         ((AttributeLink)it.next()).remove();
      }

      super.internalRemove();
   }
}
