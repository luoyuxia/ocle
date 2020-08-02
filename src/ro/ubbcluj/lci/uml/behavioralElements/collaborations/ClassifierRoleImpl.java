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
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Instance;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.core.ClassifierImpl;
import ro.ubbcluj.lci.uml.foundation.core.Feature;
import ro.ubbcluj.lci.uml.foundation.core.GeneralizableElement;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.uml.foundation.dataTypes.Multiplicity;

public class ClassifierRoleImpl extends ClassifierImpl implements ClassifierRole {
   protected Multiplicity theMultiplicity;
   protected Set theConformingInstanceList;
   protected Set theAvailableContentsList;
   protected Set theMessageList;
   protected Set theAvailableFeatureList;
   protected Set theMessage1List;
   protected Set theBaseList;

   public ClassifierRoleImpl() {
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

   public Set getCollectionConformingInstanceList() {
      return this.theConformingInstanceList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theConformingInstanceList);
   }

   public Enumeration getConformingInstanceList() {
      return Collections.enumeration(this.getCollectionConformingInstanceList());
   }

   public void addConformingInstance(Instance arg) {
      if (arg != null) {
         if (this.theConformingInstanceList == null) {
            this.theConformingInstanceList = new LinkedHashSet();
         }

         if (this.theConformingInstanceList.add(arg)) {
            arg.addPlayedRole(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "conformingInstance", 1));
            }
         }
      }

   }

   public void removeConformingInstance(Instance arg) {
      if (this.theConformingInstanceList != null && arg != null && this.theConformingInstanceList.remove(arg)) {
         arg.removePlayedRole(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "conformingInstance", 2));
         }
      }

   }

   public Set getCollectionAvailableContentsList() {
      return this.theAvailableContentsList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theAvailableContentsList);
   }

   public Enumeration getAvailableContentsList() {
      return Collections.enumeration(this.getCollectionAvailableContentsList());
   }

   public void addAvailableContents(ModelElement arg) {
      if (arg != null) {
         if (this.theAvailableContentsList == null) {
            this.theAvailableContentsList = new LinkedHashSet();
         }

         if (this.theAvailableContentsList.add(arg)) {
            arg.addClassifierRole(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "availableContents", 1));
            }
         }
      }

   }

   public void removeAvailableContents(ModelElement arg) {
      if (this.theAvailableContentsList != null && arg != null && this.theAvailableContentsList.remove(arg)) {
         arg.removeClassifierRole(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "availableContents", 2));
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
            arg.setReceiver(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "message", 1));
            }
         }
      }

   }

   public void removeMessage(Message arg) {
      if (this.theMessageList != null && arg != null && this.theMessageList.remove(arg)) {
         arg.setReceiver((ClassifierRole)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "message", 2));
         }
      }

   }

   public Set getCollectionAvailableFeatureList() {
      return this.theAvailableFeatureList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theAvailableFeatureList);
   }

   public Enumeration getAvailableFeatureList() {
      return Collections.enumeration(this.getCollectionAvailableFeatureList());
   }

   public void addAvailableFeature(Feature arg) {
      if (arg != null) {
         if (this.theAvailableFeatureList == null) {
            this.theAvailableFeatureList = new LinkedHashSet();
         }

         if (this.theAvailableFeatureList.add(arg)) {
            arg.addClassifierRole(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "availableFeature", 1));
            }
         }
      }

   }

   public void removeAvailableFeature(Feature arg) {
      if (this.theAvailableFeatureList != null && arg != null && this.theAvailableFeatureList.remove(arg)) {
         arg.removeClassifierRole(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "availableFeature", 2));
         }
      }

   }

   public Set getCollectionMessage1List() {
      return this.theMessage1List == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theMessage1List);
   }

   public Enumeration getMessage1List() {
      return Collections.enumeration(this.getCollectionMessage1List());
   }

   public void addMessage1(Message arg) {
      if (arg != null) {
         if (this.theMessage1List == null) {
            this.theMessage1List = new LinkedHashSet();
         }

         if (this.theMessage1List.add(arg)) {
            arg.setSender(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "message1", 1));
            }
         }
      }

   }

   public void removeMessage1(Message arg) {
      if (this.theMessage1List != null && arg != null && this.theMessage1List.remove(arg)) {
         arg.setSender((ClassifierRole)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "message1", 2));
         }
      }

   }

   public Set getCollectionBaseList() {
      return this.theBaseList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theBaseList);
   }

   public Enumeration getBaseList() {
      return Collections.enumeration(this.getCollectionBaseList());
   }

   public void addBase(Classifier arg) {
      if (arg != null) {
         if (this.theBaseList == null) {
            this.theBaseList = new LinkedHashSet();
         }

         if (this.theBaseList.add(arg)) {
            arg.addClassifierRole(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "base", 1));
            }
         }
      }

   }

   public void removeBase(Classifier arg) {
      if (this.theBaseList != null && arg != null && this.theBaseList.remove(arg)) {
         arg.removeClassifierRole(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "base", 2));
         }
      }

   }

   public Set allAvailableFeatures() {
      Set setAvailableFeature = this.getCollectionAvailableFeatureList();
      Set setparent = this.parent();
      List bagCollect = CollectionUtilities.newBag();
      Iterator iter = setparent.iterator();

      while(iter.hasNext()) {
         GeneralizableElement decl = (GeneralizableElement)iter.next();
         ClassifierRole classifierRoleOclAsType = (ClassifierRole)decl;
         bagCollect.add(classifierRoleOclAsType);
      }

      bagCollect = CollectionUtilities.flatten(bagCollect);
      List bagCollect0 = CollectionUtilities.newBag();
      Iterator iter0 = bagCollect.iterator();

      Set setAsSet;
      while(iter0.hasNext()) {
         ClassifierRole decl0 = (ClassifierRole)iter0.next();
         setAsSet = decl0.allAvailableFeatures();
         bagCollect0.add(setAsSet);
      }

      bagCollect0 = CollectionUtilities.flatten(bagCollect0);
      List bagUnion = CollectionUtilities.union(setAvailableFeature, bagCollect0);
      setAsSet = CollectionUtilities.asSet(bagUnion);
      return setAsSet;
   }

   public Set allAvailableContents() {
      Set setAvailableContents = this.getCollectionAvailableContentsList();
      Set setparent = this.parent();
      List bagCollect = CollectionUtilities.newBag();
      Iterator iter = setparent.iterator();

      while(iter.hasNext()) {
         GeneralizableElement decl = (GeneralizableElement)iter.next();
         ClassifierRole classifierRoleOclAsType = (ClassifierRole)decl;
         bagCollect.add(classifierRoleOclAsType);
      }

      bagCollect = CollectionUtilities.flatten(bagCollect);
      List bagCollect0 = CollectionUtilities.newBag();
      Iterator iter0 = bagCollect.iterator();

      Set setAsSet;
      while(iter0.hasNext()) {
         ClassifierRole decl0 = (ClassifierRole)iter0.next();
         setAsSet = decl0.allAvailableContents();
         bagCollect0.add(setAsSet);
      }

      bagCollect0 = CollectionUtilities.flatten(bagCollect0);
      List bagUnion = CollectionUtilities.union(setAvailableContents, bagCollect0);
      setAsSet = CollectionUtilities.asSet(bagUnion);
      return setAsSet;
   }

   protected void internalRemove() {
      Enumeration tmpConformingInstanceEnum = this.getConformingInstanceList();
      ArrayList tmpConformingInstanceList = new ArrayList();

      while(tmpConformingInstanceEnum.hasMoreElements()) {
         tmpConformingInstanceList.add(tmpConformingInstanceEnum.nextElement());
      }

      Iterator it = tmpConformingInstanceList.iterator();

      while(it.hasNext()) {
         Instance tmpConformingInstance = (Instance)it.next();
         tmpConformingInstance.removePlayedRole(this);
      }

      Enumeration tmpAvailableContentsEnum = this.getAvailableContentsList();
      ArrayList tmpAvailableContentsList = new ArrayList();

      while(tmpAvailableContentsEnum.hasMoreElements()) {
         tmpAvailableContentsList.add(tmpAvailableContentsEnum.nextElement());
      }

       it = tmpAvailableContentsList.iterator();

      while(it.hasNext()) {
         ModelElement tmpAvailableContents = (ModelElement)it.next();
         tmpAvailableContents.removeClassifierRole(this);
      }

      Enumeration tmpMessageEnum = this.getMessageList();
      ArrayList tmpMessageList = new ArrayList();

      while(tmpMessageEnum.hasMoreElements()) {
         tmpMessageList.add(tmpMessageEnum.nextElement());
      }

       it = tmpMessageList.iterator();

      while(it.hasNext()) {
         ((Message)it.next()).setReceiver((ClassifierRole)null);
      }

      Enumeration tmpAvailableFeatureEnum = this.getAvailableFeatureList();
      ArrayList tmpAvailableFeatureList = new ArrayList();

      while(tmpAvailableFeatureEnum.hasMoreElements()) {
         tmpAvailableFeatureList.add(tmpAvailableFeatureEnum.nextElement());
      }

       it = tmpAvailableFeatureList.iterator();

      while(it.hasNext()) {
         Feature tmpAvailableFeature = (Feature)it.next();
         tmpAvailableFeature.removeClassifierRole(this);
      }

      Enumeration tmpMessage1Enum = this.getMessage1List();
      ArrayList tmpMessage1List = new ArrayList();

      while(tmpMessage1Enum.hasMoreElements()) {
         tmpMessage1List.add(tmpMessage1Enum.nextElement());
      }

       it = tmpMessage1List.iterator();

      while(it.hasNext()) {
         ((Message)it.next()).setSender((ClassifierRole)null);
      }

      Enumeration tmpBaseEnum = this.getBaseList();
      ArrayList tmpBaseList = new ArrayList();

      while(tmpBaseEnum.hasMoreElements()) {
         tmpBaseList.add(tmpBaseEnum.nextElement());
      }

       it = tmpBaseList.iterator();

      while(it.hasNext()) {
         Classifier tmpBase = (Classifier)it.next();
         tmpBase.removeClassifierRole(this);
      }

      super.internalRemove();
   }
}
