package ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.objectActions;

import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.PrimitiveActionImpl;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;

public class ReclassifyObjectActionImpl extends PrimitiveActionImpl implements ReclassifyObjectAction {
   protected boolean isReplaceAll;
   protected Set theNewClassifierList;
   protected Set theOldClassifierList;

   public ReclassifyObjectActionImpl() {
   }

   public boolean isReplaceAll() {
      return this.isReplaceAll;
   }

   public void setReplaceAll(boolean isReplaceAll) {
      this.isReplaceAll = isReplaceAll;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "isReplaceAll", 0));
      }

   }

   public Set getCollectionNewClassifierList() {
      return this.theNewClassifierList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theNewClassifierList);
   }

   public Enumeration getNewClassifierList() {
      return Collections.enumeration(this.getCollectionNewClassifierList());
   }

   public void addNewClassifier(Classifier arg) {
      if (arg != null) {
         if (this.theNewClassifierList == null) {
            this.theNewClassifierList = new LinkedHashSet();
         }

         this.theNewClassifierList.add(arg);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "newClassifier", 1));
         }
      }

   }

   public void removeNewClassifier(Classifier arg) {
      if (this.theNewClassifierList != null && arg != null) {
         this.theNewClassifierList.remove(arg);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "newClassifier", 2));
         }
      }

   }

   public Set getCollectionOldClassifierList() {
      return this.theOldClassifierList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theOldClassifierList);
   }

   public Enumeration getOldClassifierList() {
      return Collections.enumeration(this.getCollectionOldClassifierList());
   }

   public void addOldClassifier(Classifier arg) {
      if (arg != null) {
         if (this.theOldClassifierList == null) {
            this.theOldClassifierList = new LinkedHashSet();
         }

         this.theOldClassifierList.add(arg);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "oldClassifier", 1));
         }
      }

   }

   public void removeOldClassifier(Classifier arg) {
      if (this.theOldClassifierList != null && arg != null) {
         this.theOldClassifierList.remove(arg);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "oldClassifier", 2));
         }
      }

   }

   protected void internalRemove() {
      super.internalRemove();
   }
}
