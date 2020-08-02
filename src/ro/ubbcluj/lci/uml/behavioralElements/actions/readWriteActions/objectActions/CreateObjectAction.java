package ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.objectActions;

import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.PrimitiveAction;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;

public interface CreateObjectAction extends PrimitiveAction {
   Classifier getClassifier();

   void setClassifier(Classifier var1);
}
