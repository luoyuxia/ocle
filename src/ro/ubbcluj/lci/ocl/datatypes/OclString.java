package ro.ubbcluj.lci.ocl.datatypes;

import java.util.StringTokenizer;
import ro.ubbcluj.lci.ocl.ExceptionAny;

public class OclString extends OclAny {
   private String value;

   public OclString() {
      this.value = "";
   }

   public OclString(String s) {
      this.value = s;
   }

   public Object getValue() {
      return this.value;
   }

   public OclBoolean equal(OclString s) {
      return new OclBoolean(s.value.equals(this.value));
   }

   public OclBoolean notequal(OclString s) {
      return new OclBoolean(!s.value.equals(this.value));
   }

   public OclBoolean lt(OclString s) {
      return new OclBoolean(this.value.compareTo(s.value) < 0);
   }

   public OclBoolean le(OclString s) {
      return new OclBoolean(this.value.compareTo(s.value) <= 0);
   }

   public OclBoolean gt(OclString s) {
      return new OclBoolean(this.value.compareTo(s.value) > 0);
   }

   public OclBoolean ge(OclString s) {
      return new OclBoolean(this.value.compareTo(s.value) >= 0);
   }

   public OclInteger size() {
      return new OclInteger((long)this.value.length());
   }

   public OclString toLower() {
      return new OclString(this.value.toLowerCase());
   }

   public OclString toUpper() {
      return new OclString(this.value.toUpperCase());
   }

   public OclString substring(OclInteger lo, OclInteger hi) throws ExceptionAny {
      int a = (int)lo.longValue();
      int b = (int)hi.longValue() + 1;
      if (a >= 0 && b <= this.value.length()) {
         return new OclString(this.value.substring(a, b));
      } else {
         throw new ExceptionAny("String.substring called for \"" + this.value + "\" with out of range arguments " + a + " and " + (b - 1) + " (warning, indexes are 0-based)");
      }
   }

   public Object toInteger() {
      try {
         int n = Integer.parseInt(this.value);
         return new OclInteger((long)n);
      } catch (NumberFormatException var2) {
         return new Undefined("toInteger with invalid argument");
      }
   }

   public Object toReal() {
      try {
         double n = Double.parseDouble(this.value);
         return new OclReal(n);
      } catch (NumberFormatException var3) {
         return new Undefined("toReal with invalid argument");
      }
   }

   public OclString concat(OclString s) {
      return new OclString(this.value + s.value);
   }

   public boolean contains(OclString s) {
      return this.value.indexOf(s.value) >= 0;
   }

   public int pos(OclString s) {
      return this.value.indexOf(s.value);
   }

   public OclSequence split(OclString s) throws ExceptionAny {
      OclSequence seq = new OclSequence();
      StringTokenizer tok = new StringTokenizer(this.value, s.value);

      while(tok.hasMoreTokens()) {
         seq.getCollection().add(new OclString(tok.nextToken()));
      }

      return seq;
   }

   public String printValue() {
      return "'" + this.value + "'";
   }
}
