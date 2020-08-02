package ro.ubbcluj.lci.uml.behavioralElements.commonBehavior;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;

public class NodeInstanceImpl extends InstanceImpl implements NodeInstance {
   protected Set theResidentList;

   public NodeInstanceImpl() {
   }

   public Set getCollectionResidentList() {
      return this.theResidentList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theResidentList);
   }

   public Enumeration getResidentList() {
      return Collections.enumeration(this.getCollectionResidentList());
   }

   public void addResident(ComponentInstance arg) {
      if (arg != null) {
         if (this.theResidentList == null) {
            this.theResidentList = new LinkedHashSet();
         }

         if (this.theResidentList.add(arg)) {
            arg.setNodeInstance(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "resident", 1));
            }
         }
      }

   }

   public void removeResident(ComponentInstance arg) {
      if (this.theResidentList != null && arg != null && this.theResidentList.remove(arg)) {
         arg.setNodeInstance((NodeInstance)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "resident", 2));
         }
      }

   }

   protected void internalRemove() {
      Enumeration tmpResidentEnum = this.getResidentList();
      ArrayList tmpResidentList = new ArrayList();

      while(tmpResidentEnum.hasMoreElements()) {
         tmpResidentList.add(tmpResidentEnum.nextElement());
      }

      Iterator it = tmpResidentList.iterator();

      while(it.hasNext()) {
         ((ComponentInstance)it.next()).setNodeInstance((NodeInstance)null);
      }

      super.internalRemove();
   }
}
