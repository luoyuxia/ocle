package ro.ubbcluj.lci.ocl;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Stack;
import java.util.Vector;
import ro.ubbcluj.lci.ocl.datatypes.OclBag;
import ro.ubbcluj.lci.ocl.datatypes.OclBoolean;
import ro.ubbcluj.lci.ocl.datatypes.OclCollection;
import ro.ubbcluj.lci.ocl.datatypes.OclEnumLiteral;
import ro.ubbcluj.lci.ocl.datatypes.OclInteger;
import ro.ubbcluj.lci.ocl.datatypes.OclOrderedSet;
import ro.ubbcluj.lci.ocl.datatypes.OclReal;
import ro.ubbcluj.lci.ocl.datatypes.OclSequence;
import ro.ubbcluj.lci.ocl.datatypes.OclSet;
import ro.ubbcluj.lci.ocl.datatypes.OclString;
import ro.ubbcluj.lci.ocl.datatypes.OclTuple;
import ro.ubbcluj.lci.ocl.datatypes.Undefined;
import ro.ubbcluj.lci.ocl.eval.OclAccumulator;
import ro.ubbcluj.lci.ocl.eval.OclConstant;
import ro.ubbcluj.lci.ocl.eval.OclExpression;
import ro.ubbcluj.lci.ocl.eval.OclLetCall;
import ro.ubbcluj.lci.ocl.eval.OclPropertyCall;
import ro.ubbcluj.lci.ocl.eval.OclTupleConstructor;
import ro.ubbcluj.lci.ocl.nodes.OclTEXT;
import ro.ubbcluj.lci.ocl.nodes.actualParameterList;
import ro.ubbcluj.lci.ocl.nodes.additiveExpression;
import ro.ubbcluj.lci.ocl.nodes.classifierContext;
import ro.ubbcluj.lci.ocl.nodes.collectionItem;
import ro.ubbcluj.lci.ocl.nodes.collectionKind;
import ro.ubbcluj.lci.ocl.nodes.collectionType;
import ro.ubbcluj.lci.ocl.nodes.constraint;
import ro.ubbcluj.lci.ocl.nodes.contextDeclaration;
import ro.ubbcluj.lci.ocl.nodes.declarator;
import ro.ubbcluj.lci.ocl.nodes.dottedPathName;
import ro.ubbcluj.lci.ocl.nodes.enumLiteral;
import ro.ubbcluj.lci.ocl.nodes.expression;
import ro.ubbcluj.lci.ocl.nodes.formalParameterList;
import ro.ubbcluj.lci.ocl.nodes.freeConstraint;
import ro.ubbcluj.lci.ocl.nodes.ifExpression;
import ro.ubbcluj.lci.ocl.nodes.letExpression;
import ro.ubbcluj.lci.ocl.nodes.literal;
import ro.ubbcluj.lci.ocl.nodes.literalCollection;
import ro.ubbcluj.lci.ocl.nodes.literalTuple;
import ro.ubbcluj.lci.ocl.nodes.logicalExpression;
import ro.ubbcluj.lci.ocl.nodes.multiplicativeExpression;
import ro.ubbcluj.lci.ocl.nodes.name;
import ro.ubbcluj.lci.ocl.nodes.number;
import ro.ubbcluj.lci.ocl.nodes.oclExpression;
import ro.ubbcluj.lci.ocl.nodes.oclFile;
import ro.ubbcluj.lci.ocl.nodes.oclPackage;
import ro.ubbcluj.lci.ocl.nodes.operationContext;
import ro.ubbcluj.lci.ocl.nodes.operationName;
import ro.ubbcluj.lci.ocl.nodes.option;
import ro.ubbcluj.lci.ocl.nodes.pathName;
import ro.ubbcluj.lci.ocl.nodes.postfixExpression;
import ro.ubbcluj.lci.ocl.nodes.primaryExpression;
import ro.ubbcluj.lci.ocl.nodes.propertyCall;
import ro.ubbcluj.lci.ocl.nodes.propertyCallParameters;
import ro.ubbcluj.lci.ocl.nodes.qualifiers;
import ro.ubbcluj.lci.ocl.nodes.relationalExpression;
import ro.ubbcluj.lci.ocl.nodes.returnType;
import ro.ubbcluj.lci.ocl.nodes.simpleTypeSpecifier;
import ro.ubbcluj.lci.ocl.nodes.stereotype;
import ro.ubbcluj.lci.ocl.nodes.string;
import ro.ubbcluj.lci.ocl.nodes.timeExpression;
import ro.ubbcluj.lci.ocl.nodes.tupleType;
import ro.ubbcluj.lci.ocl.nodes.typeSpecifier;
import ro.ubbcluj.lci.ocl.nodes.unaryExpression;
import ro.ubbcluj.lci.ocl.nodes.untilExpression;
import ro.ubbcluj.lci.uml.foundation.core.Attribute;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;

public class OclChecker extends OclVisitor {
   private OclUmlApi umlapi;
   private OclType currentContext;
   private String packagecontext;
   private char contextType;
   private String contextStereotype;
   private Stack vtSize = new Stack();
   private OclType contextOperationType;
   private boolean isPostcondition;
   private boolean isDef;
   private OclClassInfo self;
   private Object[] stateMachine;
   private int pass;
   OclNode lastStereotype;
   private boolean allowAnyConstraintType = false;
   private boolean autocompletion = false;
   private boolean isOperationBody;
   private static Hashtable ops2ids = new Hashtable();

   OclChecker(OclUmlApi p_umlapi, int pass) {
      OclUtil.oclChecker = this;
      this.umlapi = p_umlapi;
      this.pass = pass;
      if (pass == 1) {
         this.umlapi.dynamicBinding = new OclDynamicBinding(this.umlapi);
      }

   }

   void setAutocompletion(boolean autocompletion) {
      this.autocompletion = autocompletion;
   }

   private String translate(String op) throws ExceptionAny {
      String result = (String)ops2ids.get(op);
      if (result != null) {
         return result;
      } else {
         throw new ExceptionAny("[internal] unknown operator " + op);
      }
   }

   private void checkTerminal(OclNode nod) throws ExceptionChecker {
      if (nod.token_count() > 1) {
         throw new ExceptionChecker("[internal] terminal node with more tokens than one", nod);
      }
   }

   private void checkEmptyExpr(OclNode nod) {
      if (nod.children.size() == 1) {
         OclNode child = nod.firstChild();
         nod.type = child.type;
         nod.evnode = child.evnode;
      }

   }

   public void visitPost(OclTEXT nod) {
      String s = nod.getValueAsString();
      nod.stringValue = s;
   }

   public void visitPost(option nod) throws ExceptionChecker {
      String name = nod.getChild(1).stringValue;
      if (name.equals("noboolcheck")) {
         this.allowAnyConstraintType = true;
      } else {
         throw new ExceptionChecker("unknown option " + name + "(available options: noboolcheck)", nod);
      }
   }

