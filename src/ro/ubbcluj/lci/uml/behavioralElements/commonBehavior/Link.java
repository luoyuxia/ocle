package ro.ubbcluj.lci.uml.behavioralElements.commonBehavior;

import java.util.Enumeration;
import java.util.Set;
import ro.ubbcluj.lci.codegen.framework.dt.OrderedSet;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.AssociationRole;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.CollaborationInstanceSet;
import ro.ubbcluj.lci.uml.foundation.core.Association;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;

public interface Link extends ModelElement {
   Instance getOwner();

   void setOwner(Instance var1);

   Enumeration getCollaborationInstanceSetList();

   Set getCollectionCollaborationInstanceSetList();

   void addCollaborationInstanceSet(CollaborationInstanceSet var1);

   void removeCollaborationInstanceSet(CollaborationInstanceSet var1);

   Enumeration getConnectionList();

   OrderedSet getCollectionConnectionList();

   void addConnection(LinkEnd var1);

   void removeConnection(LinkEnd var1);

   Enumeration getStimulusList();

   Set getCollectionStimulusList();

   void addStimulus(Stimulus var1);

   void removeStimulus(Stimulus var1);

   Association getAssociation();

   void setAssociation(Association var1);

   Enumeration getPlayedRoleList();

   Set getCollectionPlayedRoleList();

   void addPlayedRole(AssociationRole var1);

   void removePlayedRole(AssociationRole var1);
}
