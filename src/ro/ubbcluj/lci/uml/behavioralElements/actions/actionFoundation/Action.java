package ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation;

import java.util.Enumeration;
import java.util.Set;
import ro.ubbcluj.lci.codegen.framework.dt.OrderedSet;
import ro.ubbcluj.lci.uml.behavioralElements.actions.compositeActions.GroupAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.jumpActions.JumpHandler;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.Message;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;

public interface Action extends ModelElement {
   boolean isReadOnly();

   void setReadOnly(boolean var1);

   Enumeration getJumpHandlerList();

   Set getCollectionJumpHandlerList();

   void addJumpHandler(JumpHandler var1);

   void removeJumpHandler(JumpHandler var1);

   Enumeration getOutputPinList();

   OrderedSet getCollectionOutputPinList();

   void addOutputPin(OutputPin var1);

   void removeOutputPin(OutputPin var1);

   Enumeration getAntecedentList();

   Set getCollectionAntecedentList();

   void addAntecedent(ControlFlow var1);

   void removeAntecedent(ControlFlow var1);

   Enumeration getInputPinList();

   OrderedSet getCollectionInputPinList();

   void addInputPin(InputPin var1);

   void removeInputPin(InputPin var1);

   Enumeration getConsequentList();

   Set getCollectionConsequentList();

   void addConsequent(ControlFlow var1);

   void removeConsequent(ControlFlow var1);

   Enumeration getMessageList();

   Set getCollectionMessageList();

   void addMessage(Message var1);

   void removeMessage(Message var1);

   GroupAction getGroup();

   void setGroup(GroupAction var1);
}
