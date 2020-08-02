package ro.ubbcluj.lci.ocl.eval;

import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Vector;
import ro.ubbcluj.lci.ocl.ExceptionAny;
import ro.ubbcluj.lci.uml.foundation.core.Component;
import ro.ubbcluj.lci.uml.foundation.core.ElementOwnership;
import ro.ubbcluj.lci.uml.foundation.core.ElementResidence;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.uml.foundation.core.Namespace;
import ro.ubbcluj.lci.uml.foundation.core.TemplateParameter;
import ro.ubbcluj.lci.uml.modelManagement.ElementImport;
import ro.ubbcluj.lci.uml.modelManagement.Package;

public class OclDirect {
   private ModelElement object;
   private Vector result = new Vector();

   public OclDirect(ModelElement p_object) {
      this.object = p_object;
   }

   public Enumeration directGetOwnedElementList() {
      Enumeration en = ((Namespace)this.object).getOwnedElementList();

      while(en.hasMoreElements()) {
         this.result.add(((ElementOwnership)en.nextElement()).getOwnedElement());
      }

      return this.result.elements();
   }

   public Enumeration directGetImportedElementList() {
      Enumeration en = ((Package)this.object).getImportedElementList();

      while(en.hasMoreElements()) {
         this.result.add(((ElementImport)en.nextElement()).getImportedElement());
      }

      return this.result.elements();
   }

   public Enumeration directGetResidentList() {
      Enumeration en = ((Component)this.object).getResidentList();

      while(en.hasMoreElements()) {
         this.result.add(((ElementResidence)en.nextElement()).getResident());
      }

      return this.result.elements();
   }

   public Enumeration directGetContainerList() {
      Enumeration en = this.object.getContainerList();

      while(en.hasMoreElements()) {
         this.result.add(((ElementResidence)en.nextElement()).getContainer());
      }

      return this.result.elements();
   }

   public Enumeration directGetParameterTemplateList() {
      Enumeration en = this.object.getParameterTemplateList();

      while(en.hasMoreElements()) {
         this.result.add(((TemplateParameter)en.nextElement()).getParameterTemplate());
      }

      return this.result.elements();
   }

   public Enumeration directGetPackageList() throws ExceptionAny {
      Class[] paramTypes = new Class[0];
      Method meth = null;

      try {
         meth = (ModelElement.class).getMethod("getPackageList", paramTypes);
      } catch (Exception var6) {
         try {
            meth = (ModelElement.class).getMethod("getElementImportList", paramTypes);
         } catch (Exception var5) {
            throw new ExceptionAny("[internal] cannot find getPackageList or getElementImportList method using reflection");
         }
      }

      Object[] params = new Object[0];

      try {
         Enumeration en = (Enumeration)meth.invoke(this.object, params);

         while(en.hasMoreElements()) {
            this.result.add(((ElementImport)en.nextElement()).getPackage());
         }

         return this.result.elements();
      } catch (Exception var7) {
         throw new ExceptionAny("[internal] error invoking the method " + meth.getName());
      }
   }

   public ModelElement directGetNamespace() {
      ElementOwnership eo = this.object.getNamespace();
      return eo == null ? null : eo.getNamespace();
   }

   public ModelElement directGetTemplate() {
      TemplateParameter tp = this.object.getTemplate();
      return tp == null ? null : tp.getTemplate();
   }
}
