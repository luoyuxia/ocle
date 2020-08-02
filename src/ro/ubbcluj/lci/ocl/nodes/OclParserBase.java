package ro.ubbcluj.lci.ocl.nodes;

import ro.ubbcluj.lci.ocl.OclNode;

public abstract class OclParserBase {
   protected static final String[] tokens = new String[]{"#", "(", ")", "*", "+", ",", "-", "->", ".", "..", "/", ":", "::", ";", "<", "<=", "<>", "=", ">", ">=", "?", "@", "Bag", "Collection", "OrderedSet", "Sequence", "Set", "Tuple", "TupleType", "[", "]", "^", "^^", "and", "body", "context", "def", "div", "else", "endif", "endmodel", "endpackage", "if", "implies", "in", "inv", "let", "mod", "model", "not", "option", "or", "package", "post", "pre", "then", "xor", "{", "|", "}", "NAME", "NUMBER", "STRING"};
   protected static int STRING;
   protected static int NUMBER;
   protected static int NAME;
   protected int k;

   public OclParserBase() {
   }

   protected abstract boolean match(OclNode var1, int var2);

   protected abstract boolean complete(OclNode var1, OclNode var2, int var3);

   protected boolean oclFile(OclNode parent) {
      oclFile node = new oclFile();
      int kk = this.k;
      if (this.oclFile_1(node) && this.oclFile_3(node)) {
         return this.complete(node, parent, kk);
      } else {
         this.k = kk;
         node.children.clear();
         return false;
      }
   }

   private boolean option(OclNode parent) {
      option node = new option();
      int kk = this.k;
      if (this.match(node, 50) && this.name(node)) {
         return this.complete(node, parent, kk);
      } else {
         this.k = kk;
         node.children.clear();
         return false;
      }
   }

   private boolean oclPackage(OclNode parent) {
      oclPackage node = new oclPackage();
      int kk = this.k;
      if (this.oclPackage_1(node)) {
         return this.complete(node, parent, kk);
      } else {
         this.k = kk;
         node.children.clear();
         return false;
      }
   }

   private boolean freeConstraint(OclNode parent) {
      freeConstraint node = new freeConstraint();
      int kk = this.k;
      if (this.freeConstraint_1(node)) {
         return this.complete(node, parent, kk);
      } else {
         this.k = kk;
         node.children.clear();
         return false;
      }
   }

   private boolean oclExpressions(OclNode parent) {
      oclExpressions node = new oclExpressions();
      int kk = this.k;
      if (this.oclExpressions_2(node)) {
         return this.complete(node, parent, kk);
      } else {
         this.k = kk;
         node.children.clear();
         return false;
      }
   }

   private boolean constraint(OclNode parent) {
      constraint node = new constraint();
      int kk = this.k;
      if (this.contextDeclaration(node) && this.constraint_2(node)) {
         return this.complete(node, parent, kk);
      } else {
         this.k = kk;
         node.children.clear();
         return false;
      }
   }

   private boolean contextDeclaration(OclNode parent) {
      contextDeclaration node = new contextDeclaration();
      int kk = this.k;
      if (this.match(node, 35) && this.contextDeclaration_1(node)) {
         return this.complete(node, parent, kk);
      } else {
         this.k = kk;
         node.children.clear();
         return false;
      }
   }

   private boolean classifierContext(OclNode parent) {
      classifierContext node = new classifierContext();
      int kk = this.k;
      if (this.classifierContext_1(node) && this.classifierContext_2(node)) {
         return this.complete(node, parent, kk);
      } else {
         this.k = kk;
         node.children.clear();
         return false;
      }
   }

   private boolean operationContext(OclNode parent) {
      operationContext node = new operationContext();
      int kk = this.k;
      if (this.operationContext_1(node) && this.match(node, 12) && this.operationName(node) && this.match(node, 1) && this.formalParameterList(node) && this.match(node, 2) && this.operationContext_2(node)) {
         return this.complete(node, parent, kk);
      } else {
         this.k = kk;
         node.children.clear();
         return false;
      }
   }

   private boolean stereotype(OclNode parent) {
      stereotype node = new stereotype();
      int kk = this.k;
      if (this.stereotype_1(node)) {
         return this.complete(node, parent, kk);
      } else {
         this.k = kk;
         node.children.clear();
         return false;
      }
   }

   private boolean operationName(OclNode parent) {
      operationName node = new operationName();
      int kk = this.k;
      if (this.operationName_1(node)) {
         return this.complete(node, parent, kk);
      } else {
         this.k = kk;
         node.children.clear();
         return false;
      }
   }

