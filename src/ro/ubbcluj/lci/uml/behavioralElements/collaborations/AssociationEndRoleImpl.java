package ro.ubbcluj.lci.uml.behavioralElements.collaborations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.foundation.core.AssociationEnd;
import ro.ubbcluj.lci.uml.foundation.core.AssociationEndImpl;
import ro.ubbcluj.lci.uml.foundation.core.Attribute;
import ro.ubbcluj.lci.uml.foundation.dataTypes.Multiplicity;

public class AssociationEndRoleImpl extends AssociationEndImpl implements AssociationEndRole {
   protected Multiplicity theCollaborationMultiplicity;
   protected Set theAvailableQualifierList;
   protected AssociationEnd theBase;

   public AssociationEndRoleImpl() {
   }

   public Multiplicity getCollaborationMultiplicity() {
      return this.theCollaborationMultiplicity;
   }

   public void setCollaborationMultiplicity(Multiplicity collaborationMultiplicity) {
      this.theCollaborationMultiplicity = collaborationMultiplicity;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "collaborationMultiplicity", 0));
      }

   }

   public Set getCollectionAvailableQualifierList() {
      return this.theAvailableQualifierList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theAvailableQualifierList);
   }

   public Enumeration getAvailableQualifierList() {
      return Collections.enumeration(this.getCollectionAvailableQualifierList());
   }

   public void addAvailableQualifier(Attribute arg) {
      if (arg != null) {
         if (this.theAvailableQualifierList == null) {
            this.theAvailableQualifierList = new LinkedHashSet();
         }

         if (this.theAvailableQualifierList.add(arg)) {
            arg.addAssociationEndRole(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "availableQualifier", 1));
            }
         }
      }

   }

   public void removeAvailableQualifier(Attribute arg) {
      if (this.theAvailableQualifierList != null && arg != null && this.theAvailableQualifierList.remove(arg)) {
         arg.removeAssociationEndRole(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "availableQualifier", 2));
         }
      }

   }

   public AssociationEnd getBase() {
      return this.theBase;
   }

   public void setBase(AssociationEnd arg) {
      if (this.theBase != arg) {
         AssociationEnd temp = this.theBase;
         this.theBase = null;
         if (temp != null) {
            temp.removeAssociationEndRole(this);
         }

         if (arg != null) {
            this.theBase = arg;
            arg.addAssociationEndRole(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "base", 0));
         }
      }

   }

   protected void internalRemove() {
      Enumeration tmpAvailableQualifierEnum = this.getAvailableQualifierList();
      ArrayList tmpAvailableQualifierList = new ArrayList();

      while(tmpAvailableQualifierEnum.hasMoreElements()) {
         tmpAvailableQualifierList.add(tmpAvailableQualifierEnum.nextElement());
      }

      Iterator it = tmpAvailableQualifierList.iterator();

      while(it.hasNext()) {
         Attribute tmpAvailableQualifier = (Attribute)it.next();
         tmpAvailableQualifier.removeAssociationEndRole(this);
      }

      AssociationEnd tmpBase = this.getBase();
      if (tmpBase != null) {
         tmpBase.removeAssociationEndRole(this);
      }

      super.internalRemove();
   }
}
