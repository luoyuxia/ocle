package ro.ubbcluj.lci.uml.foundation.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.Collaboration;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.CallEvent;

public class OperationImpl extends BehavioralFeatureImpl implements Operation {
   protected int theConcurrency;
   protected boolean isRoot;
   protected boolean isLeaf;
   protected boolean isAbstract;
   protected String theSpecification;
   protected Set theMethodList;
   protected Set theCollaborationList;
   protected Set theOccurenceList;

   public OperationImpl() {
   }

   public int getConcurrency() {
      return this.theConcurrency;
   }

   public void setConcurrency(int concurrency) {
      this.theConcurrency = concurrency;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "concurrency", 0));
      }

   }

   public boolean isRoot() {
      return this.isRoot;
   }

   public void setRoot(boolean isRoot) {
      this.isRoot = isRoot;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "isRoot", 0));
      }

   }

   public boolean isLeaf() {
      return this.isLeaf;
   }

   public void setLeaf(boolean isLeaf) {
      this.isLeaf = isLeaf;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "isLeaf", 0));
      }

   }

   public boolean isAbstract() {
      return this.isAbstract;
   }

   public void setAbstract(boolean isAbstract) {
      this.isAbstract = isAbstract;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "isAbstract", 0));
      }

   }

   public String getSpecification() {
      return this.theSpecification;
   }

   public void setSpecification(String specification) {
      this.theSpecification = specification;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "specification", 0));
      }

   }

   public Set getCollectionMethodList() {
      return this.theMethodList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theMethodList);
   }

   public java.util.Enumeration getMethodList() {
      return Collections.enumeration(this.getCollectionMethodList());
   }

   public void addMethod(Method arg) {
      if (arg != null) {
         if (this.theMethodList == null) {
            this.theMethodList = new LinkedHashSet();
         }

         if (this.theMethodList.add(arg)) {
            arg.setSpecification(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "method", 1));
            }
         }
      }

   }

   public void removeMethod(Method arg) {
      if (this.theMethodList != null && arg != null && this.theMethodList.remove(arg)) {
         arg.setSpecification((Operation)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "method", 2));
         }
      }

   }

   public Set getCollectionCollaborationList() {
      return this.theCollaborationList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theCollaborationList);
   }

   public java.util.Enumeration getCollaborationList() {
      return Collections.enumeration(this.getCollectionCollaborationList());
   }

   public void addCollaboration(Collaboration arg) {
      if (arg != null) {
         if (this.theCollaborationList == null) {
            this.theCollaborationList = new LinkedHashSet();
         }

         if (this.theCollaborationList.add(arg)) {
            arg.setRepresentedOperation(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "collaboration", 1));
            }
         }
      }

   }

   public void removeCollaboration(Collaboration arg) {
      if (this.theCollaborationList != null && arg != null && this.theCollaborationList.remove(arg)) {
         arg.setRepresentedOperation((Operation)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "collaboration", 2));
         }
      }

   }

   public Set getCollectionOccurenceList() {
      return this.theOccurenceList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theOccurenceList);
   }

   public java.util.Enumeration getOccurenceList() {
      return Collections.enumeration(this.getCollectionOccurenceList());
   }

   public void addOccurence(CallEvent arg) {
      if (arg != null) {
         if (this.theOccurenceList == null) {
            this.theOccurenceList = new LinkedHashSet();
         }

         if (this.theOccurenceList.add(arg)) {
            arg.setOperation(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "occurence", 1));
            }
         }
      }

   }

   public void removeOccurence(CallEvent arg) {
      if (this.theOccurenceList != null && arg != null && this.theOccurenceList.remove(arg)) {
         arg.setOperation((Operation)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "occurence", 2));
         }
      }

   }

   protected void internalRemove() {
      java.util.Enumeration tmpMethodEnum = this.getMethodList();
      ArrayList tmpMethodList = new ArrayList();

      while(tmpMethodEnum.hasMoreElements()) {
         tmpMethodList.add(tmpMethodEnum.nextElement());
      }

      Iterator it = tmpMethodList.iterator();

      while(it.hasNext()) {
         ((Method)it.next()).setSpecification((Operation)null);
      }

      java.util.Enumeration tmpCollaborationEnum = this.getCollaborationList();
      ArrayList tmpCollaborationList = new ArrayList();

      while(tmpCollaborationEnum.hasMoreElements()) {
         tmpCollaborationList.add(tmpCollaborationEnum.nextElement());
      }

       it = tmpCollaborationList.iterator();

      while(it.hasNext()) {
         ((Collaboration)it.next()).setRepresentedOperation((Operation)null);
      }

      java.util.Enumeration tmpOccurenceEnum = this.getOccurenceList();
      ArrayList tmpOccurenceList = new ArrayList();

      while(tmpOccurenceEnum.hasMoreElements()) {
         tmpOccurenceList.add(tmpOccurenceEnum.nextElement());
      }

       it = tmpOccurenceList.iterator();

      while(it.hasNext()) {
         ((CallEvent)it.next()).setOperation((Operation)null);
      }

      super.internalRemove();
   }
}
