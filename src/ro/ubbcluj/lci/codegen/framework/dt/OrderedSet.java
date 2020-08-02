package ro.ubbcluj.lci.codegen.framework.dt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public final class OrderedSet extends ArrayList implements Set {
   public OrderedSet() {
   }

   public OrderedSet(Collection start) {
      if (start != null) {
         this.addAll(start);
      }

   }

   public boolean add(Object toAdd) {
      boolean r;
      if (toAdd != null && !super.contains(toAdd)) {
         r = super.add(toAdd);
      } else {
         r = false;
      }

      return r;
   }

   public void add(int idx, Object toAdd) {
      if (toAdd != null && !super.contains(toAdd)) {
         super.add(idx, toAdd);
      }

   }

   public boolean addAll(Collection toAdd) {
      boolean changed = false;
      Object next;
      if (toAdd != null) {
         for(Iterator it = toAdd.iterator(); it.hasNext(); changed = this.add(next)) {
            next = it.next();
         }
      }

      return changed;
   }

   public boolean addAll(int idx, Collection toAdd) {
      boolean changed = false;
      if (toAdd != null) {
         Iterator it = toAdd.iterator();

         while(it.hasNext()) {
            Object next = it.next();
            if (!this.contains(next)) {
               super.add(idx++, next);
               changed = true;
            }
         }
      }

      return changed;
   }
}
