package ro.ubbcluj.lci.uml.foundation.dataTypes;

public class PseudostateKind {
   public static final int FORK = 0;
   public static final int SHALLOWHISTORY = 1;
   public static final int JOIN = 2;
   public static final int INITIAL = 3;
   public static final int CHOICE = 4;
   public static final int DEEPHISTORY = 5;
   public static final int JUNCTION = 6;

   public PseudostateKind() {
   }

   public static String getStringValue(int i) {
      switch(i) {
      case 0:
         return "fork";
      case 1:
         return "shallowHistory";
      case 2:
         return "join";
      case 3:
         return "initial";
      case 4:
         return "choice";
      case 5:
         return "deepHistory";
      default:
         return "junction";
      }
   }
}
