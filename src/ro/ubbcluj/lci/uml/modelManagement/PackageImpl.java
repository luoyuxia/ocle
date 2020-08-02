package ro.ubbcluj.lci.uml.modelManagement;

import java.lang.Class;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import ro.ubbcluj.lci.codegen.framework.dt.Integer;
import ro.ubbcluj.lci.codegen.framework.ocl.CollectionUtilities;
import ro.ubbcluj.lci.codegen.framework.ocl.Ocl;
import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.foundation.core.*;
import ro.ubbcluj.lci.uml.foundation.extensionMechanisms.Stereotype;

public class PackageImpl extends GeneralizableElementImpl implements Package {
   protected Set theImportedElementList;
   protected Set theOwnedElementList;

   public PackageImpl() {
   }

   public Set getCollectionImportedElementList() {
      return this.theImportedElementList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theImportedElementList);
   }

   public Enumeration getImportedElementList() {
      return Collections.enumeration(this.getCollectionImportedElementList());
   }

   public Set directGetCollectionImportedElementList() {
      LinkedHashSet temp = new LinkedHashSet();
      if (this.theImportedElementList != null) {
         Iterator it = this.theImportedElementList.iterator();

         while(it.hasNext()) {
            temp.add(((ElementImport)it.next()).getImportedElement());
         }
      }

      return temp;
   }

   public Enumeration directGetImportedElementList() {
      return Collections.enumeration(this.directGetCollectionImportedElementList());
   }

   public void addImportedElement(ElementImport arg) {
      if (arg != null) {
         if (this.theImportedElementList == null) {
            this.theImportedElementList = new LinkedHashSet();
         }

         if (this.theImportedElementList.add(arg)) {
            arg.setPackage(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "importedElement", 1));
            }
         }
      }

   }

   public void removeImportedElement(ElementImport arg) {
      if (this.theImportedElementList != null && arg != null && this.theImportedElementList.remove(arg)) {
         arg.setPackage((Package)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "importedElement", 2));
         }
      }

   }

   public Set getCollectionOwnedElementList() {
      return this.theOwnedElementList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theOwnedElementList);
   }

   public Enumeration getOwnedElementList() {
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

   public Enumeration directGetOwnedElementList() {
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

   public Set contents() {
      Set setOwnedElement = this.directGetCollectionOwnedElementList();
      Set setImportedElement = this.directGetCollectionImportedElementList();
      Set setUnion = CollectionUtilities.union(setOwnedElement, setImportedElement);
      return setUnion;
   }

   public Set allContents() {
      Set setcontents = this.contents();
      Set setparent = this.parent();
      List bagCollect = CollectionUtilities.newBag();
      Iterator iter = setparent.iterator();

      while(iter.hasNext()) {
         GeneralizableElement decl = (GeneralizableElement)iter.next();
         Package packageOclAsType = (Package)decl;
         bagCollect.add(packageOclAsType);
      }

      bagCollect = CollectionUtilities.flatten(bagCollect);
      List bagCollect0 = CollectionUtilities.newBag();
      Iterator iter0 = bagCollect.iterator();

      while(iter0.hasNext()) {
         Package decl0 = (Package)iter0.next();
         Set setallContents = decl0.allContents();
         bagCollect0.add(setallContents);
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
      return setUnion;
   }

   public Set clDepSuplElem(Dependency d) {
      Set setSupplier = d.getCollectionSupplierList();
      List seqAsSequence = CollectionUtilities.asSequence(setSupplier);
      ModelElement modelElementFirst = (ModelElement)CollectionUtilities.first(seqAsSequence);
      Package packageOclAsType = (Package)modelElementFirst;
      Set setOwnedElement = packageOclAsType.directGetCollectionOwnedElementList();
      return setOwnedElement;
   }

   public Set allVisibleElements() {
      Set setallContents = this.allContents();
      Set setClientDependency = this.getCollectionClientDependencyList();
      Set setSelect = CollectionUtilities.newSet();
      Iterator iter = setClientDependency.iterator();

      while(iter.hasNext()) {
         Dependency iter1 = (Dependency)iter.next();
         boolean bOclIsKindOf = Ocl.isKindOf(iter1, Ocl.getType(new Class[]{Permission.class}));
         if (bOclIsKindOf) {
            CollectionUtilities.add(setSelect, iter1);
         }
      }

      Set set = CollectionUtilities.newSet();
      Set acc = set;

      Set setIf;
      for(Iterator iter0 = setSelect.iterator(); iter0.hasNext(); acc = setIf) {
         Dependency cD = (Dependency)iter0.next();
         Set setclDepStName = this.clDepStName(cD);
         boolean bIncludes = CollectionUtilities.includes(setclDepStName, "import");
         Set setclDepStName0 = this.clDepStName(cD);
         boolean bIncludes0 = CollectionUtilities.includes(setclDepStName0, "access");
         boolean bOr = bIncludes || bIncludes0;
         Set setIf0;
         Set setSelect0;
         Set setclDepSuplElem0;
         boolean bOclIsKindOf1;
         if (bOr) {
            setIf0 = this.clDepSuplElem(cD);
            setSelect0 = CollectionUtilities.newSet();
            Iterator iter1 = setIf0.iterator();

            while(iter1.hasNext()) {
               ModelElement e = (ModelElement)iter1.next();
               boolean bOclIsKindOf0 = Ocl.isKindOf(e, Ocl.getType(new Class[]{Classifier.class}));
               ElementOwnership elementOwnershipElementOwnership = e.getNamespace();
               int nVisibility = elementOwnershipElementOwnership.getVisibility();
               bOclIsKindOf1 = nVisibility == 3;
               boolean bAnd = bOclIsKindOf0 && bOclIsKindOf1;
               if (bAnd) {
                  CollectionUtilities.add(setSelect0, e);
               }
            }

            setclDepSuplElem0 = CollectionUtilities.union(acc, setSelect0);
            setIf = setclDepSuplElem0;
         } else {
            setSelect0 = this.clDepStName(cD);
            boolean bIncludes1 = CollectionUtilities.includes(setSelect0, "friend");
            Set setSelect1;
            if (bIncludes1) {
               setclDepSuplElem0 = this.clDepSuplElem(cD);
               setSelect1 = CollectionUtilities.newSet();
               Iterator iter2 = setclDepSuplElem0.iterator();

               while(iter2.hasNext()) {
                  ModelElement decl = (ModelElement)iter2.next();
                  bOclIsKindOf1 = Ocl.isKindOf(decl, Ocl.getType(new Class[]{Classifier.class}));
                  if (bOclIsKindOf1) {
                     CollectionUtilities.add(setSelect1, decl);
                  }
               }

               Set setUnion1 = CollectionUtilities.union(acc, setSelect1);
               setIf0 = setUnion1;
            } else {
               setclDepSuplElem0 = CollectionUtilities.newSet();
               setSelect1 = CollectionUtilities.union(acc, setclDepSuplElem0);
               setIf0 = setSelect1;
            }

            setIf = setIf0;
         }
      }

      setIf = CollectionUtilities.union(setallContents, acc);
      return setIf;
   }

   public Set allImportedElements() {
      Set setImportedElement = this.directGetCollectionImportedElementList();
      Set setparent = this.parent();
      List bagCollect = CollectionUtilities.newBag();
      Iterator iter = setparent.iterator();

      while(iter.hasNext()) {
         GeneralizableElement decl = (GeneralizableElement)iter.next();
         Package packageOclAsType = (Package)decl;
         bagCollect.add(packageOclAsType);
      }

      bagCollect = CollectionUtilities.flatten(bagCollect);
      List bagCollect0 = CollectionUtilities.newBag();
      Iterator iter0 = bagCollect.iterator();

      while(iter0.hasNext()) {
         Package decl0 = (Package)iter0.next();
         Set setallImportedElements = decl0.allImportedElements();
         bagCollect0.add(setallImportedElements);
      }

      bagCollect0 = CollectionUtilities.flatten(bagCollect0);
      List bagSelect = CollectionUtilities.newBag();
      Iterator iter1 = bagCollect0.iterator();

      Set setAsSet;
      while(iter1.hasNext()) {
         ModelElement e = (ModelElement)iter1.next();
         setAsSet = e.getCollectionPackageList();
         List bagCollect1 = CollectionUtilities.newBag();
         Iterator iter2 = setAsSet.iterator();

         while(iter2.hasNext()) {
            ElementImport decl1 = (ElementImport)iter2.next();
            int nVisibility = decl1.getVisibility();
            bagCollect1.add(Integer.toInteger(nVisibility));
         }

         bagCollect1 = CollectionUtilities.flatten(bagCollect1);
         boolean bEquals = false;
         Set setElementImport0 = e.getCollectionPackageList();
         List bagCollect2 = CollectionUtilities.newBag();
         Iterator iter3 = setElementImport0.iterator();

         while(iter3.hasNext()) {
            ElementImport decl2 = (ElementImport)iter3.next();
            int nVisibility0 = decl2.getVisibility();
            bagCollect2.add(Integer.toInteger(nVisibility0));
         }

         bagCollect2 = CollectionUtilities.flatten(bagCollect2);
         boolean bEquals0 = false;
         boolean bOr = bEquals || bEquals0;
         if (bOr) {
            CollectionUtilities.add(bagSelect, e);
         }
      }

      List bagUnion = CollectionUtilities.union(setImportedElement, bagSelect);
      setAsSet = CollectionUtilities.asSet(bagUnion);
      return setAsSet;
   }

   public Set contentsAndImports() {
      Set setOwnedElement = this.directGetCollectionOwnedElementList();
      Set setallSuppliersP = this.allSuppliersP();
      Set setSelect = CollectionUtilities.newSet();
      Iterator iter = setallSuppliersP.iterator();

      while(iter.hasNext()) {
         ModelElement iter1 = (ModelElement)iter.next();
         boolean bOclIsKindOf = Ocl.isKindOf(iter1, Ocl.getType(new Class[]{Namespace.class}));
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
         boolean bOclIsKindOf = Ocl.isKindOf(iter1, Ocl.getType(new Class[]{Namespace.class}));
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
         boolean bOclIsKindOf = Ocl.isKindOf(iter1, Ocl.getType(new Class[]{Classifier.class}));
         if (bOclIsKindOf) {
            CollectionUtilities.add(setSelect, iter1);
         }
      }

      return setSelect;
   }

   public Set allContentsD() {
      boolean bOclIsKindOf = Ocl.isKindOf(this, Ocl.getType(new Class[]{GeneralizableElement.class}));
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
         boolean bOclIsKindOf = Ocl.isKindOf(iter1, Ocl.getType(new Class[]{Dependency.class}));
         boolean bOclIsKindOf0 = Ocl.isKindOf(iter1, Ocl.getType(new Class[]{Permission.class}));
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
               bOclIsKindOf2 = Ocl.isKindOf(decl, Ocl.getType(new Class[]{Classifier.class}));
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
               bOclIsKindOf2 = Ocl.isKindOf(decl, Ocl.getType(new Class[]{Classifier.class}));
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
      Enumeration tmpImportedElementEnum = this.getImportedElementList();
      ArrayList tmpImportedElementList = new ArrayList();

      while(tmpImportedElementEnum.hasMoreElements()) {
         tmpImportedElementList.add(tmpImportedElementEnum.nextElement());
      }

      Iterator it = tmpImportedElementList.iterator();

      while(it.hasNext()) {
         ElementImport tmpImportedElement = (ElementImport)it.next();
         tmpImportedElement.getImportedElement().removePackage(tmpImportedElement);
      }

      Enumeration tmpOwnedElementEnum = this.directGetOwnedElementList();
      ArrayList tmpOwnedElementList = new ArrayList();

      while(tmpOwnedElementEnum.hasMoreElements()) {
         tmpOwnedElementList.add(tmpOwnedElementEnum.nextElement());
      }

       it = tmpOwnedElementList.iterator();

      while(it.hasNext()) {
         ((ModelElement)it.next()).remove();
      }

      super.internalRemove();
   }
}
