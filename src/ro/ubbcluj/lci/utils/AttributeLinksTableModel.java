package ro.ubbcluj.lci.utils;

import javax.swing.table.DefaultTableModel;

public class AttributeLinksTableModel extends DefaultTableModel {
   public AttributeLinksTableModel(Object[] columnNames, int rowCount) {
      super(columnNames, rowCount);
   }

   public boolean isCellEditable(int row, int column) {
      return column >= this.getColumnCount() - 1;
   }
}
