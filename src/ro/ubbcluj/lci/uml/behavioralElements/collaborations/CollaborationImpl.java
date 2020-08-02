package ro.ubbcluj.lci.uml.behavioralElements.collaborations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import ro.ubbcluj.lci.codegen.framework.ocl.CollectionUtilities;
import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.core.GeneralizableElement;
import ro.ubbcluj.lci.uml.foundation.core.Generalization;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.uml.foundation.core.NamespaceImpl;
import ro.ubbcluj.lci.uml.foundation.core.Operation;

public class CollaborationImpl extends NamespaceImpl implements Collaboration {
   protected boolean isRoot;
   protected boolean isLeaf;
   protected boolean isAbstract;
   protected Set theConstrainingElementList;
   protected Operation theRepresentedOperation;
   protected Set theUsedCollaborationList;
   protected Set theCollaboration1List;
   protected Set theInteractionList;
   protected Set theCollaborationInstanceSetList;
   protected Classifier theRepresentedClassifier;
   protected Set theGeneralizationList;
   protected Set theSpecializationList;

   public CollaborationImpl() {
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
            arg.addCollaboration2(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "constrainingElement", 1));
            }
         }
      }

   }

   public void removeConstrainingElement(ModelElement arg) {
      if (this.theConstrainingElementList != null && arg != null && this.theConstrainingElementList.remove(arg)) {
         arg.removeCollaboration2(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "constrainingElement", 2));
         }
      }

   }

   public Operation getRepresentedOperation() {
      return this.theRepresentedOperation;
   }

   public void setRepresentedOperation(Operation arg) {
      if (this.theRepresentedOperation != arg) {
         Operation temp = this.theRepresentedOperation;
         this.theRepresentedOperation = null;
         if (temp != null) {
            temp.removeCollaboration(this);
         }

         if (arg != null) {
            this.theRepresentedOperation = arg;
            arg.addCollaboration(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "representedOperation", 0));
         }
      }

   }

   public Set getCollectionUsedCollaborationList() {
      return this.theUsedCollaborationList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theUsedCollaborationList);
   }

   public Enumeration getUsedCollaborationList() {
      return Collections.enumeration(this.getCollectionUsedCollaborationList());
   }

   public void addUsedCollaboration(Collaboration arg) {
      if (arg != null) {
         if (this.theUsedCollaborationList == null) {
            this.theUsedCollaborationList = new LinkedHashSet();
         }

         if (this.theUsedCollaborationList.add(arg)) {
            arg.addCollaboration1(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "usedCollaboration", 1));
            }
         }
      }

   }

   public void removeUsedCollaboration(Collaboration arg) {
      if (this.theUsedCollaborationList != null && arg != null && this.theUsedCollaborationList.remove(arg)) {
         arg.removeCollaboration1(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "usedCollaboration", 2));
         }
      }

   }

   public Set getCollectionCollaboration1List() {
      return this.theCollaboration1List == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theCollaboration1List);
   }

   public Enumeration getCollaboration1List() {
      return Collections.enumeration(this.getCollectionCollaboration1List());
   }

   public void addCollaboration1(Collaboration arg) {
      if (arg != null) {
         if (this.theCollaboration1List == null) {
            this.theCollaboration1List = new LinkedHashSet();
         }

         if (this.theCollaboration1List.add(arg)) {
            arg.addUsedCollaboration(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "collaboration1", 1));
            }
         }
      }

   }

   public void removeCollaboration1(Collaboration arg) {
      if (this.theCollaboration1List != null && arg != null && this.theCollaboration1List.remove(arg)) {
         arg.removeUsedCollaboration(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "collaboration1", 2));
         }
      }

   }

   public Set getCollectionInteractionList() {
      return this.theInteractionList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theInteractionList);
   }

   public Enumeration getInteractionList() {
      return Collections.enumeration(this.getCollectionInteractionList());
   }

   public void addInteraction(Interaction arg) {
      if (arg != null) {
         if (this.theInteractionList == null) {
            this.theInteractionList = new LinkedHashSet();
         }

         if (this.theInteractionList.add(arg)) {
            arg.setContext(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "interaction", 1));
            }
         }
      }

   }

   public void removeInteraction(Interaction arg) {
      if (this.theInteractionList != null && arg != null && this.theInteractionList.remove(arg)) {
         arg.setContext((Collaboration)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "interaction", 2));
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
            arg.setCollaboration(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "collaborationInstanceSet", 1));
            }
         }
      }

   }

   public void removeCollaborationInstanceSet(CollaborationInstanceSet arg) {
      if (this.theCollaborationInstanceSetList != null && arg != null && this.theCollaborationInstanceSetList.remove(arg)) {
         arg.setCollaboration((Collaboration)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "collaborationInstanceSet", 2));
         }
      }

   }

   public Classifier getRepresentedClassifier() {
      return this.theRepresentedClassifier;
   }

   public void setRepresentedClassifier(Classifier arg) {
      if (this.theRepresentedClassifier != arg) {
         Classifier temp = this.theRepresentedClassifier;
         this.theRepresentedClassifier = null;
         if (temp != null) {
            temp.removeCollaboration(this);
         }

         if (arg != null) {
            this.theRepresentedClassifier = arg;
            arg.addCollaboration(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "representedClassifier", 0));
         }
      }

   }

   public Set getCollectionGeneralizationList() {
      return this.theGeneralizationList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theGeneralizationList);
   }

   public Enumeration getGeneralizationList() {
      return Collections.enumeration(this.getCollectionGeneralizationList());
   }

   public void addGeneralization(Generalization arg) {
      if (arg != null) {
         if (this.theGeneralizationList == null) {
            this.theGeneralizationList = new LinkedHashSet();
         }

         if (this.theGeneralizationList.add(arg)) {
            arg.setChild(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "generalization", 1));
            }
         }
      }

   }

   public void removeGeneralization(Generalization arg) {
      if (this.theGeneralizationList != null && arg != null && this.theGeneralizationList.remove(arg)) {
         arg.setChild((GeneralizableElement)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "generalization", 2));
         }
      }

   }

   public Set getCollectionSpecializationList() {
      return this.theSpecializationList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theSpecializationList);
   }

   public Enumeration getSpecializationList() {
      return Collections.enumeration(this.getCollectionSpecializationList());
   }

   public void addSpecialization(Generalization arg) {
      if (arg != null) {
         if (this.theSpecializationList == null) {
            this.theSpecializationList = new LinkedHashSet();
         }

         if (this.theSpecializationList.add(arg)) {
            arg.setParent(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "specialization", 1));
            }
         }
      }

   }

   public void removeSpecialization(Generalization arg) {
      if (this.theSpecializationList != null && arg != null && this.theSpecializationList.remove(arg)) {
         arg.setParent((GeneralizableElement)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "specialization", 2));
         }
      }

   }

   public Set allContents() {
      Set setcontents = this.contents();
      Set setparent = this.parent();
      List bagCollect = CollectionUtilities.newBag();
      Iterator iter = setparent.iterator();

      while(iter.hasNext()) {
         GeneralizableElement decl = (GeneralizableElement)iter.next();
         Collaboration collaborationOclAsType = (Collaboration)decl;
         bagCollect.add(collaborationOclAsType);
      }

      bagCollect = CollectionUtilities.flatten(bagCollect);
      List bagCollect0 = CollectionUtilities.newBag();
      Iterator iter0 = bagCollect.iterator();

      while(iter0.hasNext()) {
         Collaboration decl0 = (Collaboration)iter0.next();
         Set setallContents = decl0.allContents();
         bagCollect0.add(setallContents);
      }

      bagCollect0 = CollectionUtilities.flatten(bagCollect0);
      List bagReject = CollectionUtilities.newBag();
      Iterator iter1 = bagCollect0.iterator();

      Set setAsSet;
      while(iter1.hasNext()) {
         ModelElement e = (ModelElement)iter1.next();
         setAsSet = this.contents();
         List bagCollect1 = CollectionUtilities.newBag();
         Iterator iter2 = setAsSet.iterator();

         while(iter2.hasNext()) {
            ModelElement decl1 = (ModelElement)iter2.next();
            String strName = decl1.getName();
            bagCollect1.add(strName);
         }

         bagCollect1 = CollectionUtilities.flatten(bagCollect1);
         String strName0 = e.getName();
         boolean bIncludes = CollectionUtilities.includes(bagCollect1, strName0);
         if (!bIncludes) {
            CollectionUtilities.add(bagReject, e);
         }
      }

      List bagUnion = CollectionUtilities.union(setcontents, bagReject);
      setAsSet = CollectionUtilities.asSet(bagUnion);
      return setAsSet;
   }

   public Set parent() {
      Set setGeneralization = this.getCollectionGeneralizationList();
      List bagCollect = CollectionUtilities.newBag();
      Iterator iter = setGeneralization.iterator();

      while(iter.hasNext()) {
         Generalization decl = (Generalization)iter.next();
         GeneralizableElement generalizableElementParent = decl.getParent();
         bagCollect.add(generalizableElementParent);
      }

      bagCollect = CollectionUtilities.flatten(bagCollect);
      Set setAsSet = CollectionUtilities.asSet(bagCollect);
      return setAsSet;
   }

   public Set allParents() {
      Set setparent = this.parent();
      Set setparent0 = this.parent();
      List bagCollect = CollectionUtilities.newBag();
      Iterator iter = setparent0.iterator();

      Set setUnion;
      while(iter.hasNext()) {
         GeneralizableElement decl = (GeneralizableElement)iter.next();
         setUnion = decl.allParents();
         bagCollect.add(setUnion);
      }

      bagCollect = CollectionUtilities.flatten(bagCollect);
      Set setAsSet = CollectionUtilities.asSet(bagCollect);
      setUnion = CollectionUtilities.union(setparent, setAsSet);
      return setUnion;
   }

   protected void internalRemove() {
      Enumeration tmpConstrainingElementEnum = this.getConstrainingElementList();
      ArrayList tmpConstrainingElementList = new ArrayList();

      while(tmpConstrainingElementEnum.hasMoreElements()) {
         tmpConstrainingElementList.add(tmpConstrainingElementEnum.nextElement());
      }

      Iterator it = tmpConstrainingElementList.iterator();

      while(it.hasNext()) {
         ModelElement tmpConstrainingElement = (ModelElement)it.next();
         tmpConstrainingElement.removeCollaboration2(this);
      }

      Operation tmpRepresentedOperation = this.getRepresentedOperation();
      if (tmpRepresentedOperation != null) {
         tmpRepresentedOperation.removeCollaboration(this);
      }

      Enumeration tmpUsedCollaborationEnum = this.getUsedCollaborationList();
      ArrayList tmpUsedCollaborationList = new ArrayList();

      while(tmpUsedCollaborationEnum.hasMoreElements()) {
         tmpUsedCollaborationList.add(tmpUsedCollaborationEnum.nextElement());
      }

       it = tmpUsedCollaborationList.iterator();

      while(it.hasNext()) {
         Collaboration tmpUsedCollaboration = (Collaboration)it.next();
         tmpUsedCollaboration.removeCollaboration1(this);
      }

      Enumeration tmpCollaboration1Enum = this.getCollaboration1List();
      ArrayList tmpCollaboration1List = new ArrayList();

      while(tmpCollaboration1Enum.hasMoreElements()) {
         tmpCollaboration1List.add(tmpCollaboration1Enum.nextElement());
      }

       it = tmpCollaboration1List.iterator();

      while(it.hasNext()) {
         Collaboration tmpCollaboration1 = (Collaboration)it.next();
         tmpCollaboration1.removeUsedCollaboration(this);
      }

      Enumeration tmpInteractionEnum = this.getInteractionList();
      ArrayList tmpInteractionList = new ArrayList();

      while(tmpInteractionEnum.hasMoreElements()) {
         tmpInteractionList.add(tmpInteractionEnum.nextElement());
      }

       it = tmpInteractionList.iterator();

      while(it.hasNext()) {
         ((Interaction)it.next()).remove();
      }

      Enumeration tmpCollaborationInstanceSetEnum = this.getCollaborationInstanceSetList();
      ArrayList tmpCollaborationInstanceSetList = new ArrayList();

      while(tmpCollaborationInstanceSetEnum.hasMoreElements()) {
         tmpCollaborationInstanceSetList.add(tmpCollaborationInstanceSetEnum.nextElement());
      }

       it = tmpCollaborationInstanceSetList.iterator();

      while(it.hasNext()) {
         ((CollaborationInstanceSet)it.next()).setCollaboration((Collaboration)null);
      }

      Classifier tmpRepresentedClassifier = this.getRepresentedClassifier();
      if (tmpRepresentedClassifier != null) {
         tmpRepresentedClassifier.removeCollaboration(this);
      }

      Enumeration tmpGeneralizationEnum = this.getGeneralizationList();
      ArrayList tmpGeneralizationList = new ArrayList();

      while(tmpGeneralizationEnum.hasMoreElements()) {
         tmpGeneralizationList.add(tmpGeneralizationEnum.nextElement());
      }

       it = tmpGeneralizationList.iterator();

      while(it.hasNext()) {
         ((Generalization)it.next()).setChild((GeneralizableElement)null);
      }

      Enumeration tmpSpecializationEnum = this.getSpecializationList();
      ArrayList tmpSpecializationList = new ArrayList();

      while(tmpSpecializationEnum.hasMoreElements()) {
         tmpSpecializationList.add(tmpSpecializationEnum.nextElement());
      }

       it = tmpSpecializationList.iterator();

      while(it.hasNext()) {
         ((Generalization)it.next()).setParent((GeneralizableElement)null);
      }

      super.internalRemove();
   }
}