   public boolean visitPre(oclFile nod) throws ExceptionChecker {
      Iterator it = nod.children.iterator();

      while(it.hasNext()) {
         OclNode child = (OclNode)it.next();
         if (child instanceof pathName) {
            String modelname = child.getValueAsString();
            if (!modelname.equals(this.umlapi.getModel().getName())) {
               throw new ExceptionChecker("the names of loaded and declared models are different", child);
            }
         }
      }

      OclClassInfo ci;
      if (this.pass == 1) {
         for(it = this.umlapi.classes.iterator(); it.hasNext(); ci.invNames = new HashMap()) {
            ci = (OclClassInfo)it.next();
            ci.defTable = null;
            ci.letTable = null;
         }
      }

      return true;
   }

   public boolean visitPre(freeConstraint nod) throws ExceptionChecker {
      this.contextType = 'C';
      this.self = this.umlapi.UNDEFINED;
      nod.type = new OclType(this.self);
      this.umlapi.varTable.clear();
      OclLetItem let;
      if (this.pass == 1) {
         nod.evnode = new OclConstant(nod, (Object)null);
         let = new OclLetItem("self", nod.type, 0, new OclConstant(nod, new Undefined()));
         nod.objectValue = let;
      } else {
         let = (OclLetItem)nod.objectValue;
      }

      this.umlapi.varTable.addVariable(let);
      this.currentContext = nod.type;
      return true;
   }

   public void visitPost(freeConstraint nod) throws ExceptionChecker {
      OclNode firstChild = nod.firstChild();
      OclNode lastChild = nod.lastChild();
      if (firstChild instanceof OclTEXT) {
         firstChild.type = lastChild.type;
         firstChild.evnode = lastChild.evnode;
      }

      nod.type = firstChild.type;
      nod.evnode = firstChild.evnode;
   }

   public boolean visitPre(oclPackage nod) throws ExceptionChecker {
      if (!(nod.firstChild() instanceof OclTEXT)) {
         this.packagecontext = "";
      } else {
         try {
            this.packagecontext = this.umlapi.checkPackageName(nod.getChild(1).getValueAsString());
         } catch (ExceptionAny var3) {
            throw new ExceptionChecker(var3.getMessage(), nod.getChild(1));
         }
      }

      return true;
   }

   public boolean visitPre(constraint nod) throws ExceptionChecker {
      this.isDef = true;
      this.lastStereotype = null;
      Iterator it = nod.children.iterator();

      while(it.hasNext()) {
         OclNode child = (OclNode)it.next();
         if (child instanceof stereotype) {
            this.isDef = false;
            this.lastStereotype = child;
         }

         if (child instanceof letExpression) {
            this.isDef = true;
            this.lastStereotype = null;
         }

         if (this.isDef || this.pass == 2) {
            try {
               child.acceptVisitor(this);
               nod.getChild(1).type = child.type;
               nod.getChild(1).evnode = child.evnode;
            } catch (ExceptionChecker var6) {
               if (!this.autocompletion) {
                  throw var6;
               }
            }
         }

         if (child instanceof name && !this.isDef && this.pass == 2) {
            String name = child.getValueAsString();
            if (this.self.invNames.get(name) != null) {
               String s = (String)this.self.invNames.get(name);
               if (s.length() > 0) {
                  s = " in " + s;
               }

               throw new ExceptionChecker("the name " + name + " already exists" + s, child);
            }

            this.self.invNames.put(name, nod.getFilename());
         }
      }

      this.visitPost(nod);
      return false;
   }

   public void visitPost(constraint nod) {
      OclNode lastStereotype = null;
      OclNode lastDef = null;
      Iterator it = nod.children.iterator();

      while(it.hasNext()) {
         OclNode child = (OclNode)it.next();
         if (child instanceof stereotype) {
            lastStereotype = child;
         }

         if (child instanceof oclExpression) {
            lastStereotype.type = child.type;
            lastStereotype.evnode = child.evnode;
         }

         if (child instanceof OclTEXT && child.getValueAsString().equals("def") && lastDef == null) {
            lastDef = child;
         }

         if (child instanceof letExpression) {
            lastDef.type = child.type;
            lastDef.evnode = child.evnode;
         }
      }

      this.isPostcondition = false;
      this.isOperationBody = false;
   }

   public boolean visitPre(contextDeclaration nod) {
      this.vtSize.clear();
      if (this.currentContext != null) {
      }

      return true;
   }

   public void visitPost(contextDeclaration nod) throws ExceptionChecker {
      this.currentContext = this.umlapi.varTable.getSelf().rettype;
   }

   public void visitPost(classifierContext nod) throws ExceptionChecker {
      this.contextType = 'C';

      try {
         String selfName = nod.firstChild() instanceof name ? nod.firstChild().stringValue : "self";
         String classifierName = nod.lastChild().stringValue;
         this.self = this.umlapi.getClassInfoByName(this.packagecontext, this.packagecontext + "::" + classifierName);
         if (this.self == null) {
            throw new ExceptionAny("classifier not found in this package");
         } else {
            if (!this.self.isCollection) {
               nod.type = new OclType(this.self);
            } else {
               nod.type = new OclType(this.self, this.umlapi.OCLANY);
            }

            this.umlapi.varTable.clear();
            OclLetItem let;
            if (this.pass == 1) {
               nod.evnode = new OclConstant(nod, (Object)null);
               let = new OclLetItem(selfName, nod.type, 0, nod.evnode);
               nod.objectValue = let;
            } else {
               let = (OclLetItem)nod.objectValue;
            }

            this.umlapi.varTable.addVariable(let);
         }
      } catch (ExceptionAny var5) {
         throw new ExceptionChecker(var5.getMessage(), nod);
      }
   }

   public boolean visitPre(operationContext nod) throws ExceptionChecker {
      this.contextType = 'O';
      nod.getChild(0).acceptVisitor(this);

      String classifierName;
      try {
         classifierName = nod.firstChild().stringValue;
         this.self = this.umlapi.getClassInfoByName(this.packagecontext, this.packagecontext + "::" + classifierName);
         if (this.self == null) {
            throw new ExceptionAny("classifier not found in this package");
         }
      } catch (ExceptionAny var5) {
         throw new ExceptionChecker(var5.getMessage(), nod);
      }

      nod.type = this.self.isCollection ? new OclType(this.self, this.umlapi.OCLANY) : new OclType(this.self);
      nod.firstChild().type = nod.type;
      this.umlapi.varTable.clear();
      if (this.pass == 1) {
         OclConstant cst = new OclConstant(nod.firstChild(), (Object)null);
         nod.objectValue = new OclLetItem("self", nod.type, 0, cst);
      }

      this.umlapi.varTable.addVariable((OclLetItem)nod.objectValue);
      nod.firstChild().evnode = ((OclLetItem)nod.objectValue).bodyexp;
      nod.getChild(2).acceptVisitor(this);
      nod.getChild(4).acceptVisitor(this);
      if (nod.getChildCount() > 6) {
         nod.getChild(7).acceptVisitor(this);
      }

      classifierName = null;

      try {
         Vector params = new Vector(nod.getChild(4).vectorValue);
         if (nod.getChildCount() > 6) {
            nod.getChild(7).type.isReturn = true;
            params.add(nod.getChild(7).type);
         }

         nod.searchResult = this.self.findFeature(nod.getChild(2).stringValue, params, 1, (String)null, (Vector)null, (OclClassInfo)null, false);
         OclType rettype = nod.searchResult.type;
         if (rettype == null) {
            throw new ExceptionAny("operation cannot be found in this classifier");
         }
      } catch (ExceptionAny var4) {
         throw new ExceptionChecker(var4.getMessage(), nod);
      }

      if (nod.getChildCount() > 6) {
         this.contextOperationType = nod.getChild(7).type;
      } else {
         this.contextOperationType = null;
      }

      return false;
   }

