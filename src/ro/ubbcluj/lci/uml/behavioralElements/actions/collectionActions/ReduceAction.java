package ro.ubbcluj.lci.uml.behavioralElements.actions.collectionActions;

import java.util.Enumeration;
import java.util.Set;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.OutputPin;

public interface ReduceAction extends CollectionAction {
   boolean isUnordered();

   void setUnordered(boolean var1);

   Enumeration getSuboutputList();

   Set getCollectionSuboutputList();

   void addSuboutput(OutputPin var1);

   void removeSuboutput(OutputPin var1);

   Enumeration getLeftSubinputList();

   Set getCollectionLeftSubinputList();

   void addLeftSubinput(OutputPin var1);

   void removeLeftSubinput(OutputPin var1);

   Enumeration getRightSubinputList();

   Set getCollectionRightSubinputList();

   void addRightSubinput(OutputPin var1);

   void removeRightSubinput(OutputPin var1);
}
