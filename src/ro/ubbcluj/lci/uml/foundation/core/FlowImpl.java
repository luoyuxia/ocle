package ro.ubbcluj.lci.uml.foundation.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;

public class FlowImpl extends RelationshipImpl implements Flow {
   protected Set theSourceList;
   protected Set theTargetList;

   public FlowImpl() {
   }

   public Set getCollectionSourceList() {
      return this.theSourceList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theSourceList);
   }

   public java.util.Enumeration getSourceList() {
      return Collections.enumeration(this.getCollectionSourceList());
   }

   public void addSource(ModelElement arg) {
      if (arg != null) {
         if (this.theSourceList == null) {
            this.theSourceList = new LinkedHashSet();
         }

         if (this.theSourceList.add(arg)) {
            arg.addSourceFlow(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "source", 1));
            }
         }
      }

   }

   public void removeSource(ModelElement arg) {
      if (this.theSourceList != null && arg != null && this.theSourceList.remove(arg)) {
         arg.removeSourceFlow(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "source", 2));
         }
      }

   }

   public Set getCollectionTargetList() {
      return this.theTargetList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theTargetList);
   }

   public java.util.Enumeration getTargetList() {
      return Collections.enumeration(this.getCollectionTargetList());
   }

   public void addTarget(ModelElement arg) {
      if (arg != null) {
         if (this.theTargetList == null) {
            this.theTargetList = new LinkedHashSet();
         }

         if (this.theTargetList.add(arg)) {
            arg.addTargetFlow(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "target", 1));
            }
         }
      }

   }

   public void removeTarget(ModelElement arg) {
      if (this.theTargetList != null && arg != null && this.theTargetList.remove(arg)) {
         arg.removeTargetFlow(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "target", 2));
         }
      }

   }

   protected void internalRemove() {
      java.util.Enumeration tmpSourceEnum = this.getSourceList();
      ArrayList tmpSourceList = new ArrayList();

      while(tmpSourceEnum.hasMoreElements()) {
         tmpSourceList.add(tmpSourceEnum.nextElement());
      }

      Iterator it = tmpSourceList.iterator();

      while(it.hasNext()) {
         ModelElement tmpSource = (ModelElement)it.next();
         tmpSource.removeSourceFlow(this);
      }

      java.util.Enumeration tmpTargetEnum = this.getTargetList();
      ArrayList tmpTargetList = new ArrayList();

      while(tmpTargetEnum.hasMoreElements()) {
         tmpTargetList.add(tmpTargetEnum.nextElement());
      }

       it = tmpTargetList.iterator();

      while(it.hasNext()) {
         ModelElement tmpTarget = (ModelElement)it.next();
         tmpTarget.removeTargetFlow(this);
      }

      super.internalRemove();
   }
}