   public void visitPost(stereotype nod) throws ExceptionChecker {
      this.contextStereotype = nod.firstChild().stringValue;
      if (this.contextStereotype.equals("inv") && this.contextType == 'O') {
         throw new ExceptionChecker("invariants can't be used in operation contexts", nod);
      } else if (!this.contextStereotype.equals("inv") && this.contextType != 'O') {
         throw new ExceptionChecker("preconditions, postconditions and body declarations can be used in operation contexts only", nod);
      } else {
         this.isPostcondition = this.contextStereotype.equals("post");
         this.isOperationBody = this.contextStereotype.equals("body");
      }
   }

   public void visitPost(operationName nod) {
      String s = nod.getValueAsString();
      if (nod.firstChild() instanceof name) {
         nod.stringValue = s;
      } else {
         try {
            nod.stringValue = this.translate(s);
         } catch (ExceptionAny var4) {
            nod.stringValue = s;
         }
      }

   }

   public void visitPost(formalParameterList nod) throws ExceptionChecker {
      if (this.pass == 1 && !(nod.parent.parent instanceof expression) || this.pass == 2 && nod.parent.parent instanceof expression) {
         nod.vectorValue = new Vector();
         nod.vectorEval = new Vector();
         nod.vectorLet = new Vector();
         String lastname = null;
         OclNode lastnamenode = null;
         Iterator it = nod.children.iterator();

         while(it.hasNext()) {
            OclNode child = (OclNode)it.next();
            if (child instanceof name) {
               lastname = child.stringValue;
               lastnamenode = child;
            }

            if (child instanceof typeSpecifier) {
               nod.vectorValue.add(child.type);
               OclConstant cst = new OclConstant(lastnamenode, (Object)null);
               nod.vectorEval.add(cst);
               OclLetItem let = new OclLetItem(lastname, child.type, 4, cst);
               nod.vectorLet.add(let);
               lastnamenode.type = child.type;
               lastnamenode.evnode = cst;
            }
         }
      }

      Iterator it = nod.vectorLet.iterator();

      while(it.hasNext()) {
         OclLetItem let = (OclLetItem)it.next();
         this.umlapi.varTable.addVariable(let);
      }

   }

   public boolean visitPre(oclExpression nod) {
      this.umlapi.varTable.pushSize();
      return true;
   }

   public void visitPost(oclExpression nod) throws ExceptionChecker {
      this.umlapi.varTable.popSize();
      Iterator it = nod.children.iterator();

      while(it.hasNext()) {
         OclNode child = (OclNode)it.next();
         if (child instanceof expression) {
            nod.type = child.type;
            nod.evnode = child.evnode;
         }
      }

      if (this.isOperationBody) {
         if (!nod.type.conforms(this.contextOperationType)) {
            throw new ExceptionChecker("Body expression type should conform to the type of the result of the context operation", nod);
         }
      } else if (nod.type.type != this.umlapi.BOOLEAN && !this.allowAnyConstraintType) {
         throw new ExceptionChecker("OCL constraint expression should result in a boolean type", nod);
      }

      if (nod.type == null) {
         throw new ExceptionChecker("[internal] expression type undetermined", nod);
      }
   }

   public void visitPost(returnType nod) throws ExceptionChecker {
      nod.type = nod.firstChild().type;
      OclConstant cst = new OclConstant(nod, (Object)null);
      this.umlapi.varTable.addVariable(new OclLetItem("result", nod.type, 4, cst));
      nod.evnode = cst;
      if (nod.type == null) {
         throw new ExceptionChecker("[internal] operation return type unknown", nod);
      }
   }

   public boolean visitPre(letExpression nod) throws ExceptionChecker {
      OclLetItem letvar;
      if (nod.parent instanceof constraint && this.pass != 1) {
         letvar = (OclLetItem)nod.objectValue;
      } else {
         letvar = new OclLetItem();
         letvar.stereotype = this.lastStereotype;
         nod.objectValue = letvar;
         letvar.paramvars = new OclConstant[0];
      }

      letvar.bodyexp = new OclConstant("this method is abstract and cannot be evaluated", nod);
      letvar.selfvar = (OclConstant)this.umlapi.varTable.getSelf().bodyexp;
      OclExpression savedSelfBodyExp = this.umlapi.varTable.getSelf().bodyexp;
      this.umlapi.varTable.getSelf().bodyexp = letvar.selfvar;
      boolean varAdded = false;
      this.umlapi.varTable.pushSize();
      Iterator it = nod.children.iterator();

      while(true) {
         do {
            do {
               do {
                  if (!it.hasNext()) {
                     this.umlapi.varTable.popSize();
                     this.umlapi.varTable.getSelf().bodyexp = savedSelfBodyExp;
                     if (nod.parent instanceof expression) {
                        this.umlapi.varTable.addVariable(letvar);
                     }

                     nod.type = letvar.rettype;
                     nod.evnode = letvar.bodyexp;
                     return false;
                  }

                  OclNode child = (OclNode)it.next();
                  if (child instanceof operationName) {
                     child.acceptVisitor(this);
                     letvar.name = child.stringValue;
                  }

                  if (child instanceof formalParameterList) {
                     child.acceptVisitor(this);
                     if (this.pass == 1 || !(nod.parent instanceof constraint)) {
                        letvar.params = child.vectorValue;
                        letvar.paramvars = new OclConstant[letvar.params.size()];

                        for(int i = 0; i < letvar.paramvars.length; ++i) {
                           letvar.paramvars[i] = (OclConstant)child.vectorEval.get(i);
                        }
                     }
                  }

                  if (child instanceof typeSpecifier) {
                     child.acceptVisitor(this);
                     letvar.rettype = child.type;
                  }

                  if (child instanceof expression && (this.pass == 2 || letvar.rettype == null)) {
                     child.acceptVisitor(this);
                     if (letvar.rettype == null) {
                        letvar.rettype = child.type;
                     }

                     if (!child.type.conforms(letvar.rettype)) {
                        throw new ExceptionChecker("body expression type doesn't conform to the declared return type", nod);
                     }

                     letvar.bodyexp = child.evnode;
                  }
               } while(letvar.rettype == null);
            } while(varAdded);

            varAdded = true;
         } while(this.pass != 1 && nod.parent instanceof constraint);

         if (nod.parent instanceof expression) {
            this.umlapi.varTable.addVariable(letvar);
         } else {
            String msg = this.self.addLetItem(letvar, nod.parent instanceof constraint);
            if (msg != null) {
               throw new ExceptionChecker(msg, nod);
            }
         }
      }
   }

   public void visitPost(typeSpecifier nod) throws ExceptionChecker {
      nod.type = nod.firstChild().type;
      if (nod.type == null) {
         throw new ExceptionChecker("[internal] unknown type", nod);
      }
   }

