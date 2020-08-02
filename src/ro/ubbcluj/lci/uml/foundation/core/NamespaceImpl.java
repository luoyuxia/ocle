package ro.ubbcluj.lci.uml.foundation.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import ro.ubbcluj.lci.codegen.framework.ocl.CollectionUtilities;
import ro.ubbcluj.lci.codegen.framework.ocl.Ocl;
import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.foundation.extensionMechanisms.Stereotype;

public class NamespaceImpl extends ModelElementImpl implements Namespace {
   protected Set theOwnedElementList;

   public NamespaceImpl() {
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
         GeneralizableElement generalizableElementOclAsType = (GeneralizableElement)this;
         Set setallParents = generalizableElementOclAsType.allParents();
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
         GeneralizableElement generalizableElementOclAsType = (GeneralizableElement)this;
         Set setallParents = generalizableElementOclAsType.allParents();
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

      super.internalRemove();
   }
}
