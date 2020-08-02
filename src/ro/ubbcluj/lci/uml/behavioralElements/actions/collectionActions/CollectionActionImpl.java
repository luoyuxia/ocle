package ro.ubbcluj.lci.uml.behavioralElements.actions.collectionActions;

import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.Action;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.ActionImpl;

public class CollectionActionImpl extends ActionImpl implements CollectionAction {
   protected Action theSubaction;

   public CollectionActionImpl() {
   }

   public Action getSubaction() {
      return this.theSubaction;
   }

   public void setSubaction(Action arg) {
      this.theSubaction = arg;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "subaction", 0));
      }

   }

   protected void internalRemove() {
      super.internalRemove();
   }
}
