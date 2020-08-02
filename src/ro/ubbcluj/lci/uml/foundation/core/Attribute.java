package ro.ubbcluj.lci.uml.foundation.core;

import java.util.Set;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.AssociationEndRole;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.AttributeLink;
import ro.ubbcluj.lci.uml.foundation.dataTypes.Expression;

public interface Attribute extends StructuralFeature {
   Expression getInitialValue();

   void setInitialValue(Expression var1);

   java.util.Enumeration getAssociationEndRoleList();

   Set getCollectionAssociationEndRoleList();

   void addAssociationEndRole(AssociationEndRole var1);

   void removeAssociationEndRole(AssociationEndRole var1);

   AssociationEnd getAssociationEnd();

   void setAssociationEnd(AssociationEnd var1);

   java.util.Enumeration getAttributeLinkList();

   Set getCollectionAttributeLinkList();

   void addAttributeLink(AttributeLink var1);

   void removeAttributeLink(AttributeLink var1);
}
