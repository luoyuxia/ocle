package ro.ubbcluj.lci.ocl.codegen.gen;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import org.apache.velocity.Template;
import org.apache.velocity.context.Context;
import ro.ubbcluj.lci.codegen.CodeGenUtilities;
import ro.ubbcluj.lci.codegen.CodeGenerationConstants;
import ro.ubbcluj.lci.codegen.CodeGeneratorManager;
import ro.ubbcluj.lci.codegen.PGElement;
import ro.ubbcluj.lci.codegen.PGMethod;
import ro.ubbcluj.lci.ocl.ExceptionAny;
import ro.ubbcluj.lci.ocl.OclClassInfo;
import ro.ubbcluj.lci.ocl.OclLetItem;
import ro.ubbcluj.lci.ocl.OclType;
import ro.ubbcluj.lci.ocl.OclUtil;
import ro.ubbcluj.lci.ocl.SearchResult;
import ro.ubbcluj.lci.ocl.codegen.norm.CollectionNode;
import ro.ubbcluj.lci.ocl.codegen.norm.DefinitionConstraintNode;
import ro.ubbcluj.lci.ocl.codegen.norm.IfExpressionNode;
import ro.ubbcluj.lci.ocl.codegen.norm.LetExpressionNode;
import ro.ubbcluj.lci.ocl.codegen.norm.LiteralNode;
import ro.ubbcluj.lci.ocl.codegen.norm.NormalForm;
import ro.ubbcluj.lci.ocl.codegen.norm.OperatorNode;
import ro.ubbcluj.lci.ocl.codegen.norm.PostfixExpressionNode;
import ro.ubbcluj.lci.ocl.codegen.norm.PropertyCallNode;
import ro.ubbcluj.lci.ocl.codegen.norm.Qualifier;
import ro.ubbcluj.lci.ocl.codegen.norm.SyntaxCodeInfo;
import ro.ubbcluj.lci.ocl.codegen.norm.SyntaxTreeNode;
import ro.ubbcluj.lci.ocl.codegen.norm.SyntaxTreeVisitor;
import ro.ubbcluj.lci.ocl.codegen.norm.TupleNode;
import ro.ubbcluj.lci.ocl.codegen.norm.TuplePart;
import ro.ubbcluj.lci.ocl.codegen.norm.TypeNode;
import ro.ubbcluj.lci.ocl.codegen.norm.Undefined;
import ro.ubbcluj.lci.ocl.codegen.utils.OclCodeGenUtilities;
import ro.ubbcluj.lci.ocl.datatypes.OclEnumLiteral;
import ro.ubbcluj.lci.ocl.nodes.propertyCall;
import ro.ubbcluj.lci.uml.foundation.core.Association;
import ro.ubbcluj.lci.uml.foundation.core.AssociationClass;
import ro.ubbcluj.lci.uml.foundation.core.AssociationEnd;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.core.EnumerationLiteral;
import ro.ubbcluj.lci.utils.NamingContext;
import ro.ubbcluj.lci.utils.uml.UMLUtilities;

class CodeVisitor extends SyntaxTreeVisitor implements CodeGenerationConstants {
   private PGElement constrained;
   private NamingContext currentContext;
   private NormalForm normalForm;
   private String currentTupleVariable = "";
   private SyntaxTreeNode currentThis;
   StringBuffer codeBuffer = new StringBuffer();
   private static HashMap collectionProperties = null;
   private static Set oclAnyOperations = null;
   private static final String WRAPPING_PREFIX = "index_wrapped_";

   public CodeVisitor(PGElement constr, NamingContext ncInitial, NormalForm nf) {
      this.constrained = constr;
      this.currentContext = ncInitial;
      this.normalForm = nf;
   }

   public void visitPostfixExpression(PostfixExpressionNode pe) {
      SyntaxTreeNode current;
      for(Enumeration children = pe.children(); children.hasMoreElements(); this.currentThis = current) {
         current = (SyntaxTreeNode)children.nextElement();
         current.accept(this);
      }

      SyntaxCodeInfo lastci = this.currentThis.getCodeInfo();
      SyntaxCodeInfo sci = new SyntaxCodeInfo(lastci.getVariableName());
      sci.setText(lastci.getText());
      pe.setCodeInfo(sci);
   }

   public void visitType(TypeNode tn) {
      OclType encapsulatedType = (OclType)tn.getUserObject();
      SyntaxCodeInfo ci = new SyntaxCodeInfo((String)null);
      ci.setText(OclCodeGenUtilities.getTextForType(encapsulatedType, this.constrained.getImportManager()).toString());
      tn.setCodeInfo(ci);
   }

   public void visitTuplePart(TuplePart tp) {
      this.visitChildren(tp);
      String var = this.currentTupleVariable + '.' + tp.getName();
      String expr = ((SyntaxTreeNode)tp.getFirstChild()).getCodeInfo().getText();
      StringBuffer bf = CodeGenUtilities.getTextForAssignment((String)null, (String)null, var, expr);
      this.codeBuffer.append(bf);
   }

