package ro.ubbcluj.lci.uml.behavioralElements.collaborations;

import java.util.Enumeration;
import java.util.Set;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.core.GeneralizableElement;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.uml.foundation.core.Namespace;
import ro.ubbcluj.lci.uml.foundation.core.Operation;

public interface Collaboration extends Namespace, GeneralizableElement {
   Enumeration getConstrainingElementList();

   Set getCollectionConstrainingElementList();

   void addConstrainingElement(ModelElement var1);

   void removeConstrainingElement(ModelElement var1);

   Operation getRepresentedOperation();

   void setRepresentedOperation(Operation var1);

   Enumeration getUsedCollaborationList();

   Set getCollectionUsedCollaborationList();

   void addUsedCollaboration(Collaboration var1);

   void removeUsedCollaboration(Collaboration var1);

   Enumeration getCollaboration1List();

   Set getCollectionCollaboration1List();

   void addCollaboration1(Collaboration var1);

   void removeCollaboration1(Collaboration var1);

   Enumeration getInteractionList();

   Set getCollectionInteractionList();

   void addInteraction(Interaction var1);

   void removeInteraction(Interaction var1);

   Enumeration getCollaborationInstanceSetList();

   Set getCollectionCollaborationInstanceSetList();

   void addCollaborationInstanceSet(CollaborationInstanceSet var1);

   void removeCollaborationInstanceSet(CollaborationInstanceSet var1);

   Classifier getRepresentedClassifier();

   void setRepresentedClassifier(Classifier var1);

   Set allContents();
}
