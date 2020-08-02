package ro.ubbcluj.lci.uml.behavioralElements.commonBehavior;

import java.util.Enumeration;
import java.util.Set;
import ro.ubbcluj.lci.codegen.framework.dt.OrderedSet;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.Action;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.InputPin;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.OutputPin;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.Message;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.State;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.Transition;
import ro.ubbcluj.lci.uml.foundation.core.Method;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.uml.foundation.dataTypes.Expression;

public interface Procedure extends ModelElement {
   String getLanguage();

   void setLanguage(String var1);

   String getBody();

   void setBody(String var1);

   boolean isList();

   void setList(boolean var1);

   State getState3();

   void setState3(State var1);

   Enumeration getResultList();

   OrderedSet getCollectionResultList();

   void addResult(InputPin var1);

   void removeResult(InputPin var1);

   Enumeration getMessageList();

   Set getCollectionMessageList();

   void addMessage(Message var1);

   void removeMessage(Message var1);

   Action getAction();

   void setAction(Action var1);

   Transition getTransition();

   void setTransition(Transition var1);

   State getState1();

   void setState1(State var1);

   Enumeration getExpressionList();

   Set getCollectionExpressionList();

   void addExpression(Expression var1);

   void removeExpression(Expression var1);

   Enumeration getArgumentList();

   OrderedSet getCollectionArgumentList();

   void addArgument(OutputPin var1);

   void removeArgument(OutputPin var1);

   Enumeration getMethodList();

   Set getCollectionMethodList();

   void addMethod(Method var1);

   void removeMethod(Method var1);

   Enumeration getStimulusList();

   Set getCollectionStimulusList();

   void addStimulus(Stimulus var1);

   void removeStimulus(Stimulus var1);

   State getState2();

   void setState2(State var1);
}
