package ro.ubbcluj.lci.uml.behavioralElements.commonBehavior;

import java.util.Enumeration;
import ro.ubbcluj.lci.codegen.framework.dt.OrderedSet;
import ro.ubbcluj.lci.uml.foundation.core.AssociationEnd;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;

public interface LinkEnd extends ModelElement {
   Instance getInstance();

   void setInstance(Instance var1);

   AssociationEnd getAssociationEnd();

   void setAssociationEnd(AssociationEnd var1);

   Enumeration getQualifierValueList();

   OrderedSet getCollectionQualifierValueList();

   void addQualifierValue(AttributeLink var1);

   void removeQualifierValue(AttributeLink var1);

   Link getLink();

   void setLink(Link var1);
}
