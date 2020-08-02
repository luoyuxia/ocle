package ro.ubbcluj.lci.ocl.datatypes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class ArrayListSet extends ArrayList {
   public ArrayListSet() {
   }

   public boolean add(Object obj) {
      return !this.contains(obj) ? super.add(obj) : false;
   }

   public boolean addAll(Collection col) {
      boolean rez = false;
      Iterator it = col.iterator();

      while(it.hasNext()) {
         Object obj = it.next();
         if (!this.contains(obj)) {
            super.add(obj);
            rez = true;
         }
      }

      return rez;
   }

   public void directAddAll(Collection col) {
      super.addAll(col);
   }

   public void directAdd(Object elem) {
      super.add(elem);
   }
}
