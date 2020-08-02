package ro.ubbcluj.lci.gui.editor.utils;

public class IntArray {
   private int[] data = new int[16];
   private int size = 0;
   private int cap = 16;

   public IntArray() {
   }

   private void resize(int atleast) {
      label21:
      while(true) {
         if (this.cap <= atleast) {
            int[] _data = this.data;
            if (this.cap > 100000) {
               this.cap += 100000;
            } else {
               this.cap *= 2;
            }

            this.data = new int[this.cap];
            int i = 0;

            while(true) {
               if (i >= this.size) {
                  continue label21;
               }

               this.data[i] = _data[i];
               ++i;
            }
         }

         return;
      }
   }

   public void add(int n) {
      this.resize(this.size + 1);
      this.data[this.size++] = n;
   }

   public void add(int k, int n) {
      this.resize(this.size + 1);

      for(int i = this.size; i > k; --i) {
         this.data[i] = this.data[i - 1];
      }

      this.data[k] = n;
      ++this.size;
   }

   public void remove(int k) {
      for(int i = k; i < this.size - 1; ++i) {
         this.data[i] = this.data[i + 1];
      }

      --this.size;
   }

   public void setSize(int size) {
      this.size = size;
   }

   public void clear() {
      this.size = 0;
   }

   public int size() {
      return this.size;
   }

   public int get(int i) {
      return this.data[i];
   }

   public int[] getData() {
      int[] rez = new int[this.size];
      System.arraycopy(this.data, 0, rez, 0, this.size);
      return rez;
   }

   public IntArray.Iterator iterator() {
      return new IntArray.Iterator() {
         private int pos = 0;

         public boolean hasNext() {
            return this.pos < IntArray.this.size;
         }

         public int next() {
            return IntArray.this.data[this.pos++];
         }
      };
   }

   public interface Iterator {
      boolean hasNext();

      int next();
   }
}
