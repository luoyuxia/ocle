package ro.ubbcluj.lci.ocl.datatypes;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;
import ro.ubbcluj.lci.ocl.ExceptionAny;

public class OclSequence extends OclCollection {
   Vector col = new Vector();

   public OclSequence() {
   }

   public OclSequence(Enumeration enum) {
      while(enum.hasMoreElements()) {
         this.col.add(enum.nextElement());
      }

   }

   public Collection getCollection() {
      return this.col;
   }

   public OclSequence addInt(OclInteger a, OclInteger b) {
      OclSequence seq = new OclSequence();

      for(long i = a.longValue(); i <= b.longValue(); ++i) {
         seq.col.add(new OclInteger(i));
      }

      return seq;
   }

   public Method getAddIntMethod() {
      Class[] paramTypes = new Class[]{OclInteger.class, OclInteger.class};

      try {
         return this.getClass().getMethod("addInt", paramTypes);
      } catch (Exception var3) {
         System.err.println("[fatal] can't find add method in collection");
         return null;
      }
   }

   public OclSequence append(Object obj) {
      OclSequence rez = new OclSequence();
      rez.col.addAll(this.col);
      rez.col.add(obj);
      return rez;
   }

   public OclSequence prepend(Object obj) {
      OclSequence rez = new OclSequence();
      rez.col.add(obj);
      rez.col.addAll(this.col);
      return rez;
   }

   public Object first() throws ExceptionAny {
      try {
         return this.col.firstElement();
      } catch (Exception var2) {
         throw new ExceptionAny("first() called for an empty sequence");
      }
   }

   public Object last() throws ExceptionAny {
      try {
         return this.col.lastElement();
      } catch (Exception var2) {
         throw new ExceptionAny("last() called for an empty sequence");
      }
   }

   public Object at(OclInteger k) throws ExceptionAny {
      try {
         return this.col.elementAt((int)k.longValue() - 1);
      } catch (Exception var3) {
         throw new ExceptionAny("sequence index out of range");
      }
   }

   public Object insertAt(OclInteger k, Object obj) throws ExceptionAny {
      try {
         OclSequence rez = new OclSequence();
         rez.col.addAll(this.col);
         rez.col.add((int)k.longValue() - 1, obj);
         return rez;
      } catch (Exception var4) {
         throw new ExceptionAny("sequence index out of range");
      }
   }

   public Object subSequence(OclInteger oi, OclInteger oj) throws ExceptionAny {
      int i = (int)oi.longValue();
      int j = (int)oj.longValue();
      if (i >= 1 && j <= this.getCollection().size()) {
         OclSequence result = new OclSequence();

         for(int k = i - 1; k <= j - 1; ++k) {
            result.getCollection().add(this.col.elementAt(k));
         }

         return result;
      } else {
         throw new ExceptionAny("sequence index out of range");
      }
   }

   public OclBoolean equal(OclCollection other) {
      if (!(other instanceof OclSequence)) {
         return new OclBoolean(false);
      } else {
         Collection col2 = other.getCollection();
         if (this.col.size() != col2.size()) {
            return new OclBoolean(false);
         } else {
            Iterator it = this.col.iterator();
            Iterator it2 = col2.iterator();

            while(it.hasNext() && it2.hasNext()) {
               if (!it.next().equals(it2.next())) {
                  return new OclBoolean(false);
               }
            }

            return new OclBoolean(true);
         }
      }
   }
}
