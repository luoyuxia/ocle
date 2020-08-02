package ro.ubbcluj.lci.gui.browser.BTree;

import java.util.Comparator;
import java.util.List;

public interface Order extends Comparator {
   List order(List var1);

   void add(List var1, Object var2);

   void add(List var1, List var2);

   int compare(Object var1, Object var2);
}
