package ro.ubbcluj.lci.ocl.codegen.norm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import javax.swing.tree.MutableTreeNode;
import ro.ubbcluj.lci.errors.BasicErrorMessage;
import ro.ubbcluj.lci.errors.ErrorListener;
import ro.ubbcluj.lci.errors.ErrorMessage;
import ro.ubbcluj.lci.errors.ErrorSource;
import ro.ubbcluj.lci.ocl.ExceptionChecker;
import ro.ubbcluj.lci.ocl.OclClassInfo;
import ro.ubbcluj.lci.ocl.OclLetItem;
import ro.ubbcluj.lci.ocl.OclNode;
import ro.ubbcluj.lci.ocl.OclToken;
import ro.ubbcluj.lci.ocl.OclType;
import ro.ubbcluj.lci.ocl.OclUtil;
import ro.ubbcluj.lci.ocl.OclVisitor;
import ro.ubbcluj.lci.ocl.SearchResult;
import ro.ubbcluj.lci.ocl.codegen.utils.OclCodeGenUtilities;
import ro.ubbcluj.lci.ocl.eval.OclConstant;
import ro.ubbcluj.lci.ocl.eval.OclExpression;
import ro.ubbcluj.lci.ocl.nodes.*;
import ro.ubbcluj.lci.uml.foundation.core.AssociationClass;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.uml.foundation.core.Operation;
import ro.ubbcluj.lci.utils.InterruptibleTask;
import ro.ubbcluj.lci.utils.uml.UMLUtilities;

public class NormalFormBuilder extends InterruptibleTask implements ErrorSource {
   private NormalForm result;
   private OclVisitor buildVisitor;
   private ArrayList warnings;
   private ConstrainedObject currentContext;
   private HashMap invalidContexts;
   private String errorMessage;
   private OperationConstraintGroup currentGroup;
   private int currentStereotype;
   private OclNode compiledForm;
   private HashSet errorListeners;
   private SyntaxTreeNode currentThis;

   public NormalFormBuilder() {
      this.result = null;
      this.buildVisitor = new NormalFormBuilder.BuildVisitor();
      this.warnings = new ArrayList();
      this.invalidContexts = new HashMap();
      this.currentGroup = null;
      this.currentStereotype = -1;
      this.errorListeners = null;
      this.isCancelled = false;
   }

   public NormalFormBuilder(OclNode cf) {
      this();
      this.compiledForm = cf;
   }

   public void realRun() {
      this.build(this.compiledForm);
   }

   public void removeErrorListener(ErrorListener l) {
      this.errorListeners.remove(l);
   }

   public void addErrorListener(ErrorListener l) {
      if (this.errorListeners == null) {
         this.errorListeners = new HashSet();
      }

      this.errorListeners.add(l);
   }

   public void errorOccured(ErrorMessage msg) {
      this.warnings.add(msg);
      if (this.errorListeners != null) {
         Iterator el = this.errorListeners.iterator();

         while(el.hasNext()) {
            ((ErrorListener)el.next()).errorOccured(msg);
         }
      }

   }

   public NormalForm getResult() {
      return this.result;
   }

   public NormalForm build(OclNode compiledForm) {
      this.result = new NormalForm();
      this.warnings.clear();

      try {
         compiledForm.acceptVisitor(this.buildVisitor);
      } catch (ExceptionChecker var3) {
         throw new RuntimeException("Unexpected checker exception!", var3);
      }

      this.result.removeEmptyConstrainedObjects();
      return this.result;
   }

   public Object[] getWarnings() {
      return this.warnings.toArray(new Object[0]);
   }

   public List getWarningsAsList() {
      return Collections.unmodifiableList(this.warnings);
   }

