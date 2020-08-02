package ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.attributeActions;

import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.PrimitiveAction;
import ro.ubbcluj.lci.uml.foundation.core.Attribute;

public interface AttributeAction extends PrimitiveAction {
   Attribute getAttribute();

   void setAttribute(Attribute var1);
}