   public void visitLetExpression(LetExpressionNode letExpr) {
      DefinitionConstraintNode[] defs = letExpr.getLocalDefinitions();

      for(int i = 0; i < defs.length; ++i) {
         SyntaxTreeNode body = defs[i].getExpression();
         String typeName = OclCodeGenUtilities.getTypeName(defs[i].getReturnType(), this.constrained.getImportManager(), false);
         body.accept(this);
         this.codeBuffer.append(CodeGenUtilities.getTextForAssignment((String)null, typeName, defs[i].getName(), body.getCodeInfo().getText()));
      }

      SyntaxTreeNode body = letExpr.getRealExpression();
      body.accept(this);
      letExpr.setCodeInfo(body.getCodeInfo());
   }

   public void visitPropertyCall(PropertyCallNode pc) {
      Object userObject = pc.getUserObject();
      SyntaxCodeInfo sci = null;
      String vName = null;
      OclClassInfo propertyOwner = null;
      String propertyName;
      if (pc.getRealOwner() != null) {
         String typeName = OclCodeGenUtilities.getTypeName(pc.getRealOwner(), this.constrained.getImportManager());
         propertyName = this.cast(this.currentThis, pc.getRealOwner());
         String tempVarName = this.currentContext.registerNewName("casted");
         this.currentThis.getCodeInfo().setVariableName(tempVarName);
         this.codeBuffer.append(CodeGenUtilities.getTextForAssignment((String)null, typeName, tempVarName, propertyName));
      }

      if (userObject instanceof OclLetItem) {
         OclLetItem item = (OclLetItem)userObject;
         switch(item.vartype) {
         case 0:
            if (this.constrained instanceof PGMethod) {
               propertyName = ((PGElement)this.constrained.getParent()).getName();
            } else {
               propertyName = this.constrained.getName();
            }

            sci = new SyntaxCodeInfo((String)null);
            sci.setText(propertyName + ".this");
            pc.setCodeInfo(sci);
            break;
         case 1:
         case 4:
         case 5:
            sci = new SyntaxCodeInfo(item.name);
            pc.setCodeInfo(sci);
            break;
         case 2:
            if (pc.getParent() instanceof PostfixExpressionNode && pc.getData().foundOwner != null) {
               vName = this.currentContext.registerNewName(OclCodeGenUtilities.getBaseName(pc));
               sci = new SyntaxCodeInfo(vName);
               this.visitChildren(pc);
               ArrayList args = new ArrayList();
               configureActualArguments(pc, args);
               StringBuffer bfTempMethodCall = CodeGenUtilities.getTextForMethodCall(this.currentThis.getCodeInfo().getText(), item.name, args);
               String typeName = OclCodeGenUtilities.getTypeName(pc.getType(), this.constrained.getImportManager(), false);
               StringBuffer bfAssignment = CodeGenUtilities.getTextForAssignment((String)null, typeName, vName, bfTempMethodCall.toString());
               this.codeBuffer.append(bfAssignment);
            } else {
               sci = new SyntaxCodeInfo(item.name);
            }

            pc.setCodeInfo(sci);
         case 3:
         }
      } else {
         propertyCall pcall = (propertyCall)userObject;
         propertyOwner = OclCodeGenUtilities.getOwner(pcall.searchResult);
         vName = this.currentContext.registerNewName(OclCodeGenUtilities.getBaseName(pc));
         sci = new SyntaxCodeInfo(vName);
         pc.setCodeInfo(sci);
         propertyName = pc.getPropertyName();
         if (propertyOwner == OclUtil.umlapi.OCLANY) {
            this.updateForOclAnyPropertyCall(propertyName, pc, vName);
         } else if (propertyOwner != OclUtil.umlapi.STRING && propertyOwner != OclUtil.umlapi.REAL && propertyOwner != OclUtil.umlapi.INTEGER && propertyOwner != OclUtil.umlapi.BOOLEAN) {
            if (!propertyOwner.conforms(OclUtil.umlapi.COLLECTION)) {
               this.updateForModelPropertyCall(pcall.searchResult, propertyName, pc, vName);
            } else {
               this.updateForCollectionPropertyCall(propertyName, pc, vName);
            }
         } else {
            this.updateForStandardOclPropertyCall(propertyName, pc, vName);
         }
      }

   }