   public void visitPost(collectionType nod) throws ExceptionChecker {
      nod.type = new OclType(nod.getChild(0).type.type, nod.getChild(2).type, false);
      if (nod.type.type != null && nod.type.element != null) {
         if (nod.parent instanceof primaryExpression) {
            nod.type = new OclType(this.umlapi.OCLTYPE, nod.type, false);
            nod.evnode = new OclConstant(nod, nod.type);
         }

      } else {
         throw new ExceptionChecker("[internal] collection type unresolved", nod);
      }
   }

   public void visitPost(tupleType nod) throws ExceptionChecker {
      int partCount = nod.children.size() / 4;
      String[] partNames = new String[partCount];
      OclType[] partTypes = new OclType[partCount];
      int k = 0;
      HashSet uniqueNames = new HashSet();
      Iterator it = nod.children.iterator();

      while(it.hasNext()) {
         OclNode child = (OclNode)it.next();
         if (child instanceof name) {
            partNames[k] = child.stringValue;
            if (uniqueNames.contains(child.stringValue)) {
               throw new ExceptionChecker("the name " + child.stringValue + " is already used in this tuple declaration", child);
            }

            uniqueNames.add(child.stringValue);
         }

         if (child instanceof typeSpecifier) {
            partTypes[k] = child.type;
            ++k;
         }
      }

      nod.type = new OclType(partCount, partNames, partTypes);
      if (nod.parent instanceof primaryExpression) {
         nod.type = new OclType(this.umlapi.OCLTYPE, nod.type, false);
         nod.evnode = new OclConstant(nod, nod.type);
      }

   }

   public void visitPost(literalTuple nod) throws ExceptionChecker {
      int maxCount = (nod.children.size() - 2) / 4;
      String[] partNames = new String[maxCount];
      OclType[] partTypes = new OclType[maxCount];
      OclExpression[] partValues = new OclExpression[maxCount];
      int k = 0;
      HashSet uniqueNames = new HashSet();
      OclType lastTypeSpecifier = null;
      Iterator it = nod.children.iterator();

      while(it.hasNext()) {
         OclNode child = (OclNode)it.next();
         if (child instanceof name) {
            partNames[k] = child.stringValue;
            if (uniqueNames.contains(child.stringValue)) {
               throw new ExceptionChecker("the name " + child.stringValue + " is already used in this tuple literal", child);
            }

            uniqueNames.add(child.stringValue);
            lastTypeSpecifier = null;
         }

         if (child instanceof typeSpecifier) {
            partTypes[k] = child.type;
            lastTypeSpecifier = child.type;
         }

         if (child instanceof expression) {
            if (lastTypeSpecifier == null) {
               partTypes[k] = child.type;
            } else {
               partTypes[k] = lastTypeSpecifier;
               if (!child.type.conforms(lastTypeSpecifier)) {
                  throw new ExceptionChecker("the type of the expression does not conform to the declared type", nod);
               }
            }

            partValues[k] = child.evnode;
            ++k;
         }
      }

      nod.type = new OclType(k, partNames, partTypes);
      nod.evnode = new OclTupleConstructor(k, partNames, partValues, nod);
   }

   public boolean visitPre(expression nod) {
      this.umlapi.varTable.pushSize();
      return true;
   }

   public void visitPost(expression nod) {
      this.umlapi.varTable.popSize();
      nod.type = nod.lastChild().type;
      nod.evnode = nod.lastChild().evnode;
   }

   public void visitPost(ifExpression nod) throws ExceptionChecker {
      OclNode nif = nod.children.get(1);
      OclNode nthen = nod.children.get(3);
      OclNode nelse = nod.children.get(5);
      if (nif.type.type != this.umlapi.UNDEFINED && (nthen.type.type != this.umlapi.UNDEFINED || nelse.type.type != this.umlapi.UNDEFINED)) {
         if (nthen.type.type == this.umlapi.UNDEFINED) {
            nod.type = nelse.type;
         } else if (nelse.type.type == this.umlapi.UNDEFINED) {
            nod.type = nthen.type;
         } else {
            if (!nif.type.type.conforms(this.umlapi.BOOLEAN)) {
               throw new ExceptionChecker("if condition is not boolean", nif);
            }

            OclType parentType = OclType.commonType(nthen.type, nelse.type);
            nod.type = parentType;
         }
      } else {
         nod.type = new OclType(this.umlapi.UNDEFINED);
      }

      nod.type = new OclType(nod.type, 0);

      try {
         Class[] ifparams = new Class[]{Object.class, Object.class};
         OclExpression[] expparams = new OclExpression[]{nthen.evnode, nelse.evnode};
         nod.evnode = new OclPropertyCall(nod, nif.evnode, OclBoolean.class.getMethod("iif", ifparams), expparams);
      } catch (NoSuchMethodException var7) {
         throw new ExceptionChecker("[internal] if method of OclBoolean cannot be localized", nod);
      }
   }

   private void visitAnyBinaryExpression(OclNode nod) throws ExceptionChecker {
      this.checkEmptyExpr(nod);
      if (nod.children.size() > 1) {
         nod.vectorValue = new Vector();
         Iterator it = nod.children.iterator();
         OclNode child = (OclNode)it.next();
         OclNode lastop = null;
         OclType left = child.type;
         nod.vectorValue.add(left);
         OclExpression leftexp = child.evnode;
         if (left == null) {
            throw new ExceptionChecker("left subexpression type unknown", nod);
         }

         String oper = "";

         for(boolean isoperator = true; it.hasNext(); isoperator = !isoperator) {
            child = (OclNode)it.next();
            if (isoperator) {
               oper = child.firstToken().getValue();
               lastop = child;
            } else {
               if (child.type == null) {
                  throw new ExceptionChecker("right subexpression type unknown", nod);
               }

               Vector v = new Vector();
               v.add(child.type);

               try {
                  String toper = this.translate(oper);
                  OclType oldleft = left;
                  SearchResult searchResult = left.findFeature(this.packagecontext, "~" + toper, v, 2);
                  left = searchResult.type;
                  if (left == null) {
                     throw new ExceptionAny("not applicable here");
                  }

                  nod.vectorValue.add(left);
                  OclExpression[] params = new OclExpression[]{child.evnode};
                  if (searchResult.foundType == 8) {
                     OclLetItem letitem = searchResult.foundLetItem;
                     leftexp = new OclLetCall(oldleft.type, (OclExpression)leftexp, nod, params, letitem.uniqueSignatureIndex, false, (OclNode)null);
                  } else {
                     Method meth = searchResult.foundOwner.getJavaMethod();
                     leftexp = new OclPropertyCall(nod, (OclExpression)leftexp, meth, params);
                  }
               } catch (ExceptionAny var15) {
                  throw new ExceptionChecker("operator '" + oper + "' cannot be applied to " + left.getFullName() + " and " + child.type.getFullName() + " (" + var15.getMessage() + ")", lastop);
               }
            }
         }

         nod.type = left;
         nod.evnode = (OclExpression)leftexp;
      }

   }

   public void visitPost(logicalExpression nod) throws ExceptionChecker {
      this.visitAnyBinaryExpression(nod);
   }

