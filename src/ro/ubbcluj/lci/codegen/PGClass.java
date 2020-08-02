package ro.ubbcluj.lci.codegen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import org.apache.velocity.Template;
import org.apache.velocity.context.Context;
import ro.ubbcluj.lci.utils.NamingContext;

public final class PGClass extends PGElement implements CodeGenerationConstants {
   private String packageName;
   private List implInterfaces;
   private List extClasses;
   private NamingContext operations = new NamingContext();
   private boolean isInterface;

   public PGClass(PGElement container) {
      super(container);
   }

   public void addImplInterface(String implInterface) {
      if (this.implInterfaces == null) {
         this.implInterfaces = new ArrayList();
      }

      this.implInterfaces.add(implInterface);
   }

   public void addExtClass(String extClass) {
      if (this.extClasses == null) {
         this.extClasses = new ArrayList();
      }

      this.extClasses.add(extClass);
   }

   public void setPackage(String packageName) {
      this.packageName = packageName;
   }

   public PGMethod getMatchingMethod(String mName, String retType, PGParameter[] fp) {
      PGMethod ret = null;
      Enumeration en = this.children();

      while(ret == null && en.hasMoreElements()) {
         Object next = en.nextElement();
         if (next instanceof PGMethod) {
            PGMethod m = (PGMethod)next;
            if (mName.equals(m.getName()) && m.matchesSignature(retType, fp)) {
               ret = m;
            }
         }
      }

      return ret;
   }

   protected StringBuffer getDeclarationCode() {
      Template template = null;
      if (this.getParent() == null) {
         template = CodeGeneratorManager.getTemplate("UML_ClassDeclaration.vm");
      } else {
         template = CodeGeneratorManager.getTemplate("UML_InnerClassDeclaration.vm");
      }

      Context context = CodeGeneratorManager.newContext();
      context.put("classname", this.name);
      context.put("packagename", this.packageName);
      context.put("modifiers", this.modifiers);
      context.put("importstatements", this.getImportManager().getImportStatements());
      context.put("implinterfaces", this.implInterfaces);
      context.put("extclasses", this.extClasses);
      StringBuffer sb = CodeGenUtilities.getText(template, context);
      return sb;
   }

   public String getAdmissibleMethodName(String requestedName) {
      return this.operations.registerNewName(requestedName);
   }

   public void registerOperation(String operationName) {
      this.operations.add(operationName);
   }

   public void orderChildren(Comparator orderStrategy) {
      if (this.children != null && orderStrategy != null) {
         Collections.sort(this.children, orderStrategy);
      }

   }

   public void setIsInterface(boolean isInterface) {
      this.isInterface = isInterface;
   }

   public boolean isInterface() {
      return this.isInterface;
   }
}
