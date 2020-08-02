package ro.ubbcluj.lci.uml.behavioralElements.commonBehavior;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;

public class ComponentInstanceImpl extends InstanceImpl implements ComponentInstance {
   protected NodeInstance theNodeInstance;
   protected Set theResidentList;

   public ComponentInstanceImpl() {
   }

   public NodeInstance getNodeInstance() {
      return this.theNodeInstance;
   }

   public void setNodeInstance(NodeInstance arg) {
      if (this.theNodeInstance != arg) {
         NodeInstance temp = this.theNodeInstance;
         this.theNodeInstance = null;
         if (temp != null) {
            temp.removeResident(this);
         }

         if (arg != null) {
            this.theNodeInstance = arg;
            arg.addResident(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "nodeInstance", 0));
         }
      }

   }

   public Set getCollectionResidentList() {
      return this.theResidentList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theResidentList);
   }

   public Enumeration getResidentList() {
      return Collections.enumeration(this.getCollectionResidentList());
   }

   public void addResident(Instance arg) {
      if (arg != null) {
         if (this.theResidentList == null) {
            this.theResidentList = new LinkedHashSet();
         }

         if (this.theResidentList.add(arg)) {
            arg.setComponentInstance(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "resident", 1));
            }
         }
      }

   }

   public void removeResident(Instance arg) {
      if (this.theResidentList != null && arg != null && this.theResidentList.remove(arg)) {
         arg.setComponentInstance((ComponentInstance)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "resident", 2));
         }
      }

   }

   protected void internalRemove() {
      NodeInstance tmpNodeInstance = this.getNodeInstance();
      if (tmpNodeInstance != null) {
         tmpNodeInstance.removeResident(this);
      }

      Enumeration tmpResidentEnum = this.getResidentList();
      ArrayList tmpResidentList = new ArrayList();

      while(tmpResidentEnum.hasMoreElements()) {
         tmpResidentList.add(tmpResidentEnum.nextElement());
      }

      Iterator it = tmpResidentList.iterator();

      while(it.hasNext()) {
         ((Instance)it.next()).setComponentInstance((ComponentInstance)null);
      }

      super.internalRemove();
   }
}
