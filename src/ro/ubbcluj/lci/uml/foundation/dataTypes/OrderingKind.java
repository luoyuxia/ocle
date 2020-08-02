package ro.ubbcluj.lci.uml.foundation.dataTypes;

public class OrderingKind {
   public static final int ORDERED = 0;
   public static final int UNORDERED = 1;

   public OrderingKind() {
   }

   public static String getStringValue(int i) {
      switch(i) {
      case 0:
         return "ordered";
      default:
         return "unordered";
      }
   }
}