   private BasicConstraintNode createBasicConstraint(oclExpression ocle) {
      if (this.isCancelled) {
         BasicErrorMessage cm = new BasicErrorMessage(this.errorMessage = "Operation cancelled");
         if (!this.warnings.contains(cm)) {
            this.warnings.add(cm);
         }

         return null;
      } else {
         String cName = null;
         OclNode parent = ocle.getParent();
         int ci = parent.indexOfChild(ocle);
         OclNode temp = parent.getChild(ci - 2);
         int si;
         if (temp instanceof name) {
            si = ci - 3;
            cName = temp.getValueAsString();
         } else {
            si = ci - 2;
         }

         String stereo = parent.getChild(si).getValueAsString();
         if ("inv".equals(stereo)) {
            this.currentStereotype = 0;
         } else if ("pre".equals(stereo)) {
            this.currentStereotype = 1;
         } else {
            this.currentStereotype = 2;
         }

         SyntaxTreeNode body = this.buildSyntaxTree(ocle.getChild(0));
         if (body == null) {
            return null;
         } else {
            BasicConstraintNode ret = new BasicConstraintNode();
            ret.setExpression(body);
            ret.setKind(this.currentStereotype);
            if (cName != null) {
               ret.setName(cName);
            }

            if (!"inv".equals(stereo)) {
               ret.setGroup(this.currentGroup);
            }

            return ret;
         }
      }
   }

   private DefinitionConstraintNode createDefinitionConstraint(letExpression le) {
      if (this.isCancelled) {
         BasicErrorMessage cm = new BasicErrorMessage(this.errorMessage = "Operation cancelled");
         if (!this.warnings.contains(cm)) {
            this.warnings.add(cm);
         }

         return null;
      } else {
         String err = "Incomplete local definition; will be ignored";
         int fpIndex = -1;
         int n = le.getChildCount();
         if (n <= 2) {
            this.errorMessage = "Incomplete local definition; will be ignored";
            return null;
         } else {
            if (le.getChild(2).getValueAsString().equals("(")) {
               if (le.getParent() instanceof expression) {
                  this.errorMessage = "Local definitions with parameters are not supported";
                  return null;
               }

               fpIndex = 3;
            }

            int nn;
            if (fpIndex > 0) {
               nn = fpIndex + 2;
               if (nn > n) {
                  this.errorMessage = "Incomplete local definition; will be ignored";
                  return null;
               }
            } else {
               nn = 2;
            }

            OclType exprType;
            if (le.getChild(nn).getValueAsString().equals(":")) {
               exprType = le.getChild(nn + 1).type;
               nn += 3;
            } else {
               ++nn;
               exprType = le.getChild(nn).type;
            }

            if (nn >= n) {
               this.errorMessage = "Incomplete local definition; will be ignored";
               return null;
            } else {
               SyntaxTreeNode body = this.buildSyntaxTree(le.getChild(nn));
               if (body == null) {
                  return null;
               } else {
                  DefinitionConstraintNode ret = new DefinitionConstraintNode();
                  ret.setName(le.getChild(1).getValueAsString());
                  ret.setExpression(body);
                  ret.setReturnType(exprType);
                  if (fpIndex > 0) {
                     this.configureParameters(ret, le.getChild(fpIndex));
                  }

                  if (exprType.isTuple()) {
                     this.result.registerTupleType(exprType);
                  }

                  return ret;
               }
            }
         }
      }
   }

   private SyntaxTreeNode buildSyntaxTree(OclNode expr) {
      SyntaxTreeNode ret = null;
      Class cls = expr.getClass();
      if (this.isCancelled) {
         BasicErrorMessage cm = new BasicErrorMessage(this.errorMessage = "Operation cancelled");
         if (!this.warnings.contains(cm)) {
            this.warnings.add(cm);
         }

         return null;
      } else {
         if (cls == (expression.class)) {
            ret = this.buildSyntaxTree((expression)expr);
         }

         if (cls == (untilExpression.class)) {
            cls = logicalExpression.class;
            expr = expr.getChild(0);
         }

         if (cls == (logicalExpression.class)) {
            if (expr.getChildCount() <= 1) {
               cls = relationalExpression.class;
               expr = expr.getChild(0);
            } else {
               ret = this.buildSyntaxTree((logicalExpression)expr);
            }
         }

         if (cls == (relationalExpression.class)) {
            if (expr.getChildCount() <= 1) {
               cls = additiveExpression.class;
               expr = expr.getChild(0);
            } else {
               ret = this.buildStandardSyntaxTree(expr);
            }
         }

         if (cls == (additiveExpression.class)) {
            if (expr.getChildCount() <= 1) {
               cls = multiplicativeExpression.class;
               expr = expr.getChild(0);
            } else {
               ret = this.buildStandardSyntaxTree(expr);
            }
         }

         if (cls == (multiplicativeExpression.class)) {
            if (expr.getChildCount() <= 1) {
               cls = unaryExpression.class;
               expr = expr.getChild(0);
            } else {
               ret = this.buildStandardSyntaxTree(expr);
            }
         }

         if (cls == (unaryExpression.class)) {
            int n = expr.getChildCount() - 1;
            postfixExpression pe = (postfixExpression)expr.getChild(n);
            SyntaxTreeNode temp = this.buildSyntaxTree(pe);
            if (temp == null) {
               return null;
            }

            SyntaxTreeNode retNode = temp;
            OclType exprType = temp.getType();

            for(int i = n - 1; i >= 0; --i) {
               OperatorNode opNode = new OperatorNode(expr.getChild(i).getValueAsString());
               opNode.setType(exprType);
               opNode.add((MutableTreeNode)retNode);
               retNode = opNode;
            }

            ret = retNode;
         }

         if (cls == (propertyCall.class)) {
            ret = this.buildSyntaxTree((propertyCall)expr);
         }

         return (SyntaxTreeNode)ret;
      }
   }

