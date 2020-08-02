package ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.variableActions;

import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.PrimitiveAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.compositeActions.Variable;

public interface VariableAction extends PrimitiveAction {
   Variable getVariable();

   void setVariable(Variable var1);
}
