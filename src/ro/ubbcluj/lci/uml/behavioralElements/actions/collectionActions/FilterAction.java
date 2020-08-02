package ro.ubbcluj.lci.uml.behavioralElements.actions.collectionActions;

import java.util.Enumeration;
import java.util.Set;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.OutputPin;

public interface FilterAction extends CollectionAction {
   OutputPin getSubtest();

   void setSubtest(OutputPin var1);

   Enumeration getSubinputList();

   Set getCollectionSubinputList();

   void addSubinput(OutputPin var1);

   void removeSubinput(OutputPin var1);
}
