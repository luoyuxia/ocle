package ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation;

import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.foundation.core.ModelElementImpl;

public class DataFlowImpl extends ModelElementImpl implements DataFlow {
   protected InputPin theDestination;
   protected OutputPin theSource;

   public DataFlowImpl() {
   }

   public InputPin getDestination() {
      return this.theDestination;
   }

   public void setDestination(InputPin arg) {
      if (this.theDestination != arg) {
         InputPin temp = this.theDestination;
         this.theDestination = null;
         if (temp != null) {
            temp.setFlow((DataFlow)null);
         }

         if (arg != null) {
            this.theDestination = arg;
            arg.setFlow(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "destination", 0));
         }
      }

   }

   public OutputPin getSource() {
      return this.theSource;
   }

   public void setSource(OutputPin arg) {
      if (this.theSource != arg) {
         OutputPin temp = this.theSource;
         this.theSource = null;
         if (temp != null) {
            temp.removeFlow(this);
         }

         if (arg != null) {
            this.theSource = arg;
            arg.addFlow(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "source", 0));
         }
      }

   }

   protected void internalRemove() {
      InputPin tmpDestination = this.getDestination();
      if (tmpDestination != null) {
         tmpDestination.setFlow((DataFlow)null);
      }

      OutputPin tmpSource = this.getSource();
      if (tmpSource != null) {
         tmpSource.removeFlow(this);
      }

      super.internalRemove();
   }
}
