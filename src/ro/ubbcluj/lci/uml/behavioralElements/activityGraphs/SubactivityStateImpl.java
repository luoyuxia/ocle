package ro.ubbcluj.lci.uml.behavioralElements.activityGraphs;

import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.SubmachineStateImpl;
import ro.ubbcluj.lci.uml.foundation.dataTypes.ArgListsExpression;
import ro.ubbcluj.lci.uml.foundation.dataTypes.Multiplicity;

public class SubactivityStateImpl extends SubmachineStateImpl implements SubactivityState {
   protected boolean isDynamic;
   protected Multiplicity theDynamicMultiplicity;
   protected ArgListsExpression theDynamicArguments;

   public SubactivityStateImpl() {
   }

   public boolean isDynamic() {
      return this.isDynamic;
   }

   public void setDynamic(boolean isDynamic) {
      this.isDynamic = isDynamic;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "isDynamic", 0));
      }

   }

   public Multiplicity getDynamicMultiplicity() {
      return this.theDynamicMultiplicity;
   }

   public void setDynamicMultiplicity(Multiplicity dynamicMultiplicity) {
      this.theDynamicMultiplicity = dynamicMultiplicity;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "dynamicMultiplicity", 0));
      }

   }

   public ArgListsExpression getDynamicArguments() {
      return this.theDynamicArguments;
   }

   public void setDynamicArguments(ArgListsExpression dynamicArguments) {
      this.theDynamicArguments = dynamicArguments;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "dynamicArguments", 0));
      }

   }

   protected void internalRemove() {
      super.internalRemove();
   }
}
