package ro.ubbcluj.lci.utils;

import java.util.EventObject;

public class ModelEvent extends EventObject {
   public static final int ELEMENT_SELECTED = 0;
   public static final int ELEMENT_ADDED = 10;
   public static final int ELEMENT_UPDATED = 20;
   public static final int ELEMENT_DEEP_UPDATED = 21;
   public static final int ELEMENT_REMOVED = 30;
   public static final int ATTR_ADDED = 40;
   private Object subject;
   private Object context;
   private int operation;

   public ModelEvent(Object source, Object subject, Object context, int operation) {
      super(source);
      this.subject = subject;
      this.context = context;
      this.operation = operation;
   }

   public Object getSubject() {
      return this.subject;
   }

   public Object getContext() {
      return this.context;
   }

   public int getOperation() {
      return this.operation;
   }

   public String toString() {
      String s = "ModelEvent[";
      s = s + "source=" + this.source + ", ";
      s = s + "subject=" + this.subject + ", ";
      s = s + "context=" + this.context + ", ";
      s = s + "operation=" + this.operation + "]";
      return s;
   }
}
