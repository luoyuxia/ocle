package ro.ubbcluj.lci.ocl.datatypes;

public class OclBoolean extends OclAny {
   private boolean value;

   public OclBoolean(boolean p_value) {
      this.value = p_value;
   }

   public Object getValue() {
      return new Boolean(this.value);
   }

   public boolean isTrue() {
      return this.value;
   }

   public boolean isFalse() {
      return !this.value;
   }

   public Object iif(Object rthen, Object relse) {
      return this.value ? rthen : relse;
   }

   public OclBoolean equal(OclBoolean b) {
      return new OclBoolean(this.value == b.value);
   }

   public OclBoolean notequal(OclBoolean b) {
      return new OclBoolean(this.value != b.value);
   }

   public OclBoolean and(OclBoolean b) {
      return new OclBoolean(this.value && b.value);
   }

   public OclBoolean or(OclBoolean b) {
      return new OclBoolean(this.value || b.value);
   }

   public OclBoolean xor(OclBoolean b) {
      return new OclBoolean(this.value ^ b.value);
   }

   public OclBoolean implies(OclBoolean b) {
      return new OclBoolean(!this.value || b.value);
   }

   public OclBoolean not() {
      return new OclBoolean(!this.value);
   }

   public String printValue() {
      return this.value ? "true" : "false";
   }
}
