package ro.ubbcluj.lci.uml.behavioralElements.stateMachines;

import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Signal;

public interface SignalEvent extends Event {
   Signal getSignal();

   void setSignal(Signal var1);
}
