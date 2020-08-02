package ro.ubbcluj.lci.uml.behavioralElements.stateMachines;

public interface SubmachineState extends CompositeState {
   StateMachine getSubmachine();

   void setSubmachine(StateMachine var1);
}
