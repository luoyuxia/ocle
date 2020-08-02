package ro.ubbcluj.lci.uml.behavioralElements.actions.mesagingActions;

import ro.ubbcluj.lci.uml.foundation.core.Operation;

public interface CallOperationAction extends ExplicitInvocationAction {
   boolean isAsynchronous();

   void setAsynchronous(boolean var1);

   Operation getOperation();

   void setOperation(Operation var1);
}
