package ro.ubbcluj.lci.uml.behavioralElements.commonBehavior;

import ro.ubbcluj.lci.uml.foundation.core.Attribute;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;

public interface AttributeLink extends ModelElement {
   Instance getValue();

   void setValue(Instance var1);

   Attribute getAttribute();

   void setAttribute(Attribute var1);

   Instance getInstance();

   void setInstance(Instance var1);

   LinkEnd getLinkEnd();

   void setLinkEnd(LinkEnd var1);
}
