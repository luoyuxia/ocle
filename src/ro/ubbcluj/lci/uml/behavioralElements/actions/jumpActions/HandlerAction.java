package ro.ubbcluj.lci.uml.behavioralElements.actions.jumpActions;

import java.util.Enumeration;
import java.util.Set;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.Action;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.OutputPin;

public interface HandlerAction extends Action {
   Action getBody();

   void setBody(Action var1);

   Enumeration getHandlerOutputList();

   Set getCollectionHandlerOutputList();

   void addHandlerOutput(OutputPin var1);

   void removeHandlerOutput(OutputPin var1);
}
