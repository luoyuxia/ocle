package ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.associationActions;

import java.util.Enumeration;
import java.util.Set;
import ro.ubbcluj.lci.uml.foundation.core.AssociationEnd;
import ro.ubbcluj.lci.uml.foundation.core.Element;

public interface LinkEndData extends Element {
   Enumeration getQualifierList();

   Set getCollectionQualifierList();

   void addQualifier(QualifierValue var1);

   void removeQualifier(QualifierValue var1);

   AssociationEnd getEnd();

   void setEnd(AssociationEnd var1);
}
