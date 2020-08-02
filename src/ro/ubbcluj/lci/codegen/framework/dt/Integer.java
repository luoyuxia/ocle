package ro.ubbcluj.lci.codegen.framework.dt;

public class Integer extends Real implements Comparable {
   public Integer(int v) {
      super((float)v);
   }

   public int asInteger() {
      return (int)this.value;
   }

   public static Integer toInteger(int x) {
      return new Integer(x);
   }

   public int hashCode() {
      return (int)this.value;
   }

   public String toString() {
      return String.valueOf((int)this.value);
   }
}
