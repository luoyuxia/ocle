package ro.ubbcluj.lci.uml.foundation.dataTypes;

public class CallConcurrencyKind {
   public static final int CONCURRENT = 0;
   public static final int SEQUENTIAL = 1;
   public static final int GUARDED = 2;

   public CallConcurrencyKind() {
   }

   public static String getStringValue(int i) {
      switch(i) {
      case 0:
         return "concurrent";
      case 1:
         return "sequential";
      default:
         return "guarded";
      }
   }
}
