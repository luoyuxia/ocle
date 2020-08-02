package ro.ubbcluj.lci.uml.behavioralElements.actions.mesagingActions;

import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;

public class AsynchronousInvocationActionImpl extends InvocationActionImpl implements AsynchronousInvocationAction {
   protected boolean isRepliable;

   public AsynchronousInvocationActionImpl() {
   }

   public boolean isRepliable() {
      return this.isRepliable;
   }

   public void setRepliable(boolean isRepliable) {
      this.isRepliable = isRepliable;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "isRepliable", 0));
      }

   }

   protected void internalRemove() {
      super.internalRemove();
   }
}
