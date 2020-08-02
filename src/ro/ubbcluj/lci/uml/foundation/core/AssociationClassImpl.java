package ro.ubbcluj.lci.uml.foundation.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import ro.ubbcluj.lci.codegen.framework.dt.OrderedSet;
import ro.ubbcluj.lci.codegen.framework.ocl.CollectionUtilities;
import ro.ubbcluj.lci.codegen.framework.ocl.Ocl;
import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.behavioralElements.activityGraphs.ClassifierInState;
import ro.ubbcluj.lci.uml.behavioralElements.activityGraphs.ObjectFlowState;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.ClassifierRole;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.Collaboration;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Instance;
import ro.ubbcluj.lci.uml.foundation.extensionMechanisms.Stereotype;

public class AssociationClassImpl extends AssociationImpl implements AssociationClass {
   protected boolean isActive;
   protected Set theOwnedElementList;
   protected OrderedSet theTypedFeatureList;
   protected Set theInstanceList;
   protected Set thePowertypeRangeList;
   protected Set theTypedParameterList;
   protected Set theCollaborationList;
   protected Set theObjectFlowStateList;
   protected OrderedSet theFeatureList;
   protected Set theClassifierInStateList;
   protected Set theClassifierRoleList;
   protected Set theAssociationList;
   protected Set theSpecifiedEndList;

   public AssociationClassImpl() {
   }

   public boolean isActive() {
      return this.isActive;
   }

