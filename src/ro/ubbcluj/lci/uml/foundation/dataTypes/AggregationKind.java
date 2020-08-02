package ro.ubbcluj.lci.uml.foundation.dataTypes;

public class AggregationKind {
   public static final int COMPOSITE = 0;
   public static final int NONE = 1;
   public static final int AGGREGATE = 2;

   public AggregationKind() {
   }

   public static String getStringValue(int i) {
      switch(i) {
      case 0:
         return "composite";
      case 1:
         return "none";
      default:
         return "aggregate";
      }
   }
}
