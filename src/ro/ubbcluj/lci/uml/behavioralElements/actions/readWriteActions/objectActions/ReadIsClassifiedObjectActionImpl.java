package ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.objectActions;

import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.PrimitiveActionImpl;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;

public class ReadIsClassifiedObjectActionImpl extends PrimitiveActionImpl implements ReadIsClassifiedObjectAction {
   protected boolean isDirect;
   protected Classifier theClassifier;

   public ReadIsClassifiedObjectActionImpl() {
   }

   public boolean isDirect() {
      return this.isDirect;
   }

   public void setDirect(boolean isDirect) {
      this.isDirect = isDirect;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "isDirect", 0));
      }

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
