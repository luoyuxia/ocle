package ro.ubbcluj.lci.uml.foundation.core;

import java.util.Set;
import ro.ubbcluj.lci.codegen.framework.dt.OrderedSet;
import ro.ubbcluj.lci.uml.behavioralElements.activityGraphs.Partition;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.ClassifierRole;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.Collaboration;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.CollaborationInstanceSet;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.StateMachine;
import ro.ubbcluj.lci.uml.foundation.extensionMechanisms.Stereotype;
import ro.ubbcluj.lci.uml.foundation.extensionMechanisms.TaggedValue;
import ro.ubbcluj.lci.uml.modelManagement.ElementImport;

public interface ModelElement extends Element {
   String getName();

   void setName(String var1);

   java.util.Enumeration getCommentList();

   Set getCollectionCommentList();

   void addComment(Comment var1);

   void removeComment(Comment var1);

   java.util.Enumeration getDefaultedParameterList();

   Set getCollectionDefaultedParameterList();

   void addDefaultedParameter(TemplateParameter var1);

   void removeDefaultedParameter(TemplateParameter var1);

   java.util.Enumeration getTargetFlowList();

   Set getCollectionTargetFlowList();

   void addTargetFlow(Flow var1);

   void removeTargetFlow(Flow var1);

   java.util.Enumeration getPackageList();

   Set getCollectionPackageList();

   java.util.Enumeration directGetPackageList();

   Set directGetCollectionPackageList();

   void addPackage(ElementImport var1);

   void removePackage(ElementImport var1);

   java.util.Enumeration getStereotypeList();

   Set getCollectionStereotypeList();

   void addStereotype(Stereotype var1);

   void removeStereotype(Stereotype var1);

   java.util.Enumeration getContainerList();

   Set getCollectionContainerList();

   java.util.Enumeration directGetContainerList();

   Set directGetCollectionContainerList();

   void addContainer(ElementResidence var1);

   void removeContainer(ElementResidence var1);

   java.util.Enumeration getPresentationList();

   Set getCollectionPresentationList();

   void addPresentation(PresentationElement var1);

   void removePresentation(PresentationElement var1);

   java.util.Enumeration getCollaboration2List();

   Set getCollectionCollaboration2List();

   void addCollaboration2(Collaboration var1);

   void removeCollaboration2(Collaboration var1);

   java.util.Enumeration getTaggedValueList();

   Set getCollectionTaggedValueList();

   void addTaggedValue(TaggedValue var1);

   void removeTaggedValue(TaggedValue var1);

   java.util.Enumeration getPartitionList();

   Set getCollectionPartitionList();

   void addPartition(Partition var1);

   void removePartition(Partition var1);

   java.util.Enumeration getReferenceTagList();

   Set getCollectionReferenceTagList();

   void addReferenceTag(TaggedValue var1);

   void removeReferenceTag(TaggedValue var1);

   java.util.Enumeration getClassifierRoleList();

   Set getCollectionClassifierRoleList();

   void addClassifierRole(ClassifierRole var1);

   void removeClassifierRole(ClassifierRole var1);

   java.util.Enumeration getSupplierDependencyList();

   Set getCollectionSupplierDependencyList();

   void addSupplierDependency(Dependency var1);

   void removeSupplierDependency(Dependency var1);

   java.util.Enumeration getSourceFlowList();

   Set getCollectionSourceFlowList();

   void addSourceFlow(Flow var1);

   void removeSourceFlow(Flow var1);

   java.util.Enumeration getParameterTemplateList();

   OrderedSet getCollectionParameterTemplateList();

   java.util.Enumeration directGetParameterTemplateList();

   OrderedSet directGetCollectionParameterTemplateList();

   void addParameterTemplate(TemplateParameter var1);

   void removeParameterTemplate(TemplateParameter var1);

   java.util.Enumeration getTemplateArgumentList();

   Set getCollectionTemplateArgumentList();

   void addTemplateArgument(TemplateArgument var1);

   void removeTemplateArgument(TemplateArgument var1);

   java.util.Enumeration getCollaborationInstanceSetList();

   Set getCollectionCollaborationInstanceSetList();

   void addCollaborationInstanceSet(CollaborationInstanceSet var1);

   void removeCollaborationInstanceSet(CollaborationInstanceSet var1);

   java.util.Enumeration getBehaviorList();

   Set getCollectionBehaviorList();

   void addBehavior(StateMachine var1);

   void removeBehavior(StateMachine var1);

   java.util.Enumeration getConstraintList();

   Set getCollectionConstraintList();

   void addConstraint(Constraint var1);

   void removeConstraint(Constraint var1);

   TemplateParameter getTemplate();

   ModelElement directGetTemplate();

   void setTemplate(TemplateParameter var1);

   ElementOwnership getNamespace();

   Namespace directGetNamespace();

   void setNamespace(ElementOwnership var1);

   java.util.Enumeration getClientDependencyList();

   Set getCollectionClientDependencyList();

   void addClientDependency(Dependency var1);

   void removeClientDependency(Dependency var1);

   Set supplier();

   Set supplierP();

   Set supplierD();

   Set allSuppliers();

   Set allSuppliersP();

   Set allSuppliersD();

   Set model();

   boolean isTemplate();

   boolean isInstantiated();

   Set templateArguments();
}
