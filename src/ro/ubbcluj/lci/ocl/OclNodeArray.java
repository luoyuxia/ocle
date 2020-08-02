package ro.ubbcluj.lci.ocl;

import java.util.Iterator;

public class OclNodeArray {
   private OclNode[] achildren = new OclNode[16];
   private int size = 0;
   private int cap = 16;

   public OclNodeArray() {
   }

   public void add(OclNode obj) {
      if (this.size == this.cap) {
         OclNode[] oldachildren = this.achildren;
         this.cap *= 2;
         this.achildren = new OclNode[this.cap];

         for(int i = 0; i < this.size; ++i) {
            this.achildren[i] = oldachildren[i];
         }
      }

      this.achildren[this.size++] = obj;
   }

   public void setSize(int size) {
      this.size = size;
   }

   public void clear() {
      this.size = 0;
   }

   public void end() {
      OclNode[] oldachildren = this.achildren;
      this.achildren = new OclNode[this.size];

      for(int i = 0; i < this.size; ++i) {
         this.achildren[i] = oldachildren[i];
      }

      this.cap = this.size;
   }

   public int size() {
      return this.size;
   }

   public OclNode elementAt(int i) {
      return this.achildren[i];
   }

   public OclNode get(int i) {
      return this.achildren[i];
   }

   public Iterator iterator() {
      return new OclNodeArray.MyIterator(this.achildren, this.size);
   }

   private class MyIterator implements Iterator {
      private int pos = 0;
      private int size;
      private OclNode[] achildren;

      public MyIterator(OclNode[] achildren, int size) {
         this.size = size;
         this.achildren = achildren;
      }

      public boolean hasNext() {
         return this.pos < this.size;
      }

      public Object next() {
         return this.achildren[this.pos++];
      }

      public void remove() {
      }
   }
}
