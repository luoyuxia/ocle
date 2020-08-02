package ro.ubbcluj.lci.uml.foundation.dataTypes;

public class ChangeableKind {
   public static final int FROZEN = 0;
   public static final int ADDONLY = 1;
   public static final int CHANGEABLE = 2;

   public ChangeableKind() {
   }

   public static String getStringValue(int i) {
      switch(i) {
      case 0:
         return "frozen";
      case 1:
         return "addOnly";
      default:
         return "changeable";
      }
   }
}
