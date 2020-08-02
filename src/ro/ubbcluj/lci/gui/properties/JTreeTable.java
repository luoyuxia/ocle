package ro.ubbcluj.lci.gui.properties;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.EventObject;
import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.LookAndFeel;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class JTreeTable extends JTable {
   protected JTreeTable.TreeTableCellRenderer tree;
   private JTreeTable.TooltipMouseListener tooltipListener = new JTreeTable.TooltipMouseListener();

   public JTreeTable(TreeTableModel treeTableModel) {
      this.tree = new JTreeTable.TreeTableCellRenderer(treeTableModel);
      this.tree.setRootVisible(false);
      this.tree.setShowsRootHandles(true);
      super.setModel(new TreeTableModelAdapter(treeTableModel, this.tree));
      JTreeTable.ListToTreeSelectionModelWrapper selectionWrapper = new JTreeTable.ListToTreeSelectionModelWrapper();
      this.tree.setSelectionModel(selectionWrapper);
      this.setSelectionModel(selectionWrapper.getListSelectionModel());
      this.setDefaultRenderer(TreeTableModel.class, this.tree);
      this.setDefaultEditor(TreeTableModel.class, new JTreeTable.TreeTableCellEditor());
      this.setShowGrid(true);
      this.getTableHeader().setReorderingAllowed(false);
      this.getTableHeader().setPreferredSize(new Dimension(this.getPreferredSize().width, 8));
   }

   public int getEditingRow() {
      return this.getColumnClass(this.editingColumn) == (TreeTableModel.class) ? -1 : this.editingRow;
   }

   public void setSelectionBackground(Color color) {
      super.setSelectionBackground(color);
      if (this.tree != null) {
         this.tree.setSelectionColor(color);
      }

   }

   public void setBackground(Color color) {
      super.setBackground(color);
      if (this.tree != null) {
         this.tree.setBackground(color);
      }

   }

   public void setRowHeight(int h) {
      super.setRowHeight(h);
      if (this.tree != null) {
         this.tree.setRowHeight(h);
      }

   }

   public void setPathsVisibility(boolean v) {
      if (v) {
         this.tree.putClientProperty("JTree.lineStyle", "Angled");
      } else {
         this.tree.putClientProperty("JTree.lineStyle", "none");
      }

   }

   public void setToolTipVisibility(boolean v) {
      if (v) {
         ToolTipManager.sharedInstance().registerComponent(this);
         this.addMouseMotionListener(this.tooltipListener);
      } else {
         ToolTipManager.sharedInstance().unregisterComponent(this);
         this.removeMouseMotionListener(this.tooltipListener);
      }

   }

   public void updateUI() {
      super.updateUI();
      if (this.tree != null) {
         this.tree.updateUI();
      }

      LookAndFeel.installColorsAndFont(this, "Tree.background", "Tree.foreground", "Tree.font");
   }

   class ListToTreeSelectionModelWrapper extends DefaultTreeSelectionModel {
      protected boolean updatingListSelectionModel;

      public ListToTreeSelectionModelWrapper() {
         this.getListSelectionModel().addListSelectionListener(this.createListSelectionListener());
         this.listSelectionModel.setSelectionMode(0);
      }

      ListSelectionModel getListSelectionModel() {
         return this.listSelectionModel;
      }

      public void resetRowSelection() {
         if (!this.updatingListSelectionModel) {
            this.updatingListSelectionModel = true;

            try {
               super.resetRowSelection();
            } finally {
               this.updatingListSelectionModel = false;
            }
         }

      }

      protected ListSelectionListener createListSelectionListener() {
         return new JTreeTable.ListToTreeSelectionModelWrapper.ListSelectionHandler();
      }

      protected void updateSelectedPathsFromSelectedRows() {
         if (!this.updatingListSelectionModel) {
            this.updatingListSelectionModel = true;

            try {
               int min = this.listSelectionModel.getMinSelectionIndex();
               int max = this.listSelectionModel.getMaxSelectionIndex();
               this.clearSelection();
               if (min != -1 && max != -1) {
                  for(int counter = min; counter <= max; ++counter) {
                     if (this.listSelectionModel.isSelectedIndex(counter)) {
                        TreePath selPath = JTreeTable.this.tree.getPathForRow(counter);
                        if (selPath != null) {
                           this.addSelectionPath(selPath);
                        }
                     }
                  }
               }
            } finally {
               this.updatingListSelectionModel = false;
            }
         }

      }

      class ListSelectionHandler implements ListSelectionListener {
         ListSelectionHandler() {
         }

         public void valueChanged(ListSelectionEvent e) {
            ListToTreeSelectionModelWrapper.this.updateSelectedPathsFromSelectedRows();
         }
      }
   }

   public class TreeTableCellEditor extends AbstractCellEditor implements TableCellEditor {
      public TreeTableCellEditor() {
      }

      public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int r, int c) {
         return JTreeTable.this.tree;
      }

      public boolean isCellEditable(EventObject e) {
         if (e instanceof MouseEvent) {
            for(int counter = JTreeTable.this.getColumnCount() - 1; counter >= 0; --counter) {
               if (JTreeTable.this.getColumnClass(counter) == (TreeTableModel.class)) {
                  MouseEvent me = (MouseEvent)e;
                  MouseEvent newME = new MouseEvent(JTreeTable.this.tree, me.getID(), me.getWhen(), me.getModifiers(), me.getX() - JTreeTable.this.getCellRect(0, counter, true).x, me.getY(), me.getClickCount(), me.isPopupTrigger());
                  JTreeTable.this.tree.dispatchEvent(newME);
                  break;
               }
            }
         }

         return false;
      }
   }

   public class TreeTableCellRenderer extends JTree implements TableCellRenderer {
      protected int visibleRow;
      protected DefaultTreeCellRenderer df = new DefaultTreeCellRenderer();

      public TreeTableCellRenderer(TreeModel model) {
         super(model);
         this.df.setOpenIcon((Icon)null);
         this.df.setClosedIcon((Icon)null);
         this.df.setLeafIcon((Icon)null);
         this.setCellRenderer(this.df);
      }

      public void setBounds(int x, int y, int w, int h) {
         super.setBounds(x, 0, w, JTreeTable.this.getHeight());
      }

      public void setSelectionColor(Color color) {
         this.df.setBackgroundSelectionColor(color);
      }

      public void setBackground(Color color) {
         super.setBackground(color);
         if (this.df != null) {
            this.df.setBackgroundNonSelectionColor(color);
         }

      }

      public void paint(Graphics g) {
         g.translate(0, -this.visibleRow * this.getRowHeight());
         super.paint(g);
      }

      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
         if (isSelected) {
            this.setBackground(table.getSelectionBackground());
         } else {
            this.setBackground(table.getBackground());
         }

         if (((PropertyModel)this.getModel()).isAllowingChildren(value)) {
            this.setFont(this.getFont().deriveFont(1));
         } else {
            this.setFont(this.getFont().deriveFont(0));
         }

         this.visibleRow = row;
         return this;
      }

      public void updateUI() {
         super.updateUI();
         TreeCellRenderer tcr = this.getCellRenderer();
         if (tcr instanceof DefaultTreeCellRenderer) {
            DefaultTreeCellRenderer dtcr = (DefaultTreeCellRenderer)tcr;
            dtcr.setTextSelectionColor(UIManager.getColor("Table.selectionForeground"));
            dtcr.setBackgroundSelectionColor(UIManager.getColor("Table.selectionBackground"));
         }

      }
   }

   private class TooltipMouseListener extends MouseMotionAdapter {
      private TooltipMouseListener() {
      }

      public void mouseMoved(MouseEvent evt) {
         if (evt.getSource() == JTreeTable.this) {
            Point p = evt.getPoint();
            int row = JTreeTable.this.rowAtPoint(p);
            int column = JTreeTable.this.columnAtPoint(p);
            if (JTreeTable.this.getModel().getValueAt(row, column) != null) {
               JTreeTable.this.setToolTipText(JTreeTable.this.getModel().getValueAt(row, column).toString());
            } else {
               JTreeTable.this.setToolTipText("");
            }
         }

      }
   }
}
