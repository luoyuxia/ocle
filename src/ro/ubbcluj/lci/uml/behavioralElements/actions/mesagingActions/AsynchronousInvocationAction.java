package ro.ubbcluj.lci.uml.behavioralElements.actions.mesagingActions;

public interface AsynchronousInvocationAction extends InvocationAction {
   boolean isRepliable();

   void setRepliable(boolean var1);
}
