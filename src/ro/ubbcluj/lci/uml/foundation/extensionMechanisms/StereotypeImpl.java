package ro.ubbcluj.lci.uml.foundation.extensionMechanisms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.StringTokenizer;
import ro.ubbcluj.lci.codegen.framework.ocl.CollectionUtilities;
import ro.ubbcluj.lci.codegen.framework.ocl.Ocl;
import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.foundation.core.Constraint;
import ro.ubbcluj.lci.uml.foundation.core.GeneralizableElementImpl;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.uml.foundation.core.Namespace;
import ro.ubbcluj.lci.uml.foundation.dataTypes.Geometry;
import ro.ubbcluj.lci.uml.modelManagement.Package;

public class StereotypeImpl extends GeneralizableElementImpl implements Stereotype {
   protected String theBaseClass;
   protected Geometry theIcon;
   protected Set theExtendedElementList;
   protected Set theStereotypeConstraintList;
   protected Set theDefinedTagList;

   public StereotypeImpl() {
   }

   public Set getCollectionBaseClassList() {
      Set result = Collections.EMPTY_SET;
      if (this.theBaseClass != null) {
         StringTokenizer tok = new StringTokenizer(this.theBaseClass);
         result = new LinkedHashSet();

         while(tok.hasMoreTokens()) {
            ((Set)result).add(tok.nextToken());
         }
      }

      return (Set)result;
   }

   public String getBaseClass() {
      return this.theBaseClass;
   }