   private SyntaxTreeNode buildSyntaxTree(postfixExpression pe) {
      if (this.isCancelled) {
         BasicErrorMessage cm = new BasicErrorMessage(this.errorMessage = "Operation cancelled");
         if (!this.warnings.contains(cm)) {
            this.warnings.add(cm);
         }

         return null;
      } else {
         int n = pe.getChildCount();
         SyntaxTreeNode ret = null;
         SyntaxTreeNode implicit = null;
         OclNode firstChild = pe.getChild(0);
         SyntaxTreeNode first;
         this.currentThis = first = this.buildSyntaxTree((primaryExpression)firstChild);

         for(int i = 2; i < n; i += 2) {
            if (ret == null) {
               ret = new PostfixExpressionNode();
            }

            SyntaxTreeNode pc = this.buildSyntaxTree((propertyCall)pe.getChild(i));
            if (pc == null) {
               return null;
            }

            this.currentThis = pc;
            ret.add(pc);
         }

         if (first == null) {
            return null;
         } else {
            OclNode fp = pe.getChild(0).getChild(0);
            if (fp instanceof propertyCall) {
               SearchResult sr = fp.searchResult;
               if (sr != null && (sr.foundType & 128) == 128) {
                  implicit = new PropertyCallNode();
                  implicit.setUserObject(sr.foundImplicitOwner);
                  implicit.setType(sr.foundImplicitOwner.rettype);
               }
            }

            if (ret == null) {
               if (implicit == null) {
                  return first;
               } else {
                  ret = new PostfixExpressionNode();
                  ret.add(implicit);
                  ret.add(first);
                  return ret;
               }
            } else {
               int indexOfFirst = 0;
               if (implicit != null) {
                  indexOfFirst = 1;
                  ret.insert(implicit, 0);
               }

               ret.insert(first, indexOfFirst);
               return ret;
            }
         }
      }
   }

   private SyntaxTreeNode buildSyntaxTree(primaryExpression pe) {
      if (this.isCancelled) {
         BasicErrorMessage cm = new BasicErrorMessage(this.errorMessage = "Operation cancelled");
         if (!this.warnings.contains(cm)) {
            this.warnings.add(cm);
         }

         return null;
      } else {
         OclNode first = pe.getChild(0);
         if (first instanceof propertyCall) {
            return this.buildSyntaxTree((propertyCall)first);
         } else if (first instanceof OclTEXT) {
            return this.buildSyntaxTree(pe.getChild(1));
         } else if (first instanceof literal) {
            LiteralNode ln = new LiteralNode();
            OclNode child = first.getChild(0);
            if (child.type.type == OclUtil.umlapi.STRING) {
               String initial = child.getValueAsString();
               ln.setUserObject('"' + initial.substring(1, initial.length() - 1) + '"');
            } else if (child.type.type == OclUtil.umlapi.INTEGER) {
               Integer i = Integer.decode(child.getValueAsString());
               ln.setUserObject(i);
            } else if (child.type.type == OclUtil.umlapi.REAL) {
               Float f = new Float(child.getValueAsString());
               ln.setUserObject(f);
            } else if (child instanceof enumLiteral) {
               ln.setUserObject(((OclConstant)child.evnode).getValue());
            }

            ln.setType(child.type);
            return ln;
         } else if (first instanceof ifExpression) {
            return this.buildSyntaxTree((ifExpression)first);
         } else if (!(first instanceof collectionType) && !(first instanceof tupleType)) {
            if (first instanceof literalCollection) {
               return this.buildSyntaxTree((literalCollection)first);
            } else {
               return first instanceof literalTuple ? this.buildSyntaxTree((literalTuple)first) : null;
            }
         } else {
            TypeNode tn = new TypeNode();
            tn.setUserObject(first.type.celement);
            if (first instanceof tupleType) {
               this.result.registerTupleType(first.type);
            }

            return tn;
         }
      }
   }

