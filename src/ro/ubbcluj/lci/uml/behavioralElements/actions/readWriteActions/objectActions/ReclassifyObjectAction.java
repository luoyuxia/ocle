package ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.objectActions;

import java.util.Enumeration;
import java.util.Set;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.PrimitiveAction;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;

public interface ReclassifyObjectAction extends PrimitiveAction {
   boolean isReplaceAll();

   void setReplaceAll(boolean var1);

   Enumeration getNewClassifierList();

   Set getCollectionNewClassifierList();

   void addNewClassifier(Classifier var1);

   void removeNewClassifier(Classifier var1);

   Enumeration getOldClassifierList();

   Set getCollectionOldClassifierList();

   void addOldClassifier(Classifier var1);

   void removeOldClassifier(Classifier var1);
}
