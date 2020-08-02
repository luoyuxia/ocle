package ro.ubbcluj.lci.uml.behavioralElements.actions.mesagingActions;

import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Signal;

public interface SendSignalAction extends ExplicitInvocationAction {
   Signal getSignal();

   void setSignal(Signal var1);
}
