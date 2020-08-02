package ro.ubbcluj.lci.codegen.framework.dt;

public class Real implements Comparable {
   protected float value;

   public Real(float v) {
      this.value = v;
   }

   public float asReal() {
      return this.value;
   }

   public int compareTo(Object o) {
      if (o instanceof Real) {
         float arg = ((Real)o).asReal();
         if (this.value < arg) {
            boolean var2 = true;
         }

         int r = this.value > arg ? 1 : 0;
         return r;
      } else {
         throw new ClassCastException(o == null ? "null" : o.toString());
      }
   }

   public boolean equals(Object x) {
      if (x instanceof Real) {
         return ((Real)x).asReal() == this.value;
      } else {
         return false;
      }
   }

   public static Real toReal(float x) {
      return new Real(x);
   }

   public int hashCode() {
      return Float.floatToIntBits(this.value);
   }

   public String toString() {
      return String.valueOf(this.value);
   }
}
