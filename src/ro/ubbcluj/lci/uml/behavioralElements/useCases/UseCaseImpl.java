package ro.ubbcluj.lci.uml.behavioralElements.useCases;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import ro.ubbcluj.lci.codegen.framework.ocl.CollectionUtilities;
import ro.ubbcluj.lci.codegen.framework.ocl.Ocl;
import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.foundation.core.ClassifierImpl;
import ro.ubbcluj.lci.uml.foundation.core.GeneralizableElement;
import ro.ubbcluj.lci.uml.foundation.core.Namespace;
import ro.ubbcluj.lci.uml.modelManagement.Subsystem;

public class UseCaseImpl extends ClassifierImpl implements UseCase {
   protected Set theExtensionPointList;
   protected Set theExtendList;
   protected Set theExtenderList;
   protected Set theIncluderList;
   protected Set theIncludeList;

   public UseCaseImpl() {
   }

   public Set getCollectionExtensionPointList() {
      return this.theExtensionPointList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theExtensionPointList);
   }

   public Enumeration getExtensionPointList() {
      return Collections.enumeration(this.getCollectionExtensionPointList());
   }

   public void addExtensionPoint(ExtensionPoint arg) {
      if (arg != null) {
         if (this.theExtensionPointList == null) {
            this.theExtensionPointList = new LinkedHashSet();
         }

         if (this.theExtensionPointList.add(arg)) {
            arg.setUseCase(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "extensionPoint", 1));
            }
         }
      }

   }

   public void removeExtensionPoint(ExtensionPoint arg) {
      if (this.theExtensionPointList != null && arg != null && this.theExtensionPointList.remove(arg)) {
         arg.setUseCase((UseCase)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "extensionPoint", 2));
         }
      }

   }

   public Set getCollectionExtendList() {
      return this.theExtendList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theExtendList);
   }

   public Enumeration getExtendList() {
      return Collections.enumeration(this.getCollectionExtendList());
   }

   public void addExtend(Extend arg) {
      if (arg != null) {
         if (this.theExtendList == null) {
            this.theExtendList = new LinkedHashSet();
         }

         if (this.theExtendList.add(arg)) {
            arg.setBase(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "extend", 1));
            }
         }
      }

   }

   public void removeExtend(Extend arg) {
      if (this.theExtendList != null && arg != null && this.theExtendList.remove(arg)) {
         arg.setBase((UseCase)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "extend", 2));
         }
      }

   }

   public Set getCollectionExtenderList() {
      return this.theExtenderList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theExtenderList);
   }

   public Enumeration getExtenderList() {
      return Collections.enumeration(this.getCollectionExtenderList());
   }

   public void addExtender(Extend arg) {
      if (arg != null) {
         if (this.theExtenderList == null) {
            this.theExtenderList = new LinkedHashSet();
         }

         if (this.theExtenderList.add(arg)) {
            arg.setExtension(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "extender", 1));
            }
         }
      }

   }

   public void removeExtender(Extend arg) {
      if (this.theExtenderList != null && arg != null && this.theExtenderList.remove(arg)) {
         arg.setExtension((UseCase)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "extender", 2));
         }
      }

   }

   public Set getCollectionIncluderList() {
      return this.theIncluderList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theIncluderList);
   }

   public Enumeration getIncluderList() {
      return Collections.enumeration(this.getCollectionIncluderList());
   }

   public void addIncluder(Include arg) {
      if (arg != null) {
         if (this.theIncluderList == null) {
            this.theIncluderList = new LinkedHashSet();
         }

         if (this.theIncluderList.add(arg)) {
            arg.setAddition(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "includer", 1));
            }
         }
      }

   }

   public void removeIncluder(Include arg) {
      if (this.theIncluderList != null && arg != null && this.theIncluderList.remove(arg)) {
         arg.setAddition((UseCase)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "includer", 2));
         }
      }

   }

   public Set getCollectionIncludeList() {
      return this.theIncludeList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theIncludeList);
   }

   public Enumeration getIncludeList() {
      return Collections.enumeration(this.getCollectionIncludeList());
   }

   public void addInclude(Include arg) {
      if (arg != null) {
         if (this.theIncludeList == null) {
            this.theIncludeList = new LinkedHashSet();
         }

         if (this.theIncludeList.add(arg)) {
            arg.setBase(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "include", 1));
            }
         }
      }

   }

   public void removeInclude(Include arg) {
      if (this.theIncludeList != null && arg != null && this.theIncludeList.remove(arg)) {
         arg.setBase((UseCase)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "include", 2));
         }
      }

   }

   public Set specificationPath() {
      Set setallSurroundingNamespaces = this.allSurroundingNamespaces();
      Set setSelect = CollectionUtilities.newSet();
      Iterator iter = setallSurroundingNamespaces.iterator();

      while(iter.hasNext()) {
         Namespace n = (Namespace)iter.next();
         boolean bOclIsKindOf = Ocl.isKindOf(n, Ocl.getType(new Class[]{Subsystem.class}));
         boolean bOclIsKindOf0 = Ocl.isKindOf(n, Ocl.getType(new Class[]{Class.class}));
         boolean bOr = bOclIsKindOf || bOclIsKindOf0;
         if (bOr) {
            CollectionUtilities.add(setSelect, n);
         }
      }

      return setSelect;
   }

   public Set allExtensionPoints() {
      Set setallParents = this.allParents();
      List bagCollect = CollectionUtilities.newBag();
      Iterator iter = setallParents.iterator();

      while(iter.hasNext()) {
         GeneralizableElement decl = (GeneralizableElement)iter.next();
         UseCase useCaseOclAsType = (UseCase)decl;
         bagCollect.add(useCaseOclAsType);
      }

      bagCollect = CollectionUtilities.flatten(bagCollect);
      List bagCollect0 = CollectionUtilities.newBag();
      Iterator iter0 = bagCollect.iterator();

      while(iter0.hasNext()) {
         UseCase decl0 = (UseCase)iter0.next();
         Set setExtensionPoint = decl0.getCollectionExtensionPointList();
         bagCollect0.add(setExtensionPoint);
      }

      bagCollect0 = CollectionUtilities.flatten(bagCollect0);
      Set setExtensionPoint0 = this.getCollectionExtensionPointList();
      List bagUnion = CollectionUtilities.union(bagCollect0, setExtensionPoint0);
      Set setAsSet = CollectionUtilities.asSet(bagUnion);
      return setAsSet;
   }

   protected void internalRemove() {
      Enumeration tmpExtensionPointEnum = this.getExtensionPointList();
      ArrayList tmpExtensionPointList = new ArrayList();

      while(tmpExtensionPointEnum.hasMoreElements()) {
         tmpExtensionPointList.add(tmpExtensionPointEnum.nextElement());
      }

      Iterator it = tmpExtensionPointList.iterator();

      while(it.hasNext()) {
         ((ExtensionPoint)it.next()).setUseCase((UseCase)null);
      }

      Enumeration tmpExtendEnum = this.getExtendList();
      ArrayList tmpExtendList = new ArrayList();

      while(tmpExtendEnum.hasMoreElements()) {
         tmpExtendList.add(tmpExtendEnum.nextElement());
      }

       it = tmpExtendList.iterator();

      while(it.hasNext()) {
         ((Extend)it.next()).setBase((UseCase)null);
      }

      Enumeration tmpExtenderEnum = this.getExtenderList();
      ArrayList tmpExtenderList = new ArrayList();

      while(tmpExtenderEnum.hasMoreElements()) {
         tmpExtenderList.add(tmpExtenderEnum.nextElement());
      }

       it = tmpExtenderList.iterator();

      while(it.hasNext()) {
         ((Extend)it.next()).setExtension((UseCase)null);
      }

      Enumeration tmpIncluderEnum = this.getIncluderList();
      ArrayList tmpIncluderList = new ArrayList();

      while(tmpIncluderEnum.hasMoreElements()) {
         tmpIncluderList.add(tmpIncluderEnum.nextElement());
      }

       it = tmpIncluderList.iterator();

      while(it.hasNext()) {
         ((Include)it.next()).setAddition((UseCase)null);
      }

      Enumeration tmpIncludeEnum = this.getIncludeList();
      ArrayList tmpIncludeList = new ArrayList();

      while(tmpIncludeEnum.hasMoreElements()) {
         tmpIncludeList.add(tmpIncludeEnum.nextElement());
      }

       it = tmpIncludeList.iterator();

      while(it.hasNext()) {
         ((Include)it.next()).setBase((UseCase)null);
      }

      super.internalRemove();
   }
}
