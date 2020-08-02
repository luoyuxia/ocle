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
import ro.ubbcluj.lci.uml.behavioralElements.activityGraphs.Partition;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.ClassifierRole;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.Collaboration;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.CollaborationInstanceSet;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.Interaction;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.Message;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.StateMachine;
import ro.ubbcluj.lci.uml.foundation.extensionMechanisms.Stereotype;
import ro.ubbcluj.lci.uml.foundation.extensionMechanisms.TaggedValue;
import ro.ubbcluj.lci.uml.modelManagement.ElementImport;
import ro.ubbcluj.lci.uml.modelManagement.Model;

public class ModelElementImpl extends ElementImpl implements ModelElement {
   protected String theName;
   protected Set theCommentList;
   protected Set theDefaultedParameterList;
   protected Set theTargetFlowList;
   protected Set thePackageList;
   protected Set theStereotypeList;
   protected Set theContainerList;
   protected Set thePresentationList;
   protected Set theCollaboration2List;
   protected Set theTaggedValueList;
   protected Set thePartitionList;
   protected Set theReferenceTagList;
   protected Set theClassifierRoleList;
   protected Set theSupplierDependencyList;
   protected Set theSourceFlowList;
   protected OrderedSet theParameterTemplateList;
   protected Set theTemplateArgumentList;
   protected Set theCollaborationInstanceSetList;
   protected Set theBehaviorList;
   protected Set theConstraintList;
   protected TemplateParameter theTemplate;
   protected ElementOwnership theNamespace;
   protected Set theClientDependencyList;

   public ModelElementImpl() {
   }

   public String getName() {
      return this.theName;
   }