   private boolean formalParameterList(OclNode parent) {
      formalParameterList node = new formalParameterList();
      int kk = this.k;
      if (this.formalParameterList_1(node)) {
         return this.complete(node, parent, kk);
      } else {
         this.k = kk;
         node.children.clear();
         return false;
      }
   }

   private boolean oclExpression(OclNode parent) {
      oclExpression node = new oclExpression();
      int kk = this.k;
      if (this.expression(node)) {
         return this.complete(node, parent, kk);
      } else {
         this.k = kk;
         node.children.clear();
         return false;
      }
   }

   private boolean returnType(OclNode parent) {
      returnType node = new returnType();
      int kk = this.k;
      if (this.typeSpecifier(node)) {
         return this.complete(node, parent, kk);
      } else {
         this.k = kk;
         node.children.clear();
         return false;
      }
   }

   private boolean letExpression(OclNode parent) {
      letExpression node = new letExpression();
      int kk = this.k;
      if (this.match(node, 46) && this.operationName(node) && this.letExpression_1(node) && this.letExpression_2(node) && this.letExpression_3(node)) {
         return this.complete(node, parent, kk);
      } else {
         this.k = kk;
         node.children.clear();
         return false;
      }
   }

   private boolean typeSpecifier(OclNode parent) {
      typeSpecifier node = new typeSpecifier();
      int kk = this.k;
      if (this.typeSpecifier_1(node)) {
         return this.complete(node, parent, kk);
      } else {
         this.k = kk;
         node.children.clear();
         return false;
      }
   }

   private boolean collectionType(OclNode parent) {
      collectionType node = new collectionType();
      int kk = this.k;
      if (this.collectionKind(node) && this.match(node, 1) && this.typeSpecifier(node) && this.match(node, 2)) {
         return this.complete(node, parent, kk);
      } else {
         this.k = kk;
         node.children.clear();
         return false;
      }
   }

   private boolean tupleType(OclNode parent) {
      tupleType node = new tupleType();
      int kk = this.k;
      if (this.match(node, 28) && this.match(node, 1) && this.name(node) && this.match(node, 11) && this.typeSpecifier(node) && this.tupleType_1(node) && this.match(node, 2)) {
         return this.complete(node, parent, kk);
      } else {
         this.k = kk;
         node.children.clear();
         return false;
      }
   }

   private boolean expression(OclNode parent) {
      expression node = new expression();
      int kk = this.k;
      if (this.expression_1(node) && this.untilExpression(node)) {
         return this.complete(node, parent, kk);
      } else {
         this.k = kk;
         node.children.clear();
         return false;
      }
   }

   private boolean ifExpression(OclNode parent) {
      ifExpression node = new ifExpression();
      int kk = this.k;
      if (this.match(node, 42) && this.expression(node) && this.match(node, 55) && this.expression(node) && this.match(node, 38) && this.expression(node) && this.match(node, 39)) {
         return this.complete(node, parent, kk);
      } else {
         this.k = kk;
         node.children.clear();
         return false;
      }
   }

   private boolean untilExpression(OclNode parent) {
      untilExpression node = new untilExpression();
      int kk = this.k;
      if (this.logicalExpression(node)) {
         return this.complete(node, parent, kk);
      } else {
         this.k = kk;
         node.children.clear();
         return false;
      }
   }

   private boolean logicalExpression(OclNode parent) {
      logicalExpression node = new logicalExpression();
      int kk = this.k;
      if (this.relationalExpression(node) && this.logicalExpression_1(node)) {
         return this.complete(node, parent, kk);
      } else {
         this.k = kk;
         node.children.clear();
         return false;
      }
   }

   private boolean relationalExpression(OclNode parent) {
      relationalExpression node = new relationalExpression();
      int kk = this.k;
      if (this.additiveExpression(node) && this.relationalExpression_1(node)) {
         return this.complete(node, parent, kk);
      } else {
         this.k = kk;
         node.children.clear();
         return false;
      }
   }

   private boolean additiveExpression(OclNode parent) {
      additiveExpression node = new additiveExpression();
      int kk = this.k;
      if (this.multiplicativeExpression(node) && this.additiveExpression_1(node)) {
         return this.complete(node, parent, kk);
      } else {
         this.k = kk;
         node.children.clear();
         return false;
      }
   }

