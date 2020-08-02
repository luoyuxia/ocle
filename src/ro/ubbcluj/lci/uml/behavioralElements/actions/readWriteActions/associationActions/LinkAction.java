package ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.associationActions;

import java.util.Enumeration;
import java.util.Set;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.PrimitiveAction;

public interface LinkAction extends PrimitiveAction {
   Enumeration getEndDataList();

   Set getCollectionEndDataList();

   void addEndData(LinkEndData var1);

   void removeEndData(LinkEndData var1);
}