   public void visitIfExpression(IfExpressionNode ifen) {
      String vName = this.currentContext.registerNewName(OclCodeGenUtilities.getBaseName(ifen));
      SyntaxCodeInfo sci = new SyntaxCodeInfo(vName);
      Template tDecl = CodeGeneratorManager.getTemplate("OCL_declaration");
      Context cDecl = CodeGeneratorManager.newContext();
      cDecl.put("VAR", vName);
      String typeName = OclCodeGenUtilities.getTypeName(ifen.getType(), this.constrained.getImportManager(), false);
      cDecl.put("TYPE", typeName);
      this.codeBuffer.append(CodeGenUtilities.getText(tDecl, cDecl));
      ((SyntaxTreeNode)ifen.getFirstChild()).accept(this);
      Template tIf = CodeGeneratorManager.getTemplate("OCL_if_else");
      Context cIf = CodeGeneratorManager.newContext();
      cIf.put("RESULT", vName);
      cIf.put("TEST", ((SyntaxTreeNode)ifen.getFirstChild()).getCodeInfo().getText());
      CodeVisitor cvIf = new CodeVisitor(this.constrained, this.currentContext, this.normalForm);
      SyntaxTreeNode ifBranch = (SyntaxTreeNode)ifen.getChildAt(1);
      SyntaxTreeNode elseBranch = (SyntaxTreeNode)ifen.getChildAt(2);
      ifBranch.accept(cvIf);
      CodeVisitor cvElse = new CodeVisitor(this.constrained, this.currentContext, this.normalForm);
      elseBranch.accept(cvElse);
      cIf.put("IF", ifBranch.getCodeInfo().getText());
      cIf.put("ELSE", elseBranch.getCodeInfo().getText());
      cIf.put("BODY_IF", cvIf.codeBuffer.toString());
      cIf.put("BODY_ELSE", cvElse.codeBuffer.toString());
      StringBuffer sbIf = CodeGenUtilities.getText(tIf, cIf);
      this.codeBuffer.append(sbIf);
      ifen.setCodeInfo(sci);
   }

   public void visitCollection(CollectionNode cn) {
      Object cKind = cn.getUserObject();
      String qualifiedTypeName = null;
      if ("Collection".equals(cKind)) {
         qualifiedTypeName = "java.util.Collection";
      } else if ("OrderedSet".equals(cKind)) {
         qualifiedTypeName = CodeGenerationConstants.DATATYPES_PATH + ".OrderedSet";
      } else if ("Set".equals(cKind)) {
         qualifiedTypeName = CodeGenerationConstants.JAVA_SET_TYPE;
      } else if ("Bag".equals(cKind) || "Sequence".equals(cKind)) {
         qualifiedTypeName = "java.util.List";
      }

      qualifiedTypeName = CodeGenUtilities.getTypeName(qualifiedTypeName, this.constrained.getImportManager());
      String vName = this.currentContext.registerNewName(OclCodeGenUtilities.getBaseName(cn));
      SyntaxCodeInfo sci = new SyntaxCodeInfo(vName);
      String temp = CodeGenUtilities.getTypeName(CodeGenerationConstants.OCL_PATH + ".CollectionUtilities", this.constrained.getImportManager());
      String expr = temp + ".new" + cKind + "()";
      this.codeBuffer.append(CodeGenUtilities.getTextForAssignment((String)null, qualifiedTypeName, vName, expr));
      Enumeration children = cn.children();
      ArrayList args = new ArrayList();
      ArrayList args2 = null;
      args.add(vName);

      while(children.hasMoreElements()) {
         SyntaxTreeNode next = (SyntaxTreeNode)children.nextElement();
         next.accept(this);
         boolean bRangeSpecifier = false;
         if (next instanceof OperatorNode && ((OperatorNode)next).getText().equals("..")) {
            bRangeSpecifier = true;
         }

         String arg;
         if (!bRangeSpecifier) {
            arg = next.getCodeInfo().getText();
            if (next.getType().isPrimitiveType()) {
               arg = OclCodeGenUtilities.getExpression(arg, next.getType(), this.constrained.getImportManager(), true);
            }

            if (args.size() < 2) {
               args.add(arg);
            } else {
               args.set(1, arg);
            }

            this.codeBuffer.append(CodeGenUtilities.getTextForMethodCall(temp, "add", args).append(";\n"));
         } else {
            this.visitChildren(next);
            arg = ((SyntaxTreeNode)next.getChildAt(0)).getCodeInfo().getText();
            Object a2 = ((SyntaxTreeNode)next.getChildAt(1)).getCodeInfo().getText();
            if (args2 == null) {
               args2 = new ArrayList();
               args2.add(vName);
            }

            if (args2.size() < 2) {
               args2.add(arg);
            } else {
               args2.set(1, arg);
            }

            if (args2.size() < 3) {
               args2.add(a2);
            } else {
               args2.set(2, a2);
            }

            this.codeBuffer.append(CodeGenUtilities.getTextForMethodCall(temp, "addRange", args2).append(";\n"));
         }
      }

      cn.setCodeInfo(sci);
   }

   public void visitOperator(OperatorNode on) {
      if (!"..".equals(on.getText())) {
         String vName = this.currentContext.registerNewName(OclCodeGenUtilities.getBaseName(on));
         SyntaxCodeInfo sci = null;
         SyntaxTreeNode first = (SyntaxTreeNode)on.getChildAt(0);
         SyntaxTreeNode second = null;
         if (on.getChildCount() > 1) {
            second = (SyntaxTreeNode)on.getChildAt(1);
         } else {
            second = first;
            first = null;
         }

         this.visitChildren(on);
         StringBuffer typeBuffer = new StringBuffer();
         String expr = OclCodeGenUtilities.getOperatorExpression(on, first, second, typeBuffer, this.constrained.getImportManager());
         this.codeBuffer.append(CodeGenUtilities.getTextForAssignment((String)null, typeBuffer.toString(), vName, expr));
         sci = new SyntaxCodeInfo(vName);
         on.setCodeInfo(sci);
      }
   }

