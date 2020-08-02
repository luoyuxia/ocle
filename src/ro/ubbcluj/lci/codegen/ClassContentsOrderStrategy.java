package ro.ubbcluj.lci.codegen;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class ClassContentsOrderStrategy implements Comparator, JavaReservedWords {
   private Class[] ordered;
   private String[] orderedModifiers;

   public ClassContentsOrderStrategy() {
      this.ordered = new Class[]{PGMethod.class, PGConstructor.class, PGField.class, PGBlock.class};
      this.orderedModifiers = new String[]{"public", "protected", "", "private"};
   }

   public int compare(Object o1, Object o2) {
      Class t1 = o1.getClass();
      Class t2 = o2.getClass();
      int i1 = getIndex(t1, this.ordered);
      int i2 = getIndex(t2, this.ordered);
      int result;
      if (i1 == i2) {
         PGElement pe1 = (PGElement)o1;
         PGElement pe2 = (PGElement)o2;
         String vis1 = this.getVisibility(pe1);
         String vis2 = this.getVisibility(pe2);
         int pos1 = getIndex(vis1, this.orderedModifiers);
         int pos2 = getIndex(vis2, this.orderedModifiers);
         result = pos1 < pos2 ? -1 : (pos1 > pos2 ? 1 : 0);
      } else {
         result = i1 < i2 ? -1 : 1;
      }

      return result;
   }

   private String getVisibility(PGElement pe) {
      String result = "";
      List mod = pe.getModifiers();
      Iterator it = mod.iterator();

      while(it.hasNext()) {
         Object m = it.next();
         if (m.equals("private") || m.equals("protected") || m.equals("public")) {
            result = (String)m;
            break;
         }
      }

      return result;
   }

   private static int getIndex(Object o, Object[] array) {
      int result = -1;

      for(int i = 0; i < array.length; ++i) {
         if (o.equals(array[i])) {
            result = i;
         }
      }

      return result;
   }
}
