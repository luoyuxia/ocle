package ro.ubbcluj.lci.uml.foundation.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.ClassifierRole;

public class FeatureImpl extends ModelElementImpl implements Feature {
   protected int theOwnerScope;
   protected int theVisibility;
   protected Set theClassifierRoleList;
   protected Classifier theOwner;

   public FeatureImpl() {
   }

   public int getOwnerScope() {
      return this.theOwnerScope;
   }

   public void setOwnerScope(int ownerScope) {
      this.theOwnerScope = ownerScope;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "ownerScope", 0));
      }

   }

   public int getVisibility() {
      return this.theVisibility;
   }

   public void setVisibility(int visibility) {
      this.theVisibility = visibility;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "visibility", 0));
      }

   }

   public Set getCollectionClassifierRoleList() {
      return this.theClassifierRoleList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theClassifierRoleList);
   }

   public java.util.Enumeration getClassifierRoleList() {
      return Collections.enumeration(this.getCollectionClassifierRoleList());
   }

   public void addClassifierRole(ClassifierRole arg) {
      if (arg != null) {
         if (this.theClassifierRoleList == null) {
            this.theClassifierRoleList = new LinkedHashSet();
         }

         if (this.theClassifierRoleList.add(arg)) {
            arg.addAvailableFeature(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "classifierRole", 1));
            }
         }
      }

   }

   public void removeClassifierRole(ClassifierRole arg) {
      if (this.theClassifierRoleList != null && arg != null && this.theClassifierRoleList.remove(arg)) {
         arg.removeAvailableFeature(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "classifierRole", 2));
         }
      }

   }

   public Classifier getOwner() {
      return this.theOwner;
   }

   public void setOwner(Classifier arg) {
      if (this.theOwner != arg) {
         Classifier temp = this.theOwner;
         this.theOwner = null;
         if (temp != null) {
            temp.removeFeature(this);
         }

         if (arg != null) {
            this.theOwner = arg;
            arg.addFeature(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "owner", 0));
         }
      }

   }

   protected void internalRemove() {
      java.util.Enumeration tmpClassifierRoleEnum = this.getClassifierRoleList();
      ArrayList tmpClassifierRoleList = new ArrayList();

      while(tmpClassifierRoleEnum.hasMoreElements()) {
         tmpClassifierRoleList.add(tmpClassifierRoleEnum.nextElement());
      }

      Iterator it = tmpClassifierRoleList.iterator();

      while(it.hasNext()) {
         ClassifierRole tmpClassifierRole = (ClassifierRole)it.next();
         tmpClassifierRole.removeAvailableFeature(this);
      }

      Classifier tmpOwner = this.getOwner();
      if (tmpOwner != null) {
         tmpOwner.removeFeature(this);
      }

      super.internalRemove();
   }
}
