package ro.ubbcluj.lci.uml.behavioralElements.actions.jumpActions;

import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.Action;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.ActionImpl;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.OutputPin;

public class HandlerActionImpl extends ActionImpl implements HandlerAction {
   protected Action theBody;
   protected Set theHandlerOutputList;

   public HandlerActionImpl() {
   }

   public Action getBody() {
      return this.theBody;
   }

   public void setBody(Action arg) {
      this.theBody = arg;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "body", 0));
      }

   }

   public Set getCollectionHandlerOutputList() {
      return this.theHandlerOutputList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theHandlerOutputList);
   }

   public Enumeration getHandlerOutputList() {
      return Collections.enumeration(this.getCollectionHandlerOutputList());
   }

   public void addHandlerOutput(OutputPin arg) {
      if (arg != null) {
         if (this.theHandlerOutputList == null) {
            this.theHandlerOutputList = new LinkedHashSet();
         }

         this.theHandlerOutputList.add(arg);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "handlerOutput", 1));
         }
      }

   }

   public void removeHandlerOutput(OutputPin arg) {
      if (this.theHandlerOutputList != null && arg != null) {
         this.theHandlerOutputList.remove(arg);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "handlerOutput", 2));
         }
      }

   }

   protected void internalRemove() {
      super.internalRemove();
   }
}