   public void visitLiteral(LiteralNode ln) {
      SyntaxCodeInfo sci = new SyntaxCodeInfo((String)null);
      Object uo = ln.getUserObject();
      if (uo instanceof OclEnumLiteral) {
         OclEnumLiteral ocle = (OclEnumLiteral)uo;
         String type = OclCodeGenUtilities.getTypeName(ocle.getEnumeration(), this.constrained.getImportManager());
         sci.setText(type + '.' + ocle.getName());
      } else if (!(uo instanceof Boolean) && !(uo instanceof Integer) && !(uo instanceof Float) && !(uo instanceof String)) {
         if (uo instanceof EnumerationLiteral) {
            EnumerationLiteral enLit = (EnumerationLiteral)uo;
            ro.ubbcluj.lci.uml.foundation.core.Enumeration enum = enLit.getEnumeration();

            try {
               OclClassInfo ci = OclUtil.umlapi.getClassInfoByClassifier(enum);
               String type = OclCodeGenUtilities.getTypeName(ci, this.constrained.getImportManager());
               sci.setText(type + "." + enLit.getName());
            } catch (ExceptionAny var8) {
               throw new RuntimeException(var8);
            }
         } else {
            if (!(uo instanceof Undefined)) {
               throw new RuntimeException("Illegal user object for a literal node: " + uo);
            }

            sci.setText("null");
         }
      } else {
         String text = uo.toString();
         if (uo instanceof Float) {
            text = text + 'f';
         }

         sci.setText(text);
      }

      ln.setCodeInfo(sci);
   }

   public void visitTuple(TupleNode tn) {
      String fullTypeName = OclCodeGenUtilities.getTupleTypesPackage() + '.' + this.normalForm.tupleTypeName(tn.getType());
      String variableName = this.currentContext.registerNewName(OclCodeGenUtilities.getBaseName(tn));
      SyntaxCodeInfo ci = new SyntaxCodeInfo(variableName);
      fullTypeName = OclCodeGenUtilities.getTypeName(fullTypeName, this.constrained.getImportManager());
      StringBuffer initializer = CodeGenUtilities.getTextForAssignment((String)null, fullTypeName, variableName, "new " + fullTypeName + "()");
      this.codeBuffer.append(initializer);
      String oldTuple = this.currentTupleVariable;
      this.currentTupleVariable = variableName;
      this.visitChildren(tn);
      this.currentTupleVariable = oldTuple;
      tn.setCodeInfo(ci);
   }

   private void visitChildren(SyntaxTreeNode node) {
      Enumeration en = node.children();
      CodeVisitor cvChildren = new CodeVisitor(this.constrained, (NamingContext)null, this.normalForm);
      cvChildren.currentThis = this.currentThis;
      cvChildren.codeBuffer = this.codeBuffer;
      cvChildren.currentContext = this.currentContext;
      cvChildren.currentTupleVariable = this.currentTupleVariable;

      while(en.hasMoreElements()) {
         ((SyntaxTreeNode)en.nextElement()).accept(cvChildren);
      }

   }

   private void updateForOclAnyPropertyCall(String propertyName, PropertyCallNode pc, String vName) {
      String selfSpec = OclCodeGenUtilities.getExpression(this.currentThis.getCodeInfo().getText(), this.currentThis.getType(), this.constrained.getImportManager(), true);
      ArrayList args = new ArrayList();
      args.add(selfSpec);
      this.visitChildren(pc);
      configureActualArguments(pc, args);
      String oclClassPropertyName;
      String temp;
      if (!"oclAsType".equals(propertyName)) {
         if (propertyName.startsWith("ocl")) {
            temp = propertyName.substring(3);
            oclClassPropertyName = Character.toLowerCase(temp.charAt(0)) + temp.substring(1);
         } else {
            oclClassPropertyName = propertyName;
         }

         String typeName = OclCodeGenUtilities.getTypeName(CodeGenerationConstants.OCL_PATH + ".Ocl", this.constrained.getImportManager());
         if ("dump".equals(propertyName)) {
            SyntaxCodeInfo sci = new SyntaxCodeInfo((String)null);
            sci.setText("true");
            pc.setCodeInfo(sci);
         } else if ("dumpi".equals(propertyName)) {
            pc.setCodeInfo(this.currentThis.getCodeInfo());
         } else {
            StringBuffer bfMethodCall = CodeGenUtilities.getTextForMethodCall(typeName, oclClassPropertyName, args);
            String resultTypeName = OclCodeGenUtilities.getTypeName(pc.getType().type, this.constrained.getImportManager());
            StringBuffer bfAssignment = CodeGenUtilities.getTextForAssignment((String)null, resultTypeName, vName, bfMethodCall.toString());
            this.codeBuffer.append(bfAssignment);
         }
      } else {
         oclClassPropertyName = OclCodeGenUtilities.getTypeName(pc.getType().type, this.constrained.getImportManager());
         temp = this.cast(this.currentThis, pc.getType().type);
         this.codeBuffer.append(CodeGenUtilities.getTextForAssignment((String)null, oclClassPropertyName, vName, temp));
      }

   }

