package ro.ubbcluj.lci.ocl;

import java.util.Iterator;
import java.util.Stack;
import java.util.Vector;

class OclVarTable {
   private Vector vartable = new Vector();
   private Stack varsizes = new Stack();

   OclVarTable() {
   }

   void clear() {
      this.vartable.clear();
      this.varsizes.clear();
   }

   boolean addVariable(OclLetItem v) {
      if (v.name == null) {
         int i = 1;

         while(true) {
            v.name = "iter" + i;
            boolean exists = false;
            Iterator it = this.vartable.iterator();

            while(it.hasNext()) {
               OclLetItem letitem = (OclLetItem)it.next();
               if (letitem.name.equals(v.name)) {
                  exists = true;
                  break;
               }
            }

            if (!exists) {
               break;
            }

            ++i;
         }
      }

      this.vartable.add(v);
      return true;
   }

   void pushSize() {
      this.varsizes.push(new Integer(this.vartable.size()));
   }

   void popSize() {
      int size = ((Integer)this.varsizes.pop()).intValue();
      this.vartable.setSize(size);
   }

   OclLetItem getSelf() {
      return this.vartable.size() == 0 ? null : (OclLetItem)this.vartable.firstElement();
   }

   Iterator getIterator() {
      return this.vartable.iterator();
   }

   Vector getVarTable() {
      return this.vartable;
   }

   OclLetItem findLetItem(String name, Vector params) {
      if (this.vartable != null) {
         for(int i = this.vartable.size() - 1; i >= 0; --i) {
            OclLetItem oclvar = (OclLetItem)this.vartable.get(i);
            if (oclvar.conformingSignature(name, params)) {
               return oclvar;
            }
         }
      }

      return null;
   }
}
