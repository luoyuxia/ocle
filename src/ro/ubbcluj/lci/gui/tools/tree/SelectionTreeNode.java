package ro.ubbcluj.lci.gui.tools.tree;

import java.util.Enumeration;
import javax.swing.tree.DefaultMutableTreeNode;

public class SelectionTreeNode extends DefaultMutableTreeNode {
   public static final int SINGLE_SELECTION = 0;
   public static final int DEEP_SELECTION = 1;
   protected int selectionMode;
   protected boolean isSelected;

   public SelectionTreeNode(Object userObject) {
      this(userObject, true, true, 1);
   }

   public SelectionTreeNode(Object userObject, int selectionMode) {
      this(userObject, true, true, selectionMode);
   }

   public SelectionTreeNode(Object userObject, boolean allowsChildren, boolean isSelected) {
      this(userObject, allowsChildren, isSelected, 1);
   }

   public SelectionTreeNode(Object userObject, boolean allowsChildren, boolean isSelected, int selectionMode) {
      super(userObject, allowsChildren);
      this.isSelected = isSelected;
      this.setSelectionMode(selectionMode);
   }

   public void setSelectionMode(int mode) {
      this.selectionMode = mode;
   }

   public int getSelectionMode() {
      return this.selectionMode;
   }

   public void setSelected(boolean isSelected) {
      this.isSelected = isSelected;
      if (this.selectionMode == 1 && this.children != null) {
         Enumeration enum = this.children.elements();

         while(enum.hasMoreElements()) {
            SelectionTreeNode node = (SelectionTreeNode)enum.nextElement();
            node.setSelected(isSelected);
         }
      }

   }

   public boolean isSelected() {
      return this.isSelected;
   }
}
