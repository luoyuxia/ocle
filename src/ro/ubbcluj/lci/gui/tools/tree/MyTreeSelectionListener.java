package ro.ubbcluj.lci.gui.tools.tree;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;

public class MyTreeSelectionListener implements TreeSelectionListener {
   private JTree tree;

   public MyTreeSelectionListener(JTree tree) {
      this.tree = tree;
   }

   public void valueChanged(TreeSelectionEvent e) {
      SelectionTreeNode node = (SelectionTreeNode)this.tree.getLastSelectedPathComponent();
      if (node != null) {
         boolean isSelected = !node.isSelected();
         node.setSelected(isSelected);
         ((DefaultTreeModel)this.tree.getModel()).nodeChanged(node);
      }
   }
}
