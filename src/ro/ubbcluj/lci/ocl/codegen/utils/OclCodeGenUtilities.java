package ro.ubbcluj.lci.ocl.codegen.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import org.apache.velocity.Template;
import org.apache.velocity.context.Context;
import ro.ubbcluj.lci.codegen.CodeGenUtilities;
import ro.ubbcluj.lci.codegen.CodeGenerationConstants;
import ro.ubbcluj.lci.codegen.CodeGeneratorManager;
import ro.ubbcluj.lci.codegen.ImportStatementManager;
import ro.ubbcluj.lci.ocl.OclClassInfo;
import ro.ubbcluj.lci.ocl.OclType;
import ro.ubbcluj.lci.ocl.OclUtil;
import ro.ubbcluj.lci.ocl.SearchResult;
import ro.ubbcluj.lci.ocl.codegen.norm.NormalForm;
import ro.ubbcluj.lci.ocl.codegen.norm.OperatorNode;
import ro.ubbcluj.lci.ocl.codegen.norm.SyntaxTreeNode;
import ro.ubbcluj.lci.uml.foundation.core.Association;
import ro.ubbcluj.lci.uml.foundation.core.AssociationEnd;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.core.Feature;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.uml.foundation.core.Operation;
import ro.ubbcluj.lci.utils.NamingContext;
import ro.ubbcluj.lci.utils.uml.UMLUtilities;

public final class OclCodeGenUtilities implements CodeGenerationConstants {
   private static NormalForm currentNormalForm = null;
   private static String tupleTypesPackageName;

   public OclCodeGenUtilities() {
   }

   public static OclType getType(SearchResult sr) {
      if (sr.foundLetItem != null) {
         return sr.foundLetItem.rettype;
      } else {
         try {
            Classifier rt;
            if (sr.attr != null) {
               rt = sr.attr.getType();
               return new OclType(OclUtil.umlapi.getClassInfoByClassifier(rt));
            } else if (sr.asend != null) {
               rt = sr.asend.getParticipant();
               OclClassInfo p_ci = OclUtil.umlapi.getClassInfoByClassifier(rt);
               OclType pType = new OclType(p_ci);
               if (UMLUtilities.isMultiple(sr.asend)) {
                  OclClassInfo set_ci = OclUtil.umlapi.SET;
                  return new OclType(set_ci, pType, false);
               } else {
                  return pType;
               }
            } else {
               rt = CodeGenUtilities.getReturnType(sr.operation);
               return new OclType(OclUtil.umlapi.getClassInfoByClassifier(rt));
            }
         } catch (Exception var5) {
            var5.printStackTrace();
            return null;
         }
      }
   }

   public static String getTupleTypesPackage() {
      return tupleTypesPackageName;
   }

   public static void setTupleTypePackage(String ttp) {
      tupleTypesPackageName = ttp;
   }

   public static void updateContext(NamingContext context, Object element) {
      if (element instanceof Classifier) {
         Iterator itFeat = ((Classifier)element).getCollectionFeatureList().iterator();

         while(itFeat.hasNext()) {
            context.add(((Feature)itFeat.next()).getName());
         }

         itFeat = ((Classifier)element).allOppositeAssociationEnds().iterator();

         while(itFeat.hasNext()) {
            context.add(((AssociationEnd)itFeat.next()).getName());
         }
      } else if (element instanceof Operation) {
         updateContext(context, ((Operation)element).getOwner());
         Enumeration params = ((Operation)element).getParameterList();

         while(params.hasMoreElements()) {
            String name = ((ModelElement)params.nextElement()).getName();
            if (!context.contains(name)) {
               context.add(name);
            }
         }
      }

   }

   public static String getDefaultValueForUndefined(OclType typeOfValue, ImportStatementManager m) {
      if (typeOfValue.type == OclUtil.umlapi.BOOLEAN) {
         return "false";
      } else {
         String fTypeName;
         if (typeOfValue.type == OclUtil.umlapi.INTEGER) {
            fTypeName = getTypeName("java.lang.Integer", m);
            return fTypeName + ".MAX_VALUE";
         } else if (typeOfValue.type == OclUtil.umlapi.REAL) {
            fTypeName = getTypeName("java.lang.Float", m);
            return fTypeName + ".POSITIVE_INFINITY";
         } else {
            return "null";
         }
      }
   }

