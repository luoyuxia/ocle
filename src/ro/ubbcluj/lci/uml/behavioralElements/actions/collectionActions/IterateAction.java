package ro.ubbcluj.lci.uml.behavioralElements.actions.collectionActions;

import java.util.Enumeration;
import java.util.Set;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.OutputPin;

public interface IterateAction extends CollectionAction {
   boolean isUnordered();

   void setUnordered(boolean var1);

   Enumeration getSuboutputList();

   Set getCollectionSuboutputList();

   void addSuboutput(OutputPin var1);

   void removeSuboutput(OutputPin var1);

   Enumeration getSubinputList();

   Set getCollectionSubinputList();

   void addSubinput(OutputPin var1);

   void removeSubinput(OutputPin var1);

   Enumeration getLoopVariableList();

   Set getCollectionLoopVariableList();

   void addLoopVariable(OutputPin var1);

   void removeLoopVariable(OutputPin var1);
}