   public void setName(String name) {
      this.theName = name;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "name", 0));
      }

   }

   public Set getCollectionCommentList() {
      return this.theCommentList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theCommentList);
   }

   public java.util.Enumeration getCommentList() {
      return Collections.enumeration(this.getCollectionCommentList());
   }

   public void addComment(Comment arg) {
      if (arg != null) {
         if (this.theCommentList == null) {
            this.theCommentList = new LinkedHashSet();
         }

         if (this.theCommentList.add(arg)) {
            arg.addAnnotatedElement(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "comment", 1));
            }
         }
      }

   }

   public void removeComment(Comment arg) {
      if (this.theCommentList != null && arg != null && this.theCommentList.remove(arg)) {
         arg.removeAnnotatedElement(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "comment", 2));
         }
      }

   }

   public Set getCollectionDefaultedParameterList() {
      return this.theDefaultedParameterList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theDefaultedParameterList);
   }

   public java.util.Enumeration getDefaultedParameterList() {
      return Collections.enumeration(this.getCollectionDefaultedParameterList());
   }

   public void addDefaultedParameter(TemplateParameter arg) {
      if (arg != null) {
         if (this.theDefaultedParameterList == null) {
            this.theDefaultedParameterList = new LinkedHashSet();
         }

         if (this.theDefaultedParameterList.add(arg)) {
            arg.setDefaultElement(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "defaultedParameter", 1));
            }
         }
      }

   }

   public void removeDefaultedParameter(TemplateParameter arg) {
      if (this.theDefaultedParameterList != null && arg != null && this.theDefaultedParameterList.remove(arg)) {
         arg.setDefaultElement((ModelElement)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "defaultedParameter", 2));
         }
      }

   }

   public Set getCollectionTargetFlowList() {
      return this.theTargetFlowList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theTargetFlowList);
   }

   public java.util.Enumeration getTargetFlowList() {
      return Collections.enumeration(this.getCollectionTargetFlowList());
   }

   public void addTargetFlow(Flow arg) {
      if (arg != null) {
         if (this.theTargetFlowList == null) {
            this.theTargetFlowList = new LinkedHashSet();
         }

         if (this.theTargetFlowList.add(arg)) {
            arg.addTarget(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "targetFlow", 1));
            }
         }
      }

   }

   public void removeTargetFlow(Flow arg) {
      if (this.theTargetFlowList != null && arg != null && this.theTargetFlowList.remove(arg)) {
         arg.removeTarget(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "targetFlow", 2));
         }
      }

   }

   public Set getCollectionPackageList() {
      return this.thePackageList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.thePackageList);
   }

   public java.util.Enumeration getPackageList() {
      return Collections.enumeration(this.getCollectionPackageList());
   }

   public Set directGetCollectionPackageList() {
      LinkedHashSet temp = new LinkedHashSet();
      if (this.thePackageList != null) {
         Iterator it = this.thePackageList.iterator();

         while(it.hasNext()) {
            temp.add(((ElementImport)it.next()).getPackage());
         }
      }

      return temp;
   }

   public java.util.Enumeration directGetPackageList() {
      return Collections.enumeration(this.directGetCollectionPackageList());
   }

   public void addPackage(ElementImport arg) {
      if (arg != null) {
         if (this.thePackageList == null) {
            this.thePackageList = new LinkedHashSet();
         }

         if (this.thePackageList.add(arg)) {
            arg.setImportedElement(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "package", 1));
            }
         }
      }

   }

   public void removePackage(ElementImport arg) {
      if (this.thePackageList != null && arg != null && this.thePackageList.remove(arg)) {
         arg.setImportedElement((ModelElement)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "package", 2));
         }
      }

   }

   public Set getCollectionStereotypeList() {
      return this.theStereotypeList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theStereotypeList);
   }

   public java.util.Enumeration getStereotypeList() {
      return Collections.enumeration(this.getCollectionStereotypeList());
   }

   public void addStereotype(Stereotype arg) {
      if (arg != null) {
         if (this.theStereotypeList == null) {
            this.theStereotypeList = new LinkedHashSet();
         }

         if (this.theStereotypeList.add(arg)) {
            arg.addExtendedElement(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "stereotype", 1));
            }
         }
      }

   }

   public void removeStereotype(Stereotype arg) {
      if (this.theStereotypeList != null && arg != null && this.theStereotypeList.remove(arg)) {
         arg.removeExtendedElement(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "stereotype", 2));
         }
      }

   }

   public Set getCollectionContainerList() {
      return this.theContainerList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theContainerList);
   }

   public java.util.Enumeration getContainerList() {
      return Collections.enumeration(this.getCollectionContainerList());
   }

   public Set directGetCollectionContainerList() {
      LinkedHashSet temp = new LinkedHashSet();
      if (this.theContainerList != null) {
         Iterator it = this.theContainerList.iterator();

         while(it.hasNext()) {
            temp.add(((ElementResidence)it.next()).getContainer());
         }
      }

      return temp;
   }

   public java.util.Enumeration directGetContainerList() {
      return Collections.enumeration(this.directGetCollectionContainerList());
   }

   public void addContainer(ElementResidence arg) {
      if (arg != null) {
         if (this.theContainerList == null) {
            this.theContainerList = new LinkedHashSet();
         }

         if (this.theContainerList.add(arg)) {
            arg.setResident(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "container", 1));
            }
         }
      }

   }

   public void removeContainer(ElementResidence arg) {
      if (this.theContainerList != null && arg != null && this.theContainerList.remove(arg)) {
         arg.setResident((ModelElement)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "container", 2));
         }
      }

   }

   public Set getCollectionPresentationList() {
      return this.thePresentationList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.thePresentationList);
   }

   public java.util.Enumeration getPresentationList() {
      return Collections.enumeration(this.getCollectionPresentationList());
   }

   public void addPresentation(PresentationElement arg) {
      if (arg != null) {
         if (this.thePresentationList == null) {
            this.thePresentationList = new LinkedHashSet();
         }

         if (this.thePresentationList.add(arg)) {
            arg.addSubject(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "presentation", 1));
            }
         }
      }

   }

   public void removePresentation(PresentationElement arg) {
      if (this.thePresentationList != null && arg != null && this.thePresentationList.remove(arg)) {
         arg.removeSubject(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "presentation", 2));
         }
      }

   }

   public Set getCollectionCollaboration2List() {
      return this.theCollaboration2List == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theCollaboration2List);
   }

   public java.util.Enumeration getCollaboration2List() {
      return Collections.enumeration(this.getCollectionCollaboration2List());
   }

   public void addCollaboration2(Collaboration arg) {
      if (arg != null) {
         if (this.theCollaboration2List == null) {
            this.theCollaboration2List = new LinkedHashSet();
         }

         if (this.theCollaboration2List.add(arg)) {
            arg.addConstrainingElement(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "collaboration2", 1));
            }
         }
      }

   }

   public void removeCollaboration2(Collaboration arg) {
      if (this.theCollaboration2List != null && arg != null && this.theCollaboration2List.remove(arg)) {
         arg.removeConstrainingElement(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "collaboration2", 2));
         }
      }

   }

   public Set getCollectionTaggedValueList() {
      return this.theTaggedValueList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theTaggedValueList);
   }

   public java.util.Enumeration getTaggedValueList() {
      return Collections.enumeration(this.getCollectionTaggedValueList());
   }

   public void addTaggedValue(TaggedValue arg) {
      if (arg != null) {
         if (this.theTaggedValueList == null) {
            this.theTaggedValueList = new LinkedHashSet();
         }

         if (this.theTaggedValueList.add(arg)) {
            arg.setModelElement(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "taggedValue", 1));
            }
         }
      }

   }

   public void removeTaggedValue(TaggedValue arg) {
      if (this.theTaggedValueList != null && arg != null && this.theTaggedValueList.remove(arg)) {
         arg.setModelElement((ModelElement)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "taggedValue", 2));
         }
      }

   }

   public Set getCollectionPartitionList() {
      return this.thePartitionList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.thePartitionList);
   }

   public java.util.Enumeration getPartitionList() {
      return Collections.enumeration(this.getCollectionPartitionList());
   }

   public void addPartition(Partition arg) {
      if (arg != null) {
         if (this.thePartitionList == null) {
            this.thePartitionList = new LinkedHashSet();
         }

         if (this.thePartitionList.add(arg)) {
            arg.addContents(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "partition", 1));
            }
         }
      }

   }

   public void removePartition(Partition arg) {
      if (this.thePartitionList != null && arg != null && this.thePartitionList.remove(arg)) {
         arg.removeContents(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "partition", 2));
         }
      }

   }

   public Set getCollectionReferenceTagList() {
      return this.theReferenceTagList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theReferenceTagList);
   }

   public java.util.Enumeration getReferenceTagList() {
      return Collections.enumeration(this.getCollectionReferenceTagList());
   }

   public void addReferenceTag(TaggedValue arg) {
      if (arg != null) {
         if (this.theReferenceTagList == null) {
            this.theReferenceTagList = new LinkedHashSet();
         }

         if (this.theReferenceTagList.add(arg)) {
            arg.addReferenceValue(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "referenceTag", 1));
            }
         }
      }

   }

   public void removeReferenceTag(TaggedValue arg) {
      if (this.theReferenceTagList != null && arg != null && this.theReferenceTagList.remove(arg)) {
         arg.removeReferenceValue(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "referenceTag", 2));
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
            arg.addAvailableContents(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "classifierRole", 1));
            }
         }
      }

   }

   public void removeClassifierRole(ClassifierRole arg) {
      if (this.theClassifierRoleList != null && arg != null && this.theClassifierRoleList.remove(arg)) {
         arg.removeAvailableContents(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "classifierRole", 2));
         }
      }

   }

   public Set getCollectionSupplierDependencyList() {
      return this.theSupplierDependencyList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theSupplierDependencyList);
   }

   public java.util.Enumeration getSupplierDependencyList() {
      return Collections.enumeration(this.getCollectionSupplierDependencyList());
   }

   public void addSupplierDependency(Dependency arg) {
      if (arg != null) {
         if (this.theSupplierDependencyList == null) {
            this.theSupplierDependencyList = new LinkedHashSet();
         }

         if (this.theSupplierDependencyList.add(arg)) {
            arg.addSupplier(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "supplierDependency", 1));
            }
         }
      }

   }

   public void removeSupplierDependency(Dependency arg) {
      if (this.theSupplierDependencyList != null && arg != null && this.theSupplierDependencyList.remove(arg)) {
         arg.removeSupplier(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "supplierDependency", 2));
         }
      }

   }

   public Set getCollectionSourceFlowList() {
      return this.theSourceFlowList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theSourceFlowList);
   }

   public java.util.Enumeration getSourceFlowList() {
      return Collections.enumeration(this.getCollectionSourceFlowList());
   }

   public void addSourceFlow(Flow arg) {
      if (arg != null) {
         if (this.theSourceFlowList == null) {
            this.theSourceFlowList = new LinkedHashSet();
         }

         if (this.theSourceFlowList.add(arg)) {
            arg.addSource(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "sourceFlow", 1));
            }
         }
      }

   }

   public void removeSourceFlow(Flow arg) {
      if (this.theSourceFlowList != null && arg != null && this.theSourceFlowList.remove(arg)) {
         arg.removeSource(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "sourceFlow", 2));
         }
      }

   }

   public OrderedSet getCollectionParameterTemplateList() {
      return this.theParameterTemplateList == null ? CollectionUtilities.newOrderedSet() : CollectionUtilities.newOrderedSet(this.theParameterTemplateList);
   }

   public java.util.Enumeration getParameterTemplateList() {
      return Collections.enumeration(this.getCollectionParameterTemplateList());
   }

   public OrderedSet directGetCollectionParameterTemplateList() {
      OrderedSet temp = new OrderedSet();
      if (this.theParameterTemplateList != null) {
         Iterator it = this.theParameterTemplateList.iterator();

         while(it.hasNext()) {
            temp.add(((TemplateParameter)it.next()).getParameterTemplate());
         }
      }

      return temp;
   }

   public java.util.Enumeration directGetParameterTemplateList() {
      return Collections.enumeration(this.directGetCollectionParameterTemplateList());
   }

   public void addParameterTemplate(TemplateParameter arg) {
      if (arg != null) {
         if (this.theParameterTemplateList == null) {
            this.theParameterTemplateList = new OrderedSet();
         }

         if (this.theParameterTemplateList.add(arg)) {
            arg.setTemplate(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "parameterTemplate", 1));
            }
         }
      }

   }

   public void removeParameterTemplate(TemplateParameter arg) {
      if (this.theParameterTemplateList != null && arg != null && this.theParameterTemplateList.remove(arg)) {
         arg.setTemplate((ModelElement)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "parameterTemplate", 2));
         }
      }

   }

   public Set getCollectionTemplateArgumentList() {
      return this.theTemplateArgumentList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theTemplateArgumentList);
   }

   public java.util.Enumeration getTemplateArgumentList() {
      return Collections.enumeration(this.getCollectionTemplateArgumentList());
   }

   public void addTemplateArgument(TemplateArgument arg) {
      if (arg != null) {
         if (this.theTemplateArgumentList == null) {
            this.theTemplateArgumentList = new LinkedHashSet();
         }

         if (this.theTemplateArgumentList.add(arg)) {
            arg.setModelElement(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "templateArgument", 1));
            }
         }
      }

   }

   public void removeTemplateArgument(TemplateArgument arg) {
      if (this.theTemplateArgumentList != null && arg != null && this.theTemplateArgumentList.remove(arg)) {
         arg.setModelElement((ModelElement)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "templateArgument", 2));
         }
      }

   }

   public Set getCollectionCollaborationInstanceSetList() {
      return this.theCollaborationInstanceSetList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theCollaborationInstanceSetList);
   }

   public java.util.Enumeration getCollaborationInstanceSetList() {
      return Collections.enumeration(this.getCollectionCollaborationInstanceSetList());
   }

   public void addCollaborationInstanceSet(CollaborationInstanceSet arg) {
      if (arg != null) {
         if (this.theCollaborationInstanceSetList == null) {
            this.theCollaborationInstanceSetList = new LinkedHashSet();
         }

         if (this.theCollaborationInstanceSetList.add(arg)) {
            arg.addConstrainingElement(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "collaborationInstanceSet", 1));
            }
         }
      }

   }

   public void removeCollaborationInstanceSet(CollaborationInstanceSet arg) {
      if (this.theCollaborationInstanceSetList != null && arg != null && this.theCollaborationInstanceSetList.remove(arg)) {
         arg.removeConstrainingElement(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "collaborationInstanceSet", 2));
         }
      }

   }

   public Set getCollectionBehaviorList() {
      return this.theBehaviorList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theBehaviorList);
   }

   public java.util.Enumeration getBehaviorList() {
      return Collections.enumeration(this.getCollectionBehaviorList());
   }

   public void addBehavior(StateMachine arg) {
      if (arg != null) {
         if (this.theBehaviorList == null) {
            this.theBehaviorList = new LinkedHashSet();
         }

         if (this.theBehaviorList.add(arg)) {
            arg.setContext(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "behavior", 1));
            }
         }
      }

   }

   public void removeBehavior(StateMachine arg) {
      if (this.theBehaviorList != null && arg != null && this.theBehaviorList.remove(arg)) {
         arg.setContext((ModelElement)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "behavior", 2));
         }
      }

   }

   public Set getCollectionConstraintList() {
      return this.theConstraintList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theConstraintList);
   }

   public java.util.Enumeration getConstraintList() {
      return Collections.enumeration(this.getCollectionConstraintList());
   }

   public void addConstraint(Constraint arg) {
      if (arg != null) {
         if (this.theConstraintList == null) {
            this.theConstraintList = new LinkedHashSet();
         }

         if (this.theConstraintList.add(arg)) {
            arg.addConstrainedElement(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "constraint", 1));
            }
         }
      }

   }

   public void removeConstraint(Constraint arg) {
      if (this.theConstraintList != null && arg != null && this.theConstraintList.remove(arg)) {
         arg.removeConstrainedElement(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "constraint", 2));
         }
      }

   }

   public TemplateParameter getTemplate() {
      return this.theTemplate;
   }

   public ModelElement directGetTemplate() {
      return this.theTemplate != null ? this.theTemplate.getTemplate() : null;
   }

   public void setTemplate(TemplateParameter arg) {
      if (this.theTemplate != arg) {
         TemplateParameter temp = this.theTemplate;
         this.theTemplate = null;
         if (temp != null) {
            temp.setParameterTemplate((ModelElement)null);
         }

         if (arg != null) {
            this.theTemplate = arg;
            arg.setParameterTemplate(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "template", 0));
         }
      }

   }

   public ElementOwnership getNamespace() {
      return this.theNamespace;
   }

   public Namespace directGetNamespace() {
      return this.theNamespace != null ? this.theNamespace.getNamespace() : null;
   }

   public void setNamespace(ElementOwnership arg) {
      if (this.theNamespace != arg) {
         ElementOwnership temp = this.theNamespace;
         this.theNamespace = null;
         if (temp != null) {
            temp.setOwnedElement((ModelElement)null);
         }

         if (arg != null) {
            this.theNamespace = arg;
            arg.setOwnedElement(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "namespace", 0));
         }
      }

   }

   public Set getCollectionClientDependencyList() {
      return this.theClientDependencyList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theClientDependencyList);
   }

   public java.util.Enumeration getClientDependencyList() {
      return Collections.enumeration(this.getCollectionClientDependencyList());
   }

   public void addClientDependency(Dependency arg) {
      if (arg != null) {
         if (this.theClientDependencyList == null) {
            this.theClientDependencyList = new LinkedHashSet();
         }

         if (this.theClientDependencyList.add(arg)) {
            arg.addClient(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "clientDependency", 1));
            }
         }
      }

   }

   public void removeClientDependency(Dependency arg) {
      if (this.theClientDependencyList != null && arg != null && this.theClientDependencyList.remove(arg)) {
         arg.removeClient(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "clientDependency", 2));
         }
      }

   }

   public Set supplier() {
      Set setClientDependency = this.getCollectionClientDependencyList();
      List bagCollect = CollectionUtilities.newBag();
      Iterator iter = setClientDependency.iterator();

      while(iter.hasNext()) {
         Dependency decl = (Dependency)iter.next();
         Set setSupplier = decl.getCollectionSupplierList();
         bagCollect.add(setSupplier);
      }

      bagCollect = CollectionUtilities.flatten(bagCollect);
      Set setAsSet = CollectionUtilities.asSet(bagCollect);
      return setAsSet;
   }

   public Set supplierP() {
      Set setClientDependency = this.getCollectionClientDependencyList();
      Set setSelect = CollectionUtilities.newSet();
      Iterator iter = setClientDependency.iterator();

      Set setAsSet;
      while(iter.hasNext()) {
         Dependency cd = (Dependency)iter.next();
         boolean bOclIsTypeOf = Ocl.isTypeOf(cd, Ocl.getType(new java.lang.Class[]{Permission.class}));
         setAsSet = cd.getCollectionStereotypeList();
         List bagCollect = CollectionUtilities.newBag();
         Iterator iter0 = setAsSet.iterator();

         while(iter0.hasNext()) {
            Stereotype decl = (Stereotype)iter0.next();
            String strName = decl.getName();
            bagCollect.add(strName);
         }

         bagCollect = CollectionUtilities.flatten(bagCollect);
         boolean bIncludes = CollectionUtilities.includes(bagCollect, "import");
         boolean bAnd = bOclIsTypeOf && bIncludes;
         if (bAnd) {
            CollectionUtilities.add(setSelect, cd);
         }
      }

      List bagCollect0 = CollectionUtilities.newBag();
      Iterator iter1 = setSelect.iterator();

      while(iter1.hasNext()) {
         Dependency decl0 = (Dependency)iter1.next();
         Set setSupplier = decl0.getCollectionSupplierList();
         bagCollect0.add(setSupplier);
      }

      bagCollect0 = CollectionUtilities.flatten(bagCollect0);
      setAsSet = CollectionUtilities.asSet(bagCollect0);
      return setAsSet;
   }

   public Set supplierD() {
      Set setClientDependency = this.getCollectionClientDependencyList();
      Set setSelect = CollectionUtilities.newSet();
      Iterator iter = setClientDependency.iterator();

      while(iter.hasNext()) {
         Dependency iter1 = (Dependency)iter.next();
         boolean bOclIsTypeOf = Ocl.isTypeOf(iter1, Ocl.getType(new java.lang.Class[]{Dependency.class}));
         if (bOclIsTypeOf) {
            CollectionUtilities.add(setSelect, iter1);
         }
      }

      List bagCollect = CollectionUtilities.newBag();
      Iterator iter0 = setSelect.iterator();

      while(iter0.hasNext()) {
         Dependency decl = (Dependency)iter0.next();
         Set setSupplier = decl.getCollectionSupplierList();
         bagCollect.add(setSupplier);
      }

      bagCollect = CollectionUtilities.flatten(bagCollect);
      Set setAsSet = CollectionUtilities.asSet(bagCollect);
      return setAsSet;
   }

   public Set allSuppliers() {
      Set set = CollectionUtilities.newSet();
      CollectionUtilities.add(set, this);
      Set setClosure = CollectionUtilities.newSet();
      List queue = CollectionUtilities.newOrderedSet();
      queue.addAll(set);

      for(int i = 0; i < queue.size(); ++i) {
         ModelElement it = (ModelElement)queue.get(i);
         Set setsupplier = it.supplier();
         queue.addAll(setsupplier);
         setClosure.addAll(setsupplier);
      }

      return setClosure;
   }

   public Set allSuppliersP() {
      Set set = CollectionUtilities.newSet();
      CollectionUtilities.add(set, this);
      Set setClosure = CollectionUtilities.newSet();
      List queue = CollectionUtilities.newOrderedSet();
      queue.addAll(set);

      for(int i = 0; i < queue.size(); ++i) {
         ModelElement it = (ModelElement)queue.get(i);
         Set setsupplierP = it.supplierP();
         queue.addAll(setsupplierP);
         setClosure.addAll(setsupplierP);
      }

      return setClosure;
   }

   public Set allSuppliersD() {
      Set set = CollectionUtilities.newSet();
      CollectionUtilities.add(set, this);
      Set setClosure = CollectionUtilities.newSet();
      List queue = CollectionUtilities.newOrderedSet();
      queue.addAll(set);

      for(int i = 0; i < queue.size(); ++i) {
         ModelElement it = (ModelElement)queue.get(i);
         Set setsupplierD = it.supplierD();
         queue.addAll(setsupplierD);
         setClosure.addAll(setsupplierD);
      }

      return setClosure;
   }

   public Set model() {
      Namespace namespaceNamespace = this.directGetNamespace();
      boolean bIsDefined = Ocl.isDefined(namespaceNamespace);
      Set setIf;
      Set setIf1;
      Set setIf2;
      boolean bOclIsKindOf2;
      Set setIf4;
      if (bIsDefined) {
         Namespace namespaceNamespace0 = this.directGetNamespace();
         Namespace namespaceNamespace1 = this.directGetNamespace();
         setIf1 = namespaceNamespace1.allSurroundingNamespaces();
         Set setUnion = CollectionUtilities.union((Object)namespaceNamespace0, (Set)setIf1);
         setIf2 = CollectionUtilities.newSet();
         Iterator iter = setUnion.iterator();

         while(iter.hasNext()) {
            Namespace ns = (Namespace)iter.next();
            bOclIsKindOf2 = Ocl.isKindOf(ns, Ocl.getType(new java.lang.Class[]{Model.class}));
            if (bOclIsKindOf2) {
               CollectionUtilities.add(setIf2, ns);
            }
         }

         List bagCollect = CollectionUtilities.newBag();
         Iterator iter0 = setIf2.iterator();

         while(iter0.hasNext()) {
            Namespace decl = (Namespace)iter0.next();
            Model modelOclAsType = (Model)decl;
            bagCollect.add(modelOclAsType);
         }

         bagCollect = CollectionUtilities.flatten(bagCollect);
         setIf4 = CollectionUtilities.asSet(bagCollect);
         setIf = setIf4;
      } else {
         boolean bOclIsKindOf0 = Ocl.isKindOf(this, Ocl.getType(new java.lang.Class[]{Feature.class}));
         Set setIf0;
         if (bOclIsKindOf0) {
            Feature featureOclAsType = (Feature)this;
            Classifier classifierOwner = featureOclAsType.getOwner();
            setIf2 = classifierOwner.model();
            setIf0 = setIf2;
         } else {
            boolean bOclIsTypeOf = Ocl.isTypeOf(this, Ocl.getType(new java.lang.Class[]{Parameter.class}));
            if (bOclIsTypeOf) {
               Parameter parameterOclAsType = (Parameter)this;
               BehavioralFeature behavioralFeatureBehavioralFeature = parameterOclAsType.getBehavioralFeature();
               Classifier classifierOwner0 = behavioralFeatureBehavioralFeature.getOwner();
               Set setmodel0 = classifierOwner0.model();
               setIf1 = setmodel0;
            } else {
               boolean bOclIsKindOf1 = Ocl.isKindOf(this, Ocl.getType(new java.lang.Class[]{AssociationEnd.class}));
               if (bOclIsKindOf1) {
                  AssociationEnd associationEndOclAsType = (AssociationEnd)this;
                  Association associationAssociation = associationEndOclAsType.getAssociation();
                  setIf4 = associationAssociation.model();
                  setIf2 = setIf4;
               } else {
                  bOclIsKindOf2 = Ocl.isKindOf(this, Ocl.getType(new java.lang.Class[]{Message.class}));
                  Set setIf3;
                  if (bOclIsKindOf2) {
                     Message messageOclAsType = (Message)this;
                     Interaction interactionInteraction = messageOclAsType.getInteraction();
                     Collaboration collaborationContext = interactionInteraction.getContext();
                     Set setmodel2 = collaborationContext.model();
                     setIf3 = setmodel2;
                  } else {
                     boolean bOclIsKindOf3 = Ocl.isKindOf(this, Ocl.getType(new java.lang.Class[]{Interaction.class}));
                     if (bOclIsKindOf3) {
                        Interaction interactionOclAsType = (Interaction)this;
                        Collaboration collaborationContext0 = interactionOclAsType.getContext();
                        Set setmodel3 = collaborationContext0.model();
                        setIf4 = setmodel3;
                     } else {
                        Set set = CollectionUtilities.newSet();
                        setIf4 = set;
                     }

                     setIf3 = setIf4;
                  }

                  setIf2 = setIf3;
               }

               setIf1 = setIf2;
            }

            setIf0 = setIf1;
         }

         setIf = setIf0;
      }

      return setIf;
   }

   public boolean isTemplate() {
      List seqTemplateParameter = this.getCollectionParameterTemplateList();
      boolean bNotEmpty = CollectionUtilities.notEmpty((Collection)seqTemplateParameter);
      return bNotEmpty;
   }

   public boolean isInstantiated() {
      Set setClientDependency = this.getCollectionClientDependencyList();
      boolean bExists = false;

      boolean bOclIsKindOf;
      for(Iterator iter = setClientDependency.iterator(); !bExists && iter.hasNext(); bExists = bOclIsKindOf) {
         Dependency iter1 = (Dependency)iter.next();
         bOclIsKindOf = Ocl.isKindOf(iter1, Ocl.getType(new java.lang.Class[]{Binding.class}));
      }

      return bExists;
   }

   public Set templateArguments() {
      Set setClientDependency = this.getCollectionClientDependencyList();
      Set setSelect = CollectionUtilities.newSet();
      Iterator iter = setClientDependency.iterator();

      while(iter.hasNext()) {
         Dependency iter1 = (Dependency)iter.next();
         boolean bOclIsKindOf = Ocl.isKindOf(iter1, Ocl.getType(new java.lang.Class[]{Binding.class}));
         if (bOclIsKindOf) {
            CollectionUtilities.add(setSelect, iter1);
         }
      }

      List bagCollect = CollectionUtilities.newBag();
      Iterator iter0 = setSelect.iterator();

      while(iter0.hasNext()) {
         Dependency decl = (Dependency)iter0.next();
         Binding bindingOclAsType = (Binding)decl;
         bagCollect.add(bindingOclAsType);
      }

      bagCollect = CollectionUtilities.flatten(bagCollect);
      List bagCollect0 = CollectionUtilities.newBag();
      Iterator iter1 = bagCollect.iterator();

      while(iter1.hasNext()) {
         Binding decl0 = (Binding)iter1.next();
         Set setArgument = decl0.getCollectionArgumentList();
         bagCollect0.add(setArgument);
      }

      bagCollect0 = CollectionUtilities.flatten(bagCollect0);
      List bagCollect1 = CollectionUtilities.newBag();
      Iterator iter2 = bagCollect0.iterator();

      while(iter2.hasNext()) {
         TemplateArgument decl1 = (TemplateArgument)iter2.next();
         ModelElement modelElementModelElement = decl1.getModelElement();
         bagCollect1.add(modelElementModelElement);
      }

      bagCollect1 = CollectionUtilities.flatten(bagCollect1);
      Set setAsSet = CollectionUtilities.asSet(bagCollect1);
      return setAsSet;
   }

   protected void internalRemove() {
      java.util.Enumeration tmpCommentEnum = this.getCommentList();
      ArrayList tmpCommentList = new ArrayList();

      while(tmpCommentEnum.hasMoreElements()) {
         tmpCommentList.add(tmpCommentEnum.nextElement());
      }

      Iterator it = tmpCommentList.iterator();

      while(it.hasNext()) {
         Comment tmpComment = (Comment)it.next();
         tmpComment.removeAnnotatedElement(this);
      }

      java.util.Enumeration tmpDefaultedParameterEnum = this.getDefaultedParameterList();
      ArrayList tmpDefaultedParameterList = new ArrayList();

      while(tmpDefaultedParameterEnum.hasMoreElements()) {
         tmpDefaultedParameterList.add(tmpDefaultedParameterEnum.nextElement());
      }

      it = tmpDefaultedParameterList.iterator();

      while(it.hasNext()) {
         ((TemplateParameter)it.next()).setDefaultElement((ModelElement)null);
      }

      java.util.Enumeration tmpTargetFlowEnum = this.getTargetFlowList();
      ArrayList tmpTargetFlowList = new ArrayList();

      while(tmpTargetFlowEnum.hasMoreElements()) {
         tmpTargetFlowList.add(tmpTargetFlowEnum.nextElement());
      }

      it = tmpTargetFlowList.iterator();

      while(it.hasNext()) {
         Flow tmpTargetFlow = (Flow)it.next();
         tmpTargetFlow.removeTarget(this);
      }

      java.util.Enumeration tmpPackageEnum = this.getPackageList();
      ArrayList tmpPackageList = new ArrayList();

      while(tmpPackageEnum.hasMoreElements()) {
         tmpPackageList.add(tmpPackageEnum.nextElement());
      }

      it = tmpPackageList.iterator();

      while(it.hasNext()) {
         ElementImport tmpPackage = (ElementImport)it.next();
         tmpPackage.getPackage().removeImportedElement(tmpPackage);
      }

      java.util.Enumeration tmpStereotypeEnum = this.getStereotypeList();
      ArrayList tmpStereotypeList = new ArrayList();

      while(tmpStereotypeEnum.hasMoreElements()) {
         tmpStereotypeList.add(tmpStereotypeEnum.nextElement());
      }

     it = tmpStereotypeList.iterator();

      while(it.hasNext()) {
         Stereotype tmpStereotype = (Stereotype)it.next();
         tmpStereotype.removeExtendedElement(this);
      }

      java.util.Enumeration tmpContainerEnum = this.getContainerList();
      ArrayList tmpContainerList = new ArrayList();

      while(tmpContainerEnum.hasMoreElements()) {
         tmpContainerList.add(tmpContainerEnum.nextElement());
      }

      it = tmpContainerList.iterator();

      while(it.hasNext()) {
         ElementResidence tmpContainer = (ElementResidence)it.next();
         tmpContainer.getContainer().removeResident(tmpContainer);
      }

      java.util.Enumeration tmpPresentationEnum = this.getPresentationList();
      ArrayList tmpPresentationList = new ArrayList();

      while(tmpPresentationEnum.hasMoreElements()) {
         tmpPresentationList.add(tmpPresentationEnum.nextElement());
      }

       it = tmpPresentationList.iterator();

      while(it.hasNext()) {
         PresentationElement tmpPresentation = (PresentationElement)it.next();
         tmpPresentation.removeSubject(this);
      }

      java.util.Enumeration tmpCollaboration2Enum = this.getCollaboration2List();
      ArrayList tmpCollaboration2List = new ArrayList();

      while(tmpCollaboration2Enum.hasMoreElements()) {
         tmpCollaboration2List.add(tmpCollaboration2Enum.nextElement());
      }

       it = tmpCollaboration2List.iterator();

      while(it.hasNext()) {
         Collaboration tmpCollaboration2 = (Collaboration)it.next();
         tmpCollaboration2.removeConstrainingElement(this);
      }

      java.util.Enumeration tmpTaggedValueEnum = this.getTaggedValueList();
      ArrayList tmpTaggedValueList = new ArrayList();

      while(tmpTaggedValueEnum.hasMoreElements()) {
         tmpTaggedValueList.add(tmpTaggedValueEnum.nextElement());
      }

       it = tmpTaggedValueList.iterator();

      while(it.hasNext()) {
         ((TaggedValue)it.next()).remove();
      }

      java.util.Enumeration tmpPartitionEnum = this.getPartitionList();
      ArrayList tmpPartitionList = new ArrayList();

      while(tmpPartitionEnum.hasMoreElements()) {
         tmpPartitionList.add(tmpPartitionEnum.nextElement());
      }

       it = tmpPartitionList.iterator();

      while(it.hasNext()) {
         Partition tmpPartition = (Partition)it.next();
         tmpPartition.removeContents(this);
      }

      java.util.Enumeration tmpReferenceTagEnum = this.getReferenceTagList();
      ArrayList tmpReferenceTagList = new ArrayList();

      while(tmpReferenceTagEnum.hasMoreElements()) {
         tmpReferenceTagList.add(tmpReferenceTagEnum.nextElement());
      }

       it = tmpReferenceTagList.iterator();

      while(it.hasNext()) {
         TaggedValue tmpReferenceTag = (TaggedValue)it.next();
         tmpReferenceTag.removeReferenceValue(this);
      }

      java.util.Enumeration tmpClassifierRoleEnum = this.getClassifierRoleList();
      ArrayList tmpClassifierRoleList = new ArrayList();

      while(tmpClassifierRoleEnum.hasMoreElements()) {
         tmpClassifierRoleList.add(tmpClassifierRoleEnum.nextElement());
      }

       it = tmpClassifierRoleList.iterator();

      while(it.hasNext()) {
         ClassifierRole tmpClassifierRole = (ClassifierRole)it.next();
         tmpClassifierRole.removeAvailableContents(this);
      }

      java.util.Enumeration tmpSupplierDependencyEnum = this.getSupplierDependencyList();
      ArrayList tmpSupplierDependencyList = new ArrayList();

      while(tmpSupplierDependencyEnum.hasMoreElements()) {
         tmpSupplierDependencyList.add(tmpSupplierDependencyEnum.nextElement());
      }

       it = tmpSupplierDependencyList.iterator();

      while(it.hasNext()) {
         Dependency tmpSupplierDependency = (Dependency)it.next();
         tmpSupplierDependency.removeSupplier(this);
         if (tmpSupplierDependency.getCollectionSupplierList().size() < 1) {
            tmpSupplierDependency.remove();
         }
      }

      java.util.Enumeration tmpSourceFlowEnum = this.getSourceFlowList();
      ArrayList tmpSourceFlowList = new ArrayList();

      while(tmpSourceFlowEnum.hasMoreElements()) {
         tmpSourceFlowList.add(tmpSourceFlowEnum.nextElement());
      }

       it = tmpSourceFlowList.iterator();

      while(it.hasNext()) {
         Flow tmpSourceFlow = (Flow)it.next();
         tmpSourceFlow.removeSource(this);
      }

      java.util.Enumeration tmpParameterTemplateEnum = this.directGetParameterTemplateList();
      ArrayList tmpParameterTemplateList = new ArrayList();

      while(tmpParameterTemplateEnum.hasMoreElements()) {
         tmpParameterTemplateList.add(tmpParameterTemplateEnum.nextElement());
      }

       it = tmpParameterTemplateList.iterator();

      while(it.hasNext()) {
         ((ModelElement)it.next()).remove();
      }

      java.util.Enumeration tmpTemplateArgumentEnum = this.getTemplateArgumentList();
      ArrayList tmpTemplateArgumentList = new ArrayList();

      while(tmpTemplateArgumentEnum.hasMoreElements()) {
         tmpTemplateArgumentList.add(tmpTemplateArgumentEnum.nextElement());
      }

       it = tmpTemplateArgumentList.iterator();

      while(it.hasNext()) {
         ((TemplateArgument)it.next()).setModelElement((ModelElement)null);
      }

      java.util.Enumeration tmpCollaborationInstanceSetEnum = this.getCollaborationInstanceSetList();
      ArrayList tmpCollaborationInstanceSetList = new ArrayList();

      while(tmpCollaborationInstanceSetEnum.hasMoreElements()) {
         tmpCollaborationInstanceSetList.add(tmpCollaborationInstanceSetEnum.nextElement());
      }

       it = tmpCollaborationInstanceSetList.iterator();

      while(it.hasNext()) {
         CollaborationInstanceSet tmpCollaborationInstanceSet = (CollaborationInstanceSet)it.next();
         tmpCollaborationInstanceSet.removeConstrainingElement(this);
      }

      java.util.Enumeration tmpBehaviorEnum = this.getBehaviorList();
      ArrayList tmpBehaviorList = new ArrayList();

      while(tmpBehaviorEnum.hasMoreElements()) {
         tmpBehaviorList.add(tmpBehaviorEnum.nextElement());
      }

       it = tmpBehaviorList.iterator();

      while(it.hasNext()) {
         ((StateMachine)it.next()).setContext((ModelElement)null);
      }

      java.util.Enumeration tmpConstraintEnum = this.getConstraintList();
      ArrayList tmpConstraintList = new ArrayList();

      while(tmpConstraintEnum.hasMoreElements()) {
         tmpConstraintList.add(tmpConstraintEnum.nextElement());
      }

       it = tmpConstraintList.iterator();

      while(it.hasNext()) {
         Constraint tmpConstraint = (Constraint)it.next();
         tmpConstraint.removeConstrainedElement(this);
      }

      ModelElement tmpTemplate = this.directGetTemplate();
      if (tmpTemplate != null) {
         tmpTemplate.removeParameterTemplate(this.getTemplate());
      }

      Namespace tmpNamespace = this.directGetNamespace();
      if (tmpNamespace != null) {
         tmpNamespace.removeOwnedElement(this.getNamespace());
      }

      java.util.Enumeration tmpClientDependencyEnum = this.getClientDependencyList();
      ArrayList tmpClientDependencyList = new ArrayList();

      while(tmpClientDependencyEnum.hasMoreElements()) {
         tmpClientDependencyList.add(tmpClientDependencyEnum.nextElement());
      }

       it = tmpClientDependencyList.iterator();

      while(it.hasNext()) {
         Dependency tmpClientDependency = (Dependency)it.next();
         tmpClientDependency.removeClient(this);
         if (tmpClientDependency.getCollectionClientList().size() < 1) {
            tmpClientDependency.remove();
         }
      }

      super.internalRemove();
   }

   public String toString() {
      return this.theName != null && !this.theName.equals("") ? this.theName : "Unnamed " + this.getMetaclassName();
   }
}
