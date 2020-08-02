package ro.ubbcluj.lci.uml.foundation.core;

import java.util.List;
import java.util.Set;
import ro.ubbcluj.lci.codegen.framework.dt.OrderedSet;
import ro.ubbcluj.lci.uml.behavioralElements.activityGraphs.ClassifierInState;
import ro.ubbcluj.lci.uml.behavioralElements.activityGraphs.ObjectFlowState;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.ClassifierRole;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.Collaboration;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Instance;

public interface Classifier extends Namespace, GeneralizableElement {
   java.util.Enumeration getTypedFeatureList();

   OrderedSet getCollectionTypedFeatureList();

   void addTypedFeature(StructuralFeature var1);

   void removeTypedFeature(StructuralFeature var1);

   java.util.Enumeration getInstanceList();

   Set getCollectionInstanceList();

   void addInstance(Instance var1);

   void removeInstance(Instance var1);

   java.util.Enumeration getPowertypeRangeList();

   Set getCollectionPowertypeRangeList();

   void addPowertypeRange(Generalization var1);

   void removePowertypeRange(Generalization var1);

   java.util.Enumeration getTypedParameterList();

   Set getCollectionTypedParameterList();

   void addTypedParameter(Parameter var1);

   void removeTypedParameter(Parameter var1);

   java.util.Enumeration getCollaborationList();

   Set getCollectionCollaborationList();

   void addCollaboration(Collaboration var1);

   void removeCollaboration(Collaboration var1);

   java.util.Enumeration getObjectFlowStateList();

   Set getCollectionObjectFlowStateList();

   void addObjectFlowState(ObjectFlowState var1);

   void removeObjectFlowState(ObjectFlowState var1);

   java.util.Enumeration getFeatureList();

   OrderedSet getCollectionFeatureList();

   void addFeature(Feature var1);

   void removeFeature(Feature var1);

   java.util.Enumeration getClassifierInStateList();

   Set getCollectionClassifierInStateList();

   void addClassifierInState(ClassifierInState var1);

   void removeClassifierInState(ClassifierInState var1);

   java.util.Enumeration getClassifierRoleList();

   Set getCollectionClassifierRoleList();

   void addClassifierRole(ClassifierRole var1);

   void removeClassifierRole(ClassifierRole var1);

   java.util.Enumeration getAssociationList();

   Set getCollectionAssociationList();

   void addAssociation(AssociationEnd var1);

   void removeAssociation(AssociationEnd var1);

   java.util.Enumeration getSpecifiedEndList();

   Set getCollectionSpecifiedEndList();

   void addSpecifiedEnd(AssociationEnd var1);

   void removeSpecifiedEnd(AssociationEnd var1);

   List allFeatures();

   Set allOperations();

   Set allMethods();

   Set allAttributes();

   Set associations();

   Set allAssociations();

   Set selOpAsEnd(Association var1, Classifier var2);

   Set oppositeAssociationEnds();

   Set allOppositeAssociationEnds();

   Set allContents();

   Set specification();

   Set allDiscriminators();
}
