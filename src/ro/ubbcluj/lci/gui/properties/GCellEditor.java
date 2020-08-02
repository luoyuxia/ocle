package ro.ubbcluj.lci.gui.properties;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class GCellEditor extends DefaultCellEditor {
   protected boolean canceled;
   protected PropertyNode theTarget;
   protected JComponent nestedEditorComponent = null;

   public GCellEditor(JCheckBox comp) {
      super(comp);
   }

   public GCellEditor(JComboBox comboBox) {
      super(comboBox);
   }

   public GCellEditor(JTextField comp) {
      super(comp);
   }

   public GCellEditor(final JButton jumpButton) {
      super(new JCheckBox());
      this.editorComponent = jumpButton;
      this.delegate = new EditorDelegate() {
         public void setValue(Object value) {
            jumpButton.setText((String)value);
         }

         public Object getCellEditorValue() {
            return jumpButton.getText();
         }
      };
      jumpButton.addActionListener(this.delegate);
   }

   public GCellEditor(JPanel panel) {
      super(new JCheckBox());
      this.editorComponent = panel;
      this.delegate = new EditorDelegate() {
         public void actionPerformed(ActionEvent evt) {
            GCellEditor.this.nestedEditorComponent = (JComponent)evt.getSource();
            super.actionPerformed(evt);
         }
      };

      for(int i = 0; i < panel.getComponentCount(); ++i) {
         Component c = panel.getComponent(i);
         if (c instanceof JButton) {
            ((JButton)c).addActionListener(this.delegate);
         } else if (c instanceof JComboBox) {
            ((JComboBox)c).addActionListener(this.delegate);
         } else if (c instanceof JTextField) {
            ((JTextField)c).addActionListener(this.delegate);
         }
      }

   }

   public GCellEditor(JComponent comp) {
      super(new JCheckBox());
      this.editorComponent = comp;
   }

   public void setCanceled(boolean b) {
      this.canceled = b;
   }

   public boolean isCanceled() {
      return this.canceled;
   }

   public void setTarget(PropertyNode target) {
      this.theTarget = target;
   }

   public PropertyNode getTarget() {
      return this.theTarget;
   }

   public JComponent getNestedEditorComponent() {
      return this.nestedEditorComponent;
   }

   public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
      return this.editorComponent;
   }
}
