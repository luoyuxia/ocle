package ro.ubbcluj.lci.uml.foundation.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import ro.ubbcluj.lci.codegen.framework.dt.OrderedSet;
import ro.ubbcluj.lci.codegen.framework.ocl.CollectionUtilities;
import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;

public class BindingImpl extends DependencyImpl implements Binding {
   protected OrderedSet theArgumentList;

   public BindingImpl() {
   }

   public OrderedSet getCollectionArgumentList() {
      return this.theArgumentList == null ? CollectionUtilities.newOrderedSet() : CollectionUtilities.newOrderedSet(this.theArgumentList);
   }

   public java.util.Enumeration getArgumentList() {
      return Collections.enumeration(this.getCollectionArgumentList());
   }

   public void addArgument(TemplateArgument arg) {
      if (arg != null) {
         if (this.theArgumentList == null) {
            this.theArgumentList = new OrderedSet();
         }

         if (this.theArgumentList.add(arg)) {
            arg.setBinding(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "argument", 1));
            }
         }
      }

   }

   public void removeArgument(TemplateArgument arg) {
      if (this.theArgumentList != null && arg != null && this.theArgumentList.remove(arg)) {
         arg.setBinding((Binding)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "argument", 2));
         }
      }

   }

   protected void internalRemove() {
      java.util.Enumeration tmpArgumentEnum = this.getArgumentList();
      ArrayList tmpArgumentList = new ArrayList();

      while(tmpArgumentEnum.hasMoreElements()) {
         tmpArgumentList.add(tmpArgumentEnum.nextElement());
      }

      Iterator it = tmpArgumentList.iterator();

      while(it.hasNext()) {
         ((TemplateArgument)it.next()).remove();
      }

      super.internalRemove();
   }
}
