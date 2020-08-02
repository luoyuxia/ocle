package ro.ubbcluj.lci.codegen;

import java.io.StringWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import org.apache.velocity.Template;
import org.apache.velocity.context.Context;
import ro.ubbcluj.lci.uml.foundation.core.AssociationEnd;
import ro.ubbcluj.lci.uml.foundation.core.Attribute;
import ro.ubbcluj.lci.uml.foundation.core.BehavioralFeature;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.uml.foundation.core.Parameter;
import ro.ubbcluj.lci.utils.DataTypeSystem;
import ro.ubbcluj.lci.utils.ModelFactory;
import ro.ubbcluj.lci.utils.uml.UMLUtilities;

public final class CodeGenUtilities implements CodeGenerationConstants {
   public CodeGenUtilities() {
   }

   public static String getTypeName(Classifier clsType, ImportStatementManager manager) {
      String startName = getTypeName(clsType);
      int idx = startName.lastIndexOf(46);
      return manager.registerClass(startName) && idx >= 0 ? startName.substring(idx + 1) : startName;
   }

   private static String getTypeName(Classifier type) {
      if (type == null) {
         return "void";
      } else {
         String base = UMLUtilities.getFullyQualifiedName((ModelElement)type).replaceAll("::", ".");
         DataTypeSystem dts = ModelFactory.getDataTypeSystem();
         if (dts.isPredefined("Real", type)) {
            base = "float";
         } else if (dts.isPredefined("Integer", type)) {
            base = "int";
         } else if (dts.isPredefined("Boolean", type)) {
            base = "boolean";
         } else if (dts.isPredefined("undefined", type)) {
            base = "void";
         } else if (dts.isPredefined("String", type)) {
            base = "java.lang.String";
         } else if (dts.isPredefined("UnlimitedInteger", type)) {
            base = "java.math.BigInteger";
         }

         return base;
      }
   }

   public static String getTypeName(String qualified, ImportStatementManager mgr) {
      String java = qualified.replaceAll("::", ".");
      int idx = java.lastIndexOf(46);
      if (mgr.registerClass(java)) {
         return idx >= 0 ? java.substring(idx + 1) : java;
      } else {
         return java;
      }
   }

   public static Classifier getReturnType(BehavioralFeature bf) {
      Enumeration en = bf.getParameterList();
      Parameter pReturn = null;

      while(pReturn == null && en.hasMoreElements()) {
         Parameter p = (Parameter)en.nextElement();
         if (p.getKind() == 3) {
            pReturn = p;
         }
      }

      return pReturn != null ? pReturn.getType() : null;
   }

   public static String getPreferredSetterPrefix(AssociationEnd ae) {
      boolean bMultiple = UMLUtilities.isMultiple(ae);
      boolean bQualified = UMLUtilities.isQualified(ae);
      return !bMultiple && !bQualified ? "set" : "add";
   }

   public static String getPreferredAccessorSuffix(AssociationEnd ae) {
      return toTitleCase(ae.getName());
   }

   public static StringBuffer getText(Template t, Context c) {
      StringWriter wr = new StringWriter();

      try {
         t.merge(c, wr);
         return wr.getBuffer();
      } catch (Exception var4) {
         var4.printStackTrace();
         return null;
      }
   }

   public static StringBuffer getTextForAssignment(String modifiers, String typeName, String varName, String expr) {
      Template tAssign = CodeGeneratorManager.getTemplate("OCL_assignment");
      Context cAssign = CodeGeneratorManager.newContext();
      cAssign.put("MOD", modifiers);
      cAssign.put("TYPE", typeName);
      cAssign.put("VAR", varName);
      cAssign.put("EXP", expr);
      return getText(tAssign, cAssign);
   }

   public static String getTextForCast(String typeSpec, String toCast) {
      Template t = CodeGeneratorManager.getTemplate("OCL_type_cast");
      Context c = CodeGeneratorManager.newContext();
      c.put("TYPE", typeSpec);
      c.put("EXP", toCast);
      return getText(t, c).toString();
   }

   public static StringBuffer getTextForAttributeCall(String selfSpec, String attributeName) {
      Template t = CodeGeneratorManager.getTemplate("OCL_attribute_call");
      Context c = CodeGeneratorManager.newContext();
      c.put("OBJECT", selfSpec);
      c.put("ATTR", attributeName);
      return getText(t, c);
   }

   public static StringBuffer getTextForReturnStatement(String val) {
      Template t = CodeGeneratorManager.getTemplate("OCL_return_statement");
      Context c = CodeGeneratorManager.newContext();
      c.put("VAL", val);
      return getText(t, c);
   }

   public static StringBuffer getTextForMethodCall(String objectSpec, String methodName, List args) {
      Context c = CodeGeneratorManager.newContext();
      Template t;
      if (objectSpec == null) {
         t = CodeGeneratorManager.getTemplate("OCL_method_call");
      } else {
         t = CodeGeneratorManager.getTemplate("OCL_method_call_object");
         c.put("OBJECT", objectSpec);
      }

      c.put("METHOD", methodName);
      if (args == null) {
         args = Collections.EMPTY_LIST;
      }

      c.put("ARGS", args);
      return getText(t, c);
   }

   public static StringBuffer getExpression(String baseExpression, Classifier type, ImportStatementManager im, boolean wrap) {
      DataTypeSystem dts = ModelFactory.getDataTypeSystem();
      boolean isPrimitive = dts.isPredefined("Integer", type) || dts.isPredefined("Real", type) || dts.isPredefined("Boolean", type);
      if (wrap && isPrimitive) {
         String qualifiedTypeName = CodeGenerationConstants.DATATYPES_PATH + '.' + type.getName();
         return new StringBuffer(getTypeName(qualifiedTypeName, im) + ".to" + type.getName() + '(' + baseExpression + ')');
      } else {
         return new StringBuffer(baseExpression);
      }
   }

   public static StringBuffer getTextForKey(Collection qualifiers, String keyVar, ImportStatementManager im) {
      StringBuffer result = new StringBuffer();
      String concreteListType = getTypeName(CodeGenerationConstants.CONCRETE_LIST_TYPE, im);
      result.append(getTextForAssignment((String)null, concreteListType, keyVar, "new " + concreteListType + "()"));
      Iterator itQualifiers = qualifiers.iterator();

      while(itQualifiers.hasNext()) {
         Attribute qualifier = (Attribute)itQualifiers.next();
         Classifier qType = qualifier.getType();
         result.append(getTextForMethodCall(keyVar, "add", Collections.singletonList(getExpression(qualifier.getName(), qType, im, true)))).append(";\n");
      }

      return result;
   }

   private static String toTitleCase(String s) {
      return s != null && s.length() >= 1 ? Character.toUpperCase(s.charAt(0)) + s.substring(1) : "";
   }
}
