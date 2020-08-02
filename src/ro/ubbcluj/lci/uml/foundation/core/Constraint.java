package ro.ubbcluj.lci.uml.foundation.core;

import ro.ubbcluj.lci.codegen.framework.dt.OrderedSet;
import ro.ubbcluj.lci.uml.foundation.dataTypes.BooleanExpression;
import ro.ubbcluj.lci.uml.foundation.extensionMechanisms.Stereotype;

public interface Constraint extends ModelElement {
   BooleanExpression getBody();

   void setBody(BooleanExpression var1);

   java.util.Enumeration getConstrainedElementList();

   OrderedSet getCollectionConstrainedElementList();

   void addConstrainedElement(ModelElement var1);

   void removeConstrainedElement(ModelElement var1);

   Stereotype getConstrainedStereotype();

   void setConstrainedStereotype(Stereotype var1);
}
