package ro.ubbcluj.lci.uml.behavioralElements.actions.computationActions;

import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.PrimitiveActionImpl;

public class NullActionImpl extends PrimitiveActionImpl implements NullAction {
   public NullActionImpl() {
   }

   protected void internalRemove() {
      super.internalRemove();
   }
}
