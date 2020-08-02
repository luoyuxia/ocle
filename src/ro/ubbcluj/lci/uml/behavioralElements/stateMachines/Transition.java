package ro.ubbcluj.lci.uml.behavioralElements.stateMachines;

import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Procedure;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;

public interface Transition extends ModelElement {
   StateVertex getTarget();

   void setTarget(StateVertex var1);

   StateVertex getSource();

   void setSource(StateVertex var1);

   Guard getGuard();

   void setGuard(Guard var1);

   State getState();

   void setState(State var1);

   Event getTrigger();

   void setTrigger(Event var1);

   StateMachine getStateMachine();

   void setStateMachine(StateMachine var1);

   Procedure getEffect();

   void setEffect(Procedure var1);
}
