package ro.ubbcluj.lci.uml.foundation.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;

public class NodeImpl extends ClassifierImpl implements Node {
   protected Set theDeployedComponentList;

   public NodeImpl() {
   }

   public Set getCollectionDeployedComponentList() {
      return this.theDeployedComponentList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theDeployedComponentList);
   }

   public java.util.Enumeration getDeployedComponentList() {
      return Collections.enumeration(this.getCollectionDeployedComponentList());
   }

   public void addDeployedComponent(Component arg) {
      if (arg != null) {
         if (this.theDeployedComponentList == null) {
            this.theDeployedComponentList = new LinkedHashSet();
         }

         if (this.theDeployedComponentList.add(arg)) {
            arg.addDeploymentLocation(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "deployedComponent", 1));
            }
         }
      }

   }

   public void removeDeployedComponent(Component arg) {
      if (this.theDeployedComponentList != null && arg != null && this.theDeployedComponentList.remove(arg)) {
         arg.removeDeploymentLocation(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "deployedComponent", 2));
         }
      }

   }

   protected void internalRemove() {
      java.util.Enumeration tmpDeployedComponentEnum = this.getDeployedComponentList();
      ArrayList tmpDeployedComponentList = new ArrayList();

      while(tmpDeployedComponentEnum.hasMoreElements()) {
         tmpDeployedComponentList.add(tmpDeployedComponentEnum.nextElement());
      }

      Iterator it = tmpDeployedComponentList.iterator();

      while(it.hasNext()) {
         Component tmpDeployedComponent = (Component)it.next();
         tmpDeployedComponent.removeDeploymentLocation(this);
      }

      super.internalRemove();
   }
}