   public void visitPost(relationalExpression nod) throws ExceptionChecker {
      this.visitAnyBinaryExpression(nod);
   }

   public void visitPost(additiveExpression nod) throws ExceptionChecker {
      this.visitAnyBinaryExpression(nod);
   }

   public void visitPost(multiplicativeExpression nod) throws ExceptionChecker {
      this.visitAnyBinaryExpression(nod);
   }

   public void visitPost(unaryExpression nod) throws ExceptionChecker {
      this.checkEmptyExpr(nod);
      if (nod.children.size() > 1) {
         nod.type = nod.getChild(nod.children.size() - 1).type;
         nod.evnode = nod.getChild(nod.children.size() - 1).evnode;

         for(int i = nod.children.size() - 2; i >= 0; --i) {
            String oper = nod.getChild(i).getValueAsString();
            if (!oper.equals("+")) {
               try {
                  if (oper.equals("-")) {
                     oper = "negate";
                  } else {
                     oper = this.translate(oper);
                  }

                  SearchResult searchResult = nod.type.findFeature(this.packagecontext, "~" + oper, new Vector(), 2);
                  nod.type = searchResult.type;
                  if (nod.type == null) {
                     throw new ExceptionAny("operator " + oper + " not applicable here");
                  }

                  Method meth = searchResult.foundOwner.getJavaMethod();
                  OclExpression[] params = new OclExpression[0];
                  nod.evnode = new OclPropertyCall(nod, nod.evnode, meth, params);
               } catch (ExceptionAny var8) {
                  throw new ExceptionChecker("operator '" + oper + "' cannot be applied to " + nod.type.getFullName(), nod);
               }
            }
         }
      }

   }

   public boolean visitPre(postfixExpression nod) throws ExceptionChecker {
      if (nod.children.size() <= 1) {
         return true;
      } else {
         OclType currentContext_old = this.currentContext;
         OclNode lastPCall = null;
         Iterator it = nod.children.iterator();
         primaryExpression exp = (primaryExpression)it.next();
         exp.acceptVisitor(this);
         this.currentContext = exp.type;

         propertyCall pcall;
         for(nod.evnode = exp.evnode; it.hasNext(); nod.evnode = pcall.evnode) {
            OclTEXT ocltext = (OclTEXT)it.next();
            ocltext.acceptVisitor(this);
            nod.stringValue = ocltext.stringValue;
            pcall = (propertyCall)it.next();
            this.umlapi.varTable.pushSize();
            pcall.acceptVisitor(this);
            this.umlapi.varTable.popSize();
            this.currentContext = pcall.type;
            lastPCall = pcall;
         }

         nod.type = this.currentContext;
         nod.evnode = lastPCall.evnode;
         this.currentContext = currentContext_old;
         return false;
      }
   }

   public void visitPost(postfixExpression nod) {
      this.checkEmptyExpr(nod);
   }

   public void visitPost(primaryExpression nod) {
      this.checkEmptyExpr(nod);
      if (nod.firstToken().getValue().equals("(")) {
         OclNode child = nod.children.get(1);
         nod.type = child.type;
         nod.evnode = child.evnode;
      }

   }

   public boolean visitPre(propertyCallParameters nod) {
      nod.searchResult = new SearchResult();
      nod.vectorEval = new Vector();
      OclLetItem oclvar;
      if (nod.parent.isCollectionOperation && !(nod.getChild(1) instanceof declarator)) {
         OclConstant cst = new OclConstant(nod, (Object)null);
         nod.vectorEval.add(new OclConstant(nod, cst));
         oclvar = new OclLetItem((String)null, this.currentContext.celement, 5, cst);
         this.umlapi.varTable.addVariable(oclvar);
         nod.searchResult.declarator1 = oclvar;
      }

      if (nod.parent.hasIterator) {
         OclExpression ev = nod.parent.parent.evnode;
         oclvar = new OclLetItem((String)null, this.currentContext, 5, ev);
         this.umlapi.varTable.addVariable(oclvar);
      }

      return true;
   }

   public void visitPost(propertyCallParameters nod) {
      nod.vectorValue = new Vector();
      Iterator it = nod.children.iterator();

      while(it.hasNext()) {
         OclNode child = (OclNode)it.next();
         if (child instanceof declarator) {
            nod.vectorValue.addAll(child.vectorValue);
            nod.vectorEval.addAll(child.vectorEval);
         }

         if (child instanceof actualParameterList) {
            nod.vectorValue.addAll(child.vectorValue);
            nod.vectorEval.addAll(child.vectorEval);
         }
      }

   }

   public void visitPost(literal nod) {
      nod.type = nod.firstChild().type;
      nod.evnode = nod.firstChild().evnode;
   }

   public void visitPost(enumLiteral nod) throws ExceptionChecker {
      String enumclass = "";
      String enumvalue = null;
      Iterator it = nod.children.iterator();

      while(it.hasNext()) {
         OclNode child = (OclNode)it.next();
         if (child instanceof pathName) {
            enumclass = child.stringValue;
         }

         if (child instanceof name) {
            enumvalue = child.stringValue;
         }
      }

      try {
         OclEnumLiteral lit = this.umlapi.checkEnumerationValue(enumclass, enumvalue);
         nod.type = new OclType(lit.getEnumeration());
         if (lit.getLiteral() != null) {
            nod.evnode = new OclConstant(nod, lit.getLiteral());
         } else {
            nod.evnode = new OclConstant(nod, lit);
         }

      } catch (ExceptionAny var6) {
         throw new ExceptionChecker(var6.getMessage(), nod);
      }
   }

   public void visitPost(simpleTypeSpecifier nod) throws ExceptionChecker {
      try {
         nod.type = new OclType(this.umlapi.getClassInfoByName(this.packagecontext, nod.firstChild().stringValue));
         if (nod.type.type == null) {
            throw new ExceptionAny("the type " + nod.firstChild().stringValue + " is unknown");
         }
      } catch (ExceptionAny var3) {
         throw new ExceptionChecker(var3.getMessage(), nod);
      }
   }

   public void visitPost(literalCollection nod) throws ExceptionChecker {
      Iterator it = nod.children.iterator();
      OclClassInfo collectionType = null;
      OclType elementType = new OclType(this.umlapi.UNDEFINED);
      OclExpression lastexp = null;
      Object lastcol = null;

      while(it.hasNext()) {
         OclNode child = (OclNode)it.next();
         if (child instanceof collectionKind) {
            collectionType = child.type.type;
            if (collectionType == this.umlapi.SET) {
               lastcol = new OclSet();
            }

            if (collectionType == this.umlapi.BAG) {
               lastcol = new OclBag();
            }

            if (collectionType == this.umlapi.SEQUENCE) {
               lastcol = new OclSequence();
            }

            if (collectionType == this.umlapi.ORDEREDSET) {
               lastcol = new OclOrderedSet();
            }

            if (collectionType == this.umlapi.COLLECTION) {
               throw new ExceptionChecker("Collection type is abstract and can't be instantiated", nod);
            }

            if (lastcol == null) {
               throw new ExceptionChecker("[internal] collection type undetermined", nod);
            }

            lastexp = new OclConstant(nod, lastcol);
         }

         if (child instanceof collectionItem) {
            OclType itemType = child.type;
            OclType oldElementType = elementType;
            if (elementType.type == this.umlapi.UNDEFINED) {
               elementType = itemType;
            } else {
               elementType = OclType.commonType(elementType, itemType);
            }

            if (elementType == null) {
               throw new ExceptionChecker("the types " + oldElementType.getFullName() + " and " + itemType.getFullName() + " don't have a common type so they can't be both members of the same collection", nod);
            }

            OclExpression[] params = new OclExpression[]{child.evnode};
            Method addmeth = child.objectValue == null ? OclCollection.getAddNestedMethod() : OclCollection.getAddMethod();
            lastexp = new OclPropertyCall(nod, (OclExpression)lastexp, addmeth, params);
         }
      }

      nod.type = new OclType(collectionType, elementType, false);
      nod.evnode = (OclExpression)lastexp;
   }

