package ro.ubbcluj.lci.ocl.datatypes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;

public class OclSet extends OclCollection {
   private ArrayListSet col = new ArrayListSet();

   public OclSet() {
   }

   public OclSet(Collection p_col) {
      this.col.addAll(p_col);
   }

   public OclSet(Enumeration enum) {
      while(enum.hasMoreElements()) {
         this.col.add(enum.nextElement());
      }

   }

   public void directAdd(Object elem) {
      this.col.directAdd(elem);
   }

   public Collection getCollection() {
      return this.col;
   }

   public OclSet subs(OclSet s) {
      OclSet rez = new OclSet();
      rez.getCollection().addAll(this.getCollection());
      rez.getCollection().removeAll(s.getCollection());
      return rez;
   }

   public OclSet symmetricDifference(OclSet s) {
      ArrayList a = (ArrayList)this.col.clone();
      ArrayList b = (ArrayList)s.col.clone();
      a.removeAll(s.col);
      b.removeAll(this.col);
      a.addAll(b);
      OclSet r = new OclSet();
      r.col.directAddAll(a);
      return r;
   }

   public OclBoolean equal(OclCollection other) {
      if (!(other instanceof OclSet)) {
         return new OclBoolean(false);
      } else {
         Collection v1 = this.col;
         Collection v2 = other.getCollection();
         return new OclBoolean(v1.containsAll(v2) && v2.containsAll(v1));
      }
   }
}
