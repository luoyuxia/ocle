package ro.ubbcluj.lci.uml.behavioralElements.actions.collectionActions;

import java.util.Enumeration;
import java.util.Set;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.OutputPin;

public interface MapAction extends CollectionAction {
   Enumeration getSubinputList();

   Set getCollectionSubinputList();

   void addSubinput(OutputPin var1);

   void removeSubinput(OutputPin var1);

   Enumeration getSuboutputList();

   Set getCollectionSuboutputList();

   void addSuboutput(OutputPin var1);

   void removeSuboutput(OutputPin var1);
}