   public void visitPost(collectionItem nod) throws ExceptionChecker {
      OclType first = ((expression)nod.children.get(0)).type;
      if (nod.children.size() == 1) {
         nod.type = first;
         nod.evnode = nod.firstChild().evnode;
         nod.objectValue = null;
      } else {
         if (first.type != this.umlapi.INTEGER || ((expression)nod.children.get(2)).type.type != this.umlapi.INTEGER) {
            throw new ExceptionChecker("non-integer type in collection interval item", nod);
         }

         nod.type = new OclType(this.umlapi.INTEGER);
         OclSequence seq = new OclSequence();
         OclExpression[] params = new OclExpression[]{nod.getChild(0).evnode, nod.getChild(2).evnode};
         nod.evnode = new OclPropertyCall(nod, new OclConstant(nod, seq), seq.getAddIntMethod(), params);
         nod.objectValue = "";
      }

   }

   public boolean visitPre(propertyCall nod) throws ExceptionChecker {
      this.umlapi.varTable.pushSize();
      String s = nod.firstChild().getValueAsString();
      nod.isCollectionOperation = false;
      nod.hasIterator = false;
      boolean isopit = OclUtil.loopOperations.get(s) != null;
      if (this.currentContext.is01 && nod.parent instanceof postfixExpression && nod.parent.stringValue.equals("->")) {
         this.currentContext = new OclType(this.umlapi.SET, this.currentContext, false);
         nod.parent.type = this.currentContext;
      }

      if (isopit) {
         nod.isCollectionOperation = true;
         if (!this.currentContext.type.isCollection) {
            throw new ExceptionChecker("collection feature called for a simple type", nod);
         }
      }

      if (s.equals("dump") || s.equals("dumpi")) {
         nod.hasIterator = true;
      }

      return true;
   }

