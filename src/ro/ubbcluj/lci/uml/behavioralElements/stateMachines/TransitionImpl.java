package ro.ubbcluj.lci.uml.behavioralElements.stateMachines;

import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Procedure;
import ro.ubbcluj.lci.uml.foundation.core.ModelElementImpl;

public class TransitionImpl extends ModelElementImpl implements Transition {
   protected StateVertex theTarget;
   protected StateVertex theSource;
   protected Guard theGuard;
   protected State theState;
   protected Event theTrigger;
   protected StateMachine theStateMachine;
   protected Procedure theEffect;

   public TransitionImpl() {
   }

   public StateVertex getTarget() {
      return this.theTarget;
   }

   public void setTarget(StateVertex arg) {
      if (this.theTarget != arg) {
         StateVertex temp = this.theTarget;
         this.theTarget = null;
         if (temp != null) {
            temp.removeIncoming(this);
         }

         if (arg != null) {
            this.theTarget = arg;
            arg.addIncoming(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "target", 0));
         }
      }

   }

   public StateVertex getSource() {
      return this.theSource;
   }

   public void setSource(StateVertex arg) {
      if (this.theSource != arg) {
         StateVertex temp = this.theSource;
         this.theSource = null;
         if (temp != null) {
            temp.removeOutgoing(this);
         }

         if (arg != null) {
            this.theSource = arg;
            arg.addOutgoing(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "source", 0));
         }
      }

   }

   public Guard getGuard() {
      return this.theGuard;
   }

   public void setGuard(Guard arg) {
      if (this.theGuard != arg) {
         Guard temp = this.theGuard;
         this.theGuard = null;
         if (temp != null) {
            temp.setTransition((Transition)null);
         }

         if (arg != null) {
            this.theGuard = arg;
            arg.setTransition(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "guard", 0));
         }
      }

   }

   public State getState() {
      return this.theState;
   }

   public void setState(State arg) {
      if (this.theState != arg) {
         State temp = this.theState;
         this.theState = null;
         if (temp != null) {
            temp.removeInternalTransition(this);
         }

         if (arg != null) {
            this.theState = arg;
            arg.addInternalTransition(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "state", 0));
         }
      }

   }

   public Event getTrigger() {
      return this.theTrigger;
   }

   public void setTrigger(Event arg) {
      if (this.theTrigger != arg) {
         Event temp = this.theTrigger;
         this.theTrigger = null;
         if (temp != null) {
            temp.removeTransition(this);
         }

         if (arg != null) {
            this.theTrigger = arg;
            arg.addTransition(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "trigger", 0));
         }
      }

   }

   public StateMachine getStateMachine() {
      return this.theStateMachine;
   }

   public void setStateMachine(StateMachine arg) {
      if (this.theStateMachine != arg) {
         StateMachine temp = this.theStateMachine;
         this.theStateMachine = null;
         if (temp != null) {
            temp.removeTransitions(this);
         }

         if (arg != null) {
            this.theStateMachine = arg;
            arg.addTransitions(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "stateMachine", 0));
         }
      }

   }

   public Procedure getEffect() {
      return this.theEffect;
   }

   public void setEffect(Procedure arg) {
      if (this.theEffect != arg) {
         Procedure temp = this.theEffect;
         this.theEffect = null;
         if (temp != null) {
            temp.setTransition((Transition)null);
         }

         if (arg != null) {
            this.theEffect = arg;
            arg.setTransition(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "effect", 0));
         }
      }

   }

   protected void internalRemove() {
      StateVertex tmpTarget = this.getTarget();
      if (tmpTarget != null) {
         tmpTarget.removeIncoming(this);
      }

      StateVertex tmpSource = this.getSource();
      if (tmpSource != null) {
         tmpSource.removeOutgoing(this);
      }

      Guard tmpGuard = this.getGuard();
      if (tmpGuard != null) {
         tmpGuard.remove();
      }

      State tmpState = this.getState();
      if (tmpState != null) {
         tmpState.removeInternalTransition(this);
      }

      Event tmpTrigger = this.getTrigger();
      if (tmpTrigger != null) {
         tmpTrigger.removeTransition(this);
      }

      StateMachine tmpStateMachine = this.getStateMachine();
      if (tmpStateMachine != null) {
         tmpStateMachine.removeTransitions(this);
      }

      Procedure tmpEffect = this.getEffect();
      if (tmpEffect != null) {
         tmpEffect.remove();
      }

      super.internalRemove();
   }
}
