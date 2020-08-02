package ro.ubbcluj.lci.ocl.datatypes;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

public class OclBag extends OclCollection {
   public Vector col = new Vector();

   public Collection getCollection() {
      return this.col;
   }

   public OclBag() {
   }

   public OclBag(Enumeration enum) {
      while(enum.hasMoreElements()) {
         this.col.add(enum.nextElement());
      }

   }

   public OclBoolean equal(OclCollection other) {
      if (!(other instanceof OclBag)) {
         return new OclBoolean(false);
      } else {
         LinkedList col2 = new LinkedList(other.getCollection());
         if (this.col.size() != col2.size()) {
            return new OclBoolean(false);
         } else {
            Iterator it = this.col.iterator();

            while(it.hasNext()) {
               Object obj = it.next();
               if (!col2.contains(obj)) {
                  return new OclBoolean(false);
               }

               col2.remove(obj);
            }

            return new OclBoolean(col2.isEmpty());
         }
      }
   }
}
