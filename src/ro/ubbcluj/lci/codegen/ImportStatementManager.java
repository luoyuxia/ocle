package ro.ubbcluj.lci.codegen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import ro.ubbcluj.lci.uml.foundation.core.AssociationClass;
import ro.ubbcluj.lci.uml.foundation.core.Class;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.core.Interface;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.uml.foundation.core.Namespace;
import ro.ubbcluj.lci.uml.modelManagement.Package;
import ro.ubbcluj.lci.utils.uml.UMLUtilities;

public final class ImportStatementManager {
   private static List PRIMITIVE_TYPES = new ArrayList();
   private String theClass;
   private String thePackage;
   private List autoImportedClasses;
   private List otherPackageClasses;

   public ImportStatementManager(Classifier classifier) {
      this(classifier, true);
   }

   public ImportStatementManager(String packagePath, String className) {
      this.theClass = className;
      this.thePackage = packagePath;
      this.autoImportedClasses = new ArrayList();
      this.otherPackageClasses = new ArrayList();
   }

   private ImportStatementManager(Classifier classifier, boolean addDefaultJavaLangClasses) {
      Namespace namespace = classifier.directGetNamespace();
      String[] autoImportedClasses = null;
      if (namespace instanceof Package) {
         autoImportedClasses = this.getClassesFromPackage((Package)namespace);
      }

      this.otherPackageClasses = new ArrayList();
      this.autoImportedClasses = new ArrayList();
      String className = UMLUtilities.getFullyQualifiedName((ModelElement)classifier).replaceAll("::", ".");
      this.setClass(className, autoImportedClasses);
      if (addDefaultJavaLangClasses) {
         this.registerJavaLangClasses();
      }

   }

   public ImportStatementManager(String theClass, String[] autoImportedClasses) {
      this(theClass, autoImportedClasses, true);
   }

   public ImportStatementManager(String theClass, String[] autoImportedClasses, boolean addDefaultJavaLangClasses) {
      this.otherPackageClasses = new ArrayList();
      this.autoImportedClasses = new ArrayList();
      this.setClass(theClass, autoImportedClasses);
      if (addDefaultJavaLangClasses) {
         this.registerJavaLangClasses();
      }

   }

   public void clearImports() {
      this.autoImportedClasses.clear();
      this.otherPackageClasses.clear();
   }

   public List getImportStatements() {
      String[] temp = new String[this.otherPackageClasses.size()];
      this.otherPackageClasses.toArray(temp);
      List statements = new ArrayList();

      for(int i = 0; i < temp.length; ++i) {
         String aClass = temp[i];
         int index = aClass.lastIndexOf(".");
         String aPackage = index >= 0 ? aClass.substring(0, aClass.lastIndexOf(".")) : "";
         if (!aPackage.equals("java.lang")) {
            statements.add("import " + aClass + ";");
         }
      }

      Collections.sort(statements);
      return statements;
   }

   private void setClass(String theClass, String[] autoImportClasses) {
      this.theClass = theClass;
      if (theClass.indexOf(".") < 0) {
         this.thePackage = "";
      } else {
         this.thePackage = theClass.substring(0, theClass.lastIndexOf("."));
      }

      this.registerClass(theClass);

      for(int i = 0; i < autoImportClasses.length; ++i) {
         this.registerClass(autoImportClasses[i]);
      }

   }

