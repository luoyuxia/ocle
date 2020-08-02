package ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.objectActions;

import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.PrimitiveAction;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;

public interface ReadIsClassifiedObjectAction extends PrimitiveAction {
   boolean isDirect();

   void setDirect(boolean var1);

   Classifier getClassifier();

   void setClassifier(Classifier var1);
}