   private SyntaxTreeNode buildSyntaxTree(literalTuple lt) {
      if (this.isCancelled) {
         BasicErrorMessage cm = new BasicErrorMessage(this.errorMessage = "Operation cancelled");
         if (!this.warnings.contains(cm)) {
            this.warnings.add(cm);
         }

         return null;
      } else {
         TupleNode ret = new TupleNode();
         this.result.registerTupleType(lt.type);
         int i = 2;

         int exprStart;
         for(int n = lt.getChildCount() - 1; i < n; i = exprStart + 2) {
            String name = lt.getChild(i).getValueAsString();
            exprStart = i + 2;
            if (lt.getChild(i + 1).getValueAsString().equals(":")) {
               exprStart = i + 4;
            }

            SyntaxTreeNode body = this.buildSyntaxTree(lt.getChild(exprStart));
            if (body == null) {
               return null;
            }

            TuplePart tp = new TuplePart(name, body);
            tp.setType(lt.getChild(i + 2).type);
            ret.add(tp);
         }

         ret.setType(lt.type);
         return ret;
      }
   }

   private SyntaxTreeNode buildSyntaxTree(propertyCall pc) {
      if (this.isCancelled) {
         BasicErrorMessage cm = new BasicErrorMessage(this.errorMessage = "Operation cancelled");
         if (!this.warnings.contains(cm)) {
            this.warnings.add(cm);
         }

         return null;
      } else {
         SyntaxTreeNode res = null;
         OclNode pte = pc.getChild(1);
         OclNode firstChild = pc.getChild(0);
         String firstChildText = firstChild.getValueAsString();
         boolean initialPath = firstChild.getChildCount() > 1;
         OclClassInfo realOwner = null;
         SearchResult sr = pc.searchResult;
         int paramIndex = 1;
         if (sr == null) {
            if (!"true".equals(firstChildText) && !"false".equals(firstChildText)) {
               if (!"undefined".equalsIgnoreCase(firstChildText)) {
                  throw new RuntimeException("SearchResult field not set for a property call " + firstChildText);
               }

               Undefined u = new Undefined();
               res = new LiteralNode();
               ((SyntaxTreeNode)res).setUserObject(u);
            } else {
               Boolean bVal = "true".equals(firstChildText) ? Boolean.TRUE : Boolean.FALSE;
               res = new LiteralNode();
               ((SyntaxTreeNode)res).setUserObject(bVal);
            }
         } else {
            OclClassInfo propertyOwner = OclCodeGenUtilities.getOwner(sr);
            if (initialPath) {
               realOwner = propertyOwner;
            }

            if (propertyOwner == OclUtil.umlapi.OCLANY && "oclIsNew".equals(firstChildText)) {
               this.errorOccured(new NormalizerWarning(pc.getFilename(), pc.getStart(), pc.getStop(), "'oclIsNew()' always returns false in this implementation"));
            }

            if (propertyOwner == OclUtil.umlapi.OCLTYPE && "allInstances".equals(firstChildText)) {
               this.result.registerAllInstancesCandidate(this.currentThis.getType().element.classifier);
            }

            if ((sr.foundType & 64) == 64) {
               return this.collectShorthandResolver(pc, realOwner);
            }

            if ((sr.foundType & 4) == 4) {
               res = new TypeNode();
               ((SyntaxTreeNode)res).setUserObject(pc.type.celement);
            } else if ((sr.foundType & 8) != 8 && sr.foundType != 0) {
               if ((sr.foundType & 512) == 512) {
                  Object val = ((OclConstant)pc.evnode).getValue();
                  res = new LiteralNode();
                  ((SyntaxTreeNode)res).setUserObject(val);
               }
            } else {
               res = new PropertyCallNode(realOwner);
               ((PropertyCallNode)res).setData(sr);
               ((SyntaxTreeNode)res).setUserObject(sr.foundLetItem);
            }
         }

         if (pte instanceof timeExpression) {
            paramIndex = 2;
            this.errorOccured(new NormalizerWarning(pte.getFilename(), pte.getStart(), pte.getStop() + 1, "Time expressions not supported. '@pre' will be ignored"));
         }

         if (res == null) {
            res = new PropertyCallNode(realOwner);
            ((SyntaxTreeNode)res).setUserObject(pc);
         }

         if (res instanceof PropertyCallNode && paramIndex < pc.getChildCount()) {
            OclNode possibleQualifiers = pc.getChild(paramIndex);
            if (possibleQualifiers instanceof qualifiers) {
               ++paramIndex;
               if (this.processQualifiers((PropertyCallNode)res, possibleQualifiers) < 0) {
                  return null;
               }
            }

            if (paramIndex < pc.getChildCount() && this.processPropertyCallParameters((SyntaxTreeNode)res, pc.getChild(paramIndex)) < 0) {
               return null;
            }
         }

         ((SyntaxTreeNode)res).setType(pc.type);
         return (SyntaxTreeNode)res;
      }
   }