   private String cast(SyntaxTreeNode base, OclClassInfo resultType) {
      OclType baseType = base.getType();
      boolean baseIsPrimitive = baseType.isPrimitiveType();
      boolean resultIsPrimitive = resultType.isPrimitiveType();
      String result = null;
      boolean castRequired;
      String wrappedBase;
      if (baseIsPrimitive == resultIsPrimitive) {
         castRequired = !baseType.type.conforms(resultType);
         if (castRequired) {
            wrappedBase = OclCodeGenUtilities.getTypeName(resultType, this.constrained.getImportManager());
            result = CodeGenUtilities.getTextForCast(wrappedBase, base.getCodeInfo().getText());
         } else {
            result = base.getCodeInfo().getText();
         }
      } else {
         String resultTypeName;
         if (baseIsPrimitive) {
            castRequired = !baseType.type.conforms(resultType);
            wrappedBase = OclCodeGenUtilities.getExpression(base.getCodeInfo().getText(), baseType, this.constrained.getImportManager(), true);
            if (castRequired) {
               resultTypeName = OclCodeGenUtilities.getTypeName(resultType, this.constrained.getImportManager());
               result = CodeGenUtilities.getTextForCast(resultTypeName, wrappedBase);
            } else {
               result = wrappedBase;
            }
         } else {
            OclType rt = new OclType(resultType);
            resultTypeName = OclCodeGenUtilities.getTypeName(rt, this.constrained.getImportManager(), true);
            result = OclCodeGenUtilities.castAndUnwrap(rt, base.getCodeInfo().getText(), resultTypeName);
         }
      }

      return result;
   }

   private void updateForStandardOclPropertyCall(String propertyName, PropertyCallNode pc, String vName) {
      String returnTypeName = OclCodeGenUtilities.getTypeName(pc.getType(), this.constrained.getImportManager(), false);
      String basicUtilitiesTypeName = OclCodeGenUtilities.getTypeName(CodeGenerationConstants.OCL_PATH + ".BasicUtilities", this.constrained.getImportManager());
      ArrayList args = new ArrayList();
      this.visitChildren(pc);
      args.add(this.currentThis.getCodeInfo().getText());
      configureActualArguments(pc, args);
      StringBuffer bfMethodCall = CodeGenUtilities.getTextForMethodCall(basicUtilitiesTypeName, propertyName, args);
      StringBuffer bfAssign = CodeGenUtilities.getTextForAssignment((String)null, returnTypeName, vName, bfMethodCall.toString());
      this.codeBuffer.append(bfAssign);
   }

   private void updateForModelPropertyCall(SearchResult sr, String propertyName, PropertyCallNode pc, String vName) {
      String typeName = OclCodeGenUtilities.getTypeName(pc.getType().type, this.constrained.getImportManager());
      boolean isStaticFeature = (sr.foundType & 256) == 256;
      String ownerName = null;
      if (isStaticFeature) {
         ownerName = OclCodeGenUtilities.getTypeName(OclCodeGenUtilities.getOwner(sr), this.constrained.getImportManager());
      }

      String objSpec;
      StringBuffer bfAssignment;
      StringBuffer bfMethodCall;
      if (sr.operation != null) {
         this.visitChildren(pc);
         ArrayList args = new ArrayList();
         configureActualArguments(pc, args);
         objSpec = isStaticFeature ? ownerName : this.currentThis.getCodeInfo().getText();
         bfMethodCall = CodeGenUtilities.getTextForMethodCall(objSpec, propertyName, args);
         bfAssignment = CodeGenUtilities.getTextForAssignment((String)null, typeName, vName, bfMethodCall.toString());
      } else {
         StringBuffer bfAttrCall;
         if (sr.attr != null) {
            objSpec = isStaticFeature ? ownerName : this.currentThis.getCodeInfo().getText();
            bfAttrCall = CodeGenUtilities.getTextForAttributeCall(objSpec, propertyName);
            bfAssignment = CodeGenUtilities.getTextForAssignment((String)null, typeName, vName, bfAttrCall.toString());
         } else if (sr.asend != null) {
            AssociationEnd ae = sr.asend;
            String endName = ae.getName();
            List qualifierList = pc.getQualifiers();
            AssociationEnd opEnd = UMLUtilities.getOppositeAssociationEnd(ae);
            Classifier source = opEnd.getParticipant();
            boolean bFromAssociationClass = UMLUtilities.fromAssociationClass(ae);
            String getterMethodName;
            if (bFromAssociationClass) {
               getterMethodName = CodeGeneratorManager.getActiveInstance().getAccessorMethodName(CodeGenerationConstants.GETTER_PREFIX, source, endName);
               bfMethodCall = CodeGenUtilities.getTextForMethodCall(this.currentThis.getCodeInfo().getText(), getterMethodName, (List)null);
            } else {
               boolean bTowardsAssociationClass = UMLUtilities.towardsAssociationClass(ae);
               if (bTowardsAssociationClass) {
                  if (qualifierList.isEmpty()) {
                     endName = UMLUtilities.getOppositeAssociationEnd(source, (Association)ae.getParticipant()).getName();
                     getterMethodName = CodeGeneratorManager.getActiveInstance().getAccessorMethodName(CodeGenerationConstants.GETTER_PREFIX, source, ae.getParticipant().getName() + endName);
                  } else {
                     Qualifier simpleQualifier = (Qualifier)qualifierList.get(0);
                     String qName = simpleQualifier.getEndName();
                     AssociationEnd end = UMLUtilities.getAssociationEnd((Association)ae.getParticipant(), qName);
                     getterMethodName = CodeGeneratorManager.getActiveInstance().getAccessorMethodName(CodeGenerationConstants.GETTER_PREFIX, source, ae.getParticipant().getName() + end.getName());
                  }

                  bfMethodCall = CodeGenUtilities.getTextForMethodCall(this.currentThis.getCodeInfo().getText(), getterMethodName, (List)null);
               } else {
                  boolean bOverAssociationClass = ae.getAssociation() instanceof AssociationClass;
                  if (bOverAssociationClass) {
                     getterMethodName = CodeGeneratorManager.getActiveInstance().getAccessorMethodName(CodeGenerationConstants.GETTER_PREFIX, source, endName);
                  } else {
                     getterMethodName = CodeGeneratorManager.getActiveInstance().getAccessorMethodName(CodeGenerationConstants.GETTER_PREFIX, source, endName);
                  }

                  if (qualifierList.isEmpty()) {
                     bfMethodCall = CodeGenUtilities.getTextForMethodCall(this.currentThis.getCodeInfo().getText(), getterMethodName, (List)null);
                  } else {
                     ArrayList qualifierArguments = new ArrayList();
                     Iterator itQualifiers = qualifierList.iterator();

                     while(itQualifiers.hasNext()) {
                        Qualifier q = (Qualifier)itQualifiers.next();
                        SyntaxTreeNode qRoot = q.getExpression();
                        qRoot.accept(this);
                        qualifierArguments.add(qRoot.getCodeInfo().getText());
                     }

                     bfMethodCall = CodeGenUtilities.getTextForMethodCall(this.currentThis.getCodeInfo().getText(), getterMethodName, qualifierArguments);
                  }
               }
            }

            bfAssignment = CodeGenUtilities.getTextForAssignment((String)null, typeName, vName, bfMethodCall.toString());
         } else {
            objSpec = isStaticFeature ? ownerName : this.currentThis.getCodeInfo().getText();
            bfAttrCall = CodeGenUtilities.getTextForAttributeCall(objSpec, propertyName);
            bfAssignment = CodeGenUtilities.getTextForAssignment((String)null, typeName, vName, bfAttrCall.toString());
         }
      }

      this.codeBuffer.append(bfAssignment);
   }

