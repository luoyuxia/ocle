package ro.ubbcluj.lci.ocl.codegen.gen;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import org.apache.velocity.Template;
import org.apache.velocity.context.Context;
import ro.ubbcluj.lci.codegen.CodeDecorator;
import ro.ubbcluj.lci.codegen.CodeGenUtilities;
import ro.ubbcluj.lci.codegen.CodeGenerationConstants;
import ro.ubbcluj.lci.codegen.CodeGeneratorManager;
import ro.ubbcluj.lci.codegen.JavaReservedWords;
import ro.ubbcluj.lci.codegen.PGBlock;
import ro.ubbcluj.lci.codegen.PGClass;
import ro.ubbcluj.lci.codegen.PGElement;
import ro.ubbcluj.lci.codegen.PGField;
import ro.ubbcluj.lci.codegen.PGMethod;
import ro.ubbcluj.lci.codegen.PGParameter;
import ro.ubbcluj.lci.ocl.codegen.norm.AbstractConstraintNode;
import ro.ubbcluj.lci.ocl.codegen.norm.BasicConstraintNode;
import ro.ubbcluj.lci.ocl.codegen.norm.ConstrainedClass;
import ro.ubbcluj.lci.ocl.codegen.norm.ConstrainedOperation;
import ro.ubbcluj.lci.ocl.codegen.norm.DefinitionConstraintNode;
import ro.ubbcluj.lci.ocl.codegen.norm.NormalForm;
import ro.ubbcluj.lci.ocl.codegen.norm.OperationConstraintGroup;
import ro.ubbcluj.lci.ocl.codegen.norm.SyntaxCodeInfo;
import ro.ubbcluj.lci.ocl.codegen.norm.SyntaxTreeNode;
import ro.ubbcluj.lci.ocl.codegen.norm.Variable;
import ro.ubbcluj.lci.ocl.codegen.utils.OclCodeGenUtilities;
import ro.ubbcluj.lci.uml.foundation.core.Class;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.core.GeneralizableElement;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.uml.foundation.core.Operation;
import ro.ubbcluj.lci.uml.foundation.core.Parameter;
import ro.ubbcluj.lci.utils.NamingContext;
import ro.ubbcluj.lci.utils.Utils;
import ro.ubbcluj.lci.utils.uml.InheritanceAndAbstractionUtility;
import ro.ubbcluj.lci.utils.uml.UMLUtilities;

public class OclCodeGenerator implements CodeDecorator, CodeGenerationConstants, JavaReservedWords {
   private NormalForm normalForm;
   private Classifier currentContext;

   public OclCodeGenerator(NormalForm nf) {
      this.normalForm = nf;
   }

   public void decorate(PGClass cls) {
      this.realUpdateForClass(cls);
   }

   private void realUpdateForClass(PGClass cls) {
      Classifier ccls = (Classifier)cls.getUserObject();
      ConstrainedClass cc = this.normalForm != null ? this.normalForm.getFor(ccls) : null;
      if (cc != null) {
         ArrayList invs = new ArrayList();
         ArrayList defs = new ArrayList();
         AbstractConstraintNode[] constr = cc.getConstraints();

         int i;
         for(i = 0; i < constr.length; ++i) {
            if (constr[i].getKind() == 0) {
               invs.add(constr[i]);
            } else {
               defs.add(constr[i]);
            }
         }

         for(i = 0; i < defs.size(); ++i) {
            this.createDefinitionConstraint((DefinitionConstraintNode)defs.get(i), cls);
         }

         if (invs.size() > 0) {
            this.createConstraintChecker(cls, cc, (BasicConstraintNode[])invs.toArray(new BasicConstraintNode[0]));
         }
      }

      ArrayList toAdd = new ArrayList();
      Enumeration children = cls.children();

      while(children.hasMoreElements()) {
         Object elem = children.nextElement();
         if (elem instanceof PGClass) {
            this.realUpdateForClass((PGClass)elem);
         } else if (elem instanceof PGMethod) {
            PGMethod proxy = this.updateForMethod((PGMethod)elem, cls);
            if (proxy != null) {
               toAdd.add(proxy);
            }
         }
      }

      int i = 0;

      for(int n = toAdd.size(); i < n; ++i) {
         cls.add((PGMethod)toAdd.get(i));
      }

      if (this.normalForm != null) {
         this.updateForAllInstances(cls);
      }

   }

