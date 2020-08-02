package ro.ubbcluj.lci.gui.browser.BTree;

import javax.swing.DefaultListModel;

class SortedListModel extends DefaultListModel {
   SortedListModel() {
   }

   public void addElement(Object obj) {
      if (obj instanceof String) {
         String str = (String)obj;

         int index;
         for(index = 0; index < this.getSize() && str.compareTo(this.get(index).toString()) > 0; ++index) {
         }

         this.add(index, str);
      }
   }
}
