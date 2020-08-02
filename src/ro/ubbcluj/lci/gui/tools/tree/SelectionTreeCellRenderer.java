package ro.ubbcluj.lci.gui.tools.tree;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.LayoutManager;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeNode;
import ro.ubbcluj.lci.ocl.OclNode;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.uml.modelManagement.Model;
import ro.ubbcluj.lci.uml.modelManagement.Package;
import ro.ubbcluj.lci.utils.Utils;

public class SelectionTreeCellRenderer extends JPanel implements TreeCellRenderer {
   private JCheckBox check;
   private SelectionTreeCellRenderer.TreeLabel label;
   private static final ImageIcon OCL_EXPRESSION_ICON;
   private static final ImageIcon PACKAGE_ICON;
   private static final ImageIcon CLASS_ICON;

   public SelectionTreeCellRenderer() {
      this.setLayout((LayoutManager)null);
      this.add(this.check = new JCheckBox());
      this.add(this.label = new SelectionTreeCellRenderer.TreeLabel());
      this.check.setBackground(UIManager.getColor("Tree.textBackground"));
   }

   public Component getTreeCellRendererComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
      SelectionTreeNode node = (SelectionTreeNode)value;
      Object obj = node.getUserObject();
      this.setEnabled(tree.isEnabled());
      this.check.setSelected(node.isSelected());
      this.label.setFont(tree.getFont());
      if (node.isRoot()) {
         this.label.setIcon(new ImageIcon((Integer.class).getResource("/resources/EvalModelsRoot.gif")));
         this.label.setText(obj.toString());
      } else {
         if (obj instanceof ModelElement) {
            this.label.setText(getTextFor((ModelElement)obj, node, tree));
         } else {
            this.label.setText(tree.convertValueToText(value, isSelected, expanded, leaf, row, hasFocus));
         }

         this.label.setSelected(isSelected);
         this.label.setFocus(hasFocus);
         Icon icon = getIconFor(obj);
         this.label.setIcon(icon);
      }

      return this;
   }

   public Dimension getPreferredSize() {
      Dimension d_check = this.check.getPreferredSize();
      Dimension d_label = this.label.getPreferredSize();
      return new Dimension(d_check.width + d_label.width, d_check.height < d_label.height ? d_label.height : d_check.height);
   }

   public void doLayout() {
      Dimension d_check = this.check.getPreferredSize();
      Dimension d_label = this.label.getPreferredSize();
      int y_check = 0;
      int y_label = 0;
      if (d_check.height < d_label.height) {
         y_check = (d_label.height - d_check.height) / 2;
      } else {
         y_label = (d_check.height - d_label.height) / 2;
      }

      this.check.setLocation(0, y_check);
      this.check.setBounds(0, y_check, d_check.width, d_check.height);
      this.label.setLocation(d_check.width, y_label);
      this.label.setBounds(d_check.width, y_label, d_label.width, d_label.height);
   }

   public void setBackground(Color color) {
      if (color instanceof ColorUIResource) {
         color = null;
      }

      super.setBackground(color);
   }

   public static Icon getIconFor(Object obj) {
      if (obj instanceof OclNode) {
         return OCL_EXPRESSION_ICON;
      } else if (obj instanceof Package) {
         return PACKAGE_ICON;
      } else {
         return (Icon)(obj instanceof Classifier ? CLASS_ICON : UIManager.getIcon("Tree.openIcon"));
      }
   }

   public static String getTextFor(ModelElement me, TreeNode current, JTree tree) {
      if (me instanceof Model) {
         DefaultMutableTreeNode parent = (DefaultMutableTreeNode)current.getParent();
         String name = me.getName();
         if (parent == tree.getModel().getRoot() && !(parent.getUserObject() instanceof ModelElement)) {
            int index = parent.getIndex(current);
            return index == 0 ? "UML metamodel(" + name + ')' : "User model(" + name + ')';
         } else {
            return parent == null ? "User model(" + name + ')' : name;
         }
      } else {
         return Utils.name(me);
      }
   }

   static {
      CLASS_ICON = new ImageIcon((Integer.class).getResource("/resources/Class.gif"));
      PACKAGE_ICON = new ImageIcon((Integer.class).getResource("/resources/Package.gif"));
      OCL_EXPRESSION_ICON = new ImageIcon((Integer.class).getResource("/resources/ocl.gif"));
   }

   private class TreeLabel extends JLabel {
      boolean isSelected;
      boolean hasFocus;

      TreeLabel() {
      }

      public void setBackground(Color color) {
         if (color instanceof ColorUIResource) {
            color = null;
         }

         super.setBackground(color);
      }

      public void paint(Graphics g) {
         String str = this.getText();
         if (str != null && 0 < str.length()) {
            if (this.isSelected) {
               g.setColor(UIManager.getColor("Tree.selectionBackground"));
            } else {
               g.setColor(UIManager.getColor("Tree.textBackground"));
            }

            Dimension d = this.getPreferredSize();
            int imageOffset = 0;
            Icon currentI = this.getIcon();
            if (currentI != null) {
               imageOffset = currentI.getIconWidth() + Math.max(0, this.getIconTextGap() - 1);
            }

            g.fillRect(imageOffset, 0, d.width - 1 - imageOffset, d.height);
            if (this.hasFocus) {
               g.setColor(UIManager.getColor("Tree.selectionBorderColor"));
               g.drawRect(imageOffset, 0, d.width - 1 - imageOffset, d.height - 1);
            }
         }

         super.paint(g);
      }

      public Dimension getPreferredSize() {
         Dimension retDimension = super.getPreferredSize();
         if (retDimension != null) {
            retDimension.width += 3;
         }

         return retDimension;
      }

      void setSelected(boolean isSelected) {
         this.isSelected = isSelected;
      }

      void setFocus(boolean hasFocus) {
         this.hasFocus = hasFocus;
      }
   }
}
