package ro.ubbcluj.lci.uml.behavioralElements.actions.computationActions;

import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.PrimitiveAction;

public interface ApplyFunctionAction extends PrimitiveAction {
   PrimitiveFunction getFunction();

   void setFunction(PrimitiveFunction var1);
}