   public static String getParameterDeclarationCode(String paramName, OclType paramType, ImportStatementManager mgr) {
      String typeName = getTypeName(paramType.type, mgr);
      StringBuffer b = (new StringBuffer(typeName)).append(' ').append(paramName);
      return b.toString();
   }

   public static String getParameterDeclarationCode(String paramName, Classifier paramType, ImportStatementManager mgr) {
      String typeName = CodeGenUtilities.getTypeName(paramType, mgr);
      StringBuffer b = (new StringBuffer(typeName)).append(' ').append(paramName);
      return b.toString();
   }

   public static String castAndUnwrap(OclType resultType, String baseExpression, String wrapperName) {
      Template t = CodeGeneratorManager.getTemplate("OCL_cast_and_unwrap");
      Context c = CodeGeneratorManager.newContext();
      c.put("EXPR", baseExpression);
      c.put("RESULT_TYPE", resultType);
      c.put("WRAPPER_NAME", wrapperName);
      String result = CodeGenUtilities.getText(t, c).toString();
      c.remove(resultType);
      return result;
   }

   public static String getTypeName(OclType t, ImportStatementManager mgr, boolean wrapped) {
      if (!t.isTuple()) {
         return getTypeName(t.type, mgr, wrapped);
      } else {
         String name = currentNormalForm.tupleTypeName(t);
         String q = getTupleTypesPackage() + '.' + name;
         return mgr.registerClass(q) ? name : q;
      }
   }

   public static String getTypeName(OclClassInfo type, ImportStatementManager mgr) {
      return getTypeName(type, mgr, false);
   }

   private static String getTypeName(OclClassInfo type, ImportStatementManager mgr, boolean wrapped) {
      String ret = UMLUtilities.getFullyQualifiedName((ModelElement)type.classifier).replaceAll("::", ".");
      if (type == OclUtil.umlapi.REAL) {
         ret = wrapped ? CodeGenerationConstants.DATATYPES_PATH + ".Real" : "float";
      } else if (type == OclUtil.umlapi.INTEGER) {
         ret = wrapped ? CodeGenerationConstants.DATATYPES_PATH + ".Integer" : "int";
      } else if (type == OclUtil.umlapi.BOOLEAN) {
         ret = wrapped ? CodeGenerationConstants.DATATYPES_PATH + ".Boolean" : "boolean";
      } else if (type == OclUtil.umlapi.STRING) {
         ret = "java.lang.String";
      } else if (type == OclUtil.umlapi.OCLTYPE) {
         ret = CodeGenerationConstants.OCL_PATH + ".OclType";
      } else if (type == OclUtil.umlapi.OCLANY) {
         ret = "java.lang.Object";
      }

      if (type.isCollectionType()) {
         String name = type.name;
         if (name.startsWith("Set")) {
            ret = "java.util.Set";
         } else if (!name.startsWith("Bag") && !name.startsWith("Sequence")) {
            if (name.startsWith("OrderedSet")) {
               ret = CodeGenerationConstants.DATATYPES_PATH + ".OrderedSet";
            } else {
               ret = "java.util.Collection";
            }
         } else {
            ret = "java.util.List";
         }
      }

      if (mgr.registerClass(ret)) {
         int i = ret.lastIndexOf(46);
         ret = ret.substring(i + 1);
      }

      return ret;
   }

   public static String getTypeName(String qualified, ImportStatementManager mgr) {
      if (mgr.registerClass(qualified)) {
         int i = qualified.lastIndexOf(46);
         return i >= 0 ? qualified.substring(i + 1) : qualified;
      } else {
         return qualified;
      }
   }

