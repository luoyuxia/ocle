package ro.ubbcluj.lci.xmi.config;

import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import ro.ubbcluj.lci.xmi.structure.ClassDescriptor;
import ro.ubbcluj.lci.xmi.structure.FieldDescriptor;

public class AccessorsClassIntrospector implements ClassIntrospector {
   private String fullyQualifiedClassName;
   private Class introspectedClass;
   private ClassDescriptor classDescriptor;
   static boolean debug = false;

   public AccessorsClassIntrospector() {
      this.fullyQualifiedClassName = new String();
   }

   public AccessorsClassIntrospector(String fccn) throws ClassNotFoundException {
      if (fccn != null) {
         this.introspectedClass = Class.forName(fccn);
         this.fullyQualifiedClassName = fccn;
      }

   }

   public void setFullyQualifiedClassName(String fccn) throws ClassNotFoundException {
      if (fccn != null && !this.fullyQualifiedClassName.equals(fccn)) {
         this.fullyQualifiedClassName = null;
         this.classDescriptor = null;
         this.introspectedClass = Class.forName(fccn);
         this.fullyQualifiedClassName = fccn;
      }

   }

   public ClassDescriptor introspect(String fccn) throws ClassNotFoundException {
      this.setFullyQualifiedClassName(fccn);
      return this.introspect();
   }

   public ClassDescriptor introspect() {
      if (this.introspectedClass == null) {
         return null;
      } else if (this.classDescriptor != null) {
         return this.classDescriptor;
      } else {
         this.classDescriptor = new ClassDescriptor();
         this.classDescriptor.setDescriptedClass(this.introspectedClass);
         Method[] methods = this.introspectedClass.getMethods();
         StringBuffer fieldName = new StringBuffer();
         HashMap hmSetters = new HashMap();
         HashMap hmGetters = new HashMap();

         for(int i = 0; i < methods.length; ++i) {
            String methodName = methods[i].getName();
            boolean isSetMethod = false;
            fieldName.setLength(0);
            if (methodName.startsWith("is")) {
               isSetMethod = false;
               fieldName.append(methodName.substring(2));
            } else if (methodName.startsWith("set")) {
               isSetMethod = true;
               fieldName.append(methodName.substring(3));
            } else if (methodName.startsWith("add")) {
               isSetMethod = true;
               fieldName.append(methodName.substring(3));
            } else if (methodName.startsWith("get") && methodName.endsWith("List")) {
               isSetMethod = false;
               fieldName.append(methodName.substring(3, methodName.length() - 4));
            } else if (methodName.startsWith("get")) {
               isSetMethod = false;
               fieldName.append(methodName.substring(3));
            }

            if (fieldName.length() > 0) {
               fieldName.setCharAt(0, Character.toLowerCase(fieldName.charAt(0)));
               Method theOther;
               String fn;
               FieldDescriptor fd;
               if (isSetMethod) {
                  fn = fieldName.toString();
                  theOther = (Method)hmGetters.get(fn);
                  if (theOther != null) {
                     if (theOther.getName().startsWith("is")) {
                        fn = "is" + Character.toUpperCase(fn.charAt(0)) + fn.substring(1);
                     }

                     fd = new FieldDescriptor(fn, theOther, methods[i], theOther.getDeclaringClass());
                     this.classDescriptor.addField(fd);
                  } else {
                     hmSetters.put(fn, methods[i]);
                  }
               } else {
                  fn = fieldName.toString();
                  theOther = (Method)hmSetters.get(fn);
                  if (theOther != null) {
                     if (methods[i].getName().startsWith("is")) {
                        fn = "is" + Character.toUpperCase(fn.charAt(0)) + fn.substring(1);
                     }

                     fd = new FieldDescriptor(fn, methods[i], theOther, theOther.getDeclaringClass());
                     this.classDescriptor.addField(fd);
                  } else {
                     hmGetters.put(fn, methods[i]);
                  }
               }
            }
         }

         if (debug) {
            String cname = this.classDescriptor.getDescriptedClass().getName();
            if (cname.endsWith("Model")) {
               System.out.println("\n\nClass: " + cname);
               Enumeration en = this.classDescriptor.getFieldList();

               while(en.hasMoreElements()) {
                  FieldDescriptor fd = (FieldDescriptor)en.nextElement();
                  System.out.println("Field: " + fd.getFieldName());
                  System.out.println("  >> Type: " + fd.getFieldType().getName());
                  System.out.println("  >> Getter: " + fd.getGetterMethod().getName());
                  System.out.println("  >> Setter: " + fd.getSetterMethod().getName());
               }
            }
         }

         return this.classDescriptor;
      }
   }

   public static void main(String[] args) {
      try {
         ClassIntrospector ci = new AccessorsClassIntrospector("ro.ubbcluj.lci.gxapi.GXClassDiagram");
         debug = true;
         ci.introspect();
      } catch (Exception var2) {
         System.out.println(var2.getMessage());
         var2.printStackTrace();
      }

   }
}
