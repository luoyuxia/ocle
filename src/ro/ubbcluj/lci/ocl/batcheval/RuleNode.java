package ro.ubbcluj.lci.ocl.batcheval;

import ro.ubbcluj.lci.gui.tools.tree.SelectionTreeNode;
import ro.ubbcluj.lci.ocl.nodes.oclExpression;
import ro.ubbcluj.lci.utils.Utils;

public class RuleNode extends SelectionTreeNode {
   protected String ruleName;

   public RuleNode(oclExpression r) {
      super(r);
      this.ruleName = Utils.getRuleName(r);
   }

   public oclExpression getRule() {
      return (oclExpression)this.getUserObject();
   }

   public String getRuleName() {
      return this.ruleName;
   }
}