   private void createConstraintChecker(PGClass cls, ConstrainedClass cc, BasicConstraintNode[] invs) {
      PGClass constraintChecker = new PGClass(cls);
      constraintChecker.setName(cc.getCodeInfo().getInnerClassName());
      constraintChecker.addModifier("public");
      constraintChecker.addModifier("class");
      ConstrainedClass parent = cc.getCodeInfo().getBaseClass();
      String parentName = null;
      String globalCheckerMethodName;
      if (parent == null) {
         parentName = OclCodeGenUtilities.getTypeName(CodeGenerationConstants.OCL_PATH + ".BasicConstraintChecker", cls.getImportManager());
      } else {
         globalCheckerMethodName = CodeGenUtilities.getTypeName(parent.getConstrainedClass(), cls.getImportManager());
         parentName = globalCheckerMethodName + '.' + parent.getCodeInfo().getInnerClassName();
      }

      constraintChecker.addExtClass(parentName);
      globalCheckerMethodName = "checkConstraints";
      PGMethod checkerMethod = new PGMethod(constraintChecker);
      checkerMethod.setName("checkConstraints");
      checkerMethod.addModifier("public");
      StringBuffer bfBody = new StringBuffer();
      bfBody.append(CodeGenUtilities.getTextForMethodCall("super", "checkConstraints", (List)null)).append(";\n");
      String preparedName = UMLUtilities.getFullyQualifiedName((ModelElement)cc.getConstrainedClass()).replaceAll("::", "_");
      Classifier oldContext = this.currentContext;
      this.currentContext = cc.getConstrainedClass();

      for(int i = 0; i < invs.length; ++i) {
         String checkerMethodName = "check_" + preparedName + '_' + invs[i].getName();
         PGMethod cm = this.createCheckerMethod(constraintChecker, checkerMethodName, invs[i]);
         constraintChecker.add(cm);
         bfBody.append(CodeGenUtilities.getTextForMethodCall((String)null, checkerMethodName, (List)null)).append(";\n");
      }

      PGBlock body = new PGBlock(checkerMethod, false, false);
      body.setBody(bfBody);
      this.currentContext = oldContext;
   }

   private void createDefinitionConstraint(DefinitionConstraintNode dcn, PGClass cls) {
      PGMethod meth = new PGMethod(cls);
      meth.setName(dcn.getName());
      meth.addModifier("public");
      String retTypeName = OclCodeGenUtilities.getTypeName(dcn.getReturnType().type, cls.getImportManager());
      meth.setReturnTypeName(retTypeName);
      Variable[] params = dcn.getParameters();

      for(int i = 0; i < params.length; ++i) {
         String typeName = OclCodeGenUtilities.getTypeName(params[i].getType(), cls.getImportManager(), false);
         PGParameter pSpec = new PGParameter(params[i].getName(), typeName);
         meth.addParameter(pSpec);
      }

      NamingContext nc = new NamingContext();

      for(int i = 0; i < params.length; ++i) {
         nc.add(params[i].getName());
      }

      StringBuffer bodyCode = this.ocl2Java(dcn.getExpression(), cls, nc);
      Template tReturn = CodeGeneratorManager.getTemplate("OCL_return_statement");
      Context cReturn = CodeGeneratorManager.newContext();
      SyntaxCodeInfo sci = dcn.getExpression().getCodeInfo();
      cReturn.put("VAL", sci.getText());
      bodyCode.append(CodeGenUtilities.getText(tReturn, cReturn));
      PGBlock bodyElement = new PGBlock(meth, false, false);
      bodyElement.setBody(bodyCode);
   }

