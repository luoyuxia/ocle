package ro.ubbcluj.lci.xmi.structure;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class FieldDescriptor {
   private String fieldName;
   private Class fieldType;
   protected Class declaringClass;
   private Method getterMethod;
   private Method setterMethod;

   public FieldDescriptor() {
   }

   public FieldDescriptor(String fn, Method gm, Method sm, Class dc) {
      this.fieldName = fn;
      this.getterMethod = gm;
      this.setterMethod = sm;
      this.fieldType = this.setterMethod.getParameterTypes()[0];
      this.declaringClass = dc;
   }

   public void setFieldName(String fn) {
      this.fieldName = fn;
   }

   public String getFieldName() {
      return this.fieldName;
   }

   public Method getGetterMethod() {
      return this.getterMethod;
   }

   public Method getSetterMethod() {
      return this.setterMethod;
   }

   public Class getFieldType() {
      return this.fieldType;
   }

   public void setFieldType(Class c) {
      this.fieldType = c;
   }

   public void setDeclaringClass(Class declaringClass) {
      this.declaringClass = declaringClass;
   }

   public Class getDeclaringClass() {
      return this.declaringClass;
   }

   public void setFieldValue(Object ownerObject, Object[] newValue) throws IllegalAccessException, InvocationTargetException {
      this.setterMethod.invoke(ownerObject, newValue);
   }

   public Object getFieldValue(Object ownerObject) throws IllegalAccessException, InvocationTargetException {
      return this.getterMethod.invoke(ownerObject, null);
   }
}
