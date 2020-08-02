package ro.ubbcluj.lci.uml.behavioralElements.commonBehavior;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import ro.ubbcluj.lci.codegen.framework.dt.OrderedSet;
import ro.ubbcluj.lci.codegen.framework.ocl.CollectionUtilities;
import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.Action;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.InputPin;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.OutputPin;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.Message;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.State;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.Transition;
import ro.ubbcluj.lci.uml.foundation.core.Method;
import ro.ubbcluj.lci.uml.foundation.core.ModelElementImpl;
import ro.ubbcluj.lci.uml.foundation.dataTypes.Expression;

public class ProcedureImpl extends ModelElementImpl implements Procedure {
   protected String theLanguage;
   protected String theBody;
   protected boolean isList;
   protected State theState3;
   protected OrderedSet theResultList;
   protected Set theMessageList;
   protected Action theAction;
   protected Transition theTransition;
   protected State theState1;
   protected Set theExpressionList;
   protected OrderedSet theArgumentList;
   protected Set theMethodList;
   protected Set theStimulusList;
   protected State theState2;

   public ProcedureImpl() {
   }

   public String getLanguage() {
      return this.theLanguage;
   }

   public void setLanguage(String language) {
      this.theLanguage = language;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "language", 0));
      }

   }

   public String getBody() {
      return this.theBody;
   }

   public void setBody(String body) {
      this.theBody = body;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "body", 0));
      }

   }

   public boolean isList() {
      return this.isList;
   }

   public void setList(boolean isList) {
      this.isList = isList;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "isList", 0));
      }

   }

   public State getState3() {
      return this.theState3;
   }

   public void setState3(State arg) {
      if (this.theState3 != arg) {
         State temp = this.theState3;
         this.theState3 = null;
         if (temp != null) {
            temp.setDoActivity((Procedure)null);
         }

         if (arg != null) {
            this.theState3 = arg;
            arg.setDoActivity(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "state3", 0));
         }
      }

   }

   public OrderedSet getCollectionResultList() {
      return this.theResultList == null ? CollectionUtilities.newOrderedSet() : CollectionUtilities.newOrderedSet(this.theResultList);
   }

   public Enumeration getResultList() {
      return Collections.enumeration(this.getCollectionResultList());
   }

   public void addResult(InputPin arg) {
      if (arg != null) {
         if (this.theResultList == null) {
            this.theResultList = new OrderedSet();
         }

         if (this.theResultList.add(arg)) {
            arg.setProcedure(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "result", 1));
            }
         }
      }

   }

   public void removeResult(InputPin arg) {
      if (this.theResultList != null && arg != null && this.theResultList.remove(arg)) {
         arg.setProcedure((Procedure)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "result", 2));
         }
      }

   }

   public Set getCollectionMessageList() {
      return this.theMessageList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theMessageList);
   }

   public Enumeration getMessageList() {
      return Collections.enumeration(this.getCollectionMessageList());
   }

   public void addMessage(Message arg) {
      if (arg != null) {
         if (this.theMessageList == null) {
            this.theMessageList = new LinkedHashSet();
         }

         if (this.theMessageList.add(arg)) {
            arg.setProcedure(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "message", 1));
            }
         }
      }

   }

   public void removeMessage(Message arg) {
      if (this.theMessageList != null && arg != null && this.theMessageList.remove(arg)) {
         arg.setProcedure((Procedure)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "message", 2));
         }
      }

   }

   public Action getAction() {
      return this.theAction;
   }

   public void setAction(Action arg) {
      this.theAction = arg;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "action", 0));
      }

   }

   public Transition getTransition() {
      return this.theTransition;
   }

   public void setTransition(Transition arg) {
      if (this.theTransition != arg) {
         Transition temp = this.theTransition;
         this.theTransition = null;
         if (temp != null) {
            temp.setEffect((Procedure)null);
         }

         if (arg != null) {
            this.theTransition = arg;
            arg.setEffect(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "transition", 0));
         }
      }

   }

   public State getState1() {
      return this.theState1;
   }

   public void setState1(State arg) {
      if (this.theState1 != arg) {
         State temp = this.theState1;
         this.theState1 = null;
         if (temp != null) {
            temp.setEntry((Procedure)null);
         }

         if (arg != null) {
            this.theState1 = arg;
            arg.setEntry(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "state1", 0));
         }
      }

   }

   public Set getCollectionExpressionList() {
      return this.theExpressionList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theExpressionList);
   }

   public Enumeration getExpressionList() {
      return Collections.enumeration(this.getCollectionExpressionList());
   }

   public void addExpression(Expression arg) {
      if (arg != null) {
         if (this.theExpressionList == null) {
            this.theExpressionList = new LinkedHashSet();
         }

         if (this.theExpressionList.add(arg)) {
            arg.setProcedure(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "expression", 1));
            }
         }
      }

   }

   public void removeExpression(Expression arg) {
      if (this.theExpressionList != null && arg != null && this.theExpressionList.remove(arg)) {
         arg.setProcedure((Procedure)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "expression", 2));
         }
      }

   }

   public OrderedSet getCollectionArgumentList() {
      return this.theArgumentList == null ? CollectionUtilities.newOrderedSet() : CollectionUtilities.newOrderedSet(this.theArgumentList);
   }

   public Enumeration getArgumentList() {
      return Collections.enumeration(this.getCollectionArgumentList());
   }

   public void addArgument(OutputPin arg) {
      if (arg != null) {
         if (this.theArgumentList == null) {
            this.theArgumentList = new OrderedSet();
         }

         if (this.theArgumentList.add(arg)) {
            arg.setProcedure(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "argument", 1));
            }
         }
      }

   }

   public void removeArgument(OutputPin arg) {
      if (this.theArgumentList != null && arg != null && this.theArgumentList.remove(arg)) {
         arg.setProcedure((Procedure)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "argument", 2));
         }
      }

   }

   public Set getCollectionMethodList() {
      return this.theMethodList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theMethodList);
   }

   public Enumeration getMethodList() {
      return Collections.enumeration(this.getCollectionMethodList());
   }

   public void addMethod(Method arg) {
      if (arg != null) {
         if (this.theMethodList == null) {
            this.theMethodList = new LinkedHashSet();
         }

         if (this.theMethodList.add(arg)) {
            arg.setProcedure(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "method", 1));
            }
         }
      }

   }

   public void removeMethod(Method arg) {
      if (this.theMethodList != null && arg != null && this.theMethodList.remove(arg)) {
         arg.setProcedure((Procedure)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "method", 2));
         }
      }

   }

   public Set getCollectionStimulusList() {
      return this.theStimulusList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theStimulusList);
   }

   public Enumeration getStimulusList() {
      return Collections.enumeration(this.getCollectionStimulusList());
   }

   public void addStimulus(Stimulus arg) {
      if (arg != null) {
         if (this.theStimulusList == null) {
            this.theStimulusList = new LinkedHashSet();
         }

         if (this.theStimulusList.add(arg)) {
            arg.setDispatchAction(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "stimulus", 1));
            }
         }
      }

   }

   public void removeStimulus(Stimulus arg) {
      if (this.theStimulusList != null && arg != null && this.theStimulusList.remove(arg)) {
         arg.setDispatchAction((Procedure)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "stimulus", 2));
         }
      }

   }

   public State getState2() {
      return this.theState2;
   }

   public void setState2(State arg) {
      if (this.theState2 != arg) {
         State temp = this.theState2;
         this.theState2 = null;
         if (temp != null) {
            temp.setExit((Procedure)null);
         }

         if (arg != null) {
            this.theState2 = arg;
            arg.setExit(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "state2", 0));
         }
      }

   }

   protected void internalRemove() {
      State tmpState3 = this.getState3();
      if (tmpState3 != null) {
         tmpState3.setDoActivity((Procedure)null);
      }

      Enumeration tmpResultEnum = this.getResultList();
      ArrayList tmpResultList = new ArrayList();

      while(tmpResultEnum.hasMoreElements()) {
         tmpResultList.add(tmpResultEnum.nextElement());
      }

      Iterator it = tmpResultList.iterator();

      while(it.hasNext()) {
         ((InputPin)it.next()).remove();
      }

      Enumeration tmpMessageEnum = this.getMessageList();
      ArrayList tmpMessageList = new ArrayList();

      while(tmpMessageEnum.hasMoreElements()) {
         tmpMessageList.add(tmpMessageEnum.nextElement());
      }

       it = tmpMessageList.iterator();

      while(it.hasNext()) {
         ((Message)it.next()).setProcedure((Procedure)null);
      }

      Transition tmpTransition = this.getTransition();
      if (tmpTransition != null) {
         tmpTransition.setEffect((Procedure)null);
      }

      State tmpState1 = this.getState1();
      if (tmpState1 != null) {
         tmpState1.setEntry((Procedure)null);
      }

      Enumeration tmpExpressionEnum = this.getExpressionList();
      ArrayList tmpExpressionList = new ArrayList();

      while(tmpExpressionEnum.hasMoreElements()) {
         tmpExpressionList.add(tmpExpressionEnum.nextElement());
      }

       it = tmpExpressionList.iterator();

      while(it.hasNext()) {
         ((Expression)it.next()).setProcedure((Procedure)null);
      }

      Enumeration tmpArgumentEnum = this.getArgumentList();
      ArrayList tmpArgumentList = new ArrayList();

      while(tmpArgumentEnum.hasMoreElements()) {
         tmpArgumentList.add(tmpArgumentEnum.nextElement());
      }

       it = tmpArgumentList.iterator();

      while(it.hasNext()) {
         ((OutputPin)it.next()).remove();
      }

      Enumeration tmpMethodEnum = this.getMethodList();
      ArrayList tmpMethodList = new ArrayList();

      while(tmpMethodEnum.hasMoreElements()) {
         tmpMethodList.add(tmpMethodEnum.nextElement());
      }

       it = tmpMethodList.iterator();

      while(it.hasNext()) {
         ((Method)it.next()).setProcedure((Procedure)null);
      }

      Enumeration tmpStimulusEnum = this.getStimulusList();
      ArrayList tmpStimulusList = new ArrayList();

      while(tmpStimulusEnum.hasMoreElements()) {
         tmpStimulusList.add(tmpStimulusEnum.nextElement());
      }

       it = tmpStimulusList.iterator();

      while(it.hasNext()) {
         ((Stimulus)it.next()).setDispatchAction((Procedure)null);
      }

      State tmpState2 = this.getState2();
      if (tmpState2 != null) {
         tmpState2.setExit((Procedure)null);
      }

      super.internalRemove();
   }
}
