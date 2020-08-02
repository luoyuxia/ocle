package ro.ubbcluj.lci.uml.foundation.dataTypes;

public class ScopeKind {
   public static final int CLASSIFIER = 0;
   public static final int INSTANCE = 1;

   public ScopeKind() {
   }

   public static String getStringValue(int i) {
      switch(i) {
      case 0:
         return "classifier";
      default:
         return "instance";
      }
   }
}
