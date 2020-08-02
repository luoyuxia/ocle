package ro.ubbcluj.lci.uml.foundation.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;

public class DependencyImpl extends RelationshipImpl implements Dependency {
   protected Set theSupplierList;
   protected Set theClientList;

   public DependencyImpl() {
   }

   public Set getCollectionSupplierList() {
      return this.theSupplierList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theSupplierList);
   }

   public java.util.Enumeration getSupplierList() {
      return Collections.enumeration(this.getCollectionSupplierList());
   }

   public void addSupplier(ModelElement arg) {
      if (arg != null) {
         if (this.theSupplierList == null) {
            this.theSupplierList = new LinkedHashSet();
         }

         if (this.theSupplierList.add(arg)) {
            arg.addSupplierDependency(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "supplier", 1));
            }
         }
      }

   }

   public void removeSupplier(ModelElement arg) {
      if (this.theSupplierList != null && arg != null && this.theSupplierList.remove(arg)) {
         arg.removeSupplierDependency(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "supplier", 2));
         }
      }

   }

   public Set getCollectionClientList() {
      return this.theClientList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theClientList);
   }

   public java.util.Enumeration getClientList() {
      return Collections.enumeration(this.getCollectionClientList());
   }

   public void addClient(ModelElement arg) {
      if (arg != null) {
         if (this.theClientList == null) {
            this.theClientList = new LinkedHashSet();
         }

         if (this.theClientList.add(arg)) {
            arg.addClientDependency(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "client", 1));
            }
         }
      }

   }

   public void removeClient(ModelElement arg) {
      if (this.theClientList != null && arg != null && this.theClientList.remove(arg)) {
         arg.removeClientDependency(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "client", 2));
         }
      }

   }

   protected void internalRemove() {
      java.util.Enumeration tmpSupplierEnum = this.getSupplierList();
      ArrayList tmpSupplierList = new ArrayList();

      while(tmpSupplierEnum.hasMoreElements()) {
         tmpSupplierList.add(tmpSupplierEnum.nextElement());
      }

      Iterator it = tmpSupplierList.iterator();

      while(it.hasNext()) {
         ModelElement tmpSupplier = (ModelElement)it.next();
         tmpSupplier.removeSupplierDependency(this);
      }

      java.util.Enumeration tmpClientEnum = this.getClientList();
      ArrayList tmpClientList = new ArrayList();

      while(tmpClientEnum.hasMoreElements()) {
         tmpClientList.add(tmpClientEnum.nextElement());
      }

       it = tmpClientList.iterator();

      while(it.hasNext()) {
         ModelElement tmpClient = (ModelElement)it.next();
         tmpClient.removeClientDependency(this);
      }

      super.internalRemove();
   }
}