   private PGMethod updateForMethod(PGMethod m, PGClass cls) {
      Operation oper = (Operation)m.getUserObject();
      ConstrainedOperation cop = this.normalForm.getFor(oper);
      if (cop == null) {
         return null;
      } else {
         String innerClsName = cop.getCodeInfo().getInnerClassName();
         String retTypeName = null;
         NamingContext local = new NamingContext();
         PGClass constraintChecker = new PGClass(m);
         constraintChecker.addModifier("class");
         constraintChecker.setName(innerClsName);
         local.add(innerClsName);
         PGField resField = new PGField(constraintChecker);
         resField.setName("result");
         ArrayList params = new ArrayList();
         ArrayList paramNames = new ArrayList();
         Iterator itParams = oper.getCollectionParameterList().iterator();
         Parameter retTypeParam = null;

         while(itParams.hasNext()) {
            Parameter p = (Parameter)itParams.next();
            if (p.getKind() == 3) {
               retTypeParam = p;
            } else {
               params.add(p);
               paramNames.add(p.getName());
               local.add(p.getName());
            }
         }

         if (retTypeParam != null) {
            retTypeName = CodeGenUtilities.getTypeName(retTypeParam.getType(), cls.getImportManager());
         }

         if (retTypeName != null && !"void".equals(retTypeName)) {
            resField.setTypeName(retTypeName);
         } else {
            constraintChecker.remove(resField);
            resField = null;
         }

         PGMethod proxyImpl = (PGMethod)m.clone();
         proxyImpl.setName(cop.getCodeInfo().getImplementationMethodName());
         proxyImpl.removeModifier("public");
         proxyImpl.removeModifier("protected");
         proxyImpl.addModifier("private");
         PGMethod mCheckPre = new PGMethod(constraintChecker);
         PGMethod mCheckPost = new PGMethod(constraintChecker);
         mCheckPre.addModifier("public");
         mCheckPost.addModifier("public");
         mCheckPre.setName("checkPreconditions");
         mCheckPost.setName("checkPostconditions");

         for(int i = 0; i < params.size(); ++i) {
            Parameter p = (Parameter)params.get(i);
            String typeName = CodeGenUtilities.getTypeName(p.getType(), cls.getImportManager());
            PGParameter pSpec = new PGParameter(p.getName(), typeName);
            mCheckPre.addParameter(pSpec);
            mCheckPost.addParameter(pSpec);
         }

         StringBuffer bfPre = null;
         StringBuffer bfPost = null;
         AbstractConstraintNode[] constr = cop.getConstraints();
         Classifier oldContext = this.currentContext;
         this.currentContext = (Classifier)cls.getUserObject();

         StringBuffer tmp;
         for(int i = 0; i < constr.length; ++i) {
            PGMethod checkMeth = this.createCheckerMethod(constraintChecker, "check_" + constr[i].getName(), (BasicConstraintNode)constr[i]);
            tmp = CodeGenUtilities.getTextForMethodCall((String)null, checkMeth.getName(), paramNames);
            tmp.append(";\n");
            switch(constr[i].getKind()) {
            case 2:
               if (bfPost == null) {
                  bfPost = tmp;
               } else {
                  bfPost.append(tmp);
               }
               break;
            default:
               if (bfPre == null) {
                  bfPre = tmp;
               } else {
                  bfPre.append(tmp);
               }
            }
         }

         this.currentContext = oldContext;
         PGBlock bodyCodeBlock = new PGBlock(mCheckPre, false, false);
         bodyCodeBlock.setBody(bfPre);
         bodyCodeBlock = new PGBlock(mCheckPost, false, false);
         bodyCodeBlock.setBody(bfPost);
         String checkerVar = local.newName("checker");
         tmp = CodeGenUtilities.getTextForAssignment((String)null, innerClsName, checkerVar, "new " + innerClsName + "()");
         tmp.append(CodeGenUtilities.getTextForMethodCall(checkerVar, mCheckPre.getName(), paramNames)).append(";\n");
         StringBuffer tmp2 = CodeGenUtilities.getTextForMethodCall((String)null, proxyImpl.getName(), paramNames);
         String var = checkerVar + ".result";
         if (!"void".equals(retTypeName)) {
            tmp2 = CodeGenUtilities.getTextForAssignment((String)null, (String)null, var, tmp2.toString());
            tmp.append(tmp2).append('\n');
         } else {
            tmp.append(tmp2).append(";\n");
         }

         tmp.append(CodeGenUtilities.getTextForMethodCall(checkerVar, mCheckPost.getName(), paramNames)).append(";\n");
         if (!"void".equals(retTypeName)) {
            Context cRet = CodeGeneratorManager.newContext();
            cRet.put("VAL", var);
            Template tReturn = CodeGeneratorManager.getTemplate("OCL_return_statement");
            tmp.append(CodeGenUtilities.getText(tReturn, cRet));
         }

         bodyCodeBlock = new PGBlock(m, false, false);
         bodyCodeBlock.setBody(tmp);
         return proxyImpl;
      }
   }

