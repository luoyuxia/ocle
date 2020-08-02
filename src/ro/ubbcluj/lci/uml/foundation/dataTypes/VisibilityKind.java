package ro.ubbcluj.lci.uml.foundation.dataTypes;

public class VisibilityKind {
   public static final int PRIVATE = 0;
   public static final int PACKAGE = 1;
   public static final int PUBLIC = 3;
   public static final int PROTECTED = 2;

   public VisibilityKind() {
   }

   public static String getStringValue(int i) {
      switch(i) {
      case 0:
         return "private";
      case 1:
         return "package";
      case 2:
      default:
         return "protected";
      case 3:
         return "public";
      }
   }
}