   private void updateForCollectionPropertyCall(String propertyName, PropertyCallNode pc, String vName) {
      if (collectionProperties == null) {
         loadCollectionPropeties();
      }

      if (OclUtil.loopOperations.containsKey(propertyName)) {
         this.updateForLoopOperation(propertyName, pc, vName);
      } else {
         this.visitChildren(pc);
         int i = 0;
         int[] indexesOfWrapping = (int[])collectionProperties.get("index_wrapped_" + propertyName);
         Enumeration children = pc.children();
         ArrayList args = new ArrayList();
         args.add(this.currentThis.getCodeInfo().getText());

         String resultType;
         for(; children.hasMoreElements(); ++i) {
            SyntaxTreeNode child = (SyntaxTreeNode)children.nextElement();
            resultType = child.getCodeInfo().getText();
            if (!child.getType().isPrimitiveType()) {
               args.add(resultType);
            } else {
               boolean mustWrap = false;
               if (indexesOfWrapping != null) {
                  for(int j = 0; j < indexesOfWrapping.length && !mustWrap; ++j) {
                     if (i == indexesOfWrapping[j]) {
                        mustWrap = true;
                     }
                  }
               }

               String realArg = OclCodeGenUtilities.getExpression(resultType, child.getType(), this.constrained.getImportManager(), mustWrap);
               args.add(realArg);
            }
         }

         String cuTypeName = OclCodeGenUtilities.getTypeName(CodeGenerationConstants.OCL_PATH + ".CollectionUtilities", this.constrained.getImportManager());
         resultType = OclCodeGenUtilities.getTypeName(pc.getType(), this.constrained.getImportManager(), false);
         String call = CodeGenUtilities.getTextForMethodCall(cuTypeName, propertyName, args).toString();
         boolean isSum = "sum".equals(propertyName);
         String resultTypeWrapped = null;
         String realExp;
         StringBuffer bfAssign;
         if (oclAnyOperations.contains(propertyName)) {
            if (pc.getType().isPrimitiveType()) {
               resultTypeWrapped = OclCodeGenUtilities.getTypeName(pc.getType(), this.constrained.getImportManager(), true);
            } else {
               resultTypeWrapped = OclCodeGenUtilities.getTypeName(pc.getType(), this.constrained.getImportManager(), false);
            }

            realExp = OclCodeGenUtilities.castAndUnwrap(pc.getType(), call, resultTypeWrapped);
            bfAssign = CodeGenUtilities.getTextForAssignment((String)null, resultType, vName, realExp);
         } else if (isSum && pc.getType().type == OclUtil.umlapi.INTEGER) {
            realExp = CodeGenUtilities.getTextForCast(resultType, call);
            bfAssign = CodeGenUtilities.getTextForAssignment((String)null, resultType, vName, realExp);
         } else {
            bfAssign = CodeGenUtilities.getTextForAssignment((String)null, resultType, vName, call);
         }

         this.codeBuffer.append(bfAssign);
      }

   }

