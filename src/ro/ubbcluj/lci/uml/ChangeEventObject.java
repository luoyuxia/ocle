package ro.ubbcluj.lci.uml;

import java.util.EventObject;

public class ChangeEventObject extends EventObject {
   public static final int REFERENCE_SET = 0;
   public static final int LIST_ADD = 1;
   public static final int LIST_REMOVE = 2;
   public static final int REMOVE = 3;
   protected String sourceField;
   protected int kind;

   public ChangeEventObject(Object source, String sourceField, int kind) {
      super(source);
      this.sourceField = sourceField;
      this.kind = kind;
   }

   public int getKind() {
      return this.kind;
   }

   public String getSourceField() {
      return this.sourceField;
   }

   public String toString() {
      String toString = this.getClass().getName() + "[source=" + this.source + " field=" + this.sourceField + " kind=";
      switch(this.kind) {
      case 0:
         toString = toString + "REFERENCE_SET]";
         break;
      case 1:
         toString = toString + "LIST_ADD]";
         break;
      case 2:
         toString = toString + "LIST_REMOVE]";
         break;
      case 3:
         toString = toString + "REMOVE]";
         break;
      default:
         toString = toString + "UNKNOWN]";
      }

      return toString;
   }
}