   private int processQualifiers(PropertyCallNode target, OclNode qualifiers) {
      boolean bSimple = false;
      SearchResult sr = ((OclNode)target.getUserObject()).searchResult;
      if (sr.asend != null && sr.asend.getParticipant() instanceof AssociationClass) {
         bSimple = true;
      }

      OclNode apl = qualifiers.getChild(1);
      int n = apl.getChildCount();

      for(int i = 0; i < n; i += 2) {
         OclNode q = apl.getChild(i);
         Qualifier qq;
         if (bSimple) {
            qq = new Qualifier(q.getValueAsString());
         } else {
            qq = new Qualifier(false);
            SyntaxTreeNode qRoot = this.buildSyntaxTree(q);
            if (qRoot == null) {
               return -1;
            }

            qq.setExpression(qRoot);
         }

         target.addQualifier(qq);
      }

      return 0;
   }

   private int processPropertyCallParameters(SyntaxTreeNode pcall, OclNode fpl) {
      if (this.isCancelled) {
         BasicErrorMessage cm = new BasicErrorMessage(this.errorMessage = "Operation cancelled");
         if (!this.warnings.contains(cm)) {
            this.warnings.add(cm);
         }

         return -1;
      } else if (fpl != null && fpl.getChildCount() > 2) {
         OclNode pd = fpl.getChild(1);
         int rpi = 1;
         if (pd instanceof declarator) {
            int j = 0;

            OclNode t;
            for(t = null; j < pd.getChildCount(); ++j) {
               OclNode acc = pd.getChild(j);
               if (";".equals(acc.getValueAsString())) {
                  t = acc;
                  break;
               }
            }

            if (t != null) {
               int skip = 3;
               if (pd.getChild(skip + j) instanceof typeSpecifier) {
                  skip = 5;
               }

               SyntaxTreeNode accNode = this.buildSyntaxTree(pd.getChild(j + skip));
               pcall.add(accNode);
            }

            rpi = 2;
         }

         OclNode realfpl = fpl.getChild(rpi);
         int n = realfpl.getChildCount();

         for(int i = 0; i < n; i += 2) {
            SyntaxTreeNode param = this.buildSyntaxTree(realfpl.getChild(i));
            if (param == null) {
               return -1;
            }

            pcall.add(param);
         }

         return 0;
      } else {
         return 0;
      }
   }

   private SyntaxTreeNode buildSyntaxTree(literalCollection lc) {
      if (this.isCancelled) {
         BasicErrorMessage cm = new BasicErrorMessage(this.errorMessage = "Operation cancelled");
         if (!this.warnings.contains(cm)) {
            this.warnings.add(cm);
         }

         return null;
      } else {
         CollectionNode coll = new CollectionNode(lc.getChild(0).getValueAsString());
         coll.setType(lc.type);
         int n = lc.getChildCount() - 1;

         for(int i = 2; i < n; i += 2) {
            OclNode item = lc.getChild(i);
            SyntaxTreeNode lower = this.buildSyntaxTree(item.getChild(0));
            if (lower == null) {
               return null;
            }

            if (item.getChildCount() > 1) {
               OperatorNode root = new OperatorNode("..");
               SyntaxTreeNode upper = this.buildSyntaxTree(item.getChild(2));
               if (upper == null) {
                  return null;
               }

               root.add(lower);
               root.add(upper);
               coll.add(root);
            } else {
               coll.add(lower);
            }
         }

         return coll;
      }
   }

