package ro.ubbcluj.lci.xmi.structure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

public class ClassDescriptor {
   private Class methodDeclareClass;
   private Class objectInstantiateClass;
   private ArrayList fieldList;

   public ClassDescriptor() {
      this.fieldList = new ArrayList();
   }

   public ClassDescriptor(Class c) {
      this.methodDeclareClass = c;
      this.fieldList = new ArrayList();
   }

   public Class getDescriptedClass() {
      return this.methodDeclareClass;
   }

   public void setDescriptedClass(Class dc) {
      this.methodDeclareClass = dc;
   }

   public void setObjectInstantiateClass(Class objectInstantiateClass) {
      this.objectInstantiateClass = objectInstantiateClass;
   }

   public Class getObjectInstantiateClass() {
      return this.objectInstantiateClass;
   }

   public void addField(FieldDescriptor fd) {
      for(int i = 0; i < this.fieldList.size(); ++i) {
         FieldDescriptor fdd = (FieldDescriptor)this.fieldList.get(i);
         if (fdd.getFieldName().equals(fd.getFieldName()) && fdd.getGetterMethod().equals(fd.getGetterMethod()) && fdd.getSetterMethod().equals(fd.getSetterMethod())) {
            return;
         }
      }

      this.fieldList.add(fd);
   }

   public Enumeration getFieldList() {
      return Collections.enumeration(this.fieldList);
   }

   public String toString() {
      return this.methodDeclareClass != null ? this.methodDeclareClass.getName() : "WARNING! Not an appropriate Class object!";
   }

   public Object newInstance() throws InstantiationException, IllegalAccessException {
      Object o = null;
      if (this.objectInstantiateClass != null) {
         o = this.objectInstantiateClass.newInstance();
      }

      return o;
   }
}
