package ro.ubbcluj.lci.ocl.datatypes;

import ro.ubbcluj.lci.ocl.ExceptionAny;

public class OclInteger extends OclReal {
   public static final OclInteger MAX_VALUE = new OclInteger(9223372036854775807L);
   private long value;

   public OclInteger() {
   }

   public OclInteger(String s) {
      this.value = Long.parseLong(s);
   }

   public OclInteger(long n) {
      this.value = n;
   }

   public Object getValue() {
      return new Double((double)this.value);
   }

   protected double val() {
      return (double)this.value;
   }

   public long longValue() {
      return this.value;
   }

   public OclBoolean equal(OclInteger n) {
      return new OclBoolean(this.value == n.value);
   }

   public OclBoolean notequal(OclInteger n) {
      return new OclBoolean(this.value != n.value);
   }

   public OclInteger min(OclInteger n) {
      return new OclInteger(this.value > n.value ? this.value : n.value);
   }

   public OclInteger max(OclInteger n) {
      return new OclInteger(this.value < n.value ? this.value : n.value);
   }

   public OclInteger div(OclInteger n) throws ExceptionAny {
      if (n.value == 0L) {
         throw new ExceptionAny("division by zero");
      } else {
         return new OclInteger(this.value / n.value);
      }
   }

   public OclInteger mod(OclInteger n) throws ExceptionAny {
      if (n.value == 0L) {
         throw new ExceptionAny("division by zero");
      } else {
         return new OclInteger(this.value % n.value);
      }
   }

   public OclInteger add(OclInteger n) {
      return new OclInteger(this.value + n.value);
   }

   public OclInteger subs(OclInteger n) {
      return new OclInteger(this.value - n.value);
   }

   public OclInteger multiply(OclInteger n) {
      return new OclInteger(this.value * n.value);
   }

   public OclReal abs() {
      return new OclInteger(this.value >= 0L ? this.value : -this.value);
   }

   public OclReal negate() {
      return new OclInteger(-this.value);
   }

   public String printValue() {
      return this.value == 9223372036854775807L ? "unlimited" : "" + this.value;
   }
}
