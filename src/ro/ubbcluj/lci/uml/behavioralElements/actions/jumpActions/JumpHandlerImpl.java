package ro.ubbcluj.lci.uml.behavioralElements.actions.jumpActions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.Action;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.core.ElementImpl;

public class JumpHandlerImpl extends ElementImpl implements JumpHandler {
   protected Classifier theJumpType;
   protected HandlerAction theBody;
   protected Set theProtectedActionList;

   public JumpHandlerImpl() {
   }

   public Classifier getJumpType() {
      return this.theJumpType;
   }

   public void setJumpType(Classifier arg) {
      this.theJumpType = arg;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "jumpType", 0));
      }

   }

   public HandlerAction getBody() {
      return this.theBody;
   }

   public void setBody(HandlerAction arg) {
      this.theBody = arg;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "body", 0));
      }

   }

   public Set getCollectionProtectedActionList() {
      return this.theProtectedActionList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theProtectedActionList);
   }

   public Enumeration getProtectedActionList() {
      return Collections.enumeration(this.getCollectionProtectedActionList());
   }

   public void addProtectedAction(Action arg) {
      if (arg != null) {
         if (this.theProtectedActionList == null) {
            this.theProtectedActionList = new LinkedHashSet();
         }

         if (this.theProtectedActionList.add(arg)) {
            arg.addJumpHandler(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "protectedAction", 1));
            }
         }
      }

   }

   public void removeProtectedAction(Action arg) {
      if (this.theProtectedActionList != null && arg != null && this.theProtectedActionList.remove(arg)) {
         arg.removeJumpHandler(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "protectedAction", 2));
         }
      }

   }

   protected void internalRemove() {
      Enumeration tmpProtectedActionEnum = this.getProtectedActionList();
      ArrayList tmpProtectedActionList = new ArrayList();

      while(tmpProtectedActionEnum.hasMoreElements()) {
         tmpProtectedActionList.add(tmpProtectedActionEnum.nextElement());
      }

      Iterator it = tmpProtectedActionList.iterator();

      while(it.hasNext()) {
         Action tmpProtectedAction = (Action)it.next();
         tmpProtectedAction.removeJumpHandler(this);
      }

      super.internalRemove();
   }
}
