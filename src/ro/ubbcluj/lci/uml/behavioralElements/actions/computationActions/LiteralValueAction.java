package ro.ubbcluj.lci.uml.behavioralElements.actions.computationActions;

import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.PrimitiveAction;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.DataValue;

public interface LiteralValueAction extends PrimitiveAction {
   DataValue getValue();

   void setValue(DataValue var1);
}
