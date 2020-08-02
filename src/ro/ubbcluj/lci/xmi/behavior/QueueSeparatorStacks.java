package ro.ubbcluj.lci.xmi.behavior;

import java.util.Vector;

public class QueueSeparatorStacks {
   private Vector v = new Vector();

   public QueueSeparatorStacks() {
   }

   public void pushSeparator() {
      this.push(new QueueSeparatorStacks.Separator());
   }

   public void push(Object o) {
      this.v.add(o);
   }

   public Object[] pop() {
      if (this.v != null && this.v.size() != 0) {
         Vector vv = new Vector();
         boolean works = true;
         boolean var3 = false;

         do {
            Object o = this.v.remove(this.v.size() - 1);
            if (o instanceof QueueSeparatorStacks.Separator) {
               works = false;
            } else {
               vv.insertElementAt(o, 0);
            }
         } while(works);

         return vv.toArray();
      } else {
         return null;
      }
   }

   class Separator {
      private String owner;

      public Separator() {
         this.owner = null;
      }

      public Separator(String s) {
         this.owner = new String(s);
      }

      public String getOwner() {
         return this.owner != null ? this.owner : null;
      }
   }
}
