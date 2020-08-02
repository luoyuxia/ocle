package ro.ubbcluj.lci.codegen.framework.dt;

public final class Boolean implements Comparable {
   private boolean value;
   private static Boolean TRUE = null;
   private static Boolean FALSE = null;

   public Boolean(boolean b) {
      this.value = b;
   }

   public boolean asBoolean() {
      return this.value;
   }

   public int compareTo(Object arg) {
      if (arg instanceof Boolean) {
         boolean a = ((Boolean)arg).asBoolean();
         return a ? (this.value ? 0 : -1) : (this.value ? 1 : 0);
      } else {
         throw new ClassCastException(arg == null ? "null" : arg.toString());
      }
   }

   public static Boolean toBoolean(boolean b) {
      Boolean ret = b ? TRUE : FALSE;
      if (ret == null) {
         ret = new Boolean(b);
         if (b) {
            TRUE = ret;
         } else {
            FALSE = ret;
         }
      }

      return ret;
   }

   public int hashCode() {
      return this.value ? 1231 : 1237;
   }

   public String toString() {
      return String.valueOf(this.value);
   }
}