   private boolean multiplicativeExpression(OclNode parent) {
      multiplicativeExpression node = new multiplicativeExpression();
      int kk = this.k;
      if (this.unaryExpression(node) && this.multiplicativeExpression_1(node)) {
         return this.complete(node, parent, kk);
      } else {
         this.k = kk;
         node.children.clear();
         return false;
      }
   }

   private boolean unaryExpression(OclNode parent) {
      unaryExpression node = new unaryExpression();
      int kk = this.k;
      if (this.unaryExpression_1(node)) {
         return this.complete(node, parent, kk);
      } else {
         this.k = kk;
         node.children.clear();
         return false;
      }
   }

   private boolean postfixExpression(OclNode parent) {
      postfixExpression node = new postfixExpression();
      int kk = this.k;
      if (this.primaryExpression(node) && this.postfixExpression_1(node)) {
         return this.complete(node, parent, kk);
      } else {
         this.k = kk;
         node.children.clear();
         return false;
      }
   }

   private boolean primaryExpression(OclNode parent) {
      primaryExpression node = new primaryExpression();
      int kk = this.k;
      if (this.primaryExpression_1(node)) {
         return this.complete(node, parent, kk);
      } else {
         this.k = kk;
         node.children.clear();
         return false;
      }
   }

   private boolean propertyCallParameters(OclNode parent) {
      propertyCallParameters node = new propertyCallParameters();
      int kk = this.k;
      if (this.match(node, 1) && this.propertyCallParameters_1(node) && this.propertyCallParameters_2(node) && this.match(node, 2)) {
         return this.complete(node, parent, kk);
      } else {
         this.k = kk;
         node.children.clear();
         return false;
      }
   }

   private boolean literal(OclNode parent) {
      literal node = new literal();
      int kk = this.k;
      if (this.literal_1(node)) {
         return this.complete(node, parent, kk);
      } else {
         this.k = kk;
         node.children.clear();
         return false;
      }
   }

   private boolean enumLiteral(OclNode parent) {
      enumLiteral node = new enumLiteral();
      int kk = this.k;
      if (this.enumLiteral_1(node) && this.match(node, 0) && this.name(node)) {
         return this.complete(node, parent, kk);
      } else {
         this.k = kk;
         node.children.clear();
         return false;
      }
   }

   private boolean simpleTypeSpecifier(OclNode parent) {
      simpleTypeSpecifier node = new simpleTypeSpecifier();
      int kk = this.k;
      if (this.pathName(node)) {
         return this.complete(node, parent, kk);
      } else {
         this.k = kk;
         node.children.clear();
         return false;
      }
   }

   private boolean literalCollection(OclNode parent) {
      literalCollection node = new literalCollection();
      int kk = this.k;
      if (this.collectionKind(node) && this.match(node, 57) && this.literalCollection_1(node) && this.match(node, 59)) {
         return this.complete(node, parent, kk);
      } else {
         this.k = kk;
         node.children.clear();
         return false;
      }
   }

   private boolean collectionItem(OclNode parent) {
      collectionItem node = new collectionItem();
      int kk = this.k;
      if (this.expression(node) && this.collectionItem_1(node)) {
         return this.complete(node, parent, kk);
      } else {
         this.k = kk;
         node.children.clear();
         return false;
      }
   }

   private boolean literalTuple(OclNode parent) {
      literalTuple node = new literalTuple();
      int kk = this.k;
      if (this.match(node, 27) && this.match(node, 57) && this.name(node) && this.literalTuple_1(node) && this.match(node, 17) && this.expression(node) && this.literalTuple_2(node) && this.match(node, 59)) {
         return this.complete(node, parent, kk);
      } else {
         this.k = kk;
         node.children.clear();
         return false;
      }
   }

   private boolean propertyCall(OclNode parent) {
      propertyCall node = new propertyCall();
      int kk = this.k;
      if (this.propertyCall_1(node) && this.propertyCall_2(node) && this.propertyCall_3(node) && this.propertyCall_4(node)) {
         return this.complete(node, parent, kk);
      } else {
         this.k = kk;
         node.children.clear();
         return false;
      }
   }

   private boolean qualifiers(OclNode parent) {
      qualifiers node = new qualifiers();
      int kk = this.k;
      if (this.match(node, 29) && this.actualParameterList(node) && this.match(node, 30)) {
         return this.complete(node, parent, kk);
      } else {
         this.k = kk;
         node.children.clear();
         return false;
      }
   }