   public void visitPost(propertyCall nod) throws ExceptionChecker {
      String separator = null;
      if (nod.parent instanceof postfixExpression) {
         separator = nod.parent.stringValue;
      }

      if (separator != null && separator.length() >= 1 && separator.charAt(0) == '^' && !this.isPostcondition) {
         throw new ExceptionChecker("Message operator " + separator + " is allowed only in postconditions", nod.parent);
      } else {
         String propertyname;
         Vector param;
         if ((separator == null || separator.equals("")) && nod.firstChild().stringValue != null) {
            propertyname = nod.firstChild().stringValue;
            if (nod.getChildCount() == 1) {
               if (propertyname.equals("true")) {
                  nod.type = new OclType(this.umlapi.BOOLEAN);
                  nod.evnode = new OclConstant(nod, new OclBoolean(true));
                  return;
               }

               if (propertyname.equals("false")) {
                  nod.type = new OclType(this.umlapi.BOOLEAN);
                  nod.evnode = new OclConstant(nod, new OclBoolean(false));
                  return;
               }

               if (propertyname.equals("unlimited")) {
                  nod.type = new OclType(this.umlapi.INTEGER);
                  nod.evnode = new OclConstant(nod, OclInteger.MAX_VALUE);
                  return;
               }

               if (propertyname.equals("undefined")) {
                  nod.type = new OclType(this.umlapi.UNDEFINED);
                  nod.evnode = new OclConstant(nod, new Undefined());
                  return;
               }
            }

            if (nod.getChildCount() == 2 && nod.getChild(1) instanceof propertyCallParameters) {
               param = nod.getChild(1).vectorEval;
               if (param.size() == 1 && param.firstElement() instanceof OclConstant) {
                  Object obj = ((OclConstant)param.firstElement()).getValue();
                  if (obj instanceof OclType) {
                     OclType param1 = ((OclType)((OclConstant)param.firstElement()).getValue()).celement;
                     if (propertyname.equals("oclUndefined")) {
                        nod.type = param1;
                        nod.evnode = new OclConstant(nod, new Undefined(param1.getFullName()));
                        return;
                     }

                     if (propertyname.equals("oclEmpty")) {
                        if (!param1.type.isCollection) {
                           throw new ExceptionChecker("oclEmpty expects a collection type as parameter", nod);
                        }

                        nod.type = param1;
                        if (param1.type == this.umlapi.SET) {
                           nod.evnode = new OclConstant(nod, new OclSet());
                        } else if (param1.type == this.umlapi.BAG) {
                           nod.evnode = new OclConstant(nod, new OclBag());
                        } else if (param1.type == this.umlapi.SEQUENCE) {
                           nod.evnode = new OclConstant(nod, new OclSequence());
                        } else {
                           if (param1.type != this.umlapi.ORDEREDSET) {
                              throw new ExceptionChecker("unknown collection type passed to oclEmpty", nod);
                           }

                           nod.evnode = new OclConstant(nod, new OclOrderedSet());
                        }
                        return;
                     }
                  }
               }
            }
         }

         propertyname = null;
         param = null;
         Vector vactualexp = null;
         String qualifier = null;
         Vector qualifiers = null;
         Iterator it = nod.children.iterator();

         while(it.hasNext()) {
            Object obj = it.next();
            if (obj instanceof pathName) {
               propertyname = ((pathName)obj).stringValue;
            }

            if (obj instanceof operationName) {
               propertyname = ((operationName)obj).stringValue;
            }

            if (obj instanceof qualifiers) {
               qualifier = ((qualifiers)obj).stringValue;
               qualifiers = ((qualifiers)obj).vectorValue;
            }

            if (obj instanceof propertyCallParameters) {
               param = ((propertyCallParameters)obj).vectorValue;
               vactualexp = ((propertyCallParameters)obj).vectorEval;
            }
         }

         String statename;
         if (propertyname.indexOf("::") >= 0 && qualifier == null && param == null && separator == null) {
            try {
               String enumclass = propertyname.substring(0, propertyname.lastIndexOf("::"));
               statename = propertyname.substring(propertyname.lastIndexOf("::") + 2);
               OclEnumLiteral lit = this.umlapi.checkEnumerationValue(enumclass, statename);
               nod.type = new OclType(lit.getEnumeration());
               if (lit.getLiteral() != null) {
                  nod.evnode = new OclConstant(nod, lit.getLiteral());
               } else {
                  nod.evnode = new OclConstant(nod, lit);
               }

               nod.searchResult = new SearchResult(nod.type, 512);
               return;
            } catch (ExceptionAny var27) {
            }
         }

         if (qualifier == null && param == null) {
            OclClassInfo ci = this.currentContext.type.isCollection ? this.currentContext.element : this.currentContext.type;

            try {
               statename = OclClassInfo.checkState(propertyname, ci.states);
               if (statename == null && this.contextType == 'T') {
                  statename = this.umlapi.findState(this.stateMachine, propertyname);
               }

               if (statename != null) {
                  nod.type = new OclType(this.umlapi.OCLSTATE);
                  nod.evnode = new OclConstant(nod, new OclString(statename));
                  return;
               }
            } catch (ExceptionAny var25) {
               throw new ExceptionChecker(var25.getMessage(), nod);
            }
         }

         try {
            try {
               propertyname = this.translate(propertyname);
            } catch (ExceptionAny var24) {
            }

            SearchResult searchResult = this.currentContext.findFeature(this.packagecontext, (separator == null ? "" : separator) + propertyname, param, 2, qualifier, qualifiers);
            nod.type = searchResult.type;
            nod.searchResult = searchResult;
            if (nod.type == null) {
               throw new ExceptionChecker("feature " + propertyname + " not found", nod);
            }

            OclExpression[] actualexp;
            if (vactualexp == null) {
               actualexp = new OclExpression[0];
            } else {
               actualexp = new OclExpression[vactualexp.size()];

               for(int i = 0; i < actualexp.length; ++i) {
                  actualexp[i] = (OclExpression)vactualexp.get(i);
               }
            }

            OclConstant itvar = new OclConstant(nod, (Object)null);
            Object evParent;
            if ((searchResult.foundType & 128) == 0) {
               evParent = nod.parent.evnode;
            } else {
               evParent = searchResult.foundImplicitOwner.bodyexp;
            }

            if ((searchResult.foundType & 64) != 0) {
               evParent = itvar;
            }

            OclExpression nodevnode = null;
            switch(searchResult.foundType & 63) {
            case 0:
               if (searchResult.foundLetItem.vartype == 2) {
                  nodevnode = new OclLetCall(searchResult.foundOwner, this.umlapi.varTable.getSelf().bodyexp, nod, actualexp, searchResult.foundLetItem);
               } else {
                  nodevnode = searchResult.foundLetItem.bodyexp;
               }
               break;
            case 2:
               Classifier csf = OclClassInfo.FindInfo.owner;
               int classType = this.umlapi.getClassInfoByClassifier(csf).classType;
               if (classType == 3) {
                  if ((searchResult.foundType & 256) != 0) {
                     Attribute at = OclClassInfo.FindInfo.theAttribute;
                     String s = at.getInitialValue() == null ? null : at.getInitialValue().getBody();
                     OclClassInfo type = this.umlapi.getClassInfoByClassifier(at.getType());

                     try {
                        Object obj;
                        if (type == this.umlapi.BOOLEAN) {
                           obj = new OclBoolean("true".equals(s));
                        } else if (type == this.umlapi.INTEGER) {
                           obj = new OclInteger(s == null ? "0" : s);
                        } else if (type == this.umlapi.REAL) {
                           obj = new OclReal(s == null ? "0" : s);
                        } else {
                           obj = new OclString(s == null ? "" : s);
                        }

                        nodevnode = new OclConstant(nod, obj);
                     } catch (Exception var23) {
                        throw new ExceptionChecker("Invalid initial value for this static attribute: " + var23.getClass().getName() + ": " + var23.getMessage(), nod);
                     }
                  } else {
                     nodevnode = searchResult.foundOwner.buildOclObjectDiagram(nod, (OclExpression)evParent, qualifier);
                  }
               } else {
                  Method method = searchResult.foundOwner.getJavaMethod();
                  nodevnode = new OclPropertyCall(nod, (OclExpression)evParent, method, actualexp);
               }
               break;
            case 4:
               nodevnode = new OclConstant(nod, nod.type);
               break;
            case 8:
               OclLetItem letitem = searchResult.foundLetItem;
               nodevnode = new OclLetCall(searchResult.foundOwner, (OclExpression)evParent, nod, actualexp, letitem.uniqueSignatureIndex, OclClassInfo.foundHasResolution, this.lastStereotype);
               break;
            case 16:
               OclExpression[] params = new OclExpression[]{new OclConstant(nod, propertyname)};
               nodevnode = new OclPropertyCall(nod, (OclExpression)evParent, OclTuple.getGetPartMethod(), params);
            }

            if ((searchResult.foundType & 64) != 0) {
               OclExpression[] params = new OclExpression[]{new OclConstant(nod, itvar), new OclConstant(nod, nodevnode)};
               nod.evnode = new OclPropertyCall(nod, nod.parent.evnode, OclCollection.getCollectMethod(), params);
            } else {
               nod.evnode = (OclExpression)nodevnode;
            }

            if (propertyname.equals("sortedBy") && searchResult.foundOwner.isCollection && (searchResult.foundType & 63) == 2) {
               OclType elemType = (OclType)param.lastElement();
               Vector params = new Vector();
               params.add(elemType);

               try {
                  SearchResult sr = elemType.findFeature("", ".lt", params, 2);
                  if (sr.type.type != OclUtil.umlapi.BOOLEAN) {
                     throw new ExceptionAny("the < (less than) operation returns non-boolean type");
                  }
               } catch (ExceptionAny var22) {
                  throw new ExceptionChecker("the type of the expression in sortedBy must implement < (less than) (" + var22.getMessage() + ")", nod);
               }
            }
         } catch (ExceptionAny var26) {
            throw new ExceptionChecker(var26.getMessage(), nod);
         }

         separator = null;
         this.umlapi.varTable.popSize();
         OclNode pcParam = null;

         for(int i = 0; i < nod.getChildCount(); ++i) {
            OclNode temp = nod.getChild(i);
            if (temp instanceof propertyCallParameters) {
               pcParam = temp;
            }
         }

         if (pcParam != null) {
            nod.searchResult.declarator1 = pcParam.searchResult.declarator1;
            nod.searchResult.declarator2 = pcParam.searchResult.declarator2;
            nod.searchResult.accumulator = pcParam.searchResult.accumulator;
            pcParam.searchResult = null;
         }

      }
   }

   public boolean visitPre(qualifiers nod) throws ExceptionChecker {
      OclNode q = nod.getChild(1);
      ExceptionChecker exc = null;

      try {
         q.acceptVisitor(this);
      } catch (ExceptionChecker var6) {
         exc = var6;
      }

      nod.vectorValue = q.vectorValue;
      nod.vectorEval = q.vectorEval;
      nod.stringValue = nod.getChild(1).getValueAsString();
      if (exc != null) {
         boolean ind = Character.isJavaIdentifierStart(nod.stringValue.charAt(0));

         for(int i = 1; i < nod.stringValue.length(); ++i) {
            if (!Character.isJavaIdentifierPart(nod.stringValue.charAt(i))) {
               ind = false;
               break;
            }
         }

         if (!ind) {
            throw exc;
         }
      }

      return false;
   }