   public static String getExpression(String baseExpression, OclType type, ImportStatementManager mgr, boolean wrap) {
      if (!wrap) {
         return baseExpression;
      } else {
         boolean isPrimitive = type.type == OclUtil.umlapi.BOOLEAN || type.type == OclUtil.umlapi.REAL || type.type == OclUtil.umlapi.INTEGER;
         if (!isPrimitive) {
            return baseExpression;
         } else {
            String typeName = getTypeName(type, mgr, true);
            return CodeGenUtilities.getTextForMethodCall(typeName, "to" + type.name(), Collections.singletonList(baseExpression)).toString();
         }
      }
   }

   public static String getBaseName(SyntaxTreeNode node) {
      NameVisitor nv = new NameVisitor();
      node.accept(nv);
      return nv.theName;
   }

   public static OclClassInfo getOwner(SearchResult sr) {
      Classifier clsOwner = null;
      if (sr.attr != null) {
         clsOwner = sr.attr.getOwner();
      } else if (sr.asend != null) {
         Classifier participant = sr.asend.getParticipant();
         Association assoc = sr.asend.getAssociation();
         Iterator itOpAsEnds = participant.oppositeAssociationEnds().iterator();

         while(clsOwner == null && itOpAsEnds.hasNext()) {
            AssociationEnd assEndNext = (AssociationEnd)itOpAsEnds.next();
            if (assEndNext.getAssociation() == assoc) {
               clsOwner = assEndNext.getParticipant();
            }
         }
      } else {
         clsOwner = sr.operation != null ? sr.operation.getOwner() : null;
      }

      try {
         return clsOwner == null ? sr.foundOwner : OclUtil.umlapi.getClassInfoByClassifier(clsOwner);
      } catch (Exception var6) {
         System.err.println(var6.getMessage());
         return null;
      }
   }

   public static StringBuffer getTextForType(OclType type, ImportStatementManager mgr) {
      Template tType = CodeGeneratorManager.getTemplate("OCL_type");
      Context cType = CodeGeneratorManager.newContext();
      String initial = CodeGenerationConstants.OCL_PATH + ".Ocl";
      String javaLangClass = "java.lang.Class";
      if (mgr.registerClass(initial)) {
         initial = "Ocl";
      }

      cType.put("OCL", initial);
      if (mgr.registerClass(javaLangClass)) {
         javaLangClass = "Class";
      }

      cType.put("CLASS", javaLangClass);
      ArrayList clsArgs = new ArrayList();

      for(OclType c = type; c != null; c = c.celement) {
         String name = getTypeName(c, mgr, true);
         name = name + ".class";
         clsArgs.add(name);
      }

      cType.put("TYPES", clsArgs);
      StringBuffer t = CodeGenUtilities.getText(tType, cType);
      return t;
   }

   public static void setCurrentNormalForm(NormalForm nf) {
      currentNormalForm = nf;
   }

   public static NormalForm getCurrentNormalForm() {
      return currentNormalForm;
   }

   static String getPrefix(OclType type) {
      String pref = "";
      OclClassInfo ci = type.type;
      if (ci == OclUtil.umlapi.REAL) {
         pref = "f";
      } else if (ci == OclUtil.umlapi.INTEGER) {
         pref = "n";
      } else if (ci == OclUtil.umlapi.BOOLEAN) {
         pref = "b";
      } else if (ci == OclUtil.umlapi.BAG) {
         pref = "bag";
      } else if (ci == OclUtil.umlapi.COLLECTION) {
         pref = "coll";
      } else if (ci == OclUtil.umlapi.OCLANY) {
         pref = "any";
      } else if (ci == OclUtil.umlapi.OCLTYPE) {
         pref = "type";
      } else if (ci != OclUtil.umlapi.ORDEREDSET && ci != OclUtil.umlapi.SET) {
         if (ci == OclUtil.umlapi.SEQUENCE) {
            pref = "seq";
         } else if (ci == OclUtil.umlapi.STRING) {
            pref = "str";
         } else {
            pref = Character.toLowerCase(ci.name.charAt(0)) + ci.name.substring(1);
         }
      } else {
         pref = "set";
      }

      return pref;
   }