   private SyntaxTreeNode buildSyntaxTree(ifExpression ie) {
      if (this.isCancelled) {
         BasicErrorMessage cm = new BasicErrorMessage(this.errorMessage = "Operation cancelled");
         if (!this.warnings.contains(cm)) {
            this.warnings.add(cm);
         }

         return null;
      } else {
         SyntaxTreeNode ifN = this.buildSyntaxTree(ie.getChild(1));
         if (ifN == null) {
            return null;
         } else {
            SyntaxTreeNode thenN = this.buildSyntaxTree(ie.getChild(3));
            if (thenN == null) {
               return null;
            } else {
               SyntaxTreeNode elseN = this.buildSyntaxTree(ie.getChild(5));
               if (elseN == null) {
                  return null;
               } else {
                  IfExpressionNode res = new IfExpressionNode();
                  res.setType(ie.type);
                  res.add(ifN);
                  res.add(thenN);
                  res.add(elseN);
                  return res;
               }
            }
         }
      }
   }

   private SyntaxTreeNode buildStandardSyntaxTree(OclNode expr) {
      if (this.isCancelled()) {
         BasicErrorMessage cm = new BasicErrorMessage(this.errorMessage = "Operation cancelled");
         if (!this.warnings.contains(cm)) {
            this.warnings.add(cm);
         }

         return null;
      } else {
         SyntaxTreeNode operand = null;
         SyntaxTreeNode operator = null;
         int n = expr.getChildCount();
         int j = 0;
         boolean bIsOperand = true;

         for(int i = 0; i < n; ++i) {
            OclNode cNode = expr.getChild(i);
            if (bIsOperand) {
               SyntaxTreeNode current = this.buildSyntaxTree(cNode);
               if (current == null) {
                  return null;
               }

               if (operand == null) {
                  operand = current;
               } else {
                  operator.add((MutableTreeNode)operand);
                  operator.add(current);
                  operand = operator;
               }
            } else {
               operator = new OperatorNode(expr.getChild(i).getValueAsString());
               ++j;
               operator.setType((OclType)expr.vectorValue.get(j));
            }

            bIsOperand = !bIsOperand;
         }

         return (SyntaxTreeNode)operand;
      }
   }

   private SyntaxTreeNode buildSyntaxTree(expression expr) {
      if (this.isCancelled) {
         BasicErrorMessage cm = new BasicErrorMessage(this.errorMessage = "Operation cancelled");
         if (!this.warnings.contains(cm)) {
            this.warnings.add(cm);
         }

         return null;
      } else {
         int n = expr.getChildCount() - 1;
         int i = 0;
         LetExpressionNode let = null;

         while(i < n) {
            if (let == null) {
               let = new LetExpressionNode();
            }

            DefinitionConstraintNode dcn = this.createDefinitionConstraint((letExpression)expr.getChild(i));
            if (dcn == null) {
               return null;
            }

            let.addLocalDefinition(dcn);
            if (expr.getChild(i + 1).getValueAsString().equals("in")) {
               i += 2;
            } else {
               ++i;
            }
         }

         SyntaxTreeNode realExpr = this.buildSyntaxTree(expr.getChild(n));
         if (realExpr == null) {
            return null;
         } else if (let != null) {
            let.setRealExpression(realExpr);
            return let;
         } else {
            return realExpr;
         }
      }
   }