   public void setActive(boolean isActive) {
      this.isActive = isActive;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "isActive", 0));
      }

   }

   public Set getCollectionOwnedElementList() {
      return this.theOwnedElementList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theOwnedElementList);
   }

   public java.util.Enumeration getOwnedElementList() {
      return Collections.enumeration(this.getCollectionOwnedElementList());
   }

   public Set directGetCollectionOwnedElementList() {
      LinkedHashSet temp = new LinkedHashSet();
      if (this.theOwnedElementList != null) {
         Iterator it = this.theOwnedElementList.iterator();

         while(it.hasNext()) {
            temp.add(((ElementOwnership)it.next()).getOwnedElement());
         }
      }

      return temp;
   }

   public java.util.Enumeration directGetOwnedElementList() {
      return Collections.enumeration(this.directGetCollectionOwnedElementList());
   }

   public void addOwnedElement(ElementOwnership arg) {
      if (arg != null) {
         if (this.theOwnedElementList == null) {
            this.theOwnedElementList = new LinkedHashSet();
         }

         if (this.theOwnedElementList.add(arg)) {
            arg.setNamespace(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "ownedElement", 1));
            }
         }
      }

   }

   public void removeOwnedElement(ElementOwnership arg) {
      if (this.theOwnedElementList != null && arg != null && this.theOwnedElementList.remove(arg)) {
         arg.setNamespace((Namespace)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "ownedElement", 2));
         }
      }

   }

   public OrderedSet getCollectionTypedFeatureList() {
      return this.theTypedFeatureList == null ? CollectionUtilities.newOrderedSet() : CollectionUtilities.newOrderedSet(this.theTypedFeatureList);
   }

   public java.util.Enumeration getTypedFeatureList() {
      return Collections.enumeration(this.getCollectionTypedFeatureList());
   }

   public void addTypedFeature(StructuralFeature arg) {
      if (arg != null) {
         if (this.theTypedFeatureList == null) {
            this.theTypedFeatureList = new OrderedSet();
         }

         if (this.theTypedFeatureList.add(arg)) {
            arg.setType(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "typedFeature", 1));
            }
         }
      }

   }

   public void removeTypedFeature(StructuralFeature arg) {
      if (this.theTypedFeatureList != null && arg != null && this.theTypedFeatureList.remove(arg)) {
         arg.setType((Classifier)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "typedFeature", 2));
         }
      }

   }

   public Set getCollectionInstanceList() {
      return this.theInstanceList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theInstanceList);
   }

   public java.util.Enumeration getInstanceList() {
      return Collections.enumeration(this.getCollectionInstanceList());
   }

   public void addInstance(Instance arg) {
      if (arg != null) {
         if (this.theInstanceList == null) {
            this.theInstanceList = new LinkedHashSet();
         }

         if (this.theInstanceList.add(arg)) {
            arg.addClassifier(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "instance", 1));
            }
         }
      }

   }

   public void removeInstance(Instance arg) {
      if (this.theInstanceList != null && arg != null && this.theInstanceList.remove(arg)) {
         arg.removeClassifier(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "instance", 2));
         }
      }

   }

   public Set getCollectionPowertypeRangeList() {
      return this.thePowertypeRangeList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.thePowertypeRangeList);
   }

   public java.util.Enumeration getPowertypeRangeList() {
      return Collections.enumeration(this.getCollectionPowertypeRangeList());
   }

   public void addPowertypeRange(Generalization arg) {
      if (arg != null) {
         if (this.thePowertypeRangeList == null) {
            this.thePowertypeRangeList = new LinkedHashSet();
         }

         if (this.thePowertypeRangeList.add(arg)) {
            arg.setPowertype(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "powertypeRange", 1));
            }
         }
      }

   }

   public void removePowertypeRange(Generalization arg) {
      if (this.thePowertypeRangeList != null && arg != null && this.thePowertypeRangeList.remove(arg)) {
         arg.setPowertype((Classifier)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "powertypeRange", 2));
         }
      }

   }

   public Set getCollectionTypedParameterList() {
      return this.theTypedParameterList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theTypedParameterList);
   }

   public java.util.Enumeration getTypedParameterList() {
      return Collections.enumeration(this.getCollectionTypedParameterList());
   }

   public void addTypedParameter(Parameter arg) {
      if (arg != null) {
         if (this.theTypedParameterList == null) {
            this.theTypedParameterList = new LinkedHashSet();
         }

         if (this.theTypedParameterList.add(arg)) {
            arg.setType(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "typedParameter", 1));
            }
         }
      }

   }

   public void removeTypedParameter(Parameter arg) {
      if (this.theTypedParameterList != null && arg != null && this.theTypedParameterList.remove(arg)) {
         arg.setType((Classifier)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "typedParameter", 2));
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
            arg.setRepresentedClassifier(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "collaboration", 1));
            }
         }
      }

   }

   public void removeCollaboration(Collaboration arg) {
      if (this.theCollaborationList != null && arg != null && this.theCollaborationList.remove(arg)) {
         arg.setRepresentedClassifier((Classifier)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "collaboration", 2));
         }
      }

   }

   public Set getCollectionObjectFlowStateList() {
      return this.theObjectFlowStateList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theObjectFlowStateList);
   }

   public java.util.Enumeration getObjectFlowStateList() {
      return Collections.enumeration(this.getCollectionObjectFlowStateList());
   }

   public void addObjectFlowState(ObjectFlowState arg) {
      if (arg != null) {
         if (this.theObjectFlowStateList == null) {
            this.theObjectFlowStateList = new LinkedHashSet();
         }

         if (this.theObjectFlowStateList.add(arg)) {
            arg.setType(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "objectFlowState", 1));
            }
         }
      }

   }

   public void removeObjectFlowState(ObjectFlowState arg) {
      if (this.theObjectFlowStateList != null && arg != null && this.theObjectFlowStateList.remove(arg)) {
         arg.setType((Classifier)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "objectFlowState", 2));
         }
      }

   }

   public OrderedSet getCollectionFeatureList() {
      return this.theFeatureList == null ? CollectionUtilities.newOrderedSet() : CollectionUtilities.newOrderedSet(this.theFeatureList);
   }

   public java.util.Enumeration getFeatureList() {
      return Collections.enumeration(this.getCollectionFeatureList());
   }

   public void addFeature(Feature arg) {
      if (arg != null) {
         if (this.theFeatureList == null) {
            this.theFeatureList = new OrderedSet();
         }

         if (this.theFeatureList.add(arg)) {
            arg.setOwner(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "feature", 1));
            }
         }
      }

   }

   public void removeFeature(Feature arg) {
      if (this.theFeatureList != null && arg != null && this.theFeatureList.remove(arg)) {
         arg.setOwner((Classifier)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "feature", 2));
         }
      }

   }

   public Set getCollectionClassifierInStateList() {
      return this.theClassifierInStateList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theClassifierInStateList);
   }

   public java.util.Enumeration getClassifierInStateList() {
      return Collections.enumeration(this.getCollectionClassifierInStateList());
   }

   public void addClassifierInState(ClassifierInState arg) {
      if (arg != null) {
         if (this.theClassifierInStateList == null) {
            this.theClassifierInStateList = new LinkedHashSet();
         }

         if (this.theClassifierInStateList.add(arg)) {
            arg.setType(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "classifierInState", 1));
            }
         }
      }

   }

   public void removeClassifierInState(ClassifierInState arg) {
      if (this.theClassifierInStateList != null && arg != null && this.theClassifierInStateList.remove(arg)) {
         arg.setType((Classifier)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "classifierInState", 2));
         }
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
            arg.addBase(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "classifierRole", 1));
            }
         }
      }

   }

   public void removeClassifierRole(ClassifierRole arg) {
      if (this.theClassifierRoleList != null && arg != null && this.theClassifierRoleList.remove(arg)) {
         arg.removeBase(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "classifierRole", 2));
         }
      }

   }

   public Set getCollectionAssociationList() {
      return this.theAssociationList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theAssociationList);
   }

   public java.util.Enumeration getAssociationList() {
      return Collections.enumeration(this.getCollectionAssociationList());
   }

   public void addAssociation(AssociationEnd arg) {
      if (arg != null) {
         if (this.theAssociationList == null) {
            this.theAssociationList = new LinkedHashSet();
         }

         if (this.theAssociationList.add(arg)) {
            arg.setParticipant(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "association", 1));
            }
         }
      }

   }

   public void removeAssociation(AssociationEnd arg) {
      if (this.theAssociationList != null && arg != null && this.theAssociationList.remove(arg)) {
         arg.setParticipant((Classifier)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "association", 2));
         }
      }

   }

   public Set getCollectionSpecifiedEndList() {
      return this.theSpecifiedEndList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theSpecifiedEndList);
   }

   public java.util.Enumeration getSpecifiedEndList() {
      return Collections.enumeration(this.getCollectionSpecifiedEndList());
   }

   public void addSpecifiedEnd(AssociationEnd arg) {
      if (arg != null) {
         if (this.theSpecifiedEndList == null) {
            this.theSpecifiedEndList = new LinkedHashSet();
         }

         if (this.theSpecifiedEndList.add(arg)) {
            arg.addSpecification(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "specifiedEnd", 1));
            }
         }
      }

   }

   public void removeSpecifiedEnd(AssociationEnd arg) {
      if (this.theSpecifiedEndList != null && arg != null && this.theSpecifiedEndList.remove(arg)) {
         arg.removeSpecification(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "specifiedEnd", 2));
         }
      }

   }

   public Set allConnections() {
      List seqConnection = this.getCollectionConnectionList();
      Set setAsSet = CollectionUtilities.asSet(seqConnection);
      Set setoppositeAssociationEnds = this.oppositeAssociationEnds();
      Set setUnion = CollectionUtilities.union(setAsSet, setoppositeAssociationEnds);
      Set setallParents = this.allParents();
      Set setSelect = CollectionUtilities.newSet();
      Iterator iter = setallParents.iterator();

      while(iter.hasNext()) {
         GeneralizableElement s = (GeneralizableElement)iter.next();
         boolean bOclIsTypeOf = Ocl.isTypeOf(s, Ocl.getType(new java.lang.Class[]{AssociationClass.class}));
         if (bOclIsTypeOf) {
            CollectionUtilities.add(setSelect, s);
         }
      }

      List bagCollect = CollectionUtilities.newBag();
      Iterator iter0 = setSelect.iterator();

      while(iter0.hasNext()) {
         GeneralizableElement decl = (GeneralizableElement)iter0.next();
         AssociationClass associationClassOclAsType = (AssociationClass)decl;
         bagCollect.add(associationClassOclAsType);
      }

      bagCollect = CollectionUtilities.flatten(bagCollect);
      List bagCollect0 = CollectionUtilities.newBag();
      Iterator iter1 = bagCollect.iterator();

      while(iter1.hasNext()) {
         AssociationClass ac = (AssociationClass)iter1.next();
         List seqConnection0 = ac.getCollectionConnectionList();
         bagCollect0.add(seqConnection0);
      }

      bagCollect0 = CollectionUtilities.flatten(bagCollect0);
      Set setAsSet0 = CollectionUtilities.asSet(bagCollect0);
      Set setallParents0 = this.allParents();
      Set setSelect0 = CollectionUtilities.newSet();
      Iterator iter2 = setallParents0.iterator();

      while(iter2.hasNext()) {
         GeneralizableElement s = (GeneralizableElement)iter2.next();
         boolean bOclIsTypeOf0 = Ocl.isTypeOf(s, Ocl.getType(new java.lang.Class[]{Class.class}));
         if (bOclIsTypeOf0) {
            CollectionUtilities.add(setSelect0, s);
         }
      }

      List bagCollect1 = CollectionUtilities.newBag();
      Iterator iter3 = setSelect0.iterator();

      while(iter3.hasNext()) {
         GeneralizableElement decl0 = (GeneralizableElement)iter3.next();
         Class classOclAsType = (Class)decl0;
         bagCollect1.add(classOclAsType);
      }

      bagCollect1 = CollectionUtilities.flatten(bagCollect1);
      List bagCollect2 = CollectionUtilities.newBag();
      Iterator iter4 = bagCollect1.iterator();

      Set setUnion1;
      while(iter4.hasNext()) {
         Class c = (Class)iter4.next();
         setUnion1 = c.oppositeAssociationEnds();
         bagCollect2.add(setUnion1);
      }

      bagCollect2 = CollectionUtilities.flatten(bagCollect2);
      Set setAsSet1 = CollectionUtilities.asSet(bagCollect2);
      setUnion1 = CollectionUtilities.union(setAsSet0, setAsSet1);
      Set setSelect1 = CollectionUtilities.newSet();
      Iterator iter5 = setUnion1.iterator();

      while(iter5.hasNext()) {
         AssociationEnd ae = (AssociationEnd)iter5.next();
         int nVisibility = ae.getVisibility();
         boolean bEquals = nVisibility == 3;
         int nVisibility0 = ae.getVisibility();
         boolean bEquals0 = nVisibility0 == 2;
         boolean bOr = bEquals || bEquals0;
         if (bOr) {
            CollectionUtilities.add(setSelect1, ae);
         }
      }

      Set setUnion0 = CollectionUtilities.union(setUnion, setSelect1);
      return setUnion0;
   }

   public Set allOppositeAssociationEnds() {
      Set setallConnections = this.allConnections();
      return setallConnections;
   }

   public Set contentsAndImports() {
      Set setOwnedElement = this.directGetCollectionOwnedElementList();
      Set setallSuppliersP = this.allSuppliersP();
      Set setSelect = CollectionUtilities.newSet();
      Iterator iter = setallSuppliersP.iterator();

      while(iter.hasNext()) {
         ModelElement iter1 = (ModelElement)iter.next();
         boolean bOclIsKindOf = Ocl.isKindOf(iter1, Ocl.getType(new java.lang.Class[]{Namespace.class}));
         if (bOclIsKindOf) {
            CollectionUtilities.add(setSelect, iter1);
         }
      }

      List bagCollect = CollectionUtilities.newBag();
      Iterator iter0 = setSelect.iterator();

      while(iter0.hasNext()) {
         ModelElement decl = (ModelElement)iter0.next();
         Namespace namespaceOclAsType = (Namespace)decl;
         bagCollect.add(namespaceOclAsType);
      }

      bagCollect = CollectionUtilities.flatten(bagCollect);
      List bagCollect0 = CollectionUtilities.newBag();
      Iterator iter1 = bagCollect.iterator();

      while(iter1.hasNext()) {
         Namespace decl0 = (Namespace)iter1.next();
         Set setOwnedElement0 = decl0.directGetCollectionOwnedElementList();
         bagCollect0.add(setOwnedElement0);
      }

      bagCollect0 = CollectionUtilities.flatten(bagCollect0);
      List bagSelect = CollectionUtilities.newBag();
      Iterator iter2 = bagCollect0.iterator();

      while(iter2.hasNext()) {
         ModelElement s = (ModelElement)iter2.next();
         ElementOwnership elementOwnershipElementOwnership = s.getNamespace();
         int nVisibility = elementOwnershipElementOwnership.getVisibility();
         boolean bEquals = nVisibility == 3;
         if (bEquals) {
            CollectionUtilities.add(bagSelect, s);
         }
      }

      Set setAsSet = CollectionUtilities.asSet(bagSelect);
      Set setUnion = CollectionUtilities.union(setOwnedElement, setAsSet);
      return setUnion;
   }

   public Set contentsAndImportsD() {
      Set setOwnedElement = this.directGetCollectionOwnedElementList();
      Set setallSuppliersD = this.allSuppliersD();
      Set setSelect = CollectionUtilities.newSet();
      Iterator iter = setallSuppliersD.iterator();

      while(iter.hasNext()) {
         ModelElement iter1 = (ModelElement)iter.next();
         boolean bOclIsKindOf = Ocl.isKindOf(iter1, Ocl.getType(new java.lang.Class[]{Namespace.class}));
         if (bOclIsKindOf) {
            CollectionUtilities.add(setSelect, iter1);
         }
      }

      List bagCollect = CollectionUtilities.newBag();
      Iterator iter0 = setSelect.iterator();

      while(iter0.hasNext()) {
         ModelElement decl = (ModelElement)iter0.next();
         Namespace namespaceOclAsType = (Namespace)decl;
         bagCollect.add(namespaceOclAsType);
      }

      bagCollect = CollectionUtilities.flatten(bagCollect);
      List bagCollect0 = CollectionUtilities.newBag();
      Iterator iter1 = bagCollect.iterator();

      while(iter1.hasNext()) {
         Namespace decl0 = (Namespace)iter1.next();
         Set setOwnedElement0 = decl0.directGetCollectionOwnedElementList();
         bagCollect0.add(setOwnedElement0);
      }

      bagCollect0 = CollectionUtilities.flatten(bagCollect0);
      List bagSelect = CollectionUtilities.newBag();
      Iterator iter2 = bagCollect0.iterator();

      while(iter2.hasNext()) {
         ModelElement s = (ModelElement)iter2.next();
         ElementOwnership elementOwnershipElementOwnership = s.getNamespace();
         int nVisibility = elementOwnershipElementOwnership.getVisibility();
         boolean bEquals = nVisibility == 3;
         if (bEquals) {
            CollectionUtilities.add(bagSelect, s);
         }
      }

      Set setAsSet = CollectionUtilities.asSet(bagSelect);
      Set setUnion = CollectionUtilities.union(setOwnedElement, setAsSet);
      return setUnion;
   }

   public Set contents() {
      Namespace namespaceNamespace = this.directGetNamespace();
      boolean bIsUndefined = Ocl.isUndefined(namespaceNamespace);
      Set setIf;
      Set setSelect;
      if (bIsUndefined) {
         setSelect = this.contentsAndImports();
         setIf = setSelect;
      } else {
         setSelect = this.contentsAndImports();
         Namespace namespaceNamespace0 = this.directGetNamespace();
         Set setcontents = namespaceNamespace0.contents();
         Set setUnion = CollectionUtilities.union(setSelect, setcontents);
         setIf = setUnion;
      }

      setSelect = CollectionUtilities.newSet();
      Iterator iter = setIf.iterator();

      while(iter.hasNext()) {
         ModelElement iter1 = (ModelElement)iter.next();
         boolean bOclIsKindOf = Ocl.isKindOf(iter1, Ocl.getType(new java.lang.Class[]{Classifier.class}));
         if (bOclIsKindOf) {
            CollectionUtilities.add(setSelect, iter1);
         }
      }

      return setSelect;
   }

   public Set contentsD() {
      Namespace namespaceNamespace = this.directGetNamespace();
      boolean bIsUndefined = Ocl.isUndefined(namespaceNamespace);
      Set setIf;
      Set setSelect;
      if (bIsUndefined) {
         setSelect = this.contentsAndImportsD();
         setIf = setSelect;
      } else {
         setSelect = this.contentsAndImportsD();
         Namespace namespaceNamespace0 = this.directGetNamespace();
         Set setcontentsD = namespaceNamespace0.contentsD();
         Set setUnion = CollectionUtilities.union(setSelect, setcontentsD);
         setIf = setUnion;
      }

      setSelect = CollectionUtilities.newSet();
      Iterator iter = setIf.iterator();

      while(iter.hasNext()) {
         ModelElement iter1 = (ModelElement)iter.next();
         boolean bOclIsKindOf = Ocl.isKindOf(iter1, Ocl.getType(new java.lang.Class[]{Classifier.class}));
         if (bOclIsKindOf) {
            CollectionUtilities.add(setSelect, iter1);
         }
      }

      return setSelect;
   }

   public Set allContents() {
      boolean bOclIsKindOf = Ocl.isKindOf(this, Ocl.getType(new java.lang.Class[]{GeneralizableElement.class}));
      Set setIf;
      Set setcontents;
      if (bOclIsKindOf) {
         setcontents = this.contents();
         Set setallParents = this.allParents();
         List bagCollect = CollectionUtilities.newBag();
         Iterator iter = setallParents.iterator();

         while(iter.hasNext()) {
            GeneralizableElement decl = (GeneralizableElement)iter.next();
            Namespace namespaceOclAsType = (Namespace)decl;
            bagCollect.add(namespaceOclAsType);
         }

         bagCollect = CollectionUtilities.flatten(bagCollect);
         List bagCollect0 = CollectionUtilities.newBag();
         Iterator iter0 = bagCollect.iterator();

         while(iter0.hasNext()) {
            Namespace decl0 = (Namespace)iter0.next();
            Set setOwnedElement = decl0.directGetCollectionOwnedElementList();
            bagCollect0.add(setOwnedElement);
         }

         bagCollect0 = CollectionUtilities.flatten(bagCollect0);
         List bagSelect = CollectionUtilities.newBag();
         Iterator iter1 = bagCollect0.iterator();

         while(iter1.hasNext()) {
            ModelElement e = (ModelElement)iter1.next();
            ElementOwnership elementOwnershipElementOwnership = e.getNamespace();
            int nVisibility = elementOwnershipElementOwnership.getVisibility();
            boolean bEquals = nVisibility == 3;
            ElementOwnership elementOwnershipElementOwnership0 = e.getNamespace();
            int nVisibility0 = elementOwnershipElementOwnership0.getVisibility();
            boolean bEquals0 = nVisibility0 == 2;
            boolean bOr = bEquals || bEquals0;
            if (bOr) {
               CollectionUtilities.add(bagSelect, e);
            }
         }

         Set setAsSet = CollectionUtilities.asSet(bagSelect);
         Set setUnion = CollectionUtilities.union(setcontents, setAsSet);
         setIf = setUnion;
      } else {
         setcontents = this.contents();
         setIf = setcontents;
      }

      return setIf;
   }

   public Set allContentsD() {
      boolean bOclIsKindOf = Ocl.isKindOf(this, Ocl.getType(new java.lang.Class[]{GeneralizableElement.class}));
      Set setIf;
      Set setcontentsD;
      if (bOclIsKindOf) {
         setcontentsD = this.contentsD();
         Set setallParents = this.allParents();
         List bagCollect = CollectionUtilities.newBag();
         Iterator iter = setallParents.iterator();

         while(iter.hasNext()) {
            GeneralizableElement decl = (GeneralizableElement)iter.next();
            Namespace namespaceOclAsType = (Namespace)decl;
            bagCollect.add(namespaceOclAsType);
         }

         bagCollect = CollectionUtilities.flatten(bagCollect);
         List bagCollect0 = CollectionUtilities.newBag();
         Iterator iter0 = bagCollect.iterator();

         while(iter0.hasNext()) {
            Namespace decl0 = (Namespace)iter0.next();
            Set setOwnedElement = decl0.directGetCollectionOwnedElementList();
            bagCollect0.add(setOwnedElement);
         }

         bagCollect0 = CollectionUtilities.flatten(bagCollect0);
         List bagSelect = CollectionUtilities.newBag();
         Iterator iter1 = bagCollect0.iterator();

         while(iter1.hasNext()) {
            ModelElement e = (ModelElement)iter1.next();
            ElementOwnership elementOwnershipElementOwnership = e.getNamespace();
            int nVisibility = elementOwnershipElementOwnership.getVisibility();
            boolean bEquals = nVisibility == 3;
            ElementOwnership elementOwnershipElementOwnership0 = e.getNamespace();
            int nVisibility0 = elementOwnershipElementOwnership0.getVisibility();
            boolean bEquals0 = nVisibility0 == 2;
            boolean bOr = bEquals || bEquals0;
            if (bOr) {
               CollectionUtilities.add(bagSelect, e);
            }
         }

         Set setAsSet = CollectionUtilities.asSet(bagSelect);
         Set setUnion = CollectionUtilities.union(setcontentsD, setAsSet);
         setIf = setUnion;
      } else {
         setcontentsD = this.contentsD();
         setIf = setcontentsD;
      }

      return setIf;
   }

   public Set clDepSuplElem(Dependency d) {
      Set setSupplier = d.getCollectionSupplierList();
      boolean bForAll = true;

      boolean bOclIsKindOf;
      for(Iterator iter = setSupplier.iterator(); bForAll && iter.hasNext(); bForAll = bOclIsKindOf) {
         ModelElement iter1 = (ModelElement)iter.next();
         bOclIsKindOf = Ocl.isKindOf(iter1, Ocl.getType(new java.lang.Class[]{Package.class}));
      }

      Set setIf;
      Set setIf0;
      if (bForAll) {
         Set setSupplier0 = d.getCollectionSupplierList();
         List bagCollect = CollectionUtilities.newBag();
         Iterator iter0 = setSupplier0.iterator();

         while(iter0.hasNext()) {
            ModelElement decl = (ModelElement)iter0.next();
            Namespace namespaceOclAsType = (Namespace)decl;
            bagCollect.add(namespaceOclAsType);
         }

         bagCollect = CollectionUtilities.flatten(bagCollect);
         List bagCollect0 = CollectionUtilities.newBag();
         Iterator iter1 = bagCollect.iterator();

         Set setSupplier1;
         while(iter1.hasNext()) {
            Namespace decl0 = (Namespace)iter1.next();
            setSupplier1 = decl0.directGetCollectionOwnedElementList();
            bagCollect0.add(setSupplier1);
         }

         bagCollect0 = CollectionUtilities.flatten(bagCollect0);
         boolean bIsEmpty = CollectionUtilities.isEmpty((Collection)bagCollect0);
         if (bIsEmpty) {
            setSupplier1 = CollectionUtilities.newSet();
            setIf0 = setSupplier1;
         } else {
            setSupplier1 = d.getCollectionSupplierList();
            Set setSupplier2 = d.getCollectionSupplierList();
            List bagCollect1 = CollectionUtilities.newBag();
            Iterator iter2 = setSupplier2.iterator();

            while(iter2.hasNext()) {
               ModelElement decl1 = (ModelElement)iter2.next();
               Namespace namespaceOclAsType0 = (Namespace)decl1;
               bagCollect1.add(namespaceOclAsType0);
            }

            bagCollect1 = CollectionUtilities.flatten(bagCollect1);
            List bagCollect2 = CollectionUtilities.newBag();
            Iterator iter3 = bagCollect1.iterator();

            Set setUnion;
            while(iter3.hasNext()) {
               Namespace decl2 = (Namespace)iter3.next();
               setUnion = decl2.directGetCollectionOwnedElementList();
               bagCollect2.add(setUnion);
            }

            bagCollect2 = CollectionUtilities.flatten(bagCollect2);
            Set setAsSet = CollectionUtilities.asSet(bagCollect2);
            setUnion = CollectionUtilities.union(setSupplier1, setAsSet);
            setIf0 = setUnion;
         }

         setIf = setIf0;
      } else {
         setIf0 = d.getCollectionSupplierList();
         setIf = setIf0;
      }

      return setIf;
   }

   public Set clDepStName(Dependency dep) {
      Set setStereotype = dep.getCollectionStereotypeList();
      List bagCollect = CollectionUtilities.newBag();
      Iterator iter = setStereotype.iterator();

      while(iter.hasNext()) {
         Stereotype decl = (Stereotype)iter.next();
         String strName = decl.getName();
         bagCollect.add(strName);
      }

      bagCollect = CollectionUtilities.flatten(bagCollect);
      Set setAsSet = CollectionUtilities.asSet(bagCollect);
      return setAsSet;
   }

   public Set depElemVisibleInNamespace() {
      Namespace namespaceNamespace = this.directGetNamespace();
      boolean bIsUndefined = Ocl.isUndefined(namespaceNamespace);
      Set setIf;
      Set setSelect;
      Set set;
      Set acc;
      if (bIsUndefined) {
         setSelect = this.getCollectionClientDependencyList();
         setIf = setSelect;
      } else {
         setSelect = this.getCollectionClientDependencyList();
         Namespace namespaceNamespace0 = this.directGetNamespace();
         set = namespaceNamespace0.getCollectionClientDependencyList();
         acc = CollectionUtilities.union(setSelect, set);
         setIf = acc;
      }

      setSelect = CollectionUtilities.newSet();
      Iterator iter = setIf.iterator();

      while(iter.hasNext()) {
         Dependency iter1 = (Dependency)iter.next();
         boolean bOclIsKindOf = Ocl.isKindOf(iter1, Ocl.getType(new java.lang.Class[]{Dependency.class}));
         boolean bOclIsKindOf0 = Ocl.isKindOf(iter1, Ocl.getType(new java.lang.Class[]{Permission.class}));
         boolean bOr = bOclIsKindOf || bOclIsKindOf0;
         if (bOr) {
            CollectionUtilities.add(setSelect, iter1);
         }
      }

      set = CollectionUtilities.newSet();
      acc = set;

      Set setIf0;
      for(Iterator iter0 = setSelect.iterator(); iter0.hasNext(); acc = setIf0) {
         Dependency cD = (Dependency)iter0.next();
         Set setclDepStName = this.clDepStName(cD);
         boolean bExcludes = CollectionUtilities.excludes(setclDepStName, "friend");
         Set setclDepSuplElem0;
         Set setSelect1;
         Iterator iter2;
         ModelElement decl;
         boolean bOclIsKindOf2;
         Set setUnion1;
         if (bExcludes) {
            setclDepSuplElem0 = this.clDepSuplElem(cD);
            setSelect1 = CollectionUtilities.newSet();
            iter2 = setclDepSuplElem0.iterator();

            while(iter2.hasNext()) {
               decl = (ModelElement)iter2.next();
               bOclIsKindOf2 = Ocl.isKindOf(decl, Ocl.getType(new java.lang.Class[]{Classifier.class}));
               ElementOwnership elementOwnershipElementOwnership = decl.getNamespace();
               int nVisibility = elementOwnershipElementOwnership.getVisibility();
               boolean bEquals = nVisibility == 3;
               boolean bAnd = bOclIsKindOf2 && bEquals;
               if (bAnd) {
                  CollectionUtilities.add(setSelect1, decl);
               }
            }

            setUnion1 = CollectionUtilities.union(acc, setSelect1);
            setIf0 = setUnion1;
         } else {
            setclDepSuplElem0 = this.clDepSuplElem(cD);
            setSelect1 = CollectionUtilities.newSet();
            iter2 = setclDepSuplElem0.iterator();

            while(iter2.hasNext()) {
               decl = (ModelElement)iter2.next();
               bOclIsKindOf2 = Ocl.isKindOf(decl, Ocl.getType(new java.lang.Class[]{Classifier.class}));
               if (bOclIsKindOf2) {
                  CollectionUtilities.add(setSelect1, decl);
               }
            }

            setUnion1 = CollectionUtilities.union(acc, setSelect1);
            setIf0 = setUnion1;
         }
      }

      return acc;
   }

   public Set allVisibleElements() {
      Set setallContents = this.allContents();
      Set setSelect = CollectionUtilities.newSet();
      Iterator iter = setallContents.iterator();

      while(iter.hasNext()) {
         ModelElement iter1 = (ModelElement)iter.next();
         boolean bOclIsKindOf = Ocl.isKindOf(iter1, Ocl.getType(new java.lang.Class[]{Classifier.class}));
         if (bOclIsKindOf) {
            CollectionUtilities.add(setSelect, iter1);
         }
      }

      Set setdepElemVisibleInNamespace = this.depElemVisibleInNamespace();
      Set setUnion = CollectionUtilities.union(setSelect, setdepElemVisibleInNamespace);
      return setUnion;
   }

   public Set allSurroundingNamespaces() {
      Namespace namespaceNamespace = this.directGetNamespace();
      boolean bIsUndefined = Ocl.isUndefined(namespaceNamespace);
      Set setIf;
      Set set;
      if (bIsUndefined) {
         set = CollectionUtilities.newSet();
         setIf = set;
      } else {
         set = CollectionUtilities.newSet();
         Namespace namespaceNamespace0 = this.directGetNamespace();
         CollectionUtilities.add(set, namespaceNamespace0);
         Namespace namespaceNamespace1 = this.directGetNamespace();
         Set setallSurroundingNamespaces = namespaceNamespace1.allSurroundingNamespaces();
         Set setUnion = CollectionUtilities.union(set, setallSurroundingNamespaces);
         setIf = setUnion;
      }

      return setIf;
   }

   public List allFeatures() {
      List seqFeature = this.getCollectionFeatureList();
      Set setparent = this.parent();
      List bagCollect = CollectionUtilities.newBag();
      Iterator iter = setparent.iterator();

      while(iter.hasNext()) {
         GeneralizableElement decl = (GeneralizableElement)iter.next();
         Classifier classifierOclAsType = (Classifier)decl;
         bagCollect.add(classifierOclAsType);
      }

      bagCollect = CollectionUtilities.flatten(bagCollect);
      List bagCollect0 = CollectionUtilities.newBag();
      Iterator iter0 = bagCollect.iterator();

      while(iter0.hasNext()) {
         Classifier decl0 = (Classifier)iter0.next();
         List seqallFeatures = decl0.allFeatures();
         bagCollect0.add(seqallFeatures);
      }

      bagCollect0 = CollectionUtilities.flatten(bagCollect0);
      List bagSelect = CollectionUtilities.newBag();
      Iterator iter1 = bagCollect0.iterator();

      while(iter1.hasNext()) {
         Feature decl1 = (Feature)iter1.next();
         ElementOwnership elementOwnershipElementOwnership = decl1.getNamespace();
         int nVisibility = elementOwnershipElementOwnership.getVisibility();
         boolean bEquals = nVisibility == 3;
         ElementOwnership elementOwnershipElementOwnership0 = decl1.getNamespace();
         int nVisibility0 = elementOwnershipElementOwnership0.getVisibility();
         boolean bEquals0 = nVisibility0 == 2;
         boolean bOr = bEquals || bEquals0;
         if (bOr) {
            CollectionUtilities.add(bagSelect, decl1);
         }
      }

      List seqAsSequence = CollectionUtilities.asSequence(bagSelect);
      List seqUnion = CollectionUtilities.union((List)seqFeature, (List)seqAsSequence);
      return seqUnion;
   }

   public Set allOperations() {
      List seqallFeatures = this.allFeatures();
      List seqSelect = CollectionUtilities.newSequence();
      Iterator iter = seqallFeatures.iterator();

      while(iter.hasNext()) {
         Feature f = (Feature)iter.next();
         boolean bOclIsKindOf = Ocl.isKindOf(f, Ocl.getType(new java.lang.Class[]{Operation.class}));
         if (bOclIsKindOf) {
            CollectionUtilities.add(seqSelect, f);
         }
      }

      List seqCollect = CollectionUtilities.newBag();
      Iterator iter0 = seqSelect.iterator();

      while(iter0.hasNext()) {
         Feature decl = (Feature)iter0.next();
         Operation operationOclAsType = (Operation)decl;
         seqCollect.add(operationOclAsType);
      }

      seqCollect = CollectionUtilities.flatten(seqCollect);
      Set setAsSet = CollectionUtilities.asSet(seqCollect);
      return setAsSet;
   }

   public Set allMethods() {
      List seqallFeatures = this.allFeatures();
      List seqSelect = CollectionUtilities.newSequence();
      Iterator iter = seqallFeatures.iterator();

      while(iter.hasNext()) {
         Feature f = (Feature)iter.next();
         boolean bOclIsKindOf = Ocl.isKindOf(f, Ocl.getType(new java.lang.Class[]{Method.class}));
         if (bOclIsKindOf) {
            CollectionUtilities.add(seqSelect, f);
         }
      }

      List seqCollect = CollectionUtilities.newBag();
      Iterator iter0 = seqSelect.iterator();

      while(iter0.hasNext()) {
         Feature decl = (Feature)iter0.next();
         Method methodOclAsType = (Method)decl;
         seqCollect.add(methodOclAsType);
      }

      seqCollect = CollectionUtilities.flatten(seqCollect);
      Set setAsSet = CollectionUtilities.asSet(seqCollect);
      return setAsSet;
   }

   public Set allAttributes() {
      List seqallFeatures = this.allFeatures();
      List seqSelect = CollectionUtilities.newSequence();
      Iterator iter = seqallFeatures.iterator();

      while(iter.hasNext()) {
         Feature f = (Feature)iter.next();
         boolean bOclIsKindOf = Ocl.isKindOf(f, Ocl.getType(new java.lang.Class[]{Attribute.class}));
         if (bOclIsKindOf) {
            CollectionUtilities.add(seqSelect, f);
         }
      }

      List seqCollect = CollectionUtilities.newBag();
      Iterator iter0 = seqSelect.iterator();

      while(iter0.hasNext()) {
         Feature decl = (Feature)iter0.next();
         Attribute attributeOclAsType = (Attribute)decl;
         seqCollect.add(attributeOclAsType);
      }

      seqCollect = CollectionUtilities.flatten(seqCollect);
      Set setAsSet = CollectionUtilities.asSet(seqCollect);
      return setAsSet;
   }

   public Set associations() {
      Set setAssociation = this.getCollectionAssociationList();
      List bagCollect = CollectionUtilities.newBag();
      Iterator iter = setAssociation.iterator();

      while(iter.hasNext()) {
         AssociationEnd decl = (AssociationEnd)iter.next();
         Association associationAssociation = decl.getAssociation();
         bagCollect.add(associationAssociation);
      }

      bagCollect = CollectionUtilities.flatten(bagCollect);
      Set setAsSet = CollectionUtilities.asSet(bagCollect);
      return setAsSet;
   }

   public Set allAssociations() {
      Set setassociations = this.associations();
      Set setparent = this.parent();
      List bagCollect = CollectionUtilities.newBag();
      Iterator iter = setparent.iterator();

      while(iter.hasNext()) {
         GeneralizableElement decl = (GeneralizableElement)iter.next();
         Classifier classifierOclAsType = (Classifier)decl;
         bagCollect.add(classifierOclAsType);
      }

      bagCollect = CollectionUtilities.flatten(bagCollect);
      List bagCollect0 = CollectionUtilities.newBag();
      Iterator iter0 = bagCollect.iterator();

      Set setAsSet;
      while(iter0.hasNext()) {
         Classifier decl0 = (Classifier)iter0.next();
         setAsSet = decl0.allAssociations();
         bagCollect0.add(setAsSet);
      }

      bagCollect0 = CollectionUtilities.flatten(bagCollect0);
      List bagUnion = CollectionUtilities.union(setassociations, bagCollect0);
      setAsSet = CollectionUtilities.asSet(bagUnion);
      return setAsSet;
   }

   public Set selOpAsEnd(Association as, Classifier c) {
      List seqConnection = as.getCollectionConnectionList();
      List seqCollect = CollectionUtilities.newBag();
      Iterator iter = seqConnection.iterator();

      while(iter.hasNext()) {
         AssociationEnd decl = (AssociationEnd)iter.next();
         Classifier classifierParticipant = decl.getParticipant();
         seqCollect.add(classifierParticipant);
      }

      seqCollect = CollectionUtilities.flatten(seqCollect);
      Set uniquenessValidator = CollectionUtilities.newSet();
      boolean bIsUnique = true;

      Classifier decl0;
      for(Iterator iter0 = seqCollect.iterator(); bIsUnique && iter0.hasNext(); bIsUnique = uniquenessValidator.add(decl0)) {
         decl0 = (Classifier)iter0.next();
      }

      Object seqIf;
      OrderedSet seqConnection0;
      if (bIsUnique) {
         seqConnection0 = as.getCollectionConnectionList();
         List seqSelect = CollectionUtilities.newSequence();
         Iterator iter1 = seqConnection0.iterator();

         while(iter1.hasNext()) {
            AssociationEnd ae = (AssociationEnd)iter1.next();
            Set setAssociation = c.getCollectionAssociationList();
            boolean bExcludes = CollectionUtilities.excludes(setAssociation, ae);
            if (bExcludes) {
               CollectionUtilities.add(seqSelect, ae);
            }
         }

         seqIf = seqSelect;
      } else {
         seqConnection0 = as.getCollectionConnectionList();
         seqIf = seqConnection0;
      }

      Set setAsSet = CollectionUtilities.asSet((Collection)seqIf);
      return setAsSet;
   }

   public Set oppositeAssociationEnds() {
      Set setassociations = this.associations();
      Set set = CollectionUtilities.newSet();
      Set acc = set;

      Set setUnion;
      for(Iterator iter = setassociations.iterator(); iter.hasNext(); acc = setUnion) {
         Association ass = (Association)iter.next();
         Set setselOpAsEnd = this.selOpAsEnd(ass, this);
         setUnion = CollectionUtilities.union(acc, setselOpAsEnd);
      }

      return acc;
   }

   public Set specification() {
      Set setClientDependency = this.getCollectionClientDependencyList();
      Set setSelect = CollectionUtilities.newSet();
      Iterator iter = setClientDependency.iterator();

      while(iter.hasNext()) {
         Dependency d = (Dependency)iter.next();
         boolean bOclIsKindOf = Ocl.isKindOf(d, Ocl.getType(new java.lang.Class[]{Abstraction.class}));
         Set setStereotype = d.getCollectionStereotypeList();
         boolean bExists = false;

         boolean bExists0;
         for(Iterator iter0 = setStereotype.iterator(); !bExists && iter0.hasNext(); bExists = bExists0) {
            Stereotype e = (Stereotype)iter0.next();
            String strName = e.getName();
            bExists0 = strName.equals("realize");
         }

         boolean bAnd0 = bOclIsKindOf && bExists;
         Set setSupplier = d.getCollectionSupplierList();
         bExists0 = false;

         boolean bOclIsKindOf0;
         for(Iterator iter1 = setSupplier.iterator(); !bExists0 && iter1.hasNext(); bExists0 = bOclIsKindOf0) {
            ModelElement e = (ModelElement)iter1.next();
            bOclIsKindOf0 = Ocl.isKindOf(e, Ocl.getType(new java.lang.Class[]{Classifier.class}));
         }

         boolean bAnd = bAnd0 && bExists0;
         if (bAnd) {
            CollectionUtilities.add(setSelect, d);
         }
      }

      List bagCollect = CollectionUtilities.newBag();
      Iterator iter2 = setSelect.iterator();

      while(iter2.hasNext()) {
         Dependency decl = (Dependency)iter2.next();
         Set setSupplier0 = decl.getCollectionSupplierList();
         bagCollect.add(setSupplier0);
      }

      bagCollect = CollectionUtilities.flatten(bagCollect);
      List bagCollect0 = CollectionUtilities.newBag();
      Iterator iter3 = bagCollect.iterator();

      while(iter3.hasNext()) {
         ModelElement decl0 = (ModelElement)iter3.next();
         Classifier classifierOclAsType = (Classifier)decl0;
         bagCollect0.add(classifierOclAsType);
      }

      bagCollect0 = CollectionUtilities.flatten(bagCollect0);
      Set setAsSet = CollectionUtilities.asSet(bagCollect0);
      return setAsSet;
   }

   public Set allDiscriminators() {
      Set setGeneralization = this.getCollectionGeneralizationList();
      List bagCollect = CollectionUtilities.newBag();
      Iterator iter = setGeneralization.iterator();

      while(iter.hasNext()) {
         Generalization decl = (Generalization)iter.next();
         String strDiscriminator = decl.getDiscriminator();
         bagCollect.add(strDiscriminator);
      }

      bagCollect = CollectionUtilities.flatten(bagCollect);
      Set setparent = this.parent();
      List bagCollect0 = CollectionUtilities.newBag();
      Iterator iter0 = setparent.iterator();

      while(iter0.hasNext()) {
         GeneralizableElement decl0 = (GeneralizableElement)iter0.next();
         Classifier classifierOclAsType = (Classifier)decl0;
         bagCollect0.add(classifierOclAsType);
      }

      bagCollect0 = CollectionUtilities.flatten(bagCollect0);
      List bagCollect1 = CollectionUtilities.newBag();
      Iterator iter1 = bagCollect0.iterator();

      Set setAsSet;
      while(iter1.hasNext()) {
         Classifier decl1 = (Classifier)iter1.next();
         setAsSet = decl1.allDiscriminators();
         bagCollect1.add(setAsSet);
      }

      bagCollect1 = CollectionUtilities.flatten(bagCollect1);
      List bagUnion = CollectionUtilities.union(bagCollect, bagCollect1);
      setAsSet = CollectionUtilities.asSet(bagUnion);
      return setAsSet;
   }

   protected void internalRemove() {
      java.util.Enumeration tmpOwnedElementEnum = this.directGetOwnedElementList();
      ArrayList tmpOwnedElementList = new ArrayList();

      while(tmpOwnedElementEnum.hasMoreElements()) {
         tmpOwnedElementList.add(tmpOwnedElementEnum.nextElement());
      }

      Iterator it = tmpOwnedElementList.iterator();

      while(it.hasNext()) {
         ((ModelElement)it.next()).remove();
      }

      java.util.Enumeration tmpTypedFeatureEnum = this.getTypedFeatureList();
      ArrayList tmpTypedFeatureList = new ArrayList();

      while(tmpTypedFeatureEnum.hasMoreElements()) {
         tmpTypedFeatureList.add(tmpTypedFeatureEnum.nextElement());
      }

       it = tmpTypedFeatureList.iterator();

      while(it.hasNext()) {
         ((StructuralFeature)it.next()).setType((Classifier)null);
      }

      java.util.Enumeration tmpInstanceEnum = this.getInstanceList();
      ArrayList tmpInstanceList = new ArrayList();

      while(tmpInstanceEnum.hasMoreElements()) {
         tmpInstanceList.add(tmpInstanceEnum.nextElement());
      }

       it = tmpInstanceList.iterator();

      while(it.hasNext()) {
         Instance tmpInstance = (Instance)it.next();
         tmpInstance.removeClassifier(this);
         if (tmpInstance.getCollectionClassifierList().size() < 1) {
            tmpInstance.remove();
         }
      }

      java.util.Enumeration tmpPowertypeRangeEnum = this.getPowertypeRangeList();
      ArrayList tmpPowertypeRangeList = new ArrayList();

      while(tmpPowertypeRangeEnum.hasMoreElements()) {
         tmpPowertypeRangeList.add(tmpPowertypeRangeEnum.nextElement());
      }

       it = tmpPowertypeRangeList.iterator();

      while(it.hasNext()) {
         ((Generalization)it.next()).setPowertype((Classifier)null);
      }

      java.util.Enumeration tmpTypedParameterEnum = this.getTypedParameterList();
      ArrayList tmpTypedParameterList = new ArrayList();

      while(tmpTypedParameterEnum.hasMoreElements()) {
         tmpTypedParameterList.add(tmpTypedParameterEnum.nextElement());
      }

       it = tmpTypedParameterList.iterator();

      while(it.hasNext()) {
         ((Parameter)it.next()).setType((Classifier)null);
      }

      java.util.Enumeration tmpCollaborationEnum = this.getCollaborationList();
      ArrayList tmpCollaborationList = new ArrayList();

      while(tmpCollaborationEnum.hasMoreElements()) {
         tmpCollaborationList.add(tmpCollaborationEnum.nextElement());
      }

       it = tmpCollaborationList.iterator();

      while(it.hasNext()) {
         ((Collaboration)it.next()).setRepresentedClassifier((Classifier)null);
      }

      java.util.Enumeration tmpObjectFlowStateEnum = this.getObjectFlowStateList();
      ArrayList tmpObjectFlowStateList = new ArrayList();

      while(tmpObjectFlowStateEnum.hasMoreElements()) {
         tmpObjectFlowStateList.add(tmpObjectFlowStateEnum.nextElement());
      }

       it = tmpObjectFlowStateList.iterator();

      while(it.hasNext()) {
         ((ObjectFlowState)it.next()).setType((Classifier)null);
      }

      java.util.Enumeration tmpFeatureEnum = this.getFeatureList();
      ArrayList tmpFeatureList = new ArrayList();

      while(tmpFeatureEnum.hasMoreElements()) {
         tmpFeatureList.add(tmpFeatureEnum.nextElement());
      }

       it = tmpFeatureList.iterator();

      while(it.hasNext()) {
         ((Feature)it.next()).remove();
      }

      java.util.Enumeration tmpClassifierInStateEnum = this.getClassifierInStateList();
      ArrayList tmpClassifierInStateList = new ArrayList();

      while(tmpClassifierInStateEnum.hasMoreElements()) {
         tmpClassifierInStateList.add(tmpClassifierInStateEnum.nextElement());
      }

       it = tmpClassifierInStateList.iterator();

      while(it.hasNext()) {
         ((ClassifierInState)it.next()).setType((Classifier)null);
      }

      java.util.Enumeration tmpClassifierRoleEnum = this.getClassifierRoleList();
      ArrayList tmpClassifierRoleList = new ArrayList();

      while(tmpClassifierRoleEnum.hasMoreElements()) {
         tmpClassifierRoleList.add(tmpClassifierRoleEnum.nextElement());
      }

       it = tmpClassifierRoleList.iterator();

      while(it.hasNext()) {
         ClassifierRole tmpClassifierRole = (ClassifierRole)it.next();
         tmpClassifierRole.removeBase(this);
         if (tmpClassifierRole.getCollectionBaseList().size() < 1) {
            tmpClassifierRole.remove();
         }
      }

      java.util.Enumeration tmpAssociationEnum = this.getAssociationList();
      ArrayList tmpAssociationList = new ArrayList();

      while(tmpAssociationEnum.hasMoreElements()) {
         tmpAssociationList.add(tmpAssociationEnum.nextElement());
      }

       it = tmpAssociationList.iterator();

      while(it.hasNext()) {
         ((AssociationEnd)it.next()).setParticipant((Classifier)null);
      }

      java.util.Enumeration tmpSpecifiedEndEnum = this.getSpecifiedEndList();
      ArrayList tmpSpecifiedEndList = new ArrayList();

      while(tmpSpecifiedEndEnum.hasMoreElements()) {
         tmpSpecifiedEndList.add(tmpSpecifiedEndEnum.nextElement());
      }

       it = tmpSpecifiedEndList.iterator();

      while(it.hasNext()) {
         AssociationEnd tmpSpecifiedEnd = (AssociationEnd)it.next();
         tmpSpecifiedEnd.removeSpecification(this);
      }

      super.internalRemove();
   }
}