   private boolean declarator(OclNode parent) {
      declarator node = new declarator();
      int kk = this.k;
      if (this.name(node) && this.declarator_1(node) && this.declarator_2(node) && this.declarator_3(node) && this.match(node, 58)) {
         return this.complete(node, parent, kk);
      } else {
         this.k = kk;
         node.children.clear();
         return false;
      }
   }

   private boolean pathName(OclNode parent) {
      pathName node = new pathName();
      int kk = this.k;
      if (this.name(node) && this.pathName_1(node)) {
         return this.complete(node, parent, kk);
      } else {
         this.k = kk;
         node.children.clear();
         return false;
      }
   }

   private boolean dottedPathName(OclNode parent) {
      dottedPathName node = new dottedPathName();
      int kk = this.k;
      if (this.name(node) && this.dottedPathName_1(node)) {
         return this.complete(node, parent, kk);
      } else {
         this.k = kk;
         node.children.clear();
         return false;
      }
   }

   private boolean timeExpression(OclNode parent) {
      timeExpression node = new timeExpression();
      int kk = this.k;
      if (this.match(node, 21) && this.match(node, 54)) {
         return this.complete(node, parent, kk);
      } else {
         this.k = kk;
         node.children.clear();
         return false;
      }
   }

   private boolean actualParameterList(OclNode parent) {
      actualParameterList node = new actualParameterList();
      int kk = this.k;
      if (this.actualParameterList_1(node) && this.actualParameterList_2(node)) {
         return this.complete(node, parent, kk);
      } else {
         this.k = kk;
         node.children.clear();
         return false;
      }
   }

   private boolean logicalOperator(OclNode parent) {
      logicalOperator node = new logicalOperator();
      int kk = this.k;
      if (this.logicalOperator_1(node)) {
         return this.complete(node, parent, kk);
      } else {
         this.k = kk;
         node.children.clear();
         return false;
      }
   }

   private boolean collectionKind(OclNode parent) {
      collectionKind node = new collectionKind();
      int kk = this.k;
      if (this.collectionKind_1(node)) {
         return this.complete(node, parent, kk);
      } else {
         this.k = kk;
         node.children.clear();
         return false;
      }
   }

   private boolean relationalOperator(OclNode parent) {
      relationalOperator node = new relationalOperator();
      int kk = this.k;
      if (this.relationalOperator_1(node)) {
         return this.complete(node, parent, kk);
      } else {
         this.k = kk;
         node.children.clear();
         return false;
      }
   }

   private boolean addOperator(OclNode parent) {
      addOperator node = new addOperator();
      int kk = this.k;
      if (this.addOperator_1(node)) {
         return this.complete(node, parent, kk);
      } else {
         this.k = kk;
         node.children.clear();
         return false;
      }
   }

   private boolean multiplyOperator(OclNode parent) {
      multiplyOperator node = new multiplyOperator();
      int kk = this.k;
      if (this.multiplyOperator_1(node)) {
         return this.complete(node, parent, kk);
      } else {
         this.k = kk;
         node.children.clear();
         return false;
      }
   }

   private boolean unaryOperator(OclNode parent) {
      unaryOperator node = new unaryOperator();
      int kk = this.k;
      if (this.unaryOperator_1(node)) {
         return this.complete(node, parent, kk);
      } else {
         this.k = kk;
         node.children.clear();
         return false;
      }
   }

   private boolean name(OclNode parent) {
      name node = new name();
      int kk = this.k;
      if (this.match(node, NAME)) {
         return this.complete(node, parent, kk);
      } else {
         this.k = kk;
         node.children.clear();
         return false;
      }
   }

   private boolean string(OclNode parent) {
      string node = new string();
      int kk = this.k;
      if (this.match(node, STRING)) {
         return this.complete(node, parent, kk);
      } else {
         this.k = kk;
         node.children.clear();
         return false;
      }
   }

   private boolean number(OclNode parent) {
      number node = new number();
      int kk = this.k;
      if (this.match(node, NUMBER)) {
         return this.complete(node, parent, kk);
      } else {
         this.k = kk;
         node.children.clear();
         return false;
      }
   }

