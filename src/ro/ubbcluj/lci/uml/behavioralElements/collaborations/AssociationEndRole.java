package ro.ubbcluj.lci.uml.behavioralElements.collaborations;

import java.util.Enumeration;
import java.util.Set;
import ro.ubbcluj.lci.uml.foundation.core.AssociationEnd;
import ro.ubbcluj.lci.uml.foundation.core.Attribute;
import ro.ubbcluj.lci.uml.foundation.dataTypes.Multiplicity;

public interface AssociationEndRole extends AssociationEnd {
   Multiplicity getCollaborationMultiplicity();

   void setCollaborationMultiplicity(Multiplicity var1);

   Enumeration getAvailableQualifierList();

   Set getCollectionAvailableQualifierList();

   void addAvailableQualifier(Attribute var1);

   void removeAvailableQualifier(Attribute var1);

   AssociationEnd getBase();

   void setBase(AssociationEnd var1);
}
