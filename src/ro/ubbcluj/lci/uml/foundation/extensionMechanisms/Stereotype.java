package ro.ubbcluj.lci.uml.foundation.extensionMechanisms;

import java.util.Enumeration;
import java.util.Set;
import ro.ubbcluj.lci.uml.foundation.core.Constraint;
import ro.ubbcluj.lci.uml.foundation.core.GeneralizableElement;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.uml.foundation.dataTypes.Geometry;

public interface Stereotype extends StereotypeKind, GeneralizableElement {
   Set getCollectionBaseClassList();

   String getBaseClass();

   void setBaseClass(String var1);

   Geometry getIcon();

   void setIcon(Geometry var1);

   Enumeration getExtendedElementList();

   Set getCollectionExtendedElementList();

   void addExtendedElement(ModelElement var1);

   void removeExtendedElement(ModelElement var1);

   Enumeration getStereotypeConstraintList();

   Set getCollectionStereotypeConstraintList();

   void addStereotypeConstraint(Constraint var1);

   void removeStereotypeConstraint(Constraint var1);

   Enumeration getDefinedTagList();

   Set getCollectionDefinedTagList();

   void addDefinedTag(TagDefinition var1);

   void removeDefinedTag(TagDefinition var1);

   Set findProfile(ModelElement var1);
}