   private SyntaxTreeNode buildSyntaxTree(logicalExpression le) {
      if (this.isCancelled) {
         BasicErrorMessage cm = new BasicErrorMessage(this.errorMessage = "Operation cancelled");
         if (!this.warnings.contains(cm)) {
            this.warnings.add(cm);
         }

         return null;
      } else {
         Stack stOperands = new Stack();
         Stack stOperators = new Stack();
         int n = le.getChildCount();

         SyntaxTreeNode op;
         for(int i = 0; i < n; ++i) {
            OclNode nd = le.getChild(i);
            if (nd.getClass() == (logicalOperator.class)) {
               OperatorNode ond = new OperatorNode(nd.getValueAsString());
               ond.setType(new OclType(OclUtil.umlapi.BOOLEAN));
               boolean ready = false;

               while(!ready && !stOperators.isEmpty()) {
                  OperatorNode top = (OperatorNode)stOperators.peek();
                  if (priority(top.getText()) < priority(ond.getText())) {
                     ready = true;
                  } else {
                     stOperators.pop();
                     SyntaxTreeNode n1 = (SyntaxTreeNode)stOperands.pop();
                     SyntaxTreeNode n2 = (SyntaxTreeNode)stOperands.pop();
                     top.add(n2);
                     top.add(n1);
                     stOperands.push(top);
                  }
               }

               stOperators.push(ond);
            } else {
               op = this.buildSyntaxTree(nd);
               if (op == null) {
                  return null;
               }

               stOperands.push(op);
            }
         }

         while(!stOperators.isEmpty()) {
            op = (SyntaxTreeNode)stOperators.pop();
            SyntaxTreeNode op1 = (SyntaxTreeNode)stOperands.pop();
            SyntaxTreeNode op2 = (SyntaxTreeNode)stOperands.pop();
            op.add(op2);
            op.add(op1);
            stOperands.push(op);
         }

         return (SyntaxTreeNode)stOperands.peek();
      }
   }

   private SyntaxTreeNode collectShorthandResolver(propertyCall pc, OclClassInfo realOwner) {
      String collectPropertyName = "collect";
      propertyCall pCallCollect = new propertyCall();
      propertyCall pcClone = null;

      try {
         pcClone = (propertyCall)pc.clone();
      } catch (CloneNotSupportedException var15) {
         this.errorOccured(new NormalizerWarning(pc.getFilename(), pc.getStart(), pc.getStop(), "Could not deal with collect shorthand notation: " + var15.getMessage()));
         return null;
      }

      OclToken[] pcCollectTokens = new OclToken[3 + pcClone.token_count()];
      System.arraycopy(pcClone.tokens, 0, pcCollectTokens, 2, pcClone.token_count());
      OclToken tok = new OclToken(4, "collect", 0, 0, 0, 0, (String)null);
      pcCollectTokens[0] = tok;
      tok = new OclToken(3, "(", 0, 0, 0, 0, (String)null);
      pcCollectTokens[1] = tok;
      tok = new OclToken(3, ")", 0, 0, 0, 0, (String)null);
      pcCollectTokens[pcCollectTokens.length - 1] = tok;
      pCallCollect.token_start = 0;
      pCallCollect.token_stop = pcCollectTokens.length - 1;
      pCallCollect.tokens = pcCollectTokens;
      OclTEXT t = new OclTEXT();
      t.tokens = pCallCollect.tokens;
      t.stringValue = "collect";
      t.token_start = t.token_stop = pCallCollect.token_start;
      pCallCollect.addChild(t);
      SearchResult srCollect = new SearchResult();
      srCollect.foundType = 2;
      Operation collectOp = this.currentThis.getType().type.findOperation("collect");
      if (collectOp == null) {
         throw new InternalError("'collect' operation not found in " + this.currentThis.getType().name());
      } else {
         srCollect.operation = collectOp;
         srCollect.type = pc.type;
         pCallCollect.type = pc.type;
         srCollect.declarator1 = new OclLetItem((String)null, this.currentThis.getType().celement, 5, (OclExpression)null);
         pCallCollect.searchResult = srCollect;
         SearchResult var10000 = pcClone.searchResult;
         var10000.foundType -= 64;
         var10000 = pcClone.searchResult;
         var10000.foundType |= 128;
         pcClone.searchResult.foundImplicitOwner = srCollect.declarator1;
         pcClone.searchResult.type = OclCodeGenUtilities.getType(pcClone.searchResult);
         pcClone.type = pcClone.searchResult.type;
         PostfixExpressionNode argNode = new PostfixExpressionNode();
         PropertyCallNode iteratorPc = new PropertyCallNode();
         iteratorPc.setUserObject(srCollect.declarator1);
         iteratorPc.setType(srCollect.declarator1.rettype);
         SyntaxTreeNode argPc = this.buildSyntaxTree(pcClone);
         argNode.add(iteratorPc);
         argNode.add(argPc);
         PropertyCallNode result = new PropertyCallNode(realOwner);
         result.setUserObject(pCallCollect);
         result.setType(pCallCollect.type);
         result.add(argNode);
         return result;
      }
   }

   private static int priority(String operator) {
      int ret = -1;
      if ("implies".equals(operator)) {
         ret = 0;
      } else if (!"or".equals(operator) && !"xor".equals(operator)) {
         if ("and".equals(operator)) {
            ret = 2;
         }
      } else {
         ret = 1;
      }

      return ret;
   }