   private void updateForLoopOperation(String operationName, PropertyCallNode pc, String vName) {
      propertyCall loopPc = (propertyCall)pc.getUserObject();
      String templateNameSuffix = operationName;
      if ("forAll".equals(operationName)) {
         if (loopPc.searchResult.declarator2 != null) {
            templateNameSuffix = operationName + '2';
         } else {
            templateNameSuffix = operationName + '1';
         }
      }

      Template tLoop = CodeGeneratorManager.getTemplate("OCL_loop_" + templateNameSuffix);
      Context cLoop = CodeGeneratorManager.newContext();
      cLoop.put("RESULT", vName);
      cLoop.put("OCL", loopPc.getValueAsString());
      cLoop.put("SELF", this.currentThis.getCodeInfo().getText());
      String javaIteratorClassName = OclCodeGenUtilities.getTypeName("java.util.Iterator", this.constrained.getImportManager());
      cLoop.put("JAVA_ITERATOR_TYPE_NAME", javaIteratorClassName);
      cLoop.put("JAVA_ITERATOR1", this.currentContext.registerNewName("iter"));
      if (templateNameSuffix.endsWith("2")) {
         cLoop.put("JAVA_ITERATOR2", this.currentContext.registerNewName("iter"));
      }

      String declName = null;
      if (loopPc.searchResult.declarator1.name == null) {
         declName = loopPc.searchResult.declarator1.name = this.currentContext.registerNewName("decl");
      } else {
         if (this.currentContext.contains(loopPc.searchResult.declarator1.name)) {
            loopPc.searchResult.declarator1.name = this.currentContext.registerNewName("decl");
         }

         declName = loopPc.searchResult.declarator1.name;
      }

      cLoop.put("DECL1_NAME", declName);
      if (templateNameSuffix.endsWith("2")) {
         if (this.currentContext.contains(loopPc.searchResult.declarator2.name)) {
            loopPc.searchResult.declarator2.name = this.currentContext.registerNewName("decl");
         }

         declName = loopPc.searchResult.declarator2.name;
         cLoop.put("DECL2_NAME", declName);
      }

      OclType declaratorType = loopPc.searchResult.declarator1.rettype;
      cLoop.put("DECL_TYPE", declaratorType);
      String declaratorTypeName = OclCodeGenUtilities.getTypeName(declaratorType, this.constrained.getImportManager(), false);
      cLoop.put("DECL_TYPE_NAME", declaratorTypeName);
      String temp1;
      if (declaratorType.isPrimitiveType()) {
         temp1 = OclCodeGenUtilities.getTypeName(declaratorType, this.constrained.getImportManager(), true);
         cLoop.put("DECL_TYPE_WRAPPER_NAME", temp1);
      } else {
         cLoop.put("DECL_TYPE_WRAPPER_NAME", declaratorTypeName);
      }

      String cuTypeName = null;
      String resultTypeName = null;
      String accVarName = null;
      if ("iterate".equals(operationName)) {
         SyntaxTreeNode accInit = (SyntaxTreeNode)pc.getChildAt(0);
         accVarName = loopPc.searchResult.accumulator.name;
         CodeVisitor cvIterateAcc = new CodeVisitor(this.constrained, this.currentContext, this.normalForm);
         if (this.currentContext.contains(accVarName)) {
            loopPc.searchResult.accumulator.name = accVarName = this.currentContext.registerNewName(accVarName);
         } else {
            this.currentContext.add(accVarName);
         }

         cLoop.put("ACC", accVarName);
         accInit.accept(cvIterateAcc);
         cLoop.put("INIT_ACC", cvIterateAcc.codeBuffer.toString());
         cLoop.put("INITIAL_VALUE", accInit.getCodeInfo().getText());
      }

      if ("select".equals(operationName) || "reject".equals(operationName) || "isUnique".equals(operationName) || operationName.startsWith("collect") || "closure".equals(operationName)) {
         cuTypeName = OclCodeGenUtilities.getTypeName(CodeGenerationConstants.OCL_PATH + ".CollectionUtilities", this.constrained.getImportManager());
         cLoop.put("COLLECTION_UTILITIES_TYPE_NAME", cuTypeName);
         if ("isUnique".equals(operationName) || "closure".equals(operationName)) {
            cLoop.put("JAVA_SET_TYPE_NAME", OclCodeGenUtilities.getTypeName("java.util.Set", this.constrained.getImportManager()));
         }
      }

      if ("select".equals(operationName) || "reject".equals(operationName)) {
         temp1 = cuTypeName + ".new" + pc.getType().name() + "()";
         cLoop.put("INITIAL_VALUE", temp1);
      }

      if ("select".equals(operationName) || "reject".equals(operationName) || "iterate".equals(operationName) || "sortedBy".equals(operationName) || "any".equals(operationName) || operationName.startsWith("collect")) {
         resultTypeName = OclCodeGenUtilities.getTypeName(pc.getType().type, this.constrained.getImportManager());
         cLoop.put("RESULT_TYPE_NAME", resultTypeName);
         if ("any".equals(operationName)) {
            cLoop.put("RESULT_TYPE_WRAPPER_NAME", OclCodeGenUtilities.getTypeName(pc.getType(), this.constrained.getImportManager(), true));
            cLoop.put("DEFAULT_VALUE", OclCodeGenUtilities.getDefaultValueForUndefined(pc.getType(), this.constrained.getImportManager()));
         }

         if ("sortedBy".equals(operationName) || "any".equals(operationName)) {
            cLoop.put("RESULT_TYPE", pc.getType());
         }
      }

      if ("sortedBy".equals(operationName)) {
         String tempTypeName = OclCodeGenUtilities.getTypeName(CodeGenerationConstants.OCL_PATH + ".Pairs", this.constrained.getImportManager());
         cLoop.put("TEMP_TYPE_NAME", tempTypeName);
         cLoop.put("INITIAL_VALUE", "new " + tempTypeName + "()");
      }

      if ("any".equals(operationName)) {
         cLoop.put("TEMP", this.currentContext.registerNewName("temp"));
      } else if ("sortedBy".equals(operationName)) {
         cLoop.put("TEMP", this.currentContext.registerNewName("sortedPairs"));
      } else if ("isUnique".equals(operationName)) {
         cLoop.put("TEMP", this.currentContext.registerNewName("uniquenessValidator"));
      } else if ("one".equals(operationName)) {
         cLoop.put("TEMP", this.currentContext.registerNewName("counter"));
      } else if ("closure".equals(operationName)) {
         cLoop.put("TEMP", this.currentContext.registerNewName("queue"));
      }

      if ("sortedBy".equals(operationName) || "any".equals(operationName) || "closure".equals(operationName)) {
         temp1 = "closure".equals(operationName) ? this.currentContext.registerNewName("i") : this.currentContext.registerNewName("temp");
         cLoop.put("TEMP1", temp1);
         if ("closure".equals(operationName)) {
            cLoop.put("TEMP2", this.currentContext.registerNewName("obj"));
         }

         cLoop.put("JAVA_OBJECT_TYPE_NAME", OclCodeGenUtilities.getTypeName("java.lang.Object", this.constrained.getImportManager()));
      }

      int argumentIndex = 0;
      if ("iterate".equals(operationName)) {
         argumentIndex = 1;
      }

      SyntaxTreeNode argExpr = (SyntaxTreeNode)pc.getChildAt(argumentIndex);
      if ("sortedBy".equals(operationName) || "isUnique".equals(operationName) || operationName.startsWith("collect") || "closure".equals(operationName)) {
         OclType argType = argExpr.getType();
         cLoop.put("ARG_TYPE", argType);
         cLoop.put("ARG_TYPE_WRAPPER_NAME", OclCodeGenUtilities.getTypeName(argType, this.constrained.getImportManager(), true));
      }

      CodeVisitor cvArgExpr = new CodeVisitor(this.constrained, this.currentContext, this.normalForm);
      argExpr.accept(cvArgExpr);
      cLoop.put("BODY_LOOP", cvArgExpr.codeBuffer.toString());
      cLoop.put("ARG", argExpr.getCodeInfo().getText());
      StringBuffer loopOpCode = CodeGenUtilities.getText(tLoop, cLoop);
      this.codeBuffer.append(loopOpCode);
   }