   public void visitPost(declarator nod) throws ExceptionChecker {
      Vector itnames = new Vector();
      boolean implicitType = true;
      boolean iteratorsEnded = false;
      nod.vectorEval = new Vector();
      OclExpression accumInitExp = null;
      if (!this.currentContext.type.isCollection) {
         throw new ExceptionChecker("iterator declarations not allowed in simple type operations", nod);
      } else {
         OclType ittype = this.currentContext.celement;
         String acname = null;
         OclType actype = null;
         Iterator it = nod.children.iterator();

         while(true) {
            OclNode child;
            do {
               do {
                  if (!it.hasNext()) {
                     return;
                  }

                  child = (OclNode)it.next();
                  if (child instanceof name) {
                     if (iteratorsEnded) {
                        acname = child.stringValue;
                        if (itnames.size() == 0) {
                           throw new ExceptionChecker("[fatal] no iterators in the declaration section", nod);
                        }
                     } else {
                        itnames.add(child);
                     }
                  }

                  if (child instanceof typeSpecifier && !iteratorsEnded) {
                     ittype = child.type;
                     implicitType = false;
                     if (!this.currentContext.celement.conforms(ittype)) {
                        throw new ExceptionChecker("iterator variable must be of the same type or a supertype of the collection element", nod);
                     }
                  }

                  if (child instanceof OclTEXT && (child.stringValue.equals(";") || child.stringValue.equals("|"))) {
                     iteratorsEnded = true;
                  }

                  if (child instanceof typeSpecifier && iteratorsEnded) {
                     actype = child.type;
                  }

                  if (child instanceof expression) {
                     if (actype == null) {
                        actype = child.type;
                     }

                     if (!child.type.conforms(actype)) {
                        throw new ExceptionChecker("initializer expression type non-conformant with accumulator declared type", nod);
                     }

                     accumInitExp = child.evnode;
                  }
               } while(!(child instanceof OclTEXT));
            } while(!child.stringValue.equals("|"));

            nod.vectorValue = new Vector();
            int i = 0;

            for(Iterator it2 = itnames.iterator(); it2.hasNext(); ++i) {
               OclNode itnode = (OclNode)it2.next();
               itnode.type = ittype;
               nod.vectorValue.add(new OclType(this.umlapi.OCLITERATOR));
               OclConstant cst = new OclConstant(itnode, (Object)null);
               nod.vectorEval.add(new OclConstant(itnode, cst));
               OclLetItem iteratorItem = new OclLetItem(itnode.stringValue, ittype, implicitType ? 5 : 1, cst);
               if (!this.umlapi.varTable.addVariable(iteratorItem)) {
                  throw new ExceptionChecker("iterator variable name already exists", nod);
               }

               if (i == 0) {
                  nod.parent.searchResult.declarator1 = iteratorItem;
               } else {
                  nod.parent.searchResult.declarator2 = iteratorItem;
               }
            }

            if (acname != null) {
               nod.vectorValue.add(actype);
               OclAccumulator vr = new OclAccumulator(nod, accumInitExp);
               nod.vectorEval.add(new OclConstant(nod, vr));
               OclLetItem accItem = new OclLetItem(acname, actype, 1, vr);
               if (!this.umlapi.varTable.addVariable(accItem)) {
                  throw new ExceptionChecker("accumulator variable name already exists", nod);
               }

               nod.parent.searchResult.accumulator = accItem;
            }
         }
      }
   }

   public void visitPost(pathName nod) {
      nod.stringValue = nod.getValueAsString();
   }

   public void visitPost(dottedPathName nod) {
      nod.stringValue = nod.getValueAsString();
   }

   public void visitPost(timeExpression nod) throws ExceptionChecker {
      if (!this.contextStereotype.equals("post")) {
         throw new ExceptionChecker("@pre can be used only in post-conditions", nod);
      }
   }

   public void visitPost(actualParameterList nod) {
      nod.vectorValue = new Vector();
      nod.vectorEval = new Vector();
      OclConstant cst = new OclConstant("Message operators are currently not evaluatable", nod);
      Iterator it = nod.children.iterator();

      while(it.hasNext()) {
         OclNode child = (OclNode)it.next();
         if (child instanceof expression) {
            nod.vectorValue.add(child.type);
            if (nod.parent.parent.isCollectionOperation) {
               nod.vectorEval.add(new OclConstant(child, child.evnode));
            } else {
               nod.vectorEval.add(child.evnode);
            }
         } else if (child instanceof OclTEXT) {
            if (child.stringValue.equals("?")) {
               nod.vectorValue.add(OclType.ANYTYPE);
               nod.vectorEval.add(cst);
            } else if (child.stringValue.equals(":")) {
               nod.vectorValue.setSize(nod.vectorValue.size() - 1);
               nod.vectorEval.setSize(nod.vectorEval.size() - 1);
            }
         } else if (child instanceof typeSpecifier) {
            nod.vectorValue.add(child.type);
            nod.vectorEval.add(cst);
         }
      }

   }

   public void visitPost(collectionKind nod) throws ExceptionChecker {
      String s = nod.getValueAsString();
      nod.stringValue = s;
      if (s.equals("Set")) {
         nod.type = new OclType(this.umlapi.SET, this.umlapi.OCLANY);
      }

      if (s.equals("Bag")) {
         nod.type = new OclType(this.umlapi.BAG, this.umlapi.OCLANY);
      }

      if (s.equals("Sequence")) {
         nod.type = new OclType(this.umlapi.SEQUENCE, this.umlapi.OCLANY);
      }

      if (s.equals("Collection")) {
         nod.type = new OclType(this.umlapi.COLLECTION, this.umlapi.OCLANY);
      }

      if (s.equals("OrderedSet")) {
         nod.type = new OclType(this.umlapi.ORDEREDSET, this.umlapi.OCLANY);
      }

      if (nod.type == null) {
         throw new ExceptionChecker("[internal] collection type unknown", nod);
      }
   }

   public void visitPost(name nod) {
      nod.stringValue = nod.getValueAsString();
   }

   public void visitPost(string nod) throws ExceptionChecker {
      this.checkTerminal(nod);
      nod.type = new OclType(this.umlapi.STRING);
      String s = nod.getValueAsString();
      s = s.substring(1, s.length() - 1);
      nod.evnode = new OclConstant(nod, new OclString(s));
   }

   public void visitPost(number nod) throws ExceptionChecker {
      this.checkTerminal(nod);
      if (nod.firstToken().getType() == 2) {
         nod.type = new OclType(this.umlapi.INTEGER);
         nod.evnode = new OclConstant(nod, new OclInteger(nod.getValueAsString()));
      } else {
         nod.type = new OclType(this.umlapi.REAL);
         nod.evnode = new OclConstant(nod, new OclReal(nod.getValueAsString()));
      }

   }

   public void visitPost(untilExpression nod) throws ExceptionChecker {
      this.checkEmptyExpr(nod);
   }

   static {
      String[] ops = new String[]{"=", "<>", "<", ">", "<=", ">=", "or", "and", "xor", "not", "+", "-", "/", "*", "div", "mod", "implies"};
      String[] ids = new String[]{"equal", "notequal", "lt", "gt", "le", "ge", "or", "and", "xor", "not", "add", "subs", "divide", "multiply", "div", "mod", "implies"};

      for(int i = 0; i < ops.length; ++i) {
         ops2ids.put(ops[i], ids[i]);
      }

   }
}
