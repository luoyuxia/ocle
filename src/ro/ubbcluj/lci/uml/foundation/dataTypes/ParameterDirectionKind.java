package ro.ubbcluj.lci.uml.foundation.dataTypes;

public class ParameterDirectionKind {
   public static final int INOUT = 0;
   public static final int OUT = 1;
   public static final int IN = 2;
   public static final int RETURN = 3;

   public ParameterDirectionKind() {
   }

   public static String getStringValue(int i) {
      switch(i) {
      case 0:
         return "inout";
      case 1:
         return "out";
      case 2:
         return "in";
      default:
         return "return";
      }
   }
}
