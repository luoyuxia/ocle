package ro.ubbcluj.lci.uml.foundation.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import ro.ubbcluj.lci.codegen.framework.dt.OrderedSet;
import ro.ubbcluj.lci.codegen.framework.ocl.CollectionUtilities;
import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;

public class EnumerationImpl extends DataTypeImpl implements Enumeration {
   protected OrderedSet theLiteralList;

   public EnumerationImpl() {
   }

   public OrderedSet getCollectionLiteralList() {
      return this.theLiteralList == null ? CollectionUtilities.newOrderedSet() : CollectionUtilities.newOrderedSet(this.theLiteralList);
   }

   public java.util.Enumeration getLiteralList() {
      return Collections.enumeration(this.getCollectionLiteralList());
   }

   public void addLiteral(EnumerationLiteral arg) {
      if (arg != null) {
         if (this.theLiteralList == null) {
            this.theLiteralList = new OrderedSet();
         }

         if (this.theLiteralList.add(arg)) {
            arg.setEnumeration(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "literal", 1));
            }
         }
      }

   }

   public void removeLiteral(EnumerationLiteral arg) {
      if (this.theLiteralList != null && arg != null && this.theLiteralList.remove(arg)) {
         arg.setEnumeration((Enumeration)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "literal", 2));
         }
      }

   }

   protected void internalRemove() {
      java.util.Enumeration tmpLiteralEnum = this.getLiteralList();
      ArrayList tmpLiteralList = new ArrayList();

      while(tmpLiteralEnum.hasMoreElements()) {
         tmpLiteralList.add(tmpLiteralEnum.nextElement());
      }

      Iterator it = tmpLiteralList.iterator();

      while(it.hasNext()) {
         ((EnumerationLiteral)it.next()).remove();
      }

      super.internalRemove();
   }
}
