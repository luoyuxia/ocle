package ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation;

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
import ro.ubbcluj.lci.uml.behavioralElements.actions.compositeActions.GroupAction;
import ro.ubbcluj.lci.uml.behavioralElements.actions.jumpActions.JumpHandler;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.Message;
import ro.ubbcluj.lci.uml.foundation.core.ModelElementImpl;

public class ActionImpl extends ModelElementImpl implements Action {
   protected boolean isReadOnly;
   protected Set theJumpHandlerList;
   protected OrderedSet theOutputPinList;
   protected Set theAntecedentList;
   protected OrderedSet theInputPinList;
   protected Set theConsequentList;
   protected Set theMessageList;
   protected GroupAction theGroup;

   public ActionImpl() {
   }

   public boolean isReadOnly() {
      return this.isReadOnly;
   }

   public void setReadOnly(boolean isReadOnly) {
      this.isReadOnly = isReadOnly;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "isReadOnly", 0));
      }

   }

   public Set getCollectionJumpHandlerList() {
      return this.theJumpHandlerList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theJumpHandlerList);
   }

   public Enumeration getJumpHandlerList() {
      return Collections.enumeration(this.getCollectionJumpHandlerList());
   }

   public void addJumpHandler(JumpHandler arg) {
      if (arg != null) {
         if (this.theJumpHandlerList == null) {
            this.theJumpHandlerList = new LinkedHashSet();
         }

         if (this.theJumpHandlerList.add(arg)) {
            arg.addProtectedAction(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "jumpHandler", 1));
            }
         }
      }

   }

   public void removeJumpHandler(JumpHandler arg) {
      if (this.theJumpHandlerList != null && arg != null && this.theJumpHandlerList.remove(arg)) {
         arg.removeProtectedAction(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "jumpHandler", 2));
         }
      }

   }

   public OrderedSet getCollectionOutputPinList() {
      return this.theOutputPinList == null ? CollectionUtilities.newOrderedSet() : CollectionUtilities.newOrderedSet(this.theOutputPinList);
   }

   public Enumeration getOutputPinList() {
      return Collections.enumeration(this.getCollectionOutputPinList());
   }

   public void addOutputPin(OutputPin arg) {
      if (arg != null) {
         if (this.theOutputPinList == null) {
            this.theOutputPinList = new OrderedSet();
         }

         if (this.theOutputPinList.add(arg)) {
            arg.setAction(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "outputPin", 1));
            }
         }
      }

   }

   public void removeOutputPin(OutputPin arg) {
      if (this.theOutputPinList != null && arg != null && this.theOutputPinList.remove(arg)) {
         arg.setAction((Action)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "outputPin", 2));
         }
      }

   }

   public Set getCollectionAntecedentList() {
      return this.theAntecedentList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theAntecedentList);
   }

   public Enumeration getAntecedentList() {
      return Collections.enumeration(this.getCollectionAntecedentList());
   }

   public void addAntecedent(ControlFlow arg) {
      if (arg != null) {
         if (this.theAntecedentList == null) {
            this.theAntecedentList = new LinkedHashSet();
         }

         if (this.theAntecedentList.add(arg)) {
            arg.setSuccessor(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "antecedent", 1));
            }
         }
      }

   }

   public void removeAntecedent(ControlFlow arg) {
      if (this.theAntecedentList != null && arg != null && this.theAntecedentList.remove(arg)) {
         arg.setSuccessor((Action)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "antecedent", 2));
         }
      }

   }

   public OrderedSet getCollectionInputPinList() {
      return this.theInputPinList == null ? CollectionUtilities.newOrderedSet() : CollectionUtilities.newOrderedSet(this.theInputPinList);
   }

   public Enumeration getInputPinList() {
      return Collections.enumeration(this.getCollectionInputPinList());
   }

   public void addInputPin(InputPin arg) {
      if (arg != null) {
         if (this.theInputPinList == null) {
            this.theInputPinList = new OrderedSet();
         }

         if (this.theInputPinList.add(arg)) {
            arg.setAction(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "inputPin", 1));
            }
         }
      }

   }

   public void removeInputPin(InputPin arg) {
      if (this.theInputPinList != null && arg != null && this.theInputPinList.remove(arg)) {
         arg.setAction((Action)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "inputPin", 2));
         }
      }

   }

   public Set getCollectionConsequentList() {
      return this.theConsequentList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theConsequentList);
   }

   public Enumeration getConsequentList() {
      return Collections.enumeration(this.getCollectionConsequentList());
   }

   public void addConsequent(ControlFlow arg) {
      if (arg != null) {
         if (this.theConsequentList == null) {
            this.theConsequentList = new LinkedHashSet();
         }

         if (this.theConsequentList.add(arg)) {
            arg.setPredecessor(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "consequent", 1));
            }
         }
      }

   }

   public void removeConsequent(ControlFlow arg) {
      if (this.theConsequentList != null && arg != null && this.theConsequentList.remove(arg)) {
         arg.setPredecessor((Action)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "consequent", 2));
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
            arg.setAction(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "message", 1));
            }
         }
      }

   }

   public void removeMessage(Message arg) {
      if (this.theMessageList != null && arg != null && this.theMessageList.remove(arg)) {
         arg.setAction((Action)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "message", 2));
         }
      }

   }

   public GroupAction getGroup() {
      return this.theGroup;
   }

   public void setGroup(GroupAction arg) {
      if (this.theGroup != arg) {
         GroupAction temp = this.theGroup;
         this.theGroup = null;
         if (temp != null) {
            temp.removeSubaction(this);
         }

         if (arg != null) {
            this.theGroup = arg;
            arg.addSubaction(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "group", 0));
         }
      }

   }

   protected void internalRemove() {
      Enumeration tmpJumpHandlerEnum = this.getJumpHandlerList();
      ArrayList tmpJumpHandlerList = new ArrayList();

      while(tmpJumpHandlerEnum.hasMoreElements()) {
         tmpJumpHandlerList.add(tmpJumpHandlerEnum.nextElement());
      }

      Iterator it = tmpJumpHandlerList.iterator();

      while(it.hasNext()) {
         JumpHandler tmpJumpHandler = (JumpHandler)it.next();
         tmpJumpHandler.removeProtectedAction(this);
      }

      Enumeration tmpOutputPinEnum = this.getOutputPinList();
      ArrayList tmpOutputPinList = new ArrayList();

      while(tmpOutputPinEnum.hasMoreElements()) {
         tmpOutputPinList.add(tmpOutputPinEnum.nextElement());
      }

       it = tmpOutputPinList.iterator();

      while(it.hasNext()) {
         ((OutputPin)it.next()).remove();
      }

      Enumeration tmpAntecedentEnum = this.getAntecedentList();
      ArrayList tmpAntecedentList = new ArrayList();

      while(tmpAntecedentEnum.hasMoreElements()) {
         tmpAntecedentList.add(tmpAntecedentEnum.nextElement());
      }

       it = tmpAntecedentList.iterator();

      while(it.hasNext()) {
         ((ControlFlow)it.next()).setSuccessor((Action)null);
      }

      Enumeration tmpInputPinEnum = this.getInputPinList();
      ArrayList tmpInputPinList = new ArrayList();

      while(tmpInputPinEnum.hasMoreElements()) {
         tmpInputPinList.add(tmpInputPinEnum.nextElement());
      }

       it = tmpInputPinList.iterator();

      while(it.hasNext()) {
         ((InputPin)it.next()).remove();
      }

      Enumeration tmpConsequentEnum = this.getConsequentList();
      ArrayList tmpConsequentList = new ArrayList();

      while(tmpConsequentEnum.hasMoreElements()) {
         tmpConsequentList.add(tmpConsequentEnum.nextElement());
      }

       it = tmpConsequentList.iterator();

      while(it.hasNext()) {
         ((ControlFlow)it.next()).setPredecessor((Action)null);
      }

      Enumeration tmpMessageEnum = this.getMessageList();
      ArrayList tmpMessageList = new ArrayList();

      while(tmpMessageEnum.hasMoreElements()) {
         tmpMessageList.add(tmpMessageEnum.nextElement());
      }

       it = tmpMessageList.iterator();

      while(it.hasNext()) {
         ((Message)it.next()).setAction((Action)null);
      }

      GroupAction tmpGroup = this.getGroup();
      if (tmpGroup != null) {
         tmpGroup.removeSubaction(this);
      }

      super.internalRemove();
   }
}