   private boolean oclFile_1(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.option(parent) && this.oclFile_1(parent)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         return true;
      }
   }

   private boolean oclFile_3(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.oclFile_4(parent) && this.oclFile_2(parent)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         return false;
      }
   }

   private boolean oclFile_2(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.oclFile_4(parent) && this.oclFile_2(parent)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         return true;
      }
   }

   private boolean oclPackage_1(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.match(parent, 52) && this.pathName(parent) && this.oclExpressions(parent) && this.match(parent, 41)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         if (this.constraint(parent)) {
            return true;
         } else {
            this.k = kk;
            parent.children.setSize(ll);
            if (this.freeConstraint(parent)) {
               return true;
            } else {
               this.k = kk;
               parent.children.setSize(ll);
               return false;
            }
         }
      }
   }

   private boolean freeConstraint_1(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.match(parent, 45) && this.freeConstraint_2(parent) && this.match(parent, 11) && this.oclExpression(parent)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         if (this.match(parent, 20) && this.expression(parent)) {
            return true;
         } else {
            this.k = kk;
            parent.children.setSize(ll);
            return false;
         }
      }
   }

   private boolean oclExpressions_2(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.constraint(parent) && this.oclExpressions_1(parent)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         return false;
      }
   }

   private boolean oclExpressions_1(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.constraint(parent) && this.oclExpressions_1(parent)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         return true;
      }
   }

   private boolean constraint_2(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.constraint_3(parent) && this.constraint_1(parent)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         return false;
      }
   }

   private boolean constraint_1(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.constraint_3(parent) && this.constraint_1(parent)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         return true;
      }
   }

   private boolean contextDeclaration_1(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.operationContext(parent)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         if (this.classifierContext(parent)) {
            return true;
         } else {
            this.k = kk;
            parent.children.setSize(ll);
            return false;
         }
      }
   }

   private boolean classifierContext_1(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.name(parent) && this.match(parent, 11)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         return true;
      }
   }

   private boolean classifierContext_2(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.collectionKind(parent)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         if (this.dottedPathName(parent)) {
            return true;
         } else {
            this.k = kk;
            parent.children.setSize(ll);
            return false;
         }
      }
   }

   private boolean operationContext_1(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.collectionKind(parent)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         if (this.dottedPathName(parent)) {
            return true;
         } else {
            this.k = kk;
            parent.children.setSize(ll);
            return false;
         }
      }
   }

   private boolean operationContext_2(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.match(parent, 11) && this.returnType(parent)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         return true;
      }
   }

   private boolean stereotype_1(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.match(parent, 54)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         if (this.match(parent, 53)) {
            return true;
         } else {
            this.k = kk;
            parent.children.setSize(ll);
            if (this.match(parent, 45)) {
               return true;
            } else {
               this.k = kk;
               parent.children.setSize(ll);
               if (this.match(parent, 34)) {
                  return true;
               } else {
                  this.k = kk;
                  parent.children.setSize(ll);
                  return false;
               }
            }
         }
      }
   }

   private boolean operationName_1(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.name(parent)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         if (this.match(parent, 17)) {
            return true;
         } else {
            this.k = kk;
            parent.children.setSize(ll);
            if (this.match(parent, 4)) {
               return true;
            } else {
               this.k = kk;
               parent.children.setSize(ll);
               if (this.match(parent, 6)) {
                  return true;
               } else {
                  this.k = kk;
                  parent.children.setSize(ll);
                  if (this.match(parent, 15)) {
                     return true;
                  } else {
                     this.k = kk;
                     parent.children.setSize(ll);
                     if (this.match(parent, 19)) {
                        return true;
                     } else {
                        this.k = kk;
                        parent.children.setSize(ll);
                        if (this.match(parent, 16)) {
                           return true;
                        } else {
                           this.k = kk;
                           parent.children.setSize(ll);
                           if (this.match(parent, 14)) {
                              return true;
                           } else {
                              this.k = kk;
                              parent.children.setSize(ll);
                              if (this.match(parent, 18)) {
                                 return true;
                              } else {
                                 this.k = kk;
                                 parent.children.setSize(ll);
                                 if (this.match(parent, 10)) {
                                    return true;
                                 } else {
                                    this.k = kk;
                                    parent.children.setSize(ll);
                                    if (this.match(parent, 3)) {
                                       return true;
                                    } else {
                                       this.k = kk;
                                       parent.children.setSize(ll);
                                       if (this.match(parent, 43)) {
                                          return true;
                                       } else {
                                          this.k = kk;
                                          parent.children.setSize(ll);
                                          if (this.match(parent, 49)) {
                                             return true;
                                          } else {
                                             this.k = kk;
                                             parent.children.setSize(ll);
                                             if (this.match(parent, 51)) {
                                                return true;
                                             } else {
                                                this.k = kk;
                                                parent.children.setSize(ll);
                                                if (this.match(parent, 56)) {
                                                   return true;
                                                } else {
                                                   this.k = kk;
                                                   parent.children.setSize(ll);
                                                   if (this.match(parent, 33)) {
                                                      return true;
                                                   } else {
                                                      this.k = kk;
                                                      parent.children.setSize(ll);
                                                      if (this.match(parent, 37)) {
                                                         return true;
                                                      } else {
                                                         this.k = kk;
                                                         parent.children.setSize(ll);
                                                         if (this.match(parent, 47)) {
                                                            return true;
                                                         } else {
                                                            this.k = kk;
                                                            parent.children.setSize(ll);
                                                            return false;
                                                         }
                                                      }
                                                   }
                                                }
                                             }
                                          }
                                       }
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private boolean formalParameterList_1(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.name(parent) && this.match(parent, 11) && this.typeSpecifier(parent) && this.formalParameterList_2(parent)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         return true;
      }
   }

   private boolean letExpression_1(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.match(parent, 1) && this.formalParameterList(parent) && this.match(parent, 2)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         return true;
      }
   }

   private boolean letExpression_2(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.match(parent, 11) && this.typeSpecifier(parent)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         return true;
      }
   }

   private boolean letExpression_3(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.match(parent, 17) && this.expression(parent)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         return true;
      }
   }

   private boolean typeSpecifier_1(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.tupleType(parent)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         if (this.collectionType(parent)) {
            return true;
         } else {
            this.k = kk;
            parent.children.setSize(ll);
            if (this.simpleTypeSpecifier(parent)) {
               return true;
            } else {
               this.k = kk;
               parent.children.setSize(ll);
               return false;
            }
         }
      }
   }

   private boolean tupleType_1(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.match(parent, 5) && this.name(parent) && this.match(parent, 11) && this.typeSpecifier(parent) && this.tupleType_1(parent)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         return true;
      }
   }

   private boolean expression_1(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.letExpression(parent) && this.expression_2(parent) && this.expression_1(parent)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         return true;
      }
   }

   private boolean logicalExpression_1(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.logicalOperator(parent) && this.relationalExpression(parent) && this.logicalExpression_1(parent)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         return true;
      }
   }

   private boolean relationalExpression_1(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.relationalOperator(parent) && this.additiveExpression(parent) && this.relationalExpression_1(parent)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         return true;
      }
   }

   private boolean additiveExpression_1(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.addOperator(parent) && this.multiplicativeExpression(parent) && this.additiveExpression_1(parent)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         return true;
      }
   }

   private boolean multiplicativeExpression_1(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.multiplyOperator(parent) && this.unaryExpression(parent) && this.multiplicativeExpression_1(parent)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         return true;
      }
   }

   private boolean unaryExpression_1(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.unaryExpression_3(parent) && this.postfixExpression(parent)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         if (this.postfixExpression(parent)) {
            return true;
         } else {
            this.k = kk;
            parent.children.setSize(ll);
            return false;
         }
      }
   }

   private boolean postfixExpression_1(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.postfixExpression_2(parent) && this.propertyCall(parent) && this.postfixExpression_1(parent)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         return true;
      }
   }

   private boolean primaryExpression_1(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.collectionType(parent)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         if (this.tupleType(parent)) {
            return true;
         } else {
            this.k = kk;
            parent.children.setSize(ll);
            if (this.ifExpression(parent)) {
               return true;
            } else {
               this.k = kk;
               parent.children.setSize(ll);
               if (this.literalCollection(parent)) {
                  return true;
               } else {
                  this.k = kk;
                  parent.children.setSize(ll);
                  if (this.literalTuple(parent)) {
                     return true;
                  } else {
                     this.k = kk;
                     parent.children.setSize(ll);
                     if (this.literal(parent)) {
                        return true;
                     } else {
                        this.k = kk;
                        parent.children.setSize(ll);
                        if (this.propertyCall(parent)) {
                           return true;
                        } else {
                           this.k = kk;
                           parent.children.setSize(ll);
                           if (this.match(parent, 1) && this.expression(parent) && this.match(parent, 2)) {
                              return true;
                           } else {
                              this.k = kk;
                              parent.children.setSize(ll);
                              return false;
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private boolean propertyCallParameters_1(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.declarator(parent)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         return true;
      }
   }

   private boolean propertyCallParameters_2(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.actualParameterList(parent)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         return true;
      }
   }

   private boolean literal_1(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.string(parent)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         if (this.number(parent)) {
            return true;
         } else {
            this.k = kk;
            parent.children.setSize(ll);
            if (this.enumLiteral(parent)) {
               return true;
            } else {
               this.k = kk;
               parent.children.setSize(ll);
               return false;
            }
         }
      }
   }

   private boolean enumLiteral_1(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.pathName(parent)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         return true;
      }
   }

   private boolean literalCollection_1(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.collectionItem(parent) && this.literalCollection_2(parent)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         return true;
      }
   }

   private boolean collectionItem_1(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.match(parent, 9) && this.expression(parent)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         return true;
      }
   }

   private boolean literalTuple_1(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.match(parent, 11) && this.typeSpecifier(parent)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         return true;
      }
   }

   private boolean literalTuple_2(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.match(parent, 5) && this.name(parent) && this.literalTuple_3(parent) && this.match(parent, 17) && this.expression(parent) && this.literalTuple_2(parent)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         return true;
      }
   }

   private boolean propertyCall_1(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.pathName(parent)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         if (this.operationName(parent)) {
            return true;
         } else {
            this.k = kk;
            parent.children.setSize(ll);
            return false;
         }
      }
   }

   private boolean propertyCall_2(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.timeExpression(parent)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         return true;
      }
   }

   private boolean propertyCall_3(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.qualifiers(parent)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         return true;
      }
   }

   private boolean propertyCall_4(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.propertyCallParameters(parent)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         return true;
      }
   }

   private boolean declarator_1(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.match(parent, 5) && this.name(parent) && this.declarator_1(parent)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         return true;
      }
   }

   private boolean declarator_2(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.match(parent, 11) && this.typeSpecifier(parent)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         return true;
      }
   }

   private boolean declarator_3(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.match(parent, 13) && this.name(parent) && this.declarator_4(parent) && this.match(parent, 17) && this.expression(parent)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         return true;
      }
   }

   private boolean pathName_1(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.match(parent, 12) && this.name(parent) && this.pathName_1(parent)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         return true;
      }
   }

   private boolean dottedPathName_1(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.match(parent, 8) && this.name(parent) && this.dottedPathName_1(parent)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         return true;
      }
   }

   private boolean actualParameterList_1(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.match(parent, 20) && this.actualParameterList_3(parent)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         if (this.expression(parent)) {
            return true;
         } else {
            this.k = kk;
            parent.children.setSize(ll);
            return false;
         }
      }
   }

   private boolean actualParameterList_2(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.match(parent, 5) && this.actualParameterList_4(parent) && this.actualParameterList_2(parent)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         return true;
      }
   }

   private boolean logicalOperator_1(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.match(parent, 33)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         if (this.match(parent, 51)) {
            return true;
         } else {
            this.k = kk;
            parent.children.setSize(ll);
            if (this.match(parent, 56)) {
               return true;
            } else {
               this.k = kk;
               parent.children.setSize(ll);
               if (this.match(parent, 43)) {
                  return true;
               } else {
                  this.k = kk;
                  parent.children.setSize(ll);
                  return false;
               }
            }
         }
      }
   }

   private boolean collectionKind_1(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.match(parent, 26)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         if (this.match(parent, 22)) {
            return true;
         } else {
            this.k = kk;
            parent.children.setSize(ll);
            if (this.match(parent, 25)) {
               return true;
            } else {
               this.k = kk;
               parent.children.setSize(ll);
               if (this.match(parent, 24)) {
                  return true;
               } else {
                  this.k = kk;
                  parent.children.setSize(ll);
                  if (this.match(parent, 23)) {
                     return true;
                  } else {
                     this.k = kk;
                     parent.children.setSize(ll);
                     return false;
                  }
               }
            }
         }
      }
   }

   private boolean relationalOperator_1(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.match(parent, 19)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         if (this.match(parent, 15)) {
            return true;
         } else {
            this.k = kk;
            parent.children.setSize(ll);
            if (this.match(parent, 16)) {
               return true;
            } else {
               this.k = kk;
               parent.children.setSize(ll);
               if (this.match(parent, 17)) {
                  return true;
               } else {
                  this.k = kk;
                  parent.children.setSize(ll);
                  if (this.match(parent, 18)) {
                     return true;
                  } else {
                     this.k = kk;
                     parent.children.setSize(ll);
                     if (this.match(parent, 14)) {
                        return true;
                     } else {
                        this.k = kk;
                        parent.children.setSize(ll);
                        return false;
                     }
                  }
               }
            }
         }
      }
   }

   private boolean addOperator_1(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.match(parent, 4)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         if (this.match(parent, 6)) {
            return true;
         } else {
            this.k = kk;
            parent.children.setSize(ll);
            return false;
         }
      }
   }

   private boolean multiplyOperator_1(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.match(parent, 3)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         if (this.match(parent, 10)) {
            return true;
         } else {
            this.k = kk;
            parent.children.setSize(ll);
            if (this.match(parent, 37)) {
               return true;
            } else {
               this.k = kk;
               parent.children.setSize(ll);
               if (this.match(parent, 47)) {
                  return true;
               } else {
                  this.k = kk;
                  parent.children.setSize(ll);
                  return false;
               }
            }
         }
      }
   }

   private boolean unaryOperator_1(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.match(parent, 6)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         if (this.match(parent, 4)) {
            return true;
         } else {
            this.k = kk;
            parent.children.setSize(ll);
            if (this.match(parent, 49)) {
               return true;
            } else {
               this.k = kk;
               parent.children.setSize(ll);
               return false;
            }
         }
      }
   }

   private boolean oclFile_4(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.match(parent, 48) && this.pathName(parent) && this.oclFile_6(parent) && this.match(parent, 40)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         if (this.oclPackage(parent)) {
            return true;
         } else {
            this.k = kk;
            parent.children.setSize(ll);
            return false;
         }
      }
   }

   private boolean freeConstraint_2(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.name(parent)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         return true;
      }
   }

   private boolean constraint_3(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.match(parent, 36) && this.constraint_4(parent) && this.match(parent, 11) && this.constraint_6(parent)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         if (this.stereotype(parent) && this.constraint_7(parent) && this.match(parent, 11) && this.oclExpression(parent)) {
            return true;
         } else {
            this.k = kk;
            parent.children.setSize(ll);
            return false;
         }
      }
   }

   private boolean formalParameterList_2(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.match(parent, 5) && this.name(parent) && this.match(parent, 11) && this.typeSpecifier(parent) && this.formalParameterList_2(parent)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         return true;
      }
   }

   private boolean expression_2(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.match(parent, 44)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         return true;
      }
   }

   private boolean unaryExpression_3(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.unaryOperator(parent) && this.unaryExpression_2(parent)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         return false;
      }
   }

   private boolean unaryExpression_2(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.unaryOperator(parent) && this.unaryExpression_2(parent)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         return true;
      }
   }

   private boolean postfixExpression_2(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.match(parent, 8)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         if (this.match(parent, 7)) {
            return true;
         } else {
            this.k = kk;
            parent.children.setSize(ll);
            if (this.match(parent, 31)) {
               return true;
            } else {
               this.k = kk;
               parent.children.setSize(ll);
               if (this.match(parent, 32)) {
                  return true;
               } else {
                  this.k = kk;
                  parent.children.setSize(ll);
                  return false;
               }
            }
         }
      }
   }

   private boolean literalCollection_2(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.match(parent, 5) && this.collectionItem(parent) && this.literalCollection_2(parent)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         return true;
      }
   }

   private boolean literalTuple_3(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.match(parent, 11) && this.typeSpecifier(parent)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         return true;
      }
   }

   private boolean declarator_4(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.match(parent, 11) && this.typeSpecifier(parent)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         return true;
      }
   }

   private boolean actualParameterList_3(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.match(parent, 11) && this.typeSpecifier(parent)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         return true;
      }
   }

   private boolean actualParameterList_4(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.match(parent, 20) && this.actualParameterList_5(parent)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         if (this.expression(parent)) {
            return true;
         } else {
            this.k = kk;
            parent.children.setSize(ll);
            return false;
         }
      }
   }

   private boolean oclFile_6(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.oclPackage(parent) && this.oclFile_5(parent)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         return false;
      }
   }

   private boolean oclFile_5(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.oclPackage(parent) && this.oclFile_5(parent)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         return true;
      }
   }

   private boolean constraint_4(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.name(parent)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         return true;
      }
   }

   private boolean constraint_6(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.letExpression(parent) && this.constraint_5(parent)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         return false;
      }
   }

   private boolean constraint_5(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.letExpression(parent) && this.constraint_5(parent)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         return true;
      }
   }

   private boolean constraint_7(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.name(parent)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         return true;
      }
   }

   private boolean actualParameterList_5(OclNode parent) {
      int kk = this.k;
      int ll = parent.children.size();
      if (this.match(parent, 11) && this.typeSpecifier(parent)) {
         return true;
      } else {
         this.k = kk;
         parent.children.setSize(ll);
         return true;
      }
   }
}
