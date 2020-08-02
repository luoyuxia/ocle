package ro.ubbcluj.lci.uml.foundation.core;

import java.util.Set;
import ro.ubbcluj.lci.codegen.framework.dt.OrderedSet;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.AssociationEndRole;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.LinkEnd;
import ro.ubbcluj.lci.uml.foundation.dataTypes.Multiplicity;

public interface AssociationEnd extends ModelElement {
   boolean isNavigable();

   void setNavigable(boolean var1);

   int getOrdering();

   void setOrdering(int var1);

   int getAggregation();

   void setAggregation(int var1);

   int getTargetScope();

   void setTargetScope(int var1);

   int getChangeability();

   void setChangeability(int var1);

   int getVisibility();

   void setVisibility(int var1);

   Multiplicity getMultiplicity();

   void setMultiplicity(Multiplicity var1);

   java.util.Enumeration getLinkEndList();

   Set getCollectionLinkEndList();

   void addLinkEnd(LinkEnd var1);

   void removeLinkEnd(LinkEnd var1);

   java.util.Enumeration getQualifierList();

   OrderedSet getCollectionQualifierList();

   void addQualifier(Attribute var1);

   void removeQualifier(Attribute var1);

   java.util.Enumeration getSpecificationList();

   Set getCollectionSpecificationList();

   void addSpecification(Classifier var1);

   void removeSpecification(Classifier var1);

   Classifier getParticipant();

   void setParticipant(Classifier var1);

   Association getAssociation();

   void setAssociation(Association var1);

   java.util.Enumeration getAssociationEndRoleList();

   Set getCollectionAssociationEndRoleList();

   void addAssociationEndRole(AssociationEndRole var1);

   void removeAssociationEndRole(AssociationEndRole var1);

   int upperbound();
}
