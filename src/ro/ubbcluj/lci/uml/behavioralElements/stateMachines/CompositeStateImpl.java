package ro.ubbcluj.lci.uml.behavioralElements.stateMachines;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;

public class CompositeStateImpl extends StateImpl implements CompositeState {
   protected boolean isConcurrent;
   protected boolean isRegion;
   protected Set theSubvertexList;

   public CompositeStateImpl() {
   }

   public boolean isConcurrent() {
      return this.isConcurrent;
   }

   public void setConcurrent(boolean isConcurrent) {
      this.isConcurrent = isConcurrent;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "isConcurrent", 0));
      }

   }

   public boolean isRegion() {
      return this.isRegion;
   }

   public void setRegion(boolean isRegion) {
      this.isRegion = isRegion;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "isRegion", 0));
      }

   }

   public Set getCollectionSubvertexList() {
      return this.theSubvertexList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theSubvertexList);
   }

   public Enumeration getSubvertexList() {
      return Collections.enumeration(this.getCollectionSubvertexList());
   }

   public void addSubvertex(StateVertex arg) {
      if (arg != null) {
         if (this.theSubvertexList == null) {
            this.theSubvertexList = new LinkedHashSet();
         }

         if (this.theSubvertexList.add(arg)) {
            arg.setContainer(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "subvertex", 1));
            }
         }
      }

   }

   public void removeSubvertex(StateVertex arg) {
      if (this.theSubvertexList != null && arg != null && this.theSubvertexList.remove(arg)) {
         arg.setContainer((CompositeState)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "subvertex", 2));
         }
      }

   }

   protected void internalRemove() {
      Enumeration tmpSubvertexEnum = this.getSubvertexList();
      ArrayList tmpSubvertexList = new ArrayList();

      while(tmpSubvertexEnum.hasMoreElements()) {
         tmpSubvertexList.add(tmpSubvertexEnum.nextElement());
      }

      Iterator it = tmpSubvertexList.iterator();

      while(it.hasNext()) {
         ((StateVertex)it.next()).remove();
      }

      super.internalRemove();
   }
}
