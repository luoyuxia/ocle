package ro.ubbcluj.lci.ocl.datatypes;

public class Undefined extends OclAny {
   private String reason = null;

   public Undefined() {
      this.reason = null;
   }

   public Undefined(String s) {
      this.reason = s;
   }

   public Object getValue() {
      return Undefined.class;
   }

   public String printValue() {
      return "Undefined" + (this.reason == null ? "" : "(" + this.reason + ")");
   }
}