   public static String getOperatorExpression(OperatorNode operator, SyntaxTreeNode first, SyntaxTreeNode second, StringBuffer typeBuffer, ImportStatementManager manager) {
      typeBuffer.setLength(0);
      typeBuffer.append(getTypeName(operator.getType(), manager, false));
      String text = operator.getText();
      String secondText = second.getCodeInfo().getText();
      StringBuffer bfResult = new StringBuffer();
      OclClassInfo secondType = second.getType().type;
      if (first != null) {
         String firstText = first.getCodeInfo().getText();
         OclClassInfo firstType = first.getType().type;
         if ("implies".equals(text)) {
            bfResult.append('!').append(firstText);
            bfResult.append(" || ").append(secondText);
         } else {
            String operatorText = null;
            boolean useOperatorText = true;
            boolean isEquals = "=".equals(text);
            boolean isNotEquals = "<>".equals(text);
            bfResult.append(firstText).append(' ');
            if ("or".equals(text)) {
               operatorText = "||";
            } else if ("and".equals(text)) {
               operatorText = "&&";
            } else if ("xor".equals(text)) {
               operatorText = "^";
            } else if (!"+".equals(text) && !"*".equals(text) && !"/".equals(text)) {
               String operationName;
               ArrayList args;
               if (!"<".equals(text) && !"<=".equals(text) && !">".equals(text) && !">=".equals(text)) {
                  if ("-".equals(text)) {
                     if (first.getType().type != OclUtil.umlapi.SET && first.getType().type != OclUtil.umlapi.ORDEREDSET) {
                        operatorText = "-";
                     } else {
                        useOperatorText = false;
                        bfResult.setLength(0);
                        operationName = getTypeName(CodeGenerationConstants.OCL_PATH + ".CollectionUtilities", manager);
                         args = new ArrayList();
                        args.add(firstText);
                        args.add(secondText);
                        bfResult.append(CodeGenUtilities.getTextForMethodCall(operationName, "minus", args));
                     }
                  } else if (isEquals || isNotEquals) {
                     boolean firstIsPrimitive = firstType.isPrimitiveType();
                     boolean secondIsPrimitive = secondType.isPrimitiveType();
                     if (firstIsPrimitive && secondIsPrimitive) {
                        if (firstType == OclUtil.umlapi.BOOLEAN ^ secondType == OclUtil.umlapi.BOOLEAN) {
                           useOperatorText = false;
                           bfResult.setLength(0);
                           bfResult.append(isEquals ? "false" : "true");
                        } else {
                           operatorText = isEquals ? "==" : "!=";
                        }
                     } else {
                        useOperatorText = false;
                        bfResult.setLength(0);
                        if (firstIsPrimitive ^ secondIsPrimitive) {
                           bfResult.append(isEquals ? "false" : "true");
                        } else {
                           args = new ArrayList();
                           args.add(secondText);
                           bfResult.append(CodeGenUtilities.getTextForMethodCall(firstText, "equals", args));
                           if (isNotEquals) {
                              bfResult.insert(0, '!');
                           }
                        }
                     }
                  }
               } else if (firstType == OclUtil.umlapi.STRING) {
                  operationName = null;
                  if ("<".equals(text)) {
                     operationName = "lt";
                  } else if ("<=".equals(text)) {
                     operationName = "le";
                  } else if (">".equals(text)) {
                     operationName = "gt";
                  } else if (">=".equals(text)) {
                     operationName = "ge";
                  }

                  useOperatorText = false;
                  bfResult.setLength(0);
                  String basicUtilitiesTypeName = getTypeName(CodeGenerationConstants.OCL_PATH + ".BasicUtilities", manager);
                  args = new ArrayList();
                  args.add(firstText);
                  args.add(secondText);
                  bfResult.append(CodeGenUtilities.getTextForMethodCall(basicUtilitiesTypeName, operationName, args));
               } else {
                  operatorText = text;
               }
            } else {
               operatorText = text;
            }

            if (useOperatorText) {
               bfResult.append(operatorText).append(' ').append(secondText);
            }
         }
      } else {
         bfResult.append(getTextForUnaryOperator(text));
         bfResult.append(secondText);
      }

      return bfResult.toString();
   }

   private static String getTextForUnaryOperator(String unaryOperator) {
      return "not".equals(unaryOperator) ? "!" : unaryOperator;
   }
}
