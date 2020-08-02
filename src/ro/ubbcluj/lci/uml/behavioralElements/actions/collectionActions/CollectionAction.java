package ro.ubbcluj.lci.uml.behavioralElements.actions.collectionActions;

import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.Action;

public interface CollectionAction extends Action {
   Action getSubaction();

   void setSubaction(Action var1);
}
