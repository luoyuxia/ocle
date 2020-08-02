package ro.ubbcluj.lci.gui.browser.BTree;

import java.util.List;

public interface Browseable {
   List getChildren();

   List getChildren(Filter var1, Order var2);
}
