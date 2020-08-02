package ro.ubbcluj.lci.gui.Actions;

import java.util.Comparator;
import ro.ubbcluj.lci.ocl.OclUserElement;
import ro.ubbcluj.lci.ocl.OclUtil;
import ro.ubbcluj.lci.utils.Utils;

class TypeBasedOrderComparator implements Comparator {
   TypeBasedOrderComparator() {
   }

   public boolean equals(Object x) {
      return x == this;
   }

   public int compare(Object o1, Object o2) {
      Object x1 = ((OclUserElement)o1).getElement();
      Object x2 = ((OclUserElement)o2).getElement();
      int first = OclUtil.className(x1).compareToIgnoreCase(OclUtil.className(x2));
      return first == 0 ? Utils.fullName(x1).compareToIgnoreCase(Utils.fullName(x2)) : first;
   }
}