   private static void configureActualArguments(PropertyCallNode pc, List args) {
      Enumeration children = pc.children();

      while(children.hasMoreElements()) {
         SyntaxTreeNode c = (SyntaxTreeNode)children.nextElement();
         args.add(c.getCodeInfo().getText());
      }

   }

   private static void loadCollectionPropeties() {
      Properties p = new Properties();
      InputStream is = (CodeVisitor.class).getResourceAsStream("collections.properties");
      if (is == null) {
         throw new InternalError("File \"collections.properties\" not found.");
      } else {
         try {
            p.load(is);
            collectionProperties = new HashMap();
            oclAnyOperations = new HashSet();
            Enumeration keys = p.keys();

            while(true) {
               while(keys.hasMoreElements()) {
                  String key = (String)keys.nextElement();
                  String prop = p.getProperty(key);
                  StringTokenizer tok = new StringTokenizer(prop, ", ", false);
                  if (key.startsWith("index_wrapped_")) {
                     int i = 0;

                     int[] indexes;
                     String token;
                     for(indexes = new int[tok.countTokens()]; tok.hasMoreTokens(); indexes[i++] = Integer.parseInt(token)) {
                        token = tok.nextToken();
                     }

                     collectionProperties.put(key, indexes);
                  } else {
                     while(tok.hasMoreTokens()) {
                        oclAnyOperations.add(tok.nextToken());
                     }
                  }
               }

               return;
            }
         } catch (IOException var9) {
            throw new InternalError("Cannot read from \"collections.properties\" file:" + var9.getMessage());
         } catch (NumberFormatException var10) {
            throw new InternalError("Invalid number in the properties file:" + var10.getMessage());
         }
      }
   }
}
