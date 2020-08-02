package ro.ubbcluj.lci.uml.behavioralElements.commonBehavior;

import java.util.Enumeration;
import java.util.Set;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.ClassifierRole;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.CollaborationInstanceSet;
import ro.ubbcluj.lci.uml.foundation.core.AssociationEnd;
import ro.ubbcluj.lci.uml.foundation.core.Attribute;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;

public interface Instance extends ModelElement {
   Enumeration getClassifierList();

   Set getCollectionClassifierList();

   void addClassifier(Classifier var1);

   void removeClassifier(Classifier var1);

   Enumeration getStimulus1List();

   Set getCollectionStimulus1List();

   void addStimulus1(Stimulus var1);

   void removeStimulus1(Stimulus var1);

   Enumeration getSlotList();

   Set getCollectionSlotList();

   void addSlot(AttributeLink var1);

   void removeSlot(AttributeLink var1);

   Enumeration getOwnedInstanceList();

   Set getCollectionOwnedInstanceList();

   void addOwnedInstance(Instance var1);

   void removeOwnedInstance(Instance var1);

   Enumeration getPlayedRoleList();

   Set getCollectionPlayedRoleList();

   void addPlayedRole(ClassifierRole var1);

   void removePlayedRole(ClassifierRole var1);

   Enumeration getCollaborationInstanceSetList();

   Set getCollectionCollaborationInstanceSetList();

   void addCollaborationInstanceSet(CollaborationInstanceSet var1);

   void removeCollaborationInstanceSet(CollaborationInstanceSet var1);

   Enumeration getStimulus2List();

   Set getCollectionStimulus2List();

   void addStimulus2(Stimulus var1);

   void removeStimulus2(Stimulus var1);

   Enumeration getAttributeLinkList();

   Set getCollectionAttributeLinkList();

   void addAttributeLink(AttributeLink var1);

   void removeAttributeLink(AttributeLink var1);

   Instance getOwner();

   void setOwner(Instance var1);

   Enumeration getStimulus3List();

   Set getCollectionStimulus3List();

   void addStimulus3(Stimulus var1);

   void removeStimulus3(Stimulus var1);

   Enumeration getOwnedLinkList();

   Set getCollectionOwnedLinkList();

   void addOwnedLink(Link var1);

   void removeOwnedLink(Link var1);

   Enumeration getLinkEndList();

   Set getCollectionLinkEndList();

   void addLinkEnd(LinkEnd var1);

   void removeLinkEnd(LinkEnd var1);

   ComponentInstance getComponentInstance();

   void setComponentInstance(ComponentInstance var1);

   Set allLinks();

   Set allOppositeLinkEnds();

   Set selectedLinkEnds(AssociationEnd var1);

   Set selectedAttributeLinks(Attribute var1);

   Set contents();
}
