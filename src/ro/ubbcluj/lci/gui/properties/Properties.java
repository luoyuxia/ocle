package ro.ubbcluj.lci.gui.properties;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.ArrayList;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;

public class Properties extends JPanel implements Serializable, CellEditorListener {
   protected Color selectionColor = UIManager.getColor("Table.selectionBackground");
   protected int rowHeight = 20;
   protected Dimension dimension = new Dimension(300, 450);
   protected boolean showingPaths = true;
   protected boolean showingTooltips = false;
   protected int editingClickCount = 2;
   protected boolean readOnly = true;
   protected String title = "Properties";
   protected ArrayList listeners = new ArrayList();
   protected transient JTreeTable table;
   public transient PropertyModel model;
   protected transient JLabel tableHeader;
   protected transient JScrollPane scrollPane;
   protected transient GTableCellEditor editor;
   protected transient Object element;

   public Properties() {
      try {
         this.setLayout(new BorderLayout());
         this.model = new PropertyModel((Object)null);
         this.model.setReadOnly(this.readOnly);
         this.table = new JTreeTable(this.model);
         this.editor = new GTableCellEditor(this.table);
         this.table.setDefaultEditor(String.class, this.editor);
         this.table.setSelectionBackground(this.selectionColor);
         this.table.setRowHeight(this.rowHeight);
         this.table.setPathsVisibility(this.showingPaths);
         this.table.setToolTipVisibility(this.showingTooltips);
         this.table.setDragEnabled(false);
         this.scrollPane = new JScrollPane(this.table);
         this.tableHeader = new JLabel(this.title);
         this.tableHeader.setHorizontalAlignment(0);
         this.tableHeader.setForeground(Color.black);
         this.add(this.tableHeader, "North");
         this.add(this.scrollPane, "Center");
         Color backgroundColor = this.scrollPane.getBackground();
         this.table.setBackground(backgroundColor);
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public Color getSelectionColor() {
      return this.selectionColor;
   }

   public void setSelectionColor(Color newSelectionColor) {
      if (this.selectionColor != newSelectionColor) {
         this.selectionColor = newSelectionColor;
         this.table.setSelectionBackground(this.selectionColor);
      }

   }

   public int getRowHeight() {
      return this.rowHeight;
   }

   public void setRowHeight(int newRowHeight) {
      if (this.rowHeight != newRowHeight) {
         this.rowHeight = newRowHeight;
         this.table.setRowHeight(this.rowHeight);
      }

   }

   public Dimension getDimension() {
      return this.dimension;
   }

   public void setDimension(Dimension newDimension) {
      Dimension old = this.dimension;

      try {
         this.dimension = newDimension;
      } catch (Exception var4) {
         this.dimension = old;
      }

      if (this.dimension != old) {
         this.repaint();
      }

   }

   public boolean isShowingPaths() {
      return this.showingPaths;
   }

   public void setShowingPaths(boolean newShowingPaths) {
      if (this.showingPaths != newShowingPaths) {
         this.showingPaths = newShowingPaths;
         this.table.setPathsVisibility(this.showingPaths);
      }

   }

   public boolean isShowingTooltips() {
      return this.showingTooltips;
   }

   public void setShowingTooltips(boolean newShowingTooltips) {
      if (this.showingTooltips != newShowingTooltips) {
         this.showingTooltips = newShowingTooltips;
         this.table.setToolTipVisibility(this.showingTooltips);
      }

   }

   public int getEditingClickCount() {
      return this.editingClickCount;
   }

   public void setEditingClickCount(int newEditingClickCount) {
      this.editingClickCount = newEditingClickCount;
      this.updateView(this.element);
   }

   public boolean isReadOnly() {
      return this.readOnly;
   }

   public void setReadOnly(boolean newReadOnly) {
      if (this.readOnly != newReadOnly) {
         this.readOnly = newReadOnly;
         this.model.setReadOnly(this.readOnly);
      }

   }

   public String getTitle() {
      return this.title;
   }

   public void setTitle(String newTitle) {
      if (!this.title.equals(newTitle)) {
         this.title = newTitle;
         this.tableHeader.setText(this.title);
         this.tableHeader.repaint();
      }

   }

   public void addPropertyListener(PropertyListener l) {
      this.listeners.add(l);
   }

   public void removePropertyListener(PropertyListener l) {
      this.listeners.remove(l);
   }

   public void firePropertyEvent(PropertyEvent evt) {
      int i;
      if (evt.getProperty() == null) {
         for(i = 0; i < this.listeners.size(); ++i) {
            ((PropertyListener)this.listeners.get(i)).subjectChanged(evt);
         }
      } else {
         for(i = 0; i < this.listeners.size(); ++i) {
            ((PropertyListener)this.listeners.get(i)).valueChanged(evt);
         }
      }

   }

   public Object getElement() {
      return this.element;
   }

   public Dimension getPreferredSize() {
      return this.dimension;
   }

   public void updateView(Object element) {
      try {
         this.element = element;
         this.table.setToolTipVisibility(false);
         this.model = new PropertyModel(element);
         this.model.setReadOnly(this.readOnly);
         this.table = new JTreeTable(this.model);
         this.editor = new GTableCellEditor(this.table);
         this.editor.setClicks(this.editingClickCount);
         this.table.setDefaultEditor(String.class, this.editor);
         this.table.setDefaultRenderer(String.class, new Properties.Renderer());
         this.table.setSelectionBackground(this.selectionColor);
         this.table.setRowHeight(this.rowHeight);
         this.table.setPathsVisibility(this.showingPaths);
         this.table.setToolTipVisibility(this.showingTooltips);
         Color backgroundColor = this.scrollPane.getBackground();
         this.table.setBackground(backgroundColor);
         this.setPropertyEditors((PropertyNode)this.model.getRoot());
         this.scrollPane.getViewport().remove(0);
         this.scrollPane.getViewport().add(this.table, 0);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   private void setPropertyEditors(PropertyNode node) {
      for(int i = 0; i < node.getChildren().length; ++i) {
         PropertyNode pNode = (PropertyNode)node.getChildren()[i];
         if (pNode.isEditable()) {
            pNode.getProperty().getEditor().addCellEditorListener(this);
            this.editor.setEditorAt(pNode, pNode.getProperty().getEditor());
         }

         if (pNode.getChildCount() > 0) {
            this.setPropertyEditors(pNode);
         }
      }

   }

   public void editingCanceled(ChangeEvent evt) {
   }

   public void editingStopped(ChangeEvent evt) {
      GCellEditor ed = (GCellEditor)evt.getSource();
      if (ed.isCanceled()) {
         this.editingCanceled(evt);
         ed.setCanceled(false);
      } else {
         PropertyNode target = ed.getTarget();
         Component comp = ed.getComponent();
         if (comp instanceof JPanel) {
            comp = ed.getNestedEditorComponent();
         }

         if (comp instanceof JumpButton) {
            this.updateView(((JumpButton)comp).getDestination());
            this.firePropertyEvent(new PropertyEvent(this, this.element, (String)null, ((JumpButton)comp).getDestination()));
         } else if (comp instanceof JComboBox) {
            this.firePropertyEvent(new PropertyEvent(this, this.element, target.getProperty().getName(), ((JComboBox)comp).getSelectedItem()));
         } else if (comp instanceof JTextField) {
            this.firePropertyEvent(new PropertyEvent(this, this.element, target.getProperty().getName(), ((JTextField)comp).getText()));
         } else if (comp instanceof JCheckBox) {
            this.firePropertyEvent(new PropertyEvent(this, this.element, target.getProperty().getName(), new Boolean(((JCheckBox)comp).isSelected())));
         }

      }
   }

   class Renderer extends DefaultTableCellRenderer {
      Renderer() {
      }

      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
         Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
         if (isSelected) {
            TableCellEditor editor = table.getCellEditor(row, column);
            int y = table.getRowHeight() * row + 3;
            int x = table.getColumnModel().getColumn(0).getWidth() + 3;
            editor.shouldSelectCell(new MouseEvent(table, 501, 0L, 1, x, y, 1, false));
            c = table.getCellEditor(row, column).getTableCellEditorComponent(table, value, isSelected, row, column);
         }

         JPanel p = new JPanel(new GridBagLayout());
         if (isSelected) {
            p.setBackground(table.getSelectionBackground());
         }

         p.add(c, new GridBagConstraints(0, 0, 1, 1, 1.0D, 1.0D, 10, 1, new Insets(1, 1, 1, 1), 0, 0));
         return p;
      }
   }
}