   private PGMethod createCheckerMethod(PGClass parent, String methodName, BasicConstraintNode cons) {
      PGMethod checkerMethod = new PGMethod(parent);
      checkerMethod.setName(methodName);
      checkerMethod.addModifier("public");
      OperationConstraintGroup grp = cons.getGroup();
      if (grp != null) {
         Variable[] params = grp.getFormalParameters();

         for(int i = 0; i < params.length; ++i) {
            String typeName = OclCodeGenUtilities.getTypeName(params[i].getType(), parent.getImportManager(), false);
            PGParameter param = new PGParameter(params[i].getName(), typeName);
            checkerMethod.addParameter(param);
         }
      }

      NamingContext nc = new NamingContext();
      StringBuffer body = this.ocl2Java(cons.getExpression(), (PGElement)parent.getParent(), nc);
      Template tFinal = CodeGeneratorManager.getTemplate("OCL_finalizer_exception");
      Context cFinal = CodeGeneratorManager.newContext();
      cFinal.put("NAME", cons.getName());
      cFinal.put("KIND", cons.getStereotypeText());
      cFinal.put("TEST", cons.getExpression().getCodeInfo().getText());
      cFinal.put("SELF", this.currentContext.getName() + ".this");
      body.append(CodeGenUtilities.getText(tFinal, cFinal));
      PGBlock bodyBlock = new PGBlock(checkerMethod, false, false);
      bodyBlock.setBody(body);
      return checkerMethod;
   }

   private void updateForAllInstances(PGClass cls) {
      Classifier realCls = (Classifier)cls.getUserObject();
      if (realCls instanceof Class) {
         List A = InheritanceAndAbstractionUtility.getImplementedInterfaces((Class)realCls);
         List B = InheritanceAndAbstractionUtility.getDirectParents((GeneralizableElement)realCls);
         ArrayList C = new ArrayList();

         List temp;
         boolean aHasChanged;
         do {
            temp = InheritanceAndAbstractionUtility.getDirectParents((List)A);
            if (temp != null) {
               aHasChanged = A != null ? Utils.distinctAddAll((List)A, temp) : false;
            } else {
               aHasChanged = false;
            }

            temp = InheritanceAndAbstractionUtility.getImplementedInterfaces(B);
            if (temp != null) {
               Utils.distinctAddAll(C, temp);
            }

            B = InheritanceAndAbstractionUtility.getDirectParents(B);
         } while(aHasChanged);

         if (A == null) {
            A = new ArrayList();
         }

         if (C != null) {
            ((List)A).removeAll(C);
            temp = InheritanceAndAbstractionUtility.getAllParents((List)C);
            if (temp != null) {
               ((List)A).removeAll(temp);
            }
         }

         ((List)A).add(realCls);
         StringBuffer bfInitializerCode = null;
         String oclType_TypeName = null;
         ArrayList args = new ArrayList();
         args.add("this");
         Iterator it = ((List)A).iterator();

         while(it.hasNext()) {
            Classifier candidate = (Classifier)it.next();
            if (this.normalForm.isAllInstancesCandidate(candidate)) {
               if (oclType_TypeName == null) {
                  oclType_TypeName = OclCodeGenUtilities.getTypeName(CodeGenerationConstants.OCL_PATH + ".OclType", cls.getImportManager());
               }

               String candidateClassName = CodeGenUtilities.getTypeName(candidate, cls.getImportManager()) + ".class";
               if (args.size() < 2) {
                  args.add(candidateClassName);
               } else {
                  args.set(1, candidateClassName);
               }

               StringBuffer bfInstRegCall = CodeGenUtilities.getTextForMethodCall(oclType_TypeName, "registerInstance", args);
               if (bfInitializerCode == null) {
                  bfInitializerCode = new StringBuffer();
               }

               bfInitializerCode.append(bfInstRegCall).append(";\n");
            }
         }

         if (bfInitializerCode != null) {
            PGBlock initializer = new PGBlock(cls, true, false);
            initializer.setBody(bfInitializerCode);
         }

      }
   }

   private StringBuffer ocl2Java(SyntaxTreeNode root, PGElement constrained, NamingContext context) {
      Object uo = constrained.getUserObject();
      OclCodeGenUtilities.updateContext(context, uo);
      CodeVisitor cv = new CodeVisitor(constrained, context, this.normalForm);
      root.accept(cv);
      return cv.codeBuffer;
   }
}
