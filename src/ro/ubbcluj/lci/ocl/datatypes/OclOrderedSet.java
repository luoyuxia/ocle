package ro.ubbcluj.lci.ocl.datatypes;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import ro.ubbcluj.lci.ocl.ExceptionAny;

public class OclOrderedSet extends OclCollection {
   ArrayListSet col = new ArrayListSet();

   public OclOrderedSet() {
   }

   public OclOrderedSet(Collection p_col) {
      this.col.addAll(p_col);
   }

   public OclOrderedSet(Enumeration enum) {
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

   public OclOrderedSet subs(OclOrderedSet s) {
      OclOrderedSet rez = new OclOrderedSet();
      rez.getCollection().addAll(this.getCollection());
      rez.getCollection().removeAll(s.getCollection());
      return rez;
   }

   public OclBoolean equal(OclCollection other) {
      OclBoolean rfalse = new OclBoolean(false);
      if (!(other instanceof OclOrderedSet)) {
         return rfalse;
      } else {
         Collection v1 = this.col;
         Collection v2 = other.getCollection();
         if (v1.size() != v2.size()) {
            return rfalse;
         } else {
            Iterator it1 = v1.iterator();
            Iterator it2 = v2.iterator();

            while(it1.hasNext() && it2.hasNext()) {
               if (!it1.next().equals(it2.next())) {
                  return rfalse;
               }
            }

            return new OclBoolean(true);
         }
      }
   }

   public OclOrderedSet append(Object obj) {
      OclOrderedSet rez = new OclOrderedSet();
      rez.col.addAll(this.col);
      rez.col.add(obj);
      return rez;
   }

   public OclOrderedSet prepend(Object obj) {
      OclOrderedSet rez = new OclOrderedSet();
      rez.col.add(obj);
      rez.col.addAll(this.col);
      return rez;
   }

   public Object first() throws ExceptionAny {
      try {
         return this.col.get(0);
      } catch (Exception var2) {
         throw new ExceptionAny("first() called for an empty sequence");
      }
   }

   public Object last() throws ExceptionAny {
      try {
         return this.col.get(this.col.size() - 1);
      } catch (Exception var2) {
         throw new ExceptionAny("last() called for an empty sequence");
      }
   }

   public Object at(OclInteger k) throws ExceptionAny {
      try {
         return this.col.get((int)k.longValue() - 1);
      } catch (Exception var3) {
         throw new ExceptionAny("sequence index out of range");
      }
   }

   public Object insertAt(OclInteger k, Object obj) throws ExceptionAny {
      try {
         OclOrderedSet rez = new OclOrderedSet();
         rez.col.addAll(this.col);
         rez.col.add((int)k.longValue() - 1, obj);
         return rez;
      } catch (Exception var4) {
         throw new ExceptionAny("sequence index out of range");
      }
   }

   public Object subOrderedSet(OclInteger oi, OclInteger oj) throws ExceptionAny {
      int i = (int)oi.longValue();
      int j = (int)oj.longValue();
      if (i >= 1 && j <= this.getCollection().size()) {
         OclOrderedSet result = new OclOrderedSet();

         for(int k = i - 1; k <= j - 1; ++k) {
            result.getCollection().add(this.col.get(k));
         }

         return result;
      } else {
         throw new ExceptionAny("sequence index out of range");
      }
   }
}