   private void configureParameters(DefinitionConstraintNode dcn, OclNode fpl) {
      int n = fpl.getChildCount();

      for(int i = 0; i < n; i += 4) {
         OclType paramType = fpl.getChild(i + 2).type;
         Variable param = new Variable(fpl.getChild(i).getValueAsString(), paramType);
         dcn.addParameter(param);
         if (paramType.isTuple()) {
            this.result.registerTupleType(paramType);
         }
      }

   }

   private static void configureParameters(OperationConstraintGroup ocg, OclNode fpl) {
      int n = fpl.getChildCount();

      for(int i = 0; i < n; i += 4) {
         Variable p = new Variable(fpl.getChild(i).getValueAsString(), fpl.getChild(i + 2).type);
         ocg.addParameter(p);
      }

   }

   private class BuildVisitor extends OclVisitor {
      private BuildVisitor() {
      }

      public boolean visitPre(operationContext oc) throws ExceptionChecker {
         Operation op = oc.searchResult.operation;
         Object invalid = NormalFormBuilder.this.invalidContexts.get(UMLUtilities.getFullyQualifiedName(op));
         if (invalid == null) {
            NormalFormBuilder.this.currentContext = NormalFormBuilder.this.result.getConstrainedOperation(op);
            NormalFormBuilder.this.currentGroup = new OperationConstraintGroup();
            NormalFormBuilder.configureParameters(NormalFormBuilder.this.currentGroup, oc.getChild(4));
         } else {
            NormalFormBuilder.this.currentContext = null;
         }

         return false;
      }

      public boolean visitPre(classifierContext cc) throws ExceptionChecker {
         Classifier c = cc.type.type.classifier;
         Object invalid = NormalFormBuilder.this.invalidContexts.get(UMLUtilities.getFullyQualifiedName((ModelElement)c));
         if (invalid == null) {
            NormalFormBuilder.this.currentContext = NormalFormBuilder.this.result.getConstrainedClass(c);
         } else {
            NormalFormBuilder.this.currentContext = null;
         }

         return false;
      }

      public boolean visitPre(letExpression le) throws ExceptionChecker {
         if (NormalFormBuilder.this.currentContext == null) {
            return false;
         } else {
            if (le.getParent() instanceof constraint) {
               OclNode realContext = le.getParent().getChild(0).getChild(1);
               if (realContext instanceof operationContext) {
                  NormalizerWarning nw = new NormalizerWarning(le.getFilename(), le.getStart(), le.getStop(), "Local definitions ignored for operation contexts");
                  NormalFormBuilder.this.errorOccured(nw);
                  NormalFormBuilder.this.invalidContexts.put(UMLUtilities.getFullyQualifiedName(realContext.searchResult.operation), Boolean.TRUE);
                  NormalFormBuilder.this.result.removeConstrainedOperation((ConstrainedOperation)NormalFormBuilder.this.currentContext);
                  NormalFormBuilder.this.currentContext = null;
               } else {
                  DefinitionConstraintNode definitionConstraint = NormalFormBuilder.this.createDefinitionConstraint(le);
                  if (definitionConstraint != null) {
                     NormalFormBuilder.this.currentContext.addConstraint(definitionConstraint);
                  } else {
                     NormalizerWarning nwx = new NormalizerWarning(le.getFilename(), le.getStart(), le.getStop(), NormalFormBuilder.this.errorMessage);
                     NormalFormBuilder.this.errorOccured(nwx);
                  }
               }
            }

            return false;
         }
      }

      public boolean visitPre(oclExpression ocle) throws ExceptionChecker {
         if (NormalFormBuilder.this.currentContext != null) {
            AbstractConstraintNode cNode = NormalFormBuilder.this.createBasicConstraint(ocle);
            if (cNode != null) {
               NormalFormBuilder.this.currentContext.addConstraint(cNode);
            } else {
               NormalizerWarning nw = new NormalizerWarning(ocle.getFilename(), ocle.getStart(), ocle.getStop(), NormalFormBuilder.this.errorMessage);
               NormalFormBuilder.this.errorOccured(nw);
            }
         }

         return false;
      }

      public boolean visitPre(freeConstraint fc) throws ExceptionChecker {
         return false;
      }
   }
}