   public boolean registerClass(String aClass) {
      if (!PRIMITIVE_TYPES.contains(aClass) && !this.theClass.equals(aClass)) {
         String aPackageName = null;
         String aClassName = null;
         if (aClass.indexOf(".") < 0) {
            aPackageName = "";
            aClassName = aClass;
         } else {
            aPackageName = aClass.substring(0, aClass.lastIndexOf("."));
            aClassName = aClass.substring(aClass.lastIndexOf(".") + 1);
         }

         Iterator it;
         String osClass;
         String osClassName;
         if (this.thePackage.equals(aPackageName)) {
            it = this.otherPackageClasses.iterator();

            while(it.hasNext()) {
               osClass = (String)it.next();
               osClassName = osClass.indexOf(".") >= 0 ? osClass.substring(osClass.lastIndexOf(".")) + 1 : osClass;
               if (aClassName.equals(osClassName)) {
                  it.remove();
               }
            }

            if (!this.autoImportedClasses.contains(aClass)) {
               this.autoImportedClasses.add(aClass);
            }
         } else {
            it = this.autoImportedClasses.iterator();

            while(it.hasNext()) {
               osClass = (String)it.next();
               osClassName = osClass.indexOf(".") >= 0 ? osClass.substring(osClass.lastIndexOf(".")) + 1 : osClass;
               if (aClassName.equals(osClassName)) {
                  return false;
               }
            }

            it = this.otherPackageClasses.iterator();

            while(it.hasNext()) {
               osClass = (String)it.next();
               osClassName = osClass.indexOf(".") >= 0 ? osClass.substring(osClass.lastIndexOf(".")) + 1 : osClass;
               if (aClassName.equals(osClassName)) {
                  return false;
               }
            }

            if (!this.otherPackageClasses.contains(aClass)) {
               this.otherPackageClasses.add(aClass);
            }
         }

         return true;
      } else {
         return true;
      }
   }

   private void registerJavaLangClasses() {
      String[] javalangclasses = new String[]{"CharSequence", "Cloneable", "Comparable", "Runnable", "Boolean", "Byte", "Character", "Class", "ClassLoader", "Compiler", "Double", "Float", "InheritableThreadLocal", "Integer", "Long", "Math", "Number", "Object", "Package", "Process", "Runtime", "RuntimePermission", "SecurityManager", "Short", "StackTraceElement", "StrictMath", "String", "StringBuffer", "System", "Thread", "ThreadGroup", "ThreadLocal", "Throwable", "Void", "ArithmeticException", "ArrayIndexOutOfBoundsException", "ArrayStoreException", "ClassCastException", "ClassNotFoundException", "CloneNotSupportedException", "Exception", "IllegalAccessException", "IllegalArgumentException", "IllegalMonitorStateException", "IllegalStateException", "IllegalThreadStateException", "IndexOutOfBoundsException", "InstantiationException", "InterruptedException", "NegativeArraySizeException", "NoSuchFieldException", "NoSuchMethodException", "NullPointerException", "NumberFormatException", "RuntimeException", "SecurityException", "StringIndexOutOfBoundsException", "UnsupportedOperationException", "AbstractMethodError", "AssertionError", "ClassCircularityError", "ClassFormatError", "Error", "ExceptionInInitializerError", "IllegalAccessError", "IncompatibleClassChangeError", "InstantiationError", "InternalError", "LinkageError", "NoClassDefFoundError", "NoSuchFieldError", "NoSuchMethodError", "OutOfMemoryError", "StackOverflowError", "ThreadDeath", "UnknownError", "UnsatisfiedLinkError", "UnsupportedClassVersionError", "VerifyError", "VirtualMachineError"};

      for(int i = 0; i < javalangclasses.length; ++i) {
         this.registerClass("java.lang." + javalangclasses[i]);
      }

   }

   private String[] getClassesFromPackage(Package umlPackage) {
      List list = new ArrayList();
      Enumeration owned = umlPackage.directGetOwnedElementList();

      while(true) {
         Object o;
         do {
            if (!owned.hasMoreElements()) {
               String[] ret = new String[list.size()];
               list.toArray(ret);
               return ret;
            }

            o = owned.nextElement();
         } while(!(o instanceof Class) && !(o instanceof Interface) && !(o instanceof AssociationClass));

         list.add(UMLUtilities.getFullyQualifiedName((ModelElement)((Classifier)o)).replaceAll("::", "."));
      }
   }

   static {
      PRIMITIVE_TYPES.add("boolean");
      PRIMITIVE_TYPES.add("byte");
      PRIMITIVE_TYPES.add("char");
      PRIMITIVE_TYPES.add("double");
      PRIMITIVE_TYPES.add("float");
      PRIMITIVE_TYPES.add("int");
      PRIMITIVE_TYPES.add("long");
      PRIMITIVE_TYPES.add("short");
      PRIMITIVE_TYPES.add("void");
   }
}
