package ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.otherActions;

import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.PrimitiveActionImpl;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;

public class ReadExtentActionImpl extends PrimitiveActionImpl implements ReadExtentAction {
   protected Classifier theClassifier;

   public ReadExtentActionImpl() {
   }

   public Classifier getClassifier() {
      return this.theClassifier;
   }

   public void setClassifier(Classifier arg) {
      this.theClassifier = arg;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "classifier", 0));
      }

   }

   protected void internalRemove() {
      super.internalRemove();
   }
}
