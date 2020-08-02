package ro.ubbcluj.lci.gui.browser.BTree;

import java.util.List;
import java.util.Vector;

public interface Filter {
   boolean accept(String var1);

   boolean accept(Object var1);

   void filter(List var1);

   Vector getAlwaysAccepted();

   Vector getAccepted();

   Vector getDenied();
}
