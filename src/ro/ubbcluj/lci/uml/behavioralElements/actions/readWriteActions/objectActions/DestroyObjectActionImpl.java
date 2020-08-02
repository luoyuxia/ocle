package ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.objectActions;

import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.PrimitiveActionImpl;

public class DestroyObjectActionImpl extends PrimitiveActionImpl implements DestroyObjectAction {
   public DestroyObjectActionImpl() {
   }

   protected void internalRemove() {
      super.internalRemove();
   }
}
