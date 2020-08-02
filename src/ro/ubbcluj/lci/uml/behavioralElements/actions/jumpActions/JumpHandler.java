package ro.ubbcluj.lci.uml.behavioralElements.actions.jumpActions;

import java.util.Enumeration;
import java.util.Set;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.Action;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.core.Element;

public interface JumpHandler extends Element {
   Classifier getJumpType();

   void setJumpType(Classifier var1);

   HandlerAction getBody();

   void setBody(HandlerAction var1);

   Enumeration getProtectedActionList();

   Set getCollectionProtectedActionList();

   void addProtectedAction(Action var1);

   void removeProtectedAction(Action var1);
}
