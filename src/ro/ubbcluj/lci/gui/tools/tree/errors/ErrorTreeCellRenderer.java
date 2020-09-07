package ro.ubbcluj.lci.gui.tools.tree.errors;

import java.awt.Component;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import ro.ubbcluj.lci.errors.AbstractEvaluationError;
import ro.ubbcluj.lci.errors.EvaluationErrorMessage;
import ro.ubbcluj.lci.gui.tools.tree.SelectionTreeCellRenderer;
import ro.ubbcluj.lci.ocl.OclNode;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;

public class ErrorTreeCellRenderer extends DefaultTreeCellRenderer {
   private static Icon ERROR_ICON;
   private static Icon EXCEPTION_ICON;

   public ErrorTreeCellRenderer() {
   }

   public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
      super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
      DefaultMutableTreeNode tn = (DefaultMutableTreeNode)value;
      Object userObject = tn.getUserObject();
      if (tn.isRoot()) {
       //  this.setIcon(new ImageIcon((Integer.class).getResource("/resources/EvalErrorsRoot.gif")));
         this.setText(userObject.toString());
      } else if (!(userObject instanceof ModelElement) && !(userObject instanceof OclNode)) {
         if (userObject instanceof AbstractEvaluationError) {
            this.setText(((AbstractEvaluationError)userObject).getDescription());
            if (userObject instanceof EvaluationErrorMessage) {
               this.setIcon(ERROR_ICON);
            } else {
               this.setIcon(EXCEPTION_ICON);
            }
         } else {
            this.setText(userObject.toString());
            if (expanded) {
               this.setIcon(UIManager.getIcon("Tree.openIcon"));
            } else {
               this.setIcon(UIManager.getIcon("Tree.closedIcon"));
            }
         }
      } else {
         this.setIcon(SelectionTreeCellRenderer.getIconFor(userObject));
         if (userObject instanceof ModelElement) {
            this.setText(SelectionTreeCellRenderer.getTextFor((ModelElement)userObject, tn, tree));
         } else {
            this.setText(((OclNode)userObject).getValueAsString());
         }
      }

      return this;
   }

   static {
      ERROR_ICON = new ImageIcon("");
      EXCEPTION_ICON = new ImageIcon("");
   }
}
