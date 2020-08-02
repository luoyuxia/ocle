package ro.ubbcluj.lci.ocl.datatypes;

import ro.ubbcluj.lci.ocl.ExceptionAny;

public class OclReal extends OclAny {
   private double rvalue;

   public OclReal() {
   }

   public OclReal(String s) {
      this.rvalue = Double.parseDouble(s);
   }

   public OclReal(double n) {
      this.rvalue = n;
   }

   public Object getValue() {
      return new Double(this.rvalue);
   }

   protected double val() {
      return this.rvalue;
   }

   public OclBoolean equal(OclReal n) {
      return new OclBoolean(this.val() == n.val());
   }

   public OclBoolean notequal(OclReal n) {
      return new OclBoolean(this.val() != n.val());
   }

   public OclBoolean lt(OclReal n) {
      return new OclBoolean(this.val() < n.val());
   }

   public OclBoolean le(OclReal n) {
      return new OclBoolean(this.val() <= n.val());
   }

   public OclBoolean gt(OclReal n) {
      return new OclBoolean(this.val() > n.val());
   }

   public OclBoolean ge(OclReal n) {
      return new OclBoolean(this.val() >= n.val());
   }

   public OclReal min(OclReal n) {
      return new OclReal(this.val() > n.val() ? this.val() : n.val());
   }

   public OclReal max(OclReal n) {
      return new OclReal(this.val() < n.val() ? this.val() : n.val());
   }

   public OclReal divide(OclReal n) throws ExceptionAny {
      if (n.val() == 0.0D) {
         throw new ExceptionAny("division by zero");
      } else {
         return new OclReal(this.val() / n.val());
      }
   }

   public OclReal add(OclReal n) {
      return new OclReal(this.val() + n.val());
   }

   public OclReal subs(OclReal n) {
      return new OclReal(this.val() - n.val());
   }

   public OclReal multiply(OclReal n) {
      return new OclReal(this.val() * n.val());
   }

   public OclReal abs() {
      return new OclReal(this.val() >= 0.0D ? this.val() : -this.val());
   }

   public OclReal floor() {
      return new OclReal(Math.floor(this.val()));
   }

   public OclInteger round() {
      return new OclInteger(Math.round(this.val()));
   }

   public OclReal negate() {
      return new OclReal(-this.val());
   }

   public String printValue() {
      return "" + this.rvalue;
   }
}
