package ro.ubbcluj.lci.utils;

public class CharArray {
   private char[] data = new char[10];
   private int size;

   public CharArray() {
   }

   public void add(char c) {
      ++this.size;
      this.ensureSize(this.size);
      this.data[this.size - 1] = c;
   }

   public boolean contains(char c) {
      boolean cont = false;
      int i = 0;

      while(i < this.size && !cont) {
         if (c == this.data[i++]) {
            cont = true;
         }
      }

      return cont;
   }

   private void ensureSize(int s) {
      if (s > this.data.length) {
         int l;
         for(l = this.data.length; l < s; l <<= 1) {
         }

         char[] n = new char[l];
         System.arraycopy(this.data, 0, n, 0, this.data.length);
         this.data = n;
      }

   }
}
