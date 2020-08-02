package ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.behavioralElements.actions.compositeActions.LoopAction;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Procedure;

public class OutputPinImpl extends PinImpl implements OutputPin {
   protected Set theFlowList;
   protected LoopAction theLoop;
   protected Action theAction;
   protected Procedure theProcedure;

   public OutputPinImpl() {
   }

   public Set getCollectionFlowList() {
      return this.theFlowList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theFlowList);
   }

   public Enumeration getFlowList() {
      return Collections.enumeration(this.getCollectionFlowList());
   }

   public void addFlow(DataFlow arg) {
      if (arg != null) {
         if (this.theFlowList == null) {
            this.theFlowList = new LinkedHashSet();
         }

         if (this.theFlowList.add(arg)) {
            arg.setSource(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "flow", 1));
            }
         }
      }

   }

   public void removeFlow(DataFlow arg) {
      if (this.theFlowList != null && arg != null && this.theFlowList.remove(arg)) {
         arg.setSource((OutputPin)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "flow", 2));
         }
      }

   }

   public LoopAction getLoop() {
      return this.theLoop;
   }

   public void setLoop(LoopAction arg) {
      if (this.theLoop != arg) {
         LoopAction temp = this.theLoop;
         this.theLoop = null;
         if (temp != null) {
            temp.removeLoopVariable(this);
         }

         if (arg != null) {
            this.theLoop = arg;
            arg.addLoopVariable(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "loop", 0));
         }
      }

   }

   public Action getAction() {
      return this.theAction;
   }

   public void setAction(Action arg) {
      if (this.theAction != arg) {
         Action temp = this.theAction;
         this.theAction = null;
         if (temp != null) {
            temp.removeOutputPin(this);
         }

         if (arg != null) {
            this.theAction = arg;
            arg.addOutputPin(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "action", 0));
         }
      }

   }

   public Procedure getProcedure() {
      return this.theProcedure;
   }

   public void setProcedure(Procedure arg) {
      if (this.theProcedure != arg) {
         Procedure temp = this.theProcedure;
         this.theProcedure = null;
         if (temp != null) {
            temp.removeArgument(this);
         }

         if (arg != null) {
            this.theProcedure = arg;
            arg.addArgument(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "procedure", 0));
         }
      }

   }

   protected void internalRemove() {
      Enumeration tmpFlowEnum = this.getFlowList();
      ArrayList tmpFlowList = new ArrayList();

      while(tmpFlowEnum.hasMoreElements()) {
         tmpFlowList.add(tmpFlowEnum.nextElement());
      }

      Iterator it = tmpFlowList.iterator();

      while(it.hasNext()) {
         ((DataFlow)it.next()).setSource((OutputPin)null);
      }

      LoopAction tmpLoop = this.getLoop();
      if (tmpLoop != null) {
         tmpLoop.removeLoopVariable(this);
      }

      Action tmpAction = this.getAction();
      if (tmpAction != null) {
         tmpAction.removeOutputPin(this);
      }

      Procedure tmpProcedure = this.getProcedure();
      if (tmpProcedure != null) {
         tmpProcedure.removeArgument(this);
      }

      super.internalRemove();
   }
}
