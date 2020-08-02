package ro.ubbcluj.lci.utils;

import java.awt.event.MouseEvent;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import ro.ubbcluj.lci.gui.properties.GTableCellEditor;

public class AttributeLinksTableCellEditor extends GTableCellEditor {
   public AttributeLinksTableCellEditor(JTable table) {
      super(table);
   }

   protected void selectEditor(MouseEvent evt) {
      int row;
      if (evt == null) {
         row = this.table.getSelectionModel().getAnchorSelectionIndex();
      } else {
         row = this.table.rowAtPoint(evt.getPoint());
      }

      if (row != -1) {
         this.editor = (TableCellEditor)this.editors.get(new Integer(row));
      }

      if (this.editor == null) {
         this.editor = this.defaultEditor;
      }

   }
}
