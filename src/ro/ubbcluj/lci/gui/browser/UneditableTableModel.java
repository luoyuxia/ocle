package ro.ubbcluj.lci.gui.browser;

import javax.swing.table.DefaultTableModel;

class UneditableTableModel extends DefaultTableModel {
   UneditableTableModel(String[] columnNames, int rows) {
      super(columnNames, rows);
   }

   public boolean isCellEditable(int row, int column) {
      return false;
   }
}
