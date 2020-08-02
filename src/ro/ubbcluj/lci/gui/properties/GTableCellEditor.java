package ro.ubbcluj.lci.gui.properties;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import java.util.Hashtable;
import javax.swing.DefaultCellEditor;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;

public class GTableCellEditor implements TableCellEditor {
   protected int clicks;
   protected Hashtable editors;
   protected TableCellEditor editor;
   protected TableCellEditor defaultEditor;
   protected JLabel label = new JLabel("");
   protected JTable table;

   public GTableCellEditor(JTable table) {
      this.table = table;
      this.editors = new Hashtable();
      this.defaultEditor = new GCellEditor(this.label);
      this.editor = this.defaultEditor;
      this.label.setFont(this.label.getFont().deriveFont(0));
   }

   public void setClicks(int c) {
      this.clicks = c;
   }

   public void setEditorAt(Object rowId, TableCellEditor editor) {
      if (editor instanceof DefaultCellEditor) {
         ((DefaultCellEditor)editor).setClickCountToStart(this.clicks);
      }

      this.editors.put(rowId, editor);
   }

   public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
      return this.editor.getTableCellEditorComponent(table, value, isSelected, row, column);
   }

   public Object getCellEditorValue() {
      return this.editor.getCellEditorValue();
   }

   public boolean stopCellEditing() {
      return this.editor.stopCellEditing();
   }

   public void cancelCellEditing() {
      this.editor.cancelCellEditing();
   }

   public boolean isCellEditable(EventObject anEvent) {
      this.selectEditor((MouseEvent)anEvent);
      return this.editor.isCellEditable(anEvent);
   }

   public void addCellEditorListener(CellEditorListener l) {
      this.editor.addCellEditorListener(l);
   }

   public void removeCellEditorListener(CellEditorListener l) {
      this.editor.removeCellEditorListener(l);
   }

   public boolean shouldSelectCell(EventObject anEvent) {
      this.selectEditor((MouseEvent)anEvent);
      return this.editor.shouldSelectCell(anEvent);
   }

   protected void selectEditor(MouseEvent e) {
      int row;
      if (e == null) {
         row = this.table.getSelectionModel().getAnchorSelectionIndex();
      } else {
         row = this.table.rowAtPoint(e.getPoint());
      }

      if (this.table.getEditingColumn() == 1) {
         if (row != this.table.getEditingRow()) {
            GCellEditor ed = (GCellEditor)this.editor;
            if (ed != null) {
               ed.setCanceled(true);
            }
         }

      } else {
         Object node = ((TreeTableModelAdapter)this.table.getModel()).nodeForRow(row);
         if (node != null) {
            this.editor = (TableCellEditor)this.editors.get(node);
         }

         Object value = ((TreeTableModelAdapter)this.table.getModel()).getValueAt(row, 1);
         if (this.editor == null) {
            this.label.setText(value.toString());
            this.editor = this.defaultEditor;
         }

      }
   }
}
