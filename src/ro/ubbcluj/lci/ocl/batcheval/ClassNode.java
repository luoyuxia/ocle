package ro.ubbcluj.lci.ocl.batcheval;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import ro.ubbcluj.lci.gui.tools.tree.SelectionTreeNode;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;

public class ClassNode extends SelectionTreeNode {
   protected List rules = new LinkedList();
   protected List instances = new ArrayList();

   public ClassNode(ModelElement cls) {
      super(cls);
   }

   public boolean acceptRule(RuleNode rule) {
      return !this.rules.contains(rule);
   }

   public void addRule(RuleNode rule) {
      if (this.acceptRule(rule)) {
         this.rules.add(0, rule);
      }

   }

   public void addInstance(Object i) {
      this.instances.add(i);
   }

   public List getRules() {
      return this.rules;
   }

   public List getInstances() {
      return this.instances;
   }

   public ModelElement getMetaclass() {
      return (ModelElement)this.getUserObject();
   }

   public void clearInstances() {
      this.instances.clear();
   }

   public void clearRules() {
      this.rules.clear();
   }
}
