package ro.ubbcluj.lci.ocl.codegen.utils;

import ro.ubbcluj.lci.ocl.OclLetItem;
import ro.ubbcluj.lci.ocl.OclNode;
import ro.ubbcluj.lci.ocl.codegen.norm.CollectionNode;
import ro.ubbcluj.lci.ocl.codegen.norm.IfExpressionNode;
import ro.ubbcluj.lci.ocl.codegen.norm.LetExpressionNode;
import ro.ubbcluj.lci.ocl.codegen.norm.LiteralNode;
import ro.ubbcluj.lci.ocl.codegen.norm.OperatorNode;
import ro.ubbcluj.lci.ocl.codegen.norm.PostfixExpressionNode;
import ro.ubbcluj.lci.ocl.codegen.norm.PropertyCallNode;
import ro.ubbcluj.lci.ocl.codegen.norm.SyntaxTreeNode;
import ro.ubbcluj.lci.ocl.codegen.norm.SyntaxTreeVisitor;
import ro.ubbcluj.lci.ocl.codegen.norm.TupleNode;
import ro.ubbcluj.lci.ocl.codegen.norm.TuplePart;
import ro.ubbcluj.lci.ocl.codegen.norm.TypeNode;

class NameVisitor extends SyntaxTreeVisitor {
   String theName = "";

   NameVisitor() {
   }

   public void visitOperator(OperatorNode opn) {
      String text = opn.getText();
      String p2 = null;
      if ("+".equals(text)) {
         p2 = "Plus";
      } else if ("-".equals(text)) {
         p2 = "Minus";
      } else if ("*".equals(text)) {
         p2 = "Multiply";
      } else if ("/".equals(text)) {
         p2 = "Divide";
      } else if (">".equals(text)) {
         p2 = "Greater";
      } else if ("<".equals(text)) {
         p2 = "LessThan";
      } else if ("<=".equals(text)) {
         p2 = "LessOrEqual";
      } else if (">=".equals(text)) {
         p2 = "GreaterOrEqual";
      } else if ("=".equals(text)) {
         p2 = "Equals";
      } else if ("<>".equals(text)) {
         p2 = "NotEquals";
      } else {
         p2 = Character.toUpperCase(text.charAt(0)) + text.substring(1);
      }

      this.theName = OclCodeGenUtilities.getPrefix(opn.getType()) + p2;
   }

   public void visitCollection(CollectionNode cn) {
      this.theName = OclCodeGenUtilities.getPrefix(cn.getType());
   }

   public void visitTuple(TupleNode tn) {
      this.theName = "tuple";
   }

   public void visitType(TypeNode tn) {
      String realType = tn.getType().element.name;
      this.theName = "type" + Character.toLowerCase(realType.charAt(0)) + realType.substring(1);
   }

   public void visitPostfixExpression(PostfixExpressionNode pe) {
      ((SyntaxTreeNode)pe.getLastChild()).accept(this);
   }

   public void visitLiteral(LiteralNode ln) {
   }

   public void visitIfExpression(IfExpressionNode ifen) {
      this.theName = OclCodeGenUtilities.getPrefix(ifen.getType()) + "If";
   }

   public void visitLetExpression(LetExpressionNode len) {
      this.theName = OclCodeGenUtilities.getPrefix(len.getType()) + "Let";
   }

   public void visitPropertyCall(PropertyCallNode pcn) {
      Object o = pcn.getUserObject();
      String prefix = OclCodeGenUtilities.getPrefix(pcn.getType());
      if (o instanceof OclNode) {
         OclNode nd = (OclNode)o;
         String temp = nd.firstToken().getValue();
         temp = Character.toUpperCase(temp.charAt(0)) + temp.substring(1);
         this.theName = prefix + temp;
      } else {
         if (!(o instanceof OclLetItem)) {
            throw new InternalError("Invalid user object for property call " + o);
         }

         OclLetItem ocllet = (OclLetItem)o;
         this.theName = prefix + ocllet.name;
      }

   }

   public void visitTuplePart(TuplePart tp) {
   }
}