   public void setBaseClass(String baseClass) {
      this.theBaseClass = baseClass;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "baseClass", 0));
      }

   }

   public Geometry getIcon() {
      return this.theIcon;
   }

   public void setIcon(Geometry icon) {
      this.theIcon = icon;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "icon", 0));
      }

   }

   public Set getCollectionExtendedElementList() {
      return this.theExtendedElementList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theExtendedElementList);
   }

   public Enumeration getExtendedElementList() {
      return Collections.enumeration(this.getCollectionExtendedElementList());
   }

   public void addExtendedElement(ModelElement arg) {
      if (arg != null) {
         if (this.theExtendedElementList == null) {
            this.theExtendedElementList = new LinkedHashSet();
         }

         if (this.theExtendedElementList.add(arg)) {
            arg.addStereotype(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "extendedElement", 1));
            }
         }
      }

   }

   public void removeExtendedElement(ModelElement arg) {
      if (this.theExtendedElementList != null && arg != null && this.theExtendedElementList.remove(arg)) {
         arg.removeStereotype(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "extendedElement", 2));
         }
      }

   }

   public Set getCollectionStereotypeConstraintList() {
      return this.theStereotypeConstraintList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theStereotypeConstraintList);
   }

   public Enumeration getStereotypeConstraintList() {
      return Collections.enumeration(this.getCollectionStereotypeConstraintList());
   }

   public void addStereotypeConstraint(Constraint arg) {
      if (arg != null) {
         if (this.theStereotypeConstraintList == null) {
            this.theStereotypeConstraintList = new LinkedHashSet();
         }

         if (this.theStereotypeConstraintList.add(arg)) {
            arg.setConstrainedStereotype(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "stereotypeConstraint", 1));
            }
         }
      }

   }

   public void removeStereotypeConstraint(Constraint arg) {
      if (this.theStereotypeConstraintList != null && arg != null && this.theStereotypeConstraintList.remove(arg)) {
         arg.setConstrainedStereotype((Stereotype)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "stereotypeConstraint", 2));
         }
      }

   }

   public Set getCollectionDefinedTagList() {
      return this.theDefinedTagList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theDefinedTagList);
   }

   public Enumeration getDefinedTagList() {
      return Collections.enumeration(this.getCollectionDefinedTagList());
   }

   public void addDefinedTag(TagDefinition arg) {
      if (arg != null) {
         if (this.theDefinedTagList == null) {
            this.theDefinedTagList = new LinkedHashSet();
         }

         if (this.theDefinedTagList.add(arg)) {
            arg.setOwner(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "definedTag", 1));
            }
         }
      }

   }

   public void removeDefinedTag(TagDefinition arg) {
      if (this.theDefinedTagList != null && arg != null && this.theDefinedTagList.remove(arg)) {
         arg.setOwner((Stereotype)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "definedTag", 2));
         }
      }

   }

   public Set findProfile(ModelElement me) {
      Namespace namespaceNamespace = me.directGetNamespace();
      boolean bNotEmpty = CollectionUtilities.notEmpty((Object)namespaceNamespace);
      Set setIf;
      Set setIf0;
      if (bNotEmpty) {
         Namespace namespaceNamespace0 = me.directGetNamespace();
         boolean bOclIsKindOf = Ocl.isKindOf(namespaceNamespace0, Ocl.getType(new Class[]{Package.class}));
         Namespace namespaceNamespace1 = me.directGetNamespace();
         Set setStereotype = namespaceNamespace1.getCollectionStereotypeList();
         boolean bNotEmpty0 = CollectionUtilities.notEmpty((Collection)setStereotype);
         boolean bAnd0 = bOclIsKindOf && bNotEmpty0;
         Namespace namespaceNamespace2 = me.directGetNamespace();
         Set setStereotype0 = namespaceNamespace2.getCollectionStereotypeList();
         boolean bExists = false;

         boolean bEquals;
         for(Iterator iter = setStereotype0.iterator(); !bExists && iter.hasNext(); bExists = bEquals) {
            Stereotype s = (Stereotype)iter.next();
            String strName = s.getName();
            bEquals = strName.equals("profile");
         }

         boolean bAnd = bAnd0 && bExists;
         if (bAnd) {
            Set set = CollectionUtilities.newSet();
            Namespace namespaceNamespace3 = me.directGetNamespace();
            Package packageOclAsType = (Package)namespaceNamespace3;
            CollectionUtilities.add(set, packageOclAsType);
            setIf0 = set;
         } else {
            Namespace namespaceNamespace4 = me.directGetNamespace();
            Set setfindProfile = this.findProfile(namespaceNamespace4);
            setIf0 = setfindProfile;
         }

         setIf = setIf0;
      } else {
         setIf0 = CollectionUtilities.newSet();
         setIf = setIf0;
      }

      return setIf;
   }

   protected void internalRemove() {
      Enumeration tmpExtendedElementEnum = this.getExtendedElementList();
      ArrayList tmpExtendedElementList = new ArrayList();

      while(tmpExtendedElementEnum.hasMoreElements()) {
         tmpExtendedElementList.add(tmpExtendedElementEnum.nextElement());
      }

      Iterator it = tmpExtendedElementList.iterator();

      while(it.hasNext()) {
         ModelElement tmpExtendedElement = (ModelElement)it.next();
         tmpExtendedElement.removeStereotype(this);
      }

      Enumeration tmpStereotypeConstraintEnum = this.getStereotypeConstraintList();
      ArrayList tmpStereotypeConstraintList = new ArrayList();

      while(tmpStereotypeConstraintEnum.hasMoreElements()) {
         tmpStereotypeConstraintList.add(tmpStereotypeConstraintEnum.nextElement());
      }

       it = tmpStereotypeConstraintList.iterator();

      while(it.hasNext()) {
         ((Constraint)it.next()).remove();
      }

      Enumeration tmpDefinedTagEnum = this.getDefinedTagList();
      ArrayList tmpDefinedTagList = new ArrayList();

      while(tmpDefinedTagEnum.hasMoreElements()) {
         tmpDefinedTagList.add(tmpDefinedTagEnum.nextElement());
      }

       it = tmpDefinedTagList.iterator();

      while(it.hasNext()) {
         ((TagDefinition)it.next()).remove();
      }

      super.internalRemove();
   }
}
