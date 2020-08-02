package ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.otherActions;

import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.PrimitiveAction;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Procedure;

public interface CallProcedureAction extends PrimitiveAction {
   boolean isSynchronous();

   void setSynchronous(boolean var1);

   Procedure getProcedure();

   void setProcedure(Procedure var1);
}
