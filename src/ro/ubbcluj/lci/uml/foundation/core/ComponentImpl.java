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

public class ComponentImpl extends ClassifierImpl implements Component {
   protected Set theResidentList;
   protected Set theDeploymentLocationList;
   protected Set theImplementationList;

   public ComponentImpl() {
   }

   public Set getCollectionResidentList() {
      return this.theResidentList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theResidentList);
   }

   public java.util.Enumeration getResidentList() {
      return Collections.enumeration(this.getCollectionResidentList());
   }

   public Set directGetCollectionResidentList() {
      LinkedHashSet temp = new LinkedHashSet();
      if (this.theResidentList != null) {
         Iterator it = this.theResidentList.iterator();

         while(it.hasNext()) {
            temp.add(((ElementResidence)it.next()).getResident());
         }
      }

      return temp;
   }

   public java.util.Enumeration directGetResidentList() {
      return Collections.enumeration(this.directGetCollectionResidentList());
   }

   public void addResident(ElementResidence arg) {
      if (arg != null) {
         if (this.theResidentList == null) {
            this.theResidentList = new LinkedHashSet();
         }

         if (this.theResidentList.add(arg)) {
            arg.setContainer(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "resident", 1));
            }
         }
      }

   }

   public void removeResident(ElementResidence arg) {
      if (this.theResidentList != null && arg != null && this.theResidentList.remove(arg)) {
         arg.setContainer((Component)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "resident", 2));
         }
      }

   }

   public Set getCollectionDeploymentLocationList() {
      return this.theDeploymentLocationList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theDeploymentLocationList);
   }

   public java.util.Enumeration getDeploymentLocationList() {
      return Collections.enumeration(this.getCollectionDeploymentLocationList());
   }

   public void addDeploymentLocation(Node arg) {
      if (arg != null) {
         if (this.theDeploymentLocationList == null) {
            this.theDeploymentLocationList = new LinkedHashSet();
         }

         if (this.theDeploymentLocationList.add(arg)) {
            arg.addDeployedComponent(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "deploymentLocation", 1));
            }
         }
      }

   }

   public void removeDeploymentLocation(Node arg) {
      if (this.theDeploymentLocationList != null && arg != null && this.theDeploymentLocationList.remove(arg)) {
         arg.removeDeployedComponent(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "deploymentLocation", 2));
         }
      }

   }

   public Set getCollectionImplementationList() {
      return this.theImplementationList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theImplementationList);
   }

   public java.util.Enumeration getImplementationList() {
      return Collections.enumeration(this.getCollectionImplementationList());
   }

   public void addImplementation(Artifact arg) {
      if (arg != null) {
         if (this.theImplementationList == null) {
            this.theImplementationList = new LinkedHashSet();
         }

         if (this.theImplementationList.add(arg)) {
            arg.addImplementationLocation(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "implementation", 1));
            }
         }
      }

   }

   public void removeImplementation(Artifact arg) {
      if (this.theImplementationList != null && arg != null && this.theImplementationList.remove(arg)) {
         arg.removeImplementationLocation(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "implementation", 2));
         }
      }

   }

   public Set allResidentElements() {
      Set setResident = this.directGetCollectionResidentList();
      Set setparent = this.parent();
      List bagCollect = CollectionUtilities.newBag();
      Iterator iter = setparent.iterator();

      while(iter.hasNext()) {
         GeneralizableElement decl = (GeneralizableElement)iter.next();
         Component componentOclAsType = (Component)decl;
         bagCollect.add(componentOclAsType);
      }

      bagCollect = CollectionUtilities.flatten(bagCollect);
      List bagCollect0 = CollectionUtilities.newBag();
      Iterator iter0 = bagCollect.iterator();

      Set acc;
      while(iter0.hasNext()) {
         Component decl0 = (Component)iter0.next();
         acc = decl0.allResidentElements();
         bagCollect0.add(acc);
      }

      bagCollect0 = CollectionUtilities.flatten(bagCollect0);
      Set set = CollectionUtilities.newSet();
      acc = set;

      Set setUnion;
      Set setAsSet;
      for(Iterator iter1 = bagCollect0.iterator(); iter1.hasNext(); acc = setAsSet) {
         ModelElement aRE = (ModelElement)iter1.next();
         setUnion = aRE.getCollectionContainerList();
         Set setSelect = CollectionUtilities.newSet();
         Iterator iter2 = setUnion.iterator();

         while(iter2.hasNext()) {
            ElementResidence decl1 = (ElementResidence)iter2.next();
            int nVisibility = decl1.getVisibility();
            boolean bEquals = nVisibility == 3;
            int nVisibility0 = decl1.getVisibility();
            boolean bEquals0 = nVisibility0 == 2;
            boolean bOr = bEquals || bEquals0;
            if (bOr) {
               CollectionUtilities.add(setSelect, decl1);
            }
         }

         List bagCollect1 = CollectionUtilities.newBag();
         Iterator iter3 = setSelect.iterator();

         while(iter3.hasNext()) {
            ElementResidence decl2 = (ElementResidence)iter3.next();
            ModelElement modelElementResident = decl2.getResident();
            bagCollect1.add(modelElementResident);
         }

         bagCollect1 = CollectionUtilities.flatten(bagCollect1);
         List bagUnion = CollectionUtilities.union(acc, bagCollect1);
         setAsSet = CollectionUtilities.asSet(bagUnion);
      }

      setUnion = CollectionUtilities.union(setResident, acc);
      return setUnion;
   }

   protected void internalRemove() {
      java.util.Enumeration tmpResidentEnum = this.getResidentList();
      ArrayList tmpResidentList = new ArrayList();

      while(tmpResidentEnum.hasMoreElements()) {
         tmpResidentList.add(tmpResidentEnum.nextElement());
      }

      Iterator it = tmpResidentList.iterator();

      while(it.hasNext()) {
         ElementResidence tmpResident = (ElementResidence)it.next();
         tmpResident.getResident().removeContainer(tmpResident);
      }

      java.util.Enumeration tmpDeploymentLocationEnum = this.getDeploymentLocationList();
      ArrayList tmpDeploymentLocationList = new ArrayList();

      while(tmpDeploymentLocationEnum.hasMoreElements()) {
         tmpDeploymentLocationList.add(tmpDeploymentLocationEnum.nextElement());
      }

       it = tmpDeploymentLocationList.iterator();

      while(it.hasNext()) {
         Node tmpDeploymentLocation = (Node)it.next();
         tmpDeploymentLocation.removeDeployedComponent(this);
      }

      java.util.Enumeration tmpImplementationEnum = this.getImplementationList();
      ArrayList tmpImplementationList = new ArrayList();

      while(tmpImplementationEnum.hasMoreElements()) {
         tmpImplementationList.add(tmpImplementationEnum.nextElement());
      }

       it = tmpImplementationList.iterator();

      while(it.hasNext()) {
         Artifact tmpImplementation = (Artifact)it.next();
         tmpImplementation.removeImplementationLocation(this);
      }

      super.internalRemove();
   }
}
