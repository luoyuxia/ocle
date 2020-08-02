package ro.ubbcluj.lci.gui.properties;

import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.tree.TreePath;

public class TreeTableModelAdapter extends AbstractTableModel {
   JTree tree;
   public TreeTableModel treeTableModel;

   public TreeTableModelAdapter(TreeTableModel treeTableModel, JTree tree) {
      this.tree = tree;
      this.treeTableModel = treeTableModel;
      tree.addTreeExpansionListener(new TreeExpansionListener() {
         public void treeExpanded(TreeExpansionEvent event) {
            TreeTableModelAdapter.this.fireTableDataChanged();
         }

         public void treeCollapsed(TreeExpansionEvent event) {
            TreeTableModelAdapter.this.fireTableDataChanged();
         }
      });
   }

   public int getColumnCount() {
      return this.treeTableModel.getColumnCount();
   }

   public String getColumnName(int column) {
      return this.treeTableModel.getColumnName(column);
   }

   public Class getColumnClass(int column) {
      return this.treeTableModel.getColumnClass(column);
   }

   public int getRowCount() {
      return this.tree.getRowCount();
   }

   public Object nodeForRow(int row) {
      TreePath treePath = this.tree.getPathForRow(row);
      return treePath == null ? null : treePath.getLastPathComponent();
   }

   public Object getValueAt(int row, int column) {
      return this.treeTableModel.getValueAt(this.nodeForRow(row), column);
   }

   public boolean isCellEditable(int row, int column) {
      return this.treeTableModel.isCellEditable(this.nodeForRow(row), column);
   }

   public void setValueAt(Object value, int row, int column) {
      this.treeTableModel.setValueAt(value, this.nodeForRow(row), column);
   }
}
