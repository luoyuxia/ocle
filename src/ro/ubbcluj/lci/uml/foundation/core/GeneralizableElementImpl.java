package ro.ubbcluj.lci.uml.foundation.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import ro.ubbcluj.lci.codegen.framework.ocl.CollectionUtilities;
import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;

public class GeneralizableElementImpl extends ModelElementImpl implements GeneralizableElement {
   protected boolean isRoot;
   protected boolean isLeaf;
   protected boolean isAbstract;
   protected Set theGeneralizationList;
   protected Set theSpecializationList;

   public GeneralizableElementImpl() {
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

   public Set getCollectionGeneralizationList() {
      return this.theGeneralizationList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theGeneralizationList);
   }

   public java.util.Enumeration getGeneralizationList() {
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

   public java.util.Enumeration getSpecializationList() {
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
      java.util.Enumeration tmpGeneralizationEnum = this.getGeneralizationList();
      ArrayList tmpGeneralizationList = new ArrayList();

      while(tmpGeneralizationEnum.hasMoreElements()) {
         tmpGeneralizationList.add(tmpGeneralizationEnum.nextElement());
      }

      Iterator it = tmpGeneralizationList.iterator();

      while(it.hasNext()) {
         ((Generalization)it.next()).remove();
      }

      java.util.Enumeration tmpSpecializationEnum = this.getSpecializationList();
      ArrayList tmpSpecializationList = new ArrayList();

      while(tmpSpecializationEnum.hasMoreElements()) {
         tmpSpecializationList.add(tmpSpecializationEnum.nextElement());
      }

       it = tmpSpecializationList.iterator();

      while(it.hasNext()) {
         ((Generalization)it.next()).remove();
      }

      super.internalRemove();
   }
}
